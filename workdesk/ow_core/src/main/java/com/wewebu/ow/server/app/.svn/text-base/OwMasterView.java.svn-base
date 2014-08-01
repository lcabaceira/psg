package com.wewebu.ow.server.app;

import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwBaseView;
import com.wewebu.ow.server.ui.OwDialogManager;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * View Module base class for the Main Area Plugins.
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
public abstract class OwMasterView extends OwView
{
    // every master plugin owns a dialog manager
    private OwDialogManager m_dialogmanager = new OwDialogManager();

    private static Logger LOG = OwLogCore.getLogger(OwMasterView.class);

    /** set the plugin description node 
     *
     */
    public OwConfiguration.OwMasterPluginInstance getPlugin()
    {
        return ((OwMasterDocument) getDocument()).getPlugin();
    }

    /** get the plugin ID 
     */
    public String getPluginID()
    {
        return getPlugin().getPluginID();
    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return getPlugin().getPluginTitle();
    }

    public String getBreadcrumbPart()
    {
        OwDialogManager dialogManager = getDialogManager();
        String dlgTitle = dialogManager.getBreadcrumbPart();
        if (dlgTitle == null || dlgTitle.equals(OwBaseView.EMPTY_STRING))
        {
            List lst = dialogManager.getViewList();
            if (lst != null && lst.size() > 0)
            {
                String clazz = lst.get(lst.size() - 1).getClass().toString();
                LOG.warn("OwMasterView.getBreadcrumbPart: The name of the dialog, used for the windows title is null. PluginID=[" + getPluginID() + "], PluginName=[" + getTitle() + "] / [" + clazz + "]");
            }
            return getTitle();
        }
        return getTitle() + " - " + dlgTitle;
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon URL, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return getPlugin().getIcon();
    }

    /** get the plugin description node 
     * @return WcmXMLUtil DOM Node containing the plugin description
     */
    public OwXMLUtil getConfigNode()
    {
        return getPlugin().getConfigNode();
    }

    /** get the configuration object
     * @return OwConfiguration
     */
    public OwConfiguration getConfiguration()
    {
        return ((OwMainAppContext) getContext()).getConfiguration();
    }

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        addView(m_dialogmanager, null);
    }

    /** get the dialog manager of this plugin
     * 
     * @return OwDialogManager
     */
    public OwDialogManager getDialogManager()
    {
        return m_dialogmanager;
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     * @param iIndex_p int index for the tab  of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        super.onActivate(iIndex_p, oReason_p);

        // notify the context about the new activated master plugin in order to generate context sensitive information
        ((OwMainAppContext) getContext()).onActivateMasterPlugin(this);
    }
}