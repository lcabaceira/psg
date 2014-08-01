package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.List;

import junit.framework.TestCase;

public class OwCMISSameTypeConverterTest extends TestCase
{
    public void testFromTo() throws Exception
    {
        OwCMISSameTypeConverter<Long> converter = new OwCMISSameTypeConverter<Long>(Long.class);

        Long v = 5679L;

        List<Long> nativeValues = converter.fromValue(v);

        Long owdValue = converter.toValue(nativeValues);

        assertEquals("Conversion from Value -> Native Value -> Value should yield the same value.", owdValue, v);

    }

    public void testArrayFromTo() throws Exception
    {
        OwCMISSameTypeConverter<Long> converter = new OwCMISSameTypeConverter<Long>(Long.class);

        Long v1 = 6741L;
        Long v2 = 6742L;

        List<Long> nativeValues = converter.fromArrayValue(new Object[] { v1, null, v2 });

        assertEquals(v1, nativeValues.get(0));
        assertNull(nativeValues.get(1));
        assertEquals(v2, nativeValues.get(2));

        Long[] owdValues = converter.toArrayValue(nativeValues);

        String message = "Conversion from Value -> Native Value -> Value should yield the same value.";
        assertEquals(message, v1, owdValues[0]);
        assertEquals(message, null, owdValues[1]);
        assertEquals(message, v2, owdValues[2]);

    }
}
