package com.wewebu.ow.server.ecmimpl.fncm5.unittest;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.unittest.log.JUnitLogger;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwDataSourceException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.history.OwHistoryManagerContext;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwTimeZoneInfo;
import com.wewebu.ow.unittest.util.AbstractNetworkContextAdapter;
import com.wewebu.ow.unittest.util.OwTestContext;
import com.wewebu.ow.unittest.util.OwTestDataSource;

/**
 *<p>
 * OwTestNetworkContext.
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
public class OwFCM5TestNetworkContext extends AbstractNetworkContextAdapter implements OwNetworkContext, OwHistoryManagerContext, OwRoleManagerContext
{
    private static final Logger LOG = JUnitLogger.getLogger(OwFCM5TestNetworkContext.class);
    private OwBaseUserInfo loggedInUser;
    private OwTimeZoneInfo tzInfo;

    private OwNetwork network;
    private JdbcTemplate jdbcTemplate;
    private OwStandardXMLUtil config;
    /** hashmap to simulate application context */
    private HashMap applicationContext = new HashMap();
    private OwRoleManager roleManager;

    public OwFCM5TestNetworkContext(OwFNCM5Network network_p, OwStandardXMLUtil config_p)
    {
        this.network = network_p;
        this.config = config_p;
        initJDBC();

    }

    private void initJDBC()
    {

        //        final String initParameterName = "initparameter";
        //        
        //        OwStandardXMLUtil initparameter = null;
        //        try
        //        {
        //            initparameter = new OwStandardXMLUtil(config.getSubNode(initParameterName));
        //        }
        //        catch (Exception e)
        //        {
        //            LOG.error("Cannot read the subnode: " + initParameterName, e);
        //            return;
        //        }

        final String datasourceNodeName = "DefaultDataSource";
        OwStandardXMLUtil datasource = null;
        try
        {
            Node subNode = config.getSubNode(datasourceNodeName);
            if (subNode != null)
            {
                datasource = new OwStandardXMLUtil(subNode);
            }
        }
        catch (Exception e)
        {
            LOG.error("Cannot read the subnode: " + datasourceNodeName, e);
            return;
        }
        if (datasource != null)
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
                String sJndiName = datasource.getSafeTextValue("JNDIName", null);
                if (ctx.lookup(sJndiName) == null)
                {
                    ctx.bind(sJndiName, new OwTestDataSource(datasource));
                }
                else
                {
                    //reinitializes the ctx because maybe the m_datasource changed.
                    ctx.rebind(sJndiName, new OwTestDataSource(datasource));
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

                }

                if (ds == null)
                {
                    String msg = "OwConfiguration.createJdbcDataSource: Could not initialize spring JDBC template";
                    LOG.fatal(msg);
                    throw new OwDataSourceException(msg);
                }
                jdbcTemplate = new JdbcTemplate(ds);
            }
            catch (NamingException e)
            {
                LOG.error("Initialization of the JDNI Context failed (NamingException). Testing is stopped...", e);
            }
            catch (Exception e)
            {
                LOG.error("Initialization of the JDNI Context failed (Exception). Testing is stopped...", e);
            }
        }

    }

    public JdbcTemplate getJDBCTemplate()
    {
        return jdbcTemplate;
    }

    public Locale getLocale()
    {
        return Locale.getDefault();
    }

    public String localizeLabel(String strName_p)
    {
        return "";
    }

    public String localize(String strKey_p, String strText_p)
    {
        return localizeLabel(strKey_p);
    }

    public String localize1(String strKey_p, String strText_p, String strAttribute1_p)
    {
        return localizeLabel(strKey_p);
    }

    public String localize2(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p)
    {
        return localizeLabel(strKey_p);
    }

    public String localize3(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p, String strAttribute3_p)
    {
        return localizeLabel(strKey_p);
    }

    public String getConfigurationName()
    {
        return "Test";
    }

    public URL getConfigURL(String strName_p) throws Exception
    {
        return getClass().getResource("/" + strName_p);
    }

    public void onLogin(OwBaseUserInfo user_p) throws Exception
    {
        LOG.info("onLogin called with " + user_p);
        this.loggedInUser = user_p;
        super.onLogin(user_p);
    }

    public OwBaseUserInfo getCurrentUser() throws Exception
    {
        return this.loggedInUser;
    }

    public OwTimeZoneInfo getClientTimeZoneInfo()
    {
        if (tzInfo == null)
        {
            tzInfo = new OwTimeZoneInfo(TimeZone.getDefault());
        }
        return tzInfo;
    }

    public TimeZone getClientTimeZone()
    {
        return getClientTimeZoneInfo().getTimeZone();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.history.OwHistoryManagerContext#getBaseConfiguration()
     */
    public OwBaseConfiguration getBaseConfiguration()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.role.OwRoleManagerContext#getNetwork()
     */
    public OwNetwork getNetwork()
    {
        return this.network;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.role.OwRoleManagerContext#getHistoryManager()
     */
    public OwHistoryManager getHistoryManager()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void loginInit() throws OwException
    {

        try
        {
            this.roleManager.loginInit();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not initialize the Role Manager.", e);
        }
    }

    public Object setApplicationAttribute(String key_p, Object object_p)
    {
        Object old = applicationContext.get(key_p);
        applicationContext.put(key_p, object_p);
        return old;
    }

    public Object getApplicationAttribute(String key_p)
    {
        if (key_p.equals("OwStandardRoleManager.m_SelectiveConfigurationMap"))
        {
            return Collections.EMPTY_MAP;
        }
        else if (key_p.equals("OwConfiguration.m_bootstrapConfiguration"))
        {
            return config;
        }
        else if (key_p.equals("OwStandardRoleManager.m_MimeMap") || key_p.equals("OwStandardRoleManager.m_DefaultMimeMap") || key_p.equals("OwStandardRoleManager.m_PluginTypesMap") || key_p.equals("OwStandardRoleManager.m_PluginMap")
                || key_p.equals("OwStandardRoleManager.m_SelectiveConfigurationMap"))
        {
            Map map = (Map) applicationContext.get(key_p);
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
            return applicationContext.get(key_p);
        }
    }

    public void setRoleManager(OwRoleManager owRoleManager)
    {
        this.roleManager = owRoleManager;

    }

    @Override
    public OwAOProvider getUnmanagedAOProvider()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
