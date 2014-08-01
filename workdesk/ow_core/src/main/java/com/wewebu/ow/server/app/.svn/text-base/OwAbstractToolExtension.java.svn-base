package com.wewebu.ow.server.app;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A generic extension of the tool item.<br/>
 * Implements the common configuration issues for an extension.
 * Extension <code>title</code> and <code>description</code> can be localized by
 * using the following constructions in <code>ow_localize_??</code> files:
 * <ul>
 *  <li><b>extension.&lt;extension_id&gt;.title</b>
 *  <li><b>extension.&lt;extension_id&gt;.description</b>
 * </ul>
 * where the <code>&lt;extension_id&gt;</code> is the configured id for the extension, in ow_plugins.xml file. 
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
public abstract class OwAbstractToolExtension implements OwToolExtension, OwToolViewItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwAbstractToolExtension.class);

    /** extension prefix*/
    protected static final String EXTENSION_PREFIX = "extension.";
    /** description suffix*/
    protected static final String DESCRIPTION_SUFFIX = ".description";
    /** title suffix*/
    protected static final String TITLE_SUFFIX = ".title";
    /** the application context*/
    protected OwMainAppContext m_context;
    /** the configuration node*/
    protected OwXMLUtil m_confignode;
    /** the icon path (default or the configured path)*/
    protected String m_icon;
    /** the big icon path (default or the configured path)*/
    protected String m_bigIcon;
    /** the title*/
    protected String m_title;
    /** the description */
    protected String m_description;
    /** the path to the JSP file*/
    protected String m_jspForm;
    /** the extension id*/
    protected String m_id;

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwToolExtension#init(com.wewebu.ow.server.util.OwXMLUtil, com.wewebu.ow.server.app.OwMainAppContext)
     */
    public void init(OwXMLUtil confignode_p, OwMainAppContext context_p) throws Exception
    {
        m_context = context_p;
        m_confignode = confignode_p;
        m_id = m_confignode.getSafeTextValue("id", getDefaultId());
        m_icon = m_confignode.getSafeTextValue("icon", getDefaultIcon());
        m_bigIcon = m_confignode.getSafeTextValue("iconbig", getDefaultBigIcon());
        m_title = localize(EXTENSION_PREFIX, m_confignode.getSafeTextValue("Name", getDefaultTitle()), TITLE_SUFFIX);
        m_description = localize(EXTENSION_PREFIX, m_confignode.getSafeTextValue("Description", getDefaultDescription()), DESCRIPTION_SUFFIX);
        m_jspForm = m_confignode.getSafeTextValue("JspForm", null);
    }

    /**
     * Get the default description for this extension.
     * @return - the default description for this extension.
     */
    protected String getDefaultDescription()
    {
        return "";
    }

    /**
     * Gets the default title for this extension.
     * @return the default title for this extension.
     */
    protected String getDefaultTitle()
    {
        return "";
    }

    /**
     * Get the default big icon path.
     * @return - the default big icon path 
     */
    protected abstract String getDefaultBigIcon();

    /**
     * Get the default icon, if nothing is configured
     * @return - the default icon path.
     */
    protected abstract String getDefaultIcon();

    /**
     * Returns the default id for this extension. Used for backward compatibility configuration
     * @return - the default id.
     */
    protected String getDefaultId()
    {
        return this.getClass().getName();
    }

    /** 
     * Localize a text based on the extension id and a given prefix and suffix.
     * @param extensionPrefix_p - the extension prefix
     * @param defaultValue_p - the default value, used in case the localization fails.
     * @param suffix_p - the suffix. 
     * @return - the localized {@link String} object, or the defaultValue_p
     */
    private String localize(String extensionPrefix_p, String defaultValue_p, String suffix_p)
    {
        String key = extensionPrefix_p + m_id + suffix_p;
        return m_context.localize(key, defaultValue_p);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem#getIcon()
     */
    public String getIcon()
    {
        return createURL(m_icon);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem#getBigIcon()
     */
    public String getBigIcon()
    {
        return createURL(m_bigIcon);
    }

    /**
     * Design relative URL helper method.
     * @param designPath_p
     * @return a design relative URL based on the give path
     */
    private String createURL(String designPath_p)
    {
        String theURL = "";
        try
        {
            theURL = m_context.getDesignURL() + designPath_p;
        }
        catch (Exception e)
        {
            LOG.error("Could not get design URL!", e);
        }
        return theURL;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem#getDescription()
     */
    public String getDescription()
    {
        return m_description;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem#getTitle()
     */
    public String getTitle()
    {
        return m_title;
    }

    public String getId()
    {
        return m_id;
    }

}
