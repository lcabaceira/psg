package com.wewebu.ow.server.util.ldap;

import java.util.Collection;
import java.util.Properties;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Schema base LDAP strategy interface.
 * Defines {@link OwLdapConnector} operations that are LDAP-schema dependent.  
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
 *@since 3.1.0.0
 */
public interface OwLdapSchemaInterpreter
{
    /**
     * Returns the full distinguished name for the login user to use for authentication later. 
     * 
     * @param user_p    representing the login name for the user being searched
     * 
     * @return String representing the full distinguished name for the login passed to the method
     * 
     * @throws OwAuthenticationException the user that wants to login is not available in the LDAP
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     */
    String findDistinguishedNameAnonymously(String user_p, OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException;

    /**
     * Reads all groups from the LDAP (e.g. CN=Builtin)
     *
     * @return List of the group names
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     * @throws OwInvalidOperationException if the operation could no be performed due to errors during LDAP access  
     */
    Collection<String> getAllShortGroupNames(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException;

    /**
     * Returns the group names of the user.
     * 
     *
     * @return List of the group names
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  if the operation could no be performed due to errors during LDAP access
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)     
     */
    Collection<String> getShortGroupNames(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException;

    /**
     * Get the user display name, or a <code>null</code> value.
     * @return the user short name, or a <code>null</code> value.
     * @throws OwAuthenticationException thrown when the user is not authenticated.
     * @throws OwInvalidOperationException thrown when the "displayName" attribute cannot be resolved.
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     */
    String getUserDisplayName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException;

    /**
     * Get the user short name, or a <code>null</code> value.
     * @return the user short name, or a <code>null</code> value.
     * @throws OwAuthenticationException thrown when the user is not authenticated.
     * @throws OwInvalidOperationException thrown when the "name" attribute cannot be resolved.
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     */
    String getUserShortName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException;

    /**
     * Gets the user's long name
     * 
     * @return a {@link String}
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     */
    String getUserLongName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException;

    /**
     * Returns a unique ID of the user
     * 
     * @return a {@link String}
     *  
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     */
    String getUserId(OwLdapConnection connection_p) throws OwAuthenticationException, OwInvalidOperationException, OwConfigurationException;

    /**
     * Properties initialization method used to make schema specific initializations of the directory properties
     *   
     * @param ldapProperties_p user based properties 
     * @param adminLdapProperties_p admin user based properties
     * @throws OwConfigurationException
     */
    void init(Properties ldapProperties_p, Properties adminLdapProperties_p) throws OwConfigurationException;

    /**
     *  Finds a user by its Distinguished Name.
     *  
     * @param strID_p
     * @param connection
     * @return user with the given DN.
     * @throws OwException Throws OwObjectNotFoundException if no user was found.
     */
    OwUserInfo findUserByDNAnonymously(String strID_p, OwLdapConnection connection) throws OwException;

    /**
     *  Finds a user by its ID.
     *  
     * @param strID_p
     * @param connection
     * @return user with the given DN.
     * @throws OwException Throws OwObjectNotFoundException if no user was found.
     */
    OwUserInfo findUserByIdAnonymously(String strID_p, OwLdapConnection connection) throws OwException;

    /**
     * @param connection 
     * @param pattern
     * @return A list of user names.
     * @throws OwException 
     */
    OwObjectCollection findUserMatching(OwLdapConnection connection, String pattern) throws OwException;

    /**
     * Search for groups that match the pattern.
     * 
     * @param connection
     * @param pattern
     * @return a collection of {@link OwUserInfo} representing groups that match the pattern.
     * @throws OwException
     * @since 4.1.1.0
     */
    OwObjectCollection findGroupsMatching(OwLdapConnection connection, String pattern) throws OwException;

    /**
     * @param dname User DN.
     * @param connection
     * @return the display name of the given user.
     * @throws OwException 
     */
    String getUserDisplayName(String dname, OwLdapConnection connection) throws OwException;

    /**
     * @param dname
     * @param connection
     * @return The login associated with this user.
     * @throws OwException
     * @since 4.2.0.0 
     */
    String getUserLongin(String dname, OwLdapConnection connection) throws OwException;

    /**
     * @param dname
     * @param connection
     * @return the value of the LDAP attribute that is configured to be used as OWD ID.
     * @throws OwException 
     */
    String getRecordId(String dname, OwLdapConnection connection) throws OwException;

    OwUserInfo findUserByLoginAnonymously(String strID_p, OwLdapConnection connection) throws OwException;
}
