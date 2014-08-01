package com.wewebu.ow.server.ecmimpl.fncm5.perm;

import java.util.Collection;
import java.util.Iterator;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.PermissionSource;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.SecurityPrincipalType;
import com.filenet.api.constants.SpecialPrincipal;
import com.filenet.api.core.Factory;
import com.filenet.api.security.AccessPermission;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Simple helper to use for investigation of access rights mask.
 * Or creating new Entries or copies of native permissions objects
 * and collections.
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
@SuppressWarnings("unchecked")
public class OwFNCM5PermissionHelper
{

    /**
     * Check if the access mask match some of the user rights,
     * if not the groups rights are compared to exact match.
     * @param perms OwFNCM5Permissions
     * @param principal OwBaseUserInfo
     * @param accessMask int access rights mask
     * @return boolean match with provided access rights mask
     * @throws OwException
     */
    public static boolean hasPermission(OwFNCM5Permissions perms, OwBaseUserInfo principal, int accessMask) throws OwException
    {
        try
        {
            AccessPermissionList permLst = perms.getNativeObject();
            Iterator<?> it = permLst.iterator();
            while (it.hasNext())
            {
                AccessPermission perm = (AccessPermission) it.next();
                String p8name = perm.get_GranteeName();
                if (p8name.equals(principal.getUserLongName()) || p8name.equals(principal.getUserShortName()) || p8name.equals(principal.getUserName()))
                {
                    return hasPermission(perm, accessMask);
                }
            }
            int accumulated = getAccumulatedGroupsPermission(permLst, principal.getGroups());
            return (accumulated & accessMask) == accessMask;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not verify permissions.", e);
        }
    }

    /**
     * Identify if one of the access mask flags matches the current permission or not.
     * <p>Will return true if any part of access mask is matching with provided permission.</p>
     * @param perm AccessPermission to check
     * @param accessMask int mask to be verified against permission
     * @return boolean any match true, no match false
     */
    public static boolean hasPermission(AccessPermission perm, int accessMask)
    {
        if (AccessType.ALLOW == perm.get_AccessType())
        {
            return (perm.get_AccessMask() & accessMask) != 0;
        }
        else
        {
            return !((perm.get_AccessMask() & accessMask) != 0);
        }
    }

    /**
     * Will analyze which AccessRights are handled/defined
     * in the given permission list for the set of groups.
     * @param permLst AccessPermissionList native permission list
     * @param groups Collection of OwBaseUserInfo objects
     * @return int representing the accumulated access rights
     * @throws OwException if cannot access the OwBaseUserInfo attributes(long name, shortname, ...)
     */
    public static int getAccumulatedGroupsPermission(AccessPermissionList permLst, Collection<?> groups) throws OwException
    {
        try
        {
            int accumulated = 0;
            if (!groups.isEmpty())
            {
                Iterator<?> it = permLst.iterator();
                int denied = 0;
                while (it.hasNext())
                {
                    AccessPermission perm = (AccessPermission) it.next();
                    String p8name = perm.get_GranteeName();
                    Iterator<?> itGrp = groups.iterator();
                    while (itGrp.hasNext())
                    {
                        OwBaseUserInfo grp = (OwBaseUserInfo) itGrp.next();
                        if (p8name.equals(grp.getUserLongName()) || p8name.equals(grp.getUserShortName()) || p8name.equals(grp.getUserName()))
                        {
                            if (perm.get_AccessType() == AccessType.ALLOW)
                            {
                                accumulated = accumulated | perm.get_AccessMask();
                            }
                            else
                            {
                                denied = denied | perm.get_AccessMask();
                            }
                        }
                    }
                }
                /* remove from accumulated allow,
                 * the whole set of denied rights*/
                accumulated = accumulated & ~denied;
            }
            return accumulated;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could retrieve permissions.", e);
        }
    }

