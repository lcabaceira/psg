package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.admin.PropertyTemplate;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5NumericTemplate.
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
public abstract class OwFNCM5NumericTemplate<C extends PropertyTemplate, N, O> extends OwFNCM5TemplatePropertyClass<C, N, O>
{

    public OwFNCM5NumericTemplate(C nativeClass_p, OwFNCM5ValueConverterClass<N, O> converterClass_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws Exception
    {
        return OwFNCM5PropertyOperators.NUMERIC_OPERATORS.list;
    }

}
