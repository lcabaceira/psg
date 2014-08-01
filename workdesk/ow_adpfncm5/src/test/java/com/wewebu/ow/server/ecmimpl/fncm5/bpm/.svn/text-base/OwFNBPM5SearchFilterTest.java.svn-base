package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwFNBPM5SearchFilterTest.
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
public class OwFNBPM5SearchFilterTest extends OwFNBPM5TestFixture
{

    private static final Logger LOG = Logger.getLogger(OwFNBPM5SearchFilterTest.class);
    private OwWorkitem testWorkitem1;
    private OwWorkitem testWorkitem2;

    @Test
    public void testSetFilter() throws Exception
    {
        // get queue "QueueA"
        OwWorkitemContainer container = getQueueA();

        OwObjectCollection unfilteredContainer = container.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, null);

        int expectedFilteredItems = 0;
        assertTrue(unfilteredContainer.size() > 1);
        for (Object element : unfilteredContainer)
        {
            OwWorkitem workitem = (OwWorkitem) element;
            OwField subjectField = workitem.getField("F_Subject");
            if (testWorkitem1.getName().equals(subjectField.getValue()))
            {
                expectedFilteredItems++;
            }
        }

        // create filter
        OwSearchNode filter = new OwSearchNode(OwSearchNode.SEARCH_OP_AND, OwSearchNode.NODE_TYPE_PROPERTY);

        Vector<String> fprops = new Vector<String>();
        fprops.add("F_Subject");
        OwFieldDefinition subjectFieldDef = (OwFieldDefinition) container.getFilterProperties(fprops).iterator().next();

        OwSearchNode filterCrit = new OwSearchNode(subjectFieldDef, OwSearchOperator.CRIT_OP_EQUAL, testWorkitem1.getName(), 0);
        filter.add(filterCrit);

        OwObjectCollection filteredContainer = container.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, new OwSort(), 100, 0, filter);
        assertEquals(expectedFilteredItems, filteredContainer.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        testWorkitem1 = createTestWorkItem(OwFNBPM5TestFixture.TEST_STEP_SUBJECT1);
        testWorkitem2 = createTestWorkItem(OwFNBPM5TestFixture.TEST_STEP_SUBJECT2);

        //move to A
        testWorkitem1.setLock(true);
        testWorkitem1.setResponse("Goto A");
        testWorkitem1.dispatch();
        testWorkitem1 = findWorkItemInQueueById(QUEUE_A, testWorkitem1.getID());
        testWorkitem1.setLock(false);

        testWorkitem2.setLock(true);
        testWorkitem2.setResponse("Goto A");
        testWorkitem2.dispatch();
        testWorkitem2 = findWorkItemInQueueById(QUEUE_A, testWorkitem2.getID());
        testWorkitem2.setLock(false);

        LOG.debug("---------------------------------- SetUp: OwFNBPMTest ----------------------------------");
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception
    {
        testWorkitem1 = findWorkItemById(testWorkitem1.getID());
        testWorkitem1.delete();

        testWorkitem2 = findWorkItemById(testWorkitem2.getID());
        testWorkitem2.delete();
        super.tearDown();
        LOG.debug("---------------------------------- TearDown: OwFNBPMTest ----------------------------------");
    }
}
