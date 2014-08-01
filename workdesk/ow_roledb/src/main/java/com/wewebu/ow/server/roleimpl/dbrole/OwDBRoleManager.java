package com.wewebu.ow.server.roleimpl.dbrole;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.role.OwIntegratedApplicationRoleManager;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.role.OwStandardRoleManager;
import com.wewebu.ow.server.roleimpl.log.OwLog;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Alfresco Workdesk DB Role Manager.<br/>
 * You have to define a DB Data Source to use the DB Role Manager.
 * Is used to set access right to different plugins, classes, index fields, design, searchtemplates...
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwDBRoleManager extends OwStandardRoleManager implements OwIntegratedApplicationRoleManager
{
    private static final String NAME_ATTRIBUTE_NAME = "name";
    private static final String ID_ATTRIBUTE_NAME = "id";
    /**the config element name for categories*/
    private static final String CONFIG_CATEGORIES_ELEMENT_NAME = "ConfigCategories";
    private static String DEFAULT_OW_ROLE_TABLE_NAME = "OW_ROLE";
    private static String DEFAULT_TB_ROLE_NAME = "TB_ROLE";
    private static final String DB_ROLE_MANAGER_TABLE_NAME = "DbTableName";
    private static final String INTEGRATED_APPLICATIONS_ELEMENT_NAME = "IntegratedApplications";
    private static final String APPLICATION = "Application";
    private static final String DEFAULT_APPLICATION_ELEMENT_NAME = "DefaultApplication";

    private static final String DEFAULT_APPLICATION_ID = "AWD";
    private static final String DEFAULT_APPLICATION_NAME = "AWD.name";

    /**empty value constant*/
    private static final String EMPTY_VALUE = "";

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDBRoleManager.class);

    /** the name of the element defining the table name in role manager configuration*/

    /** the Spring JdbcTemplate for the database access */
    private JdbcTemplate m_JdbcTemplate = null;

    /** master roles defined ? */
    private boolean m_supportMasterRoles = false;

    /** the role that is currently selected for the user */
    private String m_strMasterRole;

    /** collection of master role names */
    private Collection m_masterrolenames;

    /** filtered the roles through the selected master role group */
    private Collection m_filteredRoles;

    /** flag indicating the used database scheme */
    private int m_schemeVersion = 1;

    /** flag indicating that the current user is a security admin */
    protected boolean m_isSecurityAdmin = false;

    /** ID of admin plugin if the current user a administrator and has a admin plugin enabled (entered in owbootstrap.xml), null otherwise. */
    protected String m_securityAdminPluginID;

    /** definition of table names and columns */
    private String tbRole = DEFAULT_TB_ROLE_NAME;
    private String owRoleTableName = DEFAULT_OW_ROLE_TABLE_NAME;

    private static final String ROLE_NAME = "ROLE_NAME";
    private static final String ROLE_RESOURCE = "ROLE_RESOURCE";
    private static final String ROLE_ACCESS = "ROLE_ACCESS";
    private static final String ROLE_ACCESS_MASK = "ROLE_ACCESS_MASK";
    private static final String CATEGORY = "CATEGORY";
    /** flag informing if categories are configured in <code>owboostrap.xml</code> file*/
    private boolean m_hasCategoriesConfigured = false;

    private Map m_isAllowedCacheByCategories;

    /** collection of plugins */
    private Collection<String> m_allAllowedPlugins;

    /** the list of configured category IDs */
    private Set m_configuredCategories;

    /** list of user names defined as Security Admin's in the config */
    private Collection m_SecurityAdminUsers;

    /** list of user groups defined as Security Admin's in the config */
    private Collection m_SecurityAdminGroups;

    private Map m_effectiveAccessMaskCacheByCategories;

    private Map m_precalculatedFullAccessMasks;
    private String applicationId;

    private static String DYNAMIC_RESOURCE_PREFIX = "d:";

    private String defaultApplicationId = DEFAULT_APPLICATION_ID;

    private Map<String, OwString> applicationsNames;
    private Map<String, String> applicationsDbTableNames;
    private Map<String, Set<Integer>> applicationsCategories;

    public OwDBRoleManager()
    {
        this(DEFAULT_APPLICATION_ID);
    }

    OwDBRoleManager(String applicationId)
    {
        this.applicationId = applicationId;
        m_isAllowedCacheByCategories = new HashMap();

        m_SecurityAdminUsers = new LinkedList();
        m_SecurityAdminGroups = new LinkedList();

        m_effectiveAccessMaskCacheByCategories = new HashMap();
        applicationsNames = new HashMap<String, OwString>();
        applicationsDbTableNames = new HashMap<String, String>();
        applicationsCategories = new HashMap<String, Set<Integer>>();
    }

    /** check if rolemanager supports explicit deny of resources.
     * 
     * @return a <code>boolean</code>
     */
    public boolean canExplicitDeny()
    {
        return (m_schemeVersion >= 2);
    }

    /** init the role manager, set context
     * 
     * @param configNode_p OwXMLUtil node with configuration information
     * @param mainContext_p reference to the main app context of the application 
     * 
     * @exception OwException
     */
    public void init(OwRoleManagerContext mainContext_p, OwXMLUtil configNode_p) throws OwException
    {
        try
        {
            super.init(mainContext_p, configNode_p);

            m_JdbcTemplate = mainContext_p.getJDBCTemplate();
            if (m_JdbcTemplate == null)
            {
                String msg = "OwDBRoleManager.init: No default data source configured! The DB Role Manager needs a default data source to read the access rights from.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            m_supportMasterRoles = OwXMLDOMUtil.getSafeBooleanAttributeValue(getConfigNode().getSubNode("MasterRoles"), "enable", false);

            m_schemeVersion = getConfigNode().getSafeIntegerValue("SchemeVersion", 0); //0 = undefined schema value, or tag is missing
            if ((m_schemeVersion < 1) || (m_schemeVersion > 3))
            {
                String msg = "OwDBRoleManager.init: The tag SchemeVersion for the OwDBRoleManager (owbootstrap.xml) is missing or contains a wrong value [" + m_schemeVersion + "], it may only contain the values 1, 2 or 3.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            // get the pluginid of the security master plugin
            m_securityAdminPluginID = OwXMLDOMUtil.getSafeStringAttributeValue(m_ConfigNode.getSubNode("SecurityAdmin"), "pluginid", null);

            tbRole = configNode_p.getSafeTextValue(DB_ROLE_MANAGER_TABLE_NAME, tbRole);
            owRoleTableName = configNode_p.getSafeTextValue(DB_ROLE_MANAGER_TABLE_NAME, owRoleTableName);

            readAdminUsersAndGroups(configNode_p);

            initIntegratedApplications(mainContext_p, configNode_p);

            owRoleTableName = applicationsDbTableNames.get(applicationId);
            m_configuredCategories = applicationsCategories.get(applicationId);
            m_hasCategoriesConfigured = m_configuredCategories != null && !m_configuredCategories.isEmpty();
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not create DB role manager.", e);
        }
    }

    private void initIntegratedApplications(OwRoleManagerContext mainContext_p, OwXMLUtil configNode_p) throws Exception
    {
        defaultApplicationId = DEFAULT_APPLICATION_ID;
        String defaultAppName = DEFAULT_APPLICATION_NAME;
        OwXMLUtil defaultApplication = configNode_p.getSubUtil(DEFAULT_APPLICATION_ELEMENT_NAME);
        if (defaultApplication != null)
        {
            String id = defaultApplication.getSafeStringAttributeValue(ID_ATTRIBUTE_NAME, null);
            String name = defaultApplication.getSafeStringAttributeValue(NAME_ATTRIBUTE_NAME, null);
            if (id != null)
            {
                defaultApplicationId = id;
            }

            if (name != null)
            {
                defaultAppName = name;
            }
        }

        applicationsNames.put(defaultApplicationId, new OwString(defaultAppName));
        applicationsDbTableNames.put(defaultApplicationId, owRoleTableName);
        String defaultConfigCategories = configNode_p.getSafeTextValue(CONFIG_CATEGORIES_ELEMENT_NAME, EMPTY_VALUE);
        Set<Integer> defaultCategories = readCategries(defaultConfigCategories);
        applicationsCategories.put(defaultApplicationId, defaultCategories);

        OwXMLUtil integratedApplications = configNode_p.getSubUtil(INTEGRATED_APPLICATIONS_ELEMENT_NAME);
        if (integratedApplications != null)
        {
            List<OwXMLUtil> applications = integratedApplications.getSafeUtilList(APPLICATION);
            for (OwXMLUtil application : applications)
            {
                String id = application.getSafeStringAttributeValue(ID_ATTRIBUTE_NAME, null);
                String name = application.getSafeStringAttributeValue(NAME_ATTRIBUTE_NAME, null);
                String dbTableName = application.getSafeTextValue(DB_ROLE_MANAGER_TABLE_NAME, null);
                if (id != null && dbTableName != null)
                {
                    applicationsNames.put(id, new OwString(name));
                    applicationsDbTableNames.put(id, dbTableName);
                    String configCategoriesString = application.getSafeTextValue(CONFIG_CATEGORIES_ELEMENT_NAME, EMPTY_VALUE);
                    Set configCategories = readCategries(configCategoriesString);
                    applicationsCategories.put(id, configCategories);
                }
            }
        }
    }

    private Set<Integer> readCategries(String configCategories_p) throws OwConfigurationException, NoSuchFieldException, SecurityException
    {
        LinkedHashSet<Integer> configuredCategoriesSet = new LinkedHashSet<Integer>();

        if (configCategories_p != null && !configCategories_p.equals(EMPTY_VALUE))
        {
            StringTokenizer tokenizer = new StringTokenizer(configCategories_p.trim(), ",");

            while (tokenizer.hasMoreTokens())
            {
                String categoryAsString = tokenizer.nextToken().trim();

                int fieldValue = -1;
                try
                {
                    Field categoryField = OwRoleManager.class.getField(categoryAsString);
                    fieldValue = categoryField.getInt(null);
                }
                catch (Exception e)
                {
                    //the interface OwRoleManager doesn't have a field with the given name
                    String msg = "The field specified by category (" + categoryAsString + ") doesn't exist. ";
                    LOG.error(msg, e);
                    throw new OwConfigurationException(getContext().localize1("owrole.OwDBRoleManager.fielddoesntexist", "The field specified by category (%1) doesn't exist.", categoryAsString), e);
                }
                Integer configuredCategory = Integer.valueOf(fieldValue);
                if (getCategories().contains(configuredCategory))
                {
                    configuredCategoriesSet.add(configuredCategory);
                }
                else
                {
                    //the interface OwRoleManager has a field with the given name, but that field is not used as a category
                    String msg = "The category " + categoryAsString + " doesn't exist.";
                    LOG.error("OwDBRoleManager.initConfiguredCategories: " + msg);
                    throw new OwConfigurationException(getContext().localize1("owrole.OwDBRoleManager.categorydoesntexist", "The category (%1) doesn't exist.", categoryAsString));
                }
            }
        }

        return configuredCategoriesSet;
    }

    /** init called AFTER the user has logged in.
    *
    *  NOTE: This function is called only once after login to do special initialization, 
    *        which can only be performed with valid credentials.
    */
    public void loginInit() throws OwException
    {
        try
        {
            super.loginInit();

            // set design from role
            updateDesign();

            // set the ID of the security admin plugin
            m_isSecurityAdmin = checkForSecurityAdmin();
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not initialize the DB role manager.", e);
        }

    }

    /**
     * retrieve all users and groups defined as Security Admins from the XML config and store them in
     * the internal lists for further usage during loginInt.
     * 
     * @param configNode_p the config node with the rolemanager configuration
     * @throws Exception
     */
    private void readAdminUsersAndGroups(OwXMLUtil configNode_p) throws Exception
    {
        m_SecurityAdminUsers.clear();
        m_SecurityAdminGroups.clear();
        Node securityAdminNode = configNode_p.getSubNode("SecurityAdmin");
        if (securityAdminNode == null)
        {
            return;
        }
        for (Node testChild = securityAdminNode.getFirstChild(); testChild != null; testChild = testChild.getNextSibling())
        {
            if (testChild.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            Element elem = (Element) testChild;
            if (elem.getTagName().equals("User"))
            {
                String userName = OwXMLDOMUtil.getElementText(elem);
                if ((userName != null) && (userName.length() > 0))
                {
                    m_SecurityAdminUsers.add(userName);
                }
                else
                {
                    LOG.error("OwDBRoleManager.readAdminUsersAndGroups: A node owbootstrap.xml / <RoleManager> / <SecurityAdmin> / <User> is empty!");
                }
            }
            else if (elem.getTagName().equals("Group"))
            {
                String groupName = OwXMLDOMUtil.getElementText(elem);
                if ((groupName != null) && (groupName.length() > 0))
                {
                    m_SecurityAdminGroups.add(groupName);
                }
                else
                {
                    LOG.error("OwDBRoleManager.readAdminUsersAndGroups: A node owbootstrap.xml / <RoleManager> / <SecurityAdmin> / <Group> is empty!");
                }
            }
        }
    }

    /**
     * is user configured as Security Admin then set the ID of the enabled plugin.
     * 
     * @throws Exception
     */
    protected boolean checkForSecurityAdmin() throws Exception
    {
        // get the current user
        OwBaseUserInfo currentUser = getContext().getCurrentUser();

        // check for user name
        if (m_SecurityAdminUsers.contains(currentUser.getUserName()))
        {
            return (true);
        }

        // check for groups
        if (m_SecurityAdminGroups.size() > 0)
        {
            Iterator groupsIt = currentUser.getGroups().iterator();
            while (groupsIt.hasNext())
            {
                OwUserInfo testUserInfo = (OwUserInfo) groupsIt.next();
                if (m_SecurityAdminGroups.contains(testUserInfo.getUserName()))
                {
                    return (true);
                }
            }
        }

        // no security admin
        return (false);
    }

    /** update the design name from db */
    /**
     * @throws Exception
     */
    protected void updateDesign() throws Exception
    {
        if (!isMsOfficeEmbedded())
        {
            // === set design from role
            Collection designNames = getAllowedResources(ROLE_CATEGORY_DESIGN);
            if (designNames.size() != 0)
            {
                // take the first assigned design
                m_strDesign = (String) designNames.iterator().next();

                // log
                if (designNames.size() > 1)
                {
                    LOG.error("OwDBRoleManager.updateDesign: User has more than one design assigned in OwDBRoleManager: " + designNames.toString());
                }
            }
            // Bug 1658: leave the initially detected design name untouched has no
            // configured access rights to any design.
        }

    }

    @Override
    public String getApplicationId()
    {
        return this.applicationId;
    }

    /**
     * Get all available plugins filtered by role
     * @throws Exception 
     * @since 3.1.0.0
     */
    private Collection<String> getAllAvailablePlugins() throws Exception
    {
        // get list of roles applying to the current user
        Collection roleNamesOfUser = getFilteredRoles();
        m_allAllowedPlugins = new HashSet<String>();

        if ((m_schemeVersion == 2) || (m_schemeVersion == 3))
        {
            // we can use the SQL MAX function here to get the cumulative access right over
            // all roles at once. If at least one role has the DENY flag set (ROLE_ACCESS=2),
            // the user does not have access to the resource. MAX over all role access rights
            // will be 2 in that case. Otherwise, the user needs at least one role with positive
            // access rights to be allowed for that resource. MAX will be 1 in that case.
            StringBuffer ps = new StringBuffer();
            Object[] psParams = new Object[roleNamesOfUser.size() + 1];
            ps.append("select max(");
            ps.append(ROLE_ACCESS);
            ps.append(") AS OWDACL, ").append(ROLE_RESOURCE).append(" from ");
            ps.append(owRoleTableName);
            ps.append(" where (");
            ps.append(CATEGORY);
            ps.append(" = ?) and (");
            ps.append(ROLE_NAME);
            ps.append(" in (");
            // 1 id for plugins 
            psParams[0] = Integer.valueOf(ROLE_CATEGORY_PLUGIN);
            Iterator roleNamesIt = roleNamesOfUser.iterator();

            int pos = 1;
            while (roleNamesIt.hasNext())
            {
                if (pos > 1)
                {
                    ps.append(",");
                }
                psParams[pos++] = roleNamesIt.next();
                ps.append("?");
            }
            ps.append("))");
            ps.append("  group by ").append(ROLE_RESOURCE);

            SqlRowSet rowSet = m_JdbcTemplate.queryForRowSet(ps.toString(), psParams);

            if (rowSet.first())//check if we have rows
            {
                do
                {
                    int max = rowSet.getInt("OWDACL");
                    if (max == ROLE_ACCESS_RIGHT_ALLOWED)
                    {
                        String resource = rowSet.getString(ROLE_RESOURCE);
                        m_allAllowedPlugins.add(decodeResourceId(ROLE_CATEGORY_PLUGIN, resource));

                    }
                } while (rowSet.next());
            }
        }
        return m_allAllowedPlugins;

    }

    private Map getIsAllowedCache(int category_p)
    {
        Integer mapKey = Integer.valueOf(category_p);
        Map result = (Map) m_isAllowedCacheByCategories.get(mapKey);
        if (result != null)
        {
            return result;
        }
        // not in the map. we need to create the cache synchronized
        synchronized (m_isAllowedCacheByCategories)
        {
            result = new HashMap();
            m_isAllowedCacheByCategories.put(mapKey, result);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.role.OwRoleManager#isAllowed(int, java.lang.String)
     */
    public boolean isAllowed(int category_p, String resourceId_p) throws Exception
    {
        // if user is a Security Admin allow the Admin Plugin
        if (m_isSecurityAdmin && (m_securityAdminPluginID != null))
        {
            if ((category_p == OwRoleManager.ROLE_CATEGORY_PLUGIN) && resourceId_p.equals(m_securityAdminPluginID))
            {
                return true;
            }
        }
        // check if this category is configured in owboostrap.xml (see bug 3138)
        // and allow the resource to be used.
        if (isAllowedByConfiguredCategory(category_p))
        {
            return true;
        }

        // get the isAllowed cache
        Map isAllowedCache = getIsAllowedCache(category_p);

        // try to retrieve the value from the cache
        Boolean cachedIsAllowedState = (Boolean) isAllowedCache.get(resourceId_p);
        if (cachedIsAllowedState != null)
        {
            return cachedIsAllowedState.booleanValue();
        }
        // The isAllowed state is not in the cache. So we need to evaluate the
        // value from the database. We do not need to synchronize this section. we
        // just need to sync the put method call of the cache.

        // get list of roles applying to the current user
        Collection roleNamesOfUser = getFilteredRoles();
        // detect the isAllowedState depending on the database schema
        boolean isAllowedState = false;

        //get all plugins at once
        if (category_p == ROLE_CATEGORY_PLUGIN)
        {
            if (m_allAllowedPlugins == null)
            {
                getAllAvailablePlugins();
            }

            if (m_allAllowedPlugins.contains(resourceId_p))
            {
                isAllowedState = true;
            }
            else
            {
                isAllowedState = false;
            }
        }
        else
        {
            if ((m_schemeVersion == 2) || (m_schemeVersion == 3))
            {
                // we can use the SQL MAX function here to get the cumulative access right over
                // all roles at once. If at least one role has the DENY flag set (ROLE_ACCESS=2),
                // the user does not have access to the resource. MAX over all role access rights
                // will be 2 in that case. Otherwise, the user needs at least one role with positive
                // access rights to be allowed for that resource. MAX will be 1 in that case.
                StringBuffer ps = new StringBuffer();
                Object[] psParams = new Object[roleNamesOfUser.size() + 2];
                ps.append("select count(0),max(");
                ps.append(ROLE_ACCESS);
                ps.append(") from ");
                ps.append(owRoleTableName);
                ps.append(" where (");
                ps.append(CATEGORY);
                ps.append(" = ?) and (");
                ps.append(ROLE_RESOURCE);
                ps.append(" = ?) and (");
                ps.append(ROLE_NAME);
                ps.append(" in (");
                psParams[0] = Integer.valueOf(category_p);
                psParams[1] = encodeResourceId(category_p, resourceId_p);
                Iterator roleNamesIt = roleNamesOfUser.iterator();
                if (!roleNamesIt.hasNext())
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwDBRoleManager.isAllowed: The logged in user hasn't any role, so he will not have acces to the AWD plugins, category_p=" + category_p + ", resourceId_p=" + resourceId_p + "...");
                    }
                    return false;
                }
                int pos = 2;
                while (roleNamesIt.hasNext())
                {
                    if (pos > 2)
                    {
                        ps.append(",");
                    }
                    psParams[pos++] = roleNamesIt.next();
                    ps.append("?");
                }
                ps.append("))");
                SqlRowSet rowSet = m_JdbcTemplate.queryForRowSet(ps.toString(), psParams);
                rowSet.first();

                if (rowSet.isFirst() && rowSet.isLast())
                {
                    int count = rowSet.getInt(1);
                    int max = rowSet.getInt(2);
                    // we have a special handling for the category ROLE_CATEGORY_INDEX_FIELDS
                    if (category_p == ROLE_CATEGORY_INDEX_FIELDS)
                    {
                        if (count == roleNamesOfUser.size())
                        {
                            // the database contains data about all roles of the user. we can use
                            // the results from the MAX aggregate function directly
                            isAllowedState = (max == ROLE_ACCESS_RIGHT_ALLOWED);
                        }
                        else
                        {
                            // the database does NOT contain information about all roles of the user.
                            // missing access right information in the category ROLE_CATEGORY_INDEX_FIELDS
                            // is defaulted to 1 (ALLOWED) instead of 0 (DENIED) like in all other
                            // categories.
                            // So we need to incorporate this ALLOWED state into the cumulative
                            // access right evaluation by hand.
                            max = (max > ROLE_ACCESS_RIGHT_ALLOWED) ? max : ROLE_ACCESS_RIGHT_ALLOWED;
                            isAllowedState = (max == ROLE_ACCESS_RIGHT_ALLOWED);
                        }
                    }
                    else
                    {
                        isAllowedState = (max == ROLE_ACCESS_RIGHT_ALLOWED);
                    }
                }
                else
                {
                    // Something went wrong. The aggregate functions did not return exactly one row.
                    // block resource and log error
                    LOG.error("isAllowed(): The aggregatze functions did not return exactly one row!");
                    isAllowedState = false;
                }
            }
            else if (m_schemeVersion == 1)
            {
                StringBuffer ps = new StringBuffer();
                Object[] psParams = new Object[roleNamesOfUser.size() + 2];
                ps.append("select count(0) from ");
                ps.append(tbRole);
                ps.append(" where (");
                ps.append(CATEGORY);
                ps.append(" = ?) and (");
                ps.append(ROLE_RESOURCE);
                ps.append(" = ?) and (");
                ps.append(ROLE_NAME);
                ps.append(" in (");
                psParams[0] = Integer.valueOf(category_p);
                psParams[1] = encodeResourceId(category_p, resourceId_p);
                Iterator roleNamesIt = roleNamesOfUser.iterator();
                int pos = 2;
                while (roleNamesIt.hasNext())
                {
                    if (pos > 2)
                    {
                        ps.append(",");
                    }
                    psParams[pos++] = roleNamesIt.next();
                    ps.append("?");
                }
                ps.append("))");
                int occurrence = m_JdbcTemplate.queryForInt(ps.toString(), psParams);
                isAllowedState = (occurrence > 0);
            }
            else
            {
                String msg = "OwDBRoleManager.isAllowed: Unknown database scheme version.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }
        }

        // store the isAllowedState in the cache. SYNCHRONIZED!
        synchronized (isAllowedCache)
        {
            isAllowedCache.put(resourceId_p, (isAllowedState ? Boolean.TRUE : Boolean.FALSE));
        }

        // return the isAllowedState
        return isAllowedState;
    }

    /** update all  allowed plugins after role change */
    protected void updatePlugins()
    {
        m_allAllowedPlugins = null;
    }

    /**
     * Check if the given category was configured in <code>owboostrap.xml</code>.
     * @param category_p - the given category
     * @return - <code>true</code> the given category was not configured, so all resources must be allowed for the configured category.
     * @since 2.5.3.0
     * @throws OwInvalidOperationException if the given category is a dynamic category and it is not configured
     */
    private boolean isAllowedByConfiguredCategory(int category_p) throws OwInvalidOperationException
    {
        boolean result = false;
        if (m_hasCategoriesConfigured)
        {
            Integer categoryAsInt = Integer.valueOf(category_p);
            if (!getConfiguredCategories().contains(categoryAsInt))
            {
                if (isStaticResourceCategory(category_p))
                {
                    result = true;
                }
                else
                {
                    OwInvalidOperationException e = new OwInvalidOperationException(new OwString("owrole.OwDBRoleManager.invalidCategory", "Invalid role manager category type."));
                    LOG.error("OwDBRoleManager.isAllowedByConfiguredCategory : Static or configured-dynamic category expected but got " + category_p, e);
                    throw e;
                }
            }
        }
        return result;
    }

    private Map getEffectiveAccessMaskCache(int category_p)
    {
        Integer mapKey = Integer.valueOf(category_p);
        Map result = (Map) m_effectiveAccessMaskCacheByCategories.get(mapKey);
        if (result != null)
        {
            return result;
        }
        // not in the map. we need to create the cache synchronized
        synchronized (m_effectiveAccessMaskCacheByCategories)
        {
            result = new HashMap();
            m_effectiveAccessMaskCacheByCategories.put(mapKey, result);
        }
        return result;
    }

    /**
     * checks if the current user designated by its credentials is allowed to use the
     * given resource and has the required right in its access mask for that resource.
     * 
     * @param category_p of the requested function
     * @param resourceId_p String ID of the requested resource (function / plugin id)
     * @param requiredAccessMask_p a 32 bit bitset with all flags set that have to be checked for the current user
     *
     * @return true if user has permission, false if permission is denied
     */
    public boolean hasAccessMaskRight(int category_p, String resourceId_p, int requiredAccessMask_p) throws Exception
    {
        // if user is Security Admin allow admin plugin
        if (m_isSecurityAdmin && (m_securityAdminPluginID != null))
        {
            if ((category_p == OwRoleManager.ROLE_CATEGORY_PLUGIN) && resourceId_p.equals(m_securityAdminPluginID))
            {
                return true;
            }
        }

        // check if the user is allowed at all
        if (!isAllowed(category_p, resourceId_p))
        {
            return false;
        }

        if (m_schemeVersion < 3 || isAllowedByConfiguredCategory(category_p))
        {
            // scheme version < 3. all allowed resources have full access masks
            return true;
        }

        // get the effectiveAccessMask cache
        Map effectiveAccessMaskCache = getEffectiveAccessMaskCache(category_p);

        // try to retrieve the value from the cache
        Integer cachedEffectiveAccessMask = (Integer) effectiveAccessMaskCache.get(resourceId_p);
        if (cachedEffectiveAccessMask != null)
        {
            return (cachedEffectiveAccessMask.intValue() & requiredAccessMask_p) == requiredAccessMask_p;
        }

        // The effectiveAccessMask is not in the cache. So we need to evaluate the
        // value from the database. We do not need to synchronize this section. we
        // just need to sync the put method call of the cache.

        Integer effectiveMask;
        // get list of roles applying to the current user
        Collection roleNamesOfUser = getFilteredRoles();
        // create query
        StringBuffer ps = new StringBuffer();
        Object[] psParams = new Object[roleNamesOfUser.size() + 2];
        ps.append("select ");
        ps.append(ROLE_ACCESS_MASK);
        ps.append(" from ");
        ps.append(owRoleTableName);
        ps.append(" where (");
        ps.append(CATEGORY);
        ps.append(" = ?) and (");
        ps.append(ROLE_RESOURCE);
        ps.append(" = ?) and (");
        ps.append(ROLE_NAME);
        ps.append(" in (");
        psParams[0] = Integer.valueOf(category_p);
        psParams[1] = encodeResourceId(category_p, resourceId_p);
        Iterator roleNamesIt = roleNamesOfUser.iterator();
        if (!roleNamesIt.hasNext())
        {
            LOG.debug("OwDBRoleManager.hasAccessMaskRight: The logged in user hasn't any role, so he will not have acces to the AWD plugins, category_p=" + category_p + ", resourceId_p=" + resourceId_p + "...");
            return false;
        }
        int pos = 2;
        while (roleNamesIt.hasNext())
        {
            if (pos > 2)
            {
                ps.append(",");
            }
            psParams[pos++] = roleNamesIt.next();
            ps.append("?");
        }
        ps.append("))");
        final int requestedRoles = roleNamesOfUser.size();
        effectiveMask = (Integer) m_JdbcTemplate.query(ps.toString(), psParams, new ResultSetExtractor() {
            public Object extractData(ResultSet rs_p) throws SQLException, DataAccessException
            {
                int combinedMask = 0;
                int rolesFound = 0;
                while (rs_p.next())
                {
                    int singleMask = rs_p.getInt(ROLE_ACCESS_MASK);
                    combinedMask = combinedMask | singleMask;
                    rolesFound++;
                }
                // as defined in the system design, non-present rows in the database are equal to full access.
                // so we need to OR the combined mask against the full mask if we found less roles in the DB
                // as have been requested.
                if (rolesFound != requestedRoles)
                {
                    combinedMask = combinedMask | -1;
                }
                return Integer.valueOf(combinedMask);
            }
        });

        // store the isAllowedState in the cache. SYNCHRONIZED!
        synchronized (effectiveAccessMaskCache)
        {
            effectiveAccessMaskCache.put(resourceId_p, effectiveMask);
        }

        // return the result
        return (effectiveMask.intValue() & requiredAccessMask_p) == requiredAccessMask_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.role.OwRoleManager#getAccessRights(java.lang.String, int, java.lang.String)
     */
    public int getAccessRights(String roleName_p, int category_p, String resourceId_p) throws Exception
    {
        if (m_schemeVersion == 1)
        {
            return (getAccessRights_SchemeVersion1(roleName_p, category_p, resourceId_p));
        }
        else if ((m_schemeVersion == 2) || (m_schemeVersion == 3))
        {
            return (getAccessRights_SchemeVersion2(roleName_p, category_p, resourceId_p));
        }
        else
        {
            return (ROLE_ACCESS_RIGHT_NOT_ALLOWED);
        }
    }

    /**
     * return the access rights for scheme version 1.
     * 
     * @param roleName_p
     * @param category_p
     * @param resourceId_p
     * @return int
     * @throws Exception
     */
    protected int getAccessRights_SchemeVersion1(String roleName_p, int category_p, String resourceId_p) throws Exception
    {
        int accessRights = ROLE_ACCESS_RIGHT_NOT_ALLOWED;

        // read access rights from database
        StringBuffer ps = new StringBuffer();
        ps.append("select count(0) from ");
        ps.append(tbRole);
        ps.append(" where (");
        ps.append(ROLE_NAME);
        ps.append(" = ?) and (");
        ps.append(CATEGORY);
        ps.append(" = ?) and (");
        ps.append(ROLE_RESOURCE);
        ps.append(" = ?)");
        int occurrence = m_JdbcTemplate.queryForInt(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
        if (occurrence > 0)
        {
            accessRights = ROLE_ACCESS_RIGHT_ALLOWED;
        }

        // return the access rights
        return (accessRights);
    }

    /**
     * return the access rights for scheme version 2.
     * 
     * @param roleName_p
     * @param category_p
     * @param resourceId_p
     * @return int
     * @throws Exception
     */
    protected int getAccessRights_SchemeVersion2(String roleName_p, int category_p, String resourceId_p) throws Exception
    {
        int accessRights = ROLE_ACCESS_RIGHT_NOT_ALLOWED;

        // read access rights from database
        try
        {
            StringBuffer ps = new StringBuffer();
            ps.append("select ");
            ps.append(ROLE_ACCESS);
            ps.append(" from ");
            ps.append(owRoleTableName);
            ps.append(" where (");
            ps.append(ROLE_NAME);
            ps.append(" = ?) and (");
            ps.append(CATEGORY);
            ps.append(" = ?) and (");
            ps.append(ROLE_RESOURCE);
            ps.append(" = ?)");
            accessRights = m_JdbcTemplate.queryForInt(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
        }
        catch (IncorrectResultSizeDataAccessException e)
        {
            if ((e.getActualSize() == 0) && (category_p == OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS))
            {
                accessRights = ROLE_ACCESS_RIGHT_ALLOWED;
            }
            else
            {
                accessRights = ROLE_ACCESS_RIGHT_NOT_ALLOWED;
            }
        }

        // sanity check the value from the database
        // We throw an Exception here to stop the application. If this field contains unknown values, they could be written by
        // a new version of the OWD. Since we do not know the meaning of these values, we stop here to prevent users from
        // accessing functions with an older version of the AWD they are not intended to use.
        if ((accessRights < ROLE_ACCESS_RIGHT_NOT_ALLOWED) || (accessRights > ROLE_ACCESS_RIGHT_DENIED))
        {
            String msg = "OwDBRoleManager.getAccessRights_SchemeVersion2: The database contains invalid values in the field role_access.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }

        // return the access rights
        return (accessRights);
    }

    public void replaceResource(String roleName_p, int category_p, String oldResourceId_p, String newResourceId_p) throws Exception
    {
        if (m_schemeVersion == 1)
        {
            throw new OwInvalidOperationException("Scheme Version 1 not implemented.");
        }
        else if ((m_schemeVersion == 2) || (m_schemeVersion == 3))
        {
            replaceResource_SchemeVersion2(roleName_p, category_p, oldResourceId_p, newResourceId_p);
        }

    }

    protected void replaceResource_SchemeVersion2(String roleName_p, int category_p, String oldResourceId_p, String newResourceId_p) throws Exception
    {
        StringBuffer ps = new StringBuffer();
        ps.append("update ");
        ps.append(owRoleTableName);
        ps.append(" set ");
        ps.append(ROLE_RESOURCE);
        ps.append(" = ? where (");
        ps.append(ROLE_NAME);
        ps.append(" = ?) and (");
        ps.append(CATEGORY);
        ps.append(" = ?) and (");
        ps.append(ROLE_RESOURCE);
        ps.append(" = ?)");

        m_JdbcTemplate.update(ps.toString(), new Object[] { encodeResourceId(category_p, newResourceId_p), roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, oldResourceId_p) });
    }

    /**
     * persists the access rights for a given role to a given resource.
     * 
     * @param roleName_p name of the role to set the access rights for
     * @param category_p category of the resource to set the access rights for
     * @param resourceId_p ID of the resource to set the access rights for
     * @param accessRights_p the new access rights to persist as one of the ROLE_ACCESS_RIGHT_ constants
     * @throws Exception
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_NOT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_DENIED
     */
    public void setAccessRights(String roleName_p, int category_p, String resourceId_p, int accessRights_p) throws Exception
    {
        if (m_schemeVersion == 1)
        {
            setAccessRights_SchemeVersion1(roleName_p, category_p, resourceId_p, accessRights_p);
        }
        else if ((m_schemeVersion == 2) || (m_schemeVersion == 3))
        {
            setAccessRights_SchemeVersion2(roleName_p, category_p, resourceId_p, accessRights_p);
        }
        else
        {
            String msg = "OwDBRoleManager.setAccessRights: Invalid database scheme version, version = " + m_schemeVersion;
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
        // clear the entry in the isAllowedCache. We might have set the access rights for a role that will
        // affect the granted privileges of the currently logged on user
        Map isAllowedCache = getIsAllowedCache(category_p);
        synchronized (isAllowedCache)
        {
            isAllowedCache.remove(resourceId_p);
        }
    }

    /**
     * persists the access rights for a given role to a given resource in the database with scheme version 1.
     * 
     * @param roleName_p name of the role to set the access rights for
     * @param category_p category of the resource to set the access rights for
     * @param resourceId_p ID of the resource to set the access rights for
     * @param accessRights_p the new access rights to persist as one of the ROLE_ACCESS_RIGHT_ constants
     * @throws Exception
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_NOT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_DENIED
     */
    protected void setAccessRights_SchemeVersion1(String roleName_p, int category_p, String resourceId_p, int accessRights_p) throws Exception
    {
        // sanity check
        if ((accessRights_p < ROLE_ACCESS_RIGHT_NOT_ALLOWED) || (accessRights_p > ROLE_ACCESS_RIGHT_ALLOWED))
        {
            String msg = "OwDBRoleManager.setAccessRights_SchemeVersion1: A database in scheme version 1 can not store explicit deny values.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg);
        }
        // get current occurrence in database
        StringBuffer ps = new StringBuffer();
        ps.append("select count(0) from ");
        ps.append(tbRole);
        ps.append(" where (");
        ps.append(ROLE_NAME);
        ps.append(" = ?) and (");
        ps.append(CATEGORY);
        ps.append(" = ?) and (");
        ps.append(ROLE_RESOURCE);
        ps.append(" = ?)");
        int occurrence = m_JdbcTemplate.queryForInt(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
        // perform insert or update
        if (occurrence > 0)
        {
            // resource is currently allowed
            if (accessRights_p == ROLE_ACCESS_RIGHT_NOT_ALLOWED)
            {
                // remove access rights from database
                ps = new StringBuffer();
                ps.append("delete from ");
                ps.append(tbRole);
                ps.append(" where (");
                ps.append(ROLE_NAME);
                ps.append(" = ?) and (");
                ps.append(CATEGORY);
                ps.append(" = ?) and (");
                ps.append(ROLE_RESOURCE);
                ps.append(" = ?)");
                m_JdbcTemplate.update(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
            }
        }
        else
        {
            // resource is currently not allowed
            if (accessRights_p == ROLE_ACCESS_RIGHT_ALLOWED)
            {
                // add access rights to database
                ps = new StringBuffer();
                ps.append("insert into ");
                ps.append(tbRole);
                ps.append(" (");
                ps.append(ROLE_NAME);
                ps.append(",");
                ps.append(CATEGORY);
                ps.append(",");
                ps.append(ROLE_RESOURCE);
                ps.append(") values (?,?,?)");
                m_JdbcTemplate.update(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
            }
        }
    }

    /**
     * persists the access rights for a given role to a given resource in the database with scheme version 2.
     * 
     * @param roleName_p name of the role to set the access rights for
     * @param category_p category of the resource to set the access rights for
     * @param resourceId_p ID of the resource to set the access rights for
     * @param accessRights_p the new access rights to persist as one of the ROLE_ACCESS_RIGHT_ constants
     * @throws Exception
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_NOT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_ALLOWED
     * @see OwRoleManager#ROLE_ACCESS_RIGHT_DENIED
     */
    protected void setAccessRights_SchemeVersion2(String roleName_p, int category_p, String resourceId_p, int accessRights_p) throws Exception
    {
        if (OwRoleManager.ROLE_ACCESS_RIGHT_NOT_ALLOWED == accessRights_p)
        {
            StringBuffer ps = new StringBuffer();
            ps.append("delete from ");
            ps.append(owRoleTableName);
            ps.append(" where (");
            ps.append(ROLE_NAME);
            ps.append(" = ?) and (");
            ps.append(CATEGORY);
            ps.append(" = ?) and (");
            ps.append(ROLE_RESOURCE);
            ps.append(" = ?)");

            m_JdbcTemplate.update(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
        }
        else
        {
            StringBuffer ps = new StringBuffer();
            ps.append("update ");
            ps.append(owRoleTableName);
            ps.append(" set ");
            ps.append(ROLE_ACCESS);
            ps.append(" = ? where (");
            ps.append(ROLE_NAME);
            ps.append(" = ?) and (");
            ps.append(CATEGORY);
            ps.append(" = ?) and (");
            ps.append(ROLE_RESOURCE);
            ps.append(" = ?)");
            int updateAffectedRows = m_JdbcTemplate.update(ps.toString(), new Object[] { Integer.valueOf(accessRights_p), roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
            if (updateAffectedRows <= 0)
            {
                // this resource is not in the database. so we have to add it.
                ps = new StringBuffer();
                ps.append("insert into ");
                ps.append(owRoleTableName);
                ps.append(" (");
                ps.append(ROLE_NAME);
                ps.append(",");
                ps.append(CATEGORY);
                ps.append(",");
                ps.append(ROLE_RESOURCE);
                ps.append(",");
                ps.append(ROLE_ACCESS);
                ps.append(") values (?,?,?,?)");
                m_JdbcTemplate.update(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p), Integer.valueOf(accessRights_p) });
            }
        }
    }

    @Override
    protected String dynamicResourceNameFromId(java.util.Locale locale_p, String resourceId_p, int category_p)
    {
        return resourceId_p;
    }

    protected Map getPrecalculatedFullAccessMasks()
    {
        if (m_precalculatedFullAccessMasks == null)
        {
            m_precalculatedFullAccessMasks = new HashMap();
            Collection categories = getCategories();
            Iterator itCategories = categories.iterator();
            while (itCategories.hasNext())
            {
                int fullAccessMask = 0;
                int category = ((Integer) itCategories.next()).intValue();
                Map accessFlags = getAccessMaskDescriptions(category);
                Iterator itFlags = accessFlags.keySet().iterator();
                while (itFlags.hasNext())
                {
                    int flag = ((Integer) itFlags.next()).intValue();
                    fullAccessMask |= flag;
                }
                m_precalculatedFullAccessMasks.put(Integer.valueOf(category), Integer.valueOf(fullAccessMask));
            }
        }
        return (m_precalculatedFullAccessMasks);
    }

    public int getAccessMask(String roleName_p, int category_p, String resourceId_p)
    {
        if (m_schemeVersion >= 3)
        {
            return getAccessMask_SchemeVersion3(roleName_p, category_p, resourceId_p);
        }
        else
        {
            // schema < 3 has always full access
            Integer fullAccessMask = (Integer) getPrecalculatedFullAccessMasks().get(Integer.valueOf(category_p));
            return (fullAccessMask == null) ? 0 : fullAccessMask.intValue();
        }
    }

    /**
     * get the access mask for scheme version 3.
     * 
     * @param roleName_p
     * @param category_p
     * @param resourceId_p
     * @return int
     * @throws DataAccessException 
     */
    protected int getAccessMask_SchemeVersion3(String roleName_p, int category_p, String resourceId_p) throws DataAccessException
    {
        int accessMask = 0;

        // read access rights from database
        try
        {
            StringBuffer ps = new StringBuffer();
            ps.append("select ");
            ps.append(ROLE_ACCESS_MASK);
            ps.append(" from ");
            ps.append(owRoleTableName);
            ps.append(" where (");
            ps.append(ROLE_NAME);
            ps.append(" = ?) and (");
            ps.append(CATEGORY);
            ps.append(" = ?) and (");
            ps.append(ROLE_RESOURCE);
            ps.append(" = ?)");
            accessMask = m_JdbcTemplate.queryForInt(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
        }
        catch (IncorrectResultSizeDataAccessException e)
        {
            // as defined in the system design, non-present rows in the database are equal to full access
            accessMask = -1;
        }

        // reduce the access mask from the DB to the maximum access mask
        Integer fullAccessMask = (Integer) getPrecalculatedFullAccessMasks().get(Integer.valueOf(category_p));
        int fullMask = (fullAccessMask == null) ? 0 : fullAccessMask.intValue();
        accessMask = accessMask & fullMask;

        // return the access mask
        return (accessMask);
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.role.OwRoleManager#setAccessMask(java.lang.String, int, java.lang.String, int)
     */
    public void setAccessMask(String roleName_p, int category_p, String resourceId_p, int accessMask_p) throws Exception
    {
        if (m_schemeVersion >= 3)
        {
            setAccessMask_SchemeVersion3(roleName_p, category_p, resourceId_p, accessMask_p);
            // clear the entry in the isAllowedCache. We might have set the access rights for a role that will
            // affect the granted privileges of the currently logged on user
            Map effectiveAccessMaskCache = getEffectiveAccessMaskCache(category_p);
            synchronized (effectiveAccessMaskCache)
            {
                effectiveAccessMaskCache.remove(resourceId_p);
            }
        }
        else
        {
            String msg = "OwDBRoleManager.setAccessMask: The access mask can only be modified if you are using a database scheme greater or equal to version 3. Your current scheme version is " + m_schemeVersion;
            LOG.fatal(msg);
            throw new OwNotSupportedException(msg);
        }
    }

    private String decodeResourceId(int categoryId_p, String resourceId_p) throws OwException
    {
        if (!isStaticResourceCategory(categoryId_p))
        {
            if (resourceId_p != null && resourceId_p.startsWith(DYNAMIC_RESOURCE_PREFIX))
            {
                return resourceId_p.substring(DYNAMIC_RESOURCE_PREFIX.length());
            }
            else
            {
                LOG.error("OwDBRoleManager.dynamicDisplayNameFromId : invalid startup folder id (prefix missing) " + resourceId_p);
                throw new OwInvalidOperationException("Invalid startup folder id (prefix missing) " + resourceId_p);
            }
        }
        else
        {
            return resourceId_p;
        }
    }

    private String encodeResourceId(int categoryId_p, String resourceId_p)
    {
        if (!isStaticResourceCategory(categoryId_p))
        {
            if (resourceId_p != null)
            {
                return DYNAMIC_RESOURCE_PREFIX + resourceId_p;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return resourceId_p;
        }

    }

    /**
     * set the access mask for scheme version 3.
     * 
     * @param roleName_p
     * @param category_p
     * @param resourceId_p
     * @param accessMask_p
     * @throws OwException 
     * @throws DataAccessException 
     */
    protected void setAccessMask_SchemeVersion3(String roleName_p, int category_p, String resourceId_p, int accessMask_p) throws DataAccessException, OwException
    {
        StringBuffer ps = new StringBuffer();
        ps.append("update ");
        ps.append(owRoleTableName);
        ps.append(" set ");
        ps.append(ROLE_ACCESS_MASK);
        ps.append(" = ? where (");
        ps.append(ROLE_NAME);
        ps.append(" = ?) and (");
        ps.append(CATEGORY);
        ps.append(" = ?) and (");
        ps.append(ROLE_RESOURCE);
        ps.append(" = ?)");
        int updateAffectedRows = m_JdbcTemplate.update(ps.toString(), new Object[] { Integer.valueOf(accessMask_p), roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p) });
        if (updateAffectedRows <= 0)
        {
            // this resource is not in the database. so we have to add it.
            ps = new StringBuffer();
            ps.append("insert into ");
            ps.append(owRoleTableName);
            ps.append(" (");
            ps.append(ROLE_NAME);
            ps.append(",");
            ps.append(CATEGORY);
            ps.append(",");
            ps.append(ROLE_RESOURCE);
            ps.append(",");
            ps.append(ROLE_ACCESS);
            ps.append(",");
            ps.append(ROLE_ACCESS_MASK);
            ps.append(") values (?,?,?,?,?)");
            m_JdbcTemplate.update(ps.toString(), new Object[] { roleName_p, Integer.valueOf(category_p), encodeResourceId(category_p, resourceId_p), Integer.valueOf(ROLE_ACCESS_RIGHT_NOT_ALLOWED), Integer.valueOf(accessMask_p) });
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.role.OwRoleManager#canPersistAccessMask()
     */
    public boolean canPersistAccessMask()
    {
        // we need a database scheme from version 3 on to be able to persist the access mask
        return (m_schemeVersion >= 3);
    }

    /**
     * get all resources the current user has access to for a given category
     * 
     * @param category_p int category for given user
     * 
     * @return Collection of String (resource IDs)
     */
    public Collection getAllowedResources(int category_p) throws Exception
    {
        // retrieve all resources in that category
        Collection allResources = getResources(category_p);

        // list of resources the user has access to
        LinkedList allowedResources = new LinkedList();

        // iterate over all resources and filter out those, the user has no access to
        Iterator resIt = allResources.iterator();
        while (resIt.hasNext())
        {
            String resourceName = (String) resIt.next();
            if (isAllowed(category_p, resourceName))
            {
                allowedResources.add(resourceName);
            }
        }

        // return list of allowed resources
        return (allowedResources);
    }

    //  === Master role support.

    /** check if selectable roles are supported by rolemanager for the current user
     * 
     * @return boolean true = given user has roles
     * */
    public boolean hasMasterRoles()
    {
        try
        {
            return m_supportMasterRoles && (getMasterRoles().size() > 1);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** get the role that is currently selected for the user
     * 
     *  @return String role, or null if no roles can be selected
     * */
    public String getMasterRole() throws Exception
    {
        if (m_strMasterRole == null)
        {
            // === set default
            Collection masterRoles = getMasterRoles();
            if ((masterRoles != null) && (masterRoles.size() != 0))
            {
                m_strMasterRole = (String) masterRoles.iterator().next();
            }
        }
        return m_strMasterRole;
    }

    /** set the role that is currently selected for the user
     * 
     * @param strRole_p String
     * @return boolean true = role changed
     * */
    public boolean setMasterRole(String strRole_p) throws Exception
    {
        if (m_supportMasterRoles)
        {
            // change master role
            if (!strRole_p.equals(m_strMasterRole))
            {
                // check if master role is defined
                if (getMasterRoles().contains(strRole_p))
                {
                    m_strMasterRole = strRole_p;

                    // recreate filtered roles
                    m_filteredRoles = null;

                    // recreate caches
                    m_isAllowedCacheByCategories = new HashMap();
                    m_effectiveAccessMaskCacheByCategories = new HashMap();

                    // update design
                    updateDesign();

                    // update plugins
                    updateAllowedPlugins();

                    updatePlugins();

                    return true;
                }
                else
                {
                    LOG.error("OwDBRoleManager.setMasterRole: Masterrole is not defined: " + strRole_p);
                }
            }
        }

        return false;
    }

    /** get a displayname for the given role
     * 
     * @param locale_p Locale to use
     * @param strRole_p String
     * @return String displayname for given role
     * */
    public String getMasterRoleDisplayName(Locale locale_p, String strRole_p) throws Exception
    {
        if (strRole_p == null)
        {
            return "-";
        }

        return OwString.localizeLabel(locale_p, strRole_p);
    }

    /** get a list of master roles that can be selected for the current user
     * 
     * @return Collection of String, or null if no roles can be selected
     * */
    public Collection getMasterRoles() throws Exception
    {
        if (m_supportMasterRoles && (m_masterrolenames == null))
        {
            // roles for current user
            Collection userroles = getCurrentUser().getRoleNames();

            m_masterrolenames = new LinkedList();

            // === get the master role definition from bootstrap,
            // collect only those master roles
            // that the given user has roles defined for.
            List mastergroupnodes = getConfigNode().getSafeNodeList("MasterRoles");

            Iterator it = mastergroupnodes.iterator();
            while (it.hasNext())
            {
                OwXMLUtil mastergroupwrapper = new OwStandardXMLUtil((Node) it.next());

                // get the master role
                String strMasterRoleName = mastergroupwrapper.getSafeStringAttributeValue(NAME_ATTRIBUTE_NAME, null);
                if (strMasterRoleName == null)
                {
                    String msg = "OwDBRoleManager.getMasterRoles: Define name in MasterRoleGroup.";
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }

                // get the group of roles defined with the master role
                List roles = mastergroupwrapper.getSafeStringList();
                if (mastergroupwrapper.getSafeBooleanAttributeValue("memberOfAll", true))
                {
                    // assign master-role for current user only if he is member of all groups
                    if (userroles.containsAll(roles))
                    {
                        m_masterrolenames.add(strMasterRoleName);
                    }
                }
                else
                {
                    Iterator itUsrRoles = userroles.iterator();
                    while (itUsrRoles.hasNext())
                    {
                        if (roles.contains(itUsrRoles.next()))
                        {//if any matches then assign the master role
                            m_masterrolenames.add(strMasterRoleName);
                            break;
                        }
                    }
                }
            }
        }

        return m_masterrolenames;
    }

    /** get filtered roles through the selected master role group
     * 
     * 
     * @return Collection of String with filtered roles according to selected master role
     * */
    public Collection getFilteredRoles() throws Exception
    {
        if (null == m_filteredRoles)
        {
            if (hasMasterRoles())
            {
                // roles for current user
                Collection userroles = getCurrentUser().getRoleNames();

                Set allrolesdefinedbymaster = new HashSet();
                List masterroles = null;

                m_filteredRoles = new LinkedList();

                // === get the master role definition from bootstrap,
                // collect only those master roles
                // that the given user has roles defined for.
                List mastergroupnodes = getConfigNode().getSafeNodeList("MasterRoles");

                Iterator it = mastergroupnodes.iterator();
                while (it.hasNext())
                {
                    OwXMLUtil mastergroupwrapper = new OwStandardXMLUtil((Node) it.next());

                    // get the master role
                    String strMasterRoleName = mastergroupwrapper.getSafeStringAttributeValue(NAME_ATTRIBUTE_NAME, null);
                    if (strMasterRoleName == null)
                    {
                        String msg = "OwDBRoleManager.getFilteredRoles: Define name in MasterRoleGroup.";
                        LOG.fatal(msg);
                        throw new OwConfigurationException(msg);
                    }

                    // get the group of roles defined with the master role
                    List roles = mastergroupwrapper.getSafeStringList();

                    allrolesdefinedbymaster.addAll(roles);

                    if (strMasterRoleName.equals(getMasterRole()))
                    {
                        masterroles = roles;
                    }
                }

                if (masterroles == null)
                {
                    String msg = "OwDBRoleManager.getFilteredRoles: Masterrole is not defined, name = " + getMasterRole();
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }

                // get filtered roles
                Iterator itUser = userroles.iterator();
                while (itUser.hasNext())
                {
                    String strRole = (String) itUser.next();

                    if (masterroles.contains(strRole) || (!allrolesdefinedbymaster.contains(strRole)))
                    {
                        m_filteredRoles.add(strRole);
                    }
                }

            }
            else
            {
                m_filteredRoles = getCurrentUser().getRoleNames();
            }
        }

        return m_filteredRoles;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.role.OwRoleManager#isGlobalRoleModificationAllowed()
     */
    public boolean isGlobalRoleModificationAllowed()
    {
        return (m_isSecurityAdmin);
    }

    /**
     * Return configured categories, or all categories if no category was configured.
     * @return {@link Collection} a collection with configured categories.
     */
    public Collection getConfiguredCategories()
    {
        Collection configuredCategories = null;
        if (!m_hasCategoriesConfigured)
        {
            configuredCategories = super.getConfiguredCategories();
        }
        else
        {
            configuredCategories = m_configuredCategories.isEmpty() ? new LinkedList(getCategories()) : m_configuredCategories;
        }
        return configuredCategories;
    }

    @Override
    protected Collection<String> getDynamicResources(int category_p) throws OwException
    {
        if (ROLE_CATEGORY_STARTUP_FOLDER == category_p)
        {
            StringBuffer ps = new StringBuffer();

            ps.append("select  ");
            ps.append(ROLE_RESOURCE);
            ps.append(" from ");
            ps.append(owRoleTableName);
            ps.append(" where (");
            ps.append(CATEGORY);
            ps.append(" = ?)");

            List<Map<String, Object>> queryResult = m_JdbcTemplate.queryForList(ps.toString(), new Object[] { category_p });
            List<String> resources = new LinkedList<String>();

            for (Map<String, Object> queryResultEntry : queryResult)
            {
                Object resource = queryResultEntry.get(ROLE_RESOURCE);
                if (resource != null)
                {
                    String resourceId = resource.toString();
                    resources.add(decodeResourceId(category_p, resourceId));
                }
            }
            return resources;
        }
        else
        {
            LOG.error("OwDBRoleManager.getDynamicResouces :  invalid dynamic category " + category_p);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Map<String, OwString> getIntegratedApplicationsNames()
    {
        return applicationsNames;
    }

    @Override
    public OwIntegratedApplicationRoleManager createIntegratedRoleManager(String applicationId) throws OwException
    {
        OwDBRoleManager applicationRoleManager = new OwDBRoleManager(applicationId);
        applicationRoleManager.init(getContext(), getConfigNode());
        if (isInitialized())
        {
            applicationRoleManager.loginInit();
        }
        return applicationRoleManager;
    }
}