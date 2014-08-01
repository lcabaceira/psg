package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.List;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISDocument;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISFolder;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;

public class OwCMISNetworkBigDataTest extends OwCMISBigDataTest implements OwCMISBigDataTestResources
{

    public OwCMISNetworkBigDataTest()
    {
        super("BigDataNetworkTest");
    }

    public void testIterateSearch1() throws Exception
    {

        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_SEARCH1_XML);

        assertIterates(result, (int) getBigDataDocumentCount(D_CM_THUMBNAIL), testDocumentPageSize, (int) getBigDataDocumentCount(D_CM_THUMBNAIL));
    }

    public void testIterateSearch2() throws Exception
    {
        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_SEARCH2_XML);

        assertIterates(result, (int) getBigDataDocumentCount(D_CM_THUMBNAIL), testDocumentPageSize, (int) getBigDataDocumentCount(D_CM_THUMBNAIL));
    }

    public void testPageIterateSearchResult() throws Exception
    {
        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_SEARCH1_XML);

        final int pageSize1 = (int) (getBigDataDocumentCount(D_CM_THUMBNAIL) / 5);
        final int pageSize2 = (int) (getBigDataDocumentCount(D_CM_THUMBNAIL) / 7);

        assertIterates(result.getPage(pageSize1), (int) getBigDataDocumentCount(D_CM_THUMBNAIL), pageSize1, pageSize1);

        assertIterates(result.getPage(), (int) getBigDataDocumentCount(D_CM_THUMBNAIL), pageSize1, pageSize1);

        assertIterates(result, (int) getBigDataDocumentCount(D_CM_THUMBNAIL), testDocumentPageSize, (int) getBigDataDocumentCount(D_CM_THUMBNAIL));

        assertIterates(result.getPage(), (int) getBigDataDocumentCount(D_CM_THUMBNAIL), pageSize1, pageSize1);

        assertIterates(result.getPage(pageSize2), (int) getBigDataDocumentCount(D_CM_THUMBNAIL), pageSize2, pageSize2);

    }

    public void testItemOrderSearch() throws Exception
    {
        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_SEARCH1_XML);

        assertIterationOrdered(result);
    }

    public void testSortSearch() throws Exception
    {
        List<OwCMISObject> expectedThumbnails = assertIterates(runPageTestSearch(CM_THUMBNAIL_SEARCH1_XML), (int) getBigDataDocumentCount(D_CM_THUMBNAIL), testDocumentPageSize, (int) getBigDataDocumentCount(D_CM_THUMBNAIL));

        final String[] sortCriteriaNames = new String[] { CMIS_LAST_MODIFICATION_DATE, CMIS_NAME };
        sort(expectedThumbnails, sortCriteriaNames);

        final String[] sortCriteriaFQNames = new String[] { CMIS_LAST_MODIFICATION_DATE_FQN, CMIS_NAME_FQN };
        OwIterable<OwCMISObject> thumbnails = runPageTestSearch(CM_THUMBNAIL_SEARCH1_XML, createSort(sortCriteriaFQNames));

        assertSorted(expectedThumbnails, thumbnails);

    }

    public void testMultiTypeSearch() throws Exception
    {
        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_FOLDER_SEARCH1_XML);
        final int expectedCount = (int) (getBigDataDocumentCount(D_CM_THUMBNAIL) + getBigDataFolderCount());
        assertIterates(result, expectedCount, testDocumentPageSize, expectedCount);
    }

    public void testMultiTypeSearchPaging() throws Exception
    {
        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_FOLDER_SEARCH1_XML);
        final int expectedCount = (int) (getBigDataDocumentCount(D_CM_THUMBNAIL) + getBigDataFolderCount());

        final long documentBorderBuffer = getBigDataDocumentCount(D_CM_THUMBNAIL) / 10;
        final long folderBorderBuffer = getBigDataFolderCount() / 10;

        {
            final int borderPageSize = (int) (documentBorderBuffer + folderBorderBuffer);

            OwIterable<OwCMISObject> skipNearBorder = result.skipTo(getBigDataDocumentCount(D_CM_THUMBNAIL) - documentBorderBuffer);
            OwIterable<OwCMISObject> borderPage = skipNearBorder.getPage(borderPageSize);
            List<OwCMISObject> mixedObjects = assertIterates(borderPage, expectedCount, borderPageSize, borderPageSize);

            for (int i = 0; i < documentBorderBuffer; i++)
            {
                assertTrue(mixedObjects.get(i) instanceof OwCMISDocument);
            }

            for (int i = (int) documentBorderBuffer; i < documentBorderBuffer + folderBorderBuffer; i++)
            {
                assertTrue(mixedObjects.get(i) instanceof OwCMISFolder);
            }
        }

        {
            final int borderPageSize = (int) documentBorderBuffer;

            OwIterable<OwCMISObject> skipNearBorder = result.skipTo(getBigDataDocumentCount(D_CM_THUMBNAIL) - documentBorderBuffer);
            OwIterable<OwCMISObject> borderPage = skipNearBorder.getPage(borderPageSize);
            assertIterates(borderPage, expectedCount, borderPageSize, borderPageSize);
        }

    }

    public void testMultiTypeSearchSorting() throws Exception
    {
        OwIterable<OwCMISObject> result = runPageTestSearch(CM_THUMBNAIL_FOLDER_SEARCH1_XML);
        final int expectedDocumentCount = (int) getBigDataDocumentCount(D_CM_THUMBNAIL);
        final int expectedFolderCount = (int) getBigDataFolderCount();
        final int expectedCount = (int) (getBigDataDocumentCount(D_CM_THUMBNAIL) + getBigDataFolderCount());
        List<OwCMISObject> results = assertIterates(result, expectedCount, testDocumentPageSize, expectedCount);

        List<OwCMISObject> documents = results.subList(0, expectedDocumentCount);
        List<OwCMISObject> folders = results.subList(expectedDocumentCount, expectedDocumentCount + expectedFolderCount);

        final String[] sortCriteriaNames = new String[] { CMIS_LAST_MODIFICATION_DATE, CMIS_NAME };
        sort(documents, sortCriteriaNames);
        sort(folders, sortCriteriaNames);

        OwIterable<OwCMISObject> sortedResult = runPageTestSearch(CM_THUMBNAIL_FOLDER_SEARCH1_XML, createSort(sortCriteriaNames));

        //        for (OwCMISObject f : folders)
        //        {
        //            System.out.println(f.getName()+" "+f.getProperty(CMIS_LAST_MODIFICATION_DATE).getValue());
        //        }
        //        
        //        System.out.println("-----------------------------------------------");
        //        
        //        Iterator<OwCMISObject> i = sortedResult.iterator();
        //        while (i.hasNext())
        //        {
        //            OwCMISObject f = i.next();
        //            System.out.println(f.getName()+" "+f.getProperty(CMIS_LAST_MODIFICATION_DATE).getValue());
        //        }

        assertSorted(0, documents, sortedResult);
        assertSorted(expectedDocumentCount, folders, sortedResult);
    }

}
