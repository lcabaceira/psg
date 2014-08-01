package com.wewebu.ow.server.util.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Interpreter for X.500(96) User Schema for LDAPv3 (see RFC2256) and  
 * for variations of Unix Posix accounts based schemas. 
 * Both schemas use the inetOrgPerson object class for user directory 
 * entries (see RFC 2798).
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
public class OwStandardSchemaInterpreter extends OwBasicLdapSchemaInterpreter
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardSchemaInterpreter.class);

    private static final String UID = "uid";

    public Collection<String> getAllShortGroupNames(OwLdapConnection connection_p) throws OwAuthenticationException, OwInvalidOperationException, OwConfigurationException
    {
        String searchBase = connection_p.getProperty(GROUPS_DIRECTORY_PROPERTY, "ou=groups");
        String groupClass = getGroupClass(connection_p);
        return connection_p.searchAttributeValues(searchBase, new String[][] { { "objectClass", groupClass } }, "cn");
    }

    private String getGroupClass(OwLdapConnection connection_p)
    {
        return connection_p.getProperty(GROUPS_OBJECT_CLASS_PROPERTY, "posixGroup");
    }

    private String getGroupReferebceAttribute(OwLdapConnection connection_p)
    {
        return connection_p.getProperty(GROUP_REFERENCE_ATTRIBUTE_PROPERTY, "memberUid");
    }

    private String getUserGroupReference(OwLdapConnection connection_p)
    {
        return connection_p.getProperty(USER_GROUP_REFERENCE_PROPERTY, UID);
    }

    public Collection<String> getShortGroupNames(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String userReference = getUserGroupReference(connection_p);
        String initialValue = null;
        if ("!".equals(userReference))
        {
            initialValue = connection_p.getUserDistinguishedName();
        }
        else
        {
            initialValue = connection_p.getUserAttributeValue(userReference, true);
        }
        String searchBase = connection_p.getProperty(GROUPS_DIRECTORY_PROPERTY, "ou=groups");
        String groupClass = getGroupClass(connection_p);
        String referenceAttribute = getGroupReferebceAttribute(connection_p);

        InitialDirContext ctx = null;

        try
        {
            ctx = connection_p.createInitialDirContext();

            return searchCompoundGroups(ctx, searchBase, groupClass, referenceAttribute, initialValue, "cn");
        }
        catch (NamingException e)
        {
            String msg = "Error searching for compound groups of " + initialValue;
            LOG.error(msg, e);
            throw new OwInvalidOperationException(msg, e);
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

    }

    protected String getDisplayNameAttName(OwLdapConnection connection_p)
    {
        String attDisplayName = connection_p.getProperty(USER_DISPLAY_NAME_PROPERTY, "displayName");
        return attDisplayName;
    }

    public String getUserShortName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String shortNameAttribute = connection_p.getProperty(NAME_SHORTNAME_PROPERTY, "givenName");
        return connection_p.getUserAttributeValue(shortNameAttribute, true);
    }

    public String getUserLongName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String longNameAttribute = connection_p.getProperty(NAME_LONGNAME_PROPERTY, "displayName");
        return connection_p.getUserAttributeValue(longNameAttribute, true);
    }

    public String getUserId(OwLdapConnection connection_p) throws OwAuthenticationException, OwInvalidOperationException, OwConfigurationException
    {
        return connection_p.getUserAttributeValue(getObjectIdAttribute(connection_p), true);
    }

    private Attributes createMatchAttributes(String groupClass_p, String referenceAttribute_p, String value_p)
    {
        Attributes matchAttrs = new BasicAttributes(true);
        matchAttrs.put(new BasicAttribute("objectClass", groupClass_p));
        matchAttrs.put(new BasicAttribute(referenceAttribute_p, value_p));
        return matchAttrs;
    }

    /**
     * Searches for group directory entries that contain references to a given user identifying initial value.
     * Considers compound group schemas as groupOfNames (see RFC2256)    
     * @param context_p bound initial context to be used
     * @param searchBase_p search base DN
     * @param groupClass_p group entry class name
     * @param referenceAttribute_p group class reference attribute (will contain references to contained users and groups)
     * @param initialValue_p initial user identifying value (an user DN for X500 or uid for Unix Posix accounts) 
     * @param groupNameAttribute_p name of the group entry attribute to use for populating the returned list  
     * @return list of group identifiers : values of groupNameAttribute_p for each group entry found 
     * @throws NamingException
     */
    private List<String> searchCompoundGroups(DirContext context_p, String searchBase_p, String groupClass_p, String referenceAttribute_p, String initialValue_p, String groupNameAttribute_p) throws NamingException
    {
        List<String> attributeValueList = new ArrayList<String>();
        Stack<Attributes> matchStack = new Stack<Attributes>();
        Attributes initialAttributes = createMatchAttributes(groupClass_p, referenceAttribute_p, initialValue_p);
        matchStack.push(initialAttributes);
        while (!matchStack.isEmpty())
        {
            Attributes matchAttributes = matchStack.pop();
            NamingEnumeration<SearchResult> answer = context_p.search(searchBase_p, matchAttributes, new String[] { groupNameAttribute_p });
            String answerAttribute = groupNameAttribute_p + ":";
            int answerAttributeLen = answerAttribute.length();
            while (answer.hasMoreElements())
            {
                SearchResult sr = answer.nextElement();
                String groupDn = sr.getNameInNamespace();
                Attributes compoundGroupMatch = createMatchAttributes(groupClass_p, referenceAttribute_p, groupDn);
                matchStack.push(compoundGroupMatch);

                Attributes dnAttrs = sr.getAttributes();

                if (dnAttrs != null)
                {
                    NamingEnumeration<? extends Attribute> attrEnum = dnAttrs.getAll();
                    while (attrEnum.hasMore())
                    {
                        BasicAttribute ba = (BasicAttribute) attrEnum.next();
                        String baString = ba.toString();
                        baString = baString.substring(answerAttributeLen);
                        attributeValueList.add(baString.trim());
                    }
                }
            }
        }

        return attributeValueList;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#findUserDNsMatching(com.wewebu.ow.server.util.ldap.OwLdapConnection, java.lang.String)
     */
    public OwObjectCollection findUserMatching(OwLdapConnection connection, String pattern) throws OwException
    {
        String searchBase = connection.getProperty(OwLdapConnector.USERS_DIRECTORY_PROPERTY, "ou=people,dc=alfresco,dc=local");
        String groupClass = getGroupClass(connection);
        String filter = "(& (cn=" + pattern + ") (!(objectClass=" + groupClass + ")) )"; // http://tools.ietf.org/html/rfc2254
        return connection.searchValuesByFilter(searchBase, filter, "distinguishedName", "cn");
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwObjectCollection findGroupsMatching(OwLdapConnection connection, String pattern) throws OwException
    {
        String groupClass = getGroupClass(connection);
        String searchBase = connection.getProperty(GROUPS_DIRECTORY_PROPERTY, "ou=groups");

        try
        {
            NamingEnumeration<SearchResult> answer = connection.search(searchBase, new String[][] { { "cn", pattern }, { "objectClass", groupClass } }, new String[] { "cn" });

            OwObjectCollection groups = new OwStandardObjectCollection();
            while (answer.hasMoreElements())
            {
                SearchResult sr = answer.nextElement();
                String dn = sr.getNameInNamespace();
                String cn = "";
                Attribute cnAtt = sr.getAttributes().get("cn");
                if (null != cnAtt)
                {
                    cn = (String) cnAtt.get();
                }
                groups.add(new OwLDAPGroupInfo(dn, cn));
            }

            return groups;
        }
        catch (NamingException e)
        {
            throw new OwInvalidOperationException("Could not search by filter.", e);
        }
    }

    @Override
    protected String getObjectIdAttribute(OwLdapConnection connection_p)
    {
        String objectGUIDAttribute = connection_p.getProperty(NAME_OBJECTGUID_PROPERTY, UID);
        return objectGUIDAttribute;
    }

    protected String getUserLoginAttribute(OwLdapConnection connection_p)
    {
        String userLoginNameAttribute = connection_p.getProperty(LOGIN_QUERY_NAME, "uid");
        return userLoginNameAttribute;
    }
}