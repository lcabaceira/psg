package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;

public class PropertyTest extends AbstractNativeTest
{
    public void assertNonNullProperty(String propertyId, String objectId, String objectType, String url, boolean atom, String user, String password) throws Exception
    {
        Session fnSession = createSession(atom, null, url, user, password);
        ObjectType type = fnSession.getTypeDefinition(objectType);
        assertNotNull(type);
        Map<String, PropertyDefinition<?>> definitions = type.getPropertyDefinitions();

        PropertyDefinition<?> propertyDefinition = definitions.get(propertyId);
        assertNotNull(propertyDefinition);

        System.err.println(propertyDefinition.isQueryable());

        boolean includeAcls = true;
        boolean includeAllowableActions = true;
        boolean includePolicies = true;
        IncludeRelationships includeRelationships = IncludeRelationships.NONE;
        Set<String> renditionFilter = null;
        boolean includePathSegments = true;
        String orderBy = null;
        boolean cacheEnabled = false;
        int maxItemsPerPage = 1;

        Set<String> filter = new HashSet<String>();

        filter.add(PropertyIds.OBJECT_ID);
        filter.add(propertyId);
        filter.add(PropertyIds.OBJECT_TYPE_ID);

        OperationContext newContext = fnSession.createOperationContext(filter, includeAcls, includeAllowableActions, includePolicies, includeRelationships, renditionFilter, includePathSegments, orderBy, cacheEnabled, maxItemsPerPage);

        CmisObject object = fnSession.getObject(objectId, newContext);

        assertEquals(type.getId(), object.getType().getId());
        Property<Object> property = object.getProperty(propertyId);
        assertNotNull(property);
    }

    public void test_FN_CmIndexingFailureCode_ATOM() throws Exception
    {
        assertNonNullProperty("CmIndexingFailureCode", "idd_B7D279E9-6D86-49FC-83F6-3BA1D08F7E86", "HrDocument", "http://abs-fncm52.alfresco.com:9080/fncmis/resources/Service", true, "P8Admin", "IBMFileNetP8");
    }

    public void test_FN_CmIndexingFailureCode_SOAP() throws Exception
    {
        assertNonNullProperty("CmIndexingFailureCode", "idd_B7D279E9-6D86-49FC-83F6-3BA1D08F7E86", "HrDocument", "http://abs-fncm52.alfresco.com:9080/fncmis/wsdl!", false, "P8Admin", "IBMFileNetP8");
    }

    public void test_Alfresco_cmis_versionSeriesCheckedOutBy() throws Exception
    {
        assertNonNullProperty("cmis:versionSeriesCheckedOutBy", "workspace://SpacesStore/cb3cb0ad-f6b6-4eaa-918e-104ff9b5f623", "cmis:document", "http://abs-alfone.alfresco.com:8080/alfresco/cmisatom", true, "admin", "admin");
        assertNonNullProperty("cmis:versionSeriesCheckedOutBy", "workspace://SpacesStore/cb3cb0ad-f6b6-4eaa-918e-104ff9b5f623", "cmis:document", "http://abs-alfone.alfresco.com:8080/alfresco/cmisws/", false, "admin", "admin");
    }

}