    /**
     * Will check the access rights for provided user.
     * @param permLst AccessPermissionList native list to use
     * @param user OwBaseUserInfo to use for compare
     * @return integer access rights mask with valid accumulated permission
     * @throws OwException if could not retrieve user long name, short name, ...
     */
    public static int getAccumulatedUserPermission(AccessPermissionList permLst, OwBaseUserInfo user) throws OwException
    {
        try
        {
            int userdenied = 0, accumulated = 0;
            Iterator<?> it = permLst.iterator();
            while (it.hasNext())
            {
                AccessPermission perm = (AccessPermission) it.next();
                String p8name = perm.get_GranteeName();
                if (p8name.equals(user.getUserLongName()) || p8name.equals(user.getUserShortName()) || p8name.equals(user.getUserName()))
                {
                    if (perm.get_AccessType() == AccessType.ALLOW)
                    {
                        accumulated = perm.get_AccessMask();
                    }
                    else
                    {
                        userdenied = perm.get_AccessMask();
                    }
                }
            }
            accumulated = accumulated | getAccumulatedGroupsPermission(permLst, user.getGroups());

            return accumulated & ~userdenied;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could retrieve permissions.", e);
        }
    }

    /**
     * Create a copy of the provided list.
     * @param permList AccessPermissionList to be copied
     * @return AccessPermissionList which is a fully copy of the provided list
     */
    public static AccessPermissionList createCopy(AccessPermissionList permList)
    {
        AccessPermissionList lst = createPermissionList();
        Iterator<?> it = permList.iterator();
        while (it.hasNext())
        {
            AccessPermission perm = (AccessPermission) it.next();
            lst.add(createCopy(perm));
        }
        return lst;
    }

    /**
     * Create an empty AccessPermissionList using native factory methods.
     * @return AccessPermissionList
     */
    public static AccessPermissionList createPermissionList()
    {
        return Factory.AccessPermission.createList();
    }

    /**
     * Create a copy of the provided permission,
     * will copy the mandatory information of the object into a new instance.
     * @param permission AccessPermission to be copied
     * @return AccessPermission copy of the provided one
     */
    public static AccessPermission createCopy(AccessPermission permission)
    {
        return createAccessPermission(permission.get_GranteeName(), permission.get_AccessType(), permission.get_AccessMask(), permission.get_InheritableDepth(), permission.get_PermissionSource(), permission.get_GranteeType());
    }

    /**
     * Create an AccessPermission from given information,
     * will create a new instance and setting the provided information.
     * @param granteeName String name of the grantee
     * @param type AccessType as defined by native API
     * @param accessMask Integer access mask representing the rights
     * @param depth Integer depth which will be defined by this permission
     * @param permSrc PermissionSource of that entry
     * @param secPrincType SecurityPrincipalType
     * @return AccessPermission with defined settings
     */
    public static AccessPermission createAccessPermission(String granteeName, AccessType type, Integer accessMask, Integer depth, PermissionSource permSrc, SecurityPrincipalType secPrincType)
    {
        AccessPermission perm = com.filenet.api.core.Factory.AccessPermission.createInstance();
        perm.set_GranteeName(granteeName);
        perm.getProperties().putValue(PropertyNames.GRANTEE_TYPE, Integer.valueOf(secPrincType.getValue()));
        perm.set_AccessType(type);
        perm.set_AccessMask(accessMask);
        perm.set_InheritableDepth(depth);
        perm.getProperties().putValue(PropertyNames.PERMISSION_SOURCE, Integer.valueOf(permSrc.getValue()));

        return perm;
    }

