package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Base interface for Content Elements. Content Elements hold a piece of date, page etc.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwContentElement
{
    /** get the file path to the content (optional, only available if content is stored locally)
      *
      *  NOTE: This function is only used for ECM Systems which can only download through files
      *        If a FilePath exists, a stream will also exist. The FilePath is optional, it can not be guaranteed to exist.
      *
      * @return String with path to a file where the content can be found or null if file is not available
      */
    public abstract String getContentFilePath() throws Exception;

    /** get the content URL 
      *  The content representation is of type URL (String)
      *
      * @return String with URL to the resource where the content can be retrieved
      */
    public abstract String getContentURL() throws Exception;

    /** get the content object
      *  The content representation is of type Object
      *
      * @return Object to content access
      */
    public abstract Object getContentObject() throws Exception;

    /** get the content representation, which can be either URL, stream or object
      *
      * @return int with content representation type
      */
    public abstract int getContentRepresentation() throws Exception;

    /** check if getContentStream is based on an native InputStream or OutputStream
     *  NOTE:   getContentStream must implement for Input- and OutputStream. 
     *          However for optimization the ContentCollection will tell,
     *          whether it is based on an InputStream or an OutputStream.
     *
     * @return true = InputStream is native, false = OutputStream is native
     */
    public abstract boolean isInputStreamNative();

    /** get the content from the object. If the document is a multipage document, the first page is retrieved unless otherwise set in setPage(...)
      *  The content representation is of type Stream
      *
      *  NOTE: For performance and optimization reasons both the optional return of an InputStream and writing to the given OutputStream MUST be implemented
      *
      * @param out_p optional OutputStream, if set the method writes the OutputStream and returns null, otherwise it returns an InputStream
      *
      * @return content from the object, or throws OwContentNotFoundException if no content is available
      */
    public abstract java.io.InputStream getContentStream(java.io.OutputStream out_p) throws Exception;

    /** retrieve the page number for the given zero based index
     *  NOTE: the Page numbers might be arbitrary in the collection in order to submit specific page content
     *
     * @return long actual page number
     */
    public abstract int getPageNumber(int lPageIndex_p) throws Exception;

    /** retrieve the MIME type for the specified page number
     *
     * @return String Mimetype
     */
    public abstract String getMIMEType() throws Exception;

    /** retrieve the MIME parameter for the specified page number
     *
     * @return String MMIME Parameter
     */
    public abstract String getMIMEParameter() throws Exception;

    /**
     * method is called e.g. by GetContent servlet after the content is consumed.
     * Can be used to release resources e.g. streams, Interactions, Connections etc. AFTER content is fetched.
     */
    public abstract void releaseResources();
}