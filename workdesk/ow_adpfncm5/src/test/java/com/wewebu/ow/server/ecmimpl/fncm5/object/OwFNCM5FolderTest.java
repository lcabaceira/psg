package com.wewebu.ow.server.ecmimpl.fncm5.object;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5IntegrationTest;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwSearchTemplate;

/**
 *<p>
 * OwFNCM5FolderTest.
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
public class OwFNCM5FolderTest extends OwFNCM5IntegrationTest
{
    private static final Logger LOG = Logger.getLogger(OwFNCM5FolderTest.class);

    private static final String SOURCE_FOLDER_NAME = "sourceFolder";
    private static final String DESTINATION_FOLDER_NAME = "destinationFolder";

    protected OwFNCM5Object<?> f;
    protected String rPath = "/Test/getByPath";
    private OwFNCM5Object<?> sourceFolder;
    private OwFNCM5Object<?> destinationFolder;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        String rootId = network.getResource(null).getID();
        f = (OwFNCM5Object<?>) network.getObjectFromPath("/" + rootId + rPath, true);

        if (createManager.isFolderAvailable(SOURCE_FOLDER_NAME))
        {
            createManager.deleteFolderByPath(SOURCE_FOLDER_NAME);
        }
        String sourceFolderDMSID = createManager.createFolder(SOURCE_FOLDER_NAME);
        sourceFolder = network.getObjectFromDMSID(sourceFolderDMSID, true);

        if (createManager.isFolderAvailable(DESTINATION_FOLDER_NAME))
        {
            createManager.deleteFolderByPath(DESTINATION_FOLDER_NAME);
        }
        String destinationFolderDMSID = createManager.createFolder(DESTINATION_FOLDER_NAME);
        destinationFolder = network.getObjectFromDMSID(destinationFolderDMSID, true);
    }

    @Override
    public void tearDown() throws Exception
    {
        f = null;
        if (createManager.isFolderAvailable(SOURCE_FOLDER_NAME))
        {
            createManager.deleteFolderByPath(SOURCE_FOLDER_NAME);
        }
        if (createManager.isFolderAvailable(DESTINATION_FOLDER_NAME))
        {
            createManager.deleteFolderByPath(DESTINATION_FOLDER_NAME);
        }
        super.tearDown();
    }

    @Test
    public void testVirtualProperty() throws Exception
    {
        assertNotNull(f);
        assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, f.getType());
        //OW_ObjectPath
        OwProperty path = f.getProperty(OwResource.m_ObjectPathPropertyClass.getClassName());
        assertNotNull(path);
        assertNotNull(path.getPropertyClass());

        assertEquals(path.getPropertyClass().getJavaClassName(), OwResource.m_ObjectPathPropertyClass.getJavaClassName());
        assertEquals(rPath, path.getValue().toString());

        //OW_ObjectName
        OwProperty name = f.getProperty(OwResource.m_ObjectNamePropertyClass.getClassName());
        assertNotNull(name);
        assertNotNull(name.getPropertyClass());

        assertEquals(name.getPropertyClass().getJavaClassName(), OwResource.m_ObjectNamePropertyClass.getJavaClassName());
        assertEquals(f.getName(), name.getValue().toString());
    }

    @Test
    public void testGetParents() throws Exception
    {
        assertNotNull(f);

        OwObjectCollection col = f.getParents();
        assertNotNull(col);
        assertEquals(1, col.size());
        assertEquals("Test", ((OwFNCM5Object<?>) col.get(0)).getName());
    }

    @Test
    public void testHiddenPropery() throws Exception
    {
        assertNotNull(f);

        OwProperty prop = f.getProperty(PropertyNames.PARENT);
        assertNotNull(prop);
        assertTrue(prop.isHidden(OwPropertyClass.CONTEXT_NORMAL));
        assertTrue(prop.isHidden(OwPropertyClass.CONTEXT_ON_CHECKIN));
    }

    @Test
    public void testMIMEType() throws Exception
    {
        assertNotNull(f);

        assertTrue(f.getMIMEType().startsWith("ow_folder"));
        String fullMime = "ow_folder/" + f.getObjectClass().getClassName();

        assertEquals(fullMime, f.getMIMEType());
    }

    @Test
    public void testDMSID() throws Exception
    {
        assertNotNull(f);

        OwFNCM5Object<?> cIdObj = network.getObjectFromDMSID(f.getDMSID(), true);
        assertEquals(f.getDMSID(), cIdObj.getDMSID());

        assertEquals(f.getID(), cIdObj.getID());
    }

    @Test
    public void testNonNullMIME() throws Exception
    {
        assertNotNull(f);
        OwObjectCollection col = f.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS, OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS, OwObjectReference.OBJECT_TYPE_CUSTOM }, null, null, 50,
                OwSearchTemplate.VERSION_SELECT_DEFAULT, null);
        for (int i = 0; i < col.size(); i++)
        {
            OwObject obj = (OwObject) col.get(i);
            assertNotNull(obj);
            assertNotNull(obj.getID());
            assertNotNull(obj.getMIMEType());
            assertNotNull(obj.getMIMEParameter());
        }
    }

    @Test
    public void testMoveDocument() throws Exception
    {
        String sourceDocumentDmsid = createManager.createDocument(ClassNames.DOCUMENT, true, SOURCE_FOLDER_NAME, "aDocument");
        OwFNCM5Object<?> sourceDocument = network.getObjectFromDMSID(sourceDocumentDmsid, true);
        String sourceDocumentName = sourceDocument.getName();

        OwObjectCollection sourceContent = sourceFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertEquals(1, sourceContent.size());

        OwObjectCollection destinationContent = destinationFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertTrue(destinationContent.isEmpty());
        destinationFolder.move(sourceDocument, sourceFolder);

        destinationContent = destinationFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertEquals(1, destinationContent.size());

        OwFNCM5Object<?> destinationDocument = (OwFNCM5Object<?>) destinationContent.get(0);
        Assert.assertEquals(sourceDocumentName, destinationDocument.getName());

        sourceContent = sourceFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertTrue("The document should have been removed from its previous parent!", sourceContent.isEmpty());
    }

    @Test
    public void testMoveFolder() throws Exception
    {
        String sourceSubfolderDmsid = createManager.createFolder(sourceFolder, "sourceSubfolder");
        OwFNCM5Object<?> sourceSubfolder = network.getObjectFromDMSID(sourceSubfolderDmsid, true);
        String sourceSubfolderName = sourceSubfolder.getName();

        String sourceDocumentDmsid = createManager.createDocument(ClassNames.DOCUMENT, true, sourceSubfolder, "aDocument");
        OwFNCM5Object<?> sourceDocument = network.getObjectFromDMSID(sourceDocumentDmsid, true);
        String sourceDocumentName = sourceDocument.getName();

        OwObjectCollection sourceFolderContent = sourceFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_FOLDER }, null, null, 1000, 0, null);
        Assert.assertEquals(1, sourceFolderContent.size());

        OwObjectCollection sourceSubfolderContent = sourceSubfolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertEquals(1, sourceSubfolderContent.size());

        destinationFolder.move(sourceSubfolder, sourceFolder);

        sourceFolderContent = sourceFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_FOLDER }, null, null, 1000, 0, null);
        Assert.assertTrue(sourceFolderContent.isEmpty());

        OwObjectCollection destinationFolderContent = destinationFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_FOLDER }, null, null, 1000, 0, null);
        Assert.assertEquals(1, destinationFolderContent.size());

        OwFNCM5Object<?> destinationSubFolder = (OwFNCM5Object<?>) destinationFolderContent.get(0);
        OwObjectCollection destinationSubfolderContent = destinationSubFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertEquals(1, destinationSubfolderContent.size());

        Assert.assertEquals(sourceSubfolderName, destinationSubFolder.getName());

        OwFNCM5Object<?> destinationDocument = (OwFNCM5Object<?>) destinationSubfolderContent.get(0);
        Assert.assertEquals(sourceDocumentName, destinationDocument.getName());
    }

    @Test
    public void testAddDocument() throws Exception
    {
        String sourceDocumentDmsid = createManager.createDocument(ClassNames.DOCUMENT, true, SOURCE_FOLDER_NAME, "aDocument");
        OwFNCM5Object<?> sourceDocument = network.getObjectFromDMSID(sourceDocumentDmsid, true);
        OwObjectCollection sourceContent = sourceFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertEquals(1, sourceContent.size());

        OwObjectCollection destinationContent = destinationFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertTrue(destinationContent.isEmpty());
        destinationFolder.add(sourceDocument);

        destinationContent = destinationFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertEquals(1, destinationContent.size());

        OwFNCM5Object<?> destinationDocument = (OwFNCM5Object<?>) destinationContent.get(0);
        Assert.assertEquals(sourceDocument.getName(), destinationDocument.getName());

        sourceContent = sourceFolder.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, null, null, 1000, 0, null);
        Assert.assertEquals(1, sourceContent.size());

        System.err.println(destinationDocument.getNativeObject());
    }

    @Test
    public void testAddFolder() throws Exception
    {
        try
        {
            destinationFolder.add(sourceFolder);
            Assert.fail("You should not be allowed to add a folder to another folder !");
        }
        catch (OwInvalidOperationException e)
        {
            //OK
        }
    }

    @Test
    public void testGetProperty() throws Exception
    {
        assertNotNull(f);
        assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, f.getType());

        f.getProperty(PropertyNames.ID);

        try
        {
            f.getProperty("DocumentTitle");
            fail("Folder did not throw a exception requesting DocumentTitle property.");
        }
        catch (OwObjectNotFoundException e)
        {
            assertTrue("Exception was thrown", e != null);
        }
    }

    @Test
    public void testDelete() throws Exception
    {
        String folderDMSID = createManager.createFolder(sourceFolder, "aFolder");
        OwFNCM5Object<?> folder = network.getObjectFromDMSID(folderDMSID, true);

        String documentDMSID = createManager.createDocument(ClassNames.DOCUMENT, true, folder, "aDocument");

        folder.delete();

        try
        {
            network.getObjectFromDMSID(folderDMSID, true);
            fail("Could not delete object!");
        }
        catch (OwObjectNotFoundException e)
        {
            LOG.debug("Sucessfully deleted object.", e);
        }

        OwFNCM5Object<?> unfiledDocument = network.getObjectFromDMSID(documentDMSID, true);
        unfiledDocument.delete();

    }

    protected Map<String, OwObject> assertChildrenNames(OwObject folder_p, int[] iObjectTypes_p, String[] names_p) throws Exception
    {
        OwObjectCollection children = folder_p.getChilds(iObjectTypes_p, null, null, names_p.length + 1, 0, null);
        Map<String, OwObject> namedChildren = new HashMap<String, OwObject>();
        List<String> names = new LinkedList<String>();
        for (Iterator i = children.iterator(); i.hasNext();)
        {
            OwObject object = (OwObject) i.next();
            String objectName = object.getName();
            names.add(objectName);
            namedChildren.put(objectName, object);
        }
        assertEquals(names_p.length, names.size());
        for (int i = 0; i < names_p.length; i++)
        {
            assertTrue(names.contains(names_p[i]));
        }

        return namedChildren;
    }

    @Test
    public void testRemoveReference() throws Exception
    {
        final String aDocument = "aDocument";
        final String aReferenceToADocument = "aReferenceToADocument";
        final String aSecondReferenceToADocument = "aSecondReferenceToADocument";

        String documentDMSID = createManager.createDocument(ClassNames.DOCUMENT, true, sourceFolder, aDocument);
        OwFNCM5Object<?> document = network.getObjectFromDMSID(documentDMSID, true);

        OwFNCM5ObjectStoreResource osResource = (OwFNCM5ObjectStoreResource) document.getResource();
        OwFNCM5ObjectStore objectStore = osResource.getObjectStore();
        ObjectStore nativeObjectStore = objectStore.getNativeObject();

        Folder nativeSourceFolder = (Folder) sourceFolder.getNativeObject();
        Document aNativeDocument = (Document) document.getNativeObject();

        ReferentialContainmentRelationship rcr1 = Factory.ReferentialContainmentRelationship.createInstance(nativeObjectStore, null);

        rcr1.set_ContainmentName(aReferenceToADocument);

        rcr1.set_Head(aNativeDocument);
        rcr1.set_Tail(nativeSourceFolder);
        rcr1.save(RefreshMode.NO_REFRESH);

        ReferentialContainmentRelationship rcr2 = Factory.ReferentialContainmentRelationship.createInstance(nativeObjectStore, null);

        rcr2.set_ContainmentName(aSecondReferenceToADocument);

        rcr2.set_Head(aNativeDocument);
        rcr2.set_Tail(nativeSourceFolder);
        rcr2.save(RefreshMode.NO_REFRESH);

        final int[] iObjectTypes = new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT };

        Map<String, OwObject> childs = assertChildrenNames(sourceFolder, iObjectTypes, new String[] { aDocument, aReferenceToADocument, aSecondReferenceToADocument });
        //EvaluateReferentialContainmentRelationship = true 
        OwObject referentialDocument = childs.get(aDocument);

        try
        {
            sourceFolder.removeReference(referentialDocument);

            Map<String, OwObject> children = assertChildrenNames(sourceFolder, iObjectTypes, new String[] { aReferenceToADocument, aSecondReferenceToADocument });

            OwObject containee = children.get(aReferenceToADocument);
            OwObject secondContainee = children.get(aSecondReferenceToADocument);

            sourceFolder.removeReference(secondContainee);
            assertChildrenNames(sourceFolder, iObjectTypes, new String[] { aReferenceToADocument });

            sourceFolder.removeReference(containee);
            assertChildrenNames(sourceFolder, iObjectTypes, new String[] {});

        }
        finally
        {
            document.delete();
        }
    }
}
