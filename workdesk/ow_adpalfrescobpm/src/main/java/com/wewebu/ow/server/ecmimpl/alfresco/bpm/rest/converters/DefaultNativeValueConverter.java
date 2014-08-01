package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Fallback {@link NativeValueConverter} implementation.
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
public class DefaultNativeValueConverter implements NativeValueConverter
{

    private OwFieldDefinition propertyClass;

    /**
     * @param propertyClass
     */
    public DefaultNativeValueConverter(OwFieldDefinition propertyClass)
    {
        this.propertyClass = propertyClass;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter#fromNative(java.lang.Object)
     */
    public Object fromNative(Object nativeValue) throws OwException
    {
        if (null == nativeValue)
        {
            return null;
        }
        Object value = nativeValue;
        try
        {
            Class<?> propertyJavaClass = Class.forName(this.propertyClass.getJavaClassName());
            Class<?> propertyValueClass = nativeValue.getClass();
            if (!propertyJavaClass.isAssignableFrom(propertyValueClass) || this.propertyClass.isArray())
            {
                if (String.class.isAssignableFrom(propertyValueClass) && !propertyClass.isArray())
                {
                    value = propertyClass.getValueFromString((String) nativeValue);
                }
            }
            return value;
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not convert native value to java value.", e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter#fromJava(java.lang.Object)
     */
    public Object fromJava(Object javaValue)
    {
        if (null != javaValue)
        {
            return javaValue.toString();
        }
        else
        {
            return null;
        }
    }

}
