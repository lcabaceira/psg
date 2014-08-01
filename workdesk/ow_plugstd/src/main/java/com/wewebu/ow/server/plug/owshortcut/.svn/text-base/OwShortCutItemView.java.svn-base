package com.wewebu.ow.server.plug.owshortcut;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardUnresolvedReference;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwStandardXMLUtil;

/**
 *<p>
 * Displays {@link OwShortCutItem}s.<br/>
 * The view is based on the {@link OwShortCutDocument} model object and
 * renders object-pointing shortcuts and non-object-pointing shortcuts into two separates lists.
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
public class OwShortCutItemView extends OwView implements OwShortCutItemContext, OwObjectListViewEventListner, OwShortcutDocumentEventListner
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutItemView.class);

    public static final int SHORTCUT_ITEM_FILTER_ALL = 0;
    public static final int SHORTCUT_ITEM_FILTER_DOCUMENTS = 1;
    public static final int SHORTCUT_ITEM_FILTER_FOLDERS = 2;
    public static final int SHORTCUT_ITEM_FILTER_REST = 3;

    private static final String SHORTCUT_ITEM_ID_KEY = "scid";

    /**
     * Inline delete shortcut function to be used in lists displaying {@link OwObject} pointing shortcut
     * @since 2.5.3.0
     *
     */
    public class OwDeleteShortcutFunction extends OwDocumentFunction
    {
        private static final String PLUGIN_ID_PREFIX = "delete.shortcut";

        private String m_pluginID;

        /**
         * Constructor
         * 
         */
        public OwDeleteShortcutFunction()
        {
            super();
            Random random = new Random(System.currentTimeMillis());
            int randomSuffix = random.nextInt(Integer.MAX_VALUE);
            m_pluginID = PLUGIN_ID_PREFIX + "." + randomSuffix;
        }

        /** get the URL to the icon of the dialog / function
         */
        public String getIcon() throws Exception
        {
            return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocshortcut/bookmark_delete.png");
        }

        /** get the URL to the icon of the dialog / function
         */
        public String getBigIcon() throws Exception
        {
            return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocshortcut/bookmark_delete_24.png");
        }

        public void onClickEvent(OwObject object_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
        {
            List<OwObject> oneObjectList = new LinkedList<OwObject>();
            oneObjectList.add(object_p);
            onMultiselectClickEvent(oneObjectList, parent_p, refreshCtx_p);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void onMultiselectClickEvent(Collection objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
        {
            List<String> idsToDelete = new LinkedList<String>();
            for (Iterator<OwObject> i = objects_p.iterator(); i.hasNext();)
            {
                OwObject object = i.next();
                OwShortCutItem shrtcutItem = getShortcutForObject(object);
                idsToDelete.add(shrtcutItem.getId());

            }

            getShortcutDocument().deleteShortCuts(idsToDelete);
        }

        @SuppressWarnings("rawtypes")
        protected Set getSupportedObjectTypesFromDescriptor(String strKey_p) throws OwConfigurationException
        {
            Integer[] supportedObjects = new Integer[] { new Integer(OwObjectReference.OBJECT_TYPE_DOCUMENT), new Integer(OwObjectReference.OBJECT_TYPE_FOLDER) };
            return new HashSet<Integer>(Arrays.asList(supportedObjects));
        }

        public boolean isEnabled(OwObject object_p, OwObject parent_p, int context_p) throws Exception
        {
            return true;
        }

        public boolean getContextMenu()
        {
            return true;
        }

        public boolean getMultiselect()
        {
            return true;
        }

        public boolean getObjectInstance()
        {
            return true;
        }

        public String getPluginTitle()
        {
            return getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.remove", "Delete favorite");
        }

        public String getTooltip() throws Exception
        {
            return getPluginTitle();
        }

        public String getPluginID()
        {
            return m_pluginID;
        }
    }

    /**
     * Utility class for mapping OwObject instances in {@link Map}s
     * @since 2.5.3.0
     */
    protected class OwInstanceMapKey
    {
        private OwObject m_object;

        /**
         * 
         * @param object_p {@link OwObject} to be mapped
         */
        public OwInstanceMapKey(OwObject object_p)
        {
            super();
            this.m_object = object_p;
        }

        public final OwObject getObject()
        {
            return m_object;
        }

        public int hashCode()
        {
            try
            {
                return m_object != null ? m_object.getDMSID().hashCode() : 0;
            }
            catch (Exception e)
            {
                LOG.error("OwDeleteBookmarkFunction.hashCode(): could not create hash code.", e);
                return 0;
            }
        }

        public boolean equals(Object obj_p)
        {
            if (obj_p instanceof OwInstanceMapKey)
            {
                OwInstanceMapKey keyObject = (OwInstanceMapKey) obj_p;
                return this.m_object == keyObject.m_object;
            }
            else
            {
                return false;
            }
        }
    }

    /** used filter as defined with SHORTCUT_ITEM_FILTER_... */
    private int m_filter;

    /** the list view control used for displaying {@link OwObject} pointing shortcuts*/
    private OwObjectListView m_listView = null;

    /** maps {@link OwObject}s to shortcuts using {@link OwInstanceMapKey} keys*/
    private Map m_objectInstanceToShortcut = new HashMap();

    /** list {@link OwShortCutItem}s pointing to non-objects*/
    private List<OwShortCutItem> m_nonObjectShortcuts = new LinkedList<OwShortCutItem>();

    /** object list sort - used to sort object pointing shortcuts view objects */
    private OwSort m_objectListSort;

    /** instance of the mime manager used to open the objects */
    private OwMimeManager m_MimeManager = createOwMimeManager();

    /**
     * (overridable)
     * Factory method to create own OwMimeManager
     * @return {@link OwMimeManager}
     * @since 2.5.3.0
     */
    protected OwMimeManager createOwMimeManager()
    {
        return new OwMimeManager();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onActivate(int, java.lang.Object)
     */
    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        super.onActivate(index_p, reason_p);
        if (reason_p != null)
        {
            m_filter = ((Integer) reason_p).intValue();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#init()
     */
    protected void init() throws Exception
    {
        super.init();

        m_listView = createObjectListView();
        m_listView.setEventListner(this);
        m_listView.setExternalFormTarget(getFormTarget());
        m_objectListSort = getShortcutDocument().getColumnSortCriteria();
        applyConfiguration(m_listView);

        m_listView.attach(getContext(), null);
        enableFunctions(m_listView);
        m_listView.setExternalFormTarget(m_listView);

        // === init MIME manager as event target
        m_MimeManager.attach(getContext(), null);

        OwShortCutDocument document = getShortcutDocument();
        document.addEventListener(this);
    }

    public OwShortCutDocument getShortcutDocument()
    {
        return (OwShortCutDocument) getDocument();
    }

    /**
     * Applies a hard-coded/default configuration (egg. sort configuration , column configuration) 
     * to the give list view.<br>
     * @param listView_p
     * @throws Exception 
     */
    protected void applyConfiguration(OwObjectListView listView_p) throws Exception
    {
        listView_p.setViewMask(OwObjectListView.VIEW_MASK_USE_DOCUMENT_PLUGINS | OwObjectListView.VIEW_MASK_MULTI_SELECTION | OwObjectListView.VIEW_MASK_INSTANCE_PLUGINS);

        OwShortCutDocument document = (OwShortCutDocument) getDocument();

        Collection columninfo = document.getDefaultColumnInfo();

        if (m_objectListSort != null)
        {
            listView_p.setSort(m_objectListSort);
        }

        listView_p.setColumnInfo(columninfo);

        listView_p.setStickyFooterInUse(false);
        listView_p.attach(getContext(), null);

    }

    /**
     * 
     * @param object_p
     * @return the shortcut object that points to the given displayed object instance
     * @since 2.5.3.0
     */
    protected OwShortCutItem getShortcutForObject(OwObject object_p)
    {
        return (OwShortCutItem) m_objectInstanceToShortcut.get(new OwInstanceMapKey(object_p));
    }

    /**(overridable)
     * 
     * @return the list view control to be used when displaying {@link OwObject} pointing shortcuts
     * @throws Exception
     * @since 2.5.3.0
     */
    protected OwObjectListView createObjectListView() throws Exception
    {
        return new OwObjectListViewRow() {
            protected OwMimeManager createMimeManager()
            {
                return new OwMimeManager() {
                    public void insertTextLink(Writer w_p, String strDisplayName_p, OwObjectReference obj_p) throws Exception
                    {
                        OwObject object = obj_p.getInstance();
                        OwShortCutItem shortCutItem = getShortcutForObject(object);

                        if (shortCutItem != null)
                        {
                            shortCutItem.insertLabel(OwShortCutItemView.this, w_p);
                        }
                        else
                        {
                            super.insertTextLink(w_p, strDisplayName_p, obj_p);
                        }
                    }

                    public void insertIconLink(Writer w_p, OwObjectReference obj_p) throws Exception
                    {
                        OwObject object = obj_p.getInstance();
                        OwShortCutItem shortCutItem = getShortcutForObject(object);

                        if (shortCutItem != null)
                        {
                            shortCutItem.insertIcon(OwShortCutItemView.this, w_p);
                        }
                        else
                        {
                            super.insertIconLink(w_p, obj_p);
                        }
                    }
                };
            }
        };
    }

    /**
     * Enables the configured document functions in the given list
     * using the {@link OwShortCutDocument#getShortcutFunctionIDs()}. 
     * @param listView_p
     * @throws Exception 
     * @since 2.5.3.0
     */
    private void enableFunctions(OwObjectListView listView_p) throws Exception
    {
        List enabledDocumentFunctions = new LinkedList();

        OwShortCutDocument document = (OwShortCutDocument) getDocument();
        List bookmarkFunctionIDs = document.getShortcutFunctionIDs();

        if (bookmarkFunctionIDs != null)
        {

            for (Iterator idsIterator = bookmarkFunctionIDs.iterator(); idsIterator.hasNext();)
            {
                String id = (String) idsIterator.next();
                // only add to array if it is an allowed function
                if (((OwMainAppContext) getContext()).getConfiguration().isDocumentFunctionAllowed(id))
                {
                    OwFunction function = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(id);
                    enabledDocumentFunctions.add(function);
                }
            }
        }

        OwDeleteShortcutFunction m_delAttachmentFunction = new OwDeleteShortcutFunction();
        m_delAttachmentFunction.init(new OwStandardXMLUtil(), (OwMainAppContext) getContext());

        enabledDocumentFunctions.add(m_delAttachmentFunction);

        listView_p.setDocumentFunctionPluginList(enabledDocumentFunctions);
    }

    /** called when user clicks on paste from clipboard 
     * @throws OwShortCutException 
     * @throws Exception 
     */
    public void onRemove(HttpServletRequest request_p) throws Exception
    {
        String id = request_p.getParameter(SHORTCUT_ITEM_ID_KEY);

        id = OwAppContext.decodeURL(id);

        ((OwShortCutDocument) getDocument()).deleteShortCut(id);
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        getShortcutDocument().removeEventListener(this);

        super.detach();

        // detach the mimemanager as well
        m_MimeManager.detach();

        // detach the object list
        m_listView.detach();
    }

    /** get the URL to the favorites delete icon
     * @return String URL
     */
    public String getDeleteIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/plug/owdocshortcut/bookmark_delete.png";
    }

    /** get the URL to the unknown MIME icon
     * @return String URL
     */
    public String getUnkownMimeIcon() throws Exception
    {
        return getContext().getDesignURL() + "/micon/unknown.png";
    }

    protected void onRender(Writer w_p) throws Exception
    {
        // reset mimemanager
        m_MimeManager.reset();
        // load latest version

        refreshDisplayedShortcuts(getShortcutDocument());

        String deletetitle = getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.remove", "Delete bookmark");

        if (m_objectInstanceToShortcut.size() < 1 && m_nonObjectShortcuts.size() < 1)
        {
            w_p.write("<p class=\"OwEmptyTextMessage\">" + getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.noelements", "No favorites found") + "</p>");
        }
        else
        {
            if (m_objectInstanceToShortcut.size() > 0)
            {
                w_p.write("<div style='floaf:left;'>");
                m_listView.render(w_p);
                w_p.write("</div>");
            }

            if (m_nonObjectShortcuts.size() > 0)
            {
                w_p.write("<h3 class=\"OwInvalidText\">");
                w_p.write(getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.invalidfavorites", "Invalid Favorites"));
                w_p.write("</h3><table class=\"OwGeneralList_table\"><caption>");
                w_p.write(getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.tableCaption", "Table of favorites"));
                w_p.write("</caption><thead><tr class=\"OwGeneralList_header\"><td>&nbsp;</td><td>");
                w_p.write(getContext().localize("plugin.owshortcut.OwShortCutView.name", "Name"));
                w_p.write("</td><td>");
                //w_p.write(deletetitle);
                w_p.write("&nbsp;");
                w_p.write("</td><td width='80%'>&nbsp;</td></tr></thead>");

                // render items
                for (int i = 0; i < m_nonObjectShortcuts.size(); i++)
                {
                    OwShortCutItem item = m_nonObjectShortcuts.get(i);

                    if (isItemFiltered(item))
                    {
                        continue;
                    }
                    w_p.write("<tr class=\"OwGeneralList_");
                    w_p.write(((i % 2) == 0) ? "RowEven" : "RowOdd");
                    w_p.write("\"><td>");
                    try
                    {
                        item.insertIcon(this, w_p);
                    }
                    catch (OwException e)
                    {
                        w_p.write("<a title=\"");
                        String tooltip = getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.invaliddocument", "Invalid document");
                        w_p.write(tooltip);
                        w_p.write("\"><img src=\"");
                        w_p.write(getUnkownMimeIcon());
                        w_p.write("\" border=\"0\"");
                        w_p.write(" alt=\"");
                        w_p.write(tooltip);
                        w_p.write("\" title=\"");
                        w_p.write(tooltip);
                        w_p.write("\"/></a>");
                    }
                    w_p.write("</td>");
                    w_p.write("<td nowrap>");
                    try
                    {
                        item.insertLabel(this, w_p);
                    }
                    catch (OwException e)
                    {
                        w_p.write(getContext().localize("plugin.com.wewebu.ow.server.plug.owshortcut.alreadydeleted", "The document has already been deleted from the system."));
                    }
                    w_p.write("</td>");
                    w_p.write("<td nowrap>&nbsp;<a title=\"");
                    w_p.write(deletetitle);
                    w_p.write("\" href=\"");

                    String id = OwAppContext.encodeURL(item.getId());

                    StringBuffer buffer = new StringBuffer();
                    buffer.append(SHORTCUT_ITEM_ID_KEY).append("=").append(id);
                    w_p.write(this.getEventURL("Remove", buffer.toString()));

                    w_p.write("\"><img alt=\"");
                    w_p.write(deletetitle);
                    w_p.write("\" title=\"");
                    w_p.write(deletetitle);
                    w_p.write("\" src=\"");
                    w_p.write(getDeleteIcon());
                    w_p.write("\"></a>");
                    w_p.write("</td><td>&nbsp;</td>");
                    w_p.write("</tr>");
                }
                w_p.write("</table>");
            }
        }

    }

    /**
     * Fills the given data structures with shortcut information.
     * Basically the shortcuts are divided into {@link OwObject}-pointing-shortcuts and 
     * NON-{@link OwObject}-pointing-shortcuts.
     * @param objectShortcuts_p map of {@link OwInstanceMapKey} keys to their inner {@link OwObject} shortcut correspondent
     * @param nonObjectShortcuts_p List of NON {@link OwObject} pointing {@link OwShortCutItem}s 
     * @throws Exception
     * @since 2.5.3.0
     */
    protected void createShortcutsStructures(OwShortCutDocument document_p, Map objectShortcuts_p, List nonObjectShortcuts_p) throws Exception
    {
        objectShortcuts_p.clear();
        nonObjectShortcuts_p.clear();

        List shortcuts = document_p.getShortCutItems();
        for (Iterator it = shortcuts.iterator(); it.hasNext();)
        {
            OwShortCutItem item = (OwShortCutItem) it.next();
            if (!isItemFiltered(item))
            {
                if (item instanceof OwShortCutItemOwObject)
                {
                    OwObjectReference objectReference = ((OwShortCutItemOwObject) item).getObjRef();
                    if (objectReference != null)
                    {
                        OwNetwork network = ((OwMainAppContext) getContext()).getNetwork();
                        String referenceDMSID = "<invalidDMSID>";
                        try
                        {
                            item.refresh(network);
                            OwObject object = ((OwShortCutItemOwObject) item).getObjRef().getInstance();
                            objectShortcuts_p.put(new OwInstanceMapKey(object), item);
                        }
                        catch (OwObjectNotFoundException e)
                        {
                            LOG.debug("OwShortCutItemView.createShortcutsStructures(): shortcut object with DMSID " + referenceDMSID + " not found!", e);
                            OwStandardUnresolvedReference unresolvedReference = new OwStandardUnresolvedReference(e, "Could not display shortcut!", referenceDMSID, item.getName(), "", item.getType());
                            OwShortcutItemUnresolved urItem = new OwShortcutItemUnresolved(item.getId(), item.getName(), unresolvedReference);
                            if (!isItemFiltered(urItem))
                            {
                                nonObjectShortcuts_p.add(urItem);
                            }
                        }
                        catch (Exception e)
                        {
                            LOG.debug("OwShortCutItemView.createShortcutsStructures(): shortcut object could not be retrieved for an unknown reason! The object is considered deleted!", e);
                            OwStandardUnresolvedReference unresolvedReference = new OwStandardUnresolvedReference(e, "Could not display shortcut!", referenceDMSID, item.getName(), "", item.getType());
                            OwShortcutItemUnresolved urItem = new OwShortcutItemUnresolved(item.getId(), item.getName(), unresolvedReference);
                            if (!isItemFiltered(urItem))
                            {
                                nonObjectShortcuts_p.add(urItem);
                            }
                        }
                    }
                    else
                    {
                        LOG.debug("OwShortCutItemView.createShortcutsStructures(): legacy shortcut detected : " + item.toString());
                        nonObjectShortcuts_p.add(item);
                    }
                }
                else
                {
                    nonObjectShortcuts_p.add(item);
                }
            }
        }
    }

    /**
     * 
     * @param item_p
     * @return <code>true</code> if the given item shouldn't be displayed, <code>false</code> otherwise 
     */
    protected boolean isItemFiltered(OwShortCutItem item_p)
    {
        int type = 0;

        if (item_p instanceof OwShortCutItemOwObject)
        {
            OwObjectReference obj = ((OwShortCutItemOwObject) item_p).getObjRef();
            if (obj != null)
            {
                type = obj.getType();
            }
        }
        else if (item_p instanceof OwShortcutItemUnresolved)
        {
            OwShortcutItemUnresolved obj = (OwShortcutItemUnresolved) item_p;
            if (obj != null)
            {
                type = obj.getType();
            }
        }

        switch (m_filter)
        {
            case SHORTCUT_ITEM_FILTER_REST:
                return OwStandardObjectClass.isContentType(type) || OwStandardObjectClass.isContainerType(type);

            case SHORTCUT_ITEM_FILTER_DOCUMENTS:
                return !OwStandardObjectClass.isContentType(type);

            case SHORTCUT_ITEM_FILTER_FOLDERS:
                return !OwStandardObjectClass.isContainerType(type);

            default:
            case SHORTCUT_ITEM_FILTER_ALL:
                return false;
        }

    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owshortcut.OwShortCutItemContext#getMimeManager()
     */
    public OwMimeManager getMimeManager()
    {
        return m_MimeManager;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner#onObjectListViewFilterChange(com.wewebu.ow.server.field.OwSearchNode, com.wewebu.ow.server.ecm.OwObject)
     */
    public void onObjectListViewFilterChange(OwSearchNode filterNode_p, OwObject parent_p) throws Exception
    {
        //void
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner#onObjectListViewGetRowClassName(int, com.wewebu.ow.server.ecm.OwObject)
     */
    public String onObjectListViewGetRowClassName(int index_p, OwObject obj_p)
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner#onObjectListViewItemClick(com.wewebu.ow.server.ecm.OwObject)
     */
    public boolean onObjectListViewItemClick(OwObject obj_p) throws Exception
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner#onObjectListViewSelect(com.wewebu.ow.server.ecm.OwObject, com.wewebu.ow.server.ecm.OwObject)
     */
    public void onObjectListViewSelect(OwObject object_p, OwObject parent_p) throws Exception
    {
        //void
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwObjectListViewEventListner#onObjectListViewSort(com.wewebu.ow.server.field.OwSort, java.lang.String)
     */
    public void onObjectListViewSort(OwSort newSort_p, String strSortProperty_p) throws Exception
    {
        this.m_objectListSort = newSort_p;
    }

    /**
     * Recreates all internal view state considering the given document.
     * {@link #m_objectInstanceToShortcut}, {@link #m_nonObjectShortcuts} and the contents of
     * the {@link #m_listView} should be refreshed with correct shrtcut info.
     * @param document_p
     * @throws Exception
     */
    protected void refreshDisplayedShortcuts(OwShortCutDocument document_p) throws Exception
    {
        createShortcutsStructures(document_p, m_objectInstanceToShortcut, m_nonObjectShortcuts);
        OwObjectCollection visibleObjects = new OwStandardObjectCollection();

        Set objectInstanceKeySet = m_objectInstanceToShortcut.keySet();
        for (Iterator i = objectInstanceKeySet.iterator(); i.hasNext();)
        {
            OwInstanceMapKey instanceMapKey = (OwInstanceMapKey) i.next();
            OwObject object = instanceMapKey.getObject();
            visibleObjects.add(object);
        }
        if (m_objectListSort != null)
        {
            visibleObjects.sort(m_objectListSort);
        }
        m_listView.setObjectList(visibleObjects, null);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.owshortcut.OwShortcutDocumentEventListner#onShortcutDocumentChaged(com.wewebu.ow.server.plug.owshortcut.OwShortCutDocument)
     */
    public void onShortcutDocumentChaged(OwShortCutDocument document_p)
    {
        try
        {
            if (this == getContext().getHttpRequest().getAttribute(OwView.CURRENT_MODULE_KEY))
            {
                refreshDisplayedShortcuts(document_p);
            }
        }
        catch (Exception e)
        {
            LOG.error("OwShortCutItemView.onShortcutDocumentChaged(): could not create shortcut structures on document update!", e);
        }
    }
}