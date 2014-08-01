package com.wewebu.ow.server.ecmimpl.fncm5;

import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5EngineGroupInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5EngineUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5LDAPUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5NamedGroupInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5UserInfoFactory;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * Default implementation of {@link OwFNCM5UserInfoFactory}.
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
public class OwFNCM5DefaultUserInfoFactory implements OwFNCM5UserInfoFactory
{
    private OwMandator mandator;
    private boolean ldapAdminGroupInterogation;

    public OwFNCM5DefaultUserInfoFactory(OwMandator mandator_p, boolean ldapAdminGroupInterogation_p)
    {
        this.mandator = mandator_p;
        this.ldapAdminGroupInterogation = ldapAdminGroupInterogation_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5BaseInfoFactory#createGroupInfo(com.filenet.api.security.Group)
     */
    public OwUserInfo createGroupInfo(Group group)
    {
        return new OwFNCM5EngineGroupInfo(group, this);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5BaseInfoFactory#createUserInfo(com.filenet.api.security.User)
     */
    public OwUserInfo createUserInfo(User user)
    {
        return new OwFNCM5EngineUserInfo(user, mandator, this);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5BaseInfoFactory#createRoleInfo(java.lang.String)
     */
    public OwUserInfo createRoleInfo(String roleName_p)
    {
        return new OwFNCM5NamedGroupInfo(roleName_p, this);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5BaseInfoFactory#createUserInfo(com.wewebu.ow.server.util.ldap.OwLdapConnector)
     */
    public OwUserInfo createUserInfo(String userName_p, OwLdapConnector ldapConnector_p)
    {
        return new OwFNCM5LDAPUserInfo(userName_p, mandator, ldapConnector_p, ldapAdminGroupInterogation, this);
    }

    public OwUserInfo createGroupInfo(String groupName_p)
    {
        return new OwFNCM5NamedGroupInfo(groupName_p, this);
    }
}
