package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwScript;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwTreeView;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Object tree based on EXT-JS tree view.<br/>
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
 *@since 3.1.0.0
 */
public class OwObjectTreeViewEXTJS extends OwObjectTreeView
{
    /** the class logger*/
    private static Logger LOG = OwLogCore.getLogger(OwObjectTreeViewEXTJS.class);
    /**the CSS class for rendering close folder icon*/
    protected static final String FOLDER_CLOSE_CSS_CLASS = "folder-close";
    /**the CSS class for rendering open folder icon*/
    protected static final String FOLDER_OPEN_CSS_CLASS = "folder-open";

    /**
     *<p>
     * Tree node representation used by {@link OwObjectTreeViewEXTJS}.<br/>
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
     *@since 3.1.0.0
     */
    public class OwTreeViewNodeJS extends OwTreeViewNode
    {
        /**
         * Constructor.
         * @param treeView_p
         * @param obj_p
         */
        public OwTreeViewNodeJS(OwTreeView treeView_p, Object obj_p)
        {
            super(treeView_p, obj_p);
        }

        /**
         * Get the children of this node.
         * @return a {@link List} of children.
         */
        @SuppressWarnings("unchecked")
        public List<OwTreeViewNodeJS> getChildren() throws Exception
        {
            if (!m_fChildsCreated)
            {
                createChilds();
                m_fChildsCreated = true;
            }
            return m_childs;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return m_displaypath;
        }

        /**
         * Refresh the node. Implemented only for java access rights.
         */
        @Override
        protected void refresh() throws Exception
        {
            super.refresh();
        }

        /**
         * Creates a JSON tree representing the structure of this node and its children.
         * Only open children will be rendered.  
         * @return a JSON as string.
         * @throws Exception 
         */
        public String toJSONTree() throws Exception
        {
            StringBuilder result = new StringBuilder();
            result.append("[");
            List<OwTreeViewNodeJS> children = this.getChildren();
            for (OwTreeViewNodeJS child : children)
            {

                result.append("{text: '");
                result.append(child.getDisplayHTML());
                result.append("',cls: 'folder',id: '");
                result.append(child.getID());
                result.append("',singleClickExpand:");
                result.append(m_expandOnSelect).append(",");
                appendNodeIconLook(result, child);
                if (!child.hasPlusMinusIcon())
                {
                    result.append(", leaf: true");
                }
                else
                {
                    if (!child.isCollapsed())
                    {
                        result.append(", children: ");
                        result.append(child.toJSONTree());
                    }
                }
                result.append(",expanded:").append(!child.isCollapsed()).append("},");
            }

            int lastCharIndex = result.length() - 1;
            if (',' == result.charAt(lastCharIndex))
            {
                result.deleteCharAt(lastCharIndex);
            }

            result.append("]");
            return result.toString();
        }
    }

    /**
     * The URL that resolve the AJAX request when a node is clicked.
     */
    private String m_externalUpdateURL;

    /**
     * Map of URLs to the Alfresco Workdesk components that should be updated after a tree node was clicked. 
     * The key in the map represent the DOM element id of the container to be updated after AJAX call to the
     * corresponding URL
     */
    private Map<String, String> m_componentsUpdateURLs = new LinkedHashMap<String, String>();

    /** flag to use icon class or icon URL     */
    private boolean m_useIconClass = false;
    /**
     * Flag indicating the tree node behavior: if it's value is <code>true</code>, the selected node will be expanded,
     * otherwise the user will need to click on the expanding icon to expand the node.
     */
    private boolean m_expandOnSelect = false;
    /**
     * The current selected node.
     */
    private OwTreeViewNodeJS m_theSelectedNode = null;
    private String dnDAppletURL = null;

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwTreeView#onRender(java.io.Writer)
     */
    protected void onRender(Writer w_p) throws Exception
    {
        getContext().renderJSInclude("/js/owobjecttreeviewextjs.js", w_p);
        renderMainRegion(w_p);
        ((OwMainAppContext) getContext()).addFinalScript(new OwScript("\n if (typeof(window['OwObjectTreeViewEXTJS']) != 'undefined') {OwObjectTreeViewEXTJS.init();}\n", OwScript.DEFAULT_PRIORITY - 10));
    }

