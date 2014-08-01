package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.Collection;
import java.util.Set;

import junit.framework.TestCase;

import org.alfresco.wd.ext.restlet.auth.OwRestletBasicAuthenticationHandler;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

public class TestUsersRepositoryAlfresco extends TestCase
{
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    OwUsersRepositoryAlfresco repository;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        StringBuilder config = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        config.append("<Authentication mode=\"ALFRESCO\"><BaseURL>");
        config.append("http://abs-alfone.alfresco.com:8080/alfresco");
        config.append("</BaseURL></Authentication>");

        org.w3c.dom.Document doc = OwXMLDOMUtil.getDocumentFromString(config.toString());

        OwAuthenticationConfiguration conf = new OwAuthenticationConfiguration(doc.getFirstChild());
        repository = new OwUsersRepositoryAlfresco(conf, new OwRestletBasicAuthenticationHandler(USERNAME, PASSWORD));
    }

    public void testFindGroupsMatching() throws Exception
    {
        Set<OwGroup> allGroups = this.repository.findGroupsMatching("*");
        assertFalse(allGroups.isEmpty());

        Set<OwGroup> alfGroups = this.repository.findGroupsMatching("Alf*");
        assertEquals(2, alfGroups.size());

        alfGroups = this.repository.findGroupsMatching("ALFRESCO_ADMINISTRATORS");
        assertEquals(1, alfGroups.size());
        OwGroup first = alfGroups.iterator().next();
        assertEquals("GROUP_ALFRESCO_ADMINISTRATORS", first.getId());
        assertEquals("ALFRESCO_ADMINISTRATORS", first.getName());
    }

    public void testFindUsersMatching() throws Exception
    {
        OwObjectCollection allUsers = this.repository.findUsersMatching("*");
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());

        OwUser adminUser = null;
        for (Object record : allUsers)
        {
            OwUser aUser = (OwUser) record;
            if ("admin".equals(aUser.getId()))
            {
                adminUser = aUser;
                break;
            }
        }

        assertNotNull(adminUser);
        assertNotNull(adminUser.getGroups());
        assertFalse(adminUser.getGroups().isEmpty());
    }

    public void testFindUserByID() throws Exception
    {
        OwUserInfo adminUser = this.repository.findUserByID("admin");
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUserID());
        assertEquals("admin", adminUser.getUserName());
        assertEquals("Administrator ", adminUser.getUserDisplayName());
        assertEquals("Administrator ", adminUser.getUserLongName());
        assertEquals("", adminUser.getUserShortName());

        Collection adminGroups = adminUser.getGroups();
        assertNotNull(adminGroups);
        assertFalse(adminGroups.isEmpty());
    }
}
