package com.wewebu.ow.server.app;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

/**
 *<p>
 * Base class for a single editable property used in HTML forms.
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
public class OwEditablePropertyString implements OwEditableProperty
{
    /** current value of property */
    protected Object m_value;

    /** a error message for this field, if update failed */
    protected String m_strError;

    /** get current value of property
     * @return Object if isList() is true, Object is a list
     */
    public Object getValue()
    {
        return m_value;
    }

    /** set current value of property, to be overridden
     *
     * @param value_p Object
     */
    public void setValue(Object value_p)
    {
        // set current value list from value node
        m_value = value_p;
    }

    /** apply changes form a HTML form request, to be overridden
     *
     * @param request_p HttpServletRequest with form data to update the property
     *
     * @return true = value changed, false = value did not change
     */
    public boolean update(java.util.Locale locale_p, HttpServletRequest request_p) throws Exception
    {
        m_strError = null;

        try
        {
            m_value = getValueFromRequest(locale_p, request_p, getFormElementID());
        }
        catch (Exception e)
        {
            m_strError = e.getLocalizedMessage();
        }

        return true;
    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
     */
    protected Object getValueFromRequest(java.util.Locale locale_p, HttpServletRequest request_p, String strID_p) throws Exception
    {
        return request_p.getParameter(strID_p);
    }

    /** gets a error message for this field, if update failed, clears the message automatically 
     *
     * @return String error message or an empty string
     */
    public String getSafePropertyError(java.util.Locale locale_p)
    {
        String strRet;

        if (m_strError == null)
        {
            strRet = "";
        }
        else
        {
            strRet = m_strError;
        }

        m_strError = null;

        return strRet;
    }

    /** get a unique ID for the HTML form element
     */
    protected String getFormElementID()
    {
        return String.valueOf(this.hashCode());
    }

    /** insert the property into a HTML form for editing
     *
     * 
     * @param w_p Writer to write HTML code to
     * @param iCols_p int number of columns of 0 to use a single input field
     * @param iRows_p int number of rows of 0 to use a single input field
     */
    public void render(Writer w_p, int iCols_p, int iRows_p) throws Exception
    {
        String strValue = "";
        if (null != getValue())
        {
            strValue = getValue().toString();
        }

        if ((iCols_p != 0) && (iRows_p != 0))
        {
            w_p.write("<textarea cols='" + String.valueOf(iCols_p) + "' rows='" + String.valueOf(iRows_p) + "' type='text' name='" + getFormElementID() + "' onkeydown='event.cancelBubble=true'>" + strValue + "</textarea>");
        }
        else
        {
            w_p.write("<input type='text' name='" + getFormElementID() + "' value='" + strValue + "'>");
        }
    }
}