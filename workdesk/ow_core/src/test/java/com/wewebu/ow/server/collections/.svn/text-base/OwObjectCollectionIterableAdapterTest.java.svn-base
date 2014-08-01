package com.wewebu.ow.server.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;

public class OwObjectCollectionIterableAdapterTest extends TestCase
{
    private static final int ITEM_COUNT = 20;

    private OwObjectCollection testCollection;
    private OwIterable<Integer> testAdapter;

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        this.testCollection = new OwStandardObjectCollection();

        for (int i = 0; i < ITEM_COUNT; i++)
        {
            this.testCollection.add(new Integer(i));
        }

        this.testAdapter = new OwObjectCollectionIterableAdapter<Integer>(this.testCollection);
    }

    public void testSize() throws Exception
    {
        assertEquals(this.testCollection.size(), this.testAdapter.getTotalNumItems());
        assertEquals(10, this.testAdapter.getPageNumItems());
        assertTrue(this.testAdapter.getHasMoreItems());

        OwIterable<Integer> startingFrom10 = this.testAdapter.skipTo(10);
        assertEquals(20, startingFrom10.getTotalNumItems());
        assertEquals(10, startingFrom10.getPageNumItems());
        assertFalse(startingFrom10.getHasMoreItems());
    }

    public void testSkip() throws Exception
    {
        OwIterable<Integer> startingFrom10 = this.testAdapter.skipTo(10);
        assertNotNull(startingFrom10);
        Iterator<Integer> iteratorFrom10 = startingFrom10.iterator();
        assertEquals(Integer.valueOf(10), iteratorFrom10.next());
    }

    public void testHasMoreItems() throws Exception
    {
        OwIterable<Integer> page = this.testAdapter.skipTo(10).getPage(5);
        assertTrue(page.getHasMoreItems());

        page = this.testAdapter.skipTo(14).getPage(5);
        assertTrue(page.getHasMoreItems());

        page = this.testAdapter.skipTo(15).getPage(5);
        assertFalse(page.getHasMoreItems());
    }

    public void testIterator() throws Exception
    {
        Iterator<Integer> iterator = this.testAdapter.iterator();
        assertNotNull(iterator);
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
    }

    public void testPage() throws Exception
    {
        OwIterable<Integer> page = this.testAdapter.getPage();
        assertNotNull(page);

        Iterator<Integer> it = page.iterator();
        int i = 0;
        while (it.hasNext())
        {
            it.next();
            i++;
        }

        assertEquals(10, i);
        try
        {
            it.next();
            fail("It should throw a NoSuchElementException");
        }
        catch (NoSuchElementException e)
        {
            //ok
        }

        page = this.testAdapter.skipTo(4).getPage();
        assertEquals(Integer.valueOf(4), page.iterator().next());

        it = page.iterator();
        i = 0;
        while (it.hasNext())
        {
            it.next();
            i++;
        }

        assertEquals(10, i);
    }

    public void testPageIteration() throws Exception
    {
        assertTrue(this.testAdapter.getHasMoreItems());

        OwIterable<Integer> page0 = this.testAdapter.skipTo(5).getPage();
        assertTrue(page0.getHasMoreItems());

        OwIterable<Integer> page1 = this.testAdapter.skipTo(10).getPage();
        assertFalse(page1.getHasMoreItems());
    }
}
