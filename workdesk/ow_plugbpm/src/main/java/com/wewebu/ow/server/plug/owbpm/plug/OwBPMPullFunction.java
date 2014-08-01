package com.wewebu.ow.server.plug.owbpm.plug;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Pulls the next available item and opens it with the configured plugin.
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
public class OwBPMPullFunction extends OwDocumentFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMPullFunction.class);

    private OwDocumentFunction m_dispatchfunction;

    protected OwDocumentFunction getDispatchFunction() throws Exception
    {
        if (null == m_dispatchfunction)
        {
            // === get the dispatch document function
            String sDispatchDocumentFunctionPluginID = getConfigNode().getSafeTextValue("DispatchFunction", null);
            if (sDispatchDocumentFunctionPluginID == null)
            {
                String msg = "OwBPMPullFunction.getDispatchFunction: Define a DispatchFunction.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }
            m_dispatchfunction = getContext().getConfiguration().getDocumentFunction(sDispatchDocumentFunctionPluginID);
        }

        return m_dispatchfunction;
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getDispatchFunction().getIcon();
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getDispatchFunction().getBigIcon();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#isEnabled(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (super.isEnabled(oObject_p, oParent_p, iContext_p))
        {
            return ((OwWorkitemContainer) oObject_p).canPull(iContext_p);
        }
        else
        {
            return false;
        }
    }

    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        try
        {
            OwWorkitem pulleditem = ((OwWorkitemContainer) oObject_p).pull(null, null);

            // dispatch it
            getDispatchFunction().onClickEvent(pulleditem, oObject_p, refreshCtx_p);
            addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        }
        catch (OwObjectNotFoundException e)
        {
            addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            throw new OwObjectNotFoundException(getContext().localize("plug.owbpm.OwBPMPullFunction.pullfailed", "There is no work item to edit."), e);
        }
        catch (OwServerException e2)
        {
            addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            throw new OwObjectNotFoundException(getContext().localize("plug.owbpm.OwBPMPullFunction.pulltimeout", "No work item opened for editing. Please try it again."), e2);
        }
    }
}
