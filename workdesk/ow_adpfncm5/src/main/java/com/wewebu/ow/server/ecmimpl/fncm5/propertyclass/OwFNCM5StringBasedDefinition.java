package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.filenet.api.admin.PropertyDefinition;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5StringBasedDefinition.
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
public abstract class OwFNCM5StringBasedDefinition<C extends PropertyDefinition, N> extends OwFNCM5DefinitionPropertyClass<C, N, String>
{

    public OwFNCM5StringBasedDefinition(C nativeClass_p, OwFNCM5ValueConverterClass<N, String> converterClass_p, boolean orderable_p, boolean searchable_p, boolean selectable_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, orderable_p, searchable_p, selectable_p, preferredType_p);
    }

}
