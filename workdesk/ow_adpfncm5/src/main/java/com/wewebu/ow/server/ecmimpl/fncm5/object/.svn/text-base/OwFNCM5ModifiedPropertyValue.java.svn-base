package com.wewebu.ow.server.ecmimpl.fncm5.object;

import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5EngineProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.history.OwHistoryModifiedPropertyValue;
import com.wewebu.ow.server.history.OwStandardHistoryModifiedPropertyValue;

/**
 *<p>
 * OwFNCM5ModifiedPropertyValue.
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
public class OwFNCM5ModifiedPropertyValue implements OwHistoryModifiedPropertyValue
{

    private static final String NO_PROPERTY_STRING = "?";

    private OwFieldDefinition fieldDefinition;
    private OwFNCM5EngineProperty<?, ?> newProperty;
    private OwFNCM5EngineProperty<?, ?> oldProperty;

    public OwFNCM5ModifiedPropertyValue(OwFNCM5EngineProperty<?, ?> newProperty_p, OwFNCM5EngineProperty<?, ?> oldProperty_p)
    {
        this(newProperty_p, oldProperty_p, newProperty_p.getPropertyClass());
    }

    public OwFNCM5ModifiedPropertyValue(OwFNCM5EngineProperty<?, ?> newProperty_p, OwFNCM5EngineProperty<?, ?> oldProperty_p, OwFieldDefinition fieldDefinition_p)
    {
        this.newProperty = newProperty_p;
        this.oldProperty = oldProperty_p;
        this.fieldDefinition = fieldDefinition_p;
    }

    public String toString()
    {
        StringBuffer ret = new StringBuffer();

        ret.append(getClassName());
        ret.append(" : ");
        String oldString = getOldValueString();
        String newString = getNewValueString();
        ret.append(oldString);
        ret.append(" -> ");
        ret.append(newString);

        return ret.toString();
    }

    public String getClassName()
    {
        OwFNCM5EnginePropertyClass<?, ?, ?> newPropertyClass = newProperty.getPropertyClass();
        return newPropertyClass.getDisplayName(null);
    }

    public OwFieldDefinition getFieldDefinition()
    {
        return fieldDefinition;
    }

    protected Object valueObjectOf(OwFNCM5EngineProperty<?, ?> engineProperty_p) throws OwException
    {
        if (engineProperty_p != null)
        {
            //        Object value = engineProperty_p.getNativeValue();
            Object value = engineProperty_p.getValue();
            return value;
        }
        else
        {
            throw new OwObjectNotFoundException("null property value request");
        }
    }

    protected Object getNewValueObject() throws OwException
    {
        return valueObjectOf(newProperty);
    }

    protected Object getOldValueObject() throws OwException
    {
        return valueObjectOf(oldProperty);
    }

    protected OwField valueOf(OwFNCM5EngineProperty<?, ?> property_p) throws OwException
    {
        Object value = valueObjectOf(property_p);
        return new OwStandardHistoryModifiedPropertyValue.OwModifiedField(value, this.fieldDefinition);
    }

    protected String valueStringOf(OwFNCM5EngineProperty<?, ?> property_p)
    {
        if (property_p != null)
        {
            try
            {
                Object value = valueObjectOf(property_p);
                if (value != null)
                {
                    return value.toString();
                }
                else
                {
                    return "";
                }
            }
            catch (OwException e)
            {
                OwFNCM5Event.LOG.error("OwFNCM5ModifiedPropertyValue.valueStringOf: error retrieveing value.", e);
            }

        }

        return NO_PROPERTY_STRING;

    }

    public OwField getNewValue() throws OwException
    {
        return valueOf(newProperty);
    }

    public OwField getOldValue() throws OwException
    {
        return valueOf(oldProperty);
    }

    public String getOldValueString()
    {

        return valueStringOf(oldProperty);
    }

    public String getNewValueString()
    {
        return valueStringOf(newProperty);
    }
}