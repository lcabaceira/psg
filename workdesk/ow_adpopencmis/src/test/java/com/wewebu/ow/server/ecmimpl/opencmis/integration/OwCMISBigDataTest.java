package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.unittest.search.OwSearchTemplateLoader;

public abstract class OwCMISBigDataTest extends OwCMISIntegrationTest implements OwCMISBigDataTestResources
{

    static final Logger LOG = Logger.getLogger(OwCMISBigDataTest.class);

    private static final String IMMUTABLE_BIG_DATA_FOLDER = "ImmutableBigDataFolder";
    private static final long IMMUTABLE_BIG_DATA_FOLDER_COUNT = 40;

    private Map<String, Integer> testTypes = new HashMap<String, Integer>();
    private Map<String, String> testQTypes = new HashMap<String, String>();

    protected int testDocumentPageSize;

    public OwCMISBigDataTest(String name_p)
    {
        super(name_p);
    }

    protected Map<String, Object> createProperties(String name, String type, Map<String, Object> extraProperties)
    {
        Map<String, Object> properties = new HashMap<String, Object>(extraProperties);
        properties.put(PropertyIds.NAME, name);
        properties.put(PropertyIds.OBJECT_TYPE_ID, type);

        return properties;
    }

    protected ObjectId createDocument(Session session, String name, String type, ObjectId parent, Map<String, Object> properties)
    {
        return session.createDocument(createProperties(name, type, properties), parent, null, VersioningState.MAJOR);
    }

    protected ObjectId createFolder(Session session, String name, String type, ObjectId parent, Map<String, Object> properties)
    {
        return session.createFolder(createProperties(name, type, properties), parent);
    }

    protected void deleteBigData(Session session)
    {
        final String folderPath = getBigDataFolderPath();
        LOG.warn("Deleting big data in " + folderPath);

        Folder bigDataFolder = null;
        try
        {
            bigDataFolder = (Folder) session.getObjectByPath(folderPath);
            OperationContext ctx = session.createOperationContext();
            ItemIterable<CmisObject> bigDataChildren;
            boolean oneMore = true;
            while (oneMore)
            {

                bigDataChildren = bigDataFolder.getChildren(ctx);
                LOG.warn("Deleting " + bigDataChildren.getTotalNumItems() + " big data children in " + folderPath);
                Iterator<CmisObject> i = bigDataChildren.iterator();

                oneMore = i.hasNext();

                while (i.hasNext())
                {
                    CmisObject child = i.next();
                    if (child == null)
                    {
                        LOG.error("null next object! Invalid CmisObject iterator behaviour!");
                    }
                    else
                    {
                        child.delete();
                    }
                }
            }

            bigDataFolder.delete();

        }
        catch (CmisObjectNotFoundException e)
        {
            LOG.warn("No big data folder " + folderPath + " to delete", e);
        }
    }

