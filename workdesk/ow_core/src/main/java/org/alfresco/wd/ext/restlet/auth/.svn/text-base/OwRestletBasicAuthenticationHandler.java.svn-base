package org.alfresco.wd.ext.restlet.auth;

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

/**
 *<p>
 * Simple HTTP-BASIC authentication RESTlet handler.
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
public class OwRestletBasicAuthenticationHandler implements OwRestletAuthenticationHandler
{
    String userName, password;

    public OwRestletBasicAuthenticationHandler(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public void prepareCall(ClientResource restCall)
    {
        restCall.setChallengeResponse(ChallengeScheme.HTTP_BASIC, this.userName, this.password);
    }
}
