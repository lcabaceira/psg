package com.wewebu.ow.server.plug.owrecord;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwSearchCriteriaView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * View for the search template, to refine the child list of a open folder.
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
public class OwRecordSearchTemplateView extends OwLayout
{
    /** region of the search criteria */
    public static final int SEARCH_CRITERIA_REGION = 1;
    /** region of the submit button */
    public static final int MENU_REGION = 2;
    /** region of max size input box */
    public static final int MAX_SIZE_REGION = 3;
    /** region for the search tree dump for debugging only */
    public static final int DEBUG_SEARCH_DUMP_REGION = 4;

    /** query string key for the result list size */
    public static final String MAX_RESULT_LIST_KEY = "reslistsize";

    /** the maximum size that is possible for the maximum size parameter */
    protected int m_iMaxSizeMax;

    /** view to display and edit the criteria */
    protected OwSearchCriteriaView m_SearchCriteriaView;
    /** currently set search template */
    protected OwSearchTemplate m_searchtemplate;

    /** the buttons for the search form */
    protected OwSubMenuView m_MenuView;

    /** index of the submit button */
    protected int m_iSubmitBtnIndex;

    /** new search max list size */
    protected int m_searchListSize;

    /** get the internal criteria view
     * 
     * @return OwSearchCriteriaView
     */
    public OwSearchCriteriaView getCriteriaView()
    {
        return m_SearchCriteriaView;
    }

    /** init the target after the context is set.
      */
    protected void init() throws Exception
    {
        super.init();

        // === add the criteria region view
        m_SearchCriteriaView = new OwSearchCriteriaView();
        m_SearchCriteriaView.setExternalFormTarget(getFormTarget());
        addView(m_SearchCriteriaView, SEARCH_CRITERIA_REGION, null);

        // === create menu for search form buttons
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, MENU_REGION, null);

        // add search button
        m_iSubmitBtnIndex = m_MenuView.addFormMenuItem(this, getContext().localize("owrecord.OwRecordSearchTemplateView.search", "Search"), "DoSearch", null, getFormName());
        m_MenuView.addFormMenuItem(this, getContext().localize("owrecord.OwRecordSearchTemplateView.reset", "Reset"), "DoReset", null, getFormName());

        // === get the maximum possible size from plugin descriptor
        m_iMaxSizeMax = ((OwMasterDocument) getDocument()).getConfigNode().getSafeIntegerValue("MaxSizeMax", 200);
        int m_iMaxChild = ((OwRecordDocument) getDocument()).getMaxChildSize();

        m_searchListSize = java.lang.Math.min(m_iMaxSizeMax, m_iMaxChild);

