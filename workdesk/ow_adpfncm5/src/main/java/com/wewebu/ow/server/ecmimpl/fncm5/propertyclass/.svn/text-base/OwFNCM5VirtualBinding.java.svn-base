package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5VirtualObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstraction of AWD defined property classes bindings.<br/>
 * Creates instances of virtual properties for {@link OwFNCM5VirtualObject}s.
 * @see OwFNCM5PropertyClass  
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
public interface OwFNCM5VirtualBinding
{
    /**
     * 
     * @param virtualObject_p
     * @return an {@link OwProperty} instance for a virtual property of the given 
     *         class defined by this object 
     * @throws OwException
     */
    OwProperty propertyOf(OwFNCM5VirtualObject<?> virtualObject_p) throws OwException;
}
