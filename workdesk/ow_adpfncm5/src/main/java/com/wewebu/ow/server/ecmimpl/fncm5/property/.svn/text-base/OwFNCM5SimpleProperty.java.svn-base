package com.wewebu.ow.server.ecmimpl.fncm5.property;

import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Simple property implementation, containing only a value the the referenced
 * OwFNCM5PropertyClass for given value. 
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
public class OwFNCM5SimpleProperty extends OwFNCM5Property
{
    private Object value;

    public OwFNCM5SimpleProperty(Object value_p, OwFNCM5PropertyClass propertyClass_p)
    {
        super(propertyClass_p);
        this.value = value_p;
    }

    public boolean isReadOnly(int iContext_p) throws OwException
    {
        return getPropertyClass().isReadOnly(iContext_p);
    }

    public boolean isHidden(int iContext_p) throws Exception
    {
        return getPropertyClass().isHidden(iContext_p);
    }

    public Object getNativeObject() throws Exception
    {
        return value;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object oValue_p)
    {
        this.value = oValue_p;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new OwFNCM5SimpleProperty(this.value, getPropertyClass());
    }

}
