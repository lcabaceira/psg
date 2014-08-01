package com.wewebu.ow.server.util;

import java.io.Writer;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.wewebu.ow.server.app.OwEditablePropertyDate;
import com.wewebu.ow.server.app.OwMainAppContext;

/**
 *<p>
 * Utility class for manipulating date and time.
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
public class OwDateTimeUtil
{
    /** number of days in a week*/
    public static final int NUMBER_OF_WEEKDAYS = 7;
    /** number of months in year*/
    public static final int NUMBER_OF_MONTHS = 12;

    /** set the time of the given date to the begin of a day i.e. 00:00:00
     * 
     * @param date_p Date
     * @return Date
     */
    public static Date setBeginOfDayTime(Date date_p)
    {
        java.util.Calendar calendar = new java.util.GregorianCalendar();

        calendar.setTime(date_p);

        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
    }

    /** set the time of the given date to the begin of the NEXT day i.e. 00:00:00
     * 
     * @param date_p Date
     * @return Date
     */
    public static Date setBeginOfNextDayTime(Date date_p)
    {
        java.util.Calendar calendar = new java.util.GregorianCalendar();

        calendar.setTime(date_p);

        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /** Sets a day offset in absolute time (no DST adjustment).
     * 
     * @param iDays_p int 
     * @return Date
     */
    public static Date offsetDay(Date date_p, int iDays_p)
    {
        return offsetDay(date_p, iDays_p, false);
    }

    /**
     * Sets a day offset in absolute time with the possibility of  Daylight Savings Time adjustment.
     * @param date_p base Date 
     * @param iDays_p number of days to add to the base date (can be a negative number)
     * @param dst_p if <code>true</code> a DST adjustment will be performed<br> 
     *              if <code>false</code> absolute time will be used  
     * @return a {@link Date} before of after the given date with the specified amount of days
     * @since 3.1.0.0
     */
    public static Date offsetDay(Date date_p, int iDays_p, boolean dst_p)
    {

        long daysMillis = iDays_p * (1000L * 3600 * 24);
        if (dst_p)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(date_p);

            TimeZone timeZone = c.getTimeZone();

            int startOffset = timeZone.getOffset(c.getTimeInMillis());
            long time = c.getTimeInMillis() + daysMillis;
            int endOffset = timeZone.getOffset(time);

            time += startOffset - endOffset;
            return new Date(time);
        }
        else
        {
            long time = date_p.getTime();
            return new Date(time + daysMillis);
        }
    }

    /** set the time of the given date to the end of a day i.e. 23:59:59
     * 
     * @param date_p Date
     * @return Date
     */
    public static Date setEndOfDayTime(Date date_p)
    {
        java.util.Calendar calendar = new java.util.GregorianCalendar();

        calendar.setTime(date_p);

        calendar.set(java.util.Calendar.SECOND, 59);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);

        return calendar.getTime();
    }

    /** remove all time formating from the given date format string
     *  
     *  e.g.: "dd.MM.yyyy (HH:mm)" becomes "dd.MM.yyyy"
     *  
     * @return date format String e.g.: "dd.MM.yyyy"
     */
    public static String removeTimeFormatTokens(String sDateFormat_p)
    {
        final String[] timeTokens = new String[] { "HH:mm:ss", "HH.mm.ss", "HH-mm-ss", "HH:mm", "hh:mm", "hh.mm", "HH.mm", "HH-mm", "h.mm", "K.mm", "h:mm", "K:mm", "HH", "mm", "ss", "()", "a" };
        return removeTokens(sDateFormat_p, timeTokens);
    }

    /** 
     * Remove all date formating from the given date-format string 
     * @param sDateFormat_p the date format to be cleared of date tokens 
     * @return date format String e.g.: "HH:mm:ss"
     */
    public static String removeDateFormatTokens(String sDateFormat_p)
    {
        final String[] dateTokens = new String[] { "yyyy.MM.dd" };
        return removeTokens(sDateFormat_p, dateTokens);
    }

    /**
     * Removes tokens from a given string
     * @param aString_p String to remove tokens from
     * @param tokens_p the tokens to remove from aString
     * @return the given string (aString) with the tokens removed 
     */
    private static String removeTokens(String aString_p, String[] tokens_p)
    {
        for (int i = 0; i < tokens_p.length; i++)
        {
            aString_p = OwString.replaceAll(aString_p, tokens_p[i], "");
        }
        return aString_p.trim();
    }

    /**
     * Get the month names. Returns an array of 13 elements, see
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4146173 for explanations
     * @param locale_p - the {@link Locale} object
     * @return the month names.
     * @since 3.0.0.0
     */
    public static String[] getMonthNames(java.util.Locale locale_p)
    {
        String[] months = new String[NUMBER_OF_MONTHS + 1];

        months[0] = OwString.localize(locale_p, "app.OwEditablePropertyDate.January", "January");
        months[1] = OwString.localize(locale_p, "app.OwEditablePropertyDate.February", "February");
        months[2] = OwString.localize(locale_p, "app.OwEditablePropertyDate.March", "March");
        months[3] = OwString.localize(locale_p, "app.OwEditablePropertyDate.April", "April");
        months[4] = OwString.localize(locale_p, "app.OwEditablePropertyDate.May", "May");
        months[5] = OwString.localize(locale_p, "app.OwEditablePropertyDate.June", "June");
        months[6] = OwString.localize(locale_p, "app.OwEditablePropertyDate.July", "July");
        months[7] = OwString.localize(locale_p, "app.OwEditablePropertyDate.August", "August");
        months[8] = OwString.localize(locale_p, "app.OwEditablePropertyDate.September", "September");
        months[9] = OwString.localize(locale_p, "app.OwEditablePropertyDate.October", "OCtober");
        months[10] = OwString.localize(locale_p, "app.OwEditablePropertyDate.November", "November");
        months[11] = OwString.localize(locale_p, "app.OwEditablePropertyDate.December", "December");
        months[12] = "";
        return months;
    }

    /**
     * Get the short names for months of the year.
     * @param locale_p
     * @return an array of {@link String} objects, representing the short names for months
     * @since 3.0.0.0
     */
    public static String[] getShortMonthNames(java.util.Locale locale_p)
    {
        String[] monthNames = getMonthNames(locale_p);
        return shortenStrings(monthNames);
    }

    /**
     * Utility method, used to shorten month names and week day names.
     * @param stringsToBeShorthened_p - the original array of {@link String} objects.
     * @return an array of {@link String} objects, each of them having maximum size of 3 characters.
     * @since 3.0.0.0
     */
    private static String[] shortenStrings(String[] stringsToBeShorthened_p)
    {
        String[] shortenedStrings = new String[stringsToBeShorthened_p.length];
        for (int i = 0; i < stringsToBeShorthened_p.length; i++)
        {
            if (stringsToBeShorthened_p[i].length() > 3)
            {
                shortenedStrings[i] = stringsToBeShorthened_p[i].substring(0, 3);
            }
            else
            {
                shortenedStrings[i] = stringsToBeShorthened_p[i];
            }
        }
        return shortenedStrings;
    }

    /**
     * Get the weekdays, as they are configured in the locale files.
     * The returned array has 8 elements, see explanations at
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4146173
     * @param locale_p - the {@link Locale} object
     * @return an array of {@link String} object, representing the names of the week days, for the given {@link Locale} object. 
     */
    public static String[] getWeekDays(Locale locale_p)
    {
        String[] weekDayNames = new String[NUMBER_OF_WEEKDAYS + 1];
        weekDayNames[0] = "";
        weekDayNames[1] = OwString.localize(locale_p, "app.OwEditablePropertyDate.Sunday", "Sonntag");
        weekDayNames[2] = OwString.localize(locale_p, "app.OwEditablePropertyDate.Monday", "Montag");
        weekDayNames[3] = OwString.localize(locale_p, "app.OwEditablePropertyDate.Tuesday", "Dienstag");
        weekDayNames[4] = OwString.localize(locale_p, "app.OwEditablePropertyDate.Wednesday", "Mittwoch");
        weekDayNames[5] = OwString.localize(locale_p, "app.OwEditablePropertyDate.Thursday", "Donnerstag");
        weekDayNames[6] = OwString.localize(locale_p, "app.OwEditablePropertyDate.Friday", "Freitag");
        weekDayNames[7] = OwString.localize(locale_p, "app.OwEditablePropertyDate.Saturday", "Samstag");

        return weekDayNames;
    }

    /**
     * Get the shorten names for week days.
     * @param locale_p - the {@link Locale} object.
     * @return an array of {@link String} object, representing the names of the week days, for the given {@link Locale} object.
     */
    public static String[] getShortWeekDays(Locale locale_p)
    {
        return shortenStrings(getWeekDays(locale_p));
    }

    /**
     * Create a {@link SimpleDateFormat} object. 
     * Use this method, to ensure that the date is parsed or formatted on the same manner during 
     * {@link OwEditablePropertyDate#insertEditHTML(OwMainAppContext, Locale, Writer, Date, String, boolean, boolean, String)} and
     * {@link OwEditablePropertyDate#update(Locale, HttpServletRequest)} method calls.
     * @param locale_p - the {@link Locale} object
     * @param sDateFormat_p - the {@link String} object representing the date format.
     * @return a configured {@link SimpleDateFormat} object.
     * @since 3.0.0.0
     */
    public static SimpleDateFormat createDateFromat(java.util.Locale locale_p, String sDateFormat_p)
    {
        // use DateFormatSymbols in order to allow user to override the default locale symbols
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale_p);
        dateFormatSymbols.setMonths(getMonthNames(locale_p));
        dateFormatSymbols.setShortMonths(getShortMonthNames(locale_p));
        dateFormatSymbols.setWeekdays(getWeekDays(locale_p));
        dateFormatSymbols.setShortWeekdays(getShortWeekDays(locale_p));
        SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat_p, dateFormatSymbols);
        sdf.setLenient(false);
        return sdf;
    }

    /**
     * Converts a given {@link Date} represented time stamp from a time zone to another.
     * Although the input parameters and the return value represent dates in different time zones both the given date 
     * and the returned date will be expressed in the current time zone (see {@link Date} time zone behavior in Java).
     * @param date_p date to be converted 
     * @param fromTimeZone_p time zone the date is converted from
     * @param toTimeZone_p time zone the date is converted to
     * @return the converted date 
     * @since 3.1.0.3
     */
    public static Date convert(Date date_p, TimeZone fromTimeZone_p, TimeZone toTimeZone_p)
    {
        Date result = null;
        if (date_p != null)
        {
            result = convertToCalendar(date_p, fromTimeZone_p, toTimeZone_p).getTime();
        }
        return result;
    }

    /**
     * Convert a date into Calendar representation with specific time zone.
     * @param date_p Date (can be null)
     * @param fromTimeZone_p TimeZone of the provided date
     * @param toTimeZone_p TimeZone of resulting Calendar
     * @return Calendar with specific calendar, or null if provided date is null
     * @since 3.2.0.0
     */
    public static Calendar convertToCalendar(Date date_p, TimeZone fromTimeZone_p, TimeZone toTimeZone_p)
    {
        if (date_p == null)
        {
            return null;
        }
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date_p);

        Calendar from = Calendar.getInstance(fromTimeZone_p);

        from.set(Calendar.MILLISECOND, dateCalendar.get(Calendar.MILLISECOND));
        from.set(Calendar.SECOND, dateCalendar.get(Calendar.SECOND));
        from.set(Calendar.MINUTE, dateCalendar.get(Calendar.MINUTE));
        from.set(Calendar.HOUR_OF_DAY, dateCalendar.get(Calendar.HOUR_OF_DAY));

        from.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
        from.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
        from.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar to = Calendar.getInstance(toTimeZone_p);
        to.setTime(from.getTime());

        Calendar resultCalendar = Calendar.getInstance();

        resultCalendar.set(Calendar.MILLISECOND, to.get(Calendar.MILLISECOND));
        resultCalendar.set(Calendar.SECOND, to.get(Calendar.SECOND));
        resultCalendar.set(Calendar.MINUTE, to.get(Calendar.MINUTE));
        resultCalendar.set(Calendar.HOUR_OF_DAY, to.get(Calendar.HOUR_OF_DAY));

        resultCalendar.set(Calendar.YEAR, to.get(Calendar.YEAR));
        resultCalendar.set(Calendar.MONTH, to.get(Calendar.MONTH));
        resultCalendar.set(Calendar.DAY_OF_MONTH, to.get(Calendar.DAY_OF_MONTH));

        return resultCalendar;
    }

    /**
     * Convert between defined time zone's and also transform the result into an XMLGregorianCalendar.
     * @param date_p Date to be converted (can be null)
     * @param fromTimeZone_p TimeZone representing date time zone
     * @param toTimeZone_p TimeZone defined resulting time zone
     * @return XMLGregorianCalendar, or null if provided date is null
     * @throws DatatypeConfigurationException if could not create DatatypeFactory
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date_p, TimeZone fromTimeZone_p, TimeZone toTimeZone_p) throws DatatypeConfigurationException
    {
        if (date_p != null)
        {
            Calendar c = convertToCalendar(date_p, fromTimeZone_p, toTimeZone_p);
            DatatypeFactory factory = DatatypeFactory.newInstance();
            if (c.getMinimum(Calendar.MONTH) == 0)
            {
                return factory.newXMLGregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND),
                        toTimeZone_p.getRawOffset());
            }
            else
            {
                return factory.newXMLGregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND),
                        toTimeZone_p.getRawOffset());
            }
        }
        else
        {
            return null;
        }
    }
}