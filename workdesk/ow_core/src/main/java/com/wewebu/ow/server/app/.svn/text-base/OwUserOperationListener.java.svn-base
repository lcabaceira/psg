package com.wewebu.ow.server.app;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * The user operation listener. 
 * Classes interested to receive information about user operations (LOGIN/LOGOUT)
 * must implement this interface.
 * The user operation listeners can be registered by the classes implementing {@link OwUserOperationExecutor}. 
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
public interface OwUserOperationListener
{
    /**
     * Method called when login is performed
     * @param event_p - the user operation template 
     */
    void operationPerformed(OwUserOperationEvent event_p) throws OwException;
}
