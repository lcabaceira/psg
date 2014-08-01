package com.alfresco.ow.server.fieldctrlimpl.objectreferenceselect;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSearchCriteriaView;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Simple Object Selection Dialog based on search template.
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
public class OwObjectReferenceSelectDialog extends OwStandardDialog implements OwObjectListView.OwObjectListViewEventListner
{

    /** package logger or class logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwObjectReferenceSelectDialog.class);

    /** region of the result list */
    public static final int RESULT_REGION = 10;

    protected OwXMLUtil m_config;

    /** the search criteria view */
    protected OwSearchCriteriaView m_criteriaView;
    /** search tree to work on */
    protected OwSearchNode m_search;
    /** object list view to display the objects */
    protected OwObjectListView m_objectListView;
    /** the object that was selected */
    protected List<OwObject> m_selectedObject;
    protected OwSubMenuView m_Menu;
    /** search template to use */
    protected OwSearchTemplate m_searchtemplate;

    private int enterButtonHandler;

    public OwObjectReferenceSelectDialog(OwXMLUtil config_p)
    {
        m_config = config_p;
    }

    /** called when the view should create its HTML content to be displayed
     * 
     */
    @Override
    protected void init() throws Exception
    {
        super.init();

        OwRoleManagerContext roleCtx = getContext().getRegisteredInterface(OwRoleManagerContext.class);
        m_criteriaView = createSerachView();
        m_objectListView = createListView();
        //use only one form, for keyboard event handling
        m_criteriaView.setExternalFormTarget(this);
        m_objectListView.setExternalFormTarget(this);

        m_Menu = createMenuView();
        String searchTemplateName = m_config.getSafeStringAttributeValue(OwObjectReferenceSelectFieldControl.SEARCHTEMPLATE_NAME_ATTRIBUTE, "");
        if (searchTemplateName.isEmpty())
        {
            throw new OwConfigurationException("No search template defined.");
        }

        try
        {
            m_searchtemplate = roleCtx.getUnmanagedAOProvider().getApplicationObject(OwAOConstants.AO_SEARCHTEMPLATE, searchTemplateName, false, false);
            m_searchtemplate.init(roleCtx.getNetwork());
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Error creating search template", e);
        }

        List<OwDocumentFunction> docFunctions = new ArrayList<OwDocumentFunction>();
        @SuppressWarnings("rawtypes")
        List docFunctionsIdUtil = m_config.getSafeUtilList(OwObjectReferenceSelectFieldControl.DOCUMENT_FUNCTION_PLUGINS_NODE, OwObjectReferenceSelectFieldControl.DOCUMENT_FUNCTION_PLUGIN_ID);
        OwDocumentFunction docFunction;
        OwMainAppContext ctx = (OwMainAppContext) getContext();
        for (@SuppressWarnings("rawtypes")
        Iterator iterator = docFunctionsIdUtil.iterator(); iterator.hasNext();)
        {
            String docFunctionId = ((OwXMLUtil) iterator.next()).getSafeTextValue("");
            if (docFunctionId.isEmpty())
            {
                continue;
            }
            try
            {
                docFunction = ctx.getConfiguration().getDocumentFunction(docFunctionId);
            }
            catch (Exception e)
            {
                LOG.warn("No document function with ID " + docFunctionId, e);
                continue;
            }
            docFunctions.add(docFunction);
        }

        m_objectListView.setDocumentFunctionPluginList(docFunctions);

        // === create search criteria view
        addView(m_criteriaView, MAIN_REGION, null);

        // set search template in criteria view
        m_search = m_searchtemplate.getSearch(true);

        m_criteriaView.setCriteriaList(m_search.getCriteriaList(OwSearchNode.FILTER_HIDDEN));

        m_criteriaView.setFieldProvider(m_searchtemplate);

        addView(m_Menu, MENU_REGION, null);
        // === create submit button
        enterButtonHandler = m_Menu.addFormMenuItem(this, getContext().localize("dmsdialogs.OwObjectManagerDialog.search", "Search"), "SubmitSearch", getContext().localize("dmsdialogs.OwObjectManagerDialog.searchtooltip", "Search"), getFormName());

        // === add objectlistview
        addView(m_objectListView, RESULT_REGION, null);

        // set eventlistener
        m_objectListView.setEventListner(this);

        // set columns for the list
        m_objectListView.setColumnInfo(m_searchtemplate.getColumnInfoList());

        ((OwObjectListViewRow) m_objectListView).setMimeTypeContext("OwContractManagement");

        performSearch();
    }

