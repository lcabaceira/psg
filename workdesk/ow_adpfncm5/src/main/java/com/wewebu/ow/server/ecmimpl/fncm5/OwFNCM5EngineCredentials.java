package com.wewebu.ow.server.ecmimpl.fncm5;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.filenet.api.core.Connection;
import com.filenet.api.security.User;
import com.filenet.api.util.UserContext;
import com.filenet.apiimpl.util.J2EEUtil;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5UserInfoFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.login.OwFNCM5CallbackHandler;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwFNCM5EngineCredentials class.
 * Handle the P8 engine connection based authentication and also 
 * the creation of OwBaseUserInfo objects.
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
public class OwFNCM5EngineCredentials extends OwFNCM5Credentials
{
    /**Basic authentication , only user name and password*/
    public static final String AUTHENTICATION_MODE_NONE = "NONE";

    public static final String CONF_NODE_JAAS_CTX = "jaasContext";

    private Connection connection;
    private Subject loginSubject;
    private boolean containerBasedAuthentication;

    /**
     * Basic Authentication Credentials with user name and password.
     * @param connection_p Connection to P8 system
     * @param userName_p String user name
     * @param password_p String password
     * @param configuration_p OwXMLUtil network configuration
     * @param factory_p OwFNCM5UserInfoFactory
     * @throws OwAuthenticationException
     */
    public OwFNCM5EngineCredentials(Connection connection_p, String userName_p, String password_p, OwXMLUtil configuration_p, OwFNCM5UserInfoFactory factory_p) throws OwAuthenticationException
    {
        super(userName_p, password_p, factory_p);
        String jaasConfiguration = configuration_p.getSafeTextValue(CONF_NODE_JAAS_CTX, null);
        this.connection = connection_p;
        this.password = password_p;
        if (System.getProperty("java.security.auth.login.config") != null)
        {
            CallbackHandler handler = createCallbackHandler(connection_p, userName_p, password_p, jaasConfiguration);

            // Get login context.
            LoginContext lc;
            try
            {
                lc = createLoginContext(jaasConfiguration, handler);
            }
            catch (LoginException e)
            {
                throw new OwAuthenticationException("Login context could not be established", e);
            }

            try
            {
                lc.login();
                UserContext.get().pushSubject(lc.getSubject());
            }
            catch (LoginException ex)
            {
                throw new OwAuthenticationException("Login failed", ex);
            }
        }
        else
        {
            Subject subject = UserContext.createSubject(connection_p, userName_p, password_p, jaasConfiguration);
            UserContext.get().pushSubject(subject);
        }
        setLoginSubject(UserContext.get().getSubject());
    }

    /**
     * SSO specific Credentials handler, will try to extract current user from JEE environment.
     * @param connection_p Connection to P8 system
     * @param configuration_p OwXMLUtil network configuration
     * @param factory_p OwFNCMUserInfoFactory
     * @throws OwAuthenticationException
     * @since 3.2.0.2
     */
    public OwFNCM5EngineCredentials(Connection connection_p, OwXMLUtil configuration_p, OwFNCM5UserInfoFactory factory_p) throws OwAuthenticationException
    {
        super(null, null, factory_p);
        Subject currentSubject = J2EEUtil.getInstance().getCurrentSubject();
        if (currentSubject == null)
        {
            throw new OwAuthenticationException("JAAS Login failed. No user subject set in this thread.");
        }
        this.containerBasedAuthentication = true;
        this.connection = connection_p;
        UserContext.get().pushSubject(currentSubject);
        setLoginSubject(null);
    }

    /**(overridable)<br />
     * Create a specific call back handler for current login module.
     * @param con Connection used to access P8-CE
     * @param userName String name (can be null)
     * @param password String password (can be null)
     * @param jaasOpt String current login module
     * @return CallbackHandler or null if none is available
     */
    protected CallbackHandler createCallbackHandler(Connection con, String userName, String password, String jaasOpt)
    {
        if (password != null)
        {
            return new OwFNCM5CallbackHandler(userName, password);
        }
        else
        {
            return null;
        }
    }

    protected LoginContext createLoginContext(String jaasOpt, CallbackHandler handler) throws LoginException
    {
        if (handler != null)
        {
            return new LoginContext(jaasOpt, handler);
        }
        else
        {
            return new LoginContext(jaasOpt);
        }
    }

    @Override
    protected OwUserInfo createCurrentUserInfo(String userName_p) throws OwException
    {
        User fetchCurrent = com.filenet.api.core.Factory.User.fetchCurrent(this.connection, null);
        return createUserInfo(fetchCurrent);
    }

    public void invalidate() throws OwException
    {
        UserContext usrCtx = UserContext.get();
        usrCtx.popSubject();
        usrCtx.setLocale(null);
    }

    public Connection getConnection()
    {
        return this.connection;
    }

    /**
     * Set the subject which allows a valid authentication.
     * @param loginSubject Subject
     */
    public void setLoginSubject(Subject loginSubject)
    {
        this.loginSubject = loginSubject;
    }

    /**
     * Get subject which was used for authentication.
     * @return Subject(can return null if not set)
     */
    public Subject getLoginSubject()
    {
        try
        {
            if (isContainerBasedAuthenticated())
            {
                return J2EEUtil.getInstance().getCurrentSubject();
            }
            else
            {
                return loginSubject;
            }
        }
        catch (OwException e)
        {
            return loginSubject;
        }
    }

    @Override
    public boolean isContainerBasedAuthenticated() throws OwException
    {
        return this.containerBasedAuthentication;
    }

}