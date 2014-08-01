package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

public class TestUsersRepositoryLDAPOpenLDAP extends TestCase
{
    OwUsersRepositoryLDAP repository;
    private OwLdapConnector ldapConnector;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        //Open LDAP
        Properties ldapProps = new Properties();
        ldapProps.put("java.naming.provider.url", "ldap://192.168.5.208:389");
        ldapProps.put("SchemaInterpreter", "com.wewebu.ow.server.util.ldap.OwStandardSchemaInterpreter");

        ldapProps.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        ldapProps.put("java.naming.security.authentication", "simple");
        ldapProps.put("java.naming.security.principal", "cn=Manager,dc=alfresco,dc=local");
        ldapProps.put("java.naming.security.credentials", "Manager");

        ldapProps.put("loginQueryName", "uid");
        ldapProps.put("anonymousLogin", "false");

        ldapProps.put("UsersDirectory", "ou=people,dc=alfresco,dc=local");
        ldapProps.put("GroupsDirectory", "ou=groups,dc=alfresco,dc=local");

        ldapProps.put("GroupsObjectClass", "posixGroup");
        ldapProps.put("GroupReferenceAttribute", "member");
        ldapProps.put("UserGroupReference", "!");

        this.ldapConnector = new OwLdapConnector(ldapProps, 0);
        repository = new OwUsersRepositoryLDAP(ldapConnector);
    }

    public void testFindGroupsMatching() throws Exception
    {
        Set<OwGroup> allGroups = this.repository.findGroupsMatching("*");
        assertFalse(allGroups.isEmpty());
        assertTrue(1 < allGroups.size());

        //CN=Administrators,CN=Builtin,DC=wewebu-virtual,DC=local
        //CN=FNADMIN,CN=Users,DC=wewebu-virtual,DC=local
        Set<OwGroup> minersGroups = this.repository.findGroupsMatching("min*");
        assertEquals(1, minersGroups.size());
        OwGroup first = minersGroups.iterator().next();
        assertEquals("cn=miners,ou=groups,dc=alfresco,dc=local", first.getId());
        assertEquals("miners", first.getName());

        Set<OwGroup> groupsEndingInErs = this.repository.findGroupsMatching("*ers");
        assertEquals(2, groupsEndingInErs.size());
    }

    public void testFindUsersMatching() throws Exception
    {
        OwObjectCollection allUsers = this.repository.findUsersMatching("Admin*");
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());

        OwUser adminUser = null;
        for (Object record : allUsers)
        {
            OwUser aUser = (OwUser) record;
            if ("admin".equals(aUser.getName()))
            {
                adminUser = aUser;
                break;
            }
        }

        assertNotNull(adminUser);
        //TODO: add "member" attribute in test data
        //        assertNotNull(adminUser.getGroups());
        //        assertFalse(adminUser.getGroups().isEmpty());
    }

    @SuppressWarnings("rawtypes")
    public void testFindUserByID() throws Exception
    {
        OwUserInfo adminUser = this.repository.findUserByID("admin");
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUserID());
        assertEquals("admin", adminUser.getUserName());
        assertEquals("uid=admin,ou=people,dc=alfresco,dc=local", adminUser.getUserDisplayName());
        assertEquals("uid=admin,ou=people,dc=alfresco,dc=local", adminUser.getUserLongName());

        //TODO: add "member" attribute in test data
        //        Collection adminGroups = adminUser.getGroups();
        //        assertNotNull(adminGroups);
        //        assertFalse(adminGroups.isEmpty());
    }
}
