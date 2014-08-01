package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.admin.PropertyDefinitionId;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5IdDefinition.
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
public class OwFNCM5IdDefinition extends OwFNCM5StringBasedDefinition<PropertyDefinitionId, Id>
{

    public OwFNCM5IdDefinition(PropertyDefinitionId nativeClass_p, OwFNCM5ValueConverterClass<Id, String> converterClass_p, boolean orderable_p, boolean searchable_p, boolean selectable_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, orderable_p, searchable_p, selectable_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws Exception
    {
        return OwFNCM5PropertyOperators.ID_OPERATORS.list;
    }

}
