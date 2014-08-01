package com.wewebu.ow.server.util;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public abstract class OwDateBasedTest extends TestCase
{
    private static final long MILLISECS_PER_DAY = 1000 * 60 * 60 * 24;

    protected Date newDate(int year_p, int month_p, int day_p, int hour_p, int minute_p, int second_p, int millisecond_p)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year_p);
        c.set(Calendar.MONTH, month_p - 1);
        c.set(Calendar.DAY_OF_MONTH, day_p);
        c.set(Calendar.HOUR_OF_DAY, hour_p);
        c.set(Calendar.MINUTE, hour_p);
        c.set(Calendar.SECOND, second_p);
        c.set(Calendar.MILLISECOND, millisecond_p);

        return c.getTime();
    }

    protected Date zero(Date date_p)
    {
        java.util.Calendar calendar = new java.util.GregorianCalendar();

        calendar.setTime(date_p);

        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();

    }

    protected long diffInDays(Date date1_p, Date date2_p, boolean dst_p)
    {

        long endL = date1_p.getTime();
        long startL = date2_p.getTime();
        if (!dst_p)
        {
            return (endL - startL) / MILLISECS_PER_DAY;
        }
        else
        {
            Calendar end = Calendar.getInstance();
            Calendar start = Calendar.getInstance();

            end.setTime(date1_p);
            start.setTime(date2_p);

            endL += end.getTimeZone().getOffset(endL);
            startL += start.getTimeZone().getOffset(startL);

            return (endL - startL) / MILLISECS_PER_DAY;
        }
    }
}
