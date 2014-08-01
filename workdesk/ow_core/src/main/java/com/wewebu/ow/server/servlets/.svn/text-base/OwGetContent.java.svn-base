package com.wewebu.ow.server.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.perf4j.LoggingStopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
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
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * GetContent Servlet to download the content of documents and display them in a client or browser.
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
public class OwGetContent extends OwAbstractUpload
{
    private static final long serialVersionUID = 8503295237612126309L;

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwGetContent.class);

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request_p servlet request
     * @param response_p servlet response
     */
    protected void processRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        LoggingStopWatch stopWatchGetContent = new Log4JStopWatch("OwGetContent", request_p.getParameter(OwMimeManager.DMSID_KEY));

        // === get session object and context
        HttpSession Session = request_p.getSession();
        OwMainAppContext context = (OwMainAppContext) Session.getAttribute(OwWebApplication.CONTEXT_KEY);

        OwContentElement contentelement = null;
        OutputStream out = null;
        InputStream xslInputStream = null;
        // create in out streams
        InputStream in = null;
        try
        {

            // check if user is logged in
            if (context != null && context.isLogin())
            {
                // === user is already logged in
                // Parse query
                String strDwlMode = request_p.getParameter(OwMimeManager.DWL_MODE_KEY);
                String strDMSID = request_p.getParameter(OwMimeManager.DMSID_KEY);
                String strContentType = request_p.getParameter(OwMimeManager.CONTENT_TYPE_KEY);
                String strPage = request_p.getParameter(OwMimeManager.PAGE_KEY);
                String strXSLTransformatorURL = request_p.getParameter(OwMimeManager.XSL_TRANSFORMATOR_KEY);
                String strXSLTransformatorEncoding = request_p.getParameter(OwMimeManager.XSL_TRANSFORMATOR_ENCODING_KEY);

                int iDwlMode = OwMimeManager.DWL_MODE_OPEN;
                if (null != strDwlMode)
                {
                    iDwlMode = Integer.parseInt(strDwlMode);
                }

                int iContentType = OwContentCollection.CONTENT_TYPE_DOCUMENT;
                if (strContentType != null)
                {
                    iContentType = Integer.parseInt(strContentType);
                }

                int iPage = 1;
                if (strPage != null)
                {
                    iPage = Integer.parseInt(strPage);
                }

                // get the requested object
                OwObject downloadObject = null;

                if (strDMSID.equals(OwMimeManager.FILE_PREVIEW_DMSID))
                {
                    String fullpath = (String) request_p.getSession().getAttribute(OwMimeManager.FILE_PREVIEW_ATTRIBUTE_NAME);
                    String fileName = (String) request_p.getSession().getAttribute(OwMimeManager.FILE_PREVIEW_ATTRIBUTE_FILENAME);
                    String mimeType = (String) request_p.getSession().getAttribute(OwMimeManager.FILE_PREVIEW_ATTRIBUTE_MIMETYPE);
                    java.io.File localFile = new java.io.File(fullpath);
                    if (mimeType != null)
                    {
                        downloadObject = new com.wewebu.ow.server.ecm.OwFileObject(context.getNetwork(), localFile, fileName, mimeType);
                    }
                    else
                    {
                        downloadObject = new com.wewebu.ow.server.ecm.OwFileObject(context.getNetwork(), localFile);
                    }
                }
                else
                {
                    downloadObject = context.getNetwork().getObjectFromDMSID(strDMSID, false);
                }

                stopWatchGetContent.lap("findObject");

                if (!downloadObject.canGetContent(iContentType, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                {
                    String strName = context.localize("app.OwMimeManager.undefname", "[undefiniert]");
                    try
                    {
                        strName = downloadObject.getName().toString();
                    }
                    catch (NullPointerException e)
                    {
                        LOG.debug("You have no permission for accessing this document, documentname = " + strName, e);
                        response_p.getWriter().write(context.localize("servlets.OwGetContent.noaccess", "You have no permission for accessing this document:" + strName));
                    }
                    return;
                }

                // === upload content
                OwContentCollection contentCollection = downloadObject.getContentCollection();

                contentelement = contentCollection.getContentElement(iContentType, iPage);

                if (strXSLTransformatorURL != null)
                {
                    // === content needs to be transformed
                    // === get XSL stream
                    strXSLTransformatorURL = OwString.replaceAll(strXSLTransformatorURL, OwMimeManager.VIEWER_SERVLET_REPLACE_TOKEN_BASEDIR, context.getBasePath() + "/");
                    strXSLTransformatorURL = OwString.replaceAll(strXSLTransformatorURL, OwMimeManager.VIEWER_SERVLET_REPLACE_TOKEN_BASEURL, context.getBaseURL() + "/");
                    URL xslUrl = null;

                    // if the first replaceAll methods succeeds, then its a file
                    // URL.
                    if (!strXSLTransformatorURL.toLowerCase().startsWith("http"))
                    {
                        xslUrl = new URL("file", null, strXSLTransformatorURL);
                    }
                    else
                    {
                        xslUrl = new URL(strXSLTransformatorURL);
                    }

                    xslInputStream = xslUrl.openStream();

                    if (contentelement.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM)
                    {
                        // === content to be transformed is a XML stream
                        response_p.setContentType(contentelement.getMIMEType());

                        // set filename in header
                        addContentDisposition(request_p, response_p, downloadObject, contentelement, iPage, iDwlMode, iContentType);

                        // upload
                        javax.xml.transform.Transformer transformer;
                        javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();

                        out = response_p.getOutputStream();

                        transformer = factory.newTransformer(new javax.xml.transform.stream.StreamSource(xslInputStream));
                        transformer.transform(new javax.xml.transform.stream.StreamSource(new InputStreamReader(contentelement.getContentStream(null), strXSLTransformatorEncoding)), new javax.xml.transform.stream.StreamResult(out));

                        stopWatchGetContent.lap("retrieveAndServeContent:XSL_STREAM");
                    }
                    else if (contentelement.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_OBJECT)
                    {
                        // === content to be transformed is a DOM Object
                        response_p.setContentType(contentelement.getMIMEType());

                        // set filename in header
                        addContentDisposition(request_p, response_p, downloadObject, contentelement, iPage, iDwlMode, iContentType);

                        org.w3c.dom.Node contentNode = (org.w3c.dom.Node) contentelement.getContentObject();

                        // transform and upload
                        javax.xml.transform.Transformer transformer;
                        javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();

                        out = response_p.getOutputStream();

                        transformer = factory.newTransformer(new javax.xml.transform.stream.StreamSource(xslInputStream));
                        transformer.transform(new javax.xml.transform.dom.DOMSource(contentNode), new javax.xml.transform.stream.StreamResult(out));

                        stopWatchGetContent.lap("retrieveAndServeContent:XSL_OBJECT");
                    }
                    else
                    {
                        String msg = "OwGetContent.processRequest: Cannot transform content, must be DOM Object or XML Stream.";
                        LOG.error(msg);
                        throw new OwInvalidOperationException(msg);
                    }

                }
                else if (contentelement.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_URL)
                {
                    // automatic redirect to URL with JavaScript 
                    /*
                    writer.write("<html>\n<body>\n<script language='JavaScript'>\n");
                    writer.write("window.navigate('" + contentCollection.getContentURL(iContentType,iPage) + "');");
                    writer.write("</script>\n</body>\n</html>\n");
                    */
                    // print link for manual upload
                    PrintWriter writer = response_p.getWriter();
                    writer.write("<html>\n<body>\n" + context.localize("servlets.OwGetContent.uploadlink", "You can load the document here:"));
                    writer.write("<a href=\"" + contentelement.getContentURL() + "\">" + contentelement.getContentURL() + "</a>");
                    writer.write("</body>\n</html>\n");

                    stopWatchGetContent.lap("retrieveAndServeContent:URL");
                }
                else if (contentelement.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM)
                {
                    // === content is a stream
                    if (contentelement.isInputStreamNative())
                    {
                        // === the object is based and works better on InputStream
                        in = contentelement.getContentStream(null);

                        // set type of content (MIME) before upload, but after getContentStream, since some adapters determine the mimetype upon stream library
                        response_p.setContentType(contentelement.getMIMEType());
                        // set filename in header
                        addContentDisposition(request_p, response_p, downloadObject, contentelement, iPage, iDwlMode, iContentType);

                        // upload
                        out = response_p.getOutputStream();
                        OwStreamUtil.upload(in, out, true);
                    }
                    else
                    {
                        // === the object is based and works better on OutputStream
                        // set type of content (MIME) before upload, which is done in getContentStream with the OutputStream directly
                        response_p.setContentType(contentelement.getMIMEType());
                        // set filename in header
                        addContentDisposition(request_p, response_p, downloadObject, contentelement, iPage, iDwlMode, iContentType);

                        out = response_p.getOutputStream();
                        contentelement.getContentStream(out);
                    }

                    stopWatchGetContent.lap("retrieveAndServeContent:STREAM");
                }
                else if (contentelement.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_OBJECT)
                {
                    // === content is a object
                    // assume it as a DOM node
                    response_p.setContentType(contentelement.getMIMEType());

                    // set filename in header
                    addContentDisposition(request_p, response_p, downloadObject, contentelement, iPage, iDwlMode, iContentType);

                    org.w3c.dom.Node contentNode = (org.w3c.dom.Node) contentelement.getContentObject();

                    // transform and upload
                    javax.xml.transform.Transformer transformer;
                    javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();

                    out = response_p.getOutputStream();

                    transformer = factory.newTransformer();
                    transformer.transform(new javax.xml.transform.dom.DOMSource(contentNode), new javax.xml.transform.stream.StreamResult(out));

                    stopWatchGetContent.lap("retrieveAndServeContent:OBJECT");
                }
                else
                {
                    throw new OwInvalidOperationException("OwGetContent.processRequest: Unknown content.");
                }

                // signal event for history
                if (iContentType == OwContentCollection.CONTENT_TYPE_DOCUMENT && !strDMSID.equals(OwMimeManager.FILE_PREVIEW_DMSID))
                {
                    (context).getHistoryManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_DOWNLOAD, new OwStandardHistoryObjectChangeEvent(downloadObject, null), OwEventManager.HISTORY_STATUS_OK);
                }
            }
            else
            {
                PrintWriter writer = response_p.getWriter();
                // === user is not logged in, go to login page first
                response_p.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response_p.flushBuffer();
                writer.write("<html><head><title>FORBIDDEN ACCESS HTTP-CODE ");
                writer.write(Integer.toString(HttpServletResponse.SC_FORBIDDEN));
                writer.write("</title></head><body><h1>ACCESS FORBIDDEN</h1><p>");
                if (context != null)
                {
                    writer.write(context.localize("servlets.OwGetContent.notlogedin", "ERROR: The document cannot be shown without authentication."));
                }
                else
                {
                    writer.write("ERROR: You don't have the permission to view the document, please login to get permission");
                }
                writer.write("</p></body></html>");

                response_p.flushBuffer();

                stopWatchGetContent.lap("retrieveAndServeContent:FORBIDEN");
            }
        }
        catch (Exception e)
        {
            stopWatchGetContent.lap("failed retrieveAndServeContent", e.getMessage());

            LOG.error("OwGetContent.processRequest: Error processing the request.", e);
        }
        finally
        {
            //close all streams
            if (out != null)
            {
                try
                {
                    out.flush();
                    out.close();
                }
                catch (IOException e)
                {
                    LOG.debug("OwGetContent finally block failed ", e);
                }
                out = null;
            }
            if (xslInputStream != null)
            {
                try
                {
                    xslInputStream.close();
                }
                catch (IOException e)
                {
                    LOG.debug("OwGetContent finally block failed ", e);
                }
                xslInputStream = null;
            }
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    LOG.debug("OwGetContent finally block failed ", e);
                }
                in = null;
            }
        }

        // always make sure the resources get released !!!
        if (null != contentelement)
        {
            contentelement.releaseResources();
        }
    }

    /** create a filename for download, by setting the
     * <code>Content-Disposition</code> header in HttpServletResponse.
     * @param request_p 
     *
     * @param obj_p OwObject to upload
     * @param content_p OwContentCollection to upload
     * @param iPage_p requested page number
     * @param iDwlMode_p int one of OwMimeManager.DWL_MODE_... modes
     * @param iContentType_p int one of OwContentCollection.CONTENT_TYPE_... 
     *
     */
    private void addContentDisposition(HttpServletRequest request_p, HttpServletResponse response_p, OwObject obj_p, OwContentElement content_p, int iPage_p, int iDwlMode_p, int iContentType_p) throws Exception
    {
        String fileName = createFileName(obj_p.getName(), content_p);

        OwHttpContentDisposition contentDisposition = null;
        if (iDwlMode_p == OwMimeManager.DWL_MODE_SAVE_COPY)
        {
            contentDisposition = OwHttpContentDisposition.forRequest(request_p, fileName, OwHttpContentDisposition.OwElementType.ATTACHMENT);
        }
        else
        {
            contentDisposition = OwHttpContentDisposition.forRequest(request_p, fileName, OwHttpContentDisposition.OwElementType.INLINE);
        }

        response_p.addHeader("Content-Disposition", contentDisposition.toString());
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Upload Servlet to retrieve content from the archiv for Alfresco Workdesk.";
    }

}