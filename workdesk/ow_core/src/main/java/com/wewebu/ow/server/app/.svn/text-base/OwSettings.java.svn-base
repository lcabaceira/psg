package com.wewebu.ow.server.app;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStandardContentCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.settingsimpl.OwSettingsPropertyLanguageString;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Settings which hold the user and application settings, which can be changed during runtime.<br/>
 * The settings store the nodes in two XML documents. One for the user settings and one for the application settings.
 * Since the setting nodes come from several plugins and may contain user and application nodes, 
 * they must be mapped two the two XML documents. <br/><br/>
 * <b>NOTE:</b> Settings are available upon login, not before. <br/>
 * Unlike configuration node entries, the settings are stored for each user or for a site, 
 * where the configuration via getConfigNode is only set during startup <br/><br/>
 * You can retrieve settings by calling getSafeSetting form within your plugin function, document or view class.
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
public class OwSettings
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwSettings.class);

    protected static final String BASE_NODE_NAME = "OwSettings";

    /** reference to the application context, used to read and write Configuration. */
    protected OwMainAppContext m_Context;

    /** OwObject holding user scope preferences, one for each user  */
    protected OwObject m_UserPreferences;
    /** OwObject holding application scope preferences, identical for all users  */
    protected OwObject m_SitePreferences;

    /** map containing the settings nodes */
    protected Map m_SettingsSetMap;
    /** the above map is not ordered, so we need a backup ordered list. With JDK 1.4.x we can use a ordered HashMap. */
    protected List m_SettingsSetList;

    /** Creates a new instance of OwSettings, must be called after login */
    public OwSettings(OwMainAppContext context_p) throws Exception
    {
        m_Context = context_p;

        try
        {
            // load OwObject reference for user settings from persistent store
            m_UserPreferences = (OwObject) m_Context.getNetwork().getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_PREFERENCES, "OwSettings", true, true);
        }
        catch (Exception e)
        {
            LOG.error("Could not access user preference object.", e);
            //throw new OwConfigurationException(m_Context.localize("app.OwSettings.userprefobjectloadfailed","Could not access user settings:") + e.getLocalizedMessage());
        }

        try
        {
            // load OwObject reference for app settings from persistent store
            m_SitePreferences = (OwObject) m_Context.getNetwork().getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_PREFERENCES, "OwSettings", false, true);
        }
        catch (Exception e)
        {
            LOG.error("Could not access site preference object.", e);
            //            throw new OwConfigurationException(m_Context.localize("app.OwSettings.siteprefobjectloadfailed","Could not access site settings. The XML object could not be read or created."));
        }

        try
        {
            // on startup load all settingsset instances for all plugins that define settings
            loadSettingsFromPlugDescriptor();
        }
        catch (Exception e)
        {
            throw new OwConfigurationException(m_Context.localize("app.OwSettings.parsefailed", "The user or site settings could not be read. The XML object is incorrect."), e);
        }
    }

    /** load the DOM persistent sets from ECM
     */
    private OwXMLUtil getPersistentUserNode() throws Exception
    {
        OwXMLUtil userNode = null;
        if ((m_UserPreferences != null) && m_UserPreferences.canGetContent(OwContentCollection.CONTENT_TYPE_DOCUMENT, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) && (m_UserPreferences.getContentCollection().getPageCount() > 0))
        {
            // load user DOM node from user object
            try
            {
                InputStream contentStream = m_UserPreferences.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null);
                userNode = new OwStandardXMLUtil(contentStream, BASE_NODE_NAME);
            }
            catch (OwObjectNotFoundException e)
            {
                // ignore
            }
        }

        return userNode;
    }

    /** load the DOM persistent sets from ECM
     */
    private OwXMLUtil getPersistentAppNode() throws Exception
    {
        OwXMLUtil appNode = null;
        if ((m_SitePreferences != null) && m_SitePreferences.canGetContent(OwContentCollection.CONTENT_TYPE_DOCUMENT, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) && (m_SitePreferences.getContentCollection().getPageCount() > 0))
        {
            // load app DOM node from app object
            try
            {
                InputStream contentStream = m_SitePreferences.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null);
                appNode = new OwStandardXMLUtil(contentStream, BASE_NODE_NAME);
            }
            catch (OwObjectNotFoundException e)
            {
                // ignore
            }
        }

        return appNode;
    }

    /** reload the setting */
    public void refresh() throws Exception
    {
        m_SettingsSetMap = null;
        m_SettingsSetList = null;

        loadSettingsFromPlugDescriptor();
    }

    /** load the default settings from the plugin descriptor and map them to the plugin ID 
     *
     */
    private void loadSettingsFromPlugDescriptor() throws Exception
    {
        if (m_SettingsSetMap == null)
        {
            // === load the DOM persistent sets from ECM
            OwXMLUtil userNode = getPersistentUserNode();
            OwXMLUtil appNode = getPersistentAppNode();

            // === load property sets for each plugin
            // map for the created sets (one set for each plugin)
            m_SettingsSetMap = new HashMap();
            // ordered backup list of sets
            m_SettingsSetList = new ArrayList();

            // iterate over all plugin types
            for (int i = 0; i < OwBaseConfiguration.getPluginTypeDefinitions().length; i++)
            {
                // get plugin list of XML descriptions for this type
                List pluginList = m_Context.getConfiguration().getAllowedPlugins(OwBaseConfiguration.getPluginTypeDefinitions()[i].getType());

                // check, if for app type there is on definition
                if (OwBaseConfiguration.getPluginTypeDefinitions()[i].getType().equals(OwBaseConfiguration.PLUGINTYPE_APPLICATION) && (pluginList == null))
                {
                    // === missing app definition
                    LOG.warn("OwSettings.loadSettingsFromPlugDescriptor: ow_app plugin definition missing, using default settings for application.");
                }

                if (pluginList != null)
                {
                    // iterate over the list of plugins for this type
                    Iterator it = pluginList.iterator();
                    while (it.hasNext())
                    {
                        // get the plugin description node 
                        OwXMLUtil pluginDescriptionNode = (OwXMLUtil) it.next();

                        // get node settingsset if defined in plugin descriptor
                        Node pluginSettingsNode = pluginDescriptionNode.getSubNode(OwBaseConfiguration.PLUGIN_NODE_SETTINGS_SET);
                        if (pluginSettingsNode != null)
                        {
                            // get the ID of the plugin, which acts as the name of the settings set
                            String strPluginID = pluginDescriptionNode.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, null);

                            // === the pluginSettingsNode contains only the default value of the settings,
                            // === so we must now overwrite with the actual values from the persistent store in the ECM adapter.
                            // === i.e. get values from user settings XML document
                            Node currentUserValueNodeSet = null;
                            if (userNode != null)
                            {
                                // === persistent user values available
                                // get the subnode with the requested set, using the the set name (plugin id)
                                currentUserValueNodeSet = userNode.getSubNode(strPluginID);
                            }

                            Node currentAppValueNodeSet = null;
                            if (appNode != null)
                            {
                                // === persistent app values available
                                // get the subnode with the requested set, using the the set name (plugin id)
                                currentAppValueNodeSet = appNode.getSubNode(strPluginID);
                            }

                            // create the property instances for the set
                            Map properties = createProperties(pluginSettingsNode, strPluginID, currentUserValueNodeSet, currentAppValueNodeSet);

                            // save in map
                            OwSettingsSet newSet = new OwSettingsSet(strPluginID, properties, m_Context.getConfiguration().getLocalizedPluginTitle(pluginDescriptionNode));
                            m_SettingsSetMap.put(strPluginID, newSet);
                            // add to ordered list as well
                            m_SettingsSetList.add(newSet);
                        }
                    }
                }
            }
        }
    }

    /** iterates over the settings node and creates a OwSettingsProperty for each entry
     *
     * @param pluginSettingsNode_p the default node from the plugin descriptor
     * @param strPluginID_p String ID of the plugin (i.e. set name) for which the properties are to created
     * @param currentUserValueNodeSet_p OwXMLUtil wrapped node with current user settings
     *
     * @return Map of OwSettingsProperty key to the setting node name
     *
     */
    private Map createProperties(Node pluginSettingsNode_p, String strPluginID_p, Node currentUserValueNodeSet_p, Node currentAppValueNodeSet_p) throws Exception
    {
        Map propertyMap = new LinkedHashMap();

        // === create wrapper for value nodes
        OwXMLUtil currentUserValueNodeSet = null;
        OwXMLUtil currentAppValueNodeSet = null;

        if (currentUserValueNodeSet_p != null)
        {
            currentUserValueNodeSet = new OwStandardXMLUtil(currentUserValueNodeSet_p);
        }

        if (currentAppValueNodeSet_p != null)
        {
            currentAppValueNodeSet = new OwStandardXMLUtil(currentAppValueNodeSet_p);
        }

        // === get the settings value children
        for (Node propertyDefinitionNode = pluginSettingsNode_p.getFirstChild(); propertyDefinitionNode != null; propertyDefinitionNode = propertyDefinitionNode.getNextSibling())
        {
            if (propertyDefinitionNode.getNodeType() == Node.ELEMENT_NODE)
            {
                // set default
                Node currentValueNode = null;

                if (propertyDefinitionNode.getAttributes().getNamedItem("scope").getNodeValue().equals("user"))
                {
                    // === is a value defined in user node ?
                    if (currentUserValueNodeSet != null)
                    {
                        currentValueNode = currentUserValueNodeSet.getSubNode(propertyDefinitionNode.getNodeName());
                    }
                }
                else
                {
                    // === is a value defined in app node ?
                    if (currentAppValueNodeSet != null)
                    {
                        currentValueNode = currentAppValueNodeSet.getSubNode(propertyDefinitionNode.getNodeName());
                    }
                }

                if (currentValueNode == null)
                {
                    // value is not yet defined in currentUser/AppValue set, so set to default
                    currentValueNode = propertyDefinitionNode;
                }

                // create new property 
                OwSettingsProperty newProperty = createNewProperty(propertyDefinitionNode, currentValueNode, strPluginID_p);

                // add to map
                propertyMap.put(newProperty.getName(), newProperty);
            }
        }

        return propertyMap;
    }

    /** create a property instance for the given property definition node and value node
     *
     * @param propertyDefinitionNode_p defining node of a single plugin property with default value
     * @param currentValueNode_p actual current value of property
     * @param strSetName_p name of the property set for which the property is created
     * @return OwSettingsProperty instance
     */
    private OwSettingsProperty createNewProperty(Node propertyDefinitionNode_p, Node currentValueNode_p, String strSetName_p) throws Exception
    {
        String strJavaClassName = OwXMLDOMUtil.getSafeStringAttributeValue(propertyDefinitionNode_p, "type", null);

        // create property instance 
        Class propClass = Class.forName(strJavaClassName);
        OwSettingsProperty newProperty = (OwSettingsProperty) propClass.newInstance();

        if (newProperty instanceof OwSettingsPropertyLanguageString)
        {
            OwSettingsPropertyLanguageString dateProperty = (OwSettingsPropertyLanguageString) newProperty;
            dateProperty.setLocaleFromContext(m_Context);

        }

        // init, i.e. set value from value and description node
        newProperty.init(propertyDefinitionNode_p, currentValueNode_p, strSetName_p);

        // register as event target, only if property is editable
        if (newProperty.isEditable())
        {
            ((OwSettingsPropertyControl) newProperty).attach(m_Context, null);
        }

        return newProperty;
    }

    /** save the user preferences
     */
    public void saveUserPrefs() throws Exception
    {
        savePrefs(m_UserPreferences, true);
    }

    /** save the app preferences
     */
    public void saveSitePrefs() throws Exception
    {
        savePrefs(m_SitePreferences, false);
    }

    protected void savePrefs(OwObject prefsObject_p, boolean fUser_p) throws Exception
    {
        if (prefsObject_p != null)
        {
            OwXMLDOMUtil saveNode = new OwXMLDOMUtil(BASE_NODE_NAME);

            // === iterate over the settings info and store the properties to user prefs
            Iterator setInfoIterator = m_SettingsSetMap.values().iterator();
            while (setInfoIterator.hasNext())
            {
                OwSettingsSet info = (OwSettingsSet) setInfoIterator.next();

                // === add node for this info set ( plugin )
                Node infoSetNode;
                try
                {
                    infoSetNode = saveNode.addNode(info.getName());
                }
                catch (org.w3c.dom.DOMException ex)
                {
                    LOG.fatal("== cannot create node for infoSetName = " + info.getName());
                    throw ex;
                }

                // === iterate over the properties of a settings info
                Iterator propertyIterator = info.getProperties().values().iterator();
                while (propertyIterator.hasNext())
                {
                    OwSettingsProperty prop = (OwSettingsProperty) propertyIterator.next();

                    if (prop.isUser() == fUser_p)
                    {
                        // === user property, so save to user node
                        infoSetNode.appendChild(prop.getValueNode(infoSetNode.getOwnerDocument()));
                    }
                }
            }

            // === save user node to ECM Object
            prefsObject_p.setContentCollection(new OwStandardContentCollection(saveNode.getInputStream(), OwContentCollection.CONTENT_TYPE_DOCUMENT, 1));
        }
    }

    /** get collection to the settingssets
     *
     * @return Collection of OwSettingsSet settings nodes
     */
    public Collection getCollection()
    {
        // return list iterator for determined order
        return m_SettingsSetList;
    }

    /** retrieve a OwSettingsProperty
     *
     * @param strID_p String the ID of the settingsset, i.e. the plugin ID for which the setting is to be retrieved
     * @param strName_p Name of setting property to retrieve
     *
     * @return OwSettingsProperty
     */
    public OwSettingsProperty getProperty(String strID_p, String strName_p) throws Exception
    {
        // look up node
        OwSettingsSet info = (OwSettingsSet) m_SettingsSetMap.get(strID_p);
        if (info != null)
        {
            return (OwSettingsProperty) info.getProperties().get(strName_p);
        }
        else
        {
            return null;
        }
    }

    /** retrieve a OwSettingsSet
     *
     * @param strID_p String the ID of the settingsset, i.e. the plugin ID for which the setting is to be retrieved
     *
     * @return OwSettingsSet
     */
    public OwSettingsSet getSettingsInfo(String strID_p) throws Exception
    {
        // look up node
        return (OwSettingsSet) m_SettingsSetMap.get(strID_p);
    }
}