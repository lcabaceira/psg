package com.wewebu.ow.server.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.util.upload.UploadHelper;

/**
 *<p>
 * The <code>OwStandardDocumentImportItem</code> is a default implementation
 * that can be used if collecting the imported file(s), identified by a numerical
 * ID, in some temporary directory.
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
public class OwStandardDocumentImportItem implements OwDocumentImportItem
{

    /** the temporary directory where the import file is stored */
    protected String m_tempDir;

    /** the ID of the imported file. */
    protected int m_id;

    /** the original file name */
    protected String m_originalFileName;

    /** the MIME type of the imported file */
    protected String m_mimeType;

    /** the MIME parameter of the imported file */
    protected String m_mimeParameter;

    /**
     * Create a new <code>OwStandardDocumentImportItem</code> with the given parameters.
     * 
     * @param tempDir_p the directory where the imported file is located
     * @param id_p the numeric ID of the imported file. The file must be stored in the
     *             temporary directory by this ID as file name without a file extension,
     *             e.g. /foo/bar/123 where /foo/bar is the temp directory and 123 is the
     *             numeric ID
     * @param originalFileName_p the original file name of the imported file
     * 
     * @throws Exception if the MIME type can not be detected
     */
    public OwStandardDocumentImportItem(String tempDir_p, int id_p, String originalFileName_p) throws Exception
    {
        m_tempDir = tempDir_p;
        m_id = id_p;
        m_originalFileName = originalFileName_p;
        if (m_originalFileName != null)
        {
            ArrayList fnlist = new ArrayList();
            fnlist.add(m_originalFileName);
            m_mimeParameter = "";
            m_mimeType = (String) UploadHelper.getDocumentFormatProperty(fnlist);
            int iMimeIndex = m_mimeType.indexOf(';');
            if (iMimeIndex != -1)
            {
                m_mimeParameter = m_mimeType.substring(iMimeIndex + 1);
                m_mimeType = m_mimeType.substring(0, iMimeIndex);
            }
        }
    }

    public InputStream getContentStream(int i_p) throws Exception
    {
        if (i_p != 0)
        {
            throw new IndexOutOfBoundsException("Invalid stream index: " + i_p);
        }
        return new DataInputStream(new FileInputStream(new File(m_tempDir, Integer.toString(m_id))));
    }

    public String getPreviewFilePath(int i_p)
    {
        if (i_p != 0)
        {
            throw new IndexOutOfBoundsException("Invalid stream index: " + i_p);
        }
        return m_tempDir + File.separatorChar + Integer.toString(m_id);
    }

    public int getContentStreamCount()
    {
        return 1;
    }

    public String getDisplayName()
    {
        return m_originalFileName;
    }

    public Map getPropertyValueMap()
    {
        return new HashMap();
    }

    public String getProposedDocumentName()
    {
        String proposedName = m_originalFileName;
        int pos = proposedName.lastIndexOf('.');
        if (pos >= 0)
        {
            proposedName = proposedName.substring(0, pos);
        }
        return proposedName;
    }

    public void release() throws Exception
    {
        File f = new File(m_tempDir, Integer.toString(m_id));
        f.delete();
    }

    public String getContentMimeParameter(int i_p)
    {
        return m_mimeParameter;
    }

    public String getContentMimeType(int i_p)
    {
        return m_mimeType;
    }

    public Boolean getCheckinAsMajor()
    {
        return null;
    }

    public String getObjectClassName()
    {
        return null;
    }

}