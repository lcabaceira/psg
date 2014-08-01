package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.alfresco.cmis.client.TransientAlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import com.wewebu.ow.csqlc.ast.OwCharacterStringLiteral;
import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwColumnReference;
import com.wewebu.ow.csqlc.ast.OwCompoundSelectList;
import com.wewebu.ow.csqlc.ast.OwCorrelatedTableName;
import com.wewebu.ow.csqlc.ast.OwFromClause;
import com.wewebu.ow.csqlc.ast.OwJoinSpecification;
import com.wewebu.ow.csqlc.ast.OwJoinedTable;
import com.wewebu.ow.csqlc.ast.OwLikePredicate;
import com.wewebu.ow.csqlc.ast.OwMergeType;
import com.wewebu.ow.csqlc.ast.OwPredicateFormat;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.csqlc.ast.OwRepositoryTarget;
import com.wewebu.ow.csqlc.ast.OwSearchCondition;
import com.wewebu.ow.csqlc.ast.OwSelectSublist;
import com.wewebu.ow.csqlc.ast.OwSimpleTable;
import com.wewebu.ow.csqlc.ast.OwWhereClause;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISTestFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISTransientObjectTest;

/**
 *<p>
 * OwCMISAlfrescoBulkTransientObjectTest.
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
public abstract class OwCMISAlfrescoTransientObjectTest extends OwCMISTransientObjectTest
{

    public OwCMISAlfrescoTransientObjectTest(String name_p)
    {
        super(name_p);
    }

    @Override
    protected abstract <N extends TransientCmisObject> OwCMISAlfrescoTransientObject<N> createTestObject(N transientCmisObject, OperationContext creationContext, Session session);

    @Override
    protected OwCMISTestFixture createFixture()
    {
        return new OwCMISAlfrescoIntegrationFixture(this);
    }

    @Override
    protected String getConfigBootstrapName()
    {
        //        return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_ATOM_XML;
        return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_SOAP_XML;
    }

    public void testFetchAspectProperties() throws Exception
    {
        OwCMISAlfrescoIntegrationFixture integrationFixture = (OwCMISAlfrescoIntegrationFixture) fixture;

        Session nativeSession = getCmisSession();

        OwCMISAlfrescoTransientObject<TransientAlfrescoDocument> cachedDocument = createTestObject((TransientAlfrescoDocument) integrationFixture.getDocument().getTransientDocument(), integrationFixture.getContext(), nativeSession);

        assertNotNull(integrationFixture.getDocument().getProperty("cm:title"));
        assertNull(integrationFixture.getDocument().getProperty("cm:author"));
        assertNull(integrationFixture.getDocument().getProperty("owd:DispatchDate"));

        Property<?> documentTitle = cachedDocument.secureProperty("cm:title");
        assertNotNull(documentTitle);
        Property<?> documentAuthor = cachedDocument.secureProperty("cm:author");
        assertNotNull(documentAuthor);
        assertEquals("Document_Author", documentAuthor.getValueAsString());
        Property<?> dispatchDate = cachedDocument.secureProperty("owd:DispatchDate");
        assertNotNull(dispatchDate);
    }

    private List<String> idsOf(Collection<ObjectType> types)
    {
        List<String> ids = new LinkedList<String>();
        for (ObjectType type : types)
        {
            ids.add(type.getId());
        }
        return ids;
    }

    public void testFetchAspects() throws Exception
    {

        final Session nativeSession = getCmisSession();

        OwColumnQualifier cmisDocument = new OwColumnQualifier("cmis:document", "cmis:document", "a");
        OwColumnReference cmisName = new OwColumnReference(cmisDocument, PropertyIds.NAME);
        OwColumnReference documentCmisObjectId = new OwColumnReference(cmisDocument, PropertyIds.OBJECT_ID);
        OwColumnReference documentCmisObjectTypeId = new OwColumnReference(cmisDocument, PropertyIds.OBJECT_TYPE_ID);
        OwColumnReference documentBaseTypeId = new OwColumnReference(cmisDocument, PropertyIds.BASE_TYPE_ID);

        OwColumnQualifier cmAuthorQ = new OwColumnQualifier("cm:author", "P:cm:author", "b");
        OwColumnReference cmAuthor = new OwColumnReference(cmAuthorQ, "cm:author");
        OwColumnReference authorCmisObjectId = new OwColumnReference(cmAuthorQ, PropertyIds.OBJECT_ID);

        OwCompoundSelectList selectList = new OwCompoundSelectList();
        selectList.add(new OwSelectSublist(cmisName));
        selectList.add(new OwSelectSublist(documentCmisObjectId));
        selectList.add(new OwSelectSublist(documentCmisObjectTypeId));
        selectList.add(new OwSelectSublist(documentBaseTypeId));
        selectList.add(new OwSelectSublist(cmAuthor));

        OwSearchCondition searchCondition = new OwLikePredicate(cmisName, new OwCharacterStringLiteral("Cache%"), new OwPredicateFormat(null, null, "LIKE", "NOT LIKE", null, null));
        OwWhereClause whereClause = new OwWhereClause(searchCondition);

        OwCorrelatedTableName cmisDocumentTable = new OwCorrelatedTableName("cmis:document", cmisDocument);
        OwCorrelatedTableName cmAuthorTable = new OwCorrelatedTableName("cm:author", cmAuthorQ);
        OwJoinedTable joinedTable = new OwJoinedTable(cmisDocumentTable, cmAuthorTable, new OwJoinSpecification(documentCmisObjectId, authorCmisObjectId, OwJoinSpecification.DEFAULT));
        OwFromClause fromClause = new OwFromClause(joinedTable);

        OwSimpleTable simpleTable = new OwSimpleTable(selectList, fromClause, whereClause);

        OwRepositoryTarget repositoryTarget = new OwRepositoryTarget(nativeSession.getRepositoryInfo().getId(), OwMergeType.NONE);
        OwQueryStatement statement = new OwQueryStatement(repositoryTarget, simpleTable);

        Set<OwColumnQualifier> qualifiers = new HashSet<OwColumnQualifier>();
        qualifiers.add(cmisDocument);
        qualifiers.add(cmAuthorQ);
        statement.setNormalizedQualifiers(qualifiers);

        OperationContext oc = nativeSession.createOperationContext();
        oc.setMaxItemsPerPage(100);

        String sql = statement.createSQLString().toString();
        int queryAteemtps = 15;
        ItemIterable<QueryResult> results = null;
        QueryResult result = null;
        while (queryAteemtps > 0)
        {

            results = nativeSession.query(sql, false, oc);
            queryAteemtps--;
            results.iterator().hasNext();
            result = results.iterator().next();
            if (result != null)
            {
                break;
            }
            else
            {
                Thread.sleep(1000);
            }

        }

        if (result == null)
        {
            fail("No result found for query = " + sql);
        }

        OwCMISAlfrescoQueryResultConverterImpl<TransientDocument> converter = new OwCMISAlfrescoQueryResultConverterImpl<TransientDocument>(nativeSession);
        //        QueryResult result = results.iterator().next();

        {
            TransientDocument object = converter.toCmisObject(result, statement, oc);

            OwCMISAlfrescoTransientObject<TransientDocument> cachedResult = createTestObject(object, oc, nativeSession);

            TransientAlfrescoDocument alfrescoObject = (TransientAlfrescoDocument) cachedResult.getTransientCmisObject();
            Collection<ObjectType> aspects = alfrescoObject.getAspects();

            List<String> ids = idsOf(aspects);
            assertTrue(ids.contains("P:cm:author"));
            assertFalse(ids.contains("P:cm:titled"));

            assertTrue(cachedResult.mustSecureAspects());

            Property<?> author = cachedResult.getTransientCmisObject().getProperty("cm:author");
            assertNotNull(author);

            assertNull(cachedResult.getTransientCmisObject().getProperty("cm:title"));
            Property<?> title = cachedResult.secureProperty("cm:title");
            assertNotNull(title);

            assertFalse(cachedResult.mustSecureAspects());

            alfrescoObject = (TransientAlfrescoDocument) cachedResult.getTransientCmisObject();
            aspects = alfrescoObject.getAspects();

            ids = idsOf(aspects);
            assertTrue(ids.contains("P:cm:author"));
            assertTrue(ids.contains("P:cm:titled"));
        }
        {
            TransientDocument object = converter.toCmisObject(result, statement, oc);

            OwCMISAlfrescoTransientObject<TransientDocument> cachedResult = createTestObject(object, oc, nativeSession);

            TransientAlfrescoDocument alfrescoObject = (TransientAlfrescoDocument) cachedResult.getTransientCmisObject();
            Collection<ObjectType> aspects = alfrescoObject.getAspects();

            List<String> ids = idsOf(aspects);
            assertTrue(ids.contains("P:cm:author"));
            assertFalse(ids.contains("P:cm:titled"));

            assertTrue(cachedResult.mustSecureAspects());

            alfrescoObject = (TransientAlfrescoDocument) cachedResult.secureAspects();
            aspects = alfrescoObject.getAspects();

            ids = idsOf(aspects);
            assertTrue(ids.contains("P:cm:author"));
            assertTrue(ids.contains("P:cm:titled"));

            assertFalse(cachedResult.mustSecureAspects());
        }

    }
}
