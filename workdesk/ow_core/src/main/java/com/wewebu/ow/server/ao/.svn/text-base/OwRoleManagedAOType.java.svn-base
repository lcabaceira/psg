package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * Extension of OwAOType for role managed application objects .
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
 *@since 4.0.0.0
 */
public interface OwRoleManagedAOType<T> extends OwAOType<T>
{
    /**
     * 
     * @param object
     * @param roleManager
     * @return true if the given application object is allowed in the context of the given role manager
     */
    boolean isAllowed(T object, OwRoleManager roleManager);
}
