package com.wewebu.ow.server.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Application objects support collection configuration wrapper. 
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
public class OwSupportsConfiguration extends OwAOConfigurationPart
{
    private static final Logger LOG = OwLogCore.getLogger(OwSupportsConfiguration.class);

    private Map<String, Object> supports;
    private OwXMLUtil configuration;

    public OwSupportsConfiguration(OwXMLUtil configuration, OwAOContext context) throws OwConfigurationException
    {
        super(configuration);
        this.configuration = configuration;
        this.supports = readSupports(configuration, Object.class, context);
    }

    private <S> Map<String, S> readSupports(OwXMLUtil supportsConfiguration, Class<S> supportClass, OwAOContext context) throws OwConfigurationException
    {
        Map<String, S> supports = new HashMap<String, S>();
        List<OwXMLUtil> supportConfigurations = supportsConfiguration.getSafeUtilList(OwAOConstants.CONFIGNODE_SUPPORT);
        for (OwXMLUtil supportConfig : supportConfigurations)
        {
            String accessorClassName = supportConfig.getSafeStringAttributeValue(OwAOConstants.CONFIGATTRIBUTE_CLASS, null);
            String accessorId = supportConfig.getSafeStringAttributeValue(OwAOConstants.CONFIGATTRIBUTE_ID, null);

            if (accessorClassName != null && accessorId != null)
            {
                S accessor = safeInstanceFromName(accessorClassName, supportClass);
                if (accessor instanceof OwConfigurableSupport)
                {
                    OwConfigurableSupport configurableSupport = (OwConfigurableSupport) accessor;
                    configurableSupport.init(new OwSupportConfiguration(supportConfig), context);

                    LOG.debug("Loaded configurable AO support " + accessor);
                }
                else
                {
                    LOG.debug("Loaded non configurable AO support " + accessor);
                }

                supports.put(accessorId, accessor);
            }
            else
            {
                throw new OwConfigurationException("Invalid supports configuration.");
            }
        }

        return supports;
    }

    public <S> S getSupport(String id, Class<S> supportClass) throws OwInvalidOperationException
    {
        Object support = supports.get(id);
        if (!supportClass.isAssignableFrom(support.getClass()))
        {
            throw new OwInvalidOperationException("Unexpected support class " + support.getClass() + ". Requested class was " + supportClass);
        }

        return (S) support;
    }
}
