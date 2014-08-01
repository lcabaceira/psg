package com.wewebu.ow.server.app;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.history.OwStandardHistoryObjectChangeEvent;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base Class for Document Function plugins.
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
public abstract class OwDocumentFunction extends OwFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwDocumentFunction.class);

    /** icon URL 16x16 pixels */
    private String m_strIconURL;
    /** icon URL 24x24 pixels */
    private String m_strBigIconURL;

    /** set of the supported object types as defined in OwObject */
    private Set m_supportedObjectTypes;

    private List<String> retrievalPropertyNames;

    /** set the plugin description node 
     * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
     * @param context_p OwMainAppContext
     */
    @SuppressWarnings("unchecked")
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        String escapedToolTip = OwHTMLHelper.encodeToSecureHTML(getTooltip());
        // set icon URL 
        m_strIconURL = "<img class=\"OwFunctionIcon\" title=\"" + escapedToolTip + "\" alt=\"" + escapedToolTip + "\" src=\"" + getIcon() + "\">";
        m_strBigIconURL = "<span class=\"OwFunctionBigIcon\" title=\"" + escapedToolTip + "\"  style=\"background-image: url(" + getBigIcon() + ")\"></span>";

        // === get the supported object type map for the plugin
        m_supportedObjectTypes = getSupportedObjectTypesFromDescriptor(DESCRIPTION_KEY_OBJ_TYPES);
        if (null == m_supportedObjectTypes)
        {
            String msg = "OwDocumentFunction.init: At least one object type must be defined in plugin = " + getPluginID();
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
        List<String> propertyNamesList = getConfigNode().getSafeStringList("RetrievalProperties");
        if (!propertyNamesList.isEmpty())
        {
            this.retrievalPropertyNames = Collections.unmodifiableList(propertyNamesList);
        }
    }

    /** get column title if defined
     * @return String title HTML or empty string if not defined 
     * */
    public String getColumnTitle()
    {
        String sTitle = getConfigNode().getSafeTextValue("ColumnTitle", "");

        if (sTitle.equals("OW_ICON"))
        {
            return m_strIconURL;
        }
        else
        {
            return sTitle;
        }
    }

    /** check if plugin should generate a click event when the user clicked on the column (only when own column was defined using getColumnTitle())
    *
    * @return boolean true = enable plugin only, if a oParent_p parameter is available for the selected object, false = oParent_p can be null.
    */
    public boolean getEnableColumnClickEvent()
    {
        try
        {
            return OwXMLDOMUtil.getSafeBooleanAttributeValue(getConfigNode().getSubNode("ColumnTitle"), "clickevent", false);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** check if plugin needs oParent_p parameter in onClick handler
     *
     * @return boolean true = enable plugin only, if a oParent_p parameter is available for the selected object, false = oParent_p can be null.
     */
    public boolean getNeedParent()
    {
        return false;
    }

    /** get property from the XML plugin config node
     * 
     * @return boolean true = plugin supports multiselection and handles onMultiSelectClick, false = plugin can only work on one instance
     */
    public boolean getMultiselect()
    {
        return getConfigNode().getSafeBooleanValue("Multiselect", false);
    }

    /** get property from the XML plugin config node
     * 
     * @return boolean  true = plugin will be available next to each instance of OwObject, false = it will not be available next to the objectinstance
     */
    public boolean getObjectInstance()
    {
        return getConfigNode().getSafeBooleanValue("ObjectInstance", false);
    }

    /** get property from the XML plugin config node
     * 
     * @return boolean true = plugin will be available in object editing dialogs like OwEditPropertiesDialog
     */
    public boolean getShowInEditViews()
    {
        return getConfigNode().getSafeBooleanValue("ShowInEditViews", false);
    }

    /** get property from the XML plugin config node
     * 
     * @return boolean  true = plugin will be available in the context menu
     */
    public boolean getContextMenu()
    {
        return getConfigNode().getSafeBooleanValue("ContextMenu", false);
    }

    /** get the HTML code for the small (16x16 pixels) icon for this plugin to be displayed in the object list.
     *  An anchor tag is wrapped around this HTML code to trigger events for the plugin.
     *
     *  NOTE: you can return arbitrary HTML here, if you do not want to display an icon.
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return HTML code to be inserted for the document function plugin. 
     */
    public String getIconHTML(OwObject oObject_p, OwObject oParent_p) throws Exception
    {
        return m_strIconURL;
    }

    /** get the HTML code for the big (24 x 24 Pixels )icon for this plugin to be displayed in the object list.
     *  An anchor tag is wrapped around this HTML code to trigger events for the plugin.
     *
     *  NOTE: you can return arbitrary HTML here, if you do not want to display an icon.
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return HTML code to be inserted for the document function plugin. 
     */
    public String getBigIconHTML(OwObject oObject_p, OwObject oParent_p) throws Exception
    {
        return m_strBigIconURL;
    }

    /** get the label for the plugin, used in menus
     *
     *  Used for context menus where no object information is available
     *
     *  @return tooltip code to be inserted for the document function plugin. 
     */
    public String getDefaultLabel() throws Exception
    {
        return getPluginTitle();
    }

    /** get the label for the plugin, used in menus
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return tooltip code to be inserted for the document function plugin. 
     */
    public String getLabel(com.wewebu.ow.server.ecm.OwObject oObject_p, com.wewebu.ow.server.ecm.OwObject oParent_p) throws Exception
    {
        return getPluginTitle();
    }

    /** get the HTML code for the small (16x16 pixels) icon for this plugin to be displayed in the object list.
     *  An anchor tag is wrapped around this HTML code to trigger events for the plugin.
     *
     *  Used for context menus where no object information is available
     *
     *  NOTE: you can return arbitrary HTML here, if you do not want to display an icon.
     *
     *  @return HTML code to be inserted for the document function plugin. 
     */
    public String getDefaultIconHTML() throws Exception
    {
        return m_strIconURL;
    }

    /** get the HTML code for the big (24x24 pixels) icon for this plugin to be displayed in the object list.
     *  An anchor tag is wrapped around this HTML code to trigger events for the plugin.
     *
     *  Used for context menus where no object information is available
     *
     *  NOTE: you can return arbitrary HTML here, if you do not want to display an icon.
     *
     *  @return HTML code to be inserted for the document function plugin. 
     */
    public String getBigDefaultIconHTML() throws Exception
    {
        return m_strBigIconURL;
    }

    /** check if the object type is supported by the plugin, used to filter out plugins
     *
     * NOTE: Only used to preselect plugins, you will still have to call isEnabled, 
     *       to find out exactly if plugin supports a specific object
     *
     * @param iObjectType_p int OwObject type
     *
     * @return true = object type is supported, false otherwise
     */
    public boolean isObjectTypeSupported(int iObjectType_p)
    {
        return m_supportedObjectTypes.contains(Integer.valueOf(iObjectType_p));
    }

    /** check if function is enabled for the given object parameters (called only for single select operations)
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param iContext_p OwStatusContextDefinitions
     *
     *  @return true = enabled, false otherwise
     */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        // default implementation checks against set in plugin description node
        return (isObjectTypeSupported(oObject_p.getType()) && isObjectClassSupported(oObject_p.getObjectClass().getClassName()));
    }

    /** check if function is enabled for the given objects (called only for multi select operations)
    *
    *  @param objects_p Collection of OwObject 
    *  @param oParent_p Parent which listed the Object
    *  @param iContext_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(Collection objects_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (objects_p == null)
        {
            return false;
        }
        Iterator it = objects_p.iterator();
        while (it.hasNext())
        {
            if (!this.isEnabled((OwObject) it.next(), oParent_p, iContext_p))
            {
                return false;
            }
        }
        // default implementation checks against set in plugin description node
        return true;
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public abstract void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception;

    /** event called when user clicked the plugin for multiple selected items
     *
     *  @param objects_p Collection of OwObject 
     *  @param oParent_p Parent which listed the Objects
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        throw new OwInvalidOperationException("OwDocumentFunction.onMultiselectClickEvent: No multiselection operation allowed.");
    }

    /** event called when user clicked the plugin column (only when own column was defined using getColumnTitle())
    *
    *  @param objects_p OwObjectCollection of all OwObject s in the list 
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onColumnClickEvent(OwObjectCollection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // default calls multiselect handler
        OwMultipleSelectionCall multiCall = new OwMultipleSelectionCall(this, objects_p, oParent_p, refreshCtx_p, getContext());
        multiCall.invokeFunction();
    }

    /** add the plugin invoke event to the history manager
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param  iEventType_p int one out of:  
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT
     *  @param iStatus_p int Status as defined in OwEventManger.HISTORY_STATUS_...
     */
    protected void addHistoryEvent(OwObject oObject_p, OwObject oParent_p, int iEventType_p, int iStatus_p) throws Exception
    {
        // === historize event
        getEventManager().addEvent(iEventType_p, getPluginID(), new OwStandardHistoryObjectChangeEvent(oObject_p, oParent_p), iStatus_p);
    }

    /** add the plugin invoke event to the history manager
     *
     *  @param objects_p Collection of OwObject 
     *  @param oParent_p Parent which listed the Object
     *  @param  iEventType_p int one out of:  
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT
     *  @param iStatus_p int Status as defined in OwEventManger.HISTORY_STATUS_...
     */
    protected void addHistoryEvent(Collection objects_p, OwObject oParent_p, int iEventType_p, int iStatus_p) throws Exception
    {
        // === historize event
        getEventManager().addEvent(iEventType_p, getPluginID(), new OwStandardHistoryObjectChangeEvent(objects_p, oParent_p), iStatus_p);
    }

    /** get the plugin type
     * @return String as defined in OwConfiguration.PLUGINTYPE_...
     */
    public final String getPluginType()
    {
        return OwBaseConfiguration.PLUGINTYPE_DOCUMENT_FUNCTION;
    }

    /** Get a collection of property names that are needed to display the Objects in the list
     *  i.e. these properties should be requested in advance to save server roundtrips.
     *  <p><b>ATTENTION</b>: Define here the collection of property names, which are
     *  needed by this document function! This method is used by other plugins to
     *  create a request with all properties against the (back-end) ECM system, 
     *  so no more roundtrips have to be made by additional retrieval.
     *  </p>
     *  @return Collection of String, or null if no properties are needed
     */
    public Collection getRetrievalPropertyNames() throws Exception
    {
        return this.retrievalPropertyNames;
    }

    /**
     * Helper method that returns the display name of the user with a certain user id (login name), 
     * if the adapter implements or allows the methods, if not: the method returns the passed user id.<br/>
     * This method is used by a document function plugin to return information about a user,
     * because it cannot be executed, e.g.:<BR> 
     * - a object is checked out by a certain user:get the display name of this user<br/>
     * - a plugin cannot perform an action because the object is locked by a certain user<br/>
     * - a document is not enable, get detailed information why, check the user that is involved.
     * @param checkedoutByID_p user id which should be used to return the display name
     * @return display name of the user
     * @since 3.1.0.0
     */
    protected String getDisplayNameFromUserId(String checkedoutByID_p)
    {
        OwUserInfo userInfo = null;
        try
        {
            userInfo = getContext().getNetwork().getUserFromID(checkedoutByID_p);
        }
        catch (Exception e)
        {
            //getUserFromID() is not supported or not implemented or exception occurred
        }
        if (userInfo != null)
        {
            String userDisplayName = null;
            try
            {
                userDisplayName = userInfo.getUserDisplayName();
            }
            catch (Exception e)
            {
                //getUserDisplayName()is not supported or not implemented or exception occurred
            }
            if (userDisplayName != null && !userDisplayName.equals(""))
            {
                checkedoutByID_p = userDisplayName;
            }
        }
        return checkedoutByID_p;
    }
}