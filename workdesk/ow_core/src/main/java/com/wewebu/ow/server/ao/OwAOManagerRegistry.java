package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Defines and registers multiple {@link OwAOManager}s of different types.
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
public interface OwAOManagerRegistry
{
    /**
     * 
     * @param type_p
     * @return the registered manager for the given integer type
     * @throws OwInvalidOperationException if the requested manager is not registered with this registry
     *                                     or it could not be retrieved   
     */
    OwAOManager getManager(int type_p) throws OwInvalidOperationException;
}
