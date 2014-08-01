package com.wewebu.ow.server.plug.owdocprops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwField;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implementation of the Document edit properties plugin.
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
public class OwBatchPropertyAssignmentDocumentFunction extends OwDocumentFunction
{
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/OwObjectPropertyView/paste_metadata.png");
    }

    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/OwObjectPropertyView/paste_metadata_24.png");
    }

    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        if (!isEnabled(object_p, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(new OwString("owstd.OwBatchPropertyAssignmentDocumentFunction.invalidobject", "Cannot paste properties into the current object selection!"));
        }

        if (object_p != null)
        {
            OwClipboard clipboard = getClipboard();
            //check if clipboard is filled with fields/properties
            if (isValidClipboardContentType())
            {
                List contentList = clipboard.getContent();
                // if no content exist skip property reassign
                if (contentList != null && contentList.size() > 0)
                {
                    int noProp = 0;
                    //get property names in a Collection of Strings
                    Collection propNames = getPropertyNameList(contentList);
                    //get properties which should be changed
                    OwPropertyCollection propCol = object_p.getClonedProperties(propNames);
                    for (Iterator it = contentList.iterator(); it.hasNext();)
                    {
                        OwField updateField = ((OwClipboardContentOwField) it.next()).getField();
                        String propName = updateField.getFieldDefinition().getClassName();
                        try
                        {
                            //throws a ObjectNotFoundException if field doesn't exist
                            OwProperty objProp = (OwProperty) propCol.get(propName);

                            //check if property value can be changed
                            if (canSetValue(objProp))
                            {//set only the value, user can then choose to save or discard the changes
                                objProp.setValue(updateField.getValue());
                            }
                            else if (isSystemProperty(objProp))
                            {
                                //remove system property from collection
                                propCol.remove(propName);
                            }

                        }
                        catch (OwObjectNotFoundException nfex)
                        {
                            OwLog.getLogger(this.getClass()).warn("OwBatchPropertyAssignmentDocumentFunction." + "onClickEvent property not found! Property-Name = " + propName);
                            //ignore Exception and get next field
                            noProp++;
                        }
                    }

                    if (propCol != null && !propCol.isEmpty())
                    {
                        object_p.setProperties(propCol);//save properties to back-end system
                    }

                    //show if metadata was successfully updated, use context.
                    if (noProp == 0)
                    {
                        setFinishMessage(OwString.localize(getContext().getLocale(), "owstd.OwBatchPropertyAssignmentDocumentFunction.msg.succes", "Data updated successfully"));
                        addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);

                    }
                    else
                    {
                        setFinishMessage(OwString.localize2(getContext().getLocale(), "owstd.OwBatchPropertyAssignmentDocumentFunction.msg.fail", "Update error, %1 of %2 properties were not updated", noProp + "", propCol.size() + ""));
                        addHistoryEvent(object_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                    }
                }

            }
        }
    }

    public void onMultiselectClickEvent(Collection objectCol_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        addHistoryEvent(objectCol_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        if (objectCol_p != null && objectCol_p.size() > 0)
        {
            //get Clipboard content list
            List content = getClipboard().getContent();

            if (isValidClipboardContentType() && content != null && content.size() > 0)
            {
                int failedUpdates = 0;
                //get a string collection, with names of the properties to be updated
                //this is candidate for refactoring to OWCommand
                List processedObjects = new LinkedList();
                List disabledObjects = new LinkedList();
                List failedObjects = new LinkedList();
                Collection propNames = getPropertyNameList(content);

                for (Iterator itObject = objectCol_p.iterator(); itObject.hasNext();)
                {
                    //get object that should be updated
                    OwObject obj = (OwObject) itObject.next();
                    //check if this function is enabled for current object
                    if (!isEnabled(obj, null, 0))
                    {
                        disabledObjects.add(obj);
                        continue; //Folder should not be updated
                    }

                    try
                    {
                        OwPropertyCollection propCol = obj.getClonedProperties(propNames);

                        for (Iterator itFields = content.iterator(); itFields.hasNext();)
                        {

                            OwField updateField = ((OwClipboardContentOwField) itFields.next()).getField();
                            String propertyName = updateField.getFieldDefinition().getClassName();
                            // get property which should be edit
                            OwProperty objProp = (OwProperty) propCol.get(propertyName);

                            //check if the value can be changed
                            if (canSetValue(objProp))
                            {
                                objProp.setValue(updateField.getValue());
                            }
                            else if (isSystemProperty(objProp))
                            {//remove system properties from collection, there can't be changed
                                propCol.remove(propertyName);
                            }

                        }
                        //set Properties
                        if (propCol != null && !propCol.isEmpty())
                        {
                            obj.setProperties(propCol);
                            processedObjects.add(obj);
                        }

                    }
                    catch (OwObjectNotFoundException owEx)
                    {
                        OwLog.getLogger(this.getClass()).warn("OwBatchPropertyAssignmentDocumentFunction." + "onMultiselectClickEvent ObjectNotFound: " + owEx.getMessage());
                        /*Property does not exist in Object*/
                        failedUpdates++;
                        failedObjects.add(obj);
                    }
                }

                if (failedUpdates == 0)
                {
                    setFinishMessage(OwString.localize(getContext().getLocale(), "owstd.OwBatchPropertyAssignmentDocumentFunction.list.msg.succes", "Update finished successfully"));
                }
                else
                {
                    setFinishMessage(OwString.localize2(getContext().getLocale(), "owstd.OwBatchPropertyAssignmentDocumentFunction.list.msg.fail", "%1 of %2 objects could not be updated", failedUpdates + "", objectCol_p.size() + ""));
                }
                //historize
                if (!processedObjects.isEmpty())
                {
                    addHistoryEvent(processedObjects, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                }
                if (!failedObjects.isEmpty())
                {
                    addHistoryEvent(failedObjects, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                }
                if (!disabledObjects.isEmpty())
                {
                    addHistoryEvent(disabledObjects, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_DISABLED);
                }

            }
        }
    }

    public void setFinishMessage(String msg_p)
    {
        getContext().postMessage(msg_p);
    }

    protected OwClipboard getClipboard()
    {
        return getContext().getClipboard();
    }

    /**Helper method to Check if the clipboard is empty
     * @return boolean <code>false</code> only if content exist and content<code>!= <b>null</b></code>. 
     */
    protected boolean isClipboardEmpty()
    {
        List lst = getClipboard().getContent();
        return lst == null || lst.isEmpty();
    }

    protected boolean isValidClipboardContentType()
    {
        return !isClipboardEmpty() && (getClipboard().getContentType() == OwClipboard.CONTENT_TYPE_OW_FIELD);
    }

    /**Check if given property is a system-property.
     * 
     * @param prop_p OwProperty which should be checked
     * @return boolean <code>true</code> only if given property is a system property.
     * @throws Exception if missing rights to read information
     */
    protected boolean isSystemProperty(OwProperty prop_p) throws Exception
    {
        return prop_p.getPropertyClass().isSystemProperty();
    }

    /**
     * Check if this Property can be edit.
     * Return <b>true</b> if the property is not a system property
     * and is not read-only, else returns <b>false</b>. 
     * @param prop_p OwProperty which should be checked.
     * @return boolean true, only if user can edit the given Property
     * @throws Exception
     */
    protected boolean canSetValue(OwProperty prop_p) throws Exception
    {
        OwPropertyClass propClass = prop_p.getPropertyClass();

        return !isSystemProperty(prop_p) && !propClass.isReadOnly(OwPropertyClass.CONTEXT_NORMAL);
    }

    /**
     * Return a list containing all names of the properties, which are 
     * saved into clipboard. 
     * @param clipboardContent_p list representing clipboard content
     * @return Collection with properties names
     * @throws Exception if given list not contains objects of type OwClipboardContentOwField
     */
    private Collection getPropertyNameList(List clipboardContent_p) throws Exception
    {
        ArrayList propNames = new ArrayList();
        for (Iterator it = clipboardContent_p.iterator(); it.hasNext();)
        {
            OwField updateField = ((OwClipboardContentOwField) it.next()).getField();
            propNames.add(updateField.getFieldDefinition().getClassName());
        }
        return propNames;
    }

    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        return isValidClipboardContentType() && super.isEnabled(oObject_p, oParent_p, iContext_p);
    }

    public boolean isEnabled(Collection objCol_p, OwObject parent_p, int iContext_p) throws Exception
    {
        return isValidClipboardContentType() && super.isEnabled(objCol_p, parent_p, iContext_p);
    }
}