package com.wewebu.ow.server.history;

import java.util.Collection;
import java.util.Date;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;

/**
 *<p>
 * History entry for in memory stored events.
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
 *@since 2.5.2.0
 */
public class OwSessionHistoryEntry extends OwStandardHistoryEntry
{

    /**
     *<p>
     * Object class description of history entry.
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
    public static class OwSessionHistoryEntryObjectClass extends OwStandardHistoryEntryObjectClass
    {
        public OwSessionHistoryEntryObjectClass()
        {
            super();
            m_strClassName = "OwSessionHistoryEntryObjectClass";
        }
    }

    /** class description of history entry */
    private static OwStandardObjectClass m_ClassDescription = new OwSessionHistoryEntryObjectClass();

    /** construct a history entry for object modifying events
    *
    * @param time_p time of the event
    * @param id_p pluginid or function name the invoked event
    * @param type_p type of the event as specified in OwEventManager.HISTORY_EVENT_TYPE_...
    * @param status_p type of the event as specified in OwEventManager.HISTORY_STATUS_...
    * @param strSummary_p of the event as specified in OwEvent
    * @param parent_p OwObjectReference as specified in OwHistoryObjectChangeEvent
    * @param objects_p Collection of modified OwObjectReference's as specified in OwHistoryObjectChangeEvent
    *
    */
    public OwSessionHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int type_p, int status_p, String strSummary_p, String strUser_p, OwObjectReference parent_p, Collection objects_p) throws Exception
    {
        super(context_p, time_p, id_p, type_p, status_p, strSummary_p, strUser_p, parent_p, objects_p);
    }

    /** construct a history entry for property modifying events
    *
    * @param time_p time of the event
    * @param id_p pluginid or function name the invoked event
    * @param type_p type of the event as specified in OwEventManager.HISTORY_EVENT_TYPE_...
    * @param status_p type of the event as specified in OwEventManager.HISTORY_STATUS_...
    * @param strSummary_p of the event as specified in OwEvent
    * @param object_p modified OwObjectReference as specified in OwHistoryPropertyChangeEvent
    * @param fielddefinitionprovider_p OwFieldDefinitionProvider to lookup the property definitions
    * @param resource_p String name of the resource to lookup the property definitions, or null to use default resource
    * @param propertycardinalitiesandnames_p Collection of modified property cardinality and names as specified in CARDINALITY_.... Each cardinality follows a propertyname.
    *          in case of an array, the values are delimited with OwEscapedStringTokenizer.STANDARD_DELIMITER
    * @param oldProperties_p Collection of modified string values specified in OwHistoryPropertyChangeEvent
    * @param newProperties_p Collection of modified string values as specified in OwHistoryPropertyChangeEvent
    *
    */
    public OwSessionHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int type_p, int status_p, String strSummary_p, String strUser_p, OwObjectReference object_p, OwFieldDefinitionProvider fielddefinitionprovider_p,
            String resource_p, Collection propertycardinalitiesandnames_p, Collection oldProperties_p, Collection newProperties_p) throws Exception
    {
        super(context_p, time_p, id_p, type_p, status_p, strSummary_p, strUser_p, object_p, fielddefinitionprovider_p, resource_p, propertycardinalitiesandnames_p, oldProperties_p, newProperties_p);
    }

    /** construct a history entry for non modifying or generic events
    *
    * @param time_p time of the event
    * @param id_p pluginid or function name the invoked event
    * @param type_p type of the event as specified in OwEventManager.HISTORY_EVENT_TYPE_...
    * @param status_p type of the event as specified in OwEventManager.HISTORY_STATUS_...
    * @param strSummary_p of the event as specified in OwEvent
    *
    */
    public OwSessionHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int type_p, int status_p, String strSummary_p, String strUser_p) throws Exception
    {
        super(context_p, time_p, id_p, type_p, status_p, strSummary_p, strUser_p);
    }

    /**
     * @see com.wewebu.ow.server.history.OwStandardHistoryEntry#getObjectClass()
     */
    public com.wewebu.ow.server.ecm.OwObjectClass getObjectClass()
    {
        return m_ClassDescription;
    }

    /**
     * Get Object class
     */
    public static com.wewebu.ow.server.ecm.OwObjectClass getStaticObjectClass() throws Exception
    {
        return m_ClassDescription;
    }
}
