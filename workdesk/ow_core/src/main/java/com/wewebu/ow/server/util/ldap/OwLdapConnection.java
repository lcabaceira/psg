package com.wewebu.ow.server.util.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.SortControl;

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
 * A LDAP connection contains the directory connection {@link Properties} (see {@link InitialDirContext#InitialDirContext(java.util.Hashtable)} 
 * and standard directory operations needed by the Alfresco Workdesk connector and its schema interpreters.
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
public class OwLdapConnection
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwLdapConnection.class);

    private static final int LDAP_TIME_LIMIT = 120000;
    private static final int LDAP_COUNT_LIMIT = 100;

    /** the LDAP properties used to configure the directory context*/
    private Properties ldapProperties;

    private Boolean anonymousLoginAllowed;

    private int recursionLevel;

    private Integer ldapCountLimit;

    /**
     * Constructor
     * @param ldapProperties_p this connection properties
     * @param recursionLevel_p the configured recursion level
     */
    public OwLdapConnection(Properties ldapProperties_p, int recursionLevel_p)
    {
        super();
        this.recursionLevel = recursionLevel_p;
        this.ldapProperties = new Properties();
        this.ldapProperties.putAll(ldapProperties_p);

        this.anonymousLoginAllowed = Boolean.FALSE;
        this.anonymousLoginAllowed = Boolean.valueOf((String) this.ldapProperties.get("anonymousLogin"));

        if (this.anonymousLoginAllowed.booleanValue())
        {
            // set the authentication to anonymous, do not forget to set to initial value back again
            this.ldapProperties.put("java.naming.security.authentication", "none");
        }

        this.ldapCountLimit = this.getProperty(OwBasicLdapSchemaInterpreter.LDAP_COUNT_LIMIT_PROPERTY, LDAP_COUNT_LIMIT);
    }

    /**
     * 
     * @return a new {@link InitialDirContext} based on this connection properties 
     * @throws OwConfigurationException if the context creation fails 
     */
    public InitialLdapContext createInitialDirContext() throws OwConfigurationException
    {
        try
        {
            InitialLdapContext initialLdapContext = new InitialLdapContext(this.ldapProperties, null);
            return initialLdapContext;
        }
        catch (NamingException e)
        {
            String msg = "LDAP Authentication failed.";
            if (anonymousLoginAllowed.booleanValue())
            {
                msg += " Maybe java.naming.security.authentication=none is not allowed (bootstrap.xml: anonymousLogin=true).";
            }
            LOG.error(msg, e);
            throw new OwConfigurationException(msg, e);
        }
    }

    /**
     * 
     * @param propertyName_p
     * @param defaultValue_p
     * @return value of the given property or the given default value if the property is not defined
     */
    public String getProperty(String propertyName_p, String defaultValue_p)
    {
        if (this.ldapProperties.containsKey(propertyName_p))
        {
            return (String) this.ldapProperties.get(propertyName_p);
        }
        else
        {
            LOG.debug("OwLdapConnection.getProperty: Missing LDAP configuration for property " + propertyName_p + ", using default =[" + defaultValue_p + "]");
            return defaultValue_p;
        }
    }

    private Integer getProperty(String propertyName_p, int defaultValue_p)
    {
        String value = null;
        if (this.ldapProperties.containsKey(propertyName_p))
        {
            value = (String) this.ldapProperties.get(propertyName_p);
        }
        else
        {
            LOG.debug("OwLdapConnection.getProperty: Missing LDAP configuration for property " + propertyName_p + ", using default =[" + defaultValue_p + "]");
            return defaultValue_p;
        }

        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            LOG.warn("The value for propety " + propertyName_p + " must be an integer.", e);
            return defaultValue_p;
        }
    }

    /**
     * 
     * @return the configured recursion level. This attribute is used to resolve Groups in Groups relationship.
     *  
     * <br/>Ex. LDAP tree:
     * <ul>
     * <li>workdesk(objectClass: posixGroup )</li>
     * <li>hr(objectClass: posixGroup, <b>memberOf</b>: workdesk)</li>
     * <li>cr(objectClass: posixGroup, <b>memberOf</b>: workdesk)</li>
     * </ul>
     * 
     * <br/>If recursionlevel = 0 then groups hr and cr will not be looked up.
     * <br/>If recursionlevel >= 1 then groups hr and cr will be looked up.
     * 
     * <p>This configuration parameter has nothing to do with LDAP nested records.</p>
     */
    public int getRecursionLevel()
    {
        return this.recursionLevel;
    }

    /**
     * 
     * @return the properties of this connection
     */
    public Properties getProperties()
    {
        Properties propertiesCopy = new Properties();
        propertiesCopy.putAll(this.ldapProperties);

        return propertiesCopy;
    }

    /**
     * 
     * @return the distinguished name of the current authenticated user
     * @throws OwAuthenticationException if no user was authenticated (the {@link OwLdapConnector#USER_DN_PROPERTY} 
     * is not defined in the properties of this connection) 
     */
    public String getUserDistinguishedName() throws OwAuthenticationException
    {
        String dn = (String) this.ldapProperties.get(OwLdapConnector.USER_DN_PROPERTY);
        if (dn == null)
        {
            LOG.debug("OwLdapConnection.getUserDistinguishedName: Unauthenticated LDAP connection. Missing user distinguished name.");
            throw new OwAuthenticationException("OwLdapConnection.getUserDistinguishedName: Unauthenticated LDAP connection.");
        }

        return dn;
    }

    /**
     * 
     * @param attributeName_p
     * @param trimmCommonName_p if <code>true</code> common name prefixed values will be 
     *                          stripped of their common name prefixes (egg. "cn: alice" will be returned as "alice") 
     * @return the string value of the given attribute value as found in the current users directory entry
     * @throws OwAuthenticationException
     * @throws OwConfigurationException
     * @throws OwInvalidOperationException
     */
    public String getUserAttributeValue(String attributeName_p, boolean trimmCommonName_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String distinguishedName = getUserDistinguishedName();
        return getAttributeValue(distinguishedName, attributeName_p, trimmCommonName_p);
    }

    /**
     * 
     * @param attributeName_p
     * @return the Object value of the given attribute value as found in the current users directory entry
     * @throws OwAuthenticationException
     * @throws OwConfigurationException
     * @throws OwInvalidOperationException
     */
    public Object getUserAttributeValue(String attributeName_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        String distinguishedName = getUserDistinguishedName();
        return getAttributeValue(distinguishedName, attributeName_p);
    }

    /**
     * 
     * @param distinguishedName_p 
     * @param attributeName_p
     * @param trimmCommonName_p if <code>true</code> common name prefixed values will be 
     *                          stripped of their common name prefixes (egg. "cn: alice" will be returned as "alice") 
     * @return the string value of the given attribute value as found in the directory entry pointed by the given distinguishedName_p 
     * @throws OwAuthenticationException
     * @throws OwConfigurationException
     * @throws OwInvalidOperationException
     */
    public String getAttributeValue(String distinguishedName_p, String attributeName_p, boolean trimmCommonName_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        Object attributeValue = getAttributeValue(distinguishedName_p, attributeName_p);

        String avString = null;

        if (attributeValue != null)
        {
            avString = attributeValue.toString();
        }

        if (trimmCommonName_p && avString != null && avString.indexOf(attributeName_p + ":") == 0)
        {

            avString = avString.substring((attributeName_p + ":").length());
            avString = avString.trim();
        }

        return avString;
    }

    /**
     * Get the attribute value for given attribute name.
     * @param distinguishedName_p - the attribute name
     * @return - a {@link String} object containing the value for the given attribute, or a <code>null</code> value, 
     * in case the requested attribute is not found in LDAP 
     * @throws OwAuthenticationException thrown when the user is not authenticated.
     * @throws OwInvalidOperationException thrown when the given attribute cannot be resolved.

     */
    public Object getAttributeValue(String distinguishedName_p, String attributeName_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        //get the context
        Attributes attrs;
        InitialDirContext ctx = null;
        try
        {
            ctx = createInitialDirContext();

            if (ctx == null)
            {
                throw new OwAuthenticationException("OwLdapConnector.getUserLongName: The user is not authenticated!");
            }
            attrs = ctx.getAttributes(distinguishedName_p);
        }
        catch (NamingException e)
        {
            throw new OwAuthenticationException("Get UserLongName: The user is not authenticated!");
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
        Object attributeValue = null;
        try
        {
            // fetch the value
            Attribute attr = attrs.get(attributeName_p);
            if (attr != null)
            {
                attributeValue = attr.get();
            }
            else
            {
                LOG.info("OwLdapConnection.getAttributeValue: LDAP attribute " + attributeName_p + " not found.");
            }
        }
        catch (NamingException e)
        {
            String msg = "Exception reading the attribute, Unknown attribute: \"" + attributeName_p + "\" to fetch the name from LDAP...";
            LOG.error(msg, e);
            throw new OwInvalidOperationException(msg, e);
        }

        return attributeValue;
    }

    /**
     * 
     * @param searchBase_p  
     * @param matchAttrs_p array of String pairs of attribute name value that will be used in matching the targeted entries 
     *                     example  : new String[]{{"objectClass","groupOfNames"},{"cn","administrators"}}
     *                     
     * @param attribute_p name of the attribute whose values will be returned
     * @return a collection of String attribute values as found in all entries located in the searchBase_p directory entry 
     *         that match the given match attributes values    
     * @throws OwAuthenticationException
     * @throws OwConfigurationException
     * @throws OwInvalidOperationException
     */
    public Collection<String> searchAttributeValues(String searchBase_p, String[][] matchAttrs_p, String attribute_p) throws OwAuthenticationException, OwConfigurationException, OwInvalidOperationException
    {
        //get the context
        List<String> attributeValues = null;
        InitialDirContext ctx = null;

        try
        {
            ctx = createInitialDirContext();
            attributeValues = searchAttributeValues(ctx, searchBase_p, matchAttrs_p, attribute_p);

            return attributeValues;
        }
        catch (NamingException e)
        {
            StringBuffer matchString = new StringBuffer();
            for (int i = 0; i < matchAttrs_p.length; i++)
            {
                if (matchAttrs_p[i] != null)
                {
                    if (matchAttrs_p[i].length > 0)
                    {
                        matchString.append(matchAttrs_p[i][0]).append("=");
                    }
                    else
                    {
                        matchString.append("=");
                    }
                    if (matchAttrs_p[i].length > 1 && matchAttrs_p[i][1] != null)
                    {
                        matchString.append(matchAttrs_p[i][1]);
                    }
                }
                else
                {
                    matchString.append(" null ");
                }
            }
            String msg = "Exception reading attribute values from LDAP, LdapSearchBase=" + searchBase_p + " and matching attributes " + matchString.toString();
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

    /**
     * Searches the LDAP repository for records matching the given pattern. 
     * @param searchBase_p
     * @param attributeValues_p
     * @param returnIds_p
     * @return the result of the search
     * @throws OwConfigurationException
     * @throws OwInvalidOperationException 
     * @since 4.1.1.0
     */
    public NamingEnumeration<SearchResult> search(String searchBase_p, String[][] attributeValues_p, String[] returnIds_p) throws OwConfigurationException, OwInvalidOperationException
    {
        InitialDirContext ctx = null;
        try
        {
            ctx = createInitialDirContext();
            return this.search(ctx, searchBase_p, attributeValues_p, returnIds_p);
        }
        catch (NamingException e)
        {
            String msg = "Exception reading attribute values from LDAP, LdapSearchBase=" + searchBase_p + ".";
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

    private NamingEnumeration<SearchResult> search(DirContext context_p, String searchBase_p, String[][] attributeValues_p, String[] returnIds_p) throws NamingException
    {
        StringBuffer filter = new StringBuffer();
        if (0 != attributeValues_p.length)
        {
            for (int i = 0; i < attributeValues_p.length; i++)
            {
                filter.append("(");
                filter.append(attributeValues_p[i][0]);
                filter.append("=");
                filter.append(attributeValues_p[i][1]);
                filter.append(") ");
            }
            if (1 < attributeValues_p.length)
            {
                filter.insert(0, "(& ");
                filter.append(")");
            }
        }

        SearchControls ctrls = new SearchControls();
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        ctrls.setReturningAttributes(returnIds_p);

        NamingEnumeration<SearchResult> answer = context_p.search(searchBase_p, filter.toString(), ctrls);

        return answer;
    }

    private List<String> searchAttributeValues(DirContext context_p, String searchBase_p, String[][] attributeValues_p, String attribute_p) throws NamingException
    {
        List<String> attributeValueList = new ArrayList<String>();

        NamingEnumeration<SearchResult> answer = search(context_p, searchBase_p, attributeValues_p, new String[] { attribute_p });
        String answerAttribute = attribute_p + ":";
        int answerAttributeLen = answerAttribute.length();
        while (answer.hasMoreElements())
        {
            SearchResult sr = answer.nextElement();
            // LOG.debug("SearchResult: " + sr.toString());
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

        return attributeValueList;
    }

    /**
     * Search for objects with attributes matching a given pattern. 
     * 
     * @param searchBase
     * @param filter
     * @param returningAttibute
     * @param sortKey if null no sorting will be performed
     * @return a list of matching objects.
     */
    @SuppressWarnings("unchecked")
    public OwObjectCollection searchValuesByFilter(String searchBase, String filter, String returningAttibute, String sortKey) throws OwException
    {
        OwStandardObjectCollection result = new OwStandardObjectCollection();

        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        ctls.setTimeLimit(LDAP_TIME_LIMIT);
        ctls.setCountLimit(this.ldapCountLimit);
        String[] returningAttibutes = new String[] { returningAttibute };
        ctls.setReturningAttributes(returningAttibutes);

        InitialLdapContext ctx = createInitialDirContext();
        try
        {
            if (null != sortKey)
            {
                ctx.setRequestControls(new Control[] { new SortControl(sortKey, Control.NONCRITICAL) });
            }

            NamingEnumeration<SearchResult> answer = ctx.search(searchBase, filter, ctls);
            while (answer.hasMoreElements())
            {
                SearchResult sr = answer.nextElement();
                if ("distinguishedName".equals(returningAttibute))
                {
                    result.add(sr.getNameInNamespace());
                }
                else
                {
                    Attributes dnAttrs = sr.getAttributes();
                    if (dnAttrs != null)
                    {
                        Attribute att = dnAttrs.get(returningAttibute);
                        result.add(att.get());
                    }
                }
            }
        }
        catch (NamingException e)
        {
            throw new OwInvalidOperationException("Could not search by filter.", e);
        }
        catch (IOException e)
        {
            throw new OwInvalidOperationException("Could not sort ersults.", e);
        }

        if (result.size() >= this.ldapCountLimit)
        {
            result.setComplete(false);
        }

        LOG.info("Results: " + result.size());
        return result;
    }
}
