package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.alfresco.OwCMISAlfrescoIntegrationFixture;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDocument;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISFolder;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;

public class OwCMISFolderObjectTest extends OwCMISObjectTest
{
    private static final int SUBFOLDER_COUNT = 10;
    private static final int DOCUMENT_COUNT = 10;

    private static final String FOLDER1_NAME = "folder1";
    private static final String SUB_FOLDER_PREFIX = "sub";
    private static final String DOCUMENT_PREFIX = "sub";

    private String folder1DMSID;

    private OwCMISFolder folder1;

    public OwCMISFolderObjectTest(String name)
    {
        super("OwCMISFolderObjectTest", name);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISObjectTest#setUp()
     */
    @SuppressWarnings({ "rawtypes", "unused" })
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OwCMISIntegrationFixture integrationFixture = (OwCMISIntegrationFixture) fixture;
        folder1DMSID = integrationFixture.createTestObject(BaseTypeId.CMIS_FOLDER.value(), FOLDER1_NAME, null, integrationFixture.getTestFolder());
        folder1 = (OwCMISFolder) this.getNetwork().getObjectFromDMSID(folder1DMSID, true);
        for (int i = 0; i < SUBFOLDER_COUNT; i++)
        {
            integrationFixture.createTestObject(BaseTypeId.CMIS_FOLDER.value(), SUB_FOLDER_PREFIX + i, null, folder1);

        }

        for (int i = 0; i < DOCUMENT_COUNT; i++)
        {
            String documentId = integrationFixture.createTestObject(BaseTypeId.CMIS_DOCUMENT.value(), DOCUMENT_PREFIX + i + "d", null, folder1);

            OwCMISDocument documentObject = (OwCMISDocument) this.getNetwork().getObjectFromDMSID(documentId, true);

            Map<String, String> relProps = new HashMap<String, String>();
            relProps.put("cmis:sourceId", folder1.getNativeObject().getId());
            relProps.put("cmis:targetId", documentObject.getNativeObject().getId());
            relProps.put("cmis:objectTypeId", "R:cm:basis");
            Session openCMISSession = ((OwCMISNativeSession) getNetwork().getSession(null)).getOpenCMISSession();
            ObjectId relId = openCMISSession.createRelationship(relProps, null, null, null);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISObjectTest#tearDown()
     */
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testGetChildsFolders() throws Exception
    {
        int[] iObjectTypes_p = new int[] { OwObjectReference.OBJECT_TYPE_FOLDER };
        OwSort sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", true));
        OwSearchNode filterCriteria_p = null;

        OwObjectCollection subfolders = folder1.getChilds(iObjectTypes_p, null, sort_p, 100, 0, filterCriteria_p);
        assertEquals(SUBFOLDER_COUNT, subfolders.size());
        OwObject prev = null;
        for (Object object : subfolders)
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) <= 0);
            }
            prev = owObject;
        }

        sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", false));
        subfolders = folder1.getChilds(iObjectTypes_p, null, sort_p, 100, 0, filterCriteria_p);
        assertEquals(SUBFOLDER_COUNT, subfolders.size());
        prev = null;
        for (Object object : subfolders)
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) >= 0);
            }
            prev = owObject;
        }
    }

    public void testGetChildsDocuments() throws Exception
    {
        int[] iObjectTypes_p = new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT };
        OwSort sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", true));
        OwSearchNode filterCriteria_p = null;

        OwObjectCollection documents = folder1.getChilds(iObjectTypes_p, null, sort_p, 100, 0, filterCriteria_p);
        assertEquals(DOCUMENT_COUNT, documents.size());
        OwObject prev = null;
        for (Object object : documents)
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) <= 0);
            }
            prev = owObject;
        }

        sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", false));
        documents = folder1.getChilds(iObjectTypes_p, null, sort_p, 100, 0, filterCriteria_p);
        assertEquals(DOCUMENT_COUNT, documents.size());
        prev = null;
        for (Object object : documents)
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) >= 0);
            }
            prev = owObject;
        }
    }

    public void testGetChildrenDocuments() throws Exception
    {
        long expectedTotalCount = -1l; //DOCUMENT_COUNT;
        int[] objectTypes = new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT };
        OwSort sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", true));
        OwSearchNode filterCriteria = null;

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setMaxSize(1000);
        loadContext.setPropertyNames(null);
        loadContext.setObjectTypes(objectTypes);
        loadContext.setSorting(sort_p);
        loadContext.setVersionSelection(0);
        loadContext.setFilter(filterCriteria);

        OwIterable<OwCMISObject> documentsIterable = folder1.getChildren(loadContext);

        assertEquals(expectedTotalCount, documentsIterable.getTotalNumItems());
        OwObject prev = null;
        for (Object object : documentsIterable.skipTo(0).getPage(DOCUMENT_COUNT))
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) <= 0);
            }
            prev = owObject;
        }

        sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", false));

        loadContext = new OwLoadContext();
        loadContext.setMaxSize(1000);
        loadContext.setPropertyNames(null);
        loadContext.setObjectTypes(objectTypes);
        loadContext.setSorting(sort_p);
        loadContext.setVersionSelection(0);
        loadContext.setFilter(filterCriteria);

        documentsIterable = folder1.getChildren(loadContext);
        assertEquals(expectedTotalCount, documentsIterable.getTotalNumItems());
        prev = null;
        for (Object object : documentsIterable.skipTo(0).getPage(DOCUMENT_COUNT))
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_DOCUMENT, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) >= 0);
            }
            prev = owObject;
        }
    }

    public void testGetChildrenFolders() throws Exception
    {
        int[] objectTypes = new int[] { OwObjectReference.OBJECT_TYPE_FOLDER };
        OwSort sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", true));
        OwSearchNode filterCriteria = null;

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setPageSize(1000);
        loadContext.setPropertyNames(null);
        loadContext.setObjectTypes(objectTypes);
        loadContext.setSorting(sort_p);
        loadContext.setVersionSelection(0);
        loadContext.setFilter(filterCriteria);

        OwIterable<OwCMISObject> subfoldersIterable = folder1.getChildren(loadContext);

        int count = 0;
        OwObject prev = null;
        for (Object object : subfoldersIterable)
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) <= 0);
            }
            prev = owObject;
            count++;
        }
        assertEquals(SUBFOLDER_COUNT, count);

        sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", false));

        loadContext = new OwLoadContext();
        loadContext.setPageSize(1000);
        loadContext.setPropertyNames(null);
        loadContext.setObjectTypes(objectTypes);
        loadContext.setSorting(sort_p);
        loadContext.setVersionSelection(0);
        loadContext.setFilter(filterCriteria);

        subfoldersIterable = folder1.getChildren(loadContext);
        count = 0;
        prev = null;
        for (Object object : subfoldersIterable)
        {
            OwObject owObject = (OwObject) object;
            assertEquals(OwObjectReference.OBJECT_TYPE_FOLDER, owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) >= 0);
            }
            prev = owObject;
            count++;
        }
        assertEquals(SUBFOLDER_COUNT, count);
    }

    public void testGetChildrenRelationships() throws Exception
    {
        int[] objectTypes = new int[] { OwObjectReference.OBJECT_TYPE_FOLDER, OwObjectReference.OBJECT_TYPE_LINK };
        OwSort sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", true));
        OwSearchNode filterCriteria = null;

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setPageSize(1000);
        loadContext.setPropertyNames(null);
        loadContext.setObjectTypes(objectTypes);
        loadContext.setSorting(sort_p);
        loadContext.setVersionSelection(0);
        loadContext.setFilter(filterCriteria);

        OwIterable<OwCMISObject> subfoldersIterable = folder1.getChildren(loadContext);

        int count = 0;
        for (Object object : subfoldersIterable)
        {
            OwObject owObject = (OwObject) object;
            assertTrue(OwObjectReference.OBJECT_TYPE_FOLDER == owObject.getType() || OwObjectReference.OBJECT_TYPE_LINK == owObject.getType());
            count++;
        }
        assertEquals(DOCUMENT_COUNT, count);
    }

    public void testGetChildrenDocumentsAndFolders() throws Exception
    {
        int[] objectTypes = new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER };
        OwSort sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", true));
        OwSearchNode filterCriteria = null;

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setMaxSize(1000);
        loadContext.setPropertyNames(null);
        loadContext.setObjectTypes(objectTypes);
        loadContext.setSorting(sort_p);
        loadContext.setVersionSelection(0);
        loadContext.setFilter(filterCriteria);

        OwIterable<OwCMISObject> subfoldersIterable = folder1.getChildren(loadContext);

        assertEquals(DOCUMENT_COUNT + SUBFOLDER_COUNT, subfoldersIterable.getTotalNumItems());
        OwObject prev = null;
        for (Object object : subfoldersIterable.skipTo(0).getPage(DOCUMENT_COUNT + SUBFOLDER_COUNT))
        {
            OwObject owObject = (OwObject) object;
            assertTrue(OwObjectReference.OBJECT_TYPE_DOCUMENT == owObject.getType() || OwObjectReference.OBJECT_TYPE_FOLDER == owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) <= 0);
            }
            prev = owObject;
        }

        sort_p = new OwSort();
        sort_p.addCriteria(new OwSortCriteria("cmis:name", false));

        loadContext = new OwLoadContext();
        loadContext.setMaxSize(1000);
        loadContext.setPropertyNames(null);
        loadContext.setObjectTypes(objectTypes);
        loadContext.setSorting(sort_p);
        loadContext.setVersionSelection(0);
        loadContext.setFilter(filterCriteria);

        subfoldersIterable = folder1.getChildren(loadContext);
        assertEquals(DOCUMENT_COUNT + SUBFOLDER_COUNT, subfoldersIterable.getTotalNumItems());
        prev = null;
        for (Object object : subfoldersIterable.skipTo(0).getPage(DOCUMENT_COUNT + SUBFOLDER_COUNT))
        {
            OwObject owObject = (OwObject) object;
            assertTrue(OwObjectReference.OBJECT_TYPE_DOCUMENT == owObject.getType() || OwObjectReference.OBJECT_TYPE_FOLDER == owObject.getType());
            if (null != prev)
            {
                String prevName = prev.getName();
                String currentName = owObject.getName();
                assertTrue(prevName.compareTo(currentName) >= 0);
            }
            prev = owObject;
        }
    }

    public void testGetVirtualProperties() throws Exception
    {
        OwCMISObject folder = (OwCMISObject) this.getNetwork().getObjectFromDMSID(folder1DMSID, true);
        List<String> virtualPropNames = new LinkedList<String>();
        virtualPropNames.add("OW_ObjectName");
        virtualPropNames.add("OW_ObjectPath");
        virtualPropNames.add("OW_ClassDescription");

        List<String> props = new LinkedList<String>();
        props.add("cmis:folder.cmis:name");
        props.add("cmis:folder.cmis:objectTypeId");
        props.add("cmis:folder.cmis:baseTypeId");

        OwPropertyCollection propCol = folder.getProperties(props);
        for (String prop : props)
        {
            assertTrue("Missing requested property name = " + prop, propCol.containsKey(prop));
        }

        for (String virtualProp : virtualPropNames)
        {
            assertFalse("Returning virtual Property, but was not requested", propCol.containsKey(virtualProp));
        }

        //all properties should be returned
        propCol = folder.getProperties(null);
        for (String virtualProp : virtualPropNames)
        {
            assertTrue("Missing virtual Property, name = " + virtualProp, propCol.containsKey(virtualProp));
        }

        for (String prop : props)
        {
            assertTrue("Missing basic property! name = " + prop, propCol.containsKey(prop));
        }
        assertFalse(propCol.containsKey("OW_VersionSeries"));//only part of documents, defined in CMIS spec
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISIntegrationTest#getConfigBootstrapName()
     */
    @Override
    protected String getConfigBootstrapName()
    {
        return OwCMISAlfrescoIntegrationFixture.CMIS_OWBOOTSTRAP_ALFRESCO_ATOM_XML;
    }
}
