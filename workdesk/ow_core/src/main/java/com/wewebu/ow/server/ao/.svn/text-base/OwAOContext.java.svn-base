package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.ecm.OwNetwork;

/**
 *<p>
 *Application objects usage context.
 *Defines the configuration , corresponding network and role management options that 
 *will govern application objects created within this context. 
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
 *@since 4.2.0.0
 */
public interface OwAOContext
{
    OwNetwork<?> getNetwork();

    OwConfiguration getConfiguration();

    boolean isRoleManaged();
}
