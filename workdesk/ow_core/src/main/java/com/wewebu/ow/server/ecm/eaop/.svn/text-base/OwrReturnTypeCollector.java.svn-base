package com.wewebu.ow.server.ecm.eaop;

import java.lang.reflect.Method;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *An {@link OwReflectiveCollector} that collects results through methods 
 *than are named "collect" and have an argument that matches the type
 *of the advice method return type.
 *    
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 4.0.0.0
 */
public abstract class OwrReturnTypeCollector extends OwReflectiveCollector
{

    private static final String COLLECT = "collect";

    @Override
    protected final Method findCollectMethod(Method method_p) throws OwException
    {
        Class<? extends OwReflectiveCollector> thisClass = this.getClass();
        try
        {
            Method collectMethod = thisClass.getMethod(COLLECT, new Class[] { method_p.getReturnType() });
            if (!collectMethod.getReturnType().equals(Void.TYPE))
            {
                throw new OwInvalidOperationException("Invalid collect method " + collectMethod + ". void return type expexted.");
            }

            return collectMethod;
        }
        catch (SecurityException e)
        {
            throw new OwInvalidOperationException("Security exception while collecting result.", e);
        }
        catch (NoSuchMethodException e)
        {
            throw new OwInvalidOperationException("No reflective collection method for " + method_p.toString() + " in " + thisClass, e);
        }
    }

}
