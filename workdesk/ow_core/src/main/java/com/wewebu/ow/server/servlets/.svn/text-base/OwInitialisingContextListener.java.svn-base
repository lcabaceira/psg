package com.wewebu.ow.server.servlets;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Init listener that initializes the Alfresco Workdesk shared data.
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
public abstract class OwInitialisingContextListener implements ServletContextListener, OwBaseInitializer
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwInitialisingContextListener.class);

    /** key used to write the startup errors (configuration) in the context, used to be displayed on the logon page */
    public static final String STARTUP_ERROR_KEY = "STARTUP_ERROR_KEY";

    /** servlet context reference for this session */
    protected ServletContext m_ServletContext;

    /** Server Context - used for changing the adapter on the fly */
    private OwServerContext m_serverContext;

    private OwBaseInitializer baseInitializer;

    /**
     * initialization
     * @param servletContext_p
     */
    public void init(ServletContext servletContext_p)
    {
        m_ServletContext = servletContext_p;

        m_serverContext = OwServerContext.from(servletContext_p);

        baseInitializer = new OwServletBaseInitializer(servletContext_p);

        try
        {
            // === load config data
            applicationInitalize();
        }
        catch (OwConfigurationException e)
        {
            LOG.fatal("Errors detected on starting up the application. Please check your configuration files...", e);
            m_ServletContext.setAttribute(OwInitialisingContextListener.STARTUP_ERROR_KEY, e);
        }
        catch (OwServerException e)
        {
            LOG.fatal("Errors detected on starting up the application. Server errors detected...", e);
            m_ServletContext.setAttribute(OwInitialisingContextListener.STARTUP_ERROR_KEY, e);
        }

        if (LOG.isInfoEnabled())
        {
            LOG.info(this.getClass().getName() + ".contextInitialized: ...finished");
        }
    }

    /** Notification that the web application initialization process is starting.
     *  All ServletContextListeners are notified of context initialization before any filter or servlet in the web application is initialized
     *  
     *  @param event_p ServletContextEvent
     * 
     */
    public void contextInitialized(ServletContextEvent event_p)
    {
        if (LOG.isInfoEnabled())
        {
            LOG.info(this.getClass().getName() + ".contextInitialized: started...");
        }

        // === set vars
        // set context
        this.init(event_p.getServletContext());
    }

    /** Notification that the servlet context is about to be shut down.
     *  All servlets and filters have been destroyed before any ServletContextListeners are notified of context destruction.
     * 
     *  @param arg0_p ServletContextEvent
     *  
     */
    public void contextDestroyed(ServletContextEvent arg0_p)
    {
        if (LOG.isInfoEnabled())
        {
            LOG.info("OwInitialisingContextListener.contextDestroyed");
        }
        if (m_ServletContext != null)
        {
            Enumeration<?> names = m_ServletContext.getAttributeNames();
            while (names.hasMoreElements())
            {
                String name = names.nextElement().toString();
                m_ServletContext.removeAttribute(name);
            }
            m_ServletContext = null;
        }
    }

    /** init function for derived classes 
     * @throws OwConfigurationException */
    protected abstract void applicationInitalize() throws OwConfigurationException, OwServerException;

    /** get a attribute from the application scope
     * 
     * @param key_p
     * @return an {@link Object}
     */
    public Object getApplicationAttribute(String key_p)
    {
        return baseInitializer.getApplicationAttribute(key_p);
    }

    /** get a attribute from the application scope
     * 
     * @param key_p
     * @param object_p null removes the attribute
     * @return the previous object
     */
    public Object setApplicationAttribute(String key_p, Object object_p)
    {
        return baseInitializer.setApplicationAttribute(key_p, object_p);
    }

    /** get the base path to the application
     * 
     * @return String
     */
    public String getBasePath()
    {
        return baseInitializer.getBasePath();
    }

    /** get a parameter from the web.xml config file
     * @param strParamName_p Name of the requested parameter
     * @return parameter value, of null if not set.
     */
    public String getInitParameter(String strParamName_p)
    {
        return baseInitializer.getInitParameter(strParamName_p);
    }

    /** loads a XML Document either from local file, external file or from a JNDI context
    *
    * @param strName_p Name of the resource to look for
    *
    * @return OwXMLUtil wrapped DOM Node, or null if not found
     * @throws Exception 
    */
    public InputStream getXMLConfigDoc(String strName_p) throws Exception
    {
        return baseInitializer.getXMLConfigDoc(strName_p);
    }

    /** loads a URL for a configuration either from local file, external file or from a JNDI context
    *
    * @param strName_p Name of the configuration to look for
    * @return URL wrapped DOM Node, or null if not found
     */
    public URL getConfigURL(String strName_p) throws Exception
    {
        return baseInitializer.getConfigURL(strName_p);
    }

}