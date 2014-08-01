package com.wewebu.ow.server.plug.owsearch;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.owsearch.log.OwLog;
import com.wewebu.ow.server.ui.OwBaseView;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwNavigationView;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * Search View Module.
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
public class OwSearchView extends OwMasterView
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwSearchView.class);

    /** attribute name of last used search template */
    public static final String PERSIST_SEARCHTEMPLATE_ATTRIBUTE_NAME = "OwSearchView_LastTemplate";

    /** layout to be used for the view */
    private OwSubLayout m_Layout;

    private OwResultListView m_resultList;

    private OwSubNavigationView m_SubNavigation;

    private OwSearchTemplateView m_searchTemplateView;

    /**
     * Navigate to the given search template, parameter should reflect 
     * the name (non-localized) of wanted template.
     * @param searchTemplateName_p String name of template to select
     * @throws OwObjectNotFoundException if search template not found
     */
    public void navigate(String searchTemplateName_p) throws OwObjectNotFoundException
    {
        boolean retVal = false;
        List<?> tabs = m_SubNavigation.getTabList();
        for (int i = 0; i < tabs.size(); i++)
        {
            OwNavigationView.OwTabInfo info = (OwNavigationView.OwTabInfo) tabs.get(i);

            String templateName = (String) info.getReasonObject();
            try
            {
                if (searchTemplateName_p.equals(templateName))
                {
                    m_SubNavigation.navigate(i);
                    retVal = true;
                    break;
                }
            }
            catch (Exception e)
            {
            }
        }
        if (retVal == false)
        {
            LOG.warn("SearchTemplate " + searchTemplateName_p + " does not exists in current configuration, selection will be kept.");
            throw new OwObjectNotFoundException(getContext().localize1("owsearch.OwSearchView.navigate.unknown.searchTemplate", "SearchTemplate named %1 could not be found.", searchTemplateName_p));

        }

    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_Layout = createSubLayout();

        // === attached layout
        addView(m_Layout, null);

        // === navigation
        m_SubNavigation = createSubNavigationView();
        m_Layout.addView(m_SubNavigation, OwSubLayout.NAVIGATION_REGION, null);

        ((OwSearchDocument) getDocument()).setNavigationView(m_SubNavigation);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_SubNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

        // === get last selected searchtemplate
        OwAttributeBagWriteable attributes = getDocument().getPersistentAttributeBagWriteable();
        String currentSearchTemplateName = (String) attributes.getSafeAttribute(PERSIST_SEARCHTEMPLATE_ATTRIBUTE_NAME, "");

        // === add Document search templates
        Collection<?> templates = ((OwSearchDocument) getDocument()).getSearchTemplates();

        int iNavigationIndex = 0;

        if (templates != null)
        {
            m_searchTemplateView = createSearchTemplateView();
            Iterator<?> it = templates.iterator();
            int i = 0;
            while (it.hasNext())
            {
                // === for each template use the identical view, but with different reason, i.e. Template name.
                // set icon for the template
                OwSearchDocument.OwRepositorySearchTemplate repTemplate = ((OwSearchDocument.OwRepositorySearchTemplate) it.next());

                String strIcon = null;
                if (repTemplate.getIcon() != null)
                {
                    strIcon = getContext().getDesignURL() + repTemplate.getIcon();
                }
                else
                {
                    strIcon = getContext().getDesignURL() + "/images/plug/owsearch/find.png";
                }

                m_SubNavigation.addView(m_searchTemplateView, repTemplate.getDisplayName(getContext().getLocale()), null, strIcon, repTemplate.getName(), null);

                if (repTemplate.getName().equals(currentSearchTemplateName))
                {
                    iNavigationIndex = i;
                }

                i++;
            }

        }

        // === add delimiter
        m_SubNavigation.addDelimiter();

        // === add Result list
        m_resultList = createResultListView();
        String strIcon = getContext().getDesignURL() + "/images/plug/owsearch/hitlist.png";
        m_SubNavigation.addView(m_resultList, getContext().localize("owsearch.OwSearchView.resultlist_title", "Result List"), null, strIcon, getContext().localize("owsearch.OwSearchView.resultlist_title", "Result List"), null);
        m_resultList.getObjectListView().setEventListner((OwSearchDocument) getDocument());

        // activate the first view
        m_SubNavigation.navigate(iNavigationIndex);
    }

    /** get the reference to the result list view to display the search results
     * @return OwObjectListView
     */
    public OwObjectListView getObjectListView()
    {
        return m_resultList.getObjectListView();
    }

    /** called by the framework to update the view when OwDocument.Update was called
     *
     *  NOTE:   We can not use the onRender method to update,
     *          because we do not know the call order of onRender.
     *          onUpdate is always called before all onRender methods.
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.UPDATE_DEFAULT:
                m_resultList.activate();

                // === check for auto open first one
                OwSearchDocument searchDocument = (OwSearchDocument) getDocument();
                if (searchDocument.isAutoOpenFirstDocument() || (searchDocument.isAutoOpenSingleRecord()))
                {
                    OwObject obj = searchDocument.getResultObject(0);

                    if (obj != null)
                    {
                        if (searchDocument.autoOpenFirstObject(obj, searchDocument.getResultSize()))
                        {
                            OwMimeManager.openObject(((OwMainAppContext) getContext()), obj, null, OwMimeManager.VIEWER_MODE_DEFAULT, null);
                        }
                    }
                }

                break;
        }
    }

    // === factory methods for overriding the plugin
    /** overridable factory method
     *
     * @return OwSubNavigationView instance
     */
    protected OwSubNavigationView createSubNavigationView()
    {
        return new OwSubNavigationView();
    }

    /** overridable factory method
     *
     * @return OwSubLayout instance
     */
    protected OwSubLayout createSubLayout()
    {
        return new OwSubLayout();
    }

    /** overridable factory method
     *
     * @return OwSearchTemplateView instance
     */
    protected OwSearchTemplateView createSearchTemplateView()
    {
        return new OwSearchTemplateView();
    }

    /** overridable factory method
     *
     * @return OwResultListView instance
     */
    protected OwResultListView createResultListView()
    {
        return new OwResultListView();
    }

    /**
     * Update search Criteria View
     * @param templateObject_p
     * @throws Exception
     * @since 3.1.0.0
     */
    public void updateSearchCriteria(OwSearchTemplate templateObject_p) throws Exception
    {
        if (m_searchTemplateView != null)
        {
            m_searchTemplateView.updateSearchCriteriaView(templateObject_p);
        }
    }

    /**
     * Update search TemplareView
     * @param templateObject_p
     * @throws Exception
     */
    public void updateSearchTemplateView(OwSearchTemplate templateObject_p) throws Exception
    {
        if (m_searchTemplateView != null)
        {
            m_searchTemplateView.updateSearchTemplate(templateObject_p);
        }
    }

    public String getBreadcrumbPart()
    {
        String dlgTitle = getDialogManager().getBreadcrumbPart();
        String bcPart = getTitle() + " - " + m_SubNavigation.getTitle();
        if (dlgTitle.equals(OwBaseView.EMPTY_STRING))
        {
            return bcPart;
        }
        return bcPart + " - " + dlgTitle;
    }
}
