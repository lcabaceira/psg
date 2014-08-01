package com.wewebu.ow.server.ecmimpl.fncm5;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.fncm5.unittest.OwFCM5TestNetworkContext;
import com.wewebu.ow.server.ecmimpl.fncm5.unittest.log.JUnitLogger;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.historyimpl.simplehistory.OwSimpleHistoryManager;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.roleimpl.simplerole.OwSimpleRoleManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwFNCM5NetworkManager.
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
public class OwFNCM5NetworkManager
{
    private static final Logger LOG = JUnitLogger.getLogger(OwFNCM5NetworkManager.class);

    private static final String CLASS_NAME = "ClassName";
    private static final String ROLE_MANAGER = "RoleManager";
    private static final String HISTORY_MANAGER = "HistoryManager";
    private static final String ECM_ADAPTER = "EcmAdapter";
    private static final String DEFAULT_BOOTSTRAP_XML = "/bootstrap.xml";
    String loginUSR;
    String loginPWD;
    private OwStandardXMLUtil cfg;

    public OwFNCM5Network createdInitializedNetwork() throws OwException
    {
        return createdInitializedNetwork(DEFAULT_BOOTSTRAP_XML);
    }

    /**
     * Will create an instance of OwFNCM5Network and provide them a
     * context and a configuration node.
     * @return OwFNCM5Network which is initialized
     * @throws OwException if cannot read configuration
     */
    public OwFNCM5Network createdInitializedNetwork(String bootstrap_p) throws OwException
    {
        OwFNCM5Network network = new OwFNCM5Network();
        OwStandardXMLUtil util;
        OwStandardXMLUtil config = null;
        try
        {
            config = getConfig(bootstrap_p);
            util = config;
            OwXMLUtil login = util.getSubUtil("login");
            if (login != null)
            {
                loginUSR = login.getSafeTextValue("username", null);
                loginPWD = login.getSafeTextValue("password", null);
            }
        }
        catch (Exception e1)
        {
            throw new OwInvalidOperationException("Cannot create bootstrap configuration util");
        }

        try
        {
            OwFCM5TestNetworkContext context = new OwFCM5TestNetworkContext(network, config);
            network.init(context, util.getSubUtil(ECM_ADAPTER));

            // === create simple history manger
            OwHistoryManager eventManager = new OwSimpleHistoryManager();
            eventManager.init(context, config.getSubUtil(HISTORY_MANAGER));
            eventManager.setNetwork(network);
            network.setEventManager(eventManager);

            // === create simple role manger

            OwXMLUtil roleManagerConfiguration = config.getSubUtil(ROLE_MANAGER);
            OwRoleManager owRoleManager = null;
            if (roleManagerConfiguration != null)
            {
                OwStandardXMLUtil roleManagerClassConfig = new OwStandardXMLUtil(roleManagerConfiguration.getSubNode(CLASS_NAME));

                String roleManagerClassName = roleManagerClassConfig.getSafeTextValue(OwSimpleRoleManager.class.getCanonicalName());
                LOG.debug("TEST CONFIG ROLEMANAGER CLASS IS [" + roleManagerClassName + "]");

                Class<?> roleManagerClass = Class.forName(roleManagerClassName);

                owRoleManager = (OwRoleManager) roleManagerClass.newInstance();
            }
            else
            {
                throw new OwConfigurationException("Role manager is missing from the test configuration " + bootstrap_p);
            }

            owRoleManager.init(context, roleManagerConfiguration);
            network.setRoleManager(owRoleManager);
            context.setRoleManager(owRoleManager);

        }
        catch (Exception e)
        {
            LOG.error("Error creating test nertwork.", e);
            throw new OwInvalidOperationException("Could not create network.", e);
        }

        return network;
    }

    private synchronized OwStandardXMLUtil getConfig(String bootstrap_p) throws Exception
    {
        if (this.cfg == null)
        {
            this.cfg = new OwStandardXMLUtil(OwFNCM5NetworkManager.class.getResourceAsStream(bootstrap_p), null);
        }

        return this.cfg;
    }

    /**
     * This method will first call {@link #createdInitializedNetwork()} 
     * and afterwards process a default login.
     * <p>
     *  If the method parameter are null, the manager will use the 
     *  defined credential's from configuration.
     * </p>
     * @param user String user name to use, can be null (then will use from bootstrap.xml) 
     * @param pwd String password to use, can be null (then will use from bootstrap.xml)
     * @return OwFNCM5Network which already is logged in
     * @throws OwException if initialization or login fails
     */
    public OwFNCM5Network createLoggedInNetwork(String bootstrap_p, String user, String pwd) throws OwException
    {
        OwFNCM5Network network = createdInitializedNetwork(bootstrap_p);
        if (user != null && pwd != null)
        {
            network.loginDefault(user, pwd);
        }
        else
        {
            network.loginDefault(this.loginUSR, this.loginPWD);
        }
        ((OwFCM5TestNetworkContext) network.getContext()).loginInit();

        return network;
    }

    public OwFNCM5Network createLoggedInNetwork() throws OwException
    {
        return createLoggedInNetwork(DEFAULT_BOOTSTRAP_XML);
    }

    /**
     * Use the configuration credential's to create an network instance.
     * @return OwFNCM5Network which already processed a login
     * @throws OwException
     */
    public OwFNCM5Network createLoggedInNetwork(String bootstrap_p) throws OwException
    {
        return createLoggedInNetwork(bootstrap_p, null, null);
    }

}
