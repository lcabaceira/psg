package com.wewebu.ow.server.plug.owaddobject;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwRecordFunction;
import com.wewebu.ow.server.dmsdialogs.OwCreateObjectDialog;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.plug.owutil.OwMappingUtils;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the record function plugin, for adding objects.<br/>
 * If specified in the configuration it uses a given formular for property editing.
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
@SuppressWarnings("rawtypes")
public class OwAddObjectRecordFunction extends OwRecordFunction implements OwDialogListener
{
    /**
     * path to add record icon.
     * @since 3.1.0.0
     */
    private static final String ADD_RECORD_BIG_IMAGE = "/images/plug/owaddrecord/add_record_24.png";

    /**
     * path to add record big icon.
     * @since 3.1.0.0
     */
    private static final String ADD_RECORD_IMAGE = "/images/plug/owaddrecord/add_record.png";

    /**
     * path to add record extended big icon.
     * @since 3.1.0.0
     */
    private static final String ADD_RECORD_EXT_BIG_IMAGE = "/images/plug/owaddrecordwithid/add_record_with_id_24.png";
    /**
     * path to add record extended icon.
     * @since 3.1.0.0
     */
    private static final String ADD_RECORD_EXT_IMAGE = "/images/plug/owaddrecordwithid/add_record_with_id.png";

    /** Logger for this class */
    private static final Logger LOG = OwLog.getLogger(OwAddObjectRecordFunction.class);

    /** last root object for history events */
    protected OwObject m_rootObject;
    /** last folder object for history events */
    protected OwObject m_folderObject;

    /** 
     * the configured object class
     * @deprecated will be removed in the future. See {@link OwObjectClassSelectionCfg}. 
     */
    protected String m_objectClass;

    /** JSPForm Configuration handler.
     * @since 3.1.0.0*/
    protected OwJspFormConfigurator m_jspPageConfigurator;
    /**Flag notifying if the generation feature is used
     * @since 3.1.0.0*/
    protected boolean isGeneratedIdsFeatureInUse;

