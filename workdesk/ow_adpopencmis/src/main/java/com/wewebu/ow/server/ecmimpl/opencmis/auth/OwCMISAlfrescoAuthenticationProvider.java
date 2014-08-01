package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;

import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepositoryAlfresco;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwAuthenticationConfiguration;

/**
 *<p>
 * Alfresco based authentication provider for the Open CMIS adapter.
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
 */
public class OwCMISAlfrescoAuthenticationProvider extends OwCMISDefaultAuthenticationProvider<OwUsersRepositoryAlfresco>
{

    public OwCMISAlfrescoAuthenticationProvider(OwCMISNetwork network)
    {
        super(network);
    }

    @Override
    public void loginDefault(String strUser_p, String strPassword_p) throws OwException
    {
        OwAuthenticationConfiguration authenticationConfiguration = this.network.getNetworkConfiguration().getAuthenticationConfiguration();
        try
        {
            authenticationConfiguration.getConfiguration().getSubNode("BaseURL").getTextContent();
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not get BaseUrl from configuration!", e);
        }
        super.loginDefault(strUser_p, strPassword_p);
    }

    /**
     * Factory to create a new instance of OwUsersRepository based on Alfresco Services.
     * @param conf OwAuthenticationConfiguration
     * @param cred OwCredentials
     * @param interceptor OwCMISAuthenticationInterceptor
     * @return OwUsersRepositoryAlfresco
     * @deprecated since 4.2.0.0 use {@link #createUserRepository(OwCMISCredentials)} instead
     */
    protected OwUsersRepositoryAlfresco createUserRepositoryInstance(OwAuthenticationConfiguration conf, OwCredentials cred, OwCMISAuthenticationInterceptor interceptor)
    {
        return new OwUsersRepositoryAlfresco(conf, null);
    }

    @Override
    protected OwUsersRepositoryAlfresco createUserRepository(OwCMISCredentials credentials) throws OwException
    {
        OwRestletAuthenticationHandler auth = createAuthenticator().createRestletAuthenticationHandler(credentials);
        OwAuthenticationConfiguration authenticationConfiguration = this.network.getNetworkConfiguration().getAuthenticationConfiguration();
        return new OwUsersRepositoryAlfresco(authenticationConfiguration, auth);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.auth.OwCMISDefaultAuthenticationProvider#getUserFromID(java.lang.String)
     */
    @Override
    public OwUserInfo getUserFromID(String strID_p) throws OwException
    {
        return getUserRepository().findUserByID(strID_p);
    }

}
