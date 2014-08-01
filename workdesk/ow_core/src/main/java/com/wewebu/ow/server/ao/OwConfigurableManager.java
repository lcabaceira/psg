package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.exceptions.OwConfigurationException;

/**
 *<p>
 * Alfresco Workdesk application objects configurable manager interface.
 * Configuration and initialization is performed considering the {@link OwAOSupport} configurations ,
 * current manager configuration and the context of the provided application objects.  
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
public interface OwConfigurableManager
{
    void init(OwAOType<?> type, OwManagerConfiguration configuration, OwSupportsConfiguration supports, OwAOContext context) throws OwConfigurationException;
}
