package com.wewebu.ow.server.ecm;

import junit.framework.TestCase;

import com.wewebu.ow.server.mock.OwMockProperty;

public class OwPropertyComparatorTest extends TestCase
{
    public void testCompareNulls() throws Exception
    {
        OwPropertyComparator comparator = OwPropertyComparator.getInstance();
        OwMockProperty property = new OwMockProperty();
        property.setValue("non-null");
        OwMockProperty nullValueProperty = new OwMockProperty();
        nullValueProperty.setValue(null);

        assertEquals(0, comparator.compare(null, null));
        assertTrue(comparator.compare(null, property) > 0);
        assertTrue(comparator.compare(property, null) < 0);

        assertTrue(comparator.compare(property, nullValueProperty) < 0);
        assertTrue(comparator.compare(nullValueProperty, property) > 0);

        assertEquals(0, OwPropertyComparator.legacyCompare(null, null));
        assertTrue(OwPropertyComparator.legacyCompare(null, property) > 0);
        assertTrue(OwPropertyComparator.legacyCompare(property, null) < 0);

        assertTrue(OwPropertyComparator.legacyCompare(property, nullValueProperty) < 0);
        assertTrue(OwPropertyComparator.legacyCompare(nullValueProperty, property) > 0);
    }

    public void testCompareStrings() throws Exception
    {
        OwMockProperty p1 = new OwMockProperty();
        OwMockProperty p2 = new OwMockProperty();
        OwMockProperty p3 = new OwMockProperty();
        OwMockProperty p4 = new OwMockProperty();
        OwMockProperty p5 = new OwMockProperty();

        p1.setValue("AbC");
        p2.setValue("ABc");
        p3.setValue("Adc");
        p4.setValue("Abc");
        p5.setValue("Abc");

        OwPropertyComparator comparator = OwPropertyComparator.getInstance();

        assertTrue(comparator.compare(p1, p2) > 0);
        assertTrue(comparator.compare(p1, p3) < 0);
        assertTrue(comparator.compare(p2, p1) < 0);
        assertTrue(comparator.compare(p4, p1) > 0);
        assertEquals(0, comparator.compare(p4, p5));

        assertTrue(OwPropertyComparator.legacyCompare(p1, p2) > 0);
        assertTrue(OwPropertyComparator.legacyCompare(p1, p3) < 0);
        assertTrue(OwPropertyComparator.legacyCompare(p2, p1) < 0);
        assertTrue(OwPropertyComparator.legacyCompare(p4, p1) > 0);
        assertEquals(0, OwPropertyComparator.legacyCompare(p4, p5));

    }

    public void testCompareInts() throws Exception
    {
        OwMockProperty p1 = new OwMockProperty();
        OwMockProperty p2 = new OwMockProperty();
        OwMockProperty p3 = new OwMockProperty();
        OwMockProperty p4 = new OwMockProperty();

        p1.setValue(1);
        p2.setValue(2);
        p3.setValue(-1);
        p4.setValue(-1);

        OwPropertyComparator comparator = OwPropertyComparator.getInstance();

        assertTrue(comparator.compare(p1, p2) < 0);
        assertTrue(comparator.compare(p1, p3) > 0);
        assertEquals(0, comparator.compare(p3, p4));

        assertTrue(OwPropertyComparator.legacyCompare(p1, p2) < 0);
        assertTrue(OwPropertyComparator.legacyCompare(p1, p3) > 0);
        assertEquals(0, OwPropertyComparator.legacyCompare(p3, p4));

    }

    public void testCompare() throws Exception
    {
        OwMockProperty p1 = new OwMockProperty();
        OwMockProperty p2 = new OwMockProperty();

        p1.setValue("3411");
        p2.setValue(3411);

        OwPropertyComparator comparator = OwPropertyComparator.getInstance();

        assertEquals(1, comparator.compare(p1, p2));

        assertEquals(1, OwPropertyComparator.legacyCompare(p1, p2));

    }
}
