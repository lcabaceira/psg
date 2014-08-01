package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.filenet.api.admin.PropertyDefinitionFloat64;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5DoubleDefinition.
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
public class OwFNCM5DoubleDefinition extends OwFNCM5NumericDefinition<PropertyDefinitionFloat64, Double, Double>
{

    public OwFNCM5DoubleDefinition(PropertyDefinitionFloat64 nativeClass_p, OwFNCM5ValueConverterClass<Double, Double> converterClass_p, boolean orderable_p, boolean searchable_p, boolean selectable_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, orderable_p, searchable_p, selectable_p, preferredType_p);
    }

}
