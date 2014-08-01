package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Interface for policies used in OwPermissionCollection.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwPolicy
{
    /** get a name for the policy
     * 
     * @return String
     */
    public String getName();

    /** get a description for the policy
     * 
     * @return String
     */
    public String getDescription();
}
