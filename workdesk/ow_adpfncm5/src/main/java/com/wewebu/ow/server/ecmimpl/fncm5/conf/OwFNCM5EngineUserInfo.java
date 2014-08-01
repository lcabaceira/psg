package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.filenet.api.collection.GroupSet;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.mandator.OwMandator;

/**
 *<p>
 * User information  representation for native com.filenet.api.security.User object.
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
public class OwFNCM5EngineUserInfo extends OwFNCM5GroupedUserInfo
{

    private User user;

    public OwFNCM5EngineUserInfo(User user_p, OwMandator mandator_p, OwFNCM5UserInfoFactory infoFactory_p)
    {
        super(user_p.get_Name(), mandator_p, infoFactory_p);
        this.user = user_p;
    }

    public String getUserLongName() throws OwException
    {
        return user.get_DistinguishedName();
    }

    public String getUserDisplayName() throws OwException
    {
        return user.get_DisplayName();
    }

    public String getUserShortName() throws OwException
    {
        return user.get_ShortName();
    }

    public String getUserEmailAdress() throws OwException
    {
        return user.get_Email();
    }

    public String getUserID()
    {
        return user.get_Id();
    }

    @Override
    protected synchronized List<OwUserInfo> getDefaulfGroups() throws OwException
    {
        if (user.getProperties().find(PropertyNames.MEMBER_OF_GROUPS) == null)
        {
            user.fetchProperties(new String[] { PropertyNames.MEMBER_OF_GROUPS });
        }
        GroupSet set = user.get_MemberOfGroups();
        Iterator<?> it = set.iterator();
        LinkedList<OwUserInfo> groups = new LinkedList<OwUserInfo>();
        while (it.hasNext())
        {
            groups.add(createGroupInfo((Group) it.next()));
        }
        return groups;

    }

    /**
     * (overridable)
     * Factors a group info for a given group name
     * @param group_p
     * @return OwUserInfo
     */
    protected OwUserInfo createGroupInfo(Group group_p)
    {
        OwFNCM5UserInfoFactory factory = getInfoFactory();
        return factory.createGroupInfo(group_p);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null)
        {
            if (obj instanceof OwFNCM5EngineUserInfo)
            {
                return user.get_Id().equals(((OwFNCM5EngineUserInfo) obj).user.get_Id());
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return user.get_Id().hashCode();
    }
}
