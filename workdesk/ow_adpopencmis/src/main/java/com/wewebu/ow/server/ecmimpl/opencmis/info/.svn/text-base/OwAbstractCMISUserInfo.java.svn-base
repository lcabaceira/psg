package com.wewebu.ow.server.ecmimpl.opencmis.info;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISUserInfo.
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
public abstract class OwAbstractCMISUserInfo implements OwUserInfo
{
    private String name;

    public OwAbstractCMISUserInfo(String name_p)
    {
        this.name = name_p;
    }

    public String getUserEmailAdress() throws OwException
    {
        return "";
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

    public String getUserDisplayName() throws Exception
    {
        return this.name;
    }

    public String getUserShortName() throws Exception
    {
        return this.name;
    }

    @Override
    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwAbstractCMISUserInfo)
        {
            OwAbstractCMISUserInfo uiObj = (OwAbstractCMISUserInfo) obj_p;
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
}
