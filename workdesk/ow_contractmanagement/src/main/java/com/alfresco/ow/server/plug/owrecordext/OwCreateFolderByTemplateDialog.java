package com.alfresco.ow.server.plug.owrecordext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.OwContractGroupPropertyView;
import com.alfresco.ow.contractmanagement.log.OwLog;
import com.alfresco.ow.server.dmsdialogs.views.OwTabObjectListViewRow;
import com.alfresco.ow.server.utils.OwNetworkHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.plug.owrecordext.OwCreateContractDialog;
import com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwCreateFolderByTemplateDialog.
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
public class OwCreateFolderByTemplateDialog extends OwCreateContractDialog
{
    /** Logger for this class */
    private static final Logger LOG = OwLog.getLogger(OwCreateContractDialogAddAspects.class);

    /** name of the attribute to read from config */
    protected static final String ATTRIBUTE_USE_AS_TEMPLATE = "useAsTemplate";

    /** name of the node which tells the name / DMSID / search template of the template folder */
    protected static final String NODE_TEMPLATE_FOLDER = "TemplateFolder";

    /** name of the attribute if to use the template folder class or the dialog configured one */
    protected static final String ATTRIBUTE_USE_FOLDER_CLASS = "useTemplateFolderClass";

    /** name of the attribute  if to replace the folder class names and permissions from the subfolders of the template */
    protected static final String ATTRIBUTE_REPLACE_SUBFOLDERCLASS_AND_PERMISSION = "replaceSubFolderClassAndPermissions";

    /** stores the path / DMSID / search template name */
    private String m_TemplateFolder;

    /** stores if the folder itself should be used as template or the containment should be listed for template folder selection */
    private boolean m_UseAsTemplate;

    /** stores the object list view showing the containment of the folder or the hits of the search template */
    private OwTabObjectListViewRow m_ListView;

    /** Column info collection for header of list view */
    protected List<OwStandardFieldColumnInfo> m_ColumnInfoList;

    /** stores the tab info of the "select template" tab */
    protected OwTabInfo m_SelectTemplateTab;

    /** Stores the selected template folder */
    protected OwObject m_SelectedTemplateFolder;

    /** stores if to use the class from the template folder or to use dialogs "normal" mechanism instead. */
    private boolean m_UseFolderClass;

    /** 
     * Constructor
     * @param folderObject_p
     * @param classSelectionCfg
     * @param fOpenObject_p
     */
    public OwCreateFolderByTemplateDialog(OwObject folderObject_p, OwObjectClassSelectionCfg classSelectionCfg, boolean fOpenObject_p)
    {
        super(folderObject_p, classSelectionCfg, fOpenObject_p);
    }

    @Override
    protected void init() throws Exception
    {
        // read configuration (must be done first because init from super class calls "createPropertyViewBridge" where 
        // the config is required!)
        OwXMLUtil handler = getConfigNode().getSubUtil(NODE_TEMPLATE_FOLDER);
        if (null != handler)
        {
            // read config 
            this.m_TemplateFolder = handler.getSafeTextValue(null);
            this.m_UseAsTemplate = handler.getSafeBooleanAttributeValue(ATTRIBUTE_USE_AS_TEMPLATE, false);
            this.m_UseFolderClass = handler.getSafeBooleanAttributeValue(ATTRIBUTE_USE_FOLDER_CLASS, false);

            // set template root folder 
            this.m_SelectedTemplateFolder = getTemplateRootFolder();

            // get column info from config
            this.m_ColumnInfoList = this.getListViewColumns(getConfigNode());
        }

        // creation of views will be done with "createPropertyViewBridge" called from super class
        super.init();

        // if to use the class from folder instead of dialogs normal mechanism always hide class selection
        if (this.m_UseFolderClass)
        {
            m_classView = null;
        }
    }

    /**
     * Returns the list view column header definition from the configuration.
     * @return the list view column header definition from the configuration.
     */
    @SuppressWarnings("unchecked")
    public List<OwStandardFieldColumnInfo> getListViewColumns(OwXMLUtil config_p)
    {
        List<OwStandardFieldColumnInfo> list = new LinkedList<OwStandardFieldColumnInfo>();

        List<String> defaultPropertyNameList = config_p.getSafeStringList("ListViewColumns");
        Iterator<String> it = defaultPropertyNameList.iterator();
        while (it.hasNext())
        {
            String strPropertyName = it.next();

            // get display name
            OwFieldDefinition fielddef = null;
            try
            {
                fielddef = ((OwMainAppContext) getContext()).getNetwork().getFieldDefinition(strPropertyName, null);
            }
            catch (Exception e)
            {
                // just set a warning when property load failed, we still keep continue working at least with the remaining properties
                LOG.error("Could not resolve property for contentlist, propertyname = " + strPropertyName, e);
            }

            // add column info
            list.add(new OwStandardFieldColumnInfo(fielddef));

        }

        return list;
    }

