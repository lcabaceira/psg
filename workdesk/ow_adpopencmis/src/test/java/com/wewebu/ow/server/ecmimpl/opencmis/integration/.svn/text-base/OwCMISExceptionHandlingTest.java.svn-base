package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.HashSet;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.exceptions.OwDuplicateException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

public class OwCMISExceptionHandlingTest extends OwCMISIntegrationTest
{
    private Set<String> createdObjectsDMSIDs;

    public OwCMISExceptionHandlingTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected void postSetUp() throws Exception
    {
        super.postSetUp();
        loginAdmin();
        this.createdObjectsDMSIDs = new HashSet<String>();
    }

    @Override
    protected void tearDown() throws Exception
    {
        if (this.getNetwork() != null)
        {
            //Cleanup
            for (String documentId : this.createdObjectsDMSIDs)
            {
                try
                {
                    OwObject document = getNetwork().getObjectFromDMSID(documentId, false);
                    document.delete();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            getNetwork().logout();
        }

        super.tearDown();
    }

    public void testGetObjectNotFoundPath() throws Exception
    {
        try
        {
            this.getNetwork().getObjectFromPath("/foo/bar/baz", true);
            fail("OwObjectNotFoundException should have been thrown!");
        }
        catch (OwObjectNotFoundException onfe)
        {
            //OK
            assertEquals(onfe.getLocalizeablemessage().getKey(), "com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork.err.getObjectFromPath.objectNotFound");
        }
    }

    public void testGetObjectWrongArgumentId() throws Exception
    {
        try
        {
            this.getNetwork().getObjectFromDMSID("1-2-3!@#$", true);
            fail("OwInvalidOperationException should have been thrown!");
        }
        catch (OwInvalidOperationException ioe)
        {
            //OK
        }
    }

    public void testGetObjectWrongArgumentPath() throws Exception
    {
        try
        {
            this.getNetwork().getObjectFromPath("foo/bar/baz", true);
            fail("OwInvalidOperationException should have been thrown!");
        }
        catch (OwInvalidOperationException ioe)
        {
            //OK
        }
    }

    public void testGetPropertyNotFound() throws Exception
    {
        String newId = createTestDocument();

        OwObject aDocument = getNetwork().getObjectFromDMSID(newId, true);
        assertNotNull(aDocument);

        try
        {
            aDocument.getProperty("foo.beria");
            fail("OwObjectNotFoundException should have been thrown!");
        }
        catch (OwObjectNotFoundException onfe)
        {
            //OK
        }
    }

    public void testNewDocumentConstraintViolation() throws Exception
    {
        try
        {
            String documentName = "&&& -+!@#$~`\"Varza.viezure" + System.currentTimeMillis();
            createTestDocument(documentName);
            fail("OwInvalidOperationException should have been thrown!");
        }
        catch (OwInvalidOperationException owe)
        {
            //OK  
            assertEquals(owe.getLocalizeablemessage().getKey(), "com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork.err.createNewObject.constraint");
        }
    }

    public void testNewDocumentDuplicate() throws Exception
    {
        String documentName = "Varza.viezure" + System.currentTimeMillis();
        String newId = createTestDocument(documentName);

        OwObject aDocument = getNetwork().getObjectFromDMSID(newId, true);
        assertNotNull(aDocument);

        try
        {
            createTestDocument(documentName);
            fail("OwObjectNotFoundException should have been thrown!");
        }
        catch (OwDuplicateException ode)
        {
            //OK
            assertEquals(ode.getLocalizeablemessage().getKey(), "com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork.err.createNewObject.contentAlreadyExists");
        }
    }

    private String createTestDocument() throws OwException, Exception
    {
        String documentName = "testNocontent_Major_" + System.currentTimeMillis();
        return createTestDocument(documentName);
    }

    @SuppressWarnings("unchecked")
    private String createTestDocument(String documentName) throws OwException, Exception
    {
        OwObject testFolder = getNetwork().getObjectFromPath(OwCMISIntegrationFixture.MAIN_REPOSITORY_J_UNIT_TEST, true);
        assertNotNull(testFolder);

        OwResource resource = getNetwork().getResource(OwCMISIntegrationFixture.MAIN_REPOSITORY);
        OwObjectClass objectclass = getNetwork().getObjectClass("cmis:document", resource);

        //Checked out
        OwObjectSkeleton skeleton = getNetwork().createObjectSkeleton(objectclass, resource);
        OwPropertyCollection docStandardPropertiesMap = skeleton.getEditableProperties(OwPropertyClass.CONTEXT_ON_CREATE);

        String namePropertyName = objectclass.getNamePropertyName();
        OwPropertyClass namePropertyClass = objectclass.getPropertyClass(namePropertyName);

        OwProperty nameProperty = new OwStandardProperty(documentName, namePropertyClass);
        docStandardPropertiesMap.put(nameProperty.getPropertyClass().getClassName(), nameProperty);

        OwContentCollection noContent = null;
        String newId = getNetwork().createNewObject(true, null, resource, objectclass.getClassName(), docStandardPropertiesMap, null, noContent, null, null, null, false);
        this.createdObjectsDMSIDs.add(newId);
        return newId;
    }
}
