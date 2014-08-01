package com.wewebu.ow.server.app;

import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base interface for all plugins.
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
public interface OwPlugin
{
    /** get a localized display name
     * @return String
     */
    public abstract String getPluginTitle();

    /** get the plugin ID 
     * @return String
     */
    public abstract String getPluginID();

    /** get the plugin type
     * @return String as defined in OwConfiguration.PLUGINTYPE_...
     */
    public abstract String getPluginType();

    /** get a display name for the plugin type
     * @return String
     */
    public abstract String getPluginTypeDisplayName();

    /** get the plugin config node
     * @return OwXMLUtil
     */
    public abstract OwXMLUtil getConfigNode();

    /** get the icon URL for this plugin to be displayed
    *
    *  @return String icon url, or null if not defined
    */
    public abstract String getIcon() throws Exception;
}
