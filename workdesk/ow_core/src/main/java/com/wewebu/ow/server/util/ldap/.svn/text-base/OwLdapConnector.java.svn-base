package com.wewebu.ow.server.util.ldap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Utility class for connecting to an LDAP Server.
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
public class OwLdapConnector
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwLdapConnector.class);

    public static final String USERS_DIRECTORY_PROPERTY = "UsersDirectory";

    /** Schema dependent strategy operations interface*/
    private OwLdapSchemaInterpreter m_schemaInterpreter = null;

    /** the LDAP properties used to configure the directory context*/
    private Properties m_ldapProperties;

    /** the LDAP properties used to configure the directory context - for LDAP administrator user*/
    private Properties m_ldapAdminProperties;

    /** recursion level of getting the groups*/
    private int m_recursionLevel = 0;

    /** name of the configuration element changing the LDAP attribute storing the schema interpreter class name.
     *  @since 3.1.0.0
     */
    public static final String ELEMENT_SCHEMA_INTERPRETER = "SchemaInterpreter";

    /** Distinguished name property name 
     * @since 3.1.0.0 */
    public static final String USER_DN_PROPERTY = "com.wewebu.ow.user.dn";

    /**
     * Construct the LDAP Connector with given properties
     * 
     * @param props_p properties used to build up the LDAP context
     * @param recursionLevel_p Recursion level for getting the groups of a given user
     */
    public OwLdapConnector(Properties props_p, int recursionLevel_p)
    {
        m_ldapProperties = props_p;
        m_ldapAdminProperties = props_p;
        m_recursionLevel = recursionLevel_p;

        initialize();
    }

    /**
     * Construct LDAP connector from the XML config node of bootstrap.xml
     * 
     * @param configNode_p XML node of bootstrap.xml
     */
    public OwLdapConnector(Node configNode_p)
    {
        // load the properties for LDAP context
        // here will be the logged in user properties for LDAP context
        m_ldapProperties = new Properties();
        //here will be the admin properties defined in the bootstrap.xml for LDAP context
        m_ldapAdminProperties = new Properties();

        for (Node n = configNode_p.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                Node textNode = n.getFirstChild();
                if (null != textNode)
                {
                    String sValue = n.getFirstChild().getNodeValue().trim();
                    String sName = n.getNodeName();

                    // java.naming.security.principal / java.naming.security.credentials - from logged in user, set after calling authenticate 
                    m_ldapProperties.put(sName, sValue);

                    // java.naming.security.principal / java.naming.security.credentials - from bootstrap.xml 
                    m_ldapAdminProperties.put(sName, sValue);
                }
            }
        }

        // get recursion level for getting the groups of a given user
        Node recursionLevelNode = configNode_p.getAttributes().getNamedItem("recursionlevel");
        if (recursionLevelNode != null)
        {
            try
            {
                String recursionLevel = recursionLevelNode.getNodeValue();
                m_recursionLevel = Integer.parseInt(recursionLevel);
            }
            catch (NumberFormatException e)
            {
                LOG.error("The recursionlevel level of the LDAP connector is not valid. The recursion level is set to, level=0.", e);
            }
            catch (DOMException e)
            {
                LOG.error("Exception reading the recursionlevel level of the LDAP connector from the bootstrap.xml.", e);
            }
        }

        initialize();
    }

    /**
     *Initialize the connector and its properties.
     *@see OwLdapSchemaInterpreter#init(Properties, Properties)
     */
    private void initialize()
    {
        String interpreterClassName = getProperties(true).getProperty(ELEMENT_SCHEMA_INTERPRETER, null);
        if (interpreterClassName != null)
        {
            try
            {
                Class<? extends OwLdapSchemaInterpreter> interpreterClass = (Class<? extends OwLdapSchemaInterpreter>) Class.forName(interpreterClassName);
                m_schemaInterpreter = interpreterClass.newInstance();
            }
            catch (Exception e)
            {
                LOG.error("Invalid LDAP interpreter class " + interpreterClassName + " will use standard AD interpreter!", e);
            }
        }

        if (m_schemaInterpreter == null)
        {
            m_schemaInterpreter = new OwADSchemaInterpreter();
        }

        try
        {
            m_schemaInterpreter.init(getProperties(false), getProperties(true));
        }
        catch (Exception e)
        {
            LOG.error("Failed to initialize properties in " + m_schemaInterpreter, e);
        }
    }

    /**
     * Create a Connection with properties depending on the given parameter.
     * @param adminConnection_p boolean flag to initialize with specific properties
     * @return a new {@link OwLdapConnection} based on the administrator properties if adminConnection_p is true or the
     *         users properties if adminConnection_p is false
     * @since 3.1.0.0
     */
    public OwLdapConnection createConnection(boolean adminConnection_p)
    {
        return new OwLdapConnection(getProperties(adminConnection_p), getRecursionLevel());
    }

    /**
     * Returns the properties used to build up the LDAP context of the logged in user
     * @return {@link Properties}
     */
    public Properties getProperties()
    {
        return getProperties(false);
    }

    /**
     * Get specific properties configuration of user or admin.
     * @param admin_p boolean
     * @return Properties
     * @since 3.2.0.2
     */
    protected Properties getProperties(boolean admin_p)
    {
        return admin_p ? m_ldapAdminProperties : m_ldapProperties;
    }

    /**
     * Authenticates a user using LDAP.<br />
     * The user name and password are stored for further operations. 
     * 
     * @param userName_p (the samAccount)
     * @param password_p
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     */
    public void authenticate(String userName_p, String password_p) throws OwAuthenticationException, OwConfigurationException
    {
        if (userName_p == null || userName_p.length() == 0 || password_p == null || password_p.length() == 0)
        {
            throw new OwAuthenticationException("OwLdapConnector.authenticate: Authentication Exception, the username or password is empty...");
        }

        InitialDirContext ctx = null;
        try
        {
            prepareForAuthentication(userName_p, password_p);

            // do the authentication 
            ctx = new InitialDirContext(getProperties());

        }
        catch (AuthenticationException e)
        {
            String msg = "Exception creating InitialDirContext for LDAP. Could not authenticate the user, Username = " + userName_p;
            LOG.debug(msg, e);
            throw new OwAuthenticationException(msg, e);
        }
        catch (NamingException e)
        {
            String msg = "Exception creating InitialDirContext for LDAP. Maybe something is wrong with the configuration, Username = " + userName_p;
            LOG.debug(msg, e);
            throw new OwAuthenticationException(msg, e);
        }
        catch (OwAuthenticationException e)
        {
            throw e;
        }
        finally
        {
            if (ctx != null)
            {
                try
                {
                    ctx.close();
                }
                catch (NamingException e)
                {
                }
                ctx = null;
            }
        }

        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwLdapConnector.authenticate: Authentication successfully done, Username = " + userName_p);
        }
    }

    /**
     * Called before authentication method to prepare configuration for
     * @param userName_p
     * @param password_p
     * @throws OwAuthenticationException
     * @throws OwConfigurationException
     * @since 3.2.0.2
     */
    protected void prepareForAuthentication(String userName_p, String password_p) throws OwAuthenticationException, OwConfigurationException
    {
        // first fetch Full User Name to authenticate. This is necessary for AD.
        String distinguishedName = findDistinguishedNameAnonymously(userName_p);
        // before we logged in as clientPrincipal defined in the bootstrap settings
        // in order to get the distinguished name of the user
        // now we overwrite that principal and try to authenticate the this user.
        // m_distinguishedName: e.g. "CN=Valentin vahe. Hemmert,CN=Users,DC=wewebu-virtual,DC=local"
        Properties userProp = getProperties();
        userProp.put("java.naming.security.principal", distinguishedName);
        // add the password
        userProp.put("java.naming.security.credentials", password_p);

        userProp.put(USER_DN_PROPERTY, distinguishedName);
        getProperties(true).put(USER_DN_PROPERTY, distinguishedName);
    }

    /**
     * Returns the group names of the user.
     * 
     * @return List of the group names
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     */
    public Collection getShortGroupNames() throws OwAuthenticationException, OwInvalidOperationException
    {
        return getShortGroupNames(false);
    }

    /**
     * Returns the group names of the user.
     * 
     * @param useAdminAccount_p if true the LDAP Connector uses the administrator account set
     * in the bootstrap.xml to retrieve the user group names of the logged in user, if false use the 
     * user logged in user account to retrieve this informations.
     *
     * @return List of the group names
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     */
    public Collection getShortGroupNames(boolean useAdminAccount_p) throws OwAuthenticationException, OwInvalidOperationException
    {
        OwLdapConnection connection = createConnection(useAdminAccount_p);

        try
        {
            return m_schemaInterpreter.getShortGroupNames(connection);
        }
        catch (OwConfigurationException e)
        {
            //we're wrapping the OwException to preserve the signature 
            //no log entry needed 
            throw new OwInvalidOperationException(e.getMessage(), e);
        }
    }

    /**
     * Reads all groups from the LDAP (e.g. CN=Builtin)
     * 
     * @return Collection
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     */
    public Collection getAllShortGroupNames() throws OwAuthenticationException, OwInvalidOperationException
    {
        return getAllShortGroupNames(false);
    }

    /**
     * Reads all groups from the LDAP (e.g. CN=Builtin)
     *
     * @param useAdminAccount_p if true the LDAP Connector uses the administrator account set
     * in the bootstrap.xml to retrieve the user group names of the logged in user, if false use the 
     * user logged in user account to retrieve this informations.
     * 
     * @return List of the group names
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     */
    public Collection getAllShortGroupNames(boolean useAdminAccount_p) throws OwAuthenticationException, OwInvalidOperationException
    {
        OwLdapConnection connection = createConnection(useAdminAccount_p);

        try
        {
            return m_schemaInterpreter.getAllShortGroupNames(connection);
        }
        catch (OwConfigurationException e)
        {
            //we're wrapping the OwException to preserve the signature 
            //no log entry needed 
            throw new OwInvalidOperationException(e.getMessage(), e);
        }

    }

    /**
     * Gets the user's long name
     * 
     * @return a {@link String}
     * 
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     */
    public String getUserLongName() throws OwAuthenticationException, OwInvalidOperationException
    {
        OwLdapConnection connection = createConnection(false);
        try
        {
            return m_schemaInterpreter.getUserLongName(connection);
        }
        catch (OwConfigurationException e)
        {
            //we're wrapping the OwException to preserve the signature 
            //no log entry needed 
            throw new OwInvalidOperationException(e.getMessage(), e);
        }
    }

    /**
     * Returns a unique ID of the user
     * 
     * @return a {@link String}
     *  
     * @throws OwAuthenticationException  LDAP authorization Exception 
     * @throws OwInvalidOperationException  invalid LDAP operation
     */
    public String getUserId() throws OwAuthenticationException, OwInvalidOperationException
    {
        OwLdapConnection connection = createConnection(false);
        try
        {
            return m_schemaInterpreter.getUserId(connection);
        }
        catch (OwConfigurationException e)
        {
            //we're wrapping the OwException to preserve the signature 
            //no log entry needed 
            throw new OwInvalidOperationException(e.getMessage(), e);
        }
    }

    /**
     * Returns the full distinguished name for the login user to use for authentication later. 
     * The distinguished name is nice since it contains organizational unit and container information. 
     * This method searches anonymously if allowed otherwise takes the principle given by the properties,
     * to perform an anonymous search, the Active Directory Server admin must set up the
     * Active Directory Server to allow anonymous searches.
     * 
     * @param user_p    representing the login name for the user being searched
     * 
     * @return String representing the full distinguished name for the login passed to the method
     * 
     * @throws OwAuthenticationException the user that wants to login is not available in the LDAP
     * @throws OwConfigurationException possible cause: the LDAP connector is not set up correctly in the bootstrap.xml (Authentication node)
     */
    public String findDistinguishedNameAnonymously(String user_p) throws OwAuthenticationException, OwConfigurationException
    {
        OwLdapConnection connection = createConnection(false);
        return this.m_schemaInterpreter.findDistinguishedNameAnonymously(user_p, connection);
    }

    public OwObjectCollection findUserDNsMatching(String pattern) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        return m_schemaInterpreter.findUserMatching(connection, pattern);
    }

    /**
     * Get the user short name, or a <code>null</code> value.
     * @return the user short name, or a <code>null</code> value.
     * @throws OwAuthenticationException thrown when the user is not authenticated.
     * @throws OwInvalidOperationException thrown when the "name" attribute cannot be resolved.
     * @since 2.5.3.0
     */
    public String getUserShortName() throws OwAuthenticationException, OwInvalidOperationException
    {

        OwLdapConnection connection = createConnection(false);
        try
        {
            return m_schemaInterpreter.getUserShortName(connection);
        }
        catch (OwConfigurationException e)
        {
            //we're wrapping the OwException to preserve the signature 
            //no log entry needed 
            throw new OwInvalidOperationException(e.getMessage(), e);
        }
    }

    /**
     * Get the user display name, or a <code>null</code> value.
     * @return the user short name, or a <code>null</code> value.
     * @throws OwAuthenticationException thrown when the user is not authenticated.
     * @throws OwInvalidOperationException thrown when the "displayName" attribute cannot be resolved.
     * @since 2.5.3.0
     */
    public String getUserDisplayName() throws OwAuthenticationException, OwInvalidOperationException
    {
        OwLdapConnection connection = createConnection(false);
        try
        {
            return m_schemaInterpreter.getUserDisplayName(connection);
        }
        catch (OwConfigurationException e)
        {
            //we're wrapping the OwException to preserve the signature 
            //no log entry needed 
            throw new OwInvalidOperationException(e.getMessage(), e);
        }
    }

    /**
     * toString method: creates a String representation of the object
     * 
     * @return a {@link String} - the String representation
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("OwLdapConnector[");
        buffer.append("\n- m_recursionLevel             = ").append(m_recursionLevel);
        buffer.append("\n- m_ldapProperties = ");
        if (m_ldapProperties != null)
        {
            buffer.append("[");
            Iterator iter = m_ldapProperties.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry entry = (Map.Entry) iter.next();
                buffer.append("\n> " + entry.getKey() + " = ").append(entry.getValue());
            }
            buffer.append("\n]");
        }
        else
        {
            buffer.append("null");
        }
        buffer.append("\n]");
        return buffer.toString();
    }

    /**
     * Finds a user by its Distinguished Name.
     * @param strID_p
     * @return user with the given DN.
     * @throws OwException Throws OwObjectNotFoundException if no user was found.
     */
    public OwUserInfo findUserByDNAnonymously(String strID_p) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        return this.m_schemaInterpreter.findUserByDNAnonymously(strID_p, connection);
    }

    /**
     * Finds a user by its id.
     * @param strID_p
     * @return user with the given DN.
     * @throws OwException Throws OwObjectNotFoundException if no user was found.
     */
    public OwUserInfo findUserByIdAnonymously(String strID_p) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        return this.m_schemaInterpreter.findUserByIdAnonymously(strID_p, connection);
    }

    /**
     * Get the display name for the user with the given DN. The Display Name will be used in OWD.
     * 
     * @param dname The DN of the user.
     * @return The display name.
     */
    public String getUserDisplayName(String dname) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        try
        {
            return m_schemaInterpreter.getUserDisplayName(dname, connection);
        }
        catch (OwConfigurationException e)
        {
            throw new OwInvalidOperationException("Could not find user.", e);
        }
    }

    /**
     * Retrieves the login for the user with the given DN.
     * @param dname
     * @return the login of the user.
     * @throws OwException
     * @since 4.2.0.0 
     */
    public String getUserLogin(String dname) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        try
        {
            return m_schemaInterpreter.getUserLongin(dname, connection);
        }
        catch (OwConfigurationException e)
        {
            throw new OwInvalidOperationException("Could not find user.", e);
        }
    }

    /**
     * Retrieves the value of the attribute that should be used as OWD ID.
     * @param dname The LDAP DN of the record (group, user)
     * @return The ID to be used inside OWD for this record.
     */
    public String getRecordId(String dname) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        try
        {
            return m_schemaInterpreter.getRecordId(dname, connection);
        }
        catch (OwConfigurationException e)
        {
            throw new OwInvalidOperationException("Could not find record with DN: " + dname + ".", e);
        }
    }

    /**
     * Get the defined level of recursion.
     * @return int
     * @since 3.2.0.2
     */
    public int getRecursionLevel()
    {
        return this.m_recursionLevel;
    }

    /**
     * @param strID_p
     * @return user with the given login name or null
     * @throws OwException 
     */
    public OwUserInfo findUserByLoginAnonymously(String strID_p) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        return this.m_schemaInterpreter.findUserByLoginAnonymously(strID_p, connection);
    }

    /**
     * Find groups that match a given pattern.
     * @param namePattern pattern for the group name to search by.
     * @return a collection of {@link OwUserInfo} representing groups that match the pattern.
     * @throws OwException 
     * @since 4.1.1.0
     */
    public OwObjectCollection findGroupsMatching(String namePattern) throws OwException
    {
        OwLdapConnection connection = createConnection(false);
        return this.m_schemaInterpreter.findGroupsMatching(connection, namePattern);
    }
}