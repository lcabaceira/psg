package com.wewebu.ow.server.historyimpl.dummy;

import java.io.StringReader;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.history.OwTouchConfiguration;

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
public class TestOwTouchConfiguration extends TestCase
{
    private static String SESSION_EVENTS_XML_WRONG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><SessionEvents> <Event  eventtype=\"HISTORY_EVENT_TYPE_VIEW\" eventid=\"*\">/design/view.gif</Event>   <Event  eventtype=\"HISTORY_EVENT_TYPE_EDIT\" eventid=\"*\">/design/pencil2.png</Event></SessionEvents></root>";
    private static String SESSION_EVENTS_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><SessionEvents> <Event  eventtype=\"HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW\" eventid=\"*\">/design/view.gif</Event>   <Event  eventtype=\"HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT\" eventid=\"*\">/design/pencil2.png</Event></SessionEvents></root>";
    private static String SESSION_EVENTS_XML_REFLECTION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><SessionEvents> <Event  eventtype=\"2\" eventid=\"*\">/design/view.gif</Event>   <Event  eventtype=\"3\" eventid=\"search\">/design/pencil2.png</Event><Event  eventtype=\"HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI\" eventid=\"HISTORY_EVENT_ID_NEW_OBJECT\">/design/pencil2.png</Event></SessionEvents></root>";
    private static String SESSION_EVENTS_XML_MULTIPLE_EVENT_TYPES = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><SessionEvents> <Event  eventtype=\"HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW\" eventid=\"*\">/design/view.gif</Event>   <Event  eventtype=\"HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW\" eventid=\"search\">/design/edit1.gif</Event><Event  eventtype=\"HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW\" eventid=\"com.ui.editproperties\">/design/editprops.gif</Event></SessionEvents></root>";
    private Node testSessionNodeWrong;
    private Node testSessionNode;
    private Node testSessionReflectionNode;
    private Node testSessionMultipleEventConf;

    protected void setUp() throws Exception
    {
        super.setUp();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document sessionEventsDoc = builder.parse(new InputSource(new StringReader(SESSION_EVENTS_XML_WRONG)));
        testSessionNodeWrong = sessionEventsDoc.getDocumentElement().getElementsByTagName("SessionEvents").item(0);
        sessionEventsDoc = builder.parse(new InputSource(new StringReader(SESSION_EVENTS_XML)));
        testSessionNode = sessionEventsDoc.getDocumentElement().getElementsByTagName("SessionEvents").item(0);
        sessionEventsDoc = builder.parse(new InputSource(new StringReader(SESSION_EVENTS_XML_REFLECTION)));
        testSessionReflectionNode = sessionEventsDoc.getDocumentElement().getElementsByTagName("SessionEvents").item(0);
        sessionEventsDoc = builder.parse(new InputSource(new StringReader(SESSION_EVENTS_XML_MULTIPLE_EVENT_TYPES)));
        testSessionMultipleEventConf = sessionEventsDoc.getDocumentElement().getElementsByTagName("SessionEvents").item(0);

    }

    public void testInit() throws OwConfigurationException
    {
        OwTouchConfiguration conf = null;
        try
        {
            new OwTouchConfiguration(true, testSessionNodeWrong, Locale.ENGLISH);
            fail("Should not be here");
        }
        catch (OwConfigurationException e)
        {

        }
        conf = new OwTouchConfiguration(true, testSessionNode, Locale.ENGLISH);
        assertNotNull(conf);
    }

    public void testGetIconForEventType() throws OwConfigurationException
    {
        OwTouchConfiguration conf = new OwTouchConfiguration(true, testSessionNode, Locale.ENGLISH);
        assertEquals("/design/view.gif", conf.getIconForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_EVENT_ID_NEW_OBJECT));
        assertEquals("/design/pencil2.png", conf.getIconForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_EVENT_ID_NEW_OBJECT));
    }

    public void testGetEventIdForEventType() throws OwConfigurationException
    {
        OwTouchConfiguration conf = new OwTouchConfiguration(true, testSessionNode, Locale.ENGLISH);
        assertEquals(OwTouchConfiguration.ALL_EVENT_IDS, conf.getEventIdsForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW)[0]);
        assertEquals(OwTouchConfiguration.ALL_EVENT_IDS, conf.getEventIdsForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT)[0]);
    }

    public void testUsingReflectionIconsForEventType() throws OwConfigurationException
    {
        OwTouchConfiguration conf = new OwTouchConfiguration(true, testSessionReflectionNode, Locale.ENGLISH);
        assertEquals("/design/view.gif", conf.getIconForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwEventManager.HISTORY_EVENT_ID_NEW_OBJECT));
        assertEquals("/design/pencil2.png", conf.getIconForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT, OwEventManager.HISTORY_EVENT_ID_SEARCH));
    }

    public void testGetEventIdForEventTypeReflection() throws OwConfigurationException
    {
        OwTouchConfiguration conf = new OwTouchConfiguration(true, testSessionReflectionNode, Locale.ENGLISH);
        assertEquals(OwTouchConfiguration.ALL_EVENT_IDS, conf.getEventIdsForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW)[0]);
        assertEquals(OwEventManager.HISTORY_EVENT_ID_SEARCH, conf.getEventIdsForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT)[0]);
        assertEquals(OwEventManager.HISTORY_EVENT_ID_NEW_OBJECT, conf.getEventIdsForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI)[0]);
    }

    public void testGetEventIdForMultipleEventType() throws OwConfigurationException
    {
        OwTouchConfiguration conf = new OwTouchConfiguration(true, testSessionMultipleEventConf, Locale.ENGLISH);
        assertEquals("/design/view.gif", conf.getIconForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, OwTouchConfiguration.ALL_EVENT_IDS));
        assertEquals("/design/edit1.gif", conf.getIconForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, "search"));
        assertEquals("/design/editprops.gif", conf.getIconForEventType(OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW, "com.ui.editproperties"));
    }

}
