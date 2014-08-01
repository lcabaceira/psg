package com.wewebu.ow.server.plug.owdoccopy;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwRecordFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.owutil.OwMappingUtils;
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
public class OwRecordFunctionPaste extends OwRecordFunction
{
    /** insert mode reference */
    public static final int INSERT_MODE_REFERENCE = 0;
    /** insert mode physical copy */
    public static final int INSERT_MODE_PHYSICALCOPY = 1;

    /** insert mode - default is reference*/
    private int m_insertMode = INSERT_MODE_REFERENCE;

    /** set of the supported object types as defined in OwObjectReference, can be null */
    private Set<?> m_supportedObjectTypes;

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

        // set insert mode
        String strInsertMode = getConfigNode().getSafeTextValue("InsertMode", "INSERT_MODE_REFERENCE");
        try
        {
            Field insertModeField = getClass().getField(strInsertMode);
            this.m_insertMode = insertModeField.getInt(insertModeField);
        }
        catch (Exception e)
        {
            //set to default
            m_insertMode = INSERT_MODE_REFERENCE;
        }

    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owpaste/paste.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owpaste/paste_24.png");
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

        if (clipboard.getCut())
        {
            // === all clipboard items must be movable
            Iterator<?> it = clipboard.getContent().iterator();
            while (it.hasNext())
            {
                OwClipboardContentOwObject item = (OwClipboardContentOwObject) it.next();

                // === check if we are configured to support the object type
                if ((m_supportedObjectTypes != null) && (!m_supportedObjectTypes.contains(new Integer(item.getObject().getType()))))
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
        }
        else
        {
            // === all clipboard items must be addable
            Iterator<?> it = clipboard.getContent().iterator();
            while (it.hasNext())
            {
                OwClipboardContentOwObject item = (OwClipboardContentOwObject) it.next();

                // === check if we are configured to support the object type
                if ((m_supportedObjectTypes != null) && (!m_supportedObjectTypes.contains(Integer.valueOf(item.getObject().getType()))))
                {
                    return false;
                }

                // === check if we are configured to support the object class
                if ((m_supportedObjectClasses != null) && (!m_supportedObjectClasses.contains(item.getObject().getClassName())))
                {
                    return false;
                }

                if (m_insertMode == INSERT_MODE_REFERENCE)
                {
                    if (!folderObject_p.canAdd(item.getObject(), iContext_p))
                    {
                        return false;
                    }
                }
                else
                {
                    if (!getContext().getNetwork().canCreateObjectCopy(folderObject_p, null, iContext_p))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Overridable cut-triggered paste processing method.
     * 
     * @param clipboardObject_p cutted clipboard object to be pasted
     * @param folderObject_p paste destination folder
     * @param propertyMap_p the configured property map rules (can be null)
     * @throws Exception
     * @since 3.2.0.1
     */
    protected void processCut(OwClipboardContentOwObject clipboardObject_p, OwObject folderObject_p, Map propertyMap_p) throws Exception
    {
        folderObject_p.move(clipboardObject_p.getObject(), clipboardObject_p.getParent());
    }

    /**
     * Overridable reference paste processing method.
     * Called on {@link #INSERT_MODE_REFERENCE} configured plugin instances.
     *  
     * @param clipboardObject_p copied clipboard object to be pasted
     * @param folderObject_p paste destination folder
     * @param propertyMap_p the configured property map rules (can be null)
     * @throws Exception
     * @since 3.2.0.1
     */
    protected void processCopyRef(OwClipboardContentOwObject clipboardObject_p, OwObject folderObject_p, Map propertyMap_p) throws Exception
    {
        OwObject sourceObject_p = clipboardObject_p.getObject();
        folderObject_p.add(sourceObject_p);
    }

    /**
     * Overridable physical copy paste processing method.
     * Called on {@link #INSERT_MODE_PHYSICALCOPY} configured plugin instances.
     *  
     * @param clipboardObject_p copied clipboard object to be pasted
     * @param folderObject_p paste destination folder
     * @param propertyMap_p the configured property map rules (can be null)
     * @throws Exception
     * @since 3.2.0.1
     */
    protected void processCopyInstance(OwClipboardContentOwObject clipboardObject_p, OwObject folderObject_p, Map propertyMap_p) throws Exception
    {
        OwObject sourceObject_p = clipboardObject_p.getObject();
        OwPropertyCollection newPropertyCollection = mapPropertyCollection(propertyMap_p, folderObject_p, sourceObject_p);
        OwMainAppContext context = getContext();
        OwNetwork network = context.getNetwork();
        network.createObjectCopy(sourceObject_p, newPropertyCollection, null, folderObject_p, null);
    }

    /**
     * Applies the given mapping rules on the folderObject-sourceObjet pair.
     * See bootstrap documentation on defining property mappings.  
     * 
     * @param propertyMap_p property mappings rule map (can be null)  
     * @param folderObject_p 
     * @param sourceObject_p
     * @return an {@link OwPropertyCollection} containing properties defined by the given mapping with values set 
     *         according to the mapping expressions (see bootstrap documentation on property mappings).   
     * @throws Exception
     * @since 3.2.0.1
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected OwPropertyCollection mapPropertyCollection(Map propertyMap_p, OwObject folderObject_p, OwObject sourceObject_p) throws Exception
    {
        OwPropertyCollection newPropertyCollection = null;

        if (null != propertyMap_p && folderObject_p != null)
        {

            newPropertyCollection = new OwStandardPropertyCollection();
            OwPropertyCollection sourceProps = sourceObject_p.getClonedProperties(null);
            //copy properties
            Iterator<?> iterator = sourceProps.values().iterator();
            while (iterator.hasNext())
            {
                OwProperty prop = (OwProperty) iterator.next();
                if (prop.isReadOnly(OwPropertyClass.CONTEXT_ON_CREATE) || prop.getPropertyClass().isSystemProperty() || prop.isHidden(OwPropertyClass.CONTEXT_ON_CREATE))
                {
                    continue;
                }
                OwProperty original = (OwProperty) sourceProps.get(prop.getPropertyClass().getClassName());
                prop.setValue(original.getValue());
                newPropertyCollection.put(prop.getPropertyClass().getClassName(), prop);
            }

            Iterator<?> iti = propertyMap_p.entrySet().iterator();
            while (iti.hasNext())
            {
                Entry<Object, Object> mapEntry = (Entry<Object, Object>) iti.next();
                try
                {
                    OwProperty property = sourceObject_p.getProperty((String) mapEntry.getKey());
                    property.setValue(mapEntry.getValue());
                    // update properties with folder mapped properties 
                    newPropertyCollection.put(property.getPropertyClass().getClassName(), property);
                }
                catch (OwObjectNotFoundException e)
                {
                    // Ignore
                }

            }
        }

        return newPropertyCollection;
    }

    /**
     * event called when user clicked the plugin label / icon
     * 
     * @param rootObject_p OwObject root folder to work on
     * @param folderObject_p OwObject selected folder to work on
     * @param refreshCtx_p OwFunctionRefreshContext callback interface for the function
     *            plugins to signal refresh events to clients, can be null if no
     *            refresh is needed
     * 
     */
    public void onClickEvent(OwObject rootObject_p, final OwObject folderObject_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(rootObject_p, folderObject_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(new OwString("plug.owdoccopy.OwRecordFunctionPaste.invalidobject", "Item cannot be pasted."));
        }

        Map propertyMap = null;
        //parameter mapping
        if (createPropertyMap())
        {
            // perform optional property mapping
            propertyMap = OwMappingUtils.getParameterMapValuesFromRecord(getConfigNode(), rootObject_p, folderObject_p);
        }

        // === add all OwObjects from the clipboard to the folder
        OwClipboard clipboard = getContext().getClipboard();

        OwClipboardMoveOperation clipboardOperation = null;
        try
        {
            if (clipboard.getCut())
            {
                // === clipboard content was cut
                clipboardOperation = new OwClipboardMoveOperation(clipboard, propertyMap) {

                    @Override
                    protected void processClipboardElement(OwClipboardContentOwObject element, Map propertyMap) throws Exception
                    {
                        OwRecordFunctionPaste.this.processCut(element, folderObject_p, propertyMap);
                    }

                };
                clipboardOperation.execute();
            }
            else
            {
                // === clipboard content was copied
                Iterator<?> it = clipboard.getContent().iterator();
                while (it.hasNext())
                {
                    OwClipboardContentOwObject element = (OwClipboardContentOwObject) it.next();

                    if (m_insertMode == INSERT_MODE_REFERENCE)
                    {
                        processCopyRef(element, folderObject_p, propertyMap);
                    }
                    else
                    {
                        processCopyInstance(element, folderObject_p, propertyMap);
                    }
                }
            }
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

    /**(overridable)
     * Verification if property map need to be created for current processing.<br />
     * By default will return true, if current mode is set to {@link #INSERT_MODE_PHYSICALCOPY}.
     * @return boolean
     * @since 3.2.0.1
     */
    protected boolean createPropertyMap()
    {
        return this.m_insertMode == INSERT_MODE_PHYSICALCOPY;
    }
}