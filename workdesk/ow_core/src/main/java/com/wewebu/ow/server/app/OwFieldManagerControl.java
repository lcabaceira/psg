package com.wewebu.ow.server.app;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Base Class for field manager UI Controls.
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
public abstract class OwFieldManagerControl extends OwEventTarget
{
    /** reference to the field manager containing the field control */
    private OwFieldManager m_FieldManager;

    /** initialization of a field control to display fields
     * @param fieldmanager_p OwFieldManager reference to the field manager containing the field control
     * @param configNode_p DOM Node to the configuration XML for the control, or null to use defaults
     */
    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        // set field manager
        m_FieldManager = fieldmanager_p;
    }

    protected void init() throws Exception
    {
        // ignore
    }

    public OwFieldManager getFieldManager()
    {
        return m_FieldManager;
    }

    /** get the target, that is used for form date and renders form
     */
    public OwEventTarget getFormTarget()
    {
        return getFieldManager().getFormTarget();
    }

    @Override
    public String getFormName()
    {
        return getFieldManager().getFormName();
    }

    /**
     * Creates an URL the same way like the {@link #getFormEventFunction(String, String)},
     * but allow also to dis-/enable validation of form values.
     * @param eventName String 
     * @param additionalParameters String (can be null)
     * @param disableValidation boolean
     * @return String representation of form &quot;submit&quot; URL
     * @since 4.2.0.0
     */
    public String getFormEventURL(String eventName, String additionalParameters, boolean disableValidation)
    {
        StringBuilder extendedParams = additionalParameters == null ? new StringBuilder() : new StringBuilder(additionalParameters);
        if (disableValidation)
        {
            if (extendedParams.length() > 0)
            {
                extendedParams.append("&");
            }
            extendedParams.append(OwFieldManager.FLAG_DISABLE_VALIDATION).append("=").append("true");
        }
        return super.getFormEventURL(eventName, extendedParams.toString());
    }

    /**
     * Create a script function the same way like {@link #getFormEventFunction(String, String)},
     * but allow to control if form-validation is active or not.
     * @param eventName String 
     * @param additionalParameters String (can be null)
     * @param disableValidation boolean flag
     * @return String representing event function
     * @since 4.2.0.0
     */
    public String getFormEventFunction(String eventName, String additionalParameters, boolean disableValidation)
    {
        StringBuilder extendedParams = additionalParameters == null ? new StringBuilder() : new StringBuilder(additionalParameters);
        if (disableValidation)
        {
            if (extendedParams.length() > 0)
            {
                extendedParams.append("&");
            }
            extendedParams.append(OwFieldManager.FLAG_DISABLE_VALIDATION).append("=").append("true");
        }
        return super.getFormEventFunction(eventName, extendedParams.toString());
    }

    /** format and displays the value attached to the PropertyClass in HTML
    * @param w_p Writer object to write HTML to
    * @param fieldDef_p OwFieldDefinition definition of field
    * @param value_p Object Value to be displayed
    */
    public abstract void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception;

    /** format and displays the value attached to the fieldClass in HTML for use in a HTML Form.
     * It also creates the necessary code to update the value in the form upon request.
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition
     * @param field_p OwField Value to be displayed
     * @param strID_p ID of the HTML element
     */
    public abstract void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception;

    /** update the property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     * @param request_p  HttpServletRequest
     * @param fieldDef_p OwFieldDefinition
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     */
    public abstract Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception;

    /**
     * Renders a property control label on the given Writer.<br>
     * value type are considered at rendering time.
     * @param w_p
     * @param suffix_p
     * @param field
     * @param strID_p
     * @param writeLabel_p
     * @throws Exception
     * @since 3.2.0.0
     * @since 4.2.0.0signature changed to insertLabel(Writer, String, OwField, String, boolean)
     */
    public void insertLabel(Writer w_p, String suffix_p, OwField field, String strID_p, boolean writeLabel_p) throws Exception
    {
        if (writeLabel_p)
        {
            w_p.write("<label for=\"");
            w_p.write(strID_p);
            w_p.write("\">");
            OwFieldDefinition fieldDef_p = field.getFieldDefinition();
            w_p.write(fieldDef_p.getDisplayName(getContext().getLocale()));
            if (suffix_p != null)
            {
                w_p.write(suffix_p);
            }
            w_p.write("</label>");
        }
    }

}