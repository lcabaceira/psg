package com.wewebu.ow.server.plug.owrecord;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwDynamicLayoutSettings;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwScript;
import com.wewebu.ow.server.app.OwSearchCriteriaView;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.plug.owrecord.log.OwLog;

/**
 *<p>
 * Layout of the Record plugin area.
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
public class OwRecordSubLayout extends OwSubLayout
{
    private static final String ID_NAV_CONTENT_PANEL = "navContentPanel";
    private static final String ID_PROP_PREVIEW_CONTENT_PANEL = "propPreviewContentPanel";

    /**
     * The sub layout menu div id.
     * @since 3.1.0.0
     */
    public static final String OW_SUB_LAYOUT_MENU_DIV_ID = "OwSubLayout_menu";
    public static final String OW_REGION_DND_APPLET_DIV_ID = "OwRegion_dnd_applet";
    public static final String OW_REGION_MENU_DIV_ID = "OwRegion_menu";
    public static final String OW_REGION_HOTKEY_DIV_ID = "OwRegion_hotkey";
    public static final String OW_REGION_SEARCHTEMPLATE_DIV_ID = "OwRegion_searchtemplate";

    /**
     * The event name for update the {@link OwRecordSubLayout#OW_SUB_LAYOUT_MENU_DIV_ID} div.
     * @since 3.1.0.0
     */
    public static final String AJAX_UPDATE_SUB_LAYOUT_MENU = "UpdateSubLayoutMenu";
    public static final String AJAX_UPDATE_DND_APPLET_REGION = "UpdateDnDAppletRegion";
    public static final String AJAX_UPDATE_MENU_REGION = "UpdateMenuRegion";
    public static final String AJAX_UPDATE_HOTKEY_REGION = "UpdateHotkeyRegion";
    public static final String AJAX_UPDATE_SEARCH_TEMPLATES_REGION = "UpdateSearchTemplatesRegion";

    /** class logger*/
    private static Logger LOG = OwLog.getLogger(OwRecordSubLayout.class);
    /** the name of the JSP used for dynamic page rendering*/
    private static final String DEFAULT_DYNAMIC_JSP_FORM = "owrecord/OwRecordSubLayoutDynamic.jsp";
    /** 
     * the name of the element containing the path to the JSP used for rendering
     * @since 3.1.0.0
     */
    protected static final String JSP_FORM_ELEMENT_NAME = "JspForm";
    /** 
     * path to the default JSP form, to be used for rendering.
     * @since 3.1.0.0
     */
    protected static final String DEFAULT_JSP_FORM = "owrecord/OwRecordSubLayout.jsp";
    /** region of the search template */
    public static final int SEARCH_TEMPLATE_REGION = 25;
    /** region of the property preview */
    public static final int PREVIEW_PROPERTY_REGION = 26;
    /** region of the the treeview in register optional mode */
    public static final int NAVIGATION_REGION_REGISTER_MODE = 27;
    /** region of the search template in vertical mode */
    public static final int SEARCH_TEMPLATE_REGION_VERTICAL = 28;

    /** render the views of the region
     *
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case SEARCH_TEMPLATE_REGION_VERTICAL:
                ((OwRecordSearchTemplateView) getViewRegion(SEARCH_TEMPLATE_REGION)).getCriteriaView().setViewMask(OwSearchCriteriaView.VIEW_MASK_RENDER_VERTICAL);
                super.renderRegion(w_p, SEARCH_TEMPLATE_REGION);
                break;

            default:
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /** determine if region exists
     *
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p)
    {
        try
        {
            switch (iRegion_p)
            {
                case PREVIEW_PROPERTY_REGION:
                    return ((OwRecordDocument) getDocument()).isPropertyPreviewViewEnabled();

                case SEARCH_TEMPLATE_REGION:
                    return ((OwRecordDocument) getDocument()).isSearchTemplateViewEnabled();
            }
        }
        catch (Exception e)
        {
            return false;
        }

        // override if regions are defined
        return super.isRegion(iRegion_p);
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        boolean useDynamicSplit = ((OwRecordDocument) getDocument()).isDynamicSplitInUse();

        String defaultJspPage = useDynamicSplit ? DEFAULT_DYNAMIC_JSP_FORM : DEFAULT_JSP_FORM;
        String strJspPage = ((OwRecordDocument) getDocument()).getConfigNode().getSafeTextValue(JSP_FORM_ELEMENT_NAME, defaultJspPage);
        serverSideDesignInclude(strJspPage, w_p);

        if (this.isRegionNormal(OwSubLayout.MAIN_REGION) && !useDynamicSplit)
        {
            OwScript script_p = new OwScript("arrangeRecordHeight('" + getTreeClientSideId() + "');");
            ((OwMainAppContext) getContext()).addFinalScript(script_p);
        }
        if (useDynamicSplit)
        {
            getContext().renderJSInclude("/js/owobjectslayout.js", w_p);
            ((OwMainAppContext) getContext()).addFinalScript(new OwScript("\n if (typeof(window['OwObjectLayoutEXTJS']) != 'undefined' && typeof(window['configLayout']) != 'undefined') {configLayout();OwObjectLayoutEXTJS.init();}\n",
                    OwScript.DEFAULT_PRIORITY - 100));
        }
    }

    /**
     * Get the client side id for the tree component.
     * @return - the client side id for the tree component.
     * @since 3.1.0.0
     */
    public String getTreeClientSideId()
    {
        return ((OwRecordDocument) getDocument()).getTreeViewId();
    }

    /** 
     * Get the initial width of the navigation panel, or the value configured in the attribute bags for this width.<br>
     * In case that <UseDynamicSplit> element has <code>false</code> value, the call of this method doesn't make sense, and <code>-1</code> is returned.
     * @return - the navigation panel width.
     * @since 3.1.0.0
     */
    public int getNavigationPanelInitialWidth()
    {
        OwDynamicLayoutSettings layoutSettings = ((OwRecordDocument) getDocument()).getLayoutSettings();
        return layoutSettings.getWidth();
    }

    /** 
     * Get the initial height of the preview properties panel, or the value configured in the attribute bags for this height
     * @return - the initial height of the tree panel, or the value configured in the attribute bags for this height
     * In case that <UseDynamicSplit> element has <code>false</code> value, the call of this method doesn't make sense, and <code>-1</code> is returned.
     * @since 3.1.0.0
     */
    public int getPreviewPropPanelInitialHeight()
    {
        OwDynamicLayoutSettings layoutSettings = ((OwRecordDocument) getDocument()).getLayoutSettings();
        return layoutSettings.getHeight();
    }

    /**
     * AJAX handler invoked when the user resize the navigation panel.
     * @param request_p - the {@link HttpServletRequest} object.
     * @param response_p - the {@link HttpServletResponse} object
     * @since 3.1.0.0
     */
    public void onAjaxNavigationPanelResize(HttpServletRequest request_p, HttpServletResponse response_p)
    {
        String width = request_p.getParameter("width");
        String height = request_p.getParameter("height");
        try
        {
            if (width != null && !width.equals(""))
            {
                ((OwRecordDocument) getDocument()).getLayoutSettings().setWidth(Integer.parseInt(width));
            }
            if (height != null && !height.equals(""))
            {
                ((OwRecordDocument) getDocument()).getLayoutSettings().setHeight(Integer.parseInt(height));
            }
        }
        catch (NumberFormatException e)
        {
            //just log. do not interrupt execution.
            LOG.debug("OwRecordSubLayout.onAjaxNavigationPanelResize(): Cannot convert the values as int values", e);
        }
    }

    /**
     * AJAX handler for update the "UpdateSubLayoutMenu"
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxUpdateSubLayoutMenu(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        serverSideInclude(getContext().getDesignDir() + "/owrecord/OwRecordMenu.jsp", response_p.getWriter());
    }

    /**
     * Handler for the {@link #AJAX_UPDATE_MENU_REGION}.
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxUpdateMenuRegion(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        serverSideInclude(getContext().getDesignDir() + "/owrecord/recordmenu/MenuRegion.jsp", response_p.getWriter());
    }

    /**
     * Handler for the {@link #AJAX_UPDATE_HOTKEY_REGION}.
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxUpdateHotkeyRegion(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        serverSideInclude(getContext().getDesignDir() + "/owrecord/recordmenu/HotKeyRegion.jsp", response_p.getWriter());
    }

    /**
     * Handler for the {@link #AJAX_UPDATE_SEARCH_TEMPLATES_REGION}.
     * @param request_p
     * @param response_p
     * @throws Exception
     */
    public void onAjaxUpdateSearchTemplatesRegion(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        serverSideInclude(getContext().getDesignDir() + "/owrecord/recordmenu/SearchTemplatesRegion.jsp", response_p.getWriter());
    }

    @Override
    public void onAjaxPanelCollapsedStateChanged(HttpServletRequest request_p, HttpServletResponse response_p)
    {
        String strPanelId = request_p.getParameter("panelId");
        String strCollapsedState = request_p.getParameter("collapsedState");
        boolean colapsedState = Boolean.parseBoolean(strCollapsedState);

        if (ID_NAV_CONTENT_PANEL.equals(strPanelId))
        {
            ((OwRecordDocument) getDocument()).getLayoutSettings().setNavPanelCollapsed(colapsedState);
        }
        else if (ID_PROP_PREVIEW_CONTENT_PANEL.equals(strPanelId))
        {

            ((OwRecordDocument) getDocument()).getLayoutSettings().setPropPreviewPanelCollapsed(colapsedState);
        }
    }

    public boolean isPropPreviewPanelCollapsed()
    {
        OwDynamicLayoutSettings layoutSettings = ((OwRecordDocument) getDocument()).getLayoutSettings();
        return layoutSettings.isPropPreviewPanelCollapsed();
    }

    public boolean isNavPanelCollapsed()
    {
        OwDynamicLayoutSettings layoutSettings = ((OwRecordDocument) getDocument()).getLayoutSettings();
        return layoutSettings.isNavPanelCollapsed();
    }
}