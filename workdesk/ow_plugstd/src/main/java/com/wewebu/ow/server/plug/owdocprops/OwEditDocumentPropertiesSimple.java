package com.wewebu.ow.server.plug.owdocprops;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owutil.OwConfigUtils;
import com.wewebu.ow.server.plug.owutil.OwObjectUtils;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the simple Document edit properties plugin.
 * Renders just a given list of properties.
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
public class OwEditDocumentPropertiesSimple extends OwDocumentFunction implements OwDialogListener
{
    //   === members
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwEditDocumentPropertiesSimple.class);

    /**auto lock element name*/
    private static final String AUTO_LOCK_ELEMENT_NAME = "AutoLock";

    /**auto lock value*/
    private boolean m_useAutoLock;

    /** list of OwPropertyInfo objects that define the visibility and modifiability of properties 
     * @since 3.1.0.3 
     * @deprecated since 4.2.0.0 replaced by propertyListConfiguration*/
    private List m_propertyInfos;

    /**
     * PropertyList configuration representation
     * @since 4.2.0.0
     */
    private OwPropertyListConfiguration propertyListConfiguration;

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        m_useAutoLock = node_p.getSafeBooleanValue(AUTO_LOCK_ELEMENT_NAME, false);
        OwXMLUtil util = getConfigNode().getSubUtil(OwPropertyListConfiguration.ELEM_ROOT_NODE);
        if (util != null)
        {
            OwPropertyListConfiguration propLstConf = new OwPropertyListConfiguration(getPluginID());
            propLstConf.build(util);
            setPropertyListConfiguration(propLstConf);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwDocumentFunction#isEnabled(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
    {
        boolean result = super.isEnabled(object_p, parent_p, context_p);
        if (result && m_useAutoLock)
        {
            if (object_p.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                result = object_p.getMyLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
            }
        }
        return result;
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocprops/edit_properties_simple.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocprops/edit_properties_simple_24.png");
    }

    /** event called when user clicked the label / icon 
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            LOG.debug("OwEditDocumentPropertiesSimple.onClickEvent(): the edit function is not enabled for the given object !");
            throw new OwInvalidOperationException(new OwString("owdocprops.edit.properties.function.invalid.invalid.object", "Item can not be edited!"));
        }

        List<OwObject> objects = new LinkedList<OwObject>();

        objects.add(oObject_p);

        onMultiselectClickEvent(objects, oParent_p, refreshCtx_p);
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
        Collection<OwObject> enabledObjects = new LinkedList<OwObject>();

        for (Iterator<?> itObj = objects_p.iterator(); itObj.hasNext();)
        {
            OwObject objectToEdit = (OwObject) itObj.next();
            if (isEnabled(objectToEdit, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                enabledObjects.add(objectToEdit);
            }
        }
        if (enabledObjects.isEmpty())
        {
            LOG.error("OwEditDocumentPropertiesSimple.onMultiselectClickEvent(): Invalid object collection!The enabled objects collection is empty!");
            throw new OwInvalidOperationException(new OwString("owdocprops.edit.properties.function.invalid.object.collection", "Invalid object collection!"));
        }

        // === create new edit properties dialog
        Collection<OwObject> objects = enabledObjects;

        // select version to edit
        if (getConfigNode().getSafeBooleanValue("AlwaysUseLatestVersion", false))
        {
            objects = OwObjectUtils.getLatesVersionObjects(enabledObjects);
        }

        OwEditPropertiesDialogSimple dlg = new OwEditPropertiesDialogSimple(objects, 0, oParent_p, getPropertyListConfiguration(), m_useAutoLock);

        int iViewMask = 0;
        iViewMask |= (getConfigNode().getSafeBooleanValue("EnableMenu", true) ? 0 : OwEditPropertiesDialogSimple.VIEW_MASK_DISABLE_MENU);
        iViewMask |= (getConfigNode().getSafeBooleanValue("AutoOpen", false) ? OwEditPropertiesDialogSimple.VIEW_MASK_AUTOOPEN : 0);
        iViewMask |= OwXMLDOMUtil.getSafeBooleanAttributeValue(getConfigNode().getSubNode("AutoOpen"), "previewmode", false) ? OwEditPropertiesDialogSimple.VIEW_MASK_PREVIEW_AUTOOPEN : 0;
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "EnablePasteMetadata", OwEditPropertiesDialogSimple.VIEW_MASK_ENABLE_PASTE_METADATA);

        // set view mask bits
        dlg.setViewMask(iViewMask);

        OwXMLUtil previewConfiguration = getConfigNode().getSubUtil("Preview");
        dlg.setPreviewConfiguration(previewConfiguration);

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());
        dlg.setTitle(getDefaultLabel());

        // set info icon
        dlg.setInfoIcon(getBigIcon());

        // open dialog
        getContext().openDialog(dlg, this);

        // historize
        addHistoryEvent(objects, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * Unlock the object if auto lock function is true
     * @param object_p - the object
     * @throws Exception
     * @since 2.5.2.0
     */
    private void unlockItem(OwObject object_p) throws Exception
    {
        if (m_useAutoLock && object_p != null)
        {
            if (object_p.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                object_p.setLock(false);
            }
        }
    }

    /**
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // here receive update events from the views
        switch (code_p)
        {
            case OwUpdateCodes.MODIFIED_OBJECT_PROPERTY:
            {
                // on apply was pressed in the property view
                if (caller_p instanceof OwObjectPropertyView)
                {
                    OwObjectPropertyView realCaller = (OwObjectPropertyView) caller_p;
                    Collection objectColection = new LinkedList();
                    OwObject objectRef = realCaller.getObjectRef();
                    if (objectRef != null)
                    {
                        objectColection.add(objectRef);
                        OwObjectCollection parents = objectRef.getParents();
                        OwObject parent = parents != null && parents.size() > 0 ? (OwObject) parents.get(0) : null;
                        addHistoryEvent(objectRef, parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                    }
                }
            }
                break;
        }

    }

    /**
     * @see com.wewebu.ow.server.ui.OwDialog.OwDialogListener#onDialogClose(com.wewebu.ow.server.ui.OwDialog)
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        if (dialogView_p instanceof OwEditPropertiesDialogSimple)
        {
            OwObject currentItem = ((OwEditPropertiesDialogSimple) dialogView_p).getItem();
            unlockItem(currentItem);
        }

    }

    /**
     * Get defined PropertyList configuration
     * @return OwPropertyListConfiguration (or null if not configured)
     * @since 4.2.0.0
     */
    public OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return propertyListConfiguration;
    }

    /**
     * Set PropertyList configuration
     * @param propertyListConfiguration OwPropertyListConfiguration (can be null)
     * @since 4.2.0.0
     */
    public void setPropertyListConfiguration(OwPropertyListConfiguration propertyListConfiguration)
    {
        this.propertyListConfiguration = propertyListConfiguration;
    }
}