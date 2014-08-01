package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;

import com.filenet.api.admin.PropertyDefinitionObject;
import com.filenet.api.core.EngineObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5ObjectDefinition.
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
public class OwFNCM5ObjectDefinition extends OwFNCM5DefinitionPropertyClass<PropertyDefinitionObject, EngineObject, OwObject>
{

    public OwFNCM5ObjectDefinition(PropertyDefinitionObject nativeClass_p, OwFNCM5ValueConverterClass<EngineObject, OwObject> converterClass_p, boolean orderable_p, boolean searchable_p, boolean selectable_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, orderable_p, searchable_p, selectable_p, preferredType_p);
    }

    public Collection<Integer> getOperators() throws OwException
    {
        return null;
    }

}
