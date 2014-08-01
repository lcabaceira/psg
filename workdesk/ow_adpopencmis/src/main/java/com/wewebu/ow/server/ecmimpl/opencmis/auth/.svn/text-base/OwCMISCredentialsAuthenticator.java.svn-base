package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import java.util.Map;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.alfresco.wd.ext.restlet.auth.OwRestletBasicAuthenticationHandler;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.exceptions.CmisPermissionDeniedException;
import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.auth.OwAuthentication;
import com.wewebu.ow.server.auth.OwCredentialsAuthentication;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.ecmimpl.OwCredentialsConstants;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * CMIS credentials based authenticator. 
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
public class OwCMISCredentialsAuthenticator extends OwCMISAbstractAuthenticator
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISCredentialsAuthenticator.class);

    /**
    * @param initializer OwBaseInitializer
    */
    public OwCMISCredentialsAuthenticator(OwBaseInitializer initializer)
    {
        super(initializer);
    }

    /**
     * @param network
     */
    public OwCMISCredentialsAuthenticator(OwCMISNetwork network)
    {
        super(network);
    }

    public OwAuthentication authenticate(OwAuthentication authentication_p) throws OwAuthenticationException, OwConfigurationException, OwServerException
    {
        String userName = authentication_p.getUserName();
        String password = authentication_p.getPassword();

        if (userName != null && password != null)
        {
            OwCMISCredentials credentials = null;

            try
            {
                SessionFactoryImpl sessionFactory = SessionFactoryImpl.newInstance();
                Map<String, String> opencmisParameters = buildOpenCmisParameters(userName, password, null);
                AuthenticationProvider authProv = createAuthenticationProvider(opencmisParameters);
                if (authProv != null)
                {
                    sessionFactory.getRepositories(opencmisParameters, null, authProv, null);
                }
                else
                {
                    sessionFactory.getRepositories(opencmisParameters);
                }
                credentials = createCredentials(userName, password, authProv);
            }
            catch (CmisPermissionDeniedException e)
            {
                LOG.debug("OwCMISCredentialsAuthenticator.authenticate(): Cmis exception unallowed!", e);
                throw new OwAuthenticationException(new OwString("opencmis.auth.OwCMISCredentialsAuthenticator.err.login.failed", "Login failed: Wrong username or password!"), e);
            }
            catch (OwException owe)
            {
                LOG.error("Could not create credentials!", owe);
                throw new OwServerException("Invalid configuration exception!", owe);
            }

            OwCredentialsAuthentication credentialsAuthentication = new OwCredentialsAuthentication(credentials, userName, password);

            return credentialsAuthentication;
        }
        else
        {
            LOG.debug("OwCMISCredentialsAuthenticator.authenticate(): Login failed: Wrong username or password!");
            throw new OwAuthenticationException(new OwString("opencmis.auth.OwCMISCredentialsAuthenticator.err.login.failed", "Login failed: Wrong username or password!"));
        }
    }

    /**
     * Factory method for credentials objects.
     * @param user_p String user login name
     * @param password_p String login password
     * @param nativeAuthProvider AuthenticationProvider which should be used for OpenCMIS authentication (can be null)
     * @return OwCMISCredentials
     * @throws OwException
     */
    protected OwCMISCredentials createCredentials(String user_p, String password_p, AuthenticationProvider nativeAuthProvider) throws OwException
    {
        return new OwCMISCredentials(user_p, password_p, nativeAuthProvider);
    }

    /**
     * Factory to create an instance of OwCMISRestletAuthenticationHandler,
     * which is used in for authentication handling in non-CMIS calls.
     * <p>
     * By Default returns OwCMISBasicRestletAuthenticationHandler which is an HTTP-Basic authentication handler.
     * </p>
     * @param cred OwCMISCredentials 
     * @return OwCMISRestletAuthenticationHandler
     * @since 4.2.0.0
     */
    @Override
    public OwRestletAuthenticationHandler createRestletAuthenticationHandler(OwCMISCredentials cred) throws OwException
    {
        return new OwRestletBasicAuthenticationHandler(cred.getAuthInfo(OwCredentialsConstants.LOGIN_USR), cred.getAuthInfo(OwCredentialsConstants.LOGIN_PWD));
    }

}