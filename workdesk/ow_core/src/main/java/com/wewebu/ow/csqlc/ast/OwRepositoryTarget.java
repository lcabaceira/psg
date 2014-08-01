package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * Targeted repository information encapsulation. 
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
public class OwRepositoryTarget
{
    private String repositoryId;
    private OwMergeType mergeType;

    public OwRepositoryTarget(String repositoryId_p, OwMergeType mergeType_p)
    {
        super();
        this.repositoryId = repositoryId_p;
        this.mergeType = mergeType_p;
    }

    public String getRepositoryId()
    {
        return this.repositoryId;
    }

    public OwMergeType getMergeType()
    {
        return this.mergeType;
    }

    @Override
    public int hashCode()
    {
        return this.repositoryId.hashCode();
    }

    @Override
    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwRepositoryTarget)
        {
            OwRepositoryTarget targetObj = (OwRepositoryTarget) obj_p;
            return this.repositoryId.equals(targetObj.repositoryId) && this.mergeType.equals(targetObj.mergeType);
        }
        else
        {
            return false;
        }

    }
}
