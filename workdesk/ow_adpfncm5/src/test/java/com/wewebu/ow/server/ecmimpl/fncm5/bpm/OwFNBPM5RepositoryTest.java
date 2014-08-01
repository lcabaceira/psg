package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ClassWorkflowSubscription;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5WorkflowDefinition;

/**
 *<p>
 * OwFNBPM5RepositoryTest.
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
public class OwFNBPM5RepositoryTest extends OwFNBPM5TestFixture
{
    @Test
    @SuppressWarnings("rawtypes")
    public void testGetAllLaunchableWorkflowDescriptions() throws Exception
    {
        Collection wfNullDescriptions = getBPMRepository().getLaunchableWorkflowDescriptions(null);
        Collection wfEmptyDescriptions = getBPMRepository().getLaunchableWorkflowDescriptions(new ArrayList<OwObject>());
        for (int i = 0; i < m_allDefinedDescriptions.length; i++)
        {
            assertTrue("Workflow descriprion " + m_allDefinedDescriptions[i].getName() + " not found in null-call!", wfNullDescriptions.contains(m_allDefinedDescriptions[i]));
            assertTrue("Workflow description " + m_allDefinedDescriptions[i].getName() + " not found in empty-call!", wfEmptyDescriptions.contains(m_allDefinedDescriptions[i]));

        }
    }

    @Test
    @SuppressWarnings({ "rawtypes" })
    public void testGetObjectLaunchableWorkflowDescriptions() throws Exception
    {
        {
            //TODO: find an incompatibility scenario - currently there seems to 
            //      be a Document subscription on the CC machine
            //            final Collection wfForAllDocuments = m_bpmrepository.getLaunchableWorkflowDescriptions(Arrays.asList(m_testObjects));
            //            assertNull("No worflows should be found as no compatible sunbscriptions are set up!", wfForAllDocuments);
        }
        {
            OwFNCM5Object<?> wf1Def = m_classSubscriptions[0][0];
            OwFNCM5Object<?> wf2Def = m_classSubscriptions[1][1];

            final Collection wfForClass1 = getBPMRepository().getLaunchableWorkflowDescriptions(Arrays.asList(new OwObject[] { m_testObjects[0] }));
            assertTrue("Found only " + wfForClass1.size() + " subscriptions.", wfForClass1.size() >= 2);
            assertTrue("Workflow " + wf1Def.getName() + " should have subscriptions for target class 1!", wfForClass1.contains(wf1Def));
            assertTrue("Workflow " + wf2Def.getName() + " should have subscriptions for target class 1!", wfForClass1.contains(wf2Def));
        }
        {
            OwFNCM5Object<?> wf1Def = m_classSubscriptions[0][0];
            OwFNCM5Object<?> wf2Def = m_classSubscriptions[1][0];

            final Collection wfForClass2 = getBPMRepository().getLaunchableWorkflowDescriptions(Arrays.asList(new OwObject[] { m_testObjects[1] }));
            assertTrue(wfForClass2.size() >= 1);
            assertTrue("Workflow " + wf2Def.getName() + " should have subscriptions for target class 2!", wfForClass2.contains(wf2Def));
            assertFalse("Workflow " + wf1Def.getName() + " should have no descriptions for target class 2!", wfForClass2.contains(wf1Def));
        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetSublcassesLaunchableWorkflowDescriptions() throws Exception
    {
        OwFNCM5Object<?> wf1Def = m_classSubscriptions[0][0];
        OwFNCM5Object<?> wf2Def = m_classSubscriptions[1][1];
        final Collection wfForUnsubscribedChild = getBPMRepository().getLaunchableWorkflowDescriptions(Arrays.asList(new OwObject[] { m_unsubscribedTestObject }));

        assertNotNull("Document subscription should propagate to all Document subclasses !", wfForUnsubscribedChild);
        assertTrue("Document subscription should propagate to all Document subclasses !", wfForUnsubscribedChild.contains(m_documentSubscription));

        List<OwObject> allObjects = new LinkedList<OwObject>();
        allObjects.addAll(Arrays.asList(m_testObjects));
        allObjects.add(m_unsubscribedTestObject);

        final Collection wfForAllDocuments = getBPMRepository().getLaunchableWorkflowDescriptions(allObjects);
        assertNotNull("Workflows should be found as one compatible subscription is set up!", wfForAllDocuments);
        assertTrue(wfForAllDocuments.contains(m_documentSubscription));
        assertFalse(wfForAllDocuments.contains(wf1Def));
        assertFalse(wfForAllDocuments.contains(wf2Def));
    }

    @Test
    public void testCreateLaunchableItemFromClassWorkflowSubscription() throws Exception
    {
        List<OwObject> objectCollection = Arrays.asList(new OwObject[] { m_testObjects[1] });
        OwWorkflowDescription classWorkflowSubscription = findLaunchableWFDescription(objectCollection, WF2_TG1_SUBSCRIPTION_NAME);

        assertNotNull(classWorkflowSubscription);
        assertTrue(classWorkflowSubscription instanceof OwFNCM5ClassWorkflowSubscription);

        OwWorkitem launchItem = getBPMRepository().createLaunchableItem(classWorkflowSubscription, objectCollection);

        assertTrue(launchItem instanceof OwFNBPM5LaunchStepWorkItem);

        OwProperty attachments = launchItem.getProperty("ATTACHMENTS");
        assertNotNull(attachments);
        Object[] objectReferences = (Object[]) attachments.getValue();
        assertEquals(1, objectReferences.length);
        OwObjectReference reference = (OwObjectReference) objectReferences[0];
        assertEquals(m_testObjects[1].getDMSID(), reference.getDMSID());

    }

    @Test
    public void testCreateLaunchableItemFromWorkflowDefinition() throws Exception
    {
        List<OwObject> objectCollection = Arrays.asList(new OwObject[] { m_testObjects[1] });
        OwWorkflowDescription workflowDefinition = findLaunchableWFDescription(null, WORKFLOWDEFINITION_2_NAME);

        assertNotNull(workflowDefinition);
        assertTrue(workflowDefinition instanceof OwFNCM5WorkflowDefinition);

        OwWorkitem launchItem = getBPMRepository().createLaunchableItem(workflowDefinition, objectCollection);

        assertTrue(launchItem instanceof OwFNBPM5LaunchStepWorkItem);

        OwProperty attachments = launchItem.getProperty("ATTACHMENTS");
        assertNotNull(attachments);
        Object[] objectReferences = (Object[]) attachments.getValue();
        assertEquals(1, objectReferences.length);
        OwObjectReference reference = (OwObjectReference) objectReferences[0];
        assertEquals(m_testObjects[1].getDMSID(), reference.getDMSID());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLaunchItem() throws Exception
    {
        List<OwObject> objectCollection = Arrays.asList(new OwObject[] { m_testObjects[0] });
        OwWorkflowDescription theWorkflowDescription = findLaunchableWFDescription(objectCollection, WF1_TG1_SUBSCRIPTION_NAME);

        assertNotNull(theWorkflowDescription);

        OwWorkitem launchItem = getBPMRepository().createLaunchableItem(theWorkflowDescription, objectCollection);
        OwProperty subjectProperty = launchItem.getProperty(NAME_PROPERTY);

        subjectProperty.setValue(TEST_STEP_SUBJECT1);
        OwPropertyCollection properties = new OwStandardPropertyCollection();
        properties.put(NAME_PROPERTY, subjectProperty);
        launchItem.setProperties(properties);
        launchItem.dispatch();

        OwWorkitem queueTestItem = findWorkItemInQueue("Inbox", TEST_STEP_SUBJECT1, 5, 20000);

        assertNotNull("The test item was not launched!", queueTestItem);

        OwProperty attachment = launchItem.getProperty("ATTACHMENT");
        assertNotNull(attachment);

        OwObjectReference objectReference = (OwObjectReference) attachment.getValue();
        assertEquals(m_testObjects[0].getDMSID(), objectReference.getDMSID());

        //move to A
        queueTestItem.setLock(true);
        queueTestItem.setResponse("Goto A");
        queueTestItem.dispatch();

        //try to clean up first the bpm repository 
        String queueA = "QueueA";
        queueTestItem = findWorkItemInQueue(queueA, TEST_STEP_SUBJECT1);

        int initialCount = getQueueA().getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS }, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        assertNotNull("The test item was not launched!", queueTestItem);
        queueTestItem.delete();

        int finalCount = getQueueA().getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS }, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        assertEquals("Could not delete test item!", initialCount - 1, finalCount);
    }
}
