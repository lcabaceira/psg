package com.wewebu.ow.unittest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.ao.OwAOType;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwDataSourceException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.history.OwHistoryManagerContext;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.mandator.OwMandatorManagerContext;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwTimeZoneInfo;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Test configuration for adapters and managers.<br>
 * The base context keeps basic configuration information and is independent to the web context.
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
public class OwTestContext extends AbstractNetworkContextAdapter implements OwHistoryManagerContext, OwNetworkContext, OwRoleManagerContext, OwMandatorManagerContext
{
    /**
    * Logger for this class
    */
    private static final Logger LOG = Logger.getLogger(OwTestContext.class);

    /** context root for JNDI lookup */
    public static final String JNDI_CONTEXT_ROOT = "java:comp/env";

    private OwXMLUtil m_config;
    private String m_strConfigDir;

    private OwXMLUtil m_initparameter;
    private String m_initparameterNodeName = "initparameter";
    private OwXMLUtil m_datasource;
    private String m_datasourceNodeName = "DefaultDataSource";
    private OwNetwork m_network;
    private OwMandatorManager m_mandatorManager;
    private JdbcTemplate m_jdbcTemplate;
    private OwRoleManager roleManager;
    private OwHistoryManager m_historyManager;
    private TimeZone clientTimeZone;

    public OwTestContext(OwXMLUtil config_p, String strConfigDir_p)
    {
        this.clientTimeZone = TimeZone.getDefault();
        m_config = config_p;
        m_strConfigDir = strConfigDir_p;

        try
        {
            m_initparameter = new OwStandardXMLUtil(m_config.getSubNode(m_initparameterNodeName));
        }
        catch (Exception e)
        {
            LOG.error("Cannot read the subnode: " + m_initparameterNodeName, e);
            /*
             * [Dm] Method invokes System.exit(...) [DM_EXIT]
               Invoking System.exit shuts down the entire Java virtual machine. This should only been done when it is appropriate. Such calls make it hard or impossible for your code to be invoked by other code. Consider throwing a RuntimeException instead.
             */
            Assert.fail("Cannot read the subnode: " + m_initparameterNodeName);
        }
        try
        {
            org.w3c.dom.Node defaultResource = m_config.getSubNode(m_datasourceNodeName);
            if (defaultResource != null)
            {
                m_datasource = new OwStandardXMLUtil(defaultResource);
            }
        }
        catch (Exception e)
        {
            LOG.error("Cannot read the subnode: " + m_datasourceNodeName, e);
            /*
             * [Dm] Method invokes System.exit(...) [DM_EXIT]
               Invoking System.exit shuts down the entire Java virtual machine. This should only been done when it is appropriate. Such calls make it hard or impossible for your code to be invoked by other code. Consider throwing a RuntimeException instead.
             */
            Assert.fail("Cannot read the subnode: " + m_datasourceNodeName);
        }

        if (m_datasource != null)
        {
            try
            {
                javax.naming.Context initialContext = new javax.naming.InitialContext();
                initialContext.removeFromEnvironment("java.naming.factory.url.pkgs");
                Context ctx = (Context) initialContext.lookup(OwTestContext.JNDI_CONTEXT_ROOT);
                if (ctx == null)
                {
                    ctx = initialContext.createSubcontext(OwTestContext.JNDI_CONTEXT_ROOT);
                    LOG.debug("InitialContext().createSubcontext(" + OwTestContext.JNDI_CONTEXT_ROOT + ") successfully created...");
                }
                // init data source
                String sJndiName = m_datasource.getSafeTextValue("JNDIName", null);
                if (ctx.lookup(sJndiName) == null)
                {
                    ctx.bind(sJndiName, new OwTestDataSource(m_datasource));
                }
                else
                {
                    //reinitializes the ctx because maybe the m_datasource changed.
                    ctx.rebind(sJndiName, new OwTestDataSource(m_datasource));
                }
                // init JdbcTemplate
                DataSource ds = null;
                try
                {
                    InitialContext ic = new InitialContext();
                    try
                    {
                        Context envCtx = (Context) ic.lookup(OwTestContext.JNDI_CONTEXT_ROOT);
                        // Look up our data source
                        ds = (DataSource) envCtx.lookup(sJndiName);
                    }
                    catch (NameNotFoundException e)
                    {
                        // also check java-Namespace
                        ds = (DataSource) ic.lookup(sJndiName);
                    }
                }
                catch (NamingException ex)
                {
                    String msg = "OwConfiguration.createJdbcDataSource: Could not initialize Spring JDBC template";
                    LOG.fatal(msg, ex);
                    throw new OwDataSourceException(msg, ex);
                }

                if (ds == null)
                {
                    String msg = "OwConfiguration.createJdbcDataSource: Could not initialize spring JDBC template";
                    LOG.fatal(msg);
                    throw new OwDataSourceException(msg);
                }
                m_jdbcTemplate = new JdbcTemplate(ds);
            }
            catch (NamingException e)
            {
                LOG.error("Initialization of the JDNI Context failed (NamingException). Testing is stopped...", e);
                Assert.fail("Initialization of the JDNI Context failed (NamingException). Testing is stopped...");
            }
            catch (Exception e)
            {
                LOG.error("Initialization of the JDNI Context failed (Exception). Testing is stopped...", e);
                Assert.fail("Initialization of the JDNI Context failed (Exception). Testing is stopped...");
            }
        }
    }

