package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.Set;

/**
 *<p>
 * Simple representation of a User in a {@link OwUsersRepository}.
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
public class OwUser
{

    private String id;
    private String name;
    private OwUsersRepository userRepository;

    /**
     * @param id
     * @param name
     * @param usersRepository 
     */
    public OwUser(String id, String name, OwUsersRepository usersRepository)
    {
        if (null == id)
        {
            throw new NullPointerException("The id can not be null!");
        }
        this.id = id;
        this.name = name;
        this.userRepository = usersRepository;
    }

    /**
    * @return the id
    */
    public String getId()
    {
        return id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    public Set<OwGroup> getGroups() throws OwUserRepositoryException
    {
        return this.userRepository.findGroupsForUserID(this.getId());
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (null == obj)
        {
            return false;
        }
        else if (obj instanceof OwUser)
        {
            OwUser other = (OwUser) obj;
            return other.getId().equals(this.getId());
        }

        return false;
    }
}
