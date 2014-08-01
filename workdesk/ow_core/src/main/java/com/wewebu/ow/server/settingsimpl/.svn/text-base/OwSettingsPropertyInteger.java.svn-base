package com.wewebu.ow.server.settingsimpl;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

/**
 *<p>
 * Settings Property for Integer Values.
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
public class OwSettingsPropertyInteger extends OwSettingsPropertyBaseImpl
{
    /** overridable to create a default value for list properties
     *
     * @return Object with default value for a new list item
     */
    protected Object getDefaultListItemValue()
    {
        // default returns zero Integer
        return Integer.valueOf(0);
    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
     */
    protected Object getSingleValueFromRequest(HttpServletRequest request_p, String strID_p)
    {
        return Integer.valueOf(request_p.getParameter(strID_p));
    }

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return Integer.valueOf(((Integer) oSingleValue_p).intValue());
    }

    /** overridable to create a single value for the given node
     * @return Object with value
     */
    protected Object getSingleValue(Node valueNode_p)
    {
        // default implementation returns string from XML Text node child
        if (valueNode_p.getFirstChild() == null)
        {
            return null;
        }
        else
        {
            return Integer.valueOf(valueNode_p.getFirstChild().getNodeValue());
        }
    }

}