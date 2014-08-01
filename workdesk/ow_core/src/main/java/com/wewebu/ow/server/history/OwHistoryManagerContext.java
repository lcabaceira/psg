package com.wewebu.ow.server.history;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwRepositoryContext;

/**
 *<p>
 * Interface for the history context.<br/>
 * The context keeps basic configuration, localization and 
 * environment information and is independent to the web context.
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
public interface OwHistoryManagerContext extends OwRepositoryContext
{
    /** get the configuration information */
    public abstract OwBaseConfiguration getBaseConfiguration();

    /** get the current user
     * 
     * @return OwUserInfo
     * @throws Exception
     */
    public abstract OwBaseUserInfo getCurrentUser() throws Exception;
}