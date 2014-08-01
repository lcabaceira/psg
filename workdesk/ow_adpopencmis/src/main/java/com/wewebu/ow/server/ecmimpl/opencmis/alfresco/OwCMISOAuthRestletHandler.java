package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

/**
 *<p>
 * RESTlet OAuth version 2 handling.
 * Based on HTTP-Authorization header, which is proposed by Version 2 of OAuth specification.
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
public class OwCMISOAuthRestletHandler implements OwRestletAuthenticationHandler
{
    private String accessToken;

    public OwCMISOAuthRestletHandler(String accessToken)
    {
        this.accessToken = accessToken;
    }

    @Override
    public void prepareCall(ClientResource restCall)
    {
        /* Problem currently OAuth Version 2 is not supported by Restlet need to implement own handling,
         * providing Access Token through specific Header(s)
         * http://tools.ietf.org/html/draft-ietf-oauth-v2-bearer-20#section-2.1*/

        @SuppressWarnings("unchecked")
        Series<Header> headers = (Series<Header>) restCall.getRequestAttributes().get("org.restlet.http.headers");
        StringBuilder headerValue = new StringBuilder("Bearer ");
        headerValue.append(accessToken);

        headers.set("Authorization", headerValue.toString());

    }

}
