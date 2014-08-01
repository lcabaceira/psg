package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * List View: OwObject List View Combined.
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
public class OwObjectListViewCombined extends OwPageableListView
{
    private OwXMLUtil configNode;

    /** query string key for the page index. */
    protected static final String QUERY_KEY_PAGE_COMBINED = "combinedpage";

    /** construct a object list view
     * 
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public OwObjectListViewCombined(int iViewMask_p)
    {
        super(iViewMask_p);
    }

    public OwObjectListViewCombined()
    {
        super();
    }

    /** optional use the default constructor and set a config node to configure the view with XML 
     * This may override the settings in the ViewMaks, see setViewMask
     * 
     * @param node_p XML node with configuration information
     * @throws Exception 
     */
    public void setConfigNode(Node node_p) throws Exception
    {
        super.setConfigNode(node_p);
        configNode = new OwStandardXMLUtil(node_p);
    }

    /**
     * Simple getter of configuration
     * @return OwXMLUtil (can return null, if {@link #setConfigNode(Node)} was not called yet)
     * @since 4.2.0.0
     */
    protected OwXMLUtil getConfigNode()
    {
        return this.configNode;
    }

    /** 
     * Initialize the view after the context is set.
     */
    @SuppressWarnings("unchecked")
    protected void init() throws Exception
    {
        super.init();
        List<OwObjectListView> views = new LinkedList<OwObjectListView>();
        List<OwXMLUtil> subViewConfig = getConfigNode().getSafeUtilList("subview");
        if (subViewConfig.isEmpty())
        {
            // two hard-coded views until we have everything generic configuration available
            OwObjectListView listView = new OwObjectListViewRow();
            listView.setConfigNode(getConfigNode().getNode());
            views.add(listView);

            listView = new OwObjectListViewThumbnails();
            listView.setConfigNode(getConfigNode().getNode());
            views.add(listView);
        }
        else
        {
            for (OwXMLUtil config : subViewConfig)
            {
                String name = config.getSafeStringAttributeValue("classname", null);
                if (name != null)
                {
                    Class<?> lstClass = Class.forName(name);
                    OwObjectListView subView = (OwObjectListView) lstClass.newInstance();
                    subView.setConfigNode(config.getNode());
                    views.add(subView);
                }
            }
        }

        // suppress paging buttons in all subviews
        for (OwObjectListView subview : views)
        {
            addView(subview, null);
            subview.addViewMask(VIEW_MASK_NO_PAGE_BUTTONS);
        }

        m_pageSelectorComponent = createPageSelector();
    }

