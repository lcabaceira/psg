package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISNativeProperty;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Native property classes are abstractions of property classes that 
 * do have a direct CMIS repository representation 
 * (an exact , corresponding CMIS object-type exist).
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
public interface OwCMISNativePropertyClass<V, N, D extends PropertyDefinition<N>> extends OwCMISPropertyClass<V>
{
    @Override
    D getNativeType() throws OwException;

    @Override
    OwCMISNativeProperty<V, N> from(V... value_p) throws OwException;

    OwCMISNativeProperty<?, N> from(Property<N> property_p) throws OwException;

    @Override
    OwCMISNativeObjectClass<?, ?> getObjectClass();

}
