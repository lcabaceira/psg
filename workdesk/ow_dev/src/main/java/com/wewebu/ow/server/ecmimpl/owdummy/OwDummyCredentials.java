package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.OwAbstractCredentials;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * Implementation for the user Credentials and Session context for the login to the dummy DMS System.<br/>
 * Created after login to the DMS System. Holds Session information and user authentication information.
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
@SuppressWarnings("rawtypes")
public class OwDummyCredentials extends OwAbstractCredentials
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDummyCredentials.class);

    OwLdapConnector m_ldapConn;
    OwDummyUserInfo m_Info;

    /**
     * authenticate against LDAP
     * 
     * @param userName_p
     * @param password_p
     * @param authenticationNode_p 
     * 
     * @throws OwAuthenticationException User login data is not valid
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     * @throws OwInvalidOperationException 
     */
    public OwDummyCredentials(String userName_p, String password_p, Node authenticationNode_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        super(userName_p, password_p);
        m_ldapConn = new OwLdapConnector(authenticationNode_p);

        try
        {
            m_ldapConn.authenticate(userName_p, password_p);
        }
        catch (OwAuthenticationException e1)
        {
            throw new OwAuthenticationException("Your login information is invalid. Please make sure that name and password are correct.", e1);
        }
        catch (OwConfigurationException e)
        {
            String msg = "Exception trying to connect to LDAP, possible cause: Wrong configuration of the bootstrap.xml file (Authentication node).";
            LOG.error(msg, e);
            throw new OwConfigurationException(msg, e);
        }

        m_Info = new OwDummyUserInfo(userName_p);
        //fetch full name
        String fullName = m_ldapConn.getUserLongName();
        m_Info.setStrLongUserName(fullName);
        // fetch userId
        String userId = m_ldapConn.getUserId();
        m_Info.setUserId(userId);
        // fetch roles
        Collection roleNames = m_ldapConn.getShortGroupNames();
        m_Info.setRoleNames(roleNames);
    }

    /**
     * dummy login - do no authentication
     * 
     * @param strUserName_p
     */
    public OwDummyCredentials(String strUserName_p)
    {
        m_Info = new OwDummyUserInfo(strUserName_p);
        m_Info.setStrLongUserName(strUserName_p);
        m_Info.setUserId(strUserName_p);

        List<String> roles = new LinkedList<String>();

        roles.add("#Authenticated-Users");

        if (strUserName_p.startsWith("Admin") || strUserName_p.startsWith("admin"))
        {
            roles.add("Users");
            roles.add("Mitarbeiter");
            roles.add("Administrators");
            roles.add("Application_A");
            roles.add("Versicherungen");
            roles.add("Application_B");
            roles.add("Banken");
            roles.add("Guest");
            roles.add("Gast");
        }

        if (strUserName_p.equalsIgnoreCase("a"))
        {
            roles.add("Users");
            roles.add("Mitarbeiter");
            roles.add("Application_A");
            roles.add("Versicherungen");
        }

        if (strUserName_p.equalsIgnoreCase("b"))
        {
            roles.add("Users");
            roles.add("Mitarbeiter");
            roles.add("Application_B");
            roles.add("Banken");
        }

        if (strUserName_p.equalsIgnoreCase("Guest"))
        {
            roles.add("Guest");
            roles.add("Gast");
        }

        m_Info.setRoleNames(roles);
    }

    /**
     * get user information object for the logged in user
     */
    public OwUserInfo getUserInfo() throws Exception
    {
        return m_Info;
    }

    /** close connection, invalid credentials object */
    public void invalidate() throws Exception
    {
    }

    /** get security token used for SSO between different applications accessing the same ECM system
     * 
     * @param resource_p OwResource of subsystem or null to use default
     * @return String a security token that can be used for authentication or an empty String if no token can be generated
     */
    public String getSecurityToken(OwResource resource_p)
    {
        return "";
    }

    public boolean isContainerBasedAuthenticated() throws OwException
    {
        return false;
    }
}