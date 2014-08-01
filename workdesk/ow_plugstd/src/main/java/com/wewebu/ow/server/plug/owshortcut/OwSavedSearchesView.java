package com.wewebu.ow.server.plug.owshortcut;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Saved Searches View.<br/>
 * View that displays and executes saved searches.<br/>
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
 * @since 3.0.0.0
 */

public class OwSavedSearchesView extends OwView
{
    /** not sorted column */
    private static final int NO_SORTED_COLUMN_ID = -1;

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwSavedSearchesView.class);

    /** tag for the plugin Id of the Search plugin that is linked with the Favorites Plugin,
    in order to execute and display saved search queries. (default: SearchPluginId)*/
    private static final String KEY_SEARCH_PLUGIN_ID = "SearchPluginId";

    /** search plugin id*/
    private String m_searchPluginId;

    /** list of savedSearchs objects */
    private List m_savedSearchesList = new ArrayList();

    /** sortable column template name*/
    private OwSortableColumn m_templateNameColumn;

    /** sortable column template name*/
    private OwSortableColumn m_savedNameColumn;

    /** sort type for column id */
    private int m_sortColumnId = NO_SORTED_COLUMN_ID;

    /** default sort order */
    private int m_defaultSortOrder;

    /** default search criteria */
    private Comparator m_defaultComparator;

    /** change sort order criteria  */
    private boolean m_changeSortOrder = false;

    /** sort comparator template name    */
    private static final Comparator TEMPLATE_ORDER = new Comparator() {

        public int compare(Object e1_p, Object e2_p)
        {
            return ((OwSavedSearchWrapper) e2_p).getSearchTemplateName().compareTo(((OwSavedSearchWrapper) e1_p).getSearchTemplateName());
        }
    };

    /** sort  comparator stored  search name */
    private static final Comparator SAVENAME_ORDER = new Comparator() {

        public int compare(Object e1_p, Object e2_p)
        {
            return ((OwSavedSearchWrapper) e2_p).getSavedSearch().compareTo(((OwSavedSearchWrapper) e1_p).getSavedSearch());
        }
    };

    /**
     * Define sortable column
     *
     */
    public class OwSortableColumn
    {

        /** column displayed title */
        private String m_columnTitle;

        /** column id */
        private int m_columnId;

        /** column is not sorted */
        private static final int NOT_SORTED = 0;

        /** column is ordered in ascending order */
        private static final int ASCENDING_SORT = 1;

        /** column is ordered in descending order */
        private static final int DESCENDING_SORT = 2;

        /** flag to keep sortable column status*/
        private int m_SortInfo;

        /**
         * construct a sortable column
         * @param columnID_p int column number
         * @param columnTitle_p String column title
         * @param sortInfo_p int sort direction
         */
        public OwSortableColumn(int columnID_p, String columnTitle_p, int sortInfo_p)
        {
            this.m_columnId = columnID_p;
            this.m_columnTitle = columnTitle_p;
            this.m_SortInfo = sortInfo_p;
        }

        /**
         * column display title
         * @return String column title
         */
        public String getColumnTitle()
        {
            return m_columnTitle;
        }

        /**
         * column display title
         * @param columnTitle_p String column title
         */
        public void setColumnTitle(String columnTitle_p)
        {
            m_columnTitle = columnTitle_p;
        }

        /**
         * gets column id
         * @return int id of column
         */
        public int getColumnId()
        {
            return m_columnId;
        }

        /**
         * set a column id
         * @param columnId_p
         */
        public void setColumnId(int columnId_p)
        {
            m_columnId = columnId_p;
        }

        /**
         * Returns if a column is sortable
         * @return int sort type
         */
        public int getSortInfo()
        {
            return m_SortInfo;
        }

        /**
         * Set sort information
         * @param sortInfo_p int sort type
         */
        public void setSortInfo(int sortInfo_p)
        {
            m_SortInfo = sortInfo_p;
        }
    }

    /** Initialization after target has set */
    protected void init() throws Exception
    {
        super.init();
        //find search plugin id
        String documentId = getDocument().getID();
        OwXMLUtil pluginXML = ((OwMainAppContext) getContext()).getConfiguration().getPlugin(documentId.substring(0, documentId.length() - 4));

        if (pluginXML.getSubUtil(KEY_SEARCH_PLUGIN_ID) == null || pluginXML.getSubUtil(KEY_SEARCH_PLUGIN_ID).getSafeTextValue(null) == null)
        {
            String msg = "The mandatory plugin Id of the Search plugin that is linked with the Favorites Plugin, is missing. (Favorites Plugin, XML tag: SearchPluginId, default value: com.wewebu.ow.Search)";
            LOG.error("OwSaveSearchView.init: " + msg);
            throw new OwConfigurationException(getContext().localize("plugin.owshortcut.OwSavedSearchesView.invalidsearchplugin",
                    "The plugin Id of the Search plugin is missing from the Favorites plugin configuration. (Favorites Plugin, XML tag: SearchPluginId, default value: com.wewebu.ow.Search)"));
        }

        m_searchPluginId = pluginXML.getSubUtil(KEY_SEARCH_PLUGIN_ID).getSafeTextValue(null);

        m_savedNameColumn = new OwSortableColumn(1, getContext().localize("plugin.owshortcut.OwSavedSearchesView.name", "Name"), OwSortableColumn.NOT_SORTED);
        m_templateNameColumn = new OwSortableColumn(2, getContext().localize("plugin.owshortcut.OwSavedSearchesView.templatename", "Search Template Name"), OwSortableColumn.NOT_SORTED);
        updateList();
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        updateList();

        if (m_savedSearchesList == null || m_savedSearchesList.size() < 1)
        {
            w_p.write("<p class=\"OwEmptyTextMessage\">" + getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.noelements", "No favorites found.") + "</p>");
        }
        else
        {

            sortList(m_sortColumnId);
            String tableTitle = getContext().localize("plugin.owshortcut.OwSavedSearchesView.tableTitle", "This table containts saved searches list");
            String tableCaption = getContext().localize("plugin.owshortcut.OwSavedSearchesView.tableCaption.searches", "This table containts saved searches list");
            w_p.write("<table summary='" + tableTitle + "' class='OwGeneralList_table OwStoredSearches'>");
            w_p.write("<caption>" + tableCaption + "</caption>");

            w_p.write("<thead><tr class='OwGeneralList_header'><th class='a1'>");
            renderPropertyColumnHeader(w_p, m_savedNameColumn);
            w_p.write("</th><th class='a2'>");
            renderPropertyColumnHeader(w_p, m_templateNameColumn);
            w_p.write("&nbsp;</th><th  class='a3'>");
            w_p.write(getContext().localize("plugin.owshortcut.OwSavedSearchesView.functions", "Functions"));
            w_p.write("</tr></thead>");

            // fill data in table
            int index = 0;
            String deleteTooltip = getContext().localize("plug.owsearch.OwSearchTemplateView.deletesearchtooltip", "Delete saved search");
            String openTooltip = getContext().localize("plug.owsearch.OwSearchTemplateView.opensearchtooltip", "Open Search");
            String deleteIcon = getDeleteIcon();
            for (Iterator it = m_savedSearchesList.iterator(); it.hasNext(); index++)
            {
                OwSavedSearchWrapper savedSearch = (OwSavedSearchWrapper) it.next();

                w_p.write(((index % 2) == 0) ? "<tr class=\"OwGeneralList_RowEven\">" : "<tr class=\"OwGeneralList_RowOdd\">");
                w_p.write("<td class=\"a1\">");
                w_p.write("<a class=\"OwMimeItem\" href=\"");

                String encodedSavedSearch = OwHTMLHelper.encodeToSecureHTML(savedSearch.getSavedSearch());
                StringBuilder buffer = new StringBuilder();
                buffer.append("savedSearchIndex").append("=").append(Integer.toString(index));
                buffer.append("&templateName").append("=").append(savedSearch.getSearchTemplateName());

                w_p.write(this.getEventURL("Execute", OwHTMLHelper.encodeToSecureHTML(buffer.toString())));

                w_p.write("\" title=\"");
                w_p.write(openTooltip);
                w_p.write("\">");
                w_p.write(encodedSavedSearch);
                w_p.write("</a>");
                w_p.write("</td>");
                w_p.write("<td class=\"a2\">");

                OwHTMLHelper.writeSecureHTML(w_p, savedSearch.getSearchTemplateDisplayName());

                w_p.write("</td>");
                w_p.write("<td class=\"a3\">&nbsp;");
                w_p.write("<a href=\"");

                w_p.write(this.getEventURL("DeleteSearch", OwHTMLHelper.encodeToSecureHTML(buffer.toString())));

                w_p.write("\" title=\"");

                w_p.write(deleteTooltip);
                w_p.write("\"><img src=\"");
                w_p.write(deleteIcon);
                w_p.write("\" ");
                String deleteTooltipCustomized = getContext().localize1("plug.owsearch.OwSearchTemplateView.deletesearchtooltipforname", "Delete saved search %1", encodedSavedSearch);
                w_p.write("alt=\"");
                w_p.write(deleteTooltipCustomized);
                w_p.write("\" title=\"");
                w_p.write(deleteTooltipCustomized);
                w_p.write("\"/></a>");
                w_p.write("</td>");
                w_p.write("</tr>");
            }
            w_p.write("</table>");
        }
    }

    /** get the URL to the favorites delete icon
     * @return String URL
     */
    protected String getDeleteIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/deletebtn.png";
    }

    /**
     * Delete current saved search
     * @param request_p
     * @throws Exception
     */
    public void onDeleteSearch(HttpServletRequest request_p) throws Exception
    {
        String templateName = request_p.getParameter("templateName");
        String indexString = request_p.getParameter("savedSearchIndex");
        int index = Integer.parseInt(indexString);
        OwSavedSearchWrapper savedSearchWrapp = (OwSavedSearchWrapper) m_savedSearchesList.get(index);

        String savedSearch = savedSearchWrapp.getSavedSearch();

        Collection searchTemplates = getSearchTemplates();

        Iterator it = searchTemplates.iterator();

        while (it.hasNext())
        {
            OwSearchTemplate searchTemplate = (OwSearchTemplate) it.next();
            searchTemplate.isInitalized();
            if (templateName.equals(searchTemplate.getName()))
            {
                searchTemplate.deleteSavedSearch(savedSearch);
            }

        }

    }

    /**
     * Execute current search
     * @param request_p
     * @throws Exception
     */
    public void onExecute(HttpServletRequest request_p) throws Exception
    {
        String indexString = request_p.getParameter("savedSearchIndex");
        int index = Integer.parseInt(indexString);
        OwSavedSearchWrapper savedSearchWrapp = (OwSavedSearchWrapper) m_savedSearchesList.get(index);

        String savedSearch = savedSearchWrapp.getSavedSearch();
        String templateName = request_p.getParameter("templateName");

        OwMasterDocument searchplugin = (OwMasterDocument) getContext().getEventTarget(m_searchPluginId + OwConfiguration.MAIN_PLUGIN_DOCUMENT_ID_EXTENSION);
        if (null == searchplugin)
        {
            LOG.error("OwRemoteControlDocument.onExternalRequest: CONTROL_EVENT_SEARCH: No Masterplugin handler defined for given object in frontcontroller OwRemoteControlDocument.");
        }
        // perform search
        searchplugin.dispatch(OwDispatchCodes.OPEN_SAVED_SEARCH_TEMPLATE, templateName, savedSearch);
        // activate search view
        searchplugin.getMasterView().activate();

    }

    /**
     * Render column header with HTML link and image
     * @param w_p java.io.Writer HTML writer
     * @param column_p OwSortableColumn column to be rendered
     * @throws Exception
     */
    private void renderPropertyColumnHeader(java.io.Writer w_p, OwSortableColumn column_p) throws Exception
    {
        // write property display name and sort link
        w_p.write("<a href=\"");
        w_p.write(getEventURL("Sort", "sortField" + "=" + column_p.getColumnId()));
        w_p.write("\" title=\"");
        String text = OwString.localize2(getContext().getLocale(), "app.OwObjectListView.sortTooltip", "Sort %1 %2 ", column_p.getColumnTitle(), getSortOrderType(column_p.getSortInfo()));
        w_p.write(text);

        w_p.write("\" class=\"OwGeneralList_sort\">");
        w_p.write(column_p.getColumnTitle());
        w_p.write("&nbsp;&nbsp;&nbsp;&nbsp;");
        // display order arrows
        w_p.write("&nbsp;<img class=\"OwGeneralList_sortimg\" alt=\"" + text + "\" title=\"");
        w_p.write(text);
        w_p.write("\" border=\"0\" src=\"");
        w_p.write(getSortOrderImage(column_p.getSortInfo()));
        w_p.write("\">");
        w_p.write("</a>");

    }

    protected String getSortOrderType(int sortInfo_p) throws Exception
    {
        String sortOrderType = getContext().localize("app.OwObjectListView.sorting.ascending", "Ascending");
        String ascending = getContext().localize("app.OwObjectListView.sorting.ascending", "Ascending");
        String descending = getContext().localize("app.OwObjectListView.sorting.descending", "Descending");
        if (sortInfo_p == OwSortableColumn.NOT_SORTED)
        {
            return sortOrderType;
        }

        if (sortInfo_p == OwSortableColumn.ASCENDING_SORT)
        {
            return descending;
        }

        if (sortInfo_p == OwSortableColumn.DESCENDING_SORT)
        {
            return ascending;
        }

        return sortOrderType;
    }

    /** gets the path to the sort order image, which designates the current sort order of the specified column
     * @return image path
     */
    protected String getSortOrderImage(int sortInfo_p) throws Exception
    {

        if (sortInfo_p == OwSortableColumn.ASCENDING_SORT)
        {
            return getContext().getDesignURL() + "/images/OwObjectListView/sortasc.png";
        }

        if (sortInfo_p == OwSortableColumn.DESCENDING_SORT)
        {
            return getContext().getDesignURL() + "/images/OwObjectListView/sortdesc.png";
        }
        else
        {
            return getContext().getDesignURL() + "/images/OwObjectListView/nosort.png";
        }

    }

    /**
     * Execute sort by column
     * @param request_p HttpServletRequest request
     * @throws Exception
     */
    public void onSort(HttpServletRequest request_p) throws Exception
    {
        m_sortColumnId = Integer.parseInt(request_p.getParameter("sortField"));
        m_changeSortOrder = true;
    }

    /**
     * Sort list
     * @param sortColumnId_p int columnId
     */
    private void sortList(int sortColumnId_p)
    {
        if (sortColumnId_p != NO_SORTED_COLUMN_ID)
        {
            if (m_changeSortOrder)
            {
                if (sortColumnId_p == m_savedNameColumn.getColumnId())
                {
                    Collections.sort(m_savedSearchesList, SAVENAME_ORDER);
                    if (m_savedNameColumn.getSortInfo() == OwSortableColumn.ASCENDING_SORT)
                    {
                        m_savedNameColumn.setSortInfo(OwSortableColumn.DESCENDING_SORT);
                        m_defaultSortOrder = OwSortableColumn.DESCENDING_SORT;
                    }
                    else
                    {
                        Collections.reverse(m_savedSearchesList);
                        m_savedNameColumn.setSortInfo(OwSortableColumn.ASCENDING_SORT);
                        m_defaultSortOrder = OwSortableColumn.ASCENDING_SORT;
                    }
                    m_templateNameColumn.setSortInfo(OwSortableColumn.NOT_SORTED);
                    m_defaultComparator = SAVENAME_ORDER;

                }
                else if (sortColumnId_p == m_templateNameColumn.getColumnId())
                {
                    Collections.sort(m_savedSearchesList, TEMPLATE_ORDER);
                    if (m_templateNameColumn.getSortInfo() == OwSortableColumn.ASCENDING_SORT)
                    {
                        m_templateNameColumn.setSortInfo(OwSortableColumn.DESCENDING_SORT);
                        m_defaultSortOrder = OwSortableColumn.DESCENDING_SORT;
                    }
                    else
                    {
                        Collections.reverse(m_savedSearchesList);
                        m_templateNameColumn.setSortInfo(OwSortableColumn.ASCENDING_SORT);
                        m_defaultSortOrder = OwSortableColumn.ASCENDING_SORT;
                    }
                    m_savedNameColumn.setSortInfo(OwSortableColumn.NOT_SORTED);
                    m_defaultComparator = TEMPLATE_ORDER;
                }
                m_changeSortOrder = false;
            }
            else
            {

                defaultSort(m_defaultComparator, m_defaultSortOrder);
            }
        }

    }

    /**
     * Sort stored saved search templates using default sort criteria
     * @param defaultComparator_p
     * @param sortOrder_p
     */
    public void defaultSort(Comparator defaultComparator_p, int sortOrder_p)
    {
        if (sortOrder_p == OwSortableColumn.ASCENDING_SORT)
        {
            Collections.sort(m_savedSearchesList, defaultComparator_p);
            Collections.reverse(m_savedSearchesList);
        }
        else
        {
            Collections.sort(m_savedSearchesList, defaultComparator_p);
        }

    }

    /**
     * Fill data to sortable list
     * List contains search template and corresponding stored search
     * @throws Exception
     */
    private void updateList() throws Exception
    {
        m_savedSearchesList.clear();

        Collection searchTemplates = null;

        searchTemplates = getSearchTemplates();

        Iterator iterator = searchTemplates.iterator();
        while (iterator.hasNext())
        {
            OwSearchTemplate searchTemplate = (OwSearchTemplate) iterator.next();

            String templateName = searchTemplate.getName();

            String templateDisplayName = searchTemplate.getDisplayName(getContext().getLocale());

            Collection result = null;
            try
            {
                result = searchTemplate.getSavedSearches();
            }
            catch (OwObjectNotFoundException e)
            {
            }

            if (result != null)
            {
                Iterator it = result.iterator();
                while (it.hasNext())
                {
                    String savedName = (String) it.next();
                    m_savedSearchesList.add(new OwSavedSearchWrapper(templateName, templateDisplayName, savedName));
                }
            }

        }

    }

    /**
     * Get available search templates
     * @return Collection available SearchTemplates
     */
    protected Collection getSearchTemplates() throws Exception
    {
        OwXMLUtil pluginXML = ((OwMainAppContext) getContext()).getConfiguration().getPlugin(m_searchPluginId);

        Iterator itReps = pluginXML.getSafeNodeList("SearchTemplates").iterator();

        Collection searchTemplates = new Vector();

        while (itReps.hasNext())
        {
            // === iterate over each repository definition
            OwXMLUtil repositoryNode = new OwStandardXMLUtil((org.w3c.dom.Node) itReps.next());

            String repName = repositoryNode.getSafeStringAttributeValue("repname", null);
            OwRepository repository = (OwRepository) getRepositories().get(repName);
            if (repository == null)
            {
                String msg = "OwShortCutDocument.init: Repository is not existing, name = " + repName;
                LOG.fatal(msg);
                throw new OwConfigurationException(getContext().localize("owsearch.OwSearchDocument.repositorynotdefined", "Das Repository existiert nicht:") + " " + repName);
            }

            String strSearchTemplates = repositoryNode.getNode().getFirstChild().getNodeValue();

            if (strSearchTemplates == null)
            {
                String msg = "OwShortCutDocument.init: Search template category has not been defined at the Search Plugin.";
                LOG.fatal(msg);
                throw new OwConfigurationException(getContext().localize("owsearch.OwSearchDocument.templatesnotdefined", "Es wurde keine Suchtemplatekategory im Suchen Plugin definiert."));
            }

            // get searchtemplate objects from network
            Collection SearchTemplateObjects = ((OwMainAppContext) getContext()).getNetwork().getApplicationObjects(OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE, strSearchTemplates, false);
            if (SearchTemplateObjects != null)
            {
                // wrap OwSearchTemplates around the objects
                Iterator it = SearchTemplateObjects.iterator();
                while (it.hasNext())
                {
                    OwSearchTemplate searchtemplate = (OwSearchTemplate) it.next();

                    try
                    {
                        // add only those templates, the user has a role defined to, BUT do not init yet
                        //                        if (((OwMainAppContext) getContext()).isAllowed(OwRoleManager.ROLE_CATEGORY_SEARCH_TEMPLATE, searchtemplate.getName()))
                        {
                            searchTemplates.add(searchtemplate);
                        }
                    }
                    catch (Exception e)
                    {
                        String msg = "OwShortCutDocument.init: Search form could not be loaded, SearchTemplate = " + searchtemplate.getName();
                        LOG.error(msg, e);
                        throw new OwConfigurationException(getContext().localize1("owsearch.OwSearchDocument.searchtemplateload_failed", "Das Suchformular konnte nicht geladen werden: %1", searchtemplate.getName()), e);
                    }
                }
            }
        }

        return searchTemplates;
    }

    /** create a map of repositories that can be searched
     */
    protected Map getRepositories()
    {
        // === create map of repositories to search on
        Map repositories = new HashMap();

        repositories.put("OW_HISTORY", ((OwMainAppContext) getContext()).getHistoryManager());
        repositories.put("OW_ECM_ADAPTER", ((OwMainAppContext) getContext()).getNetwork());

        return repositories;
    }

}
