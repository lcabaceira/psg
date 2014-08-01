package com.wewebu.ow.server.util;

import java.util.Calendar;
import java.util.Date;

import com.wewebu.ow.server.fieldctrlimpl.OwRelativeDate;

public class OwRelativeDateTest extends OwDateBasedTest
{

    protected void assertZeroDate(Date date_p)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date_p);
        assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
        assertEquals(0, c.get(Calendar.MILLISECOND));
    }

    public void testToday() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate today = new OwRelativeDate(OwRelativeDate.KEY_TODAY);

        assertZeroDate(today);
        assertEquals("" + now.toString() + " today is not " + today, 0, diffInDays(today, now, true));
    }

    public void testNextOneDay() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate nextOneDay = new OwRelativeDate(OwRelativeDate.KEY_NEXT_ONE_DAY);

        assertZeroDate(nextOneDay);
        assertEquals("" + now.toString() + " next day is not " + nextOneDay, 1, diffInDays(nextOneDay, now, true));
    }

    public void testNextTwoDay() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate nextTwoDays = new OwRelativeDate(OwRelativeDate.KEY_NEXT_TWO_DAYS);

        assertZeroDate(nextTwoDays);
        assertEquals("" + now.toString() + " next 2 days is not " + nextTwoDays, 2, diffInDays(nextTwoDays, now, true));
    }

    public void testNextOneWeek() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate nextOneWeek = new OwRelativeDate(OwRelativeDate.KEY_NEXT_ONE_WEEK);

        assertZeroDate(nextOneWeek);
        assertEquals("" + now.toString() + " next 1 week is not " + nextOneWeek, 7, diffInDays(nextOneWeek, now, true));
    }

    public void testTwoWeeksDay() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate nextTwoWeeks = new OwRelativeDate(OwRelativeDate.KEY_NEXT_TWO_WEEKS);

        assertZeroDate(nextTwoWeeks);
        assertEquals("" + now.toString() + " next 2 weeks is not " + nextTwoWeeks, 14, diffInDays(nextTwoWeeks, now, true));
    }

    public void testNext30Days() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate next30Days = new OwRelativeDate(OwRelativeDate.KEY_NEXT_30_DAYS);

        assertZeroDate(next30Days);
        assertEquals("" + now.toString() + " next 30 days is not " + next30Days, 30, diffInDays(next30Days, now, true));
    }

    public void testNext90Days() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate next90Days = new OwRelativeDate(OwRelativeDate.KEY_NEXT_90_DAYS);

        assertZeroDate(next90Days);
        assertEquals("" + now.toString() + " next 90 days is not " + next90Days, 90, diffInDays(next90Days, now, true));
    }

    public void testLastOneDay() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate lastOneDay = new OwRelativeDate(OwRelativeDate.KEY_LAST_ONE_DAY);

        assertZeroDate(lastOneDay);
        assertEquals("" + now.toString() + " last 1 day is not " + lastOneDay, 1, diffInDays(now, lastOneDay, true));
    }

    public void testLastTwoDay() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate lastTwoDays = new OwRelativeDate(OwRelativeDate.KEY_LAST_TWO_DAYS);

        assertZeroDate(lastTwoDays);
        assertEquals("" + now.toString() + " last 2 days is not " + lastTwoDays, 2, diffInDays(now, lastTwoDays, true));
    }

    public void testLastOneWeek() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate lastOneWeek = new OwRelativeDate(OwRelativeDate.KEY_LAST_ONE_WEEK);

        assertZeroDate(lastOneWeek);
        assertEquals("" + now.toString() + " last 1 week is not " + lastOneWeek, 7, diffInDays(now, lastOneWeek, true));
    }

    public void testLastTwoWeek() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate lastTwoWeeks = new OwRelativeDate(OwRelativeDate.KEY_LAST_TWO_WEEKS);

        assertZeroDate(lastTwoWeeks);
        assertEquals("" + now.toString() + " last 2 weeks is not " + lastTwoWeeks, 14, diffInDays(now, lastTwoWeeks, true));
    }

    public void testLast30Days() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate last30Days = new OwRelativeDate(OwRelativeDate.KEY_LAST_30_DAYS);

        assertZeroDate(last30Days);
        assertEquals("" + now.toString() + " last 30 days is not " + last30Days, 30, diffInDays(now, last30Days, true));
    }

    public void testLast90Days() throws Exception
    {
        Date now = zero(new Date());
        OwRelativeDate last90Days = new OwRelativeDate(OwRelativeDate.KEY_LAST_90_DAYS);

        assertZeroDate(last90Days);
        assertEquals("" + now.toString() + " last 90 days is not " + last90Days, 90, diffInDays(now, last90Days, true));
    }
}
