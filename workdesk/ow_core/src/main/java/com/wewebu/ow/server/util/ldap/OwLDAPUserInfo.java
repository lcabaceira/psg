package com.wewebu.ow.server.util.ldap;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * LDAP backed user info.
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
 *@since 4.0.0.0
 */
public class OwLDAPUserInfo implements OwUserInfo
{

    private String userLoginName;
    private String distinguishedName;
    private String displayName, shortName, longName;
    private String email;

    /**
     * @param userLoginName
     * @param distinguishedName
     */
    public OwLDAPUserInfo(String userLoginName, String distinguishedName)
    {
        this.userLoginName = userLoginName;
        this.distinguishedName = distinguishedName;
    }

    public OwLDAPUserInfo(String userLoginName, String distinguishedName, String displayName, String shortName, String longName)
    {
        this(userLoginName, distinguishedName);
        this.displayName = displayName;
        this.shortName = shortName;
        this.longName = longName;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserLongName()
     */
    public String getUserLongName() throws Exception
    {
        return this.longName == null ? this.distinguishedName : this.longName;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserName()
     */
    public String getUserName() throws Exception
    {
        return this.userLoginName;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserDisplayName()
     */
    public String getUserDisplayName() throws Exception
    {
        return this.displayName == null ? this.distinguishedName : this.displayName;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserShortName()
     */
    public String getUserShortName() throws Exception
    {
        return this.shortName;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserEmailAdress()
     */
    public String getUserEmailAdress() throws Exception
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.conf.OwBaseUserInfo#getUserID()
     */
    public String getUserID()
    {
        return this.userLoginName;
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
        return false;
    }

}
