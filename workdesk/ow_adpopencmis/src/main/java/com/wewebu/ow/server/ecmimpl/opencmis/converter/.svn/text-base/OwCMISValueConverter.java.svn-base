package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.List;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwCMISValueConverter.
 * Interface for conversion from type L (base) to O (resulting), and also 
 * to transform from array to list collections.
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
public interface OwCMISValueConverter<L, O>
{
    //TODO: refactor to single convert method (from array and non-array to all-in-one) 

    /**
     * Return a representation of the given List as type O,
     * or simply converts the first entry of the list into the resulting type.
     * @param cmisValue_p List of values to convert
     * @return Resulting type or null
     * @throws OwInvalidOperationException
     */
    O toValue(List<L> cmisValue_p) throws OwInvalidOperationException;

    /**
     * Vice-versa conversion to {@link #toValue(List)}.
     * @param owdValue_p O resulting type, can be null
     * @return List of base types, or empty list
     * @throws OwInvalidOperationException
     */
    List<L> fromValue(O owdValue_p) throws OwInvalidOperationException;

    /**
     * Converts the list into an array of the resulting type.
     * @param cmisValue_p List to be converted
     * @return Array type O, or null
     * @throws OwInvalidOperationException
     */
    O[] toArrayValue(List<L> cmisValue_p) throws OwInvalidOperationException;

    /**
     * Converts the array back from type O to a list of type L.
     * @param owdValue_p array of resulting type, can be null
     * @return a list of base types, or empty list
     * @throws OwInvalidOperationException
     */
    List<L> fromArrayValue(Object[] owdValue_p) throws OwInvalidOperationException;

}
