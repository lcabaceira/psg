package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * {@link OwPrivilegeSet} document facade for the {@link OwPrivilegeSetEditor} view.   
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
 *@since 3.2.0.0
 */
public class OwPrivilegeSetEditorDocument extends OwDocument
{

    private static class OwPrivilegeEditUserInfo implements OwUserInfo
    {
        private String name;

        public OwPrivilegeEditUserInfo(String name_p)
        {
            super();
            this.name = name_p;
        }

        public String getUserLongName() throws Exception
        {
            return name;
        }

        public String getUserName() throws Exception
        {
            return name;
        }

        public String getUserDisplayName() throws Exception
        {
            return name;
        }

        public String getUserShortName() throws Exception
        {
            return name;
        }

        public String getUserEmailAdress() throws Exception
        {
            return null;
        }

        public String getUserID()
        {
            return name;
        }

        public Collection getRoleNames() throws Exception
        {
            return Collections.EMPTY_LIST;
        }

        public Collection getGroups() throws Exception
        {
            return Collections.EMPTY_LIST;
        }

        public boolean isGroup() throws Exception
        {
            return false;
        }
    }

    private OwPrivilegesDocument privilegesDocument;
    private String currentRole;
    private String[] addedPrivilegeNames = null;
    private Collection<OwPrivilege> addedPrivileges;

    public OwPrivilegeSetEditorDocument(OwPrivilegesDocument privilegesDocument_p)
    {
        super();
        this.privilegesDocument = privilegesDocument_p;
    }

    public String getCurrentRoleName()
    {
        return currentRole;
    }

    public OwUserInfo getCurrentRole()
    {
        return new OwPrivilegeEditUserInfo(currentRole);
    }

    public void setCurrentRole(String currentRole)
    {
        this.currentRole = currentRole;
    }

    public OwObject getObject()
    {
        return privilegesDocument.getObject();
    }

    public Collection<OwPrivilege> getAvailableRolePrivileges()
    {
        if (currentRole == null)
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            return privilegesDocument.getAvailablePrivileges(getCurrentRole());
        }
    }

    public String displayNameOf(OwPrivilege privilege_p)
    {
        return privilegesDocument.displayNameOf(privilege_p);
    }

    public final String privilegeToGuid(OwPrivilege privilege_p)
    {
        return OwPrivilegesDocument.privilegeToGuid(privilege_p.getName());
    }

    public String privilegeToGuid(String privilegeName_p)
    {
        return OwPrivilegesDocument.privilegeToGuid(privilegeName_p);
    }

    public void setAddedPrivileges(String[] privileges_p)
    {
        addedPrivilegeNames = privileges_p;
    }

    public Collection<OwPrivilege> getAddedPrivileges()
    {
        if (addedPrivilegeNames != null && addedPrivilegeNames.length != 0)
        {
            OwUserInfo currentRole = getCurrentRole();
            Collection<OwPrivilege> availablePrivileges = privilegesDocument.getAvailablePrivileges(currentRole);
            addedPrivileges = new LinkedList<OwPrivilege>();
            for (int i = 0; i < addedPrivilegeNames.length; i++)
            {
                for (OwPrivilege owPrivilege : availablePrivileges)
                {
                    if (owPrivilege.getName().equals(addedPrivilegeNames[i]))
                    {
                        addedPrivileges.add(owPrivilege);
                    }
                }
            }
        }
        return addedPrivileges;
    }
}
