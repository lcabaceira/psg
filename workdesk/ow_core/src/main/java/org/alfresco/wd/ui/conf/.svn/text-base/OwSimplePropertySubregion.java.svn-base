package org.alfresco.wd.ui.conf;

import java.util.List;

import org.alfresco.wd.ui.conf.prop.OwPropertyGroup;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;

/**
 *<p>
 * simple implementation of OwPropertySubregion. 
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
public class OwSimplePropertySubregion<T extends OwPropertyInfo> implements OwPropertySubregion<T>
{
    private OwPropertyGroup<T> propertyGroup;

    private List<T> props;

    public OwSimplePropertySubregion(OwPropertyGroup<T> group)
    {
        this.propertyGroup = group;
    }

    public OwSimplePropertySubregion(List<T> propInfos)
    {
        this.props = propInfos;
    }

    @Override
    public boolean isGroup()
    {
        return this.propertyGroup != null;
    }

    @Override
    public OwPropertyGroup<T> getPropertyGroup()
    {
        return propertyGroup;
    }

    @Override
    public List<T> getPropertyInfos()
    {
        if (isGroup())
        {
            return getPropertyGroup().getProperties();
        }
        else
        {
            return this.props;
        }
    }

}
