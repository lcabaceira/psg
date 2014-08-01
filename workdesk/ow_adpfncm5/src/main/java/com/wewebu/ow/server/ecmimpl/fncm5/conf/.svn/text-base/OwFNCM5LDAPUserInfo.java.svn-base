package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * OwBaseUserInfo representation for LDAP authenticated users.
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
public class OwFNCM5LDAPUserInfo extends OwFNCM5GroupedUserInfo
{

    private static final Logger LOG = OwLog.getLogger(OwFNCM5LDAPUserInfo.class);

    private OwLdapConnector ldapConnector;

    private boolean adminGroupInterogation;

    /**
     * Constructor
     * @param ldapConnector_p the LDAP connection to use 
     * @param mandator_p current {@link OwMandator} , <code>null</code> for no mandator  
     * @throws OwInvalidOperationException 
     * @throws OwAuthenticationException 
     */
    public OwFNCM5LDAPUserInfo(OwLdapConnector ldapConnector_p, OwMandator mandator_p, OwFNCM5UserInfoFactory infoFactory_p) throws OwAuthenticationException, OwInvalidOperationException
    {
        this(ldapConnector_p.getUserShortName(), mandator_p, ldapConnector_p, false, infoFactory_p);
    }

    /**
     * 
     * @param userName_p user name 
     * @param mandator_p current {@link OwMandator}, <code>null</code> for no mandator  
     * @param ldapConnector_p the LDAP connection to use 
     * @param adminGroupInterogation_p <code>true</code> to use the admin credentials when querying the LDAP groups<br>
     *                                 <code>false</code> otherwise
     * @see OwLdapConnector#getShortGroupNames(boolean) 
     */
    public OwFNCM5LDAPUserInfo(String userName_p, OwMandator mandator_p, OwLdapConnector ldapConnector_p, boolean adminGroupInterogation_p, OwFNCM5UserInfoFactory infoFactory_p)
    {
        super(userName_p, mandator_p, infoFactory_p);
        this.ldapConnector = ldapConnector_p;
        this.adminGroupInterogation = adminGroupInterogation_p;
    }

    @Override
    protected synchronized List<OwUserInfo> getDefaulfGroups() throws OwException
    {
        Collection ldapShortGroupNames = this.ldapConnector.getShortGroupNames(this.adminGroupInterogation);

        List<OwUserInfo> groups = new LinkedList<OwUserInfo>();

        for (Object object : ldapShortGroupNames)
        {
            String groupName = object.toString();
            OwUserInfo groupInfo = createGroupInfo(groupName);
            groups.add(groupInfo);
        }

        return groups;
    }

    /**
     * (overridable)
     * Factors a group info for a given group name
     * @param groupName_p
     * @return OwUserInfo
     */
    protected OwUserInfo createGroupInfo(String groupName_p)
    {
        OwFNCM5UserInfoFactory factory = getInfoFactory();
        return factory.createGroupInfo(groupName_p);
    }

    public String getUserEmailAdress() throws OwException
    {
        return "";
    }

    @Override
    public String getUserLongName() throws OwException
    {
        return ldapConnector.getUserLongName();
    }

    @Override
    public String getUserShortName() throws OwException
    {
        return ldapConnector.getUserShortName();
    }

    @Override
    public String getUserDisplayName() throws OwException
    {
        return ldapConnector.getUserDisplayName();
    }

}
