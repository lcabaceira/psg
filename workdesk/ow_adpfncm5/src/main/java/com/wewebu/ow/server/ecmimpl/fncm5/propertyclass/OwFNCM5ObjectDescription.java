package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.core.EngineObject;
import com.filenet.api.meta.PropertyDescriptionObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5ObjectDescription.
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
public class OwFNCM5ObjectDescription extends OwFNCM5DescriptionPropertyClass<PropertyDescriptionObject, EngineObject, OwObject>
{

    public OwFNCM5ObjectDescription(PropertyDescriptionObject propertyDescription_p, OwFNCM5ValueConverterClass<EngineObject, OwObject> converterClass_p, boolean nameProperty_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, nameProperty_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws Exception
    {
        return null;
    }

}
