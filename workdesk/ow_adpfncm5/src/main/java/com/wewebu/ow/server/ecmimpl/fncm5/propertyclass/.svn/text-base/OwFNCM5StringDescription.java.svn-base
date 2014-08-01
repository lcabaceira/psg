package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.meta.PropertyDescriptionString;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5StringDescription.
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
public class OwFNCM5StringDescription extends OwFNCM5StringBasedDescription<PropertyDescriptionString, String>
{

    public OwFNCM5StringDescription(PropertyDescriptionString propertyDescription_p, OwFNCM5ValueConverterClass<String, String> converterClass_p, boolean nameProperty_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, nameProperty_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws OwException
    {
        return OwFNCM5PropertyOperators.STRING_OPERATORS.list;
    }

}
