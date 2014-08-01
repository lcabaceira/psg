package com.wewebu.expression.language;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

/**
*<p>
* OwExprValueTest. 
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
public class OwExprValueTest extends TestCase
{

    protected void assertRoundtripConversion(Object value_p, Class<?> javaType_p) throws OwExprEvaluationException
    {
        OwExprValue value = OwExprValue.fromJavaValue(value_p, javaType_p);
        Object javaValue = value.toJavaObject(value.getJavaType());
        assertTrue("" + javaValue.getClass() + " not instance of " + javaType_p, javaType_p.isAssignableFrom(javaValue.getClass()));
        assertEquals(value_p, javaValue);
    }

    public void testNumericRoundtrip() throws Exception
    {
        final Integer i = new Integer(299);
        final Double d = new Double(299.87);
        final Float f = new Float(299.87);
        final Short s = new Short((short) 105);
        final BigDecimal b = new BigDecimal("8876.3321");
        final BigInteger bi = new BigInteger("8876221234");

        assertRoundtripConversion(i, Integer.class);
        assertRoundtripConversion(d, Double.class);
        assertRoundtripConversion(s, Short.class);
        assertRoundtripConversion(f, Float.class);
        assertRoundtripConversion(b, BigDecimal.class);
        assertRoundtripConversion(bi, BigInteger.class);
    }

    public void testDateRoundtrip() throws Exception
    {
        final Date d = new Date();
        final Calendar c = Calendar.getInstance();

        assertRoundtripConversion(d, Date.class);
        assertRoundtripConversion(c, Calendar.class);
    }

}
