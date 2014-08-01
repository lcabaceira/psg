package com.wewebu.ow.server.app;

import javax.servlet.http.HttpServletRequest;

/**
 *<p>
 * Interface for a editable property used in HTML forms.
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
public interface OwEditableProperty
{
    /** get current value of property
     * @return Object
     */
    public abstract Object getValue();

    /** set current value of property
     *
     * @param value_p Object
     */
    public abstract void setValue(Object value_p);

    /** apply changes form a HTML form request, to be overridden
     *
     * @param locale_p Locale to use
     * @param request_p HttpServletRequest with form data to update the property
     *
     * @return true = value changed, false = value did not change
     */
    public abstract boolean update(java.util.Locale locale_p, HttpServletRequest request_p) throws Exception;

    /** gets a error message for this field, if update failed, clears the message automatically 
     *
     * @return String error message or an empty string
     */
    public abstract String getSafePropertyError(java.util.Locale locale_p);
}