package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

/**
 *<p>
 *
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
public class SessionTest extends AbstractNativeTest
{

    public void testObjectFromPath()
    {
        CmisObject obj = getSession().getObjectByPath("/HR/Dossiers/");

        assertEquals("Dossiers", obj.getName());
        assertTrue(obj.getBaseTypeId() == BaseTypeId.CMIS_FOLDER);
    }

    public void testObjectLoadUnrequestedProperties()
    {
        String documentPath = "/HR/Dossiers/ClockLabels.doc";

        Session session = getSession();
        CmisObject obj = session.getObjectByPath(documentPath);
        String objId = obj.getId();

        OperationContext ctx = session.createOperationContext();
        ctx.setCacheEnabled(true);
        Set<String> filter = new HashSet<String>();
        filter.add(PropertyIds.NAME);
        ctx.setFilter(filter);

        obj = session.getObject(objId, ctx);

        assertNotNull(obj.getProperty(PropertyIds.NAME));
        assertNull(obj.getProperty("owd:documentComment"));

        filter.add("owd:documentComment");
        ctx.setFilter(filter);
        obj = session.getObject(objId, ctx);

        assertNotNull(obj.getProperty(PropertyIds.NAME));
        assertNotNull(obj.getProperty("owd:documentComment"));
        assertNull(obj.getProperty("owd:documentComment").getValue());
    }

    public void testObjectContentStream()
    {
        Session session = getSession();
        String documentPath = "/HR/Dossiers/ClockLabels.doc";
        CmisObject obj = session.getObjectByPath(documentPath);
        String objId = obj.getId();

        Set<String> propertyFilter = new HashSet<String>();
        propertyFilter.add("cmis:contentStreamId");

        OperationContext context = session.createOperationContext();
        context.setFilter(propertyFilter);
        context.setCacheEnabled(false);

        obj = session.getObject(objId, context);

        List<Property<?>> properties = obj.getProperties();
        for (Property<?> property : properties)
        {
            System.err.println(property.getId());
        }
        Property<Object> contentStreamId = obj.getProperty("cmis:contentStreamId");
        assertTrue(properties.contains(contentStreamId));

        assertNotNull(contentStreamId);
    }

    public void testDocumentSearch()
    {
        OperationContext opCtx = getSession().createOperationContext();
        opCtx.setIncludeAcls(false);
        opCtx.setIncludeAllowableActions(false);
        opCtx.setMaxItemsPerPage(10);
        opCtx.setIncludePathSegments(true);
        ItemIterable<QueryResult> results = getSession().query("SELECT * FROM cmis:document", false, opCtx);
        verifyResults(results, BaseTypeId.CMIS_DOCUMENT);
    }

    public void testFolderSearch()
    {
        OperationContext opCtx = getSession().createOperationContext();
        opCtx.setIncludeAcls(false);
        opCtx.setIncludeAllowableActions(false);
        opCtx.setMaxItemsPerPage(10);
        opCtx.setIncludePathSegments(true);
        ItemIterable<QueryResult> results = getSession().query("SELECT * FROM cmis:folder", false, opCtx);
        verifyResults(results, BaseTypeId.CMIS_FOLDER);
    }

    protected void verifyResults(ItemIterable<QueryResult> results, BaseTypeId type)
    {
        for (QueryResult res : results)
        {
            PropertyData<Object> prop = res.getPropertyById("cmis:baseTypeId");
            assertTrue(type.value().equals(prop.getFirstValue()));
        }
    }

}
