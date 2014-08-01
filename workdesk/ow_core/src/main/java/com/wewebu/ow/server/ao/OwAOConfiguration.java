package com.wewebu.ow.server.ao;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 *Application objects configuration wrapper. 
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
public class OwAOConfiguration
{
    private static final Logger LOG = OwLogCore.getLogger(OwAOConfiguration.class);

    private OwSupportsConfiguration supportsConfiguration;
    private OwManagersConfiguration managersConfiguration;
    private OwXMLUtil configuration;

    public OwAOConfiguration(OwXMLUtil configuration, OwAOContext context) throws OwConfigurationException
    {
        this.configuration = configuration;

        try
        {
            OwXMLUtil suportsConfiguration = configuration.getSubUtil(OwAOConstants.CONFIGNODE_SUPPORTS);
            this.supportsConfiguration = new OwSupportsConfiguration(suportsConfiguration, context);

            OwXMLUtil managersConfiguration = configuration.getSubUtil(OwAOConstants.CONFIGNODE_MANAGERS);
            this.managersConfiguration = new OwManagersConfiguration(managersConfiguration, supportsConfiguration, context);
        }
        catch (OwConfigurationException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Invalid AO configuration.", e);
        }

    }

    public OwAOManager getManager(int type_p) throws OwInvalidOperationException
    {
        return managersConfiguration.getManager(type_p);
    }
}
