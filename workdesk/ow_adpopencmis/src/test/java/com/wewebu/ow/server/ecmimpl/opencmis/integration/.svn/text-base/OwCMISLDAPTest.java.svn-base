package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import test.com.wewebu.ow.server.ecm.OwIntegrationTestSetup;

import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwRole;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.exceptions.OwException;

public class OwCMISLDAPTest extends OwCMISIntegrationTest
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(OwCMISLDAPTest.class);

    protected static final String LDAP_CMIS_CONFIG_BOOTSTRAP = "cmis_owbootstrap_ldap.xml";

    public OwCMISLDAPTest(String name_p)
    {
        super("LDAP", name_p);
    }

    @Override
    protected Class<? extends OwIntegrationTestSetup> getSetupClass()
    {
        return OwCMISLDAPTestSetup.class;
    }

    @Override
    protected String getConfigBootstrapName()
    {
        return OwCMISLDAPTestSetup.LDAP_CMIS_CONFIG_BOOTSTRAP;
    }

    @Override
    protected String getResoucesFolderPath()
    {
        return OwCMISBasicIntegrationTestSetup.TEST_CMIS_RESOUCES_FOLDER_PATH;
    }

    @SuppressWarnings("rawtypes")
    public void testGroups() throws Exception
    {
        OwCredentials credentials = this.getNetwork().getCredentials();
        OwUserInfo userInfo = credentials.getUserInfo();

        Collection groups = userInfo.getGroups();
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
        for (Object group : groups)
        {
            assertNotNull(group);
            assertTrue("Invalid group class : " + group.getClass(), group instanceof OwUserInfo);
            OwUserInfo groupUserInfo = (OwUserInfo) group;
            LOG.debug("Got group : " + groupUserInfo.getUserDisplayName());
        }
    }

    @SuppressWarnings("rawtypes")
    public void testRoles() throws Exception
    {
        OwCredentials credentials = this.getNetwork().getCredentials();
        OwUserInfo userInfo = credentials.getUserInfo();

        Collection roles = userInfo.getRoleNames();
        assertTrue(roles.contains(OwRole.OW_AUTHENTICATED));
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
        for (Object role : roles)
        {
            assertNotNull(role);
            assertTrue("Invalid group class : " + role.getClass(), role instanceof String);
            String roleStr = (String) role;
            LOG.debug("Got role : " + roleStr);
        }
    }

    public void testNames() throws Exception
    {
        OwCredentials credentials = this.getNetwork().getCredentials();
        OwUserInfo userInfo = credentials.getUserInfo();

        String userLongName = userInfo.getUserLongName();
        assertNotNull(userLongName);
        LOG.debug("Long name : " + userLongName);

        String userShortName = userInfo.getUserShortName();
        assertNotNull(userShortName);
        LOG.debug("Short name : " + userShortName);

        assertTrue(userShortName.length() < userLongName.length());

    }

    @SuppressWarnings("unchecked")
    public void testOwUserInfoObject() throws Exception
    {
        OwCredentials credentials = this.getNetwork().getCredentials();
        OwUserInfo userInfo = credentials.getUserInfo();

        assertNotNull(userInfo);

        assertNotNull(userInfo.getUserName());
        assertEquals("test", userInfo.getUserName());

        assertNotNull(userInfo.getUserLongName());
        assertEquals("CN=test,CN=USERS,CN=alfone,DC=abs,DC=local", userInfo.getUserLongName());

        assertNotNull(userInfo.getUserShortName());
        assertEquals("shorty", userInfo.getUserShortName());

        assertNotNull(userInfo.getUserDisplayName());
        assertEquals("UItest", userInfo.getUserDisplayName());

        assertNotNull(userInfo.getUserID());

        assertNotNull(userInfo.getGroups());
        List<OwUserInfo> groups = new ArrayList<OwUserInfo>(userInfo.getGroups());
        assertTrue(groups.size() >= 1);
        assertEquals("JUnitTesters", groups.get(0).getUserName());

        assertNotNull(userInfo.getRoleNames());
        assertTrue(userInfo.getRoleNames().size() >= 2);
        assertTrue(userInfo.getRoleNames().contains("JUnitTesters"));
        assertTrue(userInfo.getRoleNames().contains("OW_Authenticated"));

        assertFalse(userInfo.isGroup());
        assertTrue(userInfo.equals(credentials.getUserInfo()));
    }

    public void testGetRoleDisplayName() throws Exception
    {
        assertNotNull(this.getNetwork().getRoleDisplayName("JUnitTesters"));
    }

    public void testGetUserFromId() throws Exception
    {
        OwCredentials credentials = this.getNetwork().getCredentials();
        OwUserInfo userInfo = credentials.getUserInfo();

        assertNotNull(this.getNetwork().getUserFromID(userInfo.getUserID()));
    }

    public void testGetUserSelectionUI() throws OwException
    {
        OwUIUserSelectModul userSelectSubModul = this.getNetwork().getUserSelectSubModul("nonNullId", new int[0]);
        assertNotNull(userSelectSubModul);
    }
}
