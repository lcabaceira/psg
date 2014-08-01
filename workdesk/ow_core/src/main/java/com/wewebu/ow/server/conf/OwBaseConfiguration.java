package com.wewebu.ow.server.conf;

import org.w3c.dom.Node;

import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Interface for the base context. The base context keeps basic configuration 
 * information and is independent to the web context.
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
public abstract class OwBaseConfiguration
{
    //	 === plugin type type definitions
    /** type of the application, which is actually no plugin, 
     it is only used to declare help files and settings for the application (Workdesk) itself. */
    public static final String PLUGINTYPE_APPLICATION = "ow_app";

    /** type of the main plugins */
    public static final String PLUGINTYPE_MASTER = "ow_master";
    /** type of the document function plugins */
    public static final String PLUGINTYPE_DOCUMENT_FUNCTION = "ow_docfunction";
    /** type of the record function plugins, used in the record plugin only */
    public static final String PLUGINTYPE_RECORD_FUNCTION = "ow_recordfunction";

    /**
     *<p>
     * Type definitions of the different plugin types.
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
    public static class OwPluginTypeDefinition
    {
        /** 
         *
         * @param strType_p String type string of plugin
         * @param displayName_p OwString display name
         * @param strClassName_p String base class name
         */
        public OwPluginTypeDefinition(String strType_p, OwString displayName_p, String strClassName_p)
        {
            m_strType = strType_p;
            m_DisplayName = displayName_p;
            m_strClassName = strClassName_p;
        }

        public String getType()
        {
            return m_strType;
        }

        public String getDisplayName(java.util.Locale local_p)
        {
            return m_DisplayName.getString(local_p);
        }

        public String getClassName()
        {
            return m_strClassName;
        }

        private String m_strType;
        private OwString m_DisplayName;
        private String m_strClassName;
    }

    /** type definitions of the different plugin types */
    public static final OwPluginTypeDefinition[] m_pluginTypeDefinitions = { new OwPluginTypeDefinition(PLUGINTYPE_APPLICATION, new OwString("app.OwConfiguration.mainplugdisplayname_app", "Application Plugin"), null),
            new OwPluginTypeDefinition(PLUGINTYPE_MASTER, new OwString("app.OwConfiguration.mainplugdisplayname_master", "Master Plugin"), "com.wewebu.ow.server.app.OwMasterDocument"),
            new OwPluginTypeDefinition(PLUGINTYPE_DOCUMENT_FUNCTION, new OwString("app.OwConfiguration.mainplugdisplayname_DocFunction", "Document Plugin"), "com.wewebu.ow.server.app.OwDocumentFunction"),
            new OwPluginTypeDefinition(PLUGINTYPE_RECORD_FUNCTION, new OwString("app.OwConfiguration.mainplugdisplayname_RecordFunction", "eFile Plugin"), "com.wewebu.ow.server.app.OwRecordFunction") };

    /** get the type definitions of the different plugin types */
    public static OwPluginTypeDefinition[] getPluginTypeDefinitions()
    {
        return m_pluginTypeDefinitions;
    }

    /** get a display name for the given plugin type definition
     */
    public static String getPluginTypeDisplayName(String strPluginType_p, java.util.Locale locale_p)
    {
        for (int i = 0; i < m_pluginTypeDefinitions.length; i++)
        {
            if (m_pluginTypeDefinitions[i].getType().equals(strPluginType_p))
            {
                return m_pluginTypeDefinitions[i].getDisplayName(locale_p);
            }
        }

        return "?";
    }

    //  === plugin node definitions
    /** plugin node name for the ID */
    public static final String PLUGIN_NODE_ID = "id";
    /** plugin node name for the Name */
    public static final String PLUGIN_NODE_NAME = "Name";
    /** plugin node name for the Description */
    public static final String PLUGIN_NODE_DESCRIPTION = "Description";
    /** plugin node name for the Vender */
    public static final String PLUGIN_NODE_VENDOR = "Vendor";
    /** plugin node name for the Description */
    public static final String PLUGIN_NODE_CLASSNAME = "ClassName";
    /** plugin node name for the Description */
    public static final String PLUGIN_NODE_VIEW_CLASSNAME = "ViewClassName";
    /** plugin node name for the Description */
    public static final String PLUGIN_NODE_VERSION = "Version";
    /** plugin node name for the help JSP path  */
    public static final String PLUGIN_NODE_HELP = "helppath";

    // === plugin settings definitions
    /** name of the settings node in the plugin description DOM tree. */
    public static final String PLUGIN_NODE_SETTINGS_SET = "settingsset";
    /** node attribute for the list max size */
    public static final String PLUGIN_SETATTR_MAX_SIZE = "maxsize";
    /** node attribute for the list flag */
    public static final String PLUGIN_SETATTR_LIST = "list";
    /** node attribute for the display name */
    public static final String PLUGIN_SETATTR_DISPLAY_NAME = "displayname";
    /** node attribute for the edit flag */
    public static final String PLUGIN_SETATTR_EDIT = "edit";
    /** node attribute for the scope flag */
    public static final String PLUGIN_SETATTR_SCOPE = "scope";

    /** get the localized title of a plugin
    *
    * @param pluginDescriptionNode_p OwXMLUtil wrapper
    */
    public abstract String getLocalizedPluginTitle(OwXMLUtil pluginDescriptionNode_p);

    /** get the localized title of a plugin
    *
    * @param strID_p String plugin ID 
    */
    public abstract String getLocalizedPluginTitle(String strID_p);

    /** get the localized description of a plugin
    *
    * @param pluginDescriptionNode_p OwXMLUtil wrapper
    */
    public abstract String getLocalizedPluginDescription(OwXMLUtil pluginDescriptionNode_p);

    /** get the localized description of a plugin
    *
    * @param strID_p String plugin ID 
    */
    public abstract String getLocalizedPluginDescription(String strID_p);

    /** get the localized title of a plugin setting
    *
    * @param settingDescriptionNode_p Node
    * @param strPluginName_p String
    */
    public abstract String getLocalizedPluginSettingTitle(Node settingDescriptionNode_p, String strPluginName_p);

}