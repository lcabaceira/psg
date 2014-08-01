package com.wewebu.ow.server.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 *<p>
 * Time Zone information (UTC offset , hemisphere location , daylight savings) used 
 * to select or guess a corresponding time zone.   
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
 *@since 3.1.0.3
 */
public class OwTimeZoneInfo
{
    private static final List<String> SOUTHERN_TIMEZONES = Arrays.asList(new String[] { "Pacific/Apia", "Pacific/Easter", "America/Asuncion", "America/Montevideo", "Africa/Windhoek", "Australia/Eucla", "Australia/Adelaide", "Australia/Sydney",
            "Australia/Lord_Howe", "Pacific/Auckland", "Pacific/Chatham" });

    public static Set<Long> findDSTTransitions(TimeZone zone_p)
    {
        Set<Long> transitions = new HashSet<Long>();

        Calendar day = Calendar.getInstance();
        day.set(Calendar.YEAR, 2011);
        day.set(Calendar.DAY_OF_YEAR, 1); //first day of the year.
        day.set(Calendar.AM_PM, Calendar.PM);
        day.set(Calendar.HOUR_OF_DAY, 15);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        final int initialYear = day.get(Calendar.YEAR);

        while (day.get(Calendar.YEAR) == initialYear)
        {
            Calendar nextDay = Calendar.getInstance();
            nextDay.setTimeInMillis(day.getTimeInMillis());
            nextDay.add(Calendar.DAY_OF_YEAR, 1);

            int offset = zone_p.getOffset(day.getTimeInMillis());
            int nextOffset = zone_p.getOffset(nextDay.getTimeInMillis());

            if (offset != nextOffset)
            {
                transitions.add(day.getTimeInMillis());
            }
            day = nextDay;
        }

        return Collections.unmodifiableSet(transitions);
    }

    private synchronized static Map<String, Set<Long>> findAllDSTTransitions()
    {
        Map<String, Set<Long>> allTransitions = new HashMap<String, Set<Long>>();

        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; i++)
        {
            TimeZone zone = TimeZone.getTimeZone(ids[i]);
            Set<Long> zoneTransitions = findDSTTransitions(zone);
            allTransitions.put(zone.getID(), zoneTransitions);
        }

