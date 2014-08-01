package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwRole;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.mandator.OwMandator;

/**
 *<p>
 * OwFNCM5NamedUserInfo representation for users that belong to user groups.
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
public abstract class OwFNCM5GroupedUserInfo extends OwFNCM5NamedUserInfo
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5GroupedUserInfo.class);

    private static final List<OwUserInfo> NO_GROUPS = Collections.EMPTY_LIST;

    private Collection<OwUserInfo> groups;
    private Collection<String> roles;

    private OwMandator mandator;

    public OwFNCM5GroupedUserInfo(String name_p, OwMandator mandator_p, OwFNCM5UserInfoFactory infoFactory_p)
    {
        super(name_p, infoFactory_p);
        this.mandator = mandator_p;
    }

    /**
     * 
     * @return default user roles for this user
     * @throws OwException
     */
    protected synchronized List<String> getDefaultRoleNames() throws OwException
    {
        List<String> defaultRoleNames = new LinkedList<String>();

        List<OwUserInfo> defaulGroups = getDefaulfGroups();
        for (OwUserInfo group : defaulGroups)
        {
            try
            {
                String roleName = group.getUserDisplayName();
                if (this.mandator != null)
                {
                    roleName = this.mandator.filterRoleName(roleName);
                }
                defaultRoleNames.add(roleName);
            }
            catch (Exception e)
            {
                LOG.error("OwFNCM5GroupedUserInfo.getDefaultRoleNames(): could not create default role names ", e);
                throw new OwInvalidOperationException("Could not create default role names !", e);
            }
        }

        String authenticatedRole = OwRole.OW_AUTHENTICATED;
        if (this.mandator != null)
        {
            authenticatedRole = this.mandator.filterRoleName(authenticatedRole);
        }
        defaultRoleNames.add(authenticatedRole);

        return defaultRoleNames;
    }

    public Collection<String> getRoleNames() throws OwException
    {
        if (this.roles == null)
        {
            this.roles = new HashSet<String>();
            Collection<String> defaultRoleNames = getDefaultRoleNames();

            if (getMandator() == null)
            {
                this.roles.addAll(defaultRoleNames);
            }
            else
            {
                for (String superRole : defaultRoleNames)
                {
                    this.roles.add(getMandator().filterRoleName(superRole));
                }
            }

            this.roles.addAll(getDefaultRoleNames());
        }

        return this.roles;
    }

    public final Collection<OwUserInfo> getGroups() throws OwException
    {
        if (null == groups)
        {
            groups = new LinkedList();

            List<OwUserInfo> defaultGroups = getDefaulfGroups();

            this.groups.addAll(defaultGroups);

        }

        return groups;
    }

    public final boolean isGroup()
    {
        return false;
    }

    /**
     * 
     * @return default AWD groups for this user 
     */
    protected synchronized List<OwUserInfo> getDefaulfGroups() throws OwException
    {
        return NO_GROUPS;
    }

    protected OwMandator getMandator()
    {
        return this.mandator;
    }
}
