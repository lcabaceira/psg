package com.wewebu.ow.server.app;

/**
 *<p>
 * The factory interface for user operation listener creation. 
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
 * @since 3.1.0.3
 */
public interface OwUserOperationListenerFactory
{
    /**
     * Create an user operation listener.
     * @return the {@link OwUserOperationListener} object.
     * @throws Exception
     */
    public OwUserOperationListener createListener() throws Exception;

}
