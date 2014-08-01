package com.wewebu.ow.server.auth;

import com.wewebu.ow.server.ecm.OwCredentials;

/**
 *<p>
 * Alfresco Workdesk credentials based authentication.
 * This token associates an authenticated principal with its corresponding {@link OwCredentials} 
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
public class OwCredentialsAuthentication implements OwAuthentication
{
    private static final long serialVersionUID = -4213235467498307651L;

    transient private OwCredentials credentials;
    private String userName;
    private String password;

    /**
     * Simple non authenticated OwCredentialsAuthentication constructor
     * @param userName_p
     * @param password_p
     * @since 4.2.0.0
     */
    public OwCredentialsAuthentication(String userName_p, String password_p)
    {
        this(null, userName_p, password_p);
    }

    public OwCredentialsAuthentication(OwCredentials credentials_p, String userName_p, String password_p)
    {
        super();
        this.credentials = credentials_p;
        this.userName = userName_p;
        this.password = password_p;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public OwCredentials getOWDCredentials()
    {
        return credentials;
    }

    public boolean isAuthenticated()
    {
        return credentials != null;
    }

    public String getName()
    {
        return userName;
    }
}
