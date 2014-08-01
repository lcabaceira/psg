package com.wewebu.ow.unittest.util;

import org.w3c.dom.Node;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Test configuration for adapters and managers.<br>
 * The base context keeps basic configuration information and is independent to the web context.
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
public class OwTestConfiguration extends OwBaseConfiguration
{

    public String getLocalizedPluginDescription(OwXMLUtil pluginDescriptionNode_p)
    {
        return pluginDescriptionNode_p.getSafeTextValue(PLUGIN_NODE_DESCRIPTION, "[undef]");
    }

    public String getLocalizedPluginDescription(String strID_p)
    {
        return strID_p;
    }

    public String getLocalizedPluginSettingTitle(Node settingDescriptionNode_p, String strPluginName_p)
    {
        return OwXMLDOMUtil.getSafeStringAttributeValue(settingDescriptionNode_p, PLUGIN_SETATTR_DISPLAY_NAME, null);
    }

    public String getLocalizedPluginTitle(OwXMLUtil pluginDescriptionNode_p)
    {
        return pluginDescriptionNode_p.getSafeTextValue(PLUGIN_NODE_NAME, "[undef]");
    }

    public String getLocalizedPluginTitle(String strID_p)
    {
        return strID_p;
    }

}
