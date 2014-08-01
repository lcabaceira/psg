package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISStoredVirtualPropertyImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISVirtualProperty;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISLinkVirtualIdPropertyClass, helper class.
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
public class OwCMISLinkVirtualIdPropertyClass extends OwCMISAbstractStoredPropertyClass<OwObjectReference, OwCMISNativeObjectClass<? extends TypeDefinition, ?>>
{
    public OwCMISLinkVirtualIdPropertyClass(OwPropertyClass internalPropertyClass, OwCMISNativeObjectClass<? extends TypeDefinition, ?> objectClass)
    {
        super(internalPropertyClass.getClassName(), internalPropertyClass, null, objectClass);
    }

    @Override
    public OwCMISVirtualProperty<OwObjectReference> from(OwCMISObject object_p) throws OwException
    {
        String propertyId = getNonQualifiedName().equals(OwObjectLink.OW_LINK_SOURCE) ? PropertyIds.SOURCE_ID : PropertyIds.TARGET_ID;
        OwCMISProperty<?> prop = object_p.getProperty(propertyId);
        Object value = prop.getValue();
        if (value instanceof OwObjectReference)
        {
            return new OwCMISStoredVirtualPropertyImpl<OwObjectReference>(this, object_p, value);
        }
        else
        {
            OwCMISNativeSession session = getObjectClass().getSession();
            OwCMISValueConverter<String, OwObjectReference> idCon = session.getNativePropertyClassFactory().createIdConverter(session);

            List<String> values = new LinkedList<String>();
            values.add(value.toString());

            return new OwCMISStoredVirtualPropertyImpl<OwObjectReference>(this, object_p, idCon.toValue(values));
        }
    }

}
