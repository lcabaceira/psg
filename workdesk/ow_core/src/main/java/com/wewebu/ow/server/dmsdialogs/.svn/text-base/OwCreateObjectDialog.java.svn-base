package com.wewebu.ow.server.dmsdialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwMultipanelAccessRightsView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwRootClassCfg;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.ecmimpl.OwSimpleLocation;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwConfigurableDialog;
import com.wewebu.ow.server.ui.OwDelegateView;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwJspConfigurable;
import com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Dialog for adding records or single folders to a folder.
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
public class OwCreateObjectDialog extends OwStandardDialog implements OwObjectClassView.OwObjectClassViewListner, OwConfigurableDialog, OwJspConfigurable
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwCreateObjectDialog.class);

    /** layout to be used for the dialog */
    protected OwSubLayout m_Layout;

    /** the new folder object */
    protected OwObjectSkeleton m_sceletonObject;

    protected OwObjectClassView m_classView;

    /** navigation view to navigate through the subviews */
    protected OwSubNavigationView m_SubNavigation;

    /** the property view */
    protected OwPropertyViewBridge propertyViewBridge;

    /** DMSID of the newly created object */
    private String m_strDmsID;

    /** selected object of record */
    protected OwObject m_folderObject;

    /** the initial class
     * @deprecated will be replaced by {@link #classSelectionCfg} 
     */
    protected String m_strObjectClass;

    /** 
     * parent object class from where the user can select, only useful if classes are structured in a tree
     * @deprecated will be replaced by {@link #classSelectionCfg} 
     */
    protected String m_strObjectClassParent;

    /** class to use for new folder */
    protected OwObjectClass m_folderClass;

    /** refresh context for callback */
    protected OwClientRefreshContext m_RefreshCtx;

    /** open with new record */
    protected boolean m_fOpenObject;

    /** a set of properties that should be set as default for the new object */
    protected Map m_ValuesMap;

    /** Collection of OwObjectPropertyView.OwPropertyInfo objects */
    protected Collection m_propertyInfos;

    /** status of this dialog: nothing done yet */
    public static final int DIALOG_STATUS_NONE = 0;
    /** status of this dialog: checkin successful*/
    public static final int DIALOG_STATUS_OK = 1;
    /** status of this dialog: checkin failed */
    public static final int DIALOG_STATUS_FAILED = 2;

    /** status of this dialog needed for historization */
    protected int m_dialogStatus = DIALOG_STATUS_NONE;

    /**Handler for multiple JSP form configuration
     * @since 3.1.0.0*/
    private OwJspFormConfigurator m_jspFormConfigurator;

    /**Delegation View for JSP and default rendering of properties view
     * @since 3.1.0.0*/
    protected OwDelegateView delegateView;

    /**Tab where the properties view is contained
     *@since 3.1.0.0*/
    protected OwTabInfo propertiesTab;

    /** class name */
    protected String m_objectClassName = null;

    /**Access Rights view for access rights handling
     * @since 3.1.0.0*/
    protected OwMultipanelAccessRightsView accessRightsView;

    /** the configuration node
     *  @since 3.1.0.0*/
    protected OwXMLUtil m_configNode;

    private OwObjectClassSelectionCfg classSelectionCfg;

    /**
     * OwObjectClassProcessor
     * @since 4.1.1.0
     */
    private OwObjectClassProcessor objectClassProcessor;

    /**Handler for EditPropertyList configuration
     * @since 4.2.0.0 */
    private OwPropertyListConfiguration propertyListConfiguration;

    /** create a record / folder create dialog
    *
    * @param folderObject_p OwObject parent folder to add to
    * @param strClassName_p class name to use for new folder, null = let user select a class
    * @param strObjectClassParent_p String parent class to let user browse
    * @param fOpenObject_p boolean open the new folder or false if folder should not be opened
    * @deprecated will be replaced by {@link #OwCreateObjectDialog(OwObject, OwObjectClassSelectionCfg, boolean)}
    */
    public OwCreateObjectDialog(OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean fOpenObject_p)
    {
        m_folderObject = folderObject_p;
        m_strObjectClass = strClassName_p;
        m_fOpenObject = fOpenObject_p;
        m_strObjectClassParent = strObjectClassParent_p;

        // add view to the document
        setDocument(createDocument());
    }

    /** create a record / folder create dialog
     *
     * @param folderObject_p OwObject parent folder to add to
     * @param classSelectionCfg
     * @param fOpenObject_p boolean open the new folder or false if folder should not be opened
     * @since 4.1.0.0
     */
    public OwCreateObjectDialog(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean fOpenObject_p)
    {
        m_folderObject = folderObject_p;
        this.classSelectionCfg = classSelectionCfg;
        m_fOpenObject = fOpenObject_p;

        // add view to the document
        setDocument(createDocument());
    }

    /** 
     * Factory method for document creation
     * @return the {@link OwDocument} object
     * @since 3.1.0.0
     */
    protected OwDocument createDocument()
    {
        return new OwDocument();
    }

    /**
     * get the current status of this dialog
     * @return the status
     */
    public int getStatus()
    {
        return (m_dialogStatus);
    }

    /** submit a set of values that should be set as default for the new object
     *
     * @param properties_p Map of values keyed by parameter names to be set initially
     */
    public void setValues(Map properties_p)
    {
        m_ValuesMap = properties_p;
    }

    /** set a OwPropertyInfo list defining the visibility and writability of property values
     * that is passed on to the PropertyView.
     * 
     * @param propertyInfos_p list of OwPropertyInfo objects that are passed to the PropertyView
     * 
     * @since 2.5.3.0
     * @deprecated since 4.2.0.0 use {@link #setPropertyListConfiguration(OwPropertyListConfiguration)} instead
     */
    public void setPropertyInfos(List propertyInfos_p)
    {
        m_propertyInfos = propertyInfos_p;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === set document
        // register as event target
        getDocument().attach(getContext(), null);

        // === no class submitted, let user select, also use navigation view
        // === attached layout
        m_Layout = new OwSubLayout();
        addView(m_Layout, MAIN_REGION, null);

        // === navigation 
        m_SubNavigation = new OwSubNavigationView();
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // enable validation of panels
        m_SubNavigation.setValidatePanels(true);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);
        delegateView = createDelegateView();

        //TODO remove this branch
        if (null == this.classSelectionCfg)
        {
            if (m_strObjectClass == null)
            {
                // === add document class view
                m_classView = new OwObjectClassView(m_folderObject.getResource(), OwObjectReference.OBJECT_TYPE_FOLDER, m_strObjectClassParent);
                if (getObjectClassProcessor() != null)
                {
                    m_classView.setObjectClassProcessor(getObjectClassProcessor());
                }
                // attach view to navigation
                m_SubNavigation.addView(m_classView, getContext().localize("owaddrecord.OwCreateRecordDialog.class_title", "Choose class"), null, getContext().getDesignURL() + "/images/plug/owdocprops/class.png", null, null);

                m_classView.setSelectedItemStyle("OwSaveDlgObjectClassTextSelected");
                m_classView.setItemStyle("OwSaveDlgObjectClassText");
                m_classView.setEventListner(this);
            }
        }
        else
        {
            if (!this.classSelectionCfg.hasDefaultClass())
            {
                // === add document class view
                m_classView = new OwObjectClassView(m_folderObject.getResource(), OwObjectReference.OBJECT_TYPE_FOLDER, this.classSelectionCfg);
                if (getObjectClassProcessor() != null)
                {
                    m_classView.setObjectClassProcessor(getObjectClassProcessor());
                }

                // attach view to navigation
                m_SubNavigation.addView(m_classView, getContext().localize("owaddrecord.OwCreateRecordDialog.class_title", "Choose class"), null, getContext().getDesignURL() + "/images/plug/owdocprops/class.png", null, null);

                m_classView.setSelectedItemStyle("OwSaveDlgObjectClassTextSelected");
                m_classView.setItemStyle("OwSaveDlgObjectClassText");
                m_classView.setEventListner(this);
            }
        }

        if (getConfigNode().getSafeBooleanValue("AccessRightsView", false))
        {
            accessRightsView = createAccessRightsView();
            accessRightsView.setReadOnly(getConfigNode().getSubUtil("AccessRightsView").getSafeBooleanAttributeValue("readonly", false));
            m_SubNavigation.addView(accessRightsView, getContext().localize("owaddrecord.OwCreateRecordDialog.AccessRights_Title", "Access Rights"), null, getContext().getDesignURL() + "/images/plug/owdocprops/accessrights.png", null, null);
            accessRightsView.setNextActivateView(this.delegateView);
        }

        //  === create properties view
        this.propertyViewBridge = createPropertyViewBridge();

        // attach view to layout
        int propTab = m_SubNavigation.addView(this.delegateView, getContext().localize("owaddrecord.OwCreateRecordDialog.properties_title", "Properties"), null, getContext().getDesignURL() + "/images/plug/owdocprops/properties.png", null, null);
        propertiesTab = (OwTabInfo) m_SubNavigation.getTabList().get(propTab);
        //create the order in which the tabs are processed.
        initTabOrder();
        this.propertyViewBridge.setReadOnlyContext(OwPropertyClass.CONTEXT_ON_CREATE);

        if (null == this.classSelectionCfg)
        {
            if (m_strObjectClass != null)
            {
                m_folderClass = ((OwMainAppContext) getContext()).getNetwork().getObjectClass(m_strObjectClass, m_folderObject.getResource());
                updateObjectClass();
            }
        }
        else
        {
            if (this.classSelectionCfg.hasDefaultClass())
            {
                OwRootClassCfg defaultRootClassCfg = this.classSelectionCfg.getDefaultClass();
                m_folderClass = ((OwMainAppContext) getContext()).getNetwork().getObjectClass(defaultRootClassCfg.getName(), m_folderObject.getResource());
                updateObjectClass();
            }
        }

        // === activate the first view
        m_SubNavigation.navigate(0);
    }

    /**(overridable)
     * Called during init() method to assign
     * the view order, in which the views should be processed.
     * <p>Overriding this method allow to change the order
     * in which the views would appear, and also gives the
     * possibility to create additional views.</p>
     * @throws Exception
     * @since 3.1.0.0
     */
    protected void initTabOrder() throws Exception
    {
        if (null != m_classView)
        {
            if (accessRightsView == null)
            {
                m_classView.setNextActivateView(this.delegateView);
            }
            else
            {
                m_classView.setNextActivateView(accessRightsView);
                accessRightsView.setPrevActivateView(m_classView);
            }
        }

        if (accessRightsView != null)
        {
            propertiesTab.setDisabled(!accessRightsView.isValidated());
        }
    }

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
                    bridge = createFormViewBridge();
                }
            }
        }

        if (bridge == null)
        {
            bridge = createStandardViewBridge();
        }

        bridge.setReadOnlyContext(OwPropertyClass.CONTEXT_ON_CREATE);

        bridge.setPropertyListConfiguration(getPropertyListConfiguration());

        return bridge;
    }

    /**(overridable)
     * Create a property view bridge with form rendering.
     * @return OwFormPropertViewBridge
     * @see #getJspConfigurator()
     * @since 3.1.0.0
     */
    protected OwFormPropertyViewBridge createFormViewBridge()
    {
        OwObjectPropertyFormularView view = new OwObjectPropertyFormularView();
        view.setJspConfigurator(getJspConfigurator());
        return new OwFormPropertyViewBridge(view);
    }

    /**(overridable)
     * Create a standard property view bridge, for standard rendering.
     * @return OwStandardPropertyViewBridge
     * @throws Exception if cannot create object property view
     * @see #createObjectPropertyView()
     * @since 3.1.0.0
     */
    protected OwStandardPropertyViewBridge createStandardViewBridge() throws Exception
    {
        OwStandardPropertyViewBridge bridge = new OwStandardPropertyViewBridge(createObjectPropertyView());

        return bridge;
    }

    /** overridable to create properties view
     * 
     * @return {@link OwObjectPropertyView}
     * @since 2.5.2.0
     */
    protected OwObjectPropertyView createObjectPropertyView() throws Exception
    {
        return new OwObjectPropertyView();
    }

    /** remove view and all subviews from context */
    public void detach()
    {
        super.detach();

        // detach document
        getDocument().detach();
        if (getPropertyListConfiguration() != null)
        {
            this.propertyListConfiguration = null;
        }
    }

    /** get the newly created object */
    public OwObject getNewObject() throws Exception
    {
        if (null == m_strDmsID)
        {
            return null;
        }
        else
        {
            return ((OwMainAppContext) getContext()).getNetwork().getObjectFromDMSID(m_strDmsID, false);
        }
    }

    /** called by the framework to update the view when OwDocument.Update was called
     *
     *  NOTE:   We can not use the onRender method to update,
     *          because we do not know the call order of onRender.
     *          onUpdate is always called before all onRender methods.
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        //////////////////////////////////////////////////////
        // NOTE:    Records structures are not created here, they are created in the ECM Adapter.
        //          Here we only select a class and create it.
        //          If the class represents just a folder or a whole structure is up to the Adapter.

        switch (iCode_p)
        {
            case OwUpdateCodes.UPDATE_OBJECT_PROPERTY:
            {
                // save was pressed on the property view, now we can create the folder
                m_strDmsID = create();
                // close the dialog
                closeDialog();

                // open the newly created record
                OwObject recordObject = getNewObject();

                if (m_fOpenObject)
                {
                    OwMasterDocument recordPlugin = OwMimeManager.getHandlerMasterPlugin((OwMainAppContext) getContext(), recordObject);

                    if (recordPlugin == null)
                    {
                        String msg = "OwCreateObjectDialog.onUpdate: Recordplugin Id must be specified if record is created.";
                        LOG.fatal(msg);
                        throw new OwConfigurationException(msg);
                    }

                    recordPlugin.dispatch(OwDispatchCodes.OPEN_OBJECT, recordObject, null);
                }

                // refresh the context
                if (m_RefreshCtx != null)
                {
                    m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_FOLDER_CHILDS, recordObject);
                    m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, recordObject);
                    m_RefreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, recordObject);
                }
            }
                break;
        }
    }

    /** set a plugin refresh callback interface
     *
     * @param pluginRefreshCtx_p OwClientRefreshContext
     */
    public void setRefreshContext(OwClientRefreshContext pluginRefreshCtx_p)
    {
        m_RefreshCtx = pluginRefreshCtx_p;
    }

    /** create the folder / record 
     * @return String DMSID of new object 
     */
    protected String create() throws Exception
    {
        // filter out read-only, hidden and null properties
        OwPropertyCollection docStandardPropertiesMap = m_sceletonObject.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        // fetch DocType
        String docType = m_sceletonObject.getObjectClass().getClassName();
        OwPermissionCollection permCol = null;
        if (getAccessRightsView() != null)
        {//get Permission Collection only if the AccessRightsView is enabled
            permCol = m_sceletonObject.getPermissions();
        }
        // === create a new object
        String newDmsid = null;
        try
        {
            newDmsid = ((OwMainAppContext) getContext()).getNetwork().createNewObject(m_folderObject.getResource(), docType, docStandardPropertiesMap, permCol, null, m_folderObject, null, null);
            // flag success
            m_dialogStatus = DIALOG_STATUS_OK;
            // return DMSID 
            return newDmsid;
        }
        catch (Exception e)
        {
            // flag failure
            m_dialogStatus = DIALOG_STATUS_FAILED;
            // re-throw exception
            throw e;
        }
    }

    /** the object class for the folder was changed, update the skeleton
     */
    protected void updateObjectClass() throws Exception
    {
        OwRoleManagerContext ctx = getContext().getRegisteredInterface(OwRoleManagerContext.class);
        if (getObjectClassProcessor() != null)
        {
            OwSimpleLocation location = new OwSimpleLocation(m_folderObject);
            OwObjectClass cls = getObjectClassProcessor().process(m_folderClass, location, getContext());
            m_sceletonObject = ctx.getNetwork().createObjectSkeleton(cls, m_folderObject.getResource());
        }
        else
        {
            m_sceletonObject = ctx.getNetwork().createObjectSkeleton(m_folderClass, m_folderObject.getResource());
        }
        // special case for parent virtual folders
        if (m_folderObject instanceof OwVirtualFolderObject)
        {
            OwVirtualFolderObject vfObject = (OwVirtualFolderObject) m_folderObject;
            vfObject.setFiledObjectProperties(m_folderClass, m_sceletonObject.getProperties(null));
            // create m_propertyInfos
            if (m_propertyInfos == null)
            {
                m_propertyInfos = new ArrayList();
            }
            OwPropertyCollection tempFilledProperties = new OwStandardPropertyCollection();
            vfObject.setFiledObjectProperties(m_folderClass, tempFilledProperties);
            Iterator allPropertiesIterator = m_sceletonObject.getProperties(null).keySet().iterator();
            while (allPropertiesIterator.hasNext())
            {
                String propName = (String) allPropertiesIterator.next();
                m_propertyInfos.add(new OwPropertyInfo(propName, tempFilledProperties.containsKey(propName)));
            }
        }

        //create view bridge and set view
        //get object class name
        m_objectClassName = m_sceletonObject.getClassName();

        this.propertyViewBridge = createPropertyViewBridge();

        this.delegateView.setView(this.propertyViewBridge.getView());

        // Merge with m_ValuesMap
        if (null != m_ValuesMap)
        {
            Iterator it = m_ValuesMap.keySet().iterator();
            while (it.hasNext())
            {
                String strPropName = (String) it.next();

                try
                {
                    OwProperty skeletonProp = m_sceletonObject.getProperty(strPropName);

                    skeletonProp.setValue(m_ValuesMap.get(strPropName));
                }
                catch (OwObjectNotFoundException e)
                {
                    // Ignore
                }
            }
        }

        this.propertyViewBridge.setObjectRef(m_sceletonObject, false);

        if (getAccessRightsView() != null)
        {
            getAccessRightsView().setObjectRef(m_sceletonObject);
        }
        propertiesTab.setDisabled(m_folderClass == null);
    }

    /** event called when user selects a class
     * @param classDescription_p OwObjectClass
     * @param strPath_p String path to selected tree item
     */
    public void onObjectClassViewSelectClass(OwObjectClass classDescription_p, String strPath_p) throws Exception
    {
        m_folderClass = classDescription_p;
        updateObjectClass();
    }

    public OwCreateObjectDialog createFormDialog()
    {
        if (null != m_strObjectClass || null != m_strObjectClassParent)
        {
            return new OwCreateObjectWithFormDialog(m_folderObject, m_strObjectClass, m_strObjectClassParent, m_fOpenObject, m_jspFormConfigurator);
        }
        else
        {
            return new OwCreateObjectWithFormDialog(m_folderObject, this.classSelectionCfg, m_fOpenObject, m_jspFormConfigurator);
        }
    }

    public void setConfigNode(OwXMLUtil configNode_p)
    {
        m_configNode = configNode_p;
    }

    public void setJspConfigurator(OwJspFormConfigurator jspFormConfigurator_p)
    {
        m_jspFormConfigurator = jspFormConfigurator_p;
    }

    public OwXMLUtil getConfigNode()
    {
        return m_configNode;
    }

    public OwJspFormConfigurator getJspConfigurator()
    {
        return m_jspFormConfigurator;
    }

    /** (overridable)
     * Factory for AccessRightsView, can be over written 
     * to initialize dialog with own access rights representation. 
     * @return OwMultipanelAccessRightsView
     * @since 3.1.0.0
     */
    protected OwMultipanelAccessRightsView createAccessRightsView()
    {
        return new OwMultipanelAccessRightsView();
    }

    /**
     * Get method to retrieve the access rights view,
     * which is responsible for rendering.
     * @return OwMultipanelAccessRightsView
     * @since 3.1.0.0
     */
    protected OwMultipanelAccessRightsView getAccessRightsView()
    {
        return accessRightsView;
    }

    /**(overridable)
     * Create the instance which is used for delegation of
     * Property View rendering.
     * @return OwDelegateView
     * @since 3.1.0.0
     */
    protected OwDelegateView createDelegateView()
    {
        return new OwDelegateView();
    }

    /**
     * Set/Provide an OwObjectClassProcessor.
     * @param objProc OwObjectClassProcessor
     * @since 4.1.1.0
     */
    public void setObjectClassProcessor(OwObjectClassProcessor objProc)
    {
        this.objectClassProcessor = objProc;
    }

    /**
     * Get ObjectClassProcessor for handling of skeleton creation.<br />
     * Can return null if no processor was defined/configured.
     * @return OwObjectClassProcessor or null
     * @since 4.1.1.0
     */
    public OwObjectClassProcessor getObjectClassProcessor()
    {
        return this.objectClassProcessor;
    }

    /**
     * Get currently defined PropertyList configuration
     * @return OwPropertyListConfiguration or null
     * @since 4.2.0.0
     */
    public OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return propertyListConfiguration;
    }

    /**
     * Set PropertyList Configuration
     * @param propertyListConfiguration OwPropertyListConfiguration (can be null)
     * @since 4.2.0.0
     */
    public void setPropertyListConfiguration(OwPropertyListConfiguration propertyListConfiguration)
    {
        this.propertyListConfiguration = propertyListConfiguration;
    }
}