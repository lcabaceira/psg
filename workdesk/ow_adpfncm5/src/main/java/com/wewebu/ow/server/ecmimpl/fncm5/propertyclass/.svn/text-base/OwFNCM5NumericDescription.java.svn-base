package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.meta.PropertyDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstract class for all numeric representations.
 * Contains a static search operation collection which is supported
 * by numeric values.
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
public abstract class OwFNCM5NumericDescription<C extends PropertyDescription, N, O> extends OwFNCM5DescriptionPropertyClass<C, N, O>
{
    public OwFNCM5NumericDescription(C propertyDescription_p, OwFNCM5ValueConverterClass<N, O> converterClass_p, boolean nameProperty_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, nameProperty_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws OwException
    {
        return OwFNCM5PropertyOperators.NUMERIC_OPERATORS.list;
    }

}
