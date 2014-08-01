package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.List;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISFolder;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwException;

public class OwCMISFolderBigDataTest extends OwCMISBigDataTest
{

    public OwCMISFolderBigDataTest()
    {
        super("BigDataNetworkTest");
    }

    private OwCMISFolder getBigDataFolder() throws OwException
    {
        OwCMISNetwork nw = getNetwork();
        OwCMISResource defResource = nw.getResource(null);
        String bigFolderAWPath = "/" + defResource.getName() + getBigDataFolderPath();
        OwCMISFolder bigDataFolder = (OwCMISFolder) nw.getObjectFromPath(bigFolderAWPath, true);

        assertNotNull(bigDataFolder);

        return bigDataFolder;
    }

    public void testIterateDocuments() throws Exception
    {
        OwCMISFolder bigDataFolder = getBigDataFolder();

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_DOCUMENT);
        loadContext.setPageSize(testDocumentPageSize);
        OwIterable<OwCMISObject> documents = bigDataFolder.getChildren(loadContext);

        assertIterates(documents, -1, testDocumentPageSize, (int) getBigDataDocumentCount());
        assertIterationOrdered(documents);
    }

    public void testIterateFolders() throws Exception
    {
        OwCMISFolder bigDataFolder = getBigDataFolder();

        final int testFolderPageSize = (int) (getBigDataFolderCount() / 4);

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_FOLDER);
        loadContext.setPageSize(testFolderPageSize);

        OwIterable<OwCMISObject> folders = bigDataFolder.getChildren(loadContext);

        assertIterates(folders, -1, testFolderPageSize, (int) getBigDataFolderCount());
        assertIterationOrdered(folders);

    }

    public void testIterateMix() throws Exception
    {
        OwCMISFolder bigDataFolder = getBigDataFolder();

        final int testFolderPageSize = (int) (getBigDataFolderCount() / 4);
        final int testMixPageSize = Math.min(testFolderPageSize, testDocumentPageSize);

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER);
        loadContext.setPageSize(testMixPageSize);

        OwIterable<OwCMISObject> mix = bigDataFolder.getChildren(loadContext);

        assertIterates(mix, (int) getBigDataObjectsCount(), testMixPageSize, (int) getBigDataObjectsCount());
        assertIterationOrdered(mix);

    }

    /*//FIXME enable after migration to 4.2 environment
     * Disabling Tests since issue in 4.1.x exist, that the sorting is no correctly handled
     * CMIS sort definition cmis:lastModificationDate ASC,cmis:name ASC
     * Fix: cmis:lastModificationDate ASC,[space]cmis:name ASC <-- but creates issue in Alfresco previous to 4.2.1
    public void testSortIterate() throws Exception
    {
        OwCMISFolder bigDataFolder = getBigDataFolder();

        final String[] sortCriteriaFQNames = new String[] { CMIS_LAST_MODIFICATION_DATE_FQN, CMIS_NAME_FQN };

        List<OwCMISObject> expectedChildren = null;
        {
            OwLoadContext loadContext = new OwLoadContext();
            loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_DOCUMENT);
            loadContext.setPageSize(testDocumentPageSize);

            expectedChildren = assertIterates(bigDataFolder.getChildren(loadContext), -1, testDocumentPageSize, (int) getBigDataDocumentCount());

            sort(expectedChildren, sortCriteriaFQNames);
        }

        OwIterable<OwCMISObject> children = null;
        {

            OwLoadContext loadContext = new OwLoadContext();
            loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_DOCUMENT);
            loadContext.setPageSize(testDocumentPageSize);
            loadContext.setSorting(createSort(sortCriteriaFQNames));

            children = bigDataFolder.getChildren(loadContext);

        }

        assertSorted(expectedChildren, children);

    }
    */

    public void testMixedSortIterate() throws Exception
    {
        OwCMISFolder bigDataFolder = getBigDataFolder();

        final int testFolderPageSize = (int) (getBigDataFolderCount() / 4);
        final int testMixPageSize = Math.min(testFolderPageSize, testDocumentPageSize);

        final String[] sortCriteriaNames = new String[] { CMIS_NAME, CMIS_LAST_MODIFICATION_DATE };

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER);
        loadContext.setPageSize(testMixPageSize);
        loadContext.setSorting(createSort(sortCriteriaNames));

        OwIterable<OwCMISObject> mix = bigDataFolder.getChildren(loadContext);

        List<OwCMISObject> mixedObjects = assertIterates(mix, (int) getBigDataObjectsCount(), testMixPageSize, (int) getBigDataObjectsCount());

        List<OwCMISObject> documents = mixedObjects.subList(0, (int) getBigDataDocumentCount());
        List<OwCMISObject> folders = mixedObjects.subList((int) getBigDataDocumentCount(), (int) getBigDataObjectsCount());

        sort(documents, sortCriteriaNames);
        sort(folders, sortCriteriaNames);

        //        for (OwCMISObject doc : documents)
        //        {
        //            System.out.println(doc.getName());
        //        }
        //        System.out.println("--------------------------------------------");
        //        Iterator<OwCMISObject> i = mix.iterator();
        //        while(i.hasNext())
        //        {
        //            System.out.println(i.next().getName());
        //        }

        assertSorted(0, documents, mix);
        assertSorted((int) getBigDataDocumentCount(), folders, mix);
    }
}
