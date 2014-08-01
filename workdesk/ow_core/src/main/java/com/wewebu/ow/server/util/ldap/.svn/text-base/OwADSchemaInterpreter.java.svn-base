package com.wewebu.ow.server.util.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
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
 * Microsoft's Active Directory schema LDAP interpreter.
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
public class OwADSchemaInterpreter extends OwBasicLdapSchemaInterpreter
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwADSchemaInterpreter.class);
    private static final String BINARY_ATTRIBUTES_PROPETY = "java.naming.ldap.attributes.binary";

    private static final String DEFAULT_NAME_OBJECTGUID = "objectGUID";

    public Collection<String> getAllShortGroupNames(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String searchBase = connection_p.getProperty(GROUPS_DIRECTORY_PROPERTY, "CN=Users,DC=wewebu-virtual,DC=local");
        return connection_p.searchAttributeValues(searchBase, new String[][] { { "objectCategory", "group" } }, "cn");
    }

    public Collection<String> getShortGroupNames(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        // if (LOG.isDebugEnabled())
        // {
        //      LOG.debug("OwADSchemaInterpreter.getShortGroupNames: START - Distinguished name = " + m_distinguishedName);
        // }

        //get the context
        InitialDirContext ctx = null;
        List<String> groupList = new ArrayList<String>();
        try
        {
            ctx = connection_p.createInitialDirContext();

            String distinguisehName = connection_p.getUserDistinguishedName();
            String attributeNameMemberOf = connection_p.getProperty(NAME_MEMBER_OF_PROPERTY, "memberof");
            int recursionLevel = connection_p.getRecursionLevel();
            this.getGroups(recursionLevel, distinguisehName, attributeNameMemberOf, groupList, ctx);
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

        //if (LOG.isDebugEnabled())
        //{
        //  LOG.debug("OwADSchemaInterpreter.getShortGroupNames: END - Distinguished name = " + m_distinguishedName);
        //}

        return groupList;
    }

    private void getGroups(int recursionLevel_p, String distinguishedName_p, String attributeNameMemberOf_p, List<String> groupList_p, InitialDirContext ctx_p) throws OwInvalidOperationException, OwAuthenticationException
    {
        try
        {
            Attributes attrs = ctx_p.getAttributes(distinguishedName_p);

            // fetch the groups
            Attribute memberOf = attrs.get(attributeNameMemberOf_p);
            // DirContext attrDef = memberOf.getAttributeDefinition();
            // LOG.debug("attrDef:" + memberOf.toString());

            // if the user or group is not member of a further group we are finished
            if (memberOf == null)
            {
                return;
            }
            NamingEnumeration<?> groups = memberOf.getAll();

            while (groups.hasMore())
            {
                String group = (String) groups.next();

                //do we have to do a recursive call?
                if (recursionLevel_p > 0)
                {
                    getGroups(recursionLevel_p - 1, group, attributeNameMemberOf_p, groupList_p, ctx_p);
                }
                groupList_p.add(extractGroupName(group));
            }
        }
        catch (AuthenticationException e)
        {
            String msg = "Exception creating InitialDirContext for LDAP. Could not authenticate the user, distinguishedName=" + distinguishedName_p;
            LOG.debug(msg, e);
            throw new OwAuthenticationException(msg, e);
        }
        catch (Exception e)
        {
            String msg = "Exception getting Groups: attribute=\"" + attributeNameMemberOf_p + "\", recursionLevel=" + recursionLevel_p + ", distinguishedName=" + distinguishedName_p;
            LOG.error(msg, e);
            throw new OwInvalidOperationException(msg, e);
        }
    }

    /**
     * Extract the Group Name
     * 
     * @param group_p
     * 
     * @return a {@link String}
     */
    private String extractGroupName(String group_p)
    {
        StringTokenizer tokenizer = new StringTokenizer(group_p, ",");
        //first entry is the group name (CN=groupName,...)
        String groupEntry = tokenizer.nextToken();
        //get rid of CN=
        String groupName = groupEntry.substring(3);
        return groupName;
    }

    protected String getDisplayNameAttName(OwLdapConnection connection_p)
    {
        String attDisplayName = connection_p.getProperty(USER_DISPLAY_NAME_PROPERTY, "cn");
        return attDisplayName;
    }

    public String getUserShortName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String shortNameAttribute = connection_p.getProperty(NAME_SHORTNAME_PROPERTY, "samAccountName");
        return connection_p.getUserAttributeValue(shortNameAttribute, true);
    }

    public String getUserLongName(OwLdapConnection connection_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String longNameAttribute = connection_p.getProperty(NAME_LONGNAME_PROPERTY, "name");
        return connection_p.getUserAttributeValue(longNameAttribute, true);

    }

    public String getUserId(OwLdapConnection connection_p) throws OwAuthenticationException, OwInvalidOperationException, OwConfigurationException
    {
        String objectGUIDAttribute = getObjectIdAttribute(connection_p);
        Object guid = connection_p.getUserAttributeValue(objectGUIDAttribute);

        byte[] asBytes = (byte[]) guid;
        String strGUID = convertGUID(asBytes);
        return strGUID;
    }

    /**
     * Converts the GUID byte Array into a String representation
     * 
     * @param inArr_p the byte array
     * @return converted String
     */
    private String convertGUID(byte[] inArr_p)
    {
        //        StringBuffer retVal = new StringBuffer();
        //        String dblByte = "";
        //
        //        for (int i = 0; i < inArr_p.length; i++)
        //        {
        //            dblByte = Integer.toHexString(inArr_p[i] & 0xff);
        //
        //            if (dblByte.length() == 1)
        //            {
        //                dblByte = "0" + dblByte;
        //            }
        //
        //            retVal.append(dblByte);
        //        }
        //
        //        return retVal.toString();
        GUID guid = new GUID(inArr_p);
        return guid.toString();
    }

    private void addBinaryProperty(Properties properties_p, String propertyName_p)
    {
        String binProperties = (String) properties_p.get(BINARY_ATTRIBUTES_PROPETY);
        if (binProperties == null)
        {
            binProperties = "";
        }

        binProperties += " " + propertyName_p;

        properties_p.put(BINARY_ATTRIBUTES_PROPETY, binProperties);
    }

    @Override
    public void init(Properties ldapProperties_p, Properties adminLdapProperties_p) throws OwConfigurationException
    {
        addBinaryProperty(ldapProperties_p, ldapProperties_p.getProperty(NAME_OBJECTGUID_PROPERTY, DEFAULT_NAME_OBJECTGUID));
        addBinaryProperty(adminLdapProperties_p, adminLdapProperties_p.getProperty(NAME_OBJECTGUID_PROPERTY, DEFAULT_NAME_OBJECTGUID));
    }

    protected String getObjectIdAttribute(OwLdapConnection connection_p)
    {
        String objectGUIDAttribute = connection_p.getProperty(NAME_OBJECTGUID_PROPERTY, DEFAULT_NAME_OBJECTGUID);
        return objectGUIDAttribute;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#findUserDNsMatching(java.lang.String)
     */
    public OwObjectCollection findUserMatching(OwLdapConnection connection, String pattern) throws OwException
    {
        String searchBase = connection.getProperty(OwLdapConnector.USERS_DIRECTORY_PROPERTY, "CN=Users,DC=wewebu-virtual,DC=local");
        String filter = "(& (cn=" + pattern + ") (!(groupType=*)) )"; // http://tools.ietf.org/html/rfc2254
        return connection.searchValuesByFilter(searchBase, filter, "distinguishedName", "cn");
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwObjectCollection findGroupsMatching(OwLdapConnection connection, String pattern) throws OwException
    {
        String searchBase = connection.getProperty(GROUPS_DIRECTORY_PROPERTY, "CN=Users,DC=wewebu-virtual,DC=local");

        try
        {
            NamingEnumeration<SearchResult> answer = connection.search(searchBase, new String[][] { { "cn", pattern }, { "objectCategory", "group" } }, new String[] { "cn" });

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

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.util.ldap.OwLdapSchemaInterpreter#getRecordId(java.lang.String, com.wewebu.ow.server.util.ldap.OwLdapConnection)
     */
    @Override
    public String getRecordId(String dname, OwLdapConnection connection) throws OwException
    {
        String objectGUIDAttribute = getObjectIdAttribute(connection);
        Object guid = connection.getAttributeValue(dname, objectGUIDAttribute);

        byte[] asBytes = (byte[]) guid;
        String strGUID = convertGUID(asBytes);
        return strGUID;
    }

    protected String getUserLoginAttribute(OwLdapConnection connection_p)
    {
        String userLoginNameAttribute = connection_p.getProperty(LOGIN_QUERY_NAME, "sAMAccountName");
        return userLoginNameAttribute;
    }
}