    protected void purgeBigData(Session session) throws Exception
    {
        final String folderName = getBigDataFolderName();
        final String folderPath = getBigDataFolderPath();
        final long documentCount = getBigDataDocumentCount();
        final long folderCount = getBigDataFolderCount();
        LOG.warn("Purging big data in " + folderPath);

        deleteBigData(session);

        LOG.warn("Creating " + folderPath);
        ObjectId bigDataFolderId = createFolder(session, folderName, "cmis:folder", session.getRootFolder(), Collections.EMPTY_MAP);
        Folder bigDataFolder = (Folder) session.getObject(bigDataFolderId);

        Set<String> typeIds = testTypes.keySet();
        Random nameRandom = new Random(System.currentTimeMillis());
        {
            LOG.warn("Creating " + documentCount + " documents in " + folderPath);
            final long tenthDocumentCount = documentCount / 10;
            int totalCount = 0;
            int typeIndex = 0;

            //            String[] users = new String[] { "admin", "JUnitTester" };
            //            String[] passwords = new String[] { "admin", "junit" };
            //            login(users[0], passwords[0]);

            for (String type : typeIds)
            {
                Session userSession = getCmisSession();

                typeIndex++;
                Integer typeCount = testTypes.get(type);
                for (int i = 0; i < typeCount; i++)
                {
                    final String documentNamePrefix = "d_" + (char) ('A' + nameRandom.nextInt('Z' - 'A')) + "_";

                    totalCount++;
                    String name = documentNamePrefix + "T_" + typeIndex + "_" + String.format("%03d", totalCount);

                    Map<String, Object> properties = createTestProperties(totalCount, i, type, name);
                    createDocument(userSession, name, type, bigDataFolder, properties);

                    if (totalCount % tenthDocumentCount == 0)
                    {
                        long percent = (((totalCount) * 100) / documentCount);

                        LOG.warn("\t\t" + percent + "% of " + documentCount);

                        //                        int userIndex = (int) ((percent/10) % users.length);
                        //                        login(users[userIndex], passwords[userIndex]);
                    }
                }
            }
        }

        {
            LOG.warn("Creating " + folderCount + " folders in " + folderPath);

            for (long i = 0; i < folderCount; i++)
            {
                final String folderNamePrefix = "fld_" + (char) ('A' + nameRandom.nextInt('Z' - 'A')) + "_bfc_";
                String name = folderNamePrefix + String.format("%03d", i + 1);

                Map<String, Object> folderProperties = new HashMap<String, Object>();
                createFolder(session, name, "cmis:folder", bigDataFolder, folderProperties);

            }
        }

        for (String type : typeIds)
        {
            String qDocuments = "SELECT * FROM " + testQTypes.get(type) + " WHERE ((IN_TREE('" + bigDataFolderId.getId() + "')))";
            wait(session, qDocuments, testTypes.get(type));
        }

        String qFolders = "SELECT * FROM cmis:folder WHERE ((IN_TREE('" + bigDataFolderId.getId() + "')))";
        wait(session, qFolders, folderCount);
    }

    private Map<String, Object> createTestProperties(int totalCount, int typeCount, String type, String name)
    {
        Map<String, Object> thumbnailProperties = new HashMap<String, Object>();
        Random r = new Random(System.currentTimeMillis());
        if (type.equals(D_CM_THUMBNAIL))
        {

            thumbnailProperties.put(CM_THUMBNAIL_NAME, (char) ('A' + r.nextInt(24)) + "TN");
        }

        return thumbnailProperties;
    }

