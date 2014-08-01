package com.wewebu.ow.server.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 *<p>
 * Alfresco Workdesk Logger abstract class.
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
public abstract class OwLogCoreBase
{
    /**
    * Logger for this class
    */
    private static Logger LOG = Logger.getLogger(OwLogCoreBase.class);

    //if using of class logger, each class has it's own logger, otherwise the module logger is used.
    private static Boolean m_useclassLogger = null;

    /** reads the property file to determine if using module/package logger 
     * or class logger for each class which needs the logger 
     * @return a boolean
     */
    private synchronized static boolean readClassLoggerSettings()
    {
        boolean useClassLogger = false;
        // Read property file.
        String resource = "/owlog.properties";

        Properties properties = new Properties();
        InputStream resourceAsStream = null;
        String prop = null;
        try
        {
            resourceAsStream = OwLogCoreBase.class.getResourceAsStream(resource);
            if (resourceAsStream == null) //@since 3.2.0.1
            {
                resource = "owlog.properties";
                resourceAsStream = OwLogCoreBase.class.getClassLoader().getResourceAsStream(resource);
            }

            if (resourceAsStream == null)
            {
                LOG.info("OwLog settings (package or class logger) file not found: " + resource + " , useClassLogger = " + useClassLogger);
            }
            else
            {
                properties.load(resourceAsStream);
                prop = properties.getProperty("use.class.logger");
                if (prop != null && prop.equals("true"))
                {
                    useClassLogger = true;
                }
            }
        }
        catch (IOException e)
        {
            LOG.info("Exception reading the OwLog settings (package or class logger), resource = " + resource + " , useClassLogger = " + useClassLogger, e);
        }
        finally
        {
            if (resourceAsStream != null)
            {
                try
                {
                    resourceAsStream.close();
                }
                catch (IOException e)
                {
                }
                resourceAsStream = null;
            }
        }
        return useClassLogger;
    }

    /** using of class logger ?<BR>
     * @return
     * true = each class has it's own logger<BR>
     * false = use the module/package logger<BR>
     */
    public synchronized static boolean isUseClassLogger()
    {
        if (m_useclassLogger == null)
        {
            m_useclassLogger = OwLogCoreBase.readClassLoggerSettings() ? Boolean.TRUE : Boolean.FALSE;
        }
        return m_useclassLogger.booleanValue();
    }

    /** reinitialize the setting for using class logger or the package/module logger<BR>
     * reloads the owlog_settings.properties file
     */
    public synchronized static void reInitializeClassLoggerSettings()
    {
        m_useclassLogger = null;
    }
}