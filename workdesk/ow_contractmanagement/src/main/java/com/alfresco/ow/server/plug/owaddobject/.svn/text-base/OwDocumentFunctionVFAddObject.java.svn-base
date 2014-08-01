package com.alfresco.ow.server.plug.owaddobject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.alfresco.ow.contractmanagement.virtualfolder.OwParentFolderResolver;
import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.OwCreateObjectDialog;
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
import com.wewebu.ow.server.plug.owaddobject.OwAddObjectDocumentFunction;
import com.wewebu.ow.server.plug.owutil.OwMappingUtils;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwDocumentFunctionVFAddObject<br/>
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
public class OwDocumentFunctionVFAddObject extends OwAddObjectDocumentFunction
{

    /** Logger for this class */
    private static final Logger LOG = OwLog.getLogger(OwDocumentFunctionVFAddObject.class);

    /**
     * Determines which should be used as parent folder.
     * Could be a fixed defined folder path (e. g /Main Repository/MyFolder/TheFolderUsesAsParent).<br>
     * Also following special parameters could be used:
     * <ul>
     * <li>UseSingleRoot:<br>
     *      Use the common root folder of all selected object. If more than one root folder exists an exception will be thrown.
     *      If a single object has more then one parent an exception will be thrown.
     *      If a object has no parent folder an exception will be thrown.
     * </li>
     * <li>VirtualFolderSearchPath:<br>
     *      Physical root folder of an semi virtual folder.
     *      Uses the defined VirtualFolderProperty OwSearchPath of semi virtual folder SemiVirtualRecordClass.
     * </li>
     * </ul>
     */
    public static final String PLUGIN_PARAM_USED_PARENT_FOLDER = "ParentFolder";

    /**
     * Document Function only available for the defined mime types with attributes list <mime type="application/msword" />.
     * Remember that also Workdesk specific mime types could be used 
     * e. g. ow_default/OBJECT_TYPE_FOLDER or ow_folder/F:cmg:contract
     */
    public static final String PLUGIN_PARAM_OPBJECT_MIME_TYPES = "ObjectMimeTypes";

    private OwParentFolderResolver rootFolderResolver;

    private OwCreateObjectDialog dialog = null;
    private OwClientRefreshContext refreshCtx = null;
    private OwObject refreshObject = null;
    private List<String> mimeTypeList = null;

