package com.wewebu.ow.server.auth;

import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 *  Interface for authentication process.
 *  If an authentication need to be processed, 
 *  this interface defines an abstraction.
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
 *@since 4.0.0.0
 */
public interface OwAuthenticator
{

    /**
     * Authenticate with provided information.
     * @param authentication OwAuthentication source to use for authentication
     * @throws OwAuthenticationException if could not authenticate
     * @throws OwConfigurationException if configuration is incorrect
     * @throws OwServerException if authentication system is not available/reachable
     */
    OwAuthentication authenticate(OwAuthentication authentication) throws OwAuthenticationException, OwConfigurationException, OwServerException;
}
