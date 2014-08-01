package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImportItemContentCollection;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.OwFormPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.OwPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.OwStandardPropertyViewBridge;
import com.wewebu.ow.server.dmsdialogs.views.OwCheckInHandler;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDelegateView;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwJspConfigurable;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo;
import com.wewebu.ow.server.util.OwHTMLHelper;

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
public class OwCreateMultipleDocumentsDialog extends OwStandardDialog implements OwMultiDocumentObjectPropertyViewListner, OwDocumentImportViewListener, OwDialog.OwDialogListener, OwJspConfigurable
{
    /** package logger or class logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCreateMultipleDocumentsDialog.class);

    /** mask value to enable paste metadata in property view */
    public static final int VIEW_MASK_ENABLE_PASTE_METADATA = 0x00000001;

    /** mask value for the access rights view */
    public static final int VIEW_MASK_ACCESS_RIGHTS = 0x00000002;

    /** mask value for auto open */
    public static final int VIEW_MASK_AUTOOPEN = 0x00000004;

    /** view flag */
    public static final int VIEW_MASK_MULTIPLEFILE_UPLOAD = 0x00000008;

    /** view flag */
    public static final int VIEW_MASK_CHECKIN_MODE_OPTION = 0x00000080;

    /** view flag */
    public static final int VIEW_MASK_RELEASE_VERSION_OPTION = 0x00000100;

    /** view flag */
    public static final int VIEW_MASK_RELEASE_VERSION_DEFAULT = 0x00000200;

    /** view flag to enable save all button option when more than one documents are selected*/
    public static final int VIEW_MASK_ENABLE_SAVE_ALL = 0x00000400;

    /** status of this dialog: nothing done yet */
    protected static final int DIALOG_STATUS_NONE = 0;

    /** status of this dialog: checkin successful*/
    protected static final int DIALOG_STATUS_OK = 1;

    /** status of this dialog: checkin failed */
    protected static final int DIALOG_STATUS_FAILED = 2;

    /** status of this dialog needed for historization */
    protected int m_dialogStatus = DIALOG_STATUS_NONE;

    /** layout to be used for the dialog */
    protected OwSubLayout m_Layout;

    /** navigation view to navigate through the subviews */
    protected OwSubNavigationView m_SubNavigation;

    /** the document which is used by this dialog*/
    protected OwAddMultiDocumentsDocument m_document;

    /**reference to the document importer view*/
    protected OwDocumentImportView m_documentImportView;

    /** reference to the used class view*/
    protected OwObjectClassView m_classView;

    /** the access rights view */
    protected OwMultiDocumentObjectAccessRightsView m_AccessRightsView;

    /**
     * the property view bridge
     * @since 3.1.0.0
     */
    protected OwPropertyViewBridge m_propertyBridge;

    /**index of tab ClassView in m_SubNavigation
     * @since 2.5.2.0 */
    protected int m_idxClassView;

    /**index of tab AccessRightsView in m_SubNavigation
     * @since 2.5.2.0 */
    protected int m_idxAccesRightsView;

    /**index of tab PropertyView in m_SubNavigation
     * @since 2.5.2.0 */
    protected int m_idxPropertyView;

    /** list of OwPropertyInfo objects which defines properties that should be rendered like in OwEditDocumentPropertiesSimple */
    private List<OwPropertyInfo> m_propertyInfos;

    /** List of OwDocumentImporter objects for the source view */
    private List m_documentImporters;

    protected OwObject m_parentFolder;

    /** a Map of object class - subfolder mappings */
    protected Map m_objectClassMap;

    protected boolean m_PerformDragDrop;

    protected int m_MultipleDocumentsCurrentPos;

    /** should upload dir itself be deleted? just if it is the deploy dir of the app-server */
    protected boolean m_deleteUploadDir;

    /** refresh context for callback */
    protected OwClientRefreshContext m_RefreshCtx;

    /** 
     * name of class to start from or null to start browsing from root
     * @deprecated will be replaced with {@link #classSelectionCfg} 
     */
    protected String m_strParentObjectClass;

    protected OwObjectClassSelectionCfg classSelectionCfg;

    /** a set of properties that should be set as default for the new object */
    protected Map m_ValuesMap;

    /** filters the views and behaviors to be displayed */
    protected int m_iViewMask;

    protected boolean m_fClassView;

    private Collection m_batchIndexProperties;

    /** check if preview window was opened */
    private boolean m_previewOpened;

    /**
     * flag notifying if a post process dialog is currently open
     * @since 2.5.2.0
     */
    private boolean m_isPostProcessDlgOpen;

    /**
     * Configuration handler for multiple JspForms
     * @since 3.1.0.0
     */
    private OwJspFormConfigurator m_jspConfigurator;

    /**Delegation handler for multiple JspForms configuration
     * @since 3.1.0.0 */
    private OwDelegateView delegateView = new OwDelegateView();

    /** class name */
    private String m_objectClassName = null;

    /**Flag to define properties propagation order
     * @since 3.1.0.0*/
    private boolean importerBeforeBatch;

    /**Flag to define proposed importer name or native
     * @since 3.1.0.0*/
    private boolean useImporterProposedName;

    /** create new OwCreateMultipleDocumentsDialog
     * @param folderObject_p OwObject parent to create the new object in or null to use a default location.
     * @param batchIndexProperties_p 
     * @param documentImporters_p List of OwDocumentImporter objects
     * @deprecated will be replaced with {@link #OwCreateMultipleDocumentsDialog(OwObject, OwResource, OwObjectClassSelectionCfg, Collection, List)}
     */
    public OwCreateMultipleDocumentsDialog(OwObject folderObject_p, OwResource resource_p, String strClassName_p, String strParentObjectClass_p, Collection batchIndexProperties_p, List documentImporters_p) throws Exception
    {
        m_MultipleDocumentsCurrentPos = -1;
        m_iViewMask = 0;
        m_previewOpened = false;
        m_document = createDocument(resource_p, folderObject_p);
        setDocument(m_document);

        m_fClassView = (strClassName_p == null);
        m_document.setClassName(strClassName_p);
        m_iViewMask = VIEW_MASK_ENABLE_PASTE_METADATA;
        m_parentFolder = folderObject_p;
        m_strParentObjectClass = strParentObjectClass_p;
        m_batchIndexProperties = batchIndexProperties_p;
        m_documentImporters = documentImporters_p;
        m_isPostProcessDlgOpen = false;
        setImporterBeforeBatchHandling(false);
    }

    /** 
     * create new OwCreateMultipleDocumentsDialog
     * @param folderObject_p OwObject parent to create the new object in or null to use a default location.
     * @param batchIndexProperties_p 
     * @param documentImporters_p List of OwDocumentImporter objects
     * @since 4.1.0.0
     */
    public OwCreateMultipleDocumentsDialog(OwObject folderObject_p, OwResource resource_p, OwObjectClassSelectionCfg classSelectionCfg, Collection batchIndexProperties_p, List documentImporters_p) throws Exception
    {
        m_MultipleDocumentsCurrentPos = -1;
        m_iViewMask = 0;
        m_previewOpened = false;
        m_document = createDocument(resource_p, folderObject_p);
        setDocument(m_document);

        String defaultClassName = null;
        if (classSelectionCfg.hasDefaultClass())
        {
            defaultClassName = classSelectionCfg.getDefaultClass().getName();
        }
        m_fClassView = (defaultClassName == null);
        m_document.setClassName(defaultClassName);

        m_iViewMask = VIEW_MASK_ENABLE_PASTE_METADATA;
        m_parentFolder = folderObject_p;
        this.classSelectionCfg = classSelectionCfg;
        m_batchIndexProperties = batchIndexProperties_p;
        m_documentImporters = documentImporters_p;
        m_isPostProcessDlgOpen = false;
        setImporterBeforeBatchHandling(false);
    }

