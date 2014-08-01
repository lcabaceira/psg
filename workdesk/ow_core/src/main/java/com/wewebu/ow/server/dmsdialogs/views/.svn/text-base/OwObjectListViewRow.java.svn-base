package com.wewebu.ow.server.dmsdialogs.views;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwMultipleSelectionCall;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwUserOperationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwScriptTable;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Object list view. Displays the results of searches. <br/><br/>
 * Use setObjectList and setColumnInfo to set the objects and columns to display.
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
public class OwObjectListViewRow extends OwObjectListViewPluginCache implements OwFieldProvider
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectListViewRow.class);

    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;

    /** current zero based page number in multiples of getPageSize() for paging the results 
     * @deprecated since 4.2.0.0 use {@link #getCurrentPage()}*/
    protected int m_iCurrentPage = 0;

    /** the list with the sort criteria 
     * @deprecated since 4.2.0.0 use set-/{@link #getSort()} instead*/
    protected OwSort m_Sort;

    /** flag indicating if context menu should be shown */
    protected boolean m_useContextMenu;

    /** instance of the MIME manager used to open the objects */
    OwMimeManager m_MimeManager;

    /** a list of column info, which describe the columns of the object list 
     * @deprecated since 4.2.0.0 use set-/{@link #getColumnInfo()} instead*/
    protected Collection m_ColumnInfoList;

    /** instance of the property field class 
     * @deprecated since 4.2.0.0 use set-/{@link #getFieldManager()} instead*/
    protected OwFieldManager m_theFieldManager;

    /**event listener for the function plugin refresh events 
     * @deprecated since 4.2.0.0 use set-/{@link #getRefreshContext()} instead*/
    protected OwClientRefreshContext m_RefreshContext;

    /** object types found during rendering */
    protected Set<Integer> m_occuredObjectTypes;

    /** 
     * Construct a object list view. By default, try to use sticky footer.
     * 
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public OwObjectListViewRow(int iViewMask_p)
    {
        super(iViewMask_p);
        m_MimeManager = createMimeManager();
    }

    /**
     *  Construct a object list view.By default, try to use sticky footer.
     */
    public OwObjectListViewRow()
    {
        super();
        m_MimeManager = createMimeManager();
    }

    /**(overridable)
     * Factory method to create the MimeManager for this instance.
     * @return OwMimeManager
     */
    protected OwMimeManager createMimeManager()
    {
        return new OwMimeManager();
    }

    /**
     * Set the context to be used for MIME type resolution
     * 
     * @param context_p the context to be used for MIME type resolution, can be <code>null</code>
     * 
     * @since 2.5.3.0
     */
    public void setMimeTypeContext(String context_p)
    {
        m_MimeManager.setMimeTypeContext(context_p);
    }

    /** method to retrieve HTML alignment attributes out of OwFieldColumnInfo alignment 
     * 
     * @param iAlignment_p int alignment as defined in OwFieldColumnInfo
     *
     * @return String corresponding HTML alignment attribute
     */
    protected String getHtmlAlignment(int iAlignment_p)
    {
        return m_HtmlAlignments.m_list[iAlignment_p];
    }

    /** set the list of objects to be displayed by this list view
     * @param objectList_p OwObjectCollection
     * @param parentObject_p OwObject parent which created the object list, can be null if no parent is specified
     */
    public void setObjectList(OwObjectCollection objectList_p, OwObject parentObject_p) throws Exception
    {
        super.setObjectList(objectList_p, parentObject_p);

        m_MimeManager.setParent(parentObject_p);

        // clear persistence
        resetPersistedSelectionState();
    }

    @Override
    public void setObjectIterable(OwIterable<OwObject> iterable, OwObject parentObject_p) throws Exception
    {
        super.setObjectIterable(iterable, parentObject_p);

        m_MimeManager.setParent(parentObject_p);

        // clear persistence
        resetPersistedSelectionState();
    }

    /** to get additional form attributes used for the form
     *  override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        // render form only when multi selection is active
        return "";
    }

    /** check if paging is enabled
     * @return true = enabled, display page buttons, false = disabled
     */
    protected boolean isPagingEnabled()
    {
        OwView parent = getParent();
        return (parent == null) || (!parent.isShowMaximized());
    }

    public void setRefreshContext(OwClientRefreshContext eventlister_p)
    {
        super.setRefreshContext(eventlister_p);
        m_MimeManager.setRefreshContext(eventlister_p);
    }

    /** init the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // get and cast application context
        m_MainContext = (OwMainAppContext) getContext();

        // get preloaded plugins reference
        if (hasViewMask(OwObjectListView.VIEW_MASK_USE_DOCUMENT_PLUGINS))
        {
            // === determine the behavior 
            // context menu
            m_useContextMenu = (m_MainContext.getConfiguration().isDocmentFunctionRequirement(OwConfiguration.PLUGINS_REQUIRE_CONTEXT_MENU) && (hasViewMask(VIEW_MASK_MULTI_SELECTION) || hasViewMask(VIEW_MASK_SINGLE_SELECTION)));
            // so do we have a context menu ?
            if (!m_useContextMenu)
            {
                // no multiple selection needed without context menu plugins, so clear selection flags
                clearViewMask(OwObjectListView.VIEW_MASK_MULTI_SELECTION);
                clearViewMask(OwObjectListView.VIEW_MASK_SINGLE_SELECTION);
            }
        }
        else
        {
            // === determine the behavior 
            // no context menu needed without plugins
            m_useContextMenu = false;

            // no multiple selection needed without plugins, so clear selection flags
            clearViewMask(OwObjectListView.VIEW_MASK_MULTI_SELECTION);
            clearViewMask(OwObjectListView.VIEW_MASK_SINGLE_SELECTION);
        }

        setSort(new OwSort(m_MainContext.getMaxSortCriteriaCount(), true));

        // === get reference to the property field manager instance
        setFieldManager(m_MainContext.createFieldManager());
        getFieldManager().setExternalFormTarget(getFormTarget());
        getFieldManager().setFieldProvider(this);

        // === init MIME manager as event target
        m_MimeManager.attach(m_MainContext, null);
        // === init page selector
        m_pageSelectorComponent = createPageSelector();
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // detach the field manager and MIME manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();
    }

    /**
     * 
     * @param strProperty_p
     * @return String sort order type   Ascending | Descending
     * @throws Exception
     */
    protected String getSortOrderType(String strProperty_p) throws Exception
    {
        String sortOrderType = getContext().localize("app.OwObjectListView.sorting.ascending", "ascending");
        String ascending = getContext().localize("app.OwObjectListView.sorting.ascending", "ascending");
        String descending = getContext().localize("app.OwObjectListView.sorting.descending", "descending");
        if (null == getSort())
        {
            return sortOrderType;
        }

        OwSort.OwSortCriteria SortCriteria = getSort().getCriteria(strProperty_p);
        if (SortCriteria != null)
        {
            if (SortCriteria.getAscFlag())
            {
                return descending;
            }
            else
            {
                return ascending;
            }
        }

        return sortOrderType;
    }

    /** gets the path to the sort order image, which designates the current sort order of the specified column
     * @param strProperty_p column
     * @return image path
     */
    protected String getSortOrderImage(String strProperty_p) throws Exception
    {
        if (null == getSort())
        {
            return m_MainContext.getDesignURL() + "/images/OwObjectListView/nosort.png";
        }

        OwSort.OwSortCriteria SortCriteria = getSort().getCriteria(strProperty_p);
        if (SortCriteria != null)
        {
            if (SortCriteria.getAscFlag())
            {
                return m_MainContext.getDesignURL() + "/images/OwObjectListView/sortasc.png";
            }
            else
            {
                return m_MainContext.getDesignURL() + "/images/OwObjectListView/sortdesc.png";
            }
        }
        else
        {
            return m_MainContext.getDesignURL() + "/images/OwObjectListView/nosort.png";
        }
    }

    /** overridable to render additional columns */
    protected void renderExtraColumnHeader(java.io.Writer w_p) throws Exception
    {
        // === optional select buttons
        if (hasViewMask(VIEW_MASK_USE_SELECT_BUTTON))
        {
            renderVersionHeader(w_p);
        }

        renderVersionHeader(w_p);

    }

    /** overridable to render additional columns */
    protected void renderExtraColumnRows(java.io.Writer w_p, OwObject obj_p, int iIndex_p) throws Exception
    {
        // === optional select buttons
        if (hasViewMask(OwObjectListView.VIEW_MASK_USE_SELECT_BUTTON))
        {
            w_p.write("\n<td align=\"center\" width=\"1\"><a title=\"");
            String selectButtonTooltip = getContext().localize("app.OwObjectListView.selecttooltip", "Select");
            selectButtonTooltip = OwHTMLHelper.encodeToSecureHTML(selectButtonTooltip);
            w_p.write(selectButtonTooltip);
            w_p.write("\" href=\"");
            w_p.write(getEventURL("Select", OwObjectListView.OBJECT_INDEX_KEY));//write object index
            w_p.write("=");
            w_p.write(String.valueOf(iIndex_p));//parse int to String
            w_p.write("\">");

            w_p.write("<span class=\"OwExtraColumnRows_icon\" style=\"background-image: url(");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/OwObjectListView/select.png)\" title=\"");

            boolean identifierCondition = obj_p == null || obj_p.getName() == null || obj_p.getName().isEmpty();
            String itemStr = getContext().localize("app.OwObjectListView.item", "Item");
            String identifier = ((identifierCondition) ? itemStr + " " + Integer.toString(iIndex_p + 1) : obj_p.getName());
            w_p.write(selectButtonTooltip);
            w_p.write("\">");
            w_p.write("</span>");

            w_p.write("<span class=\"OwExtraColumnRows_text\">");
            w_p.write(selectButtonTooltip + " " + identifier);
            w_p.write("</span>");
            w_p.write("</a></td>\n");
        }
    }

    /**
     * Render lock column.
     * Check if the given Object is lock-status and render it depending on the status. 
     * @param w_p Writer to be used for rendering
     * @param obj_p OwObject which should be checked
     * @throws Exception If {@link OwObject#getLock(int)} fail
     * @throws IOException if can not use writer
     * @since 2.5.2.0
     */
    protected void renderLockedColumn(java.io.Writer w_p, OwObject obj_p) throws Exception, IOException
    {
        // === render lock status column for each object
        if (obj_p.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
        {
            String lockedTooltip = getContext().localize("image.locked", "Locked item");
            w_p.write("\n<td align=\"center\" width=\"1\"><img src=\"");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/plug/owbpm/locked.png\" ");
            w_p.write("alt=\"");
            w_p.write(lockedTooltip);
            w_p.write("\" title=\"");
            w_p.write(lockedTooltip);
            w_p.write("\" /></td>\n");
        }
        else
        {
            w_p.write("\n<td width=\"1\"></td>\n");
        }
    }

    /**
     * 
     */
    public void renderTableCaption(java.io.Writer w_p) throws Exception
    {
        w_p.write(getContext().localize("app.OwObjectListView.table.caption", "Result list"));
    }

    /** render the header portion of the object list
     *
     * @param w_p Writer object to write HTML to
     * @param instancePluginsList_p Collection of plugins that are displayed for each instance
     */
    public void renderSortHeader(java.io.Writer w_p, Collection<OwPluginEntry> instancePluginsList_p) throws Exception
    {
        if (hasViewMask(OwObjectListView.VIEW_MASK_MULTI_SELECTION))
        {
            OwScriptTable.writeSelectableListHeaderStart(String.valueOf(getListViewID()), w_p, "OwGeneralList_header", getContext());
        }
        else
        {
            w_p.write("\n<tr class=\"OwGeneralList_header\">\n");
        }

        // === Index
        renderIndexHeader(w_p);

        // === overridable to render additional columns
        renderExtraColumnHeader(w_p);

        // === create space for the document function plugins
        insertDocumentFunctionPluginHeader(w_p, instancePluginsList_p);

        // === Version 
        renderVersionHeader(w_p);

        // === MIME icon
        renderMimeTypeHeader(w_p);

        // ===  all other properties
        renderPropertiesHeader(w_p);

        w_p.write("\n</tr>\n");
    }

    /**
     * @param w_p
     * @throws IOException
     * @throws Exception
     * @since 4.0.0.0
     */
    protected void renderPropertiesHeader(java.io.Writer w_p) throws IOException, Exception
    {
        int iWidth = 90 / (getColumnInfo().size() > 0 ? getColumnInfo().size() : 1);
        String strWidth = String.valueOf(iWidth) + "%";

        Iterator<? extends OwFieldColumnInfo> columnInfoIterator = getColumnInfo().iterator();
        while (columnInfoIterator.hasNext())
        {
            // get the column info for this column
            OwFieldColumnInfo colInfo = columnInfoIterator.next();

            w_p.write("\n<th class=\"OwGeneralList_property\" ");
            w_p.write(getHtmlAlignment(colInfo.getAlignment()));
            w_p.write(" width=\"");
            w_p.write(strWidth);
            w_p.write("\">");

            renderPropertyColumnHeader(w_p, colInfo);

            w_p.write("</th>\n");
        }
    }

    /**
     * @param w_p
     * @throws IOException
     * @since 4.0.0.0
     */
    protected void renderVersionHeader(java.io.Writer w_p) throws IOException
    {
        w_p.write("\n<th width=\"1\">&nbsp;</th>\n");
    }

    /**
     * @param w_p
     * @throws IOException
     * @since 4.0.0.0
     */
    protected void renderMimeTypeHeader(java.io.Writer w_p) throws IOException
    {
        w_p.write("\n<th width=\"1\">&nbsp;</th>\n");
    }

    /**
     * @param w_p
     * @param instancePluginsList_p
     * @throws IOException
     * @throws Exception
     * @since 4.0.0.0
     */
    protected void insertDocumentFunctionPluginHeader(java.io.Writer w_p, Collection<OwPluginEntry> instancePluginsList_p) throws IOException, Exception
    {
        // iterate over preinstantiated plugins and find out, if plugin has own column set
        Iterator<OwPluginEntry> instPlugIt = instancePluginsList_p.iterator();
        while (instPlugIt.hasNext())
        {
            OwPluginEntry entry = instPlugIt.next();
            if (!getPluginCache().isPluginDisabledForAllObjects(entry))
            {
                w_p.write("\n<th width=\"1\">");

                if (entry.m_Plugin.getEnableColumnClickEvent())
                {
                    // event requested
                    w_p.write("<a title=\"");
                    w_p.write(entry.m_Plugin.getTooltip());
                    w_p.write("\" class=\"OwGeneralList_sort\" href=\"");
                    w_p.write(getEventURL("ColumnClickEvent", PLUG_INDEX_KEY + "=" + String.valueOf(entry.m_iIndex)));
                    w_p.write("\">");

                    w_p.write(entry.m_Plugin.getColumnTitle());

                    w_p.write("</a>");
                }
                else
                {
                    // no event so just write the column string
                    w_p.write(entry.m_Plugin.getColumnTitle());
                }

                w_p.write("</th>");
            }
        }
    }

    /**
     * @param w_p
     * @throws IOException
     * @throws Exception
     * @since 4.0.0.0
     */
    protected void renderIndexHeader(java.io.Writer w_p) throws IOException, Exception
    {
        w_p.write("\n<th id=\"a1\" class=\"align-right\" width=\"1\">");
        int count = getCount();
        if (count >= 0)
        {
            if (!isCollectionComplete())
            {
                w_p.write("&gt;&nbsp;");
            }
            w_p.write(String.valueOf(count));
            w_p.write("&nbsp;");
        }
        w_p.write("</th>\n");
    }

    /** (overridable) render the property sort column
     *  @param w_p java.io.Writer
     *  @param colInfo_p OwFieldColumnInfo
     * */
    protected void renderPropertyColumnHeader(java.io.Writer w_p, OwFieldColumnInfo colInfo_p) throws Exception
    {
        if (colInfo_p.isSortable())
        {
            // Sort by name ascending
            w_p.write("<a href=\"");
            w_p.write(getEventURL("Sort", SORT_PROPERTY_KEY + "=" + colInfo_p.getPropertyName()));
            w_p.write("\" title=\"");
            String text = OwString.localize2(getContext().getLocale(), "app.OwObjectListView.sortTooltip", "Sort %1 %2", colInfo_p.getDisplayName(getContext().getLocale()), getSortOrderType(colInfo_p.getPropertyName()));
            w_p.write(text);
            w_p.write("\" class=\"OwGeneralList_sort\">");
            w_p.write(colInfo_p.getDisplayName(getContext().getLocale()));
            // display order arrows
            w_p.write("&nbsp;<img class=\"OwGeneralList_sortimg\" alt=\"");
            w_p.write(text);
            w_p.write("\" title=\"");
            w_p.write(text);
            w_p.write("\" style='border:0px none;' src=\"");
            w_p.write(getSortOrderImage(colInfo_p.getPropertyName()));
            w_p.write("\">");
            w_p.write("</a>");
        }
        else
        {
            String tooltip = getContext().localize("app.OwObjectListView.notsortable", "Not sortable");
            w_p.write("<div  class=\"OwGeneralList_sort\" alt=\"");
            w_p.write(tooltip);
            w_p.write("\" title=\"");
            w_p.write(tooltip);
            w_p.write("\">");
            w_p.write(colInfo_p.getDisplayName(getContext().getLocale()));
            w_p.write("&nbsp;<img class=\"OwGeneralList_sortimg\"  src=\"");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/OwObjectListView/nosortable.png\" ");
            w_p.write("alt=\"");
            w_p.write(tooltip);
            w_p.write("\" title=\"");
            w_p.write(tooltip);
            w_p.write("\"/></div>");
        }
    }

    /** overridable get the style class name for the row
     *
     * @param iIndex_p int row index
     * @param obj_p current OwObject
     *
     * @return String with style class name
     */
    protected String getRowClassName(int iIndex_p, OwObject obj_p)
    {
        try
        {
            // callback
            return getEventListner().onObjectListViewGetRowClassName(iIndex_p, obj_p).toString();
        }
        catch (Exception e)
        {
            // use default
            return ((iIndex_p % 2) != 0) ? "OwGeneralList_RowEven" : "OwGeneralList_RowOdd";
        }
    }

    /** overridable get the style class name for the row
    *
    * @param iIndex_p int row index
    * @param obj_p current OwObject
    *
    * @return String with style class name
    */
    protected String getSelectedRowClassName(int iIndex_p, OwObject obj_p)
    {
        return "OwGeneralList_RowSelected";
    }

    /**
     * Compute the start index, the index for the first object displayed in the list, when paging is used.
     * @return the start index.
     * @since 3.0.0.0
     * @deprecated since 4.2.0.0 use {@link #getDisplayedPage()} for rendering
     */
    @Deprecated
    protected int computeStartIndex()
    {
        int iStartIndex = 0;

        if (isPagingEnabled())
        {
            int iPageSize = ((OwMainAppContext) getContext()).getPageSizeForLists();
            iStartIndex = m_iCurrentPage * iPageSize;

            //            if (m_ObjectList != null && iStartIndex > m_ObjectList.size())
            {
                m_iCurrentPage = 0;
                iStartIndex = 0;
            }
        }
        return iStartIndex;
    }

    /**
     * Compute the end index, the index for the last displayed object, when paging is used.
     * @return - the end index
     * @since 3.0.0.0
     * @deprecated since 4.2.0.0 use {@link #getDisplayedPage()} for rendering
     */
    @Deprecated
    protected int computeEndIndex()
    {
        int iEndIndex = 0;
        //        if (m_ObjectList != null)
        {
            if (isPagingEnabled())
            {
                int iPageSize = ((OwMainAppContext) getContext()).getPageSizeForLists();
                int iStartIndex = computeStartIndex();

                iEndIndex = iStartIndex + iPageSize;
            }
        }
        return iEndIndex;
    }

    /** render the header portion of the object list
     * @param w_p Writer object to write HTML to
     * @param instancePluginsList_p Collection of plugins that are displayed for each instance
     *
     */
    public void renderRows(java.io.Writer w_p, Collection<OwPluginEntry> instancePluginsList_p) throws Exception
    {
        BufferedWriter bwr = new BufferedWriter(w_p, 30000);

        // collect the object types of the rendered objects, used to preselect plugins for the multi select menu
        m_occuredObjectTypes = new HashSet<Integer>();

        // === iterate over result objects
        int i = getCurrentPage() * getPageSize();
        for (OwObject obj : getDisplayedPage())
        {
            StringWriter rowBuffer = new StringWriter(30000);
            try
            {
                // store the object type
                m_occuredObjectTypes.add(Integer.valueOf(obj.getType()));

                // compute odd even flag to toggle row classes
                String strRowClass = getRowClassName(i, obj);
                String strSelectedRowClass = getSelectedRowClassName(i, obj);
                // === create list row
                if (hasViewMask(OwObjectListView.VIEW_MASK_MULTI_SELECTION))
                {
                    boolean selectedObj = isObjectSelectionPersisted(i);
                    OwScriptTable.writeSelectableListRowStart(getContext().getLocale(), String.valueOf(getListViewID()), rowBuffer, i, strRowClass, strSelectedRowClass, true, selectedObj);
                }
                else
                {
                    rowBuffer.write("\n<tr class=\"");
                    rowBuffer.write(strRowClass);
                    rowBuffer.write("\">\n");
                }

                // === overridable to render additional columns
                renderExtraColumnRows(rowBuffer, obj, i);

                // === Index
                renderIndex(rowBuffer, i);

                //render lock
                renderLockedColumn(rowBuffer, obj);

                // === Document Plugins
                insertDocumentFunctionPluginRows(rowBuffer, obj, i, instancePluginsList_p);

                // === Version
                renderVersion(rowBuffer, obj);

                // === MIME Icon
                renderMimeType(rowBuffer, obj);

                // === other properties
                renderProperties(rowBuffer, obj);
                bwr.write(rowBuffer.getBuffer().toString());
                i++;
            }
            catch (Exception e)
            {
                LOG.error("Could not render row !", e);
            }
        }

        bwr.flush();
    }

    /**
     * @param rowBuffer
     * @param obj
     * @throws Exception
     * @since 4.0.0.0
     */
    protected void renderProperties(StringWriter rowBuffer, OwObject obj) throws Exception
    {
        // iterate over the columns / properties
        Iterator<? extends OwFieldColumnInfo> columnInfoIterator = getColumnInfo().iterator();
        while (columnInfoIterator.hasNext())
        {
            // get the column info for this column
            OwFieldColumnInfo colInfo = columnInfoIterator.next();

            // get property from object
            try
            {
                OwProperty Prop = obj.getProperty(colInfo.getPropertyName());

                // === Property found
                OwPropertyClass PropClass = Prop.getPropertyClass();
                rowBuffer.write("\n<td class=\"OwGeneralList_property\" ");
                rowBuffer.write(getHtmlAlignment(colInfo.getAlignment()));
                if (PropClass.isNameProperty())
                {
                    rowBuffer.write(" id=\"MIME_TYPE_LINK\" onMouseDown=\"onMimeMouseDown(event);\" onKeyDown=\"return onMimeKeyDown(event);\"");
                }
                rowBuffer.write(">");

                if (PropClass.isNameProperty())
                {
                    // Name properties are inserted with a download / view link
                    try
                    {
                        m_MimeManager.insertTextLink(rowBuffer, Prop.getValue().toString(), obj);
                    }
                    catch (NullPointerException e)
                    {
                        m_MimeManager.insertTextLink(rowBuffer, null, obj);
                    }
                }
                else
                {
                    // normal properties are inserted via Fieldmanager
                    getFieldManager().insertReadOnlyField(rowBuffer, Prop);
                }
                rowBuffer.write("</td>\n");
            }
            catch (OwObjectNotFoundException e)
            {
                // === Property not found
                rowBuffer.write("\n<td>&nbsp;</td>\n");
            }
        }

        rowBuffer.write("\n</tr>\n");
    }

    /**
     * @param rowBuffer
     * @param obj 
     * @throws Exception 
     * @since 4.0.0.0
     */
    protected void renderMimeType(StringWriter rowBuffer, OwObject obj) throws Exception
    {
        rowBuffer.write("\n<td align=\"center\" width=\"1\" id=\"MIME_TYPE_ICON\" onMouseDown=\"onMimeMouseDown(event);\" onKeyDown=\"return onMimeKeyDown(event);\">");
        m_MimeManager.insertIconLink(rowBuffer, obj);
        rowBuffer.write("</td>\n");
    }

    /**
     * @param rowBuffer output buffer
     * @param obj
     * @throws Exception
     * @since 4.0.0.0
     */
    protected void renderVersion(StringWriter rowBuffer, OwObject obj) throws Exception
    {
        rowBuffer.write("\n<td nowrap align=\"right\">&nbsp;");

        if (obj.hasVersionSeries())
        {
            renderCheckedOut(rowBuffer, obj);
            rowBuffer.write("(");
            rowBuffer.write(obj.getVersion().getVersionInfo());
            rowBuffer.write(")");
        }
        else
        {
            rowBuffer.write("&nbsp;");
        }

        rowBuffer.write("&nbsp;</td>\n");
    }

    /**
     * @param rowBuffer
     * @param obj
     * @throws Exception
     * @since 4.0.0.0
     */
    protected void renderCheckedOut(StringWriter rowBuffer, OwObject obj) throws Exception
    {
        if (obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
        {
            // === checked out symbol
            rowBuffer.write("<img style=\"vertical-align:top;\" src=\"");
            rowBuffer.write(getContext().getDesignURL());
            rowBuffer.write("/images/checkedout.png\"");
            String tooltip = getContext().localize("app.Checked_out", "Checked out");
            rowBuffer.write(" alt=\"");
            rowBuffer.write(tooltip);
            rowBuffer.write("\" title=\"");
            rowBuffer.write(tooltip);
            rowBuffer.write("\" />");
        }
        else
        {
            // === not checked out symbol
            rowBuffer.write("<img style=\"vertical-align:top;\" alt=\"\" title=\"\" src=\"");
            rowBuffer.write(getContext().getDesignURL());
            rowBuffer.write("/images/notcheckedout.png\" />");
        }
    }

    /**
     * @param rowBuffer output buffer
     * @param i row index 
     * @since 4.0.0.0
     */
    protected void renderIndex(StringWriter rowBuffer, int i)
    {
        rowBuffer.write("\n<td class=\"align-right\" width=\"1\">&nbsp;");
        rowBuffer.write(String.valueOf(i + 1));
        rowBuffer.write("&nbsp;</td>\n");
    }

    /** same as getIsListValid() && ( getCount() != 0 )
     * 
     * @return boolean true = valid list with at least one item to display
     */
    public boolean getIsList()
    {
        return getIsListValid() && (getCount() != 0);
    }

    /** check if list is configured well and can be displayed
     * 
     * @return boolean true = list is valid, false = no valid list, don't render anything yet
     */
    public boolean getIsListValid()
    {
        return ((getObjectList() != null || getObjectIterable() != null) && (getColumnInfo() != null));
    }

    /** check if list contains any items
     * 
     * @return boolean true = list contains at least one item
     * @deprecated since 4.2.0.0 use {@link #getCount()} instead
     */
    @Deprecated
    public boolean getIsListEmpty()
    {
        return (getCount() == 0);
    }

    /** check if page buttons should be rendered
     * 
     * @return true = render page buttons, false = no page buttons to render
     * @deprecated since 4.2.0.0 use {@link #hasPaging()} instead
     */
    @Deprecated
    public boolean getIsPaging()
    {
        return hasPaging();
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        // always reset MIME manager before inserting links !!!
        m_MimeManager.reset();
        getFieldManager().reset();

        setPluginCache(createPluginStatusCacheUtility(getPluginEntries()));
        renderMainRegion(w_p);

        if (getIsList())
        {
            // === write context menu section
            if (m_useContextMenu)
            {
                // render single select context menu
                renderContextMenu(w_p, m_occuredObjectTypes);
            }

            if (hasViewMask(OwObjectListView.VIEW_MASK_MULTI_SELECTION))
            {
                // enable scripting events for the table
                OwScriptTable.writeSelectableListEnableScript(String.valueOf(getListViewID()), w_p, getContext());
            }
        }

        // set the end point for the AJAX persistence service
        addAjaxPersistenceService(w_p, "PersistSelection");

        ((OwMainAppContext) getContext()).registerMouseAction(OwObjectListView.SELECT_DESELECT_NONCONSECUTIVE_OBJECTS_ACTION_ID, new OwString("OwObjectListView.nonconsecutive.select.description", "Select/Deselect result list entry"));
    }

    /**
     * Called upon AJAX request "PersistSelection"
     * 
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxPersistSelection(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        persistAjaxTriggeredSelection(request_p, response_p);
    }

    /** render the view JSP 
     * @param w_p Writer object to write HTML to
     */
    protected void renderMainRegion(java.io.Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwObjectListViewRow.jsp", w_p);
    }

    /** insert the document function plugins for the requested row index and object
     * @param w_p writer object for HTML output
     * @param obj_p OwObject to create Function plugin for
     * @param iIndex_p the row / object index
     * @param instancePluginsList_p Collection of plugins that are visible (have there own column) together with the global index
     */
    protected void insertDocumentFunctionPluginRows(java.io.Writer w_p, OwObject obj_p, int iIndex_p, Collection instancePluginsList_p) throws Exception
    {
        // iterate over preinstantiated plugins and create HTML 
        Iterator<?> it = instancePluginsList_p.iterator();
        while (it.hasNext())
        {
            OwPluginEntry pluginEntry = (OwPluginEntry) it.next();
            if (!getPluginCache().isPluginDisabledForAllObjects(pluginEntry))
            {
                boolean isPluginEnabled;
                OwPluginStatus status = getPluginCache().getCachedPluginState(pluginEntry, obj_p);
                if (status.isCached())
                {
                    isPluginEnabled = status.isEnabled();
                }
                else
                {
                    // check if object type is supported by plugin
                    isPluginEnabled = pluginEntry.m_Plugin.isEnabled(obj_p, getParentObject(), OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
                }

                if (isPluginEnabled)
                {
                    // get the No Event Flag indicating if plugin should receive a click event and therefore need to wrap a anchor around it.
                    if (pluginEntry.m_Plugin.getNoEvent())
                    {
                        // === do not create anchor
                        w_p.write("\n<td style=\"white-space:nowrap;\">");
                        w_p.write(pluginEntry.m_Plugin.getIconHTML(obj_p, null));
                        w_p.write("</td>\n");
                    }
                    else
                    {
                        // === create anchor with reference to the selected object and a tooltip
                        w_p.write("\n<td><a href=\"");
                        StringBuffer url = new StringBuffer(OwObjectListView.OBJECT_INDEX_KEY).append("=").append(iIndex_p);
                        url.append("&").append(OwObjectListView.PLUG_INDEX_KEY).append("=").append(pluginEntry.m_iIndex);
                        w_p.write(getEventURL("PluginEvent", url.toString()));
                        w_p.write("\" title=\"");
                        w_p.write(pluginEntry.m_Plugin.getTooltip());
                        w_p.write("\" class=\"OwGeneralList_Plugin_a\">");
                        w_p.write(pluginEntry.m_Plugin.getIconHTML(obj_p, null));
                        w_p.write("</a></td>\n");
                    }
                }
                else
                {
                    // === not supported, show nothing at all    
                    w_p.write("\n<td width=\"1\"></td>\n");
                }
            }
        }

    }

    /** event called when user clicked a sort header
     *   @param request_p  HttpServletRequest
     */
    public void onSort(HttpServletRequest request_p) throws Exception
    {
        // === get the sort property and update current sort instance
        String strSortProperty = request_p.getParameter(OwObjectListView.SORT_PROPERTY_KEY);
        if (strSortProperty != null)
        {
            // toggle the criteria ascending or descending
            getSort().toggleCriteria(strSortProperty);

            // sort list with new sort object
            /*
            if (m_ObjectList != null)
            {
                m_ObjectList.sort(m_Sort);
                resetPersistedSelectionState();
            }*/

            // notify client
            if (getEventListner() != null)
            {
                getEventListner().onObjectListViewSort(getSort(), strSortProperty);
            }
        }
    }

    /** event called when user clicked on a plugin link on the context menu
     *   @param request_p  HttpServletRequest
     */
    public void onContextMenuEvent(HttpServletRequest request_p) throws Exception
    {
        //onPluginEvent(request_p);
        // do a multi select event with the comboboxes so we can use multiple selection with context menu
        String strPlugIndex = request_p.getParameter(OwObjectListView.PLUG_INDEX_KEY);
        multiSelectEvent(request_p, strPlugIndex);
    }

    /** event called when user clicked on a plugin column
     *  @param request_p  HttpServletRequest
     */
    @SuppressWarnings("unchecked")
    public void onColumnClickEvent(HttpServletRequest request_p) throws Exception
    {
        // get plugin
        int iPlugIndex = Integer.parseInt(request_p.getParameter(OwObjectListView.PLUG_INDEX_KEY));
        OwDocumentFunction plugIn = getDocumentFunction(iPlugIndex);
        OwObjectCollection processedObjects = getObjectList();
        if (processedObjects == null)
        {
            processedObjects = new OwStandardObjectCollection();
            for (OwObject obj : getDisplayedPage())
            {
                processedObjects.add(obj);
            }
        }
        // invoke event
        plugIn.onColumnClickEvent(processedObjects, getParentObject(), getRefreshContext());
    }

    /** event called when user submitted the mulitiselect form
     *   @param request_p  HttpServletRequest
     */
    public void onMultiSelectEvent(HttpServletRequest request_p) throws Exception
    {
        // get selected plugin
        String strPlugIndex = request_p.getParameter(MULTISELECT_COMOBO_MENU_NAME);

        multiSelectEvent(request_p, strPlugIndex);
    }

    /**
     * event called when user submitted the mulitiselect form
     * @param request_p HttpServletRequest
     * @param strPluginIndex_p String
     * @throws Exception
     */
    private void multiSelectEvent(HttpServletRequest request_p, String strPluginIndex_p) throws Exception
    {
        // get selected indexes
        String[] checked = request_p.getParameterValues(OwScriptTable.getSelectableListCheckBoxName(String.valueOf(getListViewID())));
        if ((null != checked) && (strPluginIndex_p != null))
        {
            // get plugin
            int iPlugIndex = Integer.parseInt(strPluginIndex_p);
            OwDocumentFunction plugIn = getDocumentFunction(iPlugIndex);

            // create list of selected objects
            List<OwObject> objects = new LinkedList<OwObject>();

            for (int i = 0; i < checked.length; i++)
            {
                // add object
                OwObject obj = getObjectByIndex(Integer.parseInt(checked[i]));
                objects.add(obj);
            }

            // apply multiselect event on multiple objects
            if (objects.size() == 1)
            {
                plugIn.onClickEvent(objects.get(0), getParentObject(), getRefreshContext());
            }
            else
            {
                if (!plugIn.getMultiselect())
                {
                    throw new OwUserOperationException(new OwString1("app.OwObjectListViewRow.onlysingleselectfunction", "Function %1 can only be applied to one object.", plugIn.getDefaultLabel()));
                }

                OwMultipleSelectionCall multiCall = new OwMultipleSelectionCall(plugIn, objects, getParentObject(), getRefreshContext(), m_MainContext);
                multiCall.invokeFunction();
            }
        }
    }

    /** event called when user clicked on a plugin link of an object entry in the list 
     *   @param request_p  HttpServletRequest
     */
    public void onPluginEvent(HttpServletRequest request_p) throws Exception
    {
        // === handle plugin event
        // parse query string
        String strObjectIndex = request_p.getParameter(OwObjectListView.OBJECT_INDEX_KEY);
        String strPlugIndex = request_p.getParameter(OwObjectListView.PLUG_INDEX_KEY);
        if ((strObjectIndex != null) && (strPlugIndex != null))
        {
            // get plugin
            int iPlugIndex = Integer.parseInt(strPlugIndex);
            OwDocumentFunction plugIn = getDocumentFunction(iPlugIndex);

            // get object
            int iObjectIndex = Integer.parseInt(strObjectIndex);
            OwObject obj = getObjectByIndex(iObjectIndex);

            // delegate event to plugin
            plugIn.onClickEvent(obj, getParentObject(), getRefreshContext());

            resetPersistedSelectionState();
            persistObjectSelectionState(iObjectIndex, true);
        }
    }

    /** event called when user selected an item
     *   @param request_p  HttpServletRequest
     */
    public void onSelect(HttpServletRequest request_p) throws Exception
    {
        int objectIndex = Integer.parseInt(request_p.getParameter(OwObjectListView.OBJECT_INDEX_KEY));

        resetPersistedSelectionState();
        persistObjectSelectionState(objectIndex, true);
        if (getEventListner() != null)
        {
            OwObject obj = getObjectByIndex(objectIndex);
            getEventListner().onObjectListViewSelect(obj, getParentObject());
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
        return true;
    }

    /** get a collection of property names that are needed to display the Objects in the list
     *  i.e. these properties should be requested in advance to save server roundtrips.
     *  @return Collection of String
     * */
    public Collection getRetrievalPropertyNames() throws Exception
    {
        if (null == getColumnInfo())
        {
            throw new OwInvalidOperationException("OwObjectListViewRow.getRetrievalPropertyNames: Specify setColumnInfo() in OwObjectList.");
        }

        Set retList = new HashSet();

        // === add the column info properties
        Iterator it = getColumnInfo().iterator();

        while (it.hasNext())
        {
            OwFieldColumnInfo ColInfo = (OwFieldColumnInfo) it.next();
            retList.add(ColInfo.getPropertyName());
        }

        // === add properties from the plugins
        it = getPluginEntries().iterator();
        while (it.hasNext())
        {
            OwPluginEntry entry = (OwPluginEntry) it.next();
            Collection props = entry.m_Plugin.getRetrievalPropertyNames();
            if (null != props)
            {
                retList.addAll(props);
            }
        }

        return retList;
    }

    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        throw new OwObjectNotFoundException("OwObjectListViewRow.getField: Not implemented or Not supported.");
    }

    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT | OwFieldProvider.TYPE_RESULT_LIST;
    }

    public Object getFieldProviderSource()
    {
        return getDisplayedPage();
    }

    public String getFieldProviderName()
    {
        try
        {
            return getParentObject().getName();
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        OwField field = getField(sName_p);
        field.setValue(value_p);
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            OwField field = getField(sName_p);
            return field.getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        throw new OwInvalidOperationException("OwObjectListViewRow.getFields: Not implemented.");
    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return this.getContext().localize("dmsdialogs.views.OwObjectListViewRow.title", "List");
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon url, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return "/images/OwObjectListView/OwObjectListViewRow.png";
    }

    /**
     * Get current OwMimeManager instance
     * @return the associated OwMimeManager
     */
    public OwMimeManager getMimeManager()
    {
        return m_MimeManager;
    }

    /**
     * Render the page selector
     * @param w_p
     * @throws Exception
     * @since 2.5.2.0
     */
    public void renderPageSelector(java.io.Writer w_p) throws Exception
    {
        m_pageSelectorComponent.render(w_p);
    }

    /**
     * 
     * @return String URL
     * @deprecated since 4.2.0.0 use {@link #getPagePrevEventURL()}
     */
    @Deprecated
    public String getPagePrevURL()
    {
        return getPagePrevEventURL();
    }

    /**
     * 
     * @return String URL
     * @deprecated since 4.2.0.0 use {@link #getPageNextEventURL()}
     */
    @Deprecated
    public String getPageNextURL()
    {
        return getPageNextEventURL();
    }

    /**
     * get an URL to trigger page next calls;
     * @param iPage_p int page
     * @return string
     * @deprecated since 4.2.0.0 use {@link #getPageAbsolutEventURL(String)}
     */
    @Deprecated
    public String getPageAbsolutURL(int iPage_p)
    {
        return getPageAbsolutEventURL(OwObjectListView.QUERY_KEY_PAGE + "=" + String.valueOf(iPage_p));
    }

}