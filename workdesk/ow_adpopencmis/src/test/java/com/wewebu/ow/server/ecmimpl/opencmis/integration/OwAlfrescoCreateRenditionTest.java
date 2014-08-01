package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.HashSet;
import java.util.List;

import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.ChallengeScheme;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwStandardContentCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISRendition;
import com.wewebu.ow.server.exceptions.OwServerException;

public class OwAlfrescoCreateRenditionTest extends OwCMISObjectTest
{

    private static final String VERSIONING_TEST_DOC_1 = "versioningTestDoc1";
    private String versioningTestDocument1DMSID;

    public OwAlfrescoCreateRenditionTest(String name)
    {
        super("DocumentObjectTest", name);
    }

    static final Logger LOG = Logger.getLogger(OwCMISDocumentObjectTest.class);

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        versioningTestDocument1DMSID = integrationFixture.createTestObject(BaseTypeId.CMIS_DOCUMENT.value(), VERSIONING_TEST_DOC_1, null, integrationFixture.getTestFolder());

    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    //TODO: Configure test to work with AtomPub URL from bootstrap
    @Test
    @Ignore
    public void testCreateRendition() throws Exception
    {
        OwCMISObject document = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        OwCMISNativeObject<?> cmisObject = (OwCMISNativeObject<?>) document;

        OwVersion version = document.getVersion();
        OwStandardContentCollection contentCollection1 = new OwStandardContentCollection(OwCMISDocumentObjectTest.class.getResourceAsStream("test.jpg"), OwContentCollection.CONTENT_TYPE_DOCUMENT, 1, "image/jpeg");
        version.save(contentCollection1, null, null);

        HashSet<String> renditionTypes = new HashSet<String>();
        renditionTypes.add("cmis:thumbnail");

        List<OwCMISRendition> renditions = cmisObject.retrieveRenditions(renditionTypes, true);
        assertTrue(renditions.isEmpty());

        //OwGlobalParametersConfiguration util = context.getConfiguration().getGlobalParameters();

        String BASE_URI = "http://localhost:8888/alfresco";

        String SERVICE_URI = "/service/wd/webscript/rendition";
        String type = "doclib";
        String id = document.getID();
        ClientResource cr = new ClientResource(BASE_URI + SERVICE_URI + "?id=" + id + "&renditionDefn=" + type);

        try
        {
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");
            Representation response = cr.post(null);

        }
        catch (ResourceException re)
        {
            String message = String.format("Could not create rendition of type %s for object with ID %s.", type, id);
            throw new OwServerException(message, re);
        }
        finally
        {
            cr.release();
        }

        document = (OwCMISObject) this.getNetwork().getObjectFromDMSID(versioningTestDocument1DMSID, true);
        cmisObject = (OwCMISNativeObject<?>) document;

        renditions = cmisObject.retrieveRenditions(renditionTypes, true);
        assertFalse(renditions.isEmpty());
        OwCMISRendition firstRendition = renditions.get(0);
        assertEquals("cmis:thumbnail", firstRendition.getType());
    }
}
