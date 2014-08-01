package com.wewebu.ow.server.plug.owbpm;

import java.io.Writer;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMaxMinButtonControlView;
import com.wewebu.ow.server.app.OwMenuView;
import com.wewebu.ow.server.app.OwSearchCriteriaView;
import com.wewebu.ow.server.app.OwSmallSubMenuView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewControl;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Workdesk BPM Plugin.<br/>
 * View for the workitems in a queue.
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
public class OwBPMWorkItemListView extends OwLayout implements OwObjectListView.OwObjectListViewEventListner
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwBPMWorkItemListView.class);

    /** region of the work item list */
    public static final int WORKITEM_LIST_REGION = 1;
    /** region of the work item list */
    public static final int SEARCH_CRITERIA_REGION = 2;
    /** name of the min max control region */
    public static final int MIN_MAX_CONTROL_VIEW = 3;
    /** name of the button / menu region */
    public static final int SEARCH_CRITERIA_BUTTON_REGION = 4;
    /** name of the option menu region */
    public static final int OPTION_MENU_REGION = 5;
    /** name of the max object list control region */
    public static final int OBJECT_LIST_CONTROL_REGION = 6;

    /** view to display and edit the criteria */
    protected OwSearchCriteriaView m_SearchCriteriaView = new OwSearchCriteriaView();

    /** the buttons for the filter criteria form */
    protected OwMenuView m_FilterCriteriaBtnView;

    /** the buttons for the filter menu */
    protected OwMenuView m_OptionMenuView;

    /** currently selected queue */
    protected OwBPMVirtualQueue m_currentQueue = null;

    /**  control for list view selection*/
    protected OwObjectListViewControl m_listcontrol = new OwObjectListViewControl();

    /** index of the resubmit filter menu toggle button */
    protected int m_iResubmitFilterToggleBtnIndex;
    /**
     * the ID of search button.
     */
    private int m_searchButtonId;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === list control
        addView(m_listcontrol, OBJECT_LIST_CONTROL_REGION, null);
        addViewReference(m_listcontrol.getViewReference(), WORKITEM_LIST_REGION);

        // set config node after init
        m_listcontrol.setConfigNode(((OwMasterDocument) getDocument()).getConfigNode().getSubNode("ResultListViews"));

        // activate view, select persistent index
        m_listcontrol.activateListView();

        // === object list
        getListView().setEventListner(this);

        // override the object rendition with a custom field control
        getListView().getFieldManager().attachFieldControlByType("com.wewebu.ow.server.ecm.OwObjectReference",
                new OwBPMAttachmentFieldControl(((OwBPMDocument) getDocument()).getConfigNode().getSafeStringList(OwBPMAttachmentFieldControl.PLUGIN_CONFIG_ID_ATTACHMENT_FUNCTION)), null);
        getListView().getFieldManager().attachFieldControlByType("com.wewebu.ow.server.ecm.OwObject",
                new OwBPMAttachmentFieldControl(((OwBPMDocument) getDocument()).getConfigNode().getSafeStringList(OwBPMAttachmentFieldControl.PLUGIN_CONFIG_ID_ATTACHMENT_FUNCTION)), null);

        // add groupbox control as well
        getListView().getFieldManager().attachFieldControlByClass(OwWorkitemContainer.GROUPBOX_PROPERTY_NAME, new OwBPMGroupBoxControl(this), null);

        // === add min max control
        addView(new OwMaxMinButtonControlView(this), MIN_MAX_CONTROL_VIEW, null);

        // === add search template 
        addView(m_SearchCriteriaView, SEARCH_CRITERIA_REGION, null);

        // === create menu for filter form buttons
        m_FilterCriteriaBtnView = new OwSubMenuView();
        addView(m_FilterCriteriaBtnView, SEARCH_CRITERIA_BUTTON_REGION, null);

        // === add buttons
        // search button
        m_searchButtonId = m_FilterCriteriaBtnView.addFormMenuItem(this, getContext().localize("owsearch.OwBPMWorkItemListView.search", "Search"), "DoSearch", null, m_SearchCriteriaView.getFormName());

        // reset button
        m_FilterCriteriaBtnView.addMenuItem(this, getContext().localize("owsearch.OwBPMWorkItemListView.reset", "Reset"), "DoReset", null);

        // === create menu for filter menu
        m_OptionMenuView = new OwSmallSubMenuView();
        addView(m_OptionMenuView, OPTION_MENU_REGION, null);

        // === add buttons
        // resubmit view button
        String title = getContext().localize("owsearch.OwBPMWorkItemListView.resubmitview", "Resubmission View");
        m_iResubmitFilterToggleBtnIndex = m_OptionMenuView.addMenuItem(this, title, null, "ToggleResubmitFilter", null, title);
    }

    /** determine if region exists
     *
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
            case SEARCH_CRITERIA_REGION:
            {
                try
                {
                    return (m_currentQueue.getSearchTemplate() != null) && (m_currentQueue.getSearchTemplate().getSearch(false).getCriteriaList(OwSearchNode.FILTER_HIDDEN).size() != 0);
                }
                catch (Exception e)
                {
                    return false;
                }
            }

            default:
                return super.isRegion(iRegion_p);
        }

    }

    public OwBPMVirtualQueue getCurrentQueue()
    {
        return m_currentQueue;
    }

    /** get the list view
     * 
     * @return OwObjectListView
     */
    public OwObjectListView getListView()
    {
        return m_listcontrol.getObjectListView();
    }

    /** event called when user clicked DoReset
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onDoReset(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        if (m_currentQueue.getSearchTemplate() != null)
        {
            // get a refreshed search from the template
            m_SearchCriteriaView.setCriteriaList(m_currentQueue.getSearchTemplate().getSearch(true).getCriteriaList(OwSearchNode.FILTER_HIDDEN));
            m_SearchCriteriaView.setJspFormular(m_currentQueue.getSearchTemplate().getJspLayoutPage());
            m_SearchCriteriaView.setHTMLFormular(m_currentQueue.getSearchTemplate().getHtmlLayout());
        }

        // === update list
        update();
    }

    /** event called when user clicked DoSearch
     *   @param request_p a  {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onDoSearch(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // === update criteria from HTML Form
        if (!m_SearchCriteriaView.onSubmitSearch(request_p))
        {
            // do not perform search upon criteria errors
            return;
        }

        // === update list
        update();
    }

    /** event called when user clicked resubmit view button
     *   @param request_p a  {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onToggleResubmitFilter(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        m_currentQueue.toggleResubmitFilter();
        m_OptionMenuView.check(m_currentQueue.isResubmitFilter(), m_iResubmitFilterToggleBtnIndex);

        update();
    }

    /**
     * activate the target from a navigation module. Called when menu item was
     * pressed for this target.
     * 
     * @param iIndex_p tab iIndex of Navigation 
     * @param oReason_p
     *            User Object which was submitted when target was attached to
     *            the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        // set new queue
        m_currentQueue = (OwBPMVirtualQueue) oReason_p;

        // inform views
        getDocument().update(this, OwUpdateCodes.UPDATE_OBJECT_CHILDS, m_currentQueue);

        if (null != m_currentQueue.getSearchTemplate())
        {
            m_SearchCriteriaView.setCriteriaList(m_currentQueue.getSearchTemplate().getSearch(false).getCriteriaList(OwSearchNode.FILTER_HIDDEN));
            m_SearchCriteriaView.setHTMLFormular(m_currentQueue.getSearchTemplate().getHtmlLayout());
            m_SearchCriteriaView.setJspFormular(m_currentQueue.getSearchTemplate().getJspLayoutPage());
            m_SearchCriteriaView.setFieldProvider(m_currentQueue.getSearchTemplate());
            // set columns from search template, if available. User settings otherwise
            OwSearchTemplate st = m_currentQueue.getSearchTemplate();
            Collection stcolumns = st.getColumnInfoList();
            if ((null != stcolumns) && (!stcolumns.isEmpty()))
            {
                // set columns from search template
                getListView().setColumnInfo(st.getColumnInfoList());
            }
            else
            {
                // set columns from user preferences
                getListView().setColumnInfo(m_currentQueue.getColumnInfo());
            }
        }
        else
        {
            // template is not valid
            m_SearchCriteriaView.setCriteriaList(null);
            m_SearchCriteriaView.setHTMLFormular(null);
            m_SearchCriteriaView.setJspFormular(null);
            m_SearchCriteriaView.setFieldProvider(null);
            // set columns from user preferences
            getListView().setColumnInfo(m_currentQueue.getColumnInfo());
        }

        // === set sort object
        getListView().setSort(m_currentQueue.getSort());

        // === set filter
        getListView().setFilter(m_currentQueue.getObjectListFilter());

        try
        {
            // === refresh the list
            update();
        }
        catch (Exception e)
        {
            throw new OwAccessDeniedException(getContext().localize("plug.owbpm.OwBPMWorkItemListView.noaccesstoview", "Possibly, you do not have the required rights to view these objects."), e);
        }

        // === update resubmit filter toggle button
        m_OptionMenuView.enable(m_iResubmitFilterToggleBtnIndex, m_currentQueue.canResubmit());
        m_OptionMenuView.check(m_currentQueue.isResubmitFilter(), m_iResubmitFilterToggleBtnIndex);
    }

    /** refresh the list */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void update() throws Exception
    {
        OwObjectCollection objectList = getListView().getObjectList();
        if (objectList != null)
        {
            objectList.clear();
        }

        OwBPMDocument document = (OwBPMDocument) this.getDocument();
        OwWorkitemContainer queueFolder = m_currentQueue.getQueueFolder();
        if (document.isUsePaging() && queueFolder instanceof OwPageableObject)
        {
            OwLoadContext loadContext = document.createLoadContext(m_currentQueue);
            loadContext.setFilter(getListView().getFilterSearch());

            OwIterable<OwObject> iterable = ((OwPageableObject) queueFolder).getChildren(loadContext);
            getListView().setObjectIterable(iterable, queueFolder);
        }
        else
        {
            // === update workitems in the list
            getListView().setObjectList(m_currentQueue.getWorkItems(getListView().getFilterSearch()), queueFolder);
        }
        // === also update the count of all the other queues
        getDocument().update(this, OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS, null);
    }

    /** register an eventlistener with this view to receive notifications
     * @param eventlister_p OwClientRefreshContext interface
     * */
    public void setRefreshContext(OwClientRefreshContext eventlister_p)
    {
        getListView().setRefreshContext(eventlister_p);
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("owbpm/OwBPMWorkItemListView.jsp", w_p);
        String navigateURL = m_FilterCriteriaBtnView.getNavigateEventURL(this.m_searchButtonId);
        getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, navigateURL, null, getContext().localize("owsearch.OwBPMWorkItemListView.search", "Suchen"));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
        OwBPMDocument document = (OwBPMDocument) this.getDocument();
        OwWorkitemContainer queueFolder = m_currentQueue.getQueueFolder();
        if (document.isUsePaging() && queueFolder instanceof OwPageableObject)
        {
            OwLoadContext loadContext = document.createLoadContext(m_currentQueue);
            loadContext.setSorting(newSort_p);
            OwIterable iterable = ((OwPageableObject) queueFolder).getChildren(loadContext);
            getListView().setObjectIterable(iterable, queueFolder);
        }
        else
        {
            OwObjectCollection list = getListView().getObjectList();
            if (null != list)
            {
                list.sort(newSort_p);
                getListView().setObjectList(list, queueFolder);
            }
        }
    }

    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {

    }

    @SuppressWarnings("unchecked")
    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {
        OwBPMDocument document = (OwBPMDocument) this.getDocument();
        OwWorkitemContainer queueFolder = m_currentQueue.getQueueFolder();
        if (document.isUsePaging() && queueFolder instanceof OwPageableObject)
        {
            OwLoadContext loadContext = document.createLoadContext(m_currentQueue);
            loadContext.setFilter(filterNode_p);

            OwIterable<OwObject> iterable = ((OwPageableObject) queueFolder).getChildren(loadContext);
            getListView().setObjectIterable(iterable, queueFolder);
        }
        else
        {
            // === update workitems in the list
            getListView().setObjectList(m_currentQueue.getWorkItems(filterNode_p), null);
        }
    }

    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {

        return false;
    }

    /** get the currently opened work item container
     * 
     * @return OwWorkitemContainer
     * @throws OwObjectNotFoundException
     */
    public OwWorkitemContainer getCurrentWorkitemcontainer() throws OwObjectNotFoundException
    {
        if (null == m_currentQueue)
        {
            String msg = "OwBPMWorkItemListView.getCurrentWorkitemcontainer: The current queue is null.";
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }
        else
        {
            return m_currentQueue.getQueueFolder();
        }
    }

    public void updateGroupBoxSelectFilter() throws Exception
    {
        // make sure filter is on
        getListView().getFilter().setActive(OwWorkitemContainer.GROUPBOX_PROPERTY_NAME, true);
        update();
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
        String sStyle = getCurrentQueue().applyRules(obj_p);

        if (sStyle == null)
        {
            return null;
        }
        else
        {
            return sStyle;
        }
    }
}