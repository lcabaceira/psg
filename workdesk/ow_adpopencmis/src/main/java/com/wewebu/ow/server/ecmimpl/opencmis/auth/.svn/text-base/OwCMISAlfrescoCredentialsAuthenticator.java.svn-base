package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.auth.OwAuthentication;
import com.wewebu.ow.server.conf.OwBaseInitializer;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepositoryAlfresco;
import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;

/**
 *<p>
 * Alfresco based implementation of the CredentialAuthenticator. 
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
 *@since 4.1.1.0
 *@deprecated since 4.2.0.0 logic moved to OwCMISAlfrescoAuthenticationProvider
 */
public class OwCMISAlfrescoCredentialsAuthenticator extends OwCMISCredentialsAuthenticator
{
    private static final Logger LOG = OwLogCore.getLogger(OwCMISAlfrescoCredentialsAuthenticator.class);
    private OwUsersRepositoryAlfresco userRepository;

    public OwCMISAlfrescoCredentialsAuthenticator(OwCMISNetwork network, OwUsersRepositoryAlfresco userRepository)
    {
        super(network);
        this.userRepository = userRepository;
    }

    public OwCMISAlfrescoCredentialsAuthenticator(OwBaseInitializer initializer)
    {
        super(initializer);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentialsAuthenticator#createCredentials(java.lang.String, java.lang.String, org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider)
     */
    @Override
    protected OwCMISCredentials createCredentials(String user_p, String password_p, AuthenticationProvider nativeAuthProvider) throws OwException
    {
        return new OwCMISAlfrescoCredentials(user_p, password_p, nativeAuthProvider, getUserRepositoryInstance(user_p, password_p));
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISCredentialsAuthenticator#authenticate(com.wewebu.ow.server.auth.OwAuthentication)
     */
    @Override
    public OwAuthentication authenticate(OwAuthentication authentication_p) throws OwAuthenticationException, OwConfigurationException, OwServerException
    {
        return super.authenticate(authentication_p);
    }

    protected OwUsersRepositoryAlfresco getUserRepositoryInstance(String strUser_p, String strPassword_p) throws OwConfigurationException
    {
        if (this.userRepository == null)
        {
            OwAuthenticationConfiguration authenticationConfiguration = getNetworkConfiguration().getAuthenticationConfiguration();
            try
            {
                authenticationConfiguration.getConfiguration().getSubNode("BaseURL").getTextContent();
            }
            catch (Exception e)
            {
                String msg = "Could not get BaseUrl from configuration!";
                LOG.error(msg, e);
                throw new OwConfigurationException(msg, e);
            }
            this.userRepository = new OwUsersRepositoryAlfresco(authenticationConfiguration, null);
        }

        return this.userRepository;
    }

}
