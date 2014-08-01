package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import com.filenet.api.collection.EngineCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Converter class interface.
 * Implementors will act as a provider of converters and converters 
 * context independent behavior.
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

public interface OwFNCM5ValueConverterClass<N, O>
{

    /**
     * Return the java.lang.Class which should be the 
     * resulting AWD type at end of conversion.
     * @return a Class
     */
    Class<O> getOClass();

    /**
     * 
     * @param class_p 
     * @return a converter object corresponding to this converter-class object   
     */
    OwFNCM5ValueConverter<N, O> createConverter(OwFNCM5EnginePropertyClass<?, N, O> class_p);

    /**
     * Converts native values to AWD values.
     * @param nativeValue_p List of values to convert
     * @return Resulting type or null
     * @throws OwException
     */
    O convertNativeValue(N nativeValue_p) throws OwException;

    /**
     * Converts the native engine collection into an AWD values array.
     * @param engineCollection_p 
     * @return Array type O, or null
     * @throws OwException
     */
    O[] convertEngineCollection(EngineCollection engineCollection_p) throws OwException;

}
