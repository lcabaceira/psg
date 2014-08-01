package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.lang.reflect.Constructor;

import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Context dependent {@link OwFNCM5Object} factory.
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
public interface OwFNCM5ObjectFactory
{
    /**
     * A {@link Constructor} analogous factory method 
     * 
     * @param objectClass_p java class of the resulted object 
     * @param parameterTypes_p  the parameter array as in {@link Class#getConstructor(Class...)}
     * @param parameters_p the parameters as in {@link Constructor#newInstance(Object...)}
     * @return the new {@link OwFNCM5Object} instance
     * @throws OwException 
     */
    <N, O extends OwFNCM5Object<N>, R extends OwFNCM5Resource> O create(Class<O> objectClass_p, Class<?>[] parameterTypes_p, Object[] parameters_p) throws OwException;
}
