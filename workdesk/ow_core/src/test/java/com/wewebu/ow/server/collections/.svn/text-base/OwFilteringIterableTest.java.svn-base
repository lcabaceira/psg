package com.wewebu.ow.server.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;

public class OwFilteringIterableTest
{
    private static final int COL1_COUNT = 20;

    private OwObjectCollection collection1;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() throws Exception
    {
        collection1 = new OwStandardObjectCollection();
        for (int i = 0; i < COL1_COUNT; i++)
        {
            collection1.add(Integer.toString(i));
            collection1.add(Integer.valueOf(i));
        }
    }

    @Test
    public void getTotalNumItems() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwFilteringIterable<Object> iterable = new OwFilteringIterable<Object>(iterable1, new OwItemFilter<Object>() {

            @Override
            public boolean accept(Object obj)
            {
                return (obj instanceof String);
            }
        }, 5);
        //no total calculation by default
        Assert.assertEquals(-1l, iterable.getTotalNumItems());

        //enable (exact) total count calculation
        iterable.setExactCalculation(true);
        Assert.assertEquals(COL1_COUNT, iterable.getTotalNumItems());
    }

    @Test
    public void testGetHasMoreItems() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwIterable<Object> iterable = new OwFilteringIterable<Object>(iterable1, new OwItemFilter<Object>() {

            @Override
            public boolean accept(Object obj)
            {
                return (obj instanceof String);
            }
        }, 5);

        OwIterable<Object> aPage = iterable.skipTo(5).getPage();
        Assert.assertTrue(aPage.getHasMoreItems());

        aPage = iterable.skipTo(18).getPage();
        Assert.assertFalse(aPage.getHasMoreItems());

        aPage = iterable.skipTo(15).getPage();
        Assert.assertFalse(aPage.getHasMoreItems());
    }

    @Test
    public void testPageSkipTo() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwIterable<Object> iterable = new OwFilteringIterable<Object>(iterable1, new OwItemFilter<Object>() {

            @Override
            public boolean accept(Object obj)
            {
                return (obj instanceof String);
            }
        }, 5);

        OwIterable<Object> firstPage = iterable.skipTo(0).getPage();
        long count = 0;
        for (Object object : firstPage)
        {
            Assert.assertTrue(object instanceof String);
            count++;
        }
        Assert.assertEquals(5, count);

        OwIterable<Object> secondPage = iterable.skipTo(5).getPage();
        count = 0;
        for (Object object : secondPage)
        {
            Assert.assertTrue(object instanceof String);
            count++;
        }
        Assert.assertEquals(5, count);
    }

    @Test
    public void testPageMaxItemsSkipTo() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwIterable<Object> iterable = new OwFilteringIterable<Object>(iterable1, new OwItemFilter<Object>() {

            @Override
            public boolean accept(Object obj)
            {
                return (obj instanceof String);
            }
        }, 5);

        OwIterable<Object> firstPage = iterable.skipTo(0).getPage(6);
        long count = 0;
        for (Object object : firstPage)
        {
            Assert.assertTrue(object instanceof String);
            count++;
        }
        Assert.assertEquals(6, count);

        OwIterable<Object> secondPage = iterable.skipTo(6).getPage(10);
        count = 0;
        for (Object object : secondPage)
        {
            Assert.assertTrue(object instanceof String);
            count++;
        }
        Assert.assertEquals(10, count);

        OwIterable<Object> thirdPage = iterable.skipTo(16).getPage();
        count = 0;
        for (Object object : thirdPage)
        {
            Assert.assertTrue(object instanceof String);
            count++;
        }
        Assert.assertEquals(4, count);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testWholeCollectionIterator() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwIterable<Object> filteringIterablete = new OwFilteringIterable(iterable1, new OwItemFilter<Object>() {

            @Override
            public boolean accept(Object obj)
            {
                return (obj instanceof Integer);
            }
        });

        int count = 0;
        Iterator<Object> iterator = filteringIterablete.iterator();

        iterator.hasNext();
        iterator.hasNext();
        Assert.assertTrue(iterator.hasNext());

        while (iterator.hasNext())
        {
            Object object = iterator.next();
            Assert.assertTrue(object instanceof Integer);
            count++;
        }
        Assert.assertEquals(COL1_COUNT, count);
    }

    @SuppressWarnings({ "unused" })
    @Test
    public void testSkipToMiddleThenWholeIterator() throws Exception
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwIterable<Object> filteringIterable = new OwFilteringIterable<Object>(iterable1, new OwItemFilter<Object>() {

            @Override
            public boolean accept(Object obj)
            {
                return (obj instanceof Integer);
            }
        });

        int count = 4;
        for (Object object : filteringIterable.skipTo(count))
        {
            count++;
        }

        Assert.assertEquals(COL1_COUNT, count);

        count = 10;
        for (Object object : filteringIterable.skipTo(count))
        {
            count++;
        }

        Assert.assertEquals(COL1_COUNT, count);

        count = 8;
        for (Object object : filteringIterable.skipTo(count))
        {
            count++;
        }

        Assert.assertEquals(COL1_COUNT, count);

        count = 25;
        try
        {
            OwIterable<Object> it = filteringIterable.skipTo(count);
            Assert.assertTrue(false == it.getHasMoreItems());
            Assert.assertTrue(it.iterator().hasNext() == false);
        }
        catch (Exception e)
        {
            Assert.fail("Should not throw exception, return empty Iterable instead");
        }
    }

    @SuppressWarnings("unused")
    @Test
    public void testGetPageNumItems()
    {
        OwObjectCollectionIterableAdapter<Object> iterable1 = new OwObjectCollectionIterableAdapter<Object>(collection1, 5);
        OwIterable<Object> filteringIterable = new OwFilteringIterable<Object>(iterable1, new OwItemFilter<Object>() {

            @Override
            public boolean accept(Object obj)
            {
                return (obj instanceof Integer);
            }
        }, 6l);
        long numInRoot = filteringIterable.getPageNumItems();
        OwIterable<Object> page = filteringIterable.getPage();
        long numByPageNumItems = page.getPageNumItems();
        long itemsInPage = 0l;
        for (Object obj : page)
        {
            itemsInPage++;
        }
        Assert.assertTrue(String.format("Iterable.getPageNumItems (%d) must be >= Iterable.getPage().iterator (%d) ", numInRoot, itemsInPage), numInRoot >= itemsInPage);
        Assert.assertTrue(String.format("Iterable.getPageNumItems (%d) must be >= Iterable.getPage().getPageNumItems (%d)", numInRoot, numByPageNumItems), numInRoot >= numByPageNumItems);
    }

    @SuppressWarnings("unused")
    @Ignore
    @Test
    public void foo() throws Exception
    {
        Collection<Integer> content = new HashSet<Integer>();
        for (int i = 0; i < 12; i++)
        {
            content.add(Integer.valueOf(i));
        }

        Iterable<Integer> iterable = new LinkedList<Integer>(content);

        Assert.assertFalse(iterable.iterator() == iterable.iterator());

        Iterator<Integer> it1 = iterable.iterator();
        Iterator<Integer> it2 = iterable.iterator();

        int count = 0;
        while (it1.hasNext())
        {
            Assert.assertEquals(it1.next(), it2.next());
            count++;
        }
    }
}
