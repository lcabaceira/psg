package com.wewebu.ow.server.ecmimpl.opencmis.auth;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.auth.OwAuthentication;
import com.wewebu.ow.server.auth.OwAuthenticationContext;
import com.wewebu.ow.server.auth.OwCredentialsAuthentication;
import com.wewebu.ow.server.ecm.OwAuthenticationProvider;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.ui.OwCMISUILoginModule;
import com.wewebu.ow.server.ecmimpl.opencmis.ui.OwCMISUserSelectionModule;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Default authentication provider for the Open CMIS adaptor.
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
public class OwCMISDefaultAuthenticationProvider<U extends OwUsersRepository> implements OwAuthenticationProvider
{
    private static final Logger LOG = OwLog.getLogger(OwCMISDefaultAuthenticationProvider.class);

    private OwCMISCredentials credentials;
    private OwMandator mandator;
    protected OwCMISNetwork network;
    private U userRepository;

    public OwCMISDefaultAuthenticationProvider(OwCMISNetwork network)
    {
        this.network = network;
        this.mandator = this.network.getContext().getMandator();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public OwUILoginModul getLoginSubModul() throws OwException
    {
        OwCMISUILoginModule module = new OwCMISUILoginModule();
        try
        {
            module.init(this.network);
        }
        catch (OwException e)
        {
            throw e;
        }
        return module;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws OwException
    {
        if (canUserSelect())
        {
            OwCMISUserSelectionModule module = new OwCMISUserSelectionModule(getUserRepository());
            module.setFilter(types_p);
            module.setCurrentUserID(strID_p);
            try
            {
                module.init(this.network);
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                String msg = "Cannot initialize the User selection module";
                LOG.error(msg, e);
                throw new OwServerException(this.network.getContext().localize("opencmis.OwCMISNetwork.err.initUserSelectEx", "Cannot initialize the User Selection dialog."), e);
            }
            return module;
        }
        else
        {
            OwString errMessage = new OwString("opencmis.auth.OwCMISDefaultAuthenticationProvider.err.userSelection.notImplemented", "User selection is not supported in this implementation!");
            throw new OwInvalidOperationException(errMessage);
        }
    }

    @Override
    public boolean canUserSelect() throws OwException
    {
        return getUserRepository() != null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwAuthenticationProvider#getCredentials()
     */
    @Override
    public OwCredentials getCredentials() throws OwException
    {
        return this.credentials;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwAuthenticationProvider#getUserFromID(java.lang.String)
     */
    @Override
    public OwUserInfo getUserFromID(String strID_p) throws OwException
    {
        if (getUserRepository() == null)
        {
            throw new OwNotSupportedException("This method is not supported, use canUserSelect to verify capability.");
        }
        else
        {
            return getUserRepository().findUserByID(strID_p);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwAuthenticationProvider#getRoleDisplayName(java.lang.String)
     */
    @Override
    public String getRoleDisplayName(String strRoleName_p) throws OwException
    {
        // TODO retrieve display name if possible
        return strRoleName_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwAuthenticationProvider#logout()
     */
    @Override
    public void logout() throws OwException
    {
        if (this.credentials != null)
        {
            this.credentials.invalidate();
            this.credentials = null;
        }
        this.userRepository = null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwAuthenticationProvider#loginDefault(java.lang.String, java.lang.String)
     */
    @Override
    public void loginDefault(String strUser_p, String strPassword_p) throws OwException
    {
        OwAuthenticationContext authenticationContext = this.network.getAuthenticationContext();
        if (authenticationContext.getAuthentication() == null)
        {
            OwCMISAuthenticationInterceptor authenticator = createAuthenticator();
            OwAuthentication authentication = new OwCredentialsAuthentication(null, strUser_p, strPassword_p);
            authentication = authenticator.authenticate(authentication);

            if (authentication.isAuthenticated())
            {
                OwCMISCredentials credentials = (OwCMISCredentials) authentication.getOWDCredentials();

                credentials.setMandator(this.mandator);
                this.credentials = credentials;

                authenticationContext.setAuthentication(authentication);

            }
        }
        else
        {
            this.credentials = (OwCMISCredentials) authenticationContext.getAuthentication().getOWDCredentials();
        }
        U repo = createUserRepository(this.credentials);
        this.credentials.setUserRepository(repo);
        setUserRepository(repo);
    }

    protected OwCMISAuthenticationInterceptor createAuthenticator() throws OwException
    {
        return this.network.getAuthInterceptor();
    }

    /**
     * Will return a repository to handle search for User and/or Groups.
     * @return OwUsersRepository or null if no User selection available.
     * @see #canUserSelect()
     * @since 4.1.1.1
     */
    public U getUserRepository()
    {
        return this.userRepository;
    }

    /**
     * Setter for userRepository 
     * @param userRepository
     * @since 4.1.1.1
     */
    protected void setUserRepository(U userRepository)
    {
        this.userRepository = userRepository;
    }

    /**
     * Factory to create an instance of OwUsersRepository responsible for UserSelection capability.
     * @param credentials OwCMISCredentials
     * @return OwUsersRepository or null if capability is not available
     * @throws OwException
     * @since 4.2.0.0
     */
    protected U createUserRepository(OwCMISCredentials credentials) throws OwException
    {
        return null;
    }
}