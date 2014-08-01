package com.wewebu.ow.unittest.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wewebu.ow.server.app.OwUserOperationDispatch;
import com.wewebu.ow.server.app.OwUserOperationEvent;
import com.wewebu.ow.server.app.OwUserOperationEvent.OwUserOperationType;
import com.wewebu.ow.server.app.OwUserOperationListener;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManager;

/**
 *<p>
 * Dummy implementation without any logic or implementation.
 * This is just a helper class where only the needed method can
 * be overwritten instead of implementing all interface methods. 
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
public abstract class AbstractNetworkContextAdapter implements OwNetworkContext
{
    /*GlobalRegistryContext interface*/
    private ConcurrentHashMap<Class<?>, Object> register = new ConcurrentHashMap<Class<?>, Object>();
    /*OwUserOperationExecutor interface*/
    private OwUserOperationDispatch executor = new OwUserOperationDispatch();

    public void addUserOperationListener(OwUserOperationListener listener_p)
    {
        executor.addUserOperationListener(listener_p);
    }

    public void removeUserOperationListener(OwUserOperationListener listener_p)
    {
        executor.removeUserOperationListener(listener_p);
    }

    public OwBaseUserInfo getCurrentUser() throws Exception
    {
        return null;
    }

    public String getDefaultFolderClassName()
    {
        return null;
    }

    public void onLogin(OwBaseUserInfo user_p) throws Exception
    {
        OwUserOperationType.LOGIN.fire(executor, user_p, OwUserOperationEvent.OWD_APPLICATION);
    }

    public Object resolveLiteralPlaceholder(String contextname_p, String placeholdername_p) throws Exception
    {
        return null;
    }

    public void addConfigChangeEventListener(OwConfigChangeEventListener listener_p)
    {

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

        return null;
    }

    public String getClientID()
    {

        return null;
    }

    public String getClientWildCard(int wildcardtype_p)
    {

        return null;
    }

    public String getConfigPath()
    {

        return null;
    }

    public URL getConfigURL(String strName_p) throws Exception
    {

        return null;
    }

    public String getConfigurationName()
    {

        return null;
    }

    public String getInitParameter(String strParamName_p)
    {

        return null;
    }

    public JdbcTemplate getJDBCTemplate()
    {

        return null;
    }

    public Locale getLocale()
    {

        return null;
    }

    public OwMandator getMandator()
    {

        return null;
    }

    public String getMandatorID()
    {

        return null;
    }

    public OwMandatorManager getMandatorManager()
    {

        return null;
    }

    public InputStream getXMLConfigDoc(String strName_p) throws Exception
    {
        return null;
    }

    public boolean hasLabel(String strName_p)
    {

        return false;
    }

    public String localize(String strKey_p, String strText_p)
    {

        return null;
    }

    public String localize1(String strKey_p, String strText_p, String strAttribute1_p)
    {

        return null;
    }

    public String localize2(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p)
    {

        return null;
    }

    public String localize3(String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p, String strAttribute3_p)
    {

        return null;
    }

    public String localizeLabel(String strName_p)
    {

        return null;
    }

    public Object getApplicationAttribute(String key_p)
    {

        return null;
    }

    public Object setApplicationAttribute(String key_p, Object object_p)
    {

        return null;
    }

    public void registerInterface(Class<?> typeClass, Object object)
    {
        register.put(typeClass, object);
    }

    public <T> T getRegisteredInterface(Class<T> typeClass)
    {
        return (T) register.get(typeClass);
    }

    public <T> T unregisterInterface(Class<T> typeClass)
    {
        return (T) register.remove(typeClass);
    }
}
