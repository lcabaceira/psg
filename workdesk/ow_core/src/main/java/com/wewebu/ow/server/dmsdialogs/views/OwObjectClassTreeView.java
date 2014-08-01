package com.wewebu.ow.server.dmsdialogs.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwRootClassCfg;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ui.OwTreeView;
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
public class OwObjectClassTreeView extends OwTreeView
{
    /** application m_Configuration reference */
    protected OwConfiguration m_Configuration;

    /** network resource to get the classes from */
    protected OwResource m_resource;

    /** OwObject type to filter */
    protected int m_iObjectType;

    /** credentials of the user */
    protected OwNetwork m_network;

    private OwObjectClassSelectionCfg classSelectionCfg;

    /** construct a doc class view to select document classes
     *
     * @param resource_p the OwResource to get the classes from
     * @param iObjectType_p OwObject type to filter
     */
    public OwObjectClassTreeView(OwResource resource_p, int iObjectType_p)
    {
        m_resource = resource_p;
        m_iObjectType = iObjectType_p;
    }

    /**
     * @param resource_p
     * @param iObjectType_p
     * @param classSelectionCfg
     */
    public OwObjectClassTreeView(OwResource resource_p, int iObjectType_p, OwObjectClassSelectionCfg classSelectionCfg)
    {
        m_resource = resource_p;
        m_iObjectType = iObjectType_p;
        this.classSelectionCfg = classSelectionCfg;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // get application m_Configuration
        m_Configuration = ((OwMainAppContext) getContext()).getConfiguration();
        m_network = ((OwMainAppContext) getContext()).getNetwork();
    }

    /**
     * set the folder object to display
     * @param classNames_p Iterator
     * @param strSubFolderPath_p
     * @throws Exception
     */
    public void navigateClasses(Iterator classNames_p, String strSubFolderPath_p) throws Exception
    {
        navigate(classNames_p, strSubFolderPath_p);
    }

    /** navigates to the given class object
     * NOTE: the tree must have been initialized with navigateClasses already.
     *
     * @param objectClass_p OwObjectClass
     */
    public void navigateToClass(OwObjectClass objectClass_p) throws Exception
    {
        String strPath = getObjectClassPath(objectClass_p);

        navigate(strPath);
    }

    /** recursively compute the path to a object class
     *
     * @param objectClass_p OwObjectClass to compute path of
     *
     * @return String with path
     */
    private String getObjectClassPath(OwObjectClass objectClass_p) throws Exception
    {
        OwObjectClass parent = objectClass_p.getParent();
        if (parent != null)
        {
            return getObjectClassPath(parent) + "/" + objectClass_p.getClassName();
        }
        else
        {
            return "/" + objectClass_p.getClassName();
        }
    }

    // === overridden methods
    /** 
     * create a list of child objects to the given object
     * @param objectClass_p Object which is managed by the tree view
     * @return List of Objects which are child to the given object
     */
    protected java.util.List createChildObjects(Object objectClass_p) throws Exception
    {
        if (objectClass_p instanceof Iterator)
        {
            // === root object is a list of class names
            List childList = new ArrayList();

            // iterate over the class names and get the object class descriptions form network
            while (((Iterator) objectClass_p).hasNext())
            {
                String nextClassName = (String) ((Iterator) objectClass_p).next();
                OwObjectClass objClass = m_network.getObjectClass(nextClassName, m_resource);
                childList.add(objClass);
            }

            return childList;
        }
        else
        {
            // === all subnodes are OwObjectClass instances
            // create new list, which contains only the createable class definitions
            List objectClassList = ((OwObjectClass) objectClass_p).getChilds(m_network, true);
            if (objectClassList != null)
            {
                List childList = new ArrayList();
                Iterator it = objectClassList.iterator();

                while (it.hasNext())
                {
                    OwObjectClass objClass = (OwObjectClass) it.next();
                    childList.add(objClass);
                }

                return childList;
            }
            else
            {
                return null;
            }
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
            OwObjectClass myClass = (OwObjectClass) obj_p;
            if (null != this.classSelectionCfg)
            {
                OwRootClassCfg rootClassCfg = this.classSelectionCfg.get(myClass.getClassName());
                if (null != rootClassCfg)
                {
                    if (!rootClassCfg.isIncludeSubclasses())
                    {
                        return false;
                    }
                }
            }
            return myClass.hasChilds(m_network, true, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
        }
        catch (Exception e)
        {
            return super.hasPlusMinusIcon(obj_p, node_p);
        }
    }

    /** check if node is enabled, i.e. can open the folder
     *
     *  to be implemented by the derived class
     *
     * @param obj_p Object which is managed by the tree view
     * @return true = enabled, false = disabled
     */
    public boolean isEnabled(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        try
        {
            return ((OwObjectClass) obj_p).canCreateNewObject();
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }

    /** 
     * get the  name for an object, used to build paths   
     * @param obj_p Object which is managed by the tree view
     * @return the name for an object
     */
    protected String getName(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        if (obj_p instanceof OwObjectClass)
        {
            return ((OwObjectClass) obj_p).getClassName();
        }
        else
        {
            return "/";
        }
    }

    /** get the display name for an object, default returns getName
     *
     *  to be implemented by the derived class
     *
     * @param obj_p Object which is managed by the tree view
     * @return the display name for an object
     */
    protected String getDisplayName(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        if (obj_p instanceof OwObjectClass)
        {
            return ((OwObjectClass) obj_p).getDisplayName(getContext().getLocale());
        }
        else
        {
            return getContext().localize("app.OwObjectClassTreeView.rootnode", "Document Classes");
        }
    }

    /** 
     * get a URL to the close icon 
     * @param obj_p Object which is managed by the tree view
     * @param node_p an {@link com.wewebu.ow.server.ui.OwTreeView.OwTreeViewNode}
     * @return String URL to the close icon 
     */
    protected String getCloseIconURL(Object obj_p, OwTreeViewNode node_p) throws Exception
    {
        // create link
        StringBuffer iconpath = new StringBuffer();
        iconpath.append(getContext().getDesignURL());

        try
        {
            OwXMLUtil mimenode = ((OwMainAppContext) getContext()).getConfiguration().getDefaultMIMENode(((OwObjectClass) obj_p).getType());
            iconpath.append(OwMimeManager.MIME_ICON_SUBPATH);
            if (mimenode != null)
            {
                iconpath.append(mimenode.getSafeTextValue(OwMimeManager.MIME_ICON_NAME, "unknown.png"));
            }
            else
            {
                iconpath.append("unknown.png");
            }
        }
        catch (ClassCastException e)
        {
            // use root icon
            iconpath.append("/images/OwObjectClassPreviewView/root.png");
        }

        return iconpath.toString();
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
        return getCloseIconURL(obj_p, node_p);
    }

    @Override
    protected void expandParents(Object theObject_p)
    {
        // TODO Auto-generated method stub

    }
}