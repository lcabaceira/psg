package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationTest;

public abstract class OwCMISTransientObjectTest extends OwCMISIntegrationTest
{
    private Document document = null;
    private OperationContext context;

    public OwCMISTransientObjectTest(String name_p)
    {
        super(name_p);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        loginAdmin();

        Session nativeSession = getCmisSession();
        context = nativeSession.createOperationContext();

        Set<String> filter = new HashSet<String>();
        filter.add(PropertyIds.OBJECT_ID);
        filter.add(PropertyIds.OBJECT_TYPE_ID);

        context.setFilter(filter);

        try
        {
            this.document = (Document) nativeSession.getObjectByPath("/JUnitTest/FooNewName1", context);
        }
        catch (CmisObjectNotFoundException confex)
        {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
            properties.put(PropertyIds.NAME, "FooNewName1");
            ObjectId id = nativeSession.createDocument(properties, nativeSession.getObjectByPath("/JUnitTest/", context), null, VersioningState.MAJOR);

            document = (Document) nativeSession.getObject(id, context);
        }

    }

    protected abstract <N extends TransientCmisObject> OwCMISTransientObject<N> createTestObject(N transientCmisObject, OperationContext creationContext, Session session);

    public void testCreateInstance() throws Exception
    {
        Session nativeSession = getCmisSession();
        OwCMISTransientObject<TransientDocument> cache = createTestObject(document.getTransientDocument(), context, nativeSession);

        assertNotNull(cache.getTransientCmisObject().getProperty(PropertyIds.OBJECT_ID));
        assertNotNull(cache.getTransientCmisObject().getProperty(PropertyIds.OBJECT_TYPE_ID));

        assertEquals(this.document.getId(), cache.getTransientCmisObject().getProperty(PropertyIds.OBJECT_ID).getValue());
        assertEquals(this.document.getType().getId(), cache.getTransientCmisObject().getProperty(PropertyIds.OBJECT_TYPE_ID).getValue());

        assertNull(cache.getTransientCmisObject().getProperty(PropertyIds.NAME));
        assertNull(cache.getTransientCmisObject().getProperty(PropertyIds.CONTENT_STREAM_LENGTH));

        TransientDocument nativeObject = cache.getTransientCmisObject();

        assertNull(nativeObject.getProperty(PropertyIds.NAME));
        assertNull(nativeObject.getProperty(PropertyIds.CONTENT_STREAM_LENGTH));
    }

    public void testCacheAndNativeObjectSynchronized() throws Exception
    {
        Session nativeSession = getCmisSession();
        OwCMISTransientObject<TransientDocument> cache = createTestObject(document.getTransientDocument(), context, nativeSession);

        assertNull(cache.getTransientCmisObject().getProperty(PropertyIds.NAME));
        assertNull(cache.getTransientCmisObject().getProperty(PropertyIds.CONTENT_STREAM_LENGTH));

        Set<String> nativePropertyFilter = new HashSet<String>();
        nativePropertyFilter.add(PropertyIds.NAME);
        nativePropertyFilter.add(PropertyIds.CONTENT_STREAM_LENGTH);

        Map<String, Property<?>> securedProperties = cache.secureProperties(nativePropertyFilter);

        assertTrue(securedProperties.containsKey(PropertyIds.NAME));
        assertTrue(securedProperties.containsKey(PropertyIds.CONTENT_STREAM_LENGTH));

        assertNotNull(cache.getTransientCmisObject().getProperty(PropertyIds.NAME));

        TransientDocument nativeObject = cache.getTransientCmisObject();
        assertNotNull(cache.getTransientCmisObject().getProperty(PropertyIds.OBJECT_ID));
        assertNotNull(cache.getTransientCmisObject().getProperty(PropertyIds.OBJECT_TYPE_ID));

        assertNotNull(nativeObject.getProperty(PropertyIds.NAME));
        assertNotNull(nativeObject.getProperty(PropertyIds.CONTENT_STREAM_LENGTH));
    }

    public void testGetCmisObject() throws Exception
    {
        Session nativeSession = getCmisSession();
        OwCMISTransientObject<TransientDocument> cache = createTestObject(document.getTransientDocument(), context, nativeSession);

        TransientDocument nativeObject = cache.getTransientCmisObject();
        assertNull(nativeObject.getProperty(PropertyIds.NAME));
        assertNull(nativeObject.getProperty(PropertyIds.CONTENT_STREAM_LENGTH));

        Set<String> nativePropertyFilter = new HashSet<String>();
        nativePropertyFilter.add(PropertyIds.NAME);
        nativePropertyFilter.add(PropertyIds.CONTENT_STREAM_LENGTH);

        cache.secureProperties(nativePropertyFilter);
        nativeObject = cache.getTransientCmisObject();

        nativeObject = cache.getTransientCmisObject();
        assertNotNull(nativeObject.getProperty(PropertyIds.NAME));
        assertNotNull(nativeObject.getProperty(PropertyIds.CONTENT_STREAM_LENGTH));
    }
}
