package com.wewebu.ow.server.app;

import java.util.EventListener;

/**
 *<p>
 * Interface to refresh objects and display data.<br/>
 * Mainly used by the function plugins to inform their clients of changed objects, which have to be updated.<br/>
 * See {@link OwUpdateCodes} as well.
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
public interface OwClientRefreshContext extends EventListener
{
    /** call client and cause it to refresh its display data
     * @param iReason_p int reason as defined in OwUpdateCodes
     * @param param_p Object optional parameter representing the refresh, depends on the value of iReason_p, can be null
     */
    public abstract void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception;
}