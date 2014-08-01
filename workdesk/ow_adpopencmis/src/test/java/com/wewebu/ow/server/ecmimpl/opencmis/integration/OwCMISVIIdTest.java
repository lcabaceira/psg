package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.app.id.viid.OwVIIdFactory;
import com.wewebu.ow.server.app.impl.viid.OwSimpleVIIdFactory;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;

/**
 *<p>
 * OwVIId handling test. 
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
public class OwCMISVIIdTest extends OwCMISIntegrationTest
{

    private OwVIIdFactory factory;

    public OwCMISVIIdTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        if (factory == null)
        {
            factory = new OwSimpleVIIdFactory();
        }
    }

    public void testGetObjectFolder() throws Exception
    {
        loginAdmin();
        OwObject obj = getNetwork().getObjectFromPath("/Main Repository/JUnitTest", true);
        assertNotNull(obj);
        assertTrue(obj.getType() == OwObjectReference.OBJECT_TYPE_FOLDER);
        OwVIId viid = factory.createVersionIndependentId(obj);
        assertNotNull(viid);
        assertEquals(obj.getID(), viid.getObjectId());
        OwObject viObj = getNetwork().getObject(viid);
        assertEquals(obj.getDMSID(), viObj.getDMSID());
    }

    public void testGetObjectDocument() throws Exception
    {
        loginAdmin();
        OwObject obj = getNetwork().getObjectFromPath("/Main Repository/JUnitTest/[Document without content]", true);
        assertNotNull(obj);
        assertTrue(obj.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT);
        OwVIId viid = factory.createVersionIndependentId(obj);
        assertNotNull(viid);
        assertEquals(obj.getVersionSeries().getId(), viid.getObjectId());
        OwObject viObj = getNetwork().getObject(viid);
        assertEquals(obj.getDMSID(), viObj.getDMSID());
    }

}
