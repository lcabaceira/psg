package com.wewebu.ow.unittest.util;

import java.util.Collection;
import java.util.Vector;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * Test user for adapters and managers <br>
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
public class OwTestUserInfo implements OwUserInfo, OwBaseUserInfo
{
    private String m_strName;

    public OwTestUserInfo(String strName_p)
    {
        m_strName = strName_p;
    }

    public Collection getGroups() throws Exception
    {
        return new Vector();
    }

    public Collection getRoleNames() throws Exception
    {
        return new Vector();
    }

    public String getUserEmailAdress() throws Exception
    {
        return m_strName + "@wewebu.com";
    }

    public String getUserID()
    {
        return m_strName;
    }

    public String getUserLongName() throws Exception
    {
        return m_strName;
    }

    public String getUserName() throws Exception
    {
        return m_strName;
    }

    public boolean isGroup() throws Exception
    {
        return false;
    }

    public String getUserDisplayName() throws Exception
    {

        return null;
    }

    public String getUserShortName() throws Exception
    {

        return null;
    }

}
