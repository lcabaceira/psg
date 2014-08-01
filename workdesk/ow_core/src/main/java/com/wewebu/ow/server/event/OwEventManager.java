package com.wewebu.ow.server.event;

/**
 *<p>
 * Interface for adding events to a database. Base class of the history manager to add new events to the database
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
public interface OwEventManager
{
    ///////////////////////////////////////////////    
    // Event Types
    ///////////////////////////////////////////////  
    /** event type to be used in addEvent: generated for invoked plugin events, which do not modify or view anything like copy or add to favorites */
    public static final int HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI = 1;
    /** event type to be used in addEvent: generated for invoked plugin events, which view objects without modifying, like edit properties if save was not pressed */
    public static final int HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW = 2;
    /** event type to be used in addEvent: generated for invoked plugin events, which edit objects, like edit properties if save was pressed  */
    public static final int HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT = 3;
    /** event type to be used in addEvent: generated when dispatch function is called e.g. when a folder is opened in the RecordManager */
    public static final int HISTORY_EVENT_TYPE_PLUGIN_DSPATCH = 4;
    /** event type to be used in addEvent: generated for login or search events */
    public static final int HISTORY_EVENT_TYPE_ECM = 5;
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final int HISTORY_EVENT_TYPE_OBJECT = 6;
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final int HISTORY_EVENT_TYPE_PROPERTY = 7;
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final int HISTORY_EVENT_TYPE_VERSION = 9;
    /** event type to be used in addEvent: generated for arbitrary events */
    public static final int HISTORY_EVENT_TYPE_GENERIC = 10;

    /**
     * event type to be used in addEvent: 
     * used to  for clear the session history events associated with the OwObject
     * @since 3.1.0.0 
     * */
    public static final int HISTORY_EVENT_TYPE_CLEAR_SESSION_HISTORY_FOR_OBJECT = 11;

    ///////////////////////////////////////////////    
    // Predefined ID's    
    ///////////////////////////////////////////////  

    // === HISTORY_EVENT_TYPE_ECM ID's
    /** event type to be used in addEvent: generated for login events */
    public static final String HISTORY_EVENT_ID_LOGIN = "login";
    /** event type to be used in addEvent: generated for logoff events */
    public static final String HISTORY_EVENT_ID_LOGOFF = "logoff";
    /** event type to be used in addEvent: generated for search events */
    public static final String HISTORY_EVENT_ID_SEARCH = "search";

    // === HISTORY_EVENT_TYPE_OBJECT
    /** event type to be used in addEvent: generated for new object events */
    public static final String HISTORY_EVENT_ID_NEW_OBJECT = "newobject";
    /** event type to be used in addEvent: generated for new object events */
    public static final String HISTORY_EVENT_ID_COPY_OBJECT = "copyobject";
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final String HISTORY_EVENT_ID_DOWNLOAD = "download";
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final String HISTORY_EVENT_ID_UPLOAD = "upload";
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final String HISTORY_EVENT_ID_OBJECT_ADD = "addobject";
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final String HISTORY_EVENT_ID_OBJECT_MOVE = "moveobject";
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final String HISTORY_EVENT_ID_OBJECT_LOCK = "lockobject";
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final String HISTORY_EVENT_ID_OBJECT_REMOVE_REF = "removeref";
    /** event type to be used in addEvent: generated for OwObject access events */
    public static final String HISTORY_EVENT_ID_OBJECT_DELETE = "delete";
    /** event type to be used in addEvent: generated for workflow objects */
    public static final String HISTORY_EVENT_ID_OBJECT_DISPATCH = "dispatch";
    /** event type to be used in addEvent: generated for workflow objects */
    public static final String HISTORY_EVENT_ID_OBJECT_RETURN_TO_SOURCE = "returntosource";
    /** event type to be used in addEvent: generated if a object is resubmitted */
    public static final String HISTORY_EVENT_ID_OBJECT_RESUBMIT = "resubmit";
    /** event type to be used in addEvent: generic event */
    public static final String HISTORY_EVENT_ID_OBJECT_GENERIC = "generic";

    // === HISTORY_EVENT_TYPE_PROPERTY ID's    
    /** event type to be used in addEvent: generated for OwObject property events */
    public static final String HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES = "modifyprops";
    /** event type to be used in addEvent: generated for OwObject property events */
    public static final String HISTORY_EVENT_ID_OBJECT_MODIFY_PERMISSIONS = "modifyperm";
    /** event type to be used in addEvent: generated for OwObject property events */
    public static final String HISTORY_EVENT_ID_OBJECT_MODIFY_ANNOTATIONS = "modifyannot";

    // === HISTORY_EVENT_TYPE_VERSION ID's
    /** event type to be used in addEvent: generated for OwObject DMS events */
    public static final String HISTORY_EVENT_ID_OBJECT_CHECKOUT = "checkout";
    /** event type to be used in addEvent: generated for OwObject DMS events */
    public static final String HISTORY_EVENT_ID_OBJECT_CANCELCHECKOUT = "cancelcheckout";
    /** event type to be used in addEvent: generated for OwObject DMS events */
    public static final String HISTORY_EVENT_ID_OBJECT_CHECKIN = "checkin";
    /** event type to be used in addEvent: generated for OwObject DMS events */
    public static final String HISTORY_EVENT_ID_OBJECT_PROMOTE = "promote";
    /** event type to be used in addEvent: generated for OwObject DMS events */
    public static final String HISTORY_EVENT_ID_OBJECT_DEMOTE = "demote";

    // === Status flags
    /** status flag: event was ok */
    public static final int HISTORY_STATUS_OK = 1;
    /** status flag: event failed */
    public static final int HISTORY_STATUS_FAILED = 2;
    /** status flag: event was canceled */
    public static final int HISTORY_STATUS_CANCEL = 3;
    /** status flag: event started, there will be an additional failed, cancel, or ok event */
    public static final int HISTORY_STATUS_BEGIN = 4;
    /** status flag: event was not executed, the resource was disabled */
    public static final int HISTORY_STATUS_DISABLED = 5;

    /** add a new history event to the history database if supported by the historymanager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param event_p OwEvent according to iEventType_p, contains additional information, such as the affected Objects or properties
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public abstract void addEvent(int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws Exception;

    /** add a new history event to the history database if supported by the historymanager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public abstract void addEvent(int iEventType_p, String strEventID_p, int iStatus_p) throws Exception;
}