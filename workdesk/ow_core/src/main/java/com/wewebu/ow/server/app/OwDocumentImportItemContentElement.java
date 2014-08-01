package com.wewebu.ow.server.app;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwStreamUtil;

/**
 * Standard <code>{@link OwContentElement}</code> implementation that is wrapped around
 * an <code>{@link OwDocumentImportItem}</code>.<br>
 * Used by the <code>{@link OwDocumentImportItemContentCollection}</code> to represent
 * the single pages (aka content streams) of the content collection.
 * 
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 * @see OwDocumentImportItemContentCollection
 */
public class OwDocumentImportItemContentElement implements OwContentElement
{

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwDocumentImportItemContentElement.class);

    /** the content type of this element */
    protected int m_contentType;

    /** the page number of this element */
    protected int m_page;

    /** the imported document the content collection is based on */
    protected OwDocumentImportItem m_importedDocument;

    /**
     * Create a new <code>OwDocumentImportItemContentElement</code> that represents
     * a page (aka. content stream) of an imported document.
     *
     * @param contentType_p designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @param page_p int Page number to set
     */
    public OwDocumentImportItemContentElement(int contentType_p, int page_p, OwDocumentImportItem importedDocument_p)
    {
        // sanity checks
        if (importedDocument_p == null)
        {
            throw new IllegalArgumentException("OwDocumentImportItemContentElement must not be instantiated without importedDocument_p");
        }
        if ((page_p < 0) || (page_p > importedDocument_p.getContentStreamCount()))
        {
            throw new IndexOutOfBoundsException("OwDocumentImportItemContentElement: invalid page number " + page_p + " != [0 ; " + importedDocument_p.getContentStreamCount() + "]");
        }
        // set the member fields
        m_contentType = contentType_p;
        m_page = page_p;
        m_importedDocument = importedDocument_p;
    }

    /**
     * Returns the URL to the resource where the content can be retrieved. 
     * @return the URL to the resource where the content can be retrieved
     */
    public String getContentURL() throws Exception
    {
        throw new OwObjectNotFoundException("OwDocumentImportItemContentElement.getContentURL: Not implemented or Not supported. m_iContentType=" + String.valueOf(m_contentType) + ", m_iPage=" + String.valueOf(m_page));
    }

    /**
     * Check if getContentStream is based on an native InputStream or OutputStream.<br>
     * <b>NOTE:</b><br>
     * getContentStream must implement for Input- and OutputStream. However for
     * optimization the ContentCollection will tell, whether it is based on an InputStream
     * or an OutputStream.
     * 
     * @return true = InputStream is native, false = OutputStream is native
     */
    public boolean isInputStreamNative()
    {
        return true;
    }

    /**
     * Returns the file path to the content (optional, only available if content is stored locally).<br>
     * <b>NOTE:</b><br>
     * This function is only used for ECM Systems which can only download through files. If a FilePath
     * exists, a stream will also exist. The FilePath is optional, it can not be guranteed to exist.
     * 
     * @return String with path to a file where the content can be found or null if file is not available
     */
    public String getContentFilePath() throws Exception
    {
        return null;
    }

    /**
     * Returns the content from the object. If the document is a multipage document,
     * the first page is retrieved unless otherwise set in setPage(...) NOTE:
     * For performance and optimization reasons both the optional return of an
     * InputStream and writing to the given OutputStream MUST be implemented
     * 
     * @param out_p
     *            optional OutputStream, if set the method writes the
     *            OutputStream and returns null, otherwise it returns an
     *            InputStream
     * 
     * @return content from the object, or throws OwContentNotFoundException if
     *         no content is available
     */
    public InputStream getContentStream(OutputStream out_p) throws Exception
    {
        InputStream result = m_importedDocument.getContentStream(m_page - 1); // page is 1-based, index is 0-based
        if (null == out_p)
        {
            return result;
        }
        else
        {
            // === write to the outputstream
            OwStreamUtil.upload(result, out_p, false);
            out_p.flush();
            result.close();
            return null;
        }
    }

    /**
     * Returns the content object The content representation is of type Object
     * 
     * @return Object to content access
     */
    public Object getContentObject() throws Exception
    {
        throw new OwObjectNotFoundException("OwDocumentImportItemContentElement.getContentObject: Not implemented or Not supported.");
    }

    /** get the content representation, which can be either url, stream or object
      *
      * @return int with content representation type
      */
    public int getContentRepresentation() throws Exception
    {
        return OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM;
    }

    /** retrieve the page number for the given zero based index
     *  NOTE: the Page numbers might be arbitrary in the collection in order to submit specific page content
     *
     * @param lPageIndex_p zero based page index for the pages in the collection
     * @return long actual page number
     */
    public int getPageNumber(int lPageIndex_p) throws Exception
    {
        return lPageIndex_p + 1;
    }

    /** retrieve the MIME type for the specified page number
     *
     * @return String Mimetype
     */
    public String getMIMEType() throws Exception
    {
        return m_importedDocument.getContentMimeType(m_page - 1); // page is 1-based, index is 0-based
    }

    /** retrieve the MIME parameter for the specified page number
     *
     * @return String Mimeparameter
     */
    public String getMIMEParameter() throws Exception
    {
        return m_importedDocument.getContentMimeParameter(m_page - 1); // page is 1-based, index is 0-based
    }

    /**
     * @see com.wewebu.ow.server.ecm.OwContentElement#releaseResources()
     */
    public void releaseResources()
    {
        try
        {
            m_importedDocument.release();
        }
        catch (Exception e)
        {
            LOG.error("Error releasing resources", e);
        }
    }

}