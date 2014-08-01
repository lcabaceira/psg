package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Tests for retrieval of object from DMSID and Path.
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
public class OwCMISRetrievalTest extends OwCMISIntegrationTest
{

    public OwCMISRetrievalTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected void postSetUp() throws Exception
    {
        super.postSetUp();
        loginAdmin();
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (this.getNetwork() != null)
        {
            getNetwork().logout();
        }
        super.tearDown();
    }

    public void testGetObjectFromDMSID() throws Exception
    {
        OwObject pathObj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST, true);
        assertNotNull(pathObj);
        assertNotNull(pathObj.getDMSID());

        OwObject dmsidObj = getNetwork().getObjectFromDMSID(pathObj.getDMSID(), true);
        assertNotNull(dmsidObj);
        assertEquals(pathObj.getDMSID(), dmsidObj.getDMSID());
        assertEquals(pathObj.getName(), dmsidObj.getName());
        assertEquals(pathObj.getType(), dmsidObj.getType());
    }

    public void testGetObjectFromPath() throws OwException
    {
        OwObject pathObj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST, true);
        assertNotNull(pathObj);
        assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, pathObj.getType());

        pathObj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST.substring(1), true);
        assertNotNull(pathObj);
        assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, pathObj.getType());

        pathObj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + "Documents.xml", true);
        assertNotNull(pathObj);
        assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, pathObj.getType());
    }

    public void testGetFromPathSpecial() throws OwException
    {
        OwObject obj = getNetwork().getObjectFromPath(OwObject.STANDARD_PATH_DELIMITER, true);
        assertNotNull(obj);
        assertTrue(obj instanceof com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDomainFolder);

        obj = getNetwork().getObjectFromPath(OwObject.STANDARD_PATH_DELIMITER + OwCMISIntegrationFixture.MAIN_REPOSITORY + OwObject.STANDARD_PATH_DELIMITER, true);
        assertNotNull(obj);

        obj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST.substring(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST.indexOf(OwObject.STANDARD_PATH_DELIMITER, 1)), true);

        try
        {
            obj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY, true);
            fail("Incorrect path should throw ONF");
        }
        catch (OwObjectNotFoundException e)
        {

        }
    }

    public void testGetParents() throws Exception
    {
        OwObject pathObj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST, true);
        assertNotNull(pathObj);
        assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, pathObj.getType());

        OwObject childObj = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST + "Documents.xml", true);
        assertNotNull(childObj);
        assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, childObj.getType());

        OwObjectCollection parents = childObj.getParents();
        assertNotNull(parents);
        assertFalse(parents.isEmpty());

        assertEquals(pathObj.getDMSID(), ((OwObject) parents.get(0)).getDMSID());
    }

}
