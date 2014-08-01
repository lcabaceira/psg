package com.wewebu.ow.server.servlets;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwStandardDocumentImportItem;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwWebApplication;
import com.wewebu.ow.server.util.OwExceptionManager;

/**
 *<p>
 * Drag&Drop Servlet performs an upload of documents.
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
public class OwDragDropContent extends HttpServlet
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwDragDropContent.class);

    /**
     *  generated serial version uid
     */
    private static final long serialVersionUID = 5837220102300078270L;

    /**
     * Constructor of the object.
     */
    public OwDragDropContent()
    {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy()
    {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doDelete method of the servlet. <br>
     *
     * This method is called when a HTTP delete request is received.
     * 
     * @param request_p the request send by the client to the server
     * @param response_p the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doDelete(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {

        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request_p the request send by the client to the server
     * @param response_p the response send by the server to the client
     */
    public void doGet(HttpServletRequest request_p, HttpServletResponse response_p)
    {
        try
        {
            processRequest(request_p, response_p);
        }
        catch (Exception e)
        {
            // throw something
        }
    }

    @Override
    public void doPost(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        try
        {
            processRequest(request_p, response_p);
        }
        catch (Exception e)
        {
            LOG.error("OwDragDropContent.doPost Exception", e);
            OwExceptionManager.PrintCatchedException(Locale.ENGLISH, e, response_p.getWriter(), "OwOwWebApplication_EXCEPTION");
        }
    }

    /**
     * The doPut method of the servlet. <br>
     *
     * This method is called when a HTTP put request is received.
     * 
     * @param request_p the request send by the client to the server
     * @param response_p the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPut(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        try
        {
            processRequest(request_p, response_p);
        }
        catch (Exception e)
        {
            LOG.error("OwDragDropContent.doPut Exception", e);
            OwExceptionManager.PrintCatchedException(Locale.ENGLISH, e, response_p.getWriter(), "OwOwWebApplication_EXCEPTION");
        }
    }

    /**
     * Returns information about the servlet, such as 
     * author, version, and copyright. 
     *
     * @return String information about this servlet
     */
    public String getServletInfo()
    {
        return "Workdesk Drag&Drop-Servlet";
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException
    {
        // Put your code here
    }

    private static boolean isHexDigit(byte test_p)
    {
        return ((test_p >= '0' && test_p <= '9') || (test_p >= 'A' && test_p <= 'F') || (test_p >= 'a' && test_p <= 'f'));
    }

    private static char DecodeHexChar(byte b1_p, byte b2_p)
    {
        int result = (b1_p >= 'A') ? (((b1_p & 0xDF) - 'A') + 10) : (b1_p - '0');
        result = result * 16;
        result += (b2_p >= 'A') ? (((b2_p & 0xDF) - 'A') + 10) : (b2_p - '0');
        return ((char) result);
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request_p servlet request
     * @param response_p servlet response
     */
    public void processRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException, Exception
    {
        // get OwContext from session
        OwMainAppContext context = (OwMainAppContext) request_p.getSession().getAttribute(OwWebApplication.CONTEXT_KEY);

        request_p.setCharacterEncoding("UTF-8");

        if (context == null)
        {
            String msg = "OwDragDropContent.processRequest: Trying to retrieve the context from the session but the context "
                    + "(OwMainAppContext) is null. Possible cause: maybe the name of the session cookie is not supported (see 'SessionCookieNames' in owbootstrap.xml ) ...";
            LOG.error(msg);
            response_p.sendError(500);
            OwExceptionManager.PrintCatchedException(Locale.ENGLISH, new OwInvalidOperationException(msg), response_p.getWriter(), "OwOwWebApplication_EXCEPTION");
            request_p.getInputStream().close();
            throw new ServletException("msg");
        }
        //set the current request in context, else we can't create the temp folder for upload
        context.setRequest(getServletContext(), request_p, response_p);
        //release any documents that might have been left over by a previous failed import
        context.releaseDNDImportedDocuments();

        // create the upload directory
        String pathToCommonDNDTempDir = null;
        try
        {
            // create a temporary upload directory
            pathToCommonDNDTempDir = context.getDNDDocumentImportTempDir();
            // check the upload directory
            if (pathToCommonDNDTempDir == null)
            {
                String msg = "OwDragDropContent.processRequest: Creating of tempdir failed, the tempdir is null... ";
                LOG.error(msg);
                response_p.setStatus(500);
                response_p.getWriter().println(msg);
                return;
            }
        }
        catch (Exception e)
        {
            LOG.error("Exception while creating the tempdir.", e);
            response_p.setStatus(500);
            OwExceptionManager.PrintCatchedException(context.getLocale(), e, response_p.getWriter(), "OwOwWebApplication_EXCEPTION");
            return;
        }

        // factory for disk-based file-items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // set factory constraints
        factory.setSizeThreshold(40960);

        // new file-upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding("UTF-8");
        // parse the request
        List docs = upload.parseRequest(request_p);
        Iterator iter = docs.iterator();

        // mark if the upload succeeded for at least one file
        boolean atLeastOneSuccessful = false;

        // iterate through documents
        while (iter.hasNext())
        {
            // get an file item from list
            FileItem doc = (FileItem) iter.next();

            // decode filename
            String raw_filename = null;
            try
            {
                raw_filename = URLDecoder.decode(doc.getName(), "UTF-8");

            }
            catch (Exception e)
            {
                LOG.error("Exception while getting the raw filename. Continuing with next file.", e);
                continue;
            }
            String upload_filename = raw_filename.replace('/', '\\');
            int pos = upload_filename.lastIndexOf("\\");
            String rootPath = "";
            if (pos >= 0)
            {
                rootPath = upload_filename.substring(0, pos);
                rootPath = rootPath.replace('\\', '/');
                upload_filename = upload_filename.substring(pos + 1);
            }

            // create a temporary file with given path without prefix
            String sSingleSavePath = pathToCommonDNDTempDir + File.separatorChar + upload_filename;
            LOG.debug("File: " + upload_filename);

            // save file to disk
            try
            {
                // get the file ID in the temp folder
                int fileID = context.getNextDNDDocumentImportFileID();

                // create target file
                File tempFile = new File(pathToCommonDNDTempDir + File.separatorChar + Integer.toString(fileID));

                // save file item to target file
                doc.write(tempFile);

                // add the file as imported document to the DND document import stack
                OwDocumentImportItem importedDocumentItem = createDocumentImportItem(pathToCommonDNDTempDir, rootPath, upload_filename, fileID);
                context.addDNDImportedDocumentToStack(importedDocumentItem);
            }
            catch (Exception e)
            {
                LOG.error("OwDragDropContent.processRequest: Exception while writing the file contents to disk (tempfile:" + sSingleSavePath + "). Continuing with next file.", e);
                continue;
            }

            // mark successful upload
            atLeastOneSuccessful = true;
        }

        if (!atLeastOneSuccessful)
        {
            response_p.setStatus(500);
            response_p.getWriter().println("No file could be uploaded.");
            return;
        }
    }

    /**
     * Convert byte array to the original String 
     * @param bytes
     * @return String
     */
    private static String bytesToStringUTF(byte[] bytes)
    {
        char[] buffer = new char[bytes.length >> 1];
        for (int i = 0; i < buffer.length; i++)
        {
            int bpos = i << 1;
            char c = (char) (((bytes[bpos] & 0x00FF) << 8) + (bytes[bpos + 1] & 0x00FF));
            buffer[i] = c;
        }
        return new String(buffer);
    }

    /**
     * @param pathToCommonDNDTempDir_p
     * @param rootPath_p the root path sent by the client. May be empty if the client has not sent a root path.
     * @param upload_filename_p the name of the uploaded file. This is only the last part of the complete path sent by the client.
     * @param fileID_p
     * @return a new instance of a {@link OwDocumentImportItem}
     * @throws Exception
     * @since 3.1.0.3 
     */
    protected OwDocumentImportItem createDocumentImportItem(String pathToCommonDNDTempDir_p, String rootPath_p, String upload_filename_p, int fileID_p) throws Exception
    {
        return new OwStandardDocumentImportItem(pathToCommonDNDTempDir_p, fileID_p, upload_filename_p);
    }
}