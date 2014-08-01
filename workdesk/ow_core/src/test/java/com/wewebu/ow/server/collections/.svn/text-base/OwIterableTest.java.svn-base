package com.wewebu.ow.server.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwPage;

public class OwIterableTest
{
    private static final int ITEM_COUNT = 100;
    private static final int DEFAULT_ITEMS_PER_PAGE_COUNT = 10;

    private static List<Integer> testCollection = new ArrayList<Integer>();

    @BeforeClass
    public static void init()
    {
        for (int i = 0; i < ITEM_COUNT; i++)
        {
            testCollection.add(Integer.valueOf(i));
        }
    }

    @Test
    public void testGetTotalNumItems()
    {
        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        Assert.assertEquals(ITEM_COUNT, iterable.getTotalNumItems());
        Assert.assertEquals(ITEM_COUNT, iterable.skipTo(50).getTotalNumItems());
        Assert.assertEquals(ITEM_COUNT, iterable.skipTo(50).getPage().getTotalNumItems());
        Assert.assertEquals(ITEM_COUNT, iterable.skipTo(50).getPage(10).getTotalNumItems());
    }

    @Test
    public void testIterateWholeCollection()
    {
        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        int myCount = 0;
        for (Integer anInteger : iterable)
        {
            Assert.assertEquals(testCollection.get(myCount), anInteger);
            myCount++;
        }
        Assert.assertEquals(ITEM_COUNT, myCount);
    }

    @Test
    public void testIteratePageCollectionDefaultSize()
    {
        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        OwIterable<Integer> page = iterable.skipTo(30).getPage();
        int myCount = 30;
        for (Integer anInteger : page)
        {
            Assert.assertEquals(testCollection.get(myCount), anInteger);
            myCount++;
        }
        Assert.assertEquals(30 + DEFAULT_ITEMS_PER_PAGE_COUNT, myCount);
    }

    @Test
    public void testIteratePageCollectionSpecificSize()
    {
        final int PAGE_SIZE = 40;

        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        OwIterable<Integer> page = iterable.skipTo(30).getPage(PAGE_SIZE);
        int myCount = 30;
        for (Integer anInteger : page)
        {
            Assert.assertEquals(testCollection.get(myCount), anInteger);
            myCount++;
        }
        Assert.assertEquals(30 + PAGE_SIZE, myCount);
    }

    @Test
    public void testIteratePagePastTheEndOfCollection()
    {
        final int PAGE_SIZE = 40;
        final int SKIP_POSITION = ITEM_COUNT - PAGE_SIZE + 10;

        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        OwIterable<Integer> page = iterable.skipTo(SKIP_POSITION).getPage(PAGE_SIZE);
        int myCount = SKIP_POSITION;
        for (Integer anInteger : page)
        {
            Assert.assertEquals(testCollection.get(myCount), anInteger);
            myCount++;
        }
        Assert.assertEquals(ITEM_COUNT, myCount);
    }

    @Test
    public void testSkipTo()
    {
        final int SKIP_POSITION = 30;

        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        OwIterable<Integer> skippedIterable = iterable.skipTo(SKIP_POSITION);
        int myCount = SKIP_POSITION;
        for (Integer anInteger : skippedIterable)
        {
            Assert.assertEquals(testCollection.get(myCount), anInteger);
            myCount++;
        }
        Assert.assertEquals(ITEM_COUNT, myCount);
    }

    @Test
    public void testSkipPastTheEndOfCollection()
    {
        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        OwIterable<Integer> skippedIterable = iterable.skipTo(ITEM_COUNT + 10);
        Assert.assertFalse(skippedIterable.iterator().hasNext());
        Assert.assertFalse(skippedIterable.getHasMoreItems());
    }

    @Test
    public void testGetPageNumItems()
    {
        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        Assert.assertEquals(DEFAULT_ITEMS_PER_PAGE_COUNT, iterable.getPageNumItems());
        Assert.assertEquals(DEFAULT_ITEMS_PER_PAGE_COUNT, iterable.skipTo(50).getPageNumItems());
        Assert.assertEquals(DEFAULT_ITEMS_PER_PAGE_COUNT, iterable.skipTo(50).getPage().getPageNumItems());
        Assert.assertEquals(20, iterable.skipTo(50).getPage(20).getPageNumItems());
    }

    @Test
    public void testDifferentPageSizes()
    {
        final int SKIP_POSITION = 8;
        final int PAGE1_SIZE = 8;
        final int PAGE2_SIZE = 12;

        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        OwIterable<Integer> page1 = iterable.skipTo(SKIP_POSITION).getPage(PAGE1_SIZE);
        OwIterable<Integer> page2 = iterable.skipTo(SKIP_POSITION).getPage(PAGE2_SIZE);

        Assert.assertEquals(PAGE1_SIZE, page1.getPageNumItems());
        Assert.assertEquals(PAGE2_SIZE, page2.getPageNumItems());

        int myCount = SKIP_POSITION;
        for (Integer integer : page1)
        {
            Assert.assertEquals(testCollection.get(myCount), integer);
            myCount++;
        }
        Assert.assertEquals(SKIP_POSITION + PAGE1_SIZE, myCount);

        myCount = SKIP_POSITION;
        for (Integer integer : page2)
        {
            Assert.assertEquals(testCollection.get(myCount), integer);
            myCount++;
        }
        Assert.assertEquals(SKIP_POSITION + PAGE2_SIZE, myCount);
    }

    @Test
    public void testMultipleItyeratorsOnTheSameIterable()
    {
        OwAbstractIterable<Integer> iterable = new OwCollectionIterable<Integer>(0, new PhoneyFetcher(DEFAULT_ITEMS_PER_PAGE_COUNT));
        OwIterable<Integer> page = iterable.skipTo(0).getPage();

        Iterator<Integer> it1 = page.iterator();
        Iterator<Integer> it2 = page.iterator();

        while (it1.hasNext())
        {
            it1.next();
        }
        Assert.assertTrue("Itterators should not interact with each others!", it2.hasNext());
    }

    private static class PhoneyFetcher extends OwAbstractPageFetcher<Integer>
    {
        protected PhoneyFetcher(long maxNumItems)
        {
            super(maxNumItems);
        }

        protected OwPage<Integer> fetchPage(long skipCount)
        {
            int leftLimit = (int) skipCount;
            int rightLimit = (int) (skipCount + this.maxNumItems);

            if (leftLimit >= testCollection.size())
            {
                leftLimit = testCollection.size();
            }

            if (rightLimit >= testCollection.size())
            {
                rightLimit = testCollection.size();
            }

            List<Integer> pageList = testCollection.subList(leftLimit, rightLimit);
            return new OwPage<Integer>(pageList, testCollection.size(), (skipCount + this.maxNumItems) < testCollection.size());
        }

        /* (non-Javadoc)
         * @see com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher#newCopy(int)
         */
        @Override
        public OwAbstractPageFetcher<Integer> newCopy(int maxNumItems)
        {
            return new PhoneyFetcher(maxNumItems);
        }
    }
}
