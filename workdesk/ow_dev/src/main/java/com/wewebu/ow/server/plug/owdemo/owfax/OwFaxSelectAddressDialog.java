package com.wewebu.ow.server.plug.owdemo.owfax;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSearchCriteriaView;
import com.wewebu.ow.server.app.OwStandardDialog;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchSQLOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardWildCardDefinition;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Demo send Fax dialog, search criteria.
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
public class OwFaxSelectAddressDialog extends OwStandardDialog implements OwObjectListView.OwObjectListViewEventListner, OwFieldDefinitionProvider
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFaxSelectAddressDialog.class);

    /** callback reference to the calling dialog */
    protected OwSendFaxDialog m_FaxDialog;

    /** the search criteria view */
    protected OwSearchCriteriaView m_criteriaView = new OwSearchCriteriaView();

    /** search tree to work on */
    protected OwSearchNode m_search;

    /** objectlistview to display the addresses */
    protected OwObjectListView m_addressView = new OwObjectListViewRow(OwObjectListView.VIEW_MASK_USE_SELECT_BUTTON);

    /** the search submit menu button */
    protected OwSubMenuView m_Menu = new OwSubMenuView();

    /** construct address select dialog init with caller for callback */
    public OwFaxSelectAddressDialog(OwSendFaxDialog caller_p)
    {
        m_FaxDialog = caller_p;
    }

    /** called when the view should create its HTML content to be displayed
     */
    protected void init() throws Exception
    {
        super.init();

        // === create search criteria view
        addView(m_criteriaView, MAIN_REGION, null);

        // create a search template form ECM system
        OwSearchTemplate searchtemplate = (OwSearchTemplate) ((OwMainAppContext) getContext()).getNetwork().getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE, "AdressSearch", false, false);
        searchtemplate.init(this);

        // set search template in criteria view
        m_search = searchtemplate.getSearch(true);

        m_criteriaView.setCriteriaList(m_search.getCriteriaList(OwSearchNode.FILTER_HIDDEN));

        m_criteriaView.setFieldProvider(searchtemplate);

        // === create submit button
        m_Menu.addFormMenuItem(this, getContext().localize("plug.owfax.OwFaxSelectAddressDialog.search", "Search"), "SubmitSearch", getContext().localize("plug.owfax.OwFaxSelectAddressDialog.searchdescr", "Search addresses"),
                m_criteriaView.getFormName());

        addView(m_Menu, MENU_REGION, null);

        // === add objectlistview
        addView(m_addressView, OwSendFaxDialog.RESULT_REGION, null);

        // set eventlistener
        m_addressView.setEventListner(this);

        // set columns for the list
        m_addressView.setColumnInfo(searchtemplate.getColumnInfoList());
    }

    /** called when user clicked submit search */
    public void onSubmitSearch(HttpServletRequest request_p, Object user_p) throws Exception
    {
        if (!m_criteriaView.onSubmitSearch(request_p))
        {
            // do not perform search upon criteria errors
            return;
        }

        // create a SQL statement
        StringWriter SQLStatement = new StringWriter();
        OwSearchSQLOperator sqlOperator = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_MS_ACCESS);
        if (sqlOperator.createSQLSearchCriteria(m_search, SQLStatement))
        {
            // submit search
            OwObjectCollection resultObjects = SearchAddresses("select * from Adressen where " + SQLStatement.toString());

            m_addressView.setObjectList(resultObjects, null);
        }
    }

    /** submit search to database */
    private OwObjectCollection SearchAddresses(String strSelectStatement_p) throws Exception
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwFaxSelectAddressDialog.SearchAddresses: SelectStatement = " + strSelectStatement_p);
        }

        OwObjectCollection resultObjectList = new OwStandardObjectCollection();

        // Load the JDBC-ODBC bridge driver
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

        // setup the properties 
        java.util.Properties prop = new java.util.Properties();
        prop.put("charSet", "Big5");
        prop.put("user", "");
        prop.put("password", "");

        // Connect to the database
        java.sql.Connection con = java.sql.DriverManager.getConnection("jdbc:odbc:AD", prop);

        java.sql.Statement st = con.createStatement();
        java.sql.ResultSet res = st.executeQuery(strSelectStatement_p);

        while (res.next())
        {
            resultObjectList.add(new OwAddressObject(res, getContext()));
        }

        st.close();
        con.close();

        return resultObjectList;
    }

    /** called when user clicks a select button, fUseSelectButton_p must have been set to display select buttons 
     *
     * @param object_p OwObject object that was selected
     * @param parent_p OwObject parent if available, or null
     */
    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        // get faxnumber from object, use the property names defined in the dummy adaptor which we reused for the demo
        String strFaxnumber = object_p.getProperty(com.wewebu.ow.server.ecmimpl.owdummy.OwDummyFileObject.OwDummyFileObjectClass.FAX_PROPERTY).getValue().toString();
        m_FaxDialog.setFaxNumber(strFaxnumber);

        // close the dialog after user has made its selection
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

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // MVC View JSP Page
        serverSideDesignInclude("demo/OwFaxDemoDialogLayout.jsp", w_p);
    }

    /** get a field definition for the given name and resource
     *
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null 
     *
     * @return OwFieldDefinition or throws OwObjectNotFoundException
     */
    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        return OwAddressObject.m_classDescription.getPropertyClass(strFieldDefinitionName_p);
    }

    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {

    }

    /** called when uses clicks on a folder, used to redirect folder events an bypass the MIME Manager
     * 
     * @param obj_p OwObject folder object that was clicked
     * @return boolean true = event was handled, false = event was not handled, do default handler
     * 
     * @throws Exception
     */
    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        // event was not handled
        return false;
    }

    /**
     *<p>
     * Like wild card definitions flyweight.
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
    private static class OwLikeWildCardDefinitions
    {
        private Vector m_definitions;

        public OwLikeWildCardDefinitions()
        {
            m_definitions = new Vector();

            m_definitions.add(new OwStandardWildCardDefinition("*", null, OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1("owdemo.OwFaxSelectAddressDialog.WILD_CARD_TYPE_MULTI_CHAR", "(%1) replaces any characters")));
            m_definitions.add(new OwStandardWildCardDefinition("_", null, OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR, new OwString1("owdemo.OwFaxSelectAddressDialog.WILD_CARD_TYPE_SINGLE_CHAR", "(%1) replaces any character")));
        }

        public Collection getDefinitions()
        {
            return m_definitions;
        }
    }

    /** like wild card definitions flyweight */
    private OwLikeWildCardDefinitions m_likewildcarddefinitions;

    /** get a collection of wild card definitions that are allowed for the given field, resource and search operator
     * 
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null
     * @param iOp_p search operator as defined in OwSearchOperator CRIT_OP_...
     * 
     * @return Collection of OwWildCardDefinition, or null if no wildcards are defined
     * @throws Exception
     */
    public java.util.Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception
    {
        switch (iOp_p)
        {
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
            case OwSearchOperator.CRIT_OP_LIKE:
            {
                if (m_likewildcarddefinitions == null)
                {
                    m_likewildcarddefinitions = new OwLikeWildCardDefinitions();
                }

                return m_likewildcarddefinitions.getDefinitions();
            }

            default:
                return null;
        }
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
}