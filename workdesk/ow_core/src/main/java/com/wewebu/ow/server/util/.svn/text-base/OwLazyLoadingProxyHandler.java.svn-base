package com.wewebu.ow.server.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.alfresco.wd.util.loader.OwClassInstanceLoader;
import org.alfresco.wd.util.loader.OwLoader;
import org.alfresco.wd.util.loader.OwLoaderProxyHandler;
import org.alfresco.wd.util.loader.OwSingeltonLoader;

/**
 *<p>
 * Simple utility class to create a proxy based on special {@link OwLoader} implementation.
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
 *@since 4.2.0.0
 */
public class OwLazyLoadingProxyHandler
{

    /**
     * Will create an invocation handler which is loading the specific instance
     * based on the provided loader. 
     * @param loader OwLoader to create delegation object for invocation handler
     * @return InvocationHandler
     */
    public static <T> InvocationHandler createProxyHandler(OwLoader<T> loader)
    {
        return new OwLoaderProxyHandler<T>(loader);
    }

    /**
     * Create an Instance of an interface, loading the specific implementation class by an {@link OwClassInstanceLoader}.
     * <p>Creation of specific instance is based on proxy handling and reflective instantiation of the implementing class
     * using the default constructor.</p>
     * @param instanceType Class implementing interface
     * @param interfaceType Class representing the interface
     * @return T which is a Proxy lazy loading the specific type
     */
    public static <T, O extends T> T createSimpleClassLazyLoadedInstance(Class<O> instanceType, Class<T> interfaceType)
    {
        return createLazyLoadedInstance(new OwClassInstanceLoader<T, O>(instanceType), interfaceType);
    }

    /**
     * Create a specific instance based on provided loader, trying to instantiate the corresponding type only once.
     * @param loader OwLoader specific type loader
     * @param type Class describing wanted interface implementation
     * @return T Proxy loading specific implementation only once (singleton/lazy loading behavior)
     */
    public static <T> T createSingeltonLazyLoadedInstance(OwLoader<T> loader, Class<T> type)
    {
        return createLazyLoadedInstance(new OwSingeltonLoader<T>(loader), type);
    }

    /**
     * Create a Proxy based on provided Loader, and corresponding interfaces.
     * @param loader OwLoader for specific implementation of interfaces
     * @param interfaces Class array of interface which Proxy should support
     * @return Proxy lazy loading the specific implemenation
     */
    @SuppressWarnings("unchecked")
    public static <T> T createLazyLoadedInstance(OwLoader<T> loader, Class<?>... interfaces)
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return (T) Proxy.newProxyInstance(cl, interfaces, createProxyHandler(loader));
    }
}
