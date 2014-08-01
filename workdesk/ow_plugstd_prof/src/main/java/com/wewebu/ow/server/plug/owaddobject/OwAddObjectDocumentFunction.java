package com.wewebu.ow.server.plug.owaddobject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.OwCreateObjectDialog;
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
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.owutil.OwMappingUtils;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the document function plugin, for creating a new associated object (like a task).<br/>
 * Create a new object which is associated with the initial source object.
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
 *@since 3.1.0.0
 */
public class OwAddObjectDocumentFunction extends OwDocumentFunction implements OwDialogListener
{
    private static Logger LOG = OwLog.getLogger(OwAddObjectDocumentFunction.class);

    /** last root object for history events */
    protected OwObject m_sourceObject;

    /** last folder object for history events */
    protected OwObject m_folderObject;
    /** 
     * the class of the object to be created
     * @deprecated use <ObjectClassSelection/> instead. See {@link OwObjectClassSelectionCfg}.
     */
    private String m_objectClass;
    /**
     * the parent object class, used as the root of the classes tree,
     * from where the user can select the class for the object to be created
     * @deprecated use <ObjectClassSelection/> instead. See {@link OwObjectClassSelectionCfg}.
     */
    private String m_objectClassParent;

    /**
     * @since 3.1.0.0
     */
    private OwJspFormConfigurator m_jspConfigurator;

    /** config parent object*/
    private OwObject m_configParentObject;

    /**
     * Processor for OwObjectClass (for feature such as Secondary types)
     * @since 4.1.1.0
     */
    private OwObjectClassProcessor objectClassProcessor;

    /**
     * Flag to indicate if source object should be handled as parent.
     * @since 4.1.1.0
     */
    private boolean useSourceAsParent;
    /** Handler for the EditPropertyList configuration node
     * @since 4.2.0.0 */
    private OwPropertyListConfiguration propertyListConfiguration;

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        // get predefined object class name from config node
        m_objectClass = node_p.getSafeTextValue("ObjectClass", null);

        // get parent object class from where the user can select, only useful if classes are structured in a tree
        m_objectClassParent = node_p.getSafeTextValue("ObjectClassParent", null);

        // configure jsp page
        m_jspConfigurator = new OwJspFormConfigurator(node_p);

