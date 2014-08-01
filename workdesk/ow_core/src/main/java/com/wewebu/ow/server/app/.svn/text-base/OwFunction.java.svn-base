package com.wewebu.ow.server.app;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base class for all function plugins.
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
public abstract class OwFunction implements OwPlugin
{
    /** drag and drop property,see getDragDropProperties() */
    public static final String DRAG_DROP_PROPERTY_MAX_FILESIZE = "max_upload";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwFunction.class);

    /** key for the object types node in the plugin description */
    public static final String DESCRIPTION_KEY_OBJ_TYPES = "objecttypes";
    /** key for the classes types node in the plugin description */
    public static final String DESCRIPTION_KEY_CLASSES = "objectclasses";

    /** OwXMLUtil wrapped DOM Node containing the plugin description */
    private OwXMLUtil m_PluginDescriptionNode;

    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;

    /** key for a object type node in the plugin description */
    public static final String DESCRIPTION_KEY_TYPE = "type";

    /** settings info for the plugin     */
    private OwSettingsSet m_SettingsInfo;

    /** set of the supported object classes, can be null */
    private Set m_supportedObjectClassNames;

    /** set the plugin description node 
     * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
     * @param context_p OwMainAppContext
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        m_PluginDescriptionNode = node_p;
        m_MainContext = context_p;

        // === get settings map for fast access of properties
        m_SettingsInfo = getContext().getSettings().getSettingsInfo(m_PluginDescriptionNode.getSafeTextValue("id", null));

        m_supportedObjectClassNames = getSupportedObjectClassesFromDescriptor(DESCRIPTION_KEY_CLASSES);
    }

    /** get the small (16x16 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/function_plugin.png");
    }

    /** get the big (24x24 pixels) icon URL for this plugin to be displayed
    *
    *  @return String icon URL 
    */
    public String getBigIcon() throws Exception
    {
        // override to return a high resolution image instead
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/function_plugin_24.png");
    }

    /** check if the object class is supported by the plugin, used to filter out plugins
     *
     * NOTE: Only used to preselect plugins, you will still have to call isEnabled, 
     *       to find out exactly if plugin supports a specific object
     *
     * @param strClassName_p OwObject class name
     *
     * @return true = object class is supported, false otherwise
     */
    public boolean isObjectClassSupported(String strClassName_p)
    {
        if (m_supportedObjectClassNames == null)
        {
            return true;
        }
        else
        {
            return m_supportedObjectClassNames.contains(strClassName_p);
        }
    }

    /** get the help path to the plugin */
    public String getHelpPath()
    {
        return getConfigNode().getSafeTextValue("helppath", null);
    }

    /** get the name of the function plugin */
    public String getName()
    {
        return getConfigNode().getSafeTextValue("Name", "unbekannt");
    }

    /** check if the plugin handles events with functioncalls, 
     *  or if it is just used to display an icon.
     *  The default is false, i.e. plugin handles events.
     *
     *  @return boolean false = handles events, true = does not handle events
     */
    public boolean getNoEvent()
    {
        return false;
    }

    /** check if plugin acts as a drag and drop target
     * 
     * @return true = plugin is drag and drop target and can retrieve uploaded files via OwMainAppContext.getDragAndDropUploadDir, false otherwise
     */
    public boolean isDragDropTarget()
    {
        // default, plugins do not support drag and drop
        return false;
    }

    /** get the properties for drag and drop like allowed file size and file count or file type...
     * 
     * @return Properties map of properties as defined with DRAG_DROP_PROPERTY_..., or null if not properties are defined
     */
    public Properties getDragDropProperties()
    {
        return null;
    }

    /** check if plugin should be displayed in context menu */
    public boolean getContextMenu()
    {
        return false;
    }

    /** get the reference to the plugin description node
     * @return OwXMLUtil wrapped DOM node
     */
    public OwXMLUtil getConfigNode()
    {
        return m_PluginDescriptionNode;
    }

    /** get a localized display name
     * @return String
     */
    public String getPluginTitle()
    {
        return getContext().getConfiguration().getLocalizedPluginTitle(getConfigNode());
    }

    /** get the plugin ID 
     * @return String
     */
    public String getPluginID()
    {
        return getConfigNode().getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_ID, null);
    }

    /** get a display name for the plugin type
     * @return String
     */
    public String getPluginTypeDisplayName()
    {
        return OwBaseConfiguration.getPluginTypeDisplayName(getPluginType(), getContext().getLocale());
    }

    /** get a reference to the app context
     * @return OwMainAppContext
     */
    protected OwMainAppContext getContext()
    {
        return m_MainContext;
    }

    /** get a setting for the plugin
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
     * NOTE: You must call OwMainAppContext.saveUserPrefs(); to serialize the new value
    *
    * @param strName_p name of property to retrieve
    * @param value_p Object to set
    *
    */
    public void setSafeSetting(String strName_p, Object value_p) throws Exception
    {
        ((OwSettingsProperty) m_SettingsInfo.getProperties().get(strName_p)).setValue(value_p);
    }

    /** get a reference to the event manager to write history events */
    protected OwEventManager getEventManager()
    {
        return getContext().getHistoryManager();
    }

    /** utility function to retrieve a set of object types from a descriptor definition with the given key
     *
     * The object types can either be the numbers as defined in OwObjectReference or you can use the field names directly, which will then be converted to the integer numbers.
     *
     * @param strKey_p String XML node name for list
     *
     * @return Set of Integer with object types, or null if none is defined
     */
    protected Set getSupportedObjectTypesFromDescriptor(String strKey_p) throws OwConfigurationException
    {
        // === get the supported object type map for the plugin
        List supportetTypesList = getConfigNode().getSafeStringList(strKey_p);
        if (supportetTypesList.size() == 0)
        {
            return null;
        }

        Set retSet = new HashSet();
        for (int i = 0; i < supportetTypesList.size(); i++)
        {
            // === retrieve the named Integer Field Value from OwObjectReference Object type definitions
            try
            {
                retSet.add(OwObjectReference.class.getField(supportetTypesList.get(i).toString()).get(null));
            }
            catch (Exception e)
            {
                // === could not find named definition, so treat as number
                try
                {
                    retSet.add(new Integer(supportetTypesList.get(i).toString()));
                }
                catch (Exception e2)
                {
                    String msg = "Could not resolve objecttype in plugin descriptor = " + supportetTypesList.get(i).toString();
                    LOG.error(msg, e2);
                    throw new OwConfigurationException(msg, e2);
                }
            }
        }

        return retSet;
    }

    /** utility function to retrieve a set of object class names from a descriptor definition with the given key
     *
     * @param strKey_p String XML node name for list
     *
     * @return Set of String with object class names, or null if none is defined
     */
    protected Set getSupportedObjectClassesFromDescriptor(String strKey_p) throws com.wewebu.ow.server.exceptions.OwConfigurationException
    {
        // === get the supported object type map for the plugin
        List supportetTypesList = getConfigNode().getSafeStringList(strKey_p);
        if (supportetTypesList.size() == 0)
        {
            return null;
        }

        Set retSet = new HashSet();
        for (int i = 0; i < supportetTypesList.size(); i++)
        {
            retSet.add(supportetTypesList.get(i));
        }

        return retSet;
    }

    /** 
     *  Get the tooltip text
     *  @return tooltip code to be inserted for the document function plugin. 
     *  @since 3.0.0.0
     */
    public String getTooltip() throws Exception
    {
        return getContext().getConfiguration().getLocalizedPluginDescription(getConfigNode());
    }

}