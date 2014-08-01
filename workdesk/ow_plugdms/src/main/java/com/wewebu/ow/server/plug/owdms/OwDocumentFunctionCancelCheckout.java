package com.wewebu.ow.server.plug.owdms;

import java.util.Collection;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Implementation of the DMS document function cancel checkout.
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
public class OwDocumentFunctionCancelCheckout extends OwDocumentFunction
{
    // === members
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdms/cancelcheckout.png");
    }

    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdms/cancelcheckout_24.png");
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
            return oObject_p.getVersion().canCancelcheckout(iContext_p);
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
    public void onClickEvent(OwObject oObject_p, final OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwCommandCancelCheckout cancelCheckoutCommand = new OwCommandCancelCheckout(oObject_p, getContext(), new OwProcessableObjectStrategy() {

            public boolean canBeProcessed(OwObject object_p) throws Exception
            {
                boolean canBeProcessed = isEnabled(object_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                if (!canBeProcessed)
                {
                    throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionCancelCheckout.invalidobject", "Check-out cannot be canceled for this item."));
                }
                return canBeProcessed;
            }
        });

        executeCancelCheckoutCommand(oParent_p, refreshCtx_p, cancelCheckoutCommand);
    }

    /**
     * Execute cancel checkout command and historize the results.
     * @param oParent_p
     * @param refreshCtx_p
     * @param cancelCheckoutCommand_p
     * @throws Exception
     */
    private void executeCancelCheckoutCommand(final OwObject oParent_p, OwClientRefreshContext refreshCtx_p, OwCommandCancelCheckout cancelCheckoutCommand_p) throws Exception
    {
        // cancel checkout
        cancelCheckoutCommand_p.execute();
        // historize success, if any
        if (cancelCheckoutCommand_p.hasProcessedObjects())
        {
            addHistoryEvent(cancelCheckoutCommand_p.getProcessedObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        }
        if (cancelCheckoutCommand_p.hasDisabledObjects())
        {
            addHistoryEvent(cancelCheckoutCommand_p.getDisabledObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_DISABLED);
        }
        // historize failure, if any
        if (cancelCheckoutCommand_p.hasErrors())
        {
            addHistoryEvent(cancelCheckoutCommand_p.getAllErrorObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            // re-throw exception
            throw new Exception(cancelCheckoutCommand_p.getAllErrorMessages());
        }

        // === refresh necessary, call client
        if (null != refreshCtx_p)
        {
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_VERSION, null);
        }
    }

    /** event called when user clicked the plugin for multiple selected items
     *
     *  @param objects_p Collection of OwObject 
     *  @param oParent_p Parent which listed the Objects
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onMultiselectClickEvent(Collection objects_p, final OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // default implementation, can be overridden
        OwCommandCancelCheckout cancelCheckoutCommand = new OwCommandCancelCheckout(objects_p, getContext(), new OwProcessableObjectStrategy() {

            public boolean canBeProcessed(OwObject object_p) throws Exception
            {
                return isEnabled(object_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
            }
        });
        executeCancelCheckoutCommand(oParent_p, refreshCtx_p, cancelCheckoutCommand);
    }
}