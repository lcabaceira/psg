package com.wewebu.ow.server.ecmimpl.fncm5.ui;

import com.filenet.api.constants.AccessRight;

/**
 *<p>
 * Helper class for permission rendering.
 * Represents a column name and dedicated Id, for corresponding working.
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
public class OwFNCM5ColumnHeader
{
    /**A mask for headers which are not directly associated with a permission setting.*/
    public static final int NONE_MASK = ~0;

    public static final int PRINCIPAL_ICON_INT = 0;
    /** Non ACL header, named = PrincipalIcon*/
    public static final OwFNCM5ColumnHeader PRINCIPAL_ICON = new OwFNCM5ColumnHeader("PrincipalIcon", PRINCIPAL_ICON_INT, NONE_MASK);
    public static final int PRINCIPAL_NAME_INT = 1;
    /** Non ACL header, named = PrincipalName*/
    public static final OwFNCM5ColumnHeader PRINCIPAL_NAME = new OwFNCM5ColumnHeader("PrincipalName", PRINCIPAL_NAME_INT, NONE_MASK);
    public static final int DEPTH_INT = 2;
    /** Non ACL header, named = Depth*/
    public static final OwFNCM5ColumnHeader DEPTH = new OwFNCM5ColumnHeader("Depth", DEPTH_INT, NONE_MASK);
    public static final int DELETE_BTN_INT = 3;
    /** Non ACL header, named = DeleteBtn for delete button*/
    public static final OwFNCM5ColumnHeader DELETE_BTN = new OwFNCM5ColumnHeader("DeleteBtn", DELETE_BTN_INT, NONE_MASK);
    public static final int OWNER_RIGHT_INT = 4;
    /**ACL header, named = Owner*/
    public static final OwFNCM5ColumnHeader OWNER_RIGHT = new OwFNCM5ColumnHeader("Owner", OWNER_RIGHT_INT, AccessRight.WRITE_OWNER_AS_INT);
    public static final int PROPS_VIEW_INT = 5;
    /**ACL header, named = PropsView*/
    public static final OwFNCM5ColumnHeader PROPS_VIEW = new OwFNCM5ColumnHeader("PropsView", PROPS_VIEW_INT, AccessRight.READ_AS_INT);
    public static final int PROPS_EDIT_INT = 6;
    /**ACL header, named = PropsEdit*/
    public static final OwFNCM5ColumnHeader PROPS_EDIT = new OwFNCM5ColumnHeader("PropsEdit", PROPS_EDIT_INT, AccessRight.WRITE_AS_INT);
    public static final int CREATE_SUBFOLDER_INT = 7;
    /**ACL header, named = CreateFolder*/
    public static final OwFNCM5ColumnHeader CREATE_SUBFOLDER = new OwFNCM5ColumnHeader("CreateFolder", CREATE_SUBFOLDER_INT, AccessRight.CREATE_CHILD_AS_INT);
    public static final int FILE_DOCUMENT_INT = 8;
    /**ACL header, named = FileIn*/
    public static final OwFNCM5ColumnHeader FILE_DOCUMENT = new OwFNCM5ColumnHeader("FileIn", FILE_DOCUMENT_INT, AccessRight.LINK_AS_INT);
    public static final int VERSIONING_INT = 9;
    /**CL header, named = Version*/
    public static final OwFNCM5ColumnHeader VERSIONING = new OwFNCM5ColumnHeader("Version", VERSIONING_INT, AccessRight.CHANGE_STATE_AS_INT);
    public static final int CONTENT_VIEW_INT = 10;
    /**ACL header, named = ContentView*/
    public static final OwFNCM5ColumnHeader CONTENT_VIEW = new OwFNCM5ColumnHeader("ContentView", CONTENT_VIEW_INT, AccessRight.VIEW_CONTENT_AS_INT);
    public static final int CONTENT_MOD_INT = 11;
    /**ACL header, named = ContentMod*/
    public static final OwFNCM5ColumnHeader CONTENT_MOD = new OwFNCM5ColumnHeader("ContentMod", CONTENT_MOD_INT, AccessRight.MINOR_VERSION_AS_INT | AccessRight.MAJOR_VERSION_AS_INT);
    public static final int PUBLISH_INT = 12;
    /**ACL header, named = Publish*/
    public static final OwFNCM5ColumnHeader PUBLISH = new OwFNCM5ColumnHeader("Publish", PUBLISH_INT, AccessRight.PUBLISH_AS_INT);

    private String name;
    private int id;
    private int accessMask;

    public OwFNCM5ColumnHeader(String name, int id, int accessMask)
    {
        this.id = id;
        this.name = name;
        this.accessMask = accessMask;
    }

    /**
     * Name of the header, used for several purposes.
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Id of the header.
     * @return int
     */
    public int getId()
    {
        return id;
    }

    /**
     * Return the AccessMask for
     * the right to be set/active
     * if enabled.
     * @return int
     */
    public int getAccessMask()
    {
        return accessMask;
    }

    @Override
    public int hashCode()
    {
        int result = 31;
        result = result + id;
        result = result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        OwFNCM5ColumnHeader other = (OwFNCM5ColumnHeader) obj;
        if (id != other.id)
        {
            return false;
        }
        if (name == null && other.name != null)
        {
            return false;
        }
        else
        {
            if (name == null)
            {
                return null == other.name;
            }
            else
            {
                return name.equals(other.name);
            }
        }
    }

}
