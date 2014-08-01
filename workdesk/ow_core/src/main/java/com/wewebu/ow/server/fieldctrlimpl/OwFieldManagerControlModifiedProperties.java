package com.wewebu.ow.server.fieldctrlimpl;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.history.OwHistoryModifiedPropertyValue;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Control to display a array of OwHistoryModifiedPropertyValue.
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
public class OwFieldManagerControlModifiedProperties extends OwFieldManagerControl
{

    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if ((null != value_p) && (((Object[]) value_p).length > 0))
        {
            w_p.write("<table class='OwGeneralList_table' cellspacing='0' cellpadding='0' border='0'>");
            String title = getContext().localize("app.OwFieldManagerControlModifiedProperties.tableCaption", "Table of properties");
            w_p.write("<caption>" + title + "</caption>");
            // header
            w_p.write("<tr class='OwGeneralList_header'>");

            w_p.write("<th class='OwGeneralList_property'>");
            w_p.write(getContext().localize("app.OwFieldManagerControlModifiedProperties.propertycolumn", "Property"));
            w_p.write("</th>");

            w_p.write("<th class='OwGeneralList_property'>");
            w_p.write(getContext().localize("app.OwFieldManagerControlModifiedProperties.oldvaluecolumn", "Old Value"));
            w_p.write("</th>");

            w_p.write("<th class='OwGeneralList_property'>");
            w_p.write(getContext().localize("app.OwFieldManagerControlModifiedProperties.newvaluecolumn", "New Value"));
            w_p.write("</th>");

            w_p.write("</tr>");

            for (int i = 0; i < ((Object[]) value_p).length; i++)
            {
                OwHistoryModifiedPropertyValue propvalue = (OwHistoryModifiedPropertyValue) ((Object[]) value_p)[i];

                if ((i % 2) == 0)
                {
                    w_p.write("<tr class='OwGeneralList_RowEven'>");
                }
                else
                {
                    w_p.write("<tr class='OwGeneralList_RowOdd'>");
                }

                w_p.write("<td class='OwGeneralList_property'>");
                try
                {
                    OwHTMLHelper.writeSecureHTML(w_p, propvalue.getFieldDefinition().getDisplayName(this.getContext().getLocale()));
                }
                catch (OwObjectNotFoundException e)
                {
                    // just write the classname
                    OwHTMLHelper.writeSecureHTML(w_p, propvalue.getClassName());
                }
                w_p.write("</td>");

                w_p.write("<td class='OwGeneralList_property'>");
                try
                {
                    getFieldManager().insertReadOnlyField(w_p, propvalue.getOldValue());
                }
                catch (OwObjectNotFoundException e1)
                {
                    try
                    {
                        OwHTMLHelper.writeSecureHTML(w_p, propvalue.getOldValueString());
                    }
                    catch (OwObjectNotFoundException e2)
                    {
                        w_p.write("?");
                    }
                }
                w_p.write("</td>");

                w_p.write("<td class='OwGeneralList_property'>");
                try
                {
                    getFieldManager().insertReadOnlyField(w_p, propvalue.getNewValue());
                }
                catch (OwObjectNotFoundException e3)
                {
                    OwHTMLHelper.writeSecureHTML(w_p, propvalue.getNewValueString());
                }
                w_p.write("</td>");

                w_p.write("</tr>");
            }

            w_p.write("</table>");
        }
    }

    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        insertReadOnlyField(w_p, fieldDef_p, field_p.getValue());
    }

    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        return value_p;
    }

}