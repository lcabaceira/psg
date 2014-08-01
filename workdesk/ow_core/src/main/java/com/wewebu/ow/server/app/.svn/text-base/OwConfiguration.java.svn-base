package com.wewebu.ow.server.app;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwDataSourceException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.role.OwIntegratedApplicationRoleManager;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.role.OwRoleOptionAttributeBag;
import com.wewebu.ow.server.servlets.OwConfigurationInitialisingContextListener;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwDataSourceUtil;
import com.wewebu.ow.server.util.OwExceptionManager;
import com.wewebu.ow.server.util.OwMimeTypes;
import com.wewebu.ow.server.util.OwSimpleAttributeBagWriteable;
import com.wewebu.ow.server.util.OwStandardOptionXMLUtil;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwStringProperties;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtilOptionAndPlaceholderFilter;
import com.wewebu.ow.server.util.OwXMLUtilPlaceholderFilter;

/**
 *<p>
 * Configuration class for the global application Configuration and Plugins. <br/><br/>
 * <b>NOTE:</b> The Bootstrap Configuration and Plugin Descriptions will only be reloaded upon Application restart. 
 * Also, if several identical Applications are deployed in one application server,
 * they need their own classloader, otherwise they will share the bootstrap Configuration and plugins.
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
public class OwConfiguration extends OwBaseConfiguration implements OwAttributeBag
{
    // === version of core system
    /** minor version number of the used Interface */
    public static int INTERFACE_MINOR_VERSION = 0;
    /** major version number of the used Interface */
    public static int INTERFACE_MAJOR_VERSION = 0;

    private static final String MASTER_ROLE_APP_SETTING = "MasterRole";

    /** 
     * versioning since 4.2.0.0: MAJOR.MINOR.SERVICEPACK.HOTFIX
     * major version number of the implementation */
    public static final int IMPLEMENTATION_MAJOR_VERSION = 4;

    /** minor version number of the implementation */
    public static final int IMPLEMENTATION_MINOR_VERSION = 2;

    /** 
     * update version number of the implementation
     * @deprecated since 4.2.0.0 use IMPLEMENTATION_SERVICEPACK_VERSION instead */
    public static final int IMPLEMENTATION_UPDATE_VERSION = 0;

    /** service pack version number of the implementation 
     * @since 4.2.0.0 */
    public static final int IMPLEMENTATION_SERVICEPACK_VERSION = 0;

    /** 
     * fix pack version number of the implementation 
     * @deprecated since 4.2.0.0, use IMPLEMENTATION_HOTFIX_VERSION instead */
    public static final int IMPLEMENTATION_FIXPACK_VERSION = 0;

    /** 
     * hot fix version number of the implementation 
     * @since 4.2.0.0 */
    public static final int IMPLEMENTATION_HOTFIX_VERSION = 0;

    /** release type (rc1,rc2,alpha,beta) */
    public static final String IMPLEMENTATION_RELEASE_TYPE = "";

    /** prefix for the option id's for MIME types for the mandator selective configuration */
    public static final String MIME_TYPE_OPTION_PREFIX = "mime.";

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwConfiguration.class);

    public static final String ECM_ADAPTER_ELEMENT = "EcmAdapter";

    /**
     * The name of the properties file to lookup version information from.
     */
    private static final String WORKDESK_VERSION_PROPERTIES = "workdesk-version.properties";

    private static OwAppVersion version = null;

    /** key for storing the bootstrap configuration in the application context */
    private static final String ATT_OW_CONFIGURATION_M_BOOTSTRAP_CONFIGURATION = "OwConfiguration.m_bootstrapConfiguration";

    public static final String WORKDESK_APPLICATION_ID = "WorkdeskApplicationId";

    /** the element tag name for combobox renderer class name */
    private static final String COMBOBOX_RENDERER_CLASS_NAME_ELEMENT = "ComboboxRendererClassName";

    /** name of the properties file used to define own mimetypes, overriding the existing ones */
    public static final String MIMETYPES_CUSTOM_FILE_NAME = "mimetypes.properties";

    public static final String GLOBAL_PARAMETERS_ELEMENT = "globalParameters";

    /**
     * the bag id prefix for layout settings
     */
    private static final String LAYOUT_SETTINGS_BAG_ID_PREFIX = "layout_bag_id_";

    /* get a helper for the core system version */
    static synchronized OwAppVersion getVersions()
    {
        if (null == version)
        {
            String versionPropertiesFileName = "/" + WORKDESK_VERSION_PROPERTIES;
            URL versionPropertiesURL = OwConfiguration.class.getResource(versionPropertiesFileName);
            if (null != versionPropertiesURL)
            {
                version = OwAppVersion.fromProperties(versionPropertiesURL);
            }
            else
            {
                version = OwAppVersion.unknownVersion();
            }

        }
        return version;

    }

    /**
     * Return a string representing a version.
     * @return String
     */
    public static synchronized String getVersionString()
    {
        return getVersions().getVersionString();
    }

    /**
     * Get key string of the core system edition
     * @return String key
     */
    public static synchronized String getEditionString()
    {
        return getVersions().getEditionString();
    }

    /**
     * Get a readable string for buildNumber 
     * @return String build number id
     */
    public static synchronized String getBuildNumber()
    {
        return getVersions().getBuildNumber();
    }

    // === members    
    /** reference to the application context, used to read and write Configuration. */
    protected OwMainAppContext m_Context;

    /** bootstrap Configuration XML node, DO NEVER access directly, rather use getBootstrapConfiguration() 
     *  
     *  The bootstrapConfiguration are static, so they get only loaded once upon application server startup
     *  and not each time a user logs on.
     */
    private OwXMLUtil m_bootstrapConfiguration = null;

    /** selective ID's delegate for selective configuration via OwXMLUtilFilter */
    private OwRoleOptionAttributeBag m_roleoptionIDs = new OwRoleOptionAttributeBag();

    /** list of OwDocumentFunction plugins */
    protected List m_AllowedDocumentFunctionPluginList;
    /** backup map of OwDocumentFunction plugins */
    protected Map m_AllowedDocumentFunctionPluginMap;
    /** list OwRecordFunction plugins*/
    protected List m_AllowedRecordFunctionPluginList;
    /** backup map OwRecordFunction plugins*/
    protected Map m_AllowedRecordFunctionPluginMap;
    /** list of the main plugins OwMasterPluginInstance instances */
    protected List m_AllowedMasterPluginList;
    /** backup map OwMasterPluginInstance plugins*/
    protected Map m_AllowedMasterPluginMap;

    /** instance of the DMS adaptor */
    private OwNetwork m_theNetwork;

    /** instance of the rolemanager class */
    private OwRoleManager m_theRoleManager;

    /** instance of the historymanager class */
    private OwHistoryManager m_theHistoryManager;

    /** instance of the mandatormanager class */
    private OwMandatorManager m_theMandatorManager;

    /** instance of the changeable user and application settings. See getSettings() */
    private OwSettings m_Settings;

    /** instance to the settings set for the application. See getAppSettings() */
    private OwSettingsSet m_AppSettingsSet;

    /** the scalar settings specific for a user. See getUserAttributeBag() */
    private OwAttributeBagWriteable m_userattributebag;

    /** spring JDBC access */
    private JdbcTemplate m_jdbcTemplate;
    /**
     *  cached user operation factories
     *  @since 3.1.0.3 
     */
    private Set<OwContextBasedUOListenerFactory> listenerFactories;

    private String applicationId;

    /** Init called before login, creates a new instance of OwConfiguration 
     * 
     * optionally set a prefix to distinguish several different applications.
     * The rolemanager will filter the allowed plugins, MIME-settings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     * 
     * @param context_p OwMainAppContext
     * */
    public void init(OwMainAppContext context_p) throws Exception
    {
        m_Context = context_p;

        // get bootstrap from application context
        m_bootstrapConfiguration = (OwXMLUtil) context_p.getApplicationAttribute(ATT_OW_CONFIGURATION_M_BOOTSTRAP_CONFIGURATION);

        if (m_bootstrapConfiguration == null)
        {
            String msg = "OwConfiguration.init: Error, the bootstrap configuration XML node is null. Possible cause: the 'owbootstrap.xml' file is missing or has an invalid XML format.";
            LOG.fatal(msg);
            throw new OwConfigurationException(msg + " Please check your log files for further informations...)");
        }

        // wrap bootstrap for placeholder and selective configuration
        m_bootstrapConfiguration = new OwXMLUtilOptionAndPlaceholderFilter(m_bootstrapConfiguration, this, m_roleoptionIDs);

        // spring JDBC access
        m_jdbcTemplate = (JdbcTemplate) context_p.getApplicationAttribute("OwConfiguration.m_jdbcTemplate");
        if (m_jdbcTemplate == null)
        {
            LOG.warn("OwConfiguration.init: Warning, the spring JDBC access Template (m_jdbcTemplate) is null.");
        }

        applicationId = context_p.getInitParameter(WORKDESK_APPLICATION_ID);

        createMandatorManager();

        createHistoryManager();

        createNetworkAdapter();

        getHistoryManager().setNetwork(getNetwork());

        getNetwork().setEventManager(getHistoryManager());

        createRoleManager();

        getNetwork().setRoleManager(getRoleManager());
    }

    /**
     * Initialize JdbcTempalte by searching for DataSource using JNDI lookup,
     * wrapping it by a JdbcTemplate instance and registering it to the OwBaseInitializer.
     * @param initializer_p OwBaseInitializer
     * @param taskDataSourceJNDI_p String
     * @throws Exception mainly OwDataSourceException if DataSource not available by provided name
     */
    private static JdbcTemplate createJdbcDataSource(OwBaseInitializer initializer_p, String taskDataSourceJNDI_p) throws Exception
    {
        DataSource ds = OwDataSourceUtil.retrieveDataSource(taskDataSourceJNDI_p);

        if (ds == null)
        {
            String msg = "OwConfiguration.createJdbcDataSource: No DataSource available for provided name/id";
            LOG.fatal(msg);
            throw new OwDataSourceException(msg);
        }

        LOG.debug("OwConfiguration.createJdbcDataSource: Initializing datasource successfully.");
        JdbcTemplate template = OwDataSourceUtil.createJdbcTemplate(ds);
        // set in application context
        initializer_p.setApplicationAttribute("OwConfiguration.m_jdbcTemplate", template);

        LOG.debug("OwConfiguration.createJdbcDataSource: Initializing Spring Jdbc template successfully done.");

        return template;
    }

    /** get a spring JDBC template for the default DataSource
     * 
     * @return a {@link JdbcTemplate}
     */
    public JdbcTemplate getJDBCTemplate()
    {
        return m_jdbcTemplate;
    }

    /** 
     * Init application config data upon startup, 
     * make sure the servlet listener {@link OwConfigurationInitialisingContextListener} is configured for your application.
     * 
     * @param initializer_p
     */
    public static void applicationInitalize(OwBaseInitializer initializer_p) throws OwConfigurationException
    {
        try
        {
            LOG.debug("OwConfiguration.applicationInitalize: start initializing the application logger...");
            // === init logger
            URL log4jurl = initializer_p.getConfigURL("log4j.properties");
            if (log4jurl != null && new java.io.File(log4jurl.getPath()).exists())
            {
                org.apache.log4j.PropertyConfigurator.configure(log4jurl);
            }
            else
            {
                log4jurl = initializer_p.getConfigURL("log4j.xml");
                if (log4jurl != null && new java.io.File(log4jurl.getPath()).exists())
                {
                    org.apache.log4j.xml.DOMConfigurator.configure(log4jurl);
                }
            }

            if (log4jurl == null)
            {
                LOG.warn("OwConfiguration.applicationInitalize: can not found the logger initialization files: 'log4j.properties' or 'log4j.xml'...");
            }
            else
            {
                LOG.debug("OwConfiguration.applicationInitalize: ...application logger successfully configured from: " + log4jurl.toString());
            }

            LOG.debug("OwConfiguration.applicationInitalize: start reading the bootstrap configuration data...");

            // initialize role config data upon application start
            OwXMLUtil bootConfig = createBootstrap(initializer_p);

            // init spring JDBC template
            Node dataSourceNode = bootConfig.getSubNode("DefaultDataSource");
            if (dataSourceNode == null)
            {
                LOG.debug("OwConfiguration.applicationInitalize: no DataSource is specified in the owbootstrap.xml (<DefaultDataSource>)."
                        + " The application will run without additional DB functionality: do not use the DBHistoryManager and OwDBRoleManager, use instead the OwSimpleHistoryManager and OwSimpleRoleManager.");
            }
            else
            {
                OwXMLUtil datasourceconfig = new OwStandardXMLUtil(dataSourceNode);

                String taskDataSourceJNDI = datasourceconfig.getSafeTextValue("JNDIName", null);

                if (null != taskDataSourceJNDI)
                {
                    createJdbcDataSource(initializer_p, taskDataSourceJNDI);
                }
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwConfiguration.applicationInitalize: DB <DefaultDataSource> of owbootstrap.xml, JNDIName = " + taskDataSourceJNDI);
                }
            }

            LOG.debug("OwConfiguration.applicationInitalize: ...reading the bootstrap configuration data finished.");

        }
        catch (Exception e)
        {
            String msg = "Application initializing error: Exception reading the bootstrap configuration data.";
            LOG.error(msg, e);
            throw new OwConfigurationException(msg, e);
        }

        try
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwConfiguration.applicationInitalize: check if customized '" + OwConfiguration.MIMETYPES_CUSTOM_FILE_NAME + "' file should be loaded.");
            }
            // create MIME type properties
            URL mimetypesPropertiesURL = initializer_p.getConfigURL(OwConfiguration.MIMETYPES_CUSTOM_FILE_NAME);
            OwMimeTypes.loadFromUrl(mimetypesPropertiesURL);
        }
        catch (Exception e)
        {
            String msg = "Application initializing error: Exception reading the '" + OwConfiguration.MIMETYPES_CUSTOM_FILE_NAME + "' file.";
            LOG.error(msg, e);
            throw new OwConfigurationException(msg, e);
        }
    }

    /** init called AFTER the user has logged in.
     *
     *  NOTE: This function is called only once after login to do special initialization, 
     *        which can only be performed with valid credentials.
     */
    public void loginInit() throws Exception
    {
        // call loginInit in rolemanager to
        try
        {
            m_theRoleManager.loginInit();
        }
        catch (Exception e)
        {
            LOG.error("The Role Manager could not be initialized after login, RoleManager = " + m_theRoleManager.getClass().toString(), e);
            throw new OwConfigurationException(m_Context.localize("app.OwConfiguration.rolemanageriniterror", "Role Manager could not be initialized after login:") + " " + m_theRoleManager.getClass().toString() + " - " + e.getLocalizedMessage(), e);
        }

        // === create the settings object
        m_Settings = new OwSettings(m_Context);

        // === set persistent role
        if (m_theRoleManager.hasMasterRoles())
        {
            String strMasterRole = m_Context.getSafeStringAppSetting(MASTER_ROLE_APP_SETTING, null);
            if (strMasterRole != null)
            {
                if (m_theRoleManager.setMasterRole(strMasterRole))
                {
                    // update settings
                    updateSettings();
                }
            }
            else
            {
                //check if workdesk plugin is available

                List appPlugs = getAllowedPlugins(PLUGINTYPE_APPLICATION);
                if (appPlugs == null)
                {
                    LOG.error("OwConfiguration.loginInit: The Workdesk plugin is not allowed for one of the roles specified in master role " + m_theRoleManager.getMasterRole());
                    throw new OwConfigurationException(m_Context.localize1("app.OwConfiguration.masterroleconfigerror",
                            "The Workdesk plugin is not available for one of the roles specified in master role %1. Please enable the Workdesk plugin for the roles from master role %1.", m_theRoleManager.getMasterRole()));
                }
            }
        }

        // === after rolemanager is initialized, we can create the selective configuration
        m_roleoptionIDs.setIDs(getRoleManager().getAllowedResources(OwRoleManager.ROLE_CATEGORY_SELECTIVE_CONFIGURATION));

        // === after rolemanager and settings and the selective configuration is initialized, we can finally create the plugins
        createPlugins();
    }

    private void updateSettings() throws Exception
    {
        m_Settings.refresh();
        // force app settings reload
        m_AppSettingsSet = null;
    }

    /** set the new master role without logout
     * 
     * @return boolean true = role changed, false = role did not change
     * */
    public boolean setMasterRole(String strRole_p) throws Exception
    {
        // === set new role
        if (getRoleManager().setMasterRole(strRole_p))
        {
            // === re-initialize 
            m_mimenodemap.clear();
            m_roleoptionIDs = new OwRoleOptionAttributeBag();

            // the settings first
            updateSettings();

            // notify subscribed clients
            m_Context.getConfigChangeEvent().onConfigChangeEventUpdateRoles();

            // then plugins (design is done in rolemanager already)
            updatePlugins();

            m_roleoptionIDs.setIDs(getRoleManager().getAllowedResources(OwRoleManager.ROLE_CATEGORY_SELECTIVE_CONFIGURATION));

            return true;
        }
        else
        {
            return false;
        }
    }

    /** get the application wide scalar settings specific for the current user.
     * 
     *  Unlike the OwSettings the AttributeBag allows simple scalar values
     *  but with high performance.
     *  
     *  NOTE:
     *  The OwSettings are used for complex settings for the plugins which are manually changed from time to time
     *  by the user or the Administrator.
     *  	==> Performance is negligible, but values are highly structured.
     *  	==> OwSettings are usually based on a XML document. (depends on adapter implementation)
     *  
     *  The OwAttributeBagWriteable is used by the application itself to persist state.
     *  	==> Structure is negligible, but performance is important
     *  	==> OwAttributeBagWriteable is usually based on a DB table. (depends on adapter implementation)
     *  
     * @return OwAttributeBagWriteable to read and write scalars
     * @throws Exception 
     */
    public OwAttributeBagWriteable getUserAttributeBag() throws Exception
    {
        if (null == m_userattributebag)
        {
            try
            {
                m_userattributebag = (OwAttributeBagWriteable) getNetwork().getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, "OwApplicationSite", true, false);
            }
            catch (OwObjectNotFoundException e)
            {
                // no persistent attribute bag available, at least return a dummy attribute bag so application can work
                m_userattributebag = new OwSimpleAttributeBagWriteable();
                LOG.warn("No persistent attribute bag available, a dummy attribute bag is set so that the application is runnable...", e);
            }
        }

        return m_userattributebag;
    }

    /** retrieve the settings object for dynamic complex plugin settings
     * 
     *  NOTE:
     *  The OwSettings are used for complex settings for the plugins which are manually changed from time to time
     *  by the user or the Administrator.
     *  	==> Performance is negligible, but values are highly structured.
     *  	==> OwSettings are usually based on a XML document. (depends on adapter implementation)
     *  
     *  The OwAttributeBagWriteable is used by the application itself to persist state.
     *  	==> Structure is negligible, but performance is important
     *  	==> OwAttributeBagWriteable is usually based on a DB table. (depends on adapter implementation)
     * 
     * @return OwSettings
     */
    public OwSettings getSettings()
    {
        return m_Settings;
    }

    /** Retrieve the App Settings-Set for dynamic complex application settings
     * 
     *  NOTE:
     *  The OwSettings are used for complex settings for the plugins which are manually changed from time to time
     *  by the user or the Administrator.
     *  	==> Performance is negligible, but values are highly structured.
     *  	==> OwSettings are usually based on a XML document. (depends on adapter implementation)
     *  
     *  The OwAttributeBagWriteable is used by the application itself to persist state.
     *  	==> Structure is negligible, but performance is important
     *  	==> OwAttributeBagWriteable is usually based on a DB table. (depends on adapter implementation)
     * 
     * @return OwSettingsSet
     * @throws Exception
     */
    public OwSettingsSet getAppSettings() throws Exception
    {
        if (m_AppSettingsSet == null)
        {
            List appPlugs = getAllowedPlugins(PLUGINTYPE_APPLICATION);

            // get first available section
            if ((appPlugs != null) && (appPlugs.size() > 0))
            {
                if (appPlugs.size() > 1)
                {
                    LOG.warn("OwConfiguration.getAppSettings: Role has more than one workdesk sections defined. Please check roles.");
                }

                OwXMLUtil NodeWrapper = (OwXMLUtil) appPlugs.get(0);

                // get the allowed workdesk section
                m_AppSettingsSet = m_Settings.getSettingsInfo(NodeWrapper.getSafeTextValue(PLUGIN_NODE_ID, "null"));
            }
        }

        return m_AppSettingsSet;
    }

    /** retrieve the network object */
    public OwNetwork getNetwork()
    {
        return m_theNetwork;
    }

    /** retrieve the MandatorManager object */
    public OwMandatorManager getMandatorManager()
    {
        return m_theMandatorManager;
    }

    /** retrieve the RoleManager object */
    public OwRoleManager getRoleManager()
    {
        return m_theRoleManager;
    }

    /** retrieve the HistoryManager object */
    public OwHistoryManager getHistoryManager()
    {
        return m_theHistoryManager;
    }

    /** create a single network adapter instance for the user session */
    private void createNetworkAdapter() throws Exception
    {
        // === create single instance for network object (DMS adaptor)
        String strNetworkClassName = "";
        try
        {
            OwXMLUtil NetworkConfigurationNode = getNetworkAdaptorConfiguration();
            strNetworkClassName = NetworkConfigurationNode.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);
            Class NetworkClass = Class.forName(strNetworkClassName);
            m_theNetwork = (OwNetwork) NetworkClass.newInstance();
            m_theNetwork.init(m_Context, NetworkConfigurationNode);
        }
        catch (Exception e)
        {
            LOG.error("The ECM Adapter could not be loaded, NetworkClassName = " + strNetworkClassName, e);
            throw new OwConfigurationException(m_Context.localize("app.OwConfiguration.networkloaderror", "ECM adapter could not be loaded:") + " " + strNetworkClassName + " - " + e.getLocalizedMessage(), e);
        }
    }

    /** create a single history manager instance for the user session */
    private void createHistoryManager() throws Exception
    {
        // === create single instance for role manager class
        String strHistoryManagerClassName = "";
        try
        {
            OwXMLUtil ConfigurationNode = getHistoryManagerConfiguration();
            strHistoryManagerClassName = ConfigurationNode.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);
            Class HistoryManagerClass = Class.forName(strHistoryManagerClassName);
            m_theHistoryManager = (OwHistoryManager) HistoryManagerClass.newInstance();
            m_theHistoryManager.init(m_Context, ConfigurationNode);
        }
        catch (Exception e)
        {
            LOG.error("Audit trail manager could not be loaded, HistoryManagerClassName = " + strHistoryManagerClassName, e);
            throw new OwConfigurationException(m_Context.localize("app.OwConfiguration.HistoryManagermanagerloaderror", "History Manager could not be loaded:") + " " + strHistoryManagerClassName + " - " + e.getLocalizedMessage(), e);
        }
    }

    /** create a single mandator manager instance for the user session */
    private void createMandatorManager() throws Exception
    {
        // === create single instance for role manager class
        String strMandatorManagerClassName = "";
        try
        {
            OwXMLUtil ConfigurationNode = getMandatorManagerConfiguration();
            strMandatorManagerClassName = ConfigurationNode.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);
            Class MandatorManagerClass = Class.forName(strMandatorManagerClassName);
            m_theMandatorManager = (OwMandatorManager) MandatorManagerClass.newInstance();
            m_theMandatorManager.init(m_Context, ConfigurationNode);
        }
        catch (Exception e)
        {
            LOG.error("Mandator Manager could not be loaded, MandatorManagerClassName = " + strMandatorManagerClassName, e);
            throw new OwConfigurationException(m_Context.localize("app.OwConfiguration.MandatorManagerloaderror", "Tenant Manager could not be loaded:") + " " + strMandatorManagerClassName + " - " + e.getLocalizedMessage(), e);
        }
    }

    /** create a single role manager instance for the user session 
     * 
     * optionally set a prefix to distinguish several different applications.
     * The rolemanager will filter the allowed plugins, MIME-settings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     * 
     */
    private void createRoleManager() throws Exception
    {
        // === create single instance for role manager class
        String strRoleManagerClassName = "";
        try
        {
            OwXMLUtil ConfigurationNode = getRoleManagerConfiguration();
            strRoleManagerClassName = ConfigurationNode.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);
            Class roleManagerClass = Class.forName(strRoleManagerClassName);
            try
            {
                m_theRoleManager = (OwRoleManager) roleManagerClass.newInstance();
            }
            catch (Exception e)
            {
                LOG.error("The Role Manager could not be loaded (can not be instantiated), RoleManagerClassName = " + strRoleManagerClassName, e);
                throw new OwConfigurationException(m_Context.localize("app.OwConfiguration.rolemanagerloaderror", "Role Manager could not be loaded:") + " " + strRoleManagerClassName + " - " + e.getLocalizedMessage(), e);

            }
            m_theRoleManager.init(m_Context.getRegisteredInterface(OwRoleManagerContext.class), ConfigurationNode);

            if (applicationId != null && m_theRoleManager instanceof OwIntegratedApplicationRoleManager)
            {
                OwIntegratedApplicationRoleManager integratedManager = (OwIntegratedApplicationRoleManager) m_theRoleManager;
                m_theRoleManager = integratedManager.createIntegratedRoleManager(applicationId);
            }
        }
        catch (OwConfigurationException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("The Role Manager could not be loaded, RoleManagerClassName = " + strRoleManagerClassName, e);
            throw new OwConfigurationException(m_Context.localize("app.OwConfiguration.rolemanagerloaderror", "Role Manager could not be loaded:") + " " + strRoleManagerClassName + " - " + e.getLocalizedMessage(), e);
        }
    }

    /** create a new instance of the field manager */
    public OwFieldManager createFieldManager() throws Exception
    {
        // === create single instance for property manager class
        String strFieldManagerClassName = "";
        try
        {

            OwXMLUtil ConfigurationNode = getFieldManagerConfiguration();
            strFieldManagerClassName = ConfigurationNode.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);
            Class FieldManagerClass = Class.forName(strFieldManagerClassName);
            OwFieldManager fieldManager = (OwFieldManager) FieldManagerClass.newInstance();

            // init with config node
            fieldManager.init(ConfigurationNode);
            // attach as event target
            fieldManager.attach(m_Context, null);

            return fieldManager;
        }
        catch (Exception e)
        {
            LOG.error("The Attribute Field Manager could not be loaded. FieldManagerClassName = " + strFieldManagerClassName, e);
            throw new OwConfigurationException(m_Context.localize("app.OwConfiguration.propertyfieldmanagerloaderror", "Attribute Field Manager could not be loaded:") + " " + strFieldManagerClassName + " - " + e.getMessage(), e);
        }
    }

    /** update the plugins after the role has changed 
     * called after plugins have been created with createPlugins(); 
     * */
    public void updatePlugins() throws Exception
    {
        m_AllowedMasterPluginList = null;
        m_AllowedMasterPluginMap = null;

        m_AllowedDocumentFunctionPluginList = null;
        m_AllowedDocumentFunctionPluginMap = null;

        m_AllowedRecordFunctionPluginList = null;
        m_AllowedRecordFunctionPluginMap = null;

        createPlugins();
    }

    /** create all plugins
     *  NOTE: Must not be called before login
     */
    private void createPlugins() throws Exception
    {
        // === create the master plugins
        if (m_AllowedMasterPluginList == null)
        {
            m_AllowedMasterPluginList = new ArrayList();
            m_AllowedMasterPluginMap = new HashMap();

            List myPlugs = getAllowedPlugins(OwBaseConfiguration.PLUGINTYPE_MASTER);
            if (myPlugs != null)
            {
                for (int i = 0; i < myPlugs.size(); i++)
                {
                    OwXMLUtil NodeWrapper = (OwXMLUtil) myPlugs.get(i);

                    // create master plugin
                    OwMasterPluginInstance mplug = createMasterPlugin(NodeWrapper);
                    m_AllowedMasterPluginList.add(mplug);
                    m_AllowedMasterPluginMap.put(mplug.getPluginID(), mplug);
                }
            }
        }

        // === create the document function plugins
        if (m_AllowedDocumentFunctionPluginList == null)
        {
            m_AllowedDocumentFunctionPluginList = new ArrayList();
            m_AllowedDocumentFunctionPluginMap = new HashMap();
            m_iDocumentFunctionRequirementFlags = loadFunctionPlugins(PLUGINTYPE_DOCUMENT_FUNCTION, m_AllowedDocumentFunctionPluginList, m_AllowedDocumentFunctionPluginMap);
        }

        // === create the record function plugins
        if (m_AllowedRecordFunctionPluginList == null)
        {
            m_AllowedRecordFunctionPluginList = new ArrayList();
            m_AllowedRecordFunctionPluginMap = new HashMap();
            loadFunctionPlugins(PLUGINTYPE_RECORD_FUNCTION, m_AllowedRecordFunctionPluginList, m_AllowedRecordFunctionPluginMap);
        }
    }

    /** ID extension for the document of a main plugin to be added to the plugin ID */
    public static final String MAIN_PLUGIN_DOCUMENT_ID_EXTENSION = ".Doc";
    /** ID extension for the document of a main plugin to be added to the plugin ID */
    public static final String MAIN_PLUGIN_VIEW_ID_EXTENSION = ".View";

    /** plugin instance used as return value of createMasterPlugin function */
    public class OwMasterPluginInstance implements OwPlugin
    {
        /** signal an error occurred when loading / initializing the plugin
         */
        public OwMasterPluginInstance(Exception e_p, OwXMLUtil configNode_p)
        {
            m_view = getErrorView(e_p, getLocalizedPluginTitle(configNode_p));
            m_configNode = configNode_p;
        }

        /** construct a plugin instance
         */
        public OwMasterPluginInstance(OwMasterView view_p, OwMasterDocument doc_p, OwXMLUtil configNode_p) throws Exception
        {
            m_view = view_p;
            m_configNode = configNode_p;

            // set the plugin node
            doc_p.setPlugin(this);

            // detach in case it was attached before role change
            m_Context.removeTarget(getDocID());

            // set the context
            doc_p.attach(m_Context, getDocID());

            // set the plugin node
            if (view_p != null)
            {
                // set document
                view_p.setDocument(doc_p);
            }
        }

        /** get the view instance */
        public OwView getView()
        {
            return m_view;
        }

        /** get the document instance */
        public OwMasterDocument getDocument()
        {
            return (OwMasterDocument) m_Context.getEventTarget(getDocID());
        }

        /** get the title of the plugin */
        public String getPluginTitle()
        {
            return getLocalizedPluginTitle(m_configNode);
        }

        /** get the plugin ID */
        public String getPluginID()
        {
            return m_configNode.getSafeTextValue(PLUGIN_NODE_ID, null);
        }

        /** get the view target ID */
        public String getViewID()
        {
            if (getPluginID() == null)
            {
                return null;
            }
            else
            {
                return getPluginID() + MAIN_PLUGIN_VIEW_ID_EXTENSION;
            }
        }

        /** get the document target ID */
        public String getDocID()
        {
            if (getPluginID() == null)
            {
                return null;
            }
            else
            {
                return getPluginID() + MAIN_PLUGIN_DOCUMENT_ID_EXTENSION;
            }
        }

        /** get a error view if plugin addView failed to display instead of the plugin view */
        public OwView getErrorView(Exception e_p, String strTitle_p)
        {
            return new OwErrorView(e_p, strTitle_p);
        }

        /** get the plugin document class name */
        public String getPluginClassName()
        {
            return m_configNode.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);
        }

        /** get a display name for the plugin type
         * @return String
         */
        public String getPluginTypeDisplayName()
        {
            return OwBaseConfiguration.getPluginTypeDisplayName(getPluginType(), m_Context.getLocale());
        }

        /** get the plugin config node
         * @return OwXMLUtil
         */
        public OwXMLUtil getConfigNode()
        {
            return m_configNode;
        }

        /** get the plugin type
         * @return String as defined in OwConfiguration.PLUGINTYPE_...
         */
        public String getPluginType()
        {
            return PLUGINTYPE_MASTER;
        }

        /** get the icon URL for this plugin to be displayed
         *
         *  @return String icon URL, or null if not defined
         */
        public String getIcon() throws Exception
        {
            return m_Context.getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/masterplugin.png");
        }

        private OwXMLUtil m_configNode;
        private OwView m_view;
    }

    /** simple message view class to display a exception when main plugin load failed
    */
    protected class OwErrorView extends OwView
    {
        /** exception to display */
        private Exception m_exception;

        /**
         * Returns the exception
         * 
         * @return the exception
         * 
         * @since 3.2.0.0
         */
        public Exception getException()
        {
            return m_exception;
        }

        /**
         * Constructor 
         * 
         * @param e_p Exception 
         * @param strTitle_p Title of the view
         */
        public OwErrorView(Exception e_p, String strTitle_p)
        {
            m_exception = e_p;
            m_strTitle = strTitle_p;
        }

        /** get the icon URL for this plugin to be displayed
        *
        *  @return String icon URL, or null if not defined
        */
        public String getIcon() throws Exception
        {
            return m_Context.getDesignURL() + "/images/masterplugin_error.png";
        }

        private String m_strTitle;

        public String getTitle()
        {
            return m_strTitle;
        }

        /** called when the view should create its HTML content to be displayed
         * @param w_p Writer object to write HTML to
         */
        protected void onRender(Writer w_p) throws Exception
        {
            OwExceptionManager.PrintCatchedException(m_Context.getLocale(), m_exception, new PrintWriter(w_p), "OwErrorStack");
        }
    }

    /** create a single main plugin of the given plugin node wrapper
     * @param strDocClassName 
     *          Name of the document class for doc-view-model
     * @param strViewClassName
     *          Name of the view class for doc-view-model
     * @param pluginNodeWrapper_p
     *          OwXMLUtil containing the plugin configuration node from owplugins.xml
     *          
     * @throws Exception if master plugin can't be instantiated
     * 
     * @return OwMasterPluginInstance with instantiated plugin view
     * @since 3.2.0.0
     */
    protected OwMasterPluginInstance createMasterPlugin(String strDocClassName, String strViewClassName, OwXMLUtil pluginNodeWrapper_p) throws Exception
    {
        // note: worker functionality extracted because original function needs to be overwritten with ZIDI!

        // === create document
        Class DocumentClass = Class.forName(strDocClassName);
        OwMasterDocument newDoc = (OwMasterDocument) DocumentClass.newInstance();

        // === create view
        if (strViewClassName != null)
        {
            Class ViewClass = Class.forName(strViewClassName);
            OwMasterView newView = (OwMasterView) ViewClass.newInstance();

            // return master plugin with view instance
            return new OwMasterPluginInstance(newView, newDoc, pluginNodeWrapper_p);
        }

        // no view defined, just a master listener
        // return master plugin instance
        return new OwMasterPluginInstance((OwMasterView) null, newDoc, pluginNodeWrapper_p);
    }

    /** create a single main plugin of the given plugin node wrapper
     * @param pluginNodeWrapper_p
     * @return OwMasterPluginInstance with instantiated plugin view
     */
    protected OwMasterPluginInstance createMasterPlugin(OwXMLUtil pluginNodeWrapper_p) throws Exception
    {
        // === create plugin from DOM Node
        // get document class name
        String strDocClassName = pluginNodeWrapper_p.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);
        // get document class name
        String strViewClassName = pluginNodeWrapper_p.getSafeTextValue(PLUGIN_NODE_VIEW_CLASSNAME, null);

        try
        {
            return createMasterPlugin(strDocClassName, strViewClassName, pluginNodeWrapper_p);
        }
        catch (Exception e)
        {
            // display a window with the error
            // return error view instance
            LOG.error("Masterplugin can not be instantiated, DocClassName = " + strDocClassName + " , ViewClassName = " + strViewClassName, e);
            return new OwMasterPluginInstance(e, pluginNodeWrapper_p);
        }
    }

    /** flag indicating that at least one plugin needs a context menu, see getPluginRequirementFlags() */
    public static final int PLUGINS_REQUIRE_CONTEXT_MENU = 0x0001;
    /** flag indicating that at least one plugin can handle multiselection, see getPluginRequirementFlags() */
    public static final int PLUGINS_REQUIRE_CAN_MULTISELECT = 0x0002;

    /** the plugin requirement flag for document functions, flags as defined with PLUGINS_REQUIRE_... */
    private int m_iDocumentFunctionRequirementFlags;

    /** check the plugin requirement flag for document functions
     *  
     * @param iRequirement_p int flag as defined with PLUGINS_REQUIRE_...
     * @return true = if required by at least one plugin
     * 
     */
    public boolean isDocmentFunctionRequirement(int iRequirement_p)
    {
        return (iRequirement_p & m_iDocumentFunctionRequirementFlags) == iRequirement_p;
    }

    /** load function plugins plugins 
     *  retrieves list of instantiated and initialized OwFunction function plugins filtered for the current user
     *  
     * @param strPluginType_p String Plugin type as defined in OwConfiguration
     *
     * @return boolean true = one or more plugins require a context menu, false no plugins require a context menu
     */
    private int loadFunctionPlugins(String strPluginType_p, List functionList_p, Map backupMap_p) throws Exception
    {
        // === get the document function plugins
        // get plugins from Configuration
        List FunctionPlugs = getAllowedPlugins(strPluginType_p);

        int iDocumentFunctionRequirementFlags = 0;

        // iterate over plugins and create a static instance if not already done
        if (FunctionPlugs != null)
        {
            for (int p = 0; p < FunctionPlugs.size(); p++)
            {
                // === create node wrapper to access the values
                OwXMLUtil NodeWrapper = (OwXMLUtil) FunctionPlugs.get(p);

                // get plugin function class from node
                String strPluginClass = NodeWrapper.getSafeTextValue(PLUGIN_NODE_CLASSNAME, null);

                String strID = NodeWrapper.getSafeTextValue(PLUGIN_NODE_ID, null);

                try
                {
                    // === create plugin from DOM Node
                    // create plugin class
                    Class PluginClass = Class.forName(strPluginClass);
                    OwFunction newFunctionPlugIn = (OwFunction) PluginClass.newInstance();

                    // init plugin
                    newFunctionPlugIn.init(NodeWrapper, m_Context);

                    // add instance to static list so we don't need to recreate them
                    functionList_p.add(newFunctionPlugIn);

                    // add to backup map as well for fast access
                    backupMap_p.put(strID, newFunctionPlugIn);

                    // === determine the requirements for the plugins, this helps later when building the UI 
                    if (newFunctionPlugIn.getContextMenu())
                    {
                        iDocumentFunctionRequirementFlags |= PLUGINS_REQUIRE_CONTEXT_MENU;
                    }

                    try
                    {
                        if (((OwDocumentFunction) newFunctionPlugIn).getMultiselect())
                        {
                            iDocumentFunctionRequirementFlags |= PLUGINS_REQUIRE_CAN_MULTISELECT;
                        }
                    }
                    catch (ClassCastException e)
                    { /* ignore */
                    }
                }
                catch (Exception e)
                {
                    // just set a warning when plugin load failed, we still keep continue working at least with the remaining plugins
                    String msg = "Exception loading the function plugin with the plugin Id = " + strID + ", PluginClass = " + strPluginClass;
                    LOG.error(msg, e);
                    //throw new OwConfigurationException(msg, e);
                }
            }
        }

        return iDocumentFunctionRequirementFlags;
    }

    /** get a list of OwMasterPluginInstance
     * @param fExcludeListeners_p boolean true = do only return master plugins with views, false = return all plugins
     * @return List of OwMasterPluginInstance
     */
    public List getMasterPlugins(boolean fExcludeListeners_p)
    {
        if (fExcludeListeners_p)
        {
            List retList = new ArrayList();
            Iterator it = m_AllowedMasterPluginList.iterator();
            while (it.hasNext())
            {
                OwMasterPluginInstance masterPlug = (OwMasterPluginInstance) it.next();
                if (masterPlug.m_view != null)
                {
                    retList.add(masterPlug);
                }
            }

            return retList;
        }
        else
        {
            return m_AllowedMasterPluginList;
        }
    }

    /** get document function plugins plugins 
     * @return List of OwDocumentFunction instances
     */
    public List getDocumentFunctionPlugins()
    {
        return m_AllowedDocumentFunctionPluginList;
    }

    /** get a single document function by ID 
     *
     * @param strID_p String ID of function as defined in plugin descriptor
     * @return OwDocumentFunction
     */
    public OwDocumentFunction getDocumentFunction(String strID_p) throws Exception
    {
        OwDocumentFunction docFunction = (OwDocumentFunction) m_AllowedDocumentFunctionPluginMap.get(strID_p);
        if (null == docFunction)
        {
            String msg = "OwConfiguration.getDocumentFunction: Plugin 'access denied' or 'not allowed' in this context, document function plugin with Id = " + strID_p;
            LOG.debug(msg);
            throw new OwAccessDeniedException(msg);
        }
        return docFunction;
    }

    /** check if document function with given ID is allowed
     * @param strID_p String ID of function as defined in plugin descriptor
     * @return true = if document function is allowed
     */
    public boolean isDocumentFunctionAllowed(String strID_p)
    {
        return m_AllowedDocumentFunctionPluginMap.containsKey(strID_p);
    }

    /** get record function plugins plugins 
     * @return List of OwRecordFunction instances
     */
    public List getRecordFunctionPlugins()
    {
        return m_AllowedRecordFunctionPluginList;
    }

    /** get a single record function by ID 
     *
     * @param strID_p String ID of function as defined in plugin descriptor
     * @return OwDocumentFunction
     */
    public OwRecordFunction getRecordFunction(String strID_p) throws Exception
    {
        OwRecordFunction recFunction = (OwRecordFunction) m_AllowedRecordFunctionPluginMap.get(strID_p);
        if (null == recFunction)
        {
            String msg = "OwConfiguration.getRecordFunction: Access denied, getting the record function plugin with Id = " + strID_p;
            LOG.debug(msg);
            throw new OwAccessDeniedException(msg);
        }
        return recFunction;
    }

    /** check if record function with given ID is allowed
     * 
     * @param strID_p String ID of function as defined in plugin descriptor
     * @return true = if record function is allowed
     */
    public boolean isRecordFunctionAllowed(String strID_p)
    {
        return m_AllowedRecordFunctionPluginMap.containsKey(strID_p);
    }

    /** get a single record function by ID 
    *
    * @param strID_p String ID of function as defined in plugin descriptor
    * @return OwDocumentFunction
    */
    public OwMasterPluginInstance getMasterPlugin(String strID_p) throws Exception
    {
        OwMasterPluginInstance masterplug = (OwMasterPluginInstance) m_AllowedMasterPluginMap.get(strID_p);
        if (null == masterplug)
        {

            String msg = "OwConfiguration.getMasterPlugin: Access denied, getting the master plugin with Id = " + strID_p;
            LOG.debug(msg);
            throw new OwAccessDeniedException(msg);
        }
        return masterplug;
    }

    /** check if master plugin with given ID is allowed
      * @param strID_p
      * @return a boolean
      */
    public boolean isMasterPluginAllowed(String strID_p)
    {
        return m_AllowedMasterPluginMap.containsKey(strID_p);
    }

    /** get a plugin instance for the given ID 
     * 
     * @param strID_p String plugin ID 
     * @return OwPlugin instance
     * @throws Exception, OwAccessDeniedException
     */
    public OwPlugin getAllowedPluginInstance(String strID_p) throws Exception
    {
        if (isRecordFunctionAllowed(strID_p))
        {
            return getRecordFunction(strID_p);
        }
        else if (isDocumentFunctionAllowed(strID_p))
        {
            return getDocumentFunction(strID_p);
        }
        else if (isMasterPluginAllowed(strID_p))
        {
            return getMasterPlugin(strID_p);
        }
        String msg = "OwConfiguration.getAllowedPluginInstance: No allowed plugin instance found for the plugin with ID = " + strID_p;
        LOG.debug(msg);
        throw new OwObjectNotFoundException(msg);
    }

    /**
     * 
     * @param initializer_p
     * @return a bootstrap XMUL utility for the given initialize (retrieved from its application attributes)
     * @since 4.0.0.0
     */
    public static OwXMLUtil getBootstrap(OwBaseInitializer initializer_p)
    {
        return (OwXMLUtil) initializer_p.getApplicationAttribute(ATT_OW_CONFIGURATION_M_BOOTSTRAP_CONFIGURATION);
    }

    /** create bootstrap on application scope
     * 
     */
    private static OwXMLUtil createBootstrap(OwBaseInitializer initializer_p) throws Exception
    {
        String bootstrapInUse = (String) initializer_p.getApplicationAttribute("BOOTSTRAP_IN_USE");
        String shortBootstrapName = "bootstrap";
        if (bootstrapInUse != null)
        {
            shortBootstrapName = bootstrapInUse.substring(2, bootstrapInUse.length() - 4);
        }
        InputStream xmlConfigDoc = null;
        OwXMLUtil bootConfig = null;
        try
        {
            xmlConfigDoc = initializer_p.getXMLConfigDoc(shortBootstrapName);

            bootConfig = new OwStandardOptionXMLUtil(xmlConfigDoc, "bootstrap");

            // set in application context
            initializer_p.setApplicationAttribute(ATT_OW_CONFIGURATION_M_BOOTSTRAP_CONFIGURATION, bootConfig);

            //initialize custom localization files
            OwStringProperties.initialize(bootConfig);
        }
        finally
        {
            if (xmlConfigDoc != null)
            {
                xmlConfigDoc.close();
            }
        }
        return bootConfig;
    }

    /** get the bootstrap Configuration DOM node
     * @return DOM node of bootstrap Configuration
     * @since 2.5.2.0 the method is public
     */
    public OwXMLUtil getBootstrapConfiguration()
    {
        return m_bootstrapConfiguration;
    }

    /** get the mandatormanager configuration DOM node
     * @return DOM node of rolemanager configuration
     */
    public OwXMLUtil getMandatorManagerConfiguration() throws Exception
    {
        return new OwStandardXMLUtil(getBootstrapConfiguration().getSubNode("MandatorManager"));
    }

    /** get the Network Adaptor Configuration DOM node
     * @return DOM node of Network Adaptor Configuration
     */
    public OwXMLUtil getNetworkAdaptorConfiguration() throws Exception
    {
        // create configuration node with dynamic placeholders from mandator
        return new OwXMLUtilOptionAndPlaceholderFilter(getBootstrapConfiguration().getSubNode(ECM_ADAPTER_ELEMENT), this, m_roleoptionIDs);
    }

    /** get the rolemanager configuration DOM node
     * @return DOM node of rolemanager configuration
     */
    public OwXMLUtil getHistoryManagerConfiguration() throws Exception
    {
        // create configuration node with dynamic placeholders from mandator
        return new OwXMLUtilOptionAndPlaceholderFilter(getBootstrapConfiguration().getSubNode("HistoryManager"), this, m_roleoptionIDs);
    }

    /** get the rolemanager configuration DOM node
     * @return DOM node of rolemanager configuration
     */
    public OwXMLUtil getRoleManagerConfiguration() throws Exception
    {
        // create configuration node with dynamic placeholders from mandator
        return new OwXMLUtilOptionAndPlaceholderFilter(getBootstrapConfiguration().getSubNode("RoleManager"), this, m_roleoptionIDs);
    }

    /** get the fieldmanager configuration DOM node
     * @return DOM node of fieldmanager configuration
     */
    public OwXMLUtil getFieldManagerConfiguration() throws Exception
    {
        // create configuration node with dynamic placeholders from mandator
        return new OwXMLUtilOptionAndPlaceholderFilter(new OwStandardOptionXMLUtil(getBootstrapConfiguration().getSubNode("FieldManager")), this, m_roleoptionIDs);
    }

    /** get a list of plugin description nodes by type
     * @return list of plugins for the given type, or null if not found
     */
    public List getAllowedPlugins(String strType_p) throws Exception
    {
        return getRoleManager().getPlugins(strType_p);
    }

    /** get a plugin description by its key
     */
    public OwXMLUtil getPlugin(String strID_p) throws Exception
    {
        return getRoleManager().getPlugin(strID_p);
    }

    /** the client wildcard definitions to translate client wildcards to repository wildcards
     *  Map of wildcard character (String) keyed by wildcard type (Integer)
     */
    private Map m_ClientWildCardDefinitionsMap;

    /** get the client wildcard definitions to translate client wildcards to repository wildcards
     * 
     * @return Map of wildcard character (String) keyed by wildcard type (Integer) 
     */
    public Map getClientWildCardDefinitions()
    {
        if (null == m_ClientWildCardDefinitionsMap)
        {
            m_ClientWildCardDefinitionsMap = new HashMap();

            Iterator it = getBootstrapConfiguration().getSafeNodeList("ClientWildCardDefinitions").iterator();

            while (it.hasNext())
            {
                Node wcNode = (Node) it.next();

                // === get the type integer
                String sType = OwXMLDOMUtil.getSafeStringAttributeValue(wcNode, "type", null);

                if (null == sType)
                {
                    LOG.error("OwConfiguration.getClientWildCardDefinitions: Please define ClientWildCardDefinitions with type attribute in bootstrap.");
                    continue;
                }

                Integer type = null;

                try
                {
                    // === scan the field definition string if available
                    type = (Integer) OwWildCardDefinition.class.getField(sType).get(null);
                }
                catch (Exception e)
                {
                    // === must be a number
                    try
                    {
                        type = new Integer(sType);
                    }
                    catch (Exception e2)
                    {
                        LOG.error("Please define ClientWildCardDefinitions with number or OwWildCardDefinition..WILD_CARD_TYPE_... type attribute in bootstrap. Unknown type: " + sType, e2);
                        continue;
                    }
                }

                // === get the wild card character
                String sCharacter = null;
                try
                {
                    sCharacter = wcNode.getFirstChild().getNodeValue();
                }
                catch (Exception e3)
                {
                    LOG.error("OwConfiguration - Please define ClientWildCardDefinitions with a valid wildcard character in bootstrap. Type: " + sType, e3);
                    continue;
                }

                // === put in map
                m_ClientWildCardDefinitionsMap.put(type, sCharacter);
            }
        }

        return m_ClientWildCardDefinitionsMap;
    }

    /** name of the default class used to create simple folders
     *
     * @return String classname/id of default folder class or null if not defined
     */
    public String getDefaultFolderClassName()
    {
        return getBootstrapConfiguration().getSafeTextValue("DefaultFolderClass", null);
    }

    /** get the list of classnames that act as records
     * @return List of Strings with classnames
     */
    public List getRecordClassNames()
    {
        return getBootstrapConfiguration().getSafeStringList("RecordClasses");
    }

    /** get the list of language IDs for the user
     * @return List of OwXMLUtl
     */
    public List getAvailableLanguages()
    {
        return getBootstrapConfiguration().getSafeNodeList("availablelocals");
    }

    /** flag indication if language selection should be displayed
     * 
     * @return true = display language selection
     */
    public boolean displayLanguageSelection()
    {
        try
        {
            return getBootstrapConfiguration().getSubNode("availablelocals").getAttributes().getNamedItem("display").getNodeValue().equalsIgnoreCase("true");
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** 
     * the default language to be used, see also getDetectBrowserLocale()
     * @return String representing the Locale string
     * @see #getDetectBrowserLocale() 
     */
    public String getDefaultLanguage()
    {
        return getBootstrapConfiguration().getSafeTextValue("defaultlocal", "en");
    }

    /**
     * flag indicating if we use the browser language 
     * and override the default locale, see also getDefaultLanguage()
     * @return boolean (by default true)
     * @see #getDefaultLanguage()
     */
    public boolean getDetectBrowserLocale()
    {
        try
        {
            return OwXMLDOMUtil.getSafeBooleanAttributeValue(getBootstrapConfiguration().getSubNode("defaultlocal"), "detectbrowserlocale", true);
        }
        catch (Exception e)
        {
            return true;
        }
    }

    /** the behavior for UI messages caused by user error
     */
    public boolean getMessageBoxOnUserError()
    {
        return getBootstrapConfiguration().getSafeBooleanValue("MessageBoxOnUserError", false);
    }

    /** mimenode cash for fast lookup */
    private Map m_mimenodemap = new HashMap();

    /** get the MIME XML Entry for the given MIMEType. Lookup in MimeMap
     * <pre>
     * &lt;?xml version="1.0" ?&gt;
     * &lt;mimetable&gt;
     *   &lt;mime typ="text/xml"&gt;
     *        &lt;icon&gt;xml.png&lt;/icon&gt;
     *        &lt;viewerservlet&gt;{dwlurl}&lt;/viewerservlet&gt;
     *   &lt;/mime&gt;
     *   &lt;!-- further MIME entries--&gt;
     * &lt;/mimetable&gt;
     * <pre>
     * @param strMIMEType_p OwObject MIMEType
     *
     * @return OwXMLUtil wrapped DOM Node of MIME entry from MIME table, or null if not found
     */
    public OwXMLUtil getMIMENode(String strMIMEType_p) throws Exception
    {
        // fast lookup
        if (m_mimenodemap.containsKey(strMIMEType_p))
        {
            return (OwXMLUtil) m_mimenodemap.get(strMIMEType_p);
        }
        else
        {
            String optionmimetype = (String) m_roleoptionIDs.getAttribute(MIME_TYPE_OPTION_PREFIX + strMIMEType_p);
            if (null == optionmimetype)
            {
                optionmimetype = strMIMEType_p;
            }

            OwXMLUtil ret = null;

            OwXMLUtil node = getRoleManager().getMIMENode(optionmimetype);
            if (node != null)
            {
                ret = new OwXMLUtilPlaceholderFilter(node, this);
            }

            m_mimenodemap.put(strMIMEType_p, ret);

            return ret;
        }
    }

    /** get the default MIME XML Entry for the given object type.
     *
     * @param iObjectType_p Objecttype
     *
     * @return OwXMLUtil wrapped DOM Node of MIME entry from MIME table, or null if not found
     */
    public OwXMLUtil getDefaultMIMENode(int iObjectType_p) throws Exception
    {
        String key = String.valueOf(iObjectType_p);

        // fast lookup
        if (m_mimenodemap.containsKey(key))
        {
            return (OwXMLUtil) m_mimenodemap.get(key);
        }
        else
        {
            OwXMLUtil ret = null;

            OwXMLUtil node = getRoleManager().getDefaultMIMENode(iObjectType_p);
            if (node != null)
            {
                ret = new OwXMLUtilPlaceholderFilter(node, this);
            }

            m_mimenodemap.put(key, ret);

            return ret;
        }
    }

    /** SSL mode to use for requests */
    public static final int SSL_MODE_NONE = 0;
    /** SSL mode to use for requests */
    public static final int SSL_MODE_SESSION = 1;
    /** combobox renderer class name */
    private String m_comboboxRendererClassName;

    /** get the SSL mode to use for requests
     */
    public int getSSLMode()
    {
        return getBootstrapConfiguration().getSafeIntegerValue("SSLMode", SSL_MODE_SESSION);
    }

    /**
     * Get the temp dir defined in bootstrap configuration,
     * that can be used for upload. If a temp folder is not
     * defined in bootstrap configuration, this method returns
     * by default the System/Server temp folder using 
     * <code>System.<i>getProperty</i>("java.io.tmpdir")</code>.
     * 
     * @return String containing path to the temp directory
     * @throws OwConfigurationException if temp directory path can not be detected.
     */
    public String getTempDir() throws OwConfigurationException
    {
        String strTempDir = "";
        try
        {
            // first get the defined Tempdir of bootstrap
            strTempDir = getBootstrapConfiguration().getSafeTextValue("TempDir", "");

            if (strTempDir == null || strTempDir.length() == 0)
            {
                LOG.debug("Cannot find the temp dir (TempDir, defined in bootstrap configuration), that can be used for upload... Trying to set the temp dir to 'java.io.tmpdir'...");

                // if no temp dir is defined in bootstrap, get system/server temp dir
                strTempDir = System.getProperty("java.io.tmpdir");

                if (strTempDir == null || strTempDir.length() == 0)
                {// if temp dir is not defined in bootstrap neither system, get Context base path
                    try
                    {
                        strTempDir = this.m_Context.getHttpServletContext().getResource("/").getPath();
                    }
                    catch (MalformedURLException e)
                    {
                        throw new OwConfigurationException("Could not set the 'java.io.tmpdir' as temp dir....", e);
                    }
                }
            }
        }
        catch (OwConfigurationException e)
        {
            throw e;
        }
        catch (Exception ex)
        {
            throw new OwConfigurationException("Error getting the temp dir (TempDir), that can be used for upload...", ex);
        }

        return strTempDir;
    }

    // === plugin localization
    /** get the localized title of a plugin setting
     *
     * @param settingDescriptionNode_p Node
     */
    public String getLocalizedPluginSettingTitle(Node settingDescriptionNode_p, String strPluginName_p)
    {
        // === get localized displayname
        return m_Context.localize("plugin." + strPluginName_p + ".setting." + settingDescriptionNode_p.getNodeName(), OwXMLDOMUtil.getSafeStringAttributeValue(settingDescriptionNode_p, PLUGIN_SETATTR_DISPLAY_NAME, null));
    }

    /** get the localized title of a plugin
     *
     * @param pluginDescriptionNode_p OwXMLUtil wrapper
     */
    public String getLocalizedPluginTitle(OwXMLUtil pluginDescriptionNode_p)
    {
        return m_Context.localize("plugin." + pluginDescriptionNode_p.getSafeTextValue(PLUGIN_NODE_ID, "undef") + ".title", pluginDescriptionNode_p.getSafeTextValue(PLUGIN_NODE_NAME, "[undef]"));
    }

    /** get the localized title of a plugin
    *
    * @param strID_p String plugin ID 
    */
    public String getLocalizedPluginTitle(String strID_p)
    {
        try
        {
            OwXMLUtil pluginDescriptionNode = getPlugin(strID_p);
            if (pluginDescriptionNode != null)
            {
                return getLocalizedPluginTitle(pluginDescriptionNode);
            }
            else
            {
                return strID_p;
            }
        }
        catch (Exception e)
        {
            return strID_p;
        }
    }

    /** get the localized description of a plugin
     *
     * @param pluginDescriptionNode_p OwXMLUtil wrapper
     */
    public String getLocalizedPluginDescription(OwXMLUtil pluginDescriptionNode_p)
    {
        return m_Context.localize("plugin." + pluginDescriptionNode_p.getSafeTextValue(PLUGIN_NODE_ID, "undef") + ".description", pluginDescriptionNode_p.getSafeTextValue(PLUGIN_NODE_DESCRIPTION, "[undef]"));
    }

    /** get the localized description of a plugin
    *
    * @param strID_p String plugin ID 
    */
    public String getLocalizedPluginDescription(String strID_p)
    {
        try
        {
            OwXMLUtil pluginDescriptionNode = getPlugin(strID_p);
            if (pluginDescriptionNode != null)
            {
                return getLocalizedPluginDescription(pluginDescriptionNode);
            }
            else
            {
                return strID_p;
            }
        }
        catch (Exception e)
        {
            return strID_p;
        }
    }

    /** update the configuration data 
     *
     * @throws OwConfigurationException
     */
    public void refreshStaticConfiguration() throws OwConfigurationException
    {
        applicationInitalize(m_Context);
    }

    // === attribute bag implementation for mandator support
    public int attributecount()
    {
        OwMandator mandator = m_theMandatorManager.getUserMandator();

        if (null == mandator)
        {
            return 0;
        }
        else
        {
            return mandator.attributecount();
        }
    }

    public Object getAttribute(int index_p) throws Exception
    {
        OwMandator mandator = m_theMandatorManager.getUserMandator();

        if (null == mandator)
        {
            String msg = "OwConfiguration.getAttribute(int index_p): No User Mandator found by the MandatorManager, OwMandator == null / index_p = " + index_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }
        else
        {
            return mandator.getAttribute(index_p);
        }
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        OwMandator mandator = m_theMandatorManager.getUserMandator();

        if (null == mandator)
        {
            String msg = "OwConfiguration.getAttribute(String strName_p): No User Mandator found by the MandatorManager, OwMandator == null / strName_p = " + strName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }
        else
        {
            return mandator.getAttribute(strName_p);
        }
    }

    public Collection getAttributeNames()
    {
        OwMandator mandator = m_theMandatorManager.getUserMandator();

        if (null == mandator)
        {
            return new Vector();
        }
        else
        {
            return mandator.getAttributeNames();
        }
    }

    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        OwMandator mandator = m_theMandatorManager.getUserMandator();

        if (null == mandator)
        {
            return default_p;
        }
        else
        {
            return mandator.getSafeAttribute(strName_p, default_p);
        }
    }

    public boolean hasAttribute(String strName_p)
    {
        OwMandator mandator = m_theMandatorManager.getUserMandator();

        if (null == mandator)
        {
            return false;
        }
        else
        {
            return mandator.hasAttribute(strName_p);
        }
    }

    /**
     * Get the combobox renderer class name.
     * @return a {@link String} object representing the {@link OwComboboxRenderer} implementation class name.
     * @since 3.0.0.0
     */
    public String getComboboxRendererClassName()
    {
        if (m_comboboxRendererClassName == null)
        {
            try
            {
                OwXMLUtil bootstrapManagerConfiguration = getBootstrapConfiguration();
                Node subnode = bootstrapManagerConfiguration.getSubNode(COMBOBOX_RENDERER_CLASS_NAME_ELEMENT);
                if (subnode != null)
                {
                    OwXMLUtil rendererClassNameElement = new OwStandardXMLUtil(subnode);
                    m_comboboxRendererClassName = rendererClassNameElement.getSafeTextValue(OwClassicComboboxRenderer.class.getName());
                }
                else
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Cannot find the element " + COMBOBOX_RENDERER_CLASS_NAME_ELEMENT + ". Default combobox renderer class name will be used.");
                    }
                }
            }
            catch (Exception e)
            {
                LOG.error("Cannot find the element " + COMBOBOX_RENDERER_CLASS_NAME_ELEMENT + ". Default combobox renderer class name will be used.", e);
            }
            if (m_comboboxRendererClassName == null)
            {
                m_comboboxRendererClassName = OwBaseComboboxRenderer.class.getName();
            }
        }
        return m_comboboxRendererClassName;
    }

    /**
     * Get the configuration flag for dynamic split
     * @return <code>true</code> if dynamic split flag is set on <code>true</code>
     * @since 3.1.0.0
     */
    public boolean isDynamicSplitInUse()
    {
        boolean result = false;

        OwXMLUtil bootstrapConfig = getBootstrapConfiguration();
        try
        {
            OwXMLUtil ajaxLayout = bootstrapConfig.getSubUtil("AjaxLayout");
            if (ajaxLayout != null)
            {
                result = ajaxLayout.getSafeBooleanValue("UseDynamicSplit", false);
            }
        }
        catch (Exception e)
        {
            // nothing to do
        }
        return result;
    }

    /**
     * Get the layout configuration bag prefix
     * @return - the configured prefix for layout settings attribute bag
     * @since 3.1.0.0
     */
    public String getLayoutConfigurationBagPrefix()
    {
        String result = LAYOUT_SETTINGS_BAG_ID_PREFIX;

        OwXMLUtil bootstrapConfig = getBootstrapConfiguration();
        try
        {
            OwXMLUtil ajaxLayout = bootstrapConfig.getSubUtil("AjaxLayout");
            if (ajaxLayout != null)
            {
                result = ajaxLayout.getSafeTextValue("AttributeBagPrefix", result);
            }
        }
        catch (Exception e)
        {
            // nothing to do
        }
        return result;
    }

    /**
     * Do we use ExtJS for DnD or not?
     * @return the value of the <b>UseExtJsForDragAndDrop</b> configuration.
     * @since 3.2.0.0
     */
    public boolean isUseExtJsForDragAndDrop()
    {
        OwXMLUtil bootstrapConfig = getBootstrapConfiguration();
        boolean result = bootstrapConfig.getSafeBooleanValue("UseExtJsForDragAndDrop", true);
        return result;
    }

    /** 
     * Get the cached user operation listener factories.
     * @return - a set of {@link OwContextBasedUOListenerFactory} objects, or <code>null</code> if nothing is configured.
     * @throws Exception
     * @since 3.1.0.3
     */
    public Set<OwContextBasedUOListenerFactory> getUserOperationListenerFactories() throws Exception
    {
        if (this.listenerFactories == null)
        {
            Set<OwContextBasedUOListenerFactory> result = null;
            OwXMLUtil bootstrapConfiguration = getBootstrapConfiguration();
            OwXMLUtil ecmAdapterUtil = bootstrapConfiguration.getSubUtil(ECM_ADAPTER_ELEMENT);
            OwXMLUtil listenersNode = ecmAdapterUtil.getSubUtil("UserOperationListeners");
            if (listenersNode != null)
            {
                List<?> list = listenersNode.getSafeNodeList();
                if (list.size() > 0)
                {
                    result = new LinkedHashSet<OwContextBasedUOListenerFactory>();

                    for (int i = 0; i < list.size(); i++)
                    {
                        Node theNode = (Node) list.get(i);
                        try
                        {
                            NamedNodeMap attrs = theNode.getAttributes();
                            String className = attrs.getNamedItem("className").getNodeValue();
                            Class<?> factoryClass = Class.forName(className);
                            if (OwContextBasedUOListenerFactory.class.isAssignableFrom(factoryClass))
                            {
                                Constructor<?> c = factoryClass.getConstructor(m_Context.getClass());
                                OwContextBasedUOListenerFactory factoryInstance = (OwContextBasedUOListenerFactory) c.newInstance(m_Context);
                                result.add(factoryInstance);
                            }
                        }
                        catch (Exception e)
                        {
                            LOG.error("Exception occurred on initialization of user operation listener <UserOperationListeners>.", e);
                            throw new OwConfigurationException(this.m_Context.localize("app.OwMainAppContext.useroperation.listener.configuration", "Exception occurred on initialization of user operation listener <UserOperationListeners>."), e);
                        }
                    }
                    listenerFactories = result;
                }
            }
        }
        return listenerFactories;
    }

    /**
     * Return the &lt;globalParameters&gt; node, if available,
     * or will return the root Bootstrap node.
     * @return OwXMLUtil
     * @since 4.2.0.0
     * @see #getBootstrapConfiguration()
     */
    public OwGlobalParametersConfiguration getGlobalParameters()
    {
        OwXMLUtil util = getBootstrapConfiguration();
        try
        {
            OwXMLUtil global = util.getSubUtil(GLOBAL_PARAMETERS_ELEMENT);
            if (global != null)
            {
                util = global;
            }
        }
        catch (Exception e)
        {
            LOG.warn("Unable to get <" + GLOBAL_PARAMETERS_ELEMENT + "> element from bootstrap", e);
        }
        return new OwGlobalParametersConfiguration(util);
    }
}