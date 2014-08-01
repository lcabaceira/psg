package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 *Implementors are application object providers that have XML based configurations and 
 *can be initialized with application object contexts.
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
public interface OwAOConfigurableProvider extends OwAOProvider
{
    void init(OwXMLUtil configuration, OwAOContext context) throws OwConfigurationException;
}
