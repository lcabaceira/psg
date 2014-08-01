package com.wewebu.ow.server.history;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwClass;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardClassSelectObject;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardEnumCollection;
import com.wewebu.ow.server.field.OwStandardLocalizeableEnum;
import com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base implementation for history managers to collect and retrieve history information.<br/>
 * History Events are collected both in the EcmAdapter and in the Workdesk itself.<br/>
 * Most ECM Systems will write their own history and so the history manager needs only to read that information.
 * If the ECM System does not write a history, then the Adapter needs to write events to a database
 * and the history manager needs to read the events there.<br/>
 * For Workdesk events such as a clicked plugin the addEntry function needs to be
 * implemented and the event needs to be written to a database.<br/><br/>
 * To be implemented with the specific ECM system.<br/><br/>
 * You get a instance of the HistoryManager by calling getContext().getHistoryManager().
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
public abstract class OwStandardHistoryManager implements OwHistoryManager
{
    /**configuration for touch feature, aka in memory history*/
    private OwTouchConfiguration m_inMemoryConfiguration = new OwTouchConfiguration(false);
    /**contains session history entries with <code>key = dmsid+eventType</code>*/
    private Map<String, List<OwSessionHistoryEntry>> m_eventTypeSessionEntries = new HashMap<String, List<OwSessionHistoryEntry>>();
    /**contains session history entries with <code>key = dmsid+eventId</code>*/
    private Map<String, List<OwSessionHistoryEntry>> m_eventIdSessionEntries = new HashMap<String, List<OwSessionHistoryEntry>>();
    /**contains session history entries with <code>key = dmsid+eventType+eventId</code>*/
    private Map<String, List<OwSessionHistoryEntry>> m_eventTypeAndIdSessionEntries = new HashMap<String, List<OwSessionHistoryEntry>>();

    // === display names    
    /**
     *<p>
     * Singleton class for ID display strings.
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
    @SuppressWarnings("unchecked")
    private static class OwEventIDDisplayStrings
    {
        private java.util.Map m_displaystrings = new java.util.HashMap();

        public OwEventIDDisplayStrings()
        {
            m_displaystrings.put(HISTORY_EVENT_ID_LOGIN, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_LOGIN", "Login"));
            m_displaystrings.put(HISTORY_EVENT_ID_LOGOFF, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_LOGOFF", "Logout"));
            m_displaystrings.put(HISTORY_EVENT_ID_SEARCH, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_SEARCH", "Search"));
            m_displaystrings.put(HISTORY_EVENT_ID_NEW_OBJECT, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_NEW_OBJECT", "Create new object"));
            m_displaystrings.put(HISTORY_EVENT_ID_COPY_OBJECT, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_COPY_OBJECT", "Copy object"));
            m_displaystrings.put(HISTORY_EVENT_ID_DOWNLOAD, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_DOWNLOAD", "Download object content"));
            m_displaystrings.put(HISTORY_EVENT_ID_UPLOAD, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_UPLOAD", "Upload object content"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_ADD, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_ADD", "Add object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_MOVE, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_MOVE", "Move object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_LOCK, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_LOCK", "Lock object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_REMOVE_REF, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_REMOVE_REF", "Remove object reference"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_DELETE, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_DELETE", "Delete object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_DISPATCH, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_DISPATCH", "Dispatch object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_RETURN_TO_SOURCE, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_RETURN_TO_SOURCE", "Return to source"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_RESUBMIT, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_RESUBMIT", "Resubmit object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES", "Modify object properties"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_MODIFY_PERMISSIONS, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_MODIFY_PERMISSIONS", "Modify object permissions"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_CHECKOUT, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_CHECKOUT", "Check out object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_CANCELCHECKOUT, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_CANCELCHECKOUT", "Cancel check-out"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_CHECKIN, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_CHECKIN", "Check in object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_PROMOTE, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_PROMOTE", "Promote object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_DEMOTE, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_DEMOTE", "Demote object"));
            m_displaystrings.put(HISTORY_EVENT_ID_OBJECT_GENERIC, new OwString("event.OwEventManager.idname.HISTORY_EVENT_ID_OBJECT_GENERIC", "Change object"));
        }

        /** add a string to the static map
         * 
         * @param id_p
         * @param displaystring_p
         * @since 2.5.2.0
         */
        public void addDisplayString(String id_p, OwString displaystring_p)
        {
            m_displaystrings.put(id_p, displaystring_p);
        }

