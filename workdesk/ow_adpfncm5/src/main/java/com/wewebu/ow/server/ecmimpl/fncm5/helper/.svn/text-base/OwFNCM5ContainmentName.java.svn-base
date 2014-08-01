package com.wewebu.ow.server.ecmimpl.fncm5.helper;

/**
 *<p>
 * Wrapper over string. Knows how to get rid of FNCM5 offending characters.
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
 *@since 4.0.0.1
 */
public class OwFNCM5ContainmentName
{
    private String containmentName;

    public OwFNCM5ContainmentName(String name)
    {
        this.containmentName = removeOffendingCharacters(name);
    }

    private String removeOffendingCharacters(String name)
    {
        if (null == name)
        {
            return name;
        }
        String result = name.replaceAll("[\\\\/:\\*\\?\"<>|]", "");
        return result;
    }

    @Override
    public String toString()
    {
        return this.containmentName;
    }
}
