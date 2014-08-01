package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 * 
 *<p>
 * Integration Test for deletion (Document, Folder+Tree) and removeReference. 
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
public class OwCMISDeletionTest extends OwCMISIntegrationTest
{
    private static final Logger LOG = Logger.getLogger(OwCMISDeletionTest.class);
    protected static final String TEST_PATH = OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST.substring(OwCMISIntegrationFixture.MAIN_REPOSITORY.length() + 1);
    protected static final String TEST_FOLDER_NAME = "DeleteRefFolder";
    protected static final String TEST_DOC_NAME = "TreeDeleteDoc";
    private static final String DOCUMENTS_XML = "Documents.xml";

    public OwCMISDeletionTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected void postSetUp() throws Exception
    {
        super.postSetUp();
        loginAdmin();
        OwCMISNativeSession ses = (OwCMISNativeSession) getNetwork().getDefaultSession();
        Session natSes = ses.getOpenCMISSession();

        Map<String, String> props = new HashMap<String, String>();
        props.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_FOLDER.value());
        props.put(PropertyIds.NAME, TEST_FOLDER_NAME);
        checkObjectByPath(natSes, TEST_PATH + TEST_FOLDER_NAME, props);

        props.clear();
        props.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_DOCUMENT.value());
        props.put(PropertyIds.NAME, TEST_DOC_NAME);
        checkObjectByPath(natSes, TEST_PATH + TEST_FOLDER_NAME + OwObject.STANDARD_PATH_DELIMITER + TEST_DOC_NAME, props);

        try
        {
            OwObject documentsXmlObject = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME + OwObject.STANDARD_PATH_DELIMITER + DOCUMENTS_XML, true);
            documentsXmlObject.delete();
        }
        catch (OwObjectNotFoundException e)
        {
            //void
        }

        props = new HashMap<String, String>();
        props.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_DOCUMENT.value());
        props.put(PropertyIds.NAME, DOCUMENTS_XML);
        checkObjectByPath(natSes, OwCMISIntegrationFixture.J_UNIT_TEST + DOCUMENTS_XML, props);

    }

    protected void checkObjectByPath(Session natSes, String path, Map<String, String> createProps)
    {
        try
        {
            CmisObject obj = natSes.getObjectByPath(path);
            assertNotNull(obj);
            LOG.debug("Test path = " + path + " exist");
        }
        catch (CmisBaseException cex)
        {
            if (LOG.isTraceEnabled())
            {
                LOG.trace("Test path " + path + " missing will be created", cex);
            }
            else
            {
                LOG.debug("Test path " + path + " missing will be created");
            }
            //get Parent to create object 
            Folder parentFolder = (Folder) natSes.getObjectByPath(path.substring(0, path.lastIndexOf('/')));
            String type = createProps.get(PropertyIds.OBJECT_TYPE_ID);
            if (BaseTypeId.CMIS_FOLDER.value().equals(type))
            {
                parentFolder.createFolder(createProps);
            }
            else
            {
                parentFolder.createDocument(createProps, null, VersioningState.MAJOR);
            }
        }
    }

    public void testRemoveRef() throws Exception
    {

        OwObject doc = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + DOCUMENTS_XML, true);
        assertNotNull(doc);
        OwObject refParent = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME, true);
        assertNotNull(refParent);
        refParent.add(doc);

        doc = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME + OwObject.STANDARD_PATH_DELIMITER + doc.getName(), true);
        assertNotNull(doc);
        refParent.removeReference(doc);//Danger if failing the Documents.xml will also be deleted
    }

    public void testDeleteDocument() throws Exception
    {
        OwObject obj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME + OwObject.STANDARD_PATH_DELIMITER + TEST_DOC_NAME, true);
        obj.delete();//Delete of a document
        obj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME, true);
        obj.delete();//Delete of an empty folder
    }

    public void testDeleteTree() throws Exception
    {
        OwObject folderWithDocument = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME, true);
        folderWithDocument.delete();//Delete of a tree structure
    }

    public void testGetParents() throws Exception
    {
        String path = OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + TEST_FOLDER_NAME;
        OwObject folder = getNetwork().getObjectFromPath(path, true);

        assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, folder.getType());
        OwObjectCollection col = folder.getParents();
        assertNotNull("No parents available", col);
        assertEquals("Must return only one parent, CMIS spec restriction", 1, col.size());

        OwObject parent = (OwObject) col.get(0);
        String[] splitPath = path.split(OwObject.STANDARD_PATH_DELIMITER);
        assertTrue(parent.getPath().endsWith(splitPath[splitPath.length - 2]));

        col = parent.getParents();
        assertNotNull("No parents available", col);
        assertEquals("Must return only one parent, CMIS spec restriction", 1, col.size());
        OwObject repoRoot = (OwObject) col.get(0);
        assertNull("No parents should return null", repoRoot.getParents());
    }

}
