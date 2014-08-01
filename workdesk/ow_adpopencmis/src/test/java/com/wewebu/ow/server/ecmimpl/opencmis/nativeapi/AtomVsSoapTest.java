package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.enums.ExtensionLevel;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.log4j.Logger;

public class AtomVsSoapTest extends AbstractNativeTest
{

    private static final String CM_TITLE = "cm:title";

    private static final String DOCUMENT_NAME = "AtomVsSoapTestDocument";
    private static final String DOCUMENT_PATH = JUNIT_TEST_PATH + "/" + DOCUMENT_NAME;

    private Document document;

    static final Logger LOG = Logger.getLogger(AtomVsSoapTest.class);

    public AtomVsSoapTest()
    {
        super(true, AlfrescoObjectFactoryImpl.class.getName());
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        Session nativeSession = getSession();
        OperationContext context = nativeSession.createOperationContext();

        Set<String> filter = new HashSet<String>();
        filter.add(PropertyIds.OBJECT_ID);
        filter.add(PropertyIds.OBJECT_TYPE_ID);
        filter.add(CM_TITLE);

        context.setFilter(filter);

        try
        {
            document = (Document) nativeSession.getObjectByPath(DOCUMENT_PATH, context);
            document.delete();
        }
        catch (CmisObjectNotFoundException e)
        {
            LOG.warn("No test document found!");
        }

        {

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
            properties.put(PropertyIds.NAME, DOCUMENT_NAME);
            properties.put(CM_TITLE, "aTitle");

            ObjectId documentId = nativeSession.createDocument(properties, junitFolder, null, VersioningState.MAJOR);

            document = (Document) nativeSession.getObject(documentId);
        }

    };

    public void testGetAspectProperties()
    {
        String nativeId = document.getId();

        //        {
        //            ObjectId checkedOutId = document.checkOut();
        //            Document checkedOutDocument = (Document) getSession().getObject(checkedOutId);
        //
        //            Map<String, Object> properties = new HashMap<String, Object>();
        //            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
        //            properties.put(CM_TITLE, "aTitleCI");
        //
        //            ObjectId checkedInId = checkedOutDocument.checkIn(true, properties, null, null);
        //            nativeId = checkedInId.getId();
        //        }
        {
            Session atomSession = createSession(true, AlfrescoObjectFactoryImpl.class.getName());

            Document document = (Document) atomSession.getObjectByPath(DOCUMENT_PATH);
            System.out.println("====================Properties Extension====================");
            traceExtensions(document.getExtensions(ExtensionLevel.PROPERTIES), null);
            System.out.println("====================Properties OBJECT====================");
            traceExtensions(document.getExtensions(ExtensionLevel.OBJECT), null);

            Properties fetchedNativeProperties = atomSession.getBinding().getObjectService().getProperties(atomSession.getRepositoryInfo().getId(), nativeId, CM_TITLE, null);
            Map<String, Property<?>> fetchedPropertiesMap = atomSession.getObjectFactory().convertProperties(document.getType(), fetchedNativeProperties);

            traceExtensions(fetchedNativeProperties.getExtensions(), null);

            assertNotNull(fetchedNativeProperties.getExtensions());
            assertNotNull(fetchedPropertiesMap.get(CM_TITLE));
        }

        {

            Session soapSession = createSession(false, AlfrescoObjectFactoryImpl.class.getName());

            Document document = (Document) soapSession.getObjectByPath(DOCUMENT_PATH);
            System.out.println("====================Properties Extension====================");
            traceExtensions(document.getExtensions(ExtensionLevel.PROPERTIES), null);
            System.out.println("====================Properties OBJECT====================");
            traceExtensions(document.getExtensions(ExtensionLevel.OBJECT), null);

            Properties fetchedNativeProperties = soapSession.getBinding().getObjectService().getProperties(soapSession.getRepositoryInfo().getId(), nativeId, CM_TITLE, null);
            Map<String, Property<?>> fetchedPropertiesMap = soapSession.getObjectFactory().convertProperties(document.getType(), fetchedNativeProperties);

            traceExtensions(fetchedNativeProperties.getExtensions(), null);

            assertNotNull(fetchedNativeProperties.getExtensions());
            assertNotNull(fetchedPropertiesMap.get(CM_TITLE));
        }
    }
}
