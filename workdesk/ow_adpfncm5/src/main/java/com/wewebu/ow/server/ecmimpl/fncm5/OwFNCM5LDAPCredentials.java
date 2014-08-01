package com.wewebu.ow.server.ecmimpl.fncm5;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5UserInfoFactory;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * OwFNCM5LDAPCredentials class.
 * Handle the LDAP connection based authentication and also 
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
public class OwFNCM5LDAPCredentials extends OwFNCM5Credentials
{

    public static final String AUTHENTICATION_MODE_LDAP = "LDAP";

    private OwFNCM5EngineCredentials engineCredentials;
    private OwLdapConnector ldapConnector;

    public OwFNCM5LDAPCredentials(String userName_p, String password_p, OwXMLUtil configuration_p, OwFNCM5EngineCredentials engineCredentials_p) throws OwConfigurationException, OwAuthenticationException
    {
        this(userName_p, password_p, configuration_p, engineCredentials_p, engineCredentials_p);
    }

    public OwFNCM5LDAPCredentials(String userName_p, String password_p, OwXMLUtil configuration_p, OwFNCM5UserInfoFactory infoFactory_p, OwFNCM5EngineCredentials engineCredentials_p) throws OwConfigurationException, OwAuthenticationException
    {
        super(userName_p, password_p, infoFactory_p);

        OwXMLUtil authenticationConfiguration = null;
        try
        {
            authenticationConfiguration = configuration_p.getSubUtil(OwFNCM5Credentials.CONF_NODE_AUTHENTICATION);
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Error reading authentication configuration.", e);
        }

        ldapConnector = new OwLdapConnector(authenticationConfiguration.getNode());
        ldapConnector.authenticate(userName_p, password_p);
        this.engineCredentials = engineCredentials_p;
    }

    @Override
    protected OwUserInfo createCurrentUserInfo(String userName_p) throws OwException
    {
        return engineCredentials.createUserInfo(userName_p, ldapConnector);
    }

    public void invalidate() throws OwException
    {
        engineCredentials.invalidate();
    }

    public Connection getConnection()
    {
        return engineCredentials.getConnection();
    }

    public Subject getLoginSubject()
    {
        return engineCredentials.getLoginSubject();
    }

}
