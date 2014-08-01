package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.meta.PropertyDescriptionId;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5IdDescription.
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
public class OwFNCM5IdDescription extends OwFNCM5StringBasedDescription<PropertyDescriptionId, Id>
{

    public OwFNCM5IdDescription(PropertyDescriptionId propertyDescription_p, OwFNCM5ValueConverterClass<Id, String> converterClass_p, boolean nameProperty_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, nameProperty_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws OwException
    {
        return OwFNCM5PropertyOperators.ID_OPERATORS.list;
    }
}