    public String createTempDir(String strPrefix_p) throws OwConfigurationException
    {

        return null;
    }

    public void deleteTempDir(String strDir_p)
    {

    }

    public String getBasePath()
    {
        return m_strConfigDir;
    }

    public String getClientID()
    {
        return "JUnit";
    }

    public String getConfigurationName()
    {
        return "default";
    }

    public OwBaseUserInfo getCurrentUser() throws Exception
    {
        return getNetwork().getCredentials().getUserInfo();
    }

    public String getDefaultFolderClassName()
    {
        return "Folder";
    }

    public String getInitParameter(String strParamName_p)
    {
        return m_initparameter.getSafeTextValue(strParamName_p, null);
    }

    public Locale getLocale()
    {
        return Locale.GERMAN;
    }

    public InputStream getXMLConfigDoc(String strName_p) throws Exception
    {
        return new FileInputStream(new File(m_strConfigDir + "ow" + strName_p + ".xml"));
    }

    public boolean hasLabel(String strName_p)
    {
        return true;
    }

    public String localize(String strKey_p, String strText_p)
    {
        return strText_p;
    }

    public String localize1(String strKey_p, String strText_p, String strAttribute1_p)
    {
        return OwString.replaceAll(strText_p, "%1", strAttribute1_p);
    }

    public String localize2(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p)
    {
        String strRet = OwString.replaceAll(strText_p, "%1", strAttribute1_p);
        strRet = OwString.replaceAll(strRet, "%2", strAttribute2_p);

        return strRet;
    }

    public String localize3(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p, String strAttribute3_p)
    {
        String strRet = OwString.replaceAll(strText_p, "%1", strAttribute1_p);
        strRet = OwString.replaceAll(strRet, "%2", strAttribute2_p);
        strRet = OwString.replaceAll(strRet, "%3", strAttribute2_p);

        return strRet;
    }

    public String localizeLabel(String strName_p)
    {
        return strName_p;
    }

    private OwTestConfiguration m_configuration;

    public OwBaseConfiguration getBaseConfiguration()
    {
        if (null == m_configuration)
        {
            m_configuration = new OwTestConfiguration();
        }

        return m_configuration;
    }

    public String getClientWildCard(int wildcardtype_p)
    {
        return null;
    }

    public String getConfigPath()
    {
        return m_strConfigDir;
    }

    public void addConfigChangeEventListener(OwConfigChangeEventListener listener_p)
    {
        // ignore
    }

    /** hashmap to simulate application context */
    private HashMap m_applicationContext = new HashMap();

    private OwAOProvider unmanagedAOProvider;

    public Object getApplicationAttribute(String key_p)
    {
        if (key_p.equals("OwStandardRoleManager.m_SelectiveConfigurationMap"))
        {
            return Collections.EMPTY_MAP;
        }
        else if (key_p.equals("OwConfiguration.m_bootstrapConfiguration"))
        {
            return m_config;
        }
        else if (key_p.equals("OwStandardRoleManager.m_MimeMap") || key_p.equals("OwStandardRoleManager.m_DefaultMimeMap") || key_p.equals("OwStandardRoleManager.m_PluginTypesMap") || key_p.equals("OwStandardRoleManager.m_PluginMap")
                || key_p.equals("OwStandardRoleManager.m_SelectiveConfigurationMap"))
        {
            Map map = (Map) m_applicationContext.get(key_p);
            if (map == null)
            {
                return new HashMap();
            }
            else
            {
                return map;
            }
        }
        else
        {
            return m_applicationContext.get(key_p);
        }
    }

    public Object setApplicationAttribute(String key_p, Object object_p)
    {
        Object old = m_applicationContext.get(key_p);
        m_applicationContext.put(key_p, object_p);
        return old;
    }

    /** get a ID / name for the calling mandator
     * 
     * @return String mandator or null if no mandator is supported
     */
    public String getMandatorID()
    {
        // currently not supported
        return null;
    }

    public OwMandator getMandator()
    {

        return null;
    }

    public OwMandatorManager getMandatorManager()
    {
        return m_mandatorManager;
    }

    public void setMandatorManager(OwMandatorManager manager_p)
    {
        m_mandatorManager = manager_p;
    }

    public JdbcTemplate getJDBCTemplate()
    {
        return m_jdbcTemplate;
    }

    public OwNetwork getNetwork()
    {
        return m_network;
    }

    public void setRoleManager(OwRoleManager roleManager_p)
    {
        this.roleManager = roleManager_p;
    }

    public void setNetwork(OwNetwork network_p)
    {
        this.m_network = network_p;
    }

    public OwHistoryManager getHistoryManager()
    {
        return m_historyManager;
    }

    public void setHistoryManager(OwHistoryManager manager_p)
    {
        m_historyManager = manager_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwNetworkContext#resolveLiteralPlaceholder(java.lang.String, java.lang.String)
     */
    public Object resolveLiteralPlaceholder(String contextname_p, String placeholdername_p) throws Exception
    {
        if (placeholdername_p.equals(OwMainAppContext.LITERAL_PLACEHOLDER_TODAY))
        {
            return new Date();
        }

        if (placeholdername_p.equals(OwMainAppContext.LITERAL_PLACEHOLDER_USER_ID))
        {
            try
            {
                return getCurrentUser().getUserID();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(OwMainAppContext.LITERAL_PLACEHOLDER_USER_NAME))
        {
            try
            {
                return getCurrentUser().getUserName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(OwMainAppContext.LITERAL_PLACEHOLDER_USER_LONG_NAME))
        {
            try
            {
                return getCurrentUser().getUserLongName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(OwMainAppContext.LITERAL_PLACEHOLDER_USER_SHORT_NAME))
        {
            try
            {
                return getCurrentUser().getUserShortName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        if (placeholdername_p.equals(OwMainAppContext.LITERAL_PLACEHOLDER_USER_DISPLAY_NAME))
        {
            try
            {
                return getCurrentUser().getUserDisplayName();
            }
            catch (Exception e)
            {
                LOG.error("OwMainAppContext.resolveLiteralPlaceholder: The placeholder can not be resolved for the search template; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p, e);
                throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderCouldNotResolved", "The placeholder (%1) cannot be resolved for the search template (%2).", placeholdername_p, contextname_p), e);
            }
        }

        LOG.error("OwMainAppContext.resolveLiteralPlaceholderValue: Invalid placeholder for the search template used; searchtemplatename = " + contextname_p + ", placeholdername = " + placeholdername_p);
        throw new OwConfigurationException(OwString.localize2(getLocale(), "ecm.OwSearchTemplate.placeholderInvalid", "Invalid placeholder (%1) for the search template (%2) used.", placeholdername_p, contextname_p));
    }

    public URL getConfigURL(String strName_p) throws Exception
    {
        StringBuffer filePath = new StringBuffer();
        filePath.append(getConfigPath());
        filePath.append("/");
        filePath.append(strName_p);
        File configFile = new File(filePath.toString());
        if (configFile.exists())
        {
            return configFile.toURL();
        }
        return null;
    }

    public OwTimeZoneInfo getClientTimeZoneInfo()
    {
        return new OwTimeZoneInfo(this.clientTimeZone);
    }

    public TimeZone getClientTimeZone()
    {
        return this.clientTimeZone;
    }

    public void setClientTimeZone(TimeZone clientTimeZone)
    {
        this.clientTimeZone = clientTimeZone;
    }

    public void loginInit() throws Exception
    {
        roleManager.loginInit();
    }

    @Override
    public OwAOProvider getUnmanagedAOProvider() throws OwException
    {
        if (null == this.unmanagedAOProvider)
        {
            //this.unmanagedAOProvider = new OwBackwardsCompatibilityAOProvider(this.m_network, this.roleManager);
            this.unmanagedAOProvider = new OwAOProvider() {

                @Override
                public <T> List<T> getApplicationObjects(OwAOType<T> type, String name, boolean forceSpecificObj) throws OwException
                {
                    try
                    {
                        return new LinkedList<T>(OwTestContext.this.m_network.getApplicationObjects(type.getType(), name, forceSpecificObj));
                    }
                    catch (Exception e)
                    {
                        throw new OwServerException("", e);
                    }
                }

                @Override
                public <T> T getApplicationObject(OwAOType<T> type, String name, boolean forceSpecificObj, boolean createNonExisting) throws OwException
                {
                    try
                    {
                        return (T) OwTestContext.this.m_network.getApplicationObject(type.getType(), name, forceSpecificObj, createNonExisting);
                    }
                    catch (Exception e)
                    {
                        throw new OwServerException("", e);
                    }
                }

                @Override
                public <T> T getApplicationObject(OwAOType<T> aoType, String name, List<Object> params, boolean forceSpecificObj, boolean createNonExisting) throws OwException
                {
                    try
                    {
                        return (T) OwTestContext.this.m_network.getApplicationObject(aoType.getType(), name, params, forceSpecificObj, createNonExisting);
                    }
                    catch (Exception e)
                    {
                        throw new OwServerException("", e);
                    }
                }

            };
        }
        return this.unmanagedAOProvider;
    }
}
