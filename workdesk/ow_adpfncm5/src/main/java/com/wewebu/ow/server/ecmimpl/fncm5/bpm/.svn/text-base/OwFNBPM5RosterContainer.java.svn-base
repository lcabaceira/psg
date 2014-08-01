package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;

import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterDefinition;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWWorkObject;

/**
 *<p>
 * FileNet BPM Repository. OwObject implementation for roster queues.<br/>
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
public class OwFNBPM5RosterContainer extends OwFNBPM5NativeContainer
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5RosterContainer.class);

    private VWRoster m_roster;

    public OwFNBPM5RosterContainer(OwFNBPM5Repository repository_p, VWRoster roster_p) throws Exception
    {
        super(repository_p);
        m_roster = roster_p;

        // === load fielddefinitions in advance
        VWRosterDefinition definition = m_roster.fetchRosterDefinition();

        initFields(definition.getFields());
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined by {@link OwStatusContextDefinitions}
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return m_roster.fetchCount();
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/roster";
    }

    /** get Object name property string
     * @return the name property string of the object
     */
    public String getName()
    {
        try
        {
            return getContext().localizeLabel(m_roster.toString());
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
        return m_roster.toString();
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
        for (int iType = 0; iType < iObjectTypes_p.length; iType++)
        {
            switch (iObjectTypes_p[iType])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_ROSTERITEM:
                {
                    OwObjectCollection workitems = getRepository().createObjectCollection(); // new OwStandardObjectCollection();

                    // === build query
                    // create the filter parameters for the BPM query
                    OwFNBPMQueryInfo queryinfo = createFilterQueryParameter(filterCriteria_p);

                    // treat F_CreateTime from queues as F_StartTime, so we can reuse search templates
                    if (null != queryinfo.m_strFiler)
                    {
                        queryinfo.m_strFiler = OwString.replaceAll(queryinfo.m_strFiler, "F_CreateTime", "F_StartTime");
                    }

                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwFNBPMRosterContainer.getChildsInternal: " + queryinfo.m_strFiler);
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

                    VWRosterQuery query = m_roster.createQuery(queryinfo.m_index, queryinfo.m_minValues, queryinfo.m_maxValues, iOptions, queryinfo.m_strFiler, queryinfo.m_substitionvars, VWFetchType.FETCH_TYPE_WORKOBJECT);

                    // set the size
                    query.setBufferSize(iMaxSize_p);

                    // === iterate over results and build Wrapper classes
                    while (query.hasNext() && (workitems.size() < iMaxSize_p))
                    {
                        VWWorkObject workitem = (VWWorkObject) query.next();
                        workitems.add(new OwFNBPM5RosterWorkItem(this, workitem));
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

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER;
    }

    public Object getNativeObject() throws Exception
    {
        return m_roster;
    }
}