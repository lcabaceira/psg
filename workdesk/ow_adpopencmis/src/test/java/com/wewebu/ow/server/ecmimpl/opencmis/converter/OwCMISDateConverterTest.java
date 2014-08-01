package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;

public class OwCMISDateConverterTest extends TestCase
{
    public void testFromTo() throws Exception
    {
        TimeZone aClientTimezone = TimeZone.getTimeZone("GMT-6");
        Date currentDate = new Date();
        currentDate.setHours(13);
        currentDate.setMinutes(20);
        currentDate.setSeconds(0);

        OwCMISDateConverter converter = new OwCMISDateConverter(aClientTimezone);
        List<GregorianCalendar> nativeValues = converter.fromValue(currentDate);

        GregorianCalendar firstNativeValue = nativeValues.get(0);
        assertEquals(aClientTimezone, firstNativeValue.getTimeZone());

        Date owdValue = converter.toValue(nativeValues);
        assertEquals("Conversion from Value -> Native Value -> Value should yield the same value.", currentDate, owdValue);

        // Move to another time zone
        TimeZone aForeignTimezone = TimeZone.getTimeZone("GMT+4");
        converter = new OwCMISDateConverter(aForeignTimezone);
        Date owdValueInForeignTimeZone = converter.toValue(nativeValues);

        assertEquals(currentDate.getHours() + 10, owdValueInForeignTimeZone.getHours());
        assertEquals(currentDate.getMinutes(), owdValueInForeignTimeZone.getMinutes());
        assertEquals(currentDate.getSeconds(), owdValueInForeignTimeZone.getSeconds());
    }

    public void testArrayFromTo() throws Exception
    {
        TimeZone aClientTimezone = TimeZone.getTimeZone("GMT-6");
        Date date1 = new Date();
        date1.setHours(13);
        date1.setMinutes(20);
        date1.setSeconds(0);

        Date date2 = new Date();
        date2.setHours(14);
        date2.setMinutes(30);
        date2.setSeconds(0);

        OwCMISDateConverter converter = new OwCMISDateConverter(aClientTimezone);
        List<GregorianCalendar> nativeValues = converter.fromArrayValue(new Object[] { date1, null, date2 });

        GregorianCalendar firstNativeValue = nativeValues.get(0);
        assertEquals(aClientTimezone, firstNativeValue.getTimeZone());

        Date[] owdValues = converter.toArrayValue(nativeValues);
        String message = "Conversion from Value -> Native Value -> Value should yield the same value.";
        assertEquals(message, date1, owdValues[0]);
        assertEquals(message, null, owdValues[1]);
        assertEquals(message, date2, owdValues[2]);

        // Move to another time zone
        TimeZone aForeignTimezone = TimeZone.getTimeZone("GMT+4");
        converter = new OwCMISDateConverter(aForeignTimezone);
        Date[] owdValuesInForeignTimeZone = converter.toArrayValue(nativeValues);

        assertEquals(date1.getHours() + 10, owdValuesInForeignTimeZone[0].getHours());
        assertEquals(date1.getMinutes(), owdValuesInForeignTimeZone[0].getMinutes());
        assertEquals(date1.getSeconds(), owdValuesInForeignTimeZone[0].getSeconds());
    }
}
