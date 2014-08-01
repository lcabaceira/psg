package com.wewebu.ow.server.ecmimpl.fncm5.unittest.log;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 *<p>
 * JUnitLogger.
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
public class JUnitLogger
{
    public static Logger getLogger(Class<?> clazz)
    {
        if (InitLoggerHelper.getInstance().isClassLogging())
        {
            return Logger.getLogger(clazz);
        }
        else
        {
            return Logger.getLogger("fncm5.unittest");
        }
    }

    private static class InitLoggerHelper
    {
        private static InitLoggerHelper singleton;
        private boolean classLogging;

        private InitLoggerHelper(String classLogging)
        {
            if (classLogging != null)
            {
                this.classLogging = Boolean.parseBoolean(classLogging);
            }
        }

        public boolean isClassLogging()
        {
            return classLogging;
        }

        public synchronized static InitLoggerHelper getInstance()
        {
            if (singleton == null)
            {
                Properties prop = new Properties();
                try
                {
                    prop.load(InitLoggerHelper.class.getResourceAsStream("/log4j.properties"));
                    singleton = new InitLoggerHelper(prop.getProperty("classlogging"));
                    org.apache.log4j.PropertyConfigurator.configure(prop);
                }
                catch (IOException e)
                {
                    e.printStackTrace(System.err);
                }
            }
            return singleton;
        }

    }

}
