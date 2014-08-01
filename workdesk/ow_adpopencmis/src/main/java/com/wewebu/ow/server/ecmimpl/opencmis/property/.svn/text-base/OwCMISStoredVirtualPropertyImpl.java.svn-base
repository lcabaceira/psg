package com.wewebu.ow.server.ecmimpl.opencmis.property;

import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISStoredVirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISStoredVirtualPropertyImpl.
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
public class OwCMISStoredVirtualPropertyImpl<O> extends OwCMISAbstractVirtualProperty<O, OwCMISStoredVirtualPropertyClass<O>> implements OwCMISStoredVirtualProperty<O>
{
    private Object value;

    public OwCMISStoredVirtualPropertyImpl(OwCMISStoredVirtualPropertyClass<O> propertyClass_p, OwCMISObject object_p, Object value_p)
    {
        super(propertyClass_p, object_p);
        this.value = value_p;
    }

    @Override
    public Object getNativeObject() throws OwException
    {
        return value;
    }

    @Override
    protected O toValue() throws OwException
    {
        return (O) value;
    }

    @Override
    protected O[] toArrayValue() throws OwException
    {
        return (O[]) value;
    }

    @Override
    protected void fromValue(O value_p) throws OwException
    {
        this.value = value_p;
    }

    @Override
    protected void fromArrayValue(O[] value_p) throws OwException
    {
        this.value = value_p;
    }

    @Override
    public OwCMISStoredVirtualProperty<O> clone() throws CloneNotSupportedException
    {
        return new OwCMISStoredVirtualPropertyImpl<O>(getPropertyClass(), getObject(), value);
    }

}
