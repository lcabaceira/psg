package com.wewebu.ow.server.ecm;

import java.util.Collection;

/**
 *<p>
 * Interface for privilege sets used in OwPermissionCollection. <br/><br/>
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
public interface OwPrivilegeSet
{
    // privileges cannot be inherited to child object's, @see #getInheritanceDepth()
    public static final int INHERITANCE_DEPTH_NO_INHERITANCE = 0;

    // is restricted to inheritance by the immediate security child only, @see #getInheritanceDepth()
    public static final int INHERITANCE_DEPTH_ONE_LEVEL = 1;

    // can be inherited by an infinite level of security children, @see #getInheritanceDepth()
    public static final int INHERITANCE_DEPTH_UNLIMITED = -1;

    /** get the principal of this privilege set
     * 
     * @return OwUserInfo
     */
    public OwUserInfo getPrincipal();

    /** check if privileges have been allowed or denied
     * 
     * @return boolean
     */
    public boolean isDenied();

    /** get the privileges of the set
     * 
     * @return Collection of OwPrivilege
     */
    public Collection getPrivileges();

    /** get depth level to which this set is applied for child objects
     * 
     * @return int as defined in OwPrivilegeSet.INHERITANCE_DEPTH_...
     */
    public int getInheritanceDepth();
}