package com.wewebu.ow.server.mandatorimpl.simplemandator;

import java.util.HashSet;
import java.util.Set;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.mandator.OwMandatorManagerContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of a simple mandator managers (multitenancy, multi-tenant).
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
public class OwSimpleMandatorManager implements OwMandatorManager
{
    public void init(OwMandatorManagerContext context_p, OwXMLUtil configNode_p) throws Exception
    {
    }

    /** get the mandator interface of the current logged in user
     * 
     * @return OwMandator or null if not yet defined
     */
    public OwMandator getUserMandator()
    {
        return null;
    }

    /** init called AFTER the user has logged in.
    *
    *  NOTE: This function is called only once after login to do special initialization, 
    *        which can only be performed with valid credentials.
    *        
    * @param user_p OwBaseUserInfo
    */
    public void loginInit(OwBaseUserInfo user_p) throws Exception
    {

    }

    public Set getGlobalRoleNames()
    {
        // return empty set
        return new HashSet();
    }

}