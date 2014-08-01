package com.wewebu.ow.server.ecm.eaop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 * A {@link Proxy} {@link InvocationHandler} and factory ( see {@link #newAdvicedProxy(Object, Class, Object...)}) to be used 
 * in advice based contexts.
 * It ensures isolated advice delegate calls to given objects. 
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
public class OwAdvicedProxy implements InvocationHandler
{
    /**
     * 
     * @param object_p
     * @param interface_p
     * @param advices_p
     * @return a {@link Proxy} for the given object that will be called in isolated adviced contexts
     * @throws OwInvalidOperationException
     */
    public static <I> I newAdvicedProxy(Object object_p, Class<I> interface_p, Object... advices_p) throws OwInvalidOperationException
    {
        if (!interface_p.isAssignableFrom(object_p.getClass()))
        {
            throw new OwInvalidOperationException("Invalid proxyed interface " + interface_p + "");
        }

        OwAdvicedProxy handler = new OwAdvicedProxy(object_p, advices_p);
        return (I) Proxy.newProxyInstance(OwAdvicedProxy.class.getClassLoader(), new Class[] { interface_p }, handler);
    }

    private Object[] advices;
    private Object object;

    private OwAdvicedProxy(Object object_p, Object... advices_p)
    {
        super();
        this.advices = advices_p;
        this.object = object_p;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        OwThreadAdviser adviser = OwThreadAdviser.currentAdviser();
        try
        {
            adviser.add(advices);
            return method.invoke(object, args);
        }
        finally
        {
            adviser.remove(advices);
        }
    }

}