    /** called when user clicked submit search */
    public void onSubmitSearch(HttpServletRequest request_p, Object user_p) throws Exception
    {
        if (!m_criteriaView.onSubmitSearch(request_p))
        {
            // do not perform search upon criteria errors
            return;
        }

        performSearch();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void performSearch() throws Exception
    {
        // get property names to retrieve from column list
        List properties = new ArrayList();

        Iterator it = m_searchtemplate.getColumnInfoList().iterator();
        while (it.hasNext())
        {
            OwFieldColumnInfo columninfo = (OwFieldColumnInfo) it.next();
            properties.add(columninfo.getPropertyName());
        }

        // Search and display results
        m_objectListView.setObjectList(((OwMainAppContext) getContext()).getNetwork().doSearch(m_search, m_searchtemplate.getSort(1), properties, 100, 0), null);
    }

    /** get the object that was selected */
    public List<OwObject> getSelection()
    {
        return m_selectedObject;
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        // MVC View JSP Page
        serverSideDesignInclude("dmsdialogs/OwObjectReferenceSelectDialog.jsp", w_p);
        getContext()
                .registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, m_Menu.getNavigateEventURL(enterButtonHandler), getFormName(), getContext().localize("dmsdialogs.OwObjectManagerDialog.search", "Search"));
    }

    /** called when user clicks a select button, fUseSelectButton_p must have been set to display select buttons 
     *
     * @param object_p OwObject object that was selected
     * @param parent_p OwObject parent if available, or null
     */
    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        m_selectedObject = new ArrayList<OwObject>();
        m_selectedObject.add(object_p);

        closeDialog();
    }

    /** called when uses clicks on a sort header and the sort changes
     * @param newSort_p OwSort new sort
     * @param strSortProperty_p String Property Name of sort property that was changed
     * */
    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
        // ignore
    }

    /**
     * ignored
     */
    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {

    }

    /** called when uses clicks on a folder, used to redirect folder events an bypass the mimemanager
     * 
     * @param obj_p OwObject folder object that was clicked
     * @return boolean true = event was handled, false = event was not handled, do default handler
     * 
     * @throws Exception
     */
    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        // event was handled
        return true;
    }

    /** get the style class name for the row
    *
    * @param iIndex_p int row index
    * @param obj_p current OwObject
    *
    * @return String with style class name, or null to use default
    */
    public String onObjectListViewGetRowClassName(int iIndex_p, OwObject obj_p)
    {
        // use default
        return null;
    }

    protected String usesFormWithAttributes()
    {
        return "action=\"" + m_Menu.getNavigationFormAction(enterButtonHandler) + "\"";
    }

    /**
     * Factory to create Menu view for search button
     * @return OwSubMenuView
     */
    protected OwSubMenuView createMenuView()
    {
        return new OwSubMenuView();
    }

    /**
     * Factory to create ObjectListView used for the results representation
     * @return OwObjectListView
     */
    protected OwObjectListView createListView()
    {
        return new OwObjectListViewRow(OwObjectListView.VIEW_MASK_USE_SELECT_BUTTON | OwObjectListView.VIEW_MASK_INSTANCE_PLUGINS);
    }

    /**
     * Factory for OwSearchCriteriaView
     * @return OwSearchCriteriaView
     */
    protected OwSearchCriteriaView createSerachView()
    {
        return new OwSearchCriteriaView();
    }
}