    /**
     * returns the root template folder as configured.
     * @return the root template folder as configured.
     * @throws Exception
     */
    protected OwObject getTemplateRootFolder() throws Exception
    {
        // see what we have as getter - it must be either a path, an DMSID or a search template
        if (null == this.m_TemplateFolder)
        {
            String sMessage = NODE_TEMPLATE_FOLDER + " is a required argument. Please check your configuration!";
            LOG.error(sMessage);
            throw new OwConfigurationException(sMessage);
        }

        // Retrieve object from DMS
        OwObject folder = OwNetworkHelper.getObject((OwMainAppContext) getContext(), this.m_TemplateFolder, NODE_TEMPLATE_FOLDER);

        // object must be a folder ...
        if (!OwNetworkHelper.isFolder(folder))
        {
            String sMessage = "Object identified by '" + this.m_TemplateFolder + "' is not a container object!";
            LOG.error(sMessage);
            throw new OwConfigurationException(sMessage);
        }

        return folder;
    }

    @Override
    protected void initTabOrder() throws Exception
    {
        super.initTabOrder();
        if (null != this.m_TemplateFolder && null != this.m_SelectedTemplateFolder && !this.m_UseAsTemplate)
        {
            // create the embedded view
            this.m_ListView = createTemplateView();
            int idxTemplateTab = m_SubNavigation.addView(this.m_ListView, getContext().localize("owrecordext.OwCreateFolderByTemplateDialog.createTemplateFolder_title", "Select Template"), null, getContext().getDesignURL()
                    + "/images/ContractManagement/select-template.png", null, null);

            // show folder contents
            this.m_ListView.showFolderContent(m_SelectedTemplateFolder);
            //move templateSelection representation before something
            int idx = m_SubNavigation.getTabList().indexOf(propertiesTab);
            if (m_keyPatternPropertyBridge != null)
            {
                idx--;
                m_SubNavigation.enable(idx, true);
                this.m_ListView.setNextActivateView(m_keyPatternPropertyBridge.getView());
            }
            else
            {
                // set successor of m_EditAspectsView
                this.m_ListView.setNextActivateView(this.delegateView);
            }

            m_SelectTemplateTab = (OwTabInfo) m_SubNavigation.getTabList().get(idxTemplateTab);
            m_SubNavigation.getTabList().remove(m_SelectTemplateTab);
            m_SubNavigation.getTabList().add(idx, m_SelectTemplateTab);
            m_SubNavigation.enable(idx, true);

            // 1. View: m_classView -> should be null, but may also exist
            // 2. View: accessRightsView -> should be null, but may also exist
            // 3. View: m_ListView -> may be null
            // 4. View: keyPattern --> may be null
            // 5. View: delegateView -> always exists

            // set successor of m_classView 
            if (null != this.m_classView && null == this.accessRightsView)
            {
                this.m_classView.setNextActivateView(this.m_ListView);
            }

            // set successor of accessRightsView
            if (null != this.accessRightsView)
            {
                this.accessRightsView.setNextActivateView(this.m_ListView);
            }
        }
    }

    /**(overridable)
     * Factory method to create the template view.
     * @return OwTabObjectListViewRow
     * @throws OwException
     * @since 4.2.0.0     */
    protected OwTabObjectListViewRow createTemplateView() throws OwException
    {
        return new OwTabObjectListViewRow(this, getListViewColumns(getConfigNode()));
    }

