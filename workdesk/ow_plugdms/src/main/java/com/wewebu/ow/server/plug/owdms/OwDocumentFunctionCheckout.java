package com.wewebu.ow.server.plug.owdms;

import java.util.Collection;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwMimeManager.OwOpenCommand;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the DMS document function checkout.
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
public class OwDocumentFunctionCheckout extends OwDocumentFunction
{
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdms/checkout.png");
    }

    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdms/checkout_24.png");
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
        if (!super.isEnabled(oObject_p, oParent_p, iContext_p))
        {
            return false;
        }
        if (oObject_p.hasVersionSeries())
        {
            return oObject_p.getVersion().canCheckout(iContext_p);
        }
        else
        {
            return false;
        }
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(final OwObject oObject_p, final OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwProcessableObjectStrategy processableObjectStrategy = new OwProcessableObjectStrategy() {
            public boolean canBeProcessed(OwObject object_p) throws Exception
            {
                //don't use isEnabled, check if object type and object class is supported
                if (!isObjectTypeSupported(oObject_p.getType()) || !isObjectClassSupported(oObject_p.getObjectClass().getClassName()))
                {
                    throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionCheckout.msgDisabled", "Checkout cannot be executed for the selected object."));
                }
                //don't use isEnabled, check if object is versionable
                if (!object_p.hasVersionSeries())
                {
                    throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionCheckout.invalidobject", "Cannot check out the document. The document has no version series."));
                }
                boolean isMyCheckedOut = false;
                try
                {
                    isMyCheckedOut = object_p.getVersion().isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }
                catch (Exception ex)
                {
                    //isMyCheckedOut() is not supported or not implemented or exception occurred
                }
                if (isMyCheckedOut)
                {
                    throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionCheckout.documentCheckedOutbyYou", "Cannot check out the document. The document is already checked out by you."));
                }
                if (!object_p.getVersion().canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                {
                    // search for a reason why the function is not enable
                    String checkedoutBy = null;
                    try
                    {
                        checkedoutBy = object_p.getVersion().getCheckedOutUserID(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                    }
                    catch (Exception e)
                    {
                        //getCheckedOutUserID() is not supported or not implemented or exception occurred     
                    }
                    if (checkedoutBy != null && !checkedoutBy.equals(""))
                    {
                        checkedoutBy = getDisplayNameFromUserId(checkedoutBy);
                        throw new OwInvalidOperationException(getContext().localize1("plug.owdms.OwDocumentFunctionCheckout.documentCheckedOutbyOtherUser", "Cannot check out the document. The document is already checked out by user (%1).",
                                checkedoutBy));
                    }
                    throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionCheckout.documentCheckedOutNotPossible",
                            "Cannot check out the document. Probably the document is already checked out or you do not have the necessary authorization to perform this action."));
                }
                return true;
            }
        };
        OwCommandCheckout commandCheckout = new OwCommandCheckout(oObject_p, getContext(), processableObjectStrategy);
        executeCheckoutCommand(oParent_p, refreshCtx_p, commandCheckout);
        // === finally trigger download of the object's latest version
        OwObject latest = oObject_p;
        if (latest.hasVersionSeries())
        {
            latest = latest.getVersionSeries().getObject((oObject_p.getVersionSeries().getLatest()));
        }

        if (latest.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            OwXMLUtil node = OwMimeManager.getMimeNode(m_MainContext.getConfiguration(), latest);
            StringBuffer buff = new StringBuffer();
            /*Check if node exist before calling editObject, or
             * else there will be an InvalidOperationException thrown*/
            if (node.getSubNode(OwMimeManager.MIME_EDITSERLVET) != null)
            {
                OwOpenCommand cmd = OwMimeManager.editObject(m_MainContext, latest);
                if (cmd.canGetScript())
                {
                    buff.append(cmd.getScript(cmd.getViewerMode(), null, false));
                }
                else
                {
                    buff.append(cmd.getURL());
                }

            }
            else
            {
                // === download via java script
                buff.append("navigateServiceFrameTO(\"");
                buff.append(OwMimeManager.getSaveDownloadURL(m_MainContext, latest));
                buff.append("\");");
            }
            // add script to download the document after checkout
            m_MainContext.addFinalScript(buff.toString());
        }

    }

    /** event called when user clicked the plugin for multiple selected items
     *
     *  @param objects_p Iterator of OwObject 
     *  @param oParent_p Parent which listed the Objects
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onMultiselectClickEvent(Collection objects_p, final OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // default implementation, can be overridden
        OwProcessableObjectStrategy processableObjectStrategy = new OwProcessableObjectStrategy() {

            public boolean canBeProcessed(OwObject object_p) throws Exception
            {
                return isEnabled(object_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
            }
        };
        OwCommandCheckout commandCheckout = new OwCommandCheckout(objects_p, getContext(), processableObjectStrategy);
        executeCheckoutCommand(oParent_p, refreshCtx_p, commandCheckout);
    }

    private void executeCheckoutCommand(final OwObject oParent_p, OwClientRefreshContext refreshCtx_p, OwCommandCheckout commandCheckout_p) throws Exception
    {
        commandCheckout_p.execute();

        if (commandCheckout_p.hasProcessedObjects())
        {
            // historize success
            addHistoryEvent(commandCheckout_p.getProcessedObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        }
        if (commandCheckout_p.hasDisabledObjects())
        {
            addHistoryEvent(commandCheckout_p.getDisabledObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_DISABLED);
        }
        if (commandCheckout_p.hasErrors())
        {
            // historize failure
            addHistoryEvent(commandCheckout_p.getAllErrorObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            // re-throw exception
            throw new Exception(commandCheckout_p.getAllErrorMessages());
        }
        // === refresh necessary, call client
        if (null != refreshCtx_p)
        {
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_VERSION, null);
        }
    }
}