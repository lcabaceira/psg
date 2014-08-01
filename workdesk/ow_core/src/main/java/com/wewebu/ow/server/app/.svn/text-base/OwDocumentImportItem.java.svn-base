package com.wewebu.ow.server.app;

import java.io.InputStream;
import java.util.Map;

/**
 *<p>
 * Interface describing one single document imported by a document importer.<br>
 * A document importer can be used by plugins that gather content like the add document,
 * the save or the checkin plugins to receive the content from the user.
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
public interface OwDocumentImportItem
{

    /**
     * Returns the number of content elements associated with this imported document.
     * If the singleConentStreamImports option is set to true, there is a maximum of
     * 1 content element per document.
     * 
     * @return the number of content elements associated with this imported document
     */
    public int getContentStreamCount();

    /**
     * Returns the ith content stream of the imported document as InputStream. The document
     * importer has to keep this stream available until the clear() method has been called.
     * Can be null if and only if the document does not have any content.
     * 
     * @param i_p zero-based index in the list of imported content streams
     * 
     * @return the ith (0-based) content stream of the imported document as InputStream
     * 
     * @throws Exception if the <code>InputStream</code> of this imported document can not be created
     */
    public InputStream getContentStream(int i_p) throws Exception;

    /**
     * Returns the MIME type of the i-th content stream. Can be null.
     * 
     * @param i_p zero-based index in the list of imported content streams
     * 
     * @return the MIME type of the i-th content stream
     */
    public String getContentMimeType(int i_p);

    /**
     * Returns the MIME parameter of the i-th content stream. Can be null.
     * 
     * @param i_p zero-based index in the list of imported content streams
     * 
     * @return the MIME parameter of the i-th content stream
     */
    public String getContentMimeParameter(int i_p);

    /**
     * Returns a Map with property class to value mappings. These values will be set as
     * initial values of the new object before the &quot;edit properties&quot; dialog is
     * shown. The user will always have the possibility to override these values (if they
     * are not marked as read only in the master plugin).
     * 
     * @return a Map with property class to value mappings
     */
    public Map getPropertyValueMap();

    /**
     * Returns a String that represents this imported Document.<br>
     * Used for example for the stack of imported documents.
     * 
     * @return a String that represents this imported Document
     */
    public String getDisplayName();

    /**
     * Returns the proposed Document name.<br>
     * Nearly every ECM back-end knows some name property or represents item property that is used
     * by this ECM system as a name of the document. This method makes a proposal for that name
     * based on the imported document. May return null.
     * 
     * @return the proposed Document name. Can be null.
     */
    public String getProposedDocumentName();

    /**
     * Returns the full path to a file that can be used for preview of the i-th content
     * stream. Can be null.
     * 
     * @param i_p zero-based index in the list of imported conent streams
     * 
     * @return the full path to a file that can be used for preview of the i-th content stream. Can be null.
     */
    public String getPreviewFilePath(int i_p);

    /**
     * Release this imported document by freeing all temporarily acquired resources like
     * deleting temporary files.<br>
     * <b>Please note:</b><br>
     * This method must be able to deal with<br>
     * &nbsp;&nbsp;(a) multiple calls of this method, and<br>
     * &nbsp;&nbsp;(b) calls to this method AFTER a call to <code>releaseAll</code> of the creating importer.
     * 
     * @throws Exception if there are problems freeing the ocupied resources
     */
    public void release() throws Exception;

    /**
     * Return the ECM classification name for this OwDocumentItem, which should be used 
     * to save the document in the back-end system.
     * <p>Can return <code>null</code>, so the default handling
     * or user action should be processed.</p>
     * @return String name of DocumentClass/ObjectClass, or null
     */
    public String getObjectClassName();

    /**
     * Return a Boolean representing the checkin status
     * for this item, possible values are:
     * <ul>
     * <li><code>Boolean.<i>TRUE</i></code>: create/checkin as <b>major</b> version</li>
     * <li><code>Boolean.<i>FALSE</i></code>: create/checkin as <b>minor</b> version</li>
     * <li><code><b>null</b></code>: action should be request from user/or ignore</li>
     * </ul>
     * @return java.lang.Boolean or null
     */
    public Boolean getCheckinAsMajor();

}