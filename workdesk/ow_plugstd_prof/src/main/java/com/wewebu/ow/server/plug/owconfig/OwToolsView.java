package com.wewebu.ow.server.plug.owconfig;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwToolsView.
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
public class OwToolsView extends OwLayout
{

    /** tools node attribute for the ID */
    public static final String TOOLS_ATTR_ID = "id";
    /** tools node attribute for the displayname */
    public static final String TOOLS_ATTR_DISPLAYNAME = "displayName";
    /** tools node attribute for the description */
    public static final String TOOLS_ATTR_DESCRIPTION = "description";
    /** tools node attribute for the icon */
    public static final String TOOLS_ATTR_ICON = "icon";
    /** token in the viewer servlet to be replaced by the base URL of the server and application */
    public static final String URL_REPLACE_TOKEN_BASEURL = "{baseurl}";
    /** token in the viewer servlet to be replaced by the base URL of the server */
    public static final String URL_REPLACE_TOKEN_SERVERURL = "{serverurl}";
    /** token in the viewer servlet to be replaced by the security token */
    public static final String URL_REPLACE_TOKEN_SECURITYTOKEN = "{securitytoken}";
    /** token in the viewer servlet to be replaced by the encoded security token */
    public static final String URL_REPLACE_TOKEN_SECURITYTOKEN_ENC = "{securitytokenenc}";

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        serverSideDesignInclude("owconfig/OwToolsView.jsp", w_p);
    }

    /** list of all tools */
    Collection m_AdminToolDescriptions = new ArrayList();

    /** inner class to describe one admin tool */
    public class OwAdminToolDescription
    {
        private String m_displayName;
        private String m_description;
        private String m_icon;
        private String m_url;

        public OwAdminToolDescription(String displayName_p, String description_p, String icon_p, String url_p)
        {
            m_displayName = displayName_p;
            m_description = description_p;
            m_icon = icon_p;
            m_url = url_p;
        }

        public String getDisplayName()
        {
            return (m_displayName);
        }

        public String getDescription()
        {
            return (m_description);
        }

        public String getIcon()
        {
            return (m_icon);
        }

        public String getUrl()
        {
            return (m_url);
        }
    }

    /**
     * 
     * @param config_p the &lt;ToolsView&gt; configuration node
     */
    public void setConfigNode(OwXMLUtil config_p) throws Exception
    {
        m_AdminToolDescriptions.clear();
        for (Node testNode = config_p.getNode().getFirstChild(); testNode != null; testNode = testNode.getNextSibling())
        {
            if ((testNode.getNodeType() == Node.ELEMENT_NODE) && testNode.getNodeName().equals("Tool"))
            {
                OwXMLUtil toolConfig = new OwStandardXMLUtil(testNode);
                String displayName = getContext().localize("plugin." + toolConfig.getSafeStringAttributeValue(TOOLS_ATTR_ID, "undef") + ".title", toolConfig.getSafeStringAttributeValue(TOOLS_ATTR_DISPLAYNAME, "[undef]"));
                String description = getContext().localize("plugin." + toolConfig.getSafeStringAttributeValue(TOOLS_ATTR_ID, "undef") + ".description", toolConfig.getSafeStringAttributeValue(TOOLS_ATTR_DESCRIPTION, ""));
                String icon = getContext().getDesignURL() + toolConfig.getSafeStringAttributeValue(TOOLS_ATTR_ICON, "/images/plug/owconfig/tools/unknown.png");
                String url = toolConfig.getSafeTextValue("");
                url = OwString.replaceAll(url, URL_REPLACE_TOKEN_BASEURL, getContext().getBaseURL() + "/");
                url = OwString.replaceAll(url, URL_REPLACE_TOKEN_SERVERURL, getContext().getServerURL() + "/");
                url = OwString.replaceAll(url, URL_REPLACE_TOKEN_SECURITYTOKEN, ((OwMainAppContext) getContext()).getNetwork().getCredentials().getSecurityToken(null));
                url = OwString.replaceAll(url, URL_REPLACE_TOKEN_SECURITYTOKEN_ENC, OwAppContext.encodeURL(((OwMainAppContext) getContext()).getNetwork().getCredentials().getSecurityToken(null)));
                m_AdminToolDescriptions.add(new OwAdminToolDescription(displayName, description, icon, url));
            }
        }
    }

    /**
     * get the list of admin tools as Collection of OwAdminToolDescription objects
     * @return  Collection of OwAdminToolDescription objects
     */
    public Collection getAdminToolDescriptions()
    {
        return (m_AdminToolDescriptions);
    }

}