package com.wewebu.ow.server.plug.owdocsend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.servlets.OwMultifileDownload;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the Send as Attachment plugin.
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
public class OwSendAsAttachmentFunction extends OwDocumentFunction
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwSendAsAttachmentFunction.class);

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocemailattachement/sendattachement.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocemailattachement/sendattachement_24.png");
    }

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
    }

    /** check if function is enabled for the given object parameters (called only for single select operations)
    *
    *  @param object_p OwObject where event was triggered
    *  @param parent_p Parent which listed the Object
    *  @param context_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
    {
        if (!super.isEnabled(object_p, parent_p, context_p))
        {
            return false;
        }

        if ((object_p.getType() != OwObjectReference.OBJECT_TYPE_DOCUMENT) || !object_p.hasContent(context_p))
        {
            return false;
        }

        return true;
    }

    /** event called when user clicked the plugin label / icon 
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        Collection coll_Objects_p = new ArrayList();
        coll_Objects_p.add(oObject_p);
        this.onMultiselectClickEvent(coll_Objects_p, oParent_p, refreshCtx_p);

    }

    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject 
    *  @param parent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onMultiselectClickEvent(Collection objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // filter objects, which are no documents or don't have content.
        List filesToZip = new ArrayList();
        for (Iterator iterator = objects_p.iterator(); iterator.hasNext();)
        {
            OwObject object = (OwObject) iterator.next();
            if (!isEnabled(object, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                String escapedName = OwHTMLHelper.encodeToSecureHTML(object.getName());
                this.m_MainContext.postMessage(this.m_MainContext.localize1("server.plug.owdocsend.OwSendAsAttachmentFunction.nodocument", "Object %1 is not a Document.", escapedName));
                LOG.debug("OwSendAsAttachmentFunction.onMultiselectClickEvent: Object " + escapedName + " is not a Document.");
                continue;
            }

            filesToZip.add(object);
        }

        if (filesToZip == null || filesToZip.size() == 0)
        {
            m_MainContext.postMessage(this.m_MainContext.localize("server.plug.owdocsend.OwSendAsAttachmentFunction.nofile", "No valid files found to download!"));
            LOG.debug("OwSendAsAttachmentFunction.onMultiselectClickEvent: No valid files found to download!");
            return;
        }

        // store filesToZip list into session for servlet.
        (getContext()).getHttpSession().setAttribute(OwMultifileDownload.ATTRIBUTE_NAME_FILES_TO_DOWNLOAD, filesToZip);

        String appletDialogURL = getContext().getDesignURL() + "/OwMultifileAttachment.jsp";

        String strScript_p = "openCenteredDialogWindow('" + appletDialogURL + "', 'Attaching' , 200, 80);";
        this.m_MainContext.addFinalScript(strScript_p);

        addHistoryEvent(objects_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }
}