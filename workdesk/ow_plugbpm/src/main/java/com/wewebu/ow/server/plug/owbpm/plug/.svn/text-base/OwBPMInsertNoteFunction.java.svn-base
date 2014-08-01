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
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Workdesk BPM Plugin for inserting a note.
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
public class OwBPMInsertNoteFunction extends OwDocumentFunction implements OwDialog.OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMInsertNoteFunction.class);

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owbpm/note.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owbpm/note_24.png");
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
        // is note property defined in plugin descriptor
        String noteProperty = getNoteProperty();
        if (noteProperty == null)
        {
            String msg = "OwBPMInsertNoteFunction.onClickEvent: Note property had not been defined at plugin descriptor.";
            LOG.fatal(msg);
            throw new OwConfigurationException(getContext().localize("plug.owbpm.plug.OwBPMInsertNoteFunction.notenotdefined", "Notiz Property wurde im Plugin Descriptor nicht eingestellt!"));
        }

        //    	 Lock the item
        ((OwWorkitem) oObject_p).setLock(true);

        // create and open the insert note dialog
        try
        {

            // create new insert note dialog
            OwBPMInsertNoteDialog dlg = new OwBPMInsertNoteDialog((OwWorkitem) oObject_p, noteProperty, true);

            // set title
            dlg.setTitle(getLabel(oObject_p, oParent_p));

            // set icon
            dlg.setInfoIcon(getBigIcon());

            getContext().openDialog(dlg, this);

            List objects = new LinkedList();
            objects.add(oObject_p);

            OwBPMInsertNoteFunction.this.addHistoryEvent(objects, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
        }
        catch (OwBPMNotePropertyException ne)
        {
            // at least one work item has no note property so display message box
            displayMessageBox(ne);

            ((OwWorkitem) oObject_p).setLock(false);
        }
        catch (Exception e)
        {
            // some problems occurred and the dialog is not opened so we have to unlock the workitems manually
            ((OwWorkitem) oObject_p).setLock(false);
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
            String msg = "OwBPMInsertNoteFunction.onMultiselectClickEvent: Note property had not been defined at plugin descriptor.";
            LOG.fatal(msg);
            throw new OwConfigurationException(getContext().localize("plug.owbpm.plug.OwBPMInsertNoteFunction.notenotdefined", "Notiz Property wurde im Plugin Descriptor nicht eingestellt!"));
        }

        //  Lock the items		
        for (Iterator iter = objects_p.iterator(); iter.hasNext();)
        {
            OwWorkitem object = (OwWorkitem) iter.next();
            object.setLock(true);
        }

        // create and open the insert note dialog
        try
        {
            // create new insert note dialog
            OwBPMInsertNoteDialog dlg = new OwBPMInsertNoteDialog(new ArrayList(objects_p), noteProperty, true);

            // set title
            dlg.setTitle(getLabel(null, oParent_p));

            // set icon
            dlg.setInfoIcon(getBigIcon());

            m_MainContext.openDialog(dlg, this);

            OwBPMInsertNoteFunction.this.addHistoryEvent(objects_p, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
        }
        catch (OwBPMNotePropertyException ne)
        {
            // at least one work item has no note property so display message box
            displayMessageBox(ne);

            unlockAllWorkitems(objects_p);
        }
        catch (Exception e)
        {
            unlockAllWorkitems(objects_p);
        }
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
     * @throws Exception 
     */
    private void displayMessageBox(OwBPMNotePropertyException ne_p) throws Exception
    {
        // the work item has no note property defined so show message box
        List properties = ne_p.getItemsWithNoNoteProperty();
        OwMessageBox msg = new OwMessageBox(OwMessageBox.TYPE_OK, OwMessageBox.ICON_TYPE_WARNING, getContext().localize("plug.owbpm.OwBPMInsertNoteFunction.msg.title", "Note"), getContext().localize(
                "plug.owbpm.OwBPMInsertNoteFunction.msg.notice.property.missing", "Note attribute is not defined for following work items.")
                + properties.toString());

        m_MainContext.openDialog(msg, null);
    }

    /** called if the Dialog that was opened by this view closes
    *
    *  @param dialogView_p the Dialog that closed.
    */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        OwBPMInsertNoteDialog dialog = (OwBPMInsertNoteDialog) dialogView_p;

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
        switch (code_p)
        {
            case OwUpdateCodes.MODIFIED_OBJECT_PROPERTY:
            {
                // on apply was pressed in the property view
                if (caller_p instanceof OwBPMInsertNoteDialog)
                {
                    OwWorkitem workitem = ((OwBPMInsertNoteDialog) caller_p).getCurrentItem();
                    if (workitem != null)
                    {
                        OwObjectCollection parents = workitem.getParents();
                        OwObject parent = parents != null && parents.size() > 0 ? (OwObject) parents.get(0) : null;
                        OwBPMInsertNoteFunction.this.addHistoryEvent(workitem, parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                    }
                }
            }
                break;
        }
    }
}