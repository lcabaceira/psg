package com.wewebu.ow.server.ui;

/**
 *<p>
 * Base Class for Dialog Views. Dialog Views display as a dialog with the OwAppContext.openDialog(...) Method.
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
public class OwDialog extends OwLayout
{
    /**
     *<p>
     * Event listener for dialog events.
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
    public interface OwDialogListener extends java.util.EventListener, OwUpdateTarget
    {
        /** called if the Dialog that was opened by this view closes
         *
         *  @param dialogView_p the Dialog that closed.
         */
        public abstract void onDialogClose(OwDialog dialogView_p) throws Exception;
    }

    /** Listener that wants to be notified upon dialog events. */
    protected OwDialogListener m_Listener;

    /** visually close the Dialog. The behavior depends on usage
     *  If this view is a child of a DialogManager, the View gets removed from it.
     */
    public void closeDialog() throws Exception
    {
        OwView Parent = getParent();
        if (Parent instanceof OwDialogManager)
        {
            // === view is a dialog, so remove it from the manager to close it
            ((OwDialogManager) Parent).closeDialog(this);

            // notify caller
            if (m_Listener != null)
            {
                m_Listener.onDialogClose(this);
            }
        }
        else
        {
            throw new Exception("Dialog parent must be a OwDialogManager");
        }
    }

    /** set the view that opens the dialog
     *
      * @param listener_p Listener that wants to be notified upon dialog events.
     *
     */
    public void setListener(OwDialogListener listener_p)
    {
        m_Listener = listener_p;
    }

    // ==== DEBUG Properties only used for debugging
    /** count that keeps track of the target count deltas before/after opening a dialog.
     *  The target counts must return to their previous values on close dialog. 
     *  Used for debug reasons only to throw an exception
     *  and to prevent the target map from infinite increases.
     */
    public int m_iDEBUG_TargetCount = 0;

    /** init document must not change in dialog
     * if it changes, attached views may not be detached and cause memory overflow
     */
    public OwDocument m_iDEBUG_InitDocument = null;
    /** count of the attached views to the dialog manager document
     * must not change after close
     */
    public int m_iDEBUG_DocumentViewSize = 0;
}