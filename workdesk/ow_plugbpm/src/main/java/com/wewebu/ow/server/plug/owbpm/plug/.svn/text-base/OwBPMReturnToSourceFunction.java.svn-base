package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Workdesk BPM Plugin for returning the step to the original source after a reassign.
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
public class OwBPMReturnToSourceFunction extends OwDocumentFunction
{
    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owbpm/returntosource.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owbpm/returntosource_24.png");
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(com.wewebu.ow.server.ecm.OwObject oObject_p, com.wewebu.ow.server.ecm.OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwWorkitem object = (OwWorkitem) oObject_p;

        if (!object.canReturnToSource(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(getContext().localize("plug.owbpm.plug.OwBPMReturnToSourceFunction.notenabled", "Reassign function is not available for this work item."));
        }

        object.setLock(true);

        object.returnToSource();

        object.setLock(false);

        if (null != refreshCtx_p)
        {
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
        }
        Collection enabledWorkitems = new LinkedList();
        enabledWorkitems.add(object);
        addHistoryEvent(enabledWorkitems, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * @see com.wewebu.ow.server.app.OwDocumentFunction#onMultiselectClickEvent(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        // just handle the workitems which can be returned to source
        List enabledWorkitems = new ArrayList();
        for (Iterator iter = objects_p.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            if (object.canReturnToSource(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                enabledWorkitems.add(object);
            }
        }
        if (enabledWorkitems.isEmpty())
        {
            throw new OwInvalidOperationException(getContext().localize("plug.owbpm.plug.OwBPMReturnToSourceFunction.any.notenabled", "Reassign function is not available for any of the selected work items."));
        }

        // now do the work
        for (Iterator iter = enabledWorkitems.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();

            object.setLock(true);

            object.returnToSource();

            object.setLock(false);
        }

        if (null != refreshCtx_p)
        {
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
        }
        // historize
        addHistoryEvent(enabledWorkitems, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

    /** check if function is enabled for the given object parameters
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param iContext_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (!super.isEnabled(oObject_p, oParent_p, iContext_p))
        {
            return false;
        }

        return ((OwWorkitem) oObject_p).canReturnToSource(iContext_p);
    }

}