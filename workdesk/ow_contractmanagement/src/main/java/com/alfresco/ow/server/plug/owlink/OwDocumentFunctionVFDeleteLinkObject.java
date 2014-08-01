package com.alfresco.ow.server.plug.owlink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver;
import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMessageBox;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.plug.owdocdel.OwCommandDel;
import com.wewebu.ow.server.plug.owdocdel.OwDocumentFunctionDel;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwDocumentFunctionVFDeleteLinkObject<br/>
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
public class OwDocumentFunctionVFDeleteLinkObject extends OwDocumentFunctionDel
{
    public static final String PLUGIN_PARAM_USED_PARENT_FOLDER = "ParentFolder";

    private OwParentFolderResolver rootFolderResolver;

    /** set the plugin description node 
     * @param node_p OwXMLUtil wrapped DOM Node containing the plugin description
     * @param context_p OwMainAppContext
     */
    @Override
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        rootFolderResolver = new OwParentFolderResolver();
        rootFolderResolver.init(node_p.getSafeTextValue(PLUGIN_PARAM_USED_PARENT_FOLDER, ""), context_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionCut#getNeedParent()
     */
    @Override
    public boolean getNeedParent()
    {
        if (rootFolderResolver.isExplicitDefinedRootFolder())
        {
            return false;
        }

        return super.getNeedParent();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionCut#isEnabled(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean isEnabled(@SuppressWarnings("rawtypes") Collection objects_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        List<Object> refenences = new ArrayList<Object>();
        for (Object object : objects_p)
        {
            if (object instanceof OwReferencedObject)
            {
                refenences.add(((OwReferencedObject) object).getObjectLink());
            }
            else
            {
                return false;
            }
        }
        OwObject resolvedParent = rootFolderResolver.getRootFolder(objects_p, oParent_p);
        return super.isEnabled(refenences, resolvedParent, iContext_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdoccopy.OwDocumentFunctionCut#isEnabled(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, int)
     */
    @Override
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (oObject_p instanceof OwReferencedObject)
        {
            OwObject resolvedParent = rootFolderResolver.getRootFolder(oObject_p, oParent_p);
            return super.isEnabled(((OwReferencedObject) oObject_p).getObjectLink(), resolvedParent, iContext_p);
        }

        if (oObject_p.getType() == OwObjectReference.OBJECT_TYPE_LINK)
        {
            OwObject resolvedParent = rootFolderResolver.getRootFolder(oObject_p, oParent_p);
            return super.isEnabled(oObject_p, resolvedParent, iContext_p);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdocdel.OwDocumentFunctionDel#onClickEvent(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    @Override
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(oObject_p, oParent_p);
        if (oObject_p instanceof OwReferencedObject)
        {
            OwObjectLink linkObject = ((OwReferencedObject) oObject_p).getObjectLink();
            StringBuffer dialogContent = new StringBuffer();
            //insert message what happens
            dialogContent.append(getContext().localize("owlinkdel.OwDocumentFunctionVFDeleteLinkObject.wanttodeleteobject", "Warning!<br><br>You are about to delete this link permanently. The object itself will not deleted.<br />"));
            //insert object name which is processed
            dialogContent.append("<ul class=\"OwMessageBoxText\">");
            dialogContent.append("<li>");
            dialogContent.append(OwHTMLHelper.encodeToSecureHTML(oObject_p.getName()));
            dialogContent.append("</li>");
            dialogContent.append("</ul><br />");
            //insert question for user
            dialogContent.append(getContext().localize("owlinkdel.OwDocumentFunctionVFDeleteLinkObject.continue", "Do you really want to continue?"));

            // === create a message box to ask user if he really wants to remove the reference
            OwDelMessageBox dlg = new OwDelMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), dialogContent.toString());

            // save the object refs to the dialog, we need it later in the dialog
            dlg.m_Objects = new ArrayList(1);
            dlg.m_Objects.add(linkObject);

            dlg.m_Objectnames = new ArrayList(1);
            dlg.m_Objectnames.add(linkObject.getName());

            dlg.m_refreshCtx = refreshCtx_p;
            dlg.m_Parent = resolvedParent;

            // historize begin
            addHistoryEvent(dlg.m_Objectnames, dlg.m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);

            // open the dialog
            getContext().openDialog(dlg, null);
            //            super.onClickEvent(((OwReferencedObject)oObject_p).getObjectLink(), resolvedParent, refreshCtx_p);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owdocdel.OwDocumentFunctionDel#onMultiselectClickEvent(java.util.Collection, com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.app.OwClientRefreshContext)
     */
    @Override
    public void onMultiselectClickEvent(@SuppressWarnings("rawtypes") Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwObject resolvedParent = rootFolderResolver.getRootFolder(objects_p, oParent_p);
        List<Object> refenences = new ArrayList<Object>();
        for (Object object : objects_p)
        {
            if (object instanceof OwReferencedObject)
            {
                refenences.add(((OwReferencedObject) object).getObjectLink());
            }
            else
            {
                return;
            }
        }

        // === create a message box to ask user if he really wants to remove the reference

        // === create a string with the first TOTAL_ELEMENTS_DISPLAY objects
        StringBuffer dialogContent = new StringBuffer();
        //create description of what happens
        dialogContent.append(getContext().localize("owlinkdel.OwDocumentFunctionVFDeleteLinkObject.wanttodeleteobjects", "Warning!<br><br>You are about to delete this links permanently. The object itself will not deleted.<br />"));

        dialogContent.append("<ul class=\"OwMessageBoxText\">");

        Iterator it = objects_p.iterator();
        //create list of objects which are processed
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();

            if (isEnabled(obj, resolvedParent, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
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
        dialogContent.append(getContext().localize("owlinkdel.OwDocumentFunctionVFDeleteLinkObject.continue", "Do you really want to continue?"));

        OwDelMessageBox dlg = new OwDelMessageBox(OwMessageBox.TYPE_OK_CANCEL, OwMessageBox.ICON_TYPE_QUESTION, getDefaultLabel(), dialogContent.toString());

        // save the object refs to the dialog, we need it later in the dialog
        dlg.m_Objects = objects_p;
        dlg.m_Objectnames = new ArrayList(refenences.size());
        for (Iterator iterator = refenences.iterator(); iterator.hasNext();)
        {
            OwObject obj = (OwObject) iterator.next();
            dlg.m_Objectnames.add(obj.getName());

        }
        dlg.m_refreshCtx = refreshCtx_p;
        dlg.m_Parent = resolvedParent;

        // historize begin
        addHistoryEvent(dlg.m_Objectnames, dlg.m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);

        // open the dialog
        getContext().openDialog(dlg, null);
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
        @Override
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
        @Override
        public void onCancel() throws Exception
        {
            // historize cancel
            addHistoryEvent(m_Objectnames, m_Parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_CANCEL);
        }

    }
}
