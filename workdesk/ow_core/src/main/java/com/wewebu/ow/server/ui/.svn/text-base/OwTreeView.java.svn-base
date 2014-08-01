package com.wewebu.ow.server.ui;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Generic Tree View base class. Implements a template pattern to render
 * arbitrary object tree structures. Just overwrite the getName and
 * getDisplayName functions to render your special object nodes.<br/>
 * <br/>
 * To be implemented for the tree objects.
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
public abstract class OwTreeView extends OwView
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwTreeView.class);

    /** delimiter used to build the path */
    public static final String PATH_DELIMITER = "/";

    // === node class
    public class OwTreeViewNode
    {
        /**
         * construct a tree node
         *
         * @param treeView_p OwTreeView reference
         * @param obj_p Object which is managed by the tree view
         */
        public OwTreeViewNode(OwTreeView treeView_p, Object obj_p)
        {
            m_view = treeView_p;
            m_obj = obj_p;

            m_view.addNode(this);

            m_path = PATH_DELIMITER;
            m_displaypath = PATH_DELIMITER;
        }

        /**
         * 
         * @return Object which is managed by the tree view
         */
        public Object getObject()
        {
            return m_obj;
        }

        /**
         * get the event URL when clicked on a label
         */
        public String getLabelEventURL()
        {
            // check if an external form is set and create a form event,
            // otherwise the tree does not use a form
            if (useFormEventUrls())
            {
                return m_view.getFormEventURL("ClickLabel", NODE_ID_KEY + "=" + getID());
            }
            else
            {
                return m_view.getEventURL("ClickLabel", NODE_ID_KEY + "=" + getID());
            }
        }

        /**
         * get the event URL when clicked on a plus / minus icon
         */
        public String getPlusMinusIconEventURL()
        {
            // check if an external form is set and create a form event,
            // otherwise the tree does not use a form
            if (useFormEventUrls())
            {
                return m_view.getFormEventURL("ClickPlusMinus", NODE_ID_KEY + "=" + getID());
            }
            else
            {
                return m_view.getEventURL("ClickPlusMinus", NODE_ID_KEY + "=" + getID());
            }
        }

        /**
         * gets the Parent repeatedly for iLevel_p counts
         * 
         * @param iLevel_p
         * @return iLevel_p = 0 this node, iLevel_p = 1 parent of this node,
         *         iLevel_p = 2 parent of parent of this node, ...
         */
        public OwTreeViewNode getParentFromDepth(int iLevel_p)
        {
            if (iLevel_p < 1)
            {
                return this;
            }

            if (null == m_parent)
            {
                return null;
            }

            return m_parent.getParentFromDepth(iLevel_p - 1);
        }

        /**
         * get the parent node
         * 
         * @return OwTreeViewNode
         */
        public OwTreeViewNode getParent()
        {
            return m_parent;
        }

        /**
         * get the object of the parent node
         * 
         * @return the object of the parent node or <code>null</code> if none is
         *         available
         */
        public Object getObjectParent()
        {
            OwTreeViewNode theParent = getParent();
            if (theParent != null)
            {
                return theParent.m_obj;
            }
            else
            {
                return null;
            }
        }

        /**
         * get a unique string ID for the node
         */
        public String getID()
        {
            return String.valueOf(hashCode());
        }

        /**
         * get a URL to the close icon
         * 
         * @return String URL to the close icon
         */
        public String getCloseIconURL() throws Exception
        {
            if (m_strCloseFolderURL == null)
            {
                m_strCloseFolderURL = m_view.getCloseIconURL(m_obj, this);
            }

            return m_strCloseFolderURL;
        }

        /**
         * get a URL to the open icon
         * 
         * @return String URL to the close icon
         */
        public String getOpenIconURL() throws Exception
        {
            if (m_strOpenFolderURL == null)
            {
                m_strOpenFolderURL = m_view.getOpenIconURL(m_obj, this);
            }

            return m_strOpenFolderURL;
        }

        /**
         * Returns the text to be displayed as the name of the node.<br>
         * This text will be properly encoded before used.
         * 
         * @return the text to be displayed as the name of the node.
         */
        public String getDisplayName() throws Exception
        {
            return m_view.getDisplayName(m_obj, this);
        }

        /**
         * Returns HTML code to be used to display a node.<br>
         * If you override this method, you have to make sure that the HTML code
         * is properly encoded!
         * 
         * @return HTML code to be used to display a node
         * 
         * @since 2.5.3.0
         */
        public String getDisplayHTML() throws Exception
        {
            return m_view.getDisplayHTML(m_obj, this);
        }

        /**
         * check if node is enabled, i.e. can open the folder
         * 
         * @return true = enabled, false = disabled
         */
        public boolean isEnabled() throws Exception
        {
            return m_view.isEnabled(m_obj, this);
        }

        /**
         * get the name for an object, default returns getName
         * 
         * @return the display name for an object
         */
        public String getName() throws Exception
        {
            return m_view.getName(m_obj, this);
        }

        /**
         * check of node has subnodes
         * 
         * @return true if plus minus icon should be displayed to open subnodes
         */
        public boolean hasPlusMinusIcon() throws Exception
        {
            return m_view.hasPlusMinusIcon(m_obj, this);
        }

        /**
         * expand / collapse the node
         */
        public void toggleExpand() throws Exception
        {
            if (m_fCollapsed && (!m_fChildsCreated))
            {
                // === node should be expanded, created the children if not
                // already done
                createChilds();

                m_fChildsCreated = true;
            }

            // toggle flag
            m_fCollapsed = !m_fCollapsed;
        }

        public void expand() throws Exception
        {
            m_fCollapsed = false;
            createChilds();
            m_fChildsCreated = true;
        }

        /**
         * create the children or this node
         */
        protected void createChilds() throws Exception
        {
            m_childs.clear();

            // create the child objects
            List childObjects = m_view.createChildObjects(m_obj);
            if (childObjects != null)
            {
                for (int i = 0; i < childObjects.size(); i++)
                {
                    // create new child node
                    OwTreeViewNode newChild = m_view.createTreeViewNode(m_view, childObjects.get(i));

                    // set parent
                    newChild.m_parent = this;

                    // set symbol path and display path
                    newChild.m_path = m_path + newChild.getName() + PATH_DELIMITER;
                    newChild.m_displaypath = m_displaypath + newChild.getDisplayName() + PATH_DELIMITER;

                    // set siblings flags
                    if (i < (childObjects.size() - 1))
                    {
                        newChild.m_fLastSibling = false;
                    }
                    else
                    {
                        newChild.m_fLastSibling = true;
                    }

                    if (i == 0)
                    {
                        newChild.m_fFirstSibling = true;
                    }
                    else
                    {
                        newChild.m_fFirstSibling = false;
                    }

                    // add to list
                    m_childs.add(newChild);
                }
            }
            // don't show plus minus icon if we don't got any children
            if (m_childs.size() == 0)
            {
                m_fPlusMinusIcon = false;
            }
            else
            {
                m_fPlusMinusIcon = true;
            }
        }

        /** force node to reload its child nodes */
        protected void refresh() throws Exception
        {
            if (m_fChildsCreated)
            {
                // recreate again
                createChilds();
            }
        }

        /**
         * navigate to the sub node by the given path
         * 
         * @param pathtokens_p
         *            StringTokenizer tokenized path
         * @return OwTreeViewNode the navigated node
         */
        protected OwTreeViewNode navigate(StringTokenizer pathtokens_p) throws Exception
        {
            if (!pathtokens_p.hasMoreTokens())
            {
                return this;
            }

            // expand node
            if (m_fCollapsed)
            {
                toggleExpand();
            }

            // find child that matches
            String strToken = pathtokens_p.nextToken();
            for (int i = 0; i < m_childs.size(); i++)
            {
                OwTreeViewNode node = (OwTreeViewNode) m_childs.get(i);

                String strName = node.getName();
                if (strName.equals(strToken))
                {
                    return node.navigate(pathtokens_p);
                }
            }

            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object otherObject_p)
        {
            boolean result = false;
            if (otherObject_p instanceof OwTreeViewNode)
            {
                OwTreeViewNode theNode = (OwTreeViewNode) otherObject_p;
                if (this.m_obj != null)
                {
                    result = this.m_obj.equals(theNode.m_obj);
                    if (result && m_parent != null)
                    {
                        result = m_parent.equals(theNode.m_parent);
                    }
                    //compare to see if they have the same path
                    if (result)
                    {
                        if (this.m_path != null)
                        {
                            if (theNode.m_path != null)
                            {
                                result = this.m_path.equals(theNode.m_path);
                            }
                        }
                    }
                }
                else
                {
                    result = super.equals(otherObject_p);
                }
            }
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            int result = super.hashCode();
            if (m_obj != null)
            {
                result = m_obj.hashCode();
            }
            return result;
        }

        /** reference to the underlying view */
        protected OwTreeView m_view;

        /** flag indicating if this is the last sibling */
        protected boolean m_fLastSibling = true;

        /** flag indicating if this is the first sibling */
        protected boolean m_fFirstSibling = true;

        /** path to the node */
        protected String m_path;
        protected String m_displaypath;

        /** flag indicating if the children have been created already */
        protected boolean m_fChildsCreated = false;

        /** flag indicating if a plus minus icon should be drawn, initially true */
        protected boolean m_fPlusMinusIcon = true;

        /** URL to the open folder icon */
        protected String m_strOpenFolderURL;
        /** URL to the close folder icon */
        protected String m_strCloseFolderURL;

        /** parent object to the node */
        protected OwTreeViewNode m_parent;

        /** flag indicating if node is collapsed, initially true */
        protected boolean m_fCollapsed = true;

        /** child nodes */
        protected List m_childs = new ArrayList();

        /** object that is managed by the node */
        protected Object m_obj;

        /**
         * get the displayed path
         * 
         */
        public String getDisplayPath()
        {
            return m_displaypath;
        }

        /**
         * get the path
         * 
         */
        public String getPath()
        {
            return m_path;
        }

        /**
         * 
         * @return <code>true</code>if node is collapsed , <code>false</code>
         *         otherwise
         */
        public boolean isCollapsed()
        {
            return m_fCollapsed;
        }

        /**
         * Returns the flag indicating if this is the last sibling
         * 
         * @return the flag indicating if this is the last sibling
         * 
         * @since 2.5.3.0
         */
        public boolean isLastSibling()
        {
            return m_fLastSibling;
        }

        /**
         * Returns the flag indicating if this is the first sibling
         * 
         * @return the flag indicating if this is the first sibling
         * 
         * @since 2.5.3.0
         */
        public boolean isFirstSibling()
        {
            return m_fFirstSibling;
        }

    };

    // === event listener
    /**
     *<p>
     * Base class for event listeners to OwTreeView.
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
    public interface OwTreeViewEventListner extends java.util.EventListener
    {
        /**
         * event called when a user opens a folder to display its document
         * contents
         * 
         * @param node_p
         *            the node which triggered the event - the opened folder
         *            node
         * @throws Exception
         */
        public void onTreeViewOpenFolder(OwTreeViewNode node_p) throws Exception;

        /**
         * event called when a user navigates through the tree (using plus minus
         * icon), but does not open a folder
         * 
         * @param node_p
         *            the node which triggered the event - the node which is
         *            opened (+) or closed (-)
         * @throws Exception
         */
        public void onTreeViewNavigateFolder(OwTreeViewNode node_p) throws Exception;
    };

    /** eventlistener for the tree view */
    protected OwTreeViewEventListner m_eventlistner;

    /** URL to the design dir */
    protected String m_strDesignURL;

    /** root node */
    protected OwTreeViewNode m_root;

    /** currently selected node of null if nothing is selected */
    protected OwTreeViewNode m_selectedNode;

    /** currently focus node of null if nothing is focused */
    protected OwTreeViewNode m_focusNode;

    /** current navigated path */
    protected String m_strPath = PATH_DELIMITER;
    /**
     * The children sort criteria. Can be null.
     * @since 3.2.0.0
     */
    protected OwSort childrenSort = null;

    // === publics

    /**
     * use form event url's instead of get method's used to submit a neighbor
     * form
     */
    protected boolean useFormEventUrls()
    {
        return isFormTargetExternal();
    }

    /**
     * refresh the current node, i.e. recreate the children
     */
    public void refreshCurrentNode() throws Exception
    {
        if (null != m_selectedNode)
        {
            m_selectedNode.refresh();
        }
    }

    /**
     * get the currently selected node
     * 
     * @return an {@link OwTreeViewNode}
     */
    public OwTreeViewNode getCurrentNode()
    {
        return m_selectedNode;
    }

    /**
     * navigate up to the parent
     * 
     * @throws Exception
     */
    public void navigateUp() throws Exception
    {
        OwTreeViewNode parent = this.m_selectedNode.getParent();
        if (parent != null)
        {
            openNode(parent);
        }
    }

    /**
     * navigate to the specified path NOTE: the tree must have already been
     * initialized with the two parameter form of navigate
     * 
     * @param strPath_p String representing a path
     */
    public void navigate(String strPath_p) throws Exception
    {
        if ((m_root == null) || (m_root.m_obj == null))
        {
            throw new OwInvalidOperationException("OwTreeView.navigate: Initialize tree first with the two parameter form of navigate.");
        }

        navigate(m_root.m_obj, strPath_p);
    }

    /**
     * Navigate to the specific node id
     * 
     * @param nodeId_p String representing id of the node
     * @throws Exception
     * @since 3.1.0.0
     */
    public void navigateToId(String nodeId_p) throws Exception
    {
        if (nodeId_p != null && m_nodeMap.containsKey(nodeId_p))
        {
            OwTreeViewNode node = (OwTreeViewNode) m_nodeMap.get(nodeId_p);
            navigate(node.getPath());
        }
    }

    /**
     * check if tree has been initialized with objects and can be browsed
     * 
     * @return boolean
     */
    public boolean canNavigate()
    {
        return ((m_root != null) && (m_root.m_obj != null));
    }

    /**
     * init the tree and set the root object
     * 
     * @param obj_p Root Object to be managed by the tree
     * @param strPath_p String representing path
     */
    public void navigate(Object obj_p, String strPath_p) throws Exception
    {
        if (obj_p == null)
        {
            m_root = null;
            m_selectedNode = null;
            m_strPath = PATH_DELIMITER;
            return;
        }

        if (strPath_p == null)
        {
            strPath_p = PATH_DELIMITER;
        }

        if ((m_root == null) || (m_root.m_obj != obj_p))
        {
            // === new root object has been assigned
            // create new map
            m_nodeMap = new HashMap();

            // create new root node
            m_root = createTreeViewNode(this, obj_p);

            // send event to eventlistener
            if (m_eventlistner != null)
            {
                m_eventlistner.onTreeViewNavigateFolder(m_root);
            }

            m_root.toggleExpand();
        }

        if (!m_strPath.equals(strPath_p))
        {
            // === path changed
            OwTreeViewNode n = m_root.navigate(new StringTokenizer(strPath_p, PATH_DELIMITER));
            if (n != null)
            {
                openNode(n);
            }
            else
            {
                m_selectedNode = m_root;
            }
        }
        else
        {
            openNode(m_root);
        }
    }

    /**
     * check if tree view contains any node
     * 
     * @return true = treeview is empty, false = otherwise
     */
    public boolean isEmpty()
    {
        return (m_root == null);
    }

    /**
     * set an eventlistener for the tree view
     * 
     * @param listner_p OwTreeView.OwTreeViewEventListner
     */
    public void setEventListner(OwTreeView.OwTreeViewEventListner listner_p)
    {
        // set listener
        m_eventlistner = listner_p;
    }

    /**
     * called when the view should create its HTML content to be displayed
     * 
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        if (m_root != null)
        {
            // render tree
            renderTree(w_p, m_root);
        }
    }

    // === Overridable
    /**
     * Create a list of child objects to the given object.<br />
     * Must be implemented by derived classes.
     * 
     * @param obj_p Object which is managed by the tree view
     * @return List of Objects which are child to the given object
     */
    protected abstract List createChildObjects(Object obj_p) throws Exception;

    /**
     * Get an URL for the close icon.<br /> 
     * Should be implemented by the derived class.
     * 
     * @param obj_p Object which is managed by the tree view
     * @param node_p OwTreeViewNode
     * @return String URL to the close icon
     */
    protected String getCloseIconURL(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        return m_strDesignURL + "/images/OwTreeView/folder_closed.png'";
    }

    /**
     * get a URL to the open icon
     * 
     * to be implemented by the derived class
     * @param obj_p Object which is managed by the tree view
     * @param node_p OwTreeViewNode
     * @return String URL to the close icon
     */
    protected String getOpenIconURL(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        return m_strDesignURL + "/images/OwTreeView/folder_open.png'";
    }

    /**
     * Returns the text to be displayed as the name of the node.<br />
     * This text will be properly encoded before used.
     * 
     * @param obj_p Object which is managed by the tree view
     * @param node_p the node to return the text for
     * 
     * @return the text to be displayed as the name of the node.
     */
    protected String getDisplayName(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        return getName(obj_p, node_p);
    }

    /**
     * Returns HTML code to be used to display a node.<br>
     * If you override this method, you have to make sure that the HTML code is
     * properly encoded!
     * 
     * @param obj_p Object which is managed by the tree view
     * @param node_p the node to return the text for
     * 
     * @return HTML code to be used to display a node
     * 
     * @since 2.5.3.0
     */
    protected String getDisplayHTML(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        return OwHTMLHelper.encodeToSecureHTML(getDisplayName(obj_p, node_p));
    }

    /**
     * check if node is enabled, i.e. can open the folder
     * 
     * to be implemented by the derived class
     * 
     * @param obj_p Object which is managed by the tree view
     * @return true = enabled, false = disabled
     */
    public boolean isEnabled(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        return true;
    }

    /**
     * get the name for an object, used to build paths
     * 
     * to be implemented by the derived class
     * 
     * @param obj_p Object which is managed by the tree view
     * @return the name for an object
     */
    protected abstract String getName(Object obj_p, OwTreeViewNode node_p) throws Exception;

    /**
     * (overridable) check of node has subnodes to be overwritten by the derived
     * class
     * 
     * @param obj_p Object which is managed by the tree view
     * @return true if plus minus icon should be displayed to open subnodes
     */
    protected boolean hasPlusMinusIcon(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        // default implementation
        return node_p.m_fPlusMinusIcon;
    }

    /**
     * overridable factory method check of node has subnodes to be overwritten
     * by the derived class
     * 
     * @param treeView_p OwTreeView
     * @param obj_p template Object which is managed by the tree node
     * 
     */
    protected OwTreeViewNode createTreeViewNode(OwTreeView treeView_p, Object obj_p)
    {
        return new OwTreeViewNode(treeView_p, obj_p);
    }

    // === implementation
    protected static final String NODE_ID_KEY = "node";

    /**
     * init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // set design URL
        m_strDesignURL = getContext().getDesignURL();
    }

    /** class to register key events for the found nodes next to the selected */
    private class OwTreeViewFindKeyEventNodes
    {
        // === members to find the before and after selected node
        public OwTreeViewNode m_nodeBeforeSelected = null;
        public OwTreeViewNode m_nodeAfterSelected = null;
        public OwTreeViewNode m_oldnode = null;
        public boolean m_fPassedSelectedNode = false;

        /**
         * check this node out for next and previous of the selected
         */
        public void checkNode(OwTreeViewNode node_p) throws Exception
        {
            // === find before and after selected nodes
            if (m_fPassedSelectedNode && (m_nodeAfterSelected == null))
            {
                m_nodeAfterSelected = node_p;
            }

            if (isSelected(node_p))
            {
                // === save
                m_fPassedSelectedNode = true;
                m_nodeBeforeSelected = m_oldnode;

                // === plus / minus icon key event
                getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_RIGHT, OwAppContext.KEYBOARD_CTRLKEY_ALT, node_p.getPlusMinusIconEventURL(), node_p.getDisplayName());
                getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_LEFT, OwAppContext.KEYBOARD_CTRLKEY_ALT, node_p.getPlusMinusIconEventURL(), node_p.getDisplayName());
            }

            m_oldnode = node_p;
        }

        /**
         * register key events for the found nodes next to the selected
         */
        public void registerKeyEvents() throws Exception
        {
            if (m_nodeAfterSelected != null)
            {
                getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_DN, OwAppContext.KEYBOARD_CTRLKEY_ALT, m_nodeAfterSelected.getLabelEventURL(), m_nodeAfterSelected.getDisplayName());
            }

            if (m_nodeBeforeSelected != null)
            {
                getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_UP, OwAppContext.KEYBOARD_CTRLKEY_ALT, m_nodeBeforeSelected.getLabelEventURL(), m_nodeBeforeSelected.getDisplayName());
            }
        }
    }

    /** class to register key events for the found nodes next to the selected */
    private OwTreeViewFindKeyEventNodes m_finder = null;

    /** display mode for rendering of the treenodes (default) */
    public static final int DISPLAY_MODE_TREE_NODES = 0x001;
    /** display mode for rendering of the treenodes */
    public static final int DISPLAY_MODE_REGISTER_NODES = 0x002;

    /** display mode for rendering of the treenodes */
    private int m_displaymode = DISPLAY_MODE_TREE_NODES;

    /**
     * set the display mode of the view
     * 
     * @param iMode_p int mode as defined in DISPLAY_MODE_...
     */
    public void setDisplayMode(int iMode_p)
    {
        m_displaymode = iMode_p;
    }

    /**
     * get the display mode of the view
     * 
     * @return int mode as defined in DISPLAY_MODE_...
     * */
    protected int getDisplayMode()
    {
        return m_displaymode;
    }

    /**
     * render the tree recursively.
     * 
     * @param w_p Writer Object
     * @param node_p OwTreeNode Node to render
     */
    private void renderTree(Writer w_p, OwTreeViewNode node_p) throws Exception
    {
        m_finder = new OwTreeViewFindKeyEventNodes();

        switch (getDisplayMode())
        {
            case DISPLAY_MODE_TREE_NODES:
                // render tree
                renderTreeNodesMode(w_p, node_p, 1);
                break;

            case DISPLAY_MODE_REGISTER_NODES:
                renderRegisterNodesMode(w_p, node_p, 1);
                break;

            default:
                throw new OwInvalidOperationException("OwTreeView.renderTree: Invalid displaymode in OwTreeView, displayMode = " + String.valueOf(getDisplayMode()));
        }

        // === register key events
        m_finder.registerKeyEvents();
        m_finder = null; // not needed before next render
    }

    /**
     * render the tree recursively with register display mode
     * 
     * @param w_p Writer Object
     * @param node_p OwTreeNode Node to render
     * @param depth_p iterator depth (ignored, for future use)
     */
    private void renderRegisterNodesMode(Writer w_p, OwTreeViewNode node_p, int depth_p) throws Exception
    {
        // render the children
        Iterator it = node_p.m_childs.iterator();

        w_p.write("<div class='" + getRegisterItemStyle() + "'>\n");

        while (it.hasNext())
        {
            OwTreeViewNode childNode = (OwTreeViewNode) it.next();
            renderRegister(w_p, childNode, depth_p);

        }

        w_p.write("</div>\n");
    }

    /**
     * render the tree recursively with tree display mode
     * 
     * @param w_p Writer Object
     * @param node_p OwTreeNode Node to render
     * @param depth_p integer iterator depth
     */
    private void renderTreeNodesMode(Writer w_p, OwTreeViewNode node_p, int depth_p) throws Exception
    {
        // === check for key events
        m_finder.checkNode(node_p);

        // === render node and children
        renderNode(w_p, node_p, depth_p);

        // if the node is collapsed and is not root node, don't show children
        if (node_p.m_fCollapsed && (depth_p > 1))
        {
            return;
        }
        // render the children
        Iterator it = node_p.m_childs.iterator();

        while (it.hasNext())
        {
            OwTreeViewNode childNode = (OwTreeViewNode) it.next();

            renderTreeNodesMode(w_p, childNode, depth_p + 1);
        }
    }

    /**
     * render a treenode as register
     * 
     * @param writer_p Writer Object
     * @param node_p OwTreeNode Node to render
     * @param depth_p iterator depth
     */
    public void renderRegister(Writer writer_p, OwTreeViewNode node_p, int depth_p) throws Exception
    {

        boolean fSelected = isSelected(node_p);
        String folderLabelLink = node_p.getLabelEventURL();

        if (fSelected)
        {
            writer_p.write("<span class='" + getSelectedRegisterItemStyle() + "'>");
        }
        else
        {
            writer_p.write("<span class='" + getRegisterItemStyle() + "'>");
        }

        // display folder open link
        if (fSelected)
        {
            writer_p.write("<a href=\"" + folderLabelLink + "\" class='" + getSelectedRegisterItemStyle() + "'>");
        }
        else
        {
            writer_p.write("<a href=\"" + folderLabelLink + "\" class='" + getRegisterItemStyle() + "'>");
        }

        String displayName = addNonBreakingSpaces(node_p.getDisplayName());

        writer_p.write(displayName);

        writer_p.write("</a></span><span width='0' style='font-size:0px;'>\n</span>");
    }

    /**
     * @param displayName_p
     * @return a {@link String}
     */
    private String addNonBreakingSpaces(String displayName_p)
    {
        if (displayName_p.indexOf(" ") < 0)
        {
            return displayName_p;
        }
        StringTokenizer token = new StringTokenizer(displayName_p);
        StringBuffer sbuffer = new StringBuffer(token.nextToken());
        while (token.hasMoreTokens())
        {
            sbuffer.append("&nbsp;");
            sbuffer.append(token.nextToken());
        }
        return sbuffer.toString();
    }

    /**
     * get the style for the selected tree item
     */
    protected String getSelectedTreeItemStyle()
    {
        return "OwTreeViewTextSelected";
    }

    /**
     * get the style for the tree item
     */
    protected String getTreeItemStyle()
    {
        return "OwTreeViewText";
    }

    /**
     * get the style for the selected tree item
     */
    protected String getSelectedRegisterItemStyle()
    {
        return "OwRegisterViewTextSelected";
    }

    /**
     * get the style for the tree item
     */
    protected String getRegisterItemStyle()
    {
        return "OwRegisterViewText";
    }

    /** check if given node is selected
     * @param node_p OwTreeViewNode 
     * @return true if node is eqauls selected node, else false*/
    public boolean isSelected(OwTreeViewNode node_p)
    {
        boolean result = false;
        if (m_selectedNode != null)
        {
            result = m_selectedNode.equals(node_p);
        }
        return result;
    }

    /** check if given node is focused 
     * @param node_p OwTreeViewNode to check
     * @return true if given node is focused, else false*/
    public boolean isFocused(OwTreeViewNode node_p)
    {
        return (m_focusNode == node_p);
    }

    /**
     * render a treenode as node
     * 
     * @param writer_p Writer Object
     * @param node_p OwTreeNode Node to render
     * @param depth_p int iterator depth
     */
    public void renderNode(Writer writer_p, OwTreeViewNode node_p, int depth_p) throws Exception
    {
        OwTreeViewNode node = node_p;

        boolean fSelected = isSelected(node_p);
        // boolean fFocused = isFocused(node_p);

        boolean fRoot = (depth_p == 1);
        try
        {
            StringWriter nodeBuffer = new StringWriter(30000);
            // === sub nodes
            nodeBuffer.write("\n<table id='OwTreeViewNode_");
            nodeBuffer.write(String.valueOf(node.hashCode()));
            nodeBuffer.write("'><tr>\n");

            // === Draw spacer
            for (int i = 2; i < depth_p; i++)
            {

                OwTreeViewNode LevelParent = node.getParentFromDepth(depth_p - i);

                if ((null != LevelParent) && (!LevelParent.m_fLastSibling))
                {
                    // ==== further siblings to come, display a line
                    nodeBuffer.write("\n<td><img class='OwTreeViewImg' src='" + m_strDesignURL + "/images/OwTreeView/vline.gif' alt=''></td>");
                }
                else
                {
                    // === display invisible spacer
                    nodeBuffer.write("\n<td><img class='OwTreeViewImg' src='" + m_strDesignURL + "/images/OwTreeView/nix.gif' alt=''></td>");
                }
            }

            // === Draw plus minus icon
            if (!fRoot)
            {
                nodeBuffer.write("\n<td>");

                if (node.hasPlusMinusIcon())
                {
                    String nodeName = node.getName();
                    if (nodeName == null || nodeName.length() == 0)
                    {
                        throw new OwInvalidOperationException("Invalid or zerolength node name !");
                    }

                    if (!node.isCollapsed())
                    {
                        renderMinusIconLink(nodeBuffer, node);
                    }
                    else
                    {
                        renderPlusIconLink(nodeBuffer, node);
                    }
                }
                else
                {
                    // === No subnodes expected, need no icon, just an edge.
                    if (!node.isLastSibling())
                    {
                        nodeBuffer.write("<img class='OwTreeViewImg' src='" + m_strDesignURL + "/images/OwTreeView/edge_cross.gif' alt=''/>");
                    }
                    else
                    {
                        nodeBuffer.write("<img class='OwTreeViewImg' src='" + m_strDesignURL + "/images/OwTreeView/edge.gif' alt=''/>");
                    }

                }

                nodeBuffer.write("</td>");
            }

            // === Draw folder icon
            nodeBuffer.write("\n<td>");

            /*
             * if ( fFocused ) { w.write("<a href=\"#\" name=\"");
             * w.write(OwAppContext.SELECTED_ANCHOR); w.write("\"></a>"); }
             */

            renderFolderIconLink(nodeBuffer, node_p);

            nodeBuffer.write("</td>");

            // === draw label
            if (fSelected)
            {
                nodeBuffer.write("\n<td class=\"" + getSelectedTreeItemStyle() + "\">");
            }
            else
            {
                nodeBuffer.write("\n<td class=\"" + getTreeItemStyle() + "\">");
            }

            renderNodeLink(nodeBuffer, node_p);

            nodeBuffer.write("</td>");

            nodeBuffer.write("\n</tr></table>\n");

            writer_p.write(nodeBuffer.getBuffer().toString());
        }
        catch (Exception e)
        {
            LOG.error("Could not render tree node !", e);
        }
    }

    /**
     * Writes the link and icon for collapsing a sub tree (a.k.a. minus icon) to a
     * StringWriter.
     * 
     * @param nodeBuffer_p StringWriter to write the link and icon to
     * @param node_p OwTreeViewNode to write the collapse link for
     * @throws Exception
     * @since 2.5.3.0
     */
    protected void renderMinusIconLink(StringWriter nodeBuffer_p, OwTreeViewNode node_p) throws Exception
    {
        // Minus Icon
        nodeBuffer_p.write("<a id=\"OwTreeView_Minus_");
        nodeBuffer_p.write(node_p.getName());
        nodeBuffer_p.write("\" href=\"");
        nodeBuffer_p.write(node_p.getPlusMinusIconEventURL());
        nodeBuffer_p.write("\">");

        String nodeName = node_p.getDisplayName();
        String tooltip = getContext().localize1("app.OwTreeView.collapseitem", "Collapse %1", nodeName);
        if (!node_p.isLastSibling())
        {
            nodeBuffer_p.write("<img class=\"OwTreeViewImg\" src=\"");
            nodeBuffer_p.write(m_strDesignURL);
            nodeBuffer_p.write("/images/OwTreeView/box_open_cross.gif\"");
            writeTooltip(nodeBuffer_p, tooltip);
            nodeBuffer_p.write("/>");

        }
        else
        {
            nodeBuffer_p.write("<img class=\"OwTreeViewImg\" src=\"");
            nodeBuffer_p.write(m_strDesignURL);
            nodeBuffer_p.write("/images/OwTreeView/box_open.gif\"");
            writeTooltip(nodeBuffer_p, tooltip);
            nodeBuffer_p.write("/>");
        }

        nodeBuffer_p.write("</a>");
    }

    /**
     * Utility method for writing tooltip.
     * 
     * @param nodeBuffer_p {@link StringWriter} object
     * @param tooltip_p String text to use as the tooltip
     * 
     * @since 3.0.0.0
     */
    protected void writeTooltip(StringWriter nodeBuffer_p, String tooltip_p)
    {
        nodeBuffer_p.write(" alt=\"");
        nodeBuffer_p.write(tooltip_p);
        nodeBuffer_p.write("\" title=\"");
        nodeBuffer_p.write(tooltip_p);
        nodeBuffer_p.write("\" ");
    }

    /**
     * Writes the link and icon for expanding a sub tree (a.k.a. plus icon) to a
     * StringWriter.
     * 
     * @param nodeBuffer_p StringWriter to write the link and icon to
     * @param node_p OwTreeViewNode to write the expand link for
     * 
     * @throws Exception
     * 
     * @since 2.5.3.0
     */
    protected void renderPlusIconLink(StringWriter nodeBuffer_p, OwTreeViewNode node_p) throws Exception
    {
        // Plus Icon
        nodeBuffer_p.write("<a id=\"OwTreeView_Plus_");
        nodeBuffer_p.write(node_p.getName());
        nodeBuffer_p.write("\" href=\"");
        nodeBuffer_p.write(node_p.getPlusMinusIconEventURL());
        nodeBuffer_p.write("\">");

        String nodeName = node_p.getDisplayName();
        String tooltip = getContext().localize1("app.OwTreeView.expanditem", "Expand %1", nodeName);

        if (!node_p.isLastSibling())
        {
            nodeBuffer_p.write("<img class=\"OwTreeViewImg\" src=\"");
            nodeBuffer_p.write(m_strDesignURL);
            nodeBuffer_p.write("/images/OwTreeView/box_closed_cross.gif\"");
            writeTooltip(nodeBuffer_p, tooltip);
            nodeBuffer_p.write("/>");
        }
        else
        {
            nodeBuffer_p.write("<img class=\"OwTreeViewImg\" src=\"");
            nodeBuffer_p.write(m_strDesignURL);
            nodeBuffer_p.write("/images/OwTreeView/box_closed.gif\"");
            writeTooltip(nodeBuffer_p, tooltip);
            nodeBuffer_p.write("/>");
        }

        nodeBuffer_p.write("</a>");
    }

    /**
     * Writes the link and icon for a folder tree node to a StringWriter
     * 
     * @param nodeBuffer_p StringWriter to write the link and icon to
     * @param node_p OwTreeViewNode to write the link for
     * 
     * @throws Exception
     * 
     * @since 2.5.3.0
     */
    protected void renderFolderIconLink(StringWriter nodeBuffer_p, OwTreeViewNode node_p) throws Exception
    {
        // Folder Icon
        if (node_p.isEnabled())
        {
            nodeBuffer_p.write("<a href=\"");
            nodeBuffer_p.write(node_p.getLabelEventURL());
            nodeBuffer_p.write("\">");
        }

        nodeBuffer_p.write("<img class=\"OwTreeViewFolderImg\" src=\"");
        nodeBuffer_p.write((isSelected(node_p) ? node_p.getOpenIconURL() : node_p.getCloseIconURL()));
        nodeBuffer_p.write("\"");
        String tooltip = "";
        if (node_p.isEnabled())
        {
            tooltip = isSelected(node_p) ? getContext().localize1("app.OwTreeView.itemstatusexpanded", "The item %1 is opened", node_p.getDisplayName()) : getContext().localize1("app.OwTreeView.itemstatuscollapsed", "The item %1 is closed",
                    node_p.getDisplayName());
        }
        writeTooltip(nodeBuffer_p, tooltip);
        nodeBuffer_p.write(" />");
        if (node_p.isEnabled())
        {
            nodeBuffer_p.write("</a>");
        }
    }

    /**
     * Writes the link for a tree node to a StringWriter
     * 
     * @param nodeBuffer_p the StringWriter to write the link and icon to
     * @param node_p the the OwTreeViewNode to write the link for
     * 
     * @throws Exception
     * 
     * @since 2.5.3.0
     */
    protected void renderNodeLink(StringWriter nodeBuffer_p, OwTreeViewNode node_p) throws Exception
    {
        if (node_p.isEnabled())
        {
            nodeBuffer_p.write("<a href=\"");
            nodeBuffer_p.write(node_p.getLabelEventURL());
            nodeBuffer_p.write("\" class=\"");
            nodeBuffer_p.write((isSelected(node_p) ? getSelectedTreeItemStyle() : getTreeItemStyle()));
            nodeBuffer_p.write("\">");
        }

        nodeBuffer_p.write("&nbsp;");
        nodeBuffer_p.write(node_p.getDisplayHTML());

        if (node_p.isEnabled())
        {
            nodeBuffer_p.write("</a>");
        }
    }

    /** map containing all nodes mapped by their ID */
    protected Map m_nodeMap;

    /**
     * get node from node ID
     * 
     * @param strID_p String node ID from OwTreeViewNode.getID()
     * @return OwTreeViewNode for given ID
     */
    protected OwTreeViewNode getNodeFromID(String strID_p)
    {
        return (OwTreeViewNode) m_nodeMap.get(strID_p);
    }

    /**
     * add a node to the node map for fast access
     * 
     * @param node_p OwTreeViewNode to add
     */
    public void addNode(OwTreeViewNode node_p)
    {
        m_nodeMap.put(node_p.getID(), node_p);
    }

    /**
     * handles the navigate event i.e. a user has clicked a tree icon
     * 
     * @param request_p
     * @throws Exception
     */
    public void onClickPlusMinus(HttpServletRequest request_p) throws Exception
    {
        // === get clicked node
        String strId = request_p.getParameter(NODE_ID_KEY);
        OwTreeViewNode selectedNode = getNodeFromID(strId);

        m_focusNode = selectedNode;

        selectedNode.toggleExpand();

        // send event to eventlistener
        if (!selectedNode.m_fCollapsed)
        {
            if (m_eventlistner != null)
            {
                m_eventlistner.onTreeViewNavigateFolder(selectedNode);
            }

            // Ticket 1186: select the folder the user has clicked on the
            // plus/minus icon
            openNode(selectedNode);
        }
    }

    /**
     * handles the navigate event i.e. a user has clicked a tree icon
     * 
     * @param request_p
     * @throws Exception
     */
    public void onClickLabel(HttpServletRequest request_p) throws Exception
    {
        // === get clicked node
        String strId = request_p.getParameter(NODE_ID_KEY);

        OwTreeViewNode n = getNodeFromID(strId);

        m_focusNode = n;

        openNode(n);
    }

    /**
     * open the node content
     * 
     * @param node_p OwTreeViewNode whose content is to be shown
     */
    public void openNode(OwTreeViewNode node_p) throws Exception
    {
        if (!node_p.isEnabled())
        {
            return;
        }
        OwTreeViewNode previousNode = m_selectedNode;
        m_selectedNode = node_p;

        if (m_eventlistner != null)
        {
            try
            {
                m_eventlistner.onTreeViewOpenFolder(node_p);
            }
            catch (Exception e)
            {
                m_selectedNode = previousNode;
                LOG.error("OwTreeView.openNode(): Error trying to opening the selected node.", e);
                throw e;
            }
        }
    }

    /**
     * Get the id used for client side scripts for calculate the height of the
     * tree.
     * 
     * @return the client side id.
     */
    public String getClientSideId()
    {
        return "OwTreeView_" + hashCode();
    }

    /**
     * Navigate to an object that is already in the tree view.
     * @param theObject_p
     * @throws Exception
     * @since 3.1.0.0
     */
    public void navigateToChild(Object theObject_p) throws Exception
    {
        OwTreeViewNode correspondingNode = getNodeForObject(theObject_p);
        if (correspondingNode == null)
        {
            expandParents(theObject_p);
            correspondingNode = getNodeForObject(theObject_p);
        }
        if (correspondingNode != null)
        {
            if (m_selectedNode != null)
            {
                m_selectedNode.refresh();
            }

            m_focusNode = correspondingNode;

            OwTreeViewNode parent = correspondingNode.getParent();
            while (parent != null && parent.isCollapsed())
            {
                parent.expand();
                parent = parent.getParent();
            }

            correspondingNode.expand();
            String path = correspondingNode.getPath();
            if (path != null)
            {
                navigate(path);
            }

            //openNode(correspondingNode);

        }
        else
        {//Node not found, clear selection
            clearSelection();
        }
    }

    /**
     * Expand the parents of the current object
     * @param theObject_p
     * @since 3.1.0.0
     */
    protected abstract void expandParents(Object theObject_p) throws Exception;

    protected OwTreeViewNode getNodeForObject(Object theObject_p)
    {
        Iterator nodesIterator = m_nodeMap.values().iterator();
        OwTreeViewNode correspondingNode = null;
        while (nodesIterator.hasNext())
        {
            OwTreeViewNode node = (OwTreeViewNode) nodesIterator.next();
            if (node.getObject().equals(theObject_p))
            {
                correspondingNode = node;
                break;
            }
        }
        return correspondingNode;
    }

    /**(overridable)
     * Reset the current selection.
     * @since 3.1.0.0
     */
    public void clearSelection()
    {
        this.m_selectedNode = null;
    }

    /**
     * Set the children sort criteria.
     * @param childrenSort_p - the {@link OwSort} criteria.
     * @since 3.2.0.0
     */
    public void setChildrenSort(OwSort childrenSort_p)
    {
        this.childrenSort = childrenSort_p;
    }

    /**
     * Get the currently set children sort criteria
     * @return the {@link OwSort} children criteria.
     * @since 3.2.0.0
     */
    public OwSort getChildrenSort()
    {
        return childrenSort;
    }

    /**
     * Tries to find the node for the given object and then refresh it.
     * @param param_p
     * @throws Exception 
     * @since 4.0.0.0
     */
    public void refreshNodeForObject(Object param_p) throws Exception
    {
        OwTreeViewNode node = this.getNodeForObject(param_p);
        if (null != node)
        {
            node.refresh();
        }
    }
}