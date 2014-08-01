package com.wewebu.ow.server.ecmimpl.opencmis.users;

/**
 *<p>
 * Simple representation of a Group in a {@link OwUsersRepository}.
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
public class OwGroup
{
    private String id;
    private String name;

    /**
     * The id should not be null.
     * @param id
     * @param name
     */
    public OwGroup(String id, String name)
    {
        if (null == id)
        {
            throw new NullPointerException("The id can not be null!");
        }
        this.id = id;
        this.name = name;
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
}
