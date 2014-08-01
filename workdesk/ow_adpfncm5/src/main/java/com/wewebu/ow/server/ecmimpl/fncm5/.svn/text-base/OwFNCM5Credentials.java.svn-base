package com.wewebu.ow.server.ecmimpl.fncm5;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.OwAbstractCredentials;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5UserInfoFactory;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * OwFNCM5Credentials class.
 * Handle the authentication and also the creation
 * of OwBaseUserInfo objects.
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
public abstract class OwFNCM5Credentials extends OwAbstractCredentials implements OwFNCM5UserInfoFactory
{

    public static final String CONF_NODE_AUTHENTICATION = "Authentication";
    public static final String CONF_ATTRIBUTE_MODE = "mode";

    private OwFNCM5UserInfoFactory infoFactory;
    protected String password;
    protected String userName;
    private OwUserInfo userInfo;

    public OwFNCM5Credentials(String userName_p, String password_p, OwFNCM5UserInfoFactory infoFactory_p)
    {
        super(userName_p, password_p);
        this.userName = userName_p;
        this.password = password_p;
        this.infoFactory = infoFactory_p;
    }

    /**
     * User info factory method.
     * 
     * @param userName_p
     * @return a new {@link OwUserInfo} for the given user name 
     * @throws OwException
     */
    protected abstract OwUserInfo createCurrentUserInfo(String userName_p) throws OwException;

    public final synchronized OwUserInfo getUserInfo() throws OwException
    {
        if (userInfo == null)
        {
            userInfo = createCurrentUserInfo(userName);
        }

        return userInfo;
    }

    public final OwUserInfo createGroupInfo(Group engineGroup_p)
    {
        return infoFactory.createGroupInfo(engineGroup_p);
    }

    public final OwUserInfo createUserInfo(User engineUser_p)
    {
        return infoFactory.createUserInfo(engineUser_p);
    }

    public final OwUserInfo createRoleInfo(String roleName_p)
    {
        return infoFactory.createRoleInfo(roleName_p);
    }

    public final OwUserInfo createUserInfo(String userName_p, OwLdapConnector ldapConnector_p)
    {
        return infoFactory.createUserInfo(userName_p, ldapConnector_p);
    }

    public final OwUserInfo createGroupInfo(String groupName_p)
    {
        return infoFactory.createGroupInfo(groupName_p);
    }

    public String getPassword()
    {
        return password;
    }

    /**
     * 
     * @return the current P8 5.0 {@link Connection} 
     */
    public abstract Connection getConnection();

    /**
     * 
     * @return the logged in {@link Subject}
     */
    public abstract Subject getLoginSubject();

    public abstract void invalidate() throws OwException;

}
