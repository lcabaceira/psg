package com.wewebu.service.rendition.impl;

import java.util.HashMap;
import java.util.List;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Class to wrap the configuration XML of a RenditionServiceProvider. 
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
public class OwProviderConfiguration
{
    private static final String EL_MAPPING = "mapping";
    private static final String EL_NETWORK = "network";
    private static final String AT_DMSPREFIX = "dmsPrefix";
    private static final String EL_FALLBACK_SERVICE = "fallback-service";

    private HashMap<String, OwXMLUtil> providerMapping;
    private OwXMLUtil fallBackCfg;

    private OwProviderConfiguration(HashMap<String, OwXMLUtil> providerMapping, OwXMLUtil fallBackCfg)
    {
        this.providerMapping = providerMapping;
        this.fallBackCfg = fallBackCfg;
    }

    @SuppressWarnings("unchecked")
    public static OwProviderConfiguration fromXML(OwXMLUtil rootConf) throws OwConfigurationException
    {
        HashMap<String, OwXMLUtil> providerMapping = new HashMap<String, OwXMLUtil>();
        List<OwXMLUtil> mappings = rootConf.getSafeUtilList(EL_MAPPING, EL_NETWORK);
        for (OwXMLUtil network : mappings)
        {
            String prefix = network.getSafeStringAttributeValue(AT_DMSPREFIX, null);
            providerMapping.put(prefix, network);
        }
        OwXMLUtil fallBackXML = null;
        try
        {
            fallBackXML = rootConf.getSubUtil(EL_FALLBACK_SERVICE);
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not read configuration for the fallback rendition service provider.", e);
        }

        return new OwProviderConfiguration(providerMapping, fallBackXML);
    }

    /**
     * @param mappingPrefix
     * @return the XML provider configuration for the given prefix.
     */
    public OwXMLUtil getProviderCfgFor(String mappingPrefix)
    {
        return this.providerMapping.get(mappingPrefix);
    }

    /**
     * @return the fallBackCfg
     */
    public OwXMLUtil getFallBackCfg()
    {
        return fallBackCfg;
    }
}
