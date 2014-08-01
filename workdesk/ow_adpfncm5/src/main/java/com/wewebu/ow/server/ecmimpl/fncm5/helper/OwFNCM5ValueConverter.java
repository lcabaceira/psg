package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import com.filenet.api.collection.EngineCollection;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Value converter interface.
 * Based on the P8 API, a converter will transform the results into 
 * &quot;native&quot; Java and also back into P8 API representation.
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
public interface OwFNCM5ValueConverter<N, O>
{

    /**
     * Convert to native array representation.
     * Will return null if provided array is null or empty.
     * @param owdArray_p O type array to convert 
     * @return com.filenet.api.collection.EngineCollection or null.
     */
    EngineCollection toEngineCollection(O[] owdArray_p) throws OwException;

    /**
     * Convert an O type into the specific native value representation.
     * Will return null if provided value is null.
     * @param owdValue_p O type to convert
     * @return N native type or null
     */
    N toNativeValue(O owdValue_p) throws OwException;

    /**
     * Convert the native ECM array into a specific representation.
     * Will return null if native ECM array is null or empty.
     * <p>Amount of elements can vary, since null values are
     * ignored,</p>  
     * @param engineCollection_p com.filenet.api.collection.EngineCollection
     * @return O type array or null.
     */
    O[] fromEngineCollection(EngineCollection engineCollection_p) throws OwException;

    /**
     * Converting a native value to a specific representation.
     * Will return null if native value is null.
     * @param nativeValue_p ECM value to convert
     * @return O type representation or null
     */
    O fromNativeValue(N nativeValue_p) throws OwException;
}
