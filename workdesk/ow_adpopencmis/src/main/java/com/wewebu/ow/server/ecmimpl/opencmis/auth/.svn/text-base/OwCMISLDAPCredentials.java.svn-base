package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISLDAPUserInfo;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * LDAP based implementation.
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
public class OwCMISLDAPCredentials extends OwCMISCredentials
{

    private OwLdapConnector ldapConnector;

    public OwCMISLDAPCredentials(String user_p, String password_p, OwLdapConnector ldapConnector) throws OwConfigurationException
    {
        super(user_p, password_p);
        this.ldapConnector = ldapConnector;
    }

    public OwCMISLDAPCredentials(String user, String password, AuthenticationProvider nativeAuthProv, OwLdapConnector ldapConnector) throws OwConfigurationException
    {
        super(user, password, nativeAuthProv);
        this.ldapConnector = ldapConnector;
    }

    @Override
    protected OwUserInfo createUserInfo(String userName_p) throws OwException
    {
        return new OwCMISLDAPUserInfo(userName_p, getMandator(), this.ldapConnector);
    }
}
