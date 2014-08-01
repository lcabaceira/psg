package com.wewebu.ow.server.ecmimpl.opencmis;

/**
 *<p>
 * OwCMISDMSID.
 * Interface for DMSID handling and object resolution.
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
public interface OwCMISDMSID
{
    /**The char as String which should be used as separator in DMSID*/
    public static final String DMSID_SEPARATOR = ",";
    /**The char which should be used as separator in DMSID*/
    public static final char DMSID_SEPARATOR_CHAR = ',';

    /**
     * Returning the specific DMSID
     * with all information to resolve the returned
     * String into an object back again.
     * @return String representing the DMSID of the object
     */
    String getDMSIDString();

    /**
     * Returning the native CMIS ID of the object.
     * @return String representing the native ID.
     */
    String getCMISID();

    /**
     * Returning the resource (repository) ID.
     * @return String representing resource ID.
     */
    String getResourceID();
}
