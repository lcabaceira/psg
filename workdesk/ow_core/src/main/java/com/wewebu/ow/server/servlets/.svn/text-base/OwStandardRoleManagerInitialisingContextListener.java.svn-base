package com.wewebu.ow.server.servlets;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.role.OwStandardRoleManager;

/**
 *<p>
 * Init listener that initializes the standard role manager config data upon application start up.
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

public class OwStandardRoleManagerInitialisingContextListener extends OwInitialisingContextListener
{

    protected void applicationInitalize() throws OwConfigurationException, OwServerException
    {
        // delegate to standard role manager
        OwStandardRoleManager.applicationInitalize(this);
    }
}