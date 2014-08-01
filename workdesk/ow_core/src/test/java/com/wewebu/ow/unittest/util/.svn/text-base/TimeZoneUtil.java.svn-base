package com.wewebu.ow.unittest.util;

import java.util.TimeZone;

/**
 *<p>
 * TimeZoneUtil.
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
public class TimeZoneUtil
{

    /**
     * Will convert a given TimeZone to <code>GMT(+|-)hhmm</code> representation
     * removing the information of daylight saving time.
     * @param timeZone TimeZone to convert
     * @return TimeZone without DST
     */
    public static TimeZone getNonDstTimeZone(TimeZone timeZone)
    {
        if (timeZone.useDaylightTime())
        {
            StringBuilder tz = new StringBuilder("GMT");
            if (timeZone.getRawOffset() != 0)
            {
                int ctz = timeZone.getRawOffset();
                tz.append(ctz < 0 ? "-" : "+");
                int min = Math.abs(ctz / 60000);
                int hour = min / 60;
                min = min % 60;
                tz.append(hour < 10 ? "0" : "");
                tz.append(Integer.toString(hour));
                tz.append(min < 10 ? "0" : "");
                tz.append(Integer.toString(min));
            }
            return TimeZone.getTimeZone(tz.toString());
        }
        else
        {
            return timeZone;
        }
    }
}
