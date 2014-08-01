package test.com.wewebu.ow.server.ecm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.unittest.util.ResourceUtil;

/**
 *<p>
 * Default abstract base implementation of {@link OwIntegrationTestSetup}.
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
public abstract class OwBasicIntegrationTestSetup implements OwIntegrationTestSetup
{
    private static final String JUNIT_BOOTSTRAPS_RESOURCES = "junit.bootstraps.resources";

    private static final Logger LOG = Logger.getLogger(OwBasicIntegrationTestSetup.class);

    protected String bootstrapConfigurationName;
    protected OwStandardXMLUtil bootstrapConfiguration;
    protected Properties m_testerProperties;
    protected String m_testResourcesPath;

    public OwBasicIntegrationTestSetup(String bootstrapConfigurationName_p)
    {
        super();
        this.bootstrapConfigurationName = bootstrapConfigurationName_p;
    }

    @Override
    public void checkDependencies(OwIntegrationTestSession session_p) throws IllegalStateException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionSetUp(OwIntegrationTestSession session_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionTearDown(OwIntegrationTestSession session_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void testSetUp(OwIntegrationTestSession session_p, String testName_p, OwIntegrationTest test_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void testTearDown(OwIntegrationTestSession session_p, String testName_p, OwIntegrationTest test_p) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public OwStandardXMLUtil loadConfiguration(String resourcePathConfigFileName_p) throws FileNotFoundException, Exception
    {
        String bootstrapPath = getTesterBasedResourcePath(JUNIT_BOOTSTRAPS_RESOURCES, resourcePathConfigFileName_p);
        LOG.debug("Loading bootstrap " + bootstrapPath);
        OwStandardXMLUtil config = new OwStandardXMLUtil(new FileInputStream(new File(bootstrapPath)), BOOTSRAP_ROOT);
        config.getNode().normalize();

        return config;
    }

    protected String getTesterBasedResourcePath(String resourceFolderProperty_p, String resourceFile_p) throws FileNotFoundException, Exception
    {
        String testerResourceFolder = getTesterProperty(resourceFolderProperty_p);
        return m_testResourcesPath + "/" + testerResourceFolder + "/" + resourceFile_p;
    }

    protected String getTesterProperty(String propertyName_p) throws FileNotFoundException, Exception
    {
        return m_testerProperties.getProperty(propertyName_p);
    }

    @Override
    public synchronized void initializeTesterData(String resoucesFolderPath) throws Exception
    {
        if (m_testerProperties == null || m_testResourcesPath == null)
        {
            String testerUserName = System.getProperty("user.name");
            LOG.debug("Setting up configuration for user : " + testerUserName);
            String testerPropsResPath = getTestersFolderPath() + "/" + testerUserName + ".properties";

            URL testerPropertiesUrl = ResourceUtil.getInstance().getUrl(testerPropsResPath);
            if (testerPropertiesUrl == null)
            {
                URL defaultTesterPropertiesUrl = ResourceUtil.getInstance().getUrl(getTestersFolderPath() + "/" + DEFAULT_TESTER_PROPERTIES);
                LOG.debug("Could not find " + testerPropsResPath + " proceeding with default " + defaultTesterPropertiesUrl);
                testerPropertiesUrl = defaultTesterPropertiesUrl;
            }

            LOG.debug("Readin tester properties from " + testerPropertiesUrl);
            InputStream testerPropertiesIS = testerPropertiesUrl.openStream();
            try
            {
                m_testerProperties = new Properties();
                m_testerProperties.load(testerPropertiesIS);
            }
            finally
            {
                testerPropertiesIS.close();
            }

            m_testResourcesPath = ResourceUtil.getInstance().getResourcePath(resoucesFolderPath);
        }
    }

    /**
     * Returns the path to the folder that holds tester configuration property files. 
     * The path is used to get a resource URL from the current classloader. 
     * @return a String representing a folder path.
     */
    protected abstract String getTestersFolderPath();
}