package com.wewebu.ow.server.auth;

import java.io.Serializable;
import java.security.Principal;

import com.wewebu.ow.server.ecm.OwCredentials;

/**
 *<p>
 * Interface of an Authentication object, which
 * will be provided during an authentication.
 * Implementations represent tokens for authenticated principals. 
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
public interface OwAuthentication extends Principal, Serializable
{

    String getUserName();

    String getPassword();

    OwCredentials getOWDCredentials();

    boolean isAuthenticated();

}
