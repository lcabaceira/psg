package com.wewebu.expression.language;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *<p>
 * OwExprTime.
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
public class OwExprTime implements Comparable
{

    public static final double SECONDS_IN_MINUTE = 60.0;
    public static final double SECONDS_IN_HOUR = 60.0 * SECONDS_IN_MINUTE;
    public static final double SECONDS_IN_DAY = SECONDS_IN_HOUR * 24.0;
    public static final double MILISECONDS_IN_HOUR = SECONDS_IN_HOUR * 1000.0;
    public static final double MILISECONDS_IN_MINUTE = SECONDS_IN_MINUTE * 1000.0;
    public static final double MILLISECONDS_IN_DAY = 1000.0 * SECONDS_IN_DAY;

    public static final OwExprTime seconds(int seconds_p)
    {
        return time(0, 0, seconds_p);
    }

    public static final OwExprTime minutes(int minutes_p)
    {
        return time(0, minutes_p, 0);
    }

    public static final OwExprTime hours(int hours_p)
    {
        return time(hours_p, 0, 0);
    }

    public static final OwExprTime time(int hours_p, int minutes_p, int seconds_p)
    {
        int minutesInSeconds = seconds_p / 60;
        int secondsRest = seconds_p % 60;

        int hoursInMinutes = (minutes_p + minutesInSeconds) / 60;
        int minutesRest = (minutes_p + minutesInSeconds) % 60;

        int daysInHours = (hours_p + hoursInMinutes) / 24;
        int hours = (hours_p + hoursInMinutes) % 24;

        double inDays = ((daysInHours + hours * SECONDS_IN_HOUR + 60 * minutesRest + secondsRest)) / SECONDS_IN_DAY;

        return new OwExprTime(0, 0, daysInHours, inDays, hours, minutesRest, secondsRest);
    }

    public static final OwExprTime timeInDays(double days_p)
    {
        double flooredDays = Math.floor(days_p);
        double restOfDay = days_p - flooredDays;

        double hours = restOfDay * 24.0;
        double flooredHours = Math.floor(hours);
        double restOfHour = hours - flooredHours;

        double minutes = restOfHour * 60.0;
        double flooredMinutes = Math.floor(minutes);
        double restOfMinute = minutes - flooredMinutes;

        long seconds = (long) Math.floor(restOfMinute * 60.0);

        return new OwExprTime(0, 0, (int) flooredDays, days_p, (int) flooredHours, (int) flooredMinutes, (int) seconds);
    }

    public static final OwExprTime diff(Calendar c1_p, Calendar c2_p)
    {
        int y1 = c1_p.get(Calendar.YEAR);
        int mo1 = c1_p.get(Calendar.MONTH);
        int d1 = c1_p.get(Calendar.DAY_OF_MONTH);
        int h1 = c1_p.get(Calendar.HOUR_OF_DAY);
        int mi1 = c1_p.get(Calendar.MINUTE);
        int s1 = c1_p.get(Calendar.SECOND);

        int y2 = c2_p.get(Calendar.YEAR);
        int mo2 = c2_p.get(Calendar.MONTH);
        int d2 = c2_p.get(Calendar.DAY_OF_MONTH);
        int h2 = c2_p.get(Calendar.HOUR_OF_DAY);
        int mi2 = c2_p.get(Calendar.MINUTE);
        int s2 = c2_p.get(Calendar.SECOND);

        int dy = 0;
        int dmo = 0;
        int dd = 0;
        int dh = 0;
        int dmi = 0;
        int ds = s1 - s2;

        if (ds < 0)
        {
            dmo = -1;
            ds = 60 + ds;
        }

        dmi += mi1 - mi2;
        if (dmi < 0)
        {
            dh = -1;
            dmi = 60 + dmo;
        }

        dh += h1 - h2;
        if (dh < 0)
        {
            dd = -1;
            dh = 24 + dh;
        }

        dd += d1 - d2;

        if (dd < 0)
        {
            dmo = -1;

            Calendar c1Clone = (Calendar) c1_p.clone();
            c1Clone.add(Calendar.MONTH, -1);
            int daysPrevMonth = c1Clone.getActualMaximum(Calendar.DAY_OF_MONTH);
            dd = daysPrevMonth + dd;
        }

        dmo += mo1 - mo2;

        if (dmo < 0)
        {
            dy = -1;

            dmo = 13 + dmo;
        }

        dy += y1 - y2;

        long c1UTCMs = c1_p.getTimeInMillis();
        long c2UTCMs = c2_p.getTimeInMillis();
        long c1ms = c1UTCMs + c1_p.getTimeZone().getOffset(c1UTCMs);
        long c2ms = c2UTCMs + c2_p.getTimeZone().getOffset(c2UTCMs);

        long difms = c1ms - c2ms;
        long difUTCms = c1UTCMs - c2UTCMs;
        double difInDays = (difms) / MILLISECONDS_IN_DAY;
        double difInUTCDays = (difUTCms) / MILLISECONDS_IN_DAY;

        return new OwExprTime(dy, dmo, dd, difInDays, difInUTCDays, dh, dmi, ds);
    }

