package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardDecoratorObject;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.unittest.util.OwTestContext;

public class OwCMISAbstractNativeObjectTest extends OwCMISObjectTest
{
    private static final String CMIS_DOCUMENT_CMIS_NAME = "cmis:document.cmis:name";
    private static final String OWD_DOSSIER_PERSONNEL_NUMBER = "owd:dossierPersonnelNumber";
    private static final String OWD_KNOWLEDGE = "owd:knowledge";
    private static final String OWD_DOSSIER_PICTURE = "owd:dossierPicture";
    private static final String D_OWD_HRIMAGE = "D:owd:hrimage";
    private static final String F_OWD_DOSSIER = "F:owd:dossier";
    private static final String HR_IMAGE_NAME = "MyDossierImage";
    private static final String HR_DOSSIER_NAME = "MyDossier";
    private static final String COPY_FOLDER_NAME = "CopyFolder";

    private String dossierId;
    private String hrImageId;
    private String copyFolderId;

    public OwCMISAbstractNativeObjectTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;

        this.hrImageId = integrationFixture.createTestObject(D_OWD_HRIMAGE, HR_IMAGE_NAME, null, integrationFixture.getTestFolder());
        this.dossierId = integrationFixture.createTestObject(F_OWD_DOSSIER, HR_DOSSIER_NAME, null, integrationFixture.getTestFolder());

