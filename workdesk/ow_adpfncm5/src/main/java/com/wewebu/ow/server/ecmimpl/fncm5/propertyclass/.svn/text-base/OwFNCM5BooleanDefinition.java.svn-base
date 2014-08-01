package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.admin.PropertyDefinitionBoolean;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5BooleanDefinition.
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
public class OwFNCM5BooleanDefinition extends OwFNCM5DefinitionPropertyClass<PropertyDefinitionBoolean, Boolean, Boolean>
{

    public OwFNCM5BooleanDefinition(PropertyDefinitionBoolean nativeClass_p, OwFNCM5ValueConverterClass<Boolean, Boolean> converterClass_p, boolean orderable, boolean searchable, boolean selectable, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, orderable, searchable, selectable, preferredType_p);
    }

    public Collection<Integer> getOperators() throws OwException
    {
        return OwFNCM5PropertyOperators.BOOLEAN_OPERATORS.list;
    }

}
