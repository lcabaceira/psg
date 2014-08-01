package com.wewebu.ow.server.ecmimpl.fncm5.property;

import com.filenet.api.property.Property;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5EngineProperty.
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
public final class OwFNCM5EngineProperty<N, O> extends OwFNCM5Property
{
    private static final Object NO_VALUE = new Object();
    private Property property;
    private Object value = NO_VALUE;

    public OwFNCM5EngineProperty(Property property_p, OwFNCM5EnginePropertyClass<?, N, O> propertyClass_p)
    {
        this(property_p, NO_VALUE, propertyClass_p);
    }

    @Override
    public OwFNCM5EnginePropertyClass<?, N, O> getPropertyClass()
    {
        return (OwFNCM5EnginePropertyClass<?, N, O>) super.getPropertyClass();
    }

    private OwFNCM5EngineProperty(Property property_p, Object value_p, OwFNCM5EnginePropertyClass<?, N, O> propertyClass_p)
    {
        super(propertyClass_p);
        this.property = property_p;
        this.value = value_p;
    }

    public boolean isReadOnly(int iContext_p) throws OwException
    {
        return !this.property.isSettable() && getPropertyClass().isReadOnly(iContext_p);
    }

    public boolean isHidden(int iContext_p) throws Exception
    {
        return getPropertyClass().isHidden(iContext_p);
    }

    public Property getNativeObject()
    {
        return property;
    }

    public boolean isDirty() throws OwException
    {
        if (this.value != NO_VALUE)
        {
            OwFNCM5EnginePropertyClass<?, N, O> clazz = getPropertyClass();
            Object nativeValue = property.getObjectValue();
            Object pValue = clazz.fromNativeValue(nativeValue);

            if (this.value == null)
            {
                return pValue != null;
            }
            else if (pValue == null)
            {
                return true;
            }
            else
            {
                return !this.value.equals(pValue);
            }

        }
        else
        {
            return false;
        }
    }

    public Object getNativeValue() throws OwException
    {
        if (isDirty())
        {
            OwFNCM5EnginePropertyClass<?, N, O> clazz = getPropertyClass();
            return clazz.toNativeValue(this.value);
        }
        else
        {
            return property.getObjectValue();
        }
    }

    public Object getValue() throws OwException
    {
        if (isDirty())
        {
            return this.value;
        }
        else
        {
            OwFNCM5EnginePropertyClass<?, N, O> clazz = getPropertyClass();
            Object nativeValue = property.getObjectValue();
            return clazz.fromNativeValue(nativeValue);
        }
    }

    public void setValue(Object oValue_p)
    {
        this.value = oValue_p;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new OwFNCM5EngineProperty<N, O>(property, value, getPropertyClass());
    }

}
