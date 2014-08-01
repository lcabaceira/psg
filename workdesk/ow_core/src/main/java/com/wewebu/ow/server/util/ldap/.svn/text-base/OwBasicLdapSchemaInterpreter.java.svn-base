package com.wewebu.ow.server.util.ldap;

import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Common base class for LDAP schema interpreter.    
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
public abstract class OwBasicLdapSchemaInterpreter implements OwLdapSchemaInterpreter
{

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwBasicLdapSchemaInterpreter.class);

    protected static final String LOGIN_QUERY_NAME = "loginQueryName";
    protected static final String DISTINGUISHED_NAME = "distinguishedName";
    protected static final String GROUPS_DIRECTORY_PROPERTY = "GroupsDirectory";
    protected static final String LDAP_COUNT_LIMIT_PROPERTY = "LdapCountLimit";

    protected static final String USER_DISPLAY_NAME_PROPERTY = "AttributeNameUserDisplayName";
    protected static final String NAME_MEMBER_OF_PROPERTY = "AttributeNameMemberOf";
    protected static final String NAME_OBJECTGUID_PROPERTY = "AttributeNameObjectGUID";
    protected static final String NAME_SHORTNAME_PROPERTY = "AttributeNameShortName";
    protected static final String NAME_LONGNAME_PROPERTY = "AttributeNameLongName";

    protected static final String GROUPS_OBJECT_CLASS_PROPERTY = "GroupsObjectClass";
    protected static final String GROUP_REFERENCE_ATTRIBUTE_PROPERTY = "GroupReferenceAttribute";
    protected static final String USER_GROUP_REFERENCE_PROPERTY = "UserGroupReference";

    public String findDistinguishedNameAnonymously(String user_p, OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException
    {
        //Java pre 1.5
        final String distinguishedNameAttribute = "distinguishedName:";

        String searchRoot = null;
        NamingEnumeration answer = null;
        String distinguishedName = null;
        InitialDirContext ctx = null;

        try
        {
            ctx = connection_p.createInitialDirContext();
            Properties connectionProperties = connection_p.getProperties();

            // Set the controls for performing a subtree search
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            //TimeLimit: set to 120sec
            ctls.setTimeLimit(120000);

            //Java pre 1.5
            // Search for the distinguishedName attribute only
            String[] attr = { DISTINGUISHED_NAME };
            ctls.setReturningAttributes(attr);

            // Perform the search
            Attributes matchAttrs = new BasicAttributes(true);

            String loginQueryName = (String) connectionProperties.get(LOGIN_QUERY_NAME);

            matchAttrs.put(new BasicAttribute(loginQueryName, user_p));
            searchRoot = (String) connectionProperties.get(OwLdapConnector.USERS_DIRECTORY_PROPERTY);
            String filter = loginQueryName + "=" + user_p;
            answer = ctx.search(searchRoot, filter, ctls);
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult) answer.nextElement();
                /* The following code is only suitable for Java 1.5 */
                try
                {
                    distinguishedName = sr.getNameInNamespace();
                    break;
                }
                catch (UnsupportedOperationException ex)
                {
                    Attributes dnAttrs = sr.getAttributes();
                    if (dnAttrs != null)
                    {
                        NamingEnumeration<?> attrEnum = dnAttrs.getAll();
                        while (attrEnum.hasMore())
                        {
                            BasicAttribute ba = (BasicAttribute) attrEnum.next();
                            String baString = ba.toString();
                            //LOG.debug("dn attribute:"+baString.substring(DN_ATTRIBUTE.length()));
                            distinguishedName = baString.substring(distinguishedNameAttribute.length());
                        }
                    }
                }

            }
        }
        catch (NamingException e)
        {
            String msg = "LDAP Authentication failed. It seems that the \"(UsersDirectory)\" node in owbootstrap.xml is wrong! Maybe the specified domain is wrong = " + searchRoot;
            LOG.error(msg, e);
            throw new OwConfigurationException(msg, e);
        }
        finally
        {
            if (answer != null)
            {
                try
                {
                    //close the NamingEnumeration
                    answer.close();
                }
                catch (NamingException e2)
                {
                    // ignore exception
                }
                answer = null;
            }
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

        if (distinguishedName == null)
        {
            throw new OwAuthenticationException("OwLdapConnector.findDistinguishedNameAnonymously: LDAP Authentication failed. The user you specified could not be found in the LDAP directory, Username = " + user_p);
        }

        return distinguishedName;
    }

    public void init(Properties ldapProperties_p, Properties adminLdapProperties_p) throws OwConfigurationException
    {
        //void
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#findUserByDNAnonymously(java.lang.String, com.wewebu.ow.server.util.ldap.OwLdapConnection)
     */
    public OwUserInfo findUserByDNAnonymously(String strID_p, OwLdapConnection connection_p) throws OwException
    {
        String attrNameToFindBy = DISTINGUISHED_NAME;
        return findUserBy(strID_p, connection_p, attrNameToFindBy);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#findUserByIdAnonymously(java.lang.String, com.wewebu.ow.server.util.ldap.OwLdapConnection)
     */
    public OwUserInfo findUserByIdAnonymously(String strID_p, OwLdapConnection connection_p) throws OwException
    {
        String attrNameToFindBy = getObjectIdAttribute(connection_p);
        return findUserBy(strID_p, connection_p, attrNameToFindBy);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#findUserByLoginAnonymously(java.lang.String, com.wewebu.ow.server.util.ldap.OwLdapConnection)
     */
    @Override
    public OwUserInfo findUserByLoginAnonymously(String strID_p, OwLdapConnection connection_p) throws OwException
    {
        String attrNameToFindBy = getUserLoginAttribute(connection_p);
        return findUserBy(strID_p, connection_p, attrNameToFindBy);
    }

    @SuppressWarnings("rawtypes")
    private OwUserInfo findUserBy(String strID_p, OwLdapConnection connection_p, String attrNameToFindBy) throws OwConfigurationException, OwObjectNotFoundException
    {
        String searchRoot = null;
        NamingEnumeration answer = null;
        InitialDirContext ctx = null;

        try
        {
            ctx = connection_p.createInitialDirContext();
            Properties connectionProperties = connection_p.getProperties();

            // Set the controls for performing a subtree search
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            //TimeLimit: set to 120sec
            ctls.setTimeLimit(120000);

            ctls.setReturningAttributes(null);

            searchRoot = (String) connectionProperties.get(OwLdapConnector.USERS_DIRECTORY_PROPERTY);
            String filter = attrNameToFindBy + "=" + strID_p;
            answer = ctx.search(searchRoot, filter, ctls);

            if (!answer.hasMore())
            {
                throw new OwObjectNotFoundException("Could not find user '" + strID_p + "'");
            }
            SearchResult result = (SearchResult) answer.next();
            OwUserInfo userInfo = createUserInfo(result, connection_p);
            return userInfo;
        }
        catch (NamingException e)
        {
            String msg = "LDAP search failed.";
            LOG.error(msg, e);
            throw new OwObjectNotFoundException(msg, e);
        }
        finally
        {
            if (answer != null)
            {
                try
                {
                    //close the NamingEnumeration
                    answer.close();
                }
                catch (NamingException e2)
                {
                    // ignore exception
                }
                answer = null;
            }
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
    }

    private OwUserInfo createUserInfo(SearchResult result, OwLdapConnection connection_p) throws NamingException
    {
        Properties connectionProperties = connection_p.getProperties();
        String userLoginNameAttribute = getUserLoginAttribute(connection_p);
        String longNameAttribute = (String) connectionProperties.get(NAME_LONGNAME_PROPERTY);
        String shortNameAttribute = (String) connectionProperties.get(NAME_SHORTNAME_PROPERTY);
        String displayNameAttribute = getDisplayNameAttName(connection_p);

        Attribute userLoginNameAttr = result.getAttributes().get(userLoginNameAttribute);

        String userLoginName = (String) userLoginNameAttr.get();
        String longName = (String) getSafeAttributeValue(result, longNameAttribute);
        String shortName = (String) getSafeAttributeValue(result, shortNameAttribute);
        String displayName = (String) getSafeAttributeValue(result, displayNameAttribute);
        String distinguishedName = result.getNameInNamespace();

        return new OwLDAPUserInfo(userLoginName, distinguishedName, displayName, shortName, longName);
    }

    /**
     * Helper to get Attributes from SearchResult.
     * @param result SearchResult from where to request attribute
     * @param attributeName String
     * @return Object or null (if value is null, attribute does not exist, or attributeName is null)
     * @since 4.2.0.0
     */
    private Object getSafeAttributeValue(SearchResult result, String attributeName)
    {
        if (attributeName != null)
        {
            try
            {
                Attribute attr = result.getAttributes().get(attributeName);
                if (attr != null)
                {
                    return attr.get();
                }
                else
                {
                    LOG.warn("OwBasicLdapSchemaInterpreter.getSafeAttributeValue: Attribute not available or set, attribute-name = " + attributeName);
                }
            }
            catch (NamingException e)
            {
            }
        }
        return null;
    }

    public String getUserDisplayName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String attDisplayName = getDisplayNameAttName(connection_p);
        return connection_p.getUserAttributeValue(attDisplayName, true);
    }

    public String getUserDisplayName(String dname, OwLdapConnection connection) throws OwException
    {
        String attDisplayName = getDisplayNameAttName(connection);
        Object attributeValue = connection.getAttributeValue(dname, attDisplayName);
        if (null == attributeValue)
        {
            return null;
        }
        return attributeValue.toString();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#getUserLongin(java.lang.String, com.wewebu.ow.server.util.ldap.OwLdapConnection)
     */
    @Override
    public String getUserLongin(String dname, OwLdapConnection connection) throws OwException
    {
        String attLogin = getUserLoginAttribute(connection);
        Object attributeValue = connection.getAttributeValue(dname, attLogin);
        if (null == attributeValue)
        {
            return null;
        }
        return attributeValue.toString();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#getRecordId(java.lang.String, com.wewebu.ow.server.util.ldap.OwLdapConnection)
     */
    @Override
    public String getRecordId(String dname, OwLdapConnection connection) throws OwException
    {
        String attIdName = getObjectIdAttribute(connection);
        Object attributeValue = connection.getAttributeValue(dname, attIdName);
        if (null == attributeValue)
        {
            return null;
        }
        return attributeValue.toString();
    }

    protected abstract String getUserLoginAttribute(OwLdapConnection connection_p);

    protected abstract String getObjectIdAttribute(OwLdapConnection connection_p);

    protected abstract String getDisplayNameAttName(OwLdapConnection connection_p);
}
