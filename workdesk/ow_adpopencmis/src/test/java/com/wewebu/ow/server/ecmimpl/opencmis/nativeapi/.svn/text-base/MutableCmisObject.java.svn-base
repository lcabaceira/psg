package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.chemistry.opencmis.commons.enums.Updatability;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ObjectDataImpl;

public class MutableCmisObject extends AbstractNativeTest
{
    public void testMutability() throws Exception
    {
        Session mySession = getSession();

        Set<String> initialFilter = new HashSet<String>();
        initialFilter.add(PropertyIds.OBJECT_ID);
        initialFilter.add(PropertyIds.OBJECT_TYPE_ID);

        boolean includeAcls = true;
        boolean includeAllowableActions = true;
        boolean includePolicies = true;
        IncludeRelationships includeRelationships = IncludeRelationships.NONE;
        Set<String> renditionFilter = null;
        boolean includePathSegments = true;
        String orderBy = null;
        boolean cacheEnabled = true;
        int maxItemsPerPage = 100;

        OperationContext initialContext = mySession.createOperationContext(initialFilter, includeAcls, includeAllowableActions, includePolicies, includeRelationships, renditionFilter, includePathSegments, orderBy, cacheEnabled, maxItemsPerPage);

        Document document = (Document) mySession.getObjectByPath("/JUnitTest/NewName1", initialContext);

        assertNotNull(document);
        assertNotNull(document.getProperty(PropertyIds.OBJECT_ID));
        assertNotNull(document.getProperty(PropertyIds.OBJECT_TYPE_ID));
        assertNull(document.getProperty(PropertyIds.NAME));

        Set<String> getPropertiesFilter = new HashSet<String>();
        getPropertiesFilter.add(PropertyIds.NAME);

        OperationContext getPropertiesContext = mySession.createOperationContext(getPropertiesFilter, includeAcls, includeAllowableActions, includePolicies, includeRelationships, renditionFilter, includePathSegments, orderBy, cacheEnabled,
                maxItemsPerPage);

        Properties gotProperties = mySession.getBinding().getObjectService().getProperties(mySession.getRepositoryInfo().getId(), document.getId(), getPropertiesContext.getFilterString(), null);

        List<Property<?>> documentProperties = document.getProperties();
        Map<String, Object> propertiesMap = new HashMap<String, Object>();
        for (Property<?> property : documentProperties)
        {
            propertiesMap.put(property.getId(), property);
        }

        Map<String, Property<?>> gotPropertiesMap = mySession.getObjectFactory().convertProperties(document.getType(), gotProperties);
        propertiesMap.putAll(gotPropertiesMap);

        ObjectDataImpl data = new ObjectDataImpl();
        Set<Updatability> allUpdatabilities = new HashSet<Updatability>(Arrays.asList(Updatability.values()));
        Properties properties = mySession.getObjectFactory().convertProperties(propertiesMap, document.getType(), allUpdatabilities);
        data.setProperties(properties);

        Document newDocument = (Document) mySession.getObjectFactory().convertObject(data, initialContext);

        assertNotNull(newDocument.getProperty(PropertyIds.OBJECT_ID));
        assertNotNull(newDocument.getProperty(PropertyIds.OBJECT_TYPE_ID));
        assertNotNull(newDocument.getProperty(PropertyIds.NAME));
    }
}