    /** create the folder / record 
     * @return String DMSID of new object 
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String create() throws Exception
    {
        // filter out read-only, hidden and null properties
        OwPropertyCollection docStandardPropertiesMap = m_sceletonObject.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        //explicit add the name property
        String namePropertyKey = m_sceletonObject.getObjectClass().getNamePropertyName();
        docStandardPropertiesMap.put(namePropertyKey, m_sceletonObject.getProperty(namePropertyKey));

        // permissions
        OwPermissionCollection permCol = null;
        if (getAccessRightsView() != null)
        {//get Permission Collection only if the AccessRightsView is enabled
            permCol = m_sceletonObject.getPermissions();
        }
        else
        {
            // copy permissions from template folder if access rights view is null
            permCol = this.m_SelectedTemplateFolder.getPermissions();
        }
        // === create a new object
        String newDmsid = null;
        try
        {
            OwMainAppContext context = ((OwMainAppContext) getContext());
            int[] childTypes = new int[] { OwObjectReference.OBJECT_TYPE_FOLDER, OwObjectReference.OBJECT_TYPE_DOCUMENT };
            newDmsid = context.getNetwork().createObjectCopy(this.m_SelectedTemplateFolder, docStandardPropertiesMap, permCol, m_folderObject, childTypes);

            // copy the references in the original folder object
            // get target folder OwObject
            OwObject targetFolder = OwNetworkHelper.getFolderFromDmsId(context, newDmsid);
            copyRelationships(context, this.m_SelectedTemplateFolder, targetFolder);

            // flag success
            m_dialogStatus = DIALOG_STATUS_OK;

            // return DMSID 
            return newDmsid;
        }
        catch (Exception e)
        {
            // just log and hope the rest can be copied
            String sMessage = "Error copy folder structure!";
            LOG.error(sMessage, e);
            // flag failure
            m_dialogStatus = DIALOG_STATUS_FAILED;
            // re-throw exception
            throw e;
        }
    }

    /**
     * Copy relationships from source folder to newly created target folder.
     * @param context_p   OwMainAppContext: Main app context
     * @param sourceFolder_p OwObject: source folder
     * @param targetFolder_p OwObject: target folder
     * @throws OwException 
     */
    protected void copyRelationships(OwMainAppContext context_p, OwObject sourceFolder_p, OwObject targetFolder_p) throws OwException
    {
        try
        {
            // retrieve link objects from source folder 
            OwObjectCollection links = OwNetworkHelper.getLinkObjects(sourceFolder_p);

            // iterate link objects and copy them, updating the source property
            if (null != links && !links.isEmpty())
            {
                // iterate collection and copy links
                for (Object linkObj : links)
                {
                    OwObjectLink link = (OwObjectLink) linkObj;
                    copyLink(context_p, targetFolder_p, link);
                }
            }
        }
        catch (OwException e)
        {
            LOG.error("Error copying link objects to target folder.", e);
            throw e;
        }
    }

    /**
     * Copy link to target folder. 
     * @param context_p  OwMainAppContext
     * @param targetFolder_p OwObject: folder to copy to
     * @param sourceLink_p OwObjectLink: link to copy
     * @throws OwException 
     */
    protected void copyLink(OwMainAppContext context_p, OwObject targetFolder_p, OwObjectLink sourceLink_p) throws OwException
    {
        try
        {
            OwNetwork network = context_p.getNetwork();
            OwPropertyCollection props = sourceLink_p.getProperties(null);
            network.createNewObject(targetFolder_p.getResource(), sourceLink_p.getClassName(), props, null, null, targetFolder_p, null, null);
        }
        catch (Exception e)
        {
            String message = "Error copying link to target folder.";
            LOG.error(message, e);
            throw new OwInvalidOperationException(message);
        }
    }

    /** the object class for the folder was changed, update the skeleton
     */
    @Override
    protected void updateObjectClass() throws Exception
    {

        if (!this.m_UseFolderClass)
        {
            super.updateObjectClass();
            return;
        }

        // use class from template if configured
        this.m_folderClass = this.m_SelectedTemplateFolder.getObjectClass();

        updatePropertyValues();

        super.updateObjectClass();
    }

    /**
     * Updates the property values.
     * @throws Exception
     */
    protected void updatePropertyValues() throws Exception
    {
        Map<String, Object> valueMap = new HashMap<String, Object>();

        OwPropertyCollection propertyCol = this.m_SelectedTemplateFolder.getProperties(null);
        @SuppressWarnings("unchecked")
        Iterator<Entry<String, OwProperty>> iterator = propertyCol.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, OwProperty> entry = iterator.next();
            OwProperty property = entry.getValue();

            //ignore property with no value
            if (property.getValue() == null)
            {
                continue;
            }

            //ignore name property or system properties
            if (property.getPropertyClass().isNameProperty() || property.getPropertyClass().isSystemProperty())
            {
                continue;
            }

            //ignore generated properties
            if (this.m_propertyPatternConfiguration.containsKey(entry.getKey()))
            {
                continue;
            }

            valueMap.put(entry.getKey(), property.getValue());
        }
        if (!valueMap.isEmpty())
        {
            this.setValues(valueMap);
        }
    }

    /**
     * Updates the selected template folder 
     * @param folder_p folder to use as new template folder
     * @throws Exception 
     */
    public void updateTemplateFolder(OwObject folder_p) throws Exception
    {
        // if to use the class from the template update skeleton object, too!
        this.m_SelectedTemplateFolder = folder_p;
        this.updateObjectClass();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.OwCreateObjectDialog#createObjectPropertyView()
     */
    @Override
    protected OwObjectPropertyView createObjectPropertyView() throws Exception
    {
        //generate key support & CMG auditing
        return new OwContractGroupPropertyView(this.m_propertyPatternConfiguration);
    }

}
