package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSearchCriteriaView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.bpm.OwCaseObject;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Display the history of an object.
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
public class OwObjectHistoryView extends OwLayout
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectHistoryView.class);

    /** region identifier for the layout */
    public static final int OBJECT_LIST_REGION = 1;
    /** region identifier for the layout */
    public static final int SEARCH_CRITERIA_REGION = 2;
    /** region identifier for the layout */
    public static final int MENU_REGION = 3;

    /** mask value for the history view, enable object history */
    public static final int VIEW_MASK_HISTORY_SCOPE_OBJECT = 0x0001;
    /** mask value for the history view, enable case (bpm) history */
    public static final int VIEW_MASK_HISTORY_SCOPE_CASE = 0x00002;

    /** filters the views to be displayed*/
    protected int m_iViewMask = VIEW_MASK_HISTORY_SCOPE_OBJECT;

    /** object to find the parents of */
    protected OwObject m_object;

    /** objectlist that displays the history entries */
    protected OwObjectListView m_ObjectListView = new OwObjectListViewRow(0);

    /** displays the filter criteria */
    protected OwSearchCriteriaView m_SearchCriteriaView;

    /** the history entries */
    protected OwObjectCollection m_historyEntries;

    /** the searchtemplate to be used for filtering history entries */
    protected OwSearchTemplate m_searchtemplate;

    /** max number of history entries to be retrieved */
    protected int m_iMaxSize = 100;

    /** List with property names as columninfos if no searchTemplate_p is provided */
    protected Collection m_propertyColumnList;

    /** constructs a history view to display the history of an object
     *
     * @param searchTemplate_p OwSearchTemplate to use to filter and display the history events, or null if no search is available
     * @param columnInfoList_p List with property names as columninfos if no searchTemplate_p is provided
     * @param iMaxSize_p int max number of history entries to be retrieved
     * 
     */
    public OwObjectHistoryView(OwSearchTemplate searchTemplate_p, Collection columnInfoList_p, int iMaxSize_p) throws Exception
    {
        m_iMaxSize = iMaxSize_p;
        m_searchtemplate = searchTemplate_p;
        m_propertyColumnList = columnInfoList_p;
    }

    /** determine the views to be displayed by masking them with their flag
    *
    * @param iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
    */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** check if view should be displayed or is masked out
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /** create a list of OwFieldColumnInfo out of a property string list
     */
    protected Collection createColumnInfo(Collection propertyList_p) throws Exception
    {
        List columnInfoList = new ArrayList();

        Iterator it = propertyList_p.iterator();

        while (it.hasNext())
        {
            String strPropertyName = (String) it.next();

            // get display name
            OwFieldDefinition fielddef = null;
            try
            {
                // resolve display name
                fielddef = ((OwMainAppContext) getContext()).getHistoryManager().getFieldDefinition(strPropertyName, null);

                // add column info
                columnInfoList.add(new OwStandardFieldColumnInfo(fielddef));
            }
            catch (OwObjectNotFoundException e)
            {
                // === property not found
                // Continue
                // just set a warning when property load failed, we still keep continue working at least with the remaining properties
                LOG.warn("Property could not be resolved for OwObjectHistoryView list: " + strPropertyName, e);
                continue;
            }
        }

        return columnInfoList;
    }

    /** init the view after the context is set
     */
    protected void init() throws Exception
    {
        super.init();

        // === create views
        addView(m_ObjectListView, OBJECT_LIST_REGION, null);

        if (m_searchtemplate != null)
        {
            // === create searchtemplate view
            m_SearchCriteriaView = new OwSearchCriteriaView();
            addView(m_SearchCriteriaView, SEARCH_CRITERIA_REGION, null);

            // === create submit button
            OwSubMenuView m_Menu = new OwSubMenuView();
            m_Menu.addFormMenuItem(this, getContext().localize("dmsdialogs.views.OwObjectHistoryView.search", "Search"), "SubmitSearch", getContext().localize("dmsdialogs.views.OwObjectHistoryView.searchtooltip", "Search for history entries"),
                    m_SearchCriteriaView.getFormName());
            addView(m_Menu, MENU_REGION, null);

            // === init search view
            // get a search node from the template
            m_SearchCriteriaView.setCriteriaList(m_searchtemplate.getSearch(false).getCriteriaList(OwSearchNode.FILTER_HIDDEN));
            m_SearchCriteriaView.setFieldProvider(m_searchtemplate);

            // === init objectlist
            // get the column info
            m_ObjectListView.setColumnInfo(m_searchtemplate.getColumnInfoList());
        }
        else
        {
            // === no search criteria view, so we must at least configure the columns
            m_ObjectListView.setColumnInfo(createColumnInfo(m_propertyColumnList));
            m_propertyColumnList = null;
        }

        // init the sort object 
        m_ObjectListView.setSort(new OwSort());
    }

    /** called when user clicked submit search */
    public void onSubmitSearch(HttpServletRequest request_p, Object user_p) throws Exception
    {
        if (m_SearchCriteriaView.onSubmitSearch(request_p))
        {
            searchHistory();
        }
    }

    /** get the form used for the edit fields
     *
     * @return String form name
     */
    public String getFormName()
    {
        // view has no form 
        return null;
    }

    /** set the object to find the filed records for
     * @param obj_p OwObject
     */
    public void setObjectRef(OwObject obj_p) throws Exception
    {
        m_object = obj_p;
        m_historyEntries = null;
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     * @param iIndex_p int tab index of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        // We search only on activate, if there is no template defined
        if (null == m_searchtemplate)
        {
            searchHistory();
        }
    }

    /** perform a search over the history with the search template if defined
     */
    private void searchHistory() throws Exception
    {
        //////////////////////////////////////
        // TODO: Build request property list
        OwSearchNode search = null;
        if (null != m_searchtemplate)
        {
            search = m_searchtemplate.getSearch(false);
        }

        if (hasViewMask(VIEW_MASK_HISTORY_SCOPE_OBJECT))
        {
            // get the object history
            m_historyEntries = ((OwMainAppContext) getContext()).getHistoryManager().doObjectSearch(m_object, search, m_ObjectListView.getSort(), null, null, m_iMaxSize);
        }

        if (hasViewMask(VIEW_MASK_HISTORY_SCOPE_CASE))
        {
            try
            {
                // get the corresponding case (bpm) history if available
                int workitemCount = ((OwCaseObject) m_object).getWorkitemCount();
                for (int i = 0; i < workitemCount; i++)
                {
                    OwObjectReference caseref = ((OwCaseObject) m_object).getWorkitem(i);

                    OwObjectCollection caseSearch = ((OwMainAppContext) getContext()).getHistoryManager().doObjectSearch(caseref, search, m_ObjectListView.getSort(), null, null, m_iMaxSize);

                    if (null == m_historyEntries)
                    {
                        m_historyEntries = caseSearch;
                    }
                    else
                    {
                        m_historyEntries.addAll(caseSearch);
                    }
                }
            }
            catch (ClassCastException e)
            {
                // ignore, m_object is not a case object
            }
        }

        m_ObjectListView.setObjectList(m_historyEntries, null);
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("dmsdialogs/OwObjectHistoryView.jsp", w_p);
    }
}