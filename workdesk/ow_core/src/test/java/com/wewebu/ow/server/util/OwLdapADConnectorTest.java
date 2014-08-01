package com.wewebu.ow.server.util;

import java.io.InputStream;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.wewebu.ow.server.util.ldap.OwLdapConnector;

public class OwLdapADConnectorTest extends TestCase
{
    private static String XML_SOURCE_PATH = "ldap_AD_authentication.xml";
    private OwLdapConnector connector;

    protected void setUp() throws Exception
    {
        InputStream is = this.getClass().getResourceAsStream(XML_SOURCE_PATH);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        connector = new OwLdapConnector(document.getDocumentElement());
        //connector.authenticate("CN=JUnit JT. Tester,CN=Users,DC=wewebu-virtual,DC=local", "junit");
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

    public void testGetUserShortNameAdmin() throws Exception
    {
        connector.authenticate("Administrator", "wewebu2007");
        assertEquals("Administrator", connector.getUserShortName());
    }

    public void testGetUserDisplayNameAdmin() throws Exception
    {
        connector.authenticate("Administrator", "wewebu2007");
        assertEquals("Administrator", connector.getUserDisplayName());
    }

    public void testGetUserLongNameAdmin() throws Exception
    {
        connector.authenticate("Administrator", "wewebu2007");
        assertEquals("Administrator", connector.getUserLongName());
    }

    public void testUserInTopOU() throws Exception
    {
        connector.authenticate("JUnitTesterInTopOU", "junit");
        assertEquals("JUnitTesterInTopOU", connector.getUserLongName());
        assertEquals("JUnitTesterInTopOU", connector.getUserShortName());
        assertEquals("JUnitTesterInTopOU", connector.getUserDisplayName());
    }

    public void testUserInSubOU() throws Exception
    {
        connector.authenticate("JUnitTesterInSubOU", "junit");
        assertNotNull(connector);
        assertEquals("JUnitTesterInSubOU", connector.getUserLongName());
        assertEquals("JUnitTesterInSubOU", connector.getUserShortName());
        assertEquals("JUnitTesterInSubOU", connector.getUserDisplayName());
    }

    public void testUserId() throws Exception
    {
        String uid = connector.getUserId();
        assertEquals("\\31\\31\\00\\35\\fa\\2d\\a8\\43\\86\\92\\a1\\9e\\f7\\14\\3b\\74", uid);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testGroups() throws Exception
    {
        Collection allGroupNames = connector.getAllShortGroupNames(true);
        Collection groupNames = connector.getShortGroupNames(true);

        assertTrue(groupNames.contains("FNADMIN"));
        assertTrue(groupNames.contains("JUnitTesters"));
        assertFalse(groupNames.contains("Human Resources"));

        assertTrue(allGroupNames.containsAll(groupNames));
        assertTrue(allGroupNames.contains("Human Resources"));

        connector.authenticate("Administrator", "wewebu2007");
        assertEquals("Administrator", connector.getUserShortName());
    }
}
