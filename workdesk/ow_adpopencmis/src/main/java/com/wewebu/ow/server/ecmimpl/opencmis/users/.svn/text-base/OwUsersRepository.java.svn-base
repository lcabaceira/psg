package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.Set;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * An {@link OwUsersRepository} is an abstraction for a repository from which you can get information about the users, groups and roles that are available in the system. 
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
public interface OwUsersRepository
{
    /**
     * Tries to find a user by his/her ID.
     * @param id
     * @return The user with the given ID or null if not found.
     * @throws OwUserRepositoryException
     */
    //TODO: we should return an instance of OwUser instead of OwUserInfo 
    public OwUserInfo findUserByID(String id) throws OwUserRepositoryException;

    public Set<OwGroup> findGroupsMatching(String pattern) throws OwUserRepositoryException;

    public Set<OwGroup> findGroupsForUserID(String userId) throws OwUserRepositoryException;

    /**
     * Find users that match the pattern.
     * 
     * @param pattern
     * @return a collection of {@link OwUser}.
     * @throws OwUserRepositoryException
     */
    public OwObjectCollection findUsersMatching(String pattern) throws OwUserRepositoryException;

    public Set<OwUserRole> findRolesMatching(String pattern) throws OwUserRepositoryException;
}
