package com.wewebu.ow.server.plug.owdocdel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMessageBox;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.history.OwStandardHistoryObjectDeleteEvent;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Delete Record Function to delete an object.
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
public class OwDocumentFunctionDel extends OwDocumentFunction
{
    /** How many object should be displayed in the message box */
    public int TOTAL_ELEMENTS_DISPLAY = 10;

    /** get the URL to the icon of the dialog / function */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdelref/deletebtn.png");
    }

    /** get the URL to the icon of the dialog / function*/
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdelref/deletebtn_24.png");
    }

    /** check if function is enabled for the given object parameters
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *
     *  @return true = enabled, false otherwise
     */
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        return (super.isEnabled(oObject_p, oParent_p, iContext_p) && oObject_p.canDelete(iContext_p));
    }

    /** event called when user clicked the plugin label / icon 
     * 
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            // search for a reason why the function is not enable
            if (oObject_p.hasVersionSeries() && oObject_p.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                boolean isMyCheckedOut = false;
                try
                {
                    isMyCheckedOut = oObject_p.getVersion().isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }
                catch (Exception ex)
                {
                    //isMyCheckedOut() is not supported or not implemented or exception occurred
                }
                if (!isMyCheckedOut)
                {
                    String checkedoutBy = null;
                    try
                    {
                        checkedoutBy = oObject_p.getVersion().getCheckedOutUserID(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                    }
                    catch (Exception e)
                    {
                        //getCheckedOutUserID() is not supported or not implemented or exception occurred     
                    }
                    if (checkedoutBy != null && !checkedoutBy.equals(""))
                    {
                        checkedoutBy = getDisplayNameFromUserId(checkedoutBy);
                        throw new OwInvalidOperationException(getContext().localize1("plug.owdms.OwDocumentFunctionDel.documentCheckedOutbyOtherUser", "Cannot delete in the document. The document is checked out by user (%1).", checkedoutBy));
                    }
                }
            }
            throw new OwInvalidOperationException(getContext().localize("owdocdel.OwDocumentFunctionDel.invalidobject", "Item cannot be deleted."));
        }

        StringBuffer dialogContent = new StringBuffer();
        //insert message what happens
        dialogContent.append(getContext().localize("owdocdel.OwDocumentFunctionDel.wanttodeleteobject", "Warning!<br><br>You are about to delete this item permanently. In doing so, all links to the item are deleted as well.<br />"));
        //insert object name which is processed
        dialogContent.append("<ul class=\"OwMessageBoxText\">");
        dialogContent.append("<li>");
        dialogContent.append(OwHTMLHelper.encodeToSecureHTML(oObject_p.getName()));
        dialogContent.append("</li>");
        dialogContent.append("</ul><br />");
        //insert question for user
        dialogContent.append(getContext().localize("owdocdel.OwDocumentFunctionDel.continue", "Do you really want to continue?"));

        // === create a message box to ask user if he really wants to remove the reference
        OwDelMessageBox dlg = new OwDelMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), dialogContent.toString());

        // save the object refs to the dialog, we need it later in the dialog
        dlg.m_Objects = new ArrayList(1);
        dlg.m_Objects.add(oObject_p);

        dlg.m_Objectnames = new ArrayList(1);
        dlg.m_Objectnames.add(oObject_p.getName());

        dlg.m_refreshCtx = refreshCtx_p;
        dlg.m_Parent = oParent_p;

        // historize begin
        addHistoryEvent(dlg.m_Objectnames, dlg.m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);

        // open the dialog
        getContext().openDialog(dlg, null);
    }

    /** event called when user clicked the plugin for multiple selected items
     *
     *  @param objects_p Iterator of OwObject 
     *  @param oParent_p Parent which listed the Objects
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        // === create a message box to ask user if he really wants to remove the reference

        // === create a string with the first TOTAL_ELEMENTS_DISPLAY objects
        StringBuffer dialogContent = new StringBuffer();
        //create description of what happens
        dialogContent.append(getContext().localize("owdocdel.OwDocumentFunctionDel.wanttodeleteobjects", "Warning!<br><br>You are about to delete these items permanently. In doing so, all links to the items are deleted as well."));

        dialogContent.append("<ul class=\"OwMessageBoxText\">");

        Iterator it = objects_p.iterator();
        //create list of objects which are processed
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();

            if (isEnabled(obj, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                dialogContent.append("<li>");
                dialogContent.append(OwHTMLHelper.encodeToSecureHTML(obj.getName()));
                dialogContent.append("</li>");
                if ((0 == --TOTAL_ELEMENTS_DISPLAY) && it.hasNext())
                {
                    // there are more than TOTAL_ELEMENTS_DISPLAY objects, add ellipses and break
                    dialogContent.append("<li>");
                    dialogContent.append("...");
                    dialogContent.append("</li>");
                    break;
                }

            }
        }

        dialogContent.append("</ul><br />");
        //create question for user
        dialogContent.append(getContext().localize("owdocdel.OwDocumentFunctionDel.continue", "Do you really want to continue?"));

        OwDelMessageBox dlg = new OwDelMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), dialogContent.toString());

        // save the object refs to the dialog, we need it later in the dialog
        dlg.m_Objects = objects_p;
        dlg.m_Objectnames = new ArrayList(objects_p.size());
        for (Iterator iterator = objects_p.iterator(); iterator.hasNext();)
        {
            OwObject obj = (OwObject) iterator.next();
            dlg.m_Objectnames.add(obj.getName());

        }
        dlg.m_refreshCtx = refreshCtx_p;
        dlg.m_Parent = oParent_p;

        // historize begin
        addHistoryEvent(dlg.m_Objectnames, dlg.m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);

        // open the dialog
        getContext().openDialog(dlg, null);
    }

    /** overwrite to use delete event instead 
     * add the plugin invoke event to the history manager
    *
    *  @param objectnames_p Collection of object names. Only {@link String} values are permitted. 
    *  @param oParent_p Parent which listed the Object
    *  @param  iEventType_p int one out of:  
    *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI
    *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW
    *           OwEventManger.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT
    *  @param iStatus_p int Status as defined in OwEventManger.HISTORY_STATUS_...
    */
    protected void addHistoryEvent(Collection objectnames_p, OwObject oParent_p, int iEventType_p, int iStatus_p) throws Exception
    {
        // === historize event
        getEventManager().addEvent(iEventType_p, getPluginID(), new OwStandardHistoryObjectDeleteEvent(objectnames_p, oParent_p), iStatus_p);
    }

    /** overwritten MessageBox which implements the onOK method to delete the object
     */
    private class OwDelMessageBox extends OwMessageBox
    {
        /** saved object references */
        public Collection m_Objects;
        /** saved object names, necessary for history event */
        public Collection m_Objectnames;
        /** saved object parent reference */
        public OwObject m_Parent;
        /** saved refresh context */
        public OwClientRefreshContext m_refreshCtx;

        /** construct a message box with given type 
         * @param type_p int type of message box buttons
         * @param icontype_p int type of message box icon / behavior 
         * @param strTitle_p the title as <code>String</code> 
         * @param strText_p the text as <code>String</code> 
         */
        public OwDelMessageBox(int type_p, int icontype_p, String strTitle_p, String strText_p)
        {
            super(type_p, OwMessageBox.ACTION_STYLE_BUTTON, icontype_p, strTitle_p, strText_p);
        }

        /** called when user pressed the OK button
         */
        public void onOK() throws Exception
        {

            final OwProcessableObjectStrategy processableObjectStrategy = new OwProcessableObjectStrategy() {

                public boolean canBeProcessed(OwObject object_p) throws Exception
                {
                    return isEnabled(object_p, null, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }
            };
            OwCommandDel commandDel = new OwCommandDel(m_Objects, (OwMainAppContext) getContext(), processableObjectStrategy);
            commandDel.execute();

            //historize success, if any
            if (commandDel.hasProcessedObjects())
            {
                // historize success
                addHistoryEvent(commandDel.getProcessedObjectNames(), m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                // === refresh necessary, call client
                if (null != m_refreshCtx)
                {
                    m_refreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.DELETE_OBJECT, commandDel.getProcessedDMSIDs());
                }

            }

            if (commandDel.hasDisabledObjects())
            {
                addHistoryEvent(commandDel.getDisabledObjectNames(), m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_DISABLED);
            }

            if (commandDel.hasErrors())
            {
                // historize failure
                addHistoryEvent(commandDel.getAllErrorNames(), m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                // re-throw exception
                throw new Exception(commandDel.getAllErrorMessages());
            }

        }

        /**
         * Called when user pressed the CANCEL button
         */
        public void onCancel() throws Exception
        {
            // historize cancel
            addHistoryEvent(m_Objectnames, m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_CANCEL);
        }

    }
}