package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Standard policy bean implementation.
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
 *@since 3.1.0.0
 */
public class OwStandardPolicy implements OwPolicy
{

    private String name;
    private String description;

    public OwStandardPolicy(String name_p, String description_p)
    {
        super();
        this.name = name_p;
        this.description = description_p;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwStandardPolicy)
        {
            OwStandardPolicy policyObj = (OwStandardPolicy) obj_p;
            return this.name != null ? name.equals(policyObj.name) : policyObj.name == null;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return this.name != null ? this.name.hashCode() : "".hashCode();
    }
}
