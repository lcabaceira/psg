package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import org.restlet.resource.ClientProxy;
import org.restlet.resource.Delete;

/**
 *<p>
 * Models the /processes/{processId}/items/{itemId} resource
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
 *@since 4.2.0.0.0
 */
public interface ProcessInstanceItemResource extends ClientProxy
{
    @Delete
    public void delete();
}
