package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardContentCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;

/**
 *<p>
 * OwCMISDocumentObjectTest.
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
public class OwCMISDocumentObjectTest extends OwCMISObjectTest
{
    static final Logger LOG = Logger.getLogger(OwCMISDocumentObjectTest.class);

    private static final String VERSIONING_TEST_DOC_1 = "versioningTestDoc1";
    private String versioningTestDocument1DMSID;

    public OwCMISDocumentObjectTest(String name)
    {
        super("DocumentObjectTest", name);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        versioningTestDocument1DMSID = integrationFixture.createTestObject(BaseTypeId.CMIS_DOCUMENT.value(), VERSIONING_TEST_DOC_1, null, integrationFixture.getTestFolder());
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testVersionInfo() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersion version = versioningTestDocument1.getVersion();

        assertEquals("1.0", version.getVersionInfo());
    }

    public void testCheckout() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersion version = versioningTestDocument1.getVersion();

        assertTrue(version.canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        version.checkout(null);

        assertTrue(version.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(version.canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
    }

    public void testCheckInMajorProperties() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();
        OwVersion version = versioningTestDocument1.getVersion();

        assertFalse(version.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        version.checkout(null);

        assertTrue(version.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(version.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersion reservation = versionSeries.getReservation();

        assertTrue(reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue(reservation.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        final String checkedName = "newCheckedInName";

        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        OwPropertyCollection properties = integrationFixture.createProperties(new Object[][] { { PropertyIds.NAME, checkedName } }, BaseTypeId.CMIS_DOCUMENT.value());
        reservation.checkin(true, null, BaseTypeId.CMIS_DOCUMENT.value(), properties, null, null, false, null, null);

        assertFalse(reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersion checkedInVersion = versionSeries.getLatest();
        OwObject latestObject = versionSeries.getObject(checkedInVersion);

        assertFalse(checkedInVersion.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue(checkedInVersion.isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwProperty checkedInNameProperty = latestObject.getProperty(PropertyIds.NAME);
        assertEquals(checkedName, checkedInNameProperty.getValue().toString());
    }

    public void testCheckInMinorContent() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();
        OwVersion version = versioningTestDocument1.getVersion();

        assertFalse(versioningTestDocument1.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        assertFalse(version.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        version.checkout(null);

        assertTrue(version.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(version.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersion reservation = versionSeries.getReservation();

        assertTrue(reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue(reservation.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        final String checkedName = "newCheckedInName";
        OwStandardContentCollection contentCollection = new OwStandardContentCollection(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum1.txt"), OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, "text/plain");

        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        OwPropertyCollection properties = integrationFixture.createProperties(new Object[][] { { PropertyIds.NAME, checkedName } }, BaseTypeId.CMIS_DOCUMENT.value());
        reservation.checkin(false, null, BaseTypeId.CMIS_DOCUMENT.value(), properties, null, contentCollection, true, null, null);

        assertFalse(reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersion checkedInVersion = versionSeries.getLatest();
        OwObject latestObject = versionSeries.getObject(checkedInVersion);

        assertFalse(checkedInVersion.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(checkedInVersion.isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwProperty checkedInNameProperty = latestObject.getProperty(PropertyIds.NAME);
        assertEquals(checkedName, checkedInNameProperty.getValue().toString());

        assertTrue(latestObject.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        assertEquals("text/plain", latestObject.getMIMEType());
        OwContentCollection content = latestObject.getContentCollection();
        OwContentElement elementOne = content.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 0);
        InputStream contentStream = elementOne.getContentStream(null);
        assertSameStream(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum1.txt"), contentStream);
    }

    public void testCheckInOverriteContent() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();

        {
            OwVersion version = versioningTestDocument1.getVersion();
            assertFalse(versioningTestDocument1.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
            version.checkout(null);
        }

        {
            OwVersion latestVersion = versionSeries.getReservation();//Latest();

            assertNotNull(latestVersion);
            OwStandardContentCollection contentCollection = new OwStandardContentCollection(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum1.txt"), OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, "text/plain");

            latestVersion.checkin(false, null, BaseTypeId.CMIS_DOCUMENT.value(), null, null, contentCollection, true, null, null);

            assertFalse(latestVersion.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

            OwVersion checkedInVersion = versionSeries.getLatest();
            OwObject latestObject = versionSeries.getObject(checkedInVersion);

            assertFalse(checkedInVersion.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
            assertTrue(latestObject.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
            assertEquals("text/plain", latestObject.getMIMEType());
            OwContentCollection content = latestObject.getContentCollection();
            OwContentElement elementOne = content.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 0);
            InputStream contentStream = elementOne.getContentStream(null);
            assertSameStream(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum1.txt"), contentStream);
        }

        {
            OwVersion latestVersion = versionSeries.getLatest();
            latestVersion.checkout(null);

            OwVersion reservation = versionSeries.getReservation();

            OwStandardContentCollection contentCollection = new OwStandardContentCollection(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum2.txt"), OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, "text/plain");

            reservation.checkin(false, null, BaseTypeId.CMIS_DOCUMENT.value(), null, null, contentCollection, false, null, null);

            assertFalse(reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

            OwVersion checkedInVersion = versionSeries.getLatest();
            OwObject latestObject = versionSeries.getObject(checkedInVersion);

            assertFalse(checkedInVersion.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
            assertTrue(latestObject.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
            assertEquals("text/plain", latestObject.getMIMEType());
            OwContentCollection content = latestObject.getContentCollection();
            OwContentElement elementOne = content.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 0);
            InputStream contentStream = elementOne.getContentStream(null);
            assertSameStream(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum1.txt"), contentStream);
        }

    }

    public void testCancelcheckout() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();

        OwVersion version = versioningTestDocument1.getVersion();

        assertFalse(version.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(version.canCheckin(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(version.canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue(version.canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        version.checkout(null);

        assertTrue(version.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(version.canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersion reservation = versionSeries.getReservation();

        assertTrue(reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue(reservation.canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        reservation.cancelcheckout();//Reservation object will be invalid after call

        assertNull(versionSeries.getReservation());
        reservation = versionSeries.getLatest();//get latest object, pwc is invalid in case of alfresco

        assertFalse(reservation.isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertFalse(reservation.canCancelcheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

    }

    public void testLatestVersion() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersion version = versioningTestDocument1.getVersion();
        OwVersionSeries leakedVersionSeries = versioningTestDocument1.getVersionSeries();
        {
            OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();
            OwVersion latestVersion = versionSeries.getLatest();

            assertEquals("1.0", latestVersion.getVersionInfo());

            OwVersion latestLeaked = leakedVersionSeries.getLatest();
            assertEquals("1.0", latestLeaked.getVersionInfo());
        }

        version.checkout(null);

        {
            OwVersion latestLeaked = leakedVersionSeries.getLatest();
            assertNotSame("Latest version is not PWC", "pwc", latestLeaked.getVersionInfo());
            assertNotNull("getReservation from kept VersionSeries is null", leakedVersionSeries.getReservation());
            assertEquals("pwc", leakedVersionSeries.getReservation().getVersionInfo());

            OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();
            OwVersion latestVersion = versionSeries.getLatest();
            assertNotSame("Latest version is not PWC", "pwc", latestVersion.getVersionInfo());
            assertNotNull("getReservation from new VersionSeries is null", versionSeries.getReservation());
            assertEquals("pwc", versionSeries.getReservation().getVersionInfo());

        }
    }

    public void testSave() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);

        OwVersion version = versioningTestDocument1.getVersion();

        assertFalse(versioningTestDocument1.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue(version.canSave(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwStandardContentCollection contentCollection1 = new OwStandardContentCollection(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum1.txt"), OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, "text/plain");
        version.save(contentCollection1, null, null);

        assertTrue(versioningTestDocument1.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertEquals("text/plain", versioningTestDocument1.getMIMEType());

        OwContentCollection content = versioningTestDocument1.getContentCollection();

        OwContentElement elementOne = content.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 0);
        InputStream contentStream = elementOne.getContentStream(null);
        assertSameStream(OwCMISDocumentObjectTest.class.getResourceAsStream("loremipsum1.txt"), contentStream);

        //        version.save(null, null, null);
        //        assertFalse(versioningTestDocument1.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));    
    }

    public void testVersionId() throws Exception
    {
        OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwVersionSeries versionSeries = versioningTestDocument1.getVersionSeries();

        OwVersion version1 = versioningTestDocument1.getVersion();
        version1.checkout(null);

        OwVersion reservation = versionSeries.getReservation();

        final String checkedName = "newCheckedInName";
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        OwPropertyCollection properties = integrationFixture.createProperties(new Object[][] { { PropertyIds.NAME, checkedName } }, BaseTypeId.CMIS_DOCUMENT.value());
        reservation.checkin(true, null, BaseTypeId.CMIS_DOCUMENT.value(), properties, null, null, false, null, null);

        Collection<?> versions = versionSeries.getVersions(null, null, 0);
        for (Iterator<?> i = versions.iterator(); i.hasNext();)
        {
            OwVersion version = (OwVersion) i.next();

            OwObject versionObject = versionSeries.getObject(version);

            OwCMISObject versionObjectByDMSID = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versionObject.getDMSID(), true);

            OwVersion versionByDMISID = versionObjectByDMSID.getVersion();

            assertEquals(version.getVersionInfo(), versionByDMISID.getVersionInfo());
            assertEquals(versionObject.getDMSID(), versionObjectByDMSID.getDMSID());
        }

    }

    public void testIsMyCheckout() throws Exception
    {
        this.getNetwork().logout();

        login("admin", "admin");

        {
            OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);

            OwVersion version1 = versioningTestDocument1.getVersion();
            version1.checkout(null);

            assertTrue(version1.isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        }

        this.getNetwork().logout();

        login("JUnitTester", "junit");

        {
            OwCMISObject versioningTestDocument1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);

            OwVersion version1 = versioningTestDocument1.getVersion();

            assertFalse(version1.isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        }
    }

    public void testGetVirtualProperties() throws Exception
    {
        List<String> virtualPropNames = new LinkedList<String>();
        virtualPropNames.add("OW_ObjectName");
        virtualPropNames.add("OW_ObjectPath");
        virtualPropNames.add("OW_ClassDescription");
        virtualPropNames.add("OW_VersionSeries");

        OwCMISObject doc = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);

        //all properties should be returned
        OwPropertyCollection propCol = doc.getProperties(null);
        for (String virtualProp : virtualPropNames)
        {
            assertTrue("Missing virtual Property, name = " + virtualProp, propCol.containsKey(virtualProp));
        }
        assertTrue(propCol.containsKey("cmis:document.cmis:name"));

        List<String> props = new LinkedList<String>();
        props.add("cmis:document.cmis:name");
        props.add("cmis:document.cmis:objectTypeId");
        props.add("cmis:document.cmis:baseTypeId");

        propCol = doc.getProperties(props);
        for (String prop : props)
        {
            assertTrue("Missing requested property name = " + prop, propCol.containsKey(prop));
        }

        for (String virtualProp : virtualPropNames)
        {
            assertFalse("Returning virtual Property, but was not requested", propCol.containsKey(virtualProp));
        }

    }
}
