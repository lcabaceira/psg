package com.wewebu.ow.server.ecmimpl.opencmis.users.info;

import java.util.Collection;
import java.util.Collections;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwGroup;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * GroupInfo retrieved from Alfresco.
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
public class OwCMISAlfrescoGroupInfo extends OwCMISUserInfo
{
    private String displayName;

    public OwCMISAlfrescoGroupInfo(OwGroup group)
    {
        super(group.getId(), null);
        this.displayName = group.getName();
    }

    public OwCMISAlfrescoGroupInfo(String name, String displayName)
    {
        super(name, null);
        this.displayName = displayName;
    }

    public Collection<OwUserInfo> getGroups() throws OwException
    {
        return Collections.emptyList();
    }

    public Collection<String> getRoleNames() throws OwException
    {
        return Collections.emptyList();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwCMISUserInfo#isGroup()
     */
    @Override
    public boolean isGroup()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.info.OwAbstractCMISUserInfo#getUserDisplayName()
     */
    @Override
    public String getUserDisplayName() throws Exception
    {
        if (null != displayName)
        {
            return displayName;
        }
        else
        {
            return super.getUserDisplayName();
        }
    }
}
