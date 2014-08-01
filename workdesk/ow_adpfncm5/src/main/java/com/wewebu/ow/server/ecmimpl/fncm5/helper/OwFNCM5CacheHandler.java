package com.wewebu.ow.server.ecmimpl.fncm5.helper;

/**
 *<p>
 * Interface for any CacheHandler.
 * This interface must be implemented by any cache handler,
 * to simplify the resource release.
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
public interface OwFNCM5CacheHandler
{

    /**
     * Clear or release used resources.
     */
    void clearCache();
}
