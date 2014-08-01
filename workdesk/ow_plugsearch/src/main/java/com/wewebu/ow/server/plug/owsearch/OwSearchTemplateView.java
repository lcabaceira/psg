package com.wewebu.ow.server.plug.owsearch;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMaxMinButtonControlView;
import com.wewebu.ow.server.app.OwSearchCriteriaView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.owsearch.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwOSFamilyKeyAction;
import com.wewebu.ow.server.ui.OwOSFamilyKeyCodeSetting;
import com.wewebu.ow.server.ui.OwOSFamilyKeyCodeSetting.OwOSFamilyKeyCode;
import com.wewebu.ow.server.ui.ua.OwOSFamily;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Search Template View. Displays the search criteria.
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
public class OwSearchTemplateView extends OwLayout
{
    private static final Logger LOG = OwLog.getLogger(OwSearchTemplateView.class);
    /** name of the max size region */
    public static final int MAX_SIZE_REGION = 1;
    /** name of the button / menu region */
    public static final int BUTTON_REGION = 2;
    /** name of the template region */
    public static final int TEMPLATE_REGION = 3;
    /** name of the min max control region */
    public static final int MIN_MAX_CONTROL_VIEW = 4;
    /** region for the search tree dump for debugging only */
    public static final int DEBUG_SEARCH_DUMP_REGION = 5;

    /** name of the searches selection*/
    public static final int SAVED_SEARCHES_SELECT_REGION = 6;

    /** delete search button */
    public static final int SAVED_SEARCH_DELETE_BUTTON_REGION = 9;
    /** Default result size if nothing is defined
     * @since 4.0.0.1*/
    public static final int DEFAULT_RESULT_SIZE = 50;

    /** errors region */
    public static final int ERRORS_REGION = 10;

    /** query key for the selected search combobox */
    private static final String SELECTED_SAVED_SEARCH_KEY = "owselectedsavesearch";

    /** query string key for the result list size */
    protected static final String MAX_RESULT_LIST_KEY = "reslistsize";
    /** constant for search combobox */
    private static final String NOTHING_SELECTED = "nothing_selected";

    protected static final String SEARCH_LABEL_ID = "owsearch.OwSearchTemplateView.search";

    protected static final String RESET_LABEL_ID = "owsearch.OwSearchTemplateView.reset";
    /** the maximum size that is possible for the maximum size parameter */
    protected int m_iMaxSizeMax;
    /** max number of result items in a search */
    protected int m_iMaxSize;

    /** the buttons for the search form */
    protected OwSubMenuView m_MenuView;

    /** View Module to display a maximize minimize button and maximize minimize the attached view */
    protected OwMaxMinButtonControlView m_MaxMinButtonControlView;

    /** view to display and edit the criteria */
    protected OwSearchCriteriaView m_SearchCriteriaView;
    /** index of the buttons */
    protected int m_iSubmitBtnIndex;
    protected int m_iResetBtnIndex;

    private OwOSFamilyKeyCodeSetting searchKeySettings;
    private OwOSFamilyKeyCodeSetting resetKeySettings;

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === create menu for search form buttons
        m_MenuView = new OwSubMenuView();
        addView(m_MenuView, BUTTON_REGION, null);

        // === add the criteria region view
        m_SearchCriteriaView = createSearchCriteriaView();
        m_SearchCriteriaView.setExternalFormTarget(getFormTarget());
        addView(m_SearchCriteriaView, TEMPLATE_REGION, null);

        // === add buttons
        // search button
        m_iSubmitBtnIndex = m_MenuView.addFormMenuItem(this, getContext().localize(SEARCH_LABEL_ID, "Search"), "DoSearch", null);

        // reset button
        m_iResetBtnIndex = m_MenuView.addMenuItem(this, getContext().localize(RESET_LABEL_ID, "Reset"), "DoReset", null);

        // === add min max control
        m_MaxMinButtonControlView = new OwMaxMinButtonControlView(this);
        addView(m_MaxMinButtonControlView, MIN_MAX_CONTROL_VIEW, null);

