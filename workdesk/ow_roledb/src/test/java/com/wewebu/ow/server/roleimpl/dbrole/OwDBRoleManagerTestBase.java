package com.wewebu.ow.server.roleimpl.dbrole;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyMandatorManager;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyNetwork;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.historyimpl.simplehistory.OwSimpleHistoryManager;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.role.OwStandardRoleManager;
import com.wewebu.ow.server.roleimpl.dbrole.manager.OwDBRoleManagerTestManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.unittest.util.ApplicationConfiguration;
import com.wewebu.ow.unittest.util.OwTestContext;
import com.wewebu.ow.unittest.util.ResourceUtil;

/**
 *<p>
 * OwDBRoleManagerTestBase.
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
public abstract class OwDBRoleManagerTestBase extends TestCase
{
    /**
    * Logger for this class
    */
    private static final Logger LOG = Logger.getLogger(OwDBRoleManagerTestBase.class);

    // protected OwFNCMNetwork m_network;

    protected OwTestContext m_context = null;

    protected OwNetwork m_network = null;

    protected OwDBRoleManager m_roleManager = null;

    protected OwDBRoleManagerTestManager roleTestManager = null;

    protected final String m_configFile;
    protected final String m_configDir;
    protected final String m_rootnode;
    protected final String m_password;
    protected final String m_login;
    protected final String m_objectStoreName;

    /**
     * @param arg0_p
     */
    public OwDBRoleManagerTestBase(String arg0_p)
    {
        super(arg0_p);
        m_configFile = ApplicationConfiguration.getTestSuiteProperties().getString(getTestBasename() + ".configFile");
        m_configDir = ApplicationConfiguration.getTestSuiteProperties().getString(getTestBasename() + ".configDir");
        m_rootnode = ApplicationConfiguration.getTestSuiteProperties().getString(getTestBasename() + ".xml.bootstrap");
        m_password = ApplicationConfiguration.getTestSuiteProperties().getString(getTestBasename() + ".password");
        m_login = ApplicationConfiguration.getTestSuiteProperties().getString(getTestBasename() + ".login");
        m_objectStoreName = ApplicationConfiguration.getTestSuiteProperties().getString(getTestBasename() + ".objectStoreName");
    }

    abstract protected String getTestBasename();

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        try
        {
            // get test configuration
            OwXMLUtil config;
            config = new OwStandardXMLUtil(new FileInputStream(new File(getConfigDir() + this.m_configFile)), this.m_rootnode);

            // create and init the network adapter
            m_context = new OwTestContext(config, getConfigDir());
            OwStandardRoleManager.applicationInitalize(m_context);

            // create and init the network adapter
            m_network = new OwDummyNetwork();
            m_context.setNetwork(m_network);
            try
            {
                m_network.init(m_context, new OwStandardXMLUtil(config.getSubNode("EcmAdapter")));
            }
            catch (Exception e)
            {
                LOG.error("Cannot initialize the Network...");
                throw e;
            }

            // === create simple history manger
            OwHistoryManager eventManager = new OwSimpleHistoryManager();
            eventManager.init(m_context, new OwStandardXMLUtil(config.getSubNode("HistoryManager")));
            eventManager.setNetwork(m_network);
            m_network.setEventManager(eventManager);
            m_context.setHistoryManager(eventManager);

            // === create role manger
            m_roleManager = new OwDBRoleManager();
            m_roleManager.init(m_context, new OwStandardXMLUtil(config.getSubNode("RoleManager")));
            m_network.setRoleManager(m_roleManager);
            m_context.setRoleManager(m_roleManager);

            // === create mandator manager
            OwMandatorManager mandatorManager = new OwDummyMandatorManager();
            mandatorManager.init(m_context, new OwStandardXMLUtil(config.getSubNode("MandatorManager")));

            //try
            //{
            // === login
            m_network.loginDefault(this.m_login, this.m_password);
            // login init
            m_roleManager.loginInit();
            //}
            //catch (Exception e)
            //{
            //    String msg = "BaseTest.setup: Cannot login in, Loginname = [" + this.m_login + "]...";
            //    LOG.error(msg);
            //    fail(msg);
            //}

        }
        catch (FileNotFoundException e)
        {
            String msg = "BaseTest.setup: Cannot read the config file = [" + getConfigDir() + this.m_configFile + "]...";
            LOG.error(msg, e);
            fail(msg);
        }
        catch (Exception e)
        {
            String msg = "BaseTest.setup: Cannot configure the TestBase class...";
            LOG.error(msg, e);
            fail(msg);
        }

        roleTestManager = new OwDBRoleManagerTestManager(m_network);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();

        m_network.logout();

        m_network = null;
        m_roleManager = null;

        m_context = null;
        roleTestManager = null;
    }

    /** get the configuration dir 
     * @return String*/
    public String getConfigDir()
    {
        String configDirTemp = ResourceUtil.getInstance().getResourcePath(this.m_configDir);
        return configDirTemp;
    }

    public static String getVersion()
    {
        StringBuffer buffer = new StringBuffer();
        //buffer.append("Not available! Add to OwDBRoleManagerTestBase.pluginVersion()");
        buffer.append("OwDBRoleManager [");
        buffer.append("]");
        return buffer.toString();
    }
}
