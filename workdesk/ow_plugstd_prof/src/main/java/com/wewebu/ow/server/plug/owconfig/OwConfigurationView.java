package com.wewebu.ow.server.plug.owconfig;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Configuration view.
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
public class OwConfigurationView extends OwMasterView
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwConfigurationView.class);

    /** layout to be used for the view */
    private OwSubLayout m_Layout = new OwSubLayout();

    /** navigation to be used for the view */
    protected OwSubNavigationView m_SubNavigation;

    /** role configuration view */
    protected OwRoleConfigurationView m_RoleConfigurationView;

    /** tools view */
    protected OwToolsView m_ToolsView;

    /** initialize and set up this view
     */
    protected void init() throws Exception
    {
        // initialize parent
        super.init();

        // add the layout to this view
        addView(m_Layout, null);

        // create and add the navigation view
        m_SubNavigation = new OwSubNavigationView();
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

        // create the role configuration view and add it to the layout
        org.w3c.dom.Node roleViewConfigNode = getConfigNode().getSubNode("RoleView");
        if (roleViewConfigNode != null)
        {
            OwXMLUtil roleViewConfig = new OwStandardXMLUtil(roleViewConfigNode);
            if (roleViewConfig.getSafeBooleanAttributeValue("enable", false))
            {
                m_RoleConfigurationView = createRoleConfigurationView();
                m_SubNavigation.addView(m_RoleConfigurationView, getContext().localize("owconfig.OwConfigurationView.roles", "DB Role Manager"), null, null, null, null);
            }
        }

        // create the tools view and add it to the layout
        org.w3c.dom.Node toolsViewConfigNode = getConfigNode().getSubNode("ToolsView");
        if (toolsViewConfigNode != null)
        {
            OwXMLUtil toolsViewConfig = new OwStandardXMLUtil(toolsViewConfigNode);
            if (toolsViewConfig.getSafeBooleanAttributeValue("enable", false))
            {
                m_ToolsView = new OwToolsView();
                m_SubNavigation.addView(m_ToolsView, getContext().localize("owconfig.OwConfigurationView.tools", "Tools"), null, null, null, null);
                m_ToolsView.setConfigNode(toolsViewConfig);
            }
        }

        // activate first navigation page
        if (m_SubNavigation.size() <= 0)
        {
            String msg = "OwConfigurationView.init: The OwConfiguration plugin needs at least one configured page.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
        else
        {
            m_SubNavigation.navigate(0);
        }
    }

    /** overridable factory method to create view
     * 
     * @return OwRoleConfigurationView
     */
    protected OwRoleConfigurationView createRoleConfigurationView()
    {
        return new OwRoleConfigurationView();
    }
}