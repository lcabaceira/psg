package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.OwAbstractCredentials;
import com.wewebu.ow.server.ecmimpl.OwCredentialsConstants;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.mandator.OwMandator;

/**
 *<p>
 * Credentials for CMIS.
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
public class OwCMISCredentials extends OwAbstractCredentials
{
    private OwMandator mandator;
    private OwUserInfo userInfo;
    private AuthenticationProvider authenticationProvider;
    private OwUsersRepository userRepository;

    public OwCMISCredentials(String user_p, String password_p) throws OwConfigurationException
    {
        super(user_p, password_p);
    }

    public OwCMISCredentials(String user_p, String password_p, AuthenticationProvider nativeAuthProvider) throws OwConfigurationException
    {
        this(user_p, password_p);
        authenticationProvider = nativeAuthProvider;
    }

    /**
     * Factory for creation of OwCMISUserInfo object. 
     * @param userName_p String name used for logged in user
     * @return OwCMISUserInfo object
     */
    protected OwUserInfo createUserInfo(String userName_p) throws OwException
    {
        if (getUserRepository() == null)
        {
            return new OwCMISUserInfo(userName_p, getMandator());
        }
        else
        {
            return getUserRepository().findUserByID(userName_p);
        }
    }

    /**
     * Get the mandator which is associated with current user.
     * @return OwMandator or null if not set
     */
    public OwMandator getMandator()
    {
        return mandator;
    }

    public OwUserInfo getUserInfo() throws OwException
    {
        if (userInfo == null)
        {
            userInfo = createUserInfo(getAuthInfo(OwCredentialsConstants.LOGIN_USR));
        }
        return userInfo;
    }

    public void invalidate() throws OwException
    {
        userInfo = null;
        mandator = null;
        authenticationProvider = null;
        userRepository = null;
    }

    /**
     * Set the OwMandator object associated with current user.
     * @param mandator_p OwMandator
     */
    public void setMandator(OwMandator mandator_p)
    {
        this.mandator = mandator_p;
    }

    /**
     * Getter for the native AuthenticationProvider, which may exist
     * based on OpenCMIS documentation.
     * @return AuthenticationProvider (or null if not available)
     */
    public AuthenticationProvider getAuthenticationProvider()
    {
        return this.authenticationProvider;
    }

    /**
     * UserRepository which can be used for User/Group selection
     * @return OwUsersRepository or null
     * @since 4.2.0.0
     */
    public OwUsersRepository getUserRepository()
    {
        return userRepository;
    }

    /**
     * Set current OwUsersRepository
     * @param userRepository OwUsersRepository (can be null)
     * @since 4.2.0.0
     */
    public void setUserRepository(OwUsersRepository userRepository)
    {
        this.userRepository = userRepository;
    }

}
