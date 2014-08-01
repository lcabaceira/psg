package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Skeleton;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Simple interface for creation of Skeleton objects,
 * which are needed before creating ECM dependent structures.
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
public interface OwFNCM5SkeletonFactory
{
    /**
     * Create a skeleton object which can be used during
     * Add-/Creation-process of object in the ECM system.
     * @param network OwFNCM5Network to be used for creation
     * @param objClass OwObjectClass Type from which the new object will be
     * @return OwFNCM5Skeleton object
     * @throws OwException if creation of skeleton fails (or specific sub type)
     */
    public OwFNCM5Skeleton createSkeleton(OwFNCM5Network network, OwObjectClass objClass) throws OwException;
}
