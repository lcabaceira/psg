package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.auth.OwAuthentication;
import com.wewebu.ow.server.auth.OwAuthenticator;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Factory creating corresponding OwAuthenticator.<br/>
 * Will create and delegate authentication based on current configuration. 
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
public class OwCMISAuthenticationFactoryAuthenticator implements OwAuthenticator
{
    private OwBaseInitializer initializer;

    public OwCMISAuthenticationFactoryAuthenticator(OwBaseInitializer initializer)
    {
        this.initializer = initializer;
    }

    @Override
    public OwAuthentication authenticate(OwAuthentication authentication) throws OwAuthenticationException, OwConfigurationException, OwServerException
    {
        OwCMISCredentialsAuthenticator currentAuthenticator = createAuthenticator(authentication);
        return currentAuthenticator.authenticate(authentication);
    }

    protected OwCMISCredentialsAuthenticator createAuthenticator(OwAuthentication authentication) throws OwConfigurationException
    {
        OwAuthenticationConfiguration authConf = getNetworkConfiguration().getAuthenticationConfiguration();
        if (authConf.getMode() != null)
        {
            return new OwCMISCredentialsAuthenticator(this.initializer);
        }
        else
        {
            String msg = "Invalid CMIS authentication configuration! Invalid <Authentication> @mode : " + authConf.getMode();
            throw new OwConfigurationException(msg);
        }
    }

    /**
     * Get current configuration from current OwBaseInitializer instance.
     * @return OwCMISNetworkCfg
     * @throws OwConfigurationException if configuration can not be found or read.
     */
    protected OwCMISNetworkCfg getNetworkConfiguration() throws OwConfigurationException
    {
        OwXMLUtil bootstrapx = OwConfiguration.getBootstrap(this.initializer);

        if (bootstrapx == null)
        {
            throw new OwConfigurationException("No bootstrap configuration found/available");
        }

        try
        {
            return new OwCMISNetworkCfg(bootstrapx.getSubUtil("EcmAdapter"));
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Invalid bootstrap.", e);
        }
    }
}
