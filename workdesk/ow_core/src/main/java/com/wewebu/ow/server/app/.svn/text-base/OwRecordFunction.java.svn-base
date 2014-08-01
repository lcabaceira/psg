package com.wewebu.ow.server.app;

import java.util.Collection;
import java.util.Properties;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.history.OwStandardHistoryObjectChangeEvent;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Base class for Record Function plugins, used in the record plugin only.
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
public abstract class OwRecordFunction extends OwFunction
{
    /** icon URL 16x16 pixels */
    private String m_strIconURL;
    /** icon URL 24x24 pixels */
    private String m_strBigIconURL;

    /** set the plugin description node 
     * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
     * @param context_p OwMainAppContext
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        // set icon URL 
        m_strIconURL = "<span class=\"OwFunctionIcon\" style=\"background-image: url(" + getIcon() + ")\"></span>";
        m_strBigIconURL = "<span class='OwFunctionBigIcon' style='background-image: url(" + getBigIcon() + ")'></span>";
    }

    /** get the HTML code for the small (16 x16 pixels) icon for this plugin to be displayed in the object list.
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

    /** get the HTML code for the small (16x16 pixels) icon for this plugin to be displayed in the object list.
     *  An anchor tag is wrapped around this HTML code to trigger events for the plugin.
     *
     *  NOTE: you can return arbitrary HTML here, if you do not want to display an icon.
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *
     *  @return HTML code to be inserted for the document function plugin. 
     */
    public String getIconHTML(OwObject rootObject_p, OwObject folderObject_p) throws Exception
    {
        return m_strIconURL;
    }

    /** get the HTML code for the small (24x24 pixels) icon for this plugin to be displayed in the object list.
     *  An anchor tag is wrapped around this HTML code to trigger events for the plugin.
     *
     *  NOTE: you can return arbitrary HTML here, if you do not want to display an icon.
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *
     *  @return HTML code to be inserted for the document function plugin. 
     */
    public String getBigIconHTML(OwObject rootObject_p, OwObject folderObject_p) throws Exception
    {
        return m_strBigIconURL;
    }

    /** get the label for the plugin, used in menus
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *
     *  @return tooltip code to be inserted for the document function plugin. 
     */
    public String getLabel(OwObject rootObject_p, OwObject folderObject_p) throws Exception
    {
        return getPluginTitle();
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

    /** check if function is enabled for the given object parameters
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *  @param iContext_p OwStatusContextDefinitions
     *
     *  @return true = enabled, false otherwise
     */
    public boolean isEnabled(OwObject rootObject_p, OwObject folderObject_p, int iContext_p) throws Exception
    {
        if (null == folderObject_p)
        {
            return false;
        }

        return isObjectClassSupported(folderObject_p.getObjectClass().getClassName());
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

    /** check if plugin allows multiple files to be dropped on it
     * 
     * @return true = allow multiple files to be dropped on the plugin, false only single files may be dropped
     */
    public boolean isMultifileDragDropAllowed()
    {
        // plugins do not support multi file drag and drop by default
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

    /** event called when user clicked the plugin label / icon 
     *  overridable
    *
    *  @param rootObject_p OwObject root folder to work on
     * @param folderObject_p OwObject selected folder to work on
     * @param subpath_p String subpath of subfolder or null if root folder
     * @param subdisplaypath_p String subpath of subfolder
     * @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onClickEvent(OwObject rootObject_p, OwObject folderObject_p, String subpath_p, String subdisplaypath_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // call default handler
        onClickEvent(rootObject_p, folderObject_p, refreshCtx_p);
    }

    /** event called when user clicked the plugin label / icon 
      *  overridable
      *
      *  @param rootObject_p OwObject root folder to work on
      *  @param folderObject_p OwObject selected folder to work on
      *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
      *
      */
    public void onClickEvent(OwObject rootObject_p, OwObject folderObject_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // to be implemented by derived class
    }

    /** add the plugin invoke event to the history manager 
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param objects_p OwObject objects that have to be audited
     *  @param  iEventType_p int one out of:  
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW
     *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT
     *  @param iStatus_p int Status as defined in OwEventManger.HISTORY_STATUS_...
     */
    public void addHistoryEvent(OwObject rootObject_p, Collection objects_p, int iEventType_p, int iStatus_p) throws Exception
    {
        // === historize event
        getEventManager().addEvent(iEventType_p, getPluginID(), new OwStandardHistoryObjectChangeEvent(objects_p, rootObject_p), iStatus_p);
    }

    /** add the plugin invoke event to the history manager 
    *
    *  @param rootObject_p OwObject root folder to work on
    *  @param folderObject_p OwObject selected folder to work on
    *  @param  iEventType_p int one out of:  
    *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI
    *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW
    *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT
    *  @param iStatus_p int Status as defined in OwEventManger.HISTORY_STATUS_...
    */
    protected void addHistoryEvent(OwObject rootObject_p, OwObject folderObject_p, int iEventType_p, int iStatus_p) throws Exception
    {
        // === historize event
        getEventManager().addEvent(iEventType_p, getPluginID(), new OwStandardHistoryObjectChangeEvent(folderObject_p, rootObject_p), iStatus_p);
    }

    /** get the plugin type
     * @return String as defined in OwConfiguration.PLUGINTYPE_...
     */
    public String getPluginType()
    {
        return OwBaseConfiguration.PLUGINTYPE_RECORD_FUNCTION;
    }
}