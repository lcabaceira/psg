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
 * Implementation of the DMS document function demote.
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
public class OwDocumentFunctionDemote extends OwDocumentFunction
{
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdms/demoteversion.png");
    }

    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdms/demoteversion_24.png");
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
            return oObject_p.getVersion().canDemote(iContext_p);
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
        OwProcessableObjectStrategy processableObjectStrategy = new OwProcessableObjectStrategy() {

            public boolean canBeProcessed(OwObject object_p) throws Exception
            {
                boolean result = isEnabled(object_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                if (!result)
                {
                    throw new OwInvalidOperationException(getContext().localize("plug.owdms.OwDocumentFunctionDemote.invalidobject", "Item cannot be demoted."));
                }
                return result;
            }
        };

        addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        OwCommandDemote commandDemote = new OwCommandDemote(oObject_p, getContext(), processableObjectStrategy);
        executeDemoteCommand(oParent_p, refreshCtx_p, commandDemote);
    }

    /** event called when user clicked the plugin for multiple selected items
     *
     *  @param objects_p Iterator of OwObject 
     *  @param oParent_p Parent which listed the Objects
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
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
        addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_BEGIN);
        OwCommandDemote commandDemote = new OwCommandDemote(objects_p, getContext(), processableObjectStrategy);
        executeDemoteCommand(oParent_p, refreshCtx_p, commandDemote);
    }

    /**
     * Executes and historizes the demote command.
     * @param oParent_p
     * @param refreshCtx_p
     * @param commandDemote_p
     * @throws Exception -thrown when demote operation didn't work
     */
    private void executeDemoteCommand(final OwObject oParent_p, OwClientRefreshContext refreshCtx_p, OwCommandDemote commandDemote_p) throws Exception
    {
        commandDemote_p.execute();

        if (commandDemote_p.hasProcessedObjects())
        {
            // historize
            addHistoryEvent(commandDemote_p.getProcessedObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
        }
        if (commandDemote_p.hasDisabledObjects())
        {
            addHistoryEvent(commandDemote_p.getDisabledObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_DISABLED);
        }
        if (commandDemote_p.hasErrors())
        {
            addHistoryEvent(commandDemote_p.getAllErrorObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
            //NO exception was thrown in the past
            throw new Exception(commandDemote_p.getAllErrorMessages());
        }

        // === refresh necessary, call client
        if (null != refreshCtx_p)
        {
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_VERSION, null);
        }
    }
}