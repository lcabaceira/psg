package com.alfresco.ow.server.plug.owdoccopy;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.owdoccopy.OwRecordFunctionPaste;
import com.wewebu.ow.server.plug.owutil.OwMappingUtils;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 * Allows to paste objects to (semi) virtual folders. The objects from clipboard will be past to it's root object if  
 * selected folder is a virtual folder and a root folder exists.
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
public class OwRecordFunctionVFPaste extends OwRecordFunctionPaste
{
    private boolean parameter_mapping_includechildren = false;

    /** insert mode - default is reference*/
    private int m_insertMode = INSERT_MODE_REFERENCE;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwRecordFunctionPaste#init(com.wewebu.ow.server.util.OwXMLUtil, com.wewebu.ow.server.app.OwMainAppContext)
     */
    @Override
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        // set insert mode
        String strInsertMode = getConfigNode().getSafeTextValue("InsertMode", "INSERT_MODE_REFERENCE");
        try
        {
            Field insertModeField = getClass().getDeclaredField(strInsertMode);
            this.m_insertMode = insertModeField.getInt(insertModeField);
        }
        catch (Exception e)
        {
            //set to default
            m_insertMode = INSERT_MODE_REFERENCE;
        }

        OwXMLUtil mappingUtil = getConfigNode().getSubUtil("ParameterMapping");
        if (mappingUtil != null)
        {
            parameter_mapping_includechildren = mappingUtil.getSafeBooleanAttributeValue("includechildren", false);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwRecordFunctionPaste#onClickEvent(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    @Override
    public void onClickEvent(OwObject rootObject_p, OwObject folderObject_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(rootObject_p, folderObject_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(new OwString("plug.owdoccopy.OwRecordFunctionPaste.invalidobject", "Das Objekt kann nicht eingef�gt werden."));
        }

        Map propertyMap = null;

        // === add all OwObjects from the clipboard to the folder
        OwClipboard clipboard = getContext().getClipboard();

        //parameter mapping
        if (this.m_insertMode == INSERT_MODE_PHYSICALCOPY || clipboard.getCut())
        {
            // perform optional property mapping
            propertyMap = OwMappingUtils.getParameterMapValuesFromRecord(getConfigNode(), rootObject_p, folderObject_p);
        }

        //create object copy
        OwPropertyCollection newPropertyCollection = null;

        try
        {
            if (clipboard.getCut())
            {
                // === clipboard content was cut
                Iterator it = clipboard.getContent().iterator();
                while (it.hasNext())
                {
                    OwClipboardContentOwObject element = (OwClipboardContentOwObject) it.next();

                    //Virtual Folder of a semi virtual folder does not add element to their physical root, so we have to do it here
                    if (rootObject_p != null && folderObject_p.getType() == OwObjectReference.OBJECT_TYPE_VIRTUAL_FOLDER)
                    {
                        rootObject_p.move(element.getObject(), element.getParent());
                        folderObject_p.add(element.getObject());
                    }
                    else
                    {
                        folderObject_p.move(element.getObject(), element.getParent());
                    }

                    if (null != propertyMap)
                    {
                        OwObject sourceObject = element.getObject();
                        newPropertyCollection = mapProperties(sourceObject, propertyMap);
                        sourceObject.setProperties(newPropertyCollection, OwObjectClass.OPERATION_TYPE_SET_PROPERTIES);

                        if (parameter_mapping_includechildren && sourceObject.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
                        {
                            mapChildrenProperties(sourceObject, propertyMap);
                        }
                    }
                }
                // empty clipboard after paste from cut
                clipboard.clearContent();
            }
            else
            {
                // === clipboard content was copied
                Iterator it = clipboard.getContent().iterator();
                while (it.hasNext())
                {
                    OwClipboardContentOwObject element = (OwClipboardContentOwObject) it.next();

                    if (m_insertMode == INSERT_MODE_REFERENCE)
                    {
                        folderObject_p.add(element.getObject());
                    }
                    else
                    {
                        OwObject sourceObject = element.getObject();

                        if (null != propertyMap && folderObject_p != null)
                        {
                            newPropertyCollection = mapProperties(sourceObject, propertyMap);
                        }

                        String objID = getContext().getNetwork().createObjectCopy(sourceObject, newPropertyCollection, null, folderObject_p, null);

                        if (parameter_mapping_includechildren && null != propertyMap && folderObject_p != null && sourceObject.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
                        {
                            OwObject copiedObject = getContext().getNetwork().getObjectFromDMSID(objID, false);
                            mapChildrenProperties(copiedObject, propertyMap);
                        }
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
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);
        }
    }

    /**
     * Returns a Collection with mapped properties
     * 
     * @param sourceObject  
     * @param propertyMap   
     * @return Collection of all properties incl. mapped properties
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected OwPropertyCollection mapProperties(OwObject sourceObject, Map propertyMap) throws Exception
    {
        OwPropertyCollection newPropertyCollection = new OwStandardPropertyCollection();
        OwPropertyCollection sourceProps = sourceObject.getClonedProperties(null);
        //copy properties
        Iterator iterator = sourceProps.values().iterator();
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

        Set<Map.Entry<Object, Object>> entrySet = propertyMap.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet)
        {
            try
            {
                OwProperty property = sourceObject.getProperty((String) entry.getKey());
                property.setValue(entry.getValue());
                // update properties with folder mapped properties 
                newPropertyCollection.put(property.getPropertyClass().getClassName(), property);
            }
            catch (OwObjectNotFoundException e)
            {
                // Ignore
            }

        }

        return newPropertyCollection;
    }

    /**
     * Property mapping for all child objects.
     * 
     * @param sourceObject
     * @param propertyMap
     * @throws Exception
     */
    protected void mapChildrenProperties(OwObject sourceObject, Map propertyMap) throws Exception
    {
        int[] objectTypes = new int[] { OwObjectReference.OBJECT_TYPE_FOLDER, OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_CUSTOM };
        OwObjectCollection children = sourceObject.getChilds(objectTypes, propertyMap.keySet(), null, -1, 0, null);
        for (Iterator it = children.iterator(); it.hasNext();)
        {
            OwObject object = (OwObject) it.next();
            if (object.getType() == OwObjectReference.OBJECT_TYPE_FOLDER)
            {
                mapChildrenProperties(object, propertyMap);
            }
            OwPropertyCollection mappedProps = mapProperties(object, propertyMap);
            object.setProperties(mappedProps, OwObjectClass.OPERATION_TYPE_SET_PROPERTIES);
        }
    }

}
