package com.wewebu.ow.server.dmsdialogs.views;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.ui.OwTreeView;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * TreeView Module to display Tree Views of OwObjects.
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
public class OwObjectTreeView extends OwTreeView
{
    /** application m_Configuration reference */
    protected OwConfiguration m_Configuration;

    /** maximum number of child nodes */
    protected int m_iMaxChildSize = 50;
    /** flag indicating that dynamic split is in use*/
    private boolean m_isDynamicSplitUsed = false;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // get application m_Configuration
        m_Configuration = ((OwMainAppContext) getContext()).getConfiguration();

    }

    /** set maximum number of child nodes */
    public void setMaxChildSize(int iMaxChildSize_p)
    {
        m_iMaxChildSize = iMaxChildSize_p;
    }

    /** navigate to the given child object
     * 
     * @param obj_p Child OwObject to navigate to
     */
    public void navigateChildObject(OwObject obj_p) throws Exception
    {
        navigate(m_strPath + obj_p.getID() + PATH_DELIMITER);
    }

    // === overridden methods
    /** 
     * Create a list of child objects to the given object. 
     * The children are ordered using the {@link OwSort} object (@see {@link OwTreeView#getChildrenSort()}).
     *
     * @param obj_p Object which is managed by the tree view
     * @return List of Objects which are child to the given object
     */
    protected java.util.List createChildObjects(Object obj_p) throws Exception
    {
        return ((OwObject) obj_p).getChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS }, null, this.childrenSort, m_iMaxChildSize, 0, null);
    }

    /** get a URL to the close icon
     *
     *  to be implemented by the derived class
     *
     * @param obj_p Object which is managed by the tree view
     * @return String URL to the close icon 
     */
    protected String getCloseIconURL(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        OwXMLUtil mimeNode = OwMimeManager.getMimeNode(m_Configuration, (OwObject) obj_p);
        return ((OwMainAppContext) getContext()).getDesignURL() + "/micon/" + mimeNode.getSafeTextValue("icon", "unknown.png");
    }

    /** get a URL to the open icon
     *
     *  to be implemented by the derived class
     *
     * @param obj_p Object which is managed by the tree view
     * @return String URL to the close icon 
     */
    protected String getOpenIconURL(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        OwXMLUtil mimeNode = OwMimeManager.getMimeNode(m_Configuration, (OwObject) obj_p);
        return ((OwMainAppContext) getContext()).getDesignURL() + "/micon/" + mimeNode.getSafeTextValue("openicon", "unknown.png");
    }

    /** get the  name for an object, used to build paths 
    *
    *  to be implemented by the derived class
    *
    * @param obj_p Object which is managed by the tree view
    * @return the name for an object
    */
    protected String getName(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        return ((OwObject) obj_p).getID();
    }

    /** 
     * Returns the text to be displayed as the name of the node.<br>
     * This text will be properly encoded before used.
     *
     * @param obj_p Object which is managed by the tree view
     * @param node_p the node to return the text for
     * 
     * @return the text to be displayed as the name of the node.
     */
    protected String getDisplayName(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        return ((OwObject) obj_p).getName();
    }

    /**
     * Returns HTML code to be used to display a node.<br>
     * If you override this method, you have to make sure that the HTML code is properly encoded!
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
        try
        {
            // === try to display the child count if possible
            int iChildCount = ((OwObject) obj_p).getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS }, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);

            StringBuffer ret = new StringBuffer();

            ret.append(OwHTMLHelper.encodeToSecureHTML(getDisplayName(obj_p, node_p)));

            ret.append("<span class='OwObjectTreeViewChildCount'> (");
            ret.append(String.valueOf(iChildCount));
            ret.append(")</span>");

            return ret.toString();
        }
        catch (OwStatusContextException e)
        {
            return OwHTMLHelper.encodeToSecureHTML(getDisplayName(obj_p, node_p));
        }
    }

    /** (overridable) check of node has subnodes
     *  to be overwritten by the derived class
     *
     * @param obj_p Object which is managed by the tree view
     * @return true if plus minus icon should be displayed to open subnodes
     */
    protected boolean hasPlusMinusIcon(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        try
        {
            // === try to get the child status from the object
            return ((OwObject) obj_p).hasChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS }, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
        }
        catch (OwStatusContextException e)
        {
            // default implementation
            return super.hasPlusMinusIcon(obj_p, node_p);
        }
    }

    /**
     * Setter for flag {@link OwObjectTreeView#m_isDynamicSplitUsed}.
     * @param isynamicSplitUsed_p - the flag value
     * @since 3.1.0.0 
     */
    public void setIsDynamicSplitUsed(boolean isynamicSplitUsed_p)
    {
        m_isDynamicSplitUsed = isynamicSplitUsed_p;
    }

    /**
     * Getter for flag {@link OwObjectTreeView#m_isDynamicSplitUsed}.
     * @return - <code>true</code> if the dynamic split is used.
     * @since 3.1.0.0
     */
    public boolean isDynamicSplitUsed()
    {
        return m_isDynamicSplitUsed;
    }

    @Override
    protected void expandParents(Object theObject_p) throws Exception
    {
        OwObject theObject = (OwObject) theObject_p;
        OwObjectCollection parents = theObject.getParents();
        if (parents != null && parents.size() > 0)
        {
            for (int i = parents.size() - 1; i >= 0; i--)
            {
                OwTreeViewNode node = getNodeForObject(parents.get(i));
                if (node != null)
                {
                    node.expand();
                }
            }
        }
    }

}