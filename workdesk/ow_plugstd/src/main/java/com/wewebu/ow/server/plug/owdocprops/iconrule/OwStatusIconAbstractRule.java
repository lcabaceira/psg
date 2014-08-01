package com.wewebu.ow.server.plug.owdocprops.iconrule;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Abstract implementation of OwStatusIconRule, which creates a HTML-img representation.
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
public abstract class OwStatusIconAbstractRule implements OwStatusIconRule
{
    private String description;
    private String icon;

    public OwStatusIconAbstractRule(OwXMLUtil config, OwAppContext context) throws OwException
    {
        setIcon(config.getSafeStringAttributeValue("icon", null));
        if (getIcon() == null)
        {
            throw new OwConfigurationException("Please specify a icon attribute.");
        }

        setDescription(config.getSafeStringAttributeValue("tooltip", null));
    }

    public String getIcon()
    {
        return this.icon;
    }

    public void setIcon(String iconConfiguration)
    {
        this.icon = iconConfiguration;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
