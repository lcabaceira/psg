package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.server.util.ldap.OwLdapConnector;

/**
 *<p>
 * Abstract instance of OwCMISAuthenticationInterceptor, with helper methods. 
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
public abstract class OwCMISAbstractAuthenticator implements OwCMISAuthenticationInterceptor
{
    private static final String ECM_ADAPTER = "EcmAdapter";

    private OwBaseInitializer baseInitializer;
    private OwCMISNetwork network;

    public OwCMISAbstractAuthenticator(OwBaseInitializer initializer)
    {
        super();
        this.baseInitializer = initializer;
    }

    public OwCMISAbstractAuthenticator(OwCMISNetwork network)
    {
        super();
        this.network = network;
    }

    /**
     * Get configuration helper instance.
     * @return OwCMISNetworkCfg
     * @throws OwConfigurationException
     */
    protected OwCMISNetworkCfg getNetworkConfiguration() throws OwConfigurationException
    {
        if (this.network != null)
        {
            return this.network.getNetworkConfiguration();
        }
        else
        {
            OwXMLUtil bootstrapx = OwConfiguration.getBootstrap(baseInitializer);

            if (bootstrapx == null)
            {
                throw new OwConfigurationException("No bootstrap.");
            }

            try
            {
                return new OwCMISNetworkCfg(bootstrapx.getSubUtil(ECM_ADAPTER));
            }
            catch (Exception e)
            {
                throw new OwConfigurationException("Invalid bootstrap.", e);
            }
        }
    }

    /**
     * Create the OpenCmis parameters which are used for authentication verification. 
     * @param user_p String
     * @param pwd_p String
     * @param currentLocale_p Locale (can be null)
     * @return Map of key and value Strings
     * @throws OwConfigurationException
     */
    public Map<String, String> buildOpenCmisParameters(String user_p, String pwd_p, Locale currentLocale_p) throws OwConfigurationException
    {
        Map<String, String> opencmisParameters = getNetworkConfiguration().getBindingConfig();

        opencmisParameters.put(SessionParameter.USER, user_p);
        opencmisParameters.put(SessionParameter.PASSWORD, pwd_p);

        if (currentLocale_p != null && !"debugmode".equals(currentLocale_p.toString()))
        {
            opencmisParameters.put(SessionParameter.LOCALE_ISO3166_COUNTRY, currentLocale_p.getCountry());
            opencmisParameters.put(SessionParameter.LOCALE_ISO639_LANGUAGE, currentLocale_p.getLanguage());
            opencmisParameters.put(SessionParameter.LOCALE_VARIANT, currentLocale_p.getVariant());
        }

        return opencmisParameters;
    }

    /**
     * Depending on initialization
     * returning the current network or null
     * @return OwCMISNetwork (can return null)
     */
    protected OwCMISNetwork getNetwork()
    {
        return this.network;
    }

    /**
     * Depending on initialization
     * will return the OwBaseInitializer or null 
     * @return OwBaseInitializer (can return null)
     */
    protected OwBaseInitializer getBaseInitialzier()
    {
        return this.baseInitializer;
    }

    /**
     * Factory for LDAP connector instances.
     * @param authenticationConf_p OwAuthenticationConfiguration
     * @return OwLdapConnector
     * @throws OwException
     */
    protected OwLdapConnector createLDAPConnector(OwAuthenticationConfiguration authenticationConf_p) throws OwException
    {
        return new OwLdapConnector(authenticationConf_p.getConfiguration().getNode());
    }

    @Override
    public void init(OwXMLUtil configNode) throws OwException
    {

    }

    @Override
    public AuthenticationProvider createAuthenticationProvider(Map<String, String> information) throws OwException
    {
        return null;
    }

    @Override
    public boolean onRequest(HttpServletRequest req, HttpServletResponse resp) throws OwException, IOException
    {
        return false;
    }

    @Override
    public boolean processRendering(HttpServletRequest req, HttpServletResponse resp)
    {
        return true;
    }

}