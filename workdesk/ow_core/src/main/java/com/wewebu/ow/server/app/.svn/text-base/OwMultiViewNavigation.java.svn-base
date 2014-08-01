package com.wewebu.ow.server.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.ui.OwBaseView;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.ui.helper.OwInnerViewWrapper;

/**
 *<p>
 * Navigation view that allows for a secondary sub-view 
 * to be selected and displayed.  
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
 *@since 4.2.0.0
 */
public class OwMultiViewNavigation extends OwSubNavigationView
{
    private Map<Integer, OwInnerViewWrapper> secondaryInnerWrappers = new HashMap<Integer, OwInnerViewWrapper>();
    private boolean secodatyViewCollapsed = false;

    public class OwMultiViewTabInfo extends OwSubTabInfo
    {
        protected Map<Integer, OwView> secondaryViews = new HashMap<Integer, OwView>();

        public OwMultiViewTabInfo(OwSubNavigationView navView_p, OwView view_p, String strTitle_p, String strImage_p, Object oReason_p, String strToolTip_p)
        {
            super(navView_p, view_p, strTitle_p, strImage_p, oReason_p, strToolTip_p);
        }

        public void addSecondaryView(int id, OwView view)
        {
            secondaryViews.put(Integer.valueOf(id), view);
        }

        public OwView getSecondaryView(Integer id)
        {
            return secondaryViews.get(id);
        }
    }

    public int addView(OwView view_p, String strTitle_p, String strName_p, String strImage_p, Object oReason_p, String strToolTip_p) throws Exception
    {
        // === create and set new info instance for the tab
        return super.addView(new OwMultiViewTabInfo(this, view_p, strTitle_p, strImage_p, oReason_p, strToolTip_p), strName_p);
    }

    public void addSecondaryView(int tabIndex, int viewId, OwView view, String viewName) throws Exception
    {
        OwMultiViewTabInfo tab = (OwMultiViewTabInfo) getTab(tabIndex);
        registerView(view, viewName);
        tab.addSecondaryView(viewId, view);
    }

    public synchronized OwBaseView getSecondaryViewReference(int viewId)
    {
        Integer id = Integer.valueOf(viewId);
        OwInnerViewWrapper wrapper = secondaryInnerWrappers.get(id);
        if (wrapper == null)
        {
            wrapper = new OwInnerViewWrapper();
            secondaryInnerWrappers.put(id, wrapper);
        }

        return wrapper;
    }

    @Override
    public synchronized void navigate(int iIndex_p, HttpServletRequest request_p) throws Exception
    {
        super.navigate(iIndex_p, request_p);

        OwMultiViewTabInfo tab = (OwMultiViewTabInfo) getTab(iIndex_p);
        Set<Integer> ids = secondaryInnerWrappers.keySet();
        for (Integer id : ids)
        {
            OwView secondaryView = tab.getSecondaryView(id);
            secondaryInnerWrappers.get(id).setView(secondaryView);
        }
    }

    /**
     * 
     * @return default collapsed state of the secondary view 
     * @since 4.2.0.0
     */
    public boolean isSecodatyViewCollapsed()
    {
        return secodatyViewCollapsed;
    }

    /**
     * 
     * @param secodatyViewCollapsed default collapsed state of the secondary view
     * @since 4.2.0.0
     */
    public void setSecodatyViewCollapsed(boolean secodatyViewCollapsed)
    {
        this.secodatyViewCollapsed = secodatyViewCollapsed;
    }

}
