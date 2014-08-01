package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Credentials;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5NetworkManager;
import com.wewebu.ow.server.exceptions.OwLockDeniedException;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwFNBPM5FunctionTest.
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
public class OwFNBPM5FunctionTest extends OwFNBPM5TestFixture
{

    private static final Logger LOG = Logger.getLogger(OwFNBPM5FunctionTest.class);

    private OwWorkitem testWorkitem = null;

    private String m_login;
    private String m_password;

    public void setUp() throws Exception
    {
        super.setUp();

        // Keep them for further reference
        OwFNCM5Credentials credentials = this.m_fncmNetwork.getCredentials();
        this.m_login = credentials.getUserInfo().getUserShortName();
        this.m_password = credentials.getPassword();

        testWorkitem = createTestWorkItem(OwFNBPM5TestFixture.TEST_STEP_SUBJECT1);

        LOG.debug("---------------------------------- SetUp: OwFNBPMTest ----------------------------------");
    }

    public void tearDown() throws Exception
    {
        testWorkitem = findWorkItemById(testWorkitem.getID());
        testWorkitem.delete();

        super.tearDown();

        LOG.debug("---------------------------------- TearDown: OwFNBPMTest ----------------------------------");
    }

    @Test
    public void testLockUnlockWithDifferentUsers() throws Exception
    {
        try
        {
            OwWorkitem workItem = this.testWorkitem;

            //move to A
            workItem.setLock(true);
            workItem.setResponse("Goto A");
            workItem.dispatch();
            workItem = findWorkItemInQueueById(QUEUE_A, workItem.getID());
            workItem.setLock(false);

            // Start testing
            workItem.setLock(true);

            // check in cache
            assertTrue(workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

            // check after refresh
            workItem = findWorkItemInQueueById(QUEUE_A, workItem.getID());
            assertTrue(workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

            m_fncmNetwork.logout();
            this.m_fncmNetwork = new OwFNCM5NetworkManager().createdInitializedNetwork();
            m_fncmNetwork.loginDefault("JUnitTester", "junit");

            workItem = findWorkItemInQueueById(QUEUE_A, workItem.getID());
            try
            {
                workItem.setLock(true);
                fail("Should not be possible to lock an item already locked by another user.");
            }
            catch (OwLockDeniedException e)
            {
                String exceptionMessage = e.getMessage();
                assertTrue(exceptionMessage.toLowerCase().contains(this.m_login.toLowerCase()));
            }

            //restore the initial state
            m_fncmNetwork.logout();
            this.m_fncmNetwork = new OwFNCM5NetworkManager().createdInitializedNetwork();

            m_fncmNetwork.loginDefault(this.m_login, this.m_password);
            workItem = findWorkItemInQueueById(QUEUE_A, workItem.getID());
            workItem.setLock(false);
            assertFalse(workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        }
        finally
        {
            this.m_fncmNetwork = new OwFNCM5NetworkManager().createLoggedInNetwork();
        }
    }

    @Test
    public void testLockUnlock() throws Exception
    {
        OwWorkitem workItem = testWorkitem;
        workItem.setLock(true);

        // check in cache
        assertTrue(workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        // check after refresh
        workItem = findWorkItemInQueueById(INBOX, workItem.getID());
        assertTrue(workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        workItem.setLock(false);

        // check in cache
        assertFalse(workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        // check after refresh
        workItem = findWorkItemInQueueById(INBOX, workItem.getID());
        assertFalse(workItem.getLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
    }

    @Test
    public void testReassign() throws Exception
    {
        // get queue
        OwWorkitemContainer queueA = getQueueA();
        OwWorkitemContainer inbox = getInbox();

        //move to A
        testWorkitem.setLock(true);
        testWorkitem.setResponse("Goto A");
        testWorkitem.dispatch();
        testWorkitem = findWorkItemInQueueById(QUEUE_A, testWorkitem.getID());
        testWorkitem.setLock(false);

        inbox.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);

        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        int iQueueASize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();

        // reassign
        String testUserId = "cn=JUnitTester,o=sample";

        testWorkitem.setLock(true);
        testWorkitem.reassignToUserContainer(testUserId, false);
        testWorkitem.setLock(false);

        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        int iNewQueueASize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();
        assertEquals(iQueueASize - 1, iNewQueueASize);

        this.m_fncmNetwork.logout();
        this.m_fncmNetwork = new OwFNCM5NetworkManager().createdInitializedNetwork();
        this.m_fncmNetwork.loginDefault("JUnitTester", "junit");

        // return to source
        testWorkitem = findWorkItemInQueueById(INBOX, testWorkitem.getID());
        assertNotNull(testWorkitem);
        if (testWorkitem.canReturnToSource(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            testWorkitem.setLock(true);
            testWorkitem.returnToSource();
            testWorkitem.setLock(false);
        }

        this.m_fncmNetwork.logout();
        this.m_fncmNetwork = new OwFNCM5NetworkManager().createLoggedInNetwork();

        testWorkitem = findWorkItemInQueueById(QUEUE_A, testWorkitem.getID());
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        int iResetNormalSize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();

        assertEquals(iQueueASize, iResetNormalSize);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testResubmit() throws Exception
    {
        // get queue
        OwWorkitemContainer queueA = getQueueA();

        assertTrue(testWorkitem.canResubmit(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        //move to A
        testWorkitem.setLock(true);
        testWorkitem.setResponse("Goto A");
        testWorkitem.dispatch();
        testWorkitem = findWorkItemInQueueById(QUEUE_A, testWorkitem.getID());
        testWorkitem.setLock(false);

        assertTrue(testWorkitem.canResubmit(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));

        // save count
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        int iNormalSize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_RESUBMISSION);
        int iResubmissionSize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();

        int iSize = iNormalSize + iResubmissionSize;

        Date date = new Date();
        // 48 hours into the future
        date.setTime(new Date().getTime() + 48 * 1000 * 3600);

        testWorkitem.setLock(true);
        testWorkitem.resubmit(date);
        testWorkitem.setLock(false);

        // check new count
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        int iNewNormalSize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_RESUBMISSION);
        int iNewResubmissionSize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();

        assertEquals(iNormalSize - 1, iNewNormalSize);
        assertEquals(iResubmissionSize + 1, iNewResubmissionSize);

        // reset all items
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_NONE);
        Iterator it = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).iterator();
        while (it.hasNext())
        {
            testWorkitem = (OwWorkitem) it.next();
            testWorkitem.setLock(true);
            testWorkitem.resubmit(null);
            testWorkitem.setLock(false);

        }

        // wait
        Thread.sleep(1000);

        // check reset count
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        int iResetNormalSize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();
        queueA.setFilterType(OwWorkitemContainer.FILTER_TYPE_RESUBMISSION);
        int iResetResubmissionSize = queueA.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null).size();

        assertEquals(iSize, iResetNormalSize);
        assertEquals(0, iResetResubmissionSize);
    }
}
