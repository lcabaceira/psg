package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import test.com.wewebu.ow.server.ecm.OwIntegrationTest;
import test.com.wewebu.ow.server.ecm.OwIntegrationTestSession;

import com.wewebu.ow.server.ecm.OwNetwork;

public class OwCMISLDAPTestSetup extends OwCMISBasicIntegrationTestSetup
{
    private static final Logger LOG = Logger.getLogger(OwCMISLDAPTestSetup.class);

    protected static final String LDAP_CMIS_CONFIG_BOOTSTRAP = "cmis_owbootstrap_ldap.xml";
    private String ldapUser;
    private String ldapPassword;

    public OwCMISLDAPTestSetup(String bootstrapConfigurationName_p)
    {
        super(bootstrapConfigurationName_p);
    }

    @Override
    public void sessionSetUp(OwIntegrationTestSession session_p) throws FileNotFoundException, Exception
    {
        super.sessionSetUp(session_p);
        this.ldapUser = getTesterProperty("cmis.junit.ldap.user");
        this.ldapPassword = getTesterProperty("cmis.junit.ldap.user.password");
        LOG.debug("OwCMISLDAPTestSetup.sessionSetUp(): set up @ " + session_p.getSessionName());
    }

    @Override
    public void sessionTearDown(OwIntegrationTestSession session_p) throws Exception
    {
        super.sessionTearDown(session_p);
        LOG.debug("OwCMISLDAPTestSetup.sessionTearDown(): tore down @ " + session_p.getSessionName());

    }

    @Override
    public void testSetUp(OwIntegrationTestSession session_p, String testName_p, OwIntegrationTest test_p) throws Exception
    {
        super.testSetUp(session_p, testName_p, test_p);
        OwNetwork network = test_p.getNetwork();
        network.loginDefault(this.ldapUser, this.ldapPassword);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISNetworkReusingSetup#testTearDown(test.com.wewebu.ow.server.ecm.OwIntegrationTestSession, java.lang.String, test.com.wewebu.ow.server.ecm.OwIntegrationTest)
     */
    @Override
    public void testTearDown(OwIntegrationTestSession session_p, String testName_p, OwIntegrationTest test_p) throws Exception
    {
        OwNetwork network = test_p.getNetwork();
        network.logout();
        super.testTearDown(session_p, testName_p, test_p);
    }
}
