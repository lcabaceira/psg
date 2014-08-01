package com.wewebu.ow.server.plug.owbpm;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUnresolvedReference;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.fieldctrlimpl.OwFieldManagerControlObject;

/**
 *<p>
 * Field Control to display Attachments in the BPM Workitems.
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
public class OwBPMAttachmentFieldControl extends OwFieldManagerControlObject
{
    /** general ID in the plugin ID to be configure the plugin IDs for attachments */
    public static final String PLUGIN_CONFIG_ID_ATTACHMENT_FUNCTION = "AttachmentDocumentFunctions";

    /** List with IDs of document function plugins */
    protected List m_documentFunctionPluginIds;

    /** creates a field control to display fields
     * @param documentFunctionPluginIds_p List with IDs of document function plugins
     */
    public OwBPMAttachmentFieldControl(List documentFunctionPluginIds_p) throws Exception
    {
        m_documentFunctionPluginIds = documentFunctionPluginIds_p;
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
        w_p.write("\n<table class=\"OwAttachmentEntry\"><tr>\n");
        w_p.write("<td class=\"OwWorkItemListViewMimeItem\" nowrap>");

        // === render Object reference, insert a link open the item
        if (obj_p != null)
        {
            try
            {
                // if unresolved, print info for user
                String sUnresolvedReason = ((OwUnresolvedReference) obj_p).getUnresolvedReason();
                w_p.write("<span class=\"OwPropertyError\">");
                w_p.write((sUnresolvedReason == null) ? "" : sUnresolvedReason);
                w_p.write("</span>");

                obj_p = null;
            }
            catch (ClassCastException e)
            {
                // render object
                getFieldManager().getMimeManager().insertIconLink(w_p, obj_p);
                w_p.write("&nbsp;");
                getFieldManager().getMimeManager().insertTextLink(w_p, obj_p.getName(), obj_p);
            }
        }

        // === render delimiter
        w_p.write("<td width=\"80%\">&nbsp;</td>\n");

        // === render document plugin icons for the attachment
        if (obj_p != null)
        {
            OwObject objinst = obj_p.getInstance();

            for (int p = 0; p < m_documentFunctionPluginIds.size(); p++)
            {
                try
                {
                    OwDocumentFunction plugIn = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction((String) m_documentFunctionPluginIds.get(p));
                    if (plugIn.isEnabled(objinst, null, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
                    {
                        w_p.write("<td class=\"OwDocumentFunctionIconList\">");

                        if (plugIn.getNoEvent())
                        {
                            try
                            {
                                w_p.write(plugIn.getIconHTML(objinst, null));
                            }
                            catch (ClassCastException e)
                            {
                                w_p.write(plugIn.getDefaultIconHTML());
                            }
                        }
                        else
                        {
                            w_p.write("<a title=\"");
                            w_p.write(plugIn.getDefaultLabel() + "\" href=\"");
                            w_p.write(getEventURL("AttachmentDocumentFunction", PLUG_INDEX_KEY + "=" + (String) m_documentFunctionPluginIds.get(p) + "&" + ATTACHMENT_DMS_ID + "=" + obj_p.getDMSID()));
                            w_p.write("\">");

                            try
                            {
                                w_p.write(plugIn.getIconHTML(objinst, null));
                            }
                            catch (ClassCastException e)
                            {
                                w_p.write(plugIn.getDefaultIconHTML());
                            }

                            w_p.write("</a>");
                        }

                        w_p.write("</td>\n");
                    }
                }
                catch (OwAccessDeniedException e)
                {
                    // ignore
                }
            }

        }

        // === render paste button if something is in the clipboard
        if (fEdit_p)
        {
            w_p.write("<td class=\"OwDocumentFunctionIconList\">");

            //fieldDef_p - OwFieldDefinition, used to identify if single or array value, since 4.1.1.0
            renderPasteObjectLink(w_p, strID_p, fieldDef_p);

            w_p.write("</td>\n");
        }

        w_p.write("</tr></table>\n");
    }

    /** called when user clicks a document function plugin of an attachment */
    public void onAttachmentDocumentFunction(HttpServletRequest request_p) throws Exception
    {
        // get the requested plugin
        OwDocumentFunction plugIn = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(request_p.getParameter(PLUG_INDEX_KEY));

        // create the requested attachment object
        OwObject obj = ((OwMainAppContext) getContext()).getNetwork().getObjectFromDMSID(request_p.getParameter(ATTACHMENT_DMS_ID), false);

        plugIn.onClickEvent(obj, null, null);
    }

}