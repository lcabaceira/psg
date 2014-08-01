package com.wewebu.ow.server.ecmimpl.opencmis;

import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface for property class (property definition) object creations.
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
public interface OwCMISNativePropertyClassFactory
{
    /**
     * Create a corresponding {@link OwCMISNativePropertyClass} representation for provided native property definition.
     * @param className name of the returned property class ( as returned by {@link OwPropertyClass#getClassName()} )
     * @param propertyDefinition PropertyDefinition to be wrapped
     * @param objClass {@link OwCMISNativePropertyClass} object class it belongs to
     * @return OwCMISProeprtyClass
     * @throws OwException
     */
    OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> createPropertyClass(String className, PropertyDefinition<?> propertyDefinition, OwCMISNativeObjectClass<?, ?> objClass) throws OwException;

    /**
     * Factory method to create an instance of a converter.
     * Conversion will transform a String into OwObjectReference and vice versa.
     * @param session OwCMISSession for which the converter should be created (indirect resource/repository Id reference)
     * @return OwCMISValueConverter
     * @throws OwException if converter cannot be created
     */
    OwCMISValueConverter<String, OwObjectReference> createIdConverter(OwCMISSession session) throws OwException;
}
