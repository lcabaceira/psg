package com.wewebu.ow.server.app;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Layout of the tab/plugin areas.
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
public class OwSubLayout extends OwLayout
{
    /**
     *  default navigation panel width.
     *  @since 3.1.0.0
     */
    private static final int DEFAULT_NAVIGATION_PANEL_WIDTH = 235;
    /**
     * Class logger
     * @since 3.1.0.0
     */
    private static Logger LOG = OwLogCore.getLogger(OwSubLayout.class);
    /**
     * the static sub layout JSP
     * @since 3.1.0.0
     */
    private static final String OW_SUB_LAYOUT_JSP = "OwSubLayout.jsp";
    /**
     * the dynamic sub layout JSP
     * @since 3.1.0.0
     */
    private static final String OW_DYNAMIC_SUB_LAYOUT_JSP = "OwSubLayoutDynamic.jsp";

    /** region for the navigation part */
    public static final int NAVIGATION_REGION = 0;
    /** region for the optional menu */
    public static final int MENU_REGION = 1;
    /** main region */
    public static final int MAIN_REGION = 2;
    /** region for the optional hot key region e.g. a recent record list */
    public static final int HOT_KEY_REGION = 3;
    /** 
     * DnD applet region
     */
    public static final int DND_REGION = 4;

    public static final int SECONDARY_REGION = 5;

    /** reference to the cast AppContext */
    protected OwMainAppContext m_MainContext;
    /** application m_Configuration reference */
    protected OwConfiguration m_Configuration;
    /**
     * flag indicating that the layout is using EXTJS
     * @since 3.1.0.0    
     */
    protected boolean m_isDynamicSplitInUse = false;
    /** 
     * flag indicating if splitter position should be saved
     * @since 3.1.0.0
     */
    protected boolean m_saveSplitPosition = true;
    /**
     * Holder for layout settings.
     * @since 3.1.0.0
     */
    protected OwDynamicLayoutSettings m_settings = null;
    /**
     * layout bag prefix
     * @since 3.1.0.0 
     */
    private String m_layoutBagPrefix = null;

    private Map<Integer, Map<String, String>> customRegionAttributes = new HashMap<Integer, Map<String, String>>();
    private ReentrantLock customRegionLock = new ReentrantLock();

    /** 
     * initialize the view after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        // get and cast appcontext
        m_MainContext = (OwMainAppContext) getContext();
        // get application m_Configuration
        m_Configuration = m_MainContext.getConfiguration();
        m_isDynamicSplitInUse = m_Configuration.isDynamicSplitInUse();
        m_layoutBagPrefix = m_Configuration.getLayoutConfigurationBagPrefix();
    }

    /**
     * Create the layout settings object
     * @return the layout settings object
     */
    protected OwDynamicLayoutSettings createLayoutSettings()
    {
        return new OwDynamicLayoutSettings(m_saveSplitPosition, getContext(), getBagID());
    }

    /**
     * Get the created layout settings.
     * @return the {@link OwDynamicLayoutSettings} object
     * @since 3.1.0.0
     */
    protected OwDynamicLayoutSettings getLayoutSettings()
    {
        if (m_settings == null)
        {
            m_settings = createLayoutSettings();
        }
        return m_settings;
    }

    /**
     * Render the view.
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        String jspPath = m_isDynamicSplitInUse ? OW_DYNAMIC_SUB_LAYOUT_JSP : OW_SUB_LAYOUT_JSP;
        serverSideDesignInclude(jspPath, w_p);
        if (m_isDynamicSplitInUse)
        {
            getContext().renderJSInclude("/js/owsublayout.js", w_p);
        }
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
        if (m_isDynamicSplitInUse)
        {
            try
            {
                if (width != null && !width.isEmpty())
                {
                    getLayoutSettings().setWidth(Integer.parseInt(width));
                }
            }
            catch (NumberFormatException e)
            {
                //just log. do not interrupt execution.
                LOG.debug("OwSubLayout.onAjaxNavigationPanelResize(): Cannot convert the values as int values", e);
            }
        }
    }

    /**
     * AJAX handler invoked when the user collapses/un-collapses a panel.
     * @param request_p - the {@link HttpServletRequest} object.
     * @param response_p - the {@link HttpServletResponse} object
     * @since 4.2.0.0
     */
    public void onAjaxPanelCollapsedStateChanged(HttpServletRequest request_p, HttpServletResponse response_p)
    {
        String strPanelId = request_p.getParameter("panelId");
        String strCollapsedState = request_p.getParameter("collapsedState");

        if ("navContentPanel".equals(strPanelId))
        {
            this.setCustomRegionAttribute(OwSubLayout.NAVIGATION_REGION, "data-collapsed", strCollapsedState);
        }
        else if ("secContentPanel".equals(strPanelId))
        {
            this.setCustomRegionAttribute(OwSubLayout.SECONDARY_REGION, "data-collapsed", strCollapsedState);
        }

    }

    /**
     * Get the width for navigation panel
     * @return - the initial width for navigation panel
     * @since 3.1.0.0
     */
    public int getNavigationPanelWidth()
    {
        int result = DEFAULT_NAVIGATION_PANEL_WIDTH;
        if (m_isDynamicSplitInUse)
        {
            result = getLayoutSettings().getWidth();
        }
        return result;
    }

    /**
     * Compute the bag ID.
     * @return the bag ID.
     * @since 3.1.0.0
     */
    protected String getBagID()
    {
        return m_layoutBagPrefix + m_MainContext.getCurrentMasterPluginID();
    }

    public void setCustomRegionAttribute(int regionId_p, String attribute_p, String value_p)
    {
        customRegionLock.lock();
        try
        {
            if (customRegionAttributes == null)
            {
                customRegionAttributes = new HashMap<Integer, Map<String, String>>();
            }
            Map<String, String> regionAttributes = customRegionAttributes.get(regionId_p);
            if (regionAttributes == null)
            {
                regionAttributes = new HashMap<String, String>();
                customRegionAttributes.put(regionId_p, regionAttributes);
            }

            regionAttributes.put(attribute_p, value_p);
        }
        finally
        {
            customRegionLock.unlock();
        }
    }

    public void setCustomRegionAttributes(Map<Integer, Map<String, String>> customRegionAttributes)
    {
        customRegionLock.lock();
        try
        {
            this.customRegionAttributes = customRegionAttributes;
        }
        finally
        {
            customRegionLock.unlock();
        }
    }

    public Map<Integer, Map<String, String>> getCustomRegionAttributes()
    {
        customRegionLock.lock();
        try
        {
            return customRegionAttributes;
        }
        finally
        {
            customRegionLock.unlock();
        }
    }

    /**
     * 
     * @param regionId_p
     * @return HTML formated region DIV container tag attributes values
     * @since 4.2.0.0  
     */
    public synchronized String createRegionCustomAttributes(int regionId_p)
    {
        if (customRegionAttributes != null)
        {
            Map<String, String> attributes = customRegionAttributes.get(regionId_p);
            if (attributes != null)
            {
                StringBuilder attributeString = new StringBuilder();
                Set<Entry<String, String>> attEntries = attributes.entrySet();
                for (Entry<String, String> entry : attEntries)
                {
                    attributeString.append(" ");
                    attributeString.append(entry.getKey());
                    attributeString.append("=\"");
                    attributeString.append(entry.getValue());
                    attributeString.append("\"");
                }
                return attributeString.toString();
            }

        }

        return "";
    }
}