    /** 
     * @param w_p Writer to write to
     */
    protected void renderMainRegion(Writer w_p) throws Exception
    {
        // redirect to render page
        serverSideDesignInclude("OwObjectTreeViewEXTJS.jsp", w_p);
    }

    /**
     * Update the server collapsed state for the node.
     * @param request_p
     * @param response_p
     * @throws Exception
     * @since 3.1.0.3
     */
    public synchronized void onAjaxNodeCollapsed(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        String collapsedParam = request_p.getParameter("collapsed");
        String nodeId = request_p.getParameter("owNodeId");
        if (collapsedParam != null && nodeId != null)
        {
            boolean colapsedStatus = Boolean.parseBoolean(collapsedParam);
            OwTreeViewNodeJS node = (OwTreeViewNodeJS) getNodeFromID(nodeId);
            if (node != null)
            {
                if (node.isCollapsed() != colapsedStatus)
                {
                    node.toggleExpand();
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Node <" + node + "> collapsed state: " + collapsedParam);
                    }
                }
            }
        }
    }

    /**
     * Method called when the tree is loading.
     * The tree is loaded asynchronously.
     * When the user expand a node, only the children from the first level are returned.
     * In case that the tree should be refreshed from an external request (e.g - the Browse 
     * plugin is shown after an Edit Properties action),  the selected path is returned, 
     * together with the not expanded siblings of the selected nodes.
     * @param request_p - the AJAX request
     * @param response_p - the response
     * @throws Exception
     */
    public void onAjaxGetTreeData(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        synchronized (this)
        {
            if (m_theSelectedNode == null)
            {
                m_theSelectedNode = (OwTreeViewNodeJS) super.getCurrentNode();
            }
            Writer writer = response_p.getWriter();
            String node = request_p.getParameter("node");
            if (node != null)
            {
                OwTreeViewNodeJS selectedNode = (OwTreeViewNodeJS) m_nodeMap.get(node);
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("onAjaxGetTreeData: " + selectedNode.getDisplayPath());
                }
                String subtreeJSON = selectedNode.toJSONTree();
                writer.write(subtreeJSON);
            }
            writer.close();
        }
    }

    /**
     * Append node icon, or node icon class, depending on the configuration flag.
     * @param result_p
     * @param node_p
     * @throws Exception
     */
    private void appendNodeIconLook(StringBuilder result_p, OwTreeViewNodeJS node_p) throws Exception
    {
        if (m_useIconClass)
        {
            appendNodeIconCls(result_p, node_p);
        }
        else
        {
            appendNodeIcon(result_p, node_p);
            //insert here
            result_p.append(",openedIcon:'");
            result_p.append(node_p.getOpenIconURL());
            result_p.append("'");
            result_p.append(",closedIcon:'");
            result_p.append(node_p.getCloseIconURL());
            result_p.append("'");
        }
    }

    /**
     * Add the icon image to the item.
     * @param buffer_p
     * @param node_p
     * @throws Exception
     */
    private void appendNodeIcon(StringBuilder buffer_p, OwTreeViewNodeJS node_p) throws Exception
    {
        buffer_p.append("icon:'");
        String iconString = null;
        iconString = getNodeIcon(node_p);
        buffer_p.append(iconString);
        buffer_p.append("'");
    }

    /**
     * @param node_p
     * @return the node icon URL
     * @throws Exception
     */
    private String getNodeIcon(OwTreeViewNodeJS node_p) throws Exception
    {
        String iconString;
        OwTreeViewNode currentNode = getCurrentNode();
        if (currentNode != null && node_p.equals(currentNode))
        {
            iconString = node_p.getOpenIconURL();
        }
        else
        {
            iconString = node_p.getCloseIconURL();
        }
        return iconString;
    }

    /** 
     * Write in the given buffer the icon class for node
     * @param buffer_p - the buffer.
     * @param node_p - the node 
     */
    private void appendNodeIconCls(StringBuilder buffer_p, OwTreeViewNodeJS node_p)
    {
        String nodeStyleClass = null;
        nodeStyleClass = getNodeIconClass(node_p);
        buffer_p.append("iconCls: '" + nodeStyleClass + "' ");
    }

    /**
     * Compute node icon class.
     * @param node_p - the node
     * @return the CSS class.
     */
    private String getNodeIconClass(OwTreeViewNodeJS node_p)
    {
        String nodeStyleClass;
        OwTreeViewNode currentNode = getCurrentNode();
        if (currentNode != null && node_p.equals(currentNode))
        {
            nodeStyleClass = FOLDER_OPEN_CSS_CLASS;
        }
        else
        {
            nodeStyleClass = FOLDER_CLOSE_CSS_CLASS;
        }
        return nodeStyleClass;
    }

    /**
     * Get the CSS class for icon of root folder.
     * @return - the CSS class of the root folder
     */
    public String getRootIconClass()
    {
        String result = "";
        if (m_root != null)
        {
            result = getNodeIconClass((OwTreeViewNodeJS) m_root);
        }
        return result;
    }

    /**
     * Get the root displayable text.
     * @return - the display text for root element.
     * @throws Exception
     */
    public String getRootText() throws Exception
    {
        String result = "";
        if (m_root != null)
        {
            result = m_root.getDisplayHTML();
        }
        return result;
    }

    /**
     * Get the id of the root node.
     * @return - the id of the root node.
     */
    public String getRootId()
    {
        return m_root.getID();
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwObjectTreeView#getDisplayHTML(java.lang.Object, com.wewebu.ow.server.ui.OwTreeView.OwTreeViewNode)
     */
    protected String getDisplayHTML(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        try
        {
            // === try to display the child count if possible
            int iChildCount = ((OwObject) obj_p).getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS }, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);

            StringBuilder ret = new StringBuilder();

            ret.append(OwHTMLHelper.encodeToSecureHTML(getDisplayName(obj_p, node_p)));

            ret.append("<span class=&quot;OwObjectTreeViewChildCount&quot;> (");
            ret.append(String.valueOf(iChildCount));
            ret.append(")</span>");

            return ret.toString();
        }
        catch (OwStatusContextException e)
        {
            return OwHTMLHelper.encodeToSecureHTML(getDisplayName(obj_p, node_p));
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwTreeView#createTreeViewNode(com.wewebu.ow.server.ui.OwTreeView, java.lang.Object)
     */
    protected OwTreeViewNode createTreeViewNode(OwTreeView treeView_p, Object obj_p)
    {
        return new OwTreeViewNodeJS(treeView_p, obj_p);
    }

    /**
     * Get the URL that resolve the AJAX request when a node is clicked.
     * @return - the URL.
     */
    public String getExternalUpdateURL()
    {
        return m_externalUpdateURL;
    }

    /**
     * Set the URL that resolve the AJAX request when a node is clicked.
     * @param externalUpdateURL_p - the URL
     */
    public void setExternalUpdateURL(String externalUpdateURL_p)
    {
        m_externalUpdateURL = externalUpdateURL_p;
    }

    /**
     * Get the id of selected node.
     * @return - the id of selected node.
     */
    public String getSelectedId()
    {
        String selectedId = m_root.getID();
        if (getCurrentSelectedNode() != null)
        {
            selectedId = getCurrentSelectedNode().getID();
        }
        return selectedId;
    }

    /**
     * Returns a JavaScript array of IDs in the selected path.
     * @return a {@link String} object representing the JavaScript array of IDs in the selected path.
     */
    public String getSelectedPathIds()
    {
        List<String> selectedPathIds = computeSelectedPathIds();
        Iterator<String> iterator = selectedPathIds.iterator();
        return createJsArray(iterator);
    }

    /**
     * Utility method used to create a JavaScript array from an iterator
     * @param iterator_p - the iterator
     * @return  - a JavaScript prepared array
     */
    private String createJsArray(Iterator<String> iterator_p)
    {
        StringBuilder result = new StringBuilder();
        result.append("[");
        while (iterator_p.hasNext())
        {
            String element = iterator_p.next();
            result.append("'").append(element).append("'");
            if (iterator_p.hasNext())
            {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Compute a {@link List} of IDs that are in the selected path.
     * @return - a {@link List} object containing the IDs in the current selected path. 
     */
    private List<String> computeSelectedPathIds()
    {
        List<String> result = new LinkedList<String>();
        if (getCurrentSelectedNode() != null)
        {
            result.add(getCurrentSelectedNode().getID());
            OwTreeViewNode node = getCurrentSelectedNode();
            while (node.getParent() != null)
            {
                node = node.getParent();
                result.add(node.getID());
            }
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Returns the URL to the root icon.
     * @return the URL to the root icon.
     */
    public String getRootIcon() throws Exception
    {
        String result = "";
        if (m_root != null)
        {
            result = getNodeIcon((OwTreeViewNodeJS) m_root);
        }
        return result;
    }

    /**
     * Add the corresponding update URL for the given element ID. 
     * @param elementId_p - the DOM id for the container to be updated
     * @param updateLink_p - the URL where the update AJAX request will be send. 
     */
    public void addComponentUpdateURL(String elementId_p, String updateLink_p)
    {
        m_componentsUpdateURLs.put(elementId_p, updateLink_p);
    }

    /**
     * Get a prepared JavaScript array with identifiers for HTML elements that need to be updated using an AJAX request.
     * @return - the JavaScript array with identifiers of HTML elements that need to be updated 
     */
    public String getUpdateComponentIds()
    {
        return createJsArray(m_componentsUpdateURLs.keySet().iterator());
    }

    /**
     * Get a prepared JavaScript array the JavaScript array with update URL for HTML elements that need to be updated.
     * @return - the JavaScript array with update URL for HTML elements that need to be updated 
     */
    public String getUpdateComponentsURLs()
    {
        return createJsArray(m_componentsUpdateURLs.values().iterator());
    }

    /**
     * Get the closed icon URL for the root item.
     * @return - the closed icon URL for the root item.
     */
    public String getRootClosedIcon() throws Exception
    {
        String result = "";
        if (m_root != null)
        {
            result = m_root.getCloseIconURL();
        }
        return result;
    }

    /**
     * Get the closed icon URL for the root item.
     * @return - the closed icon URL for the root item.
     */
    public String getRootOpenedIcon() throws Exception
    {
        String result = "";
        if (m_root != null)
        {
            result = m_root.getOpenIconURL();
        }
        return result;
    }

    /**
     * Get the configuration flag that specify if icons in the tree are rendered using the icon CSS class. 
     * @return the configuration flag that specify if icons in the tree are rendered using the icon CSS class.
     */
    public boolean isIconClassInUse()
    {
        return m_useIconClass;
    }

    /**
     * Set the flag that specify if icons in the tree are rendered using the icon CSS class.
     * If the <code>useIconClass_p</code> is <code>true</code>, the designer has the possibility 
     * to configure the icons for folder closed or folder opened via {@link OwObjectTreeViewEXTJS#FOLDER_CLOSE_CSS_CLASS}
     * or via {@link OwObjectTreeViewEXTJS#FOLDER_OPEN_CSS_CLASS} CSS classes.
     * @param useIconClass_p - if <code>true</code> the folder icons are configured via CSS style classes ({@link OwObjectTreeViewEXTJS#FOLDER_CLOSE_CSS_CLASS}
     * or {@link OwObjectTreeViewEXTJS#FOLDER_OPEN_CSS_CLASS}), otherwise the MIME icons are used for folder icons.
     */
    public void setIconClassInUse(boolean useIconClass_p)
    {
        m_useIconClass = useIconClass_p;
    }

    /**
     * Returns the localized loading message.
     * @return - the localized loading message
     */
    public String getLoadingMessage()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.loadingMessage", "Please wait while your request is being processed...");
    }

    /**
     * Returns the localized title for error boxes.
     * @return - the localized title for error boxes.
     */
    public String getErrorTitleText()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.errorTitle", "Error");
    }

    /**
     * Returns the localized part preceding the real error cause for update error.
     * @return - the localized part preceding the real error cause for update error.
     */
    public String getUpdateErrorText()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.updateError", "Update error:");
    }

    /**
     * Returns the localized error message for request timeout.
     * @return -  the localized error message for request timeout.
     */
    public String getRequestTimeOutText()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.requestTimeOut", "Request timeout. Try again or configure a bigger timeout interval for AJAX requests.");
    }

    /**
     * Returns the localized error message when subnodes of a node cannot be loaded.
     * @return - the localized error message when subnodes of a node cannot be loaded.
     */
    public String getCannotLoadNodeText()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.cannotLoadNode", "Could not load the selected node.");
    }

    /**
     * Returns the localized message for showing the stacktrace.
     * @return - the localized message for showing the stacktrace.
     */

    public String getShowStacktraceMessage()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.showStacktrace", "Show details");
    }

    /**
     * Returns the localized message for collapsing the stacktrace.
     * @return - the localized message for collapsing the stacktrace.
     */
    public String getCollapseStacktraceMessage()
    {
        return getContext().localize("app.OwObjectTreeViewEXTJS.collapseStacktrace", "Hide details");
    }

    /**
     * Set the behavior of the tree node when is selected.
     * @param expandOnSelect_p - if <code>true</code> when a code
     */
    public void setExpandOnSelect(boolean expandOnSelect_p)
    {
        m_expandOnSelect = expandOnSelect_p;
    }

    /**
     * Get the tree node behavior when selected.
     * @return - the tree node behavior when selected.
     */
    public boolean isExpandOnSelect()
    {
        return m_expandOnSelect;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwTreeView#navigateToId(java.lang.String)
     */
    @Override
    public synchronized void navigateToId(String nodeId_p) throws Exception
    {
        super.navigateToId(nodeId_p);
        OwTreeViewNode currentNode = getCurrentNode();
        m_theSelectedNode = (OwTreeViewNodeJS) currentNode;
        if (isExpandOnSelect() && currentNode != null)
        {
            currentNode.toggleExpand();
        }
    }

    /**
     * @see com.wewebu.ow.server.ui.OwTreeView#navigate(java.lang.Object, java.lang.String)
     * @since 3.1.0.4
     */
    @Override
    public void navigate(Object obj_p, String strPath_p) throws Exception
    {
        super.navigate(obj_p, strPath_p);
        OwTreeViewNode currentNode = getCurrentNode();
        m_theSelectedNode = (OwTreeViewNodeJS) currentNode;
    }

    /**
     * Get the status of the current node.
     * @return the status of the current node
     */
    public boolean isSelectedNodeExpanded()
    {
        boolean result = false;
        try
        {
            OwTreeViewNode currentNode = getCurrentSelectedNode();
            if (currentNode != null)
            {
                result = !currentNode.isCollapsed();
            }
        }
        catch (Exception e)
        {
            // do nothing
        }
        return result;
    }

    /**
     * Get the current selected node.
     * @return - the current selected node.
     */
    public OwTreeViewNode getCurrentSelectedNode()
    {
        return m_theSelectedNode;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwView#onActivate(int, java.lang.Object)
     */
    @Override
    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        if (m_root != null)
        {
            ((OwTreeViewNodeJS) m_root).refresh();
        }
    }

    public String getDnDAppletURL()
    {
        return this.dnDAppletURL;
    }

    public void setDnDAppletURL(String dnDAppletURL)
    {
        this.dnDAppletURL = dnDAppletURL;
    }
}