    /**
     * Processing both provided permission and creating a new permission as result.
     * Will analyze the access type and choose the deny, since it is the highest in permission
     * structure. If both are from the same access type (allow or deny), the access mask is
     * merged using an binary OR function and also the inheritable depth of first parameter.
     * <p>
     *  Attention: <b>GranteeName</b>, <b>type</b> and <b>permission source</b> are not set through
     *  this method and must be set in the resulting permission.
     * </p>  
     * is assigned to the the merged permission.
     * @param aPerm AccessPermission
     * @param bPerm AccessPermission
     * @return AccessPermission
     */
    public static AccessPermission mergePermission(AccessPermission aPerm, AccessPermission bPerm)
    {
        AccessPermission perm = com.filenet.api.core.Factory.AccessPermission.createInstance();
        if (aPerm.get_AccessType() == bPerm.get_AccessType())
        {
            perm.set_AccessType(aPerm.get_AccessType());
            perm.set_AccessMask(aPerm.get_AccessMask() | bPerm.get_AccessMask());
            perm.set_InheritableDepth(aPerm.get_InheritableDepth());
        }
        else
        {
            if (aPerm.get_AccessType() == AccessType.DENY)
            {
                perm.set_AccessType(aPerm.get_AccessType());
                perm.set_AccessMask(aPerm.get_AccessMask());
                perm.set_InheritableDepth(aPerm.get_InheritableDepth());
            }
            else
            {
                perm.set_AccessType(bPerm.get_AccessType());
                perm.set_AccessMask(bPerm.get_AccessMask());
                perm.set_InheritableDepth(bPerm.get_InheritableDepth());
            }
        }
        return perm;
    }

    /**
     * Will Search for #Creator-Owner and user (grantee) occurrence in
     * provided permission list and replace them with a resulting permission.
     * Also the place-holder #Creator-Owner will be replaced with the current user name.
     * @param lst AccessPermissionList
     * @param user OwUserInfo for whom the permission are created
     * @return AccessPermissionList with corrected rights definitions.
     * @throws OwException
     */
    public static AccessPermissionList consolidateCreatorRights(AccessPermissionList lst, OwUserInfo user) throws OwException
    {
        try
        {
            AccessPermission c = null, u = null;
            Iterator<?> it = lst.iterator();
            while (it.hasNext())
            {
                AccessPermission p = (AccessPermission) it.next();
                if (SpecialPrincipal.CREATOR_OWNER.getValue().equals(p.get_GranteeName()))
                {//Permission #Create-Owner
                    c = p;
                    c.set_GranteeName(user.getUserName());
                }
                if (p.get_GranteeName().equalsIgnoreCase(user.getUserName()))
                {//Permission for current user already there
                    u = p;
                }
            }

            if (c != null && u != null)
            {//consolidate doubled entry into one resulting permission
                return consolidate(lst, c, u, user);
            }
            else
            {//no duplication there, keep it like it is.
                return lst;
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not consolidate creator rights.", e);
        }

    }

    /**
     * Parse through the list of permission removing permission
     * for the same user with a merged solution.
     * @param lst AccessPermissionList current list
     * @param creator AccessPermission creator rights
     * @param user AccessPermission user rights
     * @param usrInfo OwUserInfo current user
     * @return AccessPermissionList which represents a merged result
     * @throws Exception
     */
    public static AccessPermissionList consolidate(AccessPermissionList lst, AccessPermission creator, AccessPermission user, OwUserInfo usrInfo) throws Exception
    {
        try
        {
            AccessPermission perm = mergePermission(creator, user);
            perm.set_GranteeName(usrInfo.getUserName());

            perm.getProperties().putValue(PropertyNames.GRANTEE_TYPE, Integer.valueOf(usrInfo.isGroup() ? SecurityPrincipalType.GROUP_AS_INT : SecurityPrincipalType.USER_AS_INT));
            perm.getProperties().putValue(PropertyNames.PERMISSION_SOURCE, Integer.valueOf(PermissionSource.SOURCE_DEFAULT.getValue()));
            AccessPermissionList newlst = createPermissionList();
            newlst.add(perm);

            Iterator<?> it = lst.iterator();
            while (it.hasNext())
            {
                perm = (AccessPermission) it.next();
                if (!usrInfo.getUserName().equals(perm.get_GranteeName()))
                {
                    newlst.add(perm);
                }
            }

            return newlst;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not consolidate permissions.", e);
        }
    }
}
