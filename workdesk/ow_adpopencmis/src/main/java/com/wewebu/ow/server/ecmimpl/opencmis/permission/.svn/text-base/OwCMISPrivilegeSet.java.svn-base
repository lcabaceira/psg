package com.wewebu.ow.server.ecmimpl.opencmis.permission;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.chemistry.opencmis.commons.data.Ace;

import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 *  Simple OwPrivilegeSet implementation.<br/>
 *  Will not handle propagation definition.
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
public class OwCMISPrivilegeSet implements OwPrivilegeSet
{
    /**Number representing PROPAGATED inheritance*/
    public static final int INHERITANCE_DEPTH_PROPAGATED = 100;
    private Ace ace;
    private OwUserInfo principal;

    public OwCMISPrivilegeSet(OwUserInfo principal, Ace ace)
    {
        this.ace = ace;
        this.principal = principal;
    }

    @Override
    public OwUserInfo getPrincipal()
    {
        return this.principal;
    }

    @Override
    public boolean isDenied()
    {
        return false;
    }

    @Override
    public Collection getPrivileges()
    {
        LinkedList<OwCMISPrivilege> privileges = new LinkedList<OwCMISPrivilege>();
        for (String name : ace.getPermissions())
        {
            privileges.add(new OwCMISPrivilege(ace, name));
        }
        return privileges;
    }

    @Override
    public int getInheritanceDepth()
    {
        return ace.isDirect() ? INHERITANCE_DEPTH_NO_INHERITANCE : INHERITANCE_DEPTH_UNLIMITED;
    }

    public Ace nativeObject()
    {
        return ace;
    }

    @Override
    public int hashCode()
    {
        final int prime = 811;
        int result = ace.isDirect() ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
        result = prime * result + ace.getPrincipalId().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        OwCMISPrivilegeSet other = (OwCMISPrivilegeSet) obj;
        if (ace == null)
        {
            if (other.ace != null)
            {
                return false;
            }
        }
        else
        {
            if (!principal.getUserID().equals(other.principal.getUserID()))
            {
                return false;
            }

            if (ace.isDirect() != other.ace.isDirect())
            {
                return false;
            }

            if (!(ace.getPermissions().size() == other.ace.getPermissions().size()))
            {
                return false;
            }
        }
        return true;
    }

}
