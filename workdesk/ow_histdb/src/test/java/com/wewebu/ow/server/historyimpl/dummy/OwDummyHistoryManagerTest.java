package com.wewebu.ow.server.historyimpl.dummy;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import com.wewebu.ow.server.app.OwEcmUtil;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyHistoryManager;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyNetwork;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyObjectFactory;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.history.OwSessionHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass;
import com.wewebu.ow.server.history.OwStandardHistoryPropertyChangeEvent;
import com.wewebu.ow.server.history.OwTouchConfiguration;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.roleimpl.simplerole.OwSimpleRoleManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;
import com.wewebu.ow.unittest.util.OwTestContext;

/**
 *<p>
 * OwDummyHistoryManagerTest.
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
public class OwDummyHistoryManagerTest extends TestCase
{
    private static final String COPY_PLUGIN_EVENT_ID = "copyplugin";
    private static final String DUMMY_PLUGIN_EVENT_ID = "dummmyPlugin";

    public OwDummyHistoryManagerTest(String arg0_p)
    {
        super(arg0_p);
    }

    private OwDummyHistoryManager m_historyManager;
    private OwNetwork m_network;

    private OwObject owObject;

    protected void setUp() throws Exception
    {
        // === get test configuration
        OwXMLUtil config;
        URL resourceURL = OwDummyHistoryManagerTest.class.getResource("owbootstrap_history_dummy.xml");
        config = new OwStandardXMLUtil(resourceURL.openStream(), "bootstrap");

        // === create and init the dummy network adapter
        m_network = new OwDummyNetwork();

        OwTestContext context = new OwTestContext(config, (new File(resourceURL.getFile())).getParent());
        context.setNetwork(m_network);

        m_network.init(context, new OwStandardXMLUtil(config.getSubNode("EcmAdapter")));

        // create history manger
        m_historyManager = new OwDummyHistoryManager();
        m_historyManager.init(context, new OwStandardXMLUtil(config.getSubNode("HistoryManager")));
        m_historyManager.setNetwork(m_network);
        m_network.setEventManager(m_historyManager);

        // === create simple role manger
        OwRoleManager owRoleManager = new OwSimpleRoleManager();
        owRoleManager.init(context, new OwStandardXMLUtil(config.getSubNode("RoleManager")));
        m_network.setRoleManager(owRoleManager);

        // === login
        m_network.loginDefault("", "");
        // login init
        owRoleManager.loginInit();
        owObject = OwDummyObjectFactory.getInstance().create(m_network, new File(resourceURL.getFile()));//new OwDummyFileObject(m_network, new File(resourceURL.getFile()));

        OwEvent event1 = new OwStandardHistoryPropertyChangeEvent(owObject, owObject.getClonedProperties(null));

        m_historyManager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, DUMMY_PLUGIN_EVENT_ID, event1, 1);
        m_historyManager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, DUMMY_PLUGIN_EVENT_ID, event1, 1);
        m_historyManager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, COPY_PLUGIN_EVENT_ID, event1, 1);
    }

    public void testMemoryHistorySearch() throws Exception
    {

        OwSearchNode filter = OwEcmUtil.createSimpleSearchNode(OwSessionHistoryEntry.getStaticObjectClass().getClassName(), null, null, new OwEcmUtil.OwSimpleSearchClause[] {
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, new Integer(OwTouchConfiguration.ALL_EVENT_TYPES)),
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.ID_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, OwTouchConfiguration.ALL_EVENT_IDS) }, m_historyManager);

        OwObjectCollection result = m_historyManager.doObjectSearch(owObject, filter, null, null, null, 1);
        assertEquals(3, result.size());

    }

    public void testInMemoryInvokeViewEventType() throws Exception
    {

        OwSearchNode filter = OwEcmUtil.createSimpleSearchNode(OwSessionHistoryEntry.getStaticObjectClass().getClassName(), null, null, new OwEcmUtil.OwSimpleSearchClause[] {
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, new Integer(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW)),
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.ID_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, OwTouchConfiguration.ALL_EVENT_IDS) }, m_historyManager);

        OwObjectCollection result = m_historyManager.doObjectSearch(owObject, filter, null, null, null, 1);
        assertEquals(1, result.size());

    }

    public void testInMemoryInvokeEditEventType() throws Exception
    {

        OwSearchNode filter = OwEcmUtil.createSimpleSearchNode(OwSessionHistoryEntry.getStaticObjectClass().getClassName(), null, null, new OwEcmUtil.OwSimpleSearchClause[] {
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, new Integer(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT)),
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.ID_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, OwTouchConfiguration.ALL_EVENT_IDS) }, m_historyManager);

        OwObjectCollection result = m_historyManager.doObjectSearch(owObject, filter, null, null, null, 1);
        assertEquals(2, result.size());

    }

    public void testInMemoryDummyPluginID() throws Exception
    {

        OwSearchNode filter = OwEcmUtil.createSimpleSearchNode(OwSessionHistoryEntry.getStaticObjectClass().getClassName(), null, null, new OwEcmUtil.OwSimpleSearchClause[] {
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, new Integer(OwTouchConfiguration.ALL_EVENT_TYPES)),
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.ID_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, DUMMY_PLUGIN_EVENT_ID) }, m_historyManager);

        OwObjectCollection result = m_historyManager.doObjectSearch(owObject, filter, null, null, null, 1);
        assertEquals(2, result.size());

    }

    public void testInMemoryCopyPluginID() throws Exception
    {

        OwSearchNode filter = OwEcmUtil.createSimpleSearchNode(OwSessionHistoryEntry.getStaticObjectClass().getClassName(), null, null, new OwEcmUtil.OwSimpleSearchClause[] {
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, new Integer(OwTouchConfiguration.ALL_EVENT_TYPES)),
                new OwEcmUtil.OwSimpleSearchClause(OwStandardHistoryEntryObjectClass.ID_PROPERTY, OwSearchOperator.CRIT_OP_EQUAL, COPY_PLUGIN_EVENT_ID) }, m_historyManager);

        OwObjectCollection result = m_historyManager.doObjectSearch(owObject, filter, null, null, null, 1);
        assertEquals(1, result.size());

    }

}
