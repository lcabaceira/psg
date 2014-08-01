package com.wewebu.ow.server.app;

/**
 *<p>
 * Registry handler for global instance of factories, manager, etc. instances
 * which are single in application/session context.
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
public interface OwGlobalRegistryContext
{
    /**
     * Register some specific object for a defined class/interface.
     * <p>Will not check if the provided object is from that type.</p>
     * @param typeClass Class
     * @param object Object specific to that provided class
     * @see #getRegisteredInterface(Class)
     */
    public void registerInterface(Class<?> typeClass, Object object);

    /**
     * Return the cached object, if any is contained.
     * <p>Can throw a ClassCastException if the registered object 
     * is not an instance of the requested class.</p>
     * @param typeClass Class of the object to return
     * @return the requested object or null if none is cached
     */
    public <T> T getRegisteredInterface(Class<T> typeClass);

    /**
     * Unregister the object for given class/interface.
     * <p>Attention: If any clean or release must be executed
     * first, it must be done before calling the unregister method.</p> 
     * @param typeClass
     * @return Type or null if nothing was register for the given class type
     */
    public <T> T unregisterInterface(Class<T> typeClass);

}
