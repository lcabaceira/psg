package com.wewebu.ow.server.role;

import java.util.Map;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Interface for integrated application role managers.<br/>
 * Allows multiple applications (egg.Alfresco Workdesk and Mobile Workdesk)  to be 
 * managed separately. 
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
 *@since 4.1.1.0
 */
public interface OwIntegratedApplicationRoleManager extends OwRoleManager
{
    /**
     * Creates role manger instances for given applications.
     * 
     * @param applicationId
     * @return a role manager instance for the given application 
     * @throws OwException
     */
    OwIntegratedApplicationRoleManager createIntegratedRoleManager(String applicationId) throws OwException;

    /**
     * @return the ID of the application that is managed by this role manager  
     */
    String getApplicationId();

    /**
     * @return a map of application IDs mapped to their localized string names that can be managed by 
     *         managers created with {@link #createIntegratedRoleManager(String)} 
     */
    Map<String, OwString> getIntegratedApplicationsNames();

}
