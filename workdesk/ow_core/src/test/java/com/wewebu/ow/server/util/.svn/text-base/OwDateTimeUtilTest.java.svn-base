package com.wewebu.ow.server.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 *<p>
 * Date Time Util Test.
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
public class OwDateTimeUtilTest extends OwDateBasedTest
{
    private static final int GERMAN_SUMMER_TIME_OFFSET = -120 * 60 * 1000;
    private static final int GERMAN_WINTER_TIME_OFFSET = -60 * 60 * 1000;
    private static final int RO_SUMMER_TIME_OFFSET = -180 * 60 * 1000;
    private static final int RO_WINTER_TIME_OFFSET = -120 * 60 * 1000;
    private static final Logger LOG = Logger.getLogger(OwDateTimeUtilTest.class);
    private int roTimeOffset;
    private int deTimeOffset;

    @Override
    protected void setUp() throws Exception
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Bucharest"));
        TimeZone roTimeZone = TimeZone.getDefault();
        if (roTimeZone.inDaylightTime(new Date()))
        {
            roTimeOffset = RO_SUMMER_TIME_OFFSET;
            deTimeOffset = GERMAN_SUMMER_TIME_OFFSET;
        }
        else
        {
            roTimeOffset = RO_WINTER_TIME_OFFSET;
            deTimeOffset = GERMAN_WINTER_TIME_OFFSET;
        }
    }

    public void testRemoveTimeFormatTokens() throws Exception
    {

        final String[] testPatterns = { "MM/dd/yyyy hh:mm", "MM/dd/yyyy h:mm a", "MM/dd/yyyy h.mm a", "MM/dd/yyyy K.mm a", "MM/dd/yyyy K:mm a", "K:mm a", "MM/dd/yyyy" };

        Calendar theTestCalendar = Calendar.getInstance();
        theTestCalendar.set(Calendar.YEAR, 2009);
        theTestCalendar.set(Calendar.MONTH, 11 - 1);
        theTestCalendar.set(Calendar.DAY_OF_MONTH, 3);
        theTestCalendar.set(Calendar.HOUR_OF_DAY, 17);
        theTestCalendar.set(Calendar.MINUTE, 16);
        theTestCalendar.set(Calendar.SECOND, 15);

        Date theTestDate = theTestCalendar.getTime();

        final String[] timeElements = { "17", "5", "16", "15", "PM", "pm", "05" };

        for (int i = 0; i < testPatterns.length; i++)
        {
            String noTimeTokensPattern = OwDateTimeUtil.removeTimeFormatTokens(testPatterns[i]);
            try
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat(noTimeTokensPattern);

                String formatedDate = dateFormat.format(theTestDate);
                LOG.debug("No time token format " + testPatterns[i] + " -> " + noTimeTokensPattern + " = " + formatedDate);

                for (int j = 0; j < timeElements.length; j++)
                {
                    String assertMsg = "Found time element " + timeElements[j] + " in no time format " + noTimeTokensPattern + " from date format " + testPatterns[i];
                    assertTrue(assertMsg, formatedDate.indexOf(timeElements[j]) == -1);
                }
            }
            catch (IllegalArgumentException iae)
            {
                String msg = "Could not use format " + testPatterns[i] + " after removing time tokens -> " + noTimeTokensPattern;
                LOG.error(msg, iae);
                fail("Could not use format " + testPatterns[i] + " after removing time tokens -> " + noTimeTokensPattern);
            }
        }
    }

    public void testOffsetDay() throws Exception
    {
        Date september10th = newDate(2010, 9, 10, 0, 0, 0, 0);
        Date september10thPlus90 = OwDateTimeUtil.offsetDay(september10th, 90, true);

        assertEquals("No 90 DST days between " + september10th + " and " + september10thPlus90, 90, diffInDays(september10thPlus90, september10th, false));

        Date february10th = newDate(2010, 2, 10, 0, 0, 0, 0);
        Date february10thPlus90 = OwDateTimeUtil.offsetDay(february10th, 90, true);

        assertEquals("No 90 DST days between " + february10th + " and " + february10thPlus90, 90, diffInDays(february10thPlus90, february10th, true));

        //non DST time  
        Date september5th = newDate(2010, 9, 5, 0, 0, 0, 0);
        Date september5thPlus90 = OwDateTimeUtil.offsetDay(september5th, 90, false);

        assertEquals("No 90 absolute days between " + september5th + " and " + september5thPlus90, 90, diffInDays(september5thPlus90, september5th, false));

        //try an interval that is not affected by DST
        Date june5th = newDate(2010, 6, 5, 0, 0, 0, 0);
        Date june5thPlus90 = OwDateTimeUtil.offsetDay(june5th, 90, true);

        assertEquals("No 90 absolute days between " + june5th + " and " + june5thPlus90, 90, diffInDays(june5thPlus90, june5th, false));

    }

    protected Date date(int year_p, int month_p, int day_p, int hour_p, int minute_p, int secod_p, int millisecond_p)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MILLISECOND, millisecond_p);
        calendar.set(Calendar.SECOND, secod_p);
        calendar.set(Calendar.MINUTE, minute_p);
        calendar.set(Calendar.HOUR_OF_DAY, hour_p);

        calendar.set(Calendar.YEAR, year_p);
        calendar.set(Calendar.MONTH, month_p - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day_p);

        return calendar.getTime();
    }

    public void testConvert() throws Exception
    {
        TimeZone hongkongTZ = TimeZone.getTimeZone("Hongkong");
        TimeZone berlinTZ = TimeZone.getTimeZone("Europe/Berlin");
        TimeZone gmtTZ = TimeZone.getTimeZone("GMT");
        TimeZone bucharestTZ = TimeZone.getTimeZone("Europe/Bucharest");

        assertEquals(date(2011, 10, 17, 7, 32, 0, 0), OwDateTimeUtil.convert(date(2011, 10, 17, 6, 32, 0, 0), berlinTZ, bucharestTZ));
        assertEquals(date(2011, 10, 17, 4, 32, 0, 0), OwDateTimeUtil.convert(date(2011, 10, 17, 6, 32, 0, 0), berlinTZ, gmtTZ));
        assertEquals(date(2011, 10, 17, 12, 32, 0, 0), OwDateTimeUtil.convert(date(2011, 10, 17, 7, 32, 0, 0), bucharestTZ, hongkongTZ));
        assertEquals(date(2011, 10, 16, 20, 44, 21, 77), OwDateTimeUtil.convert(date(2011, 10, 17, 1, 44, 21, 77), hongkongTZ, bucharestTZ));

        //DST
        assertEquals(date(2011, 1, 20, 19, 44, 21, 77), OwDateTimeUtil.convert(date(2011, 1, 21, 1, 44, 21, 77), hongkongTZ, bucharestTZ));
    }
}
