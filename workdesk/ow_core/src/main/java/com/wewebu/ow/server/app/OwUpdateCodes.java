package com.wewebu.ow.server.app;

/**
 *<p>
 * Global Definitions of the codes used in the <b>OwDocument.Update(...)</b> and
 * <b>OwClientRefreshContext.onClientRefreshContextUpdate(...)</b> functions.
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
public class OwUpdateCodes
{
    /** no code is specified */
    public static final int UPDATE_DEFAULT = 0;

    /** the object versions have changed */
    public static final int UPDATE_OBJECT_VERSION = 1;

    /** the object properties need to be updated by the view */
    public static final int UPDATE_OBJECT_PROPERTY = 2;

    /** the parent of the object has changed its document children */
    public static final int UPDATE_PARENT_OBJECT_CHILDS = 3;

    /** the object has changed its document children */
    public static final int UPDATE_OBJECT_CHILDS = 4;

    /** a new object was set */
    public static final int SET_NEW_OBJECT = 5;

    /** the object has changed its folder children */
    public static final int UPDATE_OBJECT_FOLDER_CHILDS = 8;

    /** the user logs out */
    public static final int LOGOUT = 9;

    /** the settings have changed */
    public static final int UPDATE_SETTINGS = 10;

    /** the object has been deleted: parameters contains a Set of DMSIDs of the deleted objects */
    public static final int DELETE_OBJECT = 11;

    /** a view has changed */
    public static final int CHANGE_VIEW = 12;

    /** the parent object has changed its folder children */
    public static final int UPDATE_PARENT_OBJECT_FOLDER_CHILDS = 14;

    /** the object properties where modified and saved */
    public static final int MODIFIED_OBJECT_PROPERTY = 15;

    /** the object properties where not modified */
    public static final int OBJECT_PROPERTIES_NOT_CHANGED = 18;

    /** fired if a filter was changed and view needs to update
     * @since 4.2.0.0 */
    public static final int FILTER_CHANGED = 19;
    /**
     *  the object (workitem) was dispatched
     *  @since 3.1.0.0
     */
    public static final int OBJECT_DISPATCH = 16;

    /**
     *  the object (workitem) was forwarded
     *  @since 3.1.0.0
     */
    public static final int OBJECT_FORWARD = 17;

    // === CUSTOM UPDATE CODES START WITH CUSTOM_CODE_START + ...
    /** the selection changed in a view */
    public static final int CUSTOM_CODE_START = 0x1000;
}