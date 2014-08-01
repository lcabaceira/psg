package com.wewebu.ow.server.app.id.viid;

/**
 *<p>
 * Enumeration of Types.
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
public enum OwVIIdType
{
    CUSTOM("c"), FOLDER("f"), DOCUMENT("d"), WORKITEM("w"), LINK("l"), QUEUE("q");

    private String prefix;

    private OwVIIdType(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * Get prefix of this type. 
     * @return String
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Get type from prefix. Will return null if prefix is unknown, or provided prefix is a null value. 
     * @param prefix String (can be null)
     * @return OwVIObjectIdType, or null
     */
    public static OwVIIdType getFromPrefix(String prefix)
    {
        if (prefix != null)
        {
            for (OwVIIdType type : OwVIIdType.values())
            {
                if (type.getPrefix().equals(prefix))
                {
                    return type;
                }
            }
        }
        return null;
    }
}
