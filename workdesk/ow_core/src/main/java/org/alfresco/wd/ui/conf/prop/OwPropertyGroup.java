package org.alfresco.wd.ui.conf.prop;

import java.util.List;

import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Interface of a Property Group configuration.
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
 * @since 4.2.0.0
 */
public interface OwPropertyGroup<T extends OwPropertyInfo>
{
    /**
     * Return an unique group Id, in case multiple groups available.
     * @return String
     */
    String getGroupId();

    /**
     * Return grouped properties.
     * @return List of OwPropertyInfo
     */
    List<T> getProperties();

    /**
     * Get a DisplayName for current group.
     * @param context OwAppContext used for localization
     * @return String
     */
    String getDisplayName(OwAppContext context);

    /**
     * Check state of group.
     * <ul>
     * <li>true: Group properties are not visible by default</li>
     * <li>false: Group is expanded and available properties are displayed</li>
     * </ul> 
     * @return boolean
     */
    boolean isCollapsed();

    /**
     * Set collapse state of Group.
     * @param collapse boolean
     * @see #isCollapsed()
     */
    void setCollapsed(boolean collapse);
}
