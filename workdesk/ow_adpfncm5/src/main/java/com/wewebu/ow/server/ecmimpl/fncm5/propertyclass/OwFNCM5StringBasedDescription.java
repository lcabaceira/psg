package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.constants.TypeID;
import com.filenet.api.meta.PropertyDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * String property class representation.
 * This class represent two types simple Strings and also Id property definitions.
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
public abstract class OwFNCM5StringBasedDescription<C extends PropertyDescription, N> extends OwFNCM5DescriptionPropertyClass<C, N, String>
{

    public OwFNCM5StringBasedDescription(C propertyDescription_p, OwFNCM5ValueConverterClass<N, String> converterClass_p, boolean nameProperty_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, nameProperty_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws OwException
    {
        if (getType() == TypeID.STRING_AS_INT)
        {
            return OwFNCM5PropertyOperators.ID_OPERATORS.list;
        }
        else
        {
            return OwFNCM5PropertyOperators.STRING_OPERATORS.list;
        }
    }

}
