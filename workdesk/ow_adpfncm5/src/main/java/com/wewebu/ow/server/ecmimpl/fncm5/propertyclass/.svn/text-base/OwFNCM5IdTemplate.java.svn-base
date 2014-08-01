package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.admin.PropertyTemplateId;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5IdTemplate.
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
public class OwFNCM5IdTemplate extends OwFNCM5StringBasedTemplate<PropertyTemplateId, Id>
{

    public OwFNCM5IdTemplate(PropertyTemplateId nativeClass_p, OwFNCM5ValueConverterClass<Id, String> converterClass_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws Exception
    {
        return OwFNCM5PropertyOperators.ID_OPERATORS.list;
    }

}
