package com.wewebu.ow.server.historyimpl.dbhistory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyNetwork;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.history.OwStandardHistoryEntry;
import com.wewebu.ow.server.historyimpl.dbhistory.manager.OwDBHistoryTestManager;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.roleimpl.simplerole.OwSimpleRoleManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.unittest.util.ApplicationConfiguration;
import com.wewebu.ow.unittest.util.OwTestContext;
import com.wewebu.ow.unittest.util.ResourceUtil;

/**
 *<p>
 * DB Role Manager.
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
public abstract class OwDBHistoryTestBase extends TestCase
{
    /**
    * Logger for this class
    */
    private static final Logger LOG = Logger.getLogger(OwDBHistoryTestBase.class);

    // protected OwFNCMNetwork m_network;

    protected OwNetwork m_network = null;

    protected OwDBHistoryTestManager historyTestManager = null;

    protected OwHistoryManager m_historyManager;

    //private static final Logger LOG = Logger.getLogger(OwDBHistoryBaseTest.class);

    protected final String m_configFile;
    protected final String m_configDir;
    protected final String m_rootnode;
    protected final String m_password;
    protected final String m_login;
    protected final String m_objectStoreName;

    /**
     * @param arg0_p
     */
    public OwDBHistoryTestBase(String arg0_p)
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

    /** find a collection of history entriws for givfen ID and type
     * 
     * @param sID_p
     * @param iType_p
     * @return OwObjectCollection
     * @throws Exception
     */
    protected OwObjectCollection findStandardEvents(String sID_p, int iType_p) throws Exception
    {
        // create search
        OwSearchNode search = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);
        OwSearchNode IDCrit = new OwSearchNode(m_historyManager.getFieldDefinition(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY, null), OwSearchOperator.CRIT_OP_EQUAL, sID_p, 0);
        search.add(IDCrit);
        OwSearchNode TypeCrit = new OwSearchNode(m_historyManager.getFieldDefinition(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, null), OwSearchOperator.CRIT_OP_EQUAL, new Integer(iType_p), 0);
        search.add(TypeCrit);

        return m_historyManager.doSearch(search, new OwSort(), null, 10000, 0);
    }

    /** find a collection of history entriws for givfen ID and type
     * 
     * @param obj_p
     * @param sID_p
     * @param iType_p
     * @return OwObjectCollection
     * @throws Exception
     */
    protected OwObjectCollection findObjectEvents(OwObjectReference obj_p, String sID_p, int iType_p) throws Exception
    {
        // create search
        OwSearchNode search = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);
        OwSearchNode IDCrit = new OwSearchNode(m_historyManager.getFieldDefinition(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY, null), OwSearchOperator.CRIT_OP_EQUAL, sID_p, 0);
        search.add(IDCrit);
        OwSearchNode TypeCrit = new OwSearchNode(m_historyManager.getFieldDefinition(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, null), OwSearchOperator.CRIT_OP_EQUAL, new Integer(iType_p), 0);
        search.add(TypeCrit);

        return m_historyManager.doObjectSearch(obj_p, search, new OwSort(), null, null, 10000);
    }

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
            // === get test configuration
            OwXMLUtil config;
            config = new OwStandardXMLUtil(new FileInputStream(new File(getConfigDir() + this.m_configFile)), this.m_rootnode);

            // === create and init the dummy network adapter
            m_network = new OwDummyNetwork();

            OwTestContext context = new OwTestContext(config, getConfigDir());
            context.setNetwork(m_network);

            try
            {
                m_network.init(context, new OwStandardXMLUtil(config.getSubNode("EcmAdapter")));
            }
            catch (Exception e)
            {
                LOG.error("Cannot initialize the Network...");
                throw e;
            }

            // create history manger
            m_historyManager = new OwDBHistoryManager();
            m_historyManager.init(context, new OwStandardXMLUtil(config.getSubNode(getHistoryManagerSubNode())));
            m_historyManager.setNetwork(m_network);
            m_network.setEventManager(m_historyManager);

            // === create simple role manger
            OwRoleManager owRoleManager = new OwSimpleRoleManager();
            owRoleManager.init(context, new OwStandardXMLUtil(config.getSubNode("RoleManager")));
            m_network.setRoleManager(owRoleManager);

            try
            {
                // === login
                m_network.loginDefault(this.m_login, this.m_password);
                // login init
                owRoleManager.loginInit();
            }
            catch (Exception e)
            {
                String msg = "BaseTest.setup: Cannot login in, Loginname = [" + this.m_login + "]...";
                LOG.error(msg);
                fail(msg);
            }

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

        historyTestManager = new OwDBHistoryTestManager(m_network);
    }

    /** override with testcase
     * 
     * @return String
     */
    protected String getHistoryManagerSubNode()
    {
        return "HistoryManager";
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

        historyTestManager = null;
    }

    /** get the configuration dir 
     * @return String*/
    public String getConfigDir()
    {
        // return x:/workspace/ow_test/bin/resources/;
        String configDirTemp = ResourceUtil.getInstance().getResourcePath(this.m_configDir);
        return configDirTemp;
    }

    /** version *
     * @return String*/
    public static String getVersion()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("OwDBHistoryManager [");
        buffer.append("]");
        return buffer.toString();
    }
}
