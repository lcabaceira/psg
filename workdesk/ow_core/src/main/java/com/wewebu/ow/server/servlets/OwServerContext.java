package com.wewebu.ow.server.servlets;

import javax.servlet.ServletContext;

import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * This class is used to access the content of the ServletContext. 
 * Here you can implement some mutator methods.
 * You can find an instance of this class in the ServletContext under 
 * the attribute named {@link #ATT_NAME}. 
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
 *@since 2.5.2.0
 */

public class OwServerContext
{
    /** key to store the server context as attribute in the session context*/
    public static final String ATT_NAME = "ow.serverContext";

    /** servlet context */
    private ServletContext servletContext;

    /** resource OwResourceFilePath param for distributed deployment or to handle multiple configurations */
    private String resourceFilePath;

    public static synchronized OwServerContext from(ServletContext servletContext_p)
    {
        OwServerContext serverContext = (OwServerContext) servletContext_p.getAttribute(OwServerContext.ATT_NAME);
        if (serverContext == null)
        {
            serverContext = new OwServerContext(servletContext_p);
            servletContext_p.setAttribute(OwServerContext.ATT_NAME, serverContext);
        }

        return serverContext;
    }

    /**
     * 
     * @param servletContext_p
     */
    private OwServerContext(ServletContext servletContext_p)
    {
        this.servletContext = servletContext_p;
    }

    /**
     * get the OwResourceFilePath param for distributed deployment or to handle multiple configurations
     * @return String
     */
    public String getResourceFilePath()
    {
        if (null == this.resourceFilePath)
        {
            this.resourceFilePath = getInitParameter(OwAppContext.RESOURCE_FILE_PATH_PARAM);
        }
        return resourceFilePath;
    }

    /**
     * set the OwResourceFilePath param for distributed deployment or to handle multiple configurations
     * @param resourceFilePath_p
     */
    public void setResourceFilePath(String resourceFilePath_p)
    {
        this.resourceFilePath = resourceFilePath_p;
    }

    /** get a parameter from the web.xml config file
     * @param strParamName_p Name of the requested parameter
     * @return parameter value, of null if not set.
     */
    public String getInitParameter(String strParamName_p)
    {
        return servletContext.getInitParameter(strParamName_p);
    }
}