package com.wewebu.ow.server.field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wewebu.ow.server.field.OwSort.OwSortCriteria;

public class OwSortTest
{
    public static final int TEST_MAX_SIZE = 4;
    private OwSort testBase;

    @Before
    public void setUp() throws Exception
    {
        testBase = new OwSort(TEST_MAX_SIZE, false);
        testBase.setCriteria("A", true);
        testBase.setCriteria("B", true);
        testBase.setCriteria("C", false);
        testBase.setCriteria("D", true);
    }

    @Test
    public void testGetMaxSize()
    {
        Assert.assertEquals(TEST_MAX_SIZE, testBase.getMaxSize());

        OwSort defaultMax = new OwSort();
        Assert.assertEquals(1, defaultMax.getMaxSize());
    }

    @Test
    public void testGetDefaultAsc()
    {
        Assert.assertEquals(false, testBase.getDefaultAsc());

        OwSort defaultMax = new OwSort();
        Assert.assertEquals(true, defaultMax.getDefaultAsc());
    }

    @Test
    public void testSetMaxSize()
    {
        Assert.assertEquals(TEST_MAX_SIZE, testBase.getMaxSize());

        int reduceTo = 2;
        testBase.setMaxSize(reduceTo);
        Assert.assertEquals(reduceTo, testBase.getMaxSize());
        Assert.assertEquals(reduceTo, testBase.getCriteriaCollection().size());
    }

    @Test
    public void testGetSize()
    {
        int currentSize = testBase.getSize();
        Assert.assertTrue(TEST_MAX_SIZE >= currentSize);

        Assert.assertTrue(currentSize == testBase.getCriteriaCollection().size());
    }

    @Test
    public void testGetCriteriaCollection()
    {
        Assert.assertNotNull(testBase.getCriteriaCollection());

        Assert.assertNotNull(new OwSort().getCriteriaCollection());
    }

    @Test
    public void testAddCriteria()
    {
        OwSortCriteria addCriteria = new OwSortCriteria("E", false);
        Assert.assertNull(testBase.getCriteria(addCriteria.getPropertyName()));

        testBase.addCriteria(addCriteria);
        Assert.assertNotNull(testBase.getCriteria(addCriteria.getPropertyName()));

        OwSort emptySort = new OwSort();
        Assert.assertNull(emptySort.getCriteria(addCriteria.getPropertyName()));

        emptySort.addCriteria(addCriteria);
        Assert.assertNotNull(emptySort.getCriteria(addCriteria.getPropertyName()));
    }

    @Test
    public void testRemoveCriteria()
    {
        int sortSize = testBase.getSize();
        int size = testBase.getCriteriaCollection().size();

        testBase.removeCriteria(System.currentTimeMillis() + "abc");
        Assert.assertEquals(sortSize, testBase.getSize());
        Assert.assertEquals(size, testBase.getCriteriaCollection().size());

        OwSortCriteria crit = testBase.getLastCriteria();
        testBase.removeCriteria(crit.getPropertyName());
        Assert.assertTrue(sortSize > testBase.getSize());
        Assert.assertTrue(size > testBase.getCriteriaCollection().size());
    }

    @Test
    public void testGetLastCriteria()
    {
        int size = testBase.getSize();
        Assert.assertNotNull(testBase.getLastCriteria());
        Assert.assertEquals(size, testBase.getSize());
    }

    @Test
    public void testGetCriteria()
    {
        Assert.assertNotNull(testBase);
        OwSortCriteria crit = testBase.getLastCriteria();
        Assert.assertNotNull(crit);

        Assert.assertEquals(crit, testBase.getCriteria(crit.getPropertyName()));
        testBase.removeCriteria(crit.getPropertyName());
        Assert.assertNull(testBase.getCriteria(crit.getPropertyName()));
    }

    @Test
    public void testToggleCriteria()
    {
        List<Boolean> bools = new LinkedList<Boolean>();
        LinkedList<String> names = new LinkedList<String>();

        Iterator<OwSortCriteria> it = testBase.getPrioritizedIterator();
        while (it.hasNext())
        {
            OwSortCriteria next = it.next();
            bools.add(Boolean.valueOf(next.getAscFlag()));
            names.add(next.getPropertyName());
        }

        for (String sortProp : names)
        {//when toggling the Order is changed
            testBase.toggleCriteria(sortProp);
        }

        it = testBase.getCriteriaCollection().iterator();
        Iterator<Boolean> itBool = bools.iterator();
        while (it.hasNext())
        {
            OwSortCriteria next = it.next();
            Boolean bool = itBool.next();
            Assert.assertEquals(!bool.booleanValue(), next.getAscFlag());
        }
    }

    @Test
    public void testGetPriority()
    {
        int size = testBase.getSize();
        Iterator<OwSortCriteria> it = testBase.getPrioritizedIterator();
        while (it.hasNext())
        {
            Assert.assertEquals(size--, testBase.getPriority(it.next()));
        }
    }

    @Test
    public void testSetCriteria()
    {
        String[] crits = { "g", "d", "f", "t" };
        boolean[] mapBool = { true, false, true, true };
        OwSort sort = new OwSort(crits.length, true);
        for (String crit : crits)
        {
            sort.setCriteria(crit, mapBool[sort.getSize()]);
        }

        Iterator<OwSortCriteria> it = sort.getCriteriaCollection().iterator();
        for (int i = 0; it.hasNext(); i++)
        {
            OwSortCriteria crit = it.next();
            Assert.assertEquals(crits[i], crit.getPropertyName());
            Assert.assertEquals(mapBool[i], crit.getAscFlag());
        }
    }

    @Test
    public void testGetPriorizedIterator()
    {
        Iterator<OwSortCriteria> itPrio = testBase.getPrioritizedIterator();
        ArrayList<OwSortCriteria> cpyLst = new ArrayList<OwSortCriteria>(testBase.getSize());
        while (itPrio.hasNext())
        {
            cpyLst.add(itPrio.next());
        }

        for (int i = 0; i < cpyLst.size() - 1; i++)
        {
            int prior = testBase.getPriority(cpyLst.get(i));
            Assert.assertTrue(prior > testBase.getPriority(cpyLst.get(i + 1)));
        }
    }

}
