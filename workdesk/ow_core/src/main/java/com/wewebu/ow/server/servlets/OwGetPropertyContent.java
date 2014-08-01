package com.wewebu.ow.server.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwWebApplication;

/**
 *<p>
 * Servlet that download content from binary object properties.
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
public class OwGetPropertyContent extends HttpServlet
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwGetPropertyContent.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1680461643878695932L;

    private static final String DEFAULT_RESOURCE_INIT_PARAMETER = "default_resource";
    private static final String DEFAULT_RESOURCE_CONTENT_TYPE_INIT_PARAMETER = "default_resource_content_type";

    /** 
     * The default resource as specified by initialization 
     * parameter <code>DEFAULT_RESOURCE_INIT_PARAMETER</code>
     */
    private String m_defaultResource;

    /** 
     * The default resource content type as specified by initialization 
     * parameter <code>DEFAULT_RESOURCE_CONTENT_TYPE_INIT_PARAMETER</code>
     */
    private String m_defaultResourceContentType;

    /** Initializes the servlet.
     * Reads default resource initialization parameters 
     * into {@link #m_defaultResource} and  {@link #m_defaultResourceContentType}.
     */
    public void init() throws ServletException
    {
        m_defaultResource = getInitParameter(DEFAULT_RESOURCE_INIT_PARAMETER);
        m_defaultResourceContentType = getInitParameter(DEFAULT_RESOURCE_CONTENT_TYPE_INIT_PARAMETER);
    }

    /**
     * Default resource request processing method.
     * Should no satisfying result is obtained through the indicated object/property pair
     * this method will provide the default content set through the servlets ini paramenters.
     * @param request_p
     * @param response_p
     * @throws IOException
     */
    private void processWithDefaultResource(HttpServletRequest request_p, HttpServletResponse response_p) throws IOException
    {
        if (m_defaultResource != null && m_defaultResourceContentType != null)
        {
            URL defaultResourceURL = getServletContext().getResource(m_defaultResource);
            InputStream defaultResourceStream = defaultResourceURL.openStream();
            response_p.setContentType(m_defaultResourceContentType);
            ServletOutputStream stream = response_p.getOutputStream();
            byte[] readBuffer = new byte[2048];
            int readCount;
            while ((readCount = defaultResourceStream.read(readBuffer)) != -1)
            {
                stream.write(readBuffer, 0, readCount);
            }

            defaultResourceStream.close();

            stream.flush();
            stream.close();
        }
        else
        {
            LOG.debug("No default resource configured !");
            response_p.flushBuffer();
        }
    }

    protected void doGet(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        processRequest(request_p, response_p);
    }

    protected void doPost(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {
        processRequest(request_p, response_p);
    }

    /**
     * Request processing method.
     * Accesses the object requested through the request attribute defined by {@link OwMimeManager#DMSID_KEY}
     * Accesses the requested objects property defined by {@link OwMimeManager#CONTENT_PROPERTY_KEY}
     * The byte arrayed value of this property is served as response.
     * If the operation fails {@link #processWithDefaultResource(HttpServletRequest, HttpServletResponse)} is attempted. 
     * @param request_p
     * @param response_p
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws ServletException, IOException
    {

        HttpSession session = request_p.getSession();
        OwMainAppContext context = (OwMainAppContext) session.getAttribute(OwWebApplication.CONTEXT_KEY);
        try
        {
            if (context == null)
            {

                String contextMissingMsg = "Alfresco Workdesk context missing!This might not be a Alfresco Workdesk session!";
                LOG.debug(contextMissingMsg);
                throw new ServletException(contextMissingMsg);
            }
            else if (!context.isLogin())
            {
                String userIsNotLoggedIn = "User is not logged in!";
                LOG.debug(userIsNotLoggedIn);
                throw new ServletException(userIsNotLoggedIn);
            }
            else
            {
                String strDMSID = request_p.getParameter(OwMimeManager.DMSID_KEY);
                String strContentMimeType = request_p.getParameter(OwMimeManager.CONTENT_MIMETYPE_KEY);
                String strPropertyName = request_p.getParameter(OwMimeManager.CONTENT_PROPERTY_KEY);

                try
                {
                    OwObject object = context.getNetwork().getObjectFromDMSID(strDMSID, false);
                    OwObjectClass objectClass = object.getObjectClass();
                    OwPropertyClass contentPropertyClass = objectClass.getPropertyClass(strPropertyName);
                    if (contentPropertyClass != null)
                    {
                        OwProperty property = object.getProperty(strPropertyName);
                        Object propertyValue = property.getValue();
                        if (propertyValue == null)
                        {
                            processWithDefaultResource(request_p, response_p);
                        }
                        else if (propertyValue instanceof byte[])
                        {
                            byte[] byteArrayValue = (byte[]) propertyValue;
                            response_p.setContentType(strContentMimeType);
                            ServletOutputStream stream = response_p.getOutputStream();
                            stream.write(byteArrayValue);
                            stream.flush();
                            stream.close();
                        }
                        else
                        {
                            String missingContentProperty = "Content property : " + strContentMimeType + " of " + strDMSID + " has invalid value type " + propertyValue.getClass() + "  while byte[] was expected!";
                            LOG.debug(missingContentProperty);
                            processWithDefaultResource(request_p, response_p);
                        }
                    }
                    else
                    {
                        String missingContentProperty = "Missing content property : " + strContentMimeType + " for object : " + strDMSID;
                        LOG.debug(missingContentProperty);
                        processWithDefaultResource(request_p, response_p);
                    }
                }
                catch (Exception e)
                {
                    String objectAccessFailedMsg = "Could not access object for property content!";
                    LOG.debug(objectAccessFailedMsg, e);
                    processWithDefaultResource(request_p, response_p);
                }
            }
        }

        catch (Exception e)
        {
            String contextError = "Error checking context!";
            LOG.debug(contextError, e);
            throw new ServletException(contextError, e);
        }
    }
}
