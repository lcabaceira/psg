package com.wewebu.ow.server.util;

import java.io.InputStream;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.wewebu.ow.server.util.ldap.OwLdapConnector;

public class OwLdapUPAConnectorTest extends TestCase
{
    private static String XML_SOURCE_PATH = "ldap_UPA_authentication.xml";
    private OwLdapConnector connector;

    protected void setUp() throws Exception
    {
        InputStream is = this.getClass().getResourceAsStream(XML_SOURCE_PATH);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        connector = new OwLdapConnector(document.getDocumentElement());
        //connector.authenticate("uid=JUnitTester,ou=people,dc=example,dc=com", "junit");
        connector.authenticate("JUnitTester", "junit");
    }

    public void testGetUserLongName() throws Exception
    {
        assertEquals("JUnit Tester", connector.getUserLongName());
    }

    public void testGetUserShortName() throws Exception
    {
        assertEquals("JUnitTester", connector.getUserShortName());
    }

    public void testGetUserDisplayName() throws Exception
    {
        assertEquals("JUnit Tester", connector.getUserDisplayName());
    }

    public void testGetUserShortNameJohn() throws Exception
    {
        connector.authenticate("john", "foobar2");
        assertEquals("John", connector.getUserShortName());
    }

    public void testGetUserDisplayNameJohn() throws Exception
    {
        connector.authenticate("john", "foobar2");
        assertEquals("John Doe", connector.getUserDisplayName());
    }

    public void testGroups() throws Exception
    {
        Collection allGroupNames = connector.getAllShortGroupNames(true);
        Collection groupNames = connector.getShortGroupNames(true);

        assertTrue(allGroupNames.contains("testers"));
        assertTrue(allGroupNames.contains("administrators"));

        assertTrue(groupNames.contains("testers"));
        assertFalse(groupNames.contains("administrators"));

        connector.authenticate("john", "foobar2");
        assertEquals("John", connector.getUserShortName());

        Collection allAdminGroupNames = connector.getAllShortGroupNames(true);
        Collection adminGroupNames = connector.getShortGroupNames(true);

        assertTrue(allAdminGroupNames.contains("administrators"));
        assertTrue(allAdminGroupNames.contains("testers"));
        assertTrue(adminGroupNames.contains("administrators"));
        assertTrue(adminGroupNames.contains("testers"));
    }

    public void testUserId() throws Exception
    {
        String uid = connector.getUserId();
        assertEquals("JUnitTester", uid);
    }

}
