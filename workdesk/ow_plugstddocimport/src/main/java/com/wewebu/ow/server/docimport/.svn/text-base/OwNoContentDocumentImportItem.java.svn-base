package com.wewebu.ow.server.docimport;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.app.OwDocumentImportItem;

/**
 *<p>
 * A class representing a no content OwDocumentImportItem, which is used by the DocumentImporter interface.
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
public class OwNoContentDocumentImportItem implements OwDocumentImportItem
{

    /** the display name of the imported document */
    protected String m_displayName;

    /**
     * Create a new OwNoContentDocumentImportItem for a empty document with a given display name
     * 
     * @param displayName_p the display name of the empty document
     */
    public OwNoContentDocumentImportItem(String displayName_p)
    {
        m_displayName = displayName_p;
    }

    public InputStream getContentStream(int i_p) throws Exception
    {
        return null;
    }

    public String getPreviewFilePath(int i_p)
    {
        return null;
    }

    public int getContentStreamCount()
    {
        return 0;
    }

    public String getDisplayName()
    {
        return m_displayName;
    }

    public Map getPropertyValueMap()
    {
        return new HashMap();
    }

    public void release() throws Exception
    {
        // nothing to do here.
        // we do not create temporary files
    }

    public String getProposedDocumentName()
    {
        return null;
    }

    public String getContentMimeParameter(int i_p)
    {
        return null;
    }

    public String getContentMimeType(int i_p)
    {
        return null;
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