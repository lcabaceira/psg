package com.wewebu.ow.server.ecmimpl.fncm5;

import org.junit.After;
import org.junit.Before;

import com.wewebu.ow.server.app.OwUserOperationEvent;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NetworkCreateManager;

/**
 *<p>
 * OwFNCM5IntegrationTest.
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
public abstract class OwFNCM5IntegrationTest
{
    protected OwFNCM5Network network;
    protected OwFNCM5NetworkCreateManager createManager = null;

    @Before
    public void setUp() throws Exception
    {
        network = new OwFNCM5NetworkManager().createLoggedInNetwork(getBootstrap());
        network.operationPerformed(new OwUserOperationEvent(null, OwUserOperationEvent.OwUserOperationType.START, null));
        createManager = new OwFNCM5NetworkCreateManager(this.network);
    }

    protected String getBootstrap()
    {
        return "/bootstrap.xml";
    }

    @After
    public void tearDown() throws Exception
    {
        if (network != null)
        {
            network.logout();
            network.operationPerformed(new OwUserOperationEvent(null, OwUserOperationEvent.OwUserOperationType.STOP, null));
            network = null;
        }
    }
}
