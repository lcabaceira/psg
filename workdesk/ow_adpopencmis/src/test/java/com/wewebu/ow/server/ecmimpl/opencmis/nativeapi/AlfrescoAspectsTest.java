package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.cmis.client.AlfrescoAspects;
import org.alfresco.cmis.client.AlfrescoDocument;
import org.alfresco.cmis.client.AlfrescoObjectType;
import org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl;
import org.alfresco.cmis.client.type.AlfrescoType;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.log4j.Logger;

public class AlfrescoAspectsTest extends AbstractNativeTest
{
    static final Logger LOG = Logger.getLogger(AtomVsSoapTest.class);

    public AlfrescoAspectsTest()
    {
        super(true, AlfrescoObjectFactoryImpl.class.getName());//atom = true, SOAP = false
    }

    public void testAlfrescoType()
    {
        System.out.println(trace(getSession().getTypeDefinition("P:sys:localized"), true).toString());
        System.out.println(trace(getSession().getTypeDefinition("P:owd:AspectDate"), true).toString());
    }

    public void testTypeListing()
    {
        ItemIterable<ObjectType> types = getSession().getTypeChildren(null, true);

        for (ObjectType type : types)
        {
            System.out.println("----------------------");
            System.out.println(trace(type).toString());

            List<Tree<ObjectType>> children = getSession().getTypeDescendants(type.getId(), 1, true);
            for (Tree<ObjectType> subItem : children)
            {
                ObjectType subType = subItem.getItem();
                System.out.println(trace(subType).toString());
            }

        }
    }

    public void testCreate()
    {
        final String testDocumentName = "testCreateAA";
        CmisObject testObject = null;

        try
        {
            testObject = getSession().getObjectByPath("/" + testDocumentName);
            AlfrescoDocument alfrescoTestObject = (AlfrescoDocument) testObject;
            Collection<ObjectType> aspects = alfrescoTestObject.getAspects();
            System.out.println("Found test object having aspects:");
            for (ObjectType aspect : aspects)
            {
                System.out.println(trace(aspect));
            }
            testObject.delete(true);
        }
        catch (CmisObjectNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.NAME, testDocumentName);
        properties.put(PropertyIds.OBJECT_TYPE_ID, "D:owd:hrdocument,P:sys:localized,P:owd:AspectDate");
        Calendar now = Calendar.getInstance();
        System.out.println(now.getTimeZone());

        properties.put("owd:DispatchDate", now);

        ObjectId testObjectId = getSession().createDocument(properties, getSession().getRootFolder(), null, VersioningState.MAJOR);
        testObject = getSession().getObject(testObjectId);

        AlfrescoAspects alfrescoAspectsObject = (AlfrescoAspects) testObject;

        Collection<ObjectType> aspects = alfrescoAspectsObject.getAspects();
        AlfrescoType testType = (AlfrescoType) testObject.getType();
        System.err.println("TestObject type (mandatory) = " + testType.getMandatoryAspects() + " aspects : ");
        for (ObjectType aspect : aspects)
        {
            System.err.println(aspect.getId());
        }

        AlfrescoObjectType testObjectType = (AlfrescoObjectType) alfrescoAspectsObject.getTypeWithAspects();

        System.err.println("TestObjecType = " + testObjectType.getId());
        Collection<ObjectType> objectAspects = testObjectType.getAspects();

        System.err.println("TestObjecType aspects = " + Arrays.toString(objectAspects.toArray()));

        Property<Calendar> dispatchDateProperty = testObject.getProperty("owd:DispatchDate");
        Calendar dispatchDateValue = dispatchDateProperty.getValue();
        assertEquals(now.getTime(), dispatchDateValue.getTime());

    }

