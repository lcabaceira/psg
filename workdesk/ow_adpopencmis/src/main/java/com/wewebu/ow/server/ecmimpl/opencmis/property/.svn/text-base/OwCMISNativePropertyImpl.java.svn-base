package com.wewebu.ow.server.ecmimpl.opencmis.property;

import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.runtime.PropertyImpl;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 *  OwCMISNativePropertyImpl.
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
public class OwCMISNativePropertyImpl<O, T> extends OwCMISAbstractProperty<O, OwCMISNativePropertyClass<O, T, ? extends PropertyDefinition<T>>> implements OwCMISNativeProperty<O, T>
{

    private Property<T> property;
    private OwCMISValueConverter<T, O> converter;

    public OwCMISNativePropertyImpl(OwCMISNativePropertyClass<O, T, ? extends PropertyDefinition<T>> propertyClass_p, Property<T> property_p, OwCMISValueConverter<T, O> converter_p)
    {
        super(propertyClass_p);
        this.property = property_p;
        this.converter = converter_p;

    }

    @Override
    public Property<T> getNativeObject()
    {
        return property;
    }

    @Override
    protected O[] toArrayValue() throws OwException
    {
        if (property == null)
        {
            return null;
        }
        else
        {
            List<T> nativeValues = property.getValues();
            return converter.toArrayValue(nativeValues);
        }
    }

    @Override
    protected O toValue() throws OwException
    {
        if (property == null)
        {
            return null;
        }
        else
        {
            List<T> nativeValues = property.getValues();
            return converter.toValue(nativeValues);
        }
    }

    private void replaceNativeValues(List<T> values_p)
    {
        List<T> liveValues = property.getValues();
        liveValues.clear();
        liveValues.addAll(values_p);
    }

    @Override
    protected void fromValue(O value_p) throws OwException
    {
        List<T> nativeValues = converter.fromValue(value_p);
        replaceNativeValues(nativeValues);
    }

    @Override
    protected void fromArrayValue(O[] value_p) throws OwException
    {
        List<T> nativeValues = converter.fromArrayValue(value_p);
        replaceNativeValues(nativeValues);
    }

    @Override
    public OwCMISProperty<O> clone() throws CloneNotSupportedException
    {
        PropertyDefinition<T> propertyDefinition = property.getDefinition();
        OwCMISNativePropertyClass<O, T, ? extends PropertyDefinition<T>> myPropertyClass = getPropertyClass();
        List<T> valuesClone = new LinkedList<T>(property.getValues());
        Property<T> propertyClone = new PropertyImpl<T>(propertyDefinition, valuesClone);
        return new OwCMISNativePropertyImpl<O, T>(myPropertyClass, propertyClone, converter);
    }
}