    private void wait(Session session, String query, long count)
    {
        final int attempts = 10;
        int attempt = attempts;
        while (--attempt > 0)
        {
            try
            {
                final int mills = 5000;
                LOG.warn("Sleeping " + mills + " ms to allow indexation : " + (attempts - attempt) + "/" + attempts);
                Thread.sleep(mills);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            ItemIterable<QueryResult> result = session.query(query, false);
            if (result.getTotalNumItems() == count)
            {
                break;
            }
            else if (result.getTotalNumItems() > count)
            {
                throw new RuntimeException("Expected count exceded!");
            }
        }

    }

    protected boolean checkBigData(Session session)
    {

        final String folderPath = getBigDataFolderPath();
        LOG.warn("Checking big data in " + folderPath);

        Folder bigDataFolder = null;
        try
        {
            bigDataFolder = (Folder) session.getObjectByPath(folderPath);
            OperationContext ctx = session.createOperationContext();

            ItemIterable<CmisObject> bigDataChildren = bigDataFolder.getChildren(ctx);
            LOG.info("Folder children # " + bigDataChildren.getTotalNumItems() + " vs. expected # " + getBigDataObjectsCount());
            return bigDataChildren.getTotalNumItems() == getBigDataObjectsCount();

        }
        catch (CmisObjectNotFoundException e)
        {
            LOG.warn("No big data folder found in " + folderPath, e);
            return false;
        }
    }

    protected String getBigDataFolderName()
    {
        return IMMUTABLE_BIG_DATA_FOLDER + "_" + System.getProperty("user.name");
    }

    protected String getBigDataFolderPath()
    {
        return "/" + getBigDataFolderName();
    }

    protected long getBigDataObjectsCount()
    {
        return getBigDataDocumentCount() + getBigDataFolderCount();
    }

    protected long getBigDataDocumentCount(String type)
    {
        return testTypes.get(type);
    }

    protected long getBigDataDocumentCount()
    {
        Collection<Integer> counts = testTypes.values();
        int count = 0;
        for (Integer typeCount : counts)
        {
            count += typeCount;
        }

        return count;
    }

    protected long getBigDataFolderCount()
    {
        return IMMUTABLE_BIG_DATA_FOLDER_COUNT;
    }

    protected OwSearchNode injectSearchPath(OwSearchNode search, String path) throws OwException
    {
        OwSearchNode special = search.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

        List specialChildren = special.getChilds();
        Iterator itSpecial = specialChildren.iterator();

        while (itSpecial.hasNext())
        {

            OwSearchNode node = (OwSearchNode) itSpecial.next();
            OwSearchCriteria crit = node.getCriteria();
            if (crit.getClassName().equals(OwSearchPathField.CLASS_NAME))
            {
                Session session = getCmisSession();
                String id = session.getRepositoryInfo().getId();
                crit.setValue(new OwSearchPath(null, path, true, new OwSearchObjectStore(id, null)));
            }
        }

        return search;
    }

    protected OwSearchTemplate loadSearchTemplate(String searchTemplateName) throws Exception
    {
        OwSearchTemplate template = new OwSearchTemplateLoader(getClass()).loadLocalClassPathTemplate(searchTemplateName, getNetwork());
        return template;
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        testTypes = createTestTypes();
        testQTypes = createTestQTypes();

        loginAdmin();
        Session session = getCmisSession();
        //        deleteBigData(session);
        if (!checkBigData(session))
        {
            purgeBigData(session);
        }

        Collection<Integer> counts = testTypes.values();
        int minCount = Integer.MAX_VALUE;
        for (Integer count : counts)
        {
            minCount = Math.min(count, minCount);
        }

        testDocumentPageSize = minCount / 4;
    }

    protected Map<String, Integer> createTestTypes()
    {
        Map<String, Integer> types = new HashMap<String, Integer>();
        types.put("D:cm:thumbnail", 100);
        types.put("D:fm:post", 100);
        return types;
    }

    protected Map<String, String> createTestQTypes()
    {
        Map<String, String> types = new HashMap<String, String>();
        types.put("D:cm:thumbnail", "cm:thumbnail");
        types.put("D:fm:post", "fm:post");
        return types;
    }

    protected OwIterable<OwCMISObject> runPageTestSearch(String searchTemplateName) throws OwException, Exception
    {
        return runPageTestSearch(searchTemplateName, null);
    }

    protected OwIterable<OwCMISObject> runPageTestSearch(String searchTemplateName, OwSort sort) throws OwException, Exception
    {
        OwCMISNetwork myNetwork = getNetwork();

        assertTrue(myNetwork.canPageSearch());

        OwSearchTemplate searchTemplate = loadSearchTemplate(searchTemplateName);
        OwSearchNode search = searchTemplate.getSearch(true);
        injectSearchPath(search, getBigDataFolderPath());
        if (sort == null)
        {
            sort = searchTemplate.getSort(10);
        }

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setPageSize(testDocumentPageSize);
        loadContext.setSorting(sort);

        return myNetwork.doSearch(search, loadContext);
    }

    protected List<OwCMISObject> assertIterates(OwIterable<OwCMISObject> iterable, int expectedTotal, int expectedPage, int expectedCount)
    {

        assertEquals("Bad TotalNumItems ", expectedTotal, iterable.getTotalNumItems());
        assertEquals("Bad PageNumItems ", expectedPage, iterable.getPageNumItems());

        {
            List<OwCMISObject> objects = iterateOnce(iterable);
            assertEquals("Bad count ", expectedCount, objects.size());
        }

        {
            List<OwCMISObject> objects = iterateOnce(iterable);
            assertEquals("Bad re-iterate count ", expectedCount, objects.size());

            return objects;
        }

    }

    private List<OwCMISObject> iterateOnce(OwIterable<OwCMISObject> iterable)
    {
        int size = (int) iterable.getTotalNumItems();

        List<OwCMISObject> objects = size < 0 ? new ArrayList<OwCMISObject>() : new ArrayList<OwCMISObject>(size);

        Iterator<OwCMISObject> i = iterable.iterator();

        while (i.hasNext())
        {
            OwCMISObject o = i.next();
            assertNotNull(o);
            objects.add(o);
        }
        return objects;
    }

    protected void assertIterationOrdered(OwIterable<OwCMISObject> result)
    {
        Iterator<OwCMISObject> ri = result.iterator();
        List<String> orderedObjectIds = new LinkedList<String>();

        while (ri.hasNext())
        {
            OwCMISObject object = ri.next();
            orderedObjectIds.add(object.getDMSID());
        }

        LOG.info("Testing paged order of " + orderedObjectIds.size() + " items of a " + result.getTotalNumItems() + " iterable.");

        int index = 0;

        final long pageSize = result.getPageNumItems();
        long expectedPageCount;
        if (result.getTotalNumItems() < 0)
        {
            expectedPageCount = (long) Math.ceil((double) orderedObjectIds.size() / (double) pageSize);
        }
        else
        {
            expectedPageCount = (long) Math.ceil((double) result.getTotalNumItems() / (double) pageSize);
        }

        OwIterable<OwCMISObject> page = null;

        int pageCount = 0;
        do
        {
            page = result.skipTo(index).getPage();
            Iterator<OwCMISObject> pi = page.iterator();
            while (pi.hasNext())
            {
                OwCMISObject object = pi.next();
                assertEquals(orderedObjectIds.get(index), object.getDMSID());
                index++;
            }
            pageCount++;
        } while (page.getHasMoreItems());

        assertEquals(orderedObjectIds.size(), index);
        assertEquals(expectedPageCount, pageCount);
    }

    protected void assertSorted(List<OwCMISObject> expectedOrderedObjects, OwIterable<OwCMISObject> iterable)
    {
        assertSorted(0, expectedOrderedObjects, iterable);
    }

    protected void assertSorted(final int index, List<OwCMISObject> expectedOrderedObjects, OwIterable<OwCMISObject> iterable)
    {
        OwIterable<OwCMISObject> page = null;

        page = iterable.skipTo(index).getPage(expectedOrderedObjects.size());
        Iterator<OwCMISObject> pageIterator = page.iterator();
        int currentIndex = index;
        while (pageIterator.hasNext())
        {
            final int expectedIndex = currentIndex - index;
            OwCMISObject o = pageIterator.next();
            OwCMISObject exp = expectedOrderedObjects.get(expectedIndex);
            assertEquals("Bad iteration order at iterator-index " + index + " and expected index " + expectedIndex + " " + o.getName() + " vs. " + exp.getName(), exp.getDMSID(), o.getDMSID());
            currentIndex++;
        }
        assertEquals("Invalid size", expectedOrderedObjects.size(), currentIndex - index);
    }

    protected void sort(List<OwCMISObject> expectedOrderedObjects, final String[] sortCriteriaNames)
    {
        Collections.sort(expectedOrderedObjects, new Comparator<OwCMISObject>() {

            private String[] properties = sortCriteriaNames;

            @Override
            public int compare(OwCMISObject o1, OwCMISObject o2)
            {
                try
                {
                    for (String property : properties)
                    {
                        OwCMISProperty<?> o1P = o1.getProperty(property);
                        OwCMISProperty<?> o2P = o2.getProperty(property);
                        Comparable co1P = (Comparable<?>) o1P.getValue();
                        int comp = co1P.compareTo(o2P.getValue());
                        if (comp != 0)
                        {
                            return comp;
                        }
                    }
                }
                catch (OwException e)
                {
                    throw new RuntimeException(e);
                }

                return 0;
            }
        });
    }

    protected OwSort createSort(String[] sortCriteriaFQNames)
    {
        OwSort sort = new OwSort(sortCriteriaFQNames.length, true);

        for (int i = sortCriteriaFQNames.length; i > 0; i--)
        {
            OwSortCriteria sortCriteria = new OwSortCriteria(sortCriteriaFQNames[i - 1], true);

            sort.addCriteria(sortCriteria);
        }

        return sort;
    }
}
