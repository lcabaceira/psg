package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Arrays;
import java.util.List;

import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISStoredVirtualPropertyImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISVirtualProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchOperator;

/**
 *<p>
 *  {@link OwResource#m_ClassDescriptionPropertyClass} virtual property class.
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
public class OwCMISClassDescriptionPropertyClass extends OwCMISAbstractStoredPropertyClass<String, OwCMISNativeObjectClass<? extends TypeDefinition, ?>>
{

    private static final List<Integer> OPERATORS = Arrays.asList(OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL);

    public OwCMISClassDescriptionPropertyClass(OwCMISNativeObjectClass<?, ?> objectClass)
    {
        super(OwResource.m_ClassDescriptionPropertyClass.getClassName(), OwResource.m_ClassDescriptionPropertyClass, OPERATORS, objectClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwCMISVirtualProperty<String> from(OwCMISObject object_p) throws OwException
    {
        OwCMISNativeObjectClass<? extends TypeDefinition, ?> myObjectClass = (OwCMISNativeObjectClass<? extends TypeDefinition, ?>) object_p.getObjectClass();
        TypeDefinition objectType = myObjectClass.getNativeObject();

        return new OwCMISStoredVirtualPropertyImpl<String>(this, object_p, objectType.getDisplayName());
    }
}
