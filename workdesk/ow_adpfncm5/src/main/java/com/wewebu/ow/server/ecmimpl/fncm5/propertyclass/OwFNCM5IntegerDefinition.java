package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.filenet.api.admin.PropertyDefinitionInteger32;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5IntegerDefinition.
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
public class OwFNCM5IntegerDefinition extends OwFNCM5NumericDefinition<PropertyDefinitionInteger32, Integer, Integer>
{

    public OwFNCM5IntegerDefinition(PropertyDefinitionInteger32 nativeClass_p, OwFNCM5ValueConverterClass<Integer, Integer> converterClass_p, boolean orderable_p, boolean searchable_p, boolean selectable_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, orderable_p, searchable_p, selectable_p, preferredType_p);
    }

}