    private OwJspFormConfigurator m_jspConfigurator;

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owaddobject.OwAddObjectDocumentFunction#init(com.wewebu.ow.server.util.OwXMLUtil, com.wewebu.ow.server.app.OwMainAppContext)
     */
    @Override
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);

        // configure jsp page
        m_jspConfigurator = new OwJspFormConfigurator(node_p);

        rootFolderResolver = new OwParentFolderResolver();
        rootFolderResolver.init(node_p.getSafeTextValue(PLUGIN_PARAM_USED_PARENT_FOLDER, ""), context_p);

        mimeTypeList = new ArrayList<String>();
        List mimeList = node_p.getSafeUtilList(PLUGIN_PARAM_OPBJECT_MIME_TYPES, "mime");
        for (Iterator it = mimeList.iterator(); it.hasNext();)
        {
            OwXMLUtil mimeUtil = (OwXMLUtil) it.next();
            String mimeType = mimeUtil.getSafeStringAttributeValue("type", "");
            if (!mimeType.isEmpty())
            {
                mimeTypeList.add(mimeType);
            }
        }
    }

    @Override
    protected OwCreateObjectDialog createCreateObjectDialog(OwObject sourceObject_p, OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean fOpenObject_p) throws Exception
    {
        //FIXME why do we need a DialogHandler configuration?
        OwXMLUtil dialogHandler = getConfigNode().getSubUtil("DialogHandler");
        if (dialogHandler != null)
        {
            String dialogClassName = dialogHandler.getSafeStringAttributeValue("className", null);
            if (dialogClassName == null)
            {
                LOG.error("OwDocumentFunctionVFAddObject.createCreateObjectDialog(): No className attribute configured for DialogHandler.");
                throw new OwConfigurationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.classNameNotFound", "No class Name attribute configured for DialogHandler."));
            }
            try
            {
                Class<?> dialogClass = Class.forName(dialogClassName);
                Constructor<?> constructor = dialogClass.getConstructor(OwObject.class, OwObjectClassSelectionCfg.class, boolean.class);
                dialog = (OwCreateObjectDialog) constructor.newInstance(folderObject_p, classSelectionCfg, fOpenObject_p);
            }
            catch (Exception e)
            {
                LOG.error("OwDocumentFunctionVFAddObject.createCreateObjectDialog(): Cannot create dialog.", e);
                throw new OwConfigurationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.cannotCreateDialog", "Cannot create the ObjectCreateDialog instance."), e);
            }
        }
        else
        {
            dialog = new OwCreateObjectDialog(folderObject_p, classSelectionCfg, fOpenObject_p);
        }
        return dialog;
    }

    @Override
    public boolean getNeedParent()
    {
        if (rootFolderResolver.isExplicitDefinedRootFolder())
        {
            return false;
        }

        return super.getNeedParent();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isEnabled(Collection objects_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        for (Iterator it = objects_p.iterator(); it.hasNext();)
        {
            OwObject obj = (OwObject) it.next();
            if (!isMimeTypeEnabled(obj.getMIMEType()))
            {
                return false;
            }
        }
        OwObject resolvedSource = rootFolderResolver.getRootFolder(objects_p, oParent_p);
        if (resolvedSource == null || resolvedSource.equals(oParent_p))
        {
            return super.isEnabled(objects_p, oParent_p, iContext_p);
        }

        return super.isEnabled(resolvedSource, oParent_p, iContext_p);
    }

    @Override
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        if (!isMimeTypeEnabled(oObject_p.getMIMEType()))
        {
            return false;
        }
        OwObject resolvedSource = rootFolderResolver.getRootFolder(oObject_p, oParent_p);

        if (resolvedSource == null || resolvedSource.equals(oParent_p))
        {
            return super.isEnabled(oObject_p, oParent_p, iContext_p);
        }

        return super.isEnabled(resolvedSource, oParent_p, iContext_p);
    }

    private boolean isMimeTypeEnabled(String mimeType)
    {
        if (mimeTypeList.isEmpty() || mimeType == null || mimeType.isEmpty())
        {
            return true;
        }
        if (!mimeTypeList.contains(mimeType))
        {
            return false;
        }

        return true;
    }

    @Override
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        OwObject resolvedSource = rootFolderResolver.getRootFolder(oObject_p, oParent_p);

        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwAddObjectDocumentFunction.onClickEvent: The document function is not enabled for this object.");
            }
            throw new OwInvalidOperationException(getContext().localize("plug.owaddobject.OwAddObjectDocumentFunction.invalidobject", "This document function is not enabled for this object."));
        }

        if (resolvedSource == null || resolvedSource.equals(oParent_p))
        {
            m_sourceObject = oObject_p;
        }
        else
        {
            m_sourceObject = resolvedSource;
        }

        m_folderObject = useSourceAsParent() ? m_sourceObject : getContainmentFolder(m_sourceObject, oParent_p);

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

        OwCreateObjectDialog dlg = null;

        dlg = createCreateObjectDialog(m_sourceObject, m_folderObject, classSelectionCfg, false);
        //ObjectClassProcessor
        dlg.setObjectClassProcessor(getObjectClassProcessor());

        dlg.setConfigNode(getConfigNode());

        dlg.setJspConfigurator(m_jspConfigurator);

        // set the property list configuration
        dlg.setPropertyListConfiguration(getPropertyListConfiguration());

        // perform property mapping
        Map presetProperties = OwMappingUtils.getParameterMapValuesFromRecord(getConfigNode(), m_sourceObject, m_folderObject);
        Map valuesFromDocument = OwMappingUtils.getParameterMapValuesFromObject(getConfigNode(), m_sourceObject, "DocumentParameterMapping");
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
        addHistoryEvent(m_sourceObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);

        //end

        this.dialog.setListener(this);
        this.dialog.setRefreshContext(null);

        this.refreshCtx = refreshCtx_p;
        this.refreshObject = oObject_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owaddobject.OwAddObjectDocumentFunction#onDialogClose(com.wewebu.ow.server.ui.OwDialog)
     */
    @Override
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        //        super.onDialogClose(dialogView_p);
        //        // refresh the context
        if (refreshCtx != null)
        {
            refreshCtx.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, refreshObject);
        }
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
}
