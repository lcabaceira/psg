package com.wewebu.ow.server.plug.owbpm;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 *  Field Control for displaying and selecting BPM participant.
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
public class OwBPMParticipantPropertyControl extends OwFieldManagerControl implements OwDialog.OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMParticipantPropertyControl.class);

    /** query string for the selected array item */
    protected static final String ARRAY_ITEM_INDEX_KEY = "a_item";

    /** query string for the selected field */
    protected static final String FIELD_ID_KEY = "field_id";

    /**
     * specialized user select dialog to hold participants
     */
    public class OwParticipantUserSelectDialog extends OwUserSelectDialog
    {

        private OwField m_participants;

        /**
         * constructor
         * @param filterType_p
         * @param fMultiselect_p
         */
        public OwParticipantUserSelectDialog(int[] filterType_p, boolean fMultiselect_p, OwField participants_p)
        {
            super(filterType_p, fMultiselect_p);
            m_participants = participants_p;
        }

    }

    /** overridable to get the display name for a given user object ID
     *
     * @param value_p
     * @return the display name as <code>String</code>
     */
    protected String getUserDisplayName(Object value_p)
    {
        if (null != value_p)
        {
            //            LOG.debug("OwBPMParticipantPropertyControl.getUserDisplayName(): class of value = " + value_p.getClass()+ " toString = " +value_p);
            return value_p.toString();
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
            Object[] values = (Object[]) value_p;
            if (values != null)
            {
                w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

                for (int i = 0; i < values.length; i++)
                {
                    w_p.write("<tr><td valign='center'>");
                    OwHTMLHelper.writeSecureHTML(w_p, getUserDisplayName(values[i]));
                    w_p.write("</td></tr>");
                }

                w_p.write("</table>");
            }
        }
        else
        {
            //should never happen
            String msg = "OwBPMParticipantPropertyControl.insertReadOnlyField: Participant (Workgroup) should always be an array.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
    }

    /**
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String)
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {

        if (fieldDef_p.isArray())
        {
            // === display array of values
            Object[] values = (Object[]) field_p.getValue();
            if (values != null)
            {
                w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

                for (int i = 0; i < values.length; i++)
                {
                    w_p.write("<tr><td valign='center'>");

                    w_p.write("<input class='OwInputControl' name='");
                    OwHTMLHelper.writeSecureHTML(w_p, strID_p);
                    w_p.write("' type='text' value='");
                    OwHTMLHelper.writeSecureHTML(w_p, getUserDisplayName(values[i]));
                    w_p.write("' readonly>");

                    // delete icon
                    w_p.write("</td><td><a title='");
                    w_p.write(getContext().localize("app.OwStandardFieldManager.delarrayitem", "Delete Element"));
                    w_p.write("' href=\"");
                    w_p.write(getFormEventURL("DeleteArrayItem", ARRAY_ITEM_INDEX_KEY + "=" + String.valueOf(i) + "&" + FIELD_ID_KEY + "=" + strID_p));
                    w_p.write("\"><img src='");
                    w_p.write(getContext().getDesignURL());
                    w_p.write("/images/deletebtn.png'");
                    String imageTitle = getContext().localize1("fncm.bpm.OwBPMParticipantPropertyControl.deleteParticipant", "Delete participant number %1.", "" + (i + 1));
                    w_p.write(" alt='" + imageTitle + "' title='" + imageTitle + "'");
                    w_p.write("/>");

                    w_p.write("</a></td></tr>");

                }

                w_p.write("</table>");
            }

            // add a new participant
            StringBuffer addEventURL = new StringBuffer("javascript:document.");
            addEventURL.append(getFieldManager().getFormName());
            addEventURL.append(".action = '");
            addEventURL.append(getEventURL("OpenUserDialog", FIELD_ID_KEY + "=" + strID_p));
            addEventURL.append("';document.");
            addEventURL.append(getFieldManager().getFormName());
            addEventURL.append(".submit()");

            w_p.write("<a title='");
            String addParticipantTitle = getContext().localize("fncm.bpm.OwBPMParticipantPropertyControl.addparticipant", "Add another participant.");
            w_p.write(addParticipantTitle);
            w_p.write("' href=\"");
            w_p.write(addEventURL.toString());
            w_p.write("\"><img style='vertical-align:middle;border:0px none;margin:5px 0px;' src='");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/addbtn.png' ");
            w_p.write("alt='" + addParticipantTitle + "' title='" + addParticipantTitle + "' ");
            w_p.write("/></a>");
        }
        else
        {
            //should never happen
            String msg = "OwBPMParticipantPropertyControl.insertEditField: Participant (Workgroup) should always be an array.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
    }

    /**
     * called when user clicks to delete an array item
     * @param request_p
     * @throws Exception
     */
    public void onDeleteArrayItem(HttpServletRequest request_p) throws Exception
    {
        // === copy the values into a new array without the selected item
        int iIndex = Integer.parseInt(request_p.getParameter(ARRAY_ITEM_INDEX_KEY));

        OwField field = getFieldManager().getField(request_p.getParameter(FIELD_ID_KEY));

        Object[] values = (Object[]) field.getValue();

        if (values.length < 2)
        {
            // set new value
            field.setValue(null);
            return;
        }

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

        // open the user select dialog to select an additional participant
        OwParticipantUserSelectDialog userSelectDlg = new OwParticipantUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_USER, OwUIUserSelectModul.TYPE_GROUP }, true, field);
        getContext().openDialog(userSelectDlg, this);

    }

    /**
     * @see com.wewebu.ow.server.ui.OwDialog.OwDialogListener#onDialogClose(com.wewebu.ow.server.ui.OwDialog)
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        OwParticipantUserSelectDialog dialog = (OwParticipantUserSelectDialog) dialogView_p;

        //		 get the new participant
        // at the moment we just support single selection.
        String newParticipant = null;
        List users = dialog.getSelectedUsers();
        if (null != users)
        {

            Iterator it = users.iterator();
            while (it.hasNext())
            {
                OwUserInfo userinfo = (OwUserInfo) it.next();
                /* bug 2231 Quickfix: use UserInfo.getUserLongName*/
                newParticipant = userinfo.getUserLongName();

            }

        }
        else
        // now handle the groups
        {
            List roles = dialog.getSelectedRoles();
            if (null != roles)
            {
                Iterator it = roles.iterator();
                while (it.hasNext())
                {
                    OwUserInfo userinfo = (OwUserInfo) it.next();
                    /* bug 2231 Quickfix: use UserInfo.getUserLongName*/
                    newParticipant = userinfo.getUserLongName();
                }
            }
        }

        // if a new participant has been selected, add it.
        if (newParticipant != null)
        {
            OwField participantsField = dialog.m_participants;
            Object[] participants = (Object[]) participantsField.getValue();

            // === copy the values into a new array and add one item
            Object[] newValues = null;
            if (null == participants)
            {
                // === no previous values
                newValues = new Object[1];
            }
            else
            {
                newValues = new Object[participants.length + 1];

                // copy values
                for (int i = 0; i < participants.length; i++)
                {
                    newValues[i] = participants[i];
                }
            }
            // add an item
            newValues[newValues.length - 1] = newParticipant;

            LOG.info("OwBPMParticipantPropertyControl.onDialogClose(): seting new value = " + newParticipant);
            //      set new value
            participantsField.setValue(newValues);

        }

    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // ignore, used for update events from documents
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertLabel(java.io.Writer, boolean, java.lang.String, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String, boolean)
     */
    @Override
    public void insertLabel(Writer w_p, String suffix_p, OwField field, String strID_p, boolean writeLabel_p) throws Exception
    {
        OwFieldDefinition fieldDef_p = field.getFieldDefinition();
        w_p.write("<span>");
        w_p.write(fieldDef_p.getDisplayName(getContext().getLocale()));
        if (suffix_p != null)
        {
            w_p.write(suffix_p);
        }
        w_p.write("</span>");
    }
}
