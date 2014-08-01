package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepositoryLDAP;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * LDAP based authentication provider for the Open CMIS adapter.
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
public class OwCMISLDAPAuthenticationProvider extends OwCMISDefaultAuthenticationProvider<OwUsersRepositoryLDAP>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISLDAPAuthenticationProvider.class);
    private OwLdapConnector ldapCon;

    /**
     * @param network
     */
    public OwCMISLDAPAuthenticationProvider(OwCMISNetwork network)
    {
        super(network);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISDefaultAuthenticationProvider#loginDefault(java.lang.String, java.lang.String)
     */
    @Override
    public void loginDefault(String username_p, String password_p) throws OwException
    {
        if (username_p != null && password_p != null)
        {
            authenticateOverLdap(username_p, password_p);
        }
        super.loginDefault(username_p, password_p);
    }

    /**
     * Factory for creation of OwUsersRepsitory instance with LDAP connection. 
     * @param connector OwLdapConnector
     * @return OwUsersRepositoryLDAP new instance
     * @since 4.1.1.1
     */
    protected OwUsersRepositoryLDAP createLdapUserRepository(OwLdapConnector connector)
    {
        return new OwUsersRepositoryLDAP(connector);
    }

    @Override
    protected OwUsersRepositoryLDAP createUserRepository(OwCMISCredentials credentials) throws OwException
    {
        return createLdapUserRepository(getLdapConnector());
    }

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
            OwCMISNetworkCfg config = this.network.getNetworkConfiguration();
            OwAuthenticationConfiguration authConf = config.getAuthenticationConfiguration();
            if (authConf != null && OwAuthenticationConfiguration.LDAP.equals(authConf.getMode()))
            {
                this.ldapCon = createLDAPConnector(authConf);
            }
            return this.ldapCon;
        }
    }

    /**
     * Factory to create LDAP-Connector.<br />
     * Can throw NullpointerException if OwAuthenticationConfiguration is null.
     * @param authConf OwAuthenticationConfiguration
     * @return OwLdapConnector
     */
    protected OwLdapConnector createLDAPConnector(OwAuthenticationConfiguration authConf)
    {
        return new OwLdapConnector(authConf.getConfiguration().getNode());
    }

}