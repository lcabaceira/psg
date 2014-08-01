package com.wewebu.ow.server.auth;

/**
 *<p>
 * Simple authentication storing context.
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
 *@since 4.0.0.0
 */
public class OwLocalAuthenticationContext implements OwAuthenticationContext
{

    private OwAuthentication authentication;

    public void setAuthentication(OwAuthentication authentication_p)
    {
        this.authentication = authentication_p;

    }

    public OwAuthentication getAuthentication()
    {
        return this.authentication;
    }

}
