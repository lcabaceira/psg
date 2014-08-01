package org.alfresco.wd.ui.conf.prop;

import java.util.Collections;
import java.util.List;

import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Simple implementation of {@link OwPropertyGroup}. 
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
 * @param <T>
 *@since 4.2.0.0
 */
public class OwSimplePropertyGroup<T extends OwPropertyInfo> implements OwPropertyGroup<T>
{
    private String groupId;
    /** the group display name*/
    private String groupDisplayName;
    /** flag indicating the collapsed status of the group*/
    private boolean isCollapsed;
    /** properties configured for this group*/
    private List<T> properties;

    /**
     * Constructor
     * @param groupId - the group id
     * @param groupDisplayName - the group display name
     * @param propertyInfos - a {@link List} of {@link OwPropertyInfo} objects.
     */
    public OwSimplePropertyGroup(String groupId, String groupDisplayName, List<T> propertyInfos)
    {
        this(groupId, groupDisplayName, propertyInfos, false);
    }

    /**
     * Constructor
     * @param groupId - the group id
     * @param groupDisplayName - the group display name
     * @param propertyInfos - a {@link List} of {@link OwPropertyInfo} objects.
     * @param isCollapsed - collapsed status
     */
    public OwSimplePropertyGroup(String groupId, String groupDisplayName, List<T> propertyInfos, boolean isCollapsed)
    {
        this.groupId = groupId;
        this.groupDisplayName = groupDisplayName;
        this.properties = propertyInfos;
        this.isCollapsed = isCollapsed;
    }

    public String getGroupId()
    {
        return this.groupId;
    }

    /**
     * @return an unmodifiable {@link List} of {@link OwPropertyInfo} objects.
     */
    public List<T> getProperties()
    {
        return Collections.unmodifiableList(this.properties);
    }

    /**
     * Get the group display name.
     * @param context - the context.
     * @return - the localized group name based on {@link #getGroupId()}, or the configured display name
     */
    public String getDisplayName(OwAppContext context)
    {
        return context.localize(getGroupId(), this.groupDisplayName);
    }

    public void setCollapsed(boolean isCollapsed)
    {
        this.isCollapsed = isCollapsed;
    }

    public boolean isCollapsed()
    {
        return this.isCollapsed;
    }
}
