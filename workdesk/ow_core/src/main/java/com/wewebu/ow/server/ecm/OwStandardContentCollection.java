package com.wewebu.ow.server.ecm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwStreamUtil;

/**
 *<p>
 * Standard Implementation of the OwContentCollection interface for just one content type and page.
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
public class OwStandardContentCollection implements OwContentCollection, OwContentElement
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardContentCollection.class);

    /** InputStream to the content */
    private InputStream m_input;
    /** int the one and only content type */
    private int m_iContentType;
    /** int the one and only content type */
    private Collection m_ContentTypes;
    /** int the page number of the one and only page */
    private int m_iPageNumber;
    private String m_mimetype;

    /** construct a content collection with just one page and one content type
     * @param input_p InputStream to the content
     * @param iContentType_p int the one and only content type
     * @param iPageNumber_p int the page number of the one and only page
     */
    public OwStandardContentCollection(InputStream input_p, int iContentType_p, int iPageNumber_p)
    {
        this(input_p, iContentType_p, iPageNumber_p, "");
    }

    /** construct a content collection with just one page and one content type
     * @param input_p InputStream to the content
     * @param iContentType_p int the one and only content type
     * @param iPageNumber_p int the page number of the one and only page
     * @param sMimeType_p MIME Type
     */
    public OwStandardContentCollection(InputStream input_p, int iContentType_p, int iPageNumber_p, String sMimeType_p)
    {
        m_input = input_p;

        m_iContentType = iContentType_p;

        m_ContentTypes = new ArrayList(1);
        m_ContentTypes.add(Integer.valueOf(iContentType_p));

        m_iPageNumber = iPageNumber_p;

        m_mimetype = sMimeType_p;
    }

    /** get the content URL from the object.
      *
      * @return String with URL to the resource where the content can be retrieved
      */
    public String getContentURL() throws Exception
    {
        throw new OwObjectNotFoundException("OwStandardContentCollection.getContentURL: Not implemented or Not supported.");
    }

    /** get the content object
      *  The content representation is of type Object
      *
      * @return Object to content access
      */
    public Object getContentObject() throws Exception
    {
        throw new OwObjectNotFoundException("OwStandardContentCollection.getContentObject: Not implemented or Not supported.");
    }

    /** get the content representation, which can be either url, stream or object
      *
      * @return int with content representation type
      */
    public int getContentRepresentation() throws Exception
    {
        return CONTENT_REPRESENTATION_TYPE_STREAM;
    }

    /** check if getContentStream is based on an native InputStream or OutputStream
     *  NOTE:   getContentStream must implement for Input- and OutputStream. 
     *          However for optimization the ContentCollection will tell,
     *          whether it is based on an InputStream or an OutputStream.
     *
     * @return true = InputStream is native, false = OutputStream is native
     */
    public boolean isInputStreamNative()
    {
        return true;
    }

    /** get a content element for the given type and page
     * 
     * @param iContentType_p int 
     * @param iPage_p int
     * @return OwContentElement
     * @throws Exception
     */
    public OwContentElement getContentElement(int iContentType_p, int iPage_p) throws Exception
    {
        if ((iContentType_p == m_iContentType) && (iPage_p == m_iPageNumber))
        {
            return this;
        }
        else
        {
            String msg = "OwStandardContentCollection.getContentElement: Content Element not found, type = " + m_iContentType + ", page = " + m_iPageNumber;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }
    }

    /** get the content from the object. If the document is a multipage document, the first page is retrieved unless otherwise set in setPage(...)
      * NOTE: For performance and optimization reasons both the optional return of an InputStream and writing to the given OutputStream MUST be implemented
      *
      * @param out_p optional OutputStream, if set the method writes the OutputStream and returns null, otherwise it returns an InputStream
      *
      * @return content from the object, or throws OwContentNotFoundException if no content is available
      */
    public java.io.InputStream getContentStream(OutputStream out_p) throws Exception
    {
        if (null == out_p)
        {
            return m_input;
        }
        else
        {
            try
            {
                // === write to the outputstream   
                OwStreamUtil.upload(m_input, out_p, false);
                out_p.flush();
            }
            finally
            {
                try
                {
                    m_input.close();
                }
                catch (Exception e)
                {
                }
            }
            return null;
        }
    }

    /** get a list of content types used in this object 
     *
     * @return List of int content types
     */
    public java.util.Collection getContentTypes() throws Exception
    {
        return m_ContentTypes;
    }

    /** retrieve the page number for the given zero based index
     *  NOTE: the Page numbers might be arbitrary in the collection in order to submit specific page content
     *
     * @param lPageIndex_p zero based page index for the pages in the collection
     * @return long actual page number
     */
    public int getPageNumber(int lPageIndex_p) throws Exception
    {
        return m_iPageNumber;
    }

    /** retrieve the MIME type for the specified page number
     *
     * @return String Mimetype
     */
    public String getMIMEType() throws Exception
    {
        return m_mimetype;
    }

    /** retrieve the MIME parameter for the specified page number
     *
     * @return String Mimeparameter
     */
    public String getMIMEParameter() throws Exception
    {
        return "";
    }

    /** retrieve the page count
     * @return long page count
     */
    public int getPageCount() throws Exception
    {
        return 1;
    }

    /** get the file path to the content (optional, only available if content is stored locally)
      *
      *  NOTE: This function is only used for ECM Systems which can only download through files
      *        If a FilePath exists, a stream will also exist. The FilePath is optional, it can not be guaranteed to exist.
      *
      * @return String with path to a file where the content can be found or null if file is not available
      */
    public String getContentFilePath() throws Exception
    {
        return null;
    }

    /**
     * @see com.wewebu.ow.server.ecm.OwContentElement#releaseResources()
     */
    public void releaseResources()
    {
        if (m_input != null)
        {
            try
            {
                m_input.close();
            }
            catch (IOException e)
            {
            }
            m_input = null;
        }
    }

}