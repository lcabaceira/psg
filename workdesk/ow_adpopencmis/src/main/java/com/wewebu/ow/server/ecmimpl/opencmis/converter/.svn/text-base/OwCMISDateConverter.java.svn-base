package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISDateTime;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwDateTimeUtil;

/**
 *<p>
 * OwCMISStandardDateConverter.
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
public class OwCMISDateConverter implements OwCMISValueConverter<GregorianCalendar, Date>
{
    private TimeZone timeZone;

    /**
     * @param timezone {@link TimeZone} of the converted Date values
     */
    public OwCMISDateConverter(TimeZone timezone)
    {
        this.timeZone = timezone;
    }

    private GregorianCalendar toGregorianCalendar(Date date_p) throws OwInvalidOperationException
    {
        OwCMISDateTime cmisDateTime = new OwCMISDateTime(date_p, getTimeZone());
        GregorianCalendar calendar = cmisDateTime.getCalendar();
        return calendar;
    }

    private Date toDate(GregorianCalendar calendar_p)
    {
        if (calendar_p == null)
        {
            return null;
        }
        else
        {
            OwCMISDateTime cmisDateTime = new OwCMISDateTime(calendar_p);
            TimeZone tzServer = calendar_p.getTimeZone();//UTC time offset 0
            return OwDateTimeUtil.convert(cmisDateTime.getDate(true), tzServer, getTimeZone());
        }
    }

    public List<GregorianCalendar> fromArrayValue(Object[] owdValue_p) throws OwInvalidOperationException
    {
        List<GregorianCalendar> calendarList = new LinkedList<GregorianCalendar>();

        if (owdValue_p != null)
        {
            for (int i = 0; i < owdValue_p.length; i++)
            {
                if (owdValue_p[i] != null)
                {
                    GregorianCalendar calendar = toGregorianCalendar((Date) owdValue_p[i]);
                    calendarList.add(calendar);
                }
                else
                {
                    calendarList.add(null);
                }
            }
        }

        return calendarList;
    }

    public List<GregorianCalendar> fromValue(Date owdValue_p) throws OwInvalidOperationException
    {
        List<GregorianCalendar> calendarList = new LinkedList<GregorianCalendar>();
        if (owdValue_p != null)
        {
            calendarList.add(toGregorianCalendar(owdValue_p));
        }

        return calendarList;
    }

    public Date[] toArrayValue(List<GregorianCalendar> cmisValue_p)
    {
        return toStaticArrayValue(cmisValue_p);
    }

    public Date toValue(List<GregorianCalendar> cmisValue_p)
    {
        return toStaticValue(cmisValue_p);
    }

    public Class<Date> getOClass()
    {
        return Date.class;
    }

    public Date[] toStaticArrayValue(List<GregorianCalendar> cmisValue_p)
    {
        if (null == cmisValue_p || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            Date[] dateValues = new Date[cmisValue_p.size()];
            for (int i = 0; i < dateValues.length; i++)
            {
                GregorianCalendar calendarValue = cmisValue_p.get(i);
                dateValues[i] = toDate(calendarValue);
            }
            return dateValues;
        }
    }

    public Date toStaticValue(List<GregorianCalendar> cmisValue_p)
    {
        if (null == cmisValue_p || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            return toDate(cmisValue_p.get(0));
        }
    }

    /**
     * Time zone of the client, which can be used for conversion.
     * @return TimeZone of the client.
     */
    protected TimeZone getTimeZone()
    {
        return this.timeZone;
    }

}
