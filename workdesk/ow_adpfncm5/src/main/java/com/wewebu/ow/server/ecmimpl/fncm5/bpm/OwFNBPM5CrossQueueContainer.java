package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5CrossQueueContainer.OwFNBPMSearchSpecialSQLOperator.OwQueueInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * FileNet BPM Repository. Cross view container that combines other containers.
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
 */
public class OwFNBPM5CrossQueueContainer extends OwFNBPM5BaseContainer
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5CrossQueueContainer.class);

    /** the search template view definition */
    protected OwFNBPM5SearchTemplate m_searchtemplate;

    public OwFNBPM5CrossQueueContainer(OwFNBPM5Repository repository_p, OwFNBPM5SearchTemplate searchtemplate_p) throws Exception
    {
        super(repository_p);

        m_searchtemplate = searchtemplate_p;
    }

    /** set a filter to filter specific items in getChilds in addition to the getChilds OwSearchNode parameter
     * @param iFilterType_p  int filter type as defined in FILTER_TYPE_...
     */
    public void setFilterType(int iFilterType_p)
    {
        super.setFilterType(iFilterType_p);

        try
        {
            for (OwWorkitemContainer cont : getChildContainers())
            {
                cont.setFilterType(iFilterType_p);
            }
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            LOG.error("Could not set filter type.", e);
        }
    }

    /** check if item is in a user box */
    protected boolean isUserContainer()
    {
        return false;
    }

    /** get a collection of OwFieldDefinition's for a given list of names
     * 
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return Collection of OwFieldDefinition's that can actually be filtered, may be a subset of propertynames_p, or null if no filter properties are allowed
     * @throws Exception
     */
    public java.util.Collection getFilterProperties(java.util.Collection propertynames_p) throws Exception
    {
        Collection retFilterproperties = null;

        for (OwWorkitemContainer cont : getChildContainers())
        {

            Collection filterproperties = cont.getFilterProperties(propertynames_p);

            if (retFilterproperties == null)
            {
                // === first container add to map for fast lookup
                retFilterproperties = new ArrayList();

                retFilterproperties.addAll(filterproperties);
            }
            else
            {
                // === all others are merged
                Set lookupset = new HashSet();

                Iterator itProp = filterproperties.iterator();
                while (itProp.hasNext())
                {
                    OwFieldDefinition filterfield = (OwFieldDefinition) itProp.next();
                    lookupset.add(filterfield.getClassName());
                }

                Iterator itFProp = retFilterproperties.iterator();
                while (itFProp.hasNext())
                {
                    OwFieldDefinition prop = (OwFieldDefinition) itFProp.next();
                    if (!lookupset.contains(prop.getClassName()))
                    {
                        itFProp.remove();
                    }
                }
            }
        }

        return retFilterproperties;
    }

    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        // init the search template
        if (!m_searchtemplate.isInitalized())
        {
            m_searchtemplate.init(getRepository());
        }

        return m_searchtemplate;
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined by {@link OwStatusContextDefinitions}
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        throw new OwStatusContextException("");
    }

    /** cached flag check if resubmission is supported by the queue */
    private Boolean m_canResubmitFlag = null;

    /** check if resubmission is supported by the queue
     */
    public boolean canResubmit() throws Exception
    {
        if (null == m_canResubmitFlag)
        {
            m_canResubmitFlag = Boolean.TRUE;

            for (OwWorkitemContainer cont : getChildContainers())
            {
                if (!cont.canResubmit())
                {
                    m_canResubmitFlag = Boolean.FALSE;
                    break;
                }
            }
        }

        return m_canResubmitFlag.booleanValue();
    }

    /**
     *<p>
     * Overwritten SQL operator for FileNet BPM path scan, each path represents a queue to search in.
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
     */
    public static class OwFNBPMSearchSpecialSQLOperator
    {
        /**
         *<p>
         * A single queue info.
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
         */
        public static class OwQueueInfo
        {
            public OwQueueInfo(int iQueueType_p, String sQueuName_p)
            {
                m_iQueueType = iQueueType_p;
                m_sQueuName = sQueuName_p;
            }

            private int m_iQueueType;
            private String m_sQueuName;

            public int getQueueType()
            {
                return m_iQueueType;
            }

            public String getQueueName()
            {
                return m_sQueuName;
            }
        }

        /** traverse the search criteria tree and generate a SQL Statement
         */
        public void scan(OwSearchNode searchNode_p) throws Exception
        {
            if (!searchNode_p.isCriteriaNode())
            {
                Iterator itSearch = searchNode_p.getChilds().iterator();
                while (itSearch.hasNext())
                {
                    scan((OwSearchNode) itSearch.next());
                }
            }
            else
            {
                OwSearchCriteria criteria = searchNode_p.getCriteria();
                OwFieldDefinition fieldDefinition = criteria.getFieldDefinition();

                if (OwSearchPathField.CLASS_NAME.equals(fieldDefinition.getClassName()))
                {
                    // === special treatment for resources/search paths 

                    OwSearchPath path = (OwSearchPath) criteria.getValue();

                    if (null != path)
                    {
                        String pathName = path.getPathName();
                        StringTokenizer pathTokenizer = new StringTokenizer(pathName, OwSearchPath.PATH_DELIMITER);

                        String queuetype = pathTokenizer.nextToken();
                        String queuename = pathTokenizer.nextToken();
                        m_subpathes.add(new OwQueueInfo(OwWorkitemRepository.m_containerprefixmap.getContainerType(queuetype), queuename));
                    }
                }
            }
        }

        private List<OwQueueInfo> m_subpathes = new ArrayList<OwQueueInfo>();

        /** List of subpathes 
         * @return List
         */
        public List<OwQueueInfo> getQueueInfos()
        {
            return m_subpathes;
        }
    };

    /** a collection of child containers to virutally search in */
    private Collection<OwWorkitemContainer> m_childcontainers;

    /** 
     * get a collection of child containers to virutally search in 
     * @throws Exception 
     */
    protected Collection<OwWorkitemContainer> getChildContainers() throws Exception
    {
        if (m_childcontainers == null)
        {
            m_childcontainers = new ArrayList<OwWorkitemContainer>();

            // find the queues, which are selected with the path criteria
            OwSearchNode searchcriteria = getSearchTemplate().getSearch(false);
            OwSearchNode special = searchcriteria.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);
            OwFNBPMSearchSpecialSQLOperator sqlSpecialOperator = new OwFNBPMSearchSpecialSQLOperator();
            sqlSpecialOperator.scan(special);
            for (OwQueueInfo queueinfo : sqlSpecialOperator.getQueueInfos())
            {
                // find workitemcontainer
                m_childcontainers.add(getRepository().getWorkitemContainer(queueinfo.getQueueName(), queueinfo.getQueueType()));
            }
        }

        return m_childcontainers;
    }

    /** check if container supports work item pull, see pull
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * 
     * @return boolean 
      * @throws Exception
     */
    public boolean canPull(int iContext_p) throws Exception
    {
        for (OwWorkitemContainer cont : getChildContainers())
        {
            if (cont.canPull(iContext_p))
            {
                return true;
            }
        }

        return false;
    }

    /** pulls the next available work item out of the container and locks it for the user
     * 
     * @param sort_p OwSort optional sorts the items and takes the first available one, can be null
     * @param exclude_p Set of work item DMSIDs to be excluded, i.e. that may have already been pulled by the user
     * @return OwWorkitem or OwObjectNotFoundException if no object is available or OwServerException if no object could be pulled within timeout
     * @throws OwException for general error, or OwServerException if timed out or OwObjectNotFoundException if no work item is available
     */
    public OwWorkitem pull(OwSort sort_p, Set exclude_p) throws OwException
    {
        if (null != exclude_p)
        {
            throw new OwNotSupportedException("OwFNBPMCrossQueueContainer.pull: Parameter exclude_p is not supported, must be null.");
        }

        // get a locked object from the queue
        OwObjectCollection lockobjects;
        try
        {
            lockobjects = getChildsInternal(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, sort_p, 1, 0, null, true);
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not get children.", e);
        }

        if (lockobjects.size() != 0)
        {
            return (OwWorkitem) lockobjects.get(0);
        }
        else
        {
            String msg = "OwFNBPMCrossQueueContainer.puu: There is no work item to edit.";
            LOG.debug(msg);
            throw new OwObjectNotFoundException(getContext().localize("plug.owbpm.OwBPMPullFunction.pullfailed", "There is no work item to edit."));
        }
    }

    /** get the children of the object, does NOT cache the returned object
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of subfolders
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     * @param fLock_p lock the returned objects
     *
     * @return list of child objects, or null
     */
    protected OwObjectCollection getChildsInternal(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p, boolean fLock_p) throws Exception
    {
        // === perform federated search over selected queues
        // find the queues, which are selected with the path criteria
        OwSearchNode searchcriteria = getSearchTemplate().getSearch(false);

        // scan property node
        OwSearchNode searchpropertynode = searchcriteria.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY);

        OwSearchNode mergenode = searchpropertynode;

        // Merge search with filter criteria
        if (filterCriteria_p != null)
        {
            OwSearchNode filterpropertynode = filterCriteria_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY);

            // merge togther in one mergenode
            mergenode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);
            mergenode.add(searchpropertynode);
            mergenode.add(filterpropertynode);
        }

        // iterate over the queues query and collect children
        OwStandardObjectCollection retList = getRepository().createObjectCollection(); // new OwStandardObjectCollection(); 
        OwSort nosort = new OwSort();

        int iMaxSize = iMaxSize_p;

        for (OwWorkitemContainer childCont : getChildContainers())
        {
            OwFNBPM5BaseContainer cont = (OwFNBPM5BaseContainer) childCont;
            // search in container and add to result
            OwObjectCollection thisResult = cont.getChildsInternal(iObjectTypes_p, propertyNames_p, nosort, iMaxSize, iVersionSelection_p, mergenode, fLock_p);
            if (null != thisResult)
            {
                retList.addAll(thisResult);

                iMaxSize -= thisResult.size();

                if (iMaxSize <= 0)
                {
                    break;
                }
            }
        }

        // sort all elements
        if (null != sort_p)
        {
            retList.sort(sort_p);
        }

        return retList;
    }

    /** get the children of the object, does NOT cache the returned object
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of subfolders
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return getChildsInternal(iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p, false);
    }

    public String getName()
    {
        return m_searchtemplate.getDisplayName(getContext().getLocale());
    }

    public String getID()
    {
        return m_searchtemplate.getName();
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER;
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/cross";
    }

    /** get the possible reassign container names used in reassignToPublicContainer
     * 
     * @return Collection of names or null if not defined
     * @throws Exception
     */
    public Collection getPublicReassignContainerNames() throws Exception
    {
        Collection ret = ((OwFNBPM5SearchTemplate) getSearchTemplate()).getReassignContainerNames();
        if (null == ret)
        {
            ret = getRepository().getReassignContainerNames();
        }

        return ret;
    }

    /** get a display name for a reassign container name */
    public String getPublicReassignContainerDisplayName(String sName_p)
    {
        try
        {
            return getRepository().getWorkitemContainerName(sName_p, OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER);
        }
        catch (Exception e)
        {
            return sName_p;
        }
    }

}
