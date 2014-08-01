package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Base interface for the user Credentials and Session context for the login to the ECM System. <br/>
 * Created after login to the ECM System. Holds Session information and user authentication / authorization information.
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
public interface OwCredentials
{
    /**
     * get user information object for the logged on user 
     * @return OwUserInfo for current credentials object
     * @throws Exception if could not retrieve representing OwUserInfo object
     */
    public abstract OwUserInfo getUserInfo() throws Exception;

    /**
     * close connection, invalid credentials object 
     * @throws Exception if invalidation creates problems
     */
    public abstract void invalidate() throws Exception;

    /**
     * get security token used for SSO between different applications accessing the same ECM system
     * @param resource_p OwResource of subsystem or null to use default
     * @return String a security token that can be used for authentication or an empty String if no token can be generated
     */
    public abstract String getSecurityToken(OwResource resource_p);

    /**
     * check the current user is authenticated container based
     * @return true if user is logged in and the login is done with container based authentication
     * @throws OwException if problem occur during investigate of authentication
     */
    public abstract boolean isContainerBasedAuthenticated() throws OwException;

    /**
     * Return the String which is associated with the requested key.
     * Can return null if no association is defined for the provided
     * key.
     * @param infoKey_p String
     * @return String or null if no information available
     * @since 4.0.0.0
     */
    public abstract String getAuthInfo(String infoKey_p);
}