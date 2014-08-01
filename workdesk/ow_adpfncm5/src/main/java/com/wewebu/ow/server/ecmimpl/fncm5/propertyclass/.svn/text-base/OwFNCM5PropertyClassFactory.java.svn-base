package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.meta.PropertyDescription;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface of Factory for PropertyClass creation.
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
public interface OwFNCM5PropertyClassFactory
{

    <C extends PropertyDescription> OwFNCM5EnginePropertyClass<C, ?, ?> createPropertyClass(C propertyDescription_p, boolean nameProperty_p) throws OwException;

    <T extends PropertyTemplate> OwFNCM5EnginePropertyClass<T, ?, ?> createPropertyClass(T propertyTemplate_p) throws OwException;

    <D extends PropertyDefinition> OwFNCM5EnginePropertyClass<D, ?, ?> createPropertyClass(D propertyDefinition_p, boolean orderable_p, boolean searchable_p, boolean selectable_p) throws OwException;

}
