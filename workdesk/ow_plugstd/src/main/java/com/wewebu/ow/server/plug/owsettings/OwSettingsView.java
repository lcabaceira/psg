package com.wewebu.ow.server.plug.owsettings;

import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwPlugin;
import com.wewebu.ow.server.app.OwSettings;
import com.wewebu.ow.server.app.OwSettingsSet;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwNavigationView;

/**
 *<p>
 * Settings View.
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
public class OwSettingsView extends OwMasterView
{
    /** package logger for the class */
    //private static final Logger LOG = OwLog.getLogger(OwSettingsView.class); 
    /** layout to be used for the view */
    private OwSubLayout m_Layout = new OwSubLayout();

    /** reference to the configuration */
    protected OwConfiguration m_Configuration;

    protected OwSubNavigationView m_SubNavigation;

    /** reference to the settings */
    protected OwSettings m_Settings;

    /** called when the view should create its HTML content to be displayed
     */
    protected void init() throws Exception
    {
        super.init();

        // get configuration reference
        m_Configuration = ((OwMainAppContext) getContext()).getConfiguration();

        // get settings object from context
        m_Settings = ((OwMainAppContext) getContext()).getSettings();

        // === attach layout
        addView(m_Layout, null);

        // === navigation 
        m_SubNavigation = new OwSubNavigationView();
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

        // === add settings view for each plugin that defines a settingset node
        // user one group view for all group boxes.
        // override onActivate(...) to display the appropriate content
        OwSettingsSetView setView = createSettingsSetView();

        // is user administrator ? 
        boolean fAdmin = ((OwMainAppContext) getContext()).isAllowed(OwRoleManager.ROLE_CATEGORY_STANDARD_FUNCTION, OwRoleManager.STD_FUNC_CAN_EDIT_SITE_SETTINGS);

        // iterate over the settings sets for the plugins
        Iterator it = m_Settings.getCollection().iterator();
        while (it.hasNext())
        {
            OwSettingsSet settingsInfo = (OwSettingsSet) it.next();
            if (settingsInfo.hasUserEditableProperties() || (fAdmin && settingsInfo.hasAppEditableProperties()))
            {
                // add view
                String sImage = null;
                //don't retrieve plugin for ow_app setting set.
                if (settingsInfo != ((OwMainAppContext) getContext()).getConfiguration().getAppSettings())
                {
                    try
                    {
                        OwPlugin plugin = ((OwMainAppContext) getContext()).getConfiguration().getAllowedPluginInstance(settingsInfo.getName());
                        sImage = plugin.getIcon();
                    }
                    catch (Exception e)
                    {
                        //LOG.error("Error initializing the OwSettingsView.", e);
                    }
                }
                m_SubNavigation.addView(setView, settingsInfo.getDisplayName(), null, sImage, settingsInfo, null);
            }
        }

        m_SubNavigation.navigate(0);
    }

    /** overridable factory method to create view
     * 
     * @return OwSettingsSetView
     */
    protected OwSettingsSetView createSettingsSetView()
    {
        return new OwSettingsSetView();
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     * @param iIndex_p <code>int</code> tab index of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        // if a user switches to this master plugin, we activate the setting tab of the previous
        // master plugin.
        // Since this event runs before the current master view is changed in the context, we can
        // do this by comparing all settings tabs against the current master view ID.
        // But if this plugin is run as the first plugin, the previous master plugin is null.
        // We have to deal with that.
        if (((OwMainAppContext) getContext()).getCurrentMasterView() != null)
        {
            List tabList = m_SubNavigation.getTabList();
            for (int i = 0; i < tabList.size(); i++)
            {
                OwNavigationView.OwTabInfo menuTabInfo = (OwNavigationView.OwTabInfo) tabList.get(i);

                OwSettingsSet settingsInfo = (OwSettingsSet) menuTabInfo.getReasonObject();

                // check if last activated plugin matches this setting
                if ((null != settingsInfo) && (settingsInfo.getName().equals(((OwMainAppContext) getContext()).getCurrentMasterView().getPluginID())))
                {
                    m_SubNavigation.navigate(i);
                    break;
                }
            }
        }

        super.onActivate(iIndex_p, oReason_p);
    }

    /**
     * Get the configuration value for parameter {@link OwSettingsDocument#CONFIG_NODE_USE_DYNAMIC_SPLIT}.  
     * @return - the configured value for parameter {@link OwSettingsDocument#CONFIG_NODE_USE_DYNAMIC_SPLIT}.
     * @since 3.1.0.0
     */
    private boolean isDynamicSplitUsed()
    {
        return getConfigNode().getSafeBooleanValue(OwSettingsDocument.CONFIG_NODE_USE_DYNAMIC_SPLIT, false);
    }

}