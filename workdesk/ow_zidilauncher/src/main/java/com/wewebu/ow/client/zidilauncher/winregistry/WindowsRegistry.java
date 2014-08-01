package com.wewebu.ow.client.zidilauncher.winregistry;

/**
 *<p>
 * WindowsRegistry.
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
public interface WindowsRegistry
{

    public static final int HKEY_CLASSES_ROOT = 0x80000000;

    public static final int HKEY_CURRENT_USER = 0x80000001;

    public static final int HKEY_LOCAL_MACHINE = 0x80000002;

    /**
     * Reads a value from a registry key.
     * 
     * @param hkey the hieve key
     * @param key the backslash separated path to the key to read the value from
     * @param value the name of the value to read or null to read the default value of the key
     * @return the value or null if either the key or the value do not exist
     * 
     * @throws RegistryAccessException 
     */
    public String read(int hkey, String key, String value) throws RegistryAccessException;

}
