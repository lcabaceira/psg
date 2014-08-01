package com.wewebu.ow.server.dmsdialogs.views;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

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
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwUserOperationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwScriptTable;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

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
public class OwObjectListViewThumbnails extends OwPageableListView implements OwFieldProvider
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectListViewThumbnails.class);

    public static final int DEFAULT_THUMBS_PER_ROW = Integer.MAX_VALUE;

    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;

    /** current zero based page number in multiples of getPageSize() for paging the results 
     * @deprecated since 4.2.0.0 use {@link #getCurrentPage()}*/
    protected int m_iCurrentPage = 0;

    /** how many thumbnails should be display per row */
    protected int MAX_THUMBNAIL_PER_ROW = DEFAULT_THUMBS_PER_ROW;

    //  === replacement tokens for URL pattern
    /** token in the viewer servlet to be replaced by the DMSID */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_DMSID = "{dmsid}";
    /** token in the viewer servlet to be replaced by the base URL of the server */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_SERVERURL = "{serverurl}";
    /** token in the viewer servlet to be replaced by the base URL of the server with application context*/
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_BASEURL = "{baseurl}";

    /** token in the viewer servlet to be replaced by the property following the : */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START = "{prop";
    /** char to indicate encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_ENCODE_CHAR = ':';
    /** char to indicate NO encoding of given property */
    public static final char VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_NOENCODE_CHAR = '#';
    /** token in the viewer servlet to be replaced by the property end delimiter */
    public static final String VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_END = "}";

    /** the list with the sort criteria 
     * @deprecated since 4.2.0.0 use set-/{@link #getSort()} instead*/
    protected OwSort m_Sort;

    /** flag indicating if context menu should be shown */
    protected boolean m_useContextMenu;

    /** instance of the MIME manager used to open the objects */
    protected OwMimeManager m_MimeManager;

    /** a list of column info, which describe the columns of the object list 
     * @deprecated since 4.2.0.0 use set-/ {@link #getColumnInfo()} instead*/
    protected Collection<?> m_ColumnInfoList;

    /** instance of the property field class 
     * @deprecated since 4.2.0.0 use set-/ {@link #getFieldManager()} instead*/
    protected OwFieldManager m_theFieldManager;

    /**event listener for the function plugin refresh events 
     * @deprecated since 4.2.0.0 use set-/ {@link #getRefreshContext()} instead*/
    protected OwClientRefreshContext m_RefreshContext;

    /**A set of object types which are displayed by the list*/
    private Set<Integer> m_occuredObjectTypes;

    /** construct a object list view
     *
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public OwObjectListViewThumbnails(int iViewMask_p)
    {
        super(iViewMask_p);
        m_MimeManager = new OwMimeManager();
    }

    /** construct a object list view
     */
    public OwObjectListViewThumbnails()
    {
        super();
        m_MimeManager = new OwMimeManager();
    }

    /** XML node name for the config node */
    private static final String THUMBNAIL_TYPES_NODE_NAME = "ThumbnailTypes";

    public static final int THUMBNAIL_LIST = 1;

    public static final int CONTEXT_MENU = 2;

    public static final int PAGE_BUTTONS = 3;

    /** list of thumbnail types */
    protected ArrayList<ThumbnailTypeConfig> m_ThumbnailTypes = new ArrayList<ThumbnailTypeConfig>();

    /** configdata of thumbnail type */
    protected static class ThumbnailTypeConfig
    {
        private String m_displayname = "";
        private String m_url = "";
        private int m_width = 0;
        private int m_height = 0;
        private boolean m_scale;

        public ThumbnailTypeConfig(String displayname_p, int width_p, int height_p, boolean scale_p, String url_p)
        {
            m_displayname = displayname_p;
            m_width = width_p;
            m_height = height_p;
            m_url = url_p;
            m_scale = scale_p;
        }

        public String getDisplayname()
        {
            return (m_displayname);
        }

        public int getWidth()
        {
            return (m_width);
        }

        public int getHeight()
        {
            return (m_height);
        }

        public String getUrl()
        {
            return (m_url);
        }

        boolean isScale()
        {
            return m_scale;
        }

    }

    /**
     * Check if list is valid and there are available items to render.
     * @return boolean
     * @see #getIsListValid
     */
    public boolean getIsList()
    {
        return (getCount() != 0) && getIsListValid();
    }

    /**
     * Check if there are all needed information available for successful rendering.
     * By default check column definition and if items exist.
     * @return boolean
     */
    public boolean getIsListValid()
    {
        return ((getObjectList() != null || getObjectIterable() != null) && (getColumnInfo() != null));
    }

    /** optional use the default constructor and set a config node to configure the view with XML 
     * This may override the settings in the ViewMaks, see setViewMask
     * 
     * @param node_p XML node with configuration information
     * @throws Exception 
     */
    public void setConfigNode(Node node_p) throws Exception
    {
        // config parent
        super.setConfigNode(node_p);
        // get thumbnail types
        Node thumbnailtypesNode = OwXMLDOMUtil.getChildNode(node_p, THUMBNAIL_TYPES_NODE_NAME);
        if (null == thumbnailtypesNode)
        {
            String msg = "OwObjectListViewThumbnails.setConfigNode: Please define a ThumbnailTypes node in the plugin descriptor.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
        m_ThumbnailTypes.clear();
        List<?> thumbnailTypes = OwXMLDOMUtil.getSafeNodeList(thumbnailtypesNode);
        Iterator<?> it = thumbnailTypes.iterator();
        while (it.hasNext())
        {
            Node thumbnailTypeNode = (Node) it.next();
            String sDisplayname = OwXMLDOMUtil.getSafeStringAttributeValue(thumbnailTypeNode, "displayname", null);
            String sWidth = OwXMLDOMUtil.getSafeStringAttributeValue(thumbnailTypeNode, "width", null);
            String sHeight = OwXMLDOMUtil.getSafeStringAttributeValue(thumbnailTypeNode, "height", null);
            boolean scale = false;
            String sUrl = OwXMLDOMUtil.getSafeStringAttributeValue(thumbnailTypeNode, "url", null);
            if ((sDisplayname != null) && (sUrl != null))
            {
                try
                {
                    int iWidth = -1;
                    int iHeight = -1;

                    if (sWidth != null && sWidth.trim().length() > 0)
                    {
                        iWidth = Integer.parseInt(sWidth);
                        scale = true;
                    }

                    if (sHeight != null && sHeight.trim().length() > 0)
                    {
                        iHeight = Integer.parseInt(sHeight);
                        scale = true;
                    }

                    m_ThumbnailTypes.add(new ThumbnailTypeConfig(sDisplayname, iWidth, iHeight, scale, sUrl));
                }
                catch (Exception e)
                {
                    LOG.debug("Invalid thumbnail configuration.", e);
                }
            }
        }
        if (m_ThumbnailTypes.size() <= 0)
        {
            String msg = "OwObjectListViewThumbnails.setConfigNode: Please define at least one ThumbnailType in the plugin descriptor.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
    }

    /** replace a object property placeholder
     * @param strIn_p String to search and replace
     * @param obj_p OwObjectReference
     * 
     * @return String
     * @throws Exception 
     */
    protected static String replaceProperties(OwMainAppContext context_p, String strIn_p, OwObjectReference obj_p) throws Exception
    {
        // check if placeholder is available at all before we allocate the object instance
        int iIndex = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START);
        if (-1 == iIndex)
        {
            return strIn_p;
        }

        // now we get the object instance
        OwObject obj = obj_p.getInstance();

        int iOldIndex = 0;

        StringBuilder strRet = new StringBuilder();

        while (-1 != iIndex)
        {
            strRet.append(strIn_p.substring(iOldIndex, iIndex));

            // skip encoding character
            iIndex++;
            iIndex += VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START.length();

            // get the property name
            int iEnd = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_END, iIndex);
            String strPropName = strIn_p.substring(iIndex, iEnd);

            // replace property ignore all exceptions
            if (strIn_p.charAt(iIndex - 1) == VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_ENCODE_CHAR)
            {
                // encoded property
                try
                {
                    strRet.append(OwAppContext.encodeURL(obj.getProperty(strPropName).getValue().toString()));
                }
                catch (Exception e)
                {
                }
            }
            else
            {
                // unencoded property
                try
                {
                    strRet.append(obj.getProperty(strPropName).getValue().toString());
                }
                catch (Exception e)
                {
                }
            }

            iIndex = iEnd + 1;

            iOldIndex = iIndex;

            iIndex = strIn_p.indexOf(VIEWER_SERVLET_REPLACE_TOKEN_PROPERTY_START, iIndex);
        }

        if (iOldIndex <= strIn_p.length())
        {
            strRet.append(strIn_p.substring(iOldIndex, strIn_p.length()));
        }

        return strRet.toString();
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

    protected String usesFormWithAttributes()
    {
        // render form only when multi selection is active
        return "";
    }

    protected boolean isPagingEnabled()
    {
        return (!getParent().isShowMaximized());
    }

    public void setRefreshContext(OwClientRefreshContext eventlister_p)
    {
        super.setRefreshContext(eventlister_p);
        m_MimeManager.setRefreshContext(eventlister_p);
    }

    protected void init() throws Exception
    {
        super.init();

        // get and cast appcontext
        m_MainContext = (OwMainAppContext) getContext();

        // get preloaded plugins reference
        if (hasViewMask(VIEW_MASK_USE_DOCUMENT_PLUGINS))
        {
            // === determine the behavior 
            // context menu
            m_useContextMenu = (m_MainContext.getConfiguration().isDocmentFunctionRequirement(OwConfiguration.PLUGINS_REQUIRE_CONTEXT_MENU) && (hasViewMask(VIEW_MASK_MULTI_SELECTION) || hasViewMask(VIEW_MASK_SINGLE_SELECTION)));
            // so do we have a context menu ?
            if (!m_useContextMenu)
            {
                // no multi selection needed without context menu plugins, so clear selection flags
                clearViewMask(VIEW_MASK_MULTI_SELECTION);
                clearViewMask(VIEW_MASK_SINGLE_SELECTION);
            }
        }
        else
        {
            // === determine the behavior 
            // no context menu needed without plugins
            m_useContextMenu = false;

            // no multiselection needed without plugins, so clear selection flags
            clearViewMask(VIEW_MASK_MULTI_SELECTION);
            clearViewMask(VIEW_MASK_SINGLE_SELECTION);
        }

        setSort(new OwSort(m_MainContext.getMaxSortCriteriaCount(), true));

        // === get reference to the propertyfield manager instance
        setFieldManager(m_MainContext.createFieldManager());
        getFieldManager().setExternalFormTarget(getFormTarget());
        getFieldManager().setFieldProvider(this);

        // === init MIME manager as event target
        m_MimeManager.attach(m_MainContext, null);

        m_pageSelectorComponent = createPageSelector();
    }

    public void detach()
    {
        // detach the field manager and MIME manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();
        super.detach();
    }

    /**
     * Get Thumbnail RowId
     * @param idx_p int
     * @param line_p int
     * @return a {@link String} 
     */
    protected String getThumbnailRowId(int idx_p, int line_p)
    {
        String x = "" + line_p;
        while (x.length() < 4)
        {
            x = "0" + x;
        }
        return ("mtn" + x + "_" + getListViewID() + "_" + idx_p);
    }

    /** render the header portion of the object list
     * @param w_p Writer object to write HTML to
     * @param instancePluginsList_p Collection of plugins that are displayed for each instance
     *
     * @return Set of Integer object types that occurred in the render
     */
    protected Set<Integer> renderThumbnails(java.io.Writer w_p, Collection<OwPluginEntry> instancePluginsList_p) throws Exception
    {
        // === compute start end indexes
        int iStartIndex = 0;
        int iEndIndex;
        {
            int iPageSize = getPageSize();
            iStartIndex = getCurrentPage() * iPageSize;

            iEndIndex = iStartIndex + iPageSize;
        }

        if (!m_ThumbnailTypes.isEmpty())
        {
            StringWriter writeBuffer = new StringWriter();
            ThumbnailTypeConfig defaultTT = this.m_ThumbnailTypes.get(0);
            String script = String.format("currentThumnailTypeConfig = {type : %d, width : %d, height : %d, scale : %b};", 0, defaultTT.getWidth(), defaultTT.getHeight(), defaultTT.isScale());
            writeBuffer.write("<script type=\"text/javascript\">\n" + script + "\n</script>");

            w_p.write(writeBuffer.getBuffer().toString());
        }

        // collect the object types of the rendered objects, used to preselect plugins for the multiselect menu
        Set<Integer> occuredObjectTypes = new HashSet<Integer>();
        String listViewId = String.valueOf(getListViewID());
        // === iterate over result objects
        int i = iStartIndex;
        //final int pageMaxItems = iEndIndex - i;
        List<OwObject> cachedPage = new ArrayList<OwObject>();
        for (OwObject obj : getDisplayedPage())
        {
            cachedPage.add(obj);
        }
        final int pageItemsCount = cachedPage.size();
        for (OwObject obj : cachedPage)
        {
            try
            {
                StringWriter writeBuffer = new StringWriter();

                boolean objectIsSelected = isObjectSelectionPersisted(i);
                String thumbDivClass = "OwObjectListViewThumbnails_div";

                if (objectIsSelected)
                {
                    thumbDivClass = "OwObjectListViewThumbnails_divselected";
                }
                // store the object type
                occuredObjectTypes.add(Integer.valueOf(obj.getType()));

                renderBlockBegin(iStartIndex, i, writeBuffer);
                // render div start
                writeBuffer.write("<div id=\"owtndiv_");
                writeBuffer.write(listViewId);
                writeBuffer.write("_" + i);
                //                writeBuffer.write("\" style=\"width:150px; height:300px");
                writeBuffer.write("\" class=\"");
                writeBuffer.write(thumbDivClass);
                writeBuffer.write("\">\n");

                // line counter
                int linecounter = 1;

                // render thumbnail table start
                writeBuffer.write("<table class=\"OwObjectListViewThumbnails_table\" name=\"OwObjectListViewThumbnails_table\">\n");

                // render head cell start
                writeBuffer.write("<tr id=\"");
                writeBuffer.write(getThumbnailRowId(i, linecounter++));
                writeBuffer.write("\">\n");
                writeBuffer.write("<td class=\"OwObjectListViewThumbnails_headcell\">\n");
                writeBuffer.write("<table><tbody class=\"OwObjectListViewThumbnails_HeadTableBody\"><tr>");

                // render checkbox
                writeBuffer.write("<td align=\"center\" valign=\"middle\" style=\"display:none;\"  width=\"10%\">");
                writeBuffer.write("<input onclick=\"onClickThumbnailCheckBox('" + i);
                writeBuffer.write("','");
                writeBuffer.write(listViewId);
                writeBuffer.write("');\" type=\"checkbox\"");
                writeBuffer.write(" id=\"owtcid_");
                writeBuffer.write(listViewId);
                writeBuffer.write("_" + i);
                writeBuffer.write("\" name=\"owtcb_");
                writeBuffer.write(listViewId);
                writeBuffer.write("\" title=\"");
                writeBuffer.write(getContext().localize1("app.OwObjectListViewThumbnails.thumbnail.checkbox.title", "Thumbnail %1", "" + (i + 1)));
                writeBuffer.write("\" value=\"" + i + "\"");
                if (objectIsSelected)
                {
                    writeBuffer.write(" checked");
                }
                writeBuffer.write(">\n");
                writeBuffer.write("</td>\n");

                //lock status
                renderLockedColumn(writeBuffer, obj);

                // === Version
                //                boolean isLocked = obj.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
                //                String versionWidth = "43%";
                //                if (isLocked)
                //                {
                //                    versionWidth = "40%";
                //                }
                //                writeBuffer.write("<td align=\"center\" valign=\"middle\" style=\"white-space:nowrap;\"  width=\"");
                //                writeBuffer.write(versionWidth);
                //                writeBuffer.write("\">\n");
                //                if (obj.hasVersionSeries())
                //                {
                //                    if (obj.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
                //                    {
                //                        // === checked out symbol
                //                        writeBuffer.write("<img align=\"top\" src=\"" + getContext().getDesignURL() + "/images/checkedout.png\"");
                //                        String tooltip = getContext().localize("app.Checked_out", "Checked out");
                //                        writeBuffer.write(" alt=\"");
                //                        writeBuffer.write(tooltip);
                //                        writeBuffer.write("\" title=\"");
                //                        writeBuffer.write(tooltip);
                //                        writeBuffer.write("\" border=\"0\">\n");
                //                    }
                ////                    writeBuffer.write("(" + obj.getVersion().getVersionInfo() + ")\n");
                //                }
                //                writeBuffer.write("</td>\n");

                // render head cell end
                writeBuffer.write("</tr></tbody></table>");
                writeBuffer.write("</td>\n");
                writeBuffer.write("</tr>\n");

                // === create list row
                /*
                if ( hasViewMask(VIEW_MASK_MULTI_SELECTION) )
                {
                        OwScriptTable.writeSelectableListRowStart(String.valueOf(hashCode()),w_p,i,strRowClass,true);
                }
                else
                {
                        w_p.write("\n<tr class=\"" + strRowClass + "\">\n");
                }
                */

                // === name properties only
                // iterate over the columns / properties
                //                for (OwFieldColumnInfo colInfo : getColumnInfo())
                //                {
                // get property from object
                //                    try
                //                    {
                //                        OwProperty prop = obj.getProperty(colInfo.getPropertyName());
                //
                //                        // === Property found
                //                        OwPropertyClass propClass = prop.getPropertyClass();
                //                        if (propClass.isNameProperty())
                //                        {
                writeBuffer.write("<tr id=\"");
                writeBuffer.write(getThumbnailRowId(i, linecounter++));
                writeBuffer.write("\" class=\"OwObjectListViewThumbnails_name_row\">\n");
                writeBuffer.write("<td class=\"OwObjectListViewThumbnails_cell\" align=\"center\" style=\"text-align:center\"");
                //                            writeBuffer.write(getHtmlAlignment(colInfo.getAlignment()));
                writeBuffer.write(">\n");

                writeBuffer.write("<p class=\"OwObjectListViewThumbnails_ellipsis_name\" style=\"width:1px\">");
                // Name properties are inserted with a download / view link
                m_MimeManager.insertTextLink(writeBuffer, obj.getName(), obj);
                //                            try
                //                            {
                //                                m_MimeManager.insertTextLink(writeBuffer, prop.getValue().toString(), obj);
                //                            }
                //                            catch (NullPointerException e)
                //                            {
                //                                m_MimeManager.insertTextLink(writeBuffer, null, obj);
                //                            }
                writeBuffer.write("</p>");
                writeBuffer.write("\n</td>\n</tr>\n");
                //                        }

                //                    }
                //                    catch (OwObjectNotFoundException e)
                //                    {
                // === Property not found
                // Uncommented for new thumbnail view, as when columns are confiured for documents, this additional table row is rendered for folders not having the column
                //writeBuffer.write("<tr class=\"OwObjectListViewThumbnails_row\">\n<td class=\"OwObjectListViewThumbnails_cell\">\n&nbsp;\n</td>\n</tr>\n");
                //                    }
                //                }

                // render thumbnail-test cell start
                writeBuffer.write("<tr id=\"" + getThumbnailRowId(i, linecounter++));
                writeBuffer.write("\" class=\"OwObjectListViewThumbnails_image_row\">\n");
                writeBuffer.write("<td class=\"OwObjectListViewThumbnails_cell\" style=\"vertical-align:top;\">\n");

                // render thumbnail-test 
                writeBuffer.write("<img class=\"OwObjectListViewThumbnails_image\" align=\"absmiddle\" alt=\"\" title=\"\"");
                boolean defaultdone = false;
                for (int ii = 0; ii < m_ThumbnailTypes.size(); ii++)
                {
                    ThumbnailTypeConfig ttc = m_ThumbnailTypes.get(ii);
                    StringBuilder sUrl = new StringBuilder(ttc.getUrl());
                    OwString.replaceAll(sUrl, VIEWER_SERVLET_REPLACE_TOKEN_BASEURL, getContext().getBaseURL() + "/");
                    OwString.replaceAll(sUrl, VIEWER_SERVLET_REPLACE_TOKEN_SERVERURL, getContext().getServerURL() + "/");
                    OwString.replaceAll(sUrl, VIEWER_SERVLET_REPLACE_TOKEN_DMSID, OwAppContext.encodeURL(obj.getDMSID()));
                    String rsUrl = replaceProperties((OwMainAppContext) getContext(), sUrl.toString(), obj);
                    if (!defaultdone)
                    {
                        //if (ttc.isScale())
                        //{
                        //  writeBuffer.write(" width=\"" + ttc.getWidth());
                        //  writeBuffer.write("\" height=\"" + ttc.getHeight());
                        //  writeBuffer.write(" style=\"width:" + ttc.getWidth() + "px;height:" + ttc.getHeight() + "px\"");
                        //}
                        writeBuffer.write(" src=\"");
                        writeBuffer.write(rsUrl);
                        writeBuffer.write("\"");
                        defaultdone = true;
                    }

                    if (ttc.getWidth() > 0)

                    {
                        writeBuffer.write(" width");
                        writeBuffer.write(Integer.toString(ii));
                        writeBuffer.write("=");
                        writeBuffer.write("\'");
                        writeBuffer.write(String.valueOf(ttc.getWidth()));
                        writeBuffer.write("\'");
                    }

                    if (ttc.getHeight() > 0)
                    {
                        writeBuffer.write(" height");
                        writeBuffer.write(Integer.toString(ii));
                        writeBuffer.write("=");
                        writeBuffer.write("\'");
                        writeBuffer.write(String.valueOf(ttc.getHeight()));
                        writeBuffer.write("\'");
                    }

                    writeBuffer.write(" type" + ii);
                    writeBuffer.write("src=\"");
                    writeBuffer.write(rsUrl);
                    writeBuffer.write("\"");
                }
                writeBuffer.write(" border=\"0\" onload=\"onThumbReady(");
                writeBuffer.write("" + pageItemsCount);
                writeBuffer.write(")\" onerror=\"onThumbReady(");
                writeBuffer.write("" + pageItemsCount);
                writeBuffer.write(")\" name=\"OwObjectListViewThumbnails_image\">\n");

                // render thumbnail-test cell end
                writeBuffer.write("</td>\n");
                writeBuffer.write("</tr>\n");

                // === Document Plugins
                writeBuffer.write("<tr id=\"");
                writeBuffer.write(getThumbnailRowId(i, linecounter++));
                writeBuffer.write("\" class=\"OwObjectListViewThumbnails_plugin_row\">\n");
                writeBuffer.write("<td align=\"center\" style=\"text-align: center\" valign=\"middle\">\n");
                insertThumbnailBar(writeBuffer, obj, i, instancePluginsList_p);
                writeBuffer.write("</td>\n");
                writeBuffer.write("</tr>");

                // === non-name other properties
                // iterate over the columns / properties
                //                for (OwFieldColumnInfo colInfo : getColumnInfo())
                //                {
                //                    // get property from object
                //                    try
                //                    {
                //                        OwProperty prop = obj.getProperty(colInfo.getPropertyName());
                //
                //                        // === Property found
                //                        OwPropertyClass propClass = prop.getPropertyClass();
                //
                //                        if (!propClass.isNameProperty())
                //                        {
                //
                //                            writeBuffer.write("<tr id=\"");
                //                            writeBuffer.write(getThumbnailRowId(i, linecounter++));
                //                            writeBuffer.write("\" class=\"OwObjectListViewThumbnails_row\">\n");
                //                            writeBuffer.write("<td class=\"OwObjectListViewThumbnails_cell\" style=\"text-align:center\"");
                //                            writeBuffer.write(getHtmlAlignment(colInfo.getAlignment()));
                //                            writeBuffer.write(">\n");
                //                            
                //                            // normal properties are inserted via Fieldmanager
                //                            getFieldManager().insertReadOnlyField(writeBuffer, prop);
                //                        }
                //
                //                        writeBuffer.write("\n</td>\n</tr>\n");
                //                    }
                //                    catch (OwObjectNotFoundException e)
                //                    {
                //                        // === Property not found
                //                        writeBuffer.write("<tr class=\"OwObjectListViewThumbnails_row\">\n<td class=\"OwObjectListViewThumbnails_cell\">\n&nbsp;\n</td>\n</tr>\n");
                //                    }
                //                }

                // render thumbnail table end
                writeBuffer.write("</table>\n");

                // render div end
                writeBuffer.write("</div>\n");

                renderBlockEnd(iStartIndex, iEndIndex, i, writeBuffer);
                w_p.write(writeBuffer.getBuffer().toString());
                i++;
            }
            catch (Exception e)
            {
                LOG.error("Could not render thumbnail!", e);
            }
        }
        if (i < iEndIndex - 1)
        {
            StringWriter writeBuffer = new StringWriter();
            renderBlockEnd(0, i + 1, i, writeBuffer);
            w_p.write(writeBuffer.getBuffer().toString());
        }

        // render Keyboard script
        String strToolTip = getContext().localize("app.OwObjectListView.toggleselectall", "Alles (de)selektieren");
        getContext().registerKeyEvent(65, OwAppContext.KEYBOARD_CTRLKEY_CTRL, "javascript:keyboardSelectAll();", strToolTip);

        w_p.write("\n\n<script type=\"text/javascript\">activeListCheckBoxId=");
        w_p.write("\"owtcb_");
        w_p.write(listViewId);
        w_p.write("\";</script>\n\n");

        return occuredObjectTypes;
    }

    /**
     * Render block begin.
     * @param startIndexPosition_p - starting position in object list.
     * @param currentPosition_p - current position in object list.
     * @param w_p - the writer object
     * @since 3.1.0.0
     */
    protected void renderBlockBegin(int startIndexPosition_p, int currentPosition_p, StringWriter w_p)
    {
        // render block
        if ((currentPosition_p - startIndexPosition_p) % MAX_THUMBNAIL_PER_ROW == 0)
        {
            w_p.write("<div class=\"OwBlock\">");
        }
    }

    /**
     * Render block end
     * @param startIndexPosition_p - the starting position in object list.
     * @param endIndexPosition_p - the ending position in object list
     * @param currentPosition_p - the current position in object list
     * @param w_p - the writer object
     * @since 3.1.0.0
     */
    protected void renderBlockEnd(int startIndexPosition_p, int endIndexPosition_p, int currentPosition_p, StringWriter w_p)
    {
        // render block end
        if ((currentPosition_p == endIndexPosition_p - 1) || ((currentPosition_p - startIndexPosition_p) % MAX_THUMBNAIL_PER_ROW == MAX_THUMBNAIL_PER_ROW - 1))
        {
            w_p.write("</div><!-- end of .OwBlock -->");
        }
    }

    /**
     * Render the locked symbol for an object, if the object is locked.
     * @param writeBuffer_p
     * @param obj_p
     * @throws Exception
     * @since 2.5.2.0
     */
    private void renderLockedColumn(StringWriter writeBuffer_p, OwObject obj_p) throws Exception
    {
        // === render lock status column for each object
        if (obj_p.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
        {
            String lockedTooltip = getContext().localize("image.locked", "Locked item");
            writeBuffer_p.write("<td width=\"10%\"><img src=\"" + getContext().getDesignURL() + "/images/plug/owbpm/locked.png\" alt=\"" + lockedTooltip + "\" title=\"" + lockedTooltip + "\"/></td>\n");
        }
    }

    /** determine if region exists
    *
    * @param iRegion_p ID of the region to render
    * @return true if region contains anything and should be rendered
    */
    public boolean isRegion(int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case PAGE_BUTTONS:
                // === write paging buttons
                return (isPagingEnabled() && (!hasViewMask(VIEW_MASK_NO_PAGE_BUTTONS)));

            default:
                return super.isRegion(iRegion_p);
        }
    }

    /** render only a region in the view, used by derived classes
    *
    * @param w_p Writer object to write HTML to
    * @param iRegion_p ID of the region to render
    */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case CONTEXT_MENU:
                // === write context menu section
                if (m_useContextMenu)
                {
                    // render single select context menu
                    renderContextMenu(w_p, m_occuredObjectTypes);
                }

                if (hasViewMask(VIEW_MASK_MULTI_SELECTION))
                {
                    // enable scripting events for the table
                    OwScriptTable.writeSelectableListEnableScript(String.valueOf(getListViewID()), w_p, getContext());
                }
                break;

            case THUMBNAIL_LIST:
                // get the plugins that are displayed for each instance
                // NOTE: we create the list here in onRender to save session memory
                m_occuredObjectTypes = renderThumbnails(w_p, getPluginEntries());
                break;

            case PAGE_BUTTONS:
                // === write paging buttons
                if (hasPaging())
                {
                    renderPageButtons(w_p);
                }
                break;

            default:
                super.renderRegion(w_p, iRegion_p);
        }
    }

    public String getThumbnailTypeDisplayName(int i_p)
    {
        ThumbnailTypeConfig ttc = m_ThumbnailTypes.get(i_p);
        return ((OwMainAppContext) getContext()).localizeLabel(ttc.getDisplayname());
    }

    public int getThumbnailTypeCount()
    {
        return m_ThumbnailTypes.size();
    }

    public String getThumbnailTypeUrl(int i_p)
    {
        ThumbnailTypeConfig ttc = m_ThumbnailTypes.get(i_p);
        return "javascript:Thumbnails_setType(" + i_p + "," + ttc.getWidth() + "," + ttc.getHeight() + "," + ttc.isScale() + ");";
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        // always reset MIME manager before inserting links !!!
        m_MimeManager.reset();
        getFieldManager().reset();

        // set the endpoint for the AJAX persistence service
        addAjaxPersistenceService(w_p, "PersistSelection");

        serverSideDesignInclude("OwObjectListViewThumbnails.jsp", w_p);

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

    /** render the paging buttons
     * @param w_p Writer object to write HTML to
     */
    protected void renderPageButtons(java.io.Writer w_p) throws Exception
    {
        m_pageSelectorComponent.render(w_p);
    }

    /**
     * Inserts the thumbnail action bar (document function plugins and status notifications).
     * @see #insertStatus(Writer, OwObject, int)
     * @see #insertDocumentFunctionPlugins(Writer, OwObject, int, Collection)
     * @param w_p
     * @param obj_p
     * @param iIndex_p
     * @param instancePluginsList_p
     * @throws Exception
     * @since 4.2.0.0
     */
    protected void insertThumbnailBar(java.io.Writer w_p, OwObject obj_p, int iIndex_p, Collection<OwPluginEntry> instancePluginsList_p) throws Exception
    {
        final String statusDivStart = "<div class=\"OwObjectListViewThumbnails_status\">";
        w_p.write(statusDivStart);
        insertStatus(w_p, obj_p, iIndex_p);
        w_p.write("</div>");

        insertDocumentFunctionPlugins(w_p, obj_p, iIndex_p, instancePluginsList_p);

        w_p.write(statusDivStart);
        w_p.write("&nbsp;");
        w_p.write("</div>");

    }

    /**
     * Inserts status HTML part of the thumbnail bar.
     * @see{@link #insertThumbnailBar(Writer, OwObject, int, Collection)} 
     * @param w_p
     * @param obj_p
     * @param iIndex_p
     * @throws Exception
     * @since 4.2.0.0
     */
    protected void insertStatus(java.io.Writer w_p, OwObject obj_p, int iIndex_p) throws Exception
    {
        if (obj_p.hasVersionSeries() && obj_p.getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
        {
            w_p.write("<img src=\"" + getContext().getDesignURL() + "/images/checkedout.png\" alt=\"Checkedout\">");
        }
        else
        {
            w_p.write("&nbsp;");
        }
    }

    /** insert the document function plugins for the requested row index and object
     * @param w_p writer object for HTML output
     * @param obj_p OwObject to create Function plugin for
     * @param iIndex_p the row / object index
     * @param instancePluginsList_p Collection of plugins that are visible (have there own column) together with the global index
     */
    protected void insertDocumentFunctionPlugins(java.io.Writer w_p, OwObject obj_p, int iIndex_p, Collection<OwPluginEntry> instancePluginsList_p) throws Exception
    {

        w_p.write("<div class=\"OwObjectListViewThumbnails_dfplugins\">");

        // iterate over preinstantiated plugins and create HTML 
        Iterator<OwPluginEntry> it = instancePluginsList_p.iterator();
        while (it.hasNext())
        {
            OwPluginEntry plugInEntry = it.next();

            // check if object type is supported by plugin
            if (plugInEntry.m_Plugin.isEnabled(obj_p, getParentObject(), OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL))
            {
                w_p.write("<div class=\"OwObjectListViewThumbnails_dfcell\">");

                // get the No Event Flag indicating if plugin should receive a click event and therefore need to wrap a anchor around it.
                if (plugInEntry.m_Plugin.getNoEvent())
                {
                    // === do not create anchor
                    w_p.write(plugInEntry.m_Plugin.getIconHTML(obj_p, null));
                }
                else
                {
                    // === create anchor with reference to the selected object and a tooltip
                    w_p.write("<a href=\"");
                    w_p.write(getEventURL("PluginEvent", OwObjectListView.OBJECT_INDEX_KEY));
                    w_p.write("=");
                    w_p.write(String.valueOf(iIndex_p));
                    w_p.write("&");
                    w_p.write(OwObjectListView.PLUG_INDEX_KEY);
                    w_p.write("=");
                    w_p.write(String.valueOf(plugInEntry.m_iIndex));
                    w_p.write("\" title=\"");
                    w_p.write(plugInEntry.m_Plugin.getTooltip());
                    w_p.write("\" class=\"OwGeneralList_Plugin_a\">");
                    w_p.write(plugInEntry.m_Plugin.getIconHTML(obj_p, null));
                    w_p.write("</a>\n");
                }

                w_p.write("</div>");
            }
        }

        w_p.write("</div>");

    }

    /**
     * event called when user clicked a sort header
     * @param request_p  HttpServletRequest
     */
    public void onSort(HttpServletRequest request_p) throws Exception
    {
        // === get the sort property and update current sort instance
        String strSortProperty = request_p.getParameter(SORT_PROPERTY_KEY);
        if (strSortProperty != null)
        {
            // toggle the criteria ascending or descending
            getSort().toggleCriteria(strSortProperty);

            // notify client
            if (getEventListner() != null)
            {
                getEventListner().onObjectListViewSort(getSort(), strSortProperty);
            }

            // sort list with new sort object
            if (getObjectList() != null)
            {
                getObjectList().sort(getSort());
            }

        }
    }

    /**
     * event called when user clicked on a plugin link on the context menu
     * @param request_p  HttpServletRequest
     */
    public void onContextMenuEvent(HttpServletRequest request_p) throws Exception
    {
        //onPluginEvent(request_p);
        // do a multiselect event with the comoboxes so we can use multiple selection with context menu
        String strPlugIndex = request_p.getParameter(PLUG_INDEX_KEY);
        multiSelectEvent(request_p, strPlugIndex);
    }

    /**
     * event called when user clicked on a plugin column
     * @param request_p  HttpServletRequest
     */
    public void onColumnClickEvent(HttpServletRequest request_p) throws Exception
    {
        // get plugin
        int iPlugIndex = Integer.parseInt(request_p.getParameter(PLUG_INDEX_KEY));
        OwDocumentFunction plugIn = getPluginEntries().get(iPlugIndex).getPlugin();
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

    /** 
     * event called when user submitted the multi select form
     * @param request_p  HttpServletRequest
     */
    public void onMultiSelectEvent(HttpServletRequest request_p) throws Exception
    {
        // get selected plugin
        String strPlugIndex = request_p.getParameter(MULTISELECT_COMOBO_MENU_NAME);

        multiSelectEvent(request_p, strPlugIndex);
    }

    /** event called when user submitted the multi select form
     *   @param request_p  HttpServletRequest
     */
    private void multiSelectEvent(HttpServletRequest request_p, String strPluginIndex_p) throws Exception
    {
        // get selected indexes
        String[] checked = request_p.getParameterValues("owtcb_" + getListViewID());
        if ((null != checked) && (strPluginIndex_p != null))
        {
            // get plugin
            int iPlugIndex = Integer.parseInt(strPluginIndex_p);
            OwDocumentFunction plugIn = getDocumentFunction(iPlugIndex);
            //                ((OwPluginEntry) getPluginEntries().get(iPlugIndex)).getPlugin();

            // create list of selected objects
            List<OwObject> objects = new LinkedList<OwObject>();
            for (int i = 0; i < checked.length; i++)
            {
                // add object
                OwObject obj = getObjectByIndex(Integer.parseInt(checked[i]));
                objects.add(obj);
            }

            // apply multi select event on multiple objects
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
        }
    }

    /** event called when user selected an item
     *   @param request_p  HttpServletRequest
     */
    public void onSelect(HttpServletRequest request_p) throws Exception
    {
        int selected = Integer.parseInt(request_p.getParameter(OwObjectListView.OBJECT_INDEX_KEY));

        if (getEventListner() != null)
        {
            OwObject obj = getObjectByIndex(selected);
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
            throw new OwInvalidOperationException("OwObjectListViewThumbnails.getRetrievalPropertyNames: Specify setColumnInfo() in OwObjectList.");
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
        throw new OwObjectNotFoundException("OwObjectListViewThumbnails.getField: Not implemented or Not supported.");
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
        throw new OwInvalidOperationException("OwObjectListViewThumbnails.getFields: Not implemented.");
    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return this.getContext().localize("dmsdialogs.views.OwObjectListViewThumbnails.title", "Thumbnail View");
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon URL, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return "/images/OwObjectListView/OwObjectListViewThumbnail.png";
    }

}