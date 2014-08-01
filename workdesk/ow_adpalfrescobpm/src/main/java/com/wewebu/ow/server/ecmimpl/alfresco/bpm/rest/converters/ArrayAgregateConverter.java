package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters;

import java.util.ArrayList;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Converts a multi-value property.
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
public class ArrayAgregateConverter implements NativeValueConverter
{

    private NativeValueConverter singleValueConverter;

    public ArrayAgregateConverter(NativeValueConverter singleValueConverter)
    {
        this.singleValueConverter = singleValueConverter;
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

        if (!nativeValue.getClass().isArray())
        {
            if (String.class == nativeValue.getClass())
            {
                if (0 == ((String) nativeValue).length())
                {
                    return null;
                }
            }

            throw new OwServerException("The native value must be of Array type.");
        }

        ArrayList<Object> result = new ArrayList<Object>();
        Object[] arrayValue = (Object[]) nativeValue;
        for (Object nativeObject : arrayValue)
        {
            Object javaObject = this.singleValueConverter.fromNative(nativeObject);
            result.add(javaObject);
        }
        return result.toArray();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter#fromJava(java.lang.Object)
     */
    public Object fromJava(Object javaValue) throws OwException
    {
        if (null == javaValue)
        {
            return null;
        }

        if (!javaValue.getClass().isArray())
        {
            throw new OwServerException("The java value must be of Array type.");
        }

        ArrayList<Object> result = new ArrayList<Object>();
        Object[] arrayValue = (Object[]) javaValue;
        for (Object javaObject : arrayValue)
        {
            Object nativeObject = this.singleValueConverter.fromJava(javaObject);
            result.add(nativeObject);
        }
        return result.toArray();
    }

}
