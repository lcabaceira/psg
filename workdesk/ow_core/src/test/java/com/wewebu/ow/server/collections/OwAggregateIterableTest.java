package com.wewebu.ow.server.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;

public class OwAggregateIterableTest
{
    private static final int COL1_COUNT = 8;
    private static final int COL2_COUNT = 12;

    private OwObjectCollection collection1;
    private OwObjectCollection collection2;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() throws Exception
    {
        collection1 = new OwStandardObjectCollection();
        for (int i = 0; i < COL1_COUNT; i++)
        {
            collection1.add(Integer.toString(i));
        }

        collection2 = new OwStandardObjectCollection();
        for (int i = 0; i < COL2_COUNT; i++)
        {
            collection2.add(Integer.valueOf(i));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetHasMoreItems() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwObjectCollectionIterableAdapter<Object> iterable2 = new OwObjectCollectionIterableAdapter<Object>(collection2, 5);
        OwIterable<Object> aggregate = new OwAggregateIterable<Object>(iterable1, iterable2);

        OwIterable<Object> aPage = aggregate.skipTo(5).getPage();
        Assert.assertTrue(aPage.getHasMoreItems());

        aPage = aggregate.skipTo(14).getPage();
        Assert.assertTrue(aPage.getHasMoreItems());

        aPage = aggregate.skipTo(15).getPage();
        Assert.assertFalse(aPage.getHasMoreItems());

        aPage = aggregate.skipTo(18).getPage();
        Assert.assertFalse(aPage.getHasMoreItems());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAggregatePageSkipTo() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwObjectCollectionIterableAdapter<Object> iterable2 = new OwObjectCollectionIterableAdapter<Object>(collection2, 5);
        OwIterable<Object> aggregate = new OwAggregateIterable<Object>(iterable1, iterable2);

        OwIterable<Object> firstPage = aggregate.skipTo(0).getPage();
        long count = 0;
        for (Object object : firstPage)
        {
            Assert.assertTrue(object instanceof String);
            count++;
        }
        Assert.assertEquals(5, count);

        OwIterable<Object> secondPage = aggregate.skipTo(5).getPage();
        count = 0;
        for (Object object : secondPage)
        {
            if (count < 3)
            {
                Assert.assertTrue(object instanceof String);
            }
            else
            {
                Assert.assertTrue(object instanceof Integer);
            }
            count++;
        }
        Assert.assertEquals(5, count);

        OwIterable<Object> thirdPage = aggregate.skipTo(15).getPage(5);
        count = 0;
        for (Object object : thirdPage)
        {
            Assert.assertTrue(object instanceof Integer);
            count++;
        }
        Assert.assertEquals(5, count);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAggregatePageMaxItemsSkipTo() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwObjectCollectionIterableAdapter<Object> iterable2 = new OwObjectCollectionIterableAdapter<Object>(collection2, 5);
        OwIterable<Object> aggregate = new OwAggregateIterable<Object>(iterable1, iterable2);

        OwIterable<Object> firstPage = aggregate.skipTo(0).getPage(6);
        long count = 0;
        for (Object object : firstPage)
        {
            Assert.assertTrue(object instanceof String);
            count++;
        }
        Assert.assertEquals(6, count);

        OwIterable<Object> secondPage = aggregate.skipTo(6).getPage(10);
        count = 0;
        for (Object object : secondPage)
        {
            if (count < 2)
            {
                Assert.assertTrue(object instanceof String);
            }
            else
            {
                Assert.assertTrue(object instanceof Integer);
            }
            count++;
        }
        Assert.assertEquals(10, count);

        OwIterable<Object> thirdPage = aggregate.skipTo(16).getPage();
        count = 0;
        for (Object object : thirdPage)
        {
            Assert.assertTrue(object instanceof Integer);
            count++;
        }
        Assert.assertEquals(4, count);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @Test
    public void testWholeCollectionIterator() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwObjectCollectionIterableAdapter<Object> iterable2 = new OwObjectCollectionIterableAdapter<Object>(collection2, 5);
        OwIterable<Object> aggregate = new OwAggregateIterable<Object>(iterable1, iterable2);

        int count = 0;
        for (Object object : aggregate)
        {
            count++;
        }
        Assert.assertEquals(COL1_COUNT + COL2_COUNT, count);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @Test
    public void testSkipToMiddleThenWholeIterator() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwObjectCollectionIterableAdapter<Object> iterable2 = new OwObjectCollectionIterableAdapter<Object>(collection2, 5);
        OwIterable<Object> aggregate = new OwAggregateIterable<Object>(iterable1, iterable2);

        int count = 4;
        for (Object object : aggregate.skipTo(count))
        {
            count++;
        }

        Assert.assertEquals(COL1_COUNT + COL2_COUNT, count);

        count = 10;
        for (Object object : aggregate.skipTo(count))
        {
            count++;
        }

        Assert.assertEquals(COL1_COUNT + COL2_COUNT, count);

        count = 8;
        for (Object object : aggregate.skipTo(count))
        {
            count++;
        }

        Assert.assertEquals(COL1_COUNT + COL2_COUNT, count);

        count = 25;
        OwIterable<Object> empty = aggregate.skipTo(count);
        Assert.assertTrue(empty instanceof OwEmptyIterable);
    }
}
