package com.wewebu.ow.server.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.history.OwStandardHistoryObjectChangeEvent;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.servlets.download.OwHttpContentDisposition;
import com.wewebu.ow.server.ui.OwWebApplication;
import com.wewebu.ow.server.util.OwStreamUtil;

/**
 *<p>
 * OwMultifileDownload Servlet provides one or several object contents
 * for download as a zipped archive or as a single content file depending on the
 * request and init parameters.<br/>
 * The OwMultifileDownload Servlet  processes the request by creating an archive
 * of folders containing content files and/or other folders. Content files rely on {@link OwContentElement}s.
 * The created archive can be zipped or served as single file (if and only if it contains a
 * single content file).<br/>
 * The content archive is  folder-structured , can contain multiple entries with the same name
 * and it is modeled using {@link OwContentArchiveEntry} objects.
 * The archive structure is created by the {@link #createContentArchive(List, OwMainAppContext)}
 * overridable method.
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
public class OwMultifileDownload extends OwAbstractUpload
{
    private static final String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    /**
     *<p>
     * A content archive entry.<br/>
     * Each entry contains 0 or  more files, can be zipped on a {@link ZipOutputStream} under
     * a specified path and can be "served" on a {@link HttpServletResponse}.
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
    protected interface OwContentArchiveEntry
    {

        /**
         * Adds the content represented by this archive entry to the specified {@link ZipOutputStream}
         * @param zipOutputStream_p the {@link ZipOutputStream} to serialize the content on
         * @param path_p the path under which the content should be serialized on the {@link ZipOutputStream}
         * @throws Exception if an error occurs during ZIP serialization (content access related errors
         * should not be treated locally and logged)
         * @since  3.1.0.0
         */
        void zipIt(ZipOutputStream zipOutputStream_p, String path_p) throws Exception;

        /**
         * Serializes the content on the response output stream of the {@link HttpServletResponse} parameter
         * @param request_p HttpServletRequest 
         * @param response_p the HTTP response to serialize the content on
         * @param fileName_p the name under which the content should be serialized
         * @throws OwInvalidOperationException if the response serialization is not possible (for example
         * because this entry contains multiple files)
         */
        void serveSingleFileOn(HttpServletRequest request_p, HttpServletResponse response_p, String fileName_p) throws OwInvalidOperationException;

        /**
         *
         * @return the number of files contained by this entry and all its sub entries
         */
        int fileCount();
    }

    /**
     * An archive entry containing a single content file.
     */
    protected abstract class OwBufferedSingleContentEntry implements OwContentArchiveEntry
    {
        /**
         * @return the number of files contained by this entry and all its sub entries
         */
        public final int fileCount()
        {
            return 1;
        }

        /**
         * (overridable) method
         * @since 3.1.0.0
         * @throws Exception
         */
        protected abstract void downloadFile(ServletOutputStream responseOutputStream_p) throws Exception;

        /**
         * (overridable) MIME type creating hook method.
         * @return a MIME type String
         * @throws Exception
         */
        protected abstract String createMimeType() throws Exception;

        /**
         * Serializes the content  on the {@link HttpServletResponse}
         * parameter using the content type acquired through {@link #createMimeType()}.
         * @param request_p HttpServletRequest 
         * @param response_p the HTTP response to serialize the content on
         * @param fileName_p the name under which the content should be serialized
         * @throws OwInvalidOperationException if the response serialization is not possible (for example
         * because this entry contains multiple files)
         */
        public final void serveSingleFileOn(HttpServletRequest request_p, HttpServletResponse response_p, String fileName_p) throws OwInvalidOperationException
        {
            try
            {

                String mimeType = null;

                try
                {
                    mimeType = createMimeType();
                }
                catch (Exception e)
                {
                    LOG.warn("OwBufferedSingleContentEntry.serveSingleFileOn : Could not get MIME type. ''application/octet-stream'' will be used !", e);
                    mimeType = "application/octet-stream";
                }

                response_p.setContentType(mimeType);

                OwHttpContentDisposition attachmentElement = OwHttpContentDisposition.forRequest(request_p, fileName_p, OwHttpContentDisposition.OwElementType.ATTACHMENT);
                response_p.addHeader(HTTP_HEADER_CONTENT_DISPOSITION, attachmentElement.toString());

                ServletOutputStream responseOutputStream = response_p.getOutputStream();

                try
                {
                    downloadFile(responseOutputStream);
                }
                finally
                {//close streams only where you open/retrieve them
                    responseOutputStream.flush();
                    responseOutputStream.close();
                }
            }
            catch (Exception e)
            {
                String msg = "Could not get content stream for document!";
                LOG.error(msg, e);
                throw new OwInvalidOperationException(msg, e);
            }

        }
    }

    /**
     *An {@link OwContentElement} based implementation of {@link OwMultifileDownload.OwBufferedSingleContentEntry}
     */
    protected class OwContentElementEntry extends OwBufferedSingleContentEntry
    {
        /**The content element this entry relies on*/
        private OwContentElement m_element;
        /**The name of the object this content element belongs to */
        private String m_objectName;
        /**The index of the content element in its residing {@link OwContentCollection}*/
        private int m_elementIndex_p;

        /**
         * Constructor
         * @param element_p the content element this entry relies on
         * @param objectName_p the name of the object this content element belongs to
         * @param elementIndex_p the index of the content element in its residing {@link OwContentCollection}
         */
        public OwContentElementEntry(OwContentElement element_p, String objectName_p, int elementIndex_p)
        {
            super();
            this.m_element = element_p;
            this.m_objectName = objectName_p;
            this.m_elementIndex_p = elementIndex_p;
        }

        /**
        *
        * @return the MIME type of {@link OwContentElement}
        * @throws Exception
        */
        protected String createMimeType() throws Exception
        {
            try
            {
                return m_element.getMIMEType();
            }
            catch (Exception e)
            {
                LOG.warn("Could not get MIME type for document name=[" + m_objectName + "] at content element with index [" + m_elementIndex_p + "]. ''application/octet-stream'' will be used !", e);
                throw e;
            }
        }

        @Override
        protected void downloadFile(ServletOutputStream responseOutputStream_p) throws Exception
        {
            if (m_element.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM)
            {
                if (m_element.isInputStreamNative())
                {
                    InputStream input = m_element.getContentStream(null);
                    try
                    {
                        OwStreamUtil.upload(input, responseOutputStream_p, false);
                    }
                    finally
                    {
                        input.close();
                    }
                }
                else
                {
                    //get the content from the object
                    m_element.getContentStream(responseOutputStream_p);
                }

            }
            else
            {
                LOG.warn(new StringBuilder("OwContentElementEntry.createContentBuffer : currently we just support Content Type Stream in OwContentElement, documentName=[").append(m_objectName).append("] with content element index [")
                        .append(m_elementIndex_p).append("]"));
            }

        }

        public final void zipIt(ZipOutputStream zipOutputStream_p, String fileName_p) throws Exception
        {
            ZipEntry ze = new ZipEntry(fileName_p);
            zipOutputStream_p.putNextEntry(ze);

            if (m_element.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM)
            {
                InputStream fileAsStream = null;
                //if (m_element.isInputStreamNative())
                // {
                fileAsStream = m_element.getContentStream(null);
                // }
                try
                {
                    OwStreamUtil.upload(fileAsStream, zipOutputStream_p, false);
                }
                finally
                {
                    fileAsStream.close();
                }
            }
            zipOutputStream_p.closeEntry();
        }
    }

    /**
     *A folder archive entry.
     *A folder can contain multiple entries under the same name.
     *The name-conflicting entries are indexed when the folder is zipped.
     */
    protected static class OwFolderArchiveEntry implements OwContentArchiveEntry
    {
        /**Child-name to list of entries map*/
        private Map<String, List<OwContentArchiveEntry>> m_childrenTable;

        public OwFolderArchiveEntry()
        {
            m_childrenTable = new HashMap<String, List<OwContentArchiveEntry>>();
        }

        /**
         * Add new child entry to current folder entry.
         * If it is called twice with the same child name, the
         * archive entry will be added as additional content to the child entry.
         * @param childName_p the name under which the entry will be added
         * @param entry_p the entry to add
         */
        public void addChild(String childName_p, OwContentArchiveEntry entry_p)
        {
            List<OwContentArchiveEntry> childEntries = m_childrenTable.get(childName_p);
            if (childEntries == null)
            {
                childEntries = new LinkedList<OwContentArchiveEntry>();
                m_childrenTable.put(childName_p, childEntries);
            }

            childEntries.add(entry_p);
        }

        /**
         * Helper method used add a numeric index to folder child name.
         * The method finds last occurrence of the <code>'.'</code> character
         * and inserts the numeric index prefixed with the  <code>'_'</code> character.
         * If no occurrence of <code>'.'</code> is found the <code>'_'</code>  prefixed index
         * is added to the child name.<br>
         * Example: <br>
         * <code>'manual.doc'</code> becomes <code>'manual_1.doc'</code> <br>
         * <code>'invoice'</code> becomes <code>'invoice_1'</code> <br>
         * @param childName_p the child name to index
         * @return the indexed name
         */
        private String createIndexedChildName(String childName_p, int index_p)
        {
            int dotIndex = childName_p.lastIndexOf('.');
            String prefix;
            String suffix;
            if (dotIndex != -1)
            {
                prefix = childName_p.substring(0, dotIndex);
                suffix = childName_p.substring(dotIndex);
            }
            else
            {
                prefix = childName_p;
                suffix = "";
            }

            return prefix + "_" + index_p + suffix;
        }

        /**
         * Helper method to concatenate child names to certain paths
         * @param path_p
         * @param childName_p
         * @return the concatenated path <code>path_p + "/" + childName_p</code>
         */
        private String createChildZipPath(String path_p, String childName_p)
        {
            if (path_p != null && path_p.length() > 0)
            {
                return path_p + "/" + childName_p;
            }
            else
            {
                return childName_p;
            }
        }

        /**
         *
         * @return the number of files contained by this folder and all its sub entries
         */
        public int fileCount()
        {
            Set<Entry<String, List<OwContentArchiveEntry>>> entries = m_childrenTable.entrySet();
            int fileCount = 0;
            for (Entry<String, List<OwContentArchiveEntry>> childrenMapEntry : entries)
            {
                List<OwContentArchiveEntry> children = childrenMapEntry.getValue();
                for (OwContentArchiveEntry entry : children)
                {
                    fileCount += entry.fileCount();
                }
            }

            return fileCount;
        }

        /**
         *
         * @return <code>true</code> if the entries hierarchy represented
         *          by this folder contains one and only one file
         */
        public boolean isSingleFileEntry()
        {
            return fileCount() == 1;
        }

        /**
         *
         * @return the {@link #m_childrenTable} {@link java.util.Map.Entry} containing the single file
         * @throws OwInvalidOperationException if the folder is not a single file entry
         */
        private Entry<String, List<OwContentArchiveEntry>> getSingleFileMapEntry() throws OwInvalidOperationException
        {
            if (isSingleFileEntry())
            {
                Set<Entry<String, List<OwContentArchiveEntry>>> entries = m_childrenTable.entrySet();
                for (Entry<String, List<OwContentArchiveEntry>> childrenMapEntry : entries)
                {
                    List<OwContentArchiveEntry> children = childrenMapEntry.getValue();
                    for (OwContentArchiveEntry archiveEntry : children)
                    {
                        if (archiveEntry.fileCount() > 0)
                        {
                            return childrenMapEntry;
                        }
                    }
                }

                throw new OwInvalidOperationException("OwFolderArchiveEntry.getContentMimeType:Single file archive but no child file in children table!");

            }
            else
            {
                throw new OwInvalidOperationException("OwFolderArchiveEntry.getContentMimeType:Archive content MIME type retrieval permitted only on sigle file archives! ");
            }
        }

        /**
         * Serializes the content on the response output stream of the {@link HttpServletResponse} parameter
         * as a single file ID this entry is a single-file-entry
         * @param response_p the HTTP response to serialize the content on
         * @param fileName_p the name under which the content should be serialized
         * @throws OwInvalidOperationException if the response serialization is not possible (for example
         * because this entry contains multiple files)
         */
        public void serveSingleFileOn(HttpServletRequest request_p, HttpServletResponse response_p, String fileName_p) throws OwInvalidOperationException
        {
            if (isSingleFileEntry())
            {
                Entry<String, List<OwContentArchiveEntry>> singleFileMapEntry = getSingleFileMapEntry();
                String entryName = singleFileMapEntry.getKey();
                List<OwContentArchiveEntry> contentEntries = singleFileMapEntry.getValue();
                for (OwContentArchiveEntry singleContentEntry : contentEntries)
                {
                    if (singleContentEntry.fileCount() > 0)
                    {
                        singleContentEntry.serveSingleFileOn(request_p, response_p, entryName);
                        break;
                    }
                }
            }
            else
            {
                throw new OwInvalidOperationException("OwFolderArchiveEntry.serveSingleFileOn: Archive content  single file serving is permitted only on sigle file archives! ");
            }
        }

        public void zipIt(ZipOutputStream zipOutputStream_p, String path_p) throws Exception
        {
            Set<Entry<String, List<OwContentArchiveEntry>>> childEntries = m_childrenTable.entrySet();
            for (Entry<String, List<OwContentArchiveEntry>> entry : childEntries)
            {
                int childrenCount = entry.getValue().size();
                if (childrenCount > 1)
                {
                    int j = 0;
                    for (OwContentArchiveEntry contententry : entry.getValue())
                    {
                        String indexedName = createIndexedChildName(entry.getKey(), ++j);
                        String zipPath = createChildZipPath(path_p, indexedName);
                        contententry.zipIt(zipOutputStream_p, zipPath);
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("OwFolderArchiveEntry.zipOn: Z> [" + zipPath + "]");
                        }
                    }
                }
                else if (childrenCount == 1)
                {
                    OwContentArchiveEntry contententry = entry.getValue().get(0);
                    String zipPath = createChildZipPath(path_p, entry.getKey());
                    contententry.zipIt(zipOutputStream_p, zipPath);
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwFolderArchiveEntry.zipOn: Z> [" + zipPath + "]");
                    }
                }
            }
        }
    }

    private static final long serialVersionUID = -7317019966397901048L;

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwMultifileDownload.class);

    public static final String ATTRIBUTE_NAME_FILES_TO_DOWNLOAD = "OwMultifileDownload.files2download";

    /** param name of the zip file name to be downloaded */
    private static final String PARAM_ZIP_FILENAME = "download_file_name";

    /** param name of encoding type to use for filenames */
    private static final String PARAM_FILENAME_ENCODING = "file_name_encoding";

    /** param name how to handle single files (zipped or not) */
    private static final String PARAM_SINGLE_FILE_ZIPPED = "single_file_zipped";

    private String m_encodingFileName = "UTF-8";

    private String m_zipFileName;

    private boolean m_singleFileToBeZipped;

    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Servlet to upload zipped multifile for Alfresco Workdesk.";
    }

    /** Initializes the servlet.
     */
    public void init(ServletConfig config_p) throws ServletException
    {
        super.init(config_p);

        // how to handle single files
        String singleFileZipped = config_p.getInitParameter(PARAM_SINGLE_FILE_ZIPPED);
        if (singleFileZipped != null && singleFileZipped.equals("false"))
        {
            this.m_singleFileToBeZipped = false;
        }
        else
        {
            // default behavior: single files are zipped, too.
            this.m_singleFileToBeZipped = true;
        }

        // read download filename of the ZIP file.
        String fileName = config_p.getInitParameter(PARAM_ZIP_FILENAME);
        if (fileName != null)
        {
            this.m_zipFileName = fileName;
        }

        // encoding to use for filenames.
        String encoding = config_p.getInitParameter(PARAM_FILENAME_ENCODING);
        if (encoding != null)
        {
            this.m_encodingFileName = encoding;
        }

    }

    /**
     * (overridable) Creates folder names for {@link OwObject} based folder archive entries
     * @param objectName_p the name of the object who's content folder is archived
     * @return the folder name
     */
    protected String createArchivedObjectFolderName(String objectName_p)
    {
        String folderName = null;

        if (objectName_p != null)
        {
            // replace special characters in filenames.
            folderName = objectName_p.replaceAll("[<>/\\:*?\"|]", "").replaceAll("Ä", "Ae").replaceAll("ä", "ae").replaceAll("Ö", "Oe").replaceAll("ö", "oe").replaceAll("Ü", "Ue").replaceAll("ü", "ue").replaceAll("ß", "ss");
        }
        else
        {
            folderName = "undef";
        }

        return folderName;
    }

    /**
     * (overridable) Creates file names for {@link OwContentElement} based folder archive entries
     * @param defaultName_p file name prefix to be used if no name can be acquired through {@link OwContentElement}'s MIME parameter
     * @param element_p the content element for which the name is generated
     * @return the name of the file content element archive file
     */
    protected String createArchivedContentFileName(String defaultName_p, OwContentElement element_p)
    {
        String fileName;
        try
        {
            fileName = createFileName(defaultName_p, element_p);
        }
        catch (Exception e)
        {
            fileName = "unknownFile";
        }

        return fileName;
    }

    /**
     * (overridable) Creates the archive that will be served by {@link #processRequest(HttpServletRequest, HttpServletResponse)}
     * @param objectsToZip_p the objects that provide content for the returned archive
     * @param context_p
     * @return a content archive folder
     * @throws IOException
     */
    protected OwFolderArchiveEntry createContentArchive(List<OwObject> objectsToZip_p, OwMainAppContext context_p) throws IOException
    {
        LOG.info("OwMultifileDownload.createContentArchive: Creating Contents Archive ...");

        OwFolderArchiveEntry root = new OwFolderArchiveEntry();

        for (Iterator<OwObject> i = objectsToZip_p.iterator(); i.hasNext();)
        {
            OwObject owObject = i.next();
            String objectName = owObject.getName();
            OwContentCollection contentCollection;
            int pageCount;
            try
            {
                contentCollection = owObject.getContentCollection();
                pageCount = contentCollection.getPageCount();
            }
            catch (Exception e)
            {
                String msg = "OwMultifileDownload.createContentArchive : Unexpected Exception for the document, document name=[" + objectName + "] when retrieving content collection info.";
                LOG.error(msg, e);
                continue;
            }

            if (pageCount > 1)
            {
                try
                {
                    addMultiContentArchiveEntry(root, owObject);
                }
                catch (Exception e)
                {
                    String msg = "OwMultifileDownload.createContentArchive : Unexpected Exception for the document, document name=[" + objectName + "] when creating multiple content archive entry.";
                    LOG.error(msg, e);
                    continue;
                }
            }
            else if (pageCount == 1)
            {
                try
                {
                    addSingleContentEntry(root, owObject);
                }
                catch (Exception e)
                {
                    String msg = "OwMultifileDownload.createContentArchive : Unexpected Exception for the document, document name=[" + objectName + "] when creating single content archive entry.";
                    LOG.error(msg, e);
                    continue;
                }

            }
            else
            {
                String msg = "OwMultifileDownload.createContentArchive : Document has no content element and is discarded, document name=[" + objectName + "]";
                LOG.warn(msg);
            }

        }
        return root;
    }

    /**
     *(overridable) Adds an {@link OwContentArchiveEntry} to the specified parent folder for the
     *specified {@link OwObject}.
     *It is assumed that the passed {@link OwObject} contains a single {@link OwContentElement} in its {@link OwContentCollection}.
     * @param parentFolder_p the folder to add single content entry to
     * @param object_p single content object
     * @throws Exception
     */
    protected void addSingleContentEntry(OwFolderArchiveEntry parentFolder_p, OwObject object_p) throws Exception
    {
        String objectName = object_p.getName();
        String objectFolderName = createArchivedObjectFolderName(objectName);
        OwContentCollection contentCollection = object_p.getContentCollection();
        OwContentElement element;
        try
        {
            element = contentCollection.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);
        }
        catch (Exception e)
        {
            String msg = "OwMultifileDownload.createSingleContentEntry : Unexpected Exception for the document, document name=[" + objectName + "] at content element with index [1]";
            LOG.error(msg, e);
            throw e;
        }

        OwContentElementEntry contentElementEntry = new OwContentElementEntry(element, objectName, 1);
        String elementFileName = createArchivedContentFileName(objectFolderName, element);
        parentFolder_p.addChild(elementFileName, contentElementEntry);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwMultifileDownload.createSingleContentEntry: + file=[" + elementFileName + "]");
        }
    }

    /**
     *(overridable) Adds an {@link OwContentArchiveEntry} to the specified parent folder for the
     *specified {@link OwObject}.
     *It is assumed that the passed {@link OwObject} contains more than one {@link OwContentElement}s in its {@link OwContentCollection}.
     * @param parentFolder_p the folder to add the multiple content entry to
     * @param object_p a multiple content object
     * @throws Exception
     */
    protected void addMultiContentArchiveEntry(OwFolderArchiveEntry parentFolder_p, OwObject object_p) throws Exception
    {
        String objectName = object_p.getName();
        OwContentCollection contentCollection = object_p.getContentCollection();
        String folderName = createArchivedObjectFolderName(objectName);
        OwFolderArchiveEntry objectFolderEntry = new OwFolderArchiveEntry();
        parentFolder_p.addChild(folderName, objectFolderEntry);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwMultifileDownload.createMultiContentArchiveEntry: + folder=[" + folderName + "]");
        }

        int pageCount = contentCollection.getPageCount();
        for (int j = 0; j < pageCount; j++)
        {
            int elementIndex = j + 1;
            OwContentElement element;
            try
            {
                element = contentCollection.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, elementIndex);
            }
            catch (Exception e)
            {
                String msg = "OwMultifileDownload.createMultiContentArchiveEntry: Unexpected Exception for the document, folder name=[" + folderName + "] at content element with index [" + elementIndex + "]";
                LOG.error(msg, e);
                continue;
            }
            OwContentElementEntry contentElementEntry = new OwContentElementEntry(element, folderName, elementIndex);
            String elementFileName = createArchivedContentFileName(folderName, element);
            objectFolderEntry.addChild(elementFileName, contentElementEntry);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwMultifileDownload.createMultiContentArchiveEntry: + file=[" + folderName + "/" + elementFileName + "]");
            }
        }

    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * Creates an archive via {@link #createContentArchive(List, OwMainAppContext)} and serializes it to
     * the {@link HttpServletResponse} parameter.
     * @param request_p servlet request
     * @param response_p servlet response
     */
    protected void processRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        String userAgent = request_p.getHeader("User-Agent");
        LOG.info(userAgent);
        try
        {
            // get session object and context
            HttpSession session = request_p.getSession();
            OwMainAppContext context = (OwMainAppContext) session.getAttribute(OwWebApplication.CONTEXT_KEY);

            //response_p.setHeader("Cache-Control", "no-cache"); //HTTP 1.1 don't use this header, because IE cannot handle it with SSL connections
            //response_p.setHeader("Pragma", "no-cache"); //HTTP 1.0
            response_p.setHeader("Expires", "-1");
            response_p.setCharacterEncoding("UTF-8");

            if (context == null)
            {
                LOG.error("OwMultifileDownload.processRequest: Could not get Alfresco Workdesk context.");
                tidyUp(session);
                return;
            }

            // fetch the list of files
            @SuppressWarnings("unchecked")
            List<OwObject> filesToZip = (List<OwObject>) session.getAttribute(ATTRIBUTE_NAME_FILES_TO_DOWNLOAD);

            // verify that we have at least one valid file to download
            if (filesToZip == null || filesToZip.size() == 0)
            {
                // should never happen when called by OwDownloadAsZipDocumentFunction - because there the list is verified.
                String errorMsg = context.localize("servlets.OwMultifileDownload.nofilesfound", "No valid files found to download.");
                writeErrorMessagePage(response_p, errorMsg);
                tidyUp(session);
                return;
            }

            List<OwObject> filesToZipWithContent = new ArrayList<OwObject>();

            for (Iterator<OwObject> i = filesToZip.iterator(); i.hasNext();)
            {
                OwObject owObject = i.next();
                boolean hasContent;
                try
                {
                    //check if has content
                    hasContent = owObject.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                    if (hasContent)
                    {
                        filesToZipWithContent.add(owObject);
                    }

                }
                catch (Exception e)
                {
                    LOG.error("Cannot obtain the content status from the object", e);
                    throw new ServletException(e);
                }

            }

            ServletOutputStream servletOutStream = response_p.getOutputStream();
            ZipOutputStream zipOutputStream = null;

            try
            {
                // build archive only
                if (filesToZipWithContent.size() > 0)
                {
                    OwFolderArchiveEntry archive = createContentArchive(filesToZipWithContent, context);

                    if (archive.isSingleFileEntry() && !m_singleFileToBeZipped)
                    {
                        //download single file
                        archive.serveSingleFileOn(request_p, response_p, "");
                    }
                    else
                    {
                        response_p.setContentType("application/zip");
                        response_p.addHeader(HTTP_HEADER_CONTENT_DISPOSITION, "attachment;filename=\"" + this.m_zipFileName + "\"");

                        zipOutputStream = new ZipOutputStream(servletOutStream);
                        zipOutputStream.setEncoding(m_encodingFileName);
                        archive.zipIt(zipOutputStream, "");//start from root
                    }
                }

            }
            finally
            {
                try
                {
                    if (zipOutputStream != null)
                    {
                        //close streams
                        zipOutputStream.flush();
                        zipOutputStream.close();
                    }
                }
                catch (IOException ex)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Error during closing of ZIP stream handler", ex);
                    }
                }
                try
                {
                    if (servletOutStream != null)
                    {
                        servletOutStream.flush();
                        servletOutStream.close();
                    }
                }
                catch (IOException ex)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Error during closing native servlet stream", ex);
                    }
                }
                tidyUp(session);//don't forget to clean session
            }

            try
            {// signal event for history
                context.getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_DOWNLOAD, new OwStandardHistoryObjectChangeEvent(filesToZipWithContent, null), OwEventManager.HISTORY_STATUS_OK);
            }
            catch (Exception e)
            {
                LOG.error("OwMultifileDownload.processRequest: Writing history failed!", e);
            }
        }
        catch (Exception e)
        {
            writeErrorMessagePage(response_p, e.toString());
        }

    }

    private void tidyUp(HttpSession session_p)
    {
        // clear zip file list attribute immediately
        session_p.setAttribute(ATTRIBUTE_NAME_FILES_TO_DOWNLOAD, null);
    }

    /**
     * Write message to HTTP response
     * @param response_p
     * @param errorMsg_p
     * @throws IOException
     */
    private void writeErrorMessagePage(HttpServletResponse response_p, String errorMsg_p) throws IOException
    {
        // set content type and header immediately
        response_p.setContentType("text/html");

        PrintWriter writer = response_p.getWriter();
        writer.write("<html><body>");
        writer.write(errorMsg_p);
        writer.write("<br><br>");
        writer.write("<FORM><INPUT TYPE=\"button\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"> </FORM>");
        writer.write("</body></html>");
        response_p.flushBuffer();
    }

}