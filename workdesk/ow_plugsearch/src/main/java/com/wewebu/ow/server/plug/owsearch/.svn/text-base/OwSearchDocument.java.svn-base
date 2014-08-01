package com.wewebu.ow.server.plug.owsearch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner;
import com.wewebu.ow.server.dmsdialogs.views.OwPageableListView;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.plug.owsearch.log.OwLog;
import com.wewebu.ow.server.ui.OwNavigationView;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtilPlaceholderFilter;

/**
 *<p>
 * Search Document Implementation. The Document to the record management tab.
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
public class OwSearchDocument extends OwMasterDocument implements OwObjectListViewEventListner
{
    /**
     * @since 4.2.0.0
     */
    private static final String CONFIG_NODE_PAGEABLE = "pageable";

    boolean pageSearchEnabled = false;

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwSearchDocument.class);

    /** configuration node name &lt;EnabledDocumentFunctions&gt; */
    public static final String CONFIG_NODE_ENABLEDDOCUMENTFUNCTIONS = "EnabledDocumentFunctions";

    /** enabled attribute for document function plugin list */
    public static final String PLUGIN_LIST_ENABLED_ATTRIBUTE = "enable";

    /** decorator class for search templates. Assigns Repositories to search templates
     */
    public class OwRepositorySearchTemplate
    {
        /** create a repository search decorator template
         */
        public OwRepositorySearchTemplate(OwSearchTemplate searchTemplate_p, OwRepository repository_p)
        {
            m_searchTemplate = searchTemplate_p;
            m_repository = repository_p;
        }

        /** get the search template to use
         * @throws Exception */
        public OwSearchTemplate getSearchTemplate() throws Exception
        {
            if (!m_searchTemplate.isInitalized())
            {
                m_searchTemplate.init(m_repository);
            }

            return m_searchTemplate;
        }

        /** get the repository to search on */
        public OwRepository getRepository()
        {
            return m_repository;
        }

        /** get the name of the template */
        public String getName() throws Exception
        {
            return m_searchTemplate.getName();
        }

        /** get the display name of the template */
        public String getDisplayName(Locale locale_p) throws Exception
        {
            return m_searchTemplate.getDisplayName(locale_p);
        }

        /** get the icon of the template */
        public String getIcon() throws Exception
        {
            return m_searchTemplate.getIcon();
        }

        // the search template to use
        protected OwSearchTemplate m_searchTemplate;
        // the repository to search on
        protected OwRepository m_repository;
    };

    /** result list of objects after the search */
    OwObjectCollection m_SearchResultList;
    /** result iterable of objects after the page search */
    OwIterable<OwObject> m_searchResultIterable;

    /** the currently used searchtemplate view which generated the resultlist */
    private int m_iActiveSearchTemplateIndex = -1;

    /** list of OwRepositorySearchTemplate instances  */
    private HashMap m_TemplateMap = new HashMap();

    /** list of template names */
    private Collection m_OrderedTemplateVector = new Vector();

    /** currently selected template document */
    private OwRepositorySearchTemplate m_ActiveTemplate;

    /** the navigation view used to navigate the searchtemplates */
    private OwNavigationView m_navView;

    /**cached max size value - used to repeat the last performed search*/
    private int m_lastMaxListSize;

    private OwSort m_lastSort;

    /** get the reference to the result list view to display the search results
     * @return OwObjectListView
     */
    private OwObjectListView getObjectListView()
    {
        return ((OwSearchView) getMasterView()).getObjectListView();
    }

    /** overridable, create a map of repositories that can be searched
     */
    protected Map getRepositories()
    {
        // === create map of repositories to search on
        Map repositories = new HashMap();

        repositories.put("OW_HISTORY", ((OwMainAppContext) getContext()).getHistoryManager());
        repositories.put("OW_ECM_ADAPTER", ((OwMainAppContext) getContext()).getNetwork());

        return repositories;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // === get search templates from Network
        // search templates are XML Files, which describe the criteria for the search
        // they are converted to HTML to display the search view.

        Iterator itReps = getConfigNode().getSafeNodeList("SearchTemplates").iterator();
        while (itReps.hasNext())
        {
            // === iterate over each repository definition
            OwXMLUtil repositoryNode = new OwStandardXMLUtil((org.w3c.dom.Node) itReps.next());

            String repName = repositoryNode.getSafeStringAttributeValue("repname", null);
            OwRepository repository = (OwRepository) getRepositories().get(repName);
            if (repository == null)
            {
                String msg = "OwSearchDocument.init: Repository is not existing, name = " + repName;
                LOG.fatal(msg);
                throw new OwConfigurationException(getContext().localize("owsearch.OwSearchDocument.repositorynotdefined", "Das Repository existiert nicht:") + " " + repName);
            }

            String strSearchTemplates = repositoryNode.getNode().getFirstChild().getNodeValue();

            if (strSearchTemplates == null)
            {
                String msg = "OwSearchDocument.init: Search template category has not been defined at the Search Plugin.";
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
                            OwRepositorySearchTemplate reptemplate = new OwRepositorySearchTemplate(searchtemplate, repository);
                            m_OrderedTemplateVector.add(reptemplate);

                            if (m_TemplateMap.containsKey(reptemplate.getName()))
                            {
                                String msg = "OwSearchDocument.init: Name of searchtemplate is already assigned, RepositorySearchTemplate = " + reptemplate.getName() + ", SearchTemplate = " + searchtemplate.getName();
                                LOG.warn(msg);
                                throw new OwConfigurationException(getContext().localize1("owsearch.OwSearchDocument.namealreadydefined", "Name of search form is already assigned: %1", reptemplate.getName()));
                            }

                            m_TemplateMap.put(reptemplate.getName(), reptemplate);
                        }
                    }
                    catch (Exception e)
                    {
                        String msg = "OwSearchDocument.init: Search form could not be loaded, SearchTemplate = " + searchtemplate.getName();
                        LOG.error(msg, e);
                        throw new OwConfigurationException(getContext().localize1("owsearch.OwSearchDocument.searchtemplateload_failed", "Search form could not be loaded: %1", searchtemplate.getName()), e);
                    }
                }
            }
        }
    }

    /** set and activate the searchtemplate
     * @param strSearchTemplateName_p Name of the template as retrieved from OwNetwork.getSiteObjects("SearchTemplates");
     */
    public OwSearchTemplate setSearchTemplatName(String strSearchTemplateName_p) throws Exception
    {
        OwRepositorySearchTemplate newtemplate = (OwRepositorySearchTemplate) m_TemplateMap.get(strSearchTemplateName_p);

        if (newtemplate != m_ActiveTemplate)
        {
            // set optional sort from template if template changed
            OwSort sort = newtemplate.getSearchTemplate().getSort(((OwMainAppContext) getContext()).getMaxSortCriteriaCount());
            getObjectListView().setSort(sort);
        }

        m_ActiveTemplate = newtemplate;

        return m_ActiveTemplate.getSearchTemplate();
    }

    /** get the active searchtemplate
     * @return the active search template name
     */
    public OwSearchTemplate getCurrentSearchTemplate() throws Exception
    {
        return m_ActiveTemplate.getSearchTemplate();
    }

    /** get the list to the available searchtemplates, which are allowed for the user.
     * @return List of OwSearchTemplates as retrieved from OwNetwork.getSiteObjects("SearchTemplates");
     */
    public Collection getSearchTemplates() throws Exception
    {
        return m_OrderedTemplateVector;
    }

    /** compute a filtered sort out of a given sort according to the given property list
     *  used to adjust a sort created by the resultlist to the current search, which might be different to the last search
     * @param sort_p an {@link OwSort} object
     * @param properties_p Properties to filter for
     * @return cloned and filtered OwSort object
     */
    protected OwSort getFilteredSort(OwSort sort_p, Collection properties_p)
    {
        // === create clone, but do only add those properties which are contained in the Properties_p list
        OwSort Sort = new OwSort(sort_p.getMaxSize(), sort_p.getDefaultAsc());

        Iterator it = sort_p.getCriteriaCollection().iterator();
        while (it.hasNext())
        {
            OwSort.OwSortCriteria Criteria = (OwSort.OwSortCriteria) it.next();

            if (properties_p.contains(Criteria.getPropertyName()))
            {
                Sort.addCriteria(Criteria);
            }
        }

        return Sort;
    }

    /** set the currently used searchtemplate view which generated the result list
     */
    public void setNavigationView(OwNavigationView navView_p)
    {
        m_navView = navView_p;
    }

    /** check if navigate to the searchtemplate the caused the last search is possible */
    public boolean canGoBackToSearchTemplate()
    {
        return (m_iActiveSearchTemplateIndex != -1);
    }

    /** navigate to the searchtemplate the caused the last search */
    public void goBackToSearchTemplate() throws Exception
    {
        if (canGoBackToSearchTemplate())
        {
            m_navView.navigate(m_iActiveSearchTemplateIndex);
        }
    }

    /** perform a search and display the result
     *
     * @param iMaxListSize_p int maximum number of items in the search
     */

    public void doSearch(int iMaxListSize_p) throws Exception
    {
        doSearch(iMaxListSize_p, null);
    }

    /**perform a search and display the result
     * 
     * @param iMaxListSize_p int maximum number of items in the search
     * @param sort sorting rules to be used during search
     * @throws Exception
     * @since 4.2.0.0
     */
    public void doSearch(int iMaxListSize_p, OwSort sort) throws Exception
    {
        m_iActiveSearchTemplateIndex = m_navView.getNavigationIndex();

        // === set column info
        OwObjectListView objectListView = getObjectListView();
        objectListView.setColumnInfo(m_ActiveTemplate.getSearchTemplate().getColumnInfoList());

        // get the needed properties
        Collection PropertyNames = objectListView.getRetrievalPropertyNames();

        // === delegate search to the DMS Adaptor use current sort order of result view
        // create a new sort out of the previous one, which is filtered to the properties of this new search.
        OwSort newSort = sort;
        if (newSort == null)
        {
            newSort = getFilteredSort(objectListView.getSort(), PropertyNames);
        }

        objectListView.setSort(newSort);

        boolean pageSearchEnabled = isPageable();

        if (pageSearchEnabled)
        {
            if (objectListView instanceof OwPageableListView)
            {
                if (m_ActiveTemplate.getRepository().canPageSearch())
                {
                    OwLoadContext loadContext = new OwLoadContext();
                    loadContext.setSorting(newSort);
                    loadContext.setPropertyNames(PropertyNames);
                    loadContext.setMaxSize(iMaxListSize_p);
                    loadContext.setVersionSelection(m_ActiveTemplate.getSearchTemplate().getVersionSelection());
                    m_searchResultIterable = m_ActiveTemplate.getRepository().doSearch(m_ActiveTemplate.getSearchTemplate().getSearch(false), loadContext);
                    m_SearchResultList = null;

                    ((OwPageableListView) objectListView).setObjectIterable(m_searchResultIterable, null);
                }
                else
                {
                    throw new OwNotSupportedException("The repository does not support page search.");
                }
            }
            else
            {
                throw new OwNotSupportedException("List view type does not support page-search : " + objectListView.getClass() + ". " + OwPageableListView.class.getName() + " extension expected.");
            }
        }
        else
        {
            m_SearchResultList = m_ActiveTemplate.getRepository().doSearch(m_ActiveTemplate.getSearchTemplate().getSearch(false), newSort, PropertyNames, iMaxListSize_p, m_ActiveTemplate.getSearchTemplate().getVersionSelection());
            m_searchResultIterable = null;

            // === set the result view with result list and column info and the new sort
            objectListView.setObjectList(m_SearchResultList, null);

        }

        objectListView.setCurrentPage(0);

        m_lastSort = newSort;

        m_lastMaxListSize = iMaxListSize_p;

    }

    /**
     * True to enable Paging capability, by default false 
     * @return true
     * @since 4.2.0.0
     */
    boolean isPageable()
    {
        return getConfigNode().getSafeBooleanValue(CONFIG_NODE_PAGEABLE, false);
    }

    /**
     * Repeats the last search performed through {@link #doSearch(int)} preserving the
     * current page in the object list view.
     * @since 2.5.3.0
     * @throws Exception
     */
    public void repeatLastSearch() throws Exception
    {
        int activeSearchTemplateIndex = m_iActiveSearchTemplateIndex;
        try
        {
            OwObjectListView objectListView = getObjectListView();
            int currentPage = objectListView.getCurrentPage();
            doSearch(m_lastMaxListSize, m_lastSort);
            objectListView.setCurrentPage(currentPage);
        }
        finally
        {
            m_iActiveSearchTemplateIndex = activeSearchTemplateIndex;
        }
    }

    /** This function will be overloaded by the targets (Views and Documents) to perform a generic action on this target.
     *  this is a generic function, used for communication of plugins,
     *  which do not know about the interfaces of each other.
     *
     *  @param iCode_p enumerator designating the requested action
     *  @param param1_p String Name of search template and max size to activate in the form "[templatename],[maxsize]"
     *  @param param2_p Map of values mapped to property names as search parameters
     *  @return Object depending on derived implementation
     */
    public Object onDispatch(int iCode_p, Object param1_p, Object param2_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwDispatchCodes.OPEN_OBJECT:
                Map parameterMap = (Map) param2_p;

                StringTokenizer param1tokenizer = new StringTokenizer((String) param1_p, ",");
                String sTemplateName = param1tokenizer.nextToken();
                int iMaxSize = Integer.parseInt(param1tokenizer.nextToken());

                // navigate to search template
                ((OwSearchView) getMasterView()).navigate(sTemplateName);
                OwSearchNode searchNode = getCurrentSearchTemplate().getSearch(true);

                if (isFullTextSearch(parameterMap, searchNode))
                {
                    LOG.error("The Search Template contains a full text search. Full text search is not supported from a remote plugin call.");
                    getMasterView().activate();
                    throw new OwNotSupportedException(getContext().localize("owsearch.OwSearchDocument.fulltextsearch_not_supported", "Full text search is not supported from a remote plugin call."));
                }

                // set properties
                Map criterias = searchNode.getCriteriaMap(OwSearchNode.FILTER_NONE);

                //reset all criteria values, which are entered before current search
                Iterator<?> itCrit = criterias.entrySet().iterator();
                while (itCrit.hasNext())
                {
                    Entry mapEntry = (Entry) itCrit.next();
                    OwSearchCriteria critEntry = (OwSearchCriteria) mapEntry.getValue();
                    //don't change the hidden or predefined criteria of the search template
                    int attribute = critEntry.getAttributes() % 4;
                    if (attribute == OwSearchCriteria.ATTRIBUTE_NONE || attribute >= OwSearchCriteria.ATTRIBUTE_REQUIRED)
                    {
                        critEntry.setValue(critEntry.getDefaultValue());
                    }
                }

                Iterator<?> it = parameterMap.entrySet().iterator();
                //set all given values into the defined search criteria
                while (it.hasNext())
                {
                    Entry mapEntry = (Entry) it.next();

                    OwSearchCriteria crit = (OwSearchCriteria) criterias.get(mapEntry.getKey());
                    if (null == crit)
                    {
                        throw new OwInvalidOperationException("OwSearchDocument.onDispatch: Property = " + mapEntry.getKey() + " not found");
                    }
                    crit.setValue(crit.getFieldDefinition().getValueFromString((String) mapEntry.getValue()));
                }

                // search
                doSearch(iMaxSize);

                // Navigate to the result view
                update(this, OwUpdateCodes.UPDATE_DEFAULT, null);

                if (!isAutoOpenSingleRecord())
                {
                    //activate current view
                    getMasterView().activate();
                }
                break;
            case OwDispatchCodes.OPEN_SAVED_SEARCH_TEMPLATE:
            {

                OwSearchView searchView = (OwSearchView) getMasterView();
                searchView.navigate((String) param1_p);
                OwSearchTemplate searchtemplate = getCurrentSearchTemplate();
                searchtemplate.setSavedSearch((String) param2_p);

                // reinitializes the search template
                searchView.updateSearchCriteria(searchtemplate);
            }
        }

        return null;
    }

    /**
     * Check if a parameter from parameters map is referring to a CBR node type
     * @param parameterMap_p - the map with parameters
     * @param searchNode_p - the search node
     * @return <code>true</code> if a full text search will be executed.
     * @since 2.5.2.0
     */
    private boolean isFullTextSearch(Map parameterMap_p, OwSearchNode searchNode_p)
    {
        boolean result = false;
        OwSearchNode cbrNode = searchNode_p.findSearchNode(OwSearchNode.NODE_TYPE_CBR);
        if (cbrNode != null)
        {
            Map contentBasedCriteria = cbrNode.getCriteriaMap(OwSearchNode.FILTER_NONE);
            Iterator parametersIterator = parameterMap_p.keySet().iterator();
            while (parametersIterator.hasNext())
            {
                String sPropertyName = (String) parametersIterator.next();
                OwSearchCriteria crit = (OwSearchCriteria) contentBasedCriteria.get(sPropertyName);
                if (null != crit)
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 
     *@return the number of objects in the result list
     *@since 4.2.0.0
     */
    public int getResultSize()
    {
        if (m_searchResultIterable != null)
        {
            return (int) m_searchResultIterable.getTotalNumItems();
        }
        else if (m_SearchResultList != null)
        {
            return m_SearchResultList.size();
        }
        else
        {
            return 0;
        }
    }

    /**
     * 
     *@param index
     *@return the result object at the given index 
     *         or null if no object is available at the given index
     *@since 4.2.0.0  
     */
    public OwObject getResultObject(int index)
    {
        if (m_searchResultIterable != null)
        {
            OwIterable<OwObject> page = m_searchResultIterable.skipTo(index);
            Iterator<OwObject> i = page.iterator();
            if (i.hasNext())
            {
                return i.next();
            }
        }
        else if (m_SearchResultList != null)
        {
            int iSize = m_SearchResultList.size();
            if (iSize > index)
            {
                return (OwObject) m_SearchResultList.get(index);
            }
        }

        return null;
    }

    /** get the result collection
     *
     *@return an {@link OwIterable}
     *@since 4.2.0.0
     */
    public OwIterable<OwObject> getResultIterable()
    {
        return m_searchResultIterable;
    }

    /** get the result collection
     *
     * @return an {@link OwObjectCollection}
     */
    public OwObjectCollection getResultList()
    {
        return m_SearchResultList;
    }

    /** (overridable) to check for auto open first one if one or more where found
     *
     * @param obj_p first OwObject to check if it should be auto opened
     * @param iSize_p int number of objects in the result list
     *
     * @return boolean true = open the first object, false = do nothing
     */
    protected boolean autoOpenFirstObject(OwObject obj_p, int iSize_p)
    {
        if (OwStandardObjectClass.isContainerType(obj_p.getType()))
        {
            return (iSize_p == 1) && isAutoOpenSingleRecord();
        }
        return isAutoOpenFirstDocument();
    }

    /**
     * check if open single record (folder) is activated
     * @return boolean true = autoOpen if is single record
     * @since 3.1.0.0
     */
    protected boolean isAutoOpenSingleRecord()
    {
        return getConfigNode().getSafeBooleanValue("AutoOpenSingleRecord", false);
    }

    /**
     * check if open first document is activated
     * @return boolean true = autoOpen first document
     * @since 3.1.0.0
     */
    protected boolean isAutoOpenFirstDocument()
    {
        return getConfigNode().getSafeBooleanValue("AutoOpenFirstDocument", false);
    }

    /** get the document functions definition node or null if config element is missing
     * <p>Read the configuration tag: OwSearchDocument.{@link #CONFIG_NODE_ENABLEDDOCUMENTFUNCTIONS}</p>
     * @return the document function plugin {@link OwXMLUtil} node or null if config element is missing
     * @throws Exception
     */
    protected OwXMLUtil getDocumentFunctionsNode() throws Exception
    {
        Node xmlNode = getConfigNode().getSubNode(CONFIG_NODE_ENABLEDDOCUMENTFUNCTIONS);
        return (xmlNode == null) ? null : new OwXMLUtilPlaceholderFilter(xmlNode, ((OwMainAppContext) getContext()).getConfiguration());
    }

    //OwObjectListViewEventListner

    @Override
    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {
        //void
    }

    @Override
    public String onObjectListViewGetRowClassName(int iIndex_p, OwObject obj_p)
    {
        return null;
    }

    @Override
    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        return false;
    }

    @Override
    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        //void

    }

    @Override
    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
        OwObjectListView objectListView = getObjectListView();
        int currentPage = objectListView.getCurrentPage();
        doSearch(m_lastMaxListSize, newSort_p);
        objectListView.setCurrentPage(currentPage);
    }

}