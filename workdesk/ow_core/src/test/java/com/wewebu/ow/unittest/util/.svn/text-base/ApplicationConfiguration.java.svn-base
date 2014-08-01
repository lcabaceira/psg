package com.wewebu.ow.unittest.util;

import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *<p>
 * ApplicationConfiguration.
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
public class ApplicationConfiguration
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(ApplicationConfiguration.class);

    private static final String m_testSuiteResourceFolder = "/" + "resources";
    private static final String m_testSuitePropertiesFile = "configuration.properties";
    private static final String m_testSuiteResourcefiles = "testsuite.resource.files";

    private static PropertiesConfiguration m_testSuiteProperties = ApplicationConfiguration.getTestSuiteProperties();

    public static synchronized PropertiesConfiguration getTestSuiteProperties()
    {
        if (ApplicationConfiguration.m_testSuiteProperties == null)
        {
            ApplicationConfiguration.m_testSuiteProperties = new PropertiesConfiguration();
            String urlStr = ApplicationConfiguration.m_testSuiteResourceFolder + "/" + ApplicationConfiguration.m_testSuitePropertiesFile;
            URL url = ResourceUtil.getInstance().getUrl(urlStr);
            if (null == url)
            {
                ApplicationConfiguration.LOG.error("Cannot read the configuration file (Used to configure the TestSuites), URL = " + urlStr, new ConfigurationException());
                System.exit(0);
            }
            ArrayList resourcesToRead = null;
            try
            {
                ApplicationConfiguration.m_testSuiteProperties.load(url);
                resourcesToRead = (ArrayList) ApplicationConfiguration.m_testSuiteProperties.getList(ApplicationConfiguration.m_testSuiteResourcefiles);
            }
            catch (ConfigurationException e)
            {
                ApplicationConfiguration.LOG.error("Cannot read the '" + url.toString() + "' configuration file (Used to configure the TestSuites)!", e);
                System.exit(0);
            }
            // Read all resource files
            String resourceFile = "";
            try
            {
                for (int i = 0; i < resourcesToRead.size(); i++)
                {
                    resourceFile = ApplicationConfiguration.m_testSuiteResourceFolder + "/" + (String) resourcesToRead.get(i);
                    url = ResourceUtil.getInstance().getUrl(resourceFile);
                    if (null == url)
                    {
                        ApplicationConfiguration.LOG.error("Cannot read the '" + resourceFile + "' configuration file (Used to configure the TestSuites)!", new ConfigurationException());
                        System.exit(0);
                    }
                    ApplicationConfiguration.m_testSuiteProperties.load(url);
                    ApplicationConfiguration.LOG.debug("reading of resource file: " + resourceFile + " ok...");
                }
            }
            catch (ConfigurationException e)
            {
                ApplicationConfiguration.LOG.error("Cannot read the '" + resourceFile + "' configuration file (Used to configure the TestSuites)!", e);
                System.exit(0);
            }
        }
        return ApplicationConfiguration.m_testSuiteProperties;
    }

}
