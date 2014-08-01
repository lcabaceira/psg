package com.alfresco.ow.contractmanagement.fieldmanager;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwFieldManagerException;
import com.wewebu.ow.server.app.OwStandardFieldManager;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * OwContractManagementFieldManager.
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
public class OwContractManagementFieldManager extends OwStandardFieldManager
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwContractManagementFieldManager.class);

    private OwFieldManagerControl getFieldManagerControl(OwFieldDefinition fieldDef_p)
    {
        OwFieldManagerControl fieldcontrol = null;

        if (fieldDef_p instanceof OwContractManagementFieldDefinition && ((OwContractManagementFieldDefinition) fieldDef_p).getSubClassName() != null)
        {
            fieldcontrol = m_FieldCtrlMap.get(((OwContractManagementFieldDefinition) fieldDef_p).getSubClassName());
        }
        if (fieldcontrol == null)
        {
            fieldcontrol = m_FieldCtrlMap.get(fieldDef_p.getClassName());
        }
        if (fieldcontrol == null)
        {
            fieldcontrol = m_FieldCtrlMap.get(fieldDef_p.getJavaClassName());
        }

        return fieldcontrol;
    }

    @Override
    protected void insertReadOnlyFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {

        OwFieldManagerControl fieldcontrol = getFieldManagerControl(fieldDef_p);

        if (fieldcontrol != null)
        {
            // === property is rendered in field control
            try
            {
                fieldcontrol.insertReadOnlyField(w_p, fieldDef_p, value_p);
            }
            catch (Throwable t)
            {
                LOG.error("The fieldcontrol failed to execute insertReadOnlyField.", t);
                w_p.write("The fieldontrol ");
                w_p.write(fieldcontrol.getClass().getName());
                w_p.write(" failed to execute with a ");
                w_p.write(t.getClass().getName());
                w_p.write(".<br>");
                w_p.write("Error message: ");
                w_p.write(t.toString());
                w_p.write("<br>");
                StackTraceElement[] trace = t.getStackTrace();
                for (int i = 0; i < Math.min(trace.length, 20); i++)
                {
                    w_p.write(trace[i].toString() + "<br>");
                }
            }
        }
        else if (value_p != null)
        {
            if (fieldDef_p.isArray())
            {
                // === array values
                Object[] values = (Object[]) value_p;
                boolean fDelimiter = false;
                for (int i = 0; i < values.length; i++)
                {
                    // delimiter
                    if (fDelimiter)
                    {
                        w_p.write("<div style='clear:left;padding-top:2px'>");
                        w_p.write("<hr class='OwStandardFieldManager_Array'>");
                        w_p.write("</div>");

                    }

                    insertSingleReadOnlyFieldInternal(w_p, fieldDef_p, values[i]);

                    fDelimiter = true;
                }
            }
            else
            {
                // === single value
                insertSingleReadOnlyFieldInternal(w_p, fieldDef_p, value_p);
            }
        }
    }

    @Override
    protected void insertEditFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {

        OwFieldManagerControl fieldcontrol = getFieldManagerControl(fieldDef_p);
        if (fieldcontrol != null)
        {
            // === property is rendered in field control
            try
            {
                fieldcontrol.insertEditField(w_p, fieldDef_p, field_p, strID_p);
            }
            catch (Throwable t)
            {
                LOG.error("The fieldcontrol failed to execute insertEditField.", t);
                w_p.write("The fieldontrol ");
                w_p.write(fieldcontrol.getClass().getName());
                w_p.write(" failed to execute with a ");
                w_p.write(t.getClass().getName());
                w_p.write(".<br>");
                w_p.write("Error message: ");
                w_p.write(t.toString());
                w_p.write("<br>");
                StackTraceElement[] trace = t.getStackTrace();
                for (int i = 0; i < Math.min(trace.length, 20); i++)
                {
                    w_p.write(trace[i].toString() + "<br>");
                }
            }
        }
        else
        {
            // === all other values
            if (fieldDef_p.isArray())
            {
                OwFieldDefinition definition = field_p.getFieldDefinition();
                String arrayFieldDisplayName = definition.getDisplayName(getContext().getLocale());
                w_p.write("<fieldset id='");
                String fieldId = String.valueOf(field_p.hashCode());
                w_p.write(fieldId);
                w_p.write("' class='accessibility'>");
                w_p.write("<legend class='accessibility'>");
                w_p.write(arrayFieldDisplayName);
                w_p.write("</legend>");

                // === array of values
                Object[] values = (Object[]) field_p.getValue();
                if (values != null)
                {
                    w_p.write("<table>");

                    for (int i = 0; i < values.length; i++)
                    {
                        w_p.write("<tr class='");

                        w_p.write(i % 2 == 0 ? "OwGeneralList_RowEven" : "OwGeneralList_RowOdd");

                        w_p.write("'><td valign='center'>");

                        insertSingleEditFieldInternal(w_p, fieldDef_p, values[i], strID_p + COMPLEX_ID_DELIMITER + String.valueOf(i));

                        w_p.write("</td><td>");
                        /* For SearchView, a SearchCriteria which is a multi value field
                         * should display one input field by default. This Inputfield
                         * must be rendered without a delete button.
                         */
                        if (i == 0 && field_p instanceof com.wewebu.ow.server.field.OwSearchCriteria && values.length == 1)
                        {
                            w_p.write("</td></tr>");
                        }
                        else
                        {
                            writeRemoveMultiValueItemLink(w_p, fieldDef_p, i, strID_p);
                            w_p.write("</td></tr>");
                        }
                    }

                    w_p.write("</table>");
                }
                writeAddMultiValueItemLink(w_p, fieldDef_p, strID_p);

                w_p.write("</fieldset>");
            }
            else
            {
                // === single value
                insertSingleEditFieldInternal(w_p, fieldDef_p, field_p.getValue(), strID_p);
            }
        }
    }

    @Override
    protected Object updateFieldInternal(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {

        Object retObject = null;

        OwFieldManagerControl fieldcontrol = getFieldManagerControl(fieldDef_p);
        if (fieldcontrol != null)
        {
            // === property is updated in field control
            try
            {
                retObject = fieldcontrol.updateField(request_p, fieldDef_p, value_p, strID_p);
            }
            catch (Exception e)
            {
                LOG.error("The fieldcontrol failed to execute updateField.", e);
                throw e;
            }
        }
        else
        {
            if (fieldDef_p.isArray())
            {
                // === array values
                Object[] values = (Object[]) value_p;
                if (values != null)
                {
                    for (int i = 0; i < values.length; i++)
                    {
                        values[i] = updateSingleFieldInternal(request_p, fieldDef_p, values[i], new StringBuilder(strID_p).append(COMPLEX_ID_DELIMITER).append(Integer.toString(i)).toString());
                    }
                }

                retObject = value_p;
            }
            else
            {
                // === single value
                retObject = updateSingleFieldInternal(request_p, fieldDef_p, value_p, strID_p);
            }
        }

        //NULL array field item values are not allowed (but for a single null value in search forms )- ticket 2420
        if (fieldDef_p.isArray())
        {
            Object[] values = (Object[]) retObject;
            if (values != null)
            {
                OwField theField = getField(strID_p);
                String error = getSafeFieldError(theField);
                //Don't override already set errors - update errors
                if (error == null || error.length() == 0)
                {
                    //allow one null value in search forms
                    if (values.length > 1 || !(theField instanceof com.wewebu.ow.server.field.OwSearchCriteria && values.length == 1))
                    {
                        for (int i = 0; i < values.length; i++)
                        {
                            if (values[i] == null || values[i].toString().length() == 0)
                            {
                                String errorMessage = getContext().localize("app.OwStandardFieldManager.emptyarrayfielditem", "The input fields can not be empty!");
                                setFieldError(theField, errorMessage);
                            }
                        }
                    }
                }
            }
        }

        // check for required value
        if (fieldDef_p.isRequired() && ((retObject == null) || (retObject.toString().length() == 0)))
        {
            throw new OwFieldManagerException(getContext().localize("app.OwStandardFieldManager.requiredfield", "This field is mandatory and must have a value."));
        }

        return retObject;
    }

    @Override
    public void insertLabel(Writer w_p, boolean readOnlyView_p, boolean readOnly_p, OwField field_p, String suffix_p, boolean writeLabel_p) throws Exception
    {
        OwFieldDefinition fieldDefinition = field_p.getFieldDefinition();
        OwFieldManagerControl fieldcontrol = getFieldManagerControl(fieldDefinition);

        if (fieldcontrol != null)
        {
            fieldcontrol.insertLabel(w_p, suffix_p, field_p, String.valueOf(field_p.hashCode()), writeLabel_p);
        }
        else
        {
            if (readOnlyView_p || readOnly_p || fieldDefinition.isArray() || fieldDefinition.isComplex())
            {
                w_p.write(fieldDefinition.getDisplayName(getContext().getLocale()));
                if (suffix_p != null)
                {
                    w_p.write(suffix_p);
                }
            }
            else
            {
                w_p.write("<label for='");
                String fieldId = String.valueOf(field_p.hashCode());
                w_p.write(fieldId);
                w_p.write("'>");
                w_p.write(fieldDefinition.getDisplayName(getContext().getLocale()));
                if (suffix_p != null)
                {
                    w_p.write(suffix_p);
                }
                w_p.write("</label>");
            }
        }
    }
}
