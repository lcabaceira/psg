package com.wewebu.ow.server.ecm;

import java.util.Collection;

/**
 *<p>
 * Interface for privileges used in OwPermissionCollection. <br/><br/>
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
public interface OwPrivilege
{
    /** get a name for the privilege
     * 
     * @return String
     */
    public String getName();

    /** get a description for the privilege
     * 
     * @return String
     */
    public String getDescription();

    /** get the child privileges if any
     * 
     * @param recursive_p
     * @return Collection of OwPrivilege
     */
    public Collection getChilds(boolean recursive_p);

    /** check if this privilege consists of child privileges
     * 
     * @return boolean
     */
    public boolean hasChilds();
}