package com.wewebu.ow.server.plug.owrecord;

import java.io.Writer;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectTreeView;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwTreeView;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Record TreeView Module. Displays a treeview of a OwObject with children.
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
public class OwRecordTreeView extends OwView
{

    /** region of the tree view */
    public static final int TREE_REGION = 0;

    /** tree view module to display the OwObject structure */
    protected OwTreeView m_TreeView;

    /***/
    protected int m_iDisplayMode = 0;

    /** create navigation view wrapper
     * @param objectTreeView_p an {@link OwObjectTreeView}
     * @param iDisplayMode_p int display mode to render the given tree view as defined in OwTreeView
     * */
    public OwRecordTreeView(OwTreeView objectTreeView_p, int iDisplayMode_p)
    {
        m_TreeView = objectTreeView_p;
        m_iDisplayMode = iDisplayMode_p;
    }

    public void setExternalFormTarget(OwEventTarget eventtarget_p) throws Exception
    {
        m_TreeView.setExternalFormTarget(eventtarget_p);
        super.setExternalFormTarget(eventtarget_p);
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (m_TreeView.isEmpty())
        {
            // empty tree message
            w_p.write("<span class='OwRecordTreeView_EmptyTextMessage'>" + getContext().localize("owrecord.OwRecordTreeView.emptytree", "No files opened.") + "</span>");
        }
        else
        {
            m_TreeView.setDisplayMode(m_iDisplayMode);
            m_TreeView.render(w_p);
        }
    }
}