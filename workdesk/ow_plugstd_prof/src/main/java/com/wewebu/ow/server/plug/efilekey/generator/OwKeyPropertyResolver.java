package com.wewebu.ow.server.plug.efilekey.generator;

import java.util.Locale;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * This interface provide behavior for getting the value from a given property name.<br>
 * The property can be a property form a given {@link OwObject} or some predefined 
 * system property:
 * <ul>
 * <li><b>_sys_counter</b> - Designates a unique counter<br>
 * <li><b>_sys_timeofday</b> - Designates the current date and time<br>
 * <li><b>_sys_username</b> - Designates the current user name<br>
 * <li><b>_sys_userlongname</b> - Designates the current user long name<br>
 * <li><b>_sys_userid</b> - Designates the current user id<br>
 * </ul>
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
 *@since 3.1.0.0
 */
public interface OwKeyPropertyResolver
{
    /** the <code>ow_sys_counter</code> property name*/
    public static final String SYS_COUNTER_PROP_NAME = "ow_sys_counter";
    /** the <code>ow_sys_timeofday</code> property name*/
    public static final String SYS_TIME_OF_DAY_PROP_NAME = "ow_today";
    /** the <code>ow_sys_username</code> property name*/
    public static final String SYS_USER_NAME_PROP_NAME = "ow_username";
    /** the <code>ow_sys_userlongname</code> property name*/
    public static final String SYS_USER_LONG_NAME_PROP_NAME = "ow_userlongname";
    /** the <code>ow_sys_usershortname</code> property name*/
    public static final String SYS_USER_SHORT_NAME_PROP_NAME = "ow_usershortname";
    /** the <code>ow_sys_userdisplayname</code> property name*/
    public static final String SYS_USER_DISPLAY_NAME_PROP_NAME = "ow_userdisplayname";
    /** the <code>ow_sys_userid</code> property name*/
    public static final String SYS_USER_ID_PROP_NAME = "ow_userid";
    /** the <code>ow_sys_guid</code> property name*/
    public static final String SYS_GUID_PROP_NAME = "ow_sys_guid";

    /**
     * Get the value from the given property referring expression.
     * @param propertyExpression_p - the expression that refers the property.
     * @return - the value of the property.
     * @throws OwInvalidOperationException - thrown when the given property name doesn't have associated a value
     */
    Object getPropertyValue(String propertyExpression_p) throws OwInvalidOperationException;

    /**
     * Return current {@link Locale} object.
     * @return the user {@link Locale} object.
     */
    Locale getLocale();
}
