package com.wewebu.ow.server.plug.owdocprops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
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

/**
 *<p>
 * Edit multiple documents simultaneously, for a specific set of properties.
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
public class OwEditMultiDocumentFieldsSimple extends OwDocumentFunction implements OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwEditMultiDocumentFieldsSimple.class);

    private static final String ALWAYS_USE_LATEST_VERSION_CONF_ELEMENT = "AlwaysUseLatestVersion";

    private OwObject parent;

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocprops/edit_properties_multi.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocprops/edit_properties_multi_24.png");
    }

    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        Collection objects = new LinkedList();
        objects.add(oObject_p);

        if (getConfigNode().getSafeBooleanValue(ALWAYS_USE_LATEST_VERSION_CONF_ELEMENT, false))
        {
            objects = OwObjectUtils.getLatesVersionObjects(objects);
        }
        Iterator objectsIt = objects.iterator();
        OwObject objectToCheck = (OwObject) objectsIt.next();
        if (!super.isEnabled(objectToCheck, oParent_p, iContext_p))
        {
            return false;
        }

        return objectToCheck.canSetProperties(iContext_p);
    }

    public void onMultiselectClickEvent(Collection objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        Collection enabledObjects = new LinkedList();

        for (Iterator i = objects_p.iterator(); i.hasNext();)
        {
            OwObject objectToEdit = (OwObject) i.next();
            if (isEnabled(objectToEdit, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                enabledObjects.add(objectToEdit);
            }
        }
        if (enabledObjects.isEmpty())
        {
            LOG.error("OwEditMultiDocumentFieldsSimple.onMultiselectClickEvent(): Invalid object collection!The enabled objects collection is empty!");
            throw new OwInvalidOperationException(new OwString("owdocprops.edit.properties.function.invalid.object.collection", "Invalid object collection!"));
        }

        this.parent = parent_p;
        // === get collection of property names to edit
        Collection properties = getConfigNode().getSafeStringList("EditPropertyList");

        // === create new edit properties dialog
        Collection objects = enabledObjects;

        // select version to edit
        if (getConfigNode().getSafeBooleanValue(ALWAYS_USE_LATEST_VERSION_CONF_ELEMENT, false))
        {
            objects = OwObjectUtils.getLatesVersionObjects(enabledObjects);
        }

        // compute view mask
        int iViewMask = 0;
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "DocumentListView", OwEditMultiDocumentFieldsDialogSimple.VIEW_MASK_SHOW_DOCUMENT_LIST);
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "CloseOnSave", OwEditMultiDocumentFieldsDialogSimple.VIEW_MASK_CLOSE_ON_SAVE);
        iViewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "EnablePasteMetadata", OwFieldsView.VIEW_MASK_ENABLE_PASTE_METADATA);
        OwEditMultiDocumentFieldsDialogSimple dlg = new OwEditMultiDocumentFieldsDialogSimple(objects, parent_p, properties);

        dlg.setViewMask(iViewMask);

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());
        dlg.setTitle(getDefaultLabel());

        // set info icon
        dlg.setInfoIcon(getBigIcon());

        // open dialog
        getContext().openDialog(dlg, this);

        // historize
        addHistoryEvent(objects, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);

    }

    public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(object_p, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            LOG.debug("OwEditMultiDocumentFieldsSimple.onClickEvent(): the edit function is not enabled for the given object !");
            throw new OwInvalidOperationException(new OwString("owdocprops.edit.properties.function.invalid.invalid.object", "Item can not be edited!"));
        }

        // delegate to multi select function, both share the same implementation
        Collection objects = new ArrayList();

        objects.add(object_p);

        onMultiselectClickEvent(objects, parent_p, refreshCtx_p);
    }

    /**
     * @see com.wewebu.ow.server.ui.OwDialog.OwDialogListener#onDialogClose(com.wewebu.ow.server.ui.OwDialog)
     * @since 2.5.2.0
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        List failedObjects = ((OwEditMultiDocumentFieldsDialogSimple) dialogView_p).getFailedObjects();
        if (!failedObjects.isEmpty())
        {
            addHistoryEvent(failedObjects, this.parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
        }
    }

    /**
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     * @since 2.5.2.0
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        switch (code_p)
        {
            case OwUpdateCodes.MODIFIED_OBJECT_PROPERTY:
            {
                if (caller_p instanceof OwEditMultiDocumentFieldsDialogSimple)
                {
                    OwEditMultiDocumentFieldsDialogSimple dialog = (OwEditMultiDocumentFieldsDialogSimple) caller_p;
                    OwObject theObject = dialog.getProcessedObject();
                    if (theObject != null)
                    {
                        addHistoryEvent(theObject, this.parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                    }
                }
            }
        }
    }

}