        // === get the maximum possible size from plugin descriptor
        m_iMaxSizeMax = ((OwMasterDocument) getDocument()).getConfigNode().getSafeIntegerValue("MaxSizeMax", 200);

        OwOSFamilyKeyCode osX = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE);
        OwOSFamilyKeyCode win = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE);
        OwOSFamilyKeyCode unknown = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE);
        searchKeySettings = new OwOSFamilyKeyCodeSetting(SEARCH_LABEL_ID, osX, win, unknown);

        osX = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_META);
        win = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
        unknown = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
        resetKeySettings = new OwOSFamilyKeyCodeSetting(RESET_LABEL_ID, osX, win, unknown);
    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return getContext().localize("plug.owsearch.OwSearchTemplateView.title", "Search Template");
    }

    /** activate the view from a navigation module. Called when view gets displayed
     * @param iIndex_p <code>int</code> index of Navigation
     * @param oReason_p User Object which was submitted when view was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        OwSearchTemplate templateObject = ((OwSearchDocument) getDocument()).setSearchTemplatName((String) oReason_p);
        updateSearchTemplate(templateObject);
    }

    /**
     * update Search Criteria View, do not clear search criteria parameters
     * @param templateObject_p
     * @since 3.1.0.0
     * @throws Exception
     */
    protected void updateSearchCriteriaView(OwSearchTemplate templateObject_p) throws Exception
    {
        m_SearchCriteriaView.resetErrors();
        if (templateObject_p != null)
        {
            List criteriaList = templateObject_p.getSearch(false).getCriteriaList(OwSearchNode.FILTER_HIDDEN);
            m_SearchCriteriaView.setCriteriaList(criteriaList);
            m_SearchCriteriaView.setHTMLFormular(templateObject_p.getHtmlLayout());
            m_SearchCriteriaView.setJspFormular(templateObject_p.getJspLayoutPage());
            m_SearchCriteriaView.setFieldProvider(templateObject_p);
        }
        else
        {
            // template is not valid
            m_SearchCriteriaView.setCriteriaList(null);
            m_SearchCriteriaView.setHTMLFormular(null);
            m_SearchCriteriaView.setJspFormular(null);
            m_SearchCriteriaView.setFieldProvider(null);
        }
    }

    /** set and init a search template
     *
     * @param templateObject_p
     * @throws Exception
     */
    protected void updateSearchTemplate(OwSearchTemplate templateObject_p) throws Exception
    {

        if (templateObject_p != null)
        {
            m_SearchCriteriaView.resetErrors();
            if (((OwSearchDocument) getDocument()).getConfigNode().getSafeBooleanValue("AlwaysClearCriteria", false))
            {
                m_SearchCriteriaView.setCriteriaList(templateObject_p.getSearch(true).getCriteriaList(OwSearchNode.FILTER_HIDDEN));
            }
            else
            {
                m_SearchCriteriaView.setCriteriaList(templateObject_p.getSearch(false).getCriteriaList(OwSearchNode.FILTER_HIDDEN));
            }

            m_SearchCriteriaView.setHTMLFormular(templateObject_p.getHtmlLayout());
            m_SearchCriteriaView.setJspFormular(templateObject_p.getJspLayoutPage());
            m_SearchCriteriaView.setFieldProvider(templateObject_p);
        }
        else
        {
            // template is not valid
            m_SearchCriteriaView.setCriteriaList(null);
            m_SearchCriteriaView.setHTMLFormular(null);
            m_SearchCriteriaView.setJspFormular(null);
            m_SearchCriteriaView.setFieldProvider(null);
        }
    }

    /** to get additional form attributes used for the form
     *  override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        return "action='" + m_MenuView.getNavigationFormAction(m_iSubmitBtnIndex) + "'";
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        serverSideDesignInclude("owsearch/OwSearchTemplateView.jsp", w_p);

        String searchUrl = getFormEventURL("DoSearch", null);
        String resetUrl = getFormEventURL("DoReset", null);
        OwOSFamilyKeyAction searchAction = new OwOSFamilyKeyAction(searchKeySettings, searchUrl, getContext().localize(SEARCH_LABEL_ID, "Search"));
        OwOSFamilyKeyAction resetAction = new OwOSFamilyKeyAction(resetKeySettings, resetUrl, getContext().localize(RESET_LABEL_ID, "Reset"));
        // === enable keys for search
        getContext().registerKeyAction(searchAction);
        getContext().registerKeyAction(resetAction);
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case ERRORS_REGION:
            {
                m_SearchCriteriaView.renderRegion(w_p, OwSearchCriteriaView.ERRORS_REGION);
            }
                break;
            // === render internal regions here
            case SAVED_SEARCH_DELETE_BUTTON_REGION:
            {
                w_p.write("<input type=\"button\" onclick=\"");
                w_p.write(getFormEventURL("DeleteSearch", null));
                w_p.write("\" value='");
                String deleteSavedSearchTooltip = getContext().localize("plug.owsearch.OwSearchTemplateView.deletesearchtooltip", "Delete saved search");
                w_p.write(deleteSavedSearchTooltip);
                w_p.write("'/>");
            }
                break;

            case SAVED_SEARCHES_SELECT_REGION:
            {
                if (isEnabledStoredSearch())
                {
                    renderSavedSearchesRegion(w_p);
                }
            }
                break;

            case MAX_SIZE_REGION:
            {
                if (!isPageable())
                {
                    renderMaxSizeRegion(w_p);
                }
            }
                break;

            case DEBUG_SEARCH_DUMP_REGION:
            {
                // === dump search tree
                OwSearchTemplate TemplateObject = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate();
                if (TemplateObject != null)
                {
                    TemplateObject.getSearch(false).dump(w_p);
                }
            }
                break;

            default:
            {
                // delegate to base class to render other attached views
                super.renderRegion(w_p, iRegion_p);
                break;
            }
        }
    }

    private void renderMaxSizeRegion(Writer w_p) throws IOException
    {
        // === render max resultlist size
        w_p.write("\n<div class=\"OwPropertyBlock\"><div class=\"OwPropertyLabel\"><label for=\"maxresultlistsizeId\">");
        w_p.write(getContext().localize("owsearch.OwSearchTemplateView.maxresultlistsize", "Maximum number of results"));
        w_p.write("</label></div><div class=\"OwPropertyValue\"><input id=\"maxresultlistsizeId\" type=\"text\" class=\"DefaultInput\" size=\"4\" name=\"");
        w_p.write(MAX_RESULT_LIST_KEY);
        w_p.write("\" value=\"");
        w_p.write(String.valueOf(getQueryMaxSize()));
        w_p.write("\"></div></div>\n");
    }

    /** render the saved searches region with searches selection
     *
     * @param w_p
     * @throws Exception
     */
    private void renderSavedSearchesRegion(Writer w_p) throws Exception
    {
        OwSearchTemplate searchtemplate = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate();
        if (null == searchtemplate)
        {
            return;
        }

        Collection searches = searchtemplate.getSavedSearches();
        if ((null == searches) || (searches.size() == 0))
        {
            return;
        }

        w_p.write("<script>\n");
        w_p.write("     function selectSavedSearch(){\n");
        w_p.write(getFormEventURL("SelectSavedSearch", null));
        w_p.write("\n      }\n");
        w_p.write("</script>\n");

        List comboItems = new LinkedList();
        comboItems.add(new OwDefaultComboItem(NOTHING_SELECTED, getContext().localize("plug.owsearch.OwSearchTemplateView.savedsearcheselectinfo", "Saved searches are available...")));

        Iterator it = searches.iterator();
        while (it.hasNext())
        {
            String name = (String) it.next();
            StringWriter writer = new StringWriter();
            OwHTMLHelper.writeSecureHTML(writer, name);
            OwDefaultComboItem item = new OwDefaultComboItem(name, writer.toString());
            comboItems.add(item);
        }

        OwComboModel model = new OwDefaultComboModel(false, false, searchtemplate.getSavedSearch(), comboItems);
        OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, SELECTED_SAVED_SEARCH_KEY, null, null, new OwString("plug.owsearch.OwSearchTemplateView.saved.searches.title", "Saved searches"));
        renderer.addEvent("onchange", "selectSavedSearch()");
        String displayName = getContext().localize("OwSearchTemplateView.selectSavedSearch", "Select saved search");
        OwInsertLabelHelper.insertLabelValue(w_p, displayName, SELECTED_SAVED_SEARCH_KEY);
        renderer.renderCombo(w_p);
    }

    public boolean isRegion(int region_p)
    {
        OwSearchDocument doc = ((OwSearchDocument) getDocument());
        switch (region_p)
        {
            case SAVED_SEARCH_DELETE_BUTTON_REGION:
            {
                try
                {
                    Collection savedSearches = doc.getCurrentSearchTemplate().getSavedSearches();
                    if (savedSearches.isEmpty())
                    {
                        doc.getCurrentSearchTemplate().setSavedSearch(null);
                        return false;
                    }
                    else
                    {
                        String savedSearch = doc.getCurrentSearchTemplate().getSavedSearch();
                        boolean canDelete = doc.getCurrentSearchTemplate().canDeleteSearch();
                        return (savedSearch != null) && canDelete;
                    }
                }
                catch (Exception e)
                {
                    return false;
                }
            }

            case SAVED_SEARCHES_SELECT_REGION:
            {
                try
                {
                    return doc.getCurrentSearchTemplate().getSavedSearches().size() > 0;
                }
                catch (Exception e)
                {
                    return false;
                }
            }

            default:
                return super.isRegion(region_p);
        }
    }

    /** event called when user clicked delete search
     *   @param request_p an {@link HttpServletRequest}
     */
    public void onDeleteSearch(HttpServletRequest request_p) throws Exception
    {
        OwSearchTemplate searchtemplate = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate();
        if (null == searchtemplate)
        {
            return;
        }

        String name = request_p.getParameter(SELECTED_SAVED_SEARCH_KEY);
        if ((null != name) && (name.length() > 0) && !NOTHING_SELECTED.equalsIgnoreCase(name))
        {
            searchtemplate.deleteSavedSearch(name);
        }
    }

    /** event called when user changes the selection of saved searches
     *   @param request_p an  {@link HttpServletRequest}
     */
    public void onSelectSavedSearch(HttpServletRequest request_p) throws Exception
    {
        OwSearchTemplate searchtemplate = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate();
        if (null == searchtemplate)
        {
            return;
        }

        String name = request_p.getParameter(SELECTED_SAVED_SEARCH_KEY);
        if ((null != name) && (name.length() == 0) || NOTHING_SELECTED.equalsIgnoreCase(name))
        {
            name = null;
        }

        searchtemplate.setSavedSearch(name);

        // reinitializes the search template
        updateSearchTemplate(searchtemplate);
    }

    public void onDoReset(HttpServletRequest request_p) throws Exception
    {
        this.onDoReset(request_p, null);
    }

    /** event called when user clicked DoReset
     *
     *   @param request_p an {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onDoReset(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwSearchTemplate TemplateObject = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate();

        if (TemplateObject != null)
        {
            // get a refreshed search from the template
            m_SearchCriteriaView.setCriteriaList(TemplateObject.getSearch(true).getCriteriaList(OwSearchNode.FILTER_HIDDEN));
            m_SearchCriteriaView.setJspFormular(TemplateObject.getJspLayoutPage());
            m_SearchCriteriaView.setHTMLFormular(TemplateObject.getHtmlLayout());
        }
    }

    public void onDoSearch(HttpServletRequest request_p) throws Exception
    {
        this.onDoSearch(request_p, null);
    }

    /** event called when user clicked DoSearch
     *   @param request_p an {@link HttpServletRequest}
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

        // === persist the current selected searchtemplate
        OwAttributeBagWriteable attributes = getDocument().getPersistentAttributeBagWriteable();
        String currentSearchTemplateName = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate().getName();
        attributes.setAttribute(OwSearchView.PERSIST_SEARCHTEMPLATE_ATTRIBUTE_NAME, currentSearchTemplateName);
        attributes.save();

        // === get max result list size
        try
        {
            String size = request_p.getParameter(MAX_RESULT_LIST_KEY);
            if (size != null)
            {
                setQueryMaxSize(Integer.parseInt(size));
            }
        }
        catch (NumberFormatException e)
        {
            throw new OwInvalidOperationException(getContext().localize1("plug.owsearch.OwSearchTemplateView.maxsizeexceeded", "Maximum number of results must be set between 1 to %1", String.valueOf(m_iMaxSizeMax)), e);
        }

        // === perform search with the search object set by the form
        ((OwSearchDocument) getDocument()).doSearch(getQueryMaxSize());

        // === Navigate to the result view
        getDocument().update(this, OwUpdateCodes.UPDATE_DEFAULT, null);

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
        return m_SearchCriteriaView.updateExternalFormTarget(request_p, fSave_p);
    }

    /** overridable factory method
     *
     * @return OwSearchCriteriaView instance
     */
    protected OwSearchCriteriaView createSearchCriteriaView()
    {
        return new OwSearchCriteriaView();
    }

    /**
     * Enable/Disable stored searches feature
     * @return true
     * @since 3.1.0.0
     */
    protected boolean isEnabledStoredSearch()
    {
        //true for backward compatibility
        return (((OwSearchDocument) getDocument()).getConfigNode().getSafeBooleanValue("EnableStoredSearches", true));
    }

    /**
     * True to enable Paging capability, by default false 
     * @return true
     * @since 4.2.0.0
     */
    protected boolean isPageable()
    {
        //default false
        return (((OwSearchDocument) getDocument()).isPageable());
    }

    /**
     * Get query max size for search process.
     * @return integer defining max allowed results
     * @since 4.0.0.1
     */
    protected int getQueryMaxSize()
    {
        if (this.m_iMaxSize > 0)
        {
            return m_iMaxSize;
        }
        else
        {
            try
            {
                int tempSize = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate().getDefaultMaxSize();
                return tempSize > m_iMaxSizeMax ? m_iMaxSizeMax : (tempSize > 0 ? tempSize : DEFAULT_RESULT_SIZE);
            }
            catch (Exception e)
            {
                LOG.warn("Faild to get max size of current searchtemplate, set back to default");
                return DEFAULT_RESULT_SIZE;
            }
        }
    }

    /**
     * Set maximum query size for search process.
     * <p>Provided value will be checked against limits values. <br />
     * if the configured maximum is reached or negative definition
     * is provided an exception will be thrown.
     * </p>
     * @param newMax_p int new maximum
     * @throws OwInvalidOperationException
     * @since 4.0.0.1
     */
    protected void setQueryMaxSize(int newMax_p) throws OwInvalidOperationException
    {
        if (m_iMaxSize >= 0)
        {
            this.m_iMaxSize = newMax_p;
        }
        else
        {
            int currentDefault = getQueryMaxSize();
            if (DEFAULT_RESULT_SIZE != newMax_p && currentDefault != newMax_p)
            {
                this.m_iMaxSize = newMax_p;
            }
        }

        if ((m_iMaxSize > m_iMaxSizeMax) || (m_iMaxSize <= 0))
        {
            setQueryMaxSize(m_iMaxSizeMax);
            throw new OwInvalidOperationException(getContext().localize1("plug.owsearch.OwSearchTemplateView.maxsizeexceeded", "Maximum number of files is limited to %1", String.valueOf(m_iMaxSizeMax)));
        }
    }
}