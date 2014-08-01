package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.util.upload.UploadHelper;

/**
 *<p>
 * Class for handling of Drag&amp;Drop documents, which
 * are temporary saved in temp folder of application.
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
public class OwDragDropDocumentImportItem implements OwDocumentImportItem
{

    /** the path to the temporary folder where the Drag'n'Drop files are stored */
    protected String m_folderPath;

    /** the name of the file that has been dropped */
    protected String m_fileName;

    /** the MIME type of the dropped file */
    protected String m_mimeType;

    /** the MIME parameter of the dropped file */
    protected String m_mimeParameter;

    /**
     * Create a new OwDragDropDocumentImportItem for a given file name in a given
     * temporary Drag'n'Drop folder.
     * 
     * @param folderPath_p the path to the temporary folder where the Drag'n'Drop files are stored
     * @param fileName_p the name of the file that has been dropped
     * 
     * @throws Exception if the MIME type or MIME parameter can not be detected
     */
    public OwDragDropDocumentImportItem(String folderPath_p, String fileName_p) throws Exception
    {
        // set member fields
        m_folderPath = folderPath_p;
        m_fileName = fileName_p;

        m_mimeParameter = "";
        m_mimeType = UploadHelper.getMimeFormatProperty(m_fileName);
        int iMimeIndex = m_mimeType.indexOf(';');
        if (iMimeIndex != -1)
        {
            m_mimeParameter = m_mimeType.substring(iMimeIndex + 1);
            m_mimeType = m_mimeType.substring(0, iMimeIndex);
        }
    }

    public InputStream getContentStream(int i_p) throws Exception
    {
        if (i_p != 0)
        {
            throw new IndexOutOfBoundsException("Invalid stream index.");
        }
        return new DataInputStream(new FileInputStream(new File(m_folderPath, m_fileName)));
    }

    public String getPreviewFilePath(int i_p)
    {
        if (i_p != 0)
        {
            throw new IndexOutOfBoundsException("Invalid stream index.");
        }
        return m_folderPath + File.separatorChar + m_fileName;
    }

    public int getContentStreamCount()
    {
        return 1;
    }

    public String getDisplayName()
    {
        return m_fileName;
    }

    public Map getPropertyValueMap()
    {
        return new HashMap();
    }

    public String getProposedDocumentName()
    {
        String proposedName = m_fileName;
        int pos = proposedName.lastIndexOf('.');
        if (pos >= 0)
        {
            proposedName = proposedName.substring(0, pos);
        }
        return proposedName;
    }

    public void release() throws Exception
    {
        // TODO Auto-generated method stub

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