    private int m_years;
    private int m_months;
    private int m_days;
    private double m_inDays;
    private double m_inUTCDays;
    private int m_hours;
    private int m_minutes;
    private int m_seconds;

    public OwExprTime(int years_p, int months_p, int days_p, double inDays_p)
    {
        this(years_p, months_p, days_p, inDays_p, 0, 0, 0);
    }

    public OwExprTime(int hours_p, int minutes_p, int seconds_p)
    {
        this(0, 0, 0, (seconds_p + minutes_p * 60 + hours_p * 60 * 60) / SECONDS_IN_DAY, hours_p, minutes_p, seconds_p);
    }

    public OwExprTime(int years_p, int months_p, int days_p, double inDays_p, int hours_p, int minutes_p, int seconds_p)
    {
        this(years_p, months_p, days_p, inDays_p, inDays_p, hours_p, minutes_p, seconds_p);
    }

    public OwExprTime(int years_p, int months_p, int days_p, double inDays_p, double inUTCDays_p, int hours_p, int minutes_p, int seconds_p)
    {
        super();
        this.m_years = years_p;
        this.m_months = months_p;
        this.m_days = days_p;
        this.m_inDays = inDays_p;
        this.m_inUTCDays = inUTCDays_p;
        this.m_hours = hours_p;
        this.m_minutes = minutes_p;
        this.m_seconds = seconds_p;
    }

    public final int getYears()
    {
        return m_years;
    }

    public final int getMonths()
    {
        return m_months;
    }

    public final int getDays()
    {
        return m_days;
    }

    public final double getInDays()
    {
        return m_inDays;
    }

    public final double getInUTCDays()
    {
        return m_inUTCDays;
    }

    public final int getHours()
    {
        return m_hours;
    }

    public final int getMinutes()
    {
        return m_minutes;
    }

    public final int getSeconds()
    {
        return m_seconds;
    }

    public final Calendar addTo(Calendar calendar_p)
    {
        Calendar clone = (Calendar) calendar_p.clone();
        TimeZone zone = calendar_p.getTimeZone();
        clone.setTimeZone(zone);
        long utcMs = clone.getTimeInMillis();
        long zoneMs = utcMs + zone.getOffset(utcMs);
        long longTime = (long) (this.m_inDays * MILLISECONDS_IN_DAY);
        long sumInMs = zoneMs + longTime;
        clone.setTimeInMillis(sumInMs - zone.getOffset(utcMs + longTime));
        return clone;
    }

    public final Calendar substractFrom(Calendar calendar_p)
    {
        Calendar clone = (Calendar) calendar_p.clone();
        TimeZone zone = calendar_p.getTimeZone();
        clone.setTimeZone(zone);
        long utcMs = clone.getTimeInMillis();
        long zoneMs = utcMs + zone.getOffset(utcMs);
        long longTime = (long) (this.m_inDays * MILLISECONDS_IN_DAY);
        long difInMs = zoneMs - longTime;
        clone.setTimeInMillis(difInMs - zone.getOffset(utcMs - longTime));
        return clone;
    }

    public final OwExprTime substract(OwExprTime time_p)
    {
        return OwExprTime.timeInDays(this.m_inDays - time_p.m_inDays);
    }

    public final OwExprTime add(OwExprTime t_p)
    {
        OwExprTime newTime = OwExprTime.timeInDays(this.m_inDays + t_p.m_inDays);
        newTime.m_inUTCDays = this.m_inUTCDays + t_p.m_inUTCDays;
        return newTime;
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwExprTime)
        {
            OwExprTime timeObject = (OwExprTime) obj_p;
            return m_inDays == timeObject.m_inDays;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return new Double(m_inDays).hashCode();
    }

    public String toString()
    {
        return "" + m_years + ":" + m_months + ":" + m_days + "(" + m_inDays + "~" + m_inUTCDays + "):" + m_hours + ":" + m_minutes + ":" + m_seconds;
    }

    public int compareTo(Object object_p)
    {
        if (object_p instanceof OwExprTime)
        {
            OwExprTime timeObject = (OwExprTime) object_p;

            return new Double(m_inDays).compareTo(new Double(timeObject.m_inDays));
        }
        else
        {
            return 1;
        }
    }
}
