package org.alfresco.wd.ext.restlet.auth;

/**
 *<p>
 * 
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
public interface OwRestletAuthenticationHandler
{
    /**
     * Prepare a RESTlet call for current authentication mechanism.<br />
     * Can throw OwRuntimeException in case of possible problems retrieving data.
     * @param restCall org.restlet.resource.ClientResource
     */
    void prepareCall(org.restlet.resource.ClientResource restCall);
}
