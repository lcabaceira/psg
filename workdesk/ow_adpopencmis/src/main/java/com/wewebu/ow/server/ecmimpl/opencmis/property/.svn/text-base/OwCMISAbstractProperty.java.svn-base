package com.wewebu.ow.server.ecmimpl.opencmis.property;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwPropertyComparator;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 *  OwCMISAbstractProperty.
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
public abstract class OwCMISAbstractProperty<O, C extends OwCMISPropertyClass<O>> implements OwCMISProperty<O>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractProperty.class);

    private C propertyClass;

    public OwCMISAbstractProperty(C propertyClass_p)
    {
        this.propertyClass = propertyClass_p;
    }

    @Override
    public boolean isReadOnly(int iContext_p) throws OwException
    {
        return propertyClass.isReadOnly(iContext_p);
    }

    @Override
    public boolean isHidden(int iContext_p) throws OwException
    {
        return propertyClass.isHidden(iContext_p);
    }

    protected abstract O toValue() throws OwException;

    protected abstract O[] toArrayValue() throws OwException;

    protected abstract void fromValue(O value_p) throws OwException;

    protected abstract void fromArrayValue(O[] value_p) throws OwException;

    @Override
    public final Object getValue() throws OwException
    {
        if (getPropertyClass().isArray())
        {
            return toArrayValue();
        }
        else
        {
            return toValue();
        }
    }

    @Override
    public final void setValue(Object value_p) throws OwException
    {
        if (getPropertyClass().isArray())
        {
            fromArrayValue((O[]) value_p);
        }
        else
        {
            fromValue((O) value_p);
        }
    }

    @Override
    public C getPropertyClass()
    {
        return propertyClass;
    }

    @Override
    public int compareTo(Object o_p)
    {
        return OwPropertyComparator.legacyCompare(this, o_p);
    }

    @Override
    public OwFieldDefinition getFieldDefinition() throws OwException
    {
        return propertyClass;
    }

    @Override
    public abstract OwCMISProperty<O> clone() throws CloneNotSupportedException;

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("cmis-property(,");
        builder.append(getClass().getName());
        builder.append(",");
        builder.append(getPropertyClass().toString());
        builder.append("),");
        Object value = "<err-value>";
        try
        {
            value = getValue();
            if (value == null)
            {
                value = "<null-value>";
            }
        }
        catch (OwException e)
        {
            LOG.error("Could not retrieve value!", e);
        }
        builder.append("value=");
        builder.append(value.toString());
        builder.append(")");
        return builder.toString();
    }
}
