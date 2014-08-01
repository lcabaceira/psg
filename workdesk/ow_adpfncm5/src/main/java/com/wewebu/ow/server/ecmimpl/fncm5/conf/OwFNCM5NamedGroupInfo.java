package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import java.util.Collection;
import java.util.Collections;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Simple group information object.
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
public class OwFNCM5NamedGroupInfo extends OwFNCM5NamedUserInfo
{
    public OwFNCM5NamedGroupInfo(String name_p, OwFNCM5UserInfoFactory infoFactory_p)
    {
        super(name_p, infoFactory_p);
    }

    public final boolean isGroup()
    {
        return true;
    }

    public Collection<OwUserInfo> getGroups() throws OwException
    {
        return Collections.EMPTY_LIST;
    }

    public Collection<String> getRoleNames() throws OwException
    {
        return Collections.EMPTY_LIST;
    }

    public String getUserEmailAdress() throws OwException
    {
        return null;
    }

}
