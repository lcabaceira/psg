package com.wewebu.ow.server.docimport;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwAbstractDocumentImporterView;

/**
 *<p>
 * View for no content document importer.
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
public class OwNoContentDocumentImporterView extends OwAbstractDocumentImporterView
{

    /** region for the optional menu */
    public static final int MENU_REGION = 1;

    /** Menu for buttons like "Add" */
    protected OwSubMenuView m_MenuView;

    /**
     * Create a new OwNoContentDocumentImporterView for a given callback.
     * 
     * @param callback_p the callback to invoke after a document has been imported
     */
    public OwNoContentDocumentImporterView(OwNoContentDocumentImporter importer_p, OwDocumentImporterCallback callback_p)
    {
        super(importer_p, callback_p);
    }

    /**
     * Init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // create and add the menu view
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, MENU_REGION, null);

        // add menu button
        int addButtonIndex = m_MenuView.addMenuItem(this, getContext().localize("plug.docimport.OwNoContentDocumentImporterView.Add", "Add"), "Add", null);
        m_MenuView.setDefaultMenuItem(addButtonIndex);
    }

    /**
     * Render this OwDocumentImportView by including a JSP page
     * 
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("docimport/OwNoContentDocumentImporterView.jsp", w_p);
    }

    public boolean onAdd(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        fireOnDocumentImportEvent(new OwNoContentDocumentImportItem(getContext().localize("plug.docimport.OwNoContentDocumentImporterView.DocumentWithoutContent", "[Document without content]")));
        return true;
    }
}