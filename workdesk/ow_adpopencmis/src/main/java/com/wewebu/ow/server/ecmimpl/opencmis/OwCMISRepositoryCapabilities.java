package com.wewebu.ow.server.ecmimpl.opencmis;

import org.apache.chemistry.opencmis.commons.data.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.enums.CapabilityAcl;

/**
 *<p>
 * OwCMISRepositoryCapabilities.
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
public class OwCMISRepositoryCapabilities implements OwCMISCapabilities
{
    private RepositoryCapabilities repositoryCapabilities;

    public OwCMISRepositoryCapabilities(RepositoryCapabilities repositoryCapabilities)
    {
        super();
        this.repositoryCapabilities = repositoryCapabilities;
    }

    @Override
    public boolean isCapabilityPermissions()
    {
        CapabilityAcl capabilityAcl = repositoryCapabilities.getAclCapability();
        switch (capabilityAcl)
        {
            case MANAGE:
            case DISCOVER:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isCapabilityManagePermissions()
    {
        CapabilityAcl capabilityAcl = repositoryCapabilities.getAclCapability();
        switch (capabilityAcl)
        {
            case MANAGE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isCapabilityUnfiling()
    {
        return repositoryCapabilities.isUnfilingSupported();
    }

}
