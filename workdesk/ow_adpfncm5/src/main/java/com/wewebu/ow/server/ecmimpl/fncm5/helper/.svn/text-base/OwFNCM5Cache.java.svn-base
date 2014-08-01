package com.wewebu.ow.server.ecmimpl.fncm5.helper;

/**
 *<p>
 * Interface for a cache which can be implemented for the network.
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
public interface OwFNCM5Cache
{

    /**
     * Retrieve a cache handler, which should be contained there.
     * Null can be returned if no cache handler was registered for
     * given class.
     * @param clazz Class which is representing the specific handler
     * @return T the handler or null
     * @see #addCacheHandler(Class, OwFNCM5CacheHandler)
     * @see OwFNCM5CacheHandler
     */
    <T> T getCacheHandler(Class<T> clazz);

    /**
     * Register a specific cache handler, this handler will be returned
     * again if {@link #getCacheHandler(Class)} is called with the same class instance.
     * <p>If the provided cache handler is null, the cached object will be released
     * from cache but not cleared (see {@link OwFNCM5CacheHandler#clearCache()}.
     * @param clazz Class extending OwFNCM5CacheHandler
     * @param handler OwFNCM5CacheHandler
     */
    void addCacheHandler(Class<? extends OwFNCM5CacheHandler> clazz, OwFNCM5CacheHandler handler);

    /**
     * Check if a class was registered for that caching (simple key check).
     * <p>Does not verify if the handler is null or not!</p>
     * @param cachClass Class / Interface requested
     * @return boolean true only if such entry exist.
     */
    boolean containsCacheHandler(Class<? extends OwFNCM5CacheHandler> cachClass);

    /**
     * Add any object you want to add to the cache.
     * <p>Do not assume that implementation will dynamically clean the cache</p>
     * @param typeClass Class
     * @param type Object to cache
     */
    void add(Class<?> typeClass, Object type);

    /**
     * Check if a class is contained in the cache (simple key check).
     * <p>No verification is done if the value is null or not!</p>
     * @param cachedClass Class / Interface requested.
     * @return boolean
     */
    boolean contains(Class<?> cachedClass);

    /**
     * Return the cached object, if any is contained.
     * @param typeClass Class of the object to return
     * @return the requested object or null if none is cached
     */
    <T> T getCachedObject(Class<T> typeClass);

    /**
     * Should clear all cached objects.
     * @see #clearHandlerCache()
     * @see #clearObjectCache()
     */
    void clearFullCache();

    /**
     * Clear the part for &quote;any object&quote; cache.
     */
    void clearObjectCache();

    /**
     * Clear the part of the cache which holds the handler.
     * <p>Attention: The Handlers will not be cleared explicitly.</p>
     */
    void clearHandlerCache();
}
