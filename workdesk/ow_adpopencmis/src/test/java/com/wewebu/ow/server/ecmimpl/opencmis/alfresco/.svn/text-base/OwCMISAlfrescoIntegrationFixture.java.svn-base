package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.log4j.Logger;

import test.com.wewebu.ow.server.ecm.OwIntegrationTest;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationFixture;
import com.wewebu.ow.server.exceptions.OwException;

public class OwCMISAlfrescoIntegrationFixture extends OwCMISIntegrationFixture
{

    public static final Logger LOG = Logger.getLogger(OwCMISAlfrescoIntegrationFixture.class);

    public static final String CMIS_OWBOOTSTRAP_ALFRESCO_ATOM_XML = "cmis_owbootstrap_alfresco_atom.xml";
    public static final String CMIS_OWBOOTSTRAP_ALFRESCO_SOAP_XML = "cmis_owbootstrap_alfresco_soap.xml";

    public static final String DOCUMENT_AUTHOR = "Document_Author";
    public static final String DOCUMENT_TITLE = "Document_TITLE";

    public static final String DOCUMENT_NAME = "CachedObjectTestDocumentAlfresco";
    public static final String FOLDER_NAME = "CachedObjectTestFolderAlfresco";

    protected OperationContext context;

    protected Folder folder = null;
    protected Document document = null;

    public OwCMISAlfrescoIntegrationFixture(OwIntegrationTest<? extends OwCMISNetwork> integrationTest)
    {
        super(integrationTest);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        setObjectAspectsFixture();
    }

    public Folder getFolder()
    {
        return folder;
    }

    public Document getDocument()
    {
        return document;
    }

    public OperationContext getContext()
    {
        return context;
    }

    protected Session getCmisSession() throws OwException
    {
        OwCMISNativeSession session = (OwCMISNativeSession) integrationTest.getNetwork().getSession(null);
        return session.getOpenCMISSession();
    }

    private String createTypeId(String[] typeIds)
    {
        StringBuilder typeIdBuilder = new StringBuilder();

        for (int i = 0; i < typeIds.length; i++)
        {
            if (typeIdBuilder.length() > 0)
            {
                typeIdBuilder.append(",");
            }
            typeIdBuilder.append(typeIds[i]);
        }
        return typeIdBuilder.toString();
    }

    private Map<String, ?> addTypeIds(String[] typeIds, Map<String, ?> properties)
    {
        HashMap<String, Object> propertiesCopy = new HashMap<String, Object>(properties);
        propertiesCopy.put(PropertyIds.OBJECT_TYPE_ID, createTypeId(typeIds));
        return propertiesCopy;
    }

    private ObjectId createAlfrescoFolder(String[] typeIds, Map<String, ?> properties, ObjectId folderId) throws OwException
    {
        Session nativeSession = getCmisSession();
        return nativeSession.createFolder(addTypeIds(typeIds, properties), folderId);
    }

    private ObjectId createAlfrescoDocument(String[] typeIds, Map<String, ?> properties, ObjectId folderId) throws OwException
    {
        Session nativeSession = getCmisSession();
        return nativeSession.createDocument(addTypeIds(typeIds, properties), folderId, null, VersioningState.MAJOR);
    }

    protected void setObjectAspectsFixture() throws Exception
    {
        Session nativeSession = getCmisSession();
        context = nativeSession.createOperationContext();

        Set<String> filter = new HashSet<String>();
        filter.add(PropertyIds.OBJECT_ID);
        filter.add(PropertyIds.OBJECT_TYPE_ID);
        filter.add("cm:title");

        context.setFilter(filter);

        Folder junitFolder = (Folder) nativeSession.getObjectByPath("/JUnitTest/");

        try
        {
            document = (Document) nativeSession.getObjectByPath("/JUnitTest/" + DOCUMENT_NAME, context);
            document.delete();
        }
        catch (CmisObjectNotFoundException e)
        {
            LOG.warn("No test document found!");
        }

        try
        {
            folder = (Folder) nativeSession.getObjectByPath("/JUnitTest/" + FOLDER_NAME, context);
            folder.delete();
        }
        catch (CmisObjectNotFoundException e)
        {
            LOG.warn("No test folder found!");
        }

        {
            String[] typeIds = new String[] { "D:owd:hrdocument", "P:cm:titled", "P:cm:author" };

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(PropertyIds.NAME, DOCUMENT_NAME);
            properties.put("cm:title", DOCUMENT_TITLE);
            properties.put("cm:author", DOCUMENT_AUTHOR);

            ObjectId documentId = createAlfrescoDocument(typeIds, properties, junitFolder);
        }

        {
            String[] typeIds = new String[] { "cmis:folder", "P:cm:titled", "P:cm:author" };

            Map<String, Object> properties = new HashMap<String, Object>();

            properties.put(PropertyIds.NAME, FOLDER_NAME);
            properties.put("cm:title", "Folder_TITLE");
            properties.put("cm:author", "Folder_Author");

            ObjectId folderId = createAlfrescoFolder(typeIds, properties, junitFolder);
        }

        document = (Document) nativeSession.getObjectByPath("/JUnitTest/" + DOCUMENT_NAME, context);
        folder = (Folder) nativeSession.getObjectByPath("/JUnitTest/" + FOLDER_NAME, context);

    };

}