        this.copyFolderId = integrationFixture.createTestObject(ObjectType.FOLDER_BASETYPE_ID, COPY_FOLDER_NAME, null, integrationFixture.getTestFolder());
    }

    //    @Override
    //    protected String getConfigBootstrapName()
    //    {
    //        return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_ATOM_XML;
    //        //                return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_SOAP_XML;
    //    }

    @SuppressWarnings("unchecked")
    public void testSetProperties() throws Exception
    {
        OwCMISObject dossier = (OwCMISObject) getNetwork().getObjectFromDMSID(dossierId, true);
        OwProperty knowledgeProp = dossier.getProperty(OWD_KNOWLEDGE);
        assertNull(knowledgeProp.getValue());
        assertTrue(knowledgeProp.getPropertyClass().isArray());

        OwPropertyCollection updateProps = new OwStandardPropertyCollection();

        knowledgeProp.setValue(new String[] { "Administration" });
        updateProps.put(knowledgeProp.getPropertyClass(), knowledgeProp);

        dossier.setProperties(updateProps);

        dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);

        knowledgeProp = dossier.getProperty(OWD_KNOWLEDGE);
        assertNotNull(knowledgeProp.getValue());
        String[] knowledge = (String[]) knowledgeProp.getValue();
        List<String> knowledgeList = Arrays.asList(knowledge);
        assertEquals(1, knowledge.length);
        assertTrue(knowledgeList.contains("Administration"));
    }

    @SuppressWarnings("unchecked")
    public void testSetIDProperty() throws Exception
    {
        OwCMISObject dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);
        OwCMISObject hrImage = (OwCMISObject) this.getNetwork().getObjectFromDMSID(hrImageId, true);

        OwProperty dossierPictureProp = dossier.getProperty(OWD_DOSSIER_PICTURE);
        assertNull(dossierPictureProp.getValue());
        assertFalse(dossierPictureProp.getPropertyClass().isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        assertEquals(PropertyType.ID, ((PropertyDefinition<?>) dossierPictureProp.getPropertyClass().getNativeType()).getPropertyType());

        OwPropertyCollection updateProps = new OwStandardPropertyCollection();
        dossierPictureProp.setValue(hrImage);
        updateProps.put(dossierPictureProp.getPropertyClass(), dossierPictureProp);

        dossier.setProperties(updateProps);

        dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);

        dossierPictureProp = dossier.getProperty(OWD_DOSSIER_PICTURE);
        assertNotNull(dossierPictureProp.getValue());
        assertEquals(hrImage.getDMSID(), ((OwObject) dossierPictureProp.getValue()).getDMSID());
    }

    @SuppressWarnings("unchecked")
    public void testSetEnumProperty() throws Exception
    {
        OwCMISObject dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);
        OwProperty personnelNumberProp = dossier.getProperty(OWD_DOSSIER_PERSONNEL_NUMBER);
        assertNull(personnelNumberProp.getValue());
        assertTrue(personnelNumberProp.getPropertyClass().isEnum());

        OwPropertyCollection updateProps = new OwStandardPropertyCollection();

        ArrayList<OwEnum> possibleValues = new ArrayList<OwEnum>(personnelNumberProp.getPropertyClass().getEnums());
        OwEnum firstValue = possibleValues.get(0);
        personnelNumberProp.setValue(firstValue.getValue());
        updateProps.put(personnelNumberProp.getPropertyClass(), personnelNumberProp);

        dossier.setProperties(updateProps);

        dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);

        personnelNumberProp = dossier.getProperty(OWD_DOSSIER_PERSONNEL_NUMBER);
        assertNotNull(personnelNumberProp.getValue());
        assertEquals(firstValue.getValue(), personnelNumberProp.getValue());
    }

    @SuppressWarnings("unchecked")
    public void testSetWrongEnumProperty() throws Exception
    {
        OwCMISObject dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);
        OwProperty personnelNumberProp = dossier.getProperty(OWD_DOSSIER_PERSONNEL_NUMBER);
        assertNull(personnelNumberProp.getValue());
        assertTrue(personnelNumberProp.getPropertyClass().isEnum());

        OwPropertyCollection updateProps = new OwStandardPropertyCollection();

        personnelNumberProp.setValue("foobar");
        updateProps.put(personnelNumberProp.getPropertyClass(), personnelNumberProp);

        try
        {
            dossier.setProperties(updateProps);
            fail("An exception should have been thrown!");
        }
        catch (OwServerException se)
        {
            //OK (aspect waved)
        }
        catch (OwInvalidOperationException invalidEx)
        {
            //OK
        }
    }

    @SuppressWarnings("unchecked")
    public void testSetDateTimeProperties() throws Exception
    {
        this.getNetwork().logout();

        TimeZone clientTimeZone = TimeZone.getTimeZone("GMT-10");
        Calendar localCalendar = Calendar.getInstance(clientTimeZone);

        Date localBirthDate = new Date(2013 - 1900, 10, 10, 11, 20);

        localCalendar.set(Calendar.YEAR, 2013);
        localCalendar.set(Calendar.DAY_OF_MONTH, 10);
        localCalendar.set(Calendar.MONTH, 10);
        localCalendar.set(Calendar.HOUR_OF_DAY, 11);
        localCalendar.set(Calendar.MINUTE, 20);
        localCalendar.set(Calendar.SECOND, 00);
        localCalendar.set(Calendar.MILLISECOND, 00);

        ((OwTestContext) this.getNetwork().getContext()).setClientTimeZone(clientTimeZone);
        loginAdmin();

        OwCMISObject dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);

        OwProperty dateOfBirthProp = dossier.getProperty("owd:dateOfBirth");
        assertNull(dateOfBirthProp.getValue());

        OwPropertyCollection updateProps = new OwStandardPropertyCollection();

        dateOfBirthProp.setValue(localBirthDate);
        updateProps.put(dateOfBirthProp.getPropertyClass(), dateOfBirthProp);
        dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);
        dossier.setProperties(updateProps);

        dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);

        dateOfBirthProp = dossier.getProperty("owd:dateOfBirth");
        assertNotNull(dateOfBirthProp.getValue());
        assertEquals(localBirthDate, dateOfBirthProp.getValue());
        this.getNetwork().logout();

        TimeZone foreignTimeZone = TimeZone.getTimeZone("GMT+12");

        ((OwTestContext) this.getNetwork().getContext()).setClientTimeZone(foreignTimeZone);
        loginAdmin();

        dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);
        dateOfBirthProp = dossier.getProperty("owd:dateOfBirth");
        assertNotNull(dateOfBirthProp.getValue());

        Date dateOfBirth = (Date) dateOfBirthProp.getValue();

        Calendar phoneyCalendar = Calendar.getInstance();
        phoneyCalendar.setTime(dateOfBirth);
        assertEquals("10.10.2013 11:20:00 (GMT-10) should be 11.10.2013 09:20:00 (GMT+12)", 9, dateOfBirth.getHours());
        assertEquals("10.10.2013 11:20:00 (GMT-10) should be 11.10.2013 09:20:00 (GMT+12)", 11, phoneyCalendar.get(Calendar.DAY_OF_MONTH));

        assertEquals(localBirthDate.getYear(), dateOfBirth.getYear());
        assertEquals(localBirthDate.getMonth(), dateOfBirth.getMonth());
        assertEquals((localBirthDate.getHours() + 22) % 24, dateOfBirth.getHours()); // 22 hours difference between GMT-10 and GMT+12
        assertEquals(localBirthDate.getMinutes(), dateOfBirth.getMinutes());
        assertEquals(localBirthDate.getSeconds(), dateOfBirth.getSeconds());
    }

    public void testGetProperties() throws Exception
    {
        OwCMISObject hrImage = (OwCMISObject) this.getNetwork().getObjectFromDMSID(hrImageId, true);

        {
            OwPropertyCollection properties = hrImage.getProperties(Arrays.asList(CMIS_DOCUMENT_CMIS_NAME));

            assertEquals(1, properties.size());

            OwProperty nameProperty = (OwProperty) properties.get(CMIS_DOCUMENT_CMIS_NAME);
            assertEquals(HR_IMAGE_NAME, nameProperty.getValue());
        }

        {
            OwPropertyCollection properties = hrImage.getProperties(Arrays.asList(PropertyIds.NAME, OwResource.m_ObjectNamePropertyClass.getClassName()));

            OwProperty nameProperty = (OwProperty) properties.get(PropertyIds.NAME);
            OwProperty objectNameProperty = (OwProperty) properties.get(OwResource.m_ObjectNamePropertyClass.getClassName());
            assertEquals(HR_IMAGE_NAME, nameProperty.getValue());
            assertEquals(HR_IMAGE_NAME, objectNameProperty.getValue());
        }

    }

    public void testGetVirtualProperties() throws Exception
    {
        OwCMISObject dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);

        OwCMISProperty<?> owObjectPath = dossier.getProperty(OwResource.m_ObjectPathPropertyClass.getClassName());
        assertNotNull(owObjectPath);
        assertEquals(dossier.getPath(), owObjectPath.getValue());

        OwCMISProperty<?> owObjectName = dossier.getProperty(OwResource.m_ObjectNamePropertyClass.getClassName());
        assertNotNull(owObjectName);
        assertEquals(dossier.getName(), owObjectName.getValue());

        OwCMISProperty<?> owClassDescription = dossier.getProperty(OwResource.m_ClassDescriptionPropertyClass.getClassName());
        assertNotNull(owClassDescription);
        assertEquals("Dossier", owClassDescription.getValue());
    }

    public void testGetClass() throws Exception
    {
        OwCMISObject dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);
        OwCMISObjectClass objectClass = dossier.getObjectClass();
        assertEquals(F_OWD_DOSSIER, objectClass.getClassName());
    }

    public void testCopy() throws Exception
    {
        OwCMISObject copyFolder = (OwCMISObject) this.getNetwork().getObjectFromDMSID(copyFolderId, true);

        OwCMISObject dossier = (OwCMISObject) this.getNetwork().getObjectFromDMSID(dossierId, true);
        OwCMISObject image = (OwCMISObject) this.getNetwork().getObjectFromDMSID(hrImageId, true);

        {
            OwCMISObject dossierCopy = dossier.createCopy(copyFolder, null, null, null);
            OwObjectCollection col = dossierCopy.getParents();
            if (col != null)
            {
                OwCMISObject dossierCopyParent = (OwCMISObject) col.get(0);
                assertEquals(copyFolder.getDMSID(), dossierCopyParent.getDMSID());
            }
            assertEquals(dossier.getName(), dossierCopy.getName());
        }

        {
            final String newName = "newFolderCopyName";

            OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
            OwPropertyCollection newProperties = integrationFixture.createProperties(new Object[][] { { PropertyIds.NAME, newName } }, image.getObjectClass());

            OwCMISObject dossierCopy = dossier.createCopy(copyFolder, newProperties, null, null);
            OwObjectCollection col = dossierCopy.getParents();
            if (col != null)
            {
                OwCMISObject dossierCopyParent = (OwCMISObject) dossierCopy.getParents().get(0);
                assertEquals(copyFolder.getDMSID(), dossierCopyParent.getDMSID());
            }
            assertEquals(newName, dossierCopy.getName());
        }

        {
            OwCMISObject imageCopy = image.createCopy(copyFolder, null, null, null);
            OwObjectCollection col = imageCopy.getParents();
            if (col != null)
            {
                OwCMISObject imageCopyParent = (OwCMISObject) col.get(0);
                assertEquals(copyFolder.getDMSID(), imageCopyParent.getDMSID());
            }
            assertEquals(image.getName(), imageCopy.getName());
            assertEquals(image.getProperty(PropertyIds.NAME).getValue(), imageCopy.getProperty(PropertyIds.NAME).getValue());
        }

        {
            final String newName = "newDocumentCopyName";

            OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
            OwPropertyCollection newProperties = integrationFixture.createProperties(new Object[][] { { PropertyIds.NAME, newName } }, image.getObjectClass());

            OwCMISObject imageCopy = image.createCopy(copyFolder, newProperties, null, null);
            OwObjectCollection col = imageCopy.getParents();
            if (col != null)
            {
                OwCMISObject imageCopyParent = (OwCMISObject) col.get(0);
                assertEquals(copyFolder.getDMSID(), imageCopyParent.getDMSID());
            }
            assertFalse(image.getProperty(PropertyIds.NAME).getValue().equals(imageCopy.getProperty(PropertyIds.NAME).getValue()));
            assertEquals(newName, imageCopy.getProperty(PropertyIds.NAME).getValue());
        }

    }

    public void testEqualsWithDecorator() throws Exception
    {
        OwCMISObject hrImage0 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(hrImageId, true);
        final OwCMISObject hrImage1 = (OwCMISObject) this.getNetwork().getObjectFromDMSID(hrImageId, true);

        OwStandardDecoratorObject decoratedImage = new OwStandardDecoratorObject() {

            @Override
            public OwObject getWrappedObject()
            {
                return hrImage1;
            }
        };

        assertEquals(hrImage0, decoratedImage);
    }
}