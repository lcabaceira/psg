package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * Factory for OwUserInfo handling.
 * Will create from provided arguments,
 * the respective OwUserInfo representation.
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
public interface OwFNCM5UserInfoFactory
{
    /**
     * Create a wrapper for the native group.
     * @param engineGroup_p Group to be wrapped
     * @return OwUserInfo
     */
    OwUserInfo createGroupInfo(Group engineGroup_p);

    /**
     * Create a wrapper for the native group.
     * @param groupName_p Group to be wrapped
     * @return OwUserInfo
     */
    OwUserInfo createGroupInfo(String groupName_p);

    /**
     * Create a wrapper for provided user object.
     * @param engineUser_p User to be wrapped
     * @return OwUserInfo
     */
    OwUserInfo createUserInfo(User engineUser_p);

    /**
     * Create a OwUserInfo from string,
     * where string is representing a role name.
     * @param roleName_p String role name
     * @return OwUserInfo
     */
    OwUserInfo createRoleInfo(String roleName_p);

    /**
     * Create a OwUserInfo from LDAP connector instance.
     * @param userName_p
     * @param ldapConnector_p
     * @return OwUserInfo
     */
    OwUserInfo createUserInfo(String userName_p, OwLdapConnector ldapConnector_p);

}
