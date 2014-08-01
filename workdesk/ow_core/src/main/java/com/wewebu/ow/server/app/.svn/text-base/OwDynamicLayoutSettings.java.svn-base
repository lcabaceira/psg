package com.wewebu.ow.server.app;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 * <p>
 * Class for holding some of the settings of dynamic layout.
 * <p>

 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 * @since 3.1.0.0
 */
public class OwDynamicLayoutSettings
{
    /** the name of height attribute*/
    private static final String HEIGHT_ATTR_NAME = "height";
    /** the name of width attribute*/
    private static final String WIDTH_ATTR_NAME = "width";

    private static final String PROP_PREVIEW_PANEL_COLLAPSED_ATTR_NAME = "propPreviewPanelCollapsed";
    private static final String NAV_PANEL_COLLAPSED_ATTR_NAME = "navPanelCollapsed";
    /**
     * Logger for this class
     */
    private static final Logger LOG = OwLogCore.getLogger(OwDynamicLayoutSettings.class);
    /** the height of the tree panel*/
    private int m_height = -1;
    /** the width of the navigation panel*/
    private int m_width = 235;
    /**flag for persistence  usage*/
    private boolean m_usePersistency;
    /** application context*/
    private OwAppContext m_context;
    /** current layout settings bag ID*/
    private String m_bagID;

    private boolean m_propPreviewPanelCollapsed = false;
    private boolean m_navPanelCollapsed = false;

