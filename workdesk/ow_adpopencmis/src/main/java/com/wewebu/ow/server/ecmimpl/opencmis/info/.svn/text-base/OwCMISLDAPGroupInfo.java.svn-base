package com.wewebu.ow.server.ecmimpl.opencmis.info;

import java.util.Arrays;
import java.util.Collection;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Simple LDAP group information object.
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
public class OwCMISLDAPGroupInfo extends OwCMISUserInfo
{
    private static final Collection<OwUserInfo> EMPTY_INFO_COLLECTION = Arrays.asList(new OwUserInfo[] {});
    private static final Collection<String> EMPTY_STRING_COLLECTION = Arrays.asList(new String[] {});

    public OwCMISLDAPGroupInfo(String name_p)
    {
        super(name_p, null);
    }

    public final boolean isGroup()
    {
        return true;
    }

    public Collection<OwUserInfo> getGroups() throws OwException
    {
        return EMPTY_INFO_COLLECTION;
    }

    public Collection<String> getRoleNames() throws OwException
    {
        return EMPTY_STRING_COLLECTION;
    }

}
