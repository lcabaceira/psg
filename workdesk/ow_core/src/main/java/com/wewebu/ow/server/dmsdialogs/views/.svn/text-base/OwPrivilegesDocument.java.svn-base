package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPrivilege;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.ui.OwDocument;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * {@link OwPermissionCollection} privilege facade document for {@link OwPrivilegesView}
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
public class OwPrivilegesDocument extends OwDocument
{
    private static final Map<String, String> privilegeGUIDMap = new HashMap<String, String>();
    private static final Map<String, String> guidPrivilegeMap = new HashMap<String, String>();
    private static long lastGUID = 0;

    public synchronized static final String guidToPrivilege(String guid_p) throws OwInvalidOperationException
    {
        String privilege = guidPrivilegeMap.get(guid_p);
        if (privilege == null)
        {
            throw new OwInvalidOperationException("Invalid privilege GUID : " + guid_p);
        }

        return privilege;
    }

    public synchronized static final String privilegeToGuid(OwPrivilege privilege_p)
    {
        return privilegeToGuid(privilege_p.getName());
    }

    public synchronized static final String privilegeToGuid(String privilegeName_p)
    {
        String guid = privilegeGUIDMap.get(privilegeName_p);
        if (guid == null)
        {
            lastGUID++;
            guid = "" + lastGUID;
            privilegeGUIDMap.put(privilegeName_p, guid);
            guidPrivilegeMap.put(guid, privilegeName_p);
        }

        return guid;
    }

    private OwPermissionsDocument permissionsDocument;
    private Map<String, String> privilegeDisplayNames;

    public OwPrivilegesDocument(OwPermissionsDocument permissionsDocument_p, Map<String, String> privilegeDisplayNames_p)
    {
        this.permissionsDocument = permissionsDocument_p;
        this.privilegeDisplayNames = privilegeDisplayNames_p;
    }

    protected OwPermissionCollection getPermissions()
    {
        return permissionsDocument.getPermissions();
    }

    protected OwPermissionsDocument getPermissionsDocument()
    {
        return permissionsDocument;
    }

    public OwObject getObject()
    {
        return permissionsDocument.getObject();
    }

    public Collection<OwPrivilegeSet> getAppliedPrivilegeSets()
    {
        OwPermissionCollection permissions = getPermissions();
        return permissions.getAppliedPrivilegeSets();
    }

    public void removePrivilegeSet(OwPrivilegeSet privilegeSet_p) throws OwException
    {
        OwPermissionCollection permissions = getPermissions();
        permissions.removePrivilegeSet(privilegeSet_p);
    }

    public String displayNameOf(OwPrivilege privilege_p)
    {
        String name = privilege_p.getName();
        String displayName = privilegeDisplayNames.get(name.trim());
        if (displayName != null)
        {
            return displayName;
        }
        else
        {
            return getContext().localize(OwString.LABEL_PREFIX + name, name);
        }
    }

    public Collection<OwPrivilege> getAvailablePrivileges(OwUserInfo principal_p)
    {
        OwPermissionCollection permissions = getPermissions();
        return permissions.getAvailablePrivileges(principal_p);
    }

    public OwPrivilegeSet addPrivilegeSet(OwUserInfo principal_p, Collection privileges_p, boolean deny_p, int inheritancedepth_p) throws OwException
    {
        OwPermissionCollection permissions = getPermissions();
        return permissions.addPrivilegeSet(principal_p, privileges_p, deny_p, inheritancedepth_p);
    }

}
