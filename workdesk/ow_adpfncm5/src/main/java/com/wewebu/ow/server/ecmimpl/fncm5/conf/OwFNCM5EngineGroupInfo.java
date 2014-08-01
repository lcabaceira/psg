package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.filenet.api.security.Group;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * UserInfo representation for native com.filenet.api.security.Group object.
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
public class OwFNCM5EngineGroupInfo extends OwFNCM5UserInfo
{
    private Group group;

    public OwFNCM5EngineGroupInfo(Group group, OwFNCM5UserInfoFactory infoFactory_p)
    {
        super(infoFactory_p);
        this.group = group;
    }

    public String getUserLongName() throws OwException
    {
        return group.get_DistinguishedName();
    }

    public String getUserName() throws OwException
    {
        return group.get_Name();
    }

    public String getUserDisplayName() throws OwException
    {
        return group.get_DisplayName();
    }

    public String getUserShortName() throws OwException
    {
        return group.get_ShortName();
    }

    public String getUserEmailAdress() throws OwException
    {
        return null;
    }

    public String getUserID()
    {
        return group.get_Id();
    }

    public Collection getRoleNames() throws OwException
    {
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    public Collection getGroups() throws OwException
    {
        OwFNCM5UserInfoFactory factory = getInfoFactory();
        LinkedList<OwUserInfo> groupInfos = new LinkedList<OwUserInfo>();
        Iterator<Group> itGroup = group.get_Groups().iterator();
        while (itGroup.hasNext())
        {
            Group aGroup = itGroup.next();
            OwUserInfo aGroupInfo = factory.createGroupInfo(aGroup);
            groupInfos.add(aGroupInfo);
        }
        return groupInfos;
    }

    public boolean isGroup() throws OwException
    {
        return true;
    }

    @Override
    public String toString()
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
