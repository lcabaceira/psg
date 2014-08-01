package com.wewebu.ow.server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.util.OwMimeTypes;

/**
 *<p>
 *  Abstract HttpServlet which provides helper methods to
 *  handle file name for upload process.
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
 *@since 3.1.0.4
 */
public abstract class OwAbstractUpload extends HttpServlet
{
    /**Default serial version UID*/
    private static final long serialVersionUID = 1L;

    /**
     * Return the name which should be used in content disposition header.
     * Will use the getMIMEParameter() of content element first and extract the
     * file name from there. If not available a file extension is searched based on 
     * the content MIME type and will be attached to the defaultName.<br />
     * Attaching will only be processed if the defaultName not already contains the
     * same file extension suffix.
     * @param defaultName String name to be used by default
     * @param contentElement OwContentElement
     * @return String for file name upload
     * @throws Exception if could not retrieve information form provided content element
     */
    protected String createFileName(String defaultName, OwContentElement contentElement) throws Exception
    {
        String fileName;
        String sMimeFileName = OwMimeTypes.getMimeParameter(contentElement.getMIMEParameter(), "name");
        if (null != sMimeFileName)
        {
            fileName = sMimeFileName;
        }
        else
        {
            // === filename will be constructed out of the default name
            String mimetype = OwMimeTypes.getExtensionFromMime(contentElement.getMIMEType());
            fileName = defaultName;
            if (mimetype != null && !defaultName.endsWith("." + mimetype))
            {
                fileName = defaultName + "." + mimetype;
            }
        }
        return fileName;
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request_p servlet request
     * @param response_p servlet response
     */
    protected void doGet(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        processRequest(request_p, response_p);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request_p servlet request
     * @param response_p servlet response
     */
    protected void doPost(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        processRequest(request_p, response_p);
    }

    /**
     * Main method to be implemented for upload of content to the client,
     * this method is a delegation from <b>GET</b> and <b>POST</b> requests.
     * @param request_p HttpServletRequest
     * @param response_p HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected abstract void processRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException;

}
