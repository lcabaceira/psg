package com.wewebu.ow.server.plug.owbpm;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * OwBPMGroupBoxControl.
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
public class OwBPMGroupBoxControl extends OwFieldManagerControl
{
    private OwBPMWorkItemListView m_parent;

    public OwBPMGroupBoxControl(OwBPMWorkItemListView parent_p)
    {
        m_parent = parent_p;
    }

    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        try
        {
            OwHTMLHelper.writeSecureHTML(w_p, value_p.toString());
        }
        catch (NullPointerException e)
        {
            // ignore
        }
    }

    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        try
        {
            renderGroupboxes(w_p, m_parent.getCurrentWorkitemcontainer(), strID_p, field_p.getValue());
        }
        catch (Exception e)
        {

            w_p.write("<INPUT name='");
            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
            w_p.write("' type='text' value='");

            try
            {
                OwHTMLHelper.writeSecureHTML(w_p, field_p.getValue().toString());
            }
            catch (NullPointerException e1)
            {
                // ignore
            }

            w_p.write("'>");
        }
    }

    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        return request_p.getParameter(strID_p);
    }

    /** render additional options */
    public void renderGroupboxes(Writer w_p, OwWorkitemContainer container_p, String strID_p, Object value_p) throws Exception
    {
        Iterator it = container_p.getPublicReassignContainerNames().iterator();
        w_p.write("<script>\n");
        w_p.write("function onGroupBoxSelect() {\n");
        w_p.write(getFormEventFunction("GroupBoxSelect", "id=" + strID_p));
        w_p.write("\" name='");
        OwHTMLHelper.writeSecureHTML(w_p, strID_p);
        w_p.write(";\n");
        w_p.write("}\n");
        w_p.write("</script>\n");
        List items = new LinkedList();
        while (it.hasNext())
        {
            String sID = (String) it.next();
            String sDisplayName = container_p.getPublicReassignContainerDisplayName(sID);
            OwComboItem item = new OwDefaultComboItem(sID, sDisplayName);
            items.add(item);
        }
        OwComboModel model = new OwDefaultComboModel(false, false, value_p == null ? null : value_p.toString(), items);
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, strID_p, null, null, null);
        renderer.addEvent("onchange", "onGroupBoxSelect();");
        String selectGroupBoxDisplayName = getContext().localize("OwBPMGroupBoxControl.selectGroupBoxDisplayName", "Select group box");
        OwInsertLabelHelper.insertLabelValue(w_p, selectGroupBoxDisplayName, strID_p);
        renderer.renderCombo(w_p);
    }

    /** called when user changes the selection in the combobox
     * 
     * @param request_p
     * @throws Exception
     */
    public void onGroupBoxSelect(HttpServletRequest request_p) throws Exception
    {
        String sFormID = request_p.getParameter("id");

        OwField field = getFieldManager().getField(sFormID);

        field.setValue(request_p.getParameter(sFormID));

        // update the parent list
        m_parent.updateGroupBoxSelectFilter();
    }

}