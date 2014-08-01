package com.wewebu.ow.server.plug.owshortcut;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwConfiguration.OwMasterPluginInstance;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMessageBox;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Short Cuts Function, creates a short cut in the OwShortCutDocument master plugin.
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
public class OwShortCutDocumentFunction extends OwDocumentFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutDocumentFunction.class);

    /** the favorites/bookmarks/shortcuts plugin*/
    private OwShortCutDocument m_favoritePlugin = null;

    /** set the plugin description node 
     * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
     * @param context_p OwMainAppContext
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        m_favoritePlugin = (OwShortCutDocument) getFavoriteMasterPlugin();
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocshortcut/bookmark_add.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocshortcut/bookmark_add_24.png");
    }

    /** event called when user clicked the plugin label / icon 
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        Collection<OwObject> objects = new LinkedList<OwObject>();
        objects.add(oObject_p);
        if (m_favoritePlugin != null)
        {
            m_favoritePlugin.addOwObjectShortCuts(objects);

            addHistoryEvent(oObject_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
            getContext().postMessage(getContext().localize("plug.owshortcut.OwShortCutDocumentFunction.addedfavourite", "The selected object has been added to the favorites."));
        }
        else
        {
            LOG.debug("OwShortCutDocumentFunction.onClickEvent: No favorite MasterPlugin available, check the configuration");
            handleMissingShortCutSerializer(objects);
        }
    }

    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject 
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (m_favoritePlugin != null)
        {
            m_favoritePlugin.addOwObjectShortCuts(objects_p);
            getContext().postMessage(getContext().localize("plug.owshortcut.OwShortCutDocumentFunction.addedfavourites", "The selected objects have been added to the favorites."));
            addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        }
        else
        {
            LOG.debug("OwShortCutDocumentFunction.onMultiselectClickEvent: No favorite MasterPlugin available, check the configuration");
            handleMissingShortCutSerializer(objects_p);
        }
    }

    /**
     * Method is called in case no favorite serializer is available.
     * @param objects Collection of OwObject's
     * @throws Exception
     * @since 4.2.0.0
     */
    protected void handleMissingShortCutSerializer(Collection<OwObject> objects) throws Exception
    {
        String title = getContext().localize("OwShortCutDocumentFunction.dialog.noSerializer.title", "Missing or Incorrect Configuration");
        String message = getContext().localize("OwShortCutDocumentFunction.dialog.noSerializer.msg", "There is no Favorites master plugin available, cannot create Favorites.");
        OwMessageBox msgDialog = new OwMessageBox(OwMessageBox.TYPE_OK, OwMessageBox.ACTION_STYLE_BUTTON, OwMessageBox.ICON_TYPE_EXCLAMATION, title, message);
        getContext().openDialog(msgDialog, null);
    }

    /** get the Bookmark/Favorite Master Plugin
     * 
     * @return the Bookmark/Favorite Master Plugin as {@link OwMasterDocument} 
     * @throws OwConfigurationException
     */
    public OwMasterDocument getFavoriteMasterPlugin() throws OwConfigurationException
    {
        //<UsedMasterPluginID>PluginID</UsedMasterPluginID>

        String sEventTargetID = null;
        OwMasterDocument masterPlugin = null;
        //check for configuration UsedMasterPluginID element
        try
        {
            Node useMasterPluginNode = this.getConfigNode().getSubNode("UsedMasterPluginID");
            if (null != useMasterPluginNode)
            {
                sEventTargetID = useMasterPluginNode.getFirstChild().getNodeValue() + OwConfiguration.MAIN_PLUGIN_DOCUMENT_ID_EXTENSION;
                masterPlugin = (OwMasterDocument) getContext().getEventTarget(sEventTargetID);
            }
            else
            {
                masterPlugin = searchMasterPluginFromClassName();
            }

        }
        catch (Exception e)
        {
            LOG.warn("OwRecordFunctionShortcut.getFavoriteMasterPlugin: Cannot find the event target for ID: " + sEventTargetID, e);
        }

        return masterPlugin;
    }

    /**
     * @see com.wewebu.ow.server.app.OwDocumentFunction#isEnabled(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
    {
        if (null == m_favoritePlugin)
        {
            return false;
        }
        else
        {
            return super.isEnabled(object_p, parent_p, context_p);
        }
    }

    /**
     * Search the list of allowed master plugins and return first entry, 
     * which is an instance of OwShortCutDocument.
     * Can return null if allowed master plugins list is empty, or
     * no instance can be found.
     * @return OwMasterDocument can be null
     */
    private OwMasterDocument searchMasterPluginFromClassName()
    {
        List all = getContext().getConfiguration().getMasterPlugins(true);
        OwMasterDocument ret = null;
        if (all != null)
        {
            Iterator it = all.iterator();
            while (it.hasNext())
            {
                OwMasterPluginInstance inst = (OwMasterPluginInstance) it.next();
                if (inst.getDocument() instanceof OwShortCutDocument)
                {
                    ret = inst.getDocument();
                    break;
                }
            }
        }
        return ret;
    }
}
