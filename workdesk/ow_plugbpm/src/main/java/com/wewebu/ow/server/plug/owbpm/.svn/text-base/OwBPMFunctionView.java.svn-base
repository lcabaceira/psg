package com.wewebu.ow.server.plug.owbpm;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Displays the BPM Container functions
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
public class OwBPMFunctionView extends OwView implements OwClientRefreshContext
{
    /** query string key for the plugin index */
    protected static final String QUERY_KEY_PLUGIN = "plugin";

    /** function plugins */
    private List m_DocumentFunctionPluginList;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#init()
     */
    protected void init() throws Exception
    {
        super.init();

        // === get preloaded plugins reference
        m_DocumentFunctionPluginList = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunctionPlugins();

    }

    /** get function plugins
     * 
     * @return a {@link List} of function plugins
     */
    public List getDocumentFuntionPlugins()
    {
        return m_DocumentFunctionPluginList;
    }

    /** currently selected queue */
    protected OwBPMVirtualQueue m_currentQueue = null;

    private OwClientRefreshContext refreshContext;

    /** get the active container the plugins are working on
     * 
     * @return the container {@link OwObject}
     */
    public OwObject getContainerObject()
    {
        if (null == m_currentQueue)
        {
            return null;
        }

        return m_currentQueue.getQueueFolder();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.UPDATE_OBJECT_CHILDS:
            {
                m_currentQueue = (OwBPMVirtualQueue) param_p;
            }
                break;
        }
    }

    /** check if plugin is enabled
     * 
     * @param docFunctionPlugin_p
     * @return a <code>boolean</code>
     * @throws Exception 
     */
    public boolean getIsPluginEnabled(OwDocumentFunction docFunctionPlugin_p) throws Exception
    {
        if (null == getContainerObject())
        {
            return false;
        }

        // get the Need Parent Flag indicating that plugin can only working on documents listed by some parent.
        if (docFunctionPlugin_p.getNeedParent())
        {
            return false; // skip NeedParent plugins, we don't have a parent here
        }

        if (!docFunctionPlugin_p.isEnabled(getContainerObject(), null, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
        {
            return false;
        }

        return true;
    }

    /** get the event URL for document plugin functions
     * 
     * @param iPlugIndex_p
     * @return the event URL as <code>String</code>
     */
    public String getDocumentFunctionEventURL(int iPlugIndex_p)
    {
        return getEventURL("DocumentFunctionPluginEvent", QUERY_KEY_PLUGIN + "=" + String.valueOf(iPlugIndex_p));
    }

    /** event called when user clicked a plugin link
     * @param request_p HttpServletRequest
     */
    public void onDocumentFunctionPluginEvent(HttpServletRequest request_p) throws Exception
    {
        if (null == getContainerObject())
        {
            return;
        }

        // get requested plugin
        OwDocumentFunction docFunctionPlugin = (OwDocumentFunction) m_DocumentFunctionPluginList.get(Integer.parseInt(request_p.getParameter(QUERY_KEY_PLUGIN)));

        // dispatch click event to plugin
        docFunctionPlugin.onClickEvent(getContainerObject(), null, this);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onRender(java.io.Writer)
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("owbpm/OwBPMFunctionView.jsp", w_p);
    }

    public void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception
    {
        if (null != refreshContext)
        {
            refreshContext.onClientRefreshContextUpdate(iReason_p, param_p);
        }
    }

    /** 
     * register an eventlistener with this view to receive notifications
     *  @param eventlister_p OwClientRefreshContext interface
     *  @since 4.2.0.0
     */
    public void setRefreshContext(OwClientRefreshContext eventlister_p)
    {
        this.refreshContext = eventlister_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#detach()
     */
    @Override
    public void detach()
    {
        super.detach();
        this.refreshContext = null;
    }
}
