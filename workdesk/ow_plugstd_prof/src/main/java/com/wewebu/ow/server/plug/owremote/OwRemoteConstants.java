package com.wewebu.ow.server.plug.owremote;

/**
 *<p>
 * Defines constants used for remote links configuration , building and processing.  
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
 *@since 3.2.0.1
 */
public interface OwRemoteConstants
{
    // === common query keys
    /** key for event names */
    public static final String QUERY_KEY_EVENT_NAME = "ctrlev";
    /** DMSID of object to use */
    public static final String QUERY_KEY_DMSID = "dmsid";
    /** prefix of property  */
    public static final String QUERY_KEY_PROPERTY_PREFIX = "prop_";
    /** plugin to use */
    public static final String QUERY_KEY_PLUGIN_ID = "plugid";
    /** prefix of property to be used for update */
    public static final String QUERY_KEY_UPDATE_PROPERTY_PREFIX = "uprop_";

    // === events
    /** event name for opening a folder in the record plugin
     * EXAMPLE-LINK:
     * http://localhost:8080/workdesk/default41.jsp?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=openrecord&dmsid=owdm,owfi,/dummy/dummyarchiv/akte1&subpath=Vertrag_1/Fall 1
     */
    public static final String CONTROL_EVENT_OPEN_RECORD = "openrecord";

    /**
     * encrypted url for folder used in e-mail url
     */
    public static final String CONTROL_EVENT_OPEN_RECORD_ENCRYPTED = "openrecord_enc";

    /** event name for performing a template based search
     * EXAMPLE-LINK:
     * http://localhost:8080/workdesk/?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=search&plugid=com.wewebu.ow.Search&stname=FormularTest&maxsize=100&prop_name=Hallo&prop_date1=2006-09-23T10:10:10-01:00
     * */
    public static final String CONTROL_EVENT_SEARCH = "search";

    /** event name for setting object properties
     * EXAMPLE-LINK:
     * http://localhost:8080/workdesk/?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=modifyprops&dmsid=owdm,owfi,/dummy/dummyarchiv/Baufinanzierung.txt&prop_Address=New Name
     * */
    public static final String CONTROL_EVENT_MODIFY_PROPERTIES = "modifyprops";

    /** event name for starting the viewservlet from mimetable
     * EXAMPLE-LINK for FNIM docid 100000:
     * http://localhost:8080/workdesk/?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=view&dmsid=100000
     *
     * EXAMPLE-LINK for Dummy Adapter
     * http://localhost:8080/workdesk/?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=view&dmsid=owdm,owfi,/dummy/dummyarchiv/test.tif
     * */
    public static final String CONTROL_EVENT_VIEW = "view";

    /**
     * Same as CONTROL_EVENT_VIEW but with EnCryption
     */
    public static final String CONTROL_EVENT_VIEW_CRYPT = "viewcrypt";

    /** event name for opening a work item
     * EXAMPLE-LINK:
     * http://localhost:8080/workdesk/?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=workitem&dmsid=
     * */
    public static final String CONTROL_EVENT_OPEN_WORKITEM = "workitem";

    /** event name for opening a work item
     * EXAMPLE-LINK:
     * http://localhost:8080/workdesk/?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=workitem&dmsid=
     * */
    public static final String CONTROL_EVENT_OPEN_WORKITEM_ENC = "workitem_enc";

    //  === query keys
    /** subpath of record to open */
    public static final String QUERY_KEY_SUBPATH = "subpath";

    /** search template name of search template to open */
    public static final String QUERY_KEY_SEARCH_TEMPLATE_NAME = "stname";

    /** max count of documents in search */
    public static final String QUERY_KEY_SEARCH_MAX_SIZE = "maxsize";

    /** latest version parameter name */
    public static final String LATEST_VERSION_PARAMETER_NAME = "latestVersion";

    /**Configuration node name for control event overriding
     * since 3.2.0.1 */
    public static final String LINK_ELEMENTS = "LinkElements";

}
