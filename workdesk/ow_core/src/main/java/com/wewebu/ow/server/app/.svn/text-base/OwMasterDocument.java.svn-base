package com.wewebu.ow.server.app;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Document Module base class for the Main Area Plugins.
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
public class OwMasterDocument extends OwDocument
{
    /** settings info for the plugin     */
    private OwSettingsSet m_SettingsInfo;

    /** Plugin */
    private OwConfiguration.OwMasterPluginInstance m_Plugin;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === get settings map for fast access of properties
        m_SettingsInfo = ((OwMainAppContext) getContext()).getSettings().getSettingsInfo(m_Plugin.getPluginID());
    }

    /** set the plugin instance
     * @param plugin_p OwPlugin
     */
    public void setPlugin(OwConfiguration.OwMasterPluginInstance plugin_p)
    {
        m_Plugin = plugin_p;
    }

    /** set the plugin instance
     * 
     */
    public OwConfiguration.OwMasterPluginInstance getPlugin()
    {
        return m_Plugin;
    }

    /** get the plugin ID 
     */
    public String getPluginID()
    {
        return m_Plugin.getPluginID();
    }

    /** get the plugin description node 
     * @return WcmXMLUtil DOM Node containing the plugin description
     */
    public OwXMLUtil getConfigNode()
    {
        return m_Plugin.getConfigNode();
    }

    /** get the configuration object
     * @return OwConfiguration
     */
    public OwConfiguration getConfiguration()
    {
        return ((OwMainAppContext) getContext()).getConfiguration();
    }

    /** get a settings value for the plugin
     *
     * @param strName_p name of property to retrieve
     * @param default_p Object if setting is not defined in plugin descriptor
     *
     * @return Object Settings Property
     */
    public Object getSafeSetting(String strName_p, Object default_p)
    {
        try
        {
            return ((OwSettingsProperty) m_SettingsInfo.getProperties().get(strName_p)).getValue();
        }
        catch (Exception e)
        {
            return default_p;
        }
    }

    /** set a settings value for the plugin
     * NOTE: 	You must call OwMainAppContext.saveUserPrefs(); to serialize the new value
     * 			This function serializes against the XML Preferences and takes a lot of runtime.
     * 
     * 			==> If possible rather use <b>...getConfiguration().getUserAttributeBag()</b> to serialize application state.
     *
     * @param strName_p name of property to retrieve
     * @param value_p Object to set
     *
     */
    public void setSafeSetting(String strName_p, Object value_p) throws Exception
    {
        ((OwSettingsProperty) m_SettingsInfo.getProperties().get(strName_p)).setValue(value_p);
    }

    /** the scalar settings specific for a user. See getUserAttributeBag() */
    private OwAttributeBagWriteable m_userattributebag;

    /** get the plugin scalar settings specific for the current user.
     * 
     *  Unlike the OwSettings the AttributeBag allows simple scalar values
     *  but with high performance.
     *  
     *  NOTE:
     *  The OwSettings are used for complex settings for the plugins which are manually changed from time to time
     *  by the user or the Administrator.
     *  	==> Performance is negligible, but values are highly structured.
     *  	==> OwSettings are usually based on a XML document. (depends on adapter implementation)
     *  
     *  The OwAttributeBagWriteable is used by the application itself to persist state.
     *  	==> Structure is negligible, but performance is important
     *  	==> OwAttributeBagWriteable is usually based on a DB table. (depends on adapter implementation)
     *  
     * @return OwAttributeBagWriteable to read and write scalars
     * @throws Exception 
     */
    public OwAttributeBagWriteable getPersistentAttributeBagWriteable() throws Exception
    {
        if (null == m_userattributebag)
        {
            m_userattributebag = ((OwMainAppContext) getContext()).createUserAttributeBag("pid_" + getPluginID());
        }

        return m_userattributebag;
    }

    /** This function can be overloaded to dispatch generic calls from other plugins
     *  this is a generic function, used for communication of plugins,
     *  which do not know about the interfaces of each other.
     *
     *  @param iCode_p enumerator designating the requested action
     *  @param param1_p Placeholder for optional parameter
     *  @param param2_p Placeholder for optional parameter
     *  @return Object depending on derived implementation
     */
    protected Object onDispatch(int iCode_p, Object param1_p, Object param2_p) throws Exception
    {
        // default does noting.
        return null;
    }

    /** dispatch an event to the master plugin by calling onDispatch in the master document
     *
     *  @param iCode_p enumerator designating the requested action
     *  @param param1_p Placeholder for optional parameter
     *  @param param2_p Placeholder for optional parameter
     *
     *  @return Object depending on derived implementation
     */
    public Object dispatch(int iCode_p, Object param1_p, Object param2_p) throws Exception
    {
        // Write history
        ((OwMainAppContext) getContext()).getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_DSPATCH, getConfigNode().getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, null), OwEventManager.HISTORY_STATUS_OK);

        // default calls onDispatch
        return onDispatch(iCode_p, param1_p, param2_p);
    }

    /** get the corresponding masterview
     * @return OwMasterView
     * */
    public OwMasterView getMasterView()
    {
        return (OwMasterView) m_Plugin.getView();
    }
}