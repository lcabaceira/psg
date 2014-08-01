package com.alfresco.ow.server.docimport;

import static org.alfresco.wd.ui.conf.OwUniformParamConfiguration.ATT_NAME;
import static org.alfresco.wd.ui.conf.OwUniformParamConfiguration.ELEM_PARAM;
import static org.alfresco.wd.ui.conf.OwUniformParamConfiguration.ELEM_PARAM_LIST;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwTemplateDocumentImporter.
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
public class OwTemplateDocumentImporter implements OwDocumentImporter
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwTemplateDocumentImporter.class);

    public static final String CONF_TEMPLATE_DIR_PATH = "TemplateDirectoryPath";

    public static final String CONF_LIST_VIEW_COLUMNS = "ListViewColumns";

    private OwMainAppContext m_context;
    private OwXMLUtil m_config;

    private String templateDirectoryPath;
    private List<OwFieldColumnInfo> listViewColumns;

    @SuppressWarnings("unchecked")
    public void init(OwMainAppContext context_p, OwXMLUtil config_p) throws OwConfigurationException
    {
        // sanity checks
        if (context_p == null)
        {
            throw new IllegalArgumentException("OwTemplateDocumentImporter.init() must not be called without context_p");
        }
        if (config_p == null)
        {
            throw new IllegalArgumentException("OwTemplateDocumentImporter.init() must not be called without config_p");
        }
        // set member fields
        m_context = context_p;
        m_config = config_p;
        List<OwXMLUtil> config = m_config.getSafeUtilList(null);

        for (OwXMLUtil conf : config)
        {
            if (ELEM_PARAM.equals(conf.getName()))
            {
                if (CONF_TEMPLATE_DIR_PATH.equals(conf.getSafeStringAttributeValue(ATT_NAME, null)))
                {
                    templateDirectoryPath = conf.getSafeTextValue(null);
                }
            }
            if (ELEM_PARAM_LIST.equals(conf.getName()))
            {
                if (CONF_LIST_VIEW_COLUMNS.equals(conf.getSafeStringAttributeValue(ATT_NAME, null)))
                {
                    listViewColumns = createListViewColumns(conf.getSafeStringList());
                }
            }
        }

        //TODO: Remove in next release, keep for backwards compatibility
        if (null == templateDirectoryPath)
        {

            templateDirectoryPath = this.m_config.getSafeTextValue(CONF_TEMPLATE_DIR_PATH, null);
        }
        if (listViewColumns == null)
        {
            listViewColumns = createListViewColumns(this.m_config.getSafeStringList(CONF_LIST_VIEW_COLUMNS));
        }

        if (null == templateDirectoryPath)
        {
            throw new OwConfigurationException("Invalid configuration: TemplateDirectoryPath must be configured to use this plug-in");
        }
    }

    protected List<OwFieldColumnInfo> createListViewColumns(List<String> columnNames) throws OwConfigurationException
    {
        List<OwFieldColumnInfo> columns = new LinkedList<OwFieldColumnInfo>();
        for (String propertyName : columnNames)
        {
            // get display name
            OwFieldDefinition fielddef = null;
            try
            {
                fielddef = getContext().getNetwork().getFieldDefinition(propertyName, null);
            }
            catch (Exception e)
            {
                // just set a warning when property load failed, we still keep continue working at least with the remaining properties
                String msg = "OwTemplateDocumentImporter.init: Could not resolve property for " + CONF_LIST_VIEW_COLUMNS + ", propertyname = " + propertyName;
                if (LOG.isDebugEnabled())
                {
                    LOG.warn(msg, e);
                }
                else
                {
                    LOG.warn(msg);
                }
            }

            // add column info
            columns.add(new OwStandardFieldColumnInfo(fielddef));
        }
        return columns;
    }

    /**
     * Returns the list view column header definition from the configuration.
     * @return the list view column header definition from the configuration.
     */
    public List<OwFieldColumnInfo> getListViewColumns()
    {
        return listViewColumns;
    }

    /**
     * Checks and returns the context.
     * @return the context
     */
    protected OwMainAppContext getContext()
    {
        if (m_context == null)
        {
            throw new IllegalStateException("OwTemplateDocumentImporter.getContext() must not be called before this view is initialized");
        }
        return m_context;
    }

    /**
     * Returns ICON URL 
     */
    public String getIconURL()
    {
        try
        {
            return getContext().getDesignURL() + m_config.getSafeTextValue("icon", "/images/ContractManagement/select-template.png");
        }
        catch (Exception e)
        {
            LOG.error("Can not get design URL", e);
            return null;
        }
    }

    /**
     * returns the display name of the plugin
     */
    public String getDisplayName()
    {
        return getContext().localize("plug.docimport.OwTemplateDocumentImporter.DisplayName", "Template");
    }

    /**
     * ignored
     */
    public void setSingleFileImports(boolean singleFileImports_p)
    {
        // ignored
    }

    /**
     * Returns a new view plugin instance
     */
    public OwView getView(int context_p, OwDocumentImporterCallback callback_p)
    {
        return new OwTemplateDocumentImporterView(this, callback_p, getTemplateDirectoryPath(), getListViewColumns());
    }

    /**
     * ignored
     */
    public void releaseAll()
    {
        // nothing to do here
    }

    /**
     * ignored
     */
    public boolean hasPostProcessView(int importContext_p)
    {
        return false;
    }

    /**
     * ignored
     */
    public OwView getPostProcessView(int importContext_p, OwObject savedObj_p)
    {
        return null;
    }

    /**
     * Get the string which represents the configured directory path for templates. 
     * @return String path
     */
    public String getTemplateDirectoryPath()
    {
        return this.templateDirectoryPath;
    }
}
