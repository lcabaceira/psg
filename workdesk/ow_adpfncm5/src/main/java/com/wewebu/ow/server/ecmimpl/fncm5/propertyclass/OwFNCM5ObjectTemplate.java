package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.admin.PropertyTemplateObject;
import com.filenet.api.core.EngineObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;

/**
 *<p>
 * OwFNCM5ObjectTemplate.
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
public class OwFNCM5ObjectTemplate extends OwFNCM5TemplatePropertyClass<PropertyTemplateObject, EngineObject, OwObject>
{

    public OwFNCM5ObjectTemplate(PropertyTemplateObject nativeClass_p, OwFNCM5ValueConverterClass<EngineObject, OwObject> converterClass_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

}
