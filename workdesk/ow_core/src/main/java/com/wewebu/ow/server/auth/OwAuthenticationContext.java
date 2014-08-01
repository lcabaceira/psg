package com.wewebu.ow.server.auth;

/**
 *<p>
 * Authentication holder.
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
public interface OwAuthenticationContext
{
    void setAuthentication(OwAuthentication authentication_p);

    OwAuthentication getAuthentication();
}
