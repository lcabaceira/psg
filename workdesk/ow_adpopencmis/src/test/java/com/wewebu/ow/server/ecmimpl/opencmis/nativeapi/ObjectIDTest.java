package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;

public class ObjectIDTest extends AbstractNativeTest
{
    private Folder testFolder;
    private Document hrImage;
    private Folder dossier;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.testFolder = (Folder) getSession().getObjectByPath("/JUnitTest/");

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "D:owd:hrimage");
        properties.put(PropertyIds.NAME, "MyDossierImage");

        this.hrImage = testFolder.createDocument(properties, null, VersioningState.MAJOR);

        properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "F:owd:dossier");
        properties.put(PropertyIds.NAME, "MyDossier");

        this.dossier = testFolder.createFolder(properties);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.nativeapi.AbstractNativeTest#tearDown()
     */
    @Override
    protected void tearDown() throws Exception
    {
        this.dossier.delete();
        this.hrImage.delete();

        super.tearDown();
    }

    public void testObjectRefThroughID() throws Exception
    {
        OperationContext ctx = getSession().createOperationContext();
        ctx.setCacheEnabled(false);
        Folder myDossier = (Folder) getSession().getObject(this.dossier.getId(), ctx);

        Property<Object> dossierPictureProp = myDossier.getProperty("owd:dossierPicture");
        assertNull(dossierPictureProp.getValue());
        assertEquals(PropertyType.ID, dossierPictureProp.getType());

        Map<String, Object> properties = new HashMap<String, Object>();
        String imgId = this.hrImage.getId();
        //imgId = imgId.split(";")[0]; // workspace://SpacesStore/365f06a5-1783-439e-aeda-993e41ee3999;1.0
        properties.put("owd:dossierPicture", imgId);

        ObjectId newId = myDossier.updateProperties(properties, true);
        myDossier = (Folder) getSession().getObject(newId, ctx);

        dossierPictureProp = myDossier.getProperty("owd:dossierPicture");
        assertNotNull(dossierPictureProp.getValue());
        assertEquals(imgId, dossierPictureProp.getValue());
    }
}
