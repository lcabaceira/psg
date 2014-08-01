package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.filenet.api.admin.PropertyTemplateInteger32;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5IntegerTemplate.
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
public class OwFNCM5IntegerTemplate extends OwFNCM5NumericTemplate<PropertyTemplateInteger32, Integer, Integer>
{

    public OwFNCM5IntegerTemplate(PropertyTemplateInteger32 nativeClass_p, OwFNCM5ValueConverterClass<Integer, Integer> converterClass_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, preferredType_p);
    }

}
