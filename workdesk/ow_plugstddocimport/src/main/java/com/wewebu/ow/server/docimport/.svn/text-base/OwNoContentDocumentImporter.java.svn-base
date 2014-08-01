package com.wewebu.ow.server.docimport;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.docimport.log.OwLog;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Special class representing the document importer which handle no content.
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
public class OwNoContentDocumentImporter implements OwDocumentImporter
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwNoContentDocumentImporter.class);

    /** the current MainAppContext */
    protected OwMainAppContext m_context;

    /** the configuration of this importer */
    protected OwXMLUtil m_config;

    public void init(OwMainAppContext context_p, OwXMLUtil config_p) throws OwConfigurationException
    {
        // sanity checks
        if (context_p == null)
        {
            throw new IllegalArgumentException("OwNoContentDocumentImporter.init() must not be called without context_p");
        }
        if (config_p == null)
        {
            throw new IllegalArgumentException("OwNoContentDocumentImporter.init() must not be called without config_p");
        }
        // set member fields
        m_context = context_p;
        m_config = config_p;
    }

    protected OwMainAppContext getContext()
    {
        if (m_context == null)
        {
            throw new IllegalStateException("OwNoContentDocumentImporter.getContext() must not be called before this view is initialized");
        }
        return m_context;
    }

    public String getDisplayName()
    {
        return getContext().localize("plug.docimport.OwNoContentDocumentImporter.DisplayName", "No content");
    }

    public String getIconURL()
    {
        try
        {
            return getContext().getDesignURL() + m_config.getSafeTextValue("icon", "/images/docimport/no_file.png");
        }
        catch (Exception e)
        {
            LOG.error("Can not get design URL", e);
            return null;
        }
    }

    public void setSingleFileImports(boolean singleFileImports_p)
    {
        // we can silently ignore this. A document without any content has always at most
        // one content stream (namely: zero).
    }

    public OwView getView(int context_p, OwDocumentImporterCallback callback_p)
    {
        return new OwNoContentDocumentImporterView(this, callback_p);
    }

    public void releaseAll()
    {
        // nothing to do here.
        // We not not create temporary files in this no-content importer.
    }

    public OwView getPostProcessView(int importContext_p, OwObject savedObj_p)
    {
        return null;
    }

    public boolean hasPostProcessView(int importContext_p)
    {
        return Boolean.FALSE.booleanValue();
    }

    protected void finalize() throws Throwable
    {
        releaseAll();
        super.finalize();
    }

}