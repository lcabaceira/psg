package com.wewebu.ow.server.ui.viewer;

/**
 *<p>
 * Enumeration representing the information
 * to request from annotation.
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
public enum OwAnnotInfoEnum
{
    /**View access rights*/
    VIEW_ANNOTATION("view_annotation"),
    /**MODIFY access rights*/
    MODIFY_ANNOTATION("modify_annotation"),
    /**DELETE access rights*/
    DELETE_ANNOTATION("delete_annotation"),
    /**View ACL of annotation*/
    VIEW_ANNOTATION_ACL("view_acl"),
    /**Edit ACL of annotation*/
    EDIT_ANNOTATION_ACL("edit_acl");

    String type;

    private OwAnnotInfoEnum(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    /**
     * Will search for an entry with given type. 
     * @param type String matching value of enumeration entry
     * @return OwAnnotInfoEnum or null if not found
     */
    public static OwAnnotInfoEnum getEnumByType(String type)
    {
        for (OwAnnotInfoEnum entry : values())
        {
            if (entry.getType().equals(type))
            {
                return entry;
            }
        }
        return null;
    }
}
