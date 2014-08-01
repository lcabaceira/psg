package com.wewebu.ow.server.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.ecmimpl.OwAOTypesEnum;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Application objects manager collection configuration wrapper. 
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

public class OwManagersConfiguration extends OwAOConfigurationPart
{
    private static final Logger LOG = OwLogCore.getLogger(OwManagersConfiguration.class);

    private OwXMLUtil configuration;
    private Map<Integer, OwAOManager> managers;

    public OwManagersConfiguration(OwXMLUtil configuration, OwSupportsConfiguration supportsConfiguration, OwAOContext context) throws OwConfigurationException
    {
        super(configuration);
        this.configuration = configuration;
        this.managers = readManagers(configuration, supportsConfiguration, context);

    }

    private Map<Integer, OwAOManager> readManagers(OwXMLUtil managersConfiguration, OwSupportsConfiguration supports, OwAOContext context) throws OwConfigurationException
    {
        Map<Integer, OwAOManager> managers = new HashMap<Integer, OwAOManager>();

        List<OwXMLUtil> managerConfigurations = managersConfiguration.getSafeUtilList(OwAOConstants.CONFIGNODE_MANAGER);
        for (OwXMLUtil managerConfig : managerConfigurations)
        {
            String managerClassName = managerConfig.getSafeStringAttributeValue(OwAOConstants.CONFIGATTRIBUTE_CLASS, null);
            String managerType = managerConfig.getSafeStringAttributeValue(OwAOConstants.CONFIGATTRIBUTE_TYPE, null);

            if (managerClassName != null)
            {
                try
                {
                    OwAOManager manager = safeInstanceFromName(managerClassName, OwAOManager.class);
                    OwAOType<?> aoType = OwAOConstants.fromType(OwAOTypesEnum.valueOf(managerType));
                    if (manager instanceof OwConfigurableManager)
                    {
                        OwConfigurableManager configurableManager = (OwConfigurableManager) manager;

                        configurableManager.init(aoType, new OwManagerConfiguration(managerConfig), supports, context);

                        LOG.debug("Loaded configurable AO manager " + manager);
                    }
                    else
                    {
                        LOG.debug("Loaded non configurable AO manager " + manager);
                    }

                    if (managers.containsKey(aoType.getType()))
                    {
                        String msg = "Mutiple managers are registered for the same application object type " + aoType.getType();
                        LOG.error("OwStandardRegistry.registerManager():" + msg);
                        throw new OwConfigurationException(new OwString("ecmimpl.OwAOManager.registry.error", "Invalid application object manager registry operation!"));
                    }
                    managers.put(aoType.getType(), manager);
                }
                catch (OwInvalidOperationException e)
                {
                    throw new OwConfigurationException("Could not register manager.", e);
                }
            }
            else
            {
                throw new OwConfigurationException("Invalid managers configuration.");
            }
        }

        return managers;
    }

    public OwAOManager getManager(int type_p) throws OwInvalidOperationException
    {
        OwAOManager manager = this.managers.get(type_p);
        if (manager == null)
        {
            String msg = "No managers for application objects of type " + type_p + "=" + OwAOTypesEnum.fromType(type_p).name() + " are registered!";
            LOG.error("OwManagersConfiguration.getManager():" + msg);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.registry.error", "Invalid application object manager registry operation!"));
        }
        return manager;
    }

}
