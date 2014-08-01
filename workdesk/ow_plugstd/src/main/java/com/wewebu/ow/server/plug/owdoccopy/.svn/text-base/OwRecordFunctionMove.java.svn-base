package com.wewebu.ow.server.plug.owdoccopy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwRecordFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
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
public class OwRecordFunctionMove extends OwRecordFunction
{
    /** set of the supported object types as defined in OwObject */
    private Set m_supportedObjectTypes = new HashSet();

    /** set of the supported object classes available for paste */
    private Set<?> m_supportedObjectClasses;

    /** set the plugin description node 
        * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
        * @param context_p OwMainAppContext
        */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        // === get the supported object type map for the plugin
        m_supportedObjectTypes = getSupportedObjectTypesFromDescriptor("pasteitemobjecttypes");

        // === get supported object classes 
        m_supportedObjectClasses = getSupportedObjectClassesFromDescriptor("pasteitemobjectclasses");

    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owmove/move.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owmove/move_24.png");
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
        if (!super.isEnabled(rootObject_p, folderObject_p, iContext_p))
        {
            return false;
        }

        // === we must have a folder object to work on
        if (folderObject_p == null)
        {
            return false;
        }

        // === clipboard content must be set to OwObject
        OwClipboard clipboard = getContext().getClipboard();
        if (OwClipboard.CONTENT_TYPE_OW_OBJECT != clipboard.getContentType())
        {
            return false;
        }

        // === all clipboard items must be addable
        Iterator it = clipboard.getContent().iterator();
        while (it.hasNext())
        {
            OwClipboardContentOwObject item = (OwClipboardContentOwObject) it.next();

            // === check if we are configured to support the object type
            if (!m_supportedObjectTypes.contains(Integer.valueOf(item.getObject().getType())))
            {
                return false;
            }

            // === check if we are configured to support the object class
            if ((m_supportedObjectClasses != null) && (!m_supportedObjectClasses.contains(item.getObject().getClassName())))
            {
                return false;
            }

            if (!folderObject_p.canMove(item.getObject(), item.getParent(), iContext_p))
            {
                return false;
            }
        }

        return true;
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject rootObject_p, final OwObject folderObject_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(rootObject_p, folderObject_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            addHistoryEvent(rootObject_p, folderObject_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_DISABLED);
            throw new OwInvalidOperationException(new OwString("plug.owdoccopy.OwRecordFunctionMove.invalidobject", "Item cannot be moved."));
        }

        // === add all OwObjects from the clipboard to the folder
        OwClipboard clipboard = getContext().getClipboard();

        OwClipboardMoveOperation clipboardOperation = null;
        try
        {
            clipboardOperation = new OwClipboardMoveOperation(clipboard, null) {

                @Override
                protected void processClipboardElement(OwClipboardContentOwObject element, Map propertyMap) throws Exception
                {
                    folderObject_p.move(element.getObject(), element.getParent());
                }

            };
            clipboardOperation.execute();
        }
        catch (Exception e)
        {
            // historize failure
            addHistoryEvent(rootObject_p, folderObject_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
            // re-throw exception
            throw e;
        }

        // historize success
        addHistoryEvent(rootObject_p, folderObject_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);

        // === refresh necessary, call client
        if (null != refreshCtx_p)
        {
            if (null != clipboardOperation)
            {
                clipboardOperation.sendUpdates(refreshCtx_p);
            }
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);
        }
    }
}