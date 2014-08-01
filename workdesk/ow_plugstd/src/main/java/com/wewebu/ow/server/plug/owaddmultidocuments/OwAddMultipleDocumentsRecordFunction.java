package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwRecordFunction;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwStandardSearchSpecialNodeOperator;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.owutil.OwConfigUtils;
import com.wewebu.ow.server.plug.owutil.OwMappingUtils;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * View Module to edit OwObject Properties.
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
public class OwAddMultipleDocumentsRecordFunction extends OwRecordFunction implements OwDialogListener
{
    // === members
    /** last root object for history events */
    protected OwObject m_rootObject;
    /** last folder object for history events */
    protected OwObject m_folderObject;

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwAddMultipleDocumentsRecordFunction.class);

    /**list of OwPropertyInfo objects that define the visibility and writability of properties 
     * @deprecated since 4.2.0.0*/
    private List<OwPropertyInfo> m_propertyInfos;

    /** list of OwDocumentImporter objects that should be displayed as possible document source */
    private List<OwDocumentImporter> m_documentImporters;

    /**Processor for handling
     * @since 4.1.1.0*/
    private OwObjectClassProcessor processor;
    /** JSP handler reference
     * @since 3.1.0.0 */
    private OwJspFormConfigurator m_jspConfigurator;

    /**Handler for property filter and grouping configuration
     * @since 4.2.0.0*/
    private OwPropertyListConfiguration propertyListConfiguration;

    /**
     * override to get some plugin configuration tags.
     */
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        // read the properties which should be displayed while adding a new document.
        m_jspConfigurator = createOwJspFromConfigurator(node_p);
        OwXMLUtil util = getConfigNode().getSubUtil(OwPropertyListConfiguration.ELEM_ROOT_NODE);
        if (util != null)
        {
            propertyListConfiguration = new OwPropertyListConfiguration(getPluginID());
            propertyListConfiguration.build(util);
        }
        // read document importers
        m_documentImporters = new LinkedList<OwDocumentImporter>();
        if (getConfigNode().getSubUtil("DocumentImporter") != null)
        {
            Collection<?> documentImporterConfigNodes = getConfigNode().getSafeNodeList("DocumentImporter");
            Iterator<?> itDocumentImporterConfigNodes = documentImporterConfigNodes.iterator();
            while (itDocumentImporterConfigNodes.hasNext())
            {
                OwXMLUtil documentImporterConfig = new OwStandardXMLUtil((Node) itDocumentImporterConfigNodes.next());
                String className = documentImporterConfig.getSafeTextValue("ClassName", null);
                if (className != null)
                {
                    try
                    {
                        Class<?> documentImporterClass = Class.forName(className);
                        OwDocumentImporter documentImporter = (OwDocumentImporter) documentImporterClass.newInstance();
                        documentImporter.init(getContext(), documentImporterConfig);
                        m_documentImporters.add(documentImporter);
                    }
                    catch (ClassNotFoundException ex)
                    {
                        String msg = "Plugin initialization failed, Pluginid = " + getPluginID() + ". Documentimporter with classname = " + className + " not found, please check your owplugins.xml...";
                        LOG.error(msg, ex);
                        throw new OwConfigurationException(msg, ex);
                    }
                }
            }
        }
        if (m_documentImporters.size() <= 0)
        {
            throw new OwConfigurationException("There are no document importers configured for the AddMultipleDocuments Plugin. Do not know how to get the content for the new document.");
        }
        OwXMLUtil handler = getConfigNode().getSubUtil("AspectsAssociations");
        if (handler != null)
        {
            String strClazz = handler.getSafeStringAttributeValue("class", "com.wewebu.ow.server.plug.owaddmultidocuments.OwSecondaryTypesProcessor");
            if (strClazz != null)
            {
                try
                {
                    Class<?> procCls = Class.forName(strClazz);
                    processor = (OwObjectClassProcessor) procCls.newInstance();
                    processor.init(handler);
                }
                catch (ClassNotFoundException ex)
                {
                    String msg = "Plugin initialization failed, Pluginid = " + getPluginID() + ". AspectsAssociations with class = " + strClazz + " not found, please check configuration.";
                    LOG.error(msg, ex);
                    throw new OwConfigurationException(msg, ex);
                }
            }
        }
    }

    /** prevent clicking on this plugin in this version
     * @return true = do not render link to this plugin
     */
    public boolean getNoEvent()
    {
        return (false);
    }

    /** check if plugin acts as a drag and drop target
     * 
     * @return true = plugin is drag and drop target and can retrieve uploaded files via OwMainAppContext.getDragAndDropUploadDir, false otherwise
     */
    public boolean isDragDropTarget()
    {
        try
        {
            return this.getConfigNode().getSafeBooleanValue("DragDropTarget", false);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** check if plugin allows multiple files to be dropped on it
     * 
     * @return true = allow multiple files to be dropped on the plugin, false only single files may be dropped
     */
    public boolean isMultifileDragDropAllowed()
    {
        // multi file DnD only if DnDS is available in the first place
        if (!isDragDropTarget())
        {
            return (false);
        }
        // read multi file option of DragDropTarget node
        try
        {
            return (new OwStandardXMLUtil(this.getConfigNode().getSubNode("DragDropTarget"))).getSafeBooleanAttributeValue("multifile", false);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owaddmultidocuments/add_document.png");
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owaddmultidocuments/add_document_24.png");
    }

    /** check if function is enabled for the given object parameters
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *
     *  @return true = enabled, false otherwise
     */
    public boolean isEnabled(OwObject rootObject_p, OwObject folderObject_p, int iContext_p) throws Exception
    {
        if (!super.isEnabled(rootObject_p, folderObject_p, iContext_p))
        {
            return false;
        }

        // enable when parent object is set, otherwise check selected folder
        if (null == getConfigNode().getSafeTextValue("ParentObject", null))
        {
            OwResource resource = null;
            if (folderObject_p != null)
            {
                resource = folderObject_p.getResource();
            }

            boolean canCreateNewObject = getContext().getNetwork().canCreateNewObject(resource, folderObject_p, iContext_p);
            return canCreateNewObject;
        }
        else
        {
            return true;
        }
    }

    /** event called when user clicked the plugin label / icon 
     *
     *  @param rootObject_p OwObject root folder to work on
     *  @param folderObject_p OwObject selected folder to work on
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    public void onClickEvent(OwObject rootObject_p, OwObject folderObject_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(rootObject_p, folderObject_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            throw new OwInvalidOperationException(getContext().localize("plug.owaddmultidocuments.OwAddMultipleDocumentsRecordFunction.invalidobject", "It is not possible to add an object."));
        }
        // === get parent object
        OwObject parentObject = processParentObject(folderObject_p);
        // === get resource
        OwResource resource = findResource(parentObject);
        // === get defined class selection configuration
        OwObjectClassSelectionCfg classSelectionCfg = getClassSelectionCfg(getConfigNode());

        // === perform optional object class mapping
        Map objectclassmap = new HashMap();
        int iDirection = OwMappingUtils.getObjectClassMap(getConfigNode(), objectclassmap);
        classSelectionCfg = processObjectClassMapping(parentObject, classSelectionCfg, iDirection, objectclassmap);

        if (classSelectionCfg.hasDefaultClass())
        {
            String defaultClassName = classSelectionCfg.getDefaultClass().getName();
            // === verify that class is available in resource
            try
            {
                getContext().getNetwork().getObjectClass(defaultClassName, resource);
            }
            catch (OwObjectNotFoundException e)
            {
                throw new OwInvalidOperationException(getContext().localize1("plug.owaddmultidocuments.OwAddMultipleDocumentsRecordFunction.invalidobjectclass", "It is not possible to add an object because class (%1) does not exist.",
                        defaultClassName), e);
            }
        }

        // === get view properties from plugin descriptor
        int iViewMask = calculateViewMask();

        // === batch indexing
        Collection batchIndexProperties = getConfigNode().getSafeStringList("EditBatchPropertyList");

        //TODO remove strParentObjectClass and strClassName and objectClassSelectionEl 
        String strParentObjectClass = getConfigNode().getSafeTextValue("ObjectClassParent", null);
        String strClassName = getConfigNode().getSafeTextValue("ObjectClass", null);
        OwXMLUtil objectClassSelectionEl = getConfigNode().getSubUtil(OwObjectClassSelectionCfg.EL_OBJECT_CLASS_SELECTION);

        // === create new CreateMultipleDocumentsDialog
        OwCreateMultipleDocumentsDialog dlg = null;
        if (null == strClassName && null == strParentObjectClass && null != objectClassSelectionEl)
        {
            dlg = createCreateMultipleDocumentsDialog(parentObject, resource, classSelectionCfg, batchIndexProperties, m_documentImporters, iViewMask);
        }
        else
        {
            dlg = createCreateMultipleDocumentsDialog(parentObject, resource, strClassName, strParentObjectClass, batchIndexProperties, m_documentImporters, iViewMask);
        }

        dlg.setJspConfigurator(getJspConfigurator());
        dlg.setImporterBeforeBatchHandling(getConfigNode().getSafeBooleanValue("ImporterBeforeBatch", false));
        dlg.setUseImporterProposedName(getConfigNode().getSafeBooleanValue("UseProposedName", true));
        dlg.setObjectClassProcessor(getProcessor());
        m_rootObject = rootObject_p;
        m_folderObject = folderObject_p;

        if (iDirection == OwMappingUtils.AUTO_SELECT_FOLDER)
        {
            dlg.setFolderMapping(objectclassmap);
        }

        // perform optional property mapping
        dlg.setValues(OwMappingUtils.getParameterMapValuesFromRecord(getConfigNode(), rootObject_p, folderObject_p));

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());

        // set info icon
        dlg.setInfoIcon(getBigIcon());

        // set refresh callback interface
        dlg.setRefreshContext(refreshCtx_p);

        //set EditPropertyList configuration
        dlg.setPropertyListConfiguration(getPropertyListConfiguration());

        // open dialog
        getContext().openDialog(dlg, this);

        // historize
        addHistoryEvent(rootObject_p, folderObject_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);
    }

    /** 
     * overridable factory method
     * 
     * @param folderObject_p
     * @param resource_p
     * @param strClassName_p
     * @param strParentObjectClass_p
     * @param batchIndexProperties_p
     * @param documentImporters_p List of OwDocumentImporter objects
     * @param iViewMask_p
     * @return the newly created {@link OwCreateMultipleDocumentsDialog}
     * @throws Exception
     * @deprecated will be replaced by {@link #createCreateMultipleDocumentsDialog(OwObject, OwResource, OwObjectClassSelectionCfg, Collection, List, int)}
     */
    protected OwCreateMultipleDocumentsDialog createCreateMultipleDocumentsDialog(OwObject folderObject_p, OwResource resource_p, String strClassName_p, String strParentObjectClass_p, Collection batchIndexProperties_p, List documentImporters_p,
            int iViewMask_p) throws Exception
    {
        OwCreateMultipleDocumentsDialog dlg = new OwCreateMultipleDocumentsDialog(folderObject_p, resource_p, strClassName_p, strParentObjectClass_p, batchIndexProperties_p, documentImporters_p);
        dlg.setViewMask(iViewMask_p);

        return dlg;
    }

    /**(overridable)
     * Factory method for OwCreateMultipleDocumentsDialog instance
     * 
     * @param folderObject_p
     * @param resource_p
     * @param classSelectionCfg
     * @param batchIndexProperties_p
     * @param documentImporters_p
     * @param iViewMask_p
     * @return the newly created {@link OwCreateMultipleDocumentsDialog}
     * @throws Exception 
     * @since 4.1.0.0
     */
    protected OwCreateMultipleDocumentsDialog createCreateMultipleDocumentsDialog(OwObject folderObject_p, OwResource resource_p, OwObjectClassSelectionCfg classSelectionCfg, Collection batchIndexProperties_p, List documentImporters_p,
            int iViewMask_p) throws Exception
    {
        OwCreateMultipleDocumentsDialog dlg = new OwCreateMultipleDocumentsDialog(folderObject_p, resource_p, classSelectionCfg, batchIndexProperties_p, documentImporters_p);
        dlg.setViewMask(iViewMask_p);

        return dlg;
    }

    /**
     * Listener for DialogClose events used to historize SUCCESS/CANCEL/FAILURE
     * @param dialogView_p the closed dialog
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        OwCreateMultipleDocumentsDialog dlg = (OwCreateMultipleDocumentsDialog) dialogView_p;
        int status = dlg.getStatus();
        dlg.setObjectClassProcessor(null);//remove reference to current processor
        switch (status)
        {
            case OwCreateMultipleDocumentsDialog.DIALOG_STATUS_FAILED:
                addHistoryEvent(m_rootObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                break;
            case OwCreateMultipleDocumentsDialog.DIALOG_STATUS_OK:
                addHistoryEvent(m_rootObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                break;
            default:
                addHistoryEvent(m_rootObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_CANCEL);
                break;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // ignore, used for update events from documents
    }

    /**
     * Retrieve resource from virtual parent.
     * @param parent_p OwObject which is from type OwVirutalFolder
     * @return OwResource which is specified in the search template
     * @throws OwInvalidOperationException if search template have more than one object stores / repositories
     * @throws Exception if scan of search node structure is impossible
     * @since 2.5.3.0
     */
    private OwResource retrieveResourceFromParent(OwObject parent_p) throws Exception
    {
        OwStandardSearchSpecialNodeOperator scanner = new OwStandardSearchSpecialNodeOperator();
        scanner.scan(parent_p.getSearchTemplate().getSearch(Boolean.FALSE.booleanValue()));
        List objectStores = scanner.getObjectStores();
        OwMainAppContext context = getContext();
        OwNetwork network = context.getNetwork();
        Map resourcesMap = new HashMap();
        for (Iterator i = objectStores.iterator(); i.hasNext();)
        {
            OwSearchObjectStore searchStore = (OwSearchObjectStore) i.next();
            String osIdentifier = searchStore.getId();
            if (osIdentifier == null)
            {
                osIdentifier = searchStore.getName();
            }
            OwResource resource = network.getResource(osIdentifier);
            resourcesMap.put(resource.getID(), resource);
        }
        if (resourcesMap.size() == 1)
        {
            Collection resources = resourcesMap.values();
            Iterator resourcesIt = resources.iterator();
            return (OwResource) resourcesIt.next();
        }
        else
        {
            LOG.debug("OwAddMultipleDocumentsRecordFunction.retrieveResourceFromParent: Cannot execute add in none or multiple repositories/objectstores! One and only 1 objectstore must be defined by the undelaying search template!");
            throw new OwInvalidOperationException(getContext().localize("plug.owaddmultidocuments.OwAddMultipleDocumentsRecordFunction.invalidMultiResources", "The operation cannot be executed on multiple object stores/repositories."));
        }
    }

    /**(overridable)
     * Factory method for OwJspFormConfigurator object
     * @param node_p OwXMLUtil node to read configuration
     * @return OwJspFormConfigurator
     * @throws Exception if problem with instantiation of JspFormConfigurator
     * @since 3.1.0.0
     */
    protected OwJspFormConfigurator createOwJspFromConfigurator(OwXMLUtil node_p) throws Exception
    {
        return new OwJspFormConfigurator(node_p);
    }

    /**
     * Getter for OwJspFormConfigurator.
     * @return OwJspFormConfigurator
     * @since 3.1.0.0
     */
    protected OwJspFormConfigurator getJspConfigurator()
    {
        return this.m_jspConfigurator;
    }

    /**
     * Getter of restricted field definitions,
     * can be null if &lt;EditPropertyList&gt; is not defined.
     * @return List of OwPropertyInfo or null
     * @deprecated since 4.2.0.0 replaced through {@link #getPropertyListConfiguration()}
     */
    @Deprecated
    protected List<OwPropertyInfo> getPropertyInfos()
    {
        return this.m_propertyInfos;
    }

    /**
     * Getter for class processor.
     * @return OwObjectClassProcessor
     * @since 4.1.1.0
     */
    protected OwObjectClassProcessor getProcessor()
    {
        return this.processor;
    }

    /**
     * Identify object to be used as parent.
     * <p>By default read configuration &lt;ParentObject&gt; for static parent definition,
     * or return provided current parent instead.</p>
     * @param currentParent OwObject (can be null)
     * @return OwObject or null
     * @throws Exception
     * @since 4.1.1.0
     */
    protected OwObject processParentObject(OwObject currentParent) throws Exception
    {
        // === if a parent object is set, use it as parent
        String strParentObject = getConfigNode().getSafeTextValue("ParentObject", null);
        if (strParentObject != null)
        {
            return OwEcmUtil.createObjectFromString(getContext(), strParentObject);
        }
        else
        {
            return currentParent;
        }
    }

    /**
     * Find the resource which should be used.
     * <p>
     * Extract resource from provided parent if available,
     * or can return null if provide parent is null.
     * </p>
     * @param parentObject OwObject (can be null)
     * @return OwResource or null
     * @throws Exception
     * @since 4.1.1.0
     */
    protected OwResource findResource(OwObject parentObject) throws Exception
    {
        if (parentObject != null)
        {
            OwResource resource = parentObject.getResource();
            //handle resources here not in init-method
            if (resource == null && parentObject instanceof OwVirtualFolderObject)
            {
                resource = retrieveResourceFromParent(parentObject);
            }
            return resource;
        }
        else
        {
            return null;
        }
    }

    /**(overridable)
     * Configuration for object class definition.
     * @param config OwXMLUtil current configuration node
     * @return OwObjectClassSelectionCfg
     * @throws OwConfigurationException
     * @since 4.1.1.0
     */
    protected OwObjectClassSelectionCfg getClassSelectionCfg(OwXMLUtil config) throws OwConfigurationException
    {
        return OwObjectClassSelectionCfg.fromPluginXml(config);
    }

    /**(overridable)
     * Process object class mapping configuration.
     * Return an instance of OwObjectClassSelectionCfg, which should be used for creation dialog.
     * @param currentParent OwObject parent object
     * @param classSelectionCfg OwObjectClassSelectionCfg predefined object class selection
     * @param mapDirection int Constant defining mapping direction see {@link OwMappingUtils}
     * @param objectclassmap Map containing defined associations of folder/class
     * @return OwObjectClassSelection
     * @throws Exception
     * @since 4.1.1.0
     */
    protected OwObjectClassSelectionCfg processObjectClassMapping(OwObject currentParent, OwObjectClassSelectionCfg classSelectionCfg, int mapDirection, Map<String, String> objectclassmap) throws Exception
    {
        // === check for optional specified object class
        String defaultClassName = null;

        switch (mapDirection)
        {
            case OwMappingUtils.AUTO_SELECT_CLASS:
                if (classSelectionCfg.hasDefaultClass())
                {
                    String msg = "OwAddMultipleDocumentsRecordFunction.processObjectClassMapping: You can either specify ObjectClass or ObjectClassMapping, not both in plugindesriptor.";
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }

                // get the classname from folder name map
                if (currentParent != null)
                {
                    // === lookup class (either from folder name or folder class name)
                    defaultClassName = objectclassmap.get(currentParent.getName());
                    if (null == defaultClassName)
                    {
                        String folderclassname = currentParent.getObjectClass().getClassName();
                        defaultClassName = objectclassmap.get(folderclassname);
                    }
                }
                break;
            case OwMappingUtils.EMPTY_MAPPING:
            default:

        }
        if (defaultClassName == null)
        {
            return classSelectionCfg;
        }
        else
        {
            return OwObjectClassSelectionCfg.createSingleClassConfiguration(defaultClassName);
        }
    }

    /**
     * Calculation of view mask for the create dialog. 
     * @return int
     * @since 4.1.1.0
     */
    protected int calculateViewMask()
    {
        int viewMask = 0;
        viewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "EnablePasteMetadata", OwCreateMultipleDocumentsDialog.VIEW_MASK_ENABLE_PASTE_METADATA, false);
        viewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "AccessRightsView", OwCreateMultipleDocumentsDialog.VIEW_MASK_ACCESS_RIGHTS, false);
        viewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "AutoOpen", OwCreateMultipleDocumentsDialog.VIEW_MASK_AUTOOPEN, false);

        viewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "CheckInModeOption", OwCreateMultipleDocumentsDialog.VIEW_MASK_CHECKIN_MODE_OPTION, true);
        viewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "CheckInReleaseVersionOption", OwCreateMultipleDocumentsDialog.VIEW_MASK_RELEASE_VERSION_OPTION, true);
        viewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "CheckInReleaseVersionDefault", OwCreateMultipleDocumentsDialog.VIEW_MASK_RELEASE_VERSION_DEFAULT, true);

        viewMask |= OwConfigUtils.computeViewMaskFromConfig(getConfigNode(), "EnableSaveAll", OwCreateMultipleDocumentsDialog.VIEW_MASK_ENABLE_SAVE_ALL, true);
        return viewMask;
    }

    /**
     * Getter for handler defining property filter and groups.
     * @return OwProeprtyListConfiguration (or null if not defined)
     * @since 4.2.0.0
     */
    protected OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return this.propertyListConfiguration;
    }
}