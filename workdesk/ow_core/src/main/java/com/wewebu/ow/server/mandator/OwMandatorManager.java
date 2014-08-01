package com.wewebu.ow.server.mandator;

import java.util.Set;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Interface for mandators (multitenancy, multi-tenant) to specify the mandator configuration data.<br/>
 * To be implemented with the specific ECM system.<br/><br/>
 * You get a instance of the mandator manager by calling getContext().getMandatorManager().getUserMandator()
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
public interface OwMandatorManager
{
    /** init the manager, set context
     * @param context_p OwMandatorManagerContext
     * @param configNode_p OwXMLUtil node with configuration information
     * @exception Exception
     */
    public abstract void init(OwMandatorManagerContext context_p, OwXMLUtil configNode_p) throws Exception;

    /** init called AFTER the user has logged in.
    *
    *  NOTE: This function is called only once after login to do special initialization, 
    *        which can only be performed with valid credentials.
    *        
    * @param user_p OwBaseUserInfo
    */
    public abstract void loginInit(OwBaseUserInfo user_p) throws Exception;

    /** get the mandator interface of the current logged in user
     * 
     * @return OwMandator or null if not yet defined
     */
    public abstract OwMandator getUserMandator();

    /** get a set of rolenames that act globally for all mandators
     * 
     * @return a {@link Set}
     */
    public abstract Set getGlobalRoleNames();
}
