package com.wewebu.ow.server.plug.owdocdel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

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
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Delete Reference Record Function to remove references form objects.
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
public class OwDocumentFunctionDelRef extends OwDocumentFunction
{
    /** How many object should be displayed in the message box */
    public int TOTAL_ELEMENTS_DISPLAY = 10;

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdelref/remove.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdelref/remove_24.png");
    }

    /** check if plugin needs oParent_p parameter in onClick handler
     *
     * @return boolean true = enable plugin only, if a oParent_p parameter is available for the selected object, false = oParent_p can be null.
     */
    public boolean getNeedParent()
    {
        return true;
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
        return (super.isEnabled(oObject_p, oParent_p, iContext_p) && oParent_p.canRemoveReference(oObject_p, iContext_p));
    }

    /** overwritten MessageBox which implements the onOK method to delete the reference
     */
    private class OwDelRefMessageBox extends OwMessageBox
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
         * @param strTitle_p a String 
         * @param strText_p a String 
         */
        public OwDelRefMessageBox(int type_p, int icontype_p, String strTitle_p, String strText_p)
        {
            super(type_p, OwMessageBox.ACTION_STYLE_BUTTON, icontype_p, strTitle_p, strText_p);
        }

        /** called when user pressed the OK button
         */
        public void onOK() throws Exception
        {
            OwProcessableObjectStrategy processableObjectStrategy = new OwProcessableObjectStrategy() {

                public boolean canBeProcessed(OwObject object_p) throws Exception
                {
                    return isEnabled(object_p, m_Parent, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                }

            };

            OwCommandDelRef commandDelRef = new OwCommandDelRef(m_Objects, m_Parent, (OwMainAppContext) getContext(), processableObjectStrategy);
            commandDelRef.execute();

            if (commandDelRef.hasProcessedObjects())
            {
                // historize success
                addHistoryEvent(commandDelRef.getProcessedObjectNames(), m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
            }
            if (commandDelRef.hasDisabledObjects())
            {
                addHistoryEvent(commandDelRef.getDisabledObjectNames(), m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_DISABLED);
            }
            if (commandDelRef.hasErrors())
            {
                addHistoryEvent(commandDelRef.getAllErrorNames(), m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                throw new Exception(commandDelRef.getAllErrorMessages());
            }
            // === refresh necessary, call client
            if (null != m_refreshCtx)
            {
                m_refreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.DELETE_OBJECT, null);
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
            throw new OwInvalidOperationException(new OwString("owdocdel.OwDocumentFunctionDelRef.invalidobject", "Item cannot be removed."));
        }

        StringBuffer dialogContent = new StringBuffer();

        OwDelRefMessageBox dlg;
        if (1 == oObject_p.getParents().size())
        {
            // === create a message box to ask user if he really wants to remove the last reference to the object
            dialogContent.append(getContext().localize("owdocdel.OwDocumentFunctionDelRef.wanttodeletelastobject", "Warning!<br><br>You are about to delete the last link to this item."));
            //            dlg = new OwDelRefMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), 
            //                    + strObjectsTitleWithEllipses + "<br>" + );
        }
        else
        {
            // === create a message box to ask user if he really wants to remove the reference
            dialogContent.append(getContext().localize("owdocdel.OwDocumentFunctionDelRef.wanttodeleteobject", "Warning!<br><br>You are about to delete the link to this item."));

        }

        dialogContent.append("<ul class=\"OwMessageBoxText\">");
        dialogContent.append("<li>");
        dialogContent.append(OwHTMLHelper.encodeToSecureHTML(oObject_p.getName()));
        dialogContent.append("</li>");
        dialogContent.append("</ul><br/>");
        dialogContent.append(getContext().localize("owdocdel.OwDocumentFunctionDelRef.continue", "Do you really want to continue?"));

        //create dialog with given content
        dlg = new OwDelRefMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), dialogContent.toString());

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
        StringBuffer strObjectsTitleWithEllipses = new StringBuffer();
        strObjectsTitleWithEllipses.append("<ul class=\"OwMessageBoxText\">");

        // === create a vector containing the objects with only one reference
        Vector lastReferences = new Vector();

        Iterator it = objects_p.iterator();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            if (isEnabled(obj, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                strObjectsTitleWithEllipses.append("<li>");
                strObjectsTitleWithEllipses.append(OwHTMLHelper.encodeToSecureHTML(obj.getName()));

                if (1 == obj.getParents().size())
                {
                    lastReferences.add(obj);
                    strObjectsTitleWithEllipses.append(" (*)");
                }

                strObjectsTitleWithEllipses.append("</li>");

                if ((0 == --TOTAL_ELEMENTS_DISPLAY) && it.hasNext())
                {
                    // there are more than TOTAL_ELEMENTS_DISPLAY objects, add ellipses and break
                    strObjectsTitleWithEllipses.append("<li>");
                    strObjectsTitleWithEllipses.append("...");
                    strObjectsTitleWithEllipses.append("</li>");
                    break;
                }

            }
        }
        strObjectsTitleWithEllipses.append("</ul>");

        OwDelRefMessageBox dlg;
        if (0 < lastReferences.size())
        {
            // there is at least one object that has only this last reference left
            dlg = new OwDelRefMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), getContext().localize("owdocdel.OwDocumentFunctionDelRef.wanttodeletelastobjects",
                    "Warning!<br><br>You are about to delete the last link to this item.")
                    + strObjectsTitleWithEllipses + "<br>" + getContext().localize("owdocdel.OwDocumentFunctionDelRef.continue", "Do you really want to continue?"));
        }
        else
        {
            // every object has more than one reference
            dlg = new OwDelRefMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), getContext().localize("owdocdel.OwDocumentFunctionDelRef.wanttodeleteobjects",
                    "Warning!<br><br>You are about to delete the links to these items.")
                    + strObjectsTitleWithEllipses + "<br>" + getContext().localize("owdocdel.OwDocumentFunctionDelRef.continue", "Do you really want to continue?"));
        }

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

}