        return Collections.unmodifiableMap(allTransitions);
    }

    private static final Map<String, Set<Long>> ZONE_TRANSITIONS = findAllDSTTransitions();
    private static Set<Long> DST_TRANSITIONS = null;

    /**
     * 
     * @param timeZoneID_p a time zone id as defined by {@link TimeZone#getAvailableIDs()}   
     * @return a set UTC time stamps for all daylight savings transition days found for the given zone 
     */
    public static Set<Long> getZoneDSTTransitions(String timeZoneID_p)
    {
        return ZONE_TRANSITIONS.get(timeZoneID_p);
    }

    /**
     * 
     * @return a set of all UTC time stamps for all daylight savings in all time zones defined by {@link TimeZone}
     */
    public static synchronized Set<Long> getDSTTrasitions()
    {
        if (DST_TRANSITIONS == null)
        {
            Set<Long> dstTransitions = new HashSet<Long>();
            Collection<Set<Long>> allZoneTransitions = ZONE_TRANSITIONS.values();
            for (Set<Long> transition : allZoneTransitions)
            {
                dstTransitions.addAll(transition);
            }

            DST_TRANSITIONS = Collections.unmodifiableSet(dstTransitions);
        }

        return DST_TRANSITIONS;
    }

    private int offset;
    private boolean observesDaylightSavings;
    private boolean northernHemisphere;
    private long date;
    private TimeZone timeZone = null;
    private Set<Long> transitions = Collections.EMPTY_SET;

    /**
     * Constructor<br> 
     * Based on current time zone information.
     */
    public OwTimeZoneInfo()
    {
        this(TimeZone.getDefault());
    }

    public OwTimeZoneInfo(TimeZone timeZone_p)
    {
        this(new Date(), timeZone_p);
    }

    /**
     * Constructor
     * @param timeZone_p time zone used to extract the information
     */
    public OwTimeZoneInfo(Date reference_p, TimeZone timeZone_p)
    {
        this.timeZone = timeZone_p;
        this.date = reference_p.getTime();
        this.offset = this.timeZone.getOffset(this.date);
        this.observesDaylightSavings = this.timeZone.getDSTSavings() != 0;
        this.transitions = getZoneDSTTransitions(timeZone_p.getID());
        this.northernHemisphere = isNorthernHemisphere(this.timeZone);

    }

    /**
     * Constructor 
     * @param date_p January 1, 1970, 00:00:00 relative time of this information object 
     * @param offset_p  offset of this time zone from UTC at the specified date (date_p)
     * @param observesDaylightSavings_p true if the time zone indicated by this object observes daylight savings
     * @param transitions_p long UTC time-stamps of daylight savings transition days
     * @param northernHemisphere_p true if the time zone indicated by this object is located in the northern hemisphere
     */
    public OwTimeZoneInfo(long date_p, int offset_p, boolean observesDaylightSavings_p, Long[] transitions_p, boolean northernHemisphere_p)
    {
        super();
        this.offset = offset_p;
        this.observesDaylightSavings = observesDaylightSavings_p;
        this.transitions = new HashSet<Long>(Arrays.asList(transitions_p));
        this.northernHemisphere = northernHemisphere_p;
        this.date = date_p;
    }

    /**
     * 
     * @return a time zone that matches this information object or null if no matching time zone can be found  
     */
    public synchronized TimeZone getTimeZone()
    {

        if (this.timeZone == null)
        {
            String[] ids = TimeZone.getAvailableIDs();

            for (int i = 0; i < ids.length; i++)
            {
                TimeZone timeZone = TimeZone.getTimeZone(ids[i]);
                int tzOffset = timeZone.getOffset(date);
                if (offset == tzOffset)
                {
                    int dst = timeZone.getDSTSavings();
                    if (observesDaylightSavings == (dst != 0))
                    {
                        Set<Long> zoneTransitions = getZoneDSTTransitions(timeZone.getID());
                        if (sameTrasitions(this.transitions, zoneTransitions))
                        {

                            if (northernHemisphere == isNorthernHemisphere(timeZone))
                            {
                                return timeZone;
                            }
                        }
                    }
                }
            }
        }

        return this.timeZone;
    }

    private static boolean isNorthernHemisphere(TimeZone timeZone)
    {
        return !SOUTHERN_TIMEZONES.contains(timeZone.getID());
    }

    /**
     * 
     * @return true if this information object indicates a time zone in the northern hemisphere 
     */
    public boolean isNorthernHemisphere()
    {
        return northernHemisphere;
    }

    /**
     * 
     * @return UTC offset of the time zone indicated by this time zone 
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * 
     * @return true if the time zone object indicated by this object observes daylight savings
     */
    public boolean isObservesDaylightSavings()
    {
        return observesDaylightSavings;
    }

    @Override
    public int hashCode()
    {
        return Integer.valueOf(offset).hashCode();
    }

    private boolean sameTrasitions(Set<Long> t1_p, Set<Long> t2_p)
    {
        if (t1_p == null)
        {
            return t2_p == null;
        }
        else if (t2_p == null)
        {
            return false;
        }
        else if (t1_p.size() == t2_p.size())
        {
            return t1_p.containsAll(t2_p);
        }
        else
        {
            return false;
        }

    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof OwTimeZoneInfo)
        {
            OwTimeZoneInfo info = (OwTimeZoneInfo) obj;
            return this.date == info.date && this.offset == info.offset && this.observesDaylightSavings == info.observesDaylightSavings && this.northernHemisphere == info.northernHemisphere && sameTrasitions(this.transitions, info.transitions);
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        TimeZone tz = getTimeZone();
        return "OwTimeZoneInfo[date=" + date + " offset=" + offset + " dst=" + observesDaylightSavings + " northern=" + northernHemisphere + " tzID=" + (tz == null ? "<unknown>" : (tz.getID() + " aka " + tz.getDisplayName())) + "]";
    }
}
