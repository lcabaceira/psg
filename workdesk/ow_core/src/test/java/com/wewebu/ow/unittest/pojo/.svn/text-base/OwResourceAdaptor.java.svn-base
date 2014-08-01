package com.wewebu.ow.unittest.pojo;

import java.util.Locale;

import com.wewebu.ow.server.ecm.OwResource;

/**
 * Simple class to create a POJO OwResource.
 * Returning valid values by default, but still
 * the derived class should implement the needed methods.
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
public class OwResourceAdaptor implements OwResource
{
    public String id;
    public String displayName;
    public String description;

    public String getDescription(Locale locale_p)
    {
        return this.description;
    }

    public String getDisplayName(Locale locale_p)
    {
        return this.displayName;
    }

    public String getID() throws Exception
    {
        return this.id;
    }

}
