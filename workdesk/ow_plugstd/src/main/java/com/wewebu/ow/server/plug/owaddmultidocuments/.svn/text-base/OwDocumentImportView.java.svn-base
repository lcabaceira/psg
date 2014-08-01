package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwSmallSubNavigationView;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * OwDocumentImportView.
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
public class OwDocumentImportView extends OwLayout implements OwMultipanel, OwDocumentImporterCallback
{

    /** region for the navigation part */
    public static final int NAVIGATION_REGION = 0;

    /** region for the optional menu */
    public static final int MENU_REGION = 1;

    /** main region */
    public static final int MAIN_REGION = 2;

    /** navigation view to navigate through the document importers */
    protected OwSubNavigationView m_subNavigationView;

    /** Menu for buttons like "Next" */
    protected OwSubMenuView m_MenuView;

    /** the next OwView in the OwMultipanel sequence */
    protected OwView m_nextView;

    /** the previous OwView in the OwMultipanel sequence */
    protected OwView m_prevView;

    /** List of OwDocumentImporter objects for the source view */
    private List m_documentImporters;

    /** the button index of the "Next" button in the menu */
    protected int m_iNextButtonIndex;

    /** event listener */
    private OwDocumentImportViewListener m_eventListner = null;

    private LinkedList importerCallbackListener;

    public OwDocumentImportView(List documentImporters_p)
    {
        if (documentImporters_p == null)
        {
            throw new IllegalArgumentException("OwDocumentImportView must not be instantiated without List of document importers.");
        }

        m_documentImporters = documentImporters_p;
    }

    /**
     * Init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();
        this.importerCallbackListener = new LinkedList();
        // create the navigation view  and set all references
        m_subNavigationView = createNavigationView();
        addView(m_subNavigationView, NAVIGATION_REGION, null);
        m_subNavigationView.setValidatePanels(true);
        addViewReference(m_subNavigationView.getViewReference(), MAIN_REGION);

        // create and add the menu view
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, MENU_REGION, null);

        // create and add the menu button "Next"
        m_iNextButtonIndex = m_MenuView.addMenuItem(this, getContext().localize("plug.owaddmultidocuments.OwDocumentImportView.Next", "Next"), null, "Next", null, null);
        m_MenuView.enable(m_iNextButtonIndex, false);

        // add all configured document importer to the navigation
        Iterator itDocumentImporters = m_documentImporters.iterator();
        while (itDocumentImporters.hasNext())
        {
            OwDocumentImporter docImporter = (OwDocumentImporter) itDocumentImporters.next();
            OwView docImporterView = docImporter.getView(OwDocumentImporter.IMPORT_CONTEXT_NEW, this);

            String docImporterTitle = docImporter.getDisplayName();
            String docImporterIcon = docImporter.getIconURL();
            m_subNavigationView.addView(docImporterView, docImporterTitle, null, docImporterIcon, null, null);
        }

        // activate the first view
        m_subNavigationView.navigate(0);
    }

    /**
     * Sets the current event listener 
     * @param eventListner_p
     */
    public void setEventListener(OwDocumentImportViewListener eventListner_p)
    {
        this.m_eventListner = eventListner_p;
    }

    protected OwSubNavigationView createNavigationView()
    {
        return new OwSmallSubNavigationView();
    }

    /** event called when user clicked Lock button in menu 
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   @param request_p a {@link HttpServletRequest}
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        if (m_nextView != null)
        {
            m_nextView.activate();
        }
    }

    /**
     * Render this OwDocumentImportView by including a JSP page
     * 
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("owaddmulti/OwDocumentImportView.jsp", w_p);
    }

    //-------------------------------------------------------------------------
    // INTERFACE OwDocumentImporterCallback
    //-------------------------------------------------------------------------

    public void onDocumentImported(OwDocumentImporter importer_p, OwDocumentImportItem item_p)
    {
        notifyCallbackListener(importer_p, item_p);
        // enable the "Next" button
        m_MenuView.enable(m_iNextButtonIndex, true);
        if (m_eventListner != null)
        {
            m_eventListner.onDocumentImported();
        }
    }

    //-------------------------------------------------------------------------
    // INTERFACE OwMultipanel
    //-------------------------------------------------------------------------

    /**
     * Set the view that is next to this view, displays a next button to activate
     *
     * @param nextView_p OwView
     */
    public void setNextActivateView(OwView nextView_p)
    {
        m_nextView = nextView_p;
    }

    /**
     * Set the view that is prev to this view, displays a prev button to activate
     *
     * @param prevView_p OwView
     */
    public void setPrevActivateView(OwView prevView_p)
    {
        m_prevView = prevView_p;
    }

    /**
     * Check if view has validated its data and the next view can be enabled
     *
     * @return boolean true = can forward to next view, false = view has not yet validated
     */
    public boolean isValidated()
    {
        return (getCurrentDocument().getImportedDocumentsCount() > 0);
    }

    protected OwAddMultiDocumentsDocument getCurrentDocument()
    {
        return (OwAddMultiDocumentsDocument) getDocument();
    }

    public void addDocumentImporterCallBackListener(OwDocumentImporterCallback listener_p)
    {
        this.importerCallbackListener.add(listener_p);
    }

    public void removeDocumentImporterCallBackListener(OwDocumentImporterCallback listener_p)
    {
        this.importerCallbackListener.remove(listener_p);
    }

    protected void notifyCallbackListener(OwDocumentImporter importer_p, OwDocumentImportItem item_p)
    {
        Iterator it = this.importerCallbackListener.iterator();
        while (it.hasNext())
        {
            try
            {
                ((OwDocumentImporterCallback) it.next()).onDocumentImported(importer_p, item_p);
            }
            catch (Exception e)
            {

            }
        }
    }

    //-------------------------------------------------------------------------
    // methods for the GUI
    //-------------------------------------------------------------------------

    public OwDocumentImportItem getImportedDocument(int i_p)
    {
        return ((OwAddMultiDocumentsDocument) getDocument()).getImportedDocument(i_p);
    }

    public int getImportedDocumentsCount()
    {
        return ((OwAddMultiDocumentsDocument) getDocument()).getImportedDocumentsCount();
    }

}