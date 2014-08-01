package com.wewebu.ow.server.roleimpl.dbrole.manager;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecm.OwNetwork;

/**
 *<p>
 * OwDBRoleManagerTestManager.
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
public class OwDBRoleManagerTestManager extends TestCase
{

    //private static Logger LOG = Logger.getLogger("OwDBRoleManagerManager");

    // protected OwFNCMNetwork m_network;
    private OwNetwork m_network = null;

    public OwDBRoleManagerTestManager(OwNetwork network_p)
    {
        m_network = network_p;
    }

    public OwNetwork getM_network()
    {
        return m_network;
    }

    public void setM_network(OwNetwork network_p)
    {
        this.m_network = network_p;
    }

}
