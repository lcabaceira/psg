package com.wewebu.ow.server.ecmimpl.opencmis.info;

import java.util.Collection;
import java.util.HashSet;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * User info for CMIS authenticated user with extra LDAP based
 * data like groups, roles and long/short names.
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
public class OwCMISLDAPUserInfo extends OwCMISUserInfo
{
    private OwLdapConnector ldapConnector;
    private Collection<OwUserInfo> groups;
    private Collection<String> roles;
    private boolean adminGroupInterogation;
    private OwUserInfo userInfo;

    /**
     * Constructor
     * @param userName_p user name 
     * @param ldapConnector_p the LDAP connection to use 
     */
    public OwCMISLDAPUserInfo(String userName_p, OwLdapConnector ldapConnector_p)
    {
        this(userName_p, null, ldapConnector_p);
    }

    /**
     * Constructor
     * @param userName_p user name 
     * @param mandator_p current {@link OwMandator} , <code>null</code> for no mandator  
     * @param ldapConnector_p the LDAP connection to use 
     */
    public OwCMISLDAPUserInfo(String userName_p, OwMandator mandator_p, OwLdapConnector ldapConnector_p)
    {
        this(userName_p, mandator_p, ldapConnector_p, false);
    }

    /**
     * 
     * @param userName_p user name 
     * @param mandator_p current {@link OwMandator}, <code>null</code> for no mandator  
     * @param ldapConnector_p the LDAP connection to use 
     * @param adminGroupInterogation_p <code>true</code> to use the admin credentials when querying the LDAP groups<br>
     *                                 <code>false</code> otherwise
     * @see OwLdapConnector#getShortGroupNames(boolean) 
     */
    public OwCMISLDAPUserInfo(String userName_p, OwMandator mandator_p, OwLdapConnector ldapConnector_p, boolean adminGroupInterogation_p)
    {
        super(userName_p, mandator_p);
        this.ldapConnector = ldapConnector_p;
        this.adminGroupInterogation = adminGroupInterogation_p;
    }

    public synchronized Collection<OwUserInfo> getGroups() throws OwException
    {
        if (this.groups == null)
        {
            Collection<OwUserInfo> superGroups = super.getGroups();
            this.groups = new HashSet<OwUserInfo>(superGroups);

            if (this.ldapConnector != null)
            {
                Collection ldapShortGroupNames = this.ldapConnector.getShortGroupNames(this.adminGroupInterogation);
                for (Object object : ldapShortGroupNames)
                {
                    String groupName = object.toString();
                    OwUserInfo groupInfo = createGroupInfo(groupName);
                    this.groups.add(groupInfo);
                }
            }
            this.groups.addAll(getDefaulfGroups());
        }
        return this.groups;
    }

    @SuppressWarnings("unchecked")
    public synchronized Collection<String> getRoleNames() throws OwException
    {
        if (this.roles == null)
        {
            this.roles = new HashSet<String>();
            Collection<String> superRoles = getDefaultRoleNames();
            Collection ldapShortGroupNames = this.ldapConnector.getShortGroupNames(this.adminGroupInterogation);

            if (getMandator() == null)
            {
                this.roles.addAll(superRoles);
                this.roles.addAll(ldapShortGroupNames);
            }
            else
            {
                for (String superRole : superRoles)
                {
                    this.roles.add(getMandator().filterRoleName(superRole));
                }
                for (Object ldapRole : ldapShortGroupNames)
                {
                    this.roles.add(getMandator().filterRoleName(ldapRole.toString()));
                }
            }

            this.roles.addAll(getDefaultRoleNames());
        }

        return this.roles;
    }

    /**
     * (overridable)
     * Factors a group info for a given group name
     * @param groupName_p
     * @return an {@link OwCMISLDAPGroupInfo} for the given group name
     */
    protected OwCMISLDAPGroupInfo createGroupInfo(String groupName_p)
    {
        return new OwCMISLDAPGroupInfo(groupName_p);
    }

    @Override
    public String getUserLongName() throws OwException
    {
        try
        {
            return getUserInfo().getUserLongName();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Unable to retrieve LongName", e);
        }
    }

    @Override
    public String getUserShortName() throws Exception
    {
        return getUserInfo().getUserShortName();
    }

    @Override
    public String getUserDisplayName() throws Exception
    {
        return getUserInfo().getUserDisplayName();
    }

    @Override
    public String getUserID()
    {
        try
        {
            String userId = getUserInfo().getUserID();
            if (userId != null)
            {
                return userId;
            }
            else
            {
                return super.getUserID();
            }
        }
        catch (OwException e)
        {
            throw new RuntimeException("Could not get userId from LDAP.", e);
        }
    }

    /**
     * Helper method to handle base OwLDAPUserInfo.
     * @return OwUserInfo
     * @throws OwException if unable to fetch userInfo by Login Name {@link #getUserName()}
     * since 4.2.0.0
     */
    protected OwUserInfo getUserInfo() throws OwException
    {
        if (this.userInfo == null)
        {
            this.userInfo = this.ldapConnector.findUserByLoginAnonymously(super.getUserName());
        }
        return this.userInfo;
    }
}