    /**
     * ObjectClassProcessor for post processing of object class selection.
     * @since 4.1.1.0
     */
    private OwObjectClassProcessor objectClassProcessor;
    /** Handler for EditPropertyList configuration
     * @since 4.2.0.0*/
    private OwPropertyListConfiguration propertyListConfiguration;

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        isGeneratedIdsFeatureInUse = node_p.getSubUtil("DialogHandler") != null ? true : false;
        super.init(node_p, context_p);
        m_objectClass = node_p.getSafeTextValue("ObjectClass", null);
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
        OwXMLUtil util = getConfigNode().getSubUtil(OwPropertyListConfiguration.ELEM_ROOT_NODE);
        if (util != null)
        {
            propertyListConfiguration = new OwPropertyListConfiguration(getPluginID());
            propertyListConfiguration.build(util);
        }
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", isGeneratedIdsFeatureInUse ? ADD_RECORD_EXT_IMAGE : ADD_RECORD_IMAGE);
    }

    /** get the URL to the icon of the dialog / function
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", isGeneratedIdsFeatureInUse ? ADD_RECORD_EXT_BIG_IMAGE : ADD_RECORD_BIG_IMAGE);
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
        // check if parent object DMSID is defined, if not check if object can by created upon selected folder
        if (null == getConfigNode().getSafeTextValue("ParentObject", null))
        {
            // folder needs to be selected and folder class must be supported
            if (super.isEnabled(rootObject_p, folderObject_p, iContext_p))
            { // object must be createable in folder
                return getContext().getNetwork().canCreateNewObject(folderObject_p.getResource(), folderObject_p, iContext_p);
            }
            else
            { // folderObject_p == null or folder class is not supported
                return false;
            }
        }
        else
        {
            // with a defined parent we got no problems.
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
            LOG.error("OwAddObjectRecordFunction.onClickEvent(): Invalid Object - the functionality is disabled for this object.");
            throw new OwInvalidOperationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.invalidobject", "You cannot add an item."));
        }

        m_rootObject = rootObject_p;
        // configured file parent object to use, or null if the selected folder should be parent
        String fileParentDefinition = getConfigNode().getSafeTextValue("ParentObject", null);
        if (fileParentDefinition != null)
        {
            m_folderObject = OwEcmUtil.createObjectFromString(getContext(), fileParentDefinition);
        }
        else
        {
            m_folderObject = folderObject_p;
        }

        // open the newly created folder, or false if folder should not be opened
        boolean fOpenObject = getConfigNode().getSafeBooleanValue("OpenObject", false);

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
                getContext().getNetwork().getObjectClass(defaultClassName, m_folderObject.getResource());
            }
            catch (OwObjectNotFoundException e)
            {
                throw new OwInvalidOperationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFormularFunction.invalidobject", "You cannot add an item."), e);
            }
        }

        m_jspPageConfigurator = new OwJspFormConfigurator(getConfigNode());

        //TODO remove strParentObjectClass and strClassName and objectClassSelectionEl 
        String strParentObjectClass = getConfigNode().getSafeTextValue("ObjectClassParent", null);
        String strClassName = getConfigNode().getSafeTextValue("ObjectClass", null);
        OwXMLUtil objectClassSelectionEl = getConfigNode().getSubUtil(OwObjectClassSelectionCfg.EL_OBJECT_CLASS_SELECTION);

        OwCreateObjectDialog dlg = null;
        if (null == strClassName && null == strParentObjectClass && null != objectClassSelectionEl)
        {
            dlg = createCreateObjectDialog(m_folderObject, classSelectionCfg, fOpenObject);
        }
        else
        {
            dlg = createCreateObjectDialog(m_folderObject, strClassName, strParentObjectClass, fOpenObject);
        }

        dlg.setConfigNode(getConfigNode());
        dlg.setJspConfigurator(m_jspPageConfigurator);
        dlg.setObjectClassProcessor(getObjectClassProcessor());
        // set the property info
        dlg.setPropertyListConfiguration(getPropertyListConfiguration());

        // perform property mapping
        dlg.setValues(getPredefinedValues(rootObject_p, folderObject_p));

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
        addHistoryEvent(rootObject_p, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_BEGIN);
    }

    /** overridable factory method
     * <p>Since 3.1.0.0 it is possible to configure in <code>&lt;DialogHanlder/&gt;</code> node a subclass of {@link OwCreateObjectDialog}.<br /> 
     * In that situation it's mandatory that configured class must have a public constructor with the same signature as: 
     * <p>{@link OwCreateObjectDialog#OwCreateObjectDialog(OwObject, String, String, boolean)}</p> 
     * If another constructor is intended to be used, this method must be overwritten.<p>
     * 
     * @param folderObject_p
     * @param strClassName_p
     * @param strObjectClassParent_p
     * @param fOpenObject_p
     * @return OwCreateObjectDialog
     * @throws Exception
     * @deprecated
     */
    protected OwCreateObjectDialog createCreateObjectDialog(OwObject folderObject_p, String strClassName_p, String strObjectClassParent_p, boolean fOpenObject_p) throws Exception
    {
        if (isGeneratedIdsFeatureInUse)
        {
            OwXMLUtil dialogHandler = getConfigNode().getSubUtil("DialogHandler");
            String dialogClassName = dialogHandler.getSafeStringAttributeValue("className", null);
            if (dialogClassName == null)
            {
                LOG.error("OwAddObjectRecordFunction.createCreateObjectDialog(): No className attribute configured for DialogHandler.");
                throw new OwConfigurationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.classNameNotFound", "No class Name attribute configured for DialogHandler."));
            }
            try
            {
                Class<?> dialogClass = Class.forName(dialogClassName);
                Constructor<?> constructor = dialogClass.getConstructor(OwObject.class, String.class, String.class, boolean.class);
                OwCreateObjectDialog createDialog = (OwCreateObjectDialog) constructor.newInstance(folderObject_p, strClassName_p, strObjectClassParent_p, fOpenObject_p);
                return createDialog;
            }
            catch (Exception e)
            {
                LOG.error("OwAddObjectRecordFunction.createCreateObjectDialog(): Cannot create dialog.", e);
                throw new OwConfigurationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.cannotCreateDialog", "Cannot create the ObjectCreateDialog instance."), e);
            }
        }
        else
        {
            OwCreateObjectDialog dialog = new OwCreateObjectDialog(folderObject_p, strClassName_p, strObjectClassParent_p, fOpenObject_p);
            return dialog;

        }
    }

    /** overridable factory method
     * <p>Since 3.1.0.0 it is possible to configure in <code>&lt;DialogHanlder/&gt;</code> node a subclass of {@link OwCreateObjectDialog}.<br /> 
     * In that situation it's mandatory that configured class must have a public constructor with the same signature as: 
     * <p>{@link OwCreateObjectDialog#OwCreateObjectDialog(OwObject, OwObjectClassSelectionCfg, boolean)}</p> 
     * If another constructor is intended to be used, this method must be overwritten.<p>
     * 
     * @param folderObject_p
     * @param classSelectionCfg
     * @param fOpenObject_p
     * @return OwCreateObjectDialog
     * @throws Exception
     * @since 4.1.0.0
     */
    protected OwCreateObjectDialog createCreateObjectDialog(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean fOpenObject_p) throws Exception
    {
        if (isGeneratedIdsFeatureInUse)
        {
            OwXMLUtil dialogHandler = getConfigNode().getSubUtil("DialogHandler");
            String dialogClassName = dialogHandler.getSafeStringAttributeValue("className", null);
            if (dialogClassName == null)
            {
                LOG.error("OwAddObjectRecordFunction.createCreateObjectDialog(): No className attribute configured for DialogHandler.");
                throw new OwConfigurationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.classNameNotFound", "No class Name attribute configured for DialogHandler."));
            }
            try
            {
                Class<?> dialogClass = Class.forName(dialogClassName);
                Constructor<?> constructor = dialogClass.getConstructor(OwObject.class, OwObjectClassSelectionCfg.class, boolean.class);
                OwCreateObjectDialog createDialog = (OwCreateObjectDialog) constructor.newInstance(folderObject_p, classSelectionCfg, fOpenObject_p);
                return createDialog;
            }
            catch (Exception e)
            {
                LOG.error("OwAddObjectRecordFunction.createCreateObjectDialog(): Cannot create dialog.", e);
                throw new OwConfigurationException(getContext().localize("plug.owadddocument.OwAddObjectRecordFunction.cannotCreateDialog", "Cannot create the ObjectCreateDialog instance."), e);
            }
        }
        else
        {
            OwCreateObjectDialog dialog = new OwCreateObjectDialog(folderObject_p, classSelectionCfg, fOpenObject_p);
            return dialog;

        }
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
                addHistoryEvent(m_rootObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_FAILED);
                break;
            case OwCreateObjectDialog.DIALOG_STATUS_OK:
                addHistoryEvent(m_rootObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
                break;
            default:
                addHistoryEvent(m_rootObject, m_folderObject, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_CANCEL);
                break;
        }
        m_rootObject = null;
        m_folderObject = null;
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
     * Return a map with values which should be predefined/set in initial context.
     * @param rootObject_p OwObject which will be the parent
     * @param folderObject_p OwObject (can be null)
     * @return Map with specific property name to value entries, or null if none available
     * @throws Exception could not create map
     * @since 3.2.0.0
     */
    protected Map getPredefinedValues(OwObject rootObject_p, OwObject folderObject_p) throws Exception
    {
        return OwMappingUtils.getParameterMapValuesFromRecord(getConfigNode(), rootObject_p, folderObject_p);
    }

    /**
     * OwObjectClassProcessor if any was defined.
     * @return OwObjectclassProcessor
     * @since 4.1.1.0
     */
    protected OwObjectClassProcessor getObjectClassProcessor()
    {
        return this.objectClassProcessor;
    }

    /**
     * Get the current PropertyList Configuration
     * @return OwPropertyListConfiguration or null if not configured
     * @since 4.2.0.0
     */
    protected OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return this.propertyListConfiguration;
    }
}