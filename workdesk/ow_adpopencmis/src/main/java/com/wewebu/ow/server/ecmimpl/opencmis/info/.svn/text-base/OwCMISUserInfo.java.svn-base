package com.wewebu.ow.server.ecmimpl.opencmis.info;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwRole;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.mandator.OwMandator;

/**
 *<p>
 * Simple UserInfo implementation with mandator handling.
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
public class OwCMISUserInfo extends OwAbstractCMISUserInfo
{
    private static final Logger LOG = OwLog.getLogger(OwCMISUserInfo.class);

    private List<OwUserInfo> defaultGroups;
    private List<String> defaultRoleNames;
    private OwMandator mandator;

    public OwCMISUserInfo(String name_p, OwMandator mandator_p)
    {
        super(name_p);
        this.mandator = mandator_p;
        defaultGroups = Collections.emptyList();
    }

    /**
     * 
     * @return default Alfresco Workdesk groups for this user 
     */
    protected List<OwUserInfo> getDefaulfGroups() throws OwException
    {
        return this.defaultGroups;
    }

    /**
     * Generate a list with the default role names.
     * @return default user roles for this user
     * @throws OwException
     */
    protected synchronized List<String> getDefaultRoleNames() throws OwException
    {
        if (this.defaultRoleNames == null)
        {
            this.defaultRoleNames = new LinkedList<String>();

            List<OwUserInfo> defaulGroups = getDefaulfGroups();
            for (OwUserInfo group : defaulGroups)
            {
                try
                {
                    String roleName = group.getUserName();
                    if (this.mandator != null)
                    {
                        roleName = this.mandator.filterRoleName(roleName);
                    }
                    this.defaultRoleNames.add(roleName);
                }
                catch (Exception e)
                {
                    LOG.error("OwCMISSimpleUserInfo.getDefaultRoleNames(): could not create default role names ", e);
                    throw new OwInvalidOperationException("Could not create default role names !", e);
                }
            }

            String authenticatedRole = OwRole.OW_AUTHENTICATED;
            if (this.mandator != null)
            {
                authenticatedRole = this.mandator.filterRoleName(authenticatedRole);
            }
            this.defaultRoleNames.add(authenticatedRole);
        }

        return this.defaultRoleNames;
    }

    public Collection<OwUserInfo> getGroups() throws OwException
    {
        return getDefaulfGroups();
    }

    public Collection<String> getRoleNames() throws OwException
    {
        return getDefaultRoleNames();
    }

    public boolean isGroup()
    {
        return false;
    }

    /**
     * Get current associated mandator object.
     * @return OwMandator or null
     */
    protected OwMandator getMandator()
    {
        return this.mandator;
    }

}
