package com.wewebu.ow.server.plug.owrecord;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDispatchCodes;
import com.wewebu.ow.server.app.OwDynamicLayoutSettings;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwLockDeniedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwPriorityRule;
import com.wewebu.ow.server.field.OwPriorityRuleFactory;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.history.OwStandardHistoryObjectChangeEvent;
import com.wewebu.ow.server.plug.owrecord.filter.OwFilterObjectCollection;
import com.wewebu.ow.server.plug.owrecord.filter.OwFilterRuntimeException;
import com.wewebu.ow.server.plug.owrecord.filter.OwRecordFilterOperatorHelper;
import com.wewebu.ow.server.plug.owrecord.log.OwLog;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwTreeView;
import com.wewebu.ow.server.ui.OwTreeView.OwTreeViewNode;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtilPlaceholderFilter;

/**
 *<p>
 * RecordDocument Implementation. The Document to the record management tab.
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
@SuppressWarnings("rawtypes")
public class OwRecordDocument extends OwMasterDocument implements OwTreeView.OwTreeViewEventListner, OwClientRefreshContext, OwObjectListView.OwObjectListViewEventListner
{
    /**
     * Specific record bag prefix, used to make difference between the split bar
     * between tree view and result list and the split bar between navigation pane and main pane 
     * @since 3.1.0.0
     */
    private static final String RECORD_BAG_PREFIX = "_record_";
    /**
    * Logger for this class
    */
    private static final Logger LOG = OwLog.getLogger(OwRecordDocument.class);
    /** enabled attribute for function plugin lists */
    public static final String PLUGIN_LIST_ENABLED_ATTRIBUTE = "enable";
    public static final String PLUGIN_DOC_CONFIG_PARAMETER = "docparameter";

    /** settings parameter name for the column info list for the node list view. */
    public static final String SETTINGS_PARAM_COLUMN_INFO = "columninfo";

    /** settings parameter name for the sorting. */
    public static final String SETTINGS_PARAM_SORT = "ColumnSortCriteria";

    /** configuration node name &lt;DocumentFunctionPlugins&gt; */
    public static final String CONFIG_NODE_DOCUMENTFUNCTIONPLUGINS = "DocumentFunctionPlugins";

    /** configuration node name &lt;RecordFunctionPlugins&gt; */
    public static final String CONFIG_NODE_RECORDFUNCTIONPLUGINS = "RecordFunctionPlugins";

    /** configuration node name &lt;EnabledDocumentFunctions&gt; */
    public static final String CONFIG_NODE_ENABLEDDOCUMENTFUNCTIONS = "EnabledDocumentFunctions";

    /** configuration node name {@value #CONFIG_NODE_SHOWFOLDERS}
     * @since 2.5.2.0 */
    protected static final String CONFIG_NODE_SHOWFOLDERS = "DisplayFoldersInResultList";

    /**
     * @since 4.2.0.0
     */
    protected static final String CONFIG_NODE_PAGEABLE = "pageable";

    /**
     * @since 4.2.0.0
     */
    protected static final String CONFIG_NODE_CLIENT_FILTER = "ClientFilter";

    /**
     * The configuration element for dynamic split flag
     * @since 3.1.0.0
     */
    public static final String CONFIG_NODE_USE_DYNAMIC_SPLIT = "UseDynamicSplit";

    /** max number of result items in a search */
    protected int m_iMaxSize = 50;

    /** the current root folder object */
    protected OwObject m_RootFolderObject;

    /** the current open sub folder object */
    protected OwObject m_SubFolderObject;

    /** the parent of the current open sub folder object */
    protected OwObject m_subFolderObjectParent;

    /** current open subfolder */
    protected String m_strSubFolderPath;

    /** reference to the tree view */
    protected OwTreeView m_ResultTreeView;

    /** reference to the result list view to display the search results */
    private OwObjectListView m_DocumentListView;

    /** boolen flag which is used to display folder in result list, default value is false*/
    private boolean showFolder;

    private boolean usePaging;
    private boolean clientFilterEnabled;

    /** current folder sort criteria */
    private OwSort m_FolderSortCriteria;

    /** column sort criteria */
    private OwSort m_ColumnSortCriteria;

    /** Map of List of OwPriorityRule rules to be applied on  records
     *  To be accessed via {@link #getRulesList()}.*/
    protected List m_rulesList;
    /** settings for dynamic split rendering*/
    private OwDynamicLayoutSettings m_layoutSettings;
    /** default column info list if no column info is found in folder object */
    private List m_defaultColumnInfoList;
    /** signal that a valid search is available */
    private boolean m_enablesearchtemplateview;
    /** Helper class for client side filtering
     * @since 3.2.0.0*/
    private OwRecordFilterOperatorHelper operatorHelper;

    /** set the document list view  
     * @param documentListView_p OwObjectListView
     * @throws Exception 
     */
    private void setDocumentListView(OwObjectListView documentListView_p) throws Exception
    {
        m_DocumentListView = documentListView_p;
        m_DocumentListView.setEventListner(this);

        // register document as event listener
        m_DocumentListView.setRefreshContext(this);
    }

    /** called from the search template view to signal that a valid search is available */
    public void enableSearchTemplateView(boolean enablesearchtemplateview_p)
    {
        m_enablesearchtemplateview = enablesearchtemplateview_p;
    }

    /** signal that a valid search is available and the view should be displayed */
    public boolean isSearchTemplateViewEnabled()
    {
        return m_enablesearchtemplateview;
    }

    /** signal that a the property preview should be displayed */
    public boolean isPropertyPreviewViewEnabled()
    {
        return getCurrentRootFolder() != null;
    }

    /** get the default column info for the child list if no column info is defined in the opened folder
     */
    protected Collection getDefaultColumnInfo() throws Exception
    {
        if (null == m_defaultColumnInfoList)
        {
            // try to get the default column info from the plugin settings
            List defaultPropertyNameList = (List) getSafeSetting(SETTINGS_PARAM_COLUMN_INFO, null);
            if (defaultPropertyNameList != null)
            {
                // create a column info list
                m_defaultColumnInfoList = new LinkedList();

                Iterator it = defaultPropertyNameList.iterator();

                while (it.hasNext())
                {
                    String strPropertyName = (String) it.next();

                    // get display name
                    OwFieldDefinition fielddef = null;
                    try
                    {
                        fielddef = ((OwMainAppContext) getContext()).getNetwork().getFieldDefinition(strPropertyName, null);
                    }
                    catch (OwObjectNotFoundException e)
                    {
                        // === property not found
                        // just set a warning when property load failed, we still keep continue working at least with the remaining properties
                        LOG.warn("Could not resolve property for contentlist: " + strPropertyName);

                        // remove invalid property
                        it.remove();

                        // try next one
                        continue;
                    }

                    // add column info
                    m_defaultColumnInfoList.add(new OwStandardFieldColumnInfo(fielddef));
                }
            }
            else
            {
                // === create empty default lists
                m_defaultColumnInfoList = new LinkedList();
            }
        }

        return m_defaultColumnInfoList;
    }

    /** get the optional searchtemplate to use or null if not defined 
     * @throws Exception */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        OwObject openObject = getCurrentSubFolderObject();

        if (openObject != null)
        {
            return openObject.getSearchTemplate();
        }
        else
        {
            return null;
        }
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_layoutSettings = createLayoutSettings();

        // set the maximum size for object children from plugin descriptor
        m_iMaxSize = getConfigNode().getSafeIntegerValue("MaxChildSize", 200);
        showFolder = getConfigNode().getSafeBooleanValue(CONFIG_NODE_SHOWFOLDERS, false);
        this.usePaging = getConfigNode().getSafeBooleanValue(CONFIG_NODE_PAGEABLE, false);

        this.clientFilterEnabled = getConfigNode().getSafeBooleanValue(CONFIG_NODE_CLIENT_FILTER, true);

        setFolderSortCriteria();
        operatorHelper = createFilterOperatorHelper();
    }

    /**
     * Create layout settings. 
     * @return the newly created object
     * @since 3.1.0.0
     */
    protected OwDynamicLayoutSettings createLayoutSettings() throws Exception
    {
        OwDynamicLayoutSettings result = null;
        String prefix = getConfiguration().getLayoutConfigurationBagPrefix() + RECORD_BAG_PREFIX + getPluginID();
        result = new OwDynamicLayoutSettings(isDynamicSplitInUse(), getContext(), prefix);
        return result;
    }

    /**
     * Check if dynamic split is used.
     * @return <code>true</code> if the dynamic split is used
     * @throws Exception
     * @since 3.1.0.0
     */
    public boolean isDynamicSplitInUse() throws Exception
    {
        //read first the bootstrap configuration
        boolean useDynamicSplit = getConfiguration().isDynamicSplitInUse();
        //override with Plugin configuration.
        useDynamicSplit = getConfigNode().getSafeBooleanValue("UseDynamicSplit", useDynamicSplit);
        return useDynamicSplit;
    }

    /** get the maximum size for object children
     * 
     * @return int max size to use for list items
     */
    public int getMaxChildSize()
    {
        return m_iMaxSize;
    }

    /** set the maximum size for object children
     * 
     * @param iMax_p int max child size
     */
    public void setMaxChildSize(int iMax_p)
    {
        m_iMaxSize = iMax_p;
    }

    /** get the currently opened folder
     * @return OwObject Current folder of null if not set
     */
    public OwObject getCurrentRootFolder()
    {
        return m_RootFolderObject;
    }

    /** get the currently opened sub folder object
     * @return OwObject Current sub folder object
     */
    public OwObject getCurrentSubFolderObject()
    {
        return m_SubFolderObject;
    }

    /**
     * 
     * @return  the Parent object of the current sub folder object<br>
     *          <code>null</code> if no parent is available       
     */
    public OwObject getCurrentSubFolderObjectParent()
    {
        return m_subFolderObjectParent;
    }

    /** get the currently opened sub folder path
     * @return String Current sub folder path
     */
    public String getCurrentSubFolderPath()
    {
        return m_strSubFolderPath;
    }

    /** get the currently opened sub folder display path 
     * @return String Current sub folder path
     */
    public String getCurrentSubFolderDisplayPath()
    {
        if (null != m_ResultTreeView.getCurrentNode())
        {
            return m_ResultTreeView.getCurrentNode().getDisplayPath();
        }
        else
        {
            return "";
        }
    }

    /** open the specified folder in the plugin
     * @param folder_p root folder {@link OwObject} to display
     * @param strSubFolderPath_p String path that designates a subfolder to open, can be null to open the root
     */
    public void openFolder(OwObject folder_p, String strSubFolderPath_p) throws Exception
    {
        String sLockFailedUser = null;

        String rootDMSID = "";
        if (m_RootFolderObject != null)
        {
            try
            {
                rootDMSID = m_RootFolderObject.getDMSID();
            }
            catch (Exception e)
            {
                LOG.error("Old root DMSID is invalid !", e);
                m_RootFolderObject = null;
            }
        }

        // check if root folder changed
        boolean fChanged = false;
        if ((m_RootFolderObject == null) || (!folder_p.getDMSID().equals(rootDMSID)))
        {
            fChanged = true;

            // === root folder changed
            if (supportLock())
            {
                // lock new folder
                if (folder_p.canLock())
                {
                    folder_p.setLock(true);

                    // check if it is now locked by me
                    if (!folder_p.getMyLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                    {
                        // == locked by another user
                        sLockFailedUser = folder_p.getLockUserID(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                    }
                }

                // unlock old folder
                if (null != m_RootFolderObject)
                {
                    if (m_RootFolderObject.canLock())
                    {
                        m_RootFolderObject.setLock(false);
                    }
                }
            }
        }

        if (strSubFolderPath_p == null)
        {
            strSubFolderPath_p = OwTreeView.PATH_DELIMITER;
        }

        // check if root- or subfolder folder identity changed
        if ((m_RootFolderObject != folder_p) || (!strSubFolderPath_p.equals(getCurrentSubFolderPath())))
        {
            // === folder changed
            // store folder object for later reference by the attached views
            m_RootFolderObject = folder_p;
            m_strSubFolderPath = strSubFolderPath_p;

            // set folder in tree view
            if (m_ResultTreeView != null)
            {
                m_ResultTreeView.navigate(folder_p, strSubFolderPath_p);
            }

            // inform views about document change
            update(this, OwUpdateCodes.SET_NEW_OBJECT, null);
        }

        if (fChanged)
        {
            //  historize plugin invoke event
            ((OwMainAppContext) getContext()).getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, getPluginID(), new OwStandardHistoryObjectChangeEvent(m_SubFolderObject, m_RootFolderObject),
                    OwEventManager.HISTORY_STATUS_OK);
        }

        if (null != sLockFailedUser)
        {
            throw new OwLockDeniedException(new OwString1("plug.owrecord.OwRecordDocument.lockedbyanotheruser", "File is currently used by user (%1)!", sLockFailedUser));
        }
    }

    /** set the result tree view to be used by the record plugin 
      * @param resultTreeView_p OwObjectTreeView
      */
    public void setResultTreeView(OwTreeView resultTreeView_p) throws Exception
    {
        m_ResultTreeView = resultTreeView_p;

        m_ResultTreeView.setEventListner(this);
    }

    /** This function will be overloaded by the targets (Views and Documents) to perform a generic action on this target.
     *  this is a generic function, used for communication of plugins,
     *  which do not know about the interfaces of each other.
     *
     *  @param iCode_p enumerator designating the requested action
     *  @param param1_p Placeholder for optional parameter
     *  @param param2_p Placeholder for optional parameter
     *  @return Object depending on derived implementation
     */
    public Object onDispatch(int iCode_p, Object param1_p, Object param2_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwDispatchCodes.OPEN_OBJECT:
                try
                {
                    openFolder((OwObject) param1_p, (String) param2_p);
                }
                catch (OwLockDeniedException e)
                {
                    throw e;
                }
                finally
                {
                    // Activate this view even if lock did not work,
                    // we can still view the record read only
                    getMasterView().activate();
                }

                break;
        }

        return null;
    }

    /** implementation of OwTreeView.OwTreeViewEventListner: event called when a user opens a folder to display its document contents 
     * overridden from OwObjectTreeView.OwObjectTreeViewEventListner
     *
     */
    public void onTreeViewNavigateFolder(OwTreeViewNode node_p) throws Exception
    {
        // ignore
    }

    /** implementation of OwTreeView.OwTreeViewEventListner: event called when a user navigates through the tree (using plus minus icon), but does not open a folder 
     * overridden from OwObjectTreeView.OwObjectTreeViewEventListner
     *
     */
    public void onTreeViewOpenFolder(OwTreeViewNode node_p) throws Exception
    {
        // === set new subfolder path
        m_strSubFolderPath = node_p.getPath();

        m_SubFolderObject = (OwObject) node_p.getObject();

        m_subFolderObjectParent = (OwObject) node_p.getObjectParent();

        // set sort
        OwSearchTemplate st = m_SubFolderObject.getSearchTemplate();
        if (null != st)
        {
            // set the sort from the search template associated with the current opened folder
            // set the max defined sort criteria upon construction.
            m_DocumentListView.setSort(st.getSort(((OwMainAppContext) getContext()).getMaxSortCriteriaCount()));
        }

        // inform attached views
        update(this, OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);
        m_DocumentListView.setCurrentPage(0);
    }

    /** is lock supported in plugin definition
     * */
    public boolean supportLock()
    {
        return getConfigNode().getSafeBooleanValue("LockObject", true);
    }

    /** causes all attached views to receive an onUpdate event
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void update(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        switch (iCode_p)
        {
            case OwUpdateCodes.CHANGE_VIEW:
                setDocumentListView((OwObjectListView) param_p);
                break;

            case OwUpdateCodes.LOGOUT:
                if (supportLock())
                {
                    // unlock locked folder
                    if (null != m_RootFolderObject)
                    {
                        if (m_RootFolderObject.canLock())
                        {
                            m_RootFolderObject.setLock(false);
                        }
                    }
                }
                break;

            case OwUpdateCodes.DELETE_OBJECT:
                // close record
                m_RootFolderObject = null;
                m_SubFolderObject = null;
                m_strSubFolderPath = null;
                displayFolderContent();

                m_ResultTreeView.navigate(null, null);
                break;

            case OwUpdateCodes.UPDATE_OBJECT_CHILDS:
                if (null != param_p)
                {
                    m_ResultTreeView.refreshNodeForObject(param_p);
                }
                m_ResultTreeView.refreshCurrentNode();
                displayFolderContent();
                break;

            case OwUpdateCodes.UPDATE_PARENT_OBJECT_FOLDER_CHILDS:
                m_ResultTreeView.navigateUp();
                m_ResultTreeView.refreshCurrentNode();
                break;

            case OwUpdateCodes.MODIFIED_OBJECT_PROPERTY:
                m_ResultTreeView.refreshCurrentNode();
                displayFolderContent();
                break;

            case OwUpdateCodes.UPDATE_OBJECT_FOLDER_CHILDS:
                m_ResultTreeView.refreshCurrentNode();

                // navigate to the new child if available
                OwObject obj = (OwObject) param_p;
                if ((null != obj) && (m_ResultTreeView.canNavigate()))
                {

                    m_ResultTreeView.navigate(m_strSubFolderPath + obj.getID() + OwTreeView.PATH_DELIMITER);
                }

                break;

            case OwUpdateCodes.UPDATE_SETTINGS:
                // force update
                m_defaultColumnInfoList = null;
                break;
        }

        super.update(caller_p, iCode_p, param_p);
    }

    /** display the current folder, i.e. list the document children
     *
     */
    @SuppressWarnings("unchecked")
    private void displayFolderContent() throws Exception
    {
        if (null != m_SubFolderObject)
        {
            Collection columnInfoList = m_SubFolderObject.getColumnInfoList();

            // === set the result view with result list and column info
            if ((columnInfoList == null) || (columnInfoList.size() == 0))
            {
                // === no column info found in object, use default
                columnInfoList = getDefaultColumnInfo();

                // === set the column sort criteria as set in settings
                setColumnSortCriteria();
            }

            // set column info
            m_DocumentListView.setColumnInfo(columnInfoList);

            // === set filter
            Collection filterpros = collectFilterProperties(columnInfoList);
            // TODO: create separate filter for each folder
            if (m_SubFolderObject.getResource() == null)
            {
                m_DocumentListView.setFilter(OwObjectListView.createFilter(filterpros, "", this));
            }
            else
            {
                m_DocumentListView.setFilter(OwObjectListView.createFilter(filterpros, ((OwMainAppContext) getContext()).getNetwork(), m_SubFolderObject.getResourceID(), "", this));
            }

            // === check search criteria

            // check search
            if (!checkValidSearchCriteria())
            {
                // === no valid search, clear list do not get the children and perform search
                clearObjectListContent(null);
                return;
            }

            OwLoadContext loadContext = getLoadContext();
            setObjectListContent(loadContext);
        }
        else
        {
            clearObjectListContent(null);
        }
    }

    @SuppressWarnings("unchecked")
    protected OwLoadContext getLoadContext() throws Exception
    {
        OwLoadContext loadContext = new OwLoadContext();

        Collection propertyNameList = m_DocumentListView.getRetrievalPropertyNames();
        loadContext.setPropertyNames(propertyNameList);

        int[] types;
        if (showFolderInResultList())
        {
            types = new int[4];
            types[3] = OwObjectReference.OBJECT_TYPE_FOLDER;
        }
        else
        {
            types = new int[3];
        }

        types[0] = OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS;
        types[1] = OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS;
        types[2] = OwObjectReference.OBJECT_TYPE_ALL_TUPLE_OBJECTS;
        loadContext.setObjectTypes(types);

        OwSort sort = m_DocumentListView.getSort();
        loadContext.setSorting(sort);

        int maxChildSize = getMaxChildSize();
        loadContext.setMaxSize(maxChildSize);

        int versionSelectDefault = OwSearchTemplate.VERSION_SELECT_DEFAULT;
        loadContext.setVersionSelection(versionSelectDefault);

        OwSearchNode filterCriteria = m_DocumentListView.getFilterSearch();
        loadContext.setFilter(filterCriteria);
        return loadContext;
    }

    private void clearObjectListContent(OwObject parentObject) throws Exception
    {
        if (this.usePaging)
        {
            m_DocumentListView.setObjectIterable(null, parentObject);
        }
        else
        {
            m_DocumentListView.setObjectList(null, parentObject);
        }
    }

    @SuppressWarnings("unchecked")
    private void setObjectListContent(OwLoadContext loadContext) throws Exception
    {
        if (this.usePaging && m_SubFolderObject instanceof OwPageableObject)
        {
            OwIterable iterable = ((OwPageableObject) m_SubFolderObject).getChildren(loadContext);
            m_DocumentListView.setObjectIterable(iterable, m_SubFolderObject);
        }
        else
        {
            // === get and display children
            OwObjectCollection documentList = m_SubFolderObject.getChilds(loadContext.getObjectTypes(), loadContext.getPropertyNames(), loadContext.getSorting(), (int) loadContext.getMaxSize(), loadContext.getVersionSelection(),
                    loadContext.getFilter());

            // display in attached object list
            m_DocumentListView.setObjectList(documentList, m_SubFolderObject);
        }
    }

    /**
     * Create a filter properties collection which will be provided to the current OwObjectCollection.
     * @param columnInfoList_p Collection of OwFieldColumnInfo representing the column which should be shown
     * @return Collection of OwFieldDefinition
     * @throws Exception
     * @since 3.2.0.0
     */
    protected Collection<OwFieldDefinition> collectFilterProperties(Collection columnInfoList_p) throws Exception
    {
        if (!this.isClientFilterEnabled() && !this.m_SubFolderObject.canFilterChilds())
        {
            return Collections.emptyList();
        }
        return operatorHelper.collectFilterProperties(m_SubFolderObject, ((OwMainAppContext) getContext()).getNetwork(), columnInfoList_p);
    }

    /** (overridable) we must not send empty searches to the ECM system in order to prevent mass requests
     * 
     * @return true = search is valid and can be submitted
     */
    protected boolean checkValidSearchCriteria()
    {
        return true;
    }

    /** implementation of the OwFunction.OwFunctionRefreshContext interface
     *  Called from a plugin to inform its client and cause refresh of display data
     *
     * @param iReason_p reason as defined with OwFunction.REFRESH_...
     * @param param_p Object optional parameter representing the refresh, depends on the value of iReason_p, can be null
     */
    public void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception
    {
        // === plugin requests a refresh
        // translate messages
        switch (iReason_p)
        {
            case OwUpdateCodes.UPDATE_OBJECT_VERSION:
            case OwUpdateCodes.DELETE_OBJECT:
            case OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS:
                update(this, OwUpdateCodes.UPDATE_OBJECT_CHILDS, param_p);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {
        if (this.usePaging && m_SubFolderObject instanceof OwPageableObject)
        {
            if (null != filterNode_p)
            {
                OwLoadContext loadContext = getLoadContext();
                loadContext.setFilter(filterNode_p);

                OwIterable iterable = ((OwPageableObject) m_SubFolderObject).getChildren(loadContext);
                m_DocumentListView.setObjectIterable(iterable, m_SubFolderObject);
            }
        }
        else
        {
            OwObjectCollection col = m_DocumentListView.getObjectList();
            OwFilterObjectCollection filterCol = null;
            if (col instanceof OwFilterObjectCollection)
            {
                filterCol = (OwFilterObjectCollection) col;
            }
            else
            {
                filterCol = new OwFilterObjectCollection(col);
            }

            if (filterNode_p != null)
            {
                try
                {
                    filterCol.setFilter(filterNode_p);
                    m_DocumentListView.setObjectList(filterCol, parent_p);
                }
                catch (OwFilterRuntimeException ex)
                {
                    m_DocumentListView.setObjectList(null, parent_p);
                    LOG.error(ex.getMessage(), ex);
                    throw new OwInvalidOperationException(getContext().localize("plug.owrecord.OwRecordDocument.processFilterEx", "Could not process defined filter."), ex);
                }
            }
            else
            {
                m_DocumentListView.setObjectList(filterCol.getNativeCollection(), parent_p);
                filterCol.clear();
            }
        }
    }

    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {

        return false;
    }

    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {

    }

    @SuppressWarnings("unchecked")
    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
        if (this.usePaging && m_SubFolderObject instanceof OwPageableObject)
        {
            OwLoadContext loadContext = getLoadContext();

            OwIterable iterable = ((OwPageableObject) m_SubFolderObject).getChildren(loadContext);
            m_DocumentListView.setObjectIterable(iterable, m_SubFolderObject);
        }
        else
        {
            OwObjectCollection list = this.m_DocumentListView.getObjectList();
            if (null != list)
            {
                list.sort(this.m_DocumentListView.getSort());
                this.m_DocumentListView.setObjectList(list, this.m_SubFolderObject);
            }
        }
    }

    /** get the document function plugin definition node
     * 
     * @return the document function plugin {@link OwXMLUtil}
     * @throws Exception
     */
    protected OwXMLUtil getDocumentFunctionPluginsNode() throws Exception
    {
        return new OwXMLUtilPlaceholderFilter(getConfigNode().getSubNode(CONFIG_NODE_DOCUMENTFUNCTIONPLUGINS), ((OwMainAppContext) getContext()).getConfiguration());
    }

    /** get the record function plugin definition node
     * 
     * @return the record function plugin {@link OwXMLUtil} node 
     * @throws Exception
     */
    protected OwXMLUtil getRecordFunctionPluginsNode() throws Exception
    {
        return new OwXMLUtilPlaceholderFilter(getConfigNode().getSubNode(CONFIG_NODE_RECORDFUNCTIONPLUGINS), ((OwMainAppContext) getContext()).getConfiguration());
    }

    /** get the document functions definition node or null if config element is missing
     * <p>Read the configuration tag: OwRecordDocument.{@link #CONFIG_NODE_ENABLEDDOCUMENTFUNCTIONS}</p>
     * @return the document function plugin {@link OwXMLUtil} node or null if config element is missing
     * @throws Exception
     */
    protected OwXMLUtil getDocumentFunctionsNode() throws Exception
    {
        Node xmlNode = getConfigNode().getSubNode(CONFIG_NODE_ENABLEDDOCUMENTFUNCTIONS);
        return (xmlNode == null) ? null : new OwXMLUtilPlaceholderFilter(xmlNode, ((OwMainAppContext) getContext()).getConfiguration());
    }

    /** check if any function plugins are configured
     * 
     * @return a <code>boolean</code>
     * @throws Exception
     */
    protected boolean getIsPluginsEnabled() throws Exception
    {
        return (getDocumentFunctionPluginsNode().getSafeBooleanAttributeValue(PLUGIN_LIST_ENABLED_ATTRIBUTE, false) || getRecordFunctionPluginsNode().getSafeBooleanAttributeValue(PLUGIN_LIST_ENABLED_ATTRIBUTE, false));
    }

    /**
     * Returns the folder sort criteria as defined in plugin description node.<br/>
     * Folder sort criteria can be defined via <code>&lt;FolderSortCriteria&gt;</code> 
     * tag in <code>owplugins.xml</code>.<p>
     * If no criteria isn't defined the method will return an {@link OwSort}
     * object with no {@link com.wewebu.ow.server.field.OwSort.OwSortCriteria} contained.
     * @return sort criteria list with the defined sort criteria 
     */
    protected OwSort getFolderSortCriteria()
    {
        return m_FolderSortCriteria;
    }

    /**
     * Set the folder sort criteria as defined in plugin description node.<br/>
     * Folder sort criteria can be defined via <code>&lt;FolderSortCriteria&gt;</code> 
     * tag in <code>owplugins.xml</code>.<br/>
     * Double defined properties will be filtered out.
     * <p>
     * This method should be called only once by the {@link #init()} method.<br/>
     * If no criteria isn't defined the method will create an {@link OwSort}
     * object with no {@link com.wewebu.ow.server.field.OwSort.OwSortCriteria} contained.<br/>
     * You can retrieve the created criteria via {@link #getFolderSortCriteria()} method.
     */
    private void setFolderSortCriteria()
    {
        //get sort criteria properties from config node
        List properties = getConfigNode().getSafeUtilList("FolderSortCriteria", "property");

        LinkedList sortCriterias = new LinkedList();
        HashSet duplicateDedectionSet = new HashSet(properties.size());

        for (Iterator it = properties.iterator(); it.hasNext();)
        {
            OwXMLUtil prop = (OwXMLUtil) it.next();
            //retrieve property
            String sortCriteria = prop.getSafeTextValue(null);
            if (sortCriteria == null)
            {
                StringBuffer buf = new StringBuffer();
                buf.append("owplugins.xml - master plugin with id=");
                buf.append(this.getPluginID());
                buf.append(": tag <FolderSortCriteria> is configured wrongly:");
                buf.append(" one <property> tag contains no value.");
                buf.append(" This tag was ignored");
                LOG.warn(buf.toString());
                //no property defined, just continue
                continue;
            }
            if (duplicateDedectionSet.contains(sortCriteria))
            {
                StringBuffer buf = new StringBuffer();
                buf.append("owplugins.xml - master plugin with id=");
                buf.append(this.getPluginID());
                buf.append(": tag <FolderSortCriteria> is configured wrongly:");
                buf.append(" one <property> tag contains a value=");
                buf.append(sortCriteria);
                buf.append(" that was defined before.");
                buf.append(" This tag was ignored");
                LOG.warn(buf.toString());
                //property was defined twice, just continue
                continue;
            }

            duplicateDedectionSet.add(sortCriteria);
            //retrieve sorting order attribute
            boolean sortAscending = prop.getSafeBooleanAttributeValue("sortascending", true);
            //add to sort criteria list
            sortCriterias.add(new OwSortCriteria(sortCriteria, sortAscending));
        }

        //create new sort criteria list
        m_FolderSortCriteria = new OwSort(sortCriterias.size(), true);

        /*first criteria defined in owplugins.xml should have highest priority, but in
         * OwSort the latest added criteria have highest priority. So it is necessary to
         * add criteria in reverse order.
        */
        for (int i = sortCriterias.size() - 1; i >= 0; i--)
        {
            m_FolderSortCriteria.addCriteria((OwSortCriteria) sortCriterias.get(i));
        }

    }

    /**
     * Sort the displayed columns sort criteria.<br/>
     * Column sort criteria can be defined via <code>&lt;ColumnSortCriteria&gt;</code> 
     * tag in <code>owplugins.xml</code> or Settings plugin.<br/>
     * Double defined properties will be filtered out.
     */
    private void setColumnSortCriteria() throws Exception
    {
        List properties = (List) getSafeSetting(SETTINGS_PARAM_SORT, null);

        if (properties != null)
        {
            LinkedList sortCriterias = new LinkedList();
            HashSet duplicateDedectionSet = new HashSet(properties.size());
            // get already set criteria
            m_ColumnSortCriteria = m_DocumentListView.getSort();

            //create new sort criteria list
            if (m_ColumnSortCriteria == null)
            {
                m_ColumnSortCriteria = new OwSort(sortCriterias.size(), true);
            }

            Iterator it = properties.iterator();

            while (it.hasNext())
            {
                OwSortCriteria sortCriteria = (OwSortCriteria) it.next();

                if (sortCriteria == null)
                {
                    StringBuffer buf = new StringBuffer();
                    buf.append("owplugins.xml - master plugin with id=");
                    buf.append(this.getPluginID());
                    buf.append(": tag <ColumnSortCriteria> is configured wrongly:");
                    buf.append(" one <property> tag contains no value.");
                    buf.append(" This tag was ignored");
                    LOG.warn(buf.toString());
                    //no property defined, just continue
                    continue;
                }
                if (duplicateDedectionSet.contains(sortCriteria))
                {
                    StringBuffer buf = new StringBuffer();
                    buf.append("owplugins.xml - master plugin with id=");
                    buf.append(this.getPluginID());
                    buf.append(": tag <ColumnSortCriteria> is configured wrongly:");
                    buf.append(" one <property> tag contains a value=");
                    buf.append(sortCriteria.getPropertyName());
                    buf.append(" that was defined before.");
                    buf.append(" This tag was ignored");
                    LOG.warn(buf.toString());
                    //property was defined twice, just continue
                    continue;
                }

                duplicateDedectionSet.add(sortCriteria);

                boolean sortAscending = sortCriteria.getAscFlag();

                //add to sort criteria list, if not already contained
                if (m_ColumnSortCriteria.getCriteria(sortCriteria.getPropertyName()) == null)
                {
                    m_ColumnSortCriteria.addCriteria(new OwSortCriteria(sortCriteria.getPropertyName(), sortAscending));
                }
            }
        }
    }

    /**
     * One time priority rules read utility 
     * @return a list of {@link OwPriorityRule}s
     * @throws Exception if the priority rules cannot be created 
     */
    protected List getRulesList() throws Exception
    {
        if (m_rulesList == null)
        {
            OwMainAppContext mainAppContext = (OwMainAppContext) getContext();
            OwNetwork fieldProvider = mainAppContext.getNetwork();
            OwXMLUtil configNode = getConfigNode();
            OwPriorityRuleFactory rulesFactory = OwPriorityRuleFactory.getInstance();
            m_rulesList = rulesFactory.createRulesList(configNode, fieldProvider);
        }

        return m_rulesList;
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
        try
        {
            List ruleList = getRulesList();
            for (Iterator i = ruleList.iterator(); i.hasNext();)
            {
                OwPriorityRule rule = (OwPriorityRule) i.next();
                if (rule.appliesTo(obj_p))
                {
                    return rule.getStylClass();
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Could not load record document priority rules !", e);
        }

        return null;
    }

    /**
     * Flag which is used to verify, if folders should be shown in
     * result list or not.
     * @return boolean (by default <code>false</code>)
     * @since 2.5.2.0
     */
    protected boolean showFolderInResultList()
    {
        return showFolder;
    }

    /**
     * Get the id of the configured {@link OwTreeView} object.
     * @return - the id of the configured {@link OwTreeView} object.
     * @since 3.1.0.0
     */
    public String getTreeViewId()
    {
        return m_ResultTreeView.getClientSideId();
    }

    /**
     * Get the {@link OwDynamicLayoutSettings} instance.  
     * @return the {@link OwDynamicLayoutSettings} instance. 
     * @since 3.1.0.0
     */
    public OwDynamicLayoutSettings getLayoutSettings()
    {
        return m_layoutSettings;
    }

    /**(overridable)
     * Factory method to create an instance of a FilterOperatorHelper.
     * @return OwRecordFilterOperatorHelper
     * @since 3.2.0.0
     */
    protected OwRecordFilterOperatorHelper createFilterOperatorHelper()
    {
        return new OwRecordFilterOperatorHelper();
    }

    /**
     * Checks if the client side (OWD) filtering is enabled.
     * 
     * @return the clientFilterEnabled
     * @since 4.2.0.0
     */
    public boolean isClientFilterEnabled()
    {
        return clientFilterEnabled;
    }

    /**
     * 
     * @return <code>true</code> if show attribute of <code>MaxSizeMax, show="true|false"</code> is <code>true</code>, or <br/>
     *         <code>false</code> if <code>show</code> attribute is <code>false</code> 
     * @since 4.2.0.0
     */
    public boolean isMaxSizeEnabled()
    {
        boolean maxSizeEnabled = true;
        try
        {
            OwMasterDocument documentM = (this);
            OwXMLUtil configNode = documentM.getConfigNode().getSubUtil("MaxSizeMax");
            if (configNode != null)
            {
                Node maxMaxSizeMax = configNode.getNode();
                if (maxMaxSizeMax != null)
                {
                    boolean show = OwXMLDOMUtil.getSafeBooleanAttributeValue(maxMaxSizeMax, "show", true);
                    maxSizeEnabled = show;
                }
            }

        }
        catch (Exception e)
        {
            LOG.error("isMaxSizeEnabled()", e);
        }
        return maxSizeEnabled;
    }
}