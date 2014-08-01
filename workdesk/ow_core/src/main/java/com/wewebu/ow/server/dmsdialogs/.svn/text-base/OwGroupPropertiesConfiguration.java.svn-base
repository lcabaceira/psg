package com.wewebu.ow.server.dmsdialogs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Holds the configuration of the grouped properties.
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
 *@since 3.1.0.0
 *@deprecated since 4.2.0.0 use new OwPropertyListConfiguration
 */
public class OwGroupPropertiesConfiguration
{
    /**the configuration attribute <code>collapsed</code>*/
    private static final String GRUP_COLLAPSED_STATUS_ATTRIBUTE_NAME = "collapsed";
    /**the configuration attribute <code>id</code>*/
    private static final String GROUP_ID_ATTRIBUTE_NAME = "id";
    /**the configuration attribute <code>readonly</code>*/
    private static final String READONLY_ATTRIBUTE_NAME = "readonly";
    /**the configuration element <code>PropertyGroups</code>*/
    private static final String PROPERTY_GROUPS_ELEMENT_NAME = "PropertyGroups";
    /**the configuration attribute <code>displayname</code>*/
    private static final String GROUP_DISPLAY_NAME_ATTRIBUTE_NAME = "displayname";
    /** class logger*/
    private static Logger LOG = OwLogCore.getLogger(OwGroupPropertiesConfiguration.class);
    /** flag indicating that the grouping feature is configured*/
    private boolean m_isConfigured = false;
    /** map between group name and {@link OwPropertyGroup} object*/
    private Map<String, OwPropertyGroup> m_propertyGroups = null;
    /** map between property name and its read-only status*/
    private Map<String, Boolean> m_readOnlyProperties;
    /** list containing all property names*/
    private LinkedList<String> m_allPropertyNames;

    /**
     * <p>
     *  Holds the grouped properties.
     * </p>
     * 
     *<p><font size="-2">
     * Alfresco Workdesk<br/>
     * Copyright (c) Alfresco Software, Inc.<br/>
     * All rights reserved.<br/>
     * <br/>
     * For licensing information read the license.txt file or<br/>
     * go to: http://wiki.alfresco.com<br/>
     *</font></p>
     *@since 3.1.0.0
     */
    public static class OwPropertyGroup implements org.alfresco.wd.ui.conf.prop.OwPropertyGroup<OwPropertyInfo>
    {
        /** the group id*/
        private String m_groupId;
        /** the group display name*/
        private String m_groupDisplayName;
        /** flag indicating the collapsed status of the group*/
        private boolean m_isCollapsed = false;
        /** properties configured for this group*/
        private List<OwPropertyInfo> m_properties;
        /** plugin id*/
        private String m_pluginId;

        /**
         * Constructor
         * @param groupId_p - the group id
         * @param groupDisplayName_p - the group display name
         * @param pluginId_p - the plugin id
         * @param propertyInfos_p - a {@link List} of {@link OwPropertyInfo} objects.
         * @param isCollapsed_p - collapsed status
         */
        public OwPropertyGroup(String groupId_p, String groupDisplayName_p, String pluginId_p, List<OwPropertyInfo> propertyInfos_p, boolean isCollapsed_p)
        {
            m_groupId = groupId_p;
            m_groupDisplayName = groupDisplayName_p;
            m_properties = propertyInfos_p;
            m_pluginId = pluginId_p;
            m_isCollapsed = isCollapsed_p;
        }

        /**
         * Getter for group id.
         * @return - the group id
         */
        public String getGroupId()
        {
            return m_groupId;
        }

        /**
         * Getter for properties.
         * @return - a {@link List} of {@link OwPropertyInfo} objects.
         */
        public List<OwPropertyInfo> getProperties()
        {
            return Collections.unmodifiableList(m_properties);
        }

        /**
         * Get the group display name.
         * @param context_p - the context.
         * @return - the localized group name (key format: <code>&lt;plugin_id&gt;.&lt;group_id&gt;</code>), or the configured display name
         */
        public String getDisplayName(OwAppContext context_p)
        {
            return context_p.localize(getPluginId() + "." + m_groupId, m_groupDisplayName);
        }

        /**
         * Set the collapsed status.
         * @param isCollapsed_p - the collapsed status.
         */
        public void setCollapsed(boolean isCollapsed_p)
        {
            m_isCollapsed = isCollapsed_p;
        }

