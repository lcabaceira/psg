package com.wewebu.ow.client.upload;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *<p>
 * Utility class for handling configuration properties.
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
 *@since 3.2.0.0
 */
public abstract class OwAbstractCfg
{
    private static final String YES = "yes";
    private static final String TRUE = "true";

    public final boolean getBoolean(String name_p)
    {
        String value = this.getString(name_p, true);
        return YES.equalsIgnoreCase(value) || TRUE.equalsIgnoreCase(value);
    }

    protected final String getString(String name_p, boolean trimmed_p)
    {
        String parameterValue = this.getString(name_p);
        if (null != parameterValue && trimmed_p)
        {
            parameterValue = parameterValue.trim();
        }
        return parameterValue;
    }

    public final URL getURL(String name_p) throws MalformedURLException
    {
        return new URL(getString(name_p));
    }

    public int getInt(String propName_p)
    {
        return Integer.parseInt(this.getString(propName_p));
    }

    public abstract String getString(String name_p);

    /**
     * Is this parameter present and not empty?
     * @param name_p
     * @return true if the parameter was specified by the user.
     */
    public abstract boolean has(String name_p);
}
