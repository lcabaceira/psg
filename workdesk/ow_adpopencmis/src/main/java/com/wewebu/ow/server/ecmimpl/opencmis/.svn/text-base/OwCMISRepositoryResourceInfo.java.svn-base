package com.wewebu.ow.server.ecmimpl.opencmis;

import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;

/**
 *<p>
 * OwCMISRepositoryResourceInfo.
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
public class OwCMISRepositoryResourceInfo implements OwCMISResourceInfo
{
    private RepositoryInfo repositoryInfo;

    public OwCMISRepositoryResourceInfo(RepositoryInfo repositoryInfo)
    {
        super();
        this.repositoryInfo = repositoryInfo;
    }

    @Override
    public OwCMISRepositoryCapabilities getCapabilities()
    {
        return new OwCMISRepositoryCapabilities(repositoryInfo.getCapabilities());
    }

    @Override
    public String getId()
    {
        return repositoryInfo.getId();
    }

    @Override
    public String getDisplayName()
    {
        return repositoryInfo.getName();
    }
}
