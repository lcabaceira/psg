package com.wewebu.ow.server.ecmimpl.opencmis;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * DMSID for the CMIS objects encoder/decoder interface.
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
public interface OwCMISDMSIDDecoder
{

    /**
     * Return the DMSID prefix that this CODEC uses.
     * @return String representing the DMISD prefix
     */
    String getDMSIDPrefix();

    /**
     * Creates an CMIS object DMSID form a given DMSID String
     * @param dmsid_p
     * @return the CMIS DMS ID
     */
    OwCMISDMSID createDMSID(String dmsid_p) throws OwException;

}
