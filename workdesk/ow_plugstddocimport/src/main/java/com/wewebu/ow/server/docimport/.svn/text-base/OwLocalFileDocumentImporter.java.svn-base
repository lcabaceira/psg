package com.wewebu.ow.server.docimport;

import java.io.File;

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
 * Special class representing the document importer which handle local file uploads.<br />
 * The files were uploaded to a temporary location defined by the application.
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
public class OwLocalFileDocumentImporter implements OwDocumentImporter
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwLocalFileDocumentImporter.class);

    /** the current MainAppContext */
    protected OwMainAppContext m_context;

    /** the configuration of this importer */
    protected OwXMLUtil m_config;

    /** the current state of single file import */
    protected boolean m_singleFileImports = true;

    /** the directory for temporary files. null if not yet created. Use getTempDir() to get the temp dir. */
    private String m_tempDir;

    /** The next ID for files in the temp folder */
    private int m_nextTempFileID = 0;

    //    private OwLocalFileDocumentImporterView importerView;

    public void init(OwMainAppContext context_p, OwXMLUtil config_p) throws OwConfigurationException
    {
        // sanity checks
        if (context_p == null)
        {
            throw new IllegalArgumentException("OwLocalFileDocumentImporter.init() must not be called without context_p");
        }
        if (config_p == null)
        {
            throw new IllegalArgumentException("OwLocalFileDocumentImporter.init() must not be called without config_p");
        }
        // set member fields
        m_context = context_p;
        m_config = config_p;
    }

    /**
     * Returns the path to the common temporary folder for this importer. Will create the temporary folder
     * upon first usage.
     * 
     * @return the path to the common temporary folder for this importer
     * 
     * @throws OwConfigurationException if the temporary folder can not be created
     */
    protected String getTempDir() throws OwConfigurationException
    {
        if (m_tempDir == null)
        {
            m_tempDir = (getContext()).createTempDir("Upload");
        }
        return m_tempDir;
    }

    /**
     * Returns a unique ID for a file in the temp folder. This importer uses one common temp folder
     * for multiple views. So this one common folder is handled by this importer itself and not by
     * each view separately. So we need one single counter to be able to assign unique IDs.
     * 
     * @return a unique ID for a file in the temp folder
     */
    protected int getTempFileID()
    {
        return m_nextTempFileID++;
    }

    protected OwMainAppContext getContext()
    {
        if (m_context == null)
        {
            throw new IllegalStateException("OwLocalFileDocumentImporter.getContext() must not be called before this view is initialized");
        }
        return m_context;
    }

    public String getDisplayName()
    {
        return getContext().localize("plug.docimport.OwLocalFileDocumentImporter.DisplayName", "Local file");
    }

    public String getIconURL()
    {
        try
        {
            return getContext().getDesignURL() + m_config.getSafeTextValue("icon", "/images/docimport/local_file.png");
        }
        catch (Exception e)
        {
            LOG.error("Can not get design URL", e);
            return null;
        }
    }

    public void setSingleFileImports(boolean singleFileImports_p)
    {
        m_singleFileImports = singleFileImports_p;
    }

    public OwView getView(int context_p, OwDocumentImporterCallback callback_p)
    {
        return new OwLocalFileDocumentImporterView(this, m_singleFileImports, callback_p);
    }

    public void releaseAll()
    {
        String tempDirToRelease = m_tempDir;
        m_tempDir = null;
        if (tempDirToRelease != null)
        {
            deleteDir(tempDirToRelease);
        }
    }

    /**
     * Removes the contents of a Directory and then the directory itself. Invokes
     * itself recursively if directory contains other directories to remove entire
     * directory subtrees.
     * 
     * @param strDir_p The path to the directory to be removed
     */
    private void deleteDir(String strDir_p)
    {
        File delDir = new File(strDir_p);
        String[] list = delDir.list();

        // delete all files within the dir
        if (list != null)
        {
            for (int i = 0; i < list.length; i++)
            {
                File file = new File(delDir, list[i]);
                if (file.isFile())
                {
                    boolean fileExists = file.exists();
                    if (fileExists && !file.delete())
                    {
                        LOG.warn("OwLocalFileDocumentImporter.deleteDir: Error deleting all files of directory, name = " + strDir_p);
                    }
                }
                else
                {
                    deleteDir(file.getAbsolutePath());
                }
            }
        }

        // delete the directory
        boolean dirExists = delDir.exists();
        if (dirExists && !delDir.delete())
        {
            LOG.warn("OwLocalFileDocumentImporter.deleteDir: Error deleting the directory, name = " + strDir_p);
        }
    }

    public OwView getPostProcessView(int importContext_p, OwObject savedObj_p)
    {
        return null;
    }

    public boolean hasPostProcessView(int importContext_p)
    {
        return false;
    }

    protected void finalize() throws Throwable
    {
        releaseAll();
        super.finalize();
    }
}