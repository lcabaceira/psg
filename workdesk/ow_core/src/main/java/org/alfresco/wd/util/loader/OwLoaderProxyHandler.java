package org.alfresco.wd.util.loader;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *<p>
 * OwLoader based proxy handler.
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
 *@since x.x.x.x
 */
public class OwLoaderProxyHandler<T> implements InvocationHandler
{
    private OwLoader<T> loader;
    private SoftReference<T> softInstance;

    public OwLoaderProxyHandler(OwLoader<T> loader)
    {
        this.loader = loader;
        softInstance = new SoftReference<T>(null);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        T instance = softInstance.get();
        if (instance == null)
        {
            instance = loader.load();
            softInstance = new SoftReference<T>(instance);
        }
        return method.invoke(instance, args);
    }

}
