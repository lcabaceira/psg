package org.alfresco.wd.ui.conf;

import java.util.List;

import org.alfresco.wd.ui.conf.prop.OwPropertyGroup;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;

/**
 *<p>
 * Interface to represent a Property rendering region 
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
 *@since 4.2.0.0
 */
public interface OwPropertySubregion<T extends OwPropertyInfo>
{
    /**
     * Check if sub-region is based on PropertyGroup
     * @return true if group i
     */
    boolean isGroup();

    /**
     * Return a OwPropertyGroup representation if subregion is based on property group.
     * @return OwPropertyGroup or null if {@link #isGroup()} return false
     */
    OwPropertyGroup<T> getPropertyGroup();

    /**
     * Get a list of configured OwPropertyInfo for this subregion
     * @return List of OwPropertyInfo
     */
    List<T> getPropertyInfos();

}
