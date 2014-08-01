package com.wewebu.ow.server.app;

/**
 *<p>
 * Interface for user operation executor. 
 * Classes that are performing LOGIN/LOGOUT operations and want 
 * to inform different listeners about the executed operations,
 * need to implement this interface. 
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
public interface OwUserOperationExecutor
{
    /**
     * Adds a user operation listener to this executor. 
     * @param listener_p - the listener to be added.
     */
    public void addUserOperationListener(OwUserOperationListener listener_p);

    /**
     * Removes the given user operation listener from this executor.
     * @param listener_p
     */
    public void removeUserOperationListener(OwUserOperationListener listener_p);
}
