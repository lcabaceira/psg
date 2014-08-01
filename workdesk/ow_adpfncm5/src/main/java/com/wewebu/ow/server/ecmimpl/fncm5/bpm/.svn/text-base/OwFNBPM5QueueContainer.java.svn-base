package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

import filenet.vw.api.VWException;
import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueDefinition;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWQueueQuery;

/**
 *<p>
 * FileNet BPM Plugin. OwObject implementation for queues.<br/>
 * A single workitem.
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
public abstract class OwFNBPM5QueueContainer extends OwFNBPM5NativeContainer
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5QueueContainer.class);

    /** the queue */
    private VWQueue m_queue;

    public OwFNBPM5QueueContainer(OwFNBPM5Repository repository_p, VWQueue queue_p) throws Exception
    {
        super(repository_p);
        m_queue = queue_p;

        // === load field definitions in advance
        VWQueueDefinition definition = m_queue.fetchQueueDefinition();

        initFields(definition.getFields());
    }

    public VWQueue getNativeObject() throws OwException
    {
        return m_queue;
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined by {@link OwStatusContextDefinitions}
    * @return <code>int</code> number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return m_queue.fetchCount();
    }

    /** get Object name property string
     * @return the name property string of the object
     */
    public String getName()
    {
        try
        {
            return getContext().localizeLabel(m_queue.toString());
        }
        catch (Exception e)
        {
            return "unknown";
        }
    }

    /** get Object symbolic name of the object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the object which is unique among its siblings
     */
    public String getID()
    {
        return m_queue.toString();
    }

    /** check if container supports work item pull, see pull
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * 
     * @return boolean 
      * @throws Exception
     */
    public boolean canPull(int iContext_p) throws Exception, OwStatusContextException
    {
        return true;
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
        OwObjectCollection lockobjects = null;
        try
        {
            // get a locked object from the queue
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
            String msg = "OwFNBPMQueueContainer.pull: There is no work item to edit.";
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
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return getChildsInternal(iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p, false);
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
        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            switch (iObjectTypes_p[i])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_WORKITEM:
                {
                    OwObjectCollection workitems = getRepository().createObjectCollection();

                    // === build query
                    // create the filter parameters for the BPM query
                    OwFNBPMQueryInfo queryinfo = createFilterQueryParameter(filterCriteria_p);

                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwFNBPMQueueContainer.getChildsInternal: Queryinfo-Filer:" + queryinfo.m_strFiler);
                    }

                    // === perform query
                    int iOptions = VWQueue.QUERY_NO_OPTIONS;

                    if (fLock_p)
                    {
                        iOptions = VWQueue.QUERY_LOCK_OBJECTS;
                    }
                    else
                    {
                        iOptions = VWQueue.QUERY_READ_LOCKED;
                    }

                    VWQueueQuery query = createNativeQueryObject(queryinfo, iOptions);
                    // set the size
                    query.setBufferSize(iMaxSize_p);

                    // === iterate over results and build Wrapper classes
                    try
                    {
                        while (query.hasNext() && (workitems.size() < iMaxSize_p))
                        {
                            VWQueueElement workitem = (VWQueueElement) query.next();
                            workitems.add(createWorkItem(workitem));
                        }
                    }
                    catch (VWException ex)
                    {
                        throw new OwServerException("Unable to create native query object", ex);
                    }

                    // === sort result
                    if (null != sort_p)
                    {
                        workitems.sort(sort_p);
                    }

                    return workitems;
                }
            }
        }

        return null;
    }

    /**
     * create VWQueueQuery object, from provided parameters 
     * @param queryInfo OwFNBPMQueryInfo
     * @param queryOptions integer one of VWQueue.QUERY_xxx options
     * @return VWQueueQuery
     * @throws OwException if unable to create native VWQueueQuery object
     * @since 4.1.2.0
     */
    protected VWQueueQuery createNativeQueryObject(OwFNBPMQueryInfo queryInfo, int queryOptions) throws OwException
    {
        try
        {
            return getNativeObject().createQuery(queryInfo.m_index, queryInfo.m_minValues, queryInfo.m_maxValues, queryOptions, queryInfo.m_strFiler, queryInfo.m_substitionvars, VWFetchType.FETCH_TYPE_QUEUE_ELEMENT);
        }
        catch (VWException ex)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Failed to create native query object! Params: " + queryInfo + " options = " + queryOptions, ex);
            }
            throw new OwServerException("Unable to create native query object", ex);
        }
    }

    /** (overridable)Factory method to create a new OwFNBPM5WorkItem from native VWQueueElement
     * @param workitem_p VWQueueElement
     */
    protected OwFNBPM5WorkItem createWorkItem(VWQueueElement workitem_p) throws Exception
    {
        return new OwFNBPM5QueueWorkItem(this, workitem_p);
    }
}