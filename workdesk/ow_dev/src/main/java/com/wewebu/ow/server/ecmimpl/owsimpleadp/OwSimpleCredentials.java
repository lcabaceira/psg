package com.wewebu.ow.server.ecmimpl.owsimpleadp;

import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.OwAbstractCredentials;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Credentials of a user.
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
public class OwSimpleCredentials extends OwAbstractCredentials
{
    // =============================================================================
    //
    //
    // MINIMAL MEMBERS
    //
    //
    // =============================================================================    

    /** user info of the authenticated in user */
    private OwSimpleUserInfo m_userinfo;

    // =============================================================================
    //
    //
    // MINIMAL IMPLEMENTATION
    //
    //
    // =============================================================================    

    /** create a credentials object
     * @param user_p 
     * @param password_p 
     */
    public OwSimpleCredentials(String user_p, String password_p)
    {
        super(user_p, password_p);
        m_userinfo = new OwSimpleUserInfo(user_p, password_p);
    }

    public OwUserInfo getUserInfo() throws Exception
    {
        return m_userinfo;
    }

    // =============================================================================
    //
    //
    // OPTIONAL IMPLEMENTATION
    //
    //
    // =============================================================================    
    public String getSecurityToken(OwResource resource_p)
    {
        // TODO Auto-generated method stub
        return "";
    }

    public void invalidate() throws Exception
    {
        // TODO Auto-generated method stub

    }

    public boolean isContainerBasedAuthenticated() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

}