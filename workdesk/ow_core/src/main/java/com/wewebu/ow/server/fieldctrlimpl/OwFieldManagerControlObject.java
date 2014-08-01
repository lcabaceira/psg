package com.wewebu.ow.server.fieldctrlimpl;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardFieldManager;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwUnresolvedReference;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Field Control to display properties of type OwObject.
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
public class OwFieldManagerControlObject extends OwFieldManagerControl
{
    /** query key for the attachment DMSID used in renderAttachment */
    protected static final String ATTACHMENT_DMS_ID = "dmsid";

    /** query key for the attachment plugin index used in renderAttachment */
    protected static final String PLUG_INDEX_KEY = "pi";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwFieldManagerControlObject.class);

    /** formats and displays the value attached to the PropertyClass in HTML
    * @param w_p Writer object to write HTML to
    * @param fieldDef_p OwFieldDefinition definition of field
    * @param value_p Object Value to be displayed
    */
    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        // render document plugin icons for the attachment
        if (value_p != null)
        {
            if (fieldDef_p.isArray())
            {
                // === array values
                Object[] values = (Object[]) value_p;

                w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

                for (int i = 0; i < values.length; i++)
                {
                    if (values[i] != null)
                    {
                        w_p.write("<tr><td valign='center'>");

                        try
                        {
                            renderObjectReferenceLinkInternal(w_p, fieldDef_p, (OwObjectReference) values[i], null, false);
                        }
                        catch (ClassCastException cce)
                        {
                            LOG.error("Bad object for OwFieldManagerControlObject", cce);
                            w_p.write("Bad object!<br>");
                            w_p.write("The OwFieldManagerControlObject can only handle objects of type OwObjectReference,<br>");
                            w_p.write("but no objects of type ");
                            w_p.write(values[i].getClass().getName());
                            w_p.write(".");
                        }

                        w_p.write("</td></tr>");

                        if ((i + 1) < values.length)
                        {
                            // delimiter
                            w_p.write("<tr><td class='OwObjectListDelimiter'></td></tr>");
                        }
                    }
                }

                w_p.write("</table>");
            }
            else
            {
                // === single value
                try
                {
                    renderObjectReferenceLinkInternal(w_p, fieldDef_p, (OwObjectReference) value_p, null, false);
                }
                catch (ClassCastException cce)
                {
                    LOG.error("Bad object for OwFieldManagerControlObject", cce);
                    w_p.write("Bad object!<br>");
                    w_p.write("The OwFieldManagerControlObject can only handle objects of type OwObjectReference,<br>");
                    w_p.write("but no objects of type ");
                    w_p.write(value_p.getClass().getName());
                    w_p.write(".");
                }
            }
        }
    }

    /** insert a link for OwObject values
    *
    * @param w_p Writer object
    * @param fieldDef_p OwFieldDefinition
    * @param obj_p OwObject to render
    * @param strID_p String field ID
    * @param fEdit_p boolean true = edit field, false = read only field
    */
    protected void renderObjectReferenceLinkInternal(Writer w_p, OwFieldDefinition fieldDef_p, OwObjectReference obj_p, String strID_p, boolean fEdit_p) throws Exception
    {
        if (obj_p == null)
        {
            renderObjectReferenceLink(w_p, fieldDef_p, obj_p, strID_p, fEdit_p);
            return;
        }
        try
        {
            // if unresolved, print info for user
            String sUnresolvedReason = ((OwUnresolvedReference) obj_p).getUnresolvedReason();
            w_p.write("<span class=\"OwPropertyError\">");
            w_p.write((sUnresolvedReason == null) ? "" : sUnresolvedReason);
            w_p.write("</span>");
        }
        catch (ClassCastException e)
        {
            // render object
            renderObjectReferenceLink(w_p, fieldDef_p, obj_p, strID_p, fEdit_p);
        }
    }

    /** insert a link for OwObject values
     *
     * @param w_p Writer object
     * @param fieldDef_p OwFieldDefinition
     * @param obj_p OwObject to render
     * @param strID_p String field ID
     * @param fEdit_p boolean true = edit field, false = read only field
     */
    protected void renderObjectReferenceLink(Writer w_p, OwFieldDefinition fieldDef_p, OwObjectReference obj_p, String strID_p, boolean fEdit_p) throws Exception
    {
        if (obj_p != null)
        {
            w_p.write("<table border='0' cellspacing='0' cellpadding='0' width='100%'><tr>");
            w_p.write("<td class='OwWorkItemListViewMimeItem' width='10%' nowrap>");

            // === render Object reference, insert a link open the item
            getFieldManager().getMimeManager().insertIconLink(w_p, obj_p);
            w_p.write("&nbsp;");
            getFieldManager().getMimeManager().insertTextLink(w_p, obj_p.getName(), obj_p);

            // === render delimiter
            w_p.write("</td><td>&nbsp;</td>");

            // === render paste button if something is in the clipboard
            if (fEdit_p)
            {
                w_p.write("<td class='OwWorkItemListViewMimeItem'>");
                // fieldDef_p - OwFieldDefinition, used to identify if single or array value, since 4.1.1.0
                renderPasteLink(w_p, "PasteObjectReference", strID_p, fieldDef_p);
                w_p.write("</td>");
            }

            w_p.write("</tr></table>");
        }
        else
        {
            // === render paste button if something is in the clipboard
            if (fEdit_p)
            {
                // fieldDef_p - OwFieldDefinition, used to identify if single or array value, since 4.1.1.0
                renderPasteLink(w_p, "PasteObjectReference", strID_p, fieldDef_p);
            }
        }
    }

    /** render the clipboard paste link
     *
     * @param w_p Writer
     * @param strID_p ID of item
     * @param fieldDef_p - OwFieldDefinition, used to identify if single or array value, since 4.1.1.0
     *
     * @throws Exception
     */
    protected void renderPasteObjectLink(Writer w_p, String strID_p, OwFieldDefinition fieldDef_p) throws Exception
    {
        renderPasteLink(w_p, "PasteObjectReference", strID_p, fieldDef_p);
    }

    /** render the clipboard paste link
     *
     * @param w_p Writer
     * @param callbackfunction_p String name of the callback handler
     * @param strID_p ID of item
     * @param fieldDef_p - OwFieldDefinition, used to identify if single or array value, since 4.1.1.0
     *
     * @throws Exception
     */
    private void renderPasteLink(Writer w_p, String callbackfunction_p, String strID_p, OwFieldDefinition fieldDef_p) throws Exception
    {
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();
        String pasteClipboardObjectTooltip;

        if ((clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT) && (fieldDef_p.isArray() || (!fieldDef_p.isArray() && (clipboard.getContent().size() == 1))))
        {
            pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.pasteobject", "Paste an object from the clipboard.");

            w_p.write("<a class=\"OwMimeItem\" title=\"");
            w_p.write(pasteClipboardObjectTooltip);
            w_p.write("\" href=\"");
            w_p.write(getFormEventURL(callbackfunction_p, new StringBuilder(OwStandardFieldManager.FIELD_ID_KEY).append("=").append(strID_p).toString(), true));
            w_p.write("\">");

            w_p.write("<img style=\"vertical-align:top;border:0px none;margin:1px 2px;\" alt=\"");
            w_p.write(pasteClipboardObjectTooltip);
            w_p.write("\" title=\"");
            w_p.write(pasteClipboardObjectTooltip);
            w_p.write("\"  src=\"");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/plug/owpaste/add_paste.png\">");

            w_p.write("</a>");
        }
        else
        {
            if (!fieldDef_p.isArray() && (clipboard.getContent().size() > 1))
            {
                pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.howtopasteobject.onlyone", "Please copy only one object to the clipboard first. Then you can paste it using the paste icon.");
            }
            else
            {
                pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.howtopasteobject", "Please copy an object to the clipboard first. Then you can paste it using the paste icon.");
            }

            w_p.write("<img style=\"vertical-align:top;border:0px none;margin:1px 2px;\" alt=\"");
            w_p.write(pasteClipboardObjectTooltip);
            w_p.write("\" title=\"");
            w_p.write(pasteClipboardObjectTooltip);
            w_p.write("\"  src=\"");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/plug/owpaste/add_paste_disabled.png\">");
        }
    }

    /** called when user presses SetImage
     */
    public void onPasteObjectReference(HttpServletRequest request_p) throws Exception
    {
        // get the field, the action was applied upon
        OwField field = getFieldManager().getField(request_p.getParameter(OwStandardFieldManager.FIELD_ID_KEY));

        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();
        if ((clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT) && (clipboard.getContent().size() == 1))
        {
            // Insert clipboard object
            field.setValue(((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject());
            if (field.getFieldDefinition().isRequired())
            {// clear error in pre-validation
                getFieldManager().clearFieldError(field);
            }
        }
    }

    /** formates and displays the value attached to the fieldClass in HTML for use in a HTML Form.
     * It also creates the necessary code to update the value in the form upon request.
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition
     * @param field_p OwField Value to be displayed
     * @param strID_p ID of the HTML element
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        // render document plugin icons for the attachment
        if (fieldDef_p.isArray())
        {
            // === array of values
            if (field_p.getValue() != null)
            {
                Object[] values = (Object[]) field_p.getValue();
                if (values != null)
                {
                    w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");

                    for (int i = 0; i < values.length; i++)
                    {
                        if (values[i] != null)
                        {
                            w_p.write("<tr><td valign='center'>");

                            // === render without clipboard paste button for each element
                            try
                            {
                                renderObjectReferenceLinkInternal(w_p, fieldDef_p, (OwObjectReference) values[i], null, false);
                            }
                            catch (ClassCastException cce)
                            {
                                LOG.error("Bad object for OwFieldManagerControlObject", cce);
                                w_p.write("Bad object!<br>");
                                w_p.write("The OwFieldManagerControlObject can only handle objects of type OwObjectReference,<br>");
                                w_p.write("but no objects of type ");
                                w_p.write(values[i].getClass().getName());
                                w_p.write(".");
                            }

                            String tooltip = getContext().localize("app.OwFieldManagerControlObject.delarrayitem", "Delete Item");
                            w_p.write("</td><td>");
                            w_p.write("<a title=\"");
                            w_p.write(tooltip);
                            w_p.write("\" href=\"");
                            w_p.write(getFormEventURL("DeleteArrayItem", OwStandardFieldManager.ARRAY_ITEM_INDEX_KEY + "=" + String.valueOf(i) + "&" + OwStandardFieldManager.FIELD_ID_KEY + "=" + strID_p));
                            w_p.write("\"><img src=\"");
                            w_p.write(getContext().getDesignURL());
                            w_p.write("/images/deletebtn.png\"");
                            String deleteTooltip = tooltip;
                            String fieldDisplayName = fieldDef_p.getDisplayName(getContext().getLocale());
                            deleteTooltip = (getContext().localize2("app.OwStandardFieldManager.delarrayitemindexed", "Delete element at position %1 from %2", "" + (i + 1), fieldDisplayName));
                            w_p.write(" alt=\"");
                            w_p.write(deleteTooltip);
                            w_p.write("\" title=\"");
                            w_p.write(deleteTooltip);
                            w_p.write("\" /></a></td></tr>");

                            if ((i + 1) < values.length)
                            {
                                // delimiter
                                w_p.write("<tr><td class=\"OwObjectListDelimiter\"></td></tr>");
                            }
                        }
                    }

                    w_p.write("</table>");
                }
            }

            renderPasteLink(w_p, "PasteObjectArrayItem", strID_p, fieldDef_p);
        }
        else
        {
            // === single value
            try
            {
                renderObjectReferenceLinkInternal(w_p, fieldDef_p, (OwObjectReference) field_p.getValue(), strID_p, true);
            }
            catch (ClassCastException cce)
            {
                LOG.error("Bad object for OwFieldManagerControlObject", cce);
                w_p.write("Bad object!<br>");
                w_p.write("The OwFieldManagerControlObject can only handle objects of type OwObjectReference,<br>");
                w_p.write("but no objects of type ");
                w_p.write(field_p.getValue().getClass().getName());
                w_p.write(".");
            }
        }
    }

    /** called when user clicks to delete an array item */
    public void onDeleteArrayItem(HttpServletRequest request_p) throws Exception
    {
        // === copy the values into a new array without the selected item
        int iIndex = Integer.parseInt(request_p.getParameter(OwStandardFieldManager.ARRAY_ITEM_INDEX_KEY));

        OwField field = getFieldManager().getField(request_p.getParameter(OwStandardFieldManager.FIELD_ID_KEY));

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

    /** called when user clicks to paste an object array item from clipboard */
    public void onPasteObjectArrayItem(HttpServletRequest request_p) throws Exception
    {
        // render paste button if something is in the clipboard
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();

        if (clipboard.getContentType() != OwClipboard.CONTENT_TYPE_OW_OBJECT)
        {
            return;
        }

        // === copy the values into a new array and add one item
        OwField field = getFieldManager().getField(request_p.getParameter(OwStandardFieldManager.FIELD_ID_KEY));

        // === scalar type
        Object[] values = (Object[]) field.getValue();

        Object[] newValues = null;

        if (null == values)
        {
            // === no previous values
            newValues = new Object[clipboard.getContent().size()];
        }
        else
        {
            newValues = new Object[values.length + clipboard.getContent().size()];

            // copy values
            for (int i = 0; i < values.length; i++)
            {
                newValues[i] = values[i];
            }
        }

        // add an item from clipboard
        for (int i = 0; i < clipboard.getContent().size(); i++)
        {
            newValues[newValues.length - clipboard.getContent().size() + i] = ((OwClipboardContentOwObject) clipboard.getContent().get(i)).getObject();
        }

        // set new value
        field.setValue(newValues);
    }

    /** update the property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     *
     * @param request_p  HttpServletRequest
     * @param fieldDef_p OwFieldDefinition
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     */
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        return value_p;
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