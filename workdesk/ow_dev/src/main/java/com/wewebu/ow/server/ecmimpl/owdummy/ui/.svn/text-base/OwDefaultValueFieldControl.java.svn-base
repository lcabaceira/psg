package com.wewebu.ow.server.ecmimpl.owdummy.ui;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Default value button wrapper field control base class.
 * It wraps arbitrary field controls with a default value filter button.   
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
public class OwDefaultValueFieldControl extends OwFieldManagerControl
{

    private static final String VALUE_FIELD_CONTROL_ELEMENT = "ValueFieldControl";

    public static final String FIELD_ID_KEY = "fieldid";

    private static final String FIELD_CLASS_ATTRIBUTE = "fieldclass";

    /** the wrapped field control*/
    private OwFieldManagerControl m_valueField;

    /**
     * Wrapps the field using {@link #insertHTMLWrapperHead(Writer, String)} and {@link #insertHTMLWrapperTail(Writer, String)}
     * and delegates to the wrapped field in between.
     * 
     * @param w_p
     * @param fieldDef_p
     * @param field_p
     * @param strID_p
     * @throws Exception
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        insertHTMLWrapperHead(w_p, strID_p);
        m_valueField.insertEditField(w_p, fieldDef_p, field_p, strID_p);
        insertHTMLWrapperTail(w_p, strID_p);
    }

    /**
     * Inserts HTML wrapping conde before the wrapped field control. 
     * @param w_p
     * @param strID_p
     * @throws Exception
     */
    protected void insertHTMLWrapperHead(Writer w_p, String strID_p) throws Exception
    {
        w_p.write("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>");
    }

    /**
     * (overridable)
     * Inserts HTML wrapping conde after the wrapped field control.
     * @param w_p
     * @param strID_p
     * @throws Exception
     */
    protected void insertHTMLWrapperTail(Writer w_p, String strID_p) throws Exception
    {
        w_p.write("</td>");
        w_p.write("<td>");
        insertDefaultControl(w_p, strID_p);
        w_p.write("</td>");
        w_p.write("</tr></table>");
    }

    /**
     * (overridable)
     * @return the default button label. <br>
     *         The default implementation returns the value of the 
     *         localize label obtained by concatenating "default.button." with the
     *         name of this concrete class.
     */
    protected String getDefaultButtonLabel()
    {
        Class thisClass = getClass();
        String className = thisClass.getName();
        int lastDotIndex = className.lastIndexOf(".");
        if (lastDotIndex < 0)
        {
            lastDotIndex = 0;
        }
        else
        {
            lastDotIndex = lastDotIndex + 1;
        }
        className = className.substring(lastDotIndex);

        return getContext().localize("default.button." + className, "DEFAULT");
    }

    /**
     * (overridable)
     * Inserts the default button code.
     * @param w_p
     * @param strID_p
     * @throws Exception
     */
    protected void insertDefaultControl(Writer w_p, String strID_p) throws Exception
    {
        w_p.write("<input type='button' value='");
        w_p.write(getDefaultButtonLabel());
        w_p.write("' name='");
        w_p.write(getDefaultButtonLabel());
        w_p.write("' onclick=\"");
        w_p.write(getFormEventFunction("SetDefaultValue", FIELD_ID_KEY + "=" + strID_p));
        w_p.write("\");\">");
    }

    /**
     * Default value seting event hadler. 
     * @param request_p
     * @throws Exception
     */
    public void onSetDefaultValue(HttpServletRequest request_p) throws Exception
    {
        String id = request_p.getParameter(FIELD_ID_KEY);
        OwFieldManager fieldManager = getFieldManager();
        OwField field = fieldManager.getField(id);
        Object defaultValue = createDefaultValue(field, id);
        field.setValue(defaultValue);
        fieldManager.clearFieldError(field);
    }

    /**
     * 
     * @param field_p
     * @param id_p
     * @return the default value that will be set it the wrapped control's field 
     * @throws Exception
     */
    protected Object createDefaultValue(OwField field_p, String id_p) throws Exception
    {
        return null;
    }

    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        m_valueField.insertReadOnlyField(w_p, fieldDef_p, value_p);
    }

    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        return m_valueField.updateField(request_p, fieldDef_p, value_p, strID_p);
    }

    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);

        OwXMLUtil configUtil = new OwStandardXMLUtil(configNode_p);
        OwXMLUtil valueFieldConfig = configUtil.getSubUtil(VALUE_FIELD_CONTROL_ELEMENT);
        String valueFieldClassName = valueFieldConfig.getSafeStringAttributeValue(FIELD_CLASS_ATTRIBUTE, "");
        Class valueFieldClass = Class.forName(valueFieldClassName);

        m_valueField = (OwFieldManagerControl) valueFieldClass.newInstance();
        m_valueField.init(fieldmanager_p, valueFieldConfig.getNode());
    }

    public void attach(OwAppContext context_p, String strName_p) throws Exception
    {
        super.attach(context_p, strName_p);
        m_valueField.attach(getContext(), null);
    }

    public void detach()
    {
        m_valueField.detach();
        super.detach();
    }
}
