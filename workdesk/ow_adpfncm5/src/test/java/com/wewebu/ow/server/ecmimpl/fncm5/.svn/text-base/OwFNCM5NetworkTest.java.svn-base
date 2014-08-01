package com.wewebu.ow.server.ecmimpl.fncm5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Document;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectReference;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.wewebu.ow.server.app.OwUserOperationEvent;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NetworkCreateManager;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectStore;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.unittest.log.JUnitLogger;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * OwFNCM5NetworkTest.
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
public class OwFNCM5NetworkTest
{
    private static final Logger LOG = JUnitLogger.getLogger(OwFNCM5NetworkTest.class);
    OwFNCM5NetworkManager manager;
    private OwFNCM5Network network;

    @Before
    public void setUp() throws Exception
    {
        if (manager == null)
        {
            manager = new OwFNCM5NetworkManager();
        }
        this.network = this.manager.createLoggedInNetwork();
        this.network.operationPerformed(new OwUserOperationEvent(null, OwUserOperationEvent.OwUserOperationType.START, null));
    }

    @After
    public void tearDown() throws Exception
    {
        if (network != null)
        {
            network.logout();
            network.operationPerformed(new OwUserOperationEvent(null, OwUserOperationEvent.OwUserOperationType.STOP, null));
            network = null;
        }
    }

    @Test
    public void testLoginDefault() throws OwException
    {
        OwFNCM5Network network = manager.createdInitializedNetwork();
        network.loginDefault("Administrator", "wewebu2011");

        network.logout();
        network = null;
    }

    @Test
    public void testGetObjectByPath() throws OwException
    {
        StringBuilder resourceIdPath = new StringBuilder("/");
        StringBuilder resourceNamePath = new StringBuilder("/");
        try
        {
            resourceIdPath.append(network.getResource(null).getID());
            resourceNamePath.append(network.getResource(null).getSymbolicName());
        }
        catch (Exception e)
        {
            String msg = "Could not get default resource: " + e.getMessage();
            LOG.error(msg, e);
            fail(msg);
        }

        {
            OwObject resourceRoot = network.getObjectFromPath(resourceIdPath.toString(), true);
            assertNotNull(resourceRoot);
            assertTrue(resourceRoot.getType() == OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER);
        }

        {
            OwObject resourceRoot = network.getObjectFromPath(resourceNamePath.toString(), true);
            assertNotNull(resourceRoot);
            assertTrue(resourceRoot.getType() == OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER);
        }

        final String subpathToFolder = "/Test/getByPath";
        resourceIdPath.append(subpathToFolder);
        resourceNamePath.append(subpathToFolder);

        {
            OwObject rIdPathObj = network.getObjectFromPath(resourceIdPath.toString(), true);
            assertNotNull(rIdPathObj);
            assertTrue(rIdPathObj.getType() == OwObjectReference.OBJECT_TYPE_FOLDER);
            assertEquals("getByPath", rIdPathObj.getName());
        }

        {
            OwObject rNameObj = network.getObjectFromPath(resourceIdPath.toString(), true);
            assertNotNull(rNameObj);
            assertTrue(rNameObj.getType() == OwObjectReference.OBJECT_TYPE_FOLDER);
            assertEquals("getByPath", rNameObj.getName());
        }
    }

    @Test
    public void testGetObjectByDMSID() throws OwException
    {

        OwFNCM5ObjectStoreResource resource = network.getResource(null);
        OwFNCM5ObjectStore objectStore = resource.getObjectStore();
        ObjectStore nativeObjectStore = objectStore.getNativeObject();
        IndependentObject getByIdObject = nativeObjectStore.fetchObject(ClassNames.DOCUMENT, "/Test/getByPath/getById", null);
        ObjectReference getByIdObjectReferece = getByIdObject.getObjectReference();
        String testId = getByIdObjectReferece.getObjectIdentity();

        StringBuilder dmsid = new StringBuilder(OwFNCM5Network.DMS_PREFIX);
        dmsid.append(",");
        try
        {
            dmsid.append(resource.getID());
        }
        catch (Exception e)
        {
            String msg = "Could not get default resource: " + e.getMessage();
            LOG.error(msg, e);
            fail(msg);
        }
        dmsid.append(",");
        dmsid.append(testId);

        OwObject obj = network.getObjectFromDMSID(dmsid.toString(), true);

        assertNotNull(obj);
        assertTrue(obj.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT);
        assertEquals("getById", obj.getName());
    }

