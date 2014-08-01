package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISLDAPUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * User repository implementation based on a connection to a LDAP server.
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
 */
public class OwUsersRepositoryLDAP implements OwUsersRepository
{
    private static final Logger LOG = OwLog.getLogger(OwUsersRepositoryLDAP.class);
    private OwLdapConnector ldapConnector;

    public OwUsersRepositoryLDAP(OwLdapConnector ldapConnector)
    {
        this.ldapConnector = ldapConnector;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findUserByID(java.lang.String)
     */
    @Override
    public OwUserInfo findUserByID(String id) throws OwUserRepositoryException
    {
        try
        {
            OwUserInfo userInfo = null;
            try
            {
                //try by LDAP record ID
                userInfo = getLdapConnector().findUserByDNAnonymously(id);
            }
            catch (OwObjectNotFoundException e)
            {
            }

            if (userInfo == null)
            {
                try
                {
                    //try by ID
                    userInfo = getLdapConnector().findUserByIdAnonymously(id);
                }
                catch (OwObjectNotFoundException e)
                {
                }
            }

            if (userInfo == null)
            {
                //try by login
                userInfo = getLdapConnector().findUserByLoginAnonymously(id);
            }

            OwCMISLDAPUserInfo user = new OwCMISLDAPUserInfo(userInfo.getUserName(), getLdapConnector());
            return user;
        }
        catch (Exception owEx)
        {
            throw new OwUserRepositoryException(owEx);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findGroupsMatching(java.lang.String)
     */
    @Override
    public Set<OwGroup> findGroupsMatching(String pattern) throws OwUserRepositoryException
    {
        try
        {
            Set<OwGroup> result = new HashSet<OwGroup>();
            OwObjectCollection groups = getLdapConnector().findGroupsMatching(pattern);
            for (Object object : groups)
            {
                OwUserInfo groupInfo = (OwUserInfo) object;
                result.add(new OwGroup(groupInfo.getUserID(), groupInfo.getUserShortName()));
                //                result.add(new OwGroup(groupInfo.getUserShortName(), groupInfo.getUserShortName()));
            }

            return result;
        }
        catch (Exception e)
        {
            throw new OwUserRepositoryException(new OwString("opencmis.users.OwUserRepositoryLDAP.err_groupInfo", "Cannot retrieve GroupInformation"), e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findGroupsForUserID(java.lang.String)
     */
    @Override
    public Set<OwGroup> findGroupsForUserID(String userId) throws OwUserRepositoryException
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not implemented yet!");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findUsersMatching(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public OwObjectCollection findUsersMatching(String pattern) throws OwUserRepositoryException
    {
        if (!pattern.contains("*"))
        {
            if (0 != pattern.length())
            {
                pattern = "*" + pattern + "*";
            }
        }

        try
        {
            OwObjectCollection dnames = getLdapConnector().findUserDNsMatching(pattern);
            OwStandardObjectCollection lst = new OwStandardObjectCollection();
            for (Object dname : dnames)
            {
                String displayName = getLdapConnector().getUserDisplayName((String) dname);
                //String uid = getLdapConnector().getRecordId((String) dname);
                String userLogin = getLdapConnector().getUserLogin((String) dname);

                if (null == displayName)
                {
                    LOG.warn("Could not get DisplayName for " + dname);
                    continue;
                }

                if (null == userLogin)
                {
                    LOG.warn("Could not get uid for " + dname);
                    continue;
                }

                lst.add(new OwUser(userLogin, displayName, this));
            }

            lst.setComplete(dnames.isComplete());
            return lst;
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception owEx)
        {
            throw new OwUserRepositoryException(owEx);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findRolesMatching(java.lang.String)
     */
    @Override
    public Set<OwUserRole> findRolesMatching(String pattern) throws OwUserRepositoryException
    {
        try
        {
            Set<OwUserRole> result = new HashSet<OwUserRole>();
            OwObjectCollection groups = getLdapConnector().findGroupsMatching(pattern);
            for (Object object : groups)
            {
                OwUserInfo groupInfo = (OwUserInfo) object;
                //                result.add(new OwUserRole(groupInfo.getUserID(), groupInfo.getUserShortName()));
                result.add(new OwUserRole(groupInfo.getUserShortName(), groupInfo.getUserShortName()));
            }

            return result;
        }
        catch (Exception e)
        {
            throw new OwUserRepositoryException(new OwString("opencmis.users.OwUserRepositoryLDAP.err_roleInfo", "Cannot retrieve RoleInformation"), e);
        }
    }

    /**
     * Getter for used OwLdapConnector instance
     * @return OwLdapConnector
     * @since 4.1.1.1
     */
    public OwLdapConnector getLdapConnector()
    {
        return this.ldapConnector;
    }

}
