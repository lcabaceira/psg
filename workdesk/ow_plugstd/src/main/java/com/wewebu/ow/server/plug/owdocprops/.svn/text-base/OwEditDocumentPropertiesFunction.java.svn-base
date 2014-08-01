package com.wewebu.ow.server.plug.owdocprops;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.wd.ui.conf.OwPropertyListConfiguration;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.owutil.OwConfigUtils;
import com.wewebu.ow.server.plug.owutil.OwObjectUtils;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of the Document edit properties plugin.
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
public class OwEditDocumentPropertiesFunction extends OwDocumentFunction implements OwDialogListener
{
    // === members
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwEditDocumentPropertiesFunction.class);

    /**Configuration to show user modifiable properties view*/
    public static final String ELEM_PROP_VIEW = "PropertiesView";
    /**Configuration if system properties view should exist*/
    public static final String ELEM_SYS_PROP_VIEW = "SystemPropertiesView";
    /**VersionSeries information view*/
    public static final String ELEM_VERSIONS_VIEW = "VersionsView";
    /**History/Audit view configuration entry*/
    public static final String ELEM_HISTORY_VIEW = "HistoryView";
    /**Configuration to show filed in object information*/
    public static final String ELEM_FILED_VIEW = "FiledInView";
    /**Access rights view configuration entry*/
    public static final String ELEM_ACL_VIEW = "AccessRightsView";
    /**Configuration for Links view*/
    public static final String ELEM_LINKS_VIEW = "LinksView";
    /**Auto open configuration for Viewer*/
    public static final String ELEM_AUTO_OPEN = "AutoOpen";
    /**Modifiablilty hanlder configuration entry*/
    public static final String ELEM_MOD_HANDLER = "ModifiabilityHandler";

    /**Handler for multiple JSP forms configuration*/
    private OwJspFormConfigurator m_owJspFormConfigurator;
    /**PropertyList configuration handler for property filtering and/or group
     * @since 4.2.0.0*/
    private OwPropertyListConfiguration propertyListConfiguration;

    private OwEditPropertiesModifiabilityHandler modifiablityHandler;

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        m_owJspFormConfigurator = new OwJspFormConfigurator(node_p);
        OwXMLUtil util = getConfigNode().getSubUtil(OwPropertyListConfiguration.ELEM_ROOT_NODE);
        if (util != null)
        {
            propertyListConfiguration = new OwPropertyListConfiguration(getPluginID());
            propertyListConfiguration.build(util);
        }
        OwXMLUtil modConf = getConfigNode().getSubUtil(ELEM_MOD_HANDLER);
        if (modConf != null)
        {
            String className = modConf.getSafeStringAttributeValue("class", "com.wewebu.ow.server.plug.owdocprops.OwExpressionModifiabilityHandler");
            if (className != null)
            {
                Class<?> cls;
                try
                {
                    cls = Class.forName(className);
                }
                catch (ClassNotFoundException cnfEx)
                {
                    throw new OwConfigurationException(getContext().localize1("owdocprops.OwEditDocumentPropertiesFunction.init.err.modifabilityHandler.class", "Unable to find handler with class %1.", className), cnfEx);
                }

                modifiablityHandler = (OwEditPropertiesModifiabilityHandler) cls.newInstance();
                modifiablityHandler.init(getContext(), modConf);
            }
        }
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocprops/edit_properties.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocprops/edit_properties_24.png");
    }

    /** event called when user clicked the label / icon 
     *
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     *  @param refreshCtx_p OwFunctionRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {

        if (!isEnabled(oObject_p, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            LOG.debug("OwEditDocumentProperties.onClickEvent(): the edit function is not enabled for the given object !");
            throw new OwInvalidOperationException(new OwString("owdocprops.edit.properties.function.invalid.invalid.object", "Item can not be edited!"));
        }

        List objects = new LinkedList();

        objects.add(oObject_p);

        onMultiselectClickEvent(objects, oParent_p, refreshCtx_p);
    }

    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject 
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        Collection enabledObjects = new LinkedList();

        for (Iterator i = objects_p.iterator(); i.hasNext();)
        {
            OwObject objectToEdit = (OwObject) i.next();
            if (isEnabled(objectToEdit, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                enabledObjects.add(objectToEdit);
            }
        }
        if (enabledObjects.isEmpty())
        {
            LOG.error("OwEditDocumentProperties.onMultiselectClickEvent(): Invalid object collection!The enabled objects collection is empty!");
            throw new OwInvalidOperationException(new OwString("owdocprops.edit.properties.function.invalid.object.collection", "Invalid object collection!"));
        }

        //for batch import
        Collection batchIndexProperties = getConfigNode().getSafeStringList("EditBatchPropertyList");

        // === which views should be displayed
        int iViewMask = calculateViewMask(getConfigNode());

        // === which views should be displayed read-only 
        int iReadOnlyViewMask = calculateReadOnlyViewMask(getConfigNode());

        // === get a search template for the history view
        OwSearchTemplate historySearchTemplate = null;
        List historyViewColumnInfo = null;

        if ((iViewMask & OwEditPropertiesDialog.VIEW_MASK_HISTORY) == OwEditPropertiesDialog.VIEW_MASK_HISTORY)
        {
            String strHistoryViewSearchTemplate = getConfigNode().getSafeTextValue("HistoryViewSearchTemplate", null);

            if (null != strHistoryViewSearchTemplate)
            {
                historySearchTemplate = (OwSearchTemplate) getContext().getNetwork().getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE, strHistoryViewSearchTemplate, false, false);
                historySearchTemplate.init(getContext().getHistoryManager());
            }
            else
            {
                // === alternatively get a list with column info for the history
                // view
                historyViewColumnInfo = (List) getSafeSetting("HistoryViewColumnInfo", null);

                if (historyViewColumnInfo == null)
                {
                    String msg = "OwEditDocumentProperties.onMultiselectClickEvent: Please specify either &lt;HistoryViewSearchTemplate&gt; ... or &lt;settingsset&gt; &lt;HistoryViewColumnInfo&gt; in the plugin descriptor, PluginId = "
                            + getPluginID();
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }
            }
        }

        // === create new edit properties dialog
        Collection objects = enabledObjects;

        // select version to edit
        if (getConfigNode().getSafeBooleanValue("AlwaysUseLatestVersion", false))
        {
            objects = OwObjectUtils.getLatesVersionObjects(enabledObjects);
        }

        OwXMLUtil linksViewConfig = getConfigNode().getSubUtil(ELEM_LINKS_VIEW);
        OwObjectLinkRelation split = OwObjectLinkRelation.BOTH;
        Collection<String> linkClassNames = Collections.EMPTY_LIST;
        boolean displayTypedList = true;

        if (linksViewConfig != null)
        {
            displayTypedList = linksViewConfig.getSafeBooleanAttributeValue("typeList", true);
            String direction = linksViewConfig.getSafeStringAttributeValue("direction", "BOTH");
            try
            {
                split = OwObjectLinkRelation.valueOf(direction);
            }
            catch (IllegalArgumentException e)
            {
                LOG.error("Invalid direction attribute value in confiuration of document function with ID=" + getPluginID(), e);
            }
            linkClassNames = linksViewConfig.getSafeStringList("linkclasses");
        }

        OwEditPropertiesDialog dlg = createPropertiesDialog(objects, oParent_p, refreshCtx_p, getConfigNode().getSafeIntegerValue("MaxChildSize", 50), (List) getSafeSetting("VersionViewColumnInfo", null), historySearchTemplate,
                historyViewColumnInfo, batchIndexProperties, split, displayTypedList, linkClassNames);
        dlg.setJspConfigurator(m_owJspFormConfigurator);
        dlg.setPropertyListConfiguration(getPropertyListConfiguration());

        // Mask the views to be used
        dlg.setViewMask(iViewMask);
        dlg.setReadOnlyViewMask(iReadOnlyViewMask);

        // set help path if defined in plugin descriptor
        dlg.setHelp(getHelpPath());
        dlg.setTitle(getContext().localize("owdocprops.OwEditDocumentProperties.helptitle", "Dokument Eigenschaften bearbeiten"));

        // set info icon
        dlg.setInfoIcon(getBigIcon());

        OwXMLUtil previewConfiguration = getConfigNode().getSubUtil("Preview");
        dlg.setPreviewConfiguration(previewConfiguration);
        dlg.setModifiabilityHandler(this.modifiablityHandler);

        // open dialog
        getContext().openDialog(dlg, this);

        // historize
        addHistoryEvent(objects, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_STATUS_OK);
    }

    /** overridable factory method to create the dialog
     * 
     * @param objects_p
     * @param oParent_p
     * @param refreshCtx_p
     * @param iMaxElementSize_p
     * @param versionColumnInfo_p
     * @param historyViewSearchTemplate_p
     * @param historyViewColumnInfo_p
     * @param batchIndexProperties_p Collection of batch index properties each property will be copied to the next pane
     * 
     * @return the newly created {@link OwEditPropertiesDialog}
     * @throws Exception
     */
    protected OwEditPropertiesDialog createPropertiesDialog(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p, int iMaxElementSize_p, Collection versionColumnInfo_p, OwSearchTemplate historyViewSearchTemplate_p,
            Collection historyViewColumnInfo_p, Collection batchIndexProperties_p, OwObjectLinkRelation relationSplit_p, boolean displayTypedList_p, Collection<String> linkClassNames_p) throws Exception
    {
        OwEditPropertiesDialogBuilder builder = createDialogBuilder();
        builder.items(objects_p).index(0).parentObject(oParent_p);
        builder.maxElementSize(iMaxElementSize_p).versionColumnInfo(versionColumnInfo_p);
        builder.historyViewSearchTemplate(historyViewSearchTemplate_p).historyViewColumnInfo(historyViewColumnInfo_p);
        builder.batchIndexProperties(batchIndexProperties_p).relationSplit(relationSplit_p);
        builder.displayLinksByType(displayTypedList_p).linkClassNames(linkClassNames_p);

        return builder.build();
    }

    /**
     * Create a builder instance for OwEditPropertiesDialog creation.
     * @return instance of OwEditPropertiesDialogBuilder
     * @since 4.2.0.0
     */
    protected OwEditPropertiesDialogBuilder createDialogBuilder()
    {
        return new OwEditPropertiesDialogBuilder();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwDialog.OwDialogListener#onDialogClose(com.wewebu.ow.server.ui.OwDialog)
     */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // here receive update events from the views
        switch (code_p)
        {
            case OwUpdateCodes.MODIFIED_OBJECT_PROPERTY:
            {
                // on apply was pressed in the property view
                if (caller_p instanceof OwObjectPropertyView)
                {
                    OwObjectPropertyView realCaller = (OwObjectPropertyView) caller_p;
                    OwObject objectRef = realCaller.getObjectRef();
                    historizeModifiedPropsEvent(objectRef);
                }
                else if (caller_p instanceof OwObjectPropertyFormularView)
                {
                    OwObjectPropertyFormularView realCaller = (OwObjectPropertyFormularView) caller_p;
                    OwObject objectRef = realCaller.getObjectRef();
                    historizeModifiedPropsEvent(objectRef);
                }
            }
                break;
        }

    }

    /**
     * Historize the event for modified properties.
     * @param objectRef_p
     * @throws Exception
     */
    private void historizeModifiedPropsEvent(OwObject objectRef_p) throws Exception
    {
        Collection objectColection = new LinkedList();
        if (objectRef_p != null)
        {
            objectColection.add(objectRef_p);
            OwObjectCollection parents = objectRef_p.getParents();
            OwObject parent = parents != null && parents.size() > 0 ? (OwObject) parents.get(0) : null;
            this.addHistoryEvent(objectRef_p, parent, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_STATUS_OK);
        }
    }

    /**
     * Getter for available PropertyList configuration.
     * Is initialized during {@link #init(OwXMLUtil, OwMainAppContext)} method. 
     * @return OwPropertyListConfiguration or null if not configured
     * @since 4.2.0.0
     */
    protected OwPropertyListConfiguration getPropertyListConfiguration()
    {
        return propertyListConfiguration;
    }

    /**
     * Calculation of visible views, which will/should be displayed in the dialog.
     * @param configNode OwXMLUtil
     * @return OwEditPropertiesDialog view mask of views
     * @throws Exception 
     * @since 4.2.0.0
     */
    protected int calculateViewMask(OwXMLUtil configNode) throws Exception
    {
        int mask = 0;
        // === general active views
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, ELEM_PROP_VIEW, OwEditPropertiesDialog.VIEW_MASK_PROPERTIES);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, ELEM_SYS_PROP_VIEW, OwEditPropertiesDialog.VIEW_MASK_SYSTEM_PROPERTIES);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, ELEM_VERSIONS_VIEW, OwEditPropertiesDialog.VIEW_MASK_VERSIONS);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, ELEM_HISTORY_VIEW, OwEditPropertiesDialog.VIEW_MASK_HISTORY);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, ELEM_LINKS_VIEW, "show", OwEditPropertiesDialog.VIEW_MASK_LINKS, false);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, ELEM_FILED_VIEW, OwEditPropertiesDialog.VIEW_MASK_FILED_IN);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, ELEM_ACL_VIEW, OwEditPropertiesDialog.VIEW_MASK_ACCESS_RIGHTS);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, "DocumentFunctionsView", OwEditPropertiesDialog.VIEW_MASK_DOCUMENT_FUNCTIONS);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, "EnablePasteMetadata", OwEditPropertiesDialog.VIEW_MASK_ENABLE_PASTE_METADATA);
        mask |= OwConfigUtils.computeViewMaskFromConfig(configNode, "EnableSaveAll", OwEditPropertiesDialog.VIEW_MASK_ENABLE_SAVE_ALL);

        // === get scope flags for history view
        OwXMLUtil historyNode = configNode.getSubUtil(ELEM_HISTORY_VIEW);
        if (historyNode != null)
        {
            mask |= (historyNode.getSafeBooleanAttributeValue("objecthistory", true) ? OwEditPropertiesDialog.VIEW_MASK_HISTORY_SCOPE_OBJECT : 0);
            mask |= (historyNode.getSafeBooleanAttributeValue("casehistory", true) ? OwEditPropertiesDialog.VIEW_MASK_HISTORY_SCOPE_CASE : 0);
        }
        else
        {
            mask |= OwEditPropertiesDialog.VIEW_MASK_HISTORY_SCOPE_OBJECT;
            mask |= OwEditPropertiesDialog.VIEW_MASK_HISTORY_SCOPE_CASE;
        }
        // === auto open handling
        mask |= (getConfigNode().getSafeBooleanValue(ELEM_AUTO_OPEN, false) ? OwEditPropertiesDialog.VIEW_MASK_AUTOOPEN : 0);
        mask |= OwXMLDOMUtil.getSafeBooleanAttributeValue(getConfigNode().getSubNode(ELEM_AUTO_OPEN), "previewmode", false) ? OwEditPropertiesDialog.VIEW_MASK_PREVIEW_AUTOOPEN : 0;

        return mask;
    }

    /**
     * Calculate the read-only views as mask mapped into an integer. 
     * @param confNode
     * @return OwEditPropertiesDialog view mask of read-only views
     * @throws OwException
     * @since 4.2.0.0
     */
    protected int calculateReadOnlyViewMask(OwXMLUtil confNode) throws OwException
    {
        int mask = 0;
        mask |= OwConfigUtils.computeReadOnlyViewMaskFromConfig(confNode, ELEM_PROP_VIEW, OwEditPropertiesDialog.VIEW_MASK_PROPERTIES);
        mask |= OwConfigUtils.computeReadOnlyViewMaskFromConfig(confNode, ELEM_SYS_PROP_VIEW, OwEditPropertiesDialog.VIEW_MASK_SYSTEM_PROPERTIES);
        mask |= OwConfigUtils.computeReadOnlyViewMaskFromConfig(confNode, ELEM_VERSIONS_VIEW, OwEditPropertiesDialog.VIEW_MASK_VERSIONS);
        mask |= OwConfigUtils.computeReadOnlyViewMaskFromConfig(confNode, ELEM_HISTORY_VIEW, OwEditPropertiesDialog.VIEW_MASK_HISTORY);
        mask |= OwConfigUtils.computeReadOnlyViewMaskFromConfig(confNode, ELEM_FILED_VIEW, OwEditPropertiesDialog.VIEW_MASK_FILED_IN);
        mask |= OwConfigUtils.computeReadOnlyViewMaskFromConfig(confNode, ELEM_ACL_VIEW, OwEditPropertiesDialog.VIEW_MASK_ACCESS_RIGHTS);
        return mask;
    }
}