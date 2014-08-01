package com.wewebu.ow.server.plug.owbpm;

import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 *  Field Control for displaying and selecting a User or Group.
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
 *
 *@since 4.2.0.0
 */
public abstract class OwBPMUserInfoControl extends OwFieldManagerControl implements OwDialog.OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMUserInfoControl.class);

    /** query string for the selected array item */
    protected static final String ARRAY_ITEM_INDEX_KEY = "a_item";

    /** query string for the selected field */
    protected static final String FIELD_ID_KEY = "field_id";

    /**
     * specialized user select dialog
     */
    public class OwParticipantUserSelectDialog extends OwUserSelectDialog
    {

        private OwField m_field;

        public OwParticipantUserSelectDialog(int[] filterType_p, boolean fMultiselect_p, OwField field_p)
        {
            super(filterType_p, fMultiselect_p);
            m_field = field_p;
        }

    }

    private String getDisplayName(Object value_p) throws Exception
    {
        if (null != value_p)
        {
            if (OwUserInfo.class.isAssignableFrom(value_p.getClass()))
            {
                return ((OwUserInfo) value_p).getUserDisplayName();
            }
            else
            {
                return value_p.toString();
            }
        }
        else
        {
            return "";
        }
    }

    /**
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertReadOnlyField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object)
     */
    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (fieldDef_p.isArray())
        {
            // === display array of values
            w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

            Object[] values = (Object[]) value_p;
            if (values != null)
            {

                for (int i = 0; i < values.length; i++)
                {
                    Object value = values[i];

                    w_p.write("<tr><td valign='center'>");
                    if (value != null)
                    {
                        OwHTMLHelper.writeSecureHTML(w_p, getDisplayName(value));
                    }
                    w_p.write("</td>");
                    w_p.write("</tr>");
                }
            }

            w_p.write("</table>");
        }
        else
        {
            // === display array of values
            Object value = value_p;
            if (value != null)
            {
                w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

                w_p.write("<tr><td valign='center'>");
                OwHTMLHelper.writeSecureHTML(w_p, getDisplayName(value));
                w_p.write("</td></tr>");

                w_p.write("</table>");
            }
        }
    }

    /**
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String)
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        String btnSelectTitle = btnSelectTitle(fieldDef_p);

        if (fieldDef_p.isArray())
        {
            // === display array of values
            w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

            Object[] values = (Object[]) field_p.getValue();
            if (values != null)
            {

                for (int i = 0; i < values.length; i++)
                {
                    Object value = values[i];

                    w_p.write("<tr><td valign='center'>");

                    if (value != null)
                    {
                        String id = strID_p + "_" + Integer.toString(i + 1);
                        String userDisplayName = getContext().localize1("OwBPMUserInfoControl.userDisplayName", "User %1", Integer.toString(i + 1));
                        OwInsertLabelHelper.insertLabelValue(w_p, userDisplayName, id);
                        w_p.write("<input class='OwInputControl' name='");
                        OwHTMLHelper.writeSecureHTML(w_p, id);
                        w_p.write("' id='");
                        OwHTMLHelper.writeSecureHTML(w_p, id);
                        w_p.write("' type='text' value='");
                        OwHTMLHelper.writeSecureHTML(w_p, getDisplayName(value));
                        w_p.write("' readonly/>");
                    }
                    w_p.write("</td>");

                    // delete icon
                    String lblDeleteElement = getContext().localize("app.OwStandardFieldManager.delarrayitem", "Delete Element");

                    w_p.write("<td><a title='");
                    w_p.write(lblDeleteElement);
                    w_p.write("' href=\"");
                    w_p.write(getFormEventURL("DeleteArrayItem", ARRAY_ITEM_INDEX_KEY + "=" + String.valueOf(i) + "&" + FIELD_ID_KEY + "=" + strID_p));
                    w_p.write("\"><img src='");
                    w_p.write(getContext().getDesignURL());
                    w_p.write("/images/deletebtn.png'");
                    w_p.write(" alt='" + lblDeleteElement + "' title='" + lblDeleteElement + "'");
                    w_p.write("/>");
                    w_p.write("</a>");
                    w_p.write("</td>");

                    w_p.write("</tr>");
                }
            }

            // select assignee/assignees
            StringBuffer addEventURL = new StringBuffer("javascript:document.");
            addEventURL.append(getFieldManager().getFormName());
            addEventURL.append(".action = '");
            addEventURL.append(getEventURL("OpenUserDialog", FIELD_ID_KEY + "=" + strID_p));
            addEventURL.append("';document.");
            addEventURL.append(getFieldManager().getFormName());
            addEventURL.append(".submit()");

            w_p.write("<tr><td valign='center'>");

            w_p.write("<a title='");
            w_p.write(btnSelectTitle);
            w_p.write("' href=\"");
            w_p.write(addEventURL.toString());
            w_p.write("\"><img  style='vertical-align:middle;border:0px none;' src='");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/user.png' ");
            w_p.write("alt='" + btnSelectTitle + "' title='" + btnSelectTitle + "' ");
            w_p.write("/></a>");

            w_p.write("</td>");
            w_p.write("</tr>");

            w_p.write("</table>");
            //should never happen
            //            String msg = "OwBPMUserInfoControl.insertEditField: Participant (Workgroup) should not be an array.";
            //            LOG.fatal(msg);
            //            throw new OwConfigurationException(msg);
        }
        else
        {
            // === display array of values
            Object value = field_p.getValue();
            w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

            w_p.write("<tr><td valign='center'>");

            if (value != null)
            {
                w_p.write("<input class='OwInputControl' name='");
                OwHTMLHelper.writeSecureHTML(w_p, strID_p);
                w_p.write("' id='");
                OwHTMLHelper.writeSecureHTML(w_p, strID_p);
                w_p.write("' type='text' value='");
                OwHTMLHelper.writeSecureHTML(w_p, getDisplayName(value));
                w_p.write("' readonly/>");
            }

            // select person
            StringBuffer addEventURL = new StringBuffer("javascript:document.");
            addEventURL.append(getFieldManager().getFormName());
            addEventURL.append(".action = '");
            addEventURL.append(getEventURL("OpenUserDialog", FIELD_ID_KEY + "=" + strID_p));
            addEventURL.append("';document.");
            addEventURL.append(getFieldManager().getFormName());
            addEventURL.append(".submit()");

            w_p.write("<a title='");
            w_p.write(btnSelectTitle);
            w_p.write("' href=\"");
            w_p.write(addEventURL.toString());
            w_p.write("\"><img style=\"vertical-align:middle;border:0px none;\" src='");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/user.png' ");
            w_p.write("alt='" + btnSelectTitle + "' title='" + btnSelectTitle + "' ");
            w_p.write("/></a>");

            w_p.write("</td>");
            w_p.write("</tr>");
            w_p.write("</table>");
        }
    }

    /**
     * @param fieldDef 
     * @return the title to use for the select button
     * @throws Exception 
     */
    protected abstract String btnSelectTitle(OwFieldDefinition fieldDef) throws Exception;

    /**
     * called when user clicks to delete an array item
     * @param request_p
     * @throws Exception
     */
    public void onDeleteArrayItem(HttpServletRequest request_p) throws Exception
    {
        OwField field = getFieldManager().getField(request_p.getParameter(FIELD_ID_KEY));
        OwFieldDefinition fieldDefinition = field.getFieldDefinition();
        if (fieldDefinition.isArray())
        {
            // === copy the values into a new array without the selected item
            int iIndex = Integer.parseInt(request_p.getParameter(ARRAY_ITEM_INDEX_KEY));

            Object[] values = (Object[]) field.getValue();

            if (values.length < 2)
            {
                // set new value
                field.setValue(null);
            }
            else
            {

                Object[] newValues = new Object[values.length - 1];

                int iNew = 0;
                int iOrigin = 0;
                do
                {
                    if (iIndex == iOrigin)
                    {
                        iOrigin++;
                    }

                    newValues[iNew++] = values[iOrigin++];
                } while (iNew < newValues.length);

                // set new value
                field.setValue(newValues);
            }
        }
        getFieldManager().resetErrors();
    }

    /**
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#updateField(javax.servlet.http.HttpServletRequest, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object, java.lang.String)
     */
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        return value_p;
    }

    /**
     * called from JSP to get role from role dialogue
     *
     * @param request_p
     *            a HttpServletRequest object
     */
    public void onOpenUserDialog(HttpServletRequest request_p) throws Exception
    {
        // get the current participants
        OwField field = getFieldManager().getField(request_p.getParameter(FIELD_ID_KEY));

        // open the user select dialog
        boolean multiselect = field.getFieldDefinition().isArray();
        OwParticipantUserSelectDialog userSelectDlg = new OwParticipantUserSelectDialog(filterTypes(), multiselect, field);
        getContext().openDialog(userSelectDlg, this);

    }

    protected int[] filterTypes()
    {
        return new int[] { OwUIUserSelectModul.TYPE_USER, OwUIUserSelectModul.TYPE_GROUP };
    }

    /**
     * @see com.wewebu.ow.server.ui.OwDialog.OwDialogListener#onDialogClose(com.wewebu.ow.server.ui.OwDialog)
     */
    @SuppressWarnings("rawtypes")
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        OwParticipantUserSelectDialog dialog = (OwParticipantUserSelectDialog) dialogView_p;

        List<Object> selectedValues = new LinkedList<Object>();

        List users = dialog.getSelectedUsers();
        if (null != users && !users.isEmpty())
        {
            for (Object aUser : users)
            {
                //this processing is futile for now, but maybe sometime we'll do some transformations on the elements before adding them to the selectedValues list.
                OwUserInfo userinfo = (OwUserInfo) aUser;
                selectedValues.add(userinfo);

            }
        }
        else
        {
            // now handle the groups/roles
            List roles = dialog.getSelectedRoles();
            if (null != roles && !roles.isEmpty())
            {
                for (Object aRole : roles)
                {
                    //this processing is futile for now, but maybe sometime we'll do some transformations on the elements before adding them to the selectedValues list.
                    String roleGroupName = (String) aRole;
                    selectedValues.add(roleGroupName);
                }
            }
        }

        // if there is a new selection, add it.
        if (!selectedValues.isEmpty())
        {
            OwField personField = dialog.m_field;
            OwFieldDefinition fieldDefinition = personField.getFieldDefinition();

            if (!fieldDefinition.isArray())
            {
                // set new single value
                LOG.info("OwBPMUserInfoControl.onDialogClose(): seting new value = " + selectedValues);
                personField.setValue(selectedValues.get(0));
            }
            else
            {
                Object[] oldValues = (Object[]) personField.getValue();

                // === copy the values into a new array and add one item
                List<Object> newValues = new LinkedList<Object>();
                if (null != oldValues)
                {
                    newValues.addAll(Arrays.asList(oldValues));
                }

                // add selected items too
                for (Object selected : selectedValues)
                {
                    if (!newValues.contains(selected))
                    {
                        newValues.add(selected);
                    }
                }
                personField.setValue(newValues.toArray());
            }
        }
        getFieldManager().resetErrors();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // ignore, used for update events from documents
    }

    @Override
    public void insertLabel(Writer w_p, String suffix_p, OwField field, String strID_p, boolean writeLabel_p) throws Exception
    {
        OwFieldDefinition fieldDef_p = field.getFieldDefinition();
        if (!fieldDef_p.isArray() && field.getValue() != null)
        {
            String displayName = fieldDef_p.getDisplayName(getContext().getLocale());
            OwInsertLabelHelper.insertLabelWithSuffix(w_p, displayName, strID_p, suffix_p, null);
        }
        else
        {
            w_p.write("<span>");
            w_p.write(fieldDef_p.getDisplayName(getContext().getLocale()));
            if (suffix_p != null)
            {
                w_p.write(suffix_p);
            }
            w_p.write("</span>");
        }
    }
}
