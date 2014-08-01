package com.wewebu.ow.server.ecmimpl.opencmis.object;

import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

/**
 *<p>
 * OwCMISContextBoundObject.
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
public class OwCMISContextBoundObject<N extends TransientCmisObject>
{
    public final OperationContext context;
    public final N object;

    public OwCMISContextBoundObject(N object, OperationContext context)
    {
        super();
        this.object = object;
        this.context = context;
    }

}
