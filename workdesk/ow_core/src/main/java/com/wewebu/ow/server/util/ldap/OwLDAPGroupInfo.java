package com.wewebu.ow.server.util.ldap;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * LDAP backed group info.
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
 *@since 4.1.1.0
 */
public class OwLDAPGroupInfo implements OwUserInfo
{

    private String id;
    private String name;

    /**
     * @param id
     * @param name
     */
    public OwLDAPGroupInfo(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserLongName()
     */
    public String getUserLongName() throws Exception
    {
        return this.name;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserName()
     */
    public String getUserName() throws Exception
    {
        return this.id;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserDisplayName()
     */
    public String getUserDisplayName() throws Exception
    {
        return this.name;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserShortName()
     */
    public String getUserShortName() throws Exception
    {
        return this.name;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserEmailAdress()
     */
    public String getUserEmailAdress() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserID()
     */
    public String getUserID()
    {
        return this.id;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getRoleNames()
     */
    public Collection getRoleNames() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getGroups()
     */
    public Collection getGroups() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#isGroup()
     */
    public boolean isGroup() throws Exception
    {
        return true;
    }

}
