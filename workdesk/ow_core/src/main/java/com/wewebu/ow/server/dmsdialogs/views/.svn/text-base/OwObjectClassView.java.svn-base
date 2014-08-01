package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwRootClassCfg;
import com.wewebu.ow.server.ecm.OwLocation;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.OwSimpleLocation;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwMultipanel;
import com.wewebu.ow.server.ui.OwTreeView;
import com.wewebu.ow.server.ui.OwTreeView.OwTreeViewNode;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * View Module to select the document class of a new document.
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
public class OwObjectClassView extends OwLayout implements OwTreeView.OwTreeViewEventListner, OwMultipanel
{
    /** overwrite the object tree view to use own styles */
    public class OwObjectClassTreeViewViewWithStyle extends OwObjectClassTreeView
    {
        /** 
         * construct a doc class view to select document classes
         *
         * @param resource_p the OwResource to get the classes from
         * @param iObjectType_p OwObject type to filter
         */
        public OwObjectClassTreeViewViewWithStyle(OwResource resource_p, int iObjectType_p)
        {
            super(resource_p, iObjectType_p);
        }

        /**
         * @param resource_p
         * @param iObjectType_p
         * @param classSelectionCfg
         */
        public OwObjectClassTreeViewViewWithStyle(OwResource resource_p, int iObjectType_p, OwObjectClassSelectionCfg classSelectionCfg)
        {
            super(resource_p, iObjectType_p, classSelectionCfg);
        }

        /** get the style for the selected tree item
         */
        protected String getSelectedTreeItemStyle()
        {
            if (null == m_TreeSelectedItemStyle)
            {
                return super.getSelectedTreeItemStyle();
            }
            else
            {
                return m_TreeSelectedItemStyle;
            }
        }

        /** get the style for the tree item
         */
        protected String getTreeItemStyle()
        {
            if (null == m_TreeItemStyle)
            {
                return super.getTreeItemStyle();
            }
            else
            {
                return m_TreeItemStyle;
            }
        }
    }

    /** overwritten TreeItemStyle for the treeview or null to use default */
    protected String m_TreeItemStyle;
    /** overwritten TreeItemSelectedStyle for the treeview or null to use default */
    protected String m_TreeSelectedItemStyle;

    /** set the style for the selected tree item
     */
    public void setSelectedItemStyle(String strStyle_p)
    {
        m_TreeSelectedItemStyle = strStyle_p;
    }

    /** set the style for the tree item
     */
    public void setItemStyle(String strStyle_p)
    {
        m_TreeItemStyle = strStyle_p;
    }

