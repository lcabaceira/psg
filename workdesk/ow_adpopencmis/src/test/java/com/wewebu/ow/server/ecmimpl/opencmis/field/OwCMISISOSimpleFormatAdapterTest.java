package com.wewebu.ow.server.ecmimpl.opencmis.field;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public class OwCMISISOSimpleFormatAdapterTest extends TestCase
{
    private static final Logger LOG = Logger.getLogger(OwCMISISOSimpleFormatAdapterTest.class);

    private OwCMISISOSimpleFormatAdapter format = null;

    @Override
    protected void setUp() throws Exception
    {
        format = new OwCMISISOSimpleFormatAdapter("-02:00");
    }

    public void testParse() throws Exception
    {

        assertParse("2013-10-17T07:30:22.123Z", 2013, 10, 17, 7, 30, 22, 123, "GMT");
        assertParse("2013-10-17T07:30:22.123+0200", 2013, 10, 17, 7, 30, 22, 123, "GMT+2");
        assertParse("2013-10-17T07:30:22.123+02:00", 2013, 10, 17, 7, 30, 22, 123, "GMT+2");
        assertParse("2013-05-02T19:20:30.009", 2013, 5, 2, 19, 20, 30, 9, "GMT-2");

        assertParse("2013-05-01T19:20:30.009", 2013, 5, 1, 19, 20, 30, 9, "GMT-2");
        assertParse("2013-05-01T19:20:30", 2013, 5, 1, 19, 20, 30, 0, "GMT-2");
        assertParse("2013-05-01T19:20:30Z", 2013, 5, 1, 19, 20, 30, 0, "GMT");
    }

    protected void assertParse(String dateString, int yy, int mm, int dd, int h, int m, int s, int ms, String zoneId) throws ParseException
    {
        Date date = (Date) format.parseObject(dateString);

        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        assertEquals("bad year", yy, year);
        assertEquals("bad month", mm, month);
        assertEquals("bad day", dd, day);
        assertEquals("bad hour", h, hour);
        assertEquals("bad day", m, minute);
        assertEquals("bad day", s, second);
        assertEquals("bad day", ms, millisecond);
    }

    protected void assertParseFail(String dateString)
    {
        try
        {
            format.parseObject(dateString);
            fail("Should not be able to parse " + dateString);
        }
        catch (ParseException e)
        {
            LOG.info("Failed to parse " + dateString, e);
        }
    }

    public void testParseError() throws Exception
    {
        assertParseFail("2013-05-01T19:20Z");
        assertParseFail("2013-05-01T19:20+01:00");
        assertParseFail("2013-05-01T19:20");
        assertParseFail("2013-05-01");
        assertParseFail("2013-05");
        assertParseFail("2013");
    }

    public void testFormat() throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2013);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 22);
        calendar.set(Calendar.MILLISECOND, 123);
        Date date = calendar.getTime();
        int offset = TimeZone.getDefault().getOffset(date.getTime());
        int offsetH = offset / (60 * 60 * 1000);
        String offsetStr = offsetH == 0 ? "Z" : (offsetH >= 0 ? "+" : "-") + (offsetH > 9 ? "" : "0") + offsetH + "00";
        assertEquals("2013-10-17T07:30:22.123" + offsetStr, format.format(calendar.getTime()));

    }
}
