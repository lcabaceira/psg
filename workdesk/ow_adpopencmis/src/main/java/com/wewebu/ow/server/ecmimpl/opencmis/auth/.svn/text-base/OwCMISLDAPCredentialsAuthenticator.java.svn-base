package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.auth.OwAuthentication;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * LDAP based implementation of the CredentialsAuthenticator.
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
 *@since 4.1.1.0
 *@deprecated since 4.2.0.0 logic moved to OwCMISLDAPAuthenticationProvider
 */
public class OwCMISLDAPCredentialsAuthenticator extends OwCMISCredentialsAuthenticator
{
    private static final Logger LOG = OwLogCore.getLogger(OwCMISLDAPCredentialsAuthenticator.class);
    private OwLdapConnector ldapCon;

    /**
     * @param network
     * @param ldapConnector
     */
    public OwCMISLDAPCredentialsAuthenticator(OwCMISNetwork network, OwLdapConnector ldapConnector)
    {
        super(network);
        this.ldapCon = ldapConnector;
    }

    public OwCMISLDAPCredentialsAuthenticator(OwBaseInitializer initializer)
    {
        super(initializer);
    }

    /**
     * Will try to retrieve connector to an LDAP system.
     * <p>By default it will use the provided one, which was defined during instantiation.
     * If none was defined, the configuration is checked/searched for LDAP configuration.</p> 
     * @return OwLdapConfiguration or null
     * @throws OwConfigurationException
     */
    protected OwLdapConnector getLdapConnector() throws OwException
    {
        if (this.ldapCon != null)
        {
            return this.ldapCon;
        }
        else
        {
            OwCMISNetworkCfg config = getNetworkConfiguration();
            OwAuthenticationConfiguration authConf = config.getAuthenticationConfiguration();
            if (authConf != null && OwAuthenticationConfiguration.LDAP.equals(authConf.getMode()))
            {
                this.ldapCon = createLDAPConnector(authConf);
            }
            return this.ldapCon;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentialsAuthenticator#createCredentials(java.lang.String, java.lang.String, org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider)
     */
    @Override
    protected OwCMISCredentials createCredentials(String user_p, String password_p, AuthenticationProvider nativeAuthProvider) throws OwException
    {
        return new OwCMISLDAPCredentials(user_p, password_p, nativeAuthProvider, getLdapConnector());
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentialsAuthenticator#authenticate(com.wewebu.ow.server.auth.OwAuthentication)
     */
    @Override
    public OwAuthentication authenticate(OwAuthentication authentication_p) throws OwAuthenticationException, OwConfigurationException, OwServerException
    {
        String userName = authentication_p.getUserName();
        String password = authentication_p.getPassword();

        if (userName != null && password != null)
        {
            authenticateOverLdap(userName, password);
        }
        return super.authenticate(authentication_p);
    }

    /**
     * @param userName
     * @param password
     * @throws OwServerException
     * @throws OwConfigurationException
     * @throws OwAuthenticationException
     */
    private void authenticateOverLdap(String userName, String password) throws OwServerException, OwConfigurationException, OwAuthenticationException
    {
        OwLdapConnector conLdap;
        try
        {
            conLdap = getLdapConnector();
        }
        catch (OwException e1)
        {
            LOG.debug("LDAP connection cannot be retrieved", e1);
            throw new OwServerException("Faild to create LDAP connection", e1);
        }
        if (conLdap != null)
        {
            try
            {
                conLdap.authenticate(userName, password);
            }
            catch (OwAuthenticationException owa)
            {
                LOG.debug("Authentication Exception: Cloud not verify login against LDAP", owa);
                throw new OwAuthenticationException(new OwString("ecmimpl.opencmis.OwCMISNetwork.login.failed", "Login failed: Wrong username or password"), owa);
            }
        }
    }
}
