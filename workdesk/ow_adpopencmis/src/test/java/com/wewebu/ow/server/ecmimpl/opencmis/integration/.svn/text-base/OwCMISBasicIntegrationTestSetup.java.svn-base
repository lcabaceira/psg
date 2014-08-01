package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import test.com.wewebu.ow.server.ecm.OwBasicIntegrationTestSetup;
import test.com.wewebu.ow.server.ecm.OwIntegrationTest;
import test.com.wewebu.ow.server.ecm.OwIntegrationTestSession;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.historyimpl.simplehistory.OwSimpleHistoryManager;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.unittest.util.OwTestContext;

public class OwCMISBasicIntegrationTestSetup extends OwBasicIntegrationTestSetup
{
    private static final Logger LOG = Logger.getLogger(OwCMISBasicIntegrationTestSetup.class);

    public static final String CMIS_CONFIG_BOOTSTRAP_ALFRESCO_SOAP = "cmis_owbootstrap_alfresco_soap.xml";
    public static final String DEFAULT_CMIS_CONFIG_BOOTSTRAP = "cmis_owbootstrap_def.xml";
    public static final String TEST_CMIS_TESTERS_FOLDER_PATH = "resources/cmis/testers";
    public static final String TEST_CMIS_RESOUCES_FOLDER_PATH = "resources/cmis/";

    private String adminUser;
    private String adminPassword;

    public OwCMISBasicIntegrationTestSetup(String bootstrapConfigurationName_p)
    {
        super(bootstrapConfigurationName_p);
    }

    @Override
    protected String getTestersFolderPath()
    {
        return TEST_CMIS_TESTERS_FOLDER_PATH;
    }

    @Override
    public void sessionSetUp(OwIntegrationTestSession session_p) throws FileNotFoundException, Exception
    {
        super.sessionSetUp(session_p);
        this.bootstrapConfiguration = loadConfiguration(this.bootstrapConfigurationName);
        this.adminUser = getTesterProperty("cmis.junit.admin.user");
        this.adminPassword = getTesterProperty("cmis.junit.admin.password");
    }

    public static OwCMISNetwork setUpOwNetwork(OwStandardXMLUtil config_p) throws Exception
    {
        if (config_p == null)
        {
            throw new RuntimeException("Tryed to activate a null CMIS configuration ! You must call OwCMISBasicIntegrationTestSetup::loadConfiguration first!");
        }
        else
        {
            OwStandardXMLUtil ecmAdapterXMLConfig = new OwStandardXMLUtil(config_p.getSubNode(ECMADAPTER_CONFIG_NODE));

            //OwStandardXMLUtil recordClassesConfig = new OwStandardXMLUtil(ecmAdapterXMLConfig.getSubNode("RecordClasses"));
            //List recordClasesNamesConfig = recordClassesConfig.getSafeNodeList();

            //m_recordClasses = new String[recordClasesNamesConfig.size()];
            //int index = 0;
            //for (Iterator i = recordClasesNamesConfig.iterator(); i.hasNext(); index++)
            //{
            //  org.w3c.dom.Node subNode = (org.w3c.dom.Node) i.next();
            //  OwStandardXMLUtil recordClassesXML = new OwStandardXMLUtil(subNode);
            //  m_recordClasses[index] = recordClassesXML.getSafeTextValue("BadRecordClassConfig");
            //}

            OwTestContext context = new OwTestContext(config_p, "");

            OwCMISNetwork network = new OwCMISNetwork() {
                @Override
                protected OwCMISNetworkCfg createConfiguration(OwXMLUtil networkSettings_p)
                {
                    return new OwCMISNetworkTestCfg(networkSettings_p);
                }
            };

            network.init(context, ecmAdapterXMLConfig);

            OwStandardXMLUtil roleManagerConfig = new OwStandardXMLUtil(config_p.getSubNode(ROLEMANAGER_CONFIG_NODE));
            OwStandardXMLUtil roleManagerClassConfig = new OwStandardXMLUtil(roleManagerConfig.getSubNode(CLASSNAME_CONFIG_NODE));

            String roleManagerClassName = roleManagerClassConfig.getSafeTextValue("com.wewebu.ow.server.roleimpl.simplerole.OwSimpleRoleManager");
            LOG.debug("TEST CONFIG ROLEMANAGER CLASS IS " + roleManagerClassName);

            Class roleManagerClass = Class.forName(roleManagerClassName);

            Constructor roleManagerConstructor = roleManagerClass.getConstructor(new Class[] {});

            OwRoleManager owRoleManager = (OwRoleManager) roleManagerConstructor.newInstance(new Object[] {});

            owRoleManager.init(context, new OwStandardXMLUtil(config_p.getSubNode("RoleManager")));
            network.setRoleManager(owRoleManager);
            context.setRoleManager(owRoleManager);

            //    === create simple history manger
            OwHistoryManager eventManager = new OwSimpleHistoryManager();
            eventManager.setNetwork(network);
            network.setEventManager(eventManager);

            context.setNetwork(network);

            return network;
        }

    }

    @Override
    @SuppressWarnings("rawtypes")
    public void testSetUp(OwIntegrationTestSession session_p, String testName_p, OwIntegrationTest test_p) throws Exception
    {
        OwCMISNetwork network = setUpOwNetwork(this.bootstrapConfiguration);
        test_p.setUpAdminPassword(adminPassword);
        test_p.setUpAdminUser(adminUser);
        test_p.setUpNetwork(network);
    }
}