    /**
     *<p>
     * Event listener for select event.
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
    public interface OwObjectClassViewListner
    {
        /** event called when user selects a class
         * @param classDescription_p OwObjectClass
         * @param strPath_p String path to selected tree item
         */
        public abstract void onObjectClassViewSelectClass(OwObjectClass classDescription_p, String strPath_p) throws Exception;
    }

    /** event listener for select event */
    protected OwObjectClassViewListner m_listner;

    /** Menu for buttons in the view */
    protected OwSubMenuView m_MenuView = new OwSubMenuView();

    /** View to display the class description as Preview */
    protected OwObjectClassPreviewView m_ObjectClassPreview = new OwObjectClassPreviewView();

    /** region of the tree view */
    public static final int TREE_REGION = 0;
    /** region of the tree view */
    public static final int MENU_REGION = 1;
    /** region of the tree view */
    public static final int PREVIEW_REGION = 2;

    /** index of the next button in the menu */
    protected int m_iNextButtonIndex = -1;

    /** tree view module to display the OwObject structure */
    protected OwObjectClassTreeViewViewWithStyle m_TreeView;

    /** network resource to get the classes from */
    protected OwResource m_resource;

    /**
     * ObjectClassProcessor for post processing of object class selection.
     * @since 4.1.1.0
     */
    private OwObjectClassProcessor objectClassProcessor;

    /**
     * location parameter
     * @since 4.1.1.0
     */
    private OwLocation location;

    /** set object class processor
     * @param objectClassProcessor OwObjectClassProcessor
     * @since 4.1.1.0*/
    public void setObjectClassProcessor(OwObjectClassProcessor objectClassProcessor)
    {
        this.objectClassProcessor = objectClassProcessor;
        if (this.objectClassProcessor != null)
        {
            this.location = new OwSimpleLocation(m_resource);
        }
    }

    /**
     * The current location, containing the current resource.<br />
     * Only initialized if {@link #setObjectClassProcessor(OwObjectClassProcessor)} is called with non-null parameter. 
     * @return OwLocation or null
     * @since 4.1.1.0
     */
    protected OwLocation getLocation()
    {
        return this.location;
    }

    /** set event listener */
    public void setEventListner(OwObjectClassViewListner listner_p)
    {
        m_listner = listner_p;
    }

    /** get the form used for the edit fields
     *
     * @return String form name
     */
    public String getFormName()
    {
        // target has no form defined by default, override in derived class
        return null;
    }

    /** object type to display classes for */
    private int m_iObjectType;

    /** 
     * name of class to start from or null to start browsing from root
     * @deprecated will be removed in the future. You'd Better use {@link #classSelectionCfg} instead.  
     */
    private String m_strParentObjectClass;
    private OwObjectClassSelectionCfg classSelectionCfg;

    /** construct a doc class view to select document classes
     *
     * @param resource_p the OwResource to get the classes from
     * @param iObjectType_p int object to browse for
     * @param strParentObjectClass_p String name of class to start from or null to start browsing from root
     * @deprecated this will be removed in the future. Please use {@link #OwObjectClassView(OwResource, int, OwObjectClassSelectionCfg)} instead.
     */
    public OwObjectClassView(OwResource resource_p, int iObjectType_p, String strParentObjectClass_p)
    {
        m_resource = resource_p;
        m_iObjectType = iObjectType_p;
        m_strParentObjectClass = strParentObjectClass_p;
    }

    /**
     * 
     * @param resource_p
     * @param iObjectType_p
     * @param classSelectionCfg
     * @since 4.1.0.0
     */
    public OwObjectClassView(OwResource resource_p, int iObjectType_p, OwObjectClassSelectionCfg classSelectionCfg)
    {
        m_resource = resource_p;
        m_iObjectType = iObjectType_p;
        this.classSelectionCfg = classSelectionCfg;
    }

    /** construct a doc class view to select document classes
    *
    * @param resource_p the OwResource to get the classes from
    * @param iObjectType_p int object to browse for
    */
    public OwObjectClassView(OwResource resource_p, int iObjectType_p)
    {
        m_resource = resource_p;
        m_iObjectType = iObjectType_p;
    }

    /** init the target after the context is set.
      */
    protected void init() throws Exception
    {
        super.init();

        // add tree view
        m_TreeView = new OwObjectClassTreeViewViewWithStyle(m_resource, m_iObjectType, this.classSelectionCfg);
        m_TreeView.setEventListner(this);

        // === add tree view
        addView(m_TreeView, TREE_REGION, null);

        if (null != this.classSelectionCfg && !this.classSelectionCfg.getRootClasses().isEmpty())
        {
            Set<OwRootClassCfg> rootClasses = this.classSelectionCfg.getRootClasses();
            Set<String> names = new HashSet<String>();
            for (OwRootClassCfg rootClassCfg : rootClasses)
            {
                names.add(rootClassCfg.getName());
            }
            Iterator<String> namesIterator = names.iterator();
            m_TreeView.navigateClasses(namesIterator, null);
        }
        else
        {
            if (m_strParentObjectClass != null)
            {
                //TODO remove this deprecated code
                OwObjectClass rootClass = ((OwMainAppContext) getContext()).getNetwork().getObjectClass(m_strParentObjectClass, m_resource);

                // init tree from given object class
                m_TreeView.navigate(rootClass, null);
            }
            else
            {
                // Default behavior
                // init tree from root
                OwMainAppContext mainContext = (OwMainAppContext) getContext();
                OwNetwork network = mainContext.getNetwork();
                Map classNamesMap = network.getObjectClassNames(new int[] { m_iObjectType }, true, true, m_resource);
                Set names = classNamesMap.keySet();
                Iterator namesIterator = names.iterator();
                m_TreeView.navigateClasses(namesIterator, null);
            }
        }

        // === add menu
        addView(m_MenuView, MENU_REGION, null);

        // === add object preview
        addView(m_ObjectClassPreview, PREVIEW_REGION, null);
    }

    /** navigates to the given class object
     * NOTE: the tree must have been initialized with navigateClasses already.
     *
     * @param objectClass_p OwObjectClass
     */
    public void navigateToClass(OwObjectClass objectClass_p) throws Exception
    {
        try
        {
            m_TreeView.navigateToClass(objectClass_p);
        }
        catch (Exception e)
        {
            // class not found in the tree, might be hidden
            // we know it exists, so set it

            onTreeViewOpenFolder(m_TreeView.new OwTreeViewNode(m_TreeView, objectClass_p));
        }
    }

    public void onTreeViewNavigateFolder(OwTreeViewNode node_p) throws Exception
    {
        //do nothing 
    }

    /** 
     * event called when a user opens a folder to display its document contents 
     * overridden from OwObjectTreeView.OwObjectTreeViewEventListner
     *
     */
    public void onTreeViewOpenFolder(OwTreeViewNode node_p) throws Exception
    {
        Object object = node_p.getObject();
        String strPath = node_p.getPath();
        if (object instanceof OwObjectClass)
        {
            OwObjectClass clazz = (OwObjectClass) object;
            if (objectClassProcessor != null)
            {
                clazz = objectClassProcessor.process(clazz, getLocation(), getContext());
            }
            // user selected a document class, inform document
            if (m_listner != null)
            {
                m_listner.onObjectClassViewSelectClass(clazz, strPath);
            }

            // set in preview as well
            m_ObjectClassPreview.setObjectClass(clazz);

            // set next button when class was selected
            if (m_iNextButtonIndex != -1)
            {
                m_MenuView.enable(m_iNextButtonIndex, true);
            }

            m_fValid = true;
        }
    }

    /** flag indicating if view is validated */
    private boolean m_fValid = false;

    /** set the view that is next to this view, displays a next button to activate
      *
      * @param nextView_p OwView
      *
      */
    public void setNextActivateView(OwView nextView_p) throws Exception
    {
        m_iNextButtonIndex = m_MenuView.addMenuItem(this, getContext().localize("app.OwObjectClassView.next", "Next"), null, "Next", nextView_p, null);
        m_MenuView.setDefaultMenuItem(m_iNextButtonIndex);
        // disable initially
        m_MenuView.enable(m_iNextButtonIndex, false);
    }

    /** event called when user clicked Lock button in menu 
     *   @param oReason_p Optional reason object submitted in addMenuItem
     *   @param request_p  HttpServletRequest
     */
    public void onNext(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        ((OwView) oReason_p).activate();
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div class='OwObjectClassView'>");
        w_p.write("<table border='0' cellspacing='0' cellpadding='0'>");

        w_p.write("<tr>");

        w_p.write("<th valign='top'>");

        renderRegion(w_p, TREE_REGION);

        w_p.write("</th>");

        // delimiter
        w_p.write("<th>&nbsp;&nbsp;&nbsp;</th>");

        w_p.write("<th valign='top'>");

        renderRegion(w_p, PREVIEW_REGION);

        w_p.write("</th>");

        w_p.write("</tr>");

        // delimiter
        w_p.write("<tr><th colspan='3'>&nbsp;</th></tr>");

        w_p.write("<tr>");

        w_p.write("<th valign='top' colspan='3'>");

        renderRegion(w_p, MENU_REGION);

        w_p.write("</th>");

        w_p.write("</tr>");

        w_p.write("</table>");
        w_p.write("</div>");
    }

    /** check if view has validated its data and the next view can be enabled
      *
      * @return boolean true = can forward to next view, false = view has not yet validated
      *
      */
    public boolean isValidated() throws Exception
    {
        return m_fValid;
    }

    /** set the view that is prev to this view, displays a prev button to activate
      *
      * @param prevView_p OwView
      *
      */
    public void setPrevActivateView(OwView prevView_p) throws Exception
    {
        // ignore
    }

}