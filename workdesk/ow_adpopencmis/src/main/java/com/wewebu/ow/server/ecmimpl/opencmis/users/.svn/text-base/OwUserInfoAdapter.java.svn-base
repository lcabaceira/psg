package com.wewebu.ow.server.ecmimpl.opencmis.users;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * Adapter from {@link OwUser} to {@link OwUserInfo}.
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
 *@since 4.2.0.0
 */
public class OwUserInfoAdapter implements OwUserInfo
{
    private OwUser user;

    public OwUserInfoAdapter(OwUser user)
    {
        this.user = user;
    }

    @Override
    public boolean isGroup() throws Exception
    {
        return false;
    }

    @Override
    public String getUserShortName() throws Exception
    {
        return this.user.getName();
    }

    @Override
    public String getUserName() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserLongName() throws Exception
    {
        return this.user.getName();
    }

    @Override
    public String getUserID()
    {
        return this.user.getId();
    }

    @Override
    public String getUserEmailAdress() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserDisplayName() throws Exception
    {
        return this.user.getName();
    }

    @Override
    public Collection getRoleNames() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection getGroups() throws Exception
    {
        return this.user.getGroups();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (null == obj)
        {
            return false;
        }
        else if (obj instanceof OwUserInfoAdapter)
        {
            OwUserInfoAdapter other = (OwUserInfoAdapter) obj;
            return other.user.equals(this.user);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.user.hashCode();
    }
}
