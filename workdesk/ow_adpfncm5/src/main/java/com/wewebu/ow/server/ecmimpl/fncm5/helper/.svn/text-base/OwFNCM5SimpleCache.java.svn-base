package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import java.util.concurrent.ConcurrentHashMap;

/**
 *<p>
 * Simple implementation of an OwFNCM5Cache.
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
public class OwFNCM5SimpleCache implements OwFNCM5Cache
{
    private ConcurrentHashMap<Class<? extends OwFNCM5CacheHandler>, OwFNCM5CacheHandler> handlerCache;
    private ConcurrentHashMap<Class<?>, Object> anyCache;

    public OwFNCM5SimpleCache()
    {
        handlerCache = new ConcurrentHashMap<Class<? extends OwFNCM5CacheHandler>, OwFNCM5CacheHandler>();
        anyCache = new ConcurrentHashMap<Class<?>, Object>();
    }

    public <T> T getCacheHandler(Class<T> clazz)
    {
        return (T) handlerCache.get(clazz);
    }

    public synchronized void add(Class<?> typeClass, Object type)
    {
        anyCache.put(typeClass, type);
    }

    public <T> T getCachedObject(Class<T> typeClass)
    {
        return (T) anyCache.get(typeClass);
    }

    public synchronized void addCacheHandler(Class<? extends OwFNCM5CacheHandler> clazz, OwFNCM5CacheHandler handler)
    {
        handlerCache.put(clazz, handler);
    }

    public void clearFullCache()
    {
        clearObjectCache();
        clearHandlerCache();
    }

    public synchronized void clearObjectCache()
    {
        anyCache.clear();
        anyCache = null;
        anyCache = new ConcurrentHashMap<Class<?>, Object>();
    }

    public synchronized void clearHandlerCache()
    {
        handlerCache.clear();
        handlerCache = null;
        handlerCache = new ConcurrentHashMap<Class<? extends OwFNCM5CacheHandler>, OwFNCM5CacheHandler>();
    }

    public boolean containsCacheHandler(Class<? extends OwFNCM5CacheHandler> cachClass)
    {
        return handlerCache.containsKey(cachClass);
    }

    public boolean contains(Class<?> cachedClass)
    {
        return anyCache.containsKey(cachedClass);
    }

}
