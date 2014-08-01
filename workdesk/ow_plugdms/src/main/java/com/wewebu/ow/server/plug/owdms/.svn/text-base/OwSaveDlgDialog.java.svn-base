package com.wewebu.ow.server.plug.owdms;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwEventTarget;

/**
 *<p>
 * Implementation of the Add Document Dialog.
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
public abstract class OwSaveDlgDialog extends OwStandardDialog implements OwSaveDlgObjectPropertyView.OwSaveDlgObjectPropertyViewListner, OwSaveDlgDocumentImportView.OwSaveDlgDocumentImportViewListner, OwObjectClassView.OwObjectClassViewListner,
        OwDialog.OwDialogListener
{

    /** mask value to enable paste metadata in property view */
    public static final int VIEW_PROPERTY_ENABLE_PASTE_METADATA = 0x00000001;

    /** mask value to enable multiple file upload */
    public static final int VIEW_PROPERTY_ENABLE_MULTIPLE_FILE_UPLOAD = 0x00000002;

    /** view flag */
    public static final int VIEW_PROPERTY_CLASS_VIEW = 0x00000020;

    /** layout to be used for the dialog */
    protected OwSubLayout m_Layout;

    /** navigation view to navigate through the subviews */
    protected OwSubNavigationView m_SubNavigation;

    /** the document import view */
    protected OwSaveDlgDocumentImportView m_docImportView;

    /** the select class view */
    protected OwObjectClassView m_classView;

    /** the property view */
    protected OwSaveDlgObjectPropertyView m_PropertyView;

    protected int m_indexClassView;

    protected int m_indexPropertyView;

    /** the document to hold the data like the imported document */
    protected OwSaveDlgDocument m_document;

    /** List of OwDocumentImporter objects for the source view */
    private List m_documentImporters;

    /** true = displays just the source view to select a save target, false = allows edit of class, properties and access rights */
    protected boolean m_fSaveOnly;

    /** refresh context for callback */
    protected OwClientRefreshContext m_RefreshCtx;

    /** name of class to start from or null to start browsing from root */
    protected String m_strParentObjectClass;

    /** a set of properties that should be set as default for the new object */
    protected Map m_ValuesMap;

    /** filters the views and behaviors to be displayed */
    protected int m_iViewMask = 0;

    /**flag notifying if post process dialog is/was opened
     * @since 2.5.2.0
     */
    private boolean isPostProcessDlgOpen = false;

    /**
     * This method is invoked after the dialog sequence has been finished (i.e. in
     * case of a save-only dialog directly after document import and after properties
     * review otherwise).<br>
     * This method has to save, checkin or whatever it wants to do with the data stored
     * in the <code>OwSaveDlgDocument</code>, cleanup() and close the dialog.
     * 
     * @throws Exception
     */
    protected abstract void performSave() throws Exception;

    /**
     * Create new <code>OwSaveDlgDialog</code> with the given parameters
     *
     * @param resource_p the resource used to search for document classes
     * @param fSaveOnly_p true = displays just the source view to select a save target, false = allows edit of class, properties and access rights
     * @param documentImporters_p list of document importers to use
     */
    public OwSaveDlgDialog(OwResource resource_p, boolean fSaveOnly_p, List documentImporters_p)
    {
        // set member fields
        m_documentImporters = documentImporters_p;
        m_fSaveOnly = fSaveOnly_p;
        m_document = new OwSaveDlgDocument(resource_p);
        m_Layout = new OwSubLayout();
        m_SubNavigation = new OwSubNavigationView();

        // add view to the document
        setDocument(m_document);

        // set default 
        m_iViewMask = VIEW_PROPERTY_CLASS_VIEW | VIEW_PROPERTY_ENABLE_PASTE_METADATA;
    }

    /**
     * Check if view should be displayed or is masked out
     * 
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /**
     * Determine the views to be displayed by masking them with their flag
     *
     * @param iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /**
     * Set a plugin refresh callback interface
     *
     * @param pluginRefreshCtx_p OwClientRefreshContext
     */
    public void setRefreshContext(OwClientRefreshContext pluginRefreshCtx_p)
    {
        m_RefreshCtx = pluginRefreshCtx_p;
    }

    /**
     * Submit a set of values that should be set as default for the new object
     *
     * @param properties_p Map of values keyed by parameter names to be set initially
     */
    public void setValues(Map properties_p)
    {
        m_ValuesMap = properties_p;
    }

    /**
     * Init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === set document

        // register as event target
        m_document.attach(getContext(), null);
        //set the index of class view to not exist
        m_indexClassView = -1;
        m_indexPropertyView = -1;

        // === attached layout
        addView(m_Layout, MAIN_REGION, null);

        // === navigation 
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // enable validation of panels
        m_SubNavigation.setValidatePanels(true);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

        // === add document import view
        m_docImportView = createDocumentImportView(getDocumentImporters(), getDocumentImporterContext());
        m_docImportView.setEventListener(this);
        m_SubNavigation.addView(m_docImportView, getContext().localize("owsavedialog.impl.OwSaveDlgDialog.file_title", "Select file"), null, getContext().getDesignURL() + "/images/plug/owdocprops/docview.png", null, null);

        if (!m_fSaveOnly)
        {
            // === add document class view
            if (hasViewMask(VIEW_PROPERTY_CLASS_VIEW))
            {
                m_classView = new OwObjectClassView(m_document.getResource(), OwObjectReference.OBJECT_TYPE_DOCUMENT, m_strParentObjectClass);
                // attach view to navigation
                m_indexClassView = m_SubNavigation.addView(m_classView, getContext().localize("owsavedialog.impl.OwSaveDlgDialog.class_title", "Select class"), null, getContext().getDesignURL() + "/images/plug/owdocprops/class.png", null, null);

                m_classView.setSelectedItemStyle("OwSaveDlgObjectClassTextSelected");
                m_classView.setItemStyle("OwSaveDlgObjectClassText");
                m_classView.setEventListner(this);
                m_docImportView.setNextActivateView(m_classView);
            }

            //  === add properties view
            m_PropertyView = new OwSaveDlgObjectPropertyView();
            if (hasViewMask(VIEW_PROPERTY_ENABLE_PASTE_METADATA))
            {
                m_PropertyView.setViewMask(OwObjectPropertyView.VIEW_MASK_ENABLE_PASTE_METADATA);
            }

            // disable internal modes of property view, we display a own mode box here
            m_PropertyView.setModeType(OwObjectClass.OPERATION_TYPE_UNDEF);

            // add this dialog as event listener
            m_PropertyView.setEventListener(this);

            // attach view to layout
            m_indexPropertyView = m_SubNavigation.addView(m_PropertyView, getContext().localize("owsavedialog.impl.OwSaveDlgDialog.properties_title", "Properties"), null, getContext().getDesignURL() + "/images/plug/owdocprops/properties.png", null,
                    null);
            // NOTE: Initially no object is set, will set it upon class selection

            if (hasViewMask(VIEW_PROPERTY_CLASS_VIEW))
            {
                // set as next view of previous one
                m_classView.setNextActivateView(m_PropertyView);
            }
            else
            {
                m_docImportView.setNextActivateView(m_PropertyView);
            }
        }

        // === activate the first view
        m_SubNavigation.navigate(0);

    }

    //-------------------------------------------------------------------------
    //  OwSaveDlgObjectPropertyViewListner
    //-------------------------------------------------------------------------

    public void onSaveDocument() throws Exception
    {
        // this event has been fired after the user entered / changed properties.
        // This is always the last step. So perform the save
        performSave();
    }

    //-------------------------------------------------------------------------
    //  OwSaveDlgDocumentImportViewListner
    //-------------------------------------------------------------------------

    public void onDocumentImported() throws Exception
    {

        // this event is fired after the user has imported a document. If this is
        // a save-only dialog, this is the last step.
        if (m_fSaveOnly)
        {
            performSave();
        }
    }

    /**
     * Remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach document
        getDocument().detach();
    }

    /**
     * Render the views of the region
     * 
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case TITLE_REGION:
                w_p.write(getTitle());
                break;

            default:
                // render registered views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /**
     * Determine if region contains a view
     * 
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case TITLE_REGION:
                return true;

            default:
                return super.isRegion(iRegion_p);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, java.lang.Object)
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        if (iCode_p == OwUpdateCodes.SET_NEW_OBJECT)
        {
            OwObject obj = ((OwSaveDlgDocument) getDocument()).getSkeletonObject();

            // set filename property
            OwDocumentImportItem importedDocument = m_document.getImportedDocument();
            if (importedDocument != null)
            {
                /*
                String proposedDocumentName = importedDocument.getProposedDocumentName();
                if(proposedDocumentName != null)
                {
                    obj.getProperty(obj.getObjectClass().getNamePropertyName()).setValue(proposedDocumentName);
                }
                */

                // merge with property values from importer
                Map documentImportProperties = importedDocument.getPropertyValueMap();
                if (documentImportProperties != null)
                {
                    Iterator itDocumentImportProperties = documentImportProperties.entrySet().iterator();
                    while (itDocumentImportProperties.hasNext())
                    {
                        Entry entry = (Entry) itDocumentImportProperties.next();
                        try
                        {
                            OwProperty skeletonProp = obj.getProperty(entry.getKey().toString());
                            skeletonProp.setValue(entry.getValue());
                        }
                        catch (OwObjectNotFoundException onfe)
                        {
                            // ignore
                        }
                    }
                }
            }

            // Merge with values if given
            if (null != m_ValuesMap)
            {
                Iterator it = m_ValuesMap.keySet().iterator();
                while (it.hasNext())
                {
                    String strPropName = (String) it.next();

                    try
                    {
                        OwProperty skeletonProp = obj.getProperty(strPropName);

                        skeletonProp.setValue(m_ValuesMap.get(strPropName));
                    }
                    catch (OwObjectNotFoundException e)
                    {
                        // Ignore
                    }
                }
            }

            // bug 1726
            if (m_PropertyView != null)
            {
                // new set the reference
                m_PropertyView.setObjectRef(obj, false);
            }
        }
    }

    protected void cleanup() throws Exception
    {

        // clear the import item. this will release it
        m_document.setImportedDocument(null);

        // release the resources acquired by the importers. We will not need them anymore
        if (m_documentImporters != null)
        {
            Iterator itDocumentImporters = m_documentImporters.iterator();
            while (itDocumentImporters.hasNext())
            {
                OwDocumentImporter importer = (OwDocumentImporter) itDocumentImporters.next();
                importer.releaseAll();
            }
        }
    }

    /**
     * clear resources when dialog has been canceled
     */
    public void onClose(HttpServletRequest request_p) throws Exception
    {
        // cleanup
        cleanup();
        // and close
        super.onClose(request_p);
    }

    /**
     * event called when user selects a class
     * 
     * @param classDescription_p
     *            OwObjectClass
     * @param strPath_p
     *            String path to selected tree item
     */
    public void onObjectClassViewSelectClass(OwObjectClass classDescription_p, String strPath_p) throws Exception
    {
        ((OwSaveDlgDocument) getDocument()).setObjectClass(classDescription_p);
    }

    /**
     * (overridable)
     * Factory method to create own ImportViews
     * @param documentImporters_p List of OwDocumentImporter 
     * @param documentImporterContext_p int representing the 
     * @return OwSaveDlgDocumentImportView to use init document Importer view, should be one of OwDocumentImporter.IMPORT_CONTEXT_...
     * @see #getDocumentImporters()
     * @see #getDocumentImporterContext()
     * @since 2.5.2.0
     */
    protected OwSaveDlgDocumentImportView createDocumentImportView(List documentImporters_p, int documentImporterContext_p)
    {
        return new OwSaveDlgDocumentImportView(documentImporters_p, documentImporterContext_p);
    }

    /**
     * (overridable)
     * Context for the DocumentImporter which are initialized by the
     * OwSaveDlgDocumentImportView 
     * @return int OwDocumentImporter.IMPORT_CONTEXT_...
     * @since 2.5.2.0
     */
    public int getDocumentImporterContext()
    {
        return OwDocumentImporter.IMPORT_CONTEXT_NEW;
    }

    /**
     * Returns the list of document importers to use. 
     * @return List of OwDocumentImporter's
     * @since 2.5.2.0
     */
    protected List getDocumentImporters()
    {
        return this.m_documentImporters;
    }

    /**
     * (overridable)
     * Get a dialog to be used for post processing
     * of the current document item. Should return
     * a dialog where to add the post processing view. 
     * @return OwStandardDialog to be used, (non-null)
     * @since 2.5.2.0
     */
    protected OwStandardDialog getPostProcessingDialog()
    {
        return new OwStandardDialog();
    }

    /**
     * Open an post processing dialog if available for current {@link #getDocumentImporterContext()}.<br />
     * Will add the post process view to the dialog into the {@link OwStandardDialog#MAIN_REGION}
     * of given {@link #getPostProcessingDialog()} and register itself as dialog close listener.
     * @param importer_p OwDocumentImporter which should be requested for post processing view
     * @param processedObject_p OwObject which was recently processed
     * @throws Exception if problem exist with opening post processing dialog
     * @since 2.5.2.0
     */
    protected void openPostProcessingView(OwDocumentImporter importer_p, OwObject processedObject_p) throws Exception
    {
        //check if postprocessing should be executed 
        if (importer_p != null && importer_p.hasPostProcessView(getDocumentImporterContext()))
        {
            OwStandardDialog dlg = getPostProcessingDialog();
            dlg.attach(getContext(), null);
            dlg.addView(importer_p.getPostProcessView(getDocumentImporterContext(), processedObject_p), OwStandardDialog.MAIN_REGION, null);

            getContext().openDialog(dlg, this);
            setIsPostProcessDialogOpen(true);
        }
    }

    /**
     * This is the listener Implementation of
     * for the post process dialog, and cannot be overwritten
     * in sub classes.
     * <p>For processing the close event of post processing
     * dialog subclasses must implement {@link #onPostProcessDialogClose(OwDialog)}
     * </p> 
     * @param dialog_p OwDialog the post process dialog
     * @see #onPostProcessDialogClose(OwDialog)
     * @since 2.5.2.0
     */
    public final void onDialogClose(OwDialog dialog_p) throws Exception
    {
        this.isPostProcessDlgOpen = false;
        onPostProcessDialogClose(dialog_p);
    }

    /**
     * Will be called if a postprocess dialog was opened, after 
     * closing it again.
     * @param dialog_p OwDialog which was used for post processing
     * @throws Exception
     * @since 2.5.2.0
     */
    protected abstract void onPostProcessDialogClose(OwDialog dialog_p) throws Exception;

    /**
     * Flag which notifies if the post process dialog
     * was opened.
     * @return boolean
     */
    public boolean isPostProcessDialogOpen()
    {
        return this.isPostProcessDlgOpen;
    }

    /**
     * Set the flag for post process dialog, if
     * it is is open or was closed.
     * @param isOpen_p
     */
    protected void setIsPostProcessDialogOpen(boolean isOpen_p)
    {
        this.isPostProcessDlgOpen = isOpen_p;
    }

    /**
     * Helper returning the current used OwSaveDlgDocument.
     * @return {@link OwSaveDlgDocument}
     * @since 2.5.2.0
     */
    protected OwSaveDlgDocument getCurrentDocument()
    {
        return m_document;
    }
}