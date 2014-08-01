package com.wewebu.ow.server.ecmimpl.fncm5.object;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5IntegrationTest;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * OwFNCM5CustomObjectTest.
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
public class OwFNCM5CustomObjectTest extends OwFNCM5IntegrationTest
{

    private static final Logger LOG = Logger.getLogger(OwFNCM5CustomObjectTest.class);

    private static final String CUSTOM_TESTS = "CustomTests";

    private OwFNCM5Object<?> testCustomObject;

    private OwFNCM5Object<?> customTestsFolder;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        String rPath = "/" + network.getResource(null).getID() + "/Test/getByPath/TestCustomObject";
        testCustomObject = (OwFNCM5Object<?>) network.getObjectFromPath(rPath, true);

        if (createManager.isFolderAvailable(CUSTOM_TESTS))
        {
            createManager.deleteFolderByPath(CUSTOM_TESTS);
        }
        String customTestsFolderDMSID = createManager.createFolder(CUSTOM_TESTS);
        customTestsFolder = network.getObjectFromDMSID(customTestsFolderDMSID, true);
    }

    @Override
    public void tearDown() throws Exception
    {
        if (createManager.isFolderAvailable(CUSTOM_TESTS))
        {
            createManager.deleteFolderByPath(CUSTOM_TESTS);
        }

        testCustomObject = null;
        super.tearDown();
    }

    @Test
    public void testVirtualProperty() throws Exception
    {
        //        String rPath = "/Test/getByPath/TestCustomObject";
        assertNotNull(testCustomObject);
        assertEquals(OwObjectReference.OBJECT_TYPE_CUSTOM, testCustomObject.getType());
        //OW_ObjectPath
        OwProperty path = testCustomObject.getProperty(OwResource.m_ObjectPathPropertyClass.getClassName());
        assertNotNull(path);
        assertNotNull(path.getPropertyClass());

        assertEquals(path.getPropertyClass().getJavaClassName(), OwResource.m_ObjectPathPropertyClass.getJavaClassName());
        /* We cannot retrieve the same path as defined, TestCustomObject is a RCR-name
         * not the name of the custom object self, therefore we attach the Id instead of 
         * the name from RCR.*/
        /* path which is returned cannot be assumed to be specific order
         * /Test/getByPath and /Test/ are both parents */
        //        String modPath = rPath.substring(0, rPath.lastIndexOf('/') + 1) + testCustomObject.getID();
        //        assertEquals(modPath, path.getValue().toString());

        //OW_ObjectName
        OwProperty name = testCustomObject.getProperty(OwResource.m_ObjectNamePropertyClass.getClassName());
        assertNotNull(name);
        assertNotNull(name.getPropertyClass());

        assertEquals(name.getPropertyClass().getJavaClassName(), OwResource.m_ObjectNamePropertyClass.getJavaClassName());
        assertEquals(testCustomObject.getName(), name.getValue().toString());
    }

    @Test
    public void testGetParents() throws Exception
    {
        assertNotNull(testCustomObject);

        OwObjectCollection col = testCustomObject.getParents();
        assertNotNull(col);
        assertEquals(2, col.size());
        /* path which is returned cannot be assumed to be specific order
         * /Test/getByPath and /Test/ are both parents */
        //        assertEquals("getByPath", ((OwFNCM5Object<?>) col.get(0)).getName());
    }

    @Test
    public void testDMSID() throws Exception
    {
        assertNotNull(testCustomObject);

        OwFNCM5Object<?> cIdObj = network.getObjectFromDMSID(testCustomObject.getDMSID(), true);
        assertEquals(testCustomObject.getDMSID(), cIdObj.getDMSID());

        assertEquals(testCustomObject.getID(), cIdObj.getID());
    }

    @Test
    public void testGetProperty() throws Exception
    {
        assertNotNull(testCustomObject);
        assertEquals(OwObjectReference.OBJECT_TYPE_CUSTOM, testCustomObject.getType());

        testCustomObject.getProperty(PropertyNames.ID);

        try
        {
            testCustomObject.getProperty("DocumentTitle");
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
        String anObjectDMSID = createManager.createCustomObject(ClassNames.CUSTOM_OBJECT, customTestsFolder, "anObject");
        OwFNCM5Object<?> anObject = network.getObjectFromDMSID(anObjectDMSID, true);
        anObject.delete();

        try
        {
            network.getObjectFromDMSID(anObjectDMSID, true);
            fail("Coould not delete object!");
        }
        catch (OwObjectNotFoundException e)
        {
            LOG.debug("Sucessfully deleted object.", e);
        }
    }

}
