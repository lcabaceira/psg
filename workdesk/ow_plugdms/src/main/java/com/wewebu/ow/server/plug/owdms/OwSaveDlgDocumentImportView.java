package com.wewebu.ow.server.plug.owdms;

import java.io.Writer;
import java.util.EventListener;
import java.util.Iterator;
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
 * Record View Module.
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
public class OwSaveDlgDocumentImportView extends OwLayout implements OwMultipanel, OwDocumentImporterCallback
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

    protected int m_documentImporterContext;

    /**
     * Create a new <code>OwSaveDlgDocumentImportView</code> with the given parameters
     * 
     * @param documentImporters_p the List of document importer
     */
    public OwSaveDlgDocumentImportView(List documentImporters_p)
    {
        this(documentImporters_p, OwDocumentImporter.IMPORT_CONTEXT_NEW);
    }

    /**
     * Create a new <code>OwSaveDlgDocumentImportView</code> with the given parameters
     * 
     * @param documentImporters_p the List of document importer
     * @param documentImporterContext_p context representing one of OwDocumentImporter.IMPORT_CONTEXT_...
     */
    public OwSaveDlgDocumentImportView(List documentImporters_p, int documentImporterContext_p)
    {
        if (documentImporters_p == null)
        {
            throw new IllegalArgumentException("OwDocumentImportView must not be instantiated without List of document importers.");
        }

        m_documentImporters = documentImporters_p;
        m_documentImporterContext = documentImporterContext_p;
    }

    /**
     *<p>
     * The EventListener interface for Events fired by this OwSaveDlgDocumentImportView.
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
    public interface OwSaveDlgDocumentImportViewListner extends EventListener
    {

        /**
         * Event fired when document has been imported
         */
        public abstract void onDocumentImported() throws Exception;

    }

    /** the event listener */
    protected OwSaveDlgDocumentImportViewListner m_eventListener;

    /**
     * Set the event listener
     * 
     * @param eventListener_p the new event listener
     */
    public void setEventListener(OwSaveDlgDocumentImportViewListner eventListener_p)
    {
        m_eventListener = eventListener_p;
    }

    /**
     * Returns the current event listener
     * 
     * @return the current event listener
     */
    public OwSaveDlgDocumentImportViewListner getEventListener()
    {
        return m_eventListener;
    }

    /**
     * Fire the onDocumentImported Event if the event listener is set
     * @throws Exception
     */
    protected void fireDocumentImportedEvent() throws Exception
    {
        if (m_eventListener != null)
        {
            m_eventListener.onDocumentImported();
        }
    }

    /**
     * Init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

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
            OwView docImporterView = docImporter.getView(m_documentImporterContext, this);
            String docImporterTitle = docImporter.getDisplayName();
            String docImporterIcon = docImporter.getIconURL();
            m_subNavigationView.addView(docImporterView, docImporterTitle, null, docImporterIcon, null, null);
        }

        // activate the first view
        m_subNavigationView.navigate(0);
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
        serverSideDesignInclude("owdms/OwSaveDlgDocumentImportView.jsp", w_p);
    }

    //-------------------------------------------------------------------------
    // INTERFACE OwDocumentImporterCallback
    //-------------------------------------------------------------------------

    public void onDocumentImported(OwDocumentImporter importer_p, OwDocumentImportItem item_p) throws Exception
    {
        // add imported item to document input stack
        ((OwSaveDlgDocument) getDocument()).setImportedDocument(item_p);
        ((OwSaveDlgDocument) getDocument()).setDocumentImporter(importer_p);
        // enable the "Next" button
        m_MenuView.enable(m_iNextButtonIndex, true);
        // automatically jump to the next page
        if (m_nextView != null)
        {
            m_nextView.activate();
        }
        // fire the onDocumentImported event
        fireDocumentImportedEvent();
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
        return (((OwSaveDlgDocument) getDocument()).getImportedDocument() != null);
    }

    //-------------------------------------------------------------------------
    // methods for the GUI
    //-------------------------------------------------------------------------

    public OwDocumentImportItem getImportedDocument()
    {
        return ((OwSaveDlgDocument) getDocument()).getImportedDocument();
    }

}