package com.wewebu.ow.server.ecmimpl.fncm5.perm;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.PermissionSource;
import com.filenet.api.constants.SecurityPrincipalType;
import com.filenet.api.security.AccessPermission;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPolicy;
import com.wewebu.ow.server.ecm.OwPrivilegeSet;
import com.wewebu.ow.server.ecm.OwReason;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.OwStandardReason;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * OwPermissionCollection implementation for P8 5.0.
 * Handling the native collection of Permission objects.
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
@SuppressWarnings("rawtypes")
public class OwFNCM5Permissions implements OwPermissionCollection
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Permissions.class);

    private AccessPermissionList nativeObject;
    private OwNetworkContext context;

    public OwFNCM5Permissions(AccessPermissionList nativePermissions, OwNetworkContext context)
    {
        this.nativeObject = nativePermissions;
        this.context = context;
    }

    public OwReason canEditPermissions()
    {
        return OwStandardReason.ALLOWED;
    }

    public boolean canGetPrivileges()
    {
        return false;
    }

    public boolean canSetPrivileges()
    {
        return false;
    }

    public boolean canDenyPrivileges()
    {
        return false;
    }

    public Collection getAvailablePrivileges(OwUserInfo principal_p)
    {
        return null;
    }

    public Map getAvailableInheritanceDepths()
    {
        return null;
    }

    public OwPrivilegeSet addPrivilegeSet(OwUserInfo principal_p, Collection privileges_p, boolean deny_p, int inheritancedepth_p) throws OwException
    {
        return null;
    }

    public Collection getAppliedPrivilegeSets()
    {
        return null;
    }

    public void removePrivilegeSet(OwPrivilegeSet privilegeset_p) throws OwException
    {

    }

    public boolean canGetPolicies()
    {
        return false;
    }

    public boolean canSetPolicies()
    {
        return false;
    }

    public boolean canAddMultiPolicy()
    {
        return false;
    }

    public Collection getAvailablePolicies(OwUserInfo principal_p)
    {
        return null;
    }

    public void addPolicy(OwPolicy policy_p) throws OwException
    {

    }

    public Collection getAppliedPolicies()
    {
        return null;
    }

    public void removePolicy(OwPolicy policy_p) throws OwException
    {

    }

    /**
     * Return the native list of permission's.
     * @return AccessPermissionList
     */
    public AccessPermissionList getNativeObject()
    {
        return this.nativeObject;
    }

    /**
     * Create a new permission entry with
     * <ul>
     * <li>AccessType=Allow</li>
     * <li>Access = Read</li>
     * </ul>
     * @param userInfo OwBaseUserInfo
     * @throws OwException
     * @see OwFNCM5Permissions#createNewEntry(OwBaseUserInfo, int, AccessType)
     */
    public void createNewEntry(OwBaseUserInfo userInfo) throws OwException
    {
        createNewEntry(userInfo, AccessRight.READ_AS_INT | AccessRight.READ_ACL_AS_INT);
    }

    /**
     * Create a new permission entry with provided
     * values as Allowed construction.
     * @param userInfo OwBaseUserInfo
     * @param accessMask int 
     * @throws OwException
     * @see OwFNCM5Permissions#createNewEntry(OwBaseUserInfo, int, AccessType)
     */
    public void createNewEntry(OwBaseUserInfo userInfo, int accessMask) throws OwException
    {
        createNewEntry(userInfo, accessMask, AccessType.ALLOW);
    }

    /**
     * Creates a new entry with provided information.
     * @param userInfo OwBaseUserInfo
     * @param accessMask int representing Access rights
     * @param type AccessType [Allow|Deny]
     * @throws OwException
     */
    @SuppressWarnings("unchecked")
    public void createNewEntry(OwBaseUserInfo userInfo, int accessMask, AccessType type) throws OwException
    {
        try
        {
            AccessPermission perm = OwFNCM5PermissionHelper.createAccessPermission(userInfo.getUserLongName(), type, Integer.valueOf(accessMask), Integer.valueOf(0), PermissionSource.SOURCE_DIRECT, userInfo.isGroup() ? SecurityPrincipalType.GROUP
                    : SecurityPrincipalType.USER);
            getNativeObject().add(perm);
        }
        catch (Exception e)
        {
            String msg = "Could not retrieve user name for permission creation.";
            LOG.warn(msg, e);
            throw new OwServerException(getContext().localize("OwFNCM5Permissions.createEntry.userName.error", "Entry could not be created."), e);
        }
    }

    /**
     * Context used for localization.
     * @return OwNetworkContext of the current object
     */
    protected OwNetworkContext getContext()
    {
        return this.context;
    }
}