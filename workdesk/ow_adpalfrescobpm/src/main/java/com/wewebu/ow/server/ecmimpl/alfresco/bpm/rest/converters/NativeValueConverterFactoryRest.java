package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters;

import java.util.Date;
import java.util.TimeZone;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Factory for {@link NativeValueConverter}s to convert from rest values to java values and back.
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
public class NativeValueConverterFactoryRest implements NativeValueConverterFactory
{
    private TimeZone timeZone;

    public NativeValueConverterFactoryRest(TimeZone timeZone)
    {
        this.timeZone = timeZone;
    }

    public NativeValueConverter converterFor(OwFieldDefinition propertyClass) throws OwException
    {
        String propertyClassName = propertyClass.getJavaClassName();
        Class<?> propertyJavaClass;
        try
        {
            propertyJavaClass = Class.forName(propertyClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new OwServerException("Could not find java class for property class.", e);
        }

        NativeValueConverter converter;
        if (Date.class.isAssignableFrom(propertyJavaClass))
        {
            converter = new DateNativeValueConverter(this.timeZone);
        }
        else if (OwUserInfo.class.isAssignableFrom(propertyJavaClass))
        {
            converter = new OwUserInfoNativeValueConverter();
        }
        else
        {
            // fallback converter
            converter = new DefaultNativeValueConverter(propertyClass);
        }

        try
        {
            if (propertyClass.isArray())
            {
                converter = new ArrayAgregateConverter(converter);
            }
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not get native converter.", e);
        }

        return converter;
    }
}