    public void testSlashAspectProperties() throws Exception
    {
        Session nativeSession = getSession();

        OperationContext context = nativeSession.createOperationContext();

        final String slashPropertyId = "owd:Slash/Property";
        final String entryDateId = "owd:EntryDate";
        final String createObjectType = "D:owd:hrdocument,P:owd:AspectDate,P:owd:SlashAspect";
        final String documentName = "SlashAspectProperties";
        final String documentPath = JUNIT_TEST_PATH + "/" + documentName;
        final String slashPropertyValue = "spv";
        final Calendar entryDateValue = Calendar.getInstance();

        Document document;
        try
        {
            document = (Document) nativeSession.getObjectByPath(documentPath, context);
            document.delete();
        }
        catch (CmisObjectNotFoundException e)
        {
            LOG.warn("No test document found!");
        }

        ObjectId documentId = null;

        {
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, createObjectType);
            properties.put(PropertyIds.NAME, documentName);
            properties.put(entryDateId, entryDateValue);
            properties.put(slashPropertyId, slashPropertyValue);

            documentId = nativeSession.createDocument(properties, junitFolder, null, VersioningState.MAJOR);

            document = (Document) nativeSession.getObject(documentId);

            Property<String> slashProperty = document.getProperty(slashPropertyId);

            assertEquals(slashPropertyValue, slashProperty.getValue());
        }

        {
            Properties fetchedName = nativeSession.getBinding().getObjectService().getProperties(nativeSession.getRepositoryInfo().getId(), documentId.getId(), PropertyIds.NAME, null);
            Map<String, Property<?>> fetchedNameMap = nativeSession.getObjectFactory().convertProperties(document.getType(), fetchedName);

            traceExtensions(fetchedName.getExtensions(), null);

            Property<String> nameProperty = (Property<String>) fetchedNameMap.get(PropertyIds.NAME);

            assertEquals(documentName, nameProperty.getValue());
        }

        {
            Properties fetchedEntryDate = nativeSession.getBinding().getObjectService().getProperties(nativeSession.getRepositoryInfo().getId(), documentId.getId(), entryDateId, null);
            Map<String, Property<?>> fetchedEntryDateMap = nativeSession.getObjectFactory().convertProperties(document.getType(), fetchedEntryDate);

            traceExtensions(fetchedEntryDate.getExtensions(), null);

            Property<Calendar> entryDateProperty = (Property<Calendar>) fetchedEntryDateMap.get(entryDateId);
            Calendar value = entryDateProperty.getValue();

            assertEquals(entryDateValue.getTime(), value.getTime());
        }

        {

            Properties fetchedSlashProperty = nativeSession.getBinding().getObjectService().getProperties(nativeSession.getRepositoryInfo().getId(), documentId.getId(), slashPropertyId, null);
            Map<String, Property<?>> fetchedSlashPropertyMap = nativeSession.getObjectFactory().convertProperties(document.getType(), fetchedSlashProperty);

            traceExtensions(fetchedSlashProperty.getExtensions(), null);

            Property<String> slashProperty = (Property<String>) fetchedSlashPropertyMap.get(slashPropertyId);
            assertNotNull(slashProperty);
            assertEquals(slashPropertyValue, slashProperty.getValue());
        }
    }

    private StringBuilder trace(ObjectType type)
    {
        return trace(type, false);
    }

    private StringBuilder trace(ObjectType type, boolean includeProperties)
    {
        StringBuilder trace = new StringBuilder();

        if (type.isBaseType())
        {
            trace.append("BASE type ");
            trace.append(type.getId());
        }
        else if (type instanceof AlfrescoType)
        {
            AlfrescoType alfrescoType = (AlfrescoType) type;

            trace.append(type.getId());
            Collection<String> aspects = null;
            trace.append("+[ ");
            try
            {
                aspects = alfrescoType.getMandatoryAspects();
            }
            catch (NullPointerException e)
            {
                trace.append("<null>");
            }

            if (aspects != null)
            {

                for (String aspect : aspects)
                {
                    trace.append(aspect);
                    trace.append(" ");
                }
            }
            trace.append("]");
        }
        else
        {
            trace.append("NON Alfresco type ");
            trace.append(type.getId());
            trace.append(" ");
            trace.append(type.getClass());
        }

        if (includeProperties)
        {
            trace.append("\n\t{\n");
            Map<String, PropertyDefinition<?>> propertiesDefinitions = type.getPropertyDefinitions();
            Collection<PropertyDefinition<?>> defintions = propertiesDefinitions.values();
            for (PropertyDefinition<?> definition : defintions)
            {
                trace.append("\n\t\t");
                trace.append(definition.getId());
                trace.append(",");
                trace.append(definition.getPropertyType().value());
            }
            trace.append("\n\t}\n");
        }
        return trace;
    }
}
