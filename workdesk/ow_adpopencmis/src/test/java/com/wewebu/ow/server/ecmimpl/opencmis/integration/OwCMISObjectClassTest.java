package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.Map;

import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Basic OwObjectClass test cases.
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
public class OwCMISObjectClassTest extends OwCMISIntegrationTest
{

    public OwCMISObjectClassTest(String name_p)
    {
        super("OwObjectClass_Test", name_p);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        loginAdmin();
    }

    public void testClassMimeTypes() throws Exception
    {
        OwCMISObjectClass cls = getNetwork().getObjectClass(BaseTypeId.CMIS_FOLDER.value(), null);
        assertEquals("ow_folder/cmis:folder", cls.getMimetype());

        cls = getNetwork().getObjectClass(BaseTypeId.CMIS_POLICY.value(), null);
        assertEquals(OwCMISObjectClass.MIME_TYPE_PREFIX_OW_POLICY + BaseTypeId.CMIS_POLICY.value(), cls.getMimetype());

        cls = getNetwork().getObjectClass(BaseTypeId.CMIS_RELATIONSHIP.value(), null);
        assertEquals(OwCMISObjectClass.MIME_TYPE_PREFIX_OW_RELATIONSHIP + BaseTypeId.CMIS_RELATIONSHIP.value(), cls.getMimetype());
    }

    public void testCMISComplaintRootClasses() throws OwException
    {
        for (BaseTypeId id : BaseTypeId.values())
        {
            OwCMISObjectClass cls = getNetwork().getObjectClass(id.value(), null);
            assertEquals(id.value(), cls.getQueryName());
        }
    }

    public void testPropertyClasses() throws OwException
    {
        OwCMISObjectClass documentClass = getNetwork().getObjectClass(BaseTypeId.CMIS_DOCUMENT.value(), null);

        {
            OwCMISPropertyClass<?> shortName = documentClass.getPropertyClass("cmis:name");
            assertEquals(new OwCMISQualifiedName("cmis:document.cmis:name"), shortName.getFullQualifiedName());
            assertEquals("cmis:name", shortName.getNonQualifiedName());
            assertEquals("cmis:name", shortName.getClassName());
        }

        {
            OwCMISPropertyClass<?> shortName = documentClass.getPropertyClass("cmis:objectTypeId");
            assertEquals(new OwCMISQualifiedName("cmis:document.cmis:objectTypeId"), shortName.getFullQualifiedName());
            assertEquals("cmis:objectTypeId", shortName.getNonQualifiedName());
            assertEquals("cmis:objectTypeId", shortName.getClassName());
        }

        {
            OwCMISPropertyClass<?> fullName = documentClass.getPropertyClass("cmis:document.cmis:name");
            assertEquals(new OwCMISQualifiedName("cmis:document.cmis:name"), fullName.getFullQualifiedName());
            assertEquals("cmis:name", fullName.getNonQualifiedName());
            assertEquals("cmis:document.cmis:name", fullName.getClassName());
        }

        {
            OwCMISPropertyClass<?> descriptionShortName = documentClass.getPropertyClass("OW_ClassDescription");
            assertEquals(new OwCMISQualifiedName("cmis:document.OW_ClassDescription"), descriptionShortName.getFullQualifiedName());
            assertEquals("OW_ClassDescription", descriptionShortName.getNonQualifiedName());
            assertEquals("OW_ClassDescription", descriptionShortName.getClassName());
        }

    }

    public void testObjectClassRetrieval() throws OwException
    {
        OwCMISObjectClass cls = getNetwork().getObjectClass(BaseTypeId.CMIS_DOCUMENT.value(), null);
        assertNotNull(cls);
        try
        {
            cls = getNetwork().getObjectClass("Non-Exsisting-ObjectClass", null);
            fail("Method must throw OwObjectNotFoundException");
        }
        catch (OwObjectNotFoundException e)
        {
            assertNotNull(e);//just simple test that exception is thrown
        }
    }

    @SuppressWarnings("unchecked")
    public void testGetObjectClassNames() throws OwException
    {
        Map<String, String> retMap = getNetwork().getObjectClassNames(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, false, true, null);
        assertNotNull(retMap);
        assertEquals(1, retMap.size());
        retMap = getNetwork().getObjectClassNames(new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT }, false, false, null);
        assertNotNull(retMap);
        assertTrue(retMap.size() > 1);
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (this.getNetwork() != null)
        {
            this.getNetwork().logout();
        }
        super.tearDown();
    }
}
