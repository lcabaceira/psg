package com.wewebu.ow.server.plug.owbpm.plug;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * Workdesk BPM Plugin for a Standard/JSP Processor which display a graphical representation of workflow.
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
 *@since 4.2.0.0
 */
public class OwBPMDisplayGraphicalWorkflow extends OwDocumentFunction
{
    /** Listener that wants to be notified upon dialog events. */
    protected OwDialogListener m_Listener;

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owbpm/workflow.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owbpm/workflow_24.png");
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
            throw new OwInvalidOperationException(getContext().localize("plug.owbpm.OwBPMDisplayGraphicalWorkflow.invalidobject", "Cannot create object for graphical workflow representation."));
        }

        List objects = new LinkedList();

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
        // Lock the items
        Iterator it = objects_p.iterator();
        Set<OwObject> objectsToDisplayTheDiagramFor = new HashSet<OwObject>();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            if (isEnabled(obj, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                objectsToDisplayTheDiagramFor.add(obj);
            }

        }

        if (objects_p.size() == 0)
        {
            return;
        }
        displayMessageBox(objectsToDisplayTheDiagramFor, oParent_p);
    }

    /**
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // here receive update events from the views

    }

    /**
     * Display workflows in graphical representation for all ids in the list
     * @param objects
     * @throws Exception 
     */
    private void displayMessageBox(Set<OwObject> objects, OwObject oParent_p) throws Exception
    {

        OwBPMDisplayWorkflowDialog dlg = buildOwBPMDisplayWorkflowDialog(objects, oParent_p);
        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());
        dlg.setTitle(getContext().localize("owbpm.OwBPMDisplayWorkflowDialog.helptitle", "Display Workflow"));

        // set info icon
        dlg.setInfoIcon(getBigIcon());
        // open dialog
        getContext().openDialog(dlg, m_Listener);
    }

    private OwBPMDisplayWorkflowDialog buildOwBPMDisplayWorkflowDialog(Set<OwObject> objects, OwObject oParent_p) throws Exception
    {
        OwBPMDisplayWorkflowDialogBuilder builder = createDialogBuilder();
        builder.items(objects).index(0).parentObject(oParent_p);

        return builder.build();
    }

    /**
     * Create a builder instance for OwEditPropertiesDialog creation.
     * @return instance of OwEditPropertiesDialogBuilder
     * @since 4.2.0.0
     */
    protected OwBPMDisplayWorkflowDialogBuilder createDialogBuilder()
    {
        return new OwBPMDisplayWorkflowDialogBuilder();
    }

}