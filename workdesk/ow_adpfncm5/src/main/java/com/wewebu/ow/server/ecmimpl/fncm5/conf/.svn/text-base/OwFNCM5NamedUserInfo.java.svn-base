package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Named OwFNCM5UserInfo representation. 
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
public abstract class OwFNCM5NamedUserInfo extends OwFNCM5UserInfo
{
    private String name;

    public OwFNCM5NamedUserInfo(String name_p, OwFNCM5UserInfoFactory infoFactory_p)
    {
        super(infoFactory_p);
        this.name = name_p;
    }

    public String getUserID()
    {
        return Integer.toOctalString(hashCode());
    }

    public String getUserLongName() throws OwException
    {
        return this.name;
    }

    public String getUserName() throws OwException
    {
        return this.name;
    }

    public String getUserDisplayName() throws OwException
    {
        return this.name;
    }

    public String getUserShortName() throws OwException
    {
        return this.name;
    }

    @Override
    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwFNCM5NamedUserInfo)
        {
            OwFNCM5NamedUserInfo uiObj = (OwFNCM5NamedUserInfo) obj_p;
            if (this.name != null)
            {
                return this.name.equals(uiObj.name);
            }
            else
            {
                return this.hashCode() == uiObj.hashCode();
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return this.name == null ? 0 : this.name.toLowerCase().hashCode();
    }

    @Override
    public final String toString()
    {
        try
        {
            return getUserName();
        }
        catch (OwException e)
        {

            return "N/A[" + e.getMessage() + "]";
        }
    }

}
