package com.wewebu.ow.server.util;

import java.io.InputStream;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.wewebu.ow.server.util.ldap.OwLdapConnector;

public class OwLdapX500ConnectorTest extends TestCase
{
    private static String XML_SOURCE_PATH = "ldap_X500_authentication.xml";
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

        assertTrue(allGroupNames.contains("editors"));
        assertTrue(allGroupNames.contains("reviewers"));

        assertTrue(allGroupNames.contains("techpersonnel"));
        assertTrue(allGroupNames.contains("techexperts"));
        assertTrue(allGroupNames.contains("techdesigners"));

        assertFalse(groupNames.contains("editors"));
        assertTrue(groupNames.contains("reviewers"));
        assertTrue(groupNames.contains("techdevelopers"));
        assertTrue(groupNames.contains("techpersonnel"));
        assertFalse(groupNames.contains("techexperts"));

        connector.authenticate("john", "foobar2");
        assertEquals("John", connector.getUserShortName());

        Collection allJohnGroupNames = connector.getAllShortGroupNames(true);
        Collection johnGroupNames = connector.getShortGroupNames(true);

        assertTrue(allJohnGroupNames.contains("editors"));
        assertTrue(allJohnGroupNames.contains("reviewers"));

        assertTrue(allJohnGroupNames.contains("techpersonnel"));
        assertTrue(allJohnGroupNames.contains("techexperts"));
        assertTrue(allJohnGroupNames.contains("techdesigners"));

        assertTrue(johnGroupNames.contains("editors"));
        assertTrue(johnGroupNames.contains("reviewers"));

        assertTrue(johnGroupNames.contains("techdevelopers"));
        assertTrue(johnGroupNames.contains("techpersonnel"));
        assertTrue(johnGroupNames.contains("techexperts"));
        assertTrue(johnGroupNames.contains("techdesigners"));

    }

    public void testUserId() throws Exception
    {
        String uid = connector.getUserId();
        assertEquals("JUnitTester", uid);
    }

}
