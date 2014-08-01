package com.wewebu.ow.server.plug.owbpm;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwNavigationView;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Main view.
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
public class OwBPMView extends OwMasterView implements OwClientRefreshContext
{
    private static final Logger LOG = OwLog.getLogger(OwBPMView.class);

    /** work item list view with activated plugins */
    private OwBPMWorkItemListView m_workItemListView;

    /** layout to be used for the view */
    private OwSubLayout m_Layout = new OwSubLayout();

    /** the sub navigation view to navigate the different views (postboxes) */
    private OwSubNavigationView m_subNavigation;

    /** false if plugin was not already activated, true otherwise */
    private boolean m_fInitialized;

    private OwBPMFunctionView functionView;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_workItemListView = createWorkItemListView();

        // === attached layout
        addView(m_Layout, null);

        // === navigation 
        m_subNavigation = new OwSubNavigationView();
        m_Layout.addView(m_subNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // === function view
        this.functionView = new OwBPMFunctionView();
        m_Layout.addView(this.functionView, OwSubLayout.MENU_REGION, null);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_subNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

    }

    /** overridable factory method
     * 
     * @return OwBPMWorkItemListView
     */
    protected OwBPMWorkItemListView createWorkItemListView()
    {
        return new OwBPMWorkItemListView();
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     *
     * @param iIndex_p int tab index of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        super.onActivate(iIndex_p, oReason_p);

        if (!m_fInitialized)
        {
            // add the postboxes which are the tabs on the left
            initOnActivate();
            m_fInitialized = true;

            updateQueueCount();

            // === activate first error free view 
            getSubNavigation().navigateFirst();
        }
        else
        {
            m_workItemListView.update();
            updateQueueCount();
        }
    }

    /** called once after plugin has activated the first time
     * adds the postboxes, overview and configuration tab the the subnavigation
     */
    private void initOnActivate() throws Exception
    {
        // get the queues from BPM
        Collection queues = ((OwBPMDocument) getDocument()).getWorkQueues();

        if (queues.size() != 0)
        {
            Iterator it = queues.iterator();
            while (it.hasNext())
            {
                OwBPMVirtualQueue queue = (OwBPMVirtualQueue) it.next();

                if (null == queue)
                {
                    // === add a delimiter
                    getSubNavigation().addDelimiter();
                }
                else
                {
                    // === add a tab view
                    // for each queue use the identical view, but with different queue wrapper
                    getSubNavigation().addView(m_workItemListView, queue.getDisplayName(), null, queue.getIcon(), queue, queue.getDisplayName());
                }
            }

            // set refresh context to get notifications
            this.functionView.setRefreshContext(this);
            m_workItemListView.setRefreshContext(this);
        }
    }

    /** called by the framework to update the view when OwDocument.Update was called
     *
     *  NOTE:   We can not use the onRender method to update,
     *          because we do not know the call order of onRender.
     *          onUpdate is always called before all onRender methods.
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS:
                updateQueueCount();
                break;
        }
    }

    /** iterate over the navigation to update the queue count in the title
     */
    private void updateQueueCount() throws Exception
    {
        List tablist = getSubNavigation().getTabList();
        for (int i = 0; i < tablist.size(); i++)
        {
            OwNavigationView.OwTabInfo tab = (OwNavigationView.OwTabInfo) tablist.get(i);

            try
            {
                // get the associated reason object, try to cast to queue
                OwBPMVirtualQueue queue = (OwBPMVirtualQueue) tab.getReasonObject();

                try
                {
                    int iCount = queue.getItemCount(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
                    // now set the title with the new count
                    StringBuffer buf = new StringBuffer();
                    buf.append(queue.getDisplayName());
                    buf.append("<span class='OwTaskView_boxcount'>");
                    buf.append("(");
                    buf.append(String.valueOf(iCount));
                    buf.append(")");
                    buf.append("</span>");
                    m_subNavigation.setTitle(buf.toString(), i);

                    //TODO add proper unit tests for OwAlfrescoWorkItemContainer*.getType()
                    if (queue.isHideIfEmpty())
                    {
                        this.m_subNavigation.setVisible(i, 0 != iCount);
                    }
                    else
                    {
                        this.m_subNavigation.setVisible(i, true);
                    }
                }
                catch (OwStatusContextException e)
                {
                    // ignore, count could not be retrieved
                    LOG.debug("Could not retrieve item count for queue.", e);
                }
            }
            catch (NullPointerException e1)
            {
                // obviously some other navigation element
            }
            catch (ClassCastException e2)
            {
                // obviously some other navigation element
            }
        }
    }

    /** call client and cause it to refresh its display data
     * @param iReason_p reason as defined in OwUpdateCodes
     * @param param_p Object optional parameter representing the refresh, depends on the value of iReason_p, can be null
     */
    public void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception
    {
        // switch notifications from the plugins within the work item list
        switch (iReason_p)
        {
        /** the object properties changed */
            case OwUpdateCodes.UPDATE_OBJECT_PROPERTY:
                break;

            /** the object has changed its folder children */
            case OwUpdateCodes.DELETE_OBJECT:
            case OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS:
                m_workItemListView.update();
                break;

        }
    }

    /**
     * Returns the work item List View with activated plugins
     * @return {@link OwBPMWorkItemListView}
     * @since 2.5.2.0
     */
    public OwBPMWorkItemListView getWorkItemListView()
    {
        return m_workItemListView;
    }

    /**
     * Helper method to create an URL which will trigger an update of the view.
     * @return String representing the URL
     * @since 3.2.0.1
     */
    public String getRefreshURL()
    {
        return getSubNavigation().getNavigateEventURL(getSubNavigation().getNavigationIndex());
    }

    /**
     * Simple getter of current created navigation view.
     * @return OwSubNavigationView  or null if it was not initialized.
     * @since 3.2.0.1
     */
    protected OwSubNavigationView getSubNavigation()
    {
        return m_subNavigation;
    }

}