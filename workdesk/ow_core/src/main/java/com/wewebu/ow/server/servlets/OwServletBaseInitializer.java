package com.wewebu.ow.server.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Initializer implementation for {@link ServletContext} based environments
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
 *@since 4.0.0.0
 */
public class OwServletBaseInitializer implements OwBaseInitializer
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwServletBaseInitializer.class);

    private ServletContext servletContext;
    private OwServerContext serverContext;

    private String basePath;

    public OwServletBaseInitializer(ServletContext servletContext)
    {
        super();
        this.servletContext = servletContext;
        this.serverContext = OwServerContext.from(servletContext);
        initBasePath();
    }

    private void initBasePath()
    {
        // get base path from ServletContext.basePath
        basePath = servletContext.getRealPath("");
        if (basePath == null)
        {
            // use this method if the application server doesn't support getRealPath (e.g. Weblogic 9.2)
            try
            {
                //new File(new URI(url.toExternalForm()));
                String deploymentUrl = servletContext.getResource("/").toExternalForm();
                File file = new File(new URI(deploymentUrl));
                basePath = file.getPath();
            }
            catch (MalformedURLException e)
            {
                LOG.error("Could not get BasePath...", e);
            }
            catch (URISyntaxException e)
            {
                LOG.error("Could not get BasePath...", e);
            }
        }
        if (basePath.endsWith("/") || basePath.endsWith("\\"))
        {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
    }

    public String getBasePath()
    {
        return basePath;
    }

    public String getInitParameter(String strParamName_p)
    {
        return servletContext.getInitParameter(strParamName_p);
    }

    public InputStream getXMLConfigDoc(String strName_p)
    {
        URL configURL = null;
        String file = OwAppContext.CONFIG_STREAM_NAME_PREFIX + strName_p + ".xml";
        try
        {
            configURL = getConfigURL(file);
        }
        catch (Exception e)
        {
            String msg = "Initialisation Error: Cannot get the configuration from URL=" + configURL;
            LOG.error(msg, e);
            return null;
        }
        try
        {
            return configURL.openStream();
        }
        catch (IOException ioE)
        {
            String msg = "Initialisation Error: Cannot open the XML file, file not found (" + file + ")! Configuration URL=" + ((configURL == null) ? "null" : configURL.toString());
            LOG.error(msg, ioE);
            return null;
        }
    }

    /** Get the path to the configuration files, using environment and JEE interfaces.
     * <p><b>HINT</b>: This is only for backwards compatibility.</p>
     * @return String fully qualified path
     */
    private String getConfigPath()
    {
        // === check if we have a reference to an file path bootstrap resource 
        String strResourceFilePath = null;
        String strEnvironmentVarName = getInitParameter(OwAppContext.RESOURCE_FILE_PATH_ENVIRONMENT_VAR_NAME_PARAM);
        if ((strEnvironmentVarName != null) && (strEnvironmentVarName.length() > 0))
        {
            try
            {
                strResourceFilePath = System.getProperty(strEnvironmentVarName);
            }
            catch (Exception e)
            {
                // most common is a SecurityException if we do not have access to that
                // system property.
                // Report any Exceptions to the log and use ResourceFilePath or default
                // location instead.
                LOG.error("Can not read System property '" + strEnvironmentVarName + "' to get the config location. Using ResourceFilePath (if any) or default location instead.", e);
            }
        }
        if (strResourceFilePath == null)
        {
            strResourceFilePath = serverContext.getResourceFilePath();
        }

        if ((strResourceFilePath == null) || (strResourceFilePath.length() == 0))
        {
            // === use default
            return getBasePath() + "/WEB-INF";
        }

        if (strResourceFilePath.startsWith(OwAppContext.RESOURCE_DEPLOYMENT_PREFIX))
        {
            // === use subdirectory in deployment structure
            return getBasePath() + "/" + strResourceFilePath.substring(OwAppContext.RESOURCE_DEPLOYMENT_PREFIX.length(), strResourceFilePath.length());
        }

        return strResourceFilePath;
    }

    public URL getConfigURL(String strName_p) throws Exception
    {

        URL configUrl = null;

        // === check if we have a reference to an JNDI XML resource 
        StringBuffer jndiParameterName = new StringBuffer();
        jndiParameterName.append(strName_p);
        jndiParameterName.append(OwAppContext.CONFIG_JNDI_SUFFIX);
        String strJNDIName = getInitParameter(jndiParameterName.toString());

        if (strJNDIName != null)
        {
            javax.naming.InitialContext ctx = new javax.naming.InitialContext();
            configUrl = (URL) ctx.lookup(strJNDIName);
        }

        // still null
        if (configUrl == null)
        {
            // try to read configuration files as resource.
            String resourceFilePath = serverContext.getResourceFilePath();

            if ((resourceFilePath != null) && resourceFilePath.startsWith(OwAppContext.RESOURCE_CLASSPATH_PREFIX))
            {
                String configurationPathPrefix = resourceFilePath.substring(OwAppContext.RESOURCE_CLASSPATH_PREFIX.length(), resourceFilePath.length());
                StringBuffer resourcePath = new StringBuffer();
                resourcePath.append("/");
                resourcePath.append(configurationPathPrefix);
                resourcePath.append("/");
                // bootstrap, plugins, MIME table are with prefix (ow), log4.properties not.
                resourcePath.append(strName_p);
                return this.getClass().getResource(resourcePath.toString());
            }
            // === create return stream using getConfigPath() --> via getRealPath().
            // for backward compatibility
            configUrl = (new File(getConfigPath() + "/" + strName_p)).toURL();
        }

        return configUrl;
    }

    public Object getApplicationAttribute(String key_p)
    {
        return servletContext.getAttribute(key_p);
    }

    public Object setApplicationAttribute(String key_p, Object object_p)
    {
        Object previousObject = servletContext.getAttribute(key_p);

        if (object_p != null)
        {
            servletContext.setAttribute(key_p, object_p);
        }
        else
        {
            servletContext.removeAttribute(key_p);
        }

        return (previousObject);
    }

}
