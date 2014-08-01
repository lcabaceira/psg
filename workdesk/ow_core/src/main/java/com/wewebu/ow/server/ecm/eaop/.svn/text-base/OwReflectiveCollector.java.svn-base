package com.wewebu.ow.server.ecm.eaop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 * A result collector that uses reflection to match an abstract collect method 
 * for each advice method call result. 
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
public abstract class OwReflectiveCollector implements OwJoinPointResultCollector
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwReflectiveCollector.class);

    public boolean canCollect(Method method_p) throws OwException
    {
        try
        {
            findCollectMethod(method_p);
            return true;
        }
        catch (OwException e)
        {
            LOG.debug("Can not collect method " + method_p);
            return false;
        }

    }

    protected abstract Method findCollectMethod(Method method_p) throws OwException;

    public final void collect(Method method_p, Object advice_p, Object[] args_p, Object result_p) throws OwException
    {
        try
        {
            Method collectMethod = findCollectMethod(method_p);
            collectMethod.invoke(this, new Object[] { result_p });
        }
        catch (IllegalArgumentException e)
        {
            throw new OwInvalidOperationException("Could not collect result.", e);
        }
        catch (IllegalAccessException e)
        {
            throw new OwInvalidOperationException("Could not collect result.", e);
        }
        catch (InvocationTargetException e)
        {
            throw new OwInvalidOperationException("Could not collect result.", e);
        }

    }
}
