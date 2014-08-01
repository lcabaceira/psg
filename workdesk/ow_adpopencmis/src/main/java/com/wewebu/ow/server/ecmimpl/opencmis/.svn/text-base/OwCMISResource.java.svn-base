package com.wewebu.ow.server.ecmimpl.opencmis;

import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * CMIS Resource interface.
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
public interface OwCMISResource extends OwResource
{

    /**
     * 
     * @return the name of this resource
     */
    String getName();

    String getID();

    OwCMISSession createSession() throws OwException;

    OwCMISDMSIDDecoder getDMSIDecoder();
}
