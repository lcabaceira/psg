package com.wewebu.ow.server.fieldctrlimpl;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwUnresolvedReference;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Control to display a Image Property.
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
public class OwFieldManagerControlImage extends OwFieldManagerControl
{
    private static final String QUERY_KEY_ID = "id";

    /** Configuration sub node which is optional, containing link part to broken image which is shown for not resolvable references.
     * @since 4.1.0.0 */
    public static final String CONF_LINK = "BrokenImage";

    /**Link to broken image*/
    private String brokenLink;

    @Override
    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);
        OwXMLUtil util = new OwStandardXMLUtil(configNode_p);
        brokenLink = util.getSafeTextValue(CONF_LINK, "/images/doc_warning_48.png");
    }

    /** displays a image
     *
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object Value to be displayed
     */
    public void renderImage(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (fieldDef_p.getJavaClassName().equals("com.wewebu.ow.server.ecm.OwObjectReference") || fieldDef_p.getJavaClassName().equals("com.wewebu.ow.server.ecm.OwObject"))
        {
            // resolve object image reference
            if (null != value_p)
            {
                OwObjectReference object = (OwObjectReference) value_p;
                // the image property is another OwObject, we just need to know the upload URL in order to display it.
                StringBuilder strImageUploadURL = new StringBuilder(getContext().getBaseURL());
                String reason = null;
                if (object instanceof OwUnresolvedReference)
                {
                    strImageUploadURL.append(getBrokenLink());
                    reason = ((OwUnresolvedReference) object).getUnresolvedReason();
                    reason = OwHTMLHelper.encodeToSecureHTML(reason);
                }
                else
                {
                    strImageUploadURL.append("/getContent?");
                    strImageUploadURL.append(OwMimeManager.DMSID_KEY);
                    strImageUploadURL.append("=");
                    strImageUploadURL.append(OwAppContext.encodeURL(object.getDMSID()));
                }

                w_p.write("<img src=\"");
                w_p.write(strImageUploadURL.toString());
                w_p.write("\" alt=\"");
                if (reason != null)
                {
                    w_p.write(reason);
                }
                w_p.write("\" title=\"");
                if (reason != null)
                {
                    w_p.write(reason);
                }
                w_p.write("\" />");
            }
        }
        else
        {
            if (null != value_p)
            {
                // treat as string
                w_p.write("<img src=\"");
                w_p.write(getContext().getBaseURL());
                w_p.write("/demo/");
                w_p.write(value_p.toString());
                w_p.write("\" alt=\"\" title=\"\"/>");
            }
        }
    }

    /** Formats and displays the value attached to the PropertyClass in HTML
    * @param w_p Writer object to write HTML to
    * @param fieldDef_p OwFieldDefinition definition of field
    * @param value_p Object Value to be displayed
    */
    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        renderImage(w_p, fieldDef_p, value_p);
    }

    /** Formats and displays the value attached to the fieldClass in HTML for use in a HTML Form.
     * It also creates the necessary code to update the value in the form upon request.
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition
     * @param field_p OwField Value to be displayed
     * @param strID_p ID of the HTML element
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        renderImage(w_p, fieldDef_p, field_p.getValue());
        boolean renderDisable = true;
        String pasteClipboardObjectTooltip;

        // render image set link if clipboard content is available
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();

        if ((OwClipboard.CONTENT_TYPE_OW_OBJECT == clipboard.getContentType()) && (clipboard.getContent().size() == 1))
        {
            // we accept only image MIME types
            OwObject obj = ((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject();
            if (obj.getMIMEType().startsWith("image"))
            {
                pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.pasteimage", "Paste an image from the clipboard.");

                w_p.write("<a title=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\" href=\"");
                w_p.write(getFormEventURL("SetImage", QUERY_KEY_ID + "=" + strID_p));
                w_p.write("\">");

                w_p.write("<img style=\"vertical-align:top;border:0px none;margin:1px 2px;\" alt=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\" title=\"");
                w_p.write(pasteClipboardObjectTooltip);
                w_p.write("\"  src=\"");
                w_p.write(getContext().getDesignURL());
                w_p.write("/images/plug/owpaste/add_paste.png\">");

                w_p.write("</a>");

                renderDisable = false;
            }
        }

        if (renderDisable)
        {
            if (clipboard.getContent().size() > 1)
            {
                pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.howtopastimage.onlyone", "Please copy only one image to the clipboard. Then you can paste it using the paste icon.");
            }
            else
            {
                pasteClipboardObjectTooltip = getContext().localize("app.OwStandardFieldManager.howtopastimage", "Please copy an image to the clipboard first. Then you can paste it using the paste icon.");
            }
            w_p.write("<img style=\"vertical-align:top;border:0px none;margin:1px 2px;\" alt=\"");
            w_p.write(pasteClipboardObjectTooltip);
            w_p.write("\" title=\"");
            w_p.write(pasteClipboardObjectTooltip);
            w_p.write("\" src=\"");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/plug/owpaste/add_paste_disabled.png\">");
        }
    }

    /** called when user presses SetImage
     */
    public void onSetImage(HttpServletRequest request_p) throws Exception
    {
        OwClipboard clipboard = ((OwMainAppContext) getContext()).getClipboard();

        // get the field, the action was applied upon
        OwField field = getFieldManager().getField(request_p.getParameter(QUERY_KEY_ID));

        // Insert clipboard object
        field.setValue(((OwClipboardContentOwObject) clipboard.getContent().get(0)).getObject());
    }

    /** update the property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     * 
     * @param request_p  HttpServletRequest
     * @param fieldDef_p OwFieldDefinition
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     * 
     */
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        return value_p;
    }

    /**
     * Get the link part to broken image location.
     * @return String
     * @since 4.1.0.0
     */
    protected String getBrokenLink()
    {
        return this.brokenLink;
    }
}