package com.wewebu.ow.server.app;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.ao.OwAOContext;
import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.ao.OwAOProviderFactory;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;
import com.wewebu.ow.server.role.OwRoleManagerContext;

/**
 *<p>
 * Main Role Manager Context Class Implementation. Instance stays active during session.
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
 *@since 4.2.0.0
 */
public class OwMainRoleManagerContext implements OwRoleManagerContext, OwAOContext
{

    private OwMainAppContext mainContext;
    private OwAOProvider unmanagedAOProvider;

    public OwMainRoleManagerContext(OwMainAppContext mainContext)
    {
        super();
        this.mainContext = mainContext;

    }

    @Override
    public URL getConfigURL(String strName_p) throws Exception
    {
        return mainContext.getConfigURL(strName_p);
    }

    @Override
    public Object getApplicationAttribute(String key_p)
    {
        return mainContext.getApplicationAttribute(key_p);
    }

    @Override
    public Object setApplicationAttribute(String key_p, Object object_p)
    {
        return mainContext.setApplicationAttribute(key_p, object_p);
    }

    @Override
    public OwBaseConfiguration getBaseConfiguration()
    {
        return mainContext.getBaseConfiguration();
    }

    @Override
    public JdbcTemplate getJDBCTemplate()
    {
        return mainContext.getJDBCTemplate();
    }

    @Override
    public Locale getLocale()
    {
        return mainContext.getLocale();
    }

    @Override
    public String localizeLabel(String strName_p)
    {
        return mainContext.localizeLabel(strName_p);
    }

    @Override
    public boolean hasLabel(String strName_p)
    {
        return mainContext.hasLabel(strName_p);
    }

    @Override
    public String localize(String strKey_p, String strText_p)
    {
        return mainContext.localize(strKey_p, strText_p);
    }

    @Override
    public String localize1(String strKey_p, String strText_p, String strAttribute1_p)
    {
        return mainContext.localize1(strKey_p, strText_p, strAttribute1_p);
    }

    @Override
    public String localize2(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p)
    {
        return mainContext.localize2(strKey_p, strText_p, strAttribute1_p, strAttribute2_p);
    }

    @Override
    public String localize3(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p, String strAttribute3_p)
    {
        return mainContext.localize3(strKey_p, strText_p, strAttribute1_p, strAttribute2_p, strAttribute3_p);
    }

    @Override
    public String getConfigurationName()
    {
        return mainContext.getConfigurationName();
    }

    @Override
    public InputStream getXMLConfigDoc(String strName_p) throws Exception
    {
        return mainContext.getXMLConfigDoc(strName_p);
    }

    @Override
    public String getBasePath()
    {
        return mainContext.getBasePath();
    }

    @Override
    public String getInitParameter(String strParamName_p)
    {
        return mainContext.getInitParameter(strParamName_p);
    }

    @Override
    public OwBaseUserInfo getCurrentUser() throws Exception
    {
        return mainContext.getCurrentUser();
    }

    @Override
    public void deleteTempDir(String strDir_p)
    {
        mainContext.deleteTempDir(strDir_p);
    }

    @Override
    public String createTempDir(String strPrefix_p) throws OwConfigurationException
    {
        return mainContext.createTempDir(strPrefix_p);
    }

    @Override
    public String getClientID()
    {
        return mainContext.getClientID();
    }

    @Override
    public String getMandatorID()
    {
        return mainContext.getMandatorID();
    }

    @Override
    public OwMandator getMandator()
    {
        return mainContext.getMandator();
    }

    @Override
    public OwNetwork getNetwork()
    {
        return mainContext.getNetwork();
    }

    @Override
    public OwHistoryManager getHistoryManager()
    {
        return mainContext.getHistoryManager();
    }

    @Override
    public OwMandatorManager getMandatorManager()
    {
        return mainContext.getMandatorManager();
    }

    @Override
    public synchronized OwAOProvider getUnmanagedAOProvider() throws OwConfigurationException
    {
        if (unmanagedAOProvider == null)
        {
            unmanagedAOProvider = OwAOProviderFactory.getInstance().createProvider(getConfiguration(), this);
        }
        return unmanagedAOProvider;
    }

    @Override
    public OwConfiguration getConfiguration()
    {
        return mainContext.getConfiguration();
    }

    @Override
    public boolean isRoleManaged()
    {
        return false;
    }

}
