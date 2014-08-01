package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * BPM Plugin for resubmission.<br/>
 * The resubmission date of the workitems is set to null.<br/>
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
public class OwBPMResubmitResetFunction extends OwDocumentFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMResubmitResetFunction.class);

    private static final Date DATE_IN_THE_PAST = new Date(0); //January 1, 1970, 00:00:00 GMT     

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owbpm/resubmit_reset.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owbpm/resubmit_reset_24.png");
    }

    /** check if function is enabled for the given object parameters
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which lists the Object
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

        if (!((OwWorkitem) oObject_p).canResubmit(iContext_p))
        {
            return false;
        }

        // enable the function just if the resubmit date is in the future.
        Date resubmitdate = ((OwWorkitem) oObject_p).getResubmitDate(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        if (resubmitdate == null)
        {
            return false;
        }
        return resubmitdate.getTime() > new Date().getTime();

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
        List workitems = new ArrayList();
        workitems.add(oObject_p);
        this.onMultiselectClickEvent(workitems, oParent_p, refreshCtx_p);
    }

    /**
     * @see com.wewebu.ow.server.app.OwDocumentFunction#onMultiselectClickEvent(java.util.Collection,
     *      com.wewebu.ow.server.ecm.OwObject,
     *      com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        // filter the work item for which this function is enabled

        List enabledWorkitems = new ArrayList();
        for (Iterator iter = objects_p.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            if (this.isEnabled(object, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                enabledWorkitems.add(object);
            }
            else
            {
                LOG.debug("OwBPMResubmitResetFunction.onMultiselectClickEvent: Reset Resubmissiondate is not enabled for " + object.getName());
            }
        }
        if (enabledWorkitems.isEmpty())
        {
            String errMessage = getContext().localize("plug.owbpm.plug.OwBPMResubmitResetFunction.any.notenabled", "Resubmission reset function cannot be executed to any of the selected work items.");
            this.getContext().postMessage(errMessage);
            return;
        }

        // Lock the items
        for (Iterator iter = enabledWorkitems.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            object.setLock(true);
        }

        // reset the resubmission date to a date in the past.
        try
        {
            for (Iterator iter = enabledWorkitems.iterator(); iter.hasNext();)
            {
                OwWorkitem object = (OwWorkitem) iter.next();
                object.resubmit(DATE_IN_THE_PAST);
                this.addHistoryEvent(object, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
            }

            // === refresh necessary, call client
            if (null != refreshCtx_p)
            {
                refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, enabledWorkitems);
            }

            // post message to user
            m_MainContext.postMessage(this.m_MainContext.localize("plug.OwBPMResubmitResetFunction.resetmessage", "Resubmission was reset for the following processes."));
            for (Iterator iter = objects_p.iterator(); iter.hasNext();)
            {
                OwWorkitem wItem = (OwWorkitem) iter.next();
                m_MainContext.postMessage(" - " + OwHTMLHelper.encodeToSecureHTML(wItem.getName()));
            }

        }
        finally
        {
            // unLock the items
            for (Iterator iter = enabledWorkitems.iterator(); iter.hasNext();)
            {
                OwWorkitem object = (OwWorkitem) iter.next();
                object.setLock(false);
            }
        }

    }

}