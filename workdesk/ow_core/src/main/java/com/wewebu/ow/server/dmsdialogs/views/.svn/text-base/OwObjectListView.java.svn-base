package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.ui.OwScriptTable;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Base class to all views that display object collections. Object collection view.
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class OwObjectListView extends OwView implements OwPageableView, OwClientRefreshContext
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectListView.class);

    public static final String SELECT_DESELECT_NONCONSECUTIVE_OBJECTS_ACTION_ID = "object.list.select.deselect.nonconsecutive";

    /** singleton class to retrieve HTML alignment attributes out of OwFieldColumnInfo alignment */
    protected static final OwHtmlAlignmentMap m_HtmlAlignments = new OwHtmlAlignmentMap();

    /** set of selectable operators */
    private static Set<Integer> m_maxoperators = new HashSet<Integer>();

    static
    {
        m_maxoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LIKE));
        m_maxoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_EQUAL));
        m_maxoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_LESS_EQUAL));
        m_maxoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_GREATER_EQUAL));
        m_maxoperators.add(Integer.valueOf(OwSearchOperator.CRIT_OP_BETWEEN));
    }

    /** name of the combobox for multi select events */
    protected static final String MULTISELECT_COMOBO_MENU_NAME = "owmcm";

    /** query string key for the object list index to find the selected object upon onMimeOpenObject. */
    protected static final String OBJECT_INDEX_KEY = "oi";

    /** XML node name for the paging node */
    private static final String PAGING_NODE_NAME = "paging";

    /** query string key for the plugin index. */
    protected static final String PLUG_INDEX_KEY = "pi";
    /** query string key for the page index. */
    protected static final String QUERY_KEY_PAGE = "page";

    /** query string key for the sort property. */
    protected static final String SORT_PROPERTY_KEY = "prop";

    /** view mask flag to control the display behavior
     * Enable inline editing of column data. 
     * */
    public static final int VIEW_MASK_INLINE_EDITING = 0x0000080;

    /** view mask flag to control the display behavior 
     * Enable plug-in invoke icons on each item row. 
     * Otherwise the plug-ins appear only in the context menu.
     * */
    public static final int VIEW_MASK_INSTANCE_PLUGINS = 0x0000040;

    /** view mask flag to control the display behavior 
     * Enable multi selection. 
     * If neither VIEW_MASK_MULTI_SELECTION nor VIEW_MASK_SINGLE_SELECTION is selected,
     * the resultlist does not allow selection at all. 
     * */
    public static final int VIEW_MASK_MULTI_SELECTION = 0x0000004;

    /** view mask flag to control the display behavior 
     * Disable the paging buttons on the resultlist. 
     * */
    public static final int VIEW_MASK_NO_PAGE_BUTTONS = 0x0000020;

    /** view mask flag to control the display behavior 
     * Enable single selection. 
     * If neither VIEW_MASK_MULTI_SELECTION nor VIEW_MASK_SINGLE_SELECTION is selected,
     * the resultlist does not allow selection at all. 
     * */
    public static final int VIEW_MASK_SINGLE_SELECTION = 0x0000010;

    //	 === view properties, behaviors
    /** view mask flag to control the display behavior 
     * Display document plug-ins if enabled, otherwise disable document plug-ins in the result list.  
     * */
    public static final int VIEW_MASK_USE_DOCUMENT_PLUGINS = 0x0000001;

    /** view mask flag to control the display behavior 
     * Enables a select button on each resultlist row.
     * The select button will send a select event via the result list listener.
     * The select event can be used to select an item in a other context. 
     * @see OwObjectListViewEventListner#onObjectListViewSelect(OwObject, OwObject)
     * */
    public static final int VIEW_MASK_USE_SELECT_BUTTON = 0x0000002;

    /** XML node name for the config node */
    private static final String VIEW_MASKS_NODE_NAME = "viewmasks";

    /** a list of plugins which should be displayed in context menu*/
    private List contextMenuPlugins;

    /** list of Document function plugins which have been instantiated */
    private List m_DocumentFunctionPluginList;

    /** event listener registered with this view to receive notifications */
    private OwObjectListViewEventListner m_EventListner;

    /** map with the current filter settings maps property names to OwFilterEntry */
    private OwFilter m_filter;

    /** sticky footer flag*/
    /*
     * @deprecated since 4.1.1.0 this attribute is no longer used and will be soon removed
     */
    @Deprecated
    protected boolean m_isStickyFooterInUse;
    /** set of flags indicating the behavior of the view*/
    private int m_iViewMask;

    /**The ID of this list. Should be accessed only through {@link #getListViewID()}*/
    private int m_listViewID = 0;
    /** page selector component */
    protected OwPageSelectorComponent m_pageSelectorComponent;

    /**
     * the paging node
     */
    protected Node m_pagingNode;

    /** map of persisted selection state */
    private Map m_persistedSelectionState = new HashMap();

    private boolean m_renderEmptyPluginColumn;

    private OwObject parentObject;

    /** a list of plugins that are displayed for each instance */
    private List pluginEntriesList;

    /** construct a object list view
     */
    public OwObjectListView()
    {

    }

    /** construct a object list view
     * 
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public OwObjectListView(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /**
     * Outputs JavaScript service end point (onAjax[eventName] callback method) for the 
     * specified event on a given {@link Writer}.
     * @param w_p
     * @param eventName_p the AJAX event name 
     * @throws Exception
     * @since 2.5.2.0
     */
    protected void addAjaxPersistenceService(Writer w_p, String eventName_p) throws Exception
    {
        // set the end point for the AJAX persistence service
        w_p.write("\n\n<script type=\"text/javascript\">addSelectionPersistanceEndpoint('");
        w_p.write(Integer.toString(getListViewID()));
        w_p.write("','");
        w_p.write(OwHTMLHelper.encodeJavascriptString(getAjaxEventURL(eventName_p, null)));
        w_p.write("'); </script>\n\n");
    }

    /** add a view property
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags to add to
     */
    public void addViewMask(int iViewMask_p)
    {
        m_iViewMask = (m_iViewMask | iViewMask_p);
    }

    /** clears a view property
     * 
     * @param iViewMask_p int to clear
     */
    protected void clearViewMask(int iViewMask_p)
    {
        // not multiselection needed without plugins, so clear multi selection flag
        m_iViewMask &= (iViewMask_p ^ 0xFFFFFFFF);

    }

    /** 
     * Copy the contents and state of the given view.
     *  
     * @param oldview_p OwObjectListView
     * @throws Exception 
     */
    public void copy(OwObjectListView oldview_p) throws Exception
    {
        if (oldview_p.getObjectList() != null)
        {
            setObjectList(oldview_p.getObjectList(), oldview_p.getParentObject());
        }
        else
        {
            setObjectIterable(oldview_p.getObjectIterable(), oldview_p.getParentObject());
        }
        setCurrentPage(oldview_p.getCurrentPage());
        setSort(oldview_p.getSort());
        setColumnInfo(oldview_p.getColumnInfo());
        setFilter(oldview_p.getFilter());
        setEventListner(oldview_p.getEventListner());
        setRefreshContext(oldview_p.getRefreshContext());
        setFieldManager(oldview_p.getFieldManager());
        if (oldview_p.getExternalFormEventTarget() != oldview_p)
        {
            setExternalFormTarget(oldview_p.getExternalFormEventTarget());
        }
    }

    /**
     * Create the paging component, according with the configuration setting. In case 
     * that no paging element is set, the direct input page selector component is used.
     * @return an instance of OwPageSelectorComponent. 
     * @since 2.5.2.0
     */
    public OwPageSelectorComponent createPageSelector() throws Exception
    {
        OwPageSelectorComponent component = null;
        String pageSelectorClassName = OwXMLDOMUtil.getSafeStringAttributeValue(m_pagingNode, "classname", null);
        if (pageSelectorClassName == null)
        {
            component = new OwDirectInputPageSelectorComponent(this);
        }
        else
        {

            try
            {
                Class pageSelectorClass = Class.forName(pageSelectorClassName);
                Constructor pageSelectorConstructor = pageSelectorClass.getConstructor(new Class[] { OwPageableView.class });
                component = (OwPageSelectorComponent) pageSelectorConstructor.newInstance(new Object[] { this });
                component.setConfigNode(m_pagingNode);
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage(), e);
                throw e;
            }
        }
        component.init();
        return component;
    }

    /** get the list of column info to be used by this list view
     * @return Collection of OwFieldColumnInfo's
     */
    public abstract Collection<? extends OwFieldColumnInfo> getColumnInfo();

    /**
     * Return a list of document function which
     * should be displayed in the context menu of current
     * view.
     * <p>This method filters the document functions from {@link #getDocumentFunctionPluginList()},
     *  checking the value of <code>&lt;ContextMenu&gt;[true|false]&lt;/ContextMenu&gt;</code>.<br />
     *  Also this method check if a document function need a parent object, if {@link #getParentObject()}
     *  is <code>null</code> the document function is skipped.</p>
     * @return list of OwPluginEntry for context menu
     */
    protected List getContextMenuFunction()
    {
        if (contextMenuPlugins == null)
        {
            contextMenuPlugins = new ArrayList();
            for (int i = 0; i < getDocumentFunctionPluginList().size(); i++)
            {
                OwDocumentFunction plugin = (OwDocumentFunction) getDocumentFunctionPluginList().get(i);
                if (plugin.getNeedParent() && (getParentObject() == null))
                {
                    continue;//skip this entry
                }

                if (plugin.getContextMenu())
                {
                    contextMenuPlugins.add(new OwPluginEntry(plugin, i));
                }
            }
        }
        return contextMenuPlugins;
    }

    public int getCount()
    {
        if (getObjectList() == null && getObjectIterable() == null)
        {
            return 0;
        }
        else
        {
            if (getObjectList() != null)
            {
                return getObjectList().size();
            }
            else
            {
                long totalNum = getObjectIterable().getTotalNumItems();
                if (totalNum == -1l)
                {
                    return -1;
                }
                else
                {
                    return (int) totalNum;
                }
            }
        }
    }

    /**
     * Returning the document function which has the given plugin index, can
     * return null if plugin not found or plugin list is empty.
     * @param globalPluginIndex_p int representing the unique plugin index, attention not the ID
     * @return OwDocumentFunction or <b>null</b> if plugin not found.
     */
    protected OwDocumentFunction getDocumentFunction(int globalPluginIndex_p)
    {
        return (OwDocumentFunction) getDocumentFunctionPluginList().get(globalPluginIndex_p);
    }

    /**
     * Return the current set document function list, which is used by
     * the view to display in context menu and plugin column.
     * @return List <code>java.util.List</code> of <code>OwDocumentFunction</code>
     */
    public List getDocumentFunctionPluginList()
    {
        return m_DocumentFunctionPluginList;
    }

    /** get the event listener */
    protected OwObjectListViewEventListner getEventListner()
    {
        return m_EventListner;
    }

    /** get the internal fieldmanager
     */
    public abstract OwFieldManager getFieldManager();

    public OwFilter getFilter()
    {
        return m_filter;
    }

    /** get the current filter search node to filter with
     * @return OwSearchNode or null if no filter is set
     * @throws Exception 
     */
    public OwSearchNode getFilterSearch() throws Exception
    {
        if (null != m_filter)
        {
            return m_filter.getFilterSearch();
        }
        else
        {
            return null;
        }
    }

    /**
     * Unique list view ID getter.<br>
     * Unique list view IDs are used as a discriminant in rendering multiple list view instances in one page.
     * If no ID was generated before (ie. {@link #m_listViewID} is 0) and this list does not have an <code>OwObjectListView</code>
     * parent a new unique ID is generated.<br>
     * If this list has an <code>OwObjectListView</code> a combined list 
     * aggregation is assumed and the ID of the parent becomes the ID of this list.
     * @return the unique ID of this list view
     * @see OwObjectListViewCombined
     * @since 2.5.2.0
     */
    public int getListViewID()
    {
        if (m_listViewID == 0)
        {
            OwView parent = getParent();
            if (parent != null)
            {
                if (parent instanceof OwObjectListView)
                {
                    OwObjectListView parentList = (OwObjectListView) parent;
                    m_listViewID = parentList.getListViewID();
                }
                else
                {
                    //Root list view
                    m_listViewID = hashCode();
                }
            }
            else
            {
                //Root list view
                m_listViewID = hashCode();
            }
        }

        return m_listViewID;
    }

    /**
     * Helper to retrieve an OwObject from index. 
     * @param index integer (zero based)
     * @return OwObject
     * @since 4.2.0.0
     */
    protected OwObject getObjectByIndex(int index)
    {
        if (getObjectList() != null)
        {
            return (OwObject) getObjectList().get(index);
        }
        else
        {
            return getObjectIterable().skipTo(index).iterator().next();
        }
    }

    public abstract OwIterable<OwObject> getObjectIterable();

    /** get the current object list */
    public abstract OwObjectCollection getObjectList();

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageableView#getPageAbsolutEventURL(java.lang.String)
     * @since 2.5.2.0
     */
    public String getPageAbsolutEventURL(String aditionalParameters_p)
    {
        return getEventURL("PageAbsolut", aditionalParameters_p);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageableView#getPageNextEventURL()
     * @since 2.5.2.0
     */
    public String getPageNextEventURL()
    {
        return getEventURL("PageNext", null);
    }

    /**
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageableView#getPagePrevEventURL()
     * @since 2.5.2.0
     */
    public String getPagePrevEventURL()
    {
        return getEventURL("PagePrev", null);
    }

    /** get the parent object
     * 
     * @return OwObject
     */
    public OwObject getParentObject()
    {
        return this.parentObject;
    }

    /**
     * This method returns a list of document plugins,
     * which should be displayed next to the document in the document function
     * column. 
     * <p>This method filters from the list of document functions ({@link #getDocumentFunctionPluginList()} ),
     *  all functions where &lt;ObjectInstance&gt; is false.<br />
     *  Also this method check if a document function need a parent object, if {@link #getParentObject()}
     *  is <code>null</code> the document function is skipped.</p>
     * @return List of OwPluginEntry, which are filtered document plugins or empty list if {@link #VIEW_MASK_INSTANCE_PLUGINS} not set
     */
    public List<OwPluginEntry> getPluginEntries()
    {
        if (null == pluginEntriesList)
        {
            // === create list of plugins which are visible
            pluginEntriesList = new ArrayList();
            if (hasViewMask(VIEW_MASK_INSTANCE_PLUGINS))
            {
                // iterate over preinstantiated plugins and find out, if plugin is visible and has a own column
                for (int p = 0; p < m_DocumentFunctionPluginList.size(); p++)
                {
                    OwDocumentFunction plugin = (OwDocumentFunction) m_DocumentFunctionPluginList.get(p);

                    // get the Need Parent Flag indicating that plugin can only working on documents listed by some parent.
                    if (plugin.getNeedParent() && (getParentObject() == null))
                    {
                        continue; // skip NeedParent plugins, we don't have a parent here
                    }
                    // check if plugin should appear aside of objects
                    if (plugin.getObjectInstance())
                    {
                        pluginEntriesList.add(new OwPluginEntry(plugin, p));
                    }
                }
            }
        }

        return pluginEntriesList;
    }

    /** get an eventlistener with this view to receive notifications
     * @return OwClientRefreshContext
     * */
    protected abstract OwClientRefreshContext getRefreshContext();

    /** get a collection of property names that are needed to display the Objects in the list
     *  i.e. these properties should be requested in advance to save server round-trips.
     *  @return Collection of String
     */
    public abstract Collection<String> getRetrievalPropertyNames() throws Exception;

    /** get the current selected sort instance
     * @return OwSort object
     */
    public abstract OwSort getSort();

    /**
     * Check if the current view must show the paging component.<br />
     * Will check first for <code>VIEW_MASK_NO_PAGE_BUTTONS</code> view mask,<br /> if not set calling
     * {@link #isPagingEnabled()} - use {@link #isPagingEnabled()} to extend check functionality.
     * @see com.wewebu.ow.server.dmsdialogs.views.OwPageableView#hasPaging()
     * @since 2.5.2.0
     */
    public final boolean hasPaging()
    {
        return !hasViewMask(OwObjectListView.VIEW_MASK_NO_PAGE_BUTTONS) && isPagingEnabled();
    }

    /** check if a certain view property is enabled 
     * @param iViewMask_p int as defined with VIEW_MASK_...
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return (m_iViewMask & iViewMask_p) > 0;
    }

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();
        m_renderEmptyPluginColumn = false;

        // get preloaded plugins reference only if they have not been set yet
        if (m_DocumentFunctionPluginList == null)
        {
            if (hasViewMask(VIEW_MASK_USE_DOCUMENT_PLUGINS))
            {
                // === use plugins
                // get plugins and context menu
                m_DocumentFunctionPluginList = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunctionPlugins();
            }
            else
            {
                // === don't use plugins
                // empty plugin list
                m_DocumentFunctionPluginList = new ArrayList();
            }
        }

    }

    /**
     * Flag to check if empty plugin columns should be rendered.
     * @return boolean flag
     * @since 3.0.0.0
     */
    public boolean isEmptyPluginColumnRendered()
    {
        return this.m_renderEmptyPluginColumn;
    }

    /**
     * Persistence selection state query method.
     * @param objectIndex_p object index 
     * @return <code>true</code> if the object with the given index has its selection state persisted as selected<br>
     *         <code>false</code> otherwise 
     * @since 2.5.2.0
     */
    protected boolean isObjectSelectionPersisted(int objectIndex_p)
    {
        Boolean selectedObj = (Boolean) m_persistedSelectionState.get(Integer.valueOf(objectIndex_p));
        return selectedObj != null && selectedObj.booleanValue();
    }

    /**
     * Helper method to check if a document function plugin (<b>plugIn_p</b>) supports one type,
     * which is given by the list (<b>objectTypes_p</b>). 
     * @param plugIn_p OwDocumentFunction which should be checked
     * @param objectTypes_p Set of java.lang.Integer which represents OwObjectReference.OBJECT_TYPE_... constants
     * 
     * @return boolean <code>true</code> if one of the types is supported
     */
    protected boolean isObjectTypeSupportedByPlugin(OwDocumentFunction plugIn_p, Set objectTypes_p)
    {
        boolean fSupported = false;
        Iterator it = objectTypes_p.iterator();
        while (it.hasNext())
        {
            if (plugIn_p.isObjectTypeSupported(((Integer) it.next()).intValue()))
            {
                fSupported = true;
                break;
            }
        }
        return fSupported;
    }

    /** Extended check for page selector functionality.
     * @return true = enable/display page buttons, by default false = disabled
     * @see #hasPaging()
     */
    protected boolean isPagingEnabled()
    {// no paging by default. Overwrite this method to enable paging
        return false;
    }

    @Override
    public boolean isShowMaximized()
    {
        if (getParent() == null)
        {
            return super.isShowMaximized();
        }
        else
        {
            return getParent().isShowMaximized();
        }
    }

    /**
     * Check if the paging component should be displayed as a sticky footer.
     * @return true - sticky footer is used
     * @since 2.5.2.0
     * @deprecated since 4.1.1.0 this method is no longer used and it will be soon removed.
     */
    @Deprecated
    public boolean isStickyFooterInUse()
    {
        boolean stickyFooterByContext = !(m_pageSelectorComponent instanceof OwNumberBlockPageSelectorComponent);
        return m_isStickyFooterInUse && stickyFooterByContext;
    }

    /** 
     * Move to the given zero based absolute page.
     * @param iPage_p int zero based page number 
     */
    protected void pageAbsolut(int iPage_p)
    {
        // no paging by default. Overwrite this method to enable paging
    }

    /**
     * Ajax selection persistence request support method.<br>
     * Subclasses should use this method on their AJAX selection persistence methods
     * providing their current object collection. 
     * 
     * @param request_p
     * @param response_p
     * @param objects_p the collection of objects this list view handles
     * @throws Exception
     * @since 2.5.2.0
     * @deprecated since 4.2.0.0 use {@link #persistAjaxTriggeredSelection(HttpServletRequest, HttpServletResponse)} instead
     */
    @Deprecated
    protected void persistAjaxTriggeredSelection(HttpServletRequest request_p, HttpServletResponse response_p, OwObjectCollection objects_p) throws Exception
    {
        // get params
        String sObjid = request_p.getParameter("objid");
        String sStatus = request_p.getParameter("status");
        // sanity checks
        if ((sObjid == null) || (sStatus == null))
        {
            return;
        }
        boolean status = Boolean.valueOf(sStatus);
        if ((!status) && (!Boolean.FALSE.toString().equals(sStatus)))
        {
            return;
        }
        // split objid into array
        String[] objectIds = sObjid.split(",");
        // persist all given object IDs
        final int objectsCount = (objects_p != null ? objects_p.size() : 0);

        for (int i = 0; i < objectIds.length; i++)
        {
            // try to convert objid to int
            int iObjid = -1;
            try
            {
                iObjid = Integer.parseInt(objectIds[i]);
            }
            catch (Exception e)
            {
                continue;
            }
            // test if object is in map
            if (iObjid >= objectsCount || objects_p.get(iObjid) == null)
            {
                continue;
            }
            persistObjectSelectionState(iObjid, status);
        }
    }

    /**
     * AJAX selection persistence request support method.<br>
     * Subclasses should use this method on their AJAX selection persistence methods
     * providing their current object collection. 
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     * @since 4.2.0.0
     */
    protected void persistAjaxTriggeredSelection(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // get params
        String sObjid = request_p.getParameter("objid");
        String sStatus = request_p.getParameter("status");
        // sanity checks
        if ((sObjid == null) || (sStatus == null))
        {
            return;
        }
        boolean status = Boolean.valueOf(sStatus);
        if ((!status) && (!Boolean.FALSE.toString().equals(sStatus)))
        {
            return;
        }
        // split objid into array
        String[] objectIds = sObjid.split(",");
        // persist all given object IDs
        //        final int objectsCount = getCount();

        for (int i = 0; i < objectIds.length; i++)
        {
            // try to convert objid to int
            int iObjid = -1;
            try
            {
                iObjid = Integer.parseInt(objectIds[i]);
            }
            catch (Exception e)
            {
                continue;
            }
            // test if object is in map
            //            if (iObjid >= objectsCount)
            //            {
            //                OwObject obj = null;
            //                if (objectsCount == -1)
            //                {
            //                    try
            //                    {
            //                        obj = getObjectByIndex(iObjid);
            //                    }
            //                    catch (Exception e)
            //                    {
            //                        LOG.debug("ObjectListView.persistAjaxTriggeredSelection: Could not retrieve object by index", e);
            //                    }
            //                }
            //                if (obj == null)
            //                {
            //                    continue;
            //                }
            //            }
            persistObjectSelectionState(iObjid, status);
        }
    }

    /**
     * Persist selection state for given object ID (number in list)
     * @param iObjid_p - the object ID (index in list)
     * @param status_p - the status of the object (<code>true</code> means selected)
     * @since 3.0.0.0
     */
    protected void persistObjectSelectionState(int iObjid_p, boolean status_p)
    {
        // persist current selection state
        m_persistedSelectionState.put(Integer.valueOf(iObjid_p), status_p ? Boolean.TRUE : Boolean.FALSE);
    }

    /**Default method to render the context menu.
    * This method check if the plugin supports one of the occuredObjectTypes,
    * and also if an parent object is need and available.
    * @param w_p Writer object to write the HTML
    * @param occuredObjectTypes_p Set of java.lang.Integer which representing OwObjectReference.OBJECT_TYPE_... constants
    */
    protected void renderContextMenu(java.io.Writer w_p, Set occuredObjectTypes_p) throws Exception
    {
        List pluginEntries = getContextMenuFunction();
        //don't render context menu if it doesn't contain any items
        if (pluginEntries.size() == 0)
        {
            return;
        }

        // === render menu
        // compute form name and menu ID according to menu type
        StringBuilderWriter sb = new StringBuilderWriter();
        OwScriptTable.writeSelectableListContextMenuStart(String.valueOf(getListViewID()), sb, "OwContextMenu");

        boolean isMenuEmpty = true;
        // === iterate over preinstantiated plugins and create HTML
        for (int p = 0; p < pluginEntries.size(); p++)
        {
            OwPluginEntry entry = (OwPluginEntry) pluginEntries.get(p);
            OwDocumentFunction plugIn = entry.getPlugin();

            // get the Need Parent Flag indicating that plugin can only working on documents listed by some parent.
            if (plugIn.getNeedParent() && (getParentObject() == null))
            {
                continue;
            }

            if (!isObjectTypeSupportedByPlugin(plugIn, occuredObjectTypes_p))
            {
                continue;
            }

            isMenuEmpty = false;
            // === create anchor with reference to the selected object and a tooltip
            String strEventURL = getEventURL("ContextMenuEvent", PLUG_INDEX_KEY + "=" + String.valueOf(entry.getIndex()));
            OwScriptTable.writeSelectableListContextMenuTREntry(String.valueOf(getListViewID()), sb, "OwContextMenu", p, strEventURL, getFormName(), plugIn.getDefaultIconHTML(), plugIn.getDefaultLabel());
            sb.write("\n\n<script type=\"text/javascript\">addContextMenuShortcut('");
            sb.write(plugIn.getPluginID());
            sb.write("',");
            sb.write(Integer.toString(getListViewID()));
            sb.write(",'");
            sb.write(strEventURL);
            sb.write("','");
            sb.write(getFormName());
            sb.write("');</script>\n\n");
            // === register key event
            ((OwMainAppContext) getContext()).registerPluginKeyEvent(plugIn.getPluginID(), "javascript:fireContextMenuShortcut('" + plugIn.getPluginID() + "')", null, plugIn.getDefaultLabel());
        }

        OwScriptTable.writeSelectableListContextMenuEnd(String.valueOf(getListViewID()), sb);
        sb.flush();

        if (!isMenuEmpty)
        {
            w_p.append(sb.getBuilder());
        }
    }

    /**
     * Resets all previously persist selection states.<br>
     * No object state will be persisted as selected.
     * @since 2.5.2.0
     */
    protected void resetPersistedSelectionState()
    {
        m_persistedSelectionState = new HashMap();
    }

    /** set the list of column info to be used by this list view
     * @param columnInfo_p List of OwFieldColumnInfo's
     */
    public abstract void setColumnInfo(Collection<? extends OwFieldColumnInfo> columnInfo_p);

    /** optional use the default constructor and set a config node to configure the view with XML 
     * This may override the settings in the ViewMaks, see setViewMask
     * 
     * @param node_p XML node with configuration information
     * @throws Exception 
     */
    public void setConfigNode(Node node_p) throws Exception
    {
        // === read the viewmasks from config node
        Node viewmasksNode = OwXMLDOMUtil.getChildNode(node_p, VIEW_MASKS_NODE_NAME);
        List masklist = OwXMLDOMUtil.getSafeStringList(viewmasksNode);

        Iterator it = masklist.iterator();

        m_iViewMask = 0;

        // combine them bitwise
        while (it.hasNext())
        {
            String sMask = (String) it.next();

            m_iViewMask |= getClass().getField(sMask).getInt(null);
        }
        m_pagingNode = OwXMLDOMUtil.getChildNode(node_p, PAGING_NODE_NAME);
    }

    /**
     * Public setter of current page.<br>
     * The implementation relays on {@link #pageAbsolut(int)} 
     * @param iPage_p int zero based page number 
     * @since 2.5.2.0
     */
    public final void setCurrentPage(int iPage_p)
    {
        pageAbsolut(iPage_p);
    }

    /**
     * Set a <code>java.util.List</code> of <code>OwDocumentFunction</code> to be used by this
     * list. This list overrides the default set of document functions that are retrieved from
     * the context during init.
     *
     * @param pluginList_p the <code>java.util.List</code> of <code>OwDocumentFunction</code> to be used by this list. Must not be <code>null</code>.
     */
    public void setDocumentFunctionPluginList(List pluginList_p)
    {
        m_DocumentFunctionPluginList = pluginList_p;
        // we need to re-generate the m_PluginEntriesList after changing the m_DocumentFunctionPluginList
        pluginEntriesList = null;
        contextMenuPlugins = null;
    }

    /** register an event listener with this view to receive notifications
     * @param eventlister_p OwObjectListCollectionEventListner interface
     */
    public void setEventListner(OwObjectListViewEventListner eventlister_p)
    {
        m_EventListner = eventlister_p;
    }

    /** set the internal fieldmanager
     *  in case you want a specific fieldmanager
     */
    public abstract void setFieldManager(OwFieldManager fielmanager_p);

    /** set the filter to be used
     * @param filter_p an {@link OwFilter}
     */
    public void setFilter(OwFilter filter_p)
    {
        m_filter = filter_p;
    }

    /**
     * set a pageable interface for retrieval of items to display.
     * @param iterable OwIterable returning OwObjects
     * @param parentObject OwObject
     * @throws Exception
     * @since 4.2.0.0
     */
    public abstract void setObjectIterable(OwIterable<OwObject> iterable, OwObject parentObject) throws Exception;

    /** set the list of objects to be displayed by this list view
     * @param objectList_p OwObjectCollection
     * @param parentObject_p OwObject parent which created the object list, can be null if no parent is specified
     */
    public abstract void setObjectList(OwObjectCollection objectList_p, OwObject parentObject_p) throws Exception;

    protected void setParentObject(OwObject newParent_p)
    {
        this.parentObject = newParent_p;
        //we need to refresh the list because the parent has changed
        if (this.pluginEntriesList != null)
        {
            this.pluginEntriesList.clear();
            this.pluginEntriesList = null;
        }
        if (this.contextMenuPlugins != null)
        {
            this.contextMenuPlugins.clear();
            this.contextMenuPlugins = null;
        }

    }

    /** register an event listener with this view to receive notifications
     * @param eventlistener_p OwClientRefreshContext interface
     * */
    public abstract void setRefreshContext(OwClientRefreshContext eventlistener_p);

    /**
     * Set flag to enable/disable empty column rendering.
     * @param renderEmptyColumn_p boolean flag
     * @since 3.0.0.0
     */
    public void setRenderEmptyPluginColumn(boolean renderEmptyColumn_p)
    {
        this.m_renderEmptyPluginColumn = renderEmptyColumn_p;
    }

    /** set / override current sort to given sort
     * @param sort_p new OwSort
     */
    public abstract void setSort(OwSort sort_p);

    /**
     * Set the stickyFooter flag.
     * @param stickyFooterInUse_p - the new value.
     * @since 2.5.2.0
     * @deprecated since 4.1.1.0 this method is no longer used and it will be soon removed.
     */
    @Deprecated
    public void setStickyFooterInUse(boolean stickyFooterInUse_p)
    {
        m_isStickyFooterInUse = stickyFooterInUse_p;
    }

    /** set the view properties
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public void setViewMask(int iViewMask_p)
    {
        m_iViewMask = iViewMask_p;
    }

    /** 
     * Create a filter with a Collection of OwFieldDefinition for the properties to be filtered
     * and fieldDefinitionProvider for wildcard conversion
     * 
     * @param filterprops_p Collection of OwFieldDefinition for the properties to be filtered, may be null
     * @param fieldDefinitionProvider_p OwFieldDefinitionProvider to resolve wildcards
     * @param sResourceName_p for fieldDefinitionProvider_p
     * @param sID_p String ID of the filter, if you keep a collection of several filters for one list, otherwise empty string
     * @param doc_p OwDocument can be null if no persistence should be used
     * 
     * @return OwFilter to be used in setFilter
     * @throws Exception 
     */
    public static OwFilter createFilter(Collection filterprops_p, OwFieldDefinitionProvider fieldDefinitionProvider_p, String sResourceName_p, String sID_p, OwDocument doc_p) throws Exception
    {
        OwFilter retFilter = new OwFilter(sID_p);

        if (null != filterprops_p)
        {
            Iterator it = filterprops_p.iterator();
            while (it.hasNext())
            {
                OwFieldDefinition info = (OwFieldDefinition) it.next();

                Integer firstOperator = (Integer) getFilterOperators(info).iterator().next();
                retFilter.put(info.getClassName(), new OwFilterEntry(info, fieldDefinitionProvider_p, sResourceName_p, firstOperator.intValue()));
            }
        }

        if (null != doc_p)
        {
            retFilter.load(doc_p.getPersistentAttributeBagWriteable());
        }

        return retFilter;
    }

    /** create a filter with a Collection of OwFieldDefinition for the properties to be filtered
     * 
     * @param filterprops_p Collection of OwFieldDefinition for the properties to be filtered, can be null to create an empty filter
     * @param sID_p String ID of the filter, if you keep a collection of several filters for one list, otherwise empty string
     * @param doc_p OwDocument can be null if no persistence should be used
     * 
     * @return OwFilter to be used in setFilter
     * @throws Exception 
     */
    public static OwFilter createFilter(Collection filterprops_p, String sID_p, OwDocument doc_p) throws Exception
    {
        OwFilter retFilter = new OwFilter(sID_p);

        if (null != filterprops_p)
        {
            Iterator it = filterprops_p.iterator();
            while (it.hasNext())
            {
                OwFieldDefinition info = (OwFieldDefinition) it.next();

                Integer firstOperator = (Integer) getFilterOperators(info).iterator().next();
                retFilter.put(info.getClassName(), new OwFilterEntry(info, firstOperator.intValue()));
            }
        }

        if (null != doc_p)
        {
            retFilter.load(doc_p.getPersistentAttributeBagWriteable());
        }

        return retFilter;
    }

    /** create an empty filter
     * 
     * @param sID_p String ID of the filter, if you keep a collection of several filters for one list, otherwise empty string
     * 
     * @return OwFilter to be used in setFilter
     * @throws Exception 
     */
    public static OwFilter createFilter(String sID_p) throws Exception
    {
        return new OwFilter(sID_p);
    }

    /** get a collection of possible filter operators for a given field
     * 
     * @param fielddef_p OwFieldDefinition
     * @return Collection of operators as defined with OwSearchOperator.CRIT_OP_...
     * @throws Exception 
     */
    public static Collection getFilterOperators(OwFieldDefinition fielddef_p) throws Exception
    {
        Collection ops = fielddef_p.getOperators();

        if (null == ops)
        {
            String msg = "OwObjectListView.getFilterOperators: Fielddef = " + fielddef_p.getClassName() + " contains no operators, but is used as a filter.";
            LOG.error(msg);
            throw new OwObjectNotFoundException(msg);
        }

        // === filter max operators
        LinkedList ret = new LinkedList();

        Iterator it = ops.iterator();
        while (it.hasNext())
        {
            Object op = it.next();

            if (m_maxoperators.contains(op))
            {
                ret.add(op);
            }
        }

        return ret;
    }

    //---------------------inner classes and interfaces----------------------------------------------------
    /**
     *<p>
     * A map of filter settings.
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
    public static class OwFilter extends HashMap
    {

        /** DOM name for  */
        protected static final String PERSIST_ENTRY_NAME_ATTR_NAME = "en";

        /** DOM name for persistence  */
        protected static final String PERSIST_FILTER_NODE_NAME = "FN";

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /** ID of this filter for persistence */
        protected String m_sID;

        /** create filter */
        public OwFilter(String sID_p)
        {
            m_sID = sID_p;
        }

        /** get a single filter setting
         * @param  strProperty_p String filter property
         * @return OwFilterEntry
         * @throws OwObjectNotFoundException 
         * */
        public OwFilterEntry getFilterEntry(String strProperty_p) throws OwObjectNotFoundException
        {
            OwFilterEntry filter = (OwFilterEntry) get(strProperty_p);
            if (filter == null)
            {
                throw new OwObjectNotFoundException("OwObjectListView$OwFilter.getFilterEntry: Property does not allow filter = " + strProperty_p);
            }

            return filter;
        }

        /** get the current filter search node to filter with
         * @return OwSearchNode or null if no filter is set
         * @throws Exception 
         */
        public OwSearchNode getFilterSearch() throws Exception
        {
            OwSearchNode filtersearchrootnode = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);

            boolean fSet = false;

            Iterator it = values().iterator();
            while (it.hasNext())
            {
                OwFilterEntry filter = (OwFilterEntry) it.next();

                if (filter.isActive())
                {
                    filtersearchrootnode.add(filter.getSearchNode());
                    fSet = true;
                }
            }

            if (fSet)
            {
                return filtersearchrootnode;
            }
            else
            {
                return null;
            }
        }

        /** get the attribute name for serialization 
         * 
         * @return a {@link String}
         */
        private String getSerializeAttributeName()
        {
            return "OwFilter_" + m_sID;
        }

        /** set this filter from the given attribute bag
         * 
         * @param persistentAttributeBagWriteable_p OwAttributeBagWriteable
         * @throws Exception 
         */
        public void load(OwAttributeBagWriteable persistentAttributeBagWriteable_p) throws Exception
        {
            String sXML = null;
            try
            {
                sXML = (String) persistentAttributeBagWriteable_p.getAttribute(getSerializeAttributeName());
            }
            catch (OwObjectNotFoundException e)
            {
                return;
            }

            Document doc = OwXMLDOMUtil.getDocumentFromString(sXML);

            for (Node entryNode = doc.getFirstChild().getFirstChild(); entryNode != null; entryNode = entryNode.getNextSibling())
            {
                String sEntryName = entryNode.getAttributes().getNamedItem(PERSIST_ENTRY_NAME_ATTR_NAME).getNodeValue();
                OwFilterEntry entry = (OwFilterEntry) get(sEntryName);
                if (null != entry)
                {
                    entry.setPersistentNode(entryNode);
                }
            }

        }

        /** persist this filter to the given attribute bag
         * 
         * @param persistentAttributeBagWriteable_p OwAttributeBagWriteable
         * @throws Exception 
         */
        public void save(OwAttributeBagWriteable persistentAttributeBagWriteable_p) throws Exception
        {
            // serialize filter settings in one string
            Document doc = OwXMLDOMUtil.getNewDocument();

            org.w3c.dom.Element saveNode = doc.createElement(PERSIST_FILTER_NODE_NAME);

            Iterator it = this.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry) it.next();
                String sKey = (String) entry.getKey();

                OwFilterEntry value = (OwFilterEntry) entry.getValue();
                if (value.isActive())
                {
                    Element entryNode = value.getPersistentNode(doc);
                    entryNode.setAttribute(PERSIST_ENTRY_NAME_ATTR_NAME, sKey);

                    saveNode.appendChild(entryNode);
                }
            }

            //OwStandardXMLUtil.toFile(new java.io.File("d:\\temp\\f.xml"), saveNode);

            persistentAttributeBagWriteable_p.setAttribute(getSerializeAttributeName(), OwXMLDOMUtil.toString(saveNode));
            persistentAttributeBagWriteable_p.save();
        }

        /** enable / disable filter */
        public void setActive(String strProperty_p, boolean fActive_p) throws OwObjectNotFoundException
        {
            getFilterEntry(strProperty_p).setActive(fActive_p);
        }
    }

    /**
     *<p>
     * Single filter setting.
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
    public static class OwFilterEntry
    {
        /** DOM name for persistence see get/setPersistentNode() */
        protected static final String PERSIST_ACTIVE_ATTR_NAME = "ac";
        /** DOM name for persistence see get/setPersistentNode() */
        protected static final String PERSIST_NODE_NAME = "FE";

        /** active flag */
        private boolean m_fActive;

        private OwFieldDefinitionProvider m_fielddefprovider;

        /** the search node this filter works with */
        private OwSearchNode m_searchnode;

        private String m_sRescourceName;

        /**
         * 
         * @param info_p
         * @param iOperator_p
         * @throws Exception
         */
        public OwFilterEntry(OwFieldDefinition info_p, int iOperator_p) throws Exception
        {
            m_searchnode = new OwSearchNode(info_p, iOperator_p, null, OwSearchCriteria.ATTRIBUTE_IGNORE_TIME);
        }

        /**
         * 
         * @param info_p
         * @param fieldDefinitionProvider_p
         * @param sResourceName_p
         * @param iOperator_p
         * @throws Exception
         */
        public OwFilterEntry(OwFieldDefinition info_p, OwFieldDefinitionProvider fieldDefinitionProvider_p, String sResourceName_p, int iOperator_p) throws Exception
        {
            m_sRescourceName = sResourceName_p;
            m_fielddefprovider = fieldDefinitionProvider_p;

            m_searchnode = new OwSearchNode(info_p, iOperator_p, null, OwSearchCriteria.ATTRIBUTE_IGNORE_TIME);

            // set wildcard definitions
            updateWildcarddefs(iOperator_p);
        }

        /** get a XML node that persists the current state of the criteria 
         * and that can be used with setPersistentNode() to recreate the state
         */
        public org.w3c.dom.Element getPersistentNode(org.w3c.dom.Document doc_p) throws Exception
        {
            org.w3c.dom.Element retNode = doc_p.createElement(PERSIST_NODE_NAME);

            // set active attribute
            retNode.setAttribute(PERSIST_ACTIVE_ATTR_NAME, String.valueOf(isActive()));

            // append search node node
            retNode.appendChild(getSearchNode().getPersistentNode(doc_p));

            return retNode;
        }

        /** the search node this filter works with */
        public OwSearchNode getSearchNode()
        {
            return m_searchnode;
        }

        /** get active flag */
        public boolean isActive()
        {
            return m_fActive;
        }

        /** enable / disable filter */
        public void setActive(boolean fActive_p)
        {
            m_fActive = fActive_p;
        }

        /** set the operator of the criteria
         * 
         * @param iOperator_p as defined in OwSearchOperator.CRIT_OP_...
         * @throws Exception 
         */
        public void setOperator(int iOperator_p) throws Exception
        {
            // set operator
            getSearchNode().getCriteria().setOperator(iOperator_p);

            // set wildcard definitions
            updateWildcarddefs(iOperator_p);
        }

        /** set a XML node that persists the current state of the criteria 
         * @throws Exception 
         * 
         */
        public void setPersistentNode(Node entryNode_p) throws Exception
        {
            m_fActive = OwXMLDOMUtil.getSafeBooleanAttributeValue(entryNode_p, PERSIST_ACTIVE_ATTR_NAME, false);
            m_searchnode.setPersistentNode(entryNode_p.getFirstChild());
        }

        /** toggle active state */
        public void toggle()
        {
            m_fActive = !m_fActive;
        }

        /** update the wildcard definitions for the given operator
         * 
         * @param iOperator_p
         * @throws Exception 
         */
        private void updateWildcarddefs(int iOperator_p) throws Exception
        {
            if (null != m_fielddefprovider)
            {
                // set wildcard definitions
                Collection wildcarddefinitions = m_fielddefprovider.getWildCardDefinitions(getSearchNode().getCriteria().getClassName(), m_sRescourceName, iOperator_p);
                getSearchNode().getCriteria().setWildCardDefinitions(wildcarddefinitions);
            }
        }
    }

    /**
     *<p>
     * Static class to retrieve HTML alignment attributes out of 
     * OwFieldColumnInfo alignment.
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
    protected static class OwHtmlAlignmentMap
    {
        public String[] m_list = new String[OwFieldColumnInfo.ALIGNMENT_MAX];

        public OwHtmlAlignmentMap()
        {
            m_list[OwFieldColumnInfo.ALIGNMENT_DEFAULT] = ""; // left is default
            m_list[OwFieldColumnInfo.ALIGNMENT_LEFT] = ""; // left is default
            m_list[OwFieldColumnInfo.ALIGNMENT_CENTER] = "align=center";
            m_list[OwFieldColumnInfo.ALIGNMENT_RIGHT] = "align=right";
            m_list[OwFieldColumnInfo.ALIGNMENT_LEFT_NOWRAP] = "nowrap"; // left is default
            m_list[OwFieldColumnInfo.ALIGNMENT_CENTER_NOWRAP] = "nowrap align=center";
            m_list[OwFieldColumnInfo.ALIGNMENT_RIGHT_NOWRAP] = "nowrap align=right";
        }
    }

    /**
     *<p>
     * Helper class to determine how many icons exists in a given string.
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
     *@since 3.0.0.0
     */
    public static class OwIcon
    {
        /**original string*/
        private String m_iconAsString;
        /**number of icons in the given string*/
        private int m_numberOfIcons;

        /**constructor*/
        public OwIcon(String owIconAsString_p)
        {
            this.m_iconAsString = owIconAsString_p;
            m_numberOfIcons = computeNumberOfIcons();
        }

        /**
         * compute number of icons.
         * @return number of icons.
         */
        private int computeNumberOfIcons()
        {
            int result = 0;
            if (this.m_iconAsString != null)
            {
                String iconText = m_iconAsString.toLowerCase();
                String[] tokens = iconText.split("<img");
                if (tokens.length > 0)
                {
                    result = tokens.length - 1;
                }
            }

            return result;
        }

        /**
         * Number of icons.
         * @return the calculated number of icons.
         */
        public int getNumberOfIcons()
        {
            return m_numberOfIcons;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return m_iconAsString != null ? m_iconAsString : "";
        }
    }

    /**
     *<p>
     * Event listener interface for OwObjectCollectionView's.
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
    public interface OwObjectListViewEventListner extends java.util.EventListener
    {
        /** called when user changes or activates the column filter 
        *
        * @param filterNode_p OwSearchNode with filter
        * @param parent_p OwObject parent if available, or null
        */
        public abstract void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception;

        /** get the style class name for the row
        *
        * @param iIndex_p int row index
        * @param obj_p current OwObject
        *
        * @return String with style class name, or null to use default
        */
        public abstract String onObjectListViewGetRowClassName(int iIndex_p, OwObject obj_p);

        /** called when uses clicks on a folder, used to redirect folder events an bypass the mimemanager
         * 
         * @param obj_p OwObject folder object that was clicked
         * @return boolean true = event was handled, false = event was not handled, do default handler
         * 
         * @throws Exception
         */
        public abstract boolean onObjectListViewItemClick(OwObject obj_p) throws Exception;

        /** called when user clicks a select button, fUseSelectButton_p must have been set to display select buttons 
         *
         * @param object_p OwObject object that was selected
         * @param parent_p OwObject parent if available, or null
         */
        public abstract void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception;

        /** called when uses clicks on a sort header and the sort changes
         * @param newSort_p OwSort new sort
         * @param strSortProperty_p String Property Name of sort property that was changed
         * */
        public abstract void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception;
    }

    /** a single entry for a plugins with its original index */
    public static class OwPluginEntry
    {
        /** index in the m_DocumentFunctionPluginList list, where all plugins are kept */
        protected int m_iIndex;

        /** reference to the plugin */
        protected OwDocumentFunction m_Plugin;

        /** a single entry for a plugins with its original index */
        public OwPluginEntry(OwDocumentFunction plugin_p, int iIndex_p)
        {
            m_Plugin = plugin_p;
            m_iIndex = iIndex_p;
        }

        public boolean equals(Object otherObject_p)
        {
            boolean result = false;
            if (otherObject_p instanceof OwPluginEntry)
            {
                OwPluginEntry otherEntry = (OwPluginEntry) otherObject_p;
                if (m_Plugin != null && m_Plugin.getPluginID() != null && otherEntry.m_Plugin != null && otherEntry.m_Plugin.getPluginID() != null)
                {
                    if (m_Plugin.getPluginID().compareTo(otherEntry.m_Plugin.getPluginID()) == 0 && m_iIndex == otherEntry.m_iIndex)
                    {
                        result = true;
                    }
                }
            }
            return result;
        }

        /**
         * Get the index.
         * @return - the index
         */
        public int getIndex()
        {
            return m_iIndex;
        }

        /**
         * Get the document function.
         * @return - current document function
         */
        public OwDocumentFunction getPlugin()
        {
            return m_Plugin;
        }

        public int hashCode()
        {
            return m_iIndex;
        }
    }
}
