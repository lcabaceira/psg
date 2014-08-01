package com.wewebu.ow.server.plug.owshortcut;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwConfiguration.OwMasterPluginInstance;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwRecordFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Move Record Function to move items from the clipboard.
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
public class OwRecordFunctionShortcut extends OwRecordFunction
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRecordFunctionShortcut.class);

    /** insert mode reference */
    public static final int INSERT_MODE_REFERENCE = 0;
    /** insert mode physical copy */
    public static final int INSERT_MODE_PHYSICALCOPY = 1;
    /** the favorites/bookmarks/shortcuts plugin*/
    private OwShortCutDocument m_favoritePlugin = null;

    /** set of the supported object types as defined in OwObjectReference, can be null */
    //private Set m_supportedObjectTypes;
    /** set the plugin description node 
        * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
        * @param context_p OwMainAppContext
        */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        m_favoritePlugin = (OwShortCutDocument) getFavoriteMasterPlugin();
        // === get the supported object type map for the plugin
        //m_supportedObjectTypes = getSupportedObjectTypesFromDescriptor("pasteitemobjecttypes");

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

    /** check if function is enabled for the given object parameters
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *
     *  @return true = enabled, false otherwise
     */
    public boolean isEnabled(OwObject rootObject_p, OwObject folderObject_p, int iContext_p) throws Exception
    {
        if (null == m_favoritePlugin || folderObject_p == null || folderObject_p.getType() != OwObjectReference.OBJECT_TYPE_FOLDER)
        {
            return false;
        }
        else
        {
            return super.isEnabled(rootObject_p, folderObject_p, iContext_p);
        }
    }

    /** event called when user clicked the plugin label / icon 
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
        if (!isEnabled(rootObject_p, folderObject_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            addHistoryEvent(rootObject_p, folderObject_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            throw new OwInvalidOperationException(new OwString("plug.owdoccopy.OwRecordFunctionPaste.invalidobject", "Item cannot be pasted."));
        }

        // create shortcut with subpath
        m_favoritePlugin.addOwObjectShortCut(rootObject_p, subpath_p, subdisplaypath_p);

        // === refresh necessary, call client
        if (null != refreshCtx_p)
        {
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);
        }

        // user message
        getContext().postMessage(getContext().localize("plug.owshortcut.OwShortCutDocumentFunction.addedfavourite", "The selected object has been added to the favorites."));
        addHistoryEvent(rootObject_p, folderObject_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
    }

    /** get the Bookmark/Favorite Master Plugin
     * 
     * @return the Bookmark/Favorite Master Plugin as {@link OwMasterDocument} 
     * @throws OwConfigurationException
     */
    public OwMasterDocument getFavoriteMasterPlugin() throws OwConfigurationException
    {
        /*<UsedMasterPluginID>PluginID</UsedMasterPluginID> optional parameter
         * also for better performance, because the plugin is not searched.*/

        OwMasterDocument masterPlugin = null;
        String sEventTargetID = null;
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
                if (null == masterPlugin)
                {
                    masterPlugin = searchMasterPluginFromClassName();
                }
            }
        }
        catch (Exception e)
        {
            LOG.warn("OwRecordFunctionShortcut.getFavoriteMasterPlugin(): Cannot find the event target for ID: " + sEventTargetID, e);
        }
        return masterPlugin;
    }

    /**
     * Search the list of master plugins, which is an instance of 
     * OwShortCutDocument and return it. Return <b>null</b> only if no
     * instance of OwShortCutDocument can be found, or list of allowed
     * master plugins is <b>null</b>. 
     * @return OwMasterDocument can be <b>null</b>
     */
    private OwMasterDocument searchMasterPluginFromClassName()
    {
        List allowedMaster = getContext().getConfiguration().getMasterPlugins(true);
        OwMasterDocument ret = null;
        if (allowedMaster != null)
        {
            Iterator it = allowedMaster.iterator();
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