    /**
     * Constructor
     * @param usePersistency_p
     * @param context_p
     * @param bagID_p
     */
    public OwDynamicLayoutSettings(boolean usePersistency_p, OwAppContext context_p, String bagID_p)
    {
        m_context = context_p;
        m_bagID = bagID_p;
        this.m_usePersistency = usePersistency_p;
        if (isWrittableAttributeBagAvailable() && m_bagID != null)
        {
            try
            {
                OwAttributeBagWriteable attrBag = getAssociatedAttributeBag();
                m_width = getParameterFromBag(attrBag, WIDTH_ATTR_NAME, m_width);
                m_height = getParameterFromBag(attrBag, HEIGHT_ATTR_NAME, m_height);
                m_propPreviewPanelCollapsed = getParameterFromBag(attrBag, PROP_PREVIEW_PANEL_COLLAPSED_ATTR_NAME, m_propPreviewPanelCollapsed);
                m_navPanelCollapsed = getParameterFromBag(attrBag, NAV_PANEL_COLLAPSED_ATTR_NAME, m_navPanelCollapsed);
            }
            catch (Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    // cause Exception will not be passed to the logger
                    LOG.debug("OwDynamicLayoutSettings: Cannot create the attribute bag, to read/write '" + OwDynamicLayoutSettings.HEIGHT_ATTR_NAME + "' and '" + OwDynamicLayoutSettings.WIDTH_ATTR_NAME
                            + "'. Continue without attribute bag settings.");
                }
            }
        }
    }

    /**
     * Get the associated attribute bag, creates one if doesn't exist.
     * @return - the associated attribute bag
     * @throws Exception
     */
    private OwAttributeBagWriteable getAssociatedAttributeBag() throws Exception
    {
        OwMainAppContext owMainAppContext = (OwMainAppContext) m_context;
        OwNetwork network = owMainAppContext.getNetwork();
        String userId = network.getCredentials().getUserInfo().getUserID();
        OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, m_bagID, userId, true, true);
        return attrBag;
    }

    /**
     * Get the value for the given attribute name.
     * @param attrBag_p - the attribute bag
     * @param paramName_p - the attribute name
     * @param defaultValue_p - default value, in case that anything works bad
     * @return the value from the bag, or default value.
     */
    private int getParameterFromBag(OwAttributeBagWriteable attrBag_p, String paramName_p, int defaultValue_p)
    {
        int result = defaultValue_p;
        try
        {
            Object paramValue = attrBag_p.getAttribute(paramName_p);
            if (paramValue != null)
            {
                result = Integer.parseInt((String) paramValue);
            }
        }
        catch (OwObjectNotFoundException nfex)
        {
            //ignore
        }
        catch (Exception e)
        {
            LOG.debug("OwDynamicLayoutSettings.getParameterFromBag():Cannot load attribute from bag. Continue without attribute bag settins.", e);
        }
        return result;
    }

    /**
     * Get the value for the given attribute name.
     * @param attrBag_p - the attribute bag
     * @param paramName_p - the attribute name
     * @param defaultValue_p - default value, in case that anything works bad
     * @return the value from the bag, or default value.
     */
    private boolean getParameterFromBag(OwAttributeBagWriteable attrBag_p, String paramName_p, boolean defaultValue_p)
    {
        boolean result = defaultValue_p;
        try
        {
            Object paramValue = attrBag_p.getAttribute(paramName_p);
            if (paramValue != null)
            {
                result = Boolean.parseBoolean((String) paramValue);
            }
        }
        catch (OwObjectNotFoundException nfex)
        {
            //ignore
        }
        catch (Exception e)
        {
            LOG.debug("OwDynamicLayoutSettings.getParameterFromBag():Cannot load attribute from bag. Continue without attribute bag settins.", e);
        }
        return result;
    }

    /**
     * Setter for tree height property.
     */
    public void setHeight(int height_p)
    {
        m_height = height_p;
        persistAttributeValue(HEIGHT_ATTR_NAME, m_height);
    }

    /**
     * Save the given value for the given attribute name.
     * @param attrName_p - the attribute name
     * @param value_p - the attribute value
     */
    private void persistAttributeValue(String attrName_p, int value_p)
    {
        if (isWrittableAttributeBagAvailable())
        {
            try
            {
                OwAttributeBagWriteable attrBag = getAssociatedAttributeBag();
                attrBag.setAttribute(attrName_p, "" + value_p);
                attrBag.save();
            }
            catch (Exception e)
            {
                LOG.debug("OwDynamicLayoutSettings.persistAttributeValue():Cannot save attribute to bag. Continue without attribute bag settins.", e);
            }
        }
    }

    /**
     * Save the given value for the given attribute name.
     * @param attrName_p - the attribute name
     * @param value_p - the attribute value
     */
    private void persistAttributeValue(String attrName_p, boolean value_p)
    {
        if (isWrittableAttributeBagAvailable())
        {
            try
            {
                OwAttributeBagWriteable attrBag = getAssociatedAttributeBag();
                attrBag.setAttribute(attrName_p, "" + value_p);
                attrBag.save();
            }
            catch (Exception e)
            {
                LOG.debug("OwDynamicLayoutSettings.persistAttributeValue():Cannot save attribute to bag. Continue without attribute bag settins.", e);
            }
        }
    }

    /**
     * Getter for tree height property.
     * @return the tree height property value.
     */
    public int getHeight()
    {
        return m_height;
    }

    /**
     * Setter for width property.
     * @param width_p - the width.
     */
    public void setWidth(int width_p)
    {
        m_width = width_p;
        persistAttributeValue(WIDTH_ATTR_NAME, m_width);
    }

    /**
     * Getter for width property
     * @return - the width
     */
    public int getWidth()
    {
        return m_width;
    }

    /**
     * Check if there exists the possibility to save writable attribute bags.
     * @return <code>true</code> if attribute bags can be saved
     * @since 3.1.0.0
     */
    private boolean isWrittableAttributeBagAvailable()
    {
        boolean result = false;
        if (m_usePersistency)
        {
            try
            {
                OwMainAppContext owMainAppContext = (OwMainAppContext) m_context;
                OwNetwork network = owMainAppContext.getNetwork();
                String userId = network.getCredentials().getUserInfo().getUserID();
                OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, m_bagID, userId, true, false);
                if (attrBag != null)
                {
                    result = true;
                }
            }
            catch (Exception e)
            {
                LOG.debug("OwDynamicLayoutSettings.isWrittableAttributeBagAvailable():Cannot obtain a writtable attribute bag. Continue without attribute bag settins.", e);
            }
        }
        return result;
    }

    public boolean isPropPreviewPanelCollapsed()
    {
        return m_propPreviewPanelCollapsed;
    }

    public void setPropPreviewPanelCollapsed(boolean m_propPreviewPanelCollapsed)
    {
        this.m_propPreviewPanelCollapsed = m_propPreviewPanelCollapsed;
        persistAttributeValue(PROP_PREVIEW_PANEL_COLLAPSED_ATTR_NAME, m_propPreviewPanelCollapsed);
    }

    public boolean isNavPanelCollapsed()
    {
        return m_navPanelCollapsed;
    }

    public void setNavPanelCollapsed(boolean m_navPanelCollapsed)
    {
        this.m_navPanelCollapsed = m_navPanelCollapsed;
        persistAttributeValue(NAV_PANEL_COLLAPSED_ATTR_NAME, m_navPanelCollapsed);
    }

}