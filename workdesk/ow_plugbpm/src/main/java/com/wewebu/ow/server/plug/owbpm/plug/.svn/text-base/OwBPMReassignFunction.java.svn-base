package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMessageBox;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Workdesk BPM Plugin for reassigning to a different user.
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
public class OwBPMReassignFunction extends OwDocumentFunction implements OwDialog.OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMReassignFunction.class);

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owbpm/reassign.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owbpm/reassign_24.png");
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(com.wewebu.ow.server.ecm.OwObject oObject_p, com.wewebu.ow.server.ecm.OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwWorkitem workitem = (OwWorkitem) oObject_p;

        //		 is note property defined in plugin descriptor
        String noteProperty = getNoteProperty();

        if ((!workitem.canReassignToUserContainer(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)) && (!workitem.canReassignToPublicContainer(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS)))
        {
            throw new OwInvalidOperationException(getContext().localize("plug.owbpm.plug.OwBPMReassignFunction.notenabled", "You cannot forward this work item."));
        }

        //    	 Lock the item
        workitem.setLock(true);

        // create and open the insert note dialog
        try
        {

            // create new insert note dialog
            OwBPMReassignDialog dlg = new OwBPMReassignDialog(workitem, (OwWorkitemContainer) oParent_p, refreshCtx_p, noteProperty);

            // set title
            dlg.setTitle(getLabel(oObject_p, oParent_p));

            // set icon
            dlg.setInfoIcon(getBigIcon());

            getContext().openDialog(dlg, this);
            //historize
            List workitems = new LinkedList();
            workitems.add(workitem);
            OwBPMReassignFunction.this.addHistoryEvent(workitems, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);

        }
        catch (OwBPMNotePropertyException ne)
        {
            // at least one work item has no note property so display message box
            displayMessageBox(ne);

            workitem.setLock(false);
        }
        catch (Exception e)
        {
            // some problems occurred and the dialog is not opened so we have to unlock the workitems manually
            workitem.setLock(false);
        }
    }

    /**
     * @see com.wewebu.ow.server.app.OwDocumentFunction#onMultiselectClickEvent(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // is note property defined in plugin descriptor
        String noteProperty = getNoteProperty();
        if (noteProperty == null)
        {
            LOG.warn("OwBPMReassignFunction.onMultiselectClickEvent: Note property had not been defined in plugin descriptor.");
        }

        // just handle the workitems which canReassign
        List enabledWorkitems = new ArrayList();
        for (Iterator iter = objects_p.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            if (object.canReassignToUserContainer(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) || object.canReassignToPublicContainer(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                enabledWorkitems.add(object);
            }
        }
        if (enabledWorkitems.isEmpty())
        {
            throw new OwInvalidOperationException(getContext().localize("plug.owbpm.plug.OwBPMReassignFunction.any.notenabled", "You cannot forward any of the selected work items."));
        }

        // Lock the items
        for (Iterator iter = enabledWorkitems.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            object.setLock(true);
        }

        // create and open the insert note dialog
        try
        {
            // create new insert note dialog
            OwBPMReassignDialog dlg = new OwBPMReassignDialog(enabledWorkitems, (OwWorkitemContainer) oParent_p, refreshCtx_p, noteProperty);

            // set title
            dlg.setTitle(getLabel(null, oParent_p));

            // set icon
            dlg.setInfoIcon(getBigIcon());

            m_MainContext.openDialog(dlg, this);
            //historize view
            OwBPMReassignFunction.this.addHistoryEvent(enabledWorkitems, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
        }
        catch (OwBPMNotePropertyException ne)
        {
            // at least one work item has no note property so display message box
            displayMessageBox(ne);

            unlockAllWorkitems(enabledWorkitems);
        }
        catch (Exception e)
        {
            // Unlock the items		
            unlockAllWorkitems(enabledWorkitems);
        }

    }

    /** check if function is enabled for the given object parameters
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param iContext_p OwStatusContextDefinitions
    *
    *  @return true = enabled, false otherwise
    */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (!super.isEnabled(oObject_p, oParent_p, iContext_p))
        {
            return false;
        }

        return ((OwWorkitem) oObject_p).canReassignToUserContainer(iContext_p) || ((OwWorkitem) oObject_p).canReassignToPublicContainer(iContext_p);
    }

    /**
     * unlock All Workitems
     * @param objects_p
     * @throws Exception
     */
    private void unlockAllWorkitems(Collection objects_p) throws Exception
    {
        //			  Unlock the items		
        for (Iterator iter = objects_p.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            object.setLock(false);
        }
    }

    /**
     * show message box with the workitems without note properties
     * @param ne_p
     */
    private void displayMessageBox(OwBPMNotePropertyException ne_p)
    {
        // the work item has no note property defined so show message box
        List properties = ne_p.getItemsWithNoNoteProperty();
        OwMessageBox msg = new OwMessageBox(OwMessageBox.TYPE_OK, OwMessageBox.ICON_TYPE_WARNING, getContext().localize("plug.owbpm.OwBPMReassignFunction.msg.title", "Note"), getContext().localize(
                "plug.owbpm.OwBPMReassignFunction.msg.notice.property.missing", "Note property had not been defined for following work items:")
                + properties.toString());
        try
        {
            m_MainContext.openDialog(msg, null);
        }
        catch (Exception e)
        {
            LOG.error("Exception opening dialog", e);
        }
    }

    /** called if the Dialog that was opened by this view closes
    *
    *  @param dialogView_p the Dialog that closed.
    */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        OwBPMReassignDialog dialog = (OwBPMReassignDialog) dialogView_p;

        // Unlock the item
        dialog.unlockAll();
    }

    /** get property from the XML plugin config node */
    private String getNoteProperty()
    {
        return getConfigNode().getSafeTextValue("Note", null);
    }

    /**
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     * @since 2.5.2.0
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        OwWorkitem workItem = getCurrentWorkitem(caller_p);
        if (workItem != null)
        {
            OwObject parent = getParent(workItem);

            switch (code_p)
            {
                case OwUpdateCodes.MODIFIED_OBJECT_PROPERTY:
                    OwBPMReassignFunction.this.addHistoryEvent(workItem, parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                    break;
                case OwUpdateCodes.OBJECT_FORWARD:
                    OwBPMReassignFunction.this.addHistoryEvent(workItem, parent, OwEventManager.HISTORY_EVENT_TYPE_CLEAR_SESSION_HISTORY_FOR_OBJECT, OwEventManager.HISTORY_STATUS_OK);
                    break;
            }
        }
    }

    /**
     * Get the parent of the work item
     * @param workItem_p
     * @return - the parent of the given work item or <code>null</code>
     * @throws Exception
     * @since 3.1.0.0
     */
    private OwObject getParent(OwWorkitem workItem_p) throws Exception
    {
        OwObjectCollection parents = workItem_p.getParents();
        OwObject parent = parents != null && parents.size() > 0 ? (OwObject) parents.get(0) : null;
        return parent;
    }

    /**
     * Get the current workitem.
     * @param caller_p - the {@link OwEventTarget} that fired the event.
     * @return - the current work item or <code>null</code>
     * @since 3.1.0.0
     */
    private OwWorkitem getCurrentWorkitem(OwEventTarget caller_p)
    {
        OwWorkitem workItem = null;

        if (caller_p != null && caller_p instanceof OwBPMReassignDialog)
        {
            workItem = ((OwBPMReassignDialog) caller_p).getCurrentItem();
        }
        return workItem;
    }
}