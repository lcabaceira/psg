package com.wewebu.ow.server.ecm;

import java.util.TimeZone;

import com.wewebu.ow.server.app.OwGlobalRegistryContext;
import com.wewebu.ow.server.app.OwUserOperationExecutor;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.util.OwTimeZoneInfo;

/**
 *<p>
 * Interface for the ECM context. <br/>
 * The context keeps basic configuration, localization and environment information and is independent to the web context.
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
public interface OwNetworkContext extends OwRepositoryContext, OwUserOperationExecutor, OwGlobalRegistryContext
{
    /** callback from network adapter when user gets logged in
     */
    public abstract void onLogin(OwBaseUserInfo user_p) throws Exception;

    /** name of the default class used to create simple folders
    *
    * @return String classname/id of default folder class or null if not defined
    */
    public abstract String getDefaultFolderClassName();

    /** get the current user
     * 
     * @return OwUserInfo
     * @throws Exception
     */
    public abstract OwBaseUserInfo getCurrentUser() throws Exception;

    /** resolve the given literal placeholder name to a property / criteria value
     * 
     * @param contextname_p String name of calling context, e.g. searchtemplate name
     * @param placeholdername_p String name of placeholder to retrieve value for
     * @return an {@link Object}
     * @throws Exception
     */
    public Object resolveLiteralPlaceholder(String contextname_p, String placeholdername_p) throws Exception;

    /**
     * Get client's time zone offset information.
     * The time zone information can be used  to set or guess the client's 
     * actual time zone or a client-compatible time zone.  
     * @return the current timezone information .
     * @since 3.1.0.3
     */
    public OwTimeZoneInfo getClientTimeZoneInfo();

    /**
     * Return a time zone which represents the client time zone.
     * If the client time zone could not be found/recognized the
     * method return by default a time zone {@link TimeZone#getDefault()}. 
     * @return the current time zone as indicated by the current client time zone information. 
     * @since 3.1.0.3
     */
    public TimeZone getClientTimeZone();
}