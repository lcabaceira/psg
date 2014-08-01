package com.wewebu.service.rendition.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwStandardOptionXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.service.rendition.OwConfigurableRenditionService;
import com.wewebu.service.rendition.OwRenditionService;
import com.wewebu.service.rendition.OwRenditionServiceProvider;

/**
 *<p>
 * Simple implementation of OwRenditionServiceProvider.
 * Using a rendition.xml file to configure/load corresponding service definitions.
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
public class OwSimpleRenditionServiceProvider implements OwRenditionServiceProvider
{
    private static final Logger LOG = OwLogCore.getLogger(OwSimpleRenditionServiceProvider.class);
    private static final String EL_CONFIGURATION = "configuration";

    private static final String EL_SERVICE = "service";
    private static final String AT_CLASS = "class";

    private OwProviderConfiguration providerConfiguration;
    private ConcurrentHashMap<String, OwRenditionService> renditionServices;
    private OwRenditionService fallbackRendtionService;

    public OwSimpleRenditionServiceProvider()
    {
        renditionServices = new ConcurrentHashMap<String, OwRenditionService>();
        initFallbackRendtionService();
    }

    @Override
    public OwRenditionService getRendtionService(OwObject obj) throws OwException
    {
        String key = null;
        try
        {
            String[] splitDms = obj.getDMSID().split(",");
            key = splitDms[0];
        }
        catch (Exception e)
        {
            LOG.error("Could not retrieve DMSID from object", e);
            throw new OwServerException("Cannot get mapping information", e);
        }

        OwRenditionService service = renditionServices.get(key);
        if (service == null)
        {
            service = initRendtionService(key);
            if (service == null)
            {
                service = OwNoRenditionService.INSTANCE;
            }
        }
        return service;
    }

    /* (non-Javadoc)
     * @see com.wewebu.service.rendition.OwRenditionServiceProvider#getFallbackRendtionService()
     */
    @Override
    public OwRenditionService getFallbackRendtionService()
    {
        if (fallbackRendtionService != null)
        {
            return this.fallbackRendtionService;
        }
        else
        {
            return OwNoRenditionService.INSTANCE;
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized OwProviderConfiguration getProviderConfiguration() throws OwConfigurationException
    {
        if (providerConfiguration == null)
        {
            OwXMLUtil rootConf = readConfiguration(getRootConfigurationName());
            providerConfiguration = OwProviderConfiguration.fromXML(rootConf);
        }
        return providerConfiguration;
    }

    /**
     * Get the path/file name for root configuration resource.
     * @return String
     */
    protected String getRootConfigurationName()
    {
        return "/rendition.xml";
    }

    protected OwRenditionService initRendtionService(String mappingPrefix) throws OwException
    {
        OwXMLUtil mappingConf = getProviderConfiguration().getProviderCfgFor(mappingPrefix);
        OwRenditionService service = null;
        if (mappingConf != null)
        {
            String externalResource = mappingConf.getSafeStringAttributeValue(EL_CONFIGURATION, null);
            OwXMLUtil serviceConf = null;
            if (externalResource != null)
            {
                serviceConf = readConfiguration(externalResource);
            }

            try
            {
                if (serviceConf == null)
                {
                    serviceConf = mappingConf.getSubUtil(EL_SERVICE);
                }
                else
                {
                    serviceConf = serviceConf.getSubUtil(EL_SERVICE);
                }
            }
            catch (OwConfigurationException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                String msg = "Could not read inlined configuration for rendition mapping with dmsprefix = " + mappingPrefix;
                LOG.error(msg, e);
                throw new OwConfigurationException(msg, e);
            }

            String className = serviceConf.getSafeStringAttributeValue(AT_CLASS, null);
            if (className != null)
            {
                try
                {
                    service = createRenditionService(className);
                }
                catch (OwServerException e)
                {
                    LOG.error("Could not create renditionservice, will return NoRendtionService", e);
                    service = OwNoRenditionService.INSTANCE;
                }
                if (providerConfiguration != null && service instanceof OwConfigurableRenditionService)
                {
                    ((OwConfigurableRenditionService) service).init(serviceConf);
                }
                renditionServices.put(mappingPrefix, service);
            }

        }
        return service;
    }

    private void initFallbackRendtionService()
    {
        try
        {
            OwXMLUtil serviceConf = getProviderConfiguration().getFallBackCfg();
            if (null == serviceConf)
            {
                return;
            }
            String className = serviceConf.getSafeStringAttributeValue(AT_CLASS, null);
            if (className != null)
            {
                try
                {
                    this.fallbackRendtionService = createRenditionService(className);
                    if (providerConfiguration != null && this.fallbackRendtionService instanceof OwConfigurableRenditionService)
                    {
                        ((OwConfigurableRenditionService) this.fallbackRendtionService).init(serviceConf);
                    }
                }
                catch (OwException e)
                {
                    LOG.error("Could not create fallback renditionservice!", e);
                    this.renditionServices = null;
                }
            }
        }
        catch (OwConfigurationException e)
        {
            LOG.error("Could not initialize the fallback rendition provider.", e);
        }
    }

    @SuppressWarnings("unchecked")
    protected OwRenditionService createRenditionService(String fullServiceName) throws OwServerException
    {
        try
        {
            Class<OwRenditionService> clazz = (Class<OwRenditionService>) Class.forName(fullServiceName);
            OwRenditionService service = clazz.newInstance();
            return service;
        }
        catch (ClassNotFoundException e)
        {
            throw new OwServerException("Could not find class = " + fullServiceName, e);
        }
        catch (InstantiationException e)
        {
            throw new OwServerException("Cannot create instance for class = " + fullServiceName, e);
        }
        catch (IllegalAccessException e)
        {
            throw new OwServerException("Access not possible to class = " + fullServiceName, e);
        }
    }

    protected OwXMLUtil readConfiguration(String resourceName) throws OwConfigurationException
    {
        URL resource = OwSimpleRenditionServiceProvider.class.getResource(resourceName);
        if (resource == null)
        {
            throw new OwConfigurationException("No such configuration resource " + resourceName);
        }

        InputStream is = null;
        OwXMLUtil config = null;
        try
        {

            is = resource.openStream();

            config = new OwStandardOptionXMLUtil(is, EL_CONFIGURATION);

            return config;
        }
        catch (OwConfigurationException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Cannot reaad configuration resource " + resourceName, e);
            throw new OwConfigurationException("Cannot reaad configuration resource.", e);
        }
    }

}
