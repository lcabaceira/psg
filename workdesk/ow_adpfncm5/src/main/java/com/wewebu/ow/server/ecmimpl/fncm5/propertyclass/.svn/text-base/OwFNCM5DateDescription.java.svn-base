package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;
import java.util.Date;

import com.filenet.api.meta.PropertyDescriptionDateTime;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * Date property class representation.
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
public class OwFNCM5DateDescription extends OwFNCM5DescriptionPropertyClass<PropertyDescriptionDateTime, Date, Date>
{

    public OwFNCM5DateDescription(PropertyDescriptionDateTime propertyDescription_p, OwFNCM5ValueConverterClass<Date, Date> converterClass_p, boolean nameProperty_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, nameProperty_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws Exception
    {
        return OwFNCM5PropertyOperators.DATE_OPERATORS.list;
    }

}