    @Test
    public void testVirtualProperties() throws OwException
    {
        try
        {
            OwFieldDefinition def = network.getFieldDefinition(OwResource.m_ObjectNamePropertyClass.getClassName(), null);
            assertNotNull(def);
            assertEquals(OwResource.m_ObjectNamePropertyClass.getJavaClassName(), def.getJavaClassName());
        }
        catch (Exception e)
        {
            String msg = "Could not get " + OwResource.m_ObjectNamePropertyClass.getClassName() + ": " + e.getMessage();
            LOG.error(msg, e);
            fail(msg);
        }

        try
        {
            OwFieldDefinition def = network.getFieldDefinition(OwResource.m_ObjectPathPropertyClass.getClassName(), null);
            assertNotNull(def);
            assertEquals(OwResource.m_ObjectPathPropertyClass.getJavaClassName(), def.getJavaClassName());
        }
        catch (Exception e)
        {
            String msg = "Could not get " + OwResource.m_ObjectPathPropertyClass.getClassName() + ": " + e.getMessage();
            LOG.error(msg, e);
            fail(msg);
        }

        try
        {
            OwFieldDefinition def = network.getFieldDefinition(OwResource.m_ClassDescriptionPropertyClass.getClassName(), null);
            assertNotNull(def);
            assertEquals(OwResource.m_ClassDescriptionPropertyClass.getJavaClassName(), def.getJavaClassName());
        }
        catch (Exception e)
        {
            String msg = "Could not get " + OwResource.m_ClassDescriptionPropertyClass.getClassName() + ": " + e.getMessage();
            LOG.error(msg, e);
            fail(msg);
        }
    }

    @Test
    public void testFieldDefinitions() throws OwException
    {
        String[] arr = new String[] { PropertyNames.DATE_CREATED, PropertyNames.DATE_LAST_MODIFIED };

        /*Functionality is needed by SearchTemplates and OwObjectList*/
        for (String propName : arr)
        {
            OwFieldDefinition def = network.getFieldDefinition(propName, null);
            assertNotNull(def);
            assertEquals("java.util.Date", def.getJavaClassName());
        }
    }

    @Test
    public void testGetUserFromID() throws Exception
    {
        OwUserInfo testUser = network.getUserFromID("cn=JUnitTester,o=sample");
        assertNotNull(testUser);
        assertEquals("cn=JUnitTester,o=sample", testUser.getUserName());
    }

    @Test
    public void testCreateObjectSkeletonWithCreationInitialValues() throws Exception
    {
        //Email has some property defaults set in owbootstrap.xml
        /*
         <CreationInitialValues optionid="">
            <ObjectClass name="Email">
                <Property name="EmailSubject">hello, world!</Property>
                <Property name="SentOn">{today}</Property>
            </ObjectClass>
        </CreationInitialValues>
         */
        OwFNCM5ObjectStoreResource defaultResource = this.network.getResource(null);
        OwObjectClass emailClass = network.getObjectClass("Email", defaultResource);
        OwObjectSkeleton emailSkeleton = this.network.createObjectSkeleton(emailClass, defaultResource);
        OwProperty emailSubject = emailSkeleton.getProperty("EmailSubject");
        OwProperty sentOn = emailSkeleton.getProperty("SentOn");

        Assert.assertEquals("hello, world!", emailSubject.getValue());
        Calendar today = Calendar.getInstance();
        Calendar sentOnvalue = Calendar.getInstance();
        sentOnvalue.setTime((Date) sentOn.getValue());

        Assert.assertEquals(today.get(Calendar.YEAR), sentOnvalue.get(Calendar.YEAR));
        Assert.assertEquals(today.get(Calendar.MONTH), sentOnvalue.get(Calendar.MONTH));
        Assert.assertEquals(today.get(Calendar.DAY_OF_MONTH), sentOnvalue.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(today.get(Calendar.HOUR), sentOnvalue.get(Calendar.HOUR));
        Assert.assertEquals(today.get(Calendar.MINUTE), sentOnvalue.get(Calendar.MINUTE));
    }

    @Test
    public void testGetObjectClassNames() throws Exception
    {
        Map<String, String> names = this.network.getObjectClassNames(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, false, false, null);
        assertTrue(names.containsKey("Document"));
        assertTrue(names.containsKey("Email"));

        names = this.network.getObjectClassNames(new int[] { OwObjectReference.OBJECT_TYPE_CUSTOM }, false, false, null);
        assertTrue(names.containsKey("CustomObject"));

        names = this.network.getObjectClassNames(new int[] { OwObjectReference.OBJECT_TYPE_FOLDER }, false, false, null);
        assertTrue(names.containsKey("Folder"));
    }

    @Test
    public void testGetObjectClass() throws Exception
    {
        OwFNCM5Class clazz = network.getObjectClass("Document", null);
        assertNotNull("Document object class not available", clazz);
        assertTrue(clazz.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT);

        clazz = network.getObjectClass("Folder", null);
        assertNotNull("Folder object class not available", clazz);
        assertTrue(clazz.getType() == OwObjectReference.OBJECT_TYPE_FOLDER);

        clazz = network.getObjectClass("CustomObject", network.getResource(null));
        assertNotNull("CustomObject object class not available", clazz);
        assertTrue(clazz.getType() == OwObjectReference.OBJECT_TYPE_CUSTOM);
    }

    @Test
    public void testContainmentName() throws Exception
    {
        OwFNCM5Network network = manager.createLoggedInNetwork();

        OwFNCM5NetworkCreateManager createMngt = new OwFNCM5NetworkCreateManager(network);

        String unitFolder = "NetworkTest_" + System.currentTimeMillis();
        String fID = createMngt.createFolder(unitFolder);
        OwObject folder = network.getObjectFromDMSID(fID, true);
        OwObjectClass objCls = network.getObjectClass("Document", null);
        OwObjectSkeleton skeleton = network.createObjectSkeleton(objCls, null);

        OwPropertyCollection propCol = skeleton.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);
        assertNotNull(propCol);
        OwProperty propName = (OwProperty) propCol.get("DocumentTitle");
        if (propName == null)
        {
            OwPropertyClass propCls = objCls.getPropertyClass("DocumentTitle");
            propName = skeleton.createNewProperty("BetterWorld", propCls);
            propCol.put(propCls.getClassName(), propName);
        }
        else
        {
            propName.setValue("BetterWorld");
        }

        String newObjId = network.createNewObject(null, objCls.getClassName(), propCol, null, null, folder, "", "name=NotSample.txt");
        OwObject newObj = network.getObjectFromDMSID(newObjId, true);

        assertEquals("BetterWorld", newObj.getName());
        Document doc = (Document) newObj.getNativeObject();
        ReferentialContainmentRelationshipSet rcrSet = doc.get_Containers();
        assertNotNull("RCR is null", rcrSet);
        assertTrue("object was not filed", rcrSet.isEmpty() == false);
        assertEquals("BetterWorld.txt", ((ReferentialContainmentRelationship) rcrSet.iterator().next()).get_ContainmentName());

        newObj.delete();
        folder.delete();

        network.logout();
        network = null;
    }

