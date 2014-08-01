package com.wewebu.ow.server.ecmimpl.fncm5.dmsid;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Decoder interface for the DMSID.
 * Providing basic methods to get the all parts form
 * DMISD or to create the object wrapper for a DMSID.
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
public interface OwFNCM5DMSIDDecoder
{
    /**
     * Returns an array of String which represents the 
     * parts of the provided DMSID object.
     * @param dmsid OwFNCM5DMSID
     * @return String array 
     */
    String[] getParts(OwFNCM5DMSID dmsid);

    /**
     * Return the parts of the DMSID.
     * Will split the given String into an
     * array of DMSID parts.
     * @param objDmsid String
     * @return String array
     */
    String[] getParts(String objDmsid);

    /**
     * Creates an OwFNCM5DMSID from provided String.
     * Will verify if the DMSID is correct, otherwise
     * an exception will be thrown. 
     * @param dmsid String representing the DMSID
     * @return OwFNCM5DMSID object created from String
     * @throws OwInvalidOperationException if the provided String does not match the definitions of
     * the current defined DMSID structure
     */
    OwFNCM5DMSID getDmsidObject(String dmsid) throws OwInvalidOperationException;
}
