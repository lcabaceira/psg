package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.GetPersonResource;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.ListGroupsResource;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.ListPeopleResource;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.OwRestException;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.dao.GroupInstance;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.dao.GroupsList;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.dao.Person;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.dao.Person.Group;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.dao.PersonsList;
import com.wewebu.ow.server.ecmimpl.opencmis.users.info.OwCMISAlfrescoUserInfo;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;

/**
 *<p>
 * User repository implementation based on a connection to an Alfresco server (Using the REST API).
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
//TODO: either we use OwUserInfo for all finder methods or we use OwGroup,OwUserRole and OwUser for all finder methods.  
public class OwUsersRepositoryAlfresco implements OwUsersRepository
{
    private static final Logger LOG = OwLog.getLogger(OwUsersRepositoryAlfresco.class);
    private AlfrescoRESTFulFactory restFactory;

    /**
     * @param baseURL
     * @param userName
     * @param password
     * @deprecated since 4.2.0.0 use {@link #OwUsersRepositoryAlfresco(OwAuthenticationConfiguration, OwRestletAuthenticationHandler)} instead
     */
    public OwUsersRepositoryAlfresco(String baseURL, String userName, String password)
    {

    }

    /**
     * Constructor which provides all informations about repository and corresponding authentication handler.  
     * @param conf OwAuthenticationConfiguration
     * @param authHandler OwCMISRestletAuthenticationHandler
     * @since 4.2.0.0
     */
    public OwUsersRepositoryAlfresco(OwAuthenticationConfiguration conf, OwRestletAuthenticationHandler authHandler)
    {
        this.restFactory = new AlfrescoRESTFulFactory(conf, authHandler);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findUserByID(java.lang.String)
     */
    @Override
    public OwUserInfo findUserByID(String id) throws OwUserRepositoryException
    {
        try
        {
            GetPersonResource resource = this.restFactory.getPersonResource(id);
            Person result = resource.getPerson();

            return new OwCMISAlfrescoUserInfo(result, null, this);
        }
        catch (OwRestException restEx)
        {
            LOG.error("Could not get user by Id = " + id, restEx);
            throw new OwUserRepositoryException(restEx);
        }
    }

    /**
     * The pattern can be applied to the group's id (shortName field in the REST response) only.
     */
    @Override
    public Set<OwGroup> findGroupsMatching(String pattern) throws OwUserRepositoryException
    {
        try
        {
            ListGroupsResource resource = this.restFactory.listGroupsResource(pattern);
            GroupsList result = resource.list();

            Set<OwGroup> groups = new LinkedHashSet<OwGroup>();
            for (GroupInstance groupInstance : result.data)
            {
                OwGroup group = new OwGroup(groupInstance.getFullName(), groupInstance.getDisplayName());
                groups.add(group);
            }
            return groups;
        }
        catch (OwRestException restEx)
        {
            LOG.error("Could not fire group search", restEx);
            throw new OwUserRepositoryException(restEx);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findGroupsForUserID(java.lang.String)
     */
    @Override
    public Set<OwGroup> findGroupsForUserID(String id) throws OwUserRepositoryException
    {
        try
        {
            GetPersonResource resource = this.restFactory.getPersonResource(id);
            Person result = resource.getPerson();

            Set<OwGroup> groups = new HashSet<OwGroup>();
            Group[] personGroups = result.getGroups();
            if (null != personGroups)
            {
                for (Group personGroup : personGroups)
                {
                    groups.add(new OwGroup(personGroup.getItemName(), personGroup.getDisplayName()));
                }
            }
            return groups;
        }
        catch (OwRestException restEx)
        {
            LOG.error("Could not get Groups for user " + id, restEx);
            throw new OwUserRepositoryException(restEx);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findUsersMatching(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public OwObjectCollection findUsersMatching(String pattern) throws OwUserRepositoryException
    {
        try
        {
            Collection<OwUser> users = new ArrayList<OwUser>();
            ListPeopleResource resource = this.restFactory.getPeopleResource(pattern);
            PersonsList result = resource.list();
            Person[] persons = result.people;
            for (Person person : persons)
            {
                users.add(new OwUser(person.getUserName(), person.getFirstName() + " " + person.getLastName(), this));
            }

            OwStandardObjectCollection usersCollection = new OwStandardObjectCollection();
            usersCollection.addAll(users);
            return usersCollection;
        }
        catch (OwRestException restEx)
        {
            LOG.error("Could not find Users matching " + pattern, restEx);
            throw new OwUserRepositoryException(restEx);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository#findRolesMatching(java.lang.String)
     */
    @Override
    public Set<OwUserRole> findRolesMatching(String pattern) throws OwUserRepositoryException
    {
        //In alfresco Groups == Roles
        Set<OwUserRole> roles = new LinkedHashSet<OwUserRole>();
        Set<OwGroup> groups = this.findGroupsMatching(pattern);
        for (OwGroup group : groups)
        {
            OwUserRole role = new OwUserRole(group.getId(), group.getName());
            roles.add(role);
        }

        return roles;
    }
}
