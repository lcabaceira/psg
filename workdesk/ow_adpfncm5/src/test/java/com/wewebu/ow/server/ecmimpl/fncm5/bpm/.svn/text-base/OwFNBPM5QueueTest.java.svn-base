package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;

/**
 *<p>
 * OwFNBPM5QueueTest.
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
public class OwFNBPM5QueueTest extends OwFNBPM5TestFixture
{
    @Test
    @SuppressWarnings("rawtypes")
    public void testPublicQueueFolder() throws Exception
    {
        Collection containerids = getBPMRepository().getWorkitemContainerIDs(false, OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER);
        assertTrue(containerids.contains("QueueA"));
        assertTrue(containerids.contains("QueueB"));
        assertTrue(containerids.contains("QueueC"));
        assertFalse(containerids.contains("Inbox"));
        assertFalse(containerids.contains("DefaultRoster"));

        String name = getBPMRepository().getWorkitemContainerName("QueueA", OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER);
        OwWorkitemContainer container = getBPMRepository().getWorkitemContainer("QueueA", OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER);
        assertEquals(name, container.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testRosterContainer() throws Exception
    {
        Collection containerids = getBPMRepository().getWorkitemContainerIDs(false, OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER);
        assertFalse(containerids.contains("QueueA"));
        assertFalse(containerids.contains("QueueB"));
        assertFalse(containerids.contains("QueueC"));
        assertFalse(containerids.contains("Inbox"));
        assertTrue(containerids.contains("DefaultRoster"));

        String name = getBPMRepository().getWorkitemContainerName("DefaultRoster", OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER);
        OwWorkitemContainer container = getBPMRepository().getWorkitemContainer("DefaultRoster", OwObjectReference.OBJECT_TYPE_ROSTER_FOLDER);
        assertEquals(name, container.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testUserQueueContainer() throws Exception
    {
        Collection containerids = getBPMRepository().getWorkitemContainerIDs(false, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        assertFalse(containerids.contains("QueueA"));
        assertFalse(containerids.contains("QueueB"));
        assertFalse(containerids.contains("QueueC"));
        assertTrue(containerids.contains("Inbox"));
        assertFalse(containerids.contains("DefaultRoster"));

        String name = getBPMRepository().getWorkitemContainerName("Inbox", OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwWorkitemContainer container = getBPMRepository().getWorkitemContainer("Inbox", OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        assertEquals(name, container.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testTrackerQueueContainer() throws Exception
    {
        Collection containerids = getBPMRepository().getWorkitemContainerIDs(false, OwObjectReference.OBJECT_TYPE_TRACKER_QUEUE_FOLDER);
        assertFalse(containerids.contains("QueueA"));
        assertFalse(containerids.contains("QueueB"));
        assertFalse(containerids.contains("QueueC"));
        assertFalse(containerids.contains("Inbox"));
        assertFalse(containerids.contains("DefaultRoster"));
        assertTrue(containerids.contains("Tracker"));

        String name = getBPMRepository().getWorkitemContainerName("Tracker", OwObjectReference.OBJECT_TYPE_TRACKER_QUEUE_FOLDER);
        OwWorkitemContainer container = getBPMRepository().getWorkitemContainer("Tracker", OwObjectReference.OBJECT_TYPE_TRACKER_QUEUE_FOLDER);
        assertEquals(name, container.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testCrossQueueContainer() throws Exception
    {
        Collection containerids = getBPMRepository().getWorkitemContainerIDs(false, OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER);
        assertFalse(containerids.contains("QueueA"));
        assertFalse(containerids.contains("QueueB"));
        assertFalse(containerids.contains("QueueC"));
        assertFalse(containerids.contains("Inbox"));
        assertFalse(containerids.contains("DefaultRoster"));
        assertTrue(containerids.contains("Cross A.xml"));
        assertTrue(containerids.contains("Cross B.xml"));
        assertTrue(containerids.contains("Cross C.xml"));

        String name = getBPMRepository().getWorkitemContainerName("Cross A.xml", OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER);
        OwWorkitemContainer container = getBPMRepository().getWorkitemContainer("Cross A.xml", OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER);
        assertEquals(name, container.getName());
    }

    //    public void testGetWorkitemContainers() throws Exception
    //    {
    //        Map checkcountmap = new LinkedHashMap();
    //
    //        checkcountmap.put(new Integer(OwObjectReference.OBJECT_TYPE_PROXY_QUEUE_FOLDER), new Integer(1));
    //        checkcountmap.put(new Integer(OwObjectReference.OBJECT_TYPE_SYS_QUEUE_FOLDER), new Integer(2));
    //
    //        Iterator it = checkcountmap.keySet().iterator();
    //
    //        while (it.hasNext())
    //        {
    //            int iCount = 0;
    //
    //            Integer type = (Integer) it.next();
    //
    //            LOG.debug("Type = " + type);
    //
    //            Collection containerids = m_bpmrepository.getWorkitemContainerIDs(false, type.intValue());
    //
    //            Iterator itC = containerids.iterator();
    //            while (itC.hasNext())
    //            {
    //                String id = itC.next().toString();
    //
    //                String name = m_bpmrepository.getWorkitemContainerName(id, type.intValue());
    //                OwWorkitemContainer container = m_bpmrepository.getWorkitemContainer(id, type.intValue());
    //
    //                assertEquals(name, container.getName());
    //
    //                // get children
    //                OwObjectCollection obj = container.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null);
    //
    //                switch (container.getType())
    //                {
    //                    case OwObjectReference.OBJECT_TYPE_PROXY_QUEUE_FOLDER:
    //                    case OwObjectReference.OBJECT_TYPE_CROSS_QUEUE_FOLDER:
    //                        break;
    //
    //                    default:
    //                        if (obj == null)
    //                        {
    //                            //assertTrue(container.getChildCount(new int[]{OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS},OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) == 0);
    //                        }
    //                        else
    //                        {
    //                            int childCount = container.getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS }, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    //                            assertTrue(childCount == obj.size());
    //                        }
    //                }
    //
    //                iCount++;
    //            }
    //
    //            int iCheckCount = ((Integer) checkcountmap.get(type)).intValue();
    //
    //            assertEquals("For folder type " + type, iCheckCount, iCount);
    //            //System.out.println("iCount: " +  String.valueOf(iCount) + ", " + type.toString() + ", " + String.valueOf(iCheckCount));
    //        }
    //
    //    }
}