        /**
         * Getter for collapsed status.
         * @return - the collapsed status.
         */
        public boolean isCollapsed()
        {
            return m_isCollapsed;
        }

        protected String getPluginId()
        {
            return m_pluginId;
        }
    }

    /**
     * Constructor
     * @param node_p - the {@link OwXMLUtil} node
     * @param context_p - the application context
     * @throws OwConfigurationException - thrown when the XML section for grouping is not properly configured. 
     */
    public OwGroupPropertiesConfiguration(OwXMLUtil node_p, OwMainAppContext context_p) throws OwConfigurationException
    {
        try
        {
            String pluginId = node_p.getSafeTextValue("id", null);
            OwXMLUtil groupsUtil = node_p.getSubUtil(PROPERTY_GROUPS_ELEMENT_NAME);
            m_propertyGroups = new LinkedHashMap<String, OwPropertyGroup>();
            m_readOnlyProperties = new HashMap<String, Boolean>();
            m_allPropertyNames = new LinkedList<String>();
            if (groupsUtil != null)
            {
                List safeNodeList = groupsUtil.getSafeNodeList();
                Iterator groupsIterator = safeNodeList.iterator();
                while (groupsIterator.hasNext())
                {
                    OwXMLUtil group = new OwStandardXMLUtil((Node) groupsIterator.next());

                    String groupId = group.getSafeStringAttributeValue(GROUP_ID_ATTRIBUTE_NAME, null);
                    String groupDisplayName = group.getSafeStringAttributeValue(GROUP_DISPLAY_NAME_ATTRIBUTE_NAME, null);
                    boolean isCollapsed = group.getSafeBooleanAttributeValue(GRUP_COLLAPSED_STATUS_ATTRIBUTE_NAME, false);
                    List properties = group.getSafeNodeList();
                    if (properties != null)
                    {
                        List<OwPropertyInfo> propertyInfos = new LinkedList<OwPropertyInfo>();

                        Iterator it = properties.iterator();
                        while (it.hasNext())
                        {
                            OwXMLUtil propertyConfig = new OwStandardXMLUtil((Node) it.next());
                            String propertyName = propertyConfig.getSafeTextValue(null);
                            boolean fReadOnly = propertyConfig.getSafeBooleanAttributeValue(READONLY_ATTRIBUTE_NAME, false);
                            if (null != propertyName)
                            {
                                propertyInfos.add(new OwPropertyInfo(propertyName, fReadOnly));
                                if (fReadOnly)
                                {
                                    m_readOnlyProperties.put(propertyName, Boolean.TRUE);
                                }
                                m_allPropertyNames.add(propertyName);
                            }
                        }
                        OwPropertyGroup theGroup = new OwPropertyGroup(groupId, groupDisplayName, pluginId, propertyInfos, isCollapsed);
                        m_propertyGroups.put(theGroup.getGroupId(), theGroup);
                    }
                }
                m_isConfigured = true;

            }
        }
        catch (Exception e)
        {
            LOG.error("Configuration for property grouping could not be loaded. Reason: " + e.getMessage(), e);
            throw new OwConfigurationException(context_p.localize("app.OwConfiguration.groupproperties.cannotconfigure", "Configuration for property grouping could not be loaded."), e);
        }
    }

    /** 
     * Get configured status.
     * @return - <code/>true</code> if the grouping properties is configured. 
     */
    public boolean isConfigured()
    {
        return m_isConfigured;
    }

    /**
     * Get the configured groups.
     * @return - the configured groups.
     */
    public List<OwPropertyGroup> getGroups()
    {
        return new LinkedList<OwPropertyGroup>(m_propertyGroups.values());
    }

    /**
     * Get a group by its id.
     * @param groupId_p - the group id, return <code>null</code> if the wrong id is given as parameter.
     * @return - the {@link OwPropertyGroup} configured for this id, or  <code>null</code> if the group with the given id doesn't exist.
     */
    public OwPropertyGroup getGroupById(String groupId_p)
    {
        return m_propertyGroups.get(groupId_p);
    }

    /** 
     * Getter for read only properties.
     * @return - the read only properties Map.
     */
    public Map<String, Boolean> getReadOnlyProperties()
    {
        return m_readOnlyProperties;
    }

    /**
     * Get all properties names.
     * @return - all properties configured in all groups
     */
    public List<String> getAllPropertyNames()
    {
        return m_allPropertyNames;
    }
}
