package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Set;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.mandator.OwMandatorManagerContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implementation of a dummy Mandator Managers (multitenancy, multi-tenant).
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
public class OwDummyMandatorManager implements OwMandatorManager
{
    private OwXMLUtil m_configNode;

    /** the user mandator */
    private OwDummyMandator m_mandator;

    /** the context */
    private OwMandatorManagerContext m_context;

    public OwXMLUtil getConfigNode()
    {
        return m_configNode;
    }

    public void init(OwMandatorManagerContext context_p, OwXMLUtil configNode_p) throws Exception
    {
        m_configNode = configNode_p;
        m_context = context_p;
    }

    /** get the context
     * 
     * @return an {@link OwMandatorManagerContext} 
     */
    public OwMandatorManagerContext getContext()
    {
        return m_context;
    }

    /** get the mandator interface of the current logged in user
     * 
     * @return OwMandator or null if not yet defined
     */
    public OwMandator getUserMandator()
    {
        return m_mandator;
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
        // determine the mandator
        m_mandator = new OwDummyMandator(this, user_p);
    }

    public Set getGlobalRoleNames()
    {
        return getConfigNode().getSafeStringSet("GlobalRoleNames");
    }
}