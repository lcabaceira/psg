package com.wewebu.ow.server.ecmimpl.fncm5.object;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.InputStream;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.ReservationType;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardContentCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5IntegrationTest;
import com.wewebu.ow.server.ecmimpl.fncm5.content.OwFNCM5ContentElement;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.util.OwMimeTypes;

/**
 *<p>
 * OwFNCM5DocumentTest.
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
public class OwFNCM5DocumentTest extends OwFNCM5IntegrationTest
{
    private static final Logger LOG = Logger.getLogger(OwFNCM5DocumentTest.class);

    private static final String DOCUMENT_TESTS = "DocumentTests";

    private OwFNCM5Object<?> doc;
    private OwFNCM5Object<?> documentTestsFolder;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        String rPath = "/" + network.getResource(null).getID() + "/Test/getByPath/getById";
        doc = (OwFNCM5Object<?>) network.getObjectFromPath(rPath, true);

        if (createManager.isFolderAvailable(DOCUMENT_TESTS))
        {
            createManager.deleteFolderByPath(DOCUMENT_TESTS);
        }
        String documentTestsDMSID = createManager.createFolder(DOCUMENT_TESTS);
        documentTestsFolder = network.getObjectFromDMSID(documentTestsDMSID, true);
    }

    @Override
    public void tearDown() throws Exception
    {
        if (createManager.isFolderAvailable(DOCUMENT_TESTS))
        {
            createManager.deleteFolderByPath(DOCUMENT_TESTS);
        }

        doc = null;
        super.tearDown();
    }

    @Test
    public void testVirtualProperty() throws Exception
    {
        assertNotNull(doc);
        assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, doc.getType());
        //OW_ObjectPath
        OwProperty path = doc.getProperty(OwResource.m_ObjectPathPropertyClass.getClassName());
        assertNotNull(path);
        assertNotNull(path.getPropertyClass());

        assertEquals(path.getPropertyClass().getJavaClassName(), OwResource.m_ObjectPathPropertyClass.getJavaClassName());
        assertEquals("/Test/getByPath/getById", path.getValue().toString());

        //OW_ObjectName
        OwProperty name = doc.getProperty(OwResource.m_ObjectNamePropertyClass.getClassName());
        assertNotNull(name);
        assertNotNull(name.getPropertyClass());

        assertEquals(name.getPropertyClass().getJavaClassName(), OwResource.m_ObjectNamePropertyClass.getJavaClassName());
        assertEquals(doc.getName(), name.getValue().toString());

        OwProperty versionSeriesProperty = doc.getProperty(OwResource.m_VersionSeriesPropertyClass.getClassName());
        assertEquals(doc.getVersionSeries().getId(), versionSeriesProperty.getValue().toString());

        OwProperty resourceProperty = doc.getProperty(OwResource.m_ResourcePropertyClass.getClassName());
        assertEquals(doc.getResource().getID(), resourceProperty.getValue().toString());
    }

    @Test
    public void testGetParents() throws Exception
    {
        assertNotNull(doc);

        OwObjectCollection col = doc.getParents();
        assertNotNull(col);
        assertEquals(1, col.size());
        assertEquals("getByPath", ((OwFNCM5Object<?>) col.get(0)).getName());
    }

    @Test
    public void testDMSID() throws Exception
    {
        assertNotNull(doc);

        OwFNCM5Object<?> idObj = network.getObjectFromDMSID(doc.getDMSID(), true);
        assertEquals(doc.getDMSID(), idObj.getDMSID());

        assertEquals(doc.getID(), idObj.getID());
    }

    @Test
    public void testGetProperty() throws Exception
    {
        assertNotNull(doc);
        assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, doc.getType());

        doc.getProperty(PropertyNames.ID);

        try
        {
            doc.getProperty("FolderName");
            fail();
        }
        catch (OwObjectNotFoundException e)
        {
            assertTrue("Exception was thrown", e != null);
        }
    }

    @Test
    public void testDelete() throws Exception
    {
        String documentDMSID = createManager.createDocument(ClassNames.DOCUMENT, true, DOCUMENT_TESTS, "aDocument");
        OwFNCM5Object<?> document = network.getObjectFromDMSID(documentDMSID, true);
        document.delete();

        try
        {
            network.getObjectFromDMSID(documentDMSID, true);
            fail("Coould not delete object!");
        }
        catch (OwObjectNotFoundException e)
        {
            LOG.debug("Sucessfully deleted object.", e);
        }
    }

    @Test
    public void testCheckOut() throws Exception
    {
        String vdDMSID = createManager.createDocument(ClassNames.DOCUMENT, true, DOCUMENT_TESTS, "aVerisonedDocument");
        OwFNCM5Object<?> vd = network.getObjectFromDMSID(vdDMSID, true);

        assertTrue(vd.hasVersionSeries());

        OwVersionSeries vs = vd.getVersionSeries();
        Collection versions = vs.getVersions(null, null, 0);
        assertEquals(1, versions.size());
        OwFNCM5Version<?> v = vd.getVersion();

        assertTrue(v.canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertTrue(v.canCheckout(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL));

        v.checkout(ReservationType.EXCLUSIVE);

        versions = vs.getVersions(null, null, 0);
        assertEquals(2, versions.size());

        OwVersion latest = vs.getLatest();
        assertTrue(latest.isMyCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
    }

    @Test
    public void testCheckInMajor_KeepContent() throws Exception
    {
        checkInTest_KeepContent(true);
    }

    @Test
    public void testCheckInMinor_KeepContent() throws Exception
    {
        checkInTest_KeepContent(false);
    }

    private int version(int[] version_p)
    {
        int v = 0;
        for (int i = 0; i < version_p.length; i++)
        {
            v += version_p[i] * Math.pow(10, version_p.length - 1 - i);
        }

        return v;
    }

    public void checkInTest_KeepContent(boolean major_p) throws Exception
    {
        String vdDMSID = createManager.createDocument(ClassNames.DOCUMENT, true, DOCUMENT_TESTS, "aVerisonedDocument");
        OwFNCM5Object<?> vd = network.getObjectFromDMSID(vdDMSID, true);

        OwFNCM5Version<?> v = vd.getVersion();
        int[] firstVersioNumber = v.getVersionNumber();

        v.checkout(ReservationType.EXCLUSIVE);
        InputStream is = OwFNCM5DocumentTest.class.getResourceAsStream("content.txt");
        final String checkinFileName = "KeepContent.txt";
        final String checkinMIMEType = "text/plain";
        final String mimeParameter = "filename=\"" + checkinFileName + "\"";

        OwStandardContentCollection contentCollection = new OwStandardContentCollection(is, OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, "text/plain");
        v.checkin(major_p, AutoClassify.AUTO_CLASSIFY, null, null, null, contentCollection, true, checkinMIMEType, mimeParameter);

        OwVersionSeries firstSeries = vd.getVersionSeries();
        OwVersion secondVersion = firstSeries.getLatest();
        int[] secondVersioNumber = secondVersion.getVersionNumber();

        assertTrue(version(secondVersioNumber) > version(firstVersioNumber));

        secondVersion.checkout(ReservationType.EXCLUSIVE);

        secondVersion.checkin(major_p, AutoClassify.AUTO_CLASSIFY, null, null, null, null, false, "", "");

        OwVersionSeries secondSeries = vd.getVersionSeries();
        OwVersion thirdVersion = secondSeries.getLatest();
        int[] thirdVersioNumber = thirdVersion.getVersionNumber();

        assertTrue(version(thirdVersioNumber) > version(secondVersioNumber));

        OwObject thirdVersionObject = secondSeries.getObject(thirdVersion);
        OwContentCollection thirdContent = thirdVersionObject.getContentCollection();
        OwContentElement keptElement = thirdContent.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);

        assertEquals(checkinMIMEType, keptElement.getMIMEType());
        assertEquals(checkinMIMEType, thirdVersionObject.getMIMEType());

        assertEquals(checkinFileName, OwMimeTypes.getMimeParameter(keptElement.getMIMEParameter(), OwFNCM5ContentElement.FILE_NAME_MIME_PARAMETER));

        thirdVersionObject.delete();
    }

    @Test
    public void testCreate_KeepCheckedout() throws Exception
    {
        final String documentName = "document_keept_checkedout";

        String dmsID = createManager.createDocument(ClassNames.DOCUMENT, false, DOCUMENT_TESTS, documentName, true);

        assertNotNull("Cannot create document - null ", dmsID);
        assertNotSame("Cannot create document - 0 len DMSID ", "", dmsID);

        OwFNCM5Object<?> newObject = network.getObjectFromDMSID(dmsID, true);
        OwFNCM5Version<?> version = newObject.getVersion();
        int[] versionNumber = version.getVersionNumber();

        assertEquals(0, versionNumber[0]);
        assertFalse(version.isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        OwVersionSeries versionSeries = newObject.getVersionSeries();
        OwVersion latestVersion = versionSeries.getLatest();

        int[] latestVersionNumber = latestVersion.getVersionNumber();

        assertEquals(version(latestVersionNumber), version(versionNumber));
    }

    @Test
    public void testCreateEmptyDocument() throws Exception
    {
        String testFolderPath = createManager.getRootPath() + DOCUMENT_TESTS;
        OwObject folder = network.getObjectFromPath(testFolderPath, true);
        OwPropertyCollection props = new OwStandardPropertyCollection();
        String dmsID = network.createNewObject(true, null, null, ClassNames.DOCUMENT, props, null, null, folder, "", "", false);
        assertNotNull(dmsID);
    }

    @Test
    public void testGetVersion() throws Exception
    {
        assertNotNull(doc.getVersion());
        doc.getVersion().getCheckedOutUserID(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    }

}
