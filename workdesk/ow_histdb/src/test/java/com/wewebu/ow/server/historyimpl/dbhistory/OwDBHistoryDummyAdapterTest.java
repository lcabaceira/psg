package com.wewebu.ow.server.historyimpl.dbhistory;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.history.OwHistoryEntry;
import com.wewebu.ow.server.history.OwHistoryModifiedPropertyValue;
import com.wewebu.ow.server.history.OwStandardHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryObjectChangeEvent;
import com.wewebu.ow.server.history.OwStandardHistoryPropertyChangeEvent;
import com.wewebu.ow.server.util.OwEscapedStringTokenizer;
import com.wewebu.ow.unittest.util.OwTestFileObject;

/**
 *<p>
 * DBHistory Test Case: Test all history functions against dummy adapter
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
public abstract class OwDBHistoryDummyAdapterTest extends OwDBHistoryTestBase
{
    //private static final Logger LOG = Logger.getLogger(OwDBHistoryDummyAdapterTest.class);

    public OwDBHistoryDummyAdapterTest(String arg0_p)
    {
        super(arg0_p);
    }

    /** add and lookup standard event
     * 
     * @throws Exception
     */
    public void testStandardEvents() throws Exception
    {
        String sID = OwEventManager.HISTORY_EVENT_ID_LOGIN;
        int iEventType = OwEventManager.HISTORY_EVENT_TYPE_GENERIC;

        int iSize = findStandardEvents(sID, iEventType).size();
        m_historyManager.addEvent(iEventType, sID, OwEventManager.HISTORY_STATUS_OK);
        int iNewSize = findStandardEvents(sID, iEventType).size();

        assertEquals(iSize + 1, iNewSize);

    }

    /** add and lookup object event
     * 
     * @throws Exception
     */
    public void testObjectEvents() throws Exception
    {
        String sID = OwEventManager.HISTORY_EVENT_ID_OBJECT_DELETE;
        int iEventType = OwEventManager.HISTORY_EVENT_TYPE_OBJECT;

        // create unique DMSID for later retrieval
        String sDMSID = "JUNIT,testObjectEvents," + String.valueOf((System.currentTimeMillis()));

        // create a dummy object
        OwTestFileObject obj = new OwTestFileObject(Locale.GERMAN, m_historyManager, new File(getConfigDir() + "testobject.txt"), sDMSID);
        OwTestFileObject parent = new OwTestFileObject(Locale.GERMAN, m_historyManager, new File(getConfigDir() + "testobject.txt"), sDMSID);

        // get initial sizes
        int iSize = findStandardEvents(sID, iEventType).size();
        int iObjectSize = findObjectEvents(obj, sID, iEventType).size();
        // we used a unique id, so it must be zero
        assertEquals(0, iObjectSize);

        // add event
        OwStandardHistoryObjectChangeEvent deleteObjectEvent = new OwStandardHistoryObjectChangeEvent(obj, parent);

        m_historyManager.addEvent(iEventType, sID, deleteObjectEvent, OwEventManager.HISTORY_STATUS_OK);

        // check if event was inserted
        int iNewSize = findStandardEvents(sID, iEventType).size();

        assertEquals(iSize + 1, iNewSize);

        // now check via doObjectSearch method
        OwObjectCollection historyEntries = findObjectEvents(obj, sID, iEventType);
        int iNewObjectSize = historyEntries.size();

        // we used a unique id, so it must be one now
        assertEquals(1, iNewObjectSize);

        // check history entry
        OwHistoryEntry objectentry = (OwHistoryEntry) historyEntries.get(0);

        // === check event prop
        assertEquals(sID, objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY).getValue());
        assertEquals(new Integer(iEventType), objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY).getValue());

        // === check object prop
        Object[] objects = (Object[]) objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.OBJECTS_PROPERTY).getValue();

        assertEquals(1, objects.length);
        assertEquals(obj.getDMSID(), ((OwObjectReference) objects[0]).getDMSID());
        assertEquals(obj.getMIMEType(), ((OwObjectReference) objects[0]).getMIMEType());
        assertEquals(obj.getName(), ((OwObjectReference) objects[0]).getName());
        assertEquals(obj.getType(), ((OwObjectReference) objects[0]).getType());

        // === check parent prop
        OwObjectReference parentObject = (OwObjectReference) objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.PARENT_PROPERTY).getValue();

        assertEquals(parent.getDMSID(), parentObject.getDMSID());
        assertEquals(parent.getMIMEType(), parentObject.getMIMEType());
        assertEquals(parent.getName(), parentObject.getName());
        assertEquals(parent.getType(), parentObject.getType());
    }

    /** add and lookup object property event
     * 
     * @throws Exception
     */
    public void testObjectPropertyEvents() throws Exception
    {
        // OwEscapedStringTokenizer must not change delimiter, otherwise, database entries can not be read anymore
        assertEquals(';', OwEscapedStringTokenizer.STANDARD_DELIMITER);
        assertEquals('\\', OwEscapedStringTokenizer.STANDARD_ESCAPE);

        String sID = OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES;
        int iEventType = OwEventManager.HISTORY_EVENT_TYPE_OBJECT;

        // create unique DMSID for later retrieval
        String sDMSID = "JUNIT,testObjectEvents," + String.valueOf((System.currentTimeMillis()));

        // create a dummy object
        OwTestFileObject obj = new OwTestFileObject(Locale.GERMAN, m_historyManager, new File(getConfigDir() + "testobject.txt"), sDMSID);

        // get initial sizes
        int iSize = findStandardEvents(sID, iEventType).size();
        int iObjectSize = findObjectEvents(obj, sID, iEventType).size();

        // we used a unique id, so it must be zero
        assertEquals(0, iObjectSize);

        // add event
        OwPropertyCollection newProps = new OwStandardPropertyCollection();

        OwPropertyCollection oldProps = obj.getClonedProperties(null);

        // modify name property
        OwProperty nameProp = (OwProperty) oldProps.get(OwFileObject.OwFileObjectClass.NAME_PROPERTY);
        Object oldNamePropValue = nameProp.getValue();
        newProps.put(nameProp.getPropertyClass().getClassName(), nameProp);

        // set name, insert delimiters of the excape tokeinzer to test if really all kind of strings are recognized
        nameProp.setValue("JUnit - Here comes the escape char: [" + OwEscapedStringTokenizer.STANDARD_ESCAPE + "], Here comes the delimiter char: [" + OwEscapedStringTokenizer.STANDARD_DELIMITER + "]");

        // modify last modified property
        OwProperty lastmodProp = (OwProperty) oldProps.get(OwFileObject.OwFileObjectClass.LAST_MODIFIED_PROPERTY);
        Object oldLastmodPropValue = lastmodProp.getValue();
        newProps.put(lastmodProp.getPropertyClass().getClassName(), lastmodProp);
        lastmodProp.setValue(new Date());

        OwStandardHistoryPropertyChangeEvent objectPropertyModifyEvent = new OwStandardHistoryPropertyChangeEvent(obj, newProps);

        m_historyManager.addEvent(iEventType, sID, objectPropertyModifyEvent, OwEventManager.HISTORY_STATUS_OK);

        // check if event was inserted
        int iNewSize = findStandardEvents(sID, iEventType).size();

        assertEquals(iSize + 1, iNewSize);

        // now check via doObjectSearch method
        OwObjectCollection historyEntries = findObjectEvents(obj, sID, iEventType);
        int iNewObjectSize = historyEntries.size();

        // we used a unique id, so it must be one now
        assertEquals(1, iNewObjectSize);

        // check history entry
        OwHistoryEntry objectentry = (OwHistoryEntry) historyEntries.get(0);

        // === check event props
        assertEquals(sID, objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY).getValue());
        assertEquals(new Integer(iEventType), objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY).getValue());

        // === check object prop
        Object[] objects = (Object[]) objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.OBJECTS_PROPERTY).getValue();

        assertEquals(objects.length, 1);
        assertEquals(obj.getDMSID(), ((OwObjectReference) objects[0]).getDMSID());
        assertEquals(obj.getMIMEType(), ((OwObjectReference) objects[0]).getMIMEType());
        assertEquals(obj.getName(), ((OwObjectReference) objects[0]).getName());
        assertEquals(obj.getType(), ((OwObjectReference) objects[0]).getType());

        // === check modified property prop
        Object[] modifiedProps = (Object[]) objectentry.getProperty(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY).getValue();

        // convert to map
        Map modifiedPropsMap = new HashMap();
        for (int i = 0; i < modifiedProps.length; i++)
        {
            OwHistoryModifiedPropertyValue modifiedProp = (OwHistoryModifiedPropertyValue) modifiedProps[i];
            modifiedPropsMap.put(modifiedProp.getClassName(), modifiedProp);
        }

        OwHistoryModifiedPropertyValue modifiedNameProp = (OwHistoryModifiedPropertyValue) modifiedPropsMap.get(OwFileObject.OwFileObjectClass.NAME_PROPERTY);
        OwHistoryModifiedPropertyValue modifiedLastModProp = (OwHistoryModifiedPropertyValue) modifiedPropsMap.get(OwFileObject.OwFileObjectClass.LAST_MODIFIED_PROPERTY);

        assertEquals(OwFileObject.OwFileObjectClass.NAME_PROPERTY, modifiedNameProp.getFieldDefinition().getClassName());
        assertEquals(OwFileObject.OwFileObjectClass.LAST_MODIFIED_PROPERTY, modifiedLastModProp.getFieldDefinition().getClassName());

        assertEquals(modifiedNameProp.getOldValueString(), OwStandardPropertyClass.getStringFromValue(oldNamePropValue, modifiedNameProp.getFieldDefinition().getJavaClassName()));
        assertEquals(modifiedLastModProp.getOldValueString(), OwStandardPropertyClass.getStringFromValue(oldLastmodPropValue, modifiedLastModProp.getFieldDefinition().getJavaClassName()));
        assertEquals(modifiedNameProp.getNewValueString(), OwStandardPropertyClass.getStringFromValue(nameProp.getValue(), nameProp.getFieldDefinition().getJavaClassName()));
        assertEquals(modifiedLastModProp.getNewValueString(), OwStandardPropertyClass.getStringFromValue(lastmodProp.getValue(), lastmodProp.getFieldDefinition().getJavaClassName()));

        assertEquals(oldNamePropValue, modifiedNameProp.getOldValue().getValue());
        assertEquals(oldLastmodPropValue, modifiedLastModProp.getOldValue().getValue());
        assertEquals(nameProp.getValue(), modifiedNameProp.getNewValue().getValue());
        assertEquals(lastmodProp.getValue(), modifiedLastModProp.getNewValue().getValue());
    }

    /** test fieldprovider methods of history manager
     * @throws Exception 
     * 
     */
    public void testFieldprovider() throws Exception
    {
        Iterator it = OwStandardHistoryEntry.getStaticObjectClass().getPropertyClassNames().iterator();

        while (it.hasNext())
        {
            String sName = (String) it.next();
            OwFieldDefinition def = m_historyManager.getFieldDefinition(sName, null);

            assertEquals(def.getClassName(), sName);
        }
    }
}