    @Test
    public void testCreateDocumentCheckedOutMajor() throws Exception
    {
        OwFNCM5Network network = manager.createLoggedInNetwork();
        OwObjectClass objCls = network.getObjectClass("Document", null);
        OwObjectSkeleton skeleton = network.createObjectSkeleton(objCls, null);

        OwPropertyCollection propCol = skeleton.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);
        assertNotNull(propCol);
        OwProperty propName = (OwProperty) propCol.get("DocumentTitle");
        if (propName == null)
        {
            OwPropertyClass propCls = objCls.getPropertyClass("DocumentTitle");
            propName = skeleton.createNewProperty("CreateDoc" + System.currentTimeMillis(), propCls);
            propCol.put(propCls.getClassName(), propName);
        }
        else
        {
            propName.setValue("CreateDoc" + System.currentTimeMillis());
        }

        String newObjId = network.createNewObject(true, null, null, "Document", propCol, null, null, null, "", null, true);
        assertNotNull(newObjId);
        OwObject newObj = network.getObjectFromDMSID(newObjId, true);
        assertNotNull(newObj);
        OwVersion vObj = newObj.getVersion();
        assertTrue(vObj != null);
        assertTrue(vObj.isMajor(0));
        assertTrue(vObj.isCheckedOut(0));
        newObj.delete();
    }

    /*Performance test for retrieval, SHOULD NOT BE EXECUTED EVERY DAY
        @Test
        public void testGetFolderObjects() throws Exception
        {
            OwFNCM5Network network = manager.createLoggedInNetwork();
            String rootPath = "/P8ConfigObjectStore/SmallDS/test";
            List<String> dmsids = new LinkedList<String>();
            long start = System.currentTimeMillis();
            for (int i = 10; i < 111; i++)
            {
                OwObject obj = network.getObjectFromPath(rootPath + i, false);
                dmsids.add(obj.getDMSID());
            }
            long result = System.currentTimeMillis() - start;
            System.out.println("Time for path retriveal = " + result + ", average = " + result / 100);
            start = System.currentTimeMillis();
            for (String dmsid : dmsids)
            {
                OwObject obj = network.getObjectFromDMSID(dmsid, false);
                assertEquals(dmsid, obj.getDMSID());
            }
            result = System.currentTimeMillis() - start;
            System.out.println("Time for path retriveal = " + result + ", average = " + result / 100);
        }

        @Test
        public void testGetDocumentObjects() throws Exception
        {
            OwFNCM5Network network = manager.createLoggedInNetwork();
            String rootPath = "/P8ConfigObjectStore/Performance/300Docs/foo";
            List<String> dmsids = new LinkedList<String>();
            long start = System.currentTimeMillis();
            for (int i = 110; i < 211; i++)
            {
                OwObject obj = network.getObjectFromPath(rootPath + i, false);
                dmsids.add(obj.getDMSID());
            }
            long result = System.currentTimeMillis() - start;
            System.out.println("Time for path retriveal = " + result + ", average = " + result / 100);
            start = System.currentTimeMillis();
            for (String dmsid : dmsids)
            {
                OwObject obj = network.getObjectFromDMSID(dmsid, false);
                assertEquals(dmsid, obj.getDMSID());
            }
            result = System.currentTimeMillis() - start;
            System.out.println("Time for DMSID retriveal = " + result + ", average = " + result / 100);
        }*/
}
