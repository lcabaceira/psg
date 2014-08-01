package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.filenet.api.constants.PropertyNames;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwQueriedCollection;
import com.wewebu.ow.server.ecm.OwRepositoryContext;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardWildCardDefinition;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.history.OwStandardHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * FNCM History manager, retrieves the audit log from FileNet CE. <br/>
 * Event functions are ignored, since FileNet records history internally.
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
public class OwFNCM5HistoryManager extends OwStandardHistoryManager
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNCM5HistoryManager.class);

    private static final OwObjectCollection EMPTY_COLLECTION = new OwStandardObjectCollection();

    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "fchi";

    private static final String DEFAULT_BPM_OBJECTSTORE = "OW_BPM";

    /** history manager member variable. for internal use only */
    private OwHistoryManager m_bpmHistoryManager = null;

    private String m_bpmObjectstore = DEFAULT_BPM_OBJECTSTORE;

    /** add a new history event to the history database if supported by the history manager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param event_p OwHistoryEvent according to iEventType_p, contains additional information, such as the affected Objects or properties
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public void addEvent(int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws Exception
    {
        List affectedDMSIDs = getDMSIdsFromEvent(event_p);
        if (affectedDMSIDs.size() > 0)
        {
            String firstDMSID = (String) affectedDMSIDs.get(0);
            if (firstDMSID.startsWith(OwFNCM5Network.DMS_PREFIX))
            {
                super.addEvent(iEventType_p, strEventID_p, event_p, iStatus_p);
            }
            else
            {
                if (getFNBPMHistoryManager() != null)
                {
                    getFNBPMHistoryManager().addEvent(iEventType_p, strEventID_p, event_p, iStatus_p);
                }
            }
        }
        else
        {
            super.addEvent(iEventType_p, strEventID_p, event_p, iStatus_p);
        }
    }

    /** add a new history event to the history database if supported by the history manager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public void addEvent(int iEventType_p, String strEventID_p, int iStatus_p) throws Exception
    {
        // Implement if plugin events need to be audited
    }

    /** search for entries in the database
     *
     * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
     * @param sortCriteria_p OwSort to apply
     * @param propertyNames_p Collection of properties to retrieve with the history entries
     * @param iMaxSize_p max size of entries to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     *
     * @return Collection of OwHistoryEntry
     */
    public OwObjectCollection doSearch(OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        // delegate to internal doSearch with empty additionalWhereClause
        return doSearchInternal(filterCriteria_p, sortCriteria_p, propertyNames_p, iMaxSize_p, iVersionSelection_p, null);
    }

    /** search for entries in the database
     *
     * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
     * @param sortCriteria_p OwSort to apply
     * @param propertyNames_p Collection of properties to retrieve with the history entries
     * @param iMaxSize_p max size of entries to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     * @param additionalWhereClause_p String additional clause for WHERE argument. may be null
     *
     * @return OwObjectCollection of OwHistoryEntry
     */
    protected OwObjectCollection doSearchInternal(OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p, String additionalWhereClause_p) throws Exception
    {
        OwFNCM5HistoryQuery query = new OwFNCM5HistoryQuery(this, filterCriteria_p, propertyNames_p, iMaxSize_p, iVersionSelection_p, additionalWhereClause_p, m_bpmObjectstore);
        return new OwQueriedCollection(query, sortCriteria_p);
    }

    /** search for entries in the database for a specific ECM object
     *
     * @param object_p OwObject to find entries for
     * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
     * @param sortCriteria_p OwSort to apply
     * @param propertyNames_p Collection of properties to retrieve with the history entries
     * @param includeSubObjectTypes_p array of child OwObject types to be included in history, or null
     * @param iMaxSize_p max size of entries to retrieve
     *
     * @return Collection of OwHistoryEntry
     */
    public OwObjectCollection doObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception
    {
        if (object_p.getDMSID().startsWith(OwFNCM5Network.DMS_PREFIX))
        {
            //TODO: RTTID
            if (object_p instanceof com.wewebu.ow.server.ecm.OwVirtualFolderObject)
            {//return empty collection because VirtualFolder don't have a history see bug 1765
                OwObjectCollection ret = super.doObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
                if (null != ret)
                {
                    return ret;
                }
                return EMPTY_COLLECTION;
            }
            else
            {
                //TODO: RTTID
                OwFNCM5Object<?> fncmObject = (OwFNCM5Object<?>) object_p;
                return doFNCMObjectSearch(fncmObject, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
            }
        }
        else
        {
            return doFNBPMObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
        }
    }

    /** search for entries in the database for a specific ECM object
     *
     * @param object_p OwObjectReference to find entries for
     * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
     * @param sortCriteria_p OwSort to apply
     * @param propertyNames_p Collection of properties to retrieve with the history entries
     * @param includeSubObjectTypes_p array of child OwObject types to be included in history, or null
     * @param iMaxSize_p max size of entries to retrieve
     *
     * @return Collection of OwHistoryEntry
     */
    private OwObjectCollection doFNBPMObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception
    {
        OwObjectCollection result = null;

        OwHistoryManager bpmHistoryManager = getFNBPMHistoryManager();

        if (null != bpmHistoryManager)
        {
            result = bpmHistoryManager.doObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
        }

        return result;
    }

    /**
     * Return the BPM history manager if a configuration exist,
     * otherwise a <code>null</code> is returned.
     * <p>HINT: This method is also used for lazy initialization
     * of the  BPM history manager</p>
     * 
     * @return OwHistoryManager or null if not configured
     * @throws Exception if configuration of of BPMHistoryManager is not correct.
     */
    public OwHistoryManager getFNBPMHistoryManager() throws Exception
    {
        if (null == m_bpmHistoryManager)
        {
            Node bpmHistoryManagerNode = this.getConfigNode().getSubNode("BPMHistoryManager");

            if (null != bpmHistoryManagerNode)
            {
                OwXMLUtil bpmconfig = new OwStandardXMLUtil(bpmHistoryManagerNode);
                String sHistoryManagerClassName = bpmconfig.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_CLASSNAME, null);

                Class historyManagerClass = Class.forName(sHistoryManagerClassName);
                m_bpmHistoryManager = (OwHistoryManager) historyManagerClass.newInstance();
                m_bpmHistoryManager.init(getContext(), bpmconfig);

                m_bpmHistoryManager.setNetwork(getNetwork());
            }
        }

        return m_bpmHistoryManager;
    }

    /**(overridable)
     * 
     * Add object mapping values to the search node.
     *  
     * @param object_p the objects whose history events are searched  
     * @param filterCriteria_p filter criteria search node  
     * @return a history search criteria with all object related values set 
     * @throws OwException
     */
    protected OwSearchNode addObjectProperties(OwFNCM5Object<?> object_p, OwSearchNode filterCriteria_p) throws OwException
    {
        Map searchCriteriaMap = filterCriteria_p.getCriteriaMap(OwSearchNode.FILTER_NONE);
        try
        {
            if (searchCriteriaMap.containsKey(PropertyNames.VERSION_SERIES_ID))
            {
                OwSearchCriteria versionSeriesIdCriteria = (OwSearchCriteria) searchCriteriaMap.get(PropertyNames.VERSION_SERIES_ID);
                if (versionSeriesIdCriteria != null && versionSeriesIdCriteria.isHidden() && versionSeriesIdCriteria.getValue() == null)
                {
                    try
                    {
                        OwProperty versionSeriesProperty = object_p.getProperty(PropertyNames.VERSION_SERIES);
                        OwObject versionSeries = (OwObject) versionSeriesProperty.getValue();
                        OwProperty versionSeriesIdProperty = versionSeries.getProperty(PropertyNames.ID);

                        Object versionSeriesValue = versionSeriesIdProperty.getValue();

                        versionSeriesValue = versionSeriesIdProperty.getValue();
                        String versionSeriesId = versionSeriesValue.toString();

                        versionSeriesIdCriteria.setValue(versionSeriesId);
                    }
                    catch (OwObjectNotFoundException e)
                    {
                        LOG.debug("Culd not retrieve " + PropertyNames.VERSION_SERIES + ". Does DMSID " + object_p.getDMSID() + " point a versionable object ?", e);
                    }

                }
            }

            if (searchCriteriaMap.containsKey(PropertyNames.SOURCE_OBJECT_ID))
            {

                OwSearchCriteria sourceObjectIdCriteria = (OwSearchCriteria) searchCriteriaMap.get(PropertyNames.SOURCE_OBJECT_ID);
                if (sourceObjectIdCriteria != null && sourceObjectIdCriteria.isHidden() && sourceObjectIdCriteria.getValue() == null)
                {
                    OwProperty idProperty = object_p.getProperty(PropertyNames.ID);
                    String idValue = (String) idProperty.getValue();

                    sourceObjectIdCriteria.setValue(idValue);
                }
            }

        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not add object properties to events search statement!", e);
        }

        return filterCriteria_p;
    }

    /** search for entries in the database for a specific ECM object
     *
     * @param object_p OwObjectReference to find entries for
     * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
     * @param sortCriteria_p OwSort to apply
     * @param propertyNames_p Collection of properties to retrieve with the history entries
     * @param includeSubObjectTypes_p array of child OwObject types to be included in history, or null
     * @param iMaxSize_p max size of entries to retrieve
     *
     * @return Collection of OwHistoryEntry
     */
    private OwObjectCollection doFNCMObjectSearch(OwFNCM5Object<?> object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception
    {

        OwObjectCollection ret = super.doObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
        if (null != ret)
        {
            return ret;
        }

        if (null != filterCriteria_p)
        {
            // clone search node 
            OwSearchNode clonedNode = (OwSearchNode) filterCriteria_p.clone();
            addObjectProperties(object_p, clonedNode);

            // add resource node to search
            OwSearchNode specialNode = clonedNode.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

            if (specialNode == null)
            {
                specialNode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_SPECIAL);
                clonedNode.add(specialNode);
            }

            OwSearchObjectStore searchStore = new OwSearchObjectStore(object_p.getResource().getID(), null);
            OwSearchPath searchPath = new OwSearchPath(searchStore);
            OwSearchNode searchPathNode = new OwSearchNode(OwSearchPathField.classDescription, OwSearchOperator.MERGE_UNION, searchPath, OwSearchCriteria.ATTRIBUTE_HIDDEN, "historySearchPath", "historySearchPathInstruction", null);

            specialNode.add(searchPathNode);

            String additionalWhereClause = " ( SourceObjectId = " + object_p.getID() + " ) ";
            // delegate to internal doSearch with empty additionalWhereClause
            return doSearchInternal(clonedNode, sortCriteria_p, propertyNames_p, iMaxSize_p, OwSearchTemplate.VERSION_SELECT_DEFAULT, additionalWhereClause);
        }
        else
        {
            OwStandardObjectCollection eventCollection = new OwStandardObjectCollection();

            // retrieve all audit events from 'audited events' property
            OwProperty auditEventsProperty = object_p.getProperty(PropertyNames.AUDITED_EVENTS);
            OwObject[] events = (OwObject[]) auditEventsProperty.getValue();
            if (events != null)
            {
                for (int i = 0; i < events.length; i++)
                {
                    eventCollection.add(events[i]);
                }
            }

            return eventCollection;
        }
    }

    /** get a field definition for the given name and resource
     *
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null 
     *
     * @return OwFieldDefinition or throws OwObjectNotFoundException
     */
    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        // try standard classes
        try
        {
            return OwStandardHistoryEntry.getStaticObjectClass().getPropertyClass(strFieldDefinitionName_p);
        }
        catch (Exception e)
        {
        }

        // try CE
        try
        {
            return getNetwork().getFieldDefinition(strFieldDefinitionName_p, strResourceName_p);
        }
        catch (Exception e)
        {
        }

        // try PE
        try
        {
            if (null != getFNBPMHistoryManager())
            {
                return getFNBPMHistoryManager().getFieldDefinition(strFieldDefinitionName_p, strResourceName_p);
            }
        }
        catch (Exception e)
        {
        }

        throw new OwObjectNotFoundException("OwFNCM5HistoryManager.getFieldDefinition: OwFieldDefinition not found, strFieldDefinitionName_p = " + strFieldDefinitionName_p);
    }

    /** reconstructs an Object from ECM Id, see OwObject.getDMSID for details.
     *
     * @param strDMSID_p ECM ID for the requested object
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @return an Object Instance
     *
     */
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwFNCM5HistoryManager.getObjectFromDMSID: Not implemented or Not supported, DMSID = " + strDMSID_p);
    }

    /** get object from given path
     *
     * @param strPath_p path to the object starting with "/..."
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @return OwObject 
     */
    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwFNCM5HistoryManager.getObjectFromPath: Not implemented or Not supported.");
    }

    /** get a Property class description of the available object class descriptions 
     *
     * @param strClassName_p Name of class
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return OwObjectClass instance
     */
    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwFNCM5HistoryManager.getObjectClass: Not implemented or Not supported.");
    }

    /** get a list of the available object class descriptions names
     *
     * @param iTypes_p int array of Object types as defined in OwObject, of null to retrieve all class names
     * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
     * @param fRootOnly_p true = gets only the root classes if we deal with a class tree, false = gets all classes
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return Map of symbol name keys mapped to displaynames
     */
    public java.util.Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNCM5HistoryManager.getObjectClassNames: Not implemented.");
    }

    /** get the resource with the specified key
     *
     * @param strID_p String resource ID, if strID_p is null, returns the default resource
     */
    public OwResource getResource(String strID_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNCM5HistoryManager.getResource: Not implemented.");
    }

    /** get a Iterator of available resource IDs
     * 
     * @return Collection of resource IDs used in getResource, or null if no resources are available
     */
    public java.util.Iterator getResourceIDs() throws Exception
    {
        throw new OwNotSupportedException("OwFNCM5HistoryManager.getResourceIDs: Not implemented.");
    }

    /** get the instance of the history manager */
    public OwEventManager getEventManager()
    {
        return this;
    }

    /** force the network adapter to reload all the static class description data. */
    public void refreshStaticClassdescriptions() throws Exception
    {
        // ignore
    }

    /** check if reload of all the static class description data is supported / necessary. 
     *
     * @return boolean true = refresh is supported and should be done, false = refresh is not supported and not necessary.
     */
    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return false;
    }

    /** get a prefix which is used to distinguish the DMSID of objects from the repository */
    public String getDMSPrefix()
    {
        return DMS_PREFIX;
    }

    /** releases all resources that have been used during this session
     */
    public void releaseResources() throws Exception
    {
        // no resources to release
    }

    /**
     *<p>
     * Like wild card definitions flyweight.
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
    private static class OwLikeWildCardDefinitions
    {
        private Vector m_definitions;

        public OwLikeWildCardDefinitions(OwRepositoryContext context_p)
        {
            m_definitions = new Vector();

            m_definitions.add(new OwStandardWildCardDefinition("%", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1(
                    "ecmimpl.OwFNCMHistoryManager.WILD_CARD_TYPE_MULTI_CHAR", "(%1) replaces any characters")));
            m_definitions.add(new OwStandardWildCardDefinition("_", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR, new OwString1(
                    "ecmimpl.OwFNCMHistoryManager.WILD_CARD_TYPE_SINGLE_CHAR", "(%1) replaces any character")));
        }

        public Collection getDefinitions()
        {
            return m_definitions;
        }
    }

    /** like wild card definitions fly weight */
    private OwLikeWildCardDefinitions m_likewildcarddefinitions;

    /** get a collection of wild card definitions that are allowed for the given field, resource and search operator
     * 
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null
     * @param iOp_p search operator as defined in OwSearchOperator CRIT_OP_...
     * 
     * @return Collection of OwWildCardDefinition, or null if no wildcards are defined
     * @throws Exception
     */
    public java.util.Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception
    {
        switch (iOp_p)
        {
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
            case OwSearchOperator.CRIT_OP_LIKE:
            {
                if (m_likewildcarddefinitions == null)
                {
                    m_likewildcarddefinitions = new OwLikeWildCardDefinitions(getContext());
                }

                return m_likewildcarddefinitions.getDefinitions();
            }

            default:
                return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#canBatch()
     */
    public boolean canBatch()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#closeBatch(com.wewebu.ow.server.ecm.OwBatch)
     */
    public void closeBatch(OwBatch batch_p) throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#openBatch()
     */
    public OwBatch openBatch() throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }
}