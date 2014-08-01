package com.wewebu.ow.server.ecmimpl.owsimpleadp;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * A User Info.
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
public class OwSimpleUserInfo implements OwUserInfo
{
    // =============================================================================
    //
    //
    // MINIMAL MEMBERS
    //
    //
    // =============================================================================    

    private String m_username;

    // =============================================================================
    //
    //
    // MINIMAL IMPLEMENTATION
    //
    //
    // =============================================================================    

    public OwSimpleUserInfo(String user_p, String password_p)
    {
        m_username = user_p;
    }

    public String getUserID()
    {
        return m_username;
    }

    public String getUserLongName() throws Exception
    {
        return m_username;
    }

    public String getUserName() throws Exception
    {
        return m_username;
    }

    public boolean isGroup() throws Exception
    {
        return false;
    }

    // =============================================================================
    //
    //
    // OPTIONAL IMPLEMENTATION
    //
    //
    // =============================================================================    

    public Collection getGroups() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getRoleNames() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUserEmailAdress() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUserDisplayName() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUserShortName() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

}