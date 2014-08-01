package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.Map;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Base interface for ECM System specific permissions objects.
 * Supports policies (ACL's) and privilege set's.
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
public interface OwPermissionCollection
{
    // === privilege level support
    /** check if privileges can be retrieved
     * 
     * @return boolean
     */
    public boolean canGetPrivileges();

    /** check if privileges can be set
     * 
     * @return boolean
     */
    public boolean canSetPrivileges();

    /** check if privileges can be denied
     *  i.e. if deny_p parameter in addPrivilegeSet is allowed
     *  
     * @return boolean
     */
    public boolean canDenyPrivileges();

    /** get a collection of privileges that are applicable for the object and
     *  can be used in addPrivilegeSet for the given principal
     *  
     * @param principal_p
     * @return Collection of OwPrivilege
     */
    public Collection getAvailablePrivileges(OwUserInfo principal_p);

    /** get a collection of applicable levels for inheritance depth
     *
     * 
     * @return Map of display strings keyed by Integer depth level's, or null if no depth support is available
     */
    public Map getAvailableInheritanceDepths();

    /** create and add a new privilege set
     *  the new set is applied immediately to the object
     *  
     * @param principal_p
     * @param privileges_p Collection of OwPrivilege
     * @param deny_p
     * @param inheritancedepth_p depth level to which this set is applied for child objects, as defined in OwPrivilegeSet.INHERITANCE_DEPTH_... or by getAvailableInheritanceDepths 
     * @return OwPrivilegeSet
     */
    public OwPrivilegeSet addPrivilegeSet(OwUserInfo principal_p, Collection privileges_p, boolean deny_p, int inheritancedepth_p) throws OwException;

    /** get a collection of all applied privilege sets
     * 
     * @return Collection of OwPrivilegeSet
     */
    public Collection getAppliedPrivilegeSets();

    /** remove the given privilege set
     * 
     * @param privilegeset_p
     */
    public void removePrivilegeSet(OwPrivilegeSet privilegeset_p) throws OwException;

    // === policy level support
    /** check if policies can be retrieved
     * 
     * @return boolean
     */
    public boolean canGetPolicies();

    /** check if policies can be set
     * 
     * @return boolean
     */
    public boolean canSetPolicies();

    /** check if multiple policies can be set, otherwise only one policy can be applied at a time
     *  if true addPolicy will replace the previous policy
     *  
     * @return boolean
     */
    public boolean canAddMultiPolicy();

    /** get a collection of policies that are applicable for the object and
     *  can be used in addPolicy for the given principal
     *  
     * @param principal_p
     * @return Collection of OwPolicy
     */
    public Collection getAvailablePolicies(OwUserInfo principal_p);

    /** add the given policy
     *  for single policy archives addPolicy will replace the previous policy
     * 
     * @param policy_p
     */
    public void addPolicy(OwPolicy policy_p) throws OwException;

    /** get a collection of all applied policies
     * 
     * @return Collection of OwPolicy
     */
    public Collection getAppliedPolicies();

    /** remove the given privilege set
     * 
     * @param policy_p
     */
    public void removePolicy(OwPolicy policy_p) throws OwException;

    /**
     * Method to verify if the permission can be edit.
     * @return OwReason with possible reason descriptions.
     * @since 3.0.0.0
     * @see OwReason
     */
    OwReason canEditPermissions();

}
