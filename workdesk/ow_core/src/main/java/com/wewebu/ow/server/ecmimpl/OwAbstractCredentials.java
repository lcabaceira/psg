package com.wewebu.ow.server.ecmimpl;

import java.util.HashMap;

import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstract implementation of OwCredentials.
 * The method getAuthInfo is implemented based on a map of strings.
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
 * @since 4.0.0.0
 */
public abstract class OwAbstractCredentials implements OwCredentials
{
    private HashMap<String, String> authInfo;

    public OwAbstractCredentials()
    {
        authInfo = new HashMap<String, String>();
    }

    public OwAbstractCredentials(String user_p)
    {
        this(user_p, null);
    }

    public OwAbstractCredentials(String user_p, String password_p)
    {
        this();
        setAuthInfo(OwCredentialsConstants.LOGIN_USR, user_p);
        setAuthInfo(OwCredentialsConstants.LOGIN_PWD, password_p);
    }

    /**
     * @see OwCredentialsConstants
     */
    public String getAuthInfo(String infoKey_p)
    {
        return authInfo.get(infoKey_p);
    }

    /**
     * Define a specific authentication key-value association.
     * @param key_p String key association part
     * @param value_p String value association part
     * @return String which was previous set for given key, by default null
     * @see #getAuthInfo(String)
     */
    protected String setAuthInfo(String key_p, String value_p)
    {
        return authInfo.put(key_p, value_p);
    }

    public String getSecurityToken(OwResource resource_p)
    {
        return "";
    }

    public boolean isContainerBasedAuthenticated() throws OwException
    {
        return false;
    }

}
