package com.wewebu.ow.server.ui;

import java.io.Writer;
import java.util.List;

/**
 *<p>
 * Dialog Manager. Opens Dialogs, redirects requests to dialogs, handles recursive calls to dialogs.<br/>
 * Dialog Manager itself is treated as a view and can show up anywhere in the layout.
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
public class OwDialogManager extends OwView
{

    /** display a new dialog, adds it to the context for Event handling. 
     * After it is closed it gets removed from the DialogManager and the context to finalize the Dialog View
     * @param dialog_p Dialog-View of the new dialog
     * @param listener_p Listener that wants to be notified upon dialog events.
     */
    public void openDialog(OwDialog dialog_p, OwDialog.OwDialogListener listener_p) throws Exception
    {
        // get target list count before attaching the view
        int iDetachedTargetCount = getContext().getTargetCount();

        // save document object that dialog was initialized with
        dialog_p.m_iDEBUG_InitDocument = dialog_p.getDocument();

        // save attached views
        dialog_p.m_iDEBUG_DocumentViewSize = getDocument().m_ViewList.size();

        // add view and initialize it
        addView(dialog_p, null);

        // add the OwUpdateTarget to the document
        if (listener_p != null)
        {
            dialog_p.getDocument().attachView(listener_p);
        }

        // the parent is the caller, not the dialog manager
        dialog_p.setListener(listener_p);

        // save target count difference
        dialog_p.m_iDEBUG_TargetCount = getContext().getTargetCount() - iDetachedTargetCount;
    }

    /** closes the latest dialog and removes it from the dialog list, and the context.
     *
     * @param dialog_p Dialog-View to get closed
     */
    public void closeDialog(OwDialog dialog_p) throws Exception
    {
        List viewList = getViewList();

        if (viewList.size() > 0)
        {
            // === get target list count before detaching the view
            int iAttachedTargetCount = getContext().getTargetCount();

            // === get topmost dialog
            OwView dlgView = (OwView) viewList.get(viewList.size() - 1);

            // check if submitted view is really the topmost one
            if (dialog_p != dlgView)
            {
                throw new Exception("Can only close the topmost dialog!");
            }

            // === remove view, so it is subject to the garbage collection
            // remove it from the context, so it is no longer an event target
            dialog_p.detach();

            // remove the OwUpdateTarget from the document
            if (dialog_p.m_Listener != null)
            {
                dialog_p.getDocument().detachView(dialog_p.m_Listener);
            }

            // remove it from the child list
            viewList.remove(viewList.size() - 1);

            // === check attached views to document
            if (dialog_p.m_iDEBUG_DocumentViewSize != getDocument().m_ViewList.size())
            {
                throw new Exception("Attached views count exceeded. Did you forget to detach a view in your dialog document or did you not correctly assign a document in your dialog?");
            }

            // === check target count
            if ((iAttachedTargetCount - getContext().getTargetCount()) < dialog_p.m_iDEBUG_TargetCount)
            {
                throw new Exception("Target count exceeded. Did you forget to detach an event target in your dialog?");
            }

            // === check document
            if ((dialog_p.m_iDEBUG_InitDocument == null) && (dialog_p.getDocument() != getDocument()))
            {
                throw new Exception("Document changed in dialog after construction. Please set dialog in constructor.");
            }

            if ((dialog_p.m_iDEBUG_InitDocument != null) && (dialog_p.m_iDEBUG_InitDocument != dialog_p.getDocument()))
            {
                throw new Exception("Document changed in dialog after construction. Please set dialog in constructor.");
            }
        }
    }

    /** check if a dialog is currently open
     * @return true if dialog needs to be shown, otherwise false.
     */
    public boolean isDialogOpen()
    {
        return (null != getCurrentDialog());
    }

    /** closes all dialogs */
    public void closeAllDialogs() throws Exception
    {
        OwDialog dlg = getCurrentDialog();

        while (null != dlg)
        {
            dlg.closeDialog();
            dlg = getCurrentDialog();
        }
    }

    /** get the current topmost dialog 
     * @return OwDialog
     */
    protected OwDialog getCurrentDialog()
    {
        List viewList = getViewList();

        if (viewList.size() > 0)
        {
            return (OwDialog) viewList.get(viewList.size() - 1);
        }
        else
        {
            return null;
        }
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // render only the last dialog all others are hidden.
        OwDialog Dialog = getCurrentDialog();
        if (Dialog != null)
        {
            Dialog.render(w_p);
        }
    }

    public String getTitle()
    {
        if (getCurrentDialog() != null)
        {
            return getCurrentDialog().getTitle();
        }
        return "";
    }
}