package com.wewebu.ow.server.historyimpl.dbhistory.manager;

import junit.framework.TestCase;

import com.wewebu.ow.server.ecm.OwNetwork;

/**
*<p>
* OwDBHistoryTestManager. 
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
public class OwDBHistoryTestManager extends TestCase
{

    //private static Logger LOG = Logger.getLogger("OwDBHistoryTestManager");

    // protected OwFNCMNetwork m_network;
    private OwNetwork m_network = null;

    public OwDBHistoryTestManager()
    {
        super("OwDBHistoryTestManager");
    }

    public OwDBHistoryTestManager(OwNetwork network_p)
    {
        m_network = network_p;
    }

    public OwNetwork getM_network()
    {
        return m_network;
    }

    public void setM_network(OwNetwork m_network_p)
    {
        this.m_network = m_network_p;
    }
}
