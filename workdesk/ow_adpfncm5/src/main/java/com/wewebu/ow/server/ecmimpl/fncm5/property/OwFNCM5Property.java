package com.wewebu.ow.server.ecmimpl.fncm5.property;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyComparator;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * P8 5.0 property implementation.
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
public abstract class OwFNCM5Property implements OwProperty, Cloneable
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Property.class);

    private OwFNCM5PropertyClass propertyClass;

    public OwFNCM5Property(OwFNCM5PropertyClass propertyClass_p)
    {
        this.propertyClass = propertyClass_p;
    }

    public OwFNCM5PropertyClass getPropertyClass()
    {
        return this.propertyClass;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        return this.propertyClass;
    }

    public abstract boolean isReadOnly(int iContext_p) throws OwException;

    public abstract void setValue(Object oValue_p) throws OwException;

    public abstract Object getValue() throws OwException;

    public final int compareTo(Object o_p)
    {
        return OwPropertyComparator.legacyCompare(this, o_p);
    }

}
