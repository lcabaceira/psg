package com.wewebu.ow.server.servlets;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Init listener that initializes the Alfresco Workdesk configuration data upon application start up
 * (like log4j, spring JDBC template...).
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
public class OwConfigurationInitialisingContextListener extends OwInitialisingContextListener
{

    protected void applicationInitalize() throws OwConfigurationException, OwServerException
    {
        // delegate to configuration initializing method
        OwConfiguration.applicationInitalize(this);
    }

}