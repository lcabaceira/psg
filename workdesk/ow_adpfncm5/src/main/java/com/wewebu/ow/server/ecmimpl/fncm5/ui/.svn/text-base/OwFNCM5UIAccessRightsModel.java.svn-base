package com.wewebu.ow.server.ecmimpl.fncm5.ui;

import java.util.LinkedList;
import java.util.List;

import com.filenet.api.constants.SecurityPrincipalType;
import com.filenet.api.security.Permission;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.exceptions.OwRuntimeException;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Class used for rendering the Permission representation.
 * Contains a list of defined headers for specific object.
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
public class OwFNCM5UIAccessRightsModel
{
    public static final List<OwFNCM5ColumnHeader> HEADER_DEFAULT = new LinkedList<OwFNCM5ColumnHeader>();
    static
    {
        HEADER_DEFAULT.add(OwFNCM5ColumnHeader.PRINCIPAL_ICON);
        HEADER_DEFAULT.add(OwFNCM5ColumnHeader.PRINCIPAL_NAME);
        //        HEADER_DEFAULT.add(OwFNCM5ColumnHeader.DEPTH);
        HEADER_DEFAULT.add(OwFNCM5ColumnHeader.DELETE_BTN);
    }

    public static final List<OwFNCM5ColumnHeader> HEADER_FOLDER = new LinkedList<OwFNCM5ColumnHeader>();
    static
    {
        HEADER_FOLDER.add(OwFNCM5ColumnHeader.OWNER_RIGHT);
        HEADER_FOLDER.add(OwFNCM5ColumnHeader.PROPS_EDIT);
        HEADER_FOLDER.add(OwFNCM5ColumnHeader.CREATE_SUBFOLDER);
        HEADER_FOLDER.add(OwFNCM5ColumnHeader.FILE_DOCUMENT);
        HEADER_FOLDER.add(OwFNCM5ColumnHeader.PROPS_VIEW);
    }

    public static final List<OwFNCM5ColumnHeader> HEADER_DOCUMENT = new LinkedList<OwFNCM5ColumnHeader>();
    static
    {
        HEADER_DOCUMENT.add(OwFNCM5ColumnHeader.OWNER_RIGHT);
        HEADER_DOCUMENT.add(OwFNCM5ColumnHeader.VERSIONING);
        HEADER_DOCUMENT.add(OwFNCM5ColumnHeader.CONTENT_MOD);
        HEADER_DOCUMENT.add(OwFNCM5ColumnHeader.PROPS_EDIT);
        HEADER_DOCUMENT.add(OwFNCM5ColumnHeader.CONTENT_VIEW);
        HEADER_DOCUMENT.add(OwFNCM5ColumnHeader.PROPS_VIEW);
        HEADER_DOCUMENT.add(OwFNCM5ColumnHeader.PUBLISH);
    }

    public static final List<OwFNCM5ColumnHeader> HEADER_CUSTOM = new LinkedList<OwFNCM5ColumnHeader>();
    static
    {
        HEADER_CUSTOM.add(OwFNCM5ColumnHeader.OWNER_RIGHT);
        HEADER_CUSTOM.add(OwFNCM5ColumnHeader.PROPS_EDIT);
        HEADER_CUSTOM.add(OwFNCM5ColumnHeader.PROPS_VIEW);
    }

    protected OwAppContext context;

    protected String iconAllow, iconAllowDisabled, iconDeny, iconDenyDisabled;
    private String designDir;
    private List<OwFNCM5ColumnHeader> headers;

    public OwFNCM5UIAccessRightsModel(int objectType)
    {
        headers = new LinkedList<OwFNCM5ColumnHeader>();
        headers.addAll(HEADER_DEFAULT);
        switch (objectType)
        {
            case OwObjectReference.OBJECT_TYPE_CUSTOM:
            {
                headers.addAll(HEADER_CUSTOM);
            }
                break;
            case OwObjectReference.OBJECT_TYPE_DOCUMENT:
            {
                headers.addAll(HEADER_DOCUMENT);
            }
                break;
            case OwObjectReference.OBJECT_TYPE_FOLDER:
            {
                headers.addAll(HEADER_FOLDER);
            }
                break;
            default:
                ;
        }
    }

    /**
     * Called to initialize the Model.
     * Will call the {@link #init()} method if main initialization is successful.
     * <p>Attention: Will throw an OwRuntimeException if design directory could
     * not be retrieved from OwAppContext.</p>
     * @param ctxt OwAppContext to use for initialization
     */
    protected final void init(OwAppContext ctxt)
    {
        this.context = ctxt;
        try
        {
            designDir = getContext().getDesignURL();
        }
        catch (Exception e)
        {
            throw new OwRuntimeException("Could not retrieve design directory location", e) {

                private static final long serialVersionUID = 3341906302533122439L;

                @Override
                public String getModulName()
                {
                    return "fncm5.ui";
                }
            };
        }
        init();
    }

    /**(overridable)
     * Initialization method.
     * By default this method initialize only the links to the principal icon's.
     */
    protected void init()
    {
        iconAllow = getDesignDir() + "/images/acl/allow.png";
        iconAllowDisabled = getDesignDir() + "/images/acl/allow_disabled.png";
        iconDeny = getDesignDir() + "/images/acl/deny.png";
        iconDenyDisabled = getDesignDir() + "/images/acl/deny_disabled.png";
    }

    /**
     * Current design dir to use.
     * @return String representing the design dir.
     */
    public String getDesignDir()
    {
        return this.designDir;
    }

    /**
     * Return the headers which should be rendered by the AccessRightsModul.
     * <p>Should be kept consistent per instance, since it is called many times.</p>
     * @return List of OwFNCM5ColumnHeader
     */
    public List<OwFNCM5ColumnHeader> getHeaders()
    {
        return headers;
    }

    /** (overridable)
     * Get the icon for current access right!
     * @param allow boolean is access right allow
     * @param enabled boolean should icon represent enabled (modify-able)
     * @return String representing the path to the icon
     */
    public String getAclIcon(boolean allow, boolean enabled)
    {
        if (allow)
        {
            return enabled ? iconAllow : iconAllowDisabled;
        }
        else
        {
            return enabled ? iconDeny : iconDenyDisabled;
        }
    }

    /**(overridable)
     * Return an String representing the icon location
     * for current provided Permission.
     * @param perm Permission
     * @return String, or empty String (&quot;&quot;)
     */
    public String getPrincipalIcon(Permission perm)
    {
        switch (perm.get_GranteeType().getValue())
        {
            case SecurityPrincipalType.USER_AS_INT:
                return getDesignDir() + "/images/user.png";
            case SecurityPrincipalType.GROUP_AS_INT:
                return getDesignDir() + "/images/group.png";
            default:
                return "";
        }
    }

    /**(overridable)
     * Return a String representing the tool tip of the icon,
     * can return null if there is none existing.
     * @param perm Permission for which to request
     * @return String or null if none exist
     */
    public String getPrincipalIconTooltip(Permission perm)
    {
        switch (perm.get_GranteeType().getValue())
        {
            case SecurityPrincipalType.USER_AS_INT:
            {
                return getContext().localize("OwFNCM5UIAccessRightsModel.icon.user.desc", "User");
            }
            case SecurityPrincipalType.GROUP_AS_INT:
            {
                return getContext().localize("OwFNCM5UIAccessRightsModel.icon.group.desc", "Group");
            }
            default:
                return null;
        }
    }

    /**
     * Return the current OwAppContext,
     * can be null if initialization was not done.
     * @return OwAppContext (or null if model is not initialized)
     */
    public OwAppContext getContext()
    {
        return context;
    }

    /**
     * Return the Header display name.
     * <p>Localization is done by following constructed key:
     * <code>OwFNCM5UIAccessRightsModel.header." + header.getName() + ".title" </code>
     * </p>
     * @param header OwFNCM5ColumnHeader
     * @return String display name of header, or empty String &quot;&quot;
     */
    public String getHeaderDisplayName(OwFNCM5ColumnHeader header)
    {
        return getContext().localize("OwFNCM5UIAccessRightsModel.header." + header.getName() + ".title", "");
    }

}
