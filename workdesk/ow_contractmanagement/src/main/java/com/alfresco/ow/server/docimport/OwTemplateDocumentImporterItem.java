package com.alfresco.ow.server.docimport;

import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * OwTemplateDocumentImporterItem.
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
public class OwTemplateDocumentImporterItem implements OwDocumentImportItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwTemplateDocumentImporterItem.class);

    /** template document to copy */
    private OwObject m_TemplateObject = null;

    /** caches the input stream */
    private InputStream m_InputStream = null;

    /**
     * Constructor 
     * @param templateToCopy_p  template document to copy
     */
    public OwTemplateDocumentImporterItem(OwObject templateToCopy_p)
    {
        m_TemplateObject = templateToCopy_p;
    }

    /**
     * Returns number of content elements from the template object
     */
    public int getContentStreamCount()
    {
        if (null == this.m_TemplateObject)
        {
            return 0;
        }

        // check number of content elements in OwObject
        try
        {
            this.m_TemplateObject.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);
            return 1;
        }
        catch (Exception e)
        {
            LOG.error("Selected template object doesn't have content!", e);
        }

        // fallback to default
        return 0;
    }

    /**
     * Returns an input stream of the selected content.
     */
    public InputStream getContentStream(int i_p) throws Exception
    {
        if (i_p > 0)
        {
            LOG.error("Unable to get content element " + i_p);
        }
        if (null == this.m_InputStream)
        {
            this.m_InputStream = this.m_TemplateObject.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getContentStream(null);
        }
        return this.m_InputStream;
    }

    /**
     * returns the MIME type of the selected content
     */
    public String getContentMimeType(int i_p)
    {
        if (i_p > 0)
        {
            LOG.error("Unable to get content element " + i_p);
        }
        try
        {
            return this.m_TemplateObject.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getMIMEType();
        }
        catch (Exception e)
        {
            LOG.error("Error retrieving MIME type from template document.", e);
        }
        return null;
    }

    /**
     * returns the content MIME parameter for the selected content
     */
    public String getContentMimeParameter(int i_p)
    {
        if (i_p > 0)
        {
            LOG.error("Unable to get content element " + i_p);
        }
        try
        {
            return this.m_TemplateObject.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).getMIMEParameter();
        }
        catch (Exception e)
        {
            LOG.error("Error retrieving MIME parameter from template document.", e);
        }
        return null;
    }

    /**
     * ignored
     */
    public Map getPropertyValueMap()
    {
        // TODO 
        return null;
    }

    /**
     * Returns the display name of the template
     */
    public String getDisplayName()
    {
        return this.m_TemplateObject.getName();
    }

    /**
     * ignored
     */
    public String getProposedDocumentName()
    {
        return null;
    }

    /**
     * ignored
     */
    public String getPreviewFilePath(int i_p)
    {
        return null;
    }

    /**
     * Clean-up - closes the input stream
     */
    public void release() throws Exception
    {
        if (null != this.m_InputStream)
        {
            // close resources
            this.m_TemplateObject.getContentCollection().getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1).releaseResources();

            try
            {
                // close stream
                this.m_InputStream.close();
            }
            catch (Exception e)
            {
                ; // nothing to do here ...
            }
            // stored proceededs
            this.m_InputStream = null;
        }
    }

    /**
     * ignored
     */
    public String getObjectClassName()
    {
        return null;
    }

    /**
     * ignored
     */
    public Boolean getCheckinAsMajor()
    {
        return null;
    }

}
