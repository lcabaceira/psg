package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwCollectionIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.OwCredentialsConstants;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.ContainerChildrenPageFetcher;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.ContainerChildrenPageFetcher.ResourceProvider;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.util.ClientSideFilter;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.util.NoClientSideFilter;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.util.ResubmissionClientSideFilter;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.util.StandardBPMClientSideFilter;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Alfresco BPM based implementation of {@link OwWorkitemContainer}.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 4.0.0.0
 */
public abstract class OwAlfrescoBPMWorkitemContainer extends OwAlfrescoBPMBaseContainer
{
    private static final Logger LOG = OwLog.getLogger(OwAlfrescoBPMWorkitemContainer.class);

    protected String id;

    public OwAlfrescoBPMWorkitemContainer(OwNetwork network, OwAlfrescoBPMRepository bpmRepository, String sID_p)
    {
        super(network, bpmRepository);
        this.bpmRepository = bpmRepository;
        this.id = sID_p;
    }

    public String createContaineeMIMEType(OwAlfrescoBPMWorkItem item) throws Exception
    {
        OwProperty property = item.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_POOLED);
        if (property != null && ((Boolean) property.getValue()) == true)
        {
            return "ow_workitem/poolitem";
        }
        return "ow_workitem/item";
    }

    public String createContaineeMIMEParameter(OwAlfrescoBPMWorkItem item)
    {
        return "";
    }

    protected OwNetworkContext getBPMRepositoryContext()
    {
        return this.bpmRepository.getContext();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getName()
     */
    public String getName()
    {
        return getBpmRepository().getContext().localizeLabel(this.id);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getID()
     */
    public String getID()
    {
        return this.id;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer#pull(com.wewebu.ow.server.field.OwSort, java.util.Set)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public OwWorkitem pull(OwSort sort_p, Set exclude_p) throws Exception, OwObjectNotFoundException, OwServerException
    {
        OwLoadContext ctx = new OwLoadContext();
        OwIterable<OwObject> page = getChildren(ctx);
        OwObjectCollection children = processClientSideFiltering(page, getClientSideFilter(getFilterType()), 1);
        if (children.isEmpty())
        {
            String message = getNetwork().getContext().localize("plug.owbpm.OwBPMPullFunction.pullfailed", "There is no work item to edit.");
            LOG.error(message);
            throw new OwObjectNotFoundException(message);
        }
        return (OwWorkitem) children.get(0);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer#canPull(int)
     */
    @Override
    public boolean canPull(int iContext_p) throws Exception, OwStatusContextException
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer#getFilterProperties(java.util.Collection)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Collection getFilterProperties(Collection columnsInfo_p) throws Exception
    {
        Collection<OwFieldDefinition> filterProperties = new ArrayList<OwFieldDefinition>();
        // bpm:dueDate
        for (Object objColumnInfo : columnsInfo_p)
        {
            OwFieldColumnInfo columnInfo = (OwFieldColumnInfo) objColumnInfo;
            String propertyName = columnInfo.getPropertyName();
            if (propertyName.endsWith("bpm:dueDate"))
            {
                OwFieldDefinition fieldDefinition = this.network.getFieldDefinition(propertyName, null);
                filterProperties.add(fieldDefinition);
            }
        }
        return filterProperties;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer#canResubmit()
     */
    public boolean canResubmit() throws Exception
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getChilds(int[], java.util.Collection, com.wewebu.ow.server.field.OwSort, int, int, com.wewebu.ow.server.field.OwSearchNode)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        //TODO: the implementation must adhere to the sort_p, iMaxSize_p, iVersionSelection_p etc. parameters

        //This type of Queue only recognizes WORKITEMs
        OwObjectCollection result = new OwStandardObjectCollection();

        for (int type : iObjectTypes_p)
        {
            if (type == OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS || type == OwObjectReference.OBJECT_TYPE_WORKITEM)
            {

                OwIterable<OwObject> children = getChildrenInternal(filterCriteria_p, iMaxSize_p);
                result = processClientSideFiltering(children, getClientSideFilter(getFilterType()), iMaxSize_p);
            }
        }

        if (null != sort_p)
        {
            result.sort(sort_p);
        }

        return result;
    }

    /**
     * Process the provided paging instance to return a collection of defined maximum size.
     * Additionally the returned values can be filtered based on provided instance. 
     * @param paging OwIterable 
     * @param filter CliendSideFilter
     * @param maxSize integer maximum amount of entries in returned collection
     * @return OwObjectCollection 
     * @throws Exception
     */
    protected OwObjectCollection processClientSideFiltering(OwIterable<OwObject> paging, ClientSideFilter filter, int maxSize) throws Exception
    {
        OwStandardObjectCollection result = new OwStandardObjectCollection();
        for (OwObject object : paging)
        {
            if (result.size() <= maxSize && filter.match(object))
            {
                result.add(object);
            }
            if (result.size() == maxSize)
            {
                break;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwPageableObject#getChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwObject> getChildren(OwLoadContext loadContext) throws OwException
    {
        return this.getChildrenInternal(loadContext);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#getChildCount(int[], int)
     */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        //        Set<Integer> types = new HashSet<Integer>();
        //        for (int i = 0; i < iObjectTypes_p.length; i++)
        //        {
        //            types.add(iObjectTypes_p[i]);
        //        }
        //
        //        //This type of Queue only recognizes WORKITEMs
        //        int count = 0;
        //        for (Integer type : types)
        //        {
        //            switch (type)
        //            {
        //                case OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS:
        //                case OwObjectReference.OBJECT_TYPE_WORKITEM:
        //                    count += getChildrenInternal(null, Integer.MAX_VALUE).size();
        //            }
        //        }
        //        return count;
        return (int) getChildrenCountInternal();
    }

    private long getChildrenCountInternal() throws Exception
    {
        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setPageSize(5);
        return getChildrenInternal(loadContext).getTotalNumItems();
    }

    protected OwIterable<OwObject> getChildrenInternal(final OwLoadContext loadContext) throws OwException
    {
        final String user;
        try
        {
            user = this.network.getCredentials().getAuthInfo(OwCredentialsConstants.LOGIN_USR);
        }
        catch (Exception e)
        {
            throw new OwAlfrescoBPMException("Could not get current user from network.", e);
        }

        ResourceProvider resourceProvider = getTasksInstancesResourceProvider(loadContext, user);
        return new OwCollectionIterable<OwObject>(new ContainerChildrenPageFetcher(this, loadContext.getPageSize(), resourceProvider));
    }

    protected OwIterable<OwObject> getChildrenInternal(OwSearchNode filterCriteria, int iMaxSize) throws Exception
    {
        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setFilter(filterCriteria);

        OwIterable<OwObject> children = getChildrenInternal(loadContext);
        OwIterable<OwObject> result = children.skipTo(0).getPage(iMaxSize);
        return result;
    }

    /**
     * Create a filter which is used for client side filtering.
     * @param fType type of the filter
     * @return ClientSideFilter
     */
    protected ClientSideFilter getClientSideFilter(int fType)
    {
        switch (fType)
        {
            case FILTER_TYPE_NORMAL:
                return new StandardBPMClientSideFilter(new Date());
            case FILTER_TYPE_RESUBMISSION:
                return new ResubmissionClientSideFilter(new Date());
            default:
                return NoClientSideFilter.INSTANCE;
        }
    }

    /**
     * Create the proper {@link ResourceProvider} for the specific type of container.
     * @param loadContext
     * @param user
     * @return a properly configured instance of {@link ResourceProvider} to be used during page fetching.
     */
    protected abstract ResourceProvider getTasksInstancesResourceProvider(OwLoadContext loadContext, String user);

}