    /**
     * Set a <code>java.util.List</code> of <code>OwDocumentFunction</code> to be used by this
     * list. This list overrides the default set of document functions that are retrieved from
     * the context during init.
     *
     * @param pluginList_p the <code>java.util.List</code> of <code>OwDocumentFunction</code> to be used by this list. Must not be <code>null</code>.
     */
    @SuppressWarnings("rawtypes")
    public void setDocumentFunctionPluginList(List pluginList_p)
    {
        for (int i = 0; i < getViewList().size(); i++)
        {
            OwObjectListView subview = (OwObjectListView) getViewList().get(i);
            subview.setDocumentFunctionPluginList(pluginList_p);
        }
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        boolean useHtml5DragAndDrop = ((OwMainAppContext) this.getContext()).isUseHtml5DragAndDrop();
        if (useHtml5DragAndDrop)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<script type=\"text/javascript\">\n");
            sb.append("var dropArea = document.getElementById('OwSublayout_ContentContainer');\n");
            sb.append("if(typeof addHtml5DropArea == 'function') {\n");
            sb.append("addHtml5DropArea(dropArea, false);\n");
            sb.append("}\n");
            sb.append("</script>\n");
            w_p.write(sb.toString());
        }

        // test if this list is valid to avoid multiple "This list has no entries" messages from each subview
        if (getIsList())
        {
            // render all subviews
            w_p.write("<div id=\"wrap\"><!-- wrap start combined -->");
            super.onRender(w_p);
            w_p.flush();
            w_p.write("</div><!-- div wrap end combined -->");

            // render paging buttons
            if (hasPaging())
            {
                w_p.write("<div id=\"footer\"><!-- footer start combined -->");
                renderCombinedPageButtons(w_p);
                w_p.write("</div><!-- div footer end -->");
            }
        }
        else
        {
            // list is empty. render own "list is empty" message
            w_p.write("<span id=\"emptyList\"  class=\"OwEmptyTextMessage\">" + getContext().localize("app.OwObjectListView.emptylist", "No items to display") + "</span>\n");
        }
    }

    /** same as getIsListValid() && ( ! getIsListEmpty() )
     * 
     * @return boolean true = valid list with at least one item to display
     */
    public boolean getIsList()
    {
        return getIsListValid() && !(getCount() == 0);
    }

    /** check if list is configured well and can be displayed
     * 
     * @return boolean true = list is valid, false = no valid list, don't render anything yet
     */
    public boolean getIsListValid()
    {
        return ((getCount() != 0) && (getColumnInfo() != null));
    }

    /** check if list contains any items
     * 
     * @return boolean
     * @deprecated since 4.2.0.0 use <code>{@link #getCount()} != 0 </code> instead
     */
    @Deprecated
    public boolean getIsListEmpty()
    {
        return (getCount() == 0);
    }

    /** check if page buttons should be rendered
     * 
     * @return true = render page buttons, false = no page buttons to render
     * @deprecated since 4.2.0.0 use {@link #hasPaging()}
     */
    @Deprecated
    public boolean getIsPaging()
    {
        return hasPaging();
    }

    /** render the paging buttons for the combined view
     * @param w_p Writer object to write HTML to
     */
    protected void renderCombinedPageButtons(java.io.Writer w_p) throws Exception
    {
        m_pageSelectorComponent.render(w_p);
    }

    public void setFieldManager(OwFieldManager fieldmanager_p)
    {
        super.setFieldManager(fieldmanager_p);
        Iterator<?> it = getViewList().iterator();
        while (it.hasNext())
        {
            OwObjectListView subview = (OwObjectListView) it.next();
            subview.setFieldManager(fieldmanager_p);
        }
    }

    public Collection<String> getRetrievalPropertyNames() throws Exception
    {
        if (getViewList().size() > 0)
        {
            return ((OwObjectListView) getViewList().get(0)).getRetrievalPropertyNames();
        }
        else
        {
            return null;
        }
    }

    protected void pageAbsolut(int iPage_p)
    {
        super.pageAbsolut(iPage_p);
        for (int i = 0; i < getViewList().size(); i++)
        {
            OwObjectListView subview = (OwObjectListView) getViewList().get(i);
            subview.setCurrentPage(iPage_p);
        }
    }

    public void setColumnInfo(Collection<? extends OwFieldColumnInfo> columnInfo_p)
    {
        super.setColumnInfo(columnInfo_p);
        for (int i = 0; i < getViewList().size(); i++)
        {
            OwObjectListView subview = (OwObjectListView) getViewList().get(i);
            subview.setColumnInfo(columnInfo_p);
        }
    }

    public void setObjectList(OwObjectCollection objectList_p, OwObject parentObject_p) throws Exception
    {
        super.setObjectList(objectList_p, parentObject_p);
        for (int i = 0; i < getViewList().size(); i++)
        {
            OwObjectListView subview = (OwObjectListView) getViewList().get(i);
            subview.setObjectList(objectList_p, parentObject_p);
        }
    }

    @Override
    public void setObjectIterable(OwIterable<OwObject> iterable, OwObject parentObject_p) throws Exception
    {
        super.setObjectIterable(iterable, parentObject_p);
        for (int i = 0; i < getViewList().size(); i++)
        {
            OwObjectListView subview = (OwObjectListView) getViewList().get(i);
            subview.setObjectIterable(iterable, parentObject_p);
        }
    }

    public void setRefreshContext(OwClientRefreshContext eventlistner_p)
    {
        super.setRefreshContext(eventlistner_p);
        for (int i = 0; i < getViewList().size(); i++)
        {
            OwObjectListView subview = (OwObjectListView) getViewList().get(i);
            subview.setRefreshContext(eventlistner_p);
        }
    }

    public void setSort(OwSort sort_p)
    {
        super.setSort(sort_p);
        for (int i = 0; i < getViewList().size(); i++)
        {
            OwObjectListView subview = (OwObjectListView) getViewList().get(i);
            subview.setSort(sort_p);
        }
    }

    public String getTitle()
    {
        StringBuilder sTitle = new StringBuilder();

        sTitle.append(getContext().localize("dmsdialogs.views.OwObjectListViewCombined.title", "Combined:"));

        sTitle.append(" ");

        Iterator it = getViewList().iterator();
        while (it.hasNext())
        {
            OwObjectListView subview = (OwObjectListView) it.next();
            sTitle.append(subview.getTitle());

            if (it.hasNext())
            {
                sTitle.append(", ");
            }
        }

        return sTitle.toString();
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon url, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return "/images/OwObjectListView/OwObjectListViewCombined.png";
    }

    /** 
     * Register an eventlistener with this view to receive notifications.
     * Also propagates the event listener to subviews.
     * @param eventlister_p OwObjectListCollectionEventListner interface
     **/
    public void setEventListner(OwObjectListViewEventListner eventlister_p)
    {
        super.setEventListner(eventlister_p);
        for (Iterator i = getViewList().iterator(); i.hasNext();)
        {
            OwObjectListView listView = (OwObjectListView) i.next();
            listView.setEventListner(eventlister_p);
        }
    }

    @Override
    public void detach()
    {
        super.detach();
        this.m_pageSelectorComponent = null;
        this.configNode = null;
    }
}