    /**(overridable)
     * Create the Document to be used for current Dialog
     * @param resource_p OwResource resource to use if parent is null
     * @param folderObj_p OwObject parent
     * @return OwAddMultiDocumentsDocument
     * @since 3.1.0.0
     */
    protected OwAddMultiDocumentsDocument createDocument(OwResource resource_p, OwObject folderObj_p)
    {
        return new OwAddMultiDocumentsDocument(resource_p, folderObj_p);
    }

    /** check if preview window was opened */
    protected boolean getPreviewOpenend()
    {
        return m_previewOpened;
    }

    /** get a collection of property names that act in batch processing
     * 
     * @return Collection of String
     */
    protected Collection getBatchIndexProperties()
    {
        return m_batchIndexProperties;
    }

    /**
     * get the current status of this dialog
     * @return the status
     */
    public int getStatus()
    {
        return (m_dialogStatus);
    }

    /** check if view should be displayed or is masked out
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /** determine the views to be displayed by masking them with their flag
     *
     * @param iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** set a plugin refresh callback interface
     *
     * @param pluginRefreshCtx_p OwClientRefreshContext
     */
    public void setRefreshContext(OwClientRefreshContext pluginRefreshCtx_p)
    {
        m_RefreshCtx = pluginRefreshCtx_p;
    }

    /** submit a set of values that should be set as default for the new object
     *
     * @param properties_p Map of values keyed by parameter names to be set initially
     */
    public void setValues(Map properties_p)
    {
        m_ValuesMap = properties_p;
    }

    /** set a property info list defining the visibility and modifiability of property values
     * that is passed on to the PropertyView.
     * 
     * @param propertyInfos_p list of OwPropertyInfo objects that are passed to the PropertyView
     * @deprecated since 4.2.0.0 use {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    @Deprecated
    public void setPropertyInfos(List<OwPropertyInfo> propertyInfos_p)
    {
        m_propertyInfos = propertyInfos_p;
    }

    /**
     * Setter for PropertyList configuration, which will be referenced by used document.
     * @param propListConfiguration
     * @since 4.2.0.0
     */
    public void setPropertyListConfiguration(OwPropertyListConfiguration propListConfiguration)
    {
        this.m_document.setPropertyListConfiguration(propListConfiguration);
    }

    /** overridable factory method
     * 
     * @return OwMultiDocumentObjectPropertyView
     */
    protected OwMultiDocumentObjectPropertyView createMultiDocumentObjectPropertyView() throws Exception
    {
        return new OwMultiDocumentObjectPropertyView(hasViewMask(VIEW_MASK_ENABLE_SAVE_ALL));
    }

    /** overridable factory method
     * 
     * @return OwMultiDocumentObjectAccessRightsView
     */
    protected OwMultiDocumentObjectAccessRightsView createMultiDocumentObjectAccessRightsView() throws Exception
    {
        return new OwMultiDocumentObjectAccessRightsView();
    }

    /** overridable factory method
     * 
     * @param resource_p the OwResource to get the classes from
     * @param iObjectType_p int object to browse for
     * @param strParentObjectClass_p String name of class to start from or null to start browsing from root
     * 
     * @return OwObjectClassView
     * @deprecated replaced by {@link #createObjectClassView(OwResource, int, OwObjectClassSelectionCfg)}
     */
    protected OwObjectClassView createObjectClassView(OwResource resource_p, int iObjectType_p, String strParentObjectClass_p) throws Exception
    {
        return new OwMultiDocumentObjectClassView(resource_p, iObjectType_p, strParentObjectClass_p);
    }

    /** 
     * overridable factory method
     * 
     * @param resource_p the OwResource to get the classes from
     * @param iObjectType_p int object to browse for
     * @param classSelectionCfg
     * 
     * @return OwObjectClassView
     * @since 4.1.0.0
     */
    protected OwObjectClassView createObjectClassView(OwResource resource_p, int iObjectType_p, OwObjectClassSelectionCfg classSelectionCfg) throws Exception
    {
        return new OwMultiDocumentObjectClassView(resource_p, iObjectType_p, classSelectionCfg);
    }

    /** overridable factory method
     * 
     * @return OwMultiDocumentsSaveDlgContentCollection
     * @throws Exception 
     */
    protected OwDocumentImportItemContentCollection createDocumentImportItemContentCollection(OwDocumentImportItem importedDocument_p) throws Exception
    {
        return new OwDocumentImportItemContentCollection(importedDocument_p);
    }

    /** overridable factory method
     * 
     * @return OwSubLayout
     */
    protected OwSubLayout createSubLayout() throws Exception
    {
        return new OwSubLayout();
    }

    /** overridable factory method
     * 
     * @return OwSubNavigationView
     */
    protected OwSubNavigationView createSubNavigationView() throws Exception
    {
        return new OwSubNavigationView();
    }

    /**
     * overridable factory method
     */
    protected OwDocumentImportView createDocumentImporterView() throws Exception
    {
        return new OwDocumentImportView(m_documentImporters);
    }

