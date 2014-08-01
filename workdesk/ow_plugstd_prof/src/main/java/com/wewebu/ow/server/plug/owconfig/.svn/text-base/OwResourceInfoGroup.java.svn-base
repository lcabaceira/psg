package com.wewebu.ow.server.plug.owconfig;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwResourceInfo;

/**
 *<p>
 * Represents a group of resources. Resources can be added to a group, at this time there is no 
 * support for delete a resource.<br/>
 * Resources that are not containing <code>@</code> inside theirs name, are added to a <code>surrogate</code>
 * group.
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
public class OwResourceInfoGroup implements Comparable
{
    /**
     * All name - represent a group with name All
     */
    public static final String GROUP_NAME_ALL = "ALL";
    /**
     *  Surrogate group name.
     */
    private static final String SURROGATE_GROUP_NAME = "surrogate";
    /**
     * List of resources
     */
    private List m_resourceInfos = null;
    /**
     * Group ID.
     */
    private int m_groupId;
    /**
     * Group display name.
     */
    private String m_groupDisplayName;
    /**
     * Group name.
     */
    private String m_name;
    /**
     * Comparator for resources, default is null;
     */
    private Comparator m_resourceComparator;

    /**
     * Constructor. Constructs a group with given group ID.
     * @param groupId_p - the group ID.
     */
    private OwResourceInfoGroup(int groupId_p)
    {
        m_resourceInfos = new LinkedList();
        m_groupId = groupId_p;
    }

    /**
     * Constructor. Constructs a group with given parameters
     * @param name_p - the group name
     * @param groupId_p - the group ID 
     * @param groupDisplayName_p - the group display name.
     */
    public OwResourceInfoGroup(String name_p, int groupId_p, String groupDisplayName_p)
    {
        this(groupId_p);
        m_name = name_p;
        m_groupDisplayName = groupDisplayName_p;
    }

    /**
     * Constructor. Constructs a group with given parameters. If the client of this class is not satisfied 
     * with the default resource ordering, a different comparator can be set. 
     * @param name_p - the group name
     * @param groupId_p - the group ID 
     * @param groupDisplayName_p - the group display name.
     * @param resourceComparator_p - the resource comparator.
     */
    public OwResourceInfoGroup(String name_p, int groupId_p, String groupDisplayName_p, Comparator resourceComparator_p)
    {
        this(name_p, groupId_p, groupDisplayName_p);
        m_resourceComparator = resourceComparator_p;
    }

    /**
     * Add a resource to this group.
     * @param resourceInfo_p - the resource to be added.
     */
    public void addResourceInfo(OwResourceInfo resourceInfo_p)
    {
        m_resourceInfos.add(resourceInfo_p);
    }

    /**
     * Add an array of resources to this group
     * @param resourceInfoArray_p
     */
    public void addAllResourceInfo(OwResourceInfo[] resourceInfoArray_p)
    {
        for (int i = 0; i < resourceInfoArray_p.length; i++)
        {
            addResourceInfo(resourceInfoArray_p[i]);
        }
    }

    /**
     * Getter method for group Id.
     * @return the group ID. 
     */
    public int getGroupId()
    {
        return m_groupId;
    }

    /**
     * Getter method for group display name.
     * @return the group display name.
     */
    public String getGroupDisplayName()
    {
        return m_groupDisplayName;
    }

    /**
     * Getter method for group name.
     * @return the group name.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Creates a surrogate group
     * @return the surrogate group.
     */
    public static OwResourceInfoGroup createSurrogateGroup()
    {
        return new OwResourceInfoGroup(SURROGATE_GROUP_NAME, -1, SURROGATE_GROUP_NAME);
    }

    /**
     * Checks if this group is a surrogate group
     * @return <code>true</code> - if this group is surrogate.
     */
    public boolean isSurrogate()
    {
        return SURROGATE_GROUP_NAME.equals(getName());
    }

    /**
     * Gets all resources from this group, sorted by display name, or another criteria. 
     * @return resources belonging to this group.
     */
    public Collection getResources()
    {
        if (m_resourceComparator != null)
        {
            Collections.sort(m_resourceInfos, m_resourceComparator);
        }
        else
        {
            Collections.sort(m_resourceInfos);
        }
        return m_resourceInfos;
    }

    /**
     * Compare two groups
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o_p)
    {
        int result = -1;
        if (o_p instanceof OwResourceInfoGroup)
        {
            //The group all will be always the first one displayed in list
            OwResourceInfoGroup toBeCompared = (OwResourceInfoGroup) o_p;
            if (m_name.equalsIgnoreCase(GROUP_NAME_ALL))
            {
                result = -1;
            }
            else if (toBeCompared.m_name.equalsIgnoreCase(GROUP_NAME_ALL))
            {
                result = 1;
            }
            else

            if (toBeCompared.m_groupDisplayName != null)
            {
                result = (-1) * toBeCompared.m_groupDisplayName.compareTo(m_groupDisplayName);
            }
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return m_groupDisplayName + m_groupId + m_name;
    }

}