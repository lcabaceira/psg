package com.wewebu.ow.server.ecmimpl.fncm5.dmsid;

/**
 *<p>
 * Interface for DMSID handling.
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
public interface OwFNCM5DMSID
{
    /**The char as String which should be used as separator in DMSID*/
    public static final String DMSID_SEPARATOR = ",";
    /**The char which should be used as separator in DMSID*/
    public static final char DMSID_SEPARATOR_CHAR = ',';

    /**
     * Return the resulting DMSID. 
     * @return String representing the DMSID
     */
    String getDMSID();

    /**
     * Returning the native object ID of the object.
     * @return String representing the native ID.
     */
    String getObjectID();

    /**
     * Returning the resource (repository) ID.
     * @return String representing resource ID.
     */
    String getResourceID();

    /**
     * For easier handling, the toString method of an OwFNCM5DMSID
     * implementing class should return the same as the {@link #getDMSID()}
     * object.
     * @return String representing the DMSID
     */
    abstract String toString();
}
