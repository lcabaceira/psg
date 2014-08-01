package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUserRepositoryException;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepositoryAlfresco;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

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
public class OwCMISAlfrescoCredentials extends OwCMISCredentials
{
    private OwUsersRepositoryAlfresco repository;

    public OwCMISAlfrescoCredentials(String user, String password, AuthenticationProvider nativeAuthProv, OwUsersRepositoryAlfresco repository) throws OwConfigurationException
    {
        super(user, password, nativeAuthProv);
        this.repository = repository;
    }

    @Override
    protected OwUserInfo createUserInfo(String userName_p) throws OwException
    {
        try
        {
            return this.repository.findUserByID(userName_p);
        }
        catch (OwUserRepositoryException e)
        {
            String msg = "Cannot crate user info for " + userName_p;
            throw new OwServerException(msg, e);
        }
    }
}