        String parent = node_p.getSafeTextValue("ParentObject", null);
        if (parent != null)
        {
            try
            {
                m_configParentObject = getContext().getNetwork().getObjectFromPath(parent, true);
            }
            catch (OwObjectNotFoundException ex)
            {
                LOG.error("Invalide path definition for parent object. Path = " + parent, ex);
                throw new OwConfigurationException(getContext().localize("OwAddObjectDocumentFunction.invalid.parentObject", "Defined parent configuration for AddObjectDocumentFunction is incorrect"));
            }
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
                    objectClassProcessor = (OwObjectClassProcessor) procCls.newInstance();
                    objectClassProcessor.init(handler);
                }
                catch (ClassNotFoundException ex)
                {
                    String msg = "Plugin initialization failed, Pluginid = " + getPluginID() + ". AspectsAssociations with class = " + strClazz + " not found, please check configuration.";
                    LOG.error(msg, ex);
                    throw new OwConfigurationException(msg, ex);
                }
            }
        }

        useSourceAsParent = getConfigNode().getSafeBooleanValue("UseSourceAsParent", false);

        OwXMLUtil util = getConfigNode().getSubUtil(OwPropertyListConfiguration.ELEM_ROOT_NODE);
        if (util != null)
        {
            this.propertyListConfiguration = new OwPropertyListConfiguration(getPluginID());
            this.propertyListConfiguration.build(util);
        }
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owaddobject/add_object.png");
    }

    /**
     *  Get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owaddobject/add_object_24.png");
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
        OwObject contaimentObject;
        if (useSourceAsParent())
        {
            contaimentObject = rootObject_p;
        }
        else
        {
            try
            {
                contaimentObject = getContainmentFolder(rootObject_p, folderObject_p);
            }
            catch (OwException e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Failed to retrieve containment folder.", e);
                }
                return false;
            }
        }
        boolean result = super.isEnabled(rootObject_p, contaimentObject, iContext_p);
        if (result)
        {
            if (contaimentObject == null)
            {
                result = false;
            }
            else
            {
                OwResource resource = contaimentObject.getResource();
                result = getContext().getNetwork().canCreateNewObject(resource, contaimentObject, iContext_p);
            }
        }
        return result;
    }

    /** 
     *  Event called when user clicked the plugin label / icon 
     *  @param sourceObj_p OwObject root folder to work on
     *  @param parentObject_p OwObject selected folder to work on
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     *
     */
    @SuppressWarnings("rawtypes")
    public void onClickEvent(OwObject sourceObj_p, OwObject parentObject_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (!isEnabled(sourceObj_p, parentObject_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwAddObjectDocumentFunction.onClickEvent: The document function is not enabled for this object.");
            }
            throw new OwInvalidOperationException(getContext().localize("plug.owaddobject.OwAddObjectDocumentFunction.invalidobject", "This document function is not enabled for this object."));
        }

        m_sourceObject = sourceObj_p;
        m_folderObject = useSourceAsParent() ? sourceObj_p : getContainmentFolder(sourceObj_p, parentObject_p);

        OwObjectClassSelectionCfg classSelectionCfg = OwObjectClassSelectionCfg.fromPluginXml(getConfigNode());

        // === check for optional specified object class
        String defaultClassName = null;
        if (classSelectionCfg.hasDefaultClass())
        {
            defaultClassName = classSelectionCfg.getDefaultClass().getName();
        }

        if (defaultClassName != null)
        {
            // === verify that class is available in resource
            try
            {
                OwResource resource = m_folderObject.getResource();
                if (resource == null && m_folderObject instanceof OwVirtualFolderObject)
                {
                    resource = retrieveResourceFromParent(m_folderObject);
                }
                getContext().getNetwork().getObjectClass(defaultClassName, resource);
            }
            catch (OwObjectNotFoundException e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Invalid object class: " + e);
                }
                throw new OwInvalidOperationException(getContext().localize("plug.owaddobject.OwAddObjectDocumentFunction.invalidobjectclass", "Invalid object class."), e);
            }
        }

        //TODO remove objectClassSelectionEl 
        OwXMLUtil objectClassSelectionEl = getConfigNode().getSubUtil(OwObjectClassSelectionCfg.EL_OBJECT_CLASS_SELECTION);

        OwCreateObjectDialog dlg = null;
        if (null == m_objectClass && null == m_objectClassParent && null != objectClassSelectionEl)
        {
            dlg = createCreateObjectDialog(m_sourceObject, m_folderObject, classSelectionCfg, false);
        }
        else
        {
            dlg = createCreateObjectDialog(m_sourceObject, m_folderObject, m_objectClass, m_objectClassParent, false);
        }
        //ObjectClassProcessor
        dlg.setObjectClassProcessor(getObjectClassProcessor());

        dlg.setConfigNode(getConfigNode());

        dlg.setJspConfigurator(m_jspConfigurator);

        // set the property list configuration
        dlg.setPropertyListConfiguration(getPropertyListConfiguration());

        // perform property mapping
        Map presetProperties = OwMappingUtils.getParameterMapValuesFromRecord(getConfigNode(), sourceObj_p, parentObject_p);
        Map valuesFromDocument = OwMappingUtils.getParameterMapValuesFromObject(getConfigNode(), sourceObj_p, "DocumentParameterMapping");
        //merge properties, document properties override the folder properties
        if (presetProperties == null)
        {
            presetProperties = new HashMap();
        }
        if (valuesFromDocument != null)
        {
            presetProperties.putAll(valuesFromDocument);
        }
        //DocumentParameterMapping
        dlg.setValues(presetProperties);

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());

        // set info icon
        dlg.setInfoIcon(getBigIcon());

        // set title
        dlg.setTitle(getDefaultLabel());

        // set refresh context
        dlg.setRefreshContext(refreshCtx_p);

        // open dialog
        getContext().openDialog(dlg, null);

        // historize
        addHistoryEvent(sourceObj_p, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);
    }

    /** overridable factory method
     * 
     * @param folderObject_p 
     * @param strClassName_p
     * @param strObjectClassParent_p
     * @param openObj_p 
     * @return OwCreateObjectDialog
     * @throws Exception
     * @deprecated replaced by {@link #createCreateObjectDialog(OwObject, OwObject, OwObjectClassSelectionCfg, boolean)}
     */
    protected OwCreateObjectDialog createCreateObjectDialog(OwObject sourceObject_p, OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean openObj_p) throws Exception
    {
        return new OwCreateObjectDialog(folderObject_p, strClassName_p, strObjectClassParent_p, openObj_p);
    }

    /** overridable factory method
     * 
     * @param folderObject_p 
     * @param classSelectionCfg
     * @param openObj_p 
     * @return OwCreateObjectDialog
     * @throws Exception
     */
    protected OwCreateObjectDialog createCreateObjectDialog(OwObject sourceObject_p, OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean openObj_p) throws Exception
    {
        return new OwCreateObjectDialog(folderObject_p, classSelectionCfg, openObj_p);
    }

    /**
     * Listener for DialogClose events used to historize SUCCESS/CANCEL/FAILURE
     * @param dialogView_p the closed dialog
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        int status = ((OwCreateObjectDialog) dialogView_p).getStatus();
        switch (status)
        {
            case OwCreateObjectDialog.DIALOG_STATUS_FAILED:
                addHistoryEvent(m_sourceObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                break;
            case OwCreateObjectDialog.DIALOG_STATUS_OK:
                addHistoryEvent(m_sourceObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                break;
            default:
                addHistoryEvent(m_sourceObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_CANCEL);
                break;
        }
    }

    /**
     * Get containment folder. If no <code>ParentFolder</code> is set in the configuration, the
     * folder where the new object will be created will be the physical folder of the related object.
     * @param documentObj_p - the source object
     * @param objCurrentParent_p
     * @return the {@link OwObject} 
     * @throws Exception
     */
    protected OwObject getContainmentFolder(OwObject documentObj_p, OwObject objCurrentParent_p) throws Exception
    {
        if (m_configParentObject != null)
        {
            return m_configParentObject;
        }
        else
        {
            if (documentObj_p.getParents() != null)
            {
                return (OwObject) documentObj_p.getParents().get(0);
            }
            else
            {
                return objCurrentParent_p;
            }
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
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
            LOG.debug("OwAddObjectDocumentFunction.retrieveResourceFromParent: Cannot execute add object in none or multiple repositories/objectstores! One and only 1 objectstore must be defined by the undelaying search template!");
            throw new OwInvalidOperationException(getContext().localize("plug.owaddobject.OwAddObjectDocumentFunction.invalidMultiResources",
                    "Cannot execute \"add object\" operation in none or multiple repositories/object stores! One and only one object store must be defined by the underlying search template!"));
        }
    }

    /**
     * Return current OwObjectClassProcessor or null if none
     * was defined.
     * @return OwObjectClassProcessor (or null)
     * @since 4.1.1.0
     */
    protected OwObjectClassProcessor getObjectClassProcessor()
    {
        return this.objectClassProcessor;
    }

    /**
     * Flag to mark if source object should be used as parent.
     * If this return true, all other configuration will be ignored.  
     * @return boolean
     * @since 4.1.1.0
     */
    protected boolean useSourceAsParent()
    {
        return this.useSourceAsParent;
    }

    /**
     * Get the configured EditPropertyList restriction definition
     * @return OwPropertyListConfiguration (null if not configured)
     * @since 4.2.0.0
     */
    protected OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return this.propertyListConfiguration;
    }
}