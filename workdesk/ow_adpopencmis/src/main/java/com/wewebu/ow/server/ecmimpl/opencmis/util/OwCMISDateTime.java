package com.wewebu.ow.server.ecmimpl.opencmis.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * {@link Date} handling utility for CMIS adapter.<br>
 * The main purpose of this utility is to implement String to Date and reversed conversions
 * with respect to the Date String format specified by CMIS : <b>YYYY-MM-DDThh:mm:ss.sss[Z | +hh:mm | -hh:mm]</b>.
 * The implementation relays on the {@link GregorianCalendar} implementation. 
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
public class OwCMISDateTime
{
    private static Logger LOG = OwLog.getLogger(OwCMISDateTime.class);

    private GregorianCalendar m_calendar;

    /**
     * Constructor
     * @param dateLexicalRepresentation_p String representation of a CMIS date<br> 
     *                                    Example:  <br>
     *                                    "2000-01-01T06:20:13.080+07:00"<br>
     *                                    "2009-17-10T06:20:13.080Z"<br>
     *                                    "2010-06-01T06:20:13.080"<br>
     *                                    "2000-01-01T06:20:13.080+0300"<br>
     *                                    "2000-01-01T06:20:13.080-0100"<br>
     * @throws OwInvalidOperationException
     */
    public OwCMISDateTime(String dateLexicalRepresentation_p) throws OwInvalidOperationException
    {
        try
        {
            String representation = dateLexicalRepresentation_p;
            if (representation != null)
            {
                if (representation.length() > 5)
                {
                    char zoneMark = representation.charAt(representation.length() - 5);
                    if (zoneMark == '+' || zoneMark == '-')
                    {
                        char middleZoneMark = representation.charAt(representation.length() - 3);
                        if (middleZoneMark != ':')
                        {
                            representation = new StringBuffer(representation).insert(representation.length() - 2, ":").toString();
                        }
                    }
                }

            }
            DatatypeFactory factory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlGregoriaCalendar = factory.newXMLGregorianCalendar(representation);
            this.m_calendar = xmlGregoriaCalendar.toGregorianCalendar();
        }
        catch (Exception e)
        {
            String msg = "Could not parse date lexical representation :" + dateLexicalRepresentation_p;
            LOG.error("OwCMISDateTime.OwCMISDateTime():" + msg);
            throw new OwInvalidOperationException(new OwString1("opencmis.util.OwCMISDateTime.err.parse.date", "Invalid date/time format: %1", dateLexicalRepresentation_p), e);
        }
    }

    public OwCMISDateTime(GregorianCalendar calendar_p)
    {
        this.m_calendar = (GregorianCalendar) calendar_p.clone();
    }

    /**
     * Constructor
     * @param date_p date represented by this object  
     * @param timeZone_p the time zone to represent the given date in
     * @throws OwInvalidOperationException if cannot create/convert to {@link GregorianCalendar}
     */
    public OwCMISDateTime(Date date_p, TimeZone timeZone_p) throws OwInvalidOperationException
    {
        Calendar local = Calendar.getInstance();
        local.setTime(date_p);

        this.m_calendar = new GregorianCalendar(timeZone_p);

        int year = local.get(Calendar.YEAR);
        int month = local.get(Calendar.MONTH);
        int day = local.get(Calendar.DAY_OF_MONTH);
        int hour = local.get(Calendar.HOUR_OF_DAY);
        int minute = local.get(Calendar.MINUTE);
        int second = local.get(Calendar.SECOND);
        int millisecond = local.get(Calendar.MILLISECOND);

        this.m_calendar.set(Calendar.YEAR, year);
        this.m_calendar.set(Calendar.MONTH, month);
        this.m_calendar.set(Calendar.DAY_OF_MONTH, day);
        this.m_calendar.set(Calendar.HOUR_OF_DAY, hour);
        this.m_calendar.set(Calendar.MINUTE, minute);
        this.m_calendar.set(Calendar.SECOND, second);
        this.m_calendar.set(Calendar.MILLISECOND, millisecond);
    }

    /**
     * 
     * @return a CMIS String representation of this date time object
     */
    public String toCMISDateTimeString()
    {
        return toCMISDateTimeString(false);
    }

    public String toISODateTimeString()
    {
        return toISODateTimeString(false);
    }

    public String toISODateTimeString(boolean omitTimeZone_p)
    {
        String isoString = toCMISDateTimeString(omitTimeZone_p);
        if (!omitTimeZone_p)
        {
            int mark = isoString.length() - 3;
            isoString = new StringBuffer(isoString).replace(mark, mark + 1, "").toString();
        }

        return isoString;
    }

    /**
     * 
     * @param omitTimeZone_p  if <code>true</code> the time zone part of the string representation will be omitted<br>
     *                        if <code>false</code> the string representation will include the time zone part  
     * @return a CMIS String representation of this date time object
     */
    public String toCMISDateTimeString(boolean omitTimeZone_p)
    {
        XMLGregorianCalendar calendar = getXMLGregorianCalendar(omitTimeZone_p);
        return calendar.toXMLFormat();
    }

    /**
     * 
     * @param ignoreTimeZone_p if <code>true</code> ??? 
     * @return the {@link Date} representation of this object
     */
    public Date getDate(boolean ignoreTimeZone_p)
    {
        Calendar calendar = getCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        if (!ignoreTimeZone_p)//FIXME: What will do this?
        {
            calendar.setTimeZone(getTimeZone());
        }
        return calendar.getTime();
    }

    /**
     * 
     * @return the {@link GregorianCalendar} representation of this object; the {@link TimeZone} of the 
     *         returned {@link Calendar} will be set to the closest {@link TimeZone} as given 
     *         by {@link TimeZone#getAvailableIDs(int)} for this dates UTC offset
     */
    public GregorianCalendar getCalendar()
    {
        return (GregorianCalendar) m_calendar.clone();
    }

    /**
     * 
     * @return the {@link XMLGregorianCalendar} representation of this date time object
     */
    public XMLGregorianCalendar getXMLGregorianCalendar()
    {
        DatatypeFactory factory;
        try
        {
            factory = DatatypeFactory.newInstance();
            return factory.newXMLGregorianCalendar(m_calendar);
        }
        catch (DatatypeConfigurationException e)
        {
            LOG.error("OwCMISDateTime.getXMLGregorianCalendar : ", e);
            return null;
        }

    }

    /**
     * @param ignoreTimeZone_p if <code>true</code> the returned {@link XMLGregorianCalendar} will
     *                         have an undefined time zone 
     * @return the {@link XMLGregorianCalendar} representation of this date time object
     */
    public XMLGregorianCalendar getXMLGregorianCalendar(boolean ignoreTimeZone_p)
    {
        DatatypeFactory factory;
        try
        {
            factory = DatatypeFactory.newInstance();
            XMLGregorianCalendar calendarCopy = factory.newXMLGregorianCalendar(m_calendar);

            if (ignoreTimeZone_p)
            {
                calendarCopy.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            }

            return calendarCopy;

        }
        catch (DatatypeConfigurationException e)
        {
            LOG.error("OwCMISDateTime.getXMLGregorianCalendar : ", e);
            return null;
        }

    }

    /**
     * 
     * @return the time zone of this date time object
     */
    public TimeZone getTimeZone()
    {
        return m_calendar.getTimeZone();
    }

    @Override
    public String toString()
    {
        return m_calendar == null ? "<null calendar>" : toCMISDateTimeString();
    }

    @Override
    public int hashCode()
    {
        return m_calendar.hashCode();
    }

    @Override
    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwCMISDateTime)
        {
            OwCMISDateTime dateTimeObj = (OwCMISDateTime) obj_p;
            if (m_calendar == null || dateTimeObj.m_calendar == null)
            {
                return m_calendar == dateTimeObj.m_calendar;
            }
            else
            {
                return m_calendar.equals(dateTimeObj.m_calendar);
            }
        }
        else
        {
            return false;
        }
    }
}
