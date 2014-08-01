package com.wewebu.ow.server.ao;

import java.util.List;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Application objects provider for managed contexts.  
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
public class OwManagedAOProvider implements OwAOProvider, OwAOConfigurableProvider
{

    private OwAOConfiguration aoConfiguration;

    public OwManagedAOProvider()
    {

    }

    private void checkConfiguration() throws OwConfigurationException
    {
        if (aoConfiguration == null)
        {
            throw new OwConfigurationException("The AO Provider is missing thge registry manager configuration.");
        }
    }

    @Override
    public <T> List<T> getApplicationObjects(OwAOType<T> type, String name, boolean forceSpecificObj) throws OwException
    {
        checkConfiguration();
        OwAOManager aoManager = aoConfiguration.getManager(type.getType());
        return (List<T>) aoManager.getApplicationObjects(name, forceSpecificObj);
    }

    @Override
    public <T> T getApplicationObject(OwAOType<T> type, String name, boolean forceSpecificObj, boolean createNonExisting) throws OwException
    {
        checkConfiguration();
        OwAOManager aoManager = aoConfiguration.getManager(type.getType());
        return (T) aoManager.getApplicationObject(name, forceSpecificObj, createNonExisting);
    }

    @Override
    public <T> T getApplicationObject(OwAOType<T> type, String name, List<Object> params, boolean forceSpecificObj, boolean createNonExisting) throws OwException
    {
        checkConfiguration();
        OwAOManager aoManager = aoConfiguration.getManager(type.getType());
        return (T) aoManager.getApplicationObject(name, params.get(0), forceSpecificObj, createNonExisting);
    }

    @Override
    public synchronized void init(OwXMLUtil configuration, OwAOContext context) throws OwConfigurationException
    {
        aoConfiguration = createConfiguration(configuration, context);
    }

    protected OwAOConfiguration createConfiguration(OwXMLUtil configuration, OwAOContext context) throws OwConfigurationException
    {
        return new OwAOConfiguration(configuration, context);
    }

}