    /** Define a Map of object class - subfolder mappings
    *<p>
    * List of object class mappings to be used.<br />
    * Each object class is mapped to a subfolder path,
    * where the key is the objectclass and the value is the folder name to be created.
    *</p>
    */
    public void setFolderMapping(Map objectClassMap_p)
    {
        m_objectClassMap = objectClassMap_p;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_Layout = createSubLayout();
        m_SubNavigation = createSubNavigationView();
        //reset index to -1, means that not inserted to the m_SubNavigation
        m_idxAccesRightsView = m_idxClassView = m_idxPropertyView = -1;

        // === set document
        // register as event target
        m_document.attach(getContext(), null);
        OwMainAppContext context = (OwMainAppContext) getContext();
        // === detect DragDrop vs. FileSelection
        m_PerformDragDrop = context.getDNDImportedDocumentsCount() > 0;

        // === get DragAndDop file names
        if (m_PerformDragDrop)
        {
            // add all DND imported documents
            for (int i = 0; i < context.getDNDImportedDocumentsCount(); i++)
            {
                m_document.onDocumentImported(null, context.getDNDImportedDocument(i));
            }
            // start preview on first document
            if (hasViewMask(VIEW_MASK_AUTOOPEN))
            {
                previewDocument(0);
            }
        }

        // set currently handled document
        m_MultipleDocumentsCurrentPos = 0;

        // === attach layout
        addView(m_Layout, MAIN_REGION, null);

        // === navigation
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // enable validation of panels
        m_SubNavigation.setValidatePanels(true);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

        // === add multiple file selection view
        if (!m_PerformDragDrop)
        {
            m_documentImportView = createDocumentImporterView();
            m_documentImportView.setEventListener(this);
            m_SubNavigation.addView(m_documentImportView, context.localize("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.SelectFiles_Title", "Select Files"), null, getContext().getDesignURL() + "/images/plug/owdocprops/docview.png",
                    null, null);
            m_documentImportView.addDocumentImporterCallBackListener(m_document);
        }

        // === add document class view
        if (m_fClassView)
        {
            if (null != m_strParentObjectClass)
            {
                m_classView = createObjectClassView(m_document.getResource(), OwObjectReference.OBJECT_TYPE_DOCUMENT, m_strParentObjectClass);
            }
            else
            {
                m_classView = createObjectClassView(m_document.getResource(), OwObjectReference.OBJECT_TYPE_DOCUMENT, this.classSelectionCfg);
            }
            // attach view to navigation
            m_idxClassView = m_SubNavigation.addView(m_classView, context.localize("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.SelectClass_Title", "Select Class"), null, context.getDesignURL() + "/images/plug/owdocprops/class.png",
                    null, null);

            m_classView.setSelectedItemStyle("OwSaveDlgObjectClassTextSelected");
            m_classView.setItemStyle("OwSaveDlgObjectClassText");
            m_classView.setEventListner(m_document);
            m_classView.setObjectClassProcessor(m_document.getObjectClassProcessor());

            if (!m_PerformDragDrop)
            { // set as next view of previous one
                m_documentImportView.setNextActivateView(m_classView);
            }
        }

        //  === add access rights view
        if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS))
        {
            m_AccessRightsView = createMultiDocumentObjectAccessRightsView();
            // NOTE: Initially no object is set, will set it upon class selection
            m_AccessRightsView.setObjectRef(m_document.getSkeletonObject(false));
            m_idxAccesRightsView = m_SubNavigation.addView(m_AccessRightsView, context.localize("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.AccessRights_Title", "Access Rights"), null, context.getDesignURL()
                    + "/images/plug/owdocprops/accessrights.png", null, null);
            m_AccessRightsView.setReadOnly(false);
            // set the next active view
            if (m_fClassView)
            {
                // set as next view of previous one
                m_classView.setNextActivateView(m_AccessRightsView);
                // also set the previous active view for validation cascading
                m_AccessRightsView.setPrevActivateView(m_classView);
            }
            else if (!m_PerformDragDrop)
            {
                // set as next view of previous one
                m_documentImportView.setNextActivateView(m_AccessRightsView);
                // also set the previous active view for validation cascading
                m_AccessRightsView.setPrevActivateView(m_documentImportView);
            }
        }

        //  property bridge to prevent null pointer
        m_propertyBridge = createPropertyViewBridge();

        // attach view to layout
        m_idxPropertyView = m_SubNavigation.addView(this.delegateView, context.localize("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.Properties_Title", "Properties"), null, context.getDesignURL()
                + "/images/plug/owdocprops/properties.png", null, null);
        if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS))
        {
            m_AccessRightsView.setNextActivateView(this.delegateView);
        }
        else if (m_fClassView)
        {
            // set as next view of previous one
            m_classView.setNextActivateView(this.delegateView);
        }
        else if (!m_PerformDragDrop)
        {
            // set as next view of previous one
            m_documentImportView.setNextActivateView(this.delegateView);
        }

        // set the object class (if available) AFTER anything else has been initialized. This will post an update message.
        if (!m_fClassView)
        {
            changeObjectClass(m_document.getClassName());
        }

        // activate the first view
        m_SubNavigation.navigate(0);

    }

    /**
     * Creates the {@link OwPropertyViewBridge}
     * @return the newly created {@link OwPropertyViewBridge} object.
     * @throws Exception
     */
    protected OwPropertyViewBridge createPropertyViewBridge() throws Exception
    {
        OwPropertyViewBridge bridge = null;
        //uses form only for defined object classes
        if (getJspConfigurator() != null)
        {
            String jspPage = null;
            if (m_objectClassName != null && getJspConfigurator().isJspFormEnabled())
            {
                jspPage = getJspConfigurator().getJspForm(m_objectClassName);
                if (jspPage != null)
                {
                    bridge = createFormPropertyViewBridge();
                }
            }
        }

        if (bridge == null)
        {
            bridge = createStandardPropertyViewBridge();
        }

        bridge.setReadOnlyContext(OwPropertyClass.CONTEXT_ON_CREATE);
        bridge.setPropertyListConfiguration(this.m_document.getPropertyListConfiguration());

        return bridge;
    }

    /**
     * Creates an {@link OwPropertyViewBridge} that use standard way for properties rendering.
     * @return - the newly created {@link OwPropertyViewBridge} object
     * @throws Exception
     * @since 3.1.0.0
     */
    protected OwPropertyViewBridge createStandardPropertyViewBridge() throws Exception
    {
        OwMultiDocumentObjectPropertyView view = createMultiDocumentObjectPropertyView();

        view.setReadOnlyContext(OwPropertyClass.CONTEXT_ON_CREATE);
        view.setEventListener(this);
        view.setSaveAllActive(isSaveAllEnabled());
        // NOTE: Initially no object is set, will set it upon class selection
        if (hasViewMask(VIEW_MASK_ENABLE_PASTE_METADATA))
        {
            view.setViewMask(OwObjectPropertyView.VIEW_MASK_ENABLE_PASTE_METADATA);
        }
        if (!m_document.getPropertyInfos().isEmpty())
        {
            view.setVirtualParentRestriction(m_document.getPropertyInfos());
        }

        return new OwStandardPropertyViewBridge(view);
    }

    /**
     * Create an {@link OwPropertyViewBridge} object based on a JSP file.
     * @return the newly created {@link OwPropertyViewBridge}
     * @since 3.1.0.0
     */
    protected OwPropertyViewBridge createFormPropertyViewBridge()
    {
        OwObjectPropertyFormularView view = createFormBasedView();
        OwFormPropertyViewBridge result = new OwFormPropertyViewBridge(view);
        return result;
    }

    /**
     * Create a form view, with the given JSP file.
     * @return {@link OwAddMultiDocumentsPropertyFormView} document.
     * @since 3.1.0.0
     */
    protected OwObjectPropertyFormularView createFormBasedView()
    {
        OwAddMultiDocumentsPropertyFormView view = new OwAddMultiDocumentsPropertyFormView(hasViewMask(VIEW_MASK_ENABLE_SAVE_ALL));
        view.setJspConfigurator(getJspConfigurator());
        view.setEventListner(this);
        view.setReadOnlyContext(OwPropertyClass.CONTEXT_ON_CREATE);
        view.setPasteMetadataEnabled(hasViewMask(VIEW_MASK_ENABLE_PASTE_METADATA));
        view.setSaveAllActive(isSaveAllEnabled());
        return view;
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach document
        getDocument().detach();
    }

    /** overridable to display the title of the dialog
    *
    * @return title
    */
    public String getTitle()
    {
        String strTitle = getContext().localize("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.Title", "Add new document:");
        if (m_document.getImportedDocumentsCount() > 0)
        {
            String strFilename = m_document.getImportedDocument(m_MultipleDocumentsCurrentPos).getDisplayName();
            strTitle += (strFilename != null) ? strFilename : "";
        }
        return strTitle;
    }

    /** render the views of the region
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
                if (m_propertyBridge != null)
                {
                    m_propertyBridge.setSaveAllActive(isSaveAllEnabled());
                }
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** determine if region contains a view
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

            case HELP_BTN_REGION:
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

        // new document class selected
        if (iCode_p == OwUpdateCodes.SET_NEW_OBJECT)
        {
            if (m_MultipleDocumentsCurrentPos >= 0 && m_MultipleDocumentsCurrentPos < m_document.getImportedDocumentsCount())
            {
                updateViews();
                OwObject objSkeleton = m_document.getSkeletonObject(false);
                if (objSkeleton != null)
                {
                    provideValuesMap(objSkeleton);
                    // new set the reference
                    if (null != m_AccessRightsView)
                    {
                        m_AccessRightsView.setObjectRef(objSkeleton);
                    }
                    //get object class name
                    m_objectClassName = objSkeleton.getClassName();
                    //create view bridge and set view
                    this.m_propertyBridge = createPropertyViewBridge();
                    setCheckinHandling((OwCheckInHandler) m_propertyBridge.getView());
                    m_propertyBridge.setSaveAllActive(isSaveAllEnabled());
                    this.delegateView.setView(this.m_propertyBridge.getView());

                    m_propertyBridge.setObjectRef(objSkeleton, false);
                }
                // bug 1971
                // open preview (if not already shown). This will open the preview on manual file upload when the class is selected.
                if (!m_previewOpened)
                {
                    if (hasViewMask(VIEW_MASK_AUTOOPEN))
                    {
                        previewDocument(m_MultipleDocumentsCurrentPos);
                    }
                }
            }
        }

        if (iCode_p == OwUpdateCodes.LOGOUT)
        {
            // if we got a Logout event, don't forget to cleanup()
            cleanup();
        }
    }

    /**
     * Cleanup all resources acquired by this dialog.
     * 
     * @throws Exception
     */
    protected void cleanup() throws Exception
    {
        // release all documents imported so far
        m_document.releaseImportedDocuments();

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

        // release DND document import stack, if we used it
        if (m_PerformDragDrop)
        {
            ((OwMainAppContext) getContext()).releaseDNDImportedDocuments();
        }
    }

    /**
     * Invoked by the framework whenever this dialog is closed
     */
    public void onClose(HttpServletRequest request_p) throws Exception
    {
        // cleanup
        cleanup();
        if (m_documentImportView != null)
        {
            m_documentImportView.removeDocumentImporterCallBackListener(m_document);
        }
        // and close
        super.onClose(request_p);
    }

    public void onCancel() throws Exception
    {
        // we just do nothing here
        // See Ticket #864 for detailed reasons
    }

    public void onSaveDocument() throws Exception
    {
        //-----< store document >-----
        OwDocumentImportItem importedDocument = m_document.getImportedDocument(m_MultipleDocumentsCurrentPos);
        // create content collection
        OwContentCollection content = createDocumentImportItemContentCollection(importedDocument);
        // get release version and checkin-options

        boolean fReleaseVersion;
        if (importedDocument.getCheckinAsMajor() == null)
        {
            fReleaseVersion = ((OwCheckInHandler) m_propertyBridge.getView()).isReleaseVersion();
        }
        else
        {
            fReleaseVersion = importedDocument.getCheckinAsMajor().booleanValue();
        }

        Object modeObject = ((OwCheckInHandler) m_propertyBridge.getView()).getCheckinModeObject();
        String createdDMSID = null;

        // store document
        try
        {
            if (processDocument(importedDocument))
            {
                m_dialogStatus = DIALOG_STATUS_NONE;
                createdDMSID = storeDocument(content, importedDocument.getContentMimeType(0), importedDocument.getContentMimeParameter(0), modeObject, fReleaseVersion);
            }
        }
        catch (Exception e)
        {
            // flag failure
            m_dialogStatus = DIALOG_STATUS_FAILED;
            // re-throw exception
            throw e;
        }

        if (createdDMSID != null)
        {
            //new interface extension for post process view
            OwDocumentImporter importer = m_document.getDocumentImporter(m_MultipleDocumentsCurrentPos);
            OwObject objProcessed = ((OwMainAppContext) getContext()).getNetwork().getObjectFromDMSID(createdDMSID, true);
            openPostProcessDialog(importer, objProcessed);

            //removed encodeToSecureHTML because another encoding is performed in postOnSaveMessage 
            //String sDocName = OwHTMLHelper.encodeToSecureHTML(m_document.getSkeletonObject(false).getName());
            String sDocName = m_document.getSkeletonObject(false).getName();
            postOnSaveMessage(Arrays.asList(new OwDocumentImportItem[] { importedDocument }), Arrays.asList(new String[] { sDocName }), Arrays.asList(new OwDocumentImportItem[] {}), false);
        }
        else
        {
            postOnSaveMessage(Arrays.asList(new OwDocumentImportItem[] {}), Arrays.asList(new String[] {}), Arrays.asList(new OwDocumentImportItem[] { importedDocument }), false);
        }

        // flag success. Only if not already in FAILED state
        if (m_dialogStatus != DIALOG_STATUS_FAILED)
        {
            m_dialogStatus = DIALOG_STATUS_OK;
        }

        //-----< go to next document in line >-----
        m_MultipleDocumentsCurrentPos++;

        //-----< check if we hit the end >-----
        if (m_MultipleDocumentsCurrentPos < m_document.getImportedDocumentsCount())
        {
            updateViews();

            //-----< preview next document >-----
            if (hasViewMask(VIEW_MASK_AUTOOPEN))
            {
                previewDocument(m_MultipleDocumentsCurrentPos);
            }

            // === get batch values before refreshing next object
            OwPropertyCollection properties = m_document.getSkeletonObject(false).getClonedProperties(null);

            OwObject obj = m_document.getSkeletonObject(true);
            provideValuesMap(obj);
            // === set values
            // set title
            importedDocument = m_document.getImportedDocument(m_MultipleDocumentsCurrentPos);
            setSkeletonNameProperty(obj, importedDocument);

            if (isImporterBeforeBatchHandling())
            {
                propagateDocumentImporterData(importedDocument, obj);
            }
            // set other vales from batch indexing
            setBatchProperties(properties, obj);
            if (!isImporterBeforeBatchHandling())
            {
                propagateDocumentImporterData(importedDocument, obj);
            }
            m_propertyBridge.setObjectRef(obj, false);
        }
        else
        {
            close();
        }
    }

    /**
     * Sets the batch properties with values from the source attribute to the specified object
     * @param batchPropertiesSource_p collection of valued-properties to be set if they match the batched properties
     * @param object_p {@link OwObject} to set the properties to 
     * 
     */
    protected void setBatchProperties(OwPropertyCollection batchPropertiesSource_p, OwObject object_p)
    {
        // set other vales from batch indexing
        if (null != getBatchIndexProperties())
        {
            OwObjectClass objectClass = object_p.getObjectClass();
            Iterator it = getBatchIndexProperties().iterator();
            while (it.hasNext())
            {
                String sPropName = (String) it.next();
                OwProperty propertyToSet = (OwProperty) batchPropertiesSource_p.get(sPropName);
                if (propertyToSet != null)
                {
                    try
                    {
                        OwProperty objectProperty = object_p.getProperty(sPropName);
                        if (objectProperty != null)
                        {
                            Object valueToSet = propertyToSet.getValue();
                            objectProperty.setValue(valueToSet);
                        }
                        else
                        {
                            LOG.debug("OwCreateMultipleDocumentsDialog.setBatchProperties: Skipped Add Batch-Property = [ " + sPropName + " ] for object = " + object_p.getName() + " of type = " + objectClass.getClassName()
                                    + ", the object does not define this property!");
                        }
                    }
                    catch (Exception e)
                    {
                        LOG.debug("Skipped Add Batch-Property = [ " + sPropName + " ] for object = " + object_p.getName() + " of type = " + objectClass.getClassName() + ", an error occurred while attempting to set it!", e);
                    }
                }
                else
                {
                    LOG.debug("OwCreateMultipleDocumentsDialog.setBatchProperties: Skipped Add Batch-Property = [" + sPropName + " ] for object=" + object_p.getName() + " of type = " + objectClass.getClassName()
                            + ", edited object does not define this property!");
                }
            }
        }
    }

    public void onSaveAllDocument() throws Exception
    {
        // count the number of documents we created in one single run (we might stop multiple times due to errors)
        //int addedInThisRun = 0;

        //Name property which should not be added to batch properties
        String ignore = m_document.getSkeletonObject(false).getObjectClass().getNamePropertyName();
        //iterate over all possible editable properties
        Iterator it = m_document.getSkeletonObject(false).getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE).keySet().iterator();
        //create new temporary list of properties for save all function
        m_batchIndexProperties = new LinkedList();//TODO create batchIndexProperties depending on documentItem?
        while (it.hasNext())
        {
            String propName = it.next().toString();
            if (propName != null && !propName.equals(ignore))
            {
                m_batchIndexProperties.add(propName);
            }
        }

        List skippedDocuments = new ArrayList();
        List addedDocuments = new ArrayList();
        List addedDocumentsNames = new ArrayList();

        // store all documents that are in the stack
        while (m_MultipleDocumentsCurrentPos < m_document.getImportedDocumentsCount())
        {
            //-----< store document >-----
            OwDocumentImportItem importedDocument = m_document.getImportedDocument(m_MultipleDocumentsCurrentPos);
            // create content collection
            OwContentCollection content = createDocumentImportItemContentCollection(importedDocument);
            // get release version and checkin-options
            boolean fReleaseVersion = ((OwCheckInHandler) m_propertyBridge.getView()).isReleaseVersion();
            Object modeObject = ((OwCheckInHandler) m_propertyBridge.getView()).getCheckinModeObject();
            // store document
            try
            {
                if (processDocument(importedDocument))
                {
                    m_dialogStatus = DIALOG_STATUS_NONE;
                    storeDocument(content, importedDocument.getContentMimeType(0), importedDocument.getContentMimeParameter(0), modeObject, fReleaseVersion);
                    addedDocuments.add(importedDocument);
                    String sDocName = OwHTMLHelper.encodeToSecureHTML(m_document.getSkeletonObject(false).getName());
                    addedDocumentsNames.add(sDocName);
                    //addedInThisRun++;
                }
                else
                {
                    skippedDocuments.add(importedDocument);
                }

            }
            catch (Exception e)
            {
                onBatchImportError(e, importedDocument, skippedDocuments, addedDocuments, addedDocumentsNames);
            }
            // flag success. Only if not already in FAILED state
            if (m_dialogStatus != DIALOG_STATUS_FAILED)
            {
                m_dialogStatus = DIALOG_STATUS_OK;
            }

            //-----< advance to next document in line >-----
            m_MultipleDocumentsCurrentPos++;

            //-----< more documents? set the props of the next one
            if (m_MultipleDocumentsCurrentPos < m_document.getImportedDocumentsCount())
            {
                // get batch values before refreshing next object
                OwPropertyCollection properties = m_document.getSkeletonObject(false).getClonedProperties(null);

                // now get new skeleton
                OwObject obj = m_document.getSkeletonObject(true);
                // set title
                setSkeletonNameProperty(obj, m_document.getImportedDocument(m_MultipleDocumentsCurrentPos));

                // set other values from batch indexing
                setBatchProperties(properties, obj);
            }
        }

        //clear temporary created list
        m_batchIndexProperties.clear();

        // No Exception until now? OK. Cleanup and close
        postOnSaveMessage(addedDocuments, addedDocumentsNames, skippedDocuments, true);
        close();
    }

    /**
     * Called when one of the documents in a batch import fails to import.
     * If you want the import process to be stopped then throw an exception, 
     * otherwise the process will continue with the next {@link OwDocumentImportItem}.
     * 
     * @param e_p
     * @param importedDocument_p 
     * @param skippedDocuments_p
     * @param addedDocuments_p
     * @param addedDocumentsNames_p
     * @throws Exception
     * @see #onSaveAllDocument()
     * @since 3.1.0.3
     */
    protected void onBatchImportError(Exception e_p, OwDocumentImportItem importedDocument_p, List skippedDocuments_p, List addedDocuments_p, List addedDocumentsNames_p) throws Exception
    {
        // if we bail out here, we need to refresh the view
        m_propertyBridge.setObjectRef(m_document.getSkeletonObject(false), false);
        // flag failure
        m_dialogStatus = DIALOG_STATUS_FAILED;
        // report number of documents we have added until now
        postOnSaveMessage(addedDocuments_p, addedDocumentsNames_p, skippedDocuments_p, true);
        // re-throw exception
        throw e_p;
    }

    /** called by onSaveDocument when a user clicks on 'save' in the properties view
     *
     * @param content_p OwContentCollection
     * @param strMimeType_p String
     * @param strMimeParameter_p String
     * @param checkinMode_p optional Object, used with checkin operation only, can be null
     * @param fReleaseVersion_p optional boolean flag to create a release version, used with checkin operation only
     * 
     * @return String DMSID of created object
     */
    protected String storeDocument(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p, Object checkinMode_p, boolean fReleaseVersion_p) throws Exception
    {
        OwMainAppContext context = (OwMainAppContext) getContext();
        // filter out read-only, hidden and null properties
        OwPropertyCollection docStandardPropertiesMap = m_document.getSkeletonObject(false).getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        // fetch object class name
        String className = m_document.getSkeletonObject(false).getObjectClass().getClassName();

        OwObject folder = prepareParentFolder(context, className);

        // permissions
        OwPermissionCollection permissions = null;
        if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS))
        {
            permissions = m_document.getSkeletonObject(false).getPermissions();
        }

        // create a new object
        String dmsid = context.getNetwork().createNewObject(fReleaseVersion_p, checkinMode_p, m_document.getResource(), className, docStandardPropertiesMap, permissions, content_p, folder, strMimeType_p, strMimeParameter_p);

        return dmsid;
    }

    /**
     * This method prepares the parent folder for the currently processed document ({@link #m_MultipleDocumentsCurrentPos}).
     * @param context_p
     * @param documentClassName_p
     * @return the parent folder to be used for this new document. The callers can rely on the parent folder being created by these method if needed.
     * @throws Exception
     * @since 3.1.0.3
     */
    protected OwObject prepareParentFolder(OwMainAppContext context_p, String documentClassName_p) throws Exception
    {
        OwObject folder = m_parentFolder;
        // perform optional class mapping
        if (hasClassToFolderMapping())
        {
            String strFolderPath = (String) m_objectClassMap.get(documentClassName_p);
            if (strFolderPath != null)
            {
                java.util.StringTokenizer paths = new java.util.StringTokenizer(strFolderPath, "/");

                while (paths.hasMoreTokens())
                {
                    String strName = paths.nextToken();
                    // get or create subfolder
                    folder = OwEcmUtil.createSafeSubFolder(context_p, folder, strName);
                }
            }
        }
        return folder;
    }

    /**
     * If there is a mapping between Classes and Folders, 
     * documents of a given class should be saved in the same folder as specified by the mapping in {@link #m_objectClassMap}.
     * @return true if the folder is to be chosen based on the document's class.
     * @since 3.1.0.3
     */
    protected final boolean hasClassToFolderMapping()
    {
        return m_objectClassMap != null;
    }

    /** open the preview dialog for a document
     *
     * @param iIndex_p int Position of document to preview in OwAddMultiDocumentsDocument.getUploadedFilename
     *
     */
    protected void previewDocument(int iIndex_p) throws Exception
    {
        OwMainAppContext context = (OwMainAppContext) getContext();

        // get preview filename
        OwDocumentImportItem importItem = m_document.getImportedDocument(iIndex_p);
        String previewPath = importItem.getPreviewFilePath(0);
        String mimeType = importItem.getContentMimeType(0);

        // close preview or start new preview
        if (null == previewPath)
        {
            if (context.getWindowPositions().getPositionMainWindow())
            {
                context.addFinalScript("\n" + OwMimeManager.createAutoViewerRestoreMainWindowScript(context, OwMimeManager.VIEWER_MODE_DEFAULT));
            }
            m_previewOpened = false;
        }
        else
        {
            OwMimeManager.openFilePreview(context, mimeType, previewPath, importItem.getDisplayName());
            m_previewOpened = true;
        }
    }

    /** close the dialog
     *  overwritten method to close the preview
     */
    public void closeDialog() throws Exception
    {
        // close the dialog
        super.closeDialog();

        // close the preview
        if (hasViewMask(VIEW_MASK_AUTOOPEN) && m_previewOpened)
        {
            OwMainAppContext context = (OwMainAppContext) getContext();

            if (context.getWindowPositions().getPositionMainWindow())
            {
                context.addFinalScript("\n" + OwMimeManager.createAutoViewerRestoreMainWindowScript(context, OwMimeManager.VIEWER_MODE_DEFAULT));
            }
        }
    }

    /**
     * Document imported listener.
     * Resets the active status of the Save All action in the property view.   
     */
    public void onDocumentImported()
    {
        try
        {
            updateViews();
            if (m_document.hasUndefinedObjectClass())
            {
                if (m_classView != null)
                {
                    if (m_documentImportView != null)
                    {
                        m_documentImportView.setNextActivateView(m_classView);
                    }

                    if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS))
                    {
                        m_AccessRightsView.setPrevActivateView(m_classView);
                    }
                    else
                    {
                        ((OwMultipanel) m_propertyBridge.getView()).setPrevActivateView(m_classView);
                    }

                    activateTabView(m_idxClassView);
                }
            }
            else
            {
                if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS))
                {
                    //bypass validation, if prevView == null isValid == true
                    m_AccessRightsView.setPrevActivateView(null);
                }
            }
        }
        catch (Exception e)
        {
            LOG.warn("OwCreateMultipleDocumentsDialog.onDocumentImported(): Exception", e);
        }
    }

    /**
     * check if the Save all button should be enabled.
     * <p>This method analyze the viewmask {@link #VIEW_MASK_ENABLE_SAVE_ALL}
     * and also if a post process view is enabled for imported documents</p>
     * @return boolean 
     * @since 2.5.2.0
     */
    public boolean isSaveAllEnabled()
    {
        return hasViewMask(VIEW_MASK_ENABLE_SAVE_ALL) && m_document.getImportedDocumentsCount() > 1 && !m_document.showProcessView();
    }

    /**
     * (overridable)
     * Return the dialog which should be used for the post process view.
     * @return OwStandardDialog to open as post processing dialog (non-null)
     * @since 2.5.2.0
     */
    protected OwStandardDialog getPostProcessingDialog()
    {
        return new OwStandardDialog();
    }

    /**
     * (overridable)
     * Handles the close event of post process dialog.
     * By default this method is empty and do nothing, it can be overwritten 
     * if close event notification subscription is needed
     * @param dialog_p OwDialog used for post processing
     * @since 2.5.2.0
     */
    public void onPostProcessingDialogClose(OwDialog dialog_p) throws Exception
    {

    }

    /**
     * Helper method for changing the ObjectClass(=DocumentClass) of
     * skeleton. Set the new ObjectClass only if object class name is
     * valid for current context.
     * <p><b>ATTENTION</b>: After calling this method an onUpdate(caller, OwUpateCodes.SET_NEW_OBJECT, param)
     *  event will occur.</p>
     * @param objectClass_p String symbolic name of Document-/ObjectClass.
     * @throws Exception
     * @since 2.5.2.0
     */
    protected void changeObjectClass(String objectClass_p) throws Exception
    {
        try
        {
            OwMainAppContext context = (OwMainAppContext) getContext();
            OwObjectClass objClass = context.getNetwork().getObjectClass(objectClass_p, m_document.getResource());
            //TODO compare if given string is exact the same as the returned ObjectClass-name
            m_document.setObjectClass(objClass);
        }
        catch (OwObjectNotFoundException ex)
        {
            //do nothing if a document class not exist
            //else there will be an endless recursion
        }
    }

    /*This method will be called every time when there is a change of SkeletonObject,
     *after upload of an DocumentItem, and also during save of DocumentItems.
     *ATTENTION: this method deactivates and activates Tabs of m_SubNavigation,
     *depending on the added document items and current state of processing.*/
    private void updateViews() throws Exception
    {
        OwObject objSkeleton = m_document.getSkeletonObject(false);

        // set filename and given properties if we already have an imported document
        if ((m_MultipleDocumentsCurrentPos >= 0) && (m_MultipleDocumentsCurrentPos < m_document.getImportedDocumentsCount()))
        {
            // get the imported document
            OwDocumentImportItem importedDocument = m_document.getImportedDocument(m_MultipleDocumentsCurrentPos);
            String skeletonObjectClass = objSkeleton != null ? objSkeleton.getObjectClass().getClassName() : null;

            if (importedDocument.getObjectClassName() != null && m_document.isValidObjecClass(importedDocument.getObjectClassName()))
            {
                //object class of DocumentImportItem overwrites the predefined
                //or by user selected object class
                if (!importedDocument.getObjectClassName().equalsIgnoreCase(skeletonObjectClass))
                {

                    if (!m_document.hasUndefinedObjectClass())
                    {
                        if (m_documentImportView != null)
                        {
                            if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS))
                            {
                                m_documentImportView.setNextActivateView(m_AccessRightsView);
                                m_AccessRightsView.setPrevActivateView(m_documentImportView);
                            }
                            else
                            {
                                m_documentImportView.setNextActivateView(m_propertyBridge.getView());
                            }
                        }
                        deactivateTabView(m_idxClassView);
                    }
                    else
                    {
                        if (m_documentImportView != null && m_classView != null)
                        {
                            //currently making changes to properties
                            if (m_SubNavigation.getNavigationIndex() == m_idxPropertyView)
                            {
                                //if current document has no object class, keep class view active
                                if (importedDocument.getObjectClassName() == null)
                                {
                                    activateTabView(m_idxClassView);
                                    m_documentImportView.setNextActivateView(m_classView);
                                }
                                else
                                {// current document has a predefined class, deactivate class view
                                    deactivateTabView(m_idxClassView);
                                    if (hasViewMask(VIEW_MASK_ACCESS_RIGHTS))
                                    {
                                        m_documentImportView.setNextActivateView(m_AccessRightsView);
                                    }
                                }
                            }
                            else
                            {
                                m_documentImportView.setNextActivateView(m_classView);
                                if (m_AccessRightsView != null)
                                {
                                    m_AccessRightsView.setPrevActivateView(m_classView);
                                }
                                activateTabView(m_idxClassView);
                            }
                        }
                    }

                    changeObjectClass(importedDocument.getObjectClassName());
                    return;//should escape the method here, because an onUpdate will be handled
                }
            }
            else
            {
                //handle here in plugins configured class
                /*remember compare the ObjectClassName case insensitive, because P8 is returning the correct ObjectClass even 
                 * if the defined class is defined with wrong upper and lower-case*/
                if (m_document.getClassName() != null && !m_document.getClassName().equalsIgnoreCase(skeletonObjectClass))
                {
                    LOG.info("OwCreateMultipleDocumentsDialog.updateViews: Using definition of owplugins.xml: document.class=" + m_document.getClassName() + " skeleton=" + skeletonObjectClass);
                    changeObjectClass(m_document.getClassName());
                    return;//should escape the method here, because an onUpdate will be handled
                }
                //handle here user selected object class
                /*remember compare the ObjectClassName case insensitive, because P8 is returning the correct ObjectClass even 
                 * if the defined class is defined with wrong upper and lower-case*/
                if (m_document.getUserSelectedClassName() != null && !m_document.getUserSelectedClassName().equalsIgnoreCase(skeletonObjectClass))
                {
                    LOG.info("OwCreateMultipleDocumentsDialog.updateViews: Using definition from user: user document.class=" + m_document.getUserSelectedClassName() + " skeleton=" + skeletonObjectClass);
                    changeObjectClass(m_document.getUserSelectedClassName());
                    return;//should escape the method here, because an onUpdate will be handled
                }
            }

            // set filename property
            setSkeletonNameProperty(objSkeleton, importedDocument);

            // merge with property values from importer
            propagateDocumentImporterData(importedDocument, objSkeleton);

            setCheckinHandling((OwCheckInHandler) m_propertyBridge.getView());
        }

    }

    /**
     * Set the CheckInHandler regarding on current skeleton object and depending OwDocumentImportItem 
     * @param handler_p OwCheckInHandler
     * @throws Exception
     * @since 3.1.0.0
     */
    protected void setCheckinHandling(OwCheckInHandler handler_p) throws Exception
    {
        OwObject objSkeleton = m_document.getSkeletonObject(false);
        // get new checkin options
        List checkinModeList = hasViewMask(VIEW_MASK_CHECKIN_MODE_OPTION) && objSkeleton != null ? objSkeleton.getObjectClass().getModes(OwObjectClass.OPERATION_TYPE_CREATE_NEW_OBJECT) : null;
        //set if checkin version is major or minor version as default
        Boolean checkinDocItem = m_document.getImportedDocument(m_MultipleDocumentsCurrentPos).getCheckinAsMajor();
        if (checkinDocItem == null)
        {
            handler_p.setCheckInVersionMajor(hasViewMask(VIEW_MASK_RELEASE_VERSION_DEFAULT));
            //default handling as configured in plugin
            handler_p.setCheckInOptions(checkinModeList, hasViewMask(VIEW_MASK_RELEASE_VERSION_OPTION));
        }
        else
        {
            handler_p.setCheckInVersionMajor(checkinDocItem.booleanValue());
            //user cannot select major/minor version
            handler_p.setCheckInOptions(checkinModeList, false);
        }
    }

    /*Enables given tab index in m_Subnaviation,
     *enables tab only if tabIndex_p is valid.
     *@since 2.5.2.0 */
    private void deactivateTabView(int tabIndex_p)
    {
        if (tabIndex_p >= 0 && tabIndex_p < m_SubNavigation.getTabList().size())
        {
            ((OwTabInfo) m_SubNavigation.getTabList().get(tabIndex_p)).setDisabled(true);
        }
    }

    /*Disables given tab index in m_Subnaviation,
     *disables tab only if tabIndex_p is valid.
     *@since 2.5.2.0 */
    private void activateTabView(int tabIndex_p)
    {
        if (tabIndex_p >= 0 && tabIndex_p < m_SubNavigation.getTabList().size())
        {
            ((OwTabInfo) m_SubNavigation.getTabList().get(tabIndex_p)).setDisabled(false);
        }
    }

    /**
     * Method which is called after processing of documents, if
     * the dependent document importer has a post process view for
     * {@link OwDocumentImporter#IMPORT_CONTEXT_NEW}.
     * @param importer_p OwDoucmentImporter (non-null)
     * @param obj_p OwObject currently processed object (non-null)
     * @throws Exception if there are problems opening the post process dialog
     * @since 2.5.2.0
     */
    protected void openPostProcessDialog(OwDocumentImporter importer_p, OwObject obj_p) throws Exception
    {
        if (importer_p != null && importer_p.hasPostProcessView(OwDocumentImporter.IMPORT_CONTEXT_NEW))
        {
            OwDialog dlg = getPostProcessingDialog();
            dlg.attach(getContext(), null);
            dlg.addView(importer_p.getPostProcessView(OwDocumentImporter.IMPORT_CONTEXT_NEW, obj_p), OwStandardDialog.MAIN_REGION, null);
            getContext().openDialog(dlg, this);
            setPostProcessDailogOpen(true);
        }

    }

    /**
     * Handles the close event of post processing dialog.
     * This methods will call {@link #onPostProcessingDialogClose(OwDialog)}
     * before trying to close current OwCreateMultipleDocumentsDialog.
     * @see com.wewebu.ow.server.ui.OwDialog.OwDialogListener#onDialogClose(OwDialog) OwDialogListener.onDialogClose(OwDialog)
     * @since 2.5.2.0
     */
    public final void onDialogClose(OwDialog dialog_p) throws Exception
    {
        setPostProcessDailogOpen(false);
        onPostProcessingDialogClose(dialog_p);
        close();
    }

    /**
     * Execute the close of current dialog only if
     * given state allows a closeDialog call.
     * @throws Exception
     */
    protected void close() throws Exception
    {
        if (!isPostProcessDialogOpen())
        {
            cleanup();
            closeDialog();
        }
        // notify client to refresh
        if (m_RefreshCtx != null)
        {
            m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_FOLDER_CHILDS, null);
            m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);
        }
    }

    /**
     * Flag notifying if a post process dialog was/is open. 
     * @return boolean
     * @since 2.5.2.0
     */
    public boolean isPostProcessDialogOpen()
    {
        return this.m_isPostProcessDlgOpen;
    }

    /**
     * Set the flag to notify that currently a post process dialog was/is open.
     * @param flagDlgOpen_p boolean <code>true</code> if dialog is open, else <code>false</code>
     * @since 2.5.2.0
     */
    protected void setPostProcessDailogOpen(boolean flagDlgOpen_p)
    {
        this.m_isPostProcessDlgOpen = flagDlgOpen_p;
    }

    /**(overridable)
     * 
     * creates and posts a message to be displayed after the {@link #onSaveAllDocument()} or {@link #onSaveDocument()}
     * was performed
     * @param addedItems_p a  {@link List} of {@link OwDocumentImportItem}s that were added
     * @param addedDocumentNames_p a {@link List} of {@link String} document names of the already added documents  
     * @param skippedItems_p a {@link List} of {@link OwDocumentImportItem}s that were skipped from importing
     * @param isSaveAll_p <code>true</code> if this is a message post after {@link #onSaveAllDocument()} <code>false</code> otherwise 
     * @since 3.0.0.0
     */
    protected void postOnSaveMessage(List addedItems_p, List addedDocumentNames_p, List skippedItems_p, boolean isSaveAll_p) throws Exception
    {
        if (isSaveAll_p)
        {
            String sMessage = "";
            int addedContentCount = addedItems_p.size();
            if (addedContentCount == 1)
            {
                sMessage = getContext().localize1("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.MultipleDocumentsCreated.one", "%1 document has been created.", Integer.toString(addedContentCount));
            }
            else
            {
                sMessage = getContext().localize1("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.MultipleDocumentsCreated", "%1 documents have been created.", Integer.toString(addedContentCount));
            }

            if (skippedItems_p != null && !skippedItems_p.isEmpty())
            {
                StringBuffer skippedDocuments = new StringBuffer();
                for (Iterator i = skippedItems_p.iterator(); i.hasNext();)
                {
                    OwDocumentImportItem importItem = (OwDocumentImportItem) i.next();
                    String itemName = importItem.getDisplayName();
                    if (skippedDocuments.length() > 0)
                    {
                        skippedDocuments.append(",");
                    }
                    skippedDocuments.append(itemName);
                }

                String skippedMessage;

                if (skippedItems_p.size() == 1)
                {
                    skippedMessage = getContext().localize2("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.DocumentsSkipped.one", "%1 document was skipped : %2", Integer.toString(skippedItems_p.size()), skippedDocuments.toString());
                }
                else
                {
                    skippedMessage = getContext().localize2("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.DocumentsSkipped", "%1 documents were skipped : %2", Integer.toString(skippedItems_p.size()), skippedDocuments.toString());
                }

                sMessage += " " + skippedMessage;
            }

            ((OwMainAppContext) getContext()).postMessage(sMessage);
        }
        else
        {
            if (addedDocumentNames_p != null && !addedDocumentNames_p.isEmpty())
            {
                String addedDocName = (String) addedDocumentNames_p.get(0);
                // post message
                String sDocName = OwHTMLHelper.encodeToSecureHTML(addedDocName);
                String sMessage = getContext().localize1("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.DocumentCreated", "Document %1 has been created.", sDocName);
                ((OwMainAppContext) getContext()).postMessage(sMessage);
            }

            if (skippedItems_p != null && !skippedItems_p.isEmpty())
            {
                OwDocumentImportItem importItem = (OwDocumentImportItem) skippedItems_p.get(0);
                // post message
                String sDocName = OwHTMLHelper.encodeToSecureHTML(importItem.getDisplayName());
                String sMessage = getContext().localize1("plug.owaddmultidocuments.OwCreateMultipleDocumentsDialog.DocumentSkipped", "Document %1 has been skipped.", sDocName);
                ((OwMainAppContext) getContext()).postMessage(sMessage);
            }
        }
    }

    /** (overridable)
     * 
     * @param document_p document to be processed before adding
     * @return <code>true</code> if the given document should be created/added , <code>false</code> otherwise 
     * @throws OwException
     * @since 3.0.0.0
     */
    protected boolean processDocument(OwDocumentImportItem document_p) throws OwException
    {
        return true;
    }

    /**
     * Propagation of data from the importer to current skeleton object.
     * <p>By default the non-existing properties of current skeleton will
     * be ignored, and only matching properties will be set.</p>
     * @param importItem_p OwDocumentImporterItem to use for property propagation
     * @param objSkeleton_p OwObject current skeleton
     * @throws Exception if problem occur with setting data to skeleton object.
     * @since 3.1.0.0
     */
    protected void propagateDocumentImporterData(OwDocumentImportItem importItem_p, OwObject objSkeleton_p) throws Exception
    {
        if (objSkeleton_p != null)
        {
            Map importItemProperties = importItem_p.getPropertyValueMap();
            if (importItemProperties != null && !importItemProperties.isEmpty())
            {
                Iterator itImportItemProperties = importItemProperties.entrySet().iterator();
                while (itImportItemProperties.hasNext())
                {
                    Entry entryProp = (Entry) itImportItemProperties.next();
                    try
                    {
                        OwProperty skeletonProp = objSkeleton_p.getProperty((String) entryProp.getKey());
                        skeletonProp.setValue(entryProp.getValue());
                    }
                    catch (OwObjectNotFoundException onfe)
                    {
                        // ignore
                    }
                }
            }
        }
    }

    public OwJspFormConfigurator getJspConfigurator()
    {
        return this.m_jspConfigurator;
    }

    public void setJspConfigurator(OwJspFormConfigurator jspFormConfigurator_p)
    {
        this.m_jspConfigurator = jspFormConfigurator_p;
    }

    /**
     * Set the name property of given skeleton object, retrieving the value from the document importer item.
     * Will do nothing if one of the parameter is null!
     * @param objSkeleton_p OwObject can be null
     * @param importerItem_p OwDocumentImportItem can be null
     * @throws Exception if problem with retrieving or setting property
     * @since 3.1.0.0
     */
    protected void setSkeletonNameProperty(OwObject objSkeleton_p, OwDocumentImportItem importerItem_p) throws Exception
    {
        if (objSkeleton_p != null && importerItem_p != null)
        {
            String proposedDocumentName = getNamePropertyValue(importerItem_p);
            if (proposedDocumentName != null)
            {
                objSkeleton_p.getProperty(objSkeleton_p.getObjectClass().getNamePropertyName()).setValue(proposedDocumentName);
            }
        }
    }

    /**
     * Get the order to process properties propagation.
     * <p>false: Batch properties will be propagated before importer properties</p>
     * <p>true: Importer properties will be propagated before batch properties</p>
     * The second propagation can overwrite the first propagation.
     * @return boolean by default false
     * @since 3.1.0.0
     * @see #setImporterBeforeBatchHandling
     */
    public boolean isImporterBeforeBatchHandling()
    {
        return this.importerBeforeBatch;
    }

    /**
     * Set the properties propagation flag.
     * <p>false: Batch properties will be executed before importer propagation
     * which can leads that some properties are overwritten, for true it is vice versa.</p>
     * @param flag_p
     * @since 3.1.0.0
     */
    public void setImporterBeforeBatchHandling(boolean flag_p)
    {
        this.importerBeforeBatch = flag_p;
    }

    /**
     * Set flag for proposed Name handling during creation.
     * @param useImporterProposedName_p
     * @since 3.1.0.0
     */
    public void setUseImporterProposedName(boolean useImporterProposedName_p)
    {
        this.useImporterProposedName = useImporterProposedName_p;
    }

    /**
     * Get notification if proposed name or
     * display name of OwDocumentImportItem should be used.
     * @return boolean
     * @see #getNamePropertyValue(OwDocumentImportItem)
     * @since 3.1.0.0
     */
    public boolean useImporterProposedName()
    {
        return this.useImporterProposedName;
    }

    /**
     * Get a String which is used as name property value.
     * @param importer_p OwDocumentImporterItem
     * @return String representing name for Name-Property
     * @since 3.1.0.0
     */
    protected String getNamePropertyValue(OwDocumentImportItem importer_p)
    {
        if (useImporterProposedName())
        {
            return importer_p.getProposedDocumentName();
        }
        else
        {
            return importer_p.getDisplayName();
        }
    }

    /**
     * Return the list which  should be used for the 
     * properties view, of the dialog.
     * <p>By default the {@link #getPropertyInfos()}
     * is used if it is not null and size is greater than
     * 0 (zero), else the document ({@link #getDocument()}) property info list will be returned.</p>
     * @return List of OwPropertyInfo objects
     * @since 3.1.0.2
     * @deprecated since 4.2.0.0 see {@link #setPropertyListConfiguration(OwPropertyListConfiguration)}
     */
    protected Collection getUsedPropertyInfoList()
    {
        if ((m_propertyInfos != null) && (m_propertyInfos.size() > 0))
        {
            return m_propertyInfos;
        }
        else
        {
            return m_document.getPropertyInfos();
        }

    }

    /**
     * Return the property info list which was defined through 
     * the {@link #setPropertyInfos(List)} call.
     * @return List of OwPropertyInfo objects, or null
     * @since 3.1.0.2
     * @deprecated since 4.2.0.0 see {@link #setPropertyListConfiguration(OwPropertyListConfiguration)}
     */
    protected List getPropertyInfos()
    {
        return this.m_propertyInfos;
    }

    /**
     * Provide the map of values to the given skeleton object.
     * <p>Will lookup if a matching property exist in current skeleton
     * and provide it with a specific value.</p>
     * @param objSkeleton_p OwObjectSkeleton to execute mapping on
     * @throws Exception if getting property or setting value has problems
     * @since 3.1.0.2
     */
    protected void provideValuesMap(OwObject objSkeleton_p) throws Exception
    {
        // Merge with Values if given
        if (null != m_ValuesMap && objSkeleton_p != null)
        {
            Iterator it = m_ValuesMap.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry valueEntry = (Map.Entry) it.next();
                try
                {
                    OwProperty skeletonProp = objSkeleton_p.getProperty(String.valueOf(valueEntry.getKey()));
                    skeletonProp.setValue(valueEntry.getValue());
                }
                catch (OwObjectNotFoundException e)
                {
                    // Ignore
                }
            }
        }
    }

    /**
     * Set an objClassProcessor instance, which should be used before skeleton creation.
     * <p>By default there will be no pre-processing of the object class, <code>OwObjectClassProcessor = null</code>.</p>
     * @param objClassProcessor OwObjectClassProcessor (can be null)
     * @since 4.1.1.0
     */
    public void setObjectClassProcessor(OwObjectClassProcessor objClassProcessor)
    {
        this.m_document.setObjectClassProcessor(objClassProcessor);
    }

}