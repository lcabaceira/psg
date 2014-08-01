package com.wewebu.ow.server.ecmimpl.fncm5.object;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwResource.OwObjectNamePropertyClass;
import com.wewebu.ow.server.ecm.OwResource.OwObjectPathPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Instance of {@link OwFNCM5Object} that define virtual AWD properties 
 * (such as  {@link OwObjectNamePropertyClass} or {@link OwObjectPathPropertyClass}).
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
public interface OwFNCM5VirtualObject<N> extends OwFNCM5Object<N>
{
    /**
     * 
     * @param propertyClass_p
     * @return an {@link OwProperty} instance for a virtual property of the given 
     *         class defined by this object 
     * @throws OwException
     */
    OwProperty createVirtualProperty(OwFNCM5VirtualPropertyClass propertyClass_p) throws OwException;
}
