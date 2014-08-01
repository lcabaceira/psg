package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;

/**
 *<p>
 * FNBPM Test Case: Test BPM work item functions like: property access, lock.
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
public class OwFNBPM5WorkItemTest extends OwFNBPM5TestFixture
{

    private static final Logger LOG = Logger.getLogger(OwFNBPM5WorkItemTest.class);

    private OwWorkitem testWorkitem = null;

    /*
    F_StepName
    F_WobNum
    F_WorkSpaceId
    F_Locked
    F_LockMachine
    F_LockUser
    F_LockTime
    F_BindPending
    F_BoundUser
    F_BoundMachine
    F_Tag
    F_UniqueId
    F_OperationId
    F_WorkClassId
    F_QueueWPClassId
    F_EnqueueTime
    DateLastModified
    F_CreateTime
    DateCreated
    F_InstrSheetId
    F_WorkOrderId
    F_SortOrder
    F_TimeOut
    F_Subject
    DocumentTitle
    F_Overdue
    OW_RESUBMIT_DATE
    Kostenvoranschlag
    Projektname
    Projektnummer
    Projektverantwortlicher
    K_NOTIZ
    F_QueueName
    F_Description
    F_Comment
     * */
    @Test
    @SuppressWarnings("rawtypes")
    public void testGetWorkItemProperties() throws Exception
    {
        OwWorkitem workitem = testWorkitem;

        OwPropertyCollection props = workitem.getProperties(null);

        //Iterator it = props.keySet().iterator();
        /* FindBugs
         * [WMI] Inefficient use of keySet iterator instead of entrySet iterator [WMI_WRONG_MAP_ITERATOR]
         * This method accesses the value of a Map entry, using a key that was retrieved from a keySet iterator. It is more efficient to use an iterator on the entrySet of the map, to avoid the Map.get(key) lookup.
         */
        Iterator it = props.entrySet().iterator();

        while (it.hasNext())
        {
            //String key = (String) it.next();
            //OwProperty prop = (OwProperty) props.get(key);
            Map.Entry entry = (Map.Entry) it.next();
            OwProperty prop = (OwProperty) entry.getValue();
            prop.getValue();
        }

        // wob num is ID
        assertEquals(workitem.getID(), workitem.getProperty("F_WobNum").getValue());
        // DMSID must contain wob num
        assertTrue(0 != workitem.getDMSID().indexOf(workitem.getProperty("F_WobNum").getValue().toString()));
    }

    @Test
    public void testGetContentEngineCompatibleProperties() throws Exception
    {
        OwWorkitem workitem = testWorkitem;

        // check equivalent P8 CE properties
        assertEquals(workitem.getProperty("F_Subject").getValue(), workitem.getProperty("DocumentTitle").getValue());
        assertEquals(workitem.getProperty("F_CreateTime").getValue(), workitem.getProperty("DateCreated").getValue());
        assertEquals(workitem.getProperty("F_EnqueueTime").getValue(), workitem.getProperty("DateLastModified").getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        testWorkitem = createTestWorkItem(OwFNBPM5TestFixture.TEST_STEP_SUBJECT1);
        LOG.debug("---------------------------------- SetUp: OwFNBPMTest ----------------------------------");
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception
    {
        testWorkitem = findWorkItemById(testWorkitem.getID());
        testWorkitem.delete();

        super.tearDown();
        LOG.debug("---------------------------------- TearDown: OwFNBPMTest ----------------------------------");
    }
}
