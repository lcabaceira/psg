package com.wewebu.expression.language;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *<p>
 * The system scope is the default top level scope. 
 * No named reference can be made to it. 
 * This makes so called domain-less or scope-less function calls and property references legal. <br/>
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
public class OwExprSystem extends OwExprReflectiveScope
{
    /**Expression language major version*/
    public static final int VERSION_MAJOR = 1;
    /**Expression language minor version*/
    public static final int VERSION_MINOR = 3;
    /**Expression language update version*/
    public static final int VERSION_UPDATE = 0;
    /**Expression language version string*/
    public static final String VERSION_STRING = "" + VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_UPDATE;

    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("##");

    public OwExprSystem()
    {
        super("system");
    }

    /**
     * The expression language <code>version():NUMERIC[3]</code> function implementation.
     * @return an <code>int</code> array containing the version numbers in the major,minor, update order 
     */
    public final int[] version()
    {
        return new int[] { VERSION_MAJOR, VERSION_MINOR, VERSION_UPDATE };
    }

    /**
     * The expression language <code>versionString</code> property implementation.
     * @return the expression language version String 
     */
    public final String getVersionString()
    {
        return VERSION_STRING;
    }

    /**
     * Array based item equality operator.<br>
     * Array objects are considered and compared accordingly.<br>
     * Example:<br> 
     * <code> 
     * arrayItemEquals(new Object[]{new Object[]{'a','b'}},new Object[]{new Object[]{'a','b'}})
     * </code> will return <code>true</code>
     * 
     * @param item_p
     * @param object_p
     * @return true if and only if the two objects are identical (arrays considered)
     * @since version 1.1.0 and AWD 3.1.0.0
     */
    public static boolean arrayItemEquals(Object item_p, Object object_p)
    {
        if (item_p != null && item_p.getClass().isArray())
        {
            if (object_p != null && object_p.getClass().isArray())
            {
                Object[] itemArray = (Object[]) item_p;
                Object[] objectArray = (Object[]) object_p;
                if (itemArray.length == objectArray.length)
                {
                    for (int i = 0; i < objectArray.length; i++)
                    {
                        if (!arrayItemEquals(itemArray[i], objectArray[i]))
                        {
                            return false;
                        }
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else if (item_p != null)
        {
            return item_p.equals(object_p);
        }
        else
        {
            return object_p == null;
        }

    }

    /**
     * Array item containment check method. 
     * The expression language <code>code(SCOPE,SCOPE)</code> function implementation.
     * @param array_p
     * @param object_p
     * @return <code>true</code> if and only if object_p is contained by the given objects array 
     * @since version 1.1.0 and AWD 3.1.0.0
     */
    public final boolean contains(Object[] array_p, Object object_p)
    {
        for (int i = 0; i < array_p.length; i++)
        {
            if (arrayItemEquals(array_p[i], object_p))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * The expression language <code>today()</code> function implementation.
     * @return the current date 
     */
    public final Calendar today()
    {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today;
    }

    /**
     * The expression language <code>date(NUMERIC:year,NUMERIC:month,NUMERIC:day):DATE</code> function implementation.
     * @param year_p
     * @param month_p
     * @param day_p
     * @return a date for the given parameters 
     */
    public Calendar date(int year_p, int month_p, int day_p)
    {
        return date(year_p, month_p, day_p, 0, 0, 0);
    }

    public Calendar date(int year_p, int month_p, int day_p, String timeZoneID_p)
    {
        return date(year_p, month_p, day_p, 0, 0, 0, timeZoneID_p);
    }

    public Calendar date(int year_p, int month_p, int day_p, int hour_p, int minute_p, int second_p)
    {
        return date(year_p, month_p, day_p, hour_p, minute_p, second_p, TimeZone.getDefault().getID());
    }

    public Calendar date(int year_p, int month_p, int day_p, int hour_p, int minute_p, int second_p, String timeZoneID_p)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZoneID_p));
        calendar.set(Calendar.YEAR, year_p);
        calendar.set(Calendar.MONTH, month_p - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day_p);
        calendar.set(Calendar.HOUR_OF_DAY, hour_p);
        calendar.set(Calendar.MINUTE, minute_p);
        calendar.set(Calendar.SECOND, second_p);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    /**
     * The expression language <code>date(STRING:date):DATE</code> function implementation.
     * @param date_p String date formated as  yyyy-MM-dd'T'HH:mm:ssZ
     * @return a Calendar for the given date_p parameter 
     */
    public Calendar date(String date_p) throws ParseException
    {
        String dateString = date_p;
        if (dateString.length() == 25 && dateString.charAt(22) == ':' && (dateString.charAt(19) == '+' || dateString.charAt(19) == '-'))
        {
            //found ':' including timezone offset
            dateString = dateString.substring(0, 22) + dateString.substring(23, 25);
        }
        String timeZoneString = dateString.substring(19);

        TimeZone GMT = TimeZone.getTimeZone("GMT");
        TimeZone timeZone = GMT;
        if (!timeZoneString.equals("GMT"))
        {
            timeZone = TimeZone.getTimeZone(timeZoneString);
            if (timeZone.equals(GMT))
            {
                //could understand the time zone , must be offset 
                if (timeZoneString.length() == 5)
                {
                    int timeSign = timeZoneString.charAt(0) == '-' ? -1 : 1;
                    String hourOffsetStr = timeZoneString.substring(1, 3);
                    String minuteOffsetStr = timeZoneString.substring(3, 5);

                    try
                    {
                        NUMBER_FORMAT.parse("02").intValue();
                        int hourOffset = NUMBER_FORMAT.parse(hourOffsetStr).intValue();
                        int minuteOffset = NUMBER_FORMAT.parse(minuteOffsetStr).intValue();
                        timeZone.setRawOffset(timeSign * (int) (hourOffset * OwExprTime.MILISECONDS_IN_HOUR + minuteOffset * OwExprTime.MILISECONDS_IN_MINUTE));
                    }
                    catch (ParseException e)
                    {
                        throw new ParseException("Invalid time zone offset : " + timeZoneString, 19);
                    }
                }
                else
                {
                    throw new ParseException("Invalid time zone : " + timeZoneString, 19);
                }
            }
        }

        TimeZone gmtRelative = TimeZone.getTimeZone("GMT");
        gmtRelative.setRawOffset(timeZone.getRawOffset());
        timeZone = gmtRelative;

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
        Calendar initCalendar = Calendar.getInstance();

        dateFormat.setCalendar(initCalendar);
        dateFormat.setTimeZone(timeZone);
        dateFormat.parse(dateString);
        Calendar calendar = dateFormat.getCalendar();
        Calendar calClone = (Calendar) calendar.clone();

        return calClone;

    }

    /**
     * The expression language <code>days(NUMERIC:timeInDays):TIME</code> function implementation.
     * @param days_p
     * @return a time span {@link OwExprTime} object for the given number of days
     */
    public final OwExprTime days(double days_p)
    {
        return OwExprTime.timeInDays(days_p);
    }

    public final OwExprTime seconds(int seconds_p)
    {
        return OwExprTime.seconds(seconds_p);
    }

    public final OwExprTime minutes(int minutes_p)
    {
        return OwExprTime.minutes(minutes_p);
    }

    public final OwExprTime hours(int hours_p)
    {
        return OwExprTime.hours(hours_p);
    }

    public final OwExprTime time(int hours_p, int minutes_p, int seconds_p)
    {
        return OwExprTime.time(hours_p, minutes_p, seconds_p);
    }

}
