package com.wewebu.ow.server.roleimpl.simplerole;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.role.OwStandardRoleManager;

/**
 *<p>
 * Simple role manager, which allows everything.
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
public class OwSimpleRoleManager extends OwStandardRoleManager
{
    /**
     * checks if the current user designated by its credentials is allowed to use the given resource / function
     * 
     * @param iCategory_p of the requested function
     * @param strResourceID_p String ID of the requested resource (function / plugin id)
     *
     * @return true if user has permission, false if permission is denied
     */
    public boolean isAllowed(int iCategory_p, String strResourceID_p) throws Exception
    {
        return (getAccessRights(getCurrentUser().getUserName(), iCategory_p, strResourceID_p) == ROLE_ACCESS_RIGHT_ALLOWED);
    }

    /**
     * checks if the current user designated by its credentials is allowed to use the
     * given resource and has the required right in its access mask for that resource.
     * 
     * @param iCategory_p of the requested function
     * @param strResourceID_p String ID of the requested resource (function / plugin id)
     * @param requiredAccessMask_p a 32 bit bitset with all flags set that have to be checked for the current user
     *
     * @return true if user has permission, false if permission is denied
     */
    public boolean hasAccessMaskRight(int iCategory_p, String strResourceID_p, int requiredAccessMask_p) throws Exception
    {
        // check if user is allowed for this resource at all
        if (!isAllowed(iCategory_p, strResourceID_p))
        {
            return false;
        }
        // check if all required access right flags are set
        return (getAccessMask(getCurrentUser().getUserName(), iCategory_p, strResourceID_p) & requiredAccessMask_p) == requiredAccessMask_p;
    }

    /**
     * Retrieves the access rights for a given role to a given resource.
     * @param roleName_p the name of the role to retrieve the access rights for
     * @param category_p the category of the resource to retrieve the access rights for
     * @param resourceId_p the ID of the resource to retrieve the access rights for
     * @return one of the ROLE_ACCESS_RIGHT_ constants
     * @throws Exception
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_NOT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_DENIED
     */
    public int getAccessRights(String roleName_p, int category_p, String resourceId_p) throws Exception
    {
        // dummy implementation to simulate admin who can edit site settings
        if (category_p == ROLE_CATEGORY_STANDARD_FUNCTION)
        {
            if (resourceId_p.equals(STD_FUNC_CAN_EDIT_SITE_SETTINGS))
            {
                return getConfigNode().getSafeStringSet("SiteAdmins").contains(roleName_p) ? ROLE_ACCESS_RIGHT_ALLOWED : ROLE_ACCESS_RIGHT_NOT_ALLOWED;
            }
        }

        // default returns allowed, override this function to determine allowed status via role
        return (ROLE_ACCESS_RIGHT_ALLOWED);
    }

    /**
     * get all resources the current user has access to for a given category
     * 
     * @param category_p int category for given user
     * 
     * @return Collection of String (resource IDs)
     */
    public Collection getAllowedResources(int category_p) throws Exception
    {
        // user have access to all resources. So delegate to the resource enumeration
        // and return all resources
        return (getResources(category_p));
    }

    //  === Master role support.    
    /** get a list of roles that can be selected for the current user
     * 
     * @return Collection of String, or null if no roles can be selected
     * */
    public Collection getMasterRoles() throws Exception
    {
        return null;
    }

    /** get a displayname for the given role
     * 
     * @param locale_p Locale to use
     * @param strRole_p String
     * @return String displayname for given role
     * */
    public String getMasterRoleDisplayName(java.util.Locale locale_p, String strRole_p) throws Exception
    {
        return strRole_p;
    }

    /** get the role that is currently selected for the user
     * 
     *  @return String role, or null if no roles can be selected
     * */
    public String getMasterRole() throws Exception
    {

        return null;
    }

    /** set the role that is currently selected for the user
     * 
     * @param strRole_p String
     * @return boolean true = role changed
     * */
    public boolean setMasterRole(String strRole_p) throws Exception
    {
        return false;
    }

    /** check if selectable roles are supported by rolemanager for the current user
     * 
     * @return boolean true = given user has roles
     * */
    public boolean hasMasterRoles()
    {
        return false;
    }

    public void setAccessRights(String roleName_p, int category_p, String resourceId_p, int accessRights_p) throws Exception
    {
        throw new com.wewebu.ow.server.exceptions.OwNotSupportedException(getContext().localize("roleimpl.simplerole.OwSimpleRoleManager.accessrightschangenotsupported", "Changing access rights is not supported by SimpleRoleManager."));
    }

    public boolean canExplicitDeny()
    {
        return false;
    }

    public boolean isGlobalRoleModificationAllowed()
    {
        return false;
    }

    private Map m_precalculatedSimpleAccessMasks;

    protected Map getPrecalculatedSimpleAccessMasks()
    {
        if (m_precalculatedSimpleAccessMasks == null)
        {
            m_precalculatedSimpleAccessMasks = new HashMap();
            Collection categories = getCategories();
            Iterator itCategories = categories.iterator();
            while (itCategories.hasNext())
            {
                int fullAccessMask = 0;
                int category = ((Integer) itCategories.next()).intValue();
                Map accessFlags = getAccessMaskDescriptions(category);
                Iterator itFlags = accessFlags.keySet().iterator();
                while (itFlags.hasNext())
                {
                    int flag = ((Integer) itFlags.next()).intValue();
                    fullAccessMask |= flag;
                }
                m_precalculatedSimpleAccessMasks.put(Integer.valueOf(category), Integer.valueOf(fullAccessMask));
            }
        }
        return (m_precalculatedSimpleAccessMasks);
    }

    public int getAccessMask(String roleName_p, int category_p, String resourceId_p)
    {
        Integer fullAccessMask = (Integer) getPrecalculatedSimpleAccessMasks().get(Integer.valueOf(category_p));
        return (fullAccessMask == null) ? 0 : fullAccessMask.intValue();
    }

    public void setAccessMask(String roleName_p, int category_p, String resourceId_p, int accessMask_p) throws Exception
    {
        throw new com.wewebu.ow.server.exceptions.OwNotSupportedException(getContext().localize("roleimpl.simplerole.OwSimpleRoleManager.accessrightschangenotsupported", "Changing access rights is not supported by SimpleRoleManager."));
    }

    public boolean canPersistAccessMask()
    {
        // the simple role manager can not persist the access mask
        return false;
    }

    @Override
    protected Collection<String> getDynamicResources(int category_p)
    {
        return Collections.emptyList();
    }

    public void replaceResource(String roleName_p, int category_p, String oldResourceId_p, String newResourceId_p) throws Exception
    {
        //void
    }

    @Override
    protected String dynamicResourceNameFromId(Locale locale_p, String resourceId_p, int category_p)
    {
        return resourceId_p;
    }
}