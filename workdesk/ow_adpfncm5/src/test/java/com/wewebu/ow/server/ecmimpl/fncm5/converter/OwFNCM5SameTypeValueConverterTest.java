package com.wewebu.ow.server.ecmimpl.fncm5.converter;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.filenet.api.collection.BooleanList;
import com.filenet.api.collection.Float64List;
import com.filenet.api.collection.Integer32List;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverter;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5SameTypeValueConverterTest.
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
public class OwFNCM5SameTypeValueConverterTest extends TestCase
{
    public void testSingleValue() throws OwException
    {
        OwFNCM5ValueConverter<Boolean, Boolean> booleanConverter = OwFNCM5SameTypeConverterClass.BOOLEAN_CONVERTER.createConverter(null);
        assertFalse(booleanConverter.fromNativeValue(false));
        assertTrue(booleanConverter.fromNativeValue(true));
        assertNull(booleanConverter.fromNativeValue(null));

        OwFNCM5ValueConverter<Integer, Integer> intConverter = OwFNCM5SameTypeConverterClass.INTEGER_CONVERTER.createConverter(null);
        assertEquals(new Integer(2341), intConverter.fromNativeValue(2341));
        assertEquals(Integer.valueOf(0), intConverter.fromNativeValue(0));
        assertNotSame(new Integer(789), intConverter.fromNativeValue(987));

    }

    public void testMultiValue() throws OwException
    {
        OwFNCM5ValueConverter<Boolean, Boolean> booleanConverter = OwFNCM5SameTypeConverterClass.BOOLEAN_CONVERTER.createConverter(null);
        OwFNCM5ValueConverter<Double, Double> doubleConverter = OwFNCM5SameTypeConverterClass.DOUBLE_CONVERTER.createConverter(null);
        OwFNCM5ValueConverter<Integer, Integer> intConverter = OwFNCM5SameTypeConverterClass.INTEGER_CONVERTER.createConverter(null);

        {
            BooleanList booleanList = OwFNCM5EngineListFactory.BooleanList.createList();
            Boolean[] owdArray = new Boolean[] { true, false };
            List<Boolean> owdList = Arrays.asList(owdArray);
            booleanList.addAll(owdList);
            Boolean[] convertedArray = booleanConverter.fromEngineCollection(booleanList);
            assertTrue("Unexpected conversion result : " + Arrays.toString(convertedArray), Arrays.equals(owdArray, convertedArray));
        }

        {
            BooleanList booleanList = OwFNCM5EngineListFactory.BooleanList.createList();
            Boolean[] owdArray = new Boolean[] { true, false };
            List<Boolean> owdList = Arrays.asList(owdArray);
            booleanList.addAll(owdList);
            Boolean[] convertedArray = booleanConverter.fromEngineCollection(booleanList);
            assertFalse("Unexpected conversion result : " + Arrays.toString(convertedArray), Arrays.equals(new Boolean[] { false, true }, convertedArray));
        }

        {
            Integer32List intList = OwFNCM5EngineListFactory.Integer32List.createList();
            Integer[] owdArray = new Integer[] { 11, 67 };
            List<Integer> owdList = Arrays.asList(owdArray);
            intList.addAll(owdList);
            Integer[] convertedArray = intConverter.fromEngineCollection(intList);
            assertTrue("Unexpected conversion result : " + Arrays.toString(convertedArray) + " when expecting : " + Arrays.toString(owdArray), Arrays.equals(owdArray, convertedArray));
        }

        {
            Float64List floatList = OwFNCM5EngineListFactory.Float64List.createList();
            Double[] owdArray = new Double[] { 11.12, 0.67 };
            List<Double> owdList = Arrays.asList(owdArray);
            floatList.addAll(owdList);
            Double[] convertedArray = doubleConverter.fromEngineCollection(floatList);
            assertFalse("Unexpected conversion result : " + Arrays.toString(convertedArray), Arrays.equals(new Double[] { 11.12, 0.67, 4.0 }, convertedArray));
        }

        {
            Float64List floatList = OwFNCM5EngineListFactory.Float64List.createList();
            Double[] owdArray = new Double[] {};
            List<Double> owdList = Arrays.asList(owdArray);
            floatList.addAll(owdList);
            Double[] convertedArray = doubleConverter.fromEngineCollection(floatList);
            assertTrue("Unexpected conversion result : " + Arrays.toString(convertedArray), Arrays.equals(owdArray, convertedArray));
        }

        {

            Double[] convertedArray = doubleConverter.fromEngineCollection(null);
            assertNull("Unexpected conversion result : " + Arrays.toString(convertedArray), convertedArray);
        }
    }
}
