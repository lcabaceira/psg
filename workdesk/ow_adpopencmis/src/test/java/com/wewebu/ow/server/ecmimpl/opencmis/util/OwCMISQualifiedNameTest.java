package com.wewebu.ow.server.ecmimpl.opencmis.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

import junit.framework.TestCase;

public class OwCMISQualifiedNameTest extends TestCase
{
    public void testMultipleNamespaces() throws Exception
    {
        OwCMISQualifiedName qualifiedName = new OwCMISQualifiedName("foo", "bar");
        assertEquals("foo", qualifiedName.getNamespace());
        assertEquals("bar", qualifiedName.getName());

        qualifiedName = new OwCMISQualifiedName("foo.bar");
        assertEquals("foo", qualifiedName.getNamespace());
        assertEquals("bar", qualifiedName.getName());
    }

    public void testHashCode()
    {
        String qName = "foo.bar";
        OwCMISQualifiedName qualifiedName = new OwCMISQualifiedName(qName);
        assertEquals("foo", qualifiedName.getNamespace());
        assertEquals("bar", qualifiedName.getName());

        assertEquals(qName.hashCode(), qualifiedName.hashCode());
    }

    public void testToStringHashCode()
    {
        String qName = "foo.bar";
        OwCMISQualifiedName qualifiedName = new OwCMISQualifiedName(qName);
        assertEquals("foo", qualifiedName.getNamespace());
        assertEquals("bar", qualifiedName.getName());

        assertEquals(qName, qualifiedName.toString());
        assertEquals(qName.hashCode(), qualifiedName.toString().hashCode());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testMapUsage()
    {
        String qName = "foo.bar";
        OwCMISQualifiedName qualifiedName = new OwCMISQualifiedName(qName);
        HashMap map = new HashMap();
        LinkedHashMap linkedMap = new LinkedHashMap();
        assertTrue(map.size() == linkedMap.size());
        map.put(qualifiedName, qName);
        linkedMap.put(qName, qualifiedName);
        assertEquals(1, map.size());
        assertEquals(1, linkedMap.size());

        assertEquals(qName.hashCode(), qualifiedName.hashCode());

        assertFalse("Should not contains the same key", map.containsKey(qName));
        assertFalse("Should not contains the same key", linkedMap.containsKey(qualifiedName));

        map.put(qName, qualifiedName);
        linkedMap.put(qualifiedName, qName);
        assertEquals(2, map.size());
        assertEquals(2, linkedMap.size());

        assertTrue(map.containsKey(qualifiedName));//contains the previous added object
        assertTrue(linkedMap.containsKey(qName));//contains the previous added object
    }
}
