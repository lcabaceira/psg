package com.wewebu.ow.server.plug.owdms;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owdms.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the DMS document function change class.
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
public class OwDocumentFunctionChangeClass extends OwDocumentFunction implements OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDocumentFunctionChangeClass.class);

    // === members
    /** the last object to check in needed for status historization in onCloseDialog() */
    protected OwObject m_object;

    /** the last parent object needed for status historization in onCloseDialog() */
    protected OwObject m_parentObject;

    /**
     * override to get some plugin configuration tags.
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

    }

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdms/changeclass.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdms/changeclass_24.png");
    }

    /** check if function is enabled for the given object parameters
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return true = enabled, false otherwise
     */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        return super.isEnabled(oObject_p, oParent_p, iContext_p) && oObject_p.canChangeClass();
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwDocumentFunctionChangeClass.onClickEvent: It is not possible to change the class of the document. DMSID = " + oObject_p != null ? oObject_p.getDMSID() : "<null>");
            }
            throw new OwInvalidOperationException(getContext().localize("owdms.OwDocumentFunctionChangeClass.disabled", "It is not possible to change the class of the document."));
        }

        // === create a checkin dialog
        OwChangeClassDialog dlg = new OwChangeClassDialog(oObject_p, this, refreshCtx_p);
        m_object = oObject_p;
        m_parentObject = oParent_p;

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());
        dlg.setTitle(getDefaultLabel());

        // set info icon
        dlg.setInfoIcon(getBigIcon());

        getContext().openDialog(dlg, this);

        // historize begin
        addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwDocumentFunctionChangeClass.onClickEvent: Changing class for object: " + oObject_p.getName() + "initiated!");
        }
    }

    /**
     * Listener for DialogClose events used to historize SUCCESS/CANCEL/FAILURE
     * @param dialogView_p the closed dialog
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {

    }

    /**
     * Historize the status.
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        if (code_p == OwUpdateCodes.MODIFIED_OBJECT_PROPERTY && caller_p instanceof OwChangeClassDialog)
        {
            // historize begin
            addHistoryEvent(m_object, m_parentObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
        }
    }
}