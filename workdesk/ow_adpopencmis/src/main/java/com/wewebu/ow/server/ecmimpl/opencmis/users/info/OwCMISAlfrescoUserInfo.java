package com.wewebu.ow.server.ecmimpl.opencmis.users.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwGroup;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository;
import com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.dao.Person;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.mandator.OwMandator;

/**
 *<p>
 * User info for Alfresco based authenticated user.
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
public class OwCMISAlfrescoUserInfo extends OwCMISUserInfo
{
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Collection<OwUserInfo> groups;
    private OwUsersRepository userRepository;

    public OwCMISAlfrescoUserInfo(Person person, OwMandator mandator, OwUsersRepository userrRepository)
    {
        super(person.getUserName(), mandator);
        this.userRepository = userrRepository;
        this.id = person.getUserName();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();

        Collection<OwUserInfo> groups = new LinkedList<OwUserInfo>();
        Person.Group[] personGroups = person.getGroups();
        if (null != personGroups)
        {
            for (Person.Group group : personGroups)
            {
                groups.add(new OwCMISAlfrescoGroupInfo(group.getItemName(), group.getDisplayName()));
            }
        }

        this.groups = groups;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo#getGroups()
     */
    @Override
    public Collection<OwUserInfo> getGroups() throws OwException
    {
        if (null == groups)
        {
            this.groups = new HashSet<OwUserInfo>();
            Set<OwGroup> userGroups = this.userRepository.findGroupsForUserID(this.getUserID());
            for (OwGroup owGroup : userGroups)
            {
                this.groups.add(new OwCMISAlfrescoGroupInfo(owGroup));
            }
        }

        Collection<OwUserInfo> result = new ArrayList<OwUserInfo>();
        result.addAll(this.groups);
        result.addAll(getDefaulfGroups());
        return result;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo#getRoleNames()
     */
    @Override
    public Collection<String> getRoleNames() throws OwException
    {
        try
        {
            Collection<String> names = new LinkedHashSet<String>();

            Set<String> shortGroupNames = new HashSet<String>();
            for (OwUserInfo group : this.getGroups())
            {
                OwCMISAlfrescoGroupInfo groupInfo = (OwCMISAlfrescoGroupInfo) group;
                shortGroupNames.add(groupInfo.getUserShortName());
            }

            Collection<String> superRoles = getDefaultRoleNames();
            if (getMandator() == null)
            {
                names.addAll(superRoles);
                names.addAll(shortGroupNames);
            }
            else
            {
                for (String superRole : superRoles)
                {
                    names.add(getMandator().filterRoleName(superRole));
                }
                for (String groupName : shortGroupNames)
                {
                    names.add(getMandator().filterRoleName(groupName));
                }
            }

            names.addAll(superRoles);

            return names;
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not process roles for this user.", e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwAbstractCMISUserInfo#getUserDisplayName()
     */
    @Override
    public String getUserDisplayName() throws Exception
    {
        return firstName + " " + lastName;
    }

    /**
     * userId and username are the same for Alfresco.
     */
    @Override
    public String getUserID()
    {
        return this.id;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwAbstractCMISUserInfo#getUserLongName()
     */
    @Override
    public String getUserLongName() throws OwException
    {
        return this.firstName + " " + this.lastName;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwAbstractCMISUserInfo#getUserShortName()
     */
    @Override
    public String getUserShortName() throws Exception
    {
        return this.lastName;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwAbstractCMISUserInfo#getUserEmailAdress()
     */
    @Override
    public String getUserEmailAdress() throws OwException
    {
        return this.email;
    }
}
