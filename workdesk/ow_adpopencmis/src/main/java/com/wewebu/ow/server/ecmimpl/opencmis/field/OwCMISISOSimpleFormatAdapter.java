package com.wewebu.ow.server.ecmimpl.opencmis.field;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *<p>
 * ISO 8601  compliant Format adapter for {@link SimpleDateFormat}.
 * Adapts Z (Zulu time zone)  to simple date formats 0 offset.
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
public class OwCMISISOSimpleFormatAdapter extends Format
{
    /**generated serial version UID*/
    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    protected static final String PATTERN_FULL_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private SimpleDateFormat zoneFormat;
    private SimpleDateFormat noZoneFormat;
    private String currentZoneOffset;

    public OwCMISISOSimpleFormatAdapter()
    {
        this("+00:00");
    }

    public OwCMISISOSimpleFormatAdapter(String currentOffset)
    {
        this(new SimpleDateFormat(PATTERN_FULL_ZONE), new SimpleDateFormat(PATTERN_FULL), currentOffset);
    }

    public OwCMISISOSimpleFormatAdapter(SimpleDateFormat zoneFormat_p, SimpleDateFormat noZoneFormat_p, String currentOffset)
    {
        super();
        this.zoneFormat = zoneFormat_p;
        this.noZoneFormat = noZoneFormat_p;
        this.currentZoneOffset = currentOffset;
    }

    @Override
    public final StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        return this.zoneFormat.format(obj, toAppendTo, pos);
    }

    @Override
    public final Object parseObject(String source, ParsePosition pos)
    {
        Date result = null;

        String adaptedSource = source;
        SimpleDateFormat format = zoneFormat;

        if (source.endsWith("Z"))
        {
            adaptedSource = source.replace("Z", "+0000");
        }

        if (adaptedSource.length() > 28)
        {
            if (':' == adaptedSource.charAt(26))
            {
                adaptedSource = adaptedSource.substring(0, 26) + adaptedSource.substring(27);
            }
        }

        if (adaptedSource.length() > 23)
        {
            char zoneMark = adaptedSource.charAt(23);
            if (!('-' == zoneMark || '+' == zoneMark))
            {
                format = noZoneFormat;
            }
        }

        result = format.parse(adaptedSource, pos);

        if (result != null)
        {
            return result;
        }
        else
        {
            pos.setIndex(0);
            return parseObjectByIdentification(source, pos);
        }
    }

    /**
     * Process if default parse does not work like requested.
     * <p>By default use the {@link #PATTERN_FULL} format without
     * TimeZone, handling time zone with additional logic.</p> 
     * @param source String representing ISO date
     * @param pos ParsePostion used by java.text.Format
     * @return Object (Date) or null if could not parse
     */
    protected Object parseObjectByIdentification(final String source, ParsePosition pos)
    {
        String[] split = source.split("T");
        Object result = null;
        if (split.length == 2)
        {
            StringBuilder src = new StringBuilder(split[0]);
            src.append("T");
            String time, zone;
            int idxZone;
            if ((idxZone = split[1].indexOf('-')) > 0 || (idxZone = split[1].indexOf('+')) > 0)
            {
                time = split[1].substring(0, idxZone);
                zone = split[1].substring(idxZone);
            }
            else
            {//NO digit time zone
                if (split[1].endsWith("Z"))
                {
                    time = split[1].substring(0, split[1].length() - 1);
                    zone = "+0000";//understand it as UTC (GMT+0|ZULU)
                }
                else
                {//No timezone at all
                    time = split[1];
                    zone = currentZoneOffset;//as ISO 8601 define local time zone
                }
            }

            if (time.indexOf(':') > 0)
            {//HH:mm:ss
                src.append(time);
                if (time.indexOf('.') < 0)
                {
                    src.append(".000");
                }
            }
            else
            {//definition looks like HHmmss
                for (int i = 0; i < 3; i++)
                {
                    src.append(time.substring(i * 2, i * 2 + 2));//HH
                    src.append(i == 2 ? "." : ":");
                }

                if (time.length() > 6)
                {
                    src.append("000");
                }
                else
                {
                    src.append(time.substring(6));
                }
            }

            SimpleDateFormat fullDateFormat = new SimpleDateFormat(PATTERN_FULL);
            fullDateFormat.setTimeZone(parseTimeZone(zone));
            result = fullDateFormat.parseObject(src.toString(), pos);
        }
        return result;
    }

    /**
     * Create specific zone 
     * @param zone String extracted time zone
     * @return TimeZone
     */
    protected TimeZone parseTimeZone(final String zone)
    {
        StringBuilder zoneDef = new StringBuilder("GMT");
        String[] split = zone.split(":");
        if (split.length == 2)
        {
            zoneDef.append(split[0]);
            zoneDef.append(split[1]);
        }
        else
        {
            zoneDef.append(zone);
        }
        return TimeZone.getTimeZone(zoneDef.toString());
    }

}
