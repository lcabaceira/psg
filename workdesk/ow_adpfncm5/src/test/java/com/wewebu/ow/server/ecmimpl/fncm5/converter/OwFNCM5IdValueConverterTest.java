package com.wewebu.ow.server.ecmimpl.fncm5.converter;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.filenet.api.collection.EngineCollection;
import com.filenet.api.collection.IdList;
import com.filenet.api.core.Factory;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverter;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5IdValueConverterTest.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public class OwFNCM5IdValueConverterTest extends TestCase
{
    private OwFNCM5ValueConverter<Id, String> converter;
    private OwFNCM5IdValueConverterClass converterClass;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        this.converterClass = new OwFNCM5IdValueConverterClass();
        this.converter = this.converterClass.createConverter(null);
    }

    public void testSingleValue() throws OwException
    {
        Id a = new Id("ABCDEF12-1111-2222-3333-ABCDEF123456");

        String ret = converter.fromNativeValue(a);
        assertEquals(a.toString(), ret);

        Id b = converter.toNativeValue(ret);
        assertTrue(a.equals(b));
    }

    @SuppressWarnings("rawtypes")
    public void testMultiValue() throws OwException
    {
        String[] arr = new String[] { "{1BCDEF12-1111-2222-3333-ABCDEF123456}", "{2BCDEF12-1111-2222-3333-ABCDEF123456}", "{3BCDEF12-1111-2222-3333-ABCDEF123456}" };

        EngineCollection l = converter.toEngineCollection(arr);
        assertTrue(arr.length == ((List<Id>) l).size());

        Iterator it = l.iterator();
        while (it.hasNext())
        {
            Id natId = (Id) it.next();
            assertTrue(natId.toString().equals("{1BCDEF12-1111-2222-3333-ABCDEF123456}") || natId.toString().equals("{2BCDEF12-1111-2222-3333-ABCDEF123456}") || natId.toString().equals("{3BCDEF12-1111-2222-3333-ABCDEF123456}"));
        }
    }

    public void testNativeSimulation() throws OwException
    {
        IdList nativeLst = Factory.IdList.createList();
        nativeLst.add(new Id("{1BCDEF12-1111-2222-3333-ABCDEF123456}"));
        nativeLst.add(new Id("{2BCDEF12-1111-2222-3333-ABCDEF123456}"));
        nativeLst.add(new Id("{3BCDEF12-1111-2222-3333-ABCDEF123456}"));

        String[] arr = converter.fromEngineCollection(nativeLst);
        assertNotNull(arr);
        assertEquals(nativeLst.size(), arr.length);

        for (int i = 0; i < arr.length; i++)
        {
            assertEquals(nativeLst.get(i).toString(), arr[i]);
        }
    }

    public void testNullValues() throws OwException
    {
        final String aNull = null;
        Id id = null;
        //        assertTrue(aNull == null);
        assertTrue(null == converter.toNativeValue(aNull));

        assertTrue(null == converter.fromNativeValue(id));

        assertTrue(null == converter.fromNativeValue(null));

        IdList test = Factory.IdList.createList();
        assertNotNull(converter.fromEngineCollection(test));
        assertEquals(0, converter.fromEngineCollection(test).length);

        id = new Id("{1BCDEF12-1111-2222-3333-ABCDEF123456}");
        String[] arr = new String[] { aNull, id.toString() };
        EngineCollection convertedCollection = converter.toEngineCollection(arr);
        assertEquals(arr.length, ((List<Id>) convertedCollection).size());
        test.add(aNull);
        test.add(aNull);
        test.add(aNull);
        assertNotNull(converter.fromEngineCollection(test));
        assertNotNull(converter.fromEngineCollection(test));

        test.add(id);
        arr = converter.fromEngineCollection(test);
        assertNotNull(arr);
        assertEquals(test.size(), arr.length);

    }

}
