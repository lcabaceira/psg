package com.wewebu.ow.server.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.wewebu.ow.server.util.paramcodec.OwParameterMap;

public class OwParameterMapTest extends TestCase
{
    private OwParameterMap parameterMap;

    protected void setUp() throws Exception
    {
        super.setUp();
        parameterMap = new OwParameterMap();
    }

    public void testNewOwParameterMap() throws Exception
    {
        Map testMap = new HashMap();
        testMap.put("K1", new String[] { "K1V1", "K1V2" });
        testMap.put("K2", new String[] { "K2V1", "K2V2" });

        OwParameterMap newMap = new OwParameterMap(testMap);

        Set names = newMap.getParameterNames();
        assertTrue(names.contains("K1"));
        assertTrue(names.contains("K2"));
        assertFalse(names.contains("K3"));

        String k1Value = newMap.getParameter("K1");
        assertEquals("K1V1", k1Value);

        String[] k2Values = newMap.getParameterValues("K2");
        assertEquals(2, k2Values.length);
        assertEquals("K2V2", k2Values[1]);

        String k2Value = newMap.getParameter("K3");
        assertNull(k2Value);
    }

    public void testToRequestQueryString()
    {
        parameterMap.setParameter("p1", "P1V1");
        parameterMap.setParameter("p1", "P1V2");
        parameterMap.setParameter("p2", "P2V1");
        parameterMap.setParameter("p3", "P3V1");
        parameterMap.setParameter("p3", null);

        String qs = parameterMap.toRequestQueryString();
        assertTrue(qs.indexOf("p1=P1V1") != -1);
        assertTrue(qs.indexOf("p1=P1V2") != -1);
        assertTrue(qs.indexOf("p2=P2V1") != -1);
    }

    public void testToRequestParametersMap()
    {
        Map testMap = new HashMap();
        testMap.put("K1", new String[] { "K1V1", "K1V2" });
        testMap.put("K2", new String[] { "K2V1", "K2V2" });

        parameterMap.setParameter("K1", "K1V1");
        parameterMap.setParameter("K1", "K1V2");
        parameterMap.setParameter("K2", "K2V1");
        parameterMap.setParameter("K2", "K2V2");

        //        assertEquals(testMap, parameterMap.toRequestParametersMap());
    }

    public void testAddAll() throws Exception
    {
        Map m1 = new HashMap();
        Map m2 = new HashMap();

        m1.put("k1", new String[] { "m1k1v1", "m1k1v2" });
        m1.put("k2", new String[] { "m1k2v1" });

        m2.put("k1", new String[] { "m2k1v1", "m2k1v2" });
        m2.put("k2", new String[] { "m2k2v1" });
        m2.put("k3", new String[] { "m2k3v1" });

        parameterMap.addAll(m1);

        parameterMap.addAll(m2);

        String[] k1Values = parameterMap.getParameterValues("k1");
        List k1ValuesList = Arrays.asList(k1Values);

        assertTrue(k1ValuesList.contains("m2k1v1"));
        assertTrue(k1ValuesList.contains("m1k1v1"));

        String[] k3Values = parameterMap.getParameterValues("k3");

        assertEquals(1, k3Values.length);
    }
}
