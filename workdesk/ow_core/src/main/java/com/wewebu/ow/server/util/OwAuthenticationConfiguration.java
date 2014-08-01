package com.wewebu.ow.server.util;

/**
 *<p>
 * Helper to wrap Authentication Configuration node.
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
 *@since 4.1.0.0
 */
public class OwAuthenticationConfiguration
{
    /**Configuration mode defining authentication against back-end*/
    public static final String NONE = "NONE";
    /**Configuration mode defining authentication additional against a LDAP system*/
    public static final String LDAP = "LDAP";

    /**Configuration &quot;mode&quot; attribute name*/
    public static final String AT_AUTHENTICATION_MODE = "mode";

    private String mode;
    private OwXMLUtil configuration;

    public OwAuthenticationConfiguration(org.w3c.dom.Node confNode_p) throws Exception
    {
        this(new OwStandardXMLUtil(confNode_p));
    }

    public OwAuthenticationConfiguration(OwXMLUtil confUtil_p)
    {
        configuration = confUtil_p;
        setMode(confUtil_p.getSafeStringAttributeValue(AT_AUTHENTICATION_MODE, null));
    }

    /**
     * Returns the Authentication mode.
     * Can return null if not specified in configuration
     * @return String (can return null)
     */
    public String getMode()
    {
        return mode;
    }

    /**
     * Set the mode of authentication configuration.
     * @param mode String name of mode
     */
    public void setMode(String mode)
    {
        this.mode = mode;
    }

    /**
     * Get the helper which represents/hold the configuration for extended authentication.
     * @return OwXMLUtil
     */
    public OwXMLUtil getConfiguration()
    {
        return this.configuration;
    }
}