        /** get displayname for given event ID as defined in HISTORY_EVENT_ID_...
         */
        public String getDisplayName(java.util.Locale locale_p, String strEventID_p)
        {
            OwString displayname = (OwString) m_displaystrings.get(strEventID_p);
            if (displayname == null)
            {
                return strEventID_p;
            }
            else
            {
                return displayname.getString(locale_p);
            }
        }

        /** get a collection of the predefined event IDs 
         * 
         * @return a {@link Collection}
         */
        public Collection getEventIDs()
        {
            return m_displaystrings.keySet();
        }
    }

    /** singleton for ID display strings */
    private static OwEventIDDisplayStrings m_EventIDDisplayNames = new OwEventIDDisplayStrings();

    /**
     *<p>
     * Event displaynames singleton class.
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
    private static class OwEventTypeDisplayNames
    {
        /** map event types to displaynames */
        private OwStandardEnumCollection m_enums = new OwStandardEnumCollection();

        /** init the map */
        public OwEventTypeDisplayNames()
        {
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_GENERIC), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_GENERIC", "General")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI", "User Interface")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW", "View")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT", "Edit")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_PLUGIN_DSPATCH), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_DSPATCH", "Open")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_ECM), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_ECM", "ECM")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_OBJECT), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_OBJECT", "Object")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_PROPERTY), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_PROPERTY", "Property")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_EVENT_TYPE_VERSION), new OwString("event.OwEventManager.HISTORY_EVENT_TYPE_VERSION", "Version")));
        }

        /** get a OwField enumerator for Combobox selection
         */
        public OwEnumCollection getEnum()
        {
            return m_enums;
        }
    }

    /** event displaynames singleton */
    private static OwEventTypeDisplayNames m_EventTypeDisplayNames = new OwEventTypeDisplayNames();

    /**
     *<p>
     * Status displaynames singleton class.
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
    private static class OwEventStatusDisplayNames
    {
        /** map event types to displaynames */
        private OwStandardEnumCollection m_enums = new OwStandardEnumCollection();

        /** init the map */
        public OwEventStatusDisplayNames()
        {
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_STATUS_OK), new OwString("event.OwEventManager.HISTORY_STATUS_OK", "OK")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_STATUS_FAILED), new OwString("event.OwEventManager.HISTORY_STATUS_FAILED", "Failed")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_STATUS_CANCEL), new OwString("event.OwEventManager.HISTORY_STATUS_CANCEL", "Cancel")));
            m_enums.add(new OwStandardLocalizeableEnum(Integer.valueOf(HISTORY_STATUS_BEGIN), new OwString("event.OwEventManager.HISTORY_STATUS_BEGIN", "Begin")));
        }

        /** get a OwField enumerator for Combobox selection
         */
        public OwEnumCollection getEnum()
        {
            return m_enums;
        }
    }

    /** status displaynames singleton */
    private static OwEventStatusDisplayNames m_StatusDisplayNames = new OwEventStatusDisplayNames();

    /** configuration node with XML config information */
    private OwXMLUtil m_ConfigNode;

    /** reference to the main app context of the application */
    private OwHistoryManagerContext m_MainContext;
    private OwNetwork m_network;

    /** add a string to the static map
     * 
     * @param id_p
     * @param displaystring_p
     * @since 2.5.2.0
     */
    public static void addEventIDDisplayString(String id_p, OwString displaystring_p)
    {
        m_EventIDDisplayNames.addDisplayString(id_p, displaystring_p);
    }

    /** get displayname for given predefined event ID as defined in HISTORY_EVENT_ID_...
     */
    public static String getEventIDDisplayName(java.util.Locale locale_p, String strEventID_p)
    {
        return m_EventIDDisplayNames.getDisplayName(locale_p, strEventID_p);
    }

    /** get the predefined event IDs 
     */
    @SuppressWarnings("unchecked")
    public static Collection getEventIDs()
    {
        return m_EventIDDisplayNames.getEventIDs();
    }

    /** get a OwEnum enumerator for Combobox selection
     */
    public static OwEnumCollection getEventTypeEnum()
    {
        return m_EventTypeDisplayNames.getEnum();
    }

    /** get a OwEnum enumerator for Combobox selection
     */
    public static OwEnumCollection getEventStatusEnum()
    {
        return m_StatusDisplayNames.getEnum();
    }

    /** get configuration node with XML config information */
    protected OwXMLUtil getConfigNode()
    {
        return m_ConfigNode;
    }

    /** get the application context
     *
     * @return OwMainAppContext
     */
    public OwHistoryManagerContext getContext()
    {
        return m_MainContext;
    }

    /** init the manager, set context
     * @param configNode_p OwXMLUtil node with configuration information
     * @param mainContext_p reference to the main app context of the application 
     * @exception Exception
     */
    public void init(OwHistoryManagerContext mainContext_p, OwXMLUtil configNode_p) throws Exception
    {
        // === init members
        m_MainContext = mainContext_p;
        m_ConfigNode = configNode_p;
        Locale locale = m_MainContext.getLocale();
        if (locale == null)
        {
            locale = Locale.ENGLISH;
        }
        m_inMemoryConfiguration.init(m_ConfigNode.getSubNode("SessionEvents"), locale);
    }

    /** set a reference to the network adapter 
     * 
     * @param network_p OwNetwork
     */
    public void setNetwork(OwNetwork network_p)
    {
        m_network = network_p;
    }

    /** get a reference to the network adapter
     * 
     * @return OwNetwork
     */
    public OwNetwork getNetwork()
    {
        return m_network;
    }

    /**
     * Search for in memory history entries.
     * @see com.wewebu.ow.server.history.OwHistoryManager#doObjectSearch(com.wewebu.ow.server.ecm.OwObjectReference, com.wewebu.ow.server.field.OwSearchNode, com.wewebu.ow.server.field.OwSort, java.util.Collection, int[], int)
     */
    @SuppressWarnings("unchecked")
    public OwObjectCollection doObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int[] includeSubObjectTypes_p, int maxSize_p) throws Exception
    {
        OwObjectCollection result = null;
        if (m_inMemoryConfiguration.isTouchConfigurationInitialized() && object_p != null)
        {
            if (filterCriteria_p != null)
            {
                Map criteriaMap = filterCriteria_p.getCriteriaMap(OwSearchNode.FILTER_NONE);

                OwSearchCriteria mainCriteria = (OwSearchCriteria) criteriaMap.get(OwStandardClassSelectObject.CLASS_NAME);
                if (mainCriteria != null)
                {
                    Object value = mainCriteria.getValue();
                    if (value != null)
                    {
                        OwClass selectionClass = null;
                        if (value.getClass().isArray())
                        {
                            Object[] valueAsArray = (Object[]) value;
                            selectionClass = valueAsArray.length > 0 ? (OwClass) valueAsArray[0] : null;
                        }

                        if (selectionClass != null && selectionClass.getClassName().equals(OwSessionHistoryEntry.getStaticObjectClass().getClassName()))
                        {
                            result = new OwStandardObjectCollection();
                            Integer eventTypeAsInteger = null;
                            String eventId = null;
                            OwSearchCriteria eventTypeCriteria = (OwSearchCriteria) criteriaMap.get(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY);
                            if (eventTypeCriteria != null)
                            {
                                if (eventTypeCriteria.getOperator() != OwSearchOperator.CRIT_OP_EQUAL)
                                {
                                    throw new OwInvalidOperationException("Only search for exact match is allowed");
                                }
                                eventTypeAsInteger = (Integer) eventTypeCriteria.getValue();
                            }
                            OwSearchCriteria eventIdCriteria = (OwSearchCriteria) criteriaMap.get(OwStandardHistoryEntryObjectClass.ID_PROPERTY);
                            if (eventIdCriteria != null)
                            {

                                if (eventIdCriteria.getOperator() != OwSearchOperator.CRIT_OP_EQUAL)
                                {
                                    throw new OwInvalidOperationException("Only search for exact match is allowed");
                                }
                                eventId = (String) eventIdCriteria.getValue();
                            }
                            String key = null;
                            Map<String, List<OwSessionHistoryEntry>> searchMap = null;
                            //exact match
                            if (eventId == null)
                            {
                                if (eventTypeAsInteger != null)
                                {
                                    key = object_p.getDMSID() + String.valueOf(eventTypeAsInteger);
                                    searchMap = m_eventTypeSessionEntries;
                                }
                                // if eventTypeAsInteger == null and eventId==null do not perform any search
                            }
                            else
                            {
                                if (eventTypeAsInteger == null)
                                {
                                    key = createDmsidEventIdKey(eventId, object_p.getDMSID());
                                    searchMap = m_eventIdSessionEntries;
                                }
                                else
                                {
                                    key = createDmsidEventTypeEventIdKey(eventTypeAsInteger.intValue(), eventId, object_p.getDMSID());
                                    searchMap = m_eventTypeAndIdSessionEntries;
                                }
                            }
                            if (searchMap != null)
                            {
                                List<OwSessionHistoryEntry> partialResult = searchMap.get(key);
                                if (partialResult != null)
                                {
                                    result.addAll(partialResult);
                                }

                                String filteringKey = null;
                                searchMap = null;
                                //handle generics search for combination of ALL eventIDs and All event types 
                                //because there is no generic entry in the key map
                                if (m_inMemoryConfiguration.isGenericEventId(eventId) && m_inMemoryConfiguration.isGenericEventType(eventTypeAsInteger))
                                {
                                    filteringKey = object_p.getDMSID();
                                    searchMap = m_eventTypeAndIdSessionEntries;
                                }
                                else if (m_inMemoryConfiguration.isGenericEventId(eventId) && !m_inMemoryConfiguration.isGenericEventType(eventTypeAsInteger))
                                {
                                    //event ID = * eventType is null or a specific value
                                    if (eventTypeAsInteger == null)
                                    {
                                        filteringKey = object_p.getDMSID();
                                        searchMap = m_eventIdSessionEntries;
                                    }
                                    else
                                    {
                                        filteringKey = createDmsidEventTypeKey(eventTypeAsInteger.intValue(), object_p.getDMSID());
                                        searchMap = m_eventTypeSessionEntries;
                                    }

                                }
                                else if (!m_inMemoryConfiguration.isGenericEventId(eventId) && m_inMemoryConfiguration.isGenericEventType(eventTypeAsInteger))
                                {
                                    //event ID is null or have a specific value, event type is *
                                    if (eventId == null)
                                    {
                                        filteringKey = object_p.getDMSID();
                                        searchMap = m_eventTypeSessionEntries;
                                    }
                                    else
                                    {
                                        filteringKey = createDmsidEventIdKey(eventId, object_p.getDMSID());
                                        searchMap = m_eventIdSessionEntries;
                                    }
                                }
                                if (filteringKey != null && searchMap != null)
                                {
                                    addEntriesToResultForDMSID(result, searchMap, filteringKey);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Append all values to result collection, corresponding to a key that is starting with the given String 
     * @param result_p - the container of results
     * @param searchMap_p - the map to search for keys
     * @param filteringKey_p - the string from the beginning of key
     * @since 2.5.2.0
     */
    @SuppressWarnings(value = { "unchecked" })
    private void addEntriesToResultForDMSID(OwObjectCollection result_p, Map<String, List<OwSessionHistoryEntry>> searchMap_p, String filteringKey_p)
    {
        Iterator<Entry<String, List<OwSessionHistoryEntry>>> entryIterator = searchMap_p.entrySet().iterator();
        while (entryIterator.hasNext())
        {
            Entry<String, List<OwSessionHistoryEntry>> entry = entryIterator.next();
            String toBeComparedKey = entry.getKey();
            if (toBeComparedKey.startsWith(filteringKey_p))
            {
                result_p.addAll(entry.getValue());
            }
        }
    }

    /**
     * @see com.wewebu.ow.server.event.OwEventManager#addEvent(int, java.lang.String, com.wewebu.ow.server.event.OwEvent, int)
     */
    @SuppressWarnings("unchecked")
    public void addEvent(int eventType_p, String strEventID_p, OwEvent event_p, int status_p) throws Exception
    {
        if (m_inMemoryConfiguration.isTouchConfigurationInitialized() && status_p == OwEventManager.HISTORY_STATUS_OK)
        {
            boolean isConfigured = false;
            if (strEventID_p == null)
            {
                isConfigured = m_inMemoryConfiguration.isConfigured(eventType_p);
            }
            else
            {

                isConfigured = m_inMemoryConfiguration.isConfigured(eventType_p, strEventID_p);
            }
            if (isConfigured)
            {
                if (OwEventManager.HISTORY_EVENT_TYPE_CLEAR_SESSION_HISTORY_FOR_OBJECT == eventType_p)
                {
                    clearSessionEvents(event_p);
                }
                else
                {
                    List<String> dmsIDs = getDMSIdsFromEvent(event_p);
                    for (Iterator<String> iterator = dmsIDs.iterator(); iterator.hasNext();)
                    {
                        String dmsID = iterator.next();
                        String keyDmsidEventType = createDmsidEventTypeKey(eventType_p, dmsID);
                        String keyDmsIdEventId = createDmsidEventIdKey(strEventID_p, dmsID); // works event when strEvetnId_p is null;
                        String keyDmsIdEventTypeEventId = createDmsidEventTypeEventIdKey(eventType_p, strEventID_p, dmsID);

                        OwSessionHistoryEntry historyEntry = new OwSessionHistoryEntry(getContext(), new Date(), strEventID_p, eventType_p, status_p, event_p.getSummary(), getNetwork().getCredentials().getUserInfo().getUserID());

                        saveHistoryEntry(historyEntry, eventType_p, strEventID_p, event_p, status_p, keyDmsidEventType, m_eventTypeSessionEntries);

                        saveHistoryEntry(historyEntry, eventType_p, strEventID_p, event_p, status_p, keyDmsIdEventId, m_eventIdSessionEntries);

                        saveHistoryEntry(historyEntry, eventType_p, strEventID_p, event_p, status_p, keyDmsIdEventTypeEventId, m_eventTypeAndIdSessionEntries);

                    }
                }
            }
        }
    }

    /**
     * Clear all events related to the DMSIDs contained in the event parameter
     * @param event_p - the event parameter
     * @throws Exception 
     * @since 3.1.0.0
     */
    @SuppressWarnings("unchecked")
    private void clearSessionEvents(OwEvent event_p) throws Exception
    {
        List dmsIds = getDMSIdsFromEvent(event_p);
        Iterator dmsidsIterator = dmsIds.iterator();
        while (dmsidsIterator.hasNext())
        {
            String dmsId = (String) dmsidsIterator.next();
            clearSessionEventsFromMap(dmsId, m_eventIdSessionEntries);
            clearSessionEventsFromMap(dmsId, m_eventTypeAndIdSessionEntries);
            clearSessionEventsFromMap(dmsId, m_eventTypeSessionEntries);
        }
    }

    /**
     * Remove the entries associated with this <code>DMSID</code>
     * @param dmsId_p - the object DMSID
     * @param sessionEntries_p - the map containing entries for this DMSID
     * @since 3.1.0.0
     */
    private void clearSessionEventsFromMap(String dmsId_p, Map<String, List<OwSessionHistoryEntry>> sessionEntries_p)
    {
        Set<String> keyset = sessionEntries_p.keySet();
        Iterator<String> keysIterator = keyset.iterator();
        while (keysIterator.hasNext())
        {
            String key = keysIterator.next();
            if (key.startsWith(dmsId_p))
            {
                keysIterator.remove();
            }
        }
    }

    /**
     * Create a key for in memory session events.
     * @param eventType_p
     * @param strEventID_p
     * @param dmsID_p
     * @return the created key.
     * @since 2.5.2.0
     */
    private String createDmsidEventTypeEventIdKey(int eventType_p, String strEventID_p, String dmsID_p)
    {
        return dmsID_p + String.valueOf(eventType_p) + strEventID_p;
    }

    /**
     * Create a key for in memory session events.
     * @param strEventID_p
     * @param dmsID_p
     * @return the created key
     * @since 2.5.2.0
     */
    private String createDmsidEventIdKey(String strEventID_p, String dmsID_p)
    {
        return dmsID_p + strEventID_p;
    }

    /**
     * Create a key for in memory session events.
     * @param eventType_p - the event type
     * @param dmsID_p - object DMSID
     * @return the created key
     * @since 2.5.2.0
     */
    private String createDmsidEventTypeKey(int eventType_p, String dmsID_p)
    {
        return dmsID_p + String.valueOf(eventType_p);
    }

    /**
     * Create a new entry in history map. 
     * @param historyEntry_p 
     * @param eventType_p - event type
     * @param strEventID_p - event ID 
     * @param event_p - the event
     * @param status_p - the status
     * @param key_p - the key
     * @param sessionEntriesMap_p - the map
     * @throws Exception - thrown if something is wrong.
     * @since 2.5.2
     */
    private void saveHistoryEntry(OwSessionHistoryEntry historyEntry_p, int eventType_p, String strEventID_p, OwEvent event_p, int status_p, String key_p, Map<String, List<OwSessionHistoryEntry>> sessionEntriesMap_p) throws Exception
    {

        List<OwSessionHistoryEntry> availableEntries = sessionEntriesMap_p.get(key_p);
        if (availableEntries == null)
        {
            availableEntries = new ArrayList<OwSessionHistoryEntry>();
        }
        availableEntries.add(historyEntry_p);
        sessionEntriesMap_p.put(key_p, availableEntries);
    }

    /**
     * Create a list of object DMSIDs affected by this event.
     * The delete event is ignored.
     * @param event_p - the event.
     * @return <code>java.util.List</code> -  a list of DMSID affected by the given event.
     * @since 2.5.2.0
     */
    @SuppressWarnings(value = { "unchecked" })
    protected List getDMSIdsFromEvent(OwEvent event_p) throws Exception
    {
        List affectedDMSids = new LinkedList();
        if (event_p != null)
        {
            if (event_p instanceof OwHistoryAnnotationEvent)
            {
                OwHistoryAnnotationEvent event = (OwHistoryAnnotationEvent) event_p;
                if (event.getAffectedObject() != null)
                {
                    affectedDMSids.add(event.getAffectedObject().getDMSID());
                }
            }
            else if (event_p instanceof OwHistoryObjectChangeEvent)
            {
                OwHistoryObjectChangeEvent event = (OwHistoryObjectChangeEvent) event_p;
                Collection affectedObjects = event.getAffectedObjects();
                if (affectedObjects != null) //Bug 2498, event.getAffectedObjects(); can be null
                {
                    for (Iterator iterator = affectedObjects.iterator(); iterator.hasNext();)
                    {
                        OwObjectReference object = (OwObjectReference) iterator.next();
                        affectedDMSids.add(object.getDMSID());
                    }
                }
            }
            else if (event_p instanceof OwHistoryObjectCreateEvent)
            {
                OwHistoryObjectCreateEvent event = (OwHistoryObjectCreateEvent) event_p;
                affectedDMSids.add(event.getDmsid());
            }
            else if (event_p instanceof OwHistoryPropertyChangeEvent)
            {
                OwHistoryPropertyChangeEvent event = (OwHistoryPropertyChangeEvent) event_p;
                if (event.getAffectedObject() != null)
                {
                    affectedDMSids.add(event.getAffectedObject().getDMSID());
                }
            }
            else if (event_p instanceof OwSessionHistoryDeleteEvent)
            {
                OwSessionHistoryDeleteEvent event = (OwSessionHistoryDeleteEvent) event_p;
                affectedDMSids.add(event.getAffectedDmsId());
            }
        }
        return affectedDMSids;
    }

    @Override
    public OwIterable<OwHistoryEntry> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO : History manager page search
        throw new OwNotSupportedException("History managers do not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }

}