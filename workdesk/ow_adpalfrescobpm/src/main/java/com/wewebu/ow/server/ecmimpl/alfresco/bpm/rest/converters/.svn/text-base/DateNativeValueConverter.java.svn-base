package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISDateTime;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwDateTimeUtil;

/**
 *<p>
 * Converts from {@link String} to {@link Date} and back.
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
 *@since 4.0.0.0
 */
public class DateNativeValueConverter implements NativeValueConverter
{
    private TimeZone timeZone;

    public DateNativeValueConverter(TimeZone timeZone)
    {
        this.timeZone = timeZone;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter#fromNative(java.lang.Object)
     */
    public Object fromNative(Object nativeValue) throws OwException
    {
        //The native value is always in UTC time zone.
        try
        {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String strNative = (String) nativeValue;
            Date firstDateAttempt = format.parse(strNative);
            TimeZone tzServer = Calendar.getInstance().getTimeZone();
            Date secondDateAttempt = OwDateTimeUtil.convert(firstDateAttempt, tzServer, this.timeZone);
            return secondDateAttempt;
        }
        catch (ParseException e)
        {
            throw new OwServerException("Could not get java value from native value " + nativeValue, e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter#fromJava(java.lang.Object)
     */
    public Object fromJava(Object javaValue) throws OwException
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date dateValue = (Date) javaValue;
        OwCMISDateTime cmisDateTime = new OwCMISDateTime(dateValue, this.timeZone);
        Date preparedDate = cmisDateTime.getDate(false);

        String iso8601Representation = format.format(preparedDate);
        return iso8601Representation;
    }
}