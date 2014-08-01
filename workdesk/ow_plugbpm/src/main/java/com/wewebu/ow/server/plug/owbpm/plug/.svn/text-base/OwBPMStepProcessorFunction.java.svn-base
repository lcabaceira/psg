package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Workdesk BPM Plugin for a Standard/JSP Processor which displays all properties.
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
 *@since 3.1.0.0
 */
public class OwBPMStepProcessorFunction extends OwDocumentFunction implements OwDialogListener
{
    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owbpm/standardprocessor.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owbpm/standardprocessor_24.png");
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
        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(getContext().localize("plug.owbpm.OwBPMStandardProcessorFunction.invalidobject", "Das Objekt kann nicht im Schrittprozessor bearbeitet werden."));
        }

        // Lock the item
        ((OwWorkitem) oObject_p).setLock(true);

        // create new step processor dialog
        List objects = new ArrayList();
        objects.add(oObject_p);
        OwStandardSequenceDialog dlg = createDialog(objects, refreshCtx_p, getConfigNode());

        // set title
        dlg.setTitle(getLabel(oObject_p, oParent_p));

        // set icon
        dlg.setInfoIcon(getBigIcon());

        getContext().openDialog(dlg, this);
        // historize
        addHistoryEvent(((OwBPMProcessorDialog) dlg).getWorkItems(), null, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }

    /** overridable factory method
     * 
     * @param objects_p
     * @param refreshCtx_p
     * @param configNode_p
     * @return OwBPMPreviewProcessorDialog
     * @throws Exception
     */
    protected OwBPMStandardProcessorDialog createProcessorDialog(Collection objects_p, OwClientRefreshContext refreshCtx_p, OwXMLUtil configNode_p) throws Exception
    {
        return new OwBPMStandardProcessorDialog(objects_p, refreshCtx_p, configNode_p);
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
        // Lock the items
        Iterator it = objects_p.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            if (isEnabled(obj, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                ((OwWorkitem) obj).setLock(true);
            }
            else
            {
                it.remove();
            }
        }

        if (objects_p.size() == 0)
        {
            return;
        }

        // create new step processor dialog
        OwStandardSequenceDialog dlg = createDialog(objects_p, refreshCtx_p, getConfigNode());

        // set title
        dlg.setTitle(getPluginTitle());

        // set icon
        dlg.setInfoIcon(getBigIcon());

        getContext().openDialog(dlg, this);
        addHistoryEvent(((OwBPMProcessorDialog) dlg).getWorkItems(), null, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * Create the corresponding dialog.
     * @param objects_p
     * @param refreshCtx_p
     * @param configNode_p
     * @return the dialog object
     * @throws Exception
     * @since 3.1.0.0
     */
    protected OwStandardSequenceDialog createDialog(Collection objects_p, OwClientRefreshContext refreshCtx_p, OwXMLUtil configNode_p) throws Exception
    {
        OwStandardSequenceDialog dialog = null;
        String jspForm = configNode_p.getSafeTextValue("JspForm", null);
        if (jspForm != null)
        {
            dialog = createJspProcessorDialog(objects_p, configNode_p, refreshCtx_p);
        }
        else
        {
            dialog = createProcessorDialog(objects_p, refreshCtx_p, configNode_p);
        }
        return dialog;
    }

    /** called if the Dialog that was opened by this view closes
     *
     *  @param dialogView_p the Dialog that closed.
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        // Unlock the items
        ((OwBPMProcessorDialog) dialogView_p).unlock();

    }

    /**
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // here receive update events from the views
        if (code_p == OwUpdateCodes.MODIFIED_OBJECT_PROPERTY || code_p == OwUpdateCodes.OBJECT_DISPATCH)
        {
            // on apply was pressed in the property view
            if (caller_p instanceof OwBPMStandardProcessorView)
            {
                OwBPMStandardProcessorView realCaller = (OwBPMStandardProcessorView) caller_p;
                OwObject objectRef = realCaller.getObjectRef();
                historize(objectRef, code_p);
            }
            else if (caller_p instanceof OwBPMJspProcessorView)
            {
                OwBPMJspProcessorView realCaller = (OwBPMJspProcessorView) caller_p;
                OwObject objectRef = realCaller.getObjectRef();
                historize(objectRef, code_p);
            }
        }

    }

    /**
     * Historize the edit properties status
     * @param objectRef_p - the object
     * @throws Exception
     */
    private void historize(OwObject objectRef_p, int code_p) throws Exception
    {
        if (objectRef_p != null)
        {
            OwObjectCollection parents = objectRef_p.getParents();
            OwObject parent = parents != null && parents.size() > 0 ? (OwObject) parents.get(0) : null;
            if (code_p == OwUpdateCodes.MODIFIED_OBJECT_PROPERTY)
            {
                addHistoryEvent(objectRef_p, parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
            }
            else if (code_p == OwUpdateCodes.OBJECT_DISPATCH)
            {
                addHistoryEvent(objectRef_p, parent, OwEventManager.HISTORY_EVENT_TYPE_CLEAR_SESSION_HISTORY_FOR_OBJECT, OwEventManager.HISTORY_STATUS_OK);
            }
        }
    }

    /**
     * Create the JSP based step processor dialog.    
     * @param objects_p - the collection  of objects.
     * @param configNode_p  - the configuration node.
     * @param refreshCtx_p - the refresh context object.
     * @return the newly created {@link OwBPMJspProcessorDialog}
     * @throws Exception
     */
    protected OwBPMJspProcessorDialog createJspProcessorDialog(Collection objects_p, OwXMLUtil configNode_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        return new OwBPMJspProcessorDialog(objects_p, configNode_p, refreshCtx_p);
    }
}