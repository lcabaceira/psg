package com.wewebu.ow.client.zidilauncher.winregistry;

import java.lang.reflect.Method;
import java.util.prefs.Preferences;

/**
 *<p>
 * PreferencesRegistry.
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
public class PreferencesRegistry implements WindowsRegistry
{

    private Method methodRegOpenKey = null;

    private Method methodRegCloseKey = null;

    private Method methodRegQueryValueEx = null;

    private static final int KEY_READ = 0x20019;

    public static final int REG_SUCCESS = 0;

    public static final int REG_NOTFOUND = 2;

    public static final int REG_ACCESSDENIED = 5;

    private static Preferences preferencesUserRoot = Preferences.userRoot();

    private static Class<? extends Preferences> preferencesUserRootClass = preferencesUserRoot.getClass();

    public PreferencesRegistry() throws RegistryNotAvailableException
    {
        try
        {
            methodRegOpenKey = preferencesUserRootClass.getDeclaredMethod("WindowsRegOpenKey", new Class[] { int.class, byte[].class, int.class });
            methodRegOpenKey.setAccessible(true);
            methodRegCloseKey = preferencesUserRootClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { int.class });
            methodRegCloseKey.setAccessible(true);
            methodRegQueryValueEx = preferencesUserRootClass.getDeclaredMethod("WindowsRegQueryValueEx", new Class[] { int.class, byte[].class });
            methodRegQueryValueEx.setAccessible(true);
        }
        catch (Exception e)
        {
            throw new RegistryNotAvailableException(e);
        }
    }

    public String read(int hkey, String key, String value) throws RegistryAccessException
    {
        if (value == null)
        {
            value = "";
        }
        try
        {
            int[] handles = (int[]) methodRegOpenKey.invoke(preferencesUserRoot, new Object[] { Integer.valueOf(hkey), toCstr(key), Integer.valueOf(KEY_READ) });
            if (handles[1] != REG_SUCCESS)
            {
                return null;
            }
            byte[] valb = (byte[]) methodRegQueryValueEx.invoke(preferencesUserRoot, new Object[] { Integer.valueOf(handles[0]), toCstr(value) });
            methodRegCloseKey.invoke(preferencesUserRoot, new Object[] { Integer.valueOf(handles[0]) });
            return (valb != null ? new String(valb).trim() : null);
        }
        catch (Exception e)
        {
            throw new RegistryAccessException(e);
        }
    }

    private static byte[] toCstr(String str)
    {
        byte[] result = new byte[str.length() + 1];

        for (int i = 0; i < str.length(); i++)
        {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }

}
