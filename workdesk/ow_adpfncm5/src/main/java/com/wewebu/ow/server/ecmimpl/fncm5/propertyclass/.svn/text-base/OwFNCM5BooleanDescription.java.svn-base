package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.meta.PropertyDescriptionBoolean;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Boolean property class representation.
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
public class OwFNCM5BooleanDescription extends OwFNCM5DescriptionPropertyClass<PropertyDescriptionBoolean, Boolean, Boolean>
{
    public OwFNCM5BooleanDescription(PropertyDescriptionBoolean propertyDescription_p, OwFNCM5ValueConverterClass<Boolean, Boolean> converterClass_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, false, preferredType_p);
    }

    public Collection<Integer> getOperators() throws OwException
    {
        return OwFNCM5PropertyOperators.BOOLEAN_OPERATORS.list;
    }

}
