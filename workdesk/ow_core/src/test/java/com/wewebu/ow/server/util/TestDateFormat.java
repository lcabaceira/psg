package com.wewebu.ow.server.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

public class TestDateFormat extends TestCase
{
    private String[] formats = { "F/MMMM/yyyy", "D/MM/yyyy", "EEEE d MMMM yyyy" };

    public void testDateFormat1() throws Exception
    {
        Calendar c = Calendar.getInstance();
        c.set(2010, 07, 14);
        Date d = c.getTime();
        System.out.println(d);
        SimpleDateFormat df;
        for (int i = 0; i < formats.length; i++)
        {
            df = new SimpleDateFormat(formats[i]);
            System.out.println(formats[i] + "\t" + df.format(d));
        }
        c.set(2010, 00, 01);
        d = c.getTime();
        df = new SimpleDateFormat(formats[1]);
        System.out.println(d);
        System.out.println(df.format(d));
        //
        c.set(2010, 07, 14);
        d = c.getTime();
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.ENGLISH);
        System.out.println(Arrays.asList(symbols.getWeekdays()));
        System.out.println(Arrays.asList(symbols.getMonths()));
        df = new SimpleDateFormat(formats[2]);
        System.out.println(d);
        System.out.println(df.format(d));
        //Date date = df.parse("Marţi 5 ianuarie 2010");
        //System.out.println(date);
    }

}
