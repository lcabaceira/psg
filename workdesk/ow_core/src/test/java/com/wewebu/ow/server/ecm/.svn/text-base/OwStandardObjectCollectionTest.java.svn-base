package com.wewebu.ow.server.ecm;

import junit.framework.TestCase;

import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.mock.OwMockObject;
import com.wewebu.ow.server.mock.OwMockProperty;
import com.wewebu.ow.server.mock.OwMockPropertyClass;

public class OwStandardObjectCollectionTest extends TestCase
{

    protected OwMockObject createMockObject(String[] propertyNames, String[] propertyValues) throws Exception
    {
        OwMockObject mockObject = new OwMockObject(getClass().toString());
        OwPropertyCollection propertyCollection = new OwStandardPropertyCollection();

        for (int i = 0; i < propertyNames.length; i++)
        {
            OwMockPropertyClass propertyClass = new OwMockPropertyClass();
            propertyClass.setClassName(propertyNames[i]);
            propertyClass.setJavaClassName(String.class.getName());

            OwMockProperty property = new OwMockProperty();
            property.setValue(propertyValues[i]);
            property.setPropertyClass(propertyClass);
            property.setFieldDefinition(propertyClass);

            propertyCollection.put(propertyNames[i], property);
        }

        mockObject.setProperties(propertyCollection, null);

        return mockObject;
    }

    public void testSort5() throws Exception
    {

        OwMockObject mockObject1 = createMockObject(new String[] { "P1", "P2" }, new String[] { "B", "B" });
        OwMockObject mockObject2 = createMockObject(new String[] { "P1", "P2" }, new String[] { "C", "B" });
        OwMockObject mockObject5 = createMockObject(new String[] { "P1", "P2" }, new String[] { "E", "B" });
        OwMockObject mockObject3 = createMockObject(new String[] { "P1", "P2" }, new String[] { "A", "B" });
        OwMockObject mockObject4 = createMockObject(new String[] { "P1", "P2" }, new String[] { "D", "A" });

        OwSort sort = new OwSort();
        sort.addCriteria(new OwSortCriteria("P1", true));
        sort.addCriteria(new OwSortCriteria("P2", true));

        OwObjectCollection objectCollection = new OwStandardObjectCollection();
        objectCollection.add(mockObject2);
        objectCollection.add(mockObject3);
        objectCollection.add(mockObject1);
        objectCollection.add(mockObject4);
        objectCollection.add(mockObject5);

        objectCollection.sort(sort);

        assertTrue(objectCollection.get(0) == mockObject4);
        assertTrue(objectCollection.get(1) == mockObject3);
        assertTrue(objectCollection.get(2) == mockObject1);
        assertTrue(objectCollection.get(3) == mockObject2);
        assertTrue(objectCollection.get(4) == mockObject5);
    }

    public void testSort8() throws Exception
    {

        OwMockObject mockObject1 = createMockObject(new String[] { "P1", "P2" }, new String[] { "A", "B" });
        OwMockObject mockObject2 = createMockObject(new String[] { "P1", "P2" }, new String[] { "B", "B" });
        OwMockObject mockObject3 = createMockObject(new String[] { "P1", "P2" }, new String[] { "C", "B" });
        OwMockObject mockObject4 = createMockObject(new String[] { "P1", "P2" }, new String[] { "D", "B" });
        OwMockObject mockObject5 = createMockObject(new String[] { "P1", "P2" }, new String[] { "E", "B" });
        OwMockObject mockObject6 = createMockObject(new String[] { "P1", "P2" }, new String[] { "F", "A" });
        OwMockObject mockObject7 = createMockObject(new String[] { "P1", "P2" }, new String[] { "G", "A" });
        OwMockObject mockObject8 = createMockObject(new String[] { "P1", "P2" }, new String[] { "H", "A" });

        OwSort sort = new OwSort();
        sort.addCriteria(new OwSortCriteria("P1", true));
        sort.addCriteria(new OwSortCriteria("P2", true));

        OwObjectCollection objectCollection = new OwStandardObjectCollection();
        objectCollection.add(mockObject2);
        objectCollection.add(mockObject3);
        objectCollection.add(mockObject1);
        objectCollection.add(mockObject4);
        objectCollection.add(mockObject5);
        objectCollection.add(mockObject6);
        objectCollection.add(mockObject7);
        objectCollection.add(mockObject8);

        objectCollection.sort(sort);

        System.out.println(objectCollection);

        assertTrue(objectCollection.get(0) == mockObject6);
        assertTrue(objectCollection.get(1) == mockObject7);
        assertTrue(objectCollection.get(2) == mockObject8);
        assertTrue(objectCollection.get(3) == mockObject1);
        assertTrue(objectCollection.get(4) == mockObject2);
        assertTrue(objectCollection.get(5) == mockObject3);
        assertTrue(objectCollection.get(6) == mockObject4);
        assertTrue(objectCollection.get(7) == mockObject5);

    }

    public void testIsComplete() throws Exception
    {
        OwStandardObjectCollection col = new OwStandardObjectCollection();
        assertTrue(col.isComplete());

        col.setComplete(true);
        assertTrue(col.isComplete());

        assertEquals(Boolean.TRUE, col.getAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE));

        String isComplete = "False";
        col.setAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, isComplete);

        assertEquals(isComplete, col.getAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE));

        assertFalse(col.isComplete());
    }
}
