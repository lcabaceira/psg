package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Base interface for Content Collections. Content Collections hold several content types with several pages each.<br/>
 * <b>Each page can contain a different content representation, which are URL, Stream and Object.<br/>
 * The count of all available pages is returned with the zero based index function getPageCount().
 * However, the actual page number must be retrieved with the one based function getPageNumber().<br/><br/>
 * e.g.: the ContentCollection could hold content for 3 pages with page numbers 5, 19, 201.<br/><br/>
 * Calls to getPageNumber would result as follows:
 * <LI>getPageNumber(0)    = 5</LI>
 * <LI>getPageNumber(1)    = 19</LI>
 * <LI>getPageNumber(2)    = 201</LI>
 * <br/><br/>
 * For the getter methods to work, you must use the page number rather than the page index.<br/><br/>
 * e.g.: getContentURL(CONTENT_TYPE_DOCUMENT,getPageNumber(0) would retrieve the document 
 * content of the page number 5, which has index 0.<br/><br/>
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
public interface OwContentCollection
{
    // === content type base definitions
    /** content type Document enumerator */
    public static final int CONTENT_TYPE_DOCUMENT = 0x0001;
    /** content type Annotation enumerator */
    public static final int CONTENT_TYPE_ANNOTATION = 0x0002;
    /** content type Security enumerator */
    public static final int CONTENT_TYPE_SECURITY = 0x0003;

    /** start value for user defined content types */
    public static final int CONTENT_TYPE_USER_START = 0x1000;

    // === content representation types

    /** content representation type */
    public static final int CONTENT_REPRESENTATION_TYPE_NONE = 0x0001;
    /** content representation type */
    public static final int CONTENT_REPRESENTATION_TYPE_STREAM = 0x0002;
    /** content representation type */
    public static final int CONTENT_REPRESENTATION_TYPE_URL = 0x0004;
    /** content representation type */
    public static final int CONTENT_REPRESENTATION_TYPE_OBJECT = 0x0008;

    /** start value for user defined content representation types */
    public static final int CONTENT_REPRESENTATION_TYPE_USER_START = 0x1000;

    /** get a content element for the given type and page
     * 
     * @param iContentType_p int 
     * @param iPage_p int
     * @return OwContentElement
     * @throws Exception
     */
    public abstract OwContentElement getContentElement(int iContentType_p, int iPage_p) throws Exception;

    /** retrieve the page count
     *  NOTE:   You can iterate through all pages 
     *          by looping a index value from zero to getPageCount 
     *          and use getPageNumber to resolve the actually page number.
     *
     * @return long page count
     */
    public abstract int getPageCount() throws Exception;

    /** get a list of content types used in this object 
    *
    * @return List of int content types
    */
    public abstract java.util.Collection getContentTypes() throws Exception;

}