package com.wewebu.ow.server.ao;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.ecmimpl.OwBackwardsCompatibilityAOProvider;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 *Singleton configuration and context based application object factory.
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
public class OwAOProviderFactory
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwAOProviderFactory.class);

    public static final String CONFIGNODE_APPLICATIONOBJECTS = "AO";

    public static final String CONFIGATTRIBUTE_PROVIDER = "provider";

    private static OwAOProviderFactory instance = null;

    public synchronized static OwAOProviderFactory getInstance()
    {
        if (instance == null)
        {
            instance = new OwAOProviderFactory();
        }

        return instance;
    }

    private OwAOProviderFactory()
    {
        //void
    }

    public OwAOProvider createProvider(OwConfiguration configuration, OwAOContext context) throws OwConfigurationException
    {
        try
        {
            OwXMLUtil bootstrap = configuration.getBootstrapConfiguration();

            OwXMLUtil applicationObjectsConfig = bootstrap.getSubUtil(CONFIGNODE_APPLICATIONOBJECTS);

            String providerClassName = OwBackwardsCompatibilityAOProvider.class.getName();

            if (applicationObjectsConfig == null)
            {
                LOG.warn("Could not find the AO provider configuration. Please check your owbootstrap.xml. Proceeding with " + OwBackwardsCompatibilityAOProvider.class + ".");
            }
            else
            {
                providerClassName = applicationObjectsConfig.getSafeStringAttributeValue(CONFIGATTRIBUTE_PROVIDER, null);
            }

            if (providerClassName == null)
            {
                throw new OwConfigurationException("Invalid AO provider configiration : no provider class ");
            }

            Class<?> providerClass = Class.forName(providerClassName);
            if (OwAOProvider.class.isAssignableFrom(providerClass))
            {
                OwAOProvider provier = (OwAOProvider) providerClass.newInstance();
                if (provier instanceof OwAOConfigurableProvider)
                {
                    OwAOConfigurableProvider configurableProvider = (OwAOConfigurableProvider) provier;
                    configurableProvider.init(applicationObjectsConfig, context);
                    LOG.debug("Loaded configurable AO provider " + provier);
                }
                else
                {
                    LOG.debug("Loaded AO provider " + provier);
                }

                return provier;
            }
            else
            {
                throw new OwConfigurationException("Invalid AO provider configiration : " + OwAOProvider.class + " extension expected.");
            }

        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Invalid application objects configuration", e);
        }

    }
}