        ((OwRecordDocument) getDocument()).setMaxChildSize(m_searchListSize);

    }

    /** called by the framework to update the view when OwDocument.Update was called
     *
     *  NOTE:   We can not use the onRender method to update,
     *          because we do not know the call order of onRender.
     *          onUpdate is always called before all onRender methods.
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param  iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.SET_NEW_OBJECT:
            case OwUpdateCodes.UPDATE_OBJECT_FOLDER_CHILDS:
            case OwUpdateCodes.UPDATE_OBJECT_CHILDS:
            {
                // === add the folder opened in the record document to the set

                OwSearchTemplate searchtemplate = ((OwRecordDocument) getDocument()).getSearchTemplate();

                if (searchtemplate != null)
                {
                    // === search template defined for this node
                    if (searchtemplate != m_searchtemplate)
                    {
                        // === search template differs from previous one, set it as current
                        m_searchtemplate = searchtemplate;
                        List criterias = searchtemplate.getSearch(false).getCriteriaList(OwSearchNode.FILTER_HIDDEN);
                        m_SearchCriteriaView.setCriteriaList(criterias);
                        m_SearchCriteriaView.setHTMLFormular(searchtemplate.getHtmlLayout());
                        m_SearchCriteriaView.setJspFormular(searchtemplate.getJspLayoutPage());
                        m_SearchCriteriaView.setFieldProvider(m_searchtemplate);

                        // signal that a valid search with editable criteria exits
                        ((OwRecordDocument) getDocument()).enableSearchTemplateView(criterias.size() > 0);
                    }
                }
                else
                {
                    // === no search template defined
                    // clear current template
                    m_searchtemplate = null;
                    m_SearchCriteriaView.setCriteriaList(null);
                    m_SearchCriteriaView.setHTMLFormular(null);
                    m_SearchCriteriaView.setFieldProvider(null);
                }
            }
                break;
        }
    }

    /** event called when user clicked DoReset
     *   @param request_p  {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onDoReset(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwSearchTemplate searchtemplate = ((OwRecordDocument) getDocument()).getSearchTemplate();

        if (searchtemplate != null)
        {
            // get completely refreshed Search
            OwSearchNode currentSearch = searchtemplate.getSearch(true);
            // reset the search to default
            currentSearch.reset();
            // insert the criteria list with empty/default non-hidden criteria
            m_SearchCriteriaView.setCriteriaList(currentSearch.getCriteriaList(OwSearchNode.FILTER_HIDDEN));
            m_SearchCriteriaView.setHTMLFormular(searchtemplate.getHtmlLayout());
            m_SearchCriteriaView.setJspFormular(searchtemplate.getJspLayoutPage());
            // fire update Event, cause we don't have a reset event
            getDocument().update(this, OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);

        }
    }

    /** event called when user clicked the search submit button
     *   @param request_p a {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onDoSearch(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        // === update criteria from HTML Form
        if (!m_SearchCriteriaView.onSubmitSearch(request_p))
        {
            // do not perform update of folder content upon criteria errors
            return;
        }

        // lock after update
        if (((OwRecordDocument) getDocument()).supportLock() && ((OwRecordDocument) getDocument()).getCurrentRootFolder().canLock())
        {
            ((OwRecordDocument) getDocument()).getCurrentRootFolder().setLock(true);
        }

        getDocument().update(this, OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (m_searchtemplate != null)
        {
            w_p.write("<form name='" + getFormName() + "' method='post' action='" + m_MenuView.getNavigationFormAction(m_iSubmitBtnIndex) + "'>");
            if (m_SearchCriteriaView != null)
            {
                m_SearchCriteriaView.renderRegion(w_p, OwSearchCriteriaView.ERRORS_REGION);
            }
            serverSideDesignInclude("owrecord/OwRecordSearchTemplateView.jsp", w_p);
            w_p.write("</form>");

            // === enable return key for search
            getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, m_MenuView.getNavigationFormAction(m_iSubmitBtnIndex), getFormName(),
                    getContext().localize("owrecord.OwRecordSearchTemplateView.search", "Suchen"));
        }
    }

    /** render the views of the region
    *
    * @param w_p Writer object to write HTML to
    * @param iRegion_p ID of the region to render
    */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case DEBUG_SEARCH_DUMP_REGION:
            {
                // === dump search tree
                if (m_searchtemplate != null)
                {
                    m_searchtemplate.getSearch(false).dump(w_p);
                }
            }
                break;

            case MAX_SIZE_REGION:
                w_p.write("<input type=\"text\" class=\"OwInputControl OwInputControlString OwInputControl_ow_MaxNoSearch\" size=\"4\" name=\"");
                w_p.write(MAX_RESULT_LIST_KEY);
                w_p.write("\" id=\"");
                w_p.write(MAX_RESULT_LIST_KEY);
                w_p.write("\" value=\"");
                w_p.write(String.valueOf(m_searchListSize));
                w_p.write("\">");
                break;

            default:
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** update the target after a form event, so it can set its form fields
     *
     * @param request_p HttpServletRequest
     * @param fSave_p boolean true = save the changes of the form data, false = just update the form data, but do not save
     *
     * @return true = field data was valid, false = field data was invalid
     */
    public boolean updateExternalFormTarget(javax.servlet.http.HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        m_SearchCriteriaView.updateExternalFormTarget(request_p, fSave_p);

        String sMaxSize = request_p.getParameter(MAX_RESULT_LIST_KEY);

        if (null != sMaxSize)
        {
            try
            {
                m_searchListSize = java.lang.Math.min(m_searchListSize, Integer.parseInt(sMaxSize));

                if ((Integer.parseInt(sMaxSize) > m_searchListSize) || (Integer.parseInt(sMaxSize) < 0))
                {
                    ((OwRecordDocument) getDocument()).setMaxChildSize(m_searchListSize);
                    throw new OwInvalidOperationException(getContext().localize1("plug.owrecord.OwRecordSearchTemplateView.maxsizeexceeded", "The maximal number of documents is limited to %1", String.valueOf(m_iMaxSizeMax)));
                }

                ((OwRecordDocument) getDocument()).setMaxChildSize(m_searchListSize);

                m_searchListSize = Integer.parseInt(sMaxSize);
            }
            catch (NumberFormatException e)
            {
                throw new OwInvalidOperationException(getContext().localize1("plug.owrecord.OwRecordSearchTemplateView.maxsizeexceeded", "The maximal number of documents is limited to %1", String.valueOf(m_searchListSize)), e);
            }
        }

        return true;
    }

    public boolean isMaxResultEnabled()
    {
        OwRecordDocument document = (OwRecordDocument) getDocument();
        boolean maxSizeEnabled = document.isMaxSizeEnabled();
        return maxSizeEnabled;
    }

    public int getiMaxSizeMax()
    {
        return m_iMaxSizeMax;
    }

    public void setiMaxSizeMax(int m_iMaxSizeMax)
    {
        this.m_iMaxSizeMax = m_iMaxSizeMax;
    }

}