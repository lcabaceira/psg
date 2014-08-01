package com.wewebu.ow.server.plug.owshortcut;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMasterView;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.plug.std.log.OwLog;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Short Cuts View. Master plugin that displays and executes shortcuts.
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
public class OwShortCutView extends OwMasterView
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwShortCutView.class);

    /** XML node name for the viewmasks node */
    private static final String VIEW_MASKS_NODE_NAME = "viewmasks";

    /** XML node name for the subview node */
    private static final String SUBVIEW_MASKS_NODE_NAME = "subview";

    /** ShortCutItem view to display the shortcuts */
    private OwShortCutItemView m_itemview;

    /** SavedSearchesView to display and execute saved searches */
    private OwSavedSearchesView m_searchesView;

    /** layout to be used for the view */
    private OwSubLayout m_Layout;

    /** the sub navigation view to navigate the different views (shortcut filters) */
    private OwSubNavigationView m_subNavigation;

    /** display the tab "All" in the favorites plugin */
    public static final int VIEW_MASK_DISPLAY_ALL = 0x0000001;

    /** display the tab "Documents" in the favorites plugin */
    public static final int VIEW_MASK_DISPLAY_DOCUMENTS = 0x0000002;

    /** display the tab "Folders" in the favorites plugin */
    public static final int VIEW_MASK_DISPLAY_FOLDERS = 0x0000004;

    /** display the tab "Other" in the favorites plugin */
    public static final int VIEW_MASK_DISPLAY_OTHER = 0x0000008;

    /** display the tab "Stored Searches" in the favorites plugin */
    public static final int VIEW_MASK_DISPLAY_STORED_SEARCHES = 0x0000010;

    /** get document instance */
    OwShortCutDocument owShorcutDocument;

    /** filters the views and behaviors to be displayed */
    protected int m_iViewMask = 0;

    public OwShortCutView()
    {

    }

    /** Initialization after target has set */
    protected void init() throws Exception
    {
        super.init();

        createViewMask();

        m_itemview = createShortCutItemView();
        m_searchesView = createSavedSearchesView();
        m_Layout = new OwSubLayout();
        owShorcutDocument = (OwShortCutDocument) getDocument();

        // === Attach layout
        addView(m_Layout, null);

        // === navigation
        m_subNavigation = new OwSubNavigationView();
        m_Layout.addView(m_subNavigation, OwSubLayout.NAVIGATION_REGION, null);

        // === function view
        m_Layout.addView(new OwShortCutFunctionsView(), OwSubLayout.MENU_REGION, null);

        // === add the current view of the navigation to the layout
        m_Layout.addViewReference(m_subNavigation.getViewReference(), OwSubLayout.MAIN_REGION);

        String designRoot = getContext().getDesignURL();
        if (hasViewMask(VIEW_MASK_DISPLAY_ALL))
        {
            m_subNavigation.addView(m_itemview, getContext().localize("plug.owshorcut.filterall", "All"), null, null, Integer.valueOf(OwShortCutItemView.SHORTCUT_ITEM_FILTER_ALL),
                    getContext().localize("plug.owshorcut.filterall_tooltip", "Show all favorites"));
        }
        if (hasViewMask(VIEW_MASK_DISPLAY_DOCUMENTS))
        {
            m_subNavigation.addView(m_itemview, getContext().localize("plug.owshorcut.filterdocuments", "Documents"), null, designRoot + "/micon/doc.png", Integer.valueOf(OwShortCutItemView.SHORTCUT_ITEM_FILTER_DOCUMENTS),
                    getContext().localize("plug.owshorcut.filterdocuments_tooltip", "Show document favorites"));
        }
        if (hasViewMask(VIEW_MASK_DISPLAY_FOLDERS))
        {
            m_subNavigation.addView(m_itemview, getContext().localize("plug.owshorcut.filterfolders", "eFiles / Folders"), null, designRoot + "/micon/folder.png", Integer.valueOf(OwShortCutItemView.SHORTCUT_ITEM_FILTER_FOLDERS), getContext()
                    .localize("plug.owshorcut.filterfolders_tooltip", "Show eFile or folder favorites"));
        }
        if (hasViewMask(VIEW_MASK_DISPLAY_OTHER))
        {
            m_subNavigation.addView(m_itemview, getContext().localize("plug.owshorcut.filterrest", "Other"), null, designRoot + "/micon/custom.png", Integer.valueOf(OwShortCutItemView.SHORTCUT_ITEM_FILTER_REST),
                    getContext().localize("plug.owshorcut.filterrest_tooltip", "Shows other favorites"));
        }
        if (hasViewMask(VIEW_MASK_DISPLAY_STORED_SEARCHES))
        {
            // === add a delimiter if other tabs are shown before; see ticket 5350
            if (hasViewMask(VIEW_MASK_DISPLAY_OTHER) || hasViewMask(VIEW_MASK_DISPLAY_FOLDERS) || hasViewMask(VIEW_MASK_DISPLAY_DOCUMENTS) || hasViewMask(VIEW_MASK_DISPLAY_ALL))
            {
                m_subNavigation.addDelimiter();
            }
            m_subNavigation.addView(m_searchesView, getContext().localize("plug.owshorcut.storedsearch", "Stored Searches"), null, designRoot + "/micon/stored_search.png", null,
                    getContext().localize("plug.owshorcut.storedsearch_tooltip", "Stored Searches"));
        }

        m_searchesView.setDocument(this.getDocument());
        m_subNavigation.navigateFirst();
    }

    /** get the item view containing the items */
    public OwShortCutItemView getItemView()
    {
        return m_itemview;
    }

    /**
     * (overridable)
     * Factory method to create own ShortCutItemView
     * @return {@link OwShortCutItemView}
     * @since 2.5.3.0
     */
    public OwShortCutItemView createShortCutItemView()
    {
        return new OwShortCutItemView();
    }

    /**
     * (overridable)
     * Factory method to create own SavedSearchesView
     * @return {@link OwSavedSearchesView}
     * @since 2.5.3.0
     */
    public OwSavedSearchesView createSavedSearchesView()
    {
        return new OwSavedSearchesView();
    }

    /**
     * 
     */
    protected void onActivate(int index_p, Object reason_p) throws Exception
    {
        if (m_iViewMask > 0)
        {
            super.onActivate(index_p, reason_p);
        }
    }

    /**
     * create tab viewmask from XML configuration 
     * @return viewMask
     * @since 3.1.0.0
     */
    protected int createViewMask()
    {
        OwXMLUtil subnode;
        try
        {
            OwXMLUtil node = getConfigNode().getSubUtil(SUBVIEW_MASKS_NODE_NAME);
            if (null == node)
            {
                return defaultViewMask();
            }
            subnode = node.getSubUtil(VIEW_MASKS_NODE_NAME);

            List maskValues = subnode.getSafeStringList();
            Iterator it = maskValues.iterator();

            // combine them bitwise
            while (it.hasNext())
            {
                String sMask = (String) it.next();
                m_iViewMask |= getClass().getField(sMask).getInt(null);

            }
        }
        catch (Exception e)
        {
            LOG.info("Subview masks are not configured. Using default settings.");
        }

        return m_iViewMask;
    }

    /**
     * Check if view should be displayed or is masked out
     * @since 3.1.0.0 
     * @param  iViewMask_p bitmask according to VIEW_MASK_SYSTEM_... flags
     */
    protected boolean hasViewMask(int iViewMask_p)
    {
        return ((iViewMask_p & m_iViewMask) != 0);
    }

    /**
     * set default mask, enable all tabs
     * @return int
     * @since 3.1.0.0
     */
    protected int defaultViewMask()
    {
        m_iViewMask = VIEW_MASK_DISPLAY_ALL | VIEW_MASK_DISPLAY_DOCUMENTS | VIEW_MASK_DISPLAY_FOLDERS | VIEW_MASK_DISPLAY_OTHER | VIEW_MASK_DISPLAY_STORED_SEARCHES;

        return m_iViewMask;
    }

}