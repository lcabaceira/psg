package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

public class TestUsersRepositoryLDAPAD extends TestCase
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

        //AD LDAP
        Properties ldapProps = new Properties();
        ldapProps.put("java.naming.provider.url", "ldap://abs-dbmssql.alfresco.com:389");
        ldapProps.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        ldapProps.put("java.naming.security.authentication", "simple");
        ldapProps.put("java.naming.security.principal", "CN=Administrator,CN=Users,DC=wewebu-virtual,DC=local");
        ldapProps.put("java.naming.security.credentials", "wewebu2007");
        ldapProps.put("loginQueryName", "sAMAccountName");

        ldapProps.put("UsersDirectory", "CN=Users,DC=wewebu-virtual,DC=local");
        ldapProps.put("GroupsDirectory", "CN=Users,DC=wewebu-virtual,DC=local");

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
        Set<OwGroup> fnAdminGroups = this.repository.findGroupsMatching("FNADMIN*");
        assertEquals(1, fnAdminGroups.size());
        OwGroup first = fnAdminGroups.iterator().next();
        assertEquals("CN=FNADMIN,CN=Users,DC=wewebu-virtual,DC=local", first.getId());
        assertEquals("FNADMIN", first.getName());

        Set<OwGroup> rolesGroups = this.repository.findGroupsMatching("Role*");
        assertEquals(4, rolesGroups.size());
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
            if ("Administrator".equals(aUser.getName()))
            {
                adminUser = aUser;
                break;
            }
        }

        assertNotNull(adminUser);
        //TODO: getGroups is not implemented yet
        //        assertNotNull(adminUser.getGroups());
        //        assertFalse(adminUser.getGroups().isEmpty());
    }

    public void testFindUserByID() throws Exception
    {
        OwUserInfo adminUser = this.repository.findUserByID("admin");
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUserID());
        assertEquals("admin", adminUser.getUserName());
        assertEquals("CMIS Admin", adminUser.getUserDisplayName());
        assertEquals("CN=CMIS Admin,CN=Users,DC=wewebu-virtual,DC=local", adminUser.getUserLongName());
        //assertEquals("vahe", adminUser.getUserShortName());

        //TODO: groups are not properly implemented
        //        Collection adminGroups = adminUser.getGroups();
        //        assertNotNull(adminGroups);
        //        assertFalse(adminGroups.isEmpty());
    }
}
