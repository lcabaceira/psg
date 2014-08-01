package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Base interface for unresolved ECM Objects. Used to determine access errors. <br/><br/>
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
public interface OwUnresolvedReference extends OwObjectReference
{
    /** get the causing exception
     * 
     * @return Exception or null if not available
     */
    public abstract Exception getUnresolvedCause();

    /** get the reason for being unresolved
     * 
     * @return String with reason description
     */
    public abstract String getUnresolvedReason();
}