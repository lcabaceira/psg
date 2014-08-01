package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;

/**
 *<p>
 * A single settings boolean property.
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
public class OwSettingsPropertyBoolean extends OwSettingsPropertyBaseImpl
{
    /** overridable to create a default value for list properties
     *
     * @return Object with default value for a new list item
     */
    protected Object getDefaultListItemValue()
    {
        // default returns empty string
        return Boolean.FALSE;
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
            return Boolean.valueOf(valueNode_p.getFirstChild().getNodeValue());
        }
    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
     */
    protected Object getSingleValueFromRequest(HttpServletRequest request_p, String strID_p)
    {
        return Boolean.valueOf(request_p.getParameter(strID_p));
    }

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return Boolean.valueOf((Boolean) oSingleValue_p);
    }

    /** overridable to insert a single value into a edit HTML form
     *
     * @param w_p Writer to write HTML code to
     * @param value_p the property value to edit
     * @param strID_p String the ID of the HTML element for use in onApply
    * @param iIndex_p int Index of item if it is a list
    */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        boolean fValue = false;
        if (value_p != null)
        {
            fValue = ((Boolean) value_p).booleanValue();
        }
        String[] values = { "" + true, "" + false };
        String[] displayValues = { getContext().localize("settingsimpl.OwSettingsPropertyBoolean.yes", "Yes"), getContext().localize("settingsimpl.OwSettingsPropertyBoolean.no", "No") };
        OwComboModel comboModel = new OwDefaultComboModel(false, false, "" + fValue, values, displayValues);
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(comboModel, strID_p, null, null, null);
        renderer.renderCombo(w_p);
    }
}