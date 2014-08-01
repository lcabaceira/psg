package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMLaunchableWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem;

/**
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
public class TestAlfrescoBPMWorkflow extends AlfrescoBPMRepositoryFixture
{

    public TestAlfrescoBPMWorkflow(String name) throws Exception
    {
        super(name);
    }

    @SuppressWarnings({ "unchecked" })
    public void testStartWorkflow() throws Exception
    {
        OwWorkflowDescription processDescription = getAdhockProcessDescritpion();

        OwWorkitemContainer inboxQueue = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwObjectCollection myWorkItems = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

        int workitemsBefore = myWorkItems.size();

        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(processDescription, null);

        try
        {
            Assert.assertNotNull(launchableWfItem);

            OwPropertyCollection properties = new OwStandardPropertyCollection();

            OwObjectClass objectClass = launchableWfItem.getObjectClass();
            OwPropertyClass wfDescPropClass = objectClass.getPropertyClass("bpm:workflowDescription");
            Assert.assertNotNull(wfDescPropClass);
            System.err.println(wfDescPropClass.isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
            System.err.println(wfDescPropClass.isSystemProperty());

            OwProperty propWorkflowDescription = launchableWfItem.getProperty("bpm:workflowDescription");
            propWorkflowDescription.setValue("Foo baz - " + System.currentTimeMillis());
            properties.put("bpm:workflowDescription", propWorkflowDescription);

            launchableWfItem.setProperties(properties);

            launchableWfItem.dispatch();

            inboxQueue = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
            myWorkItems = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

            int workitemsAfter = myWorkItems.size();

            Assert.assertEquals(workitemsBefore + 1, workitemsAfter);
        }
        finally
        {
            String wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchableWfItem).getWorkflowInstanceId();
            deleteWorkflowInstance(wfId);
        }
    }

    @SuppressWarnings("unchecked")
    public void testStartWorkflowWithGroupAssignee() throws Exception
    {
        OwWorkflowDescription processDescription = getParallelGroupReviewProcessDescription();

        OwWorkitemContainer inboxQueue = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwObjectCollection myWorkItems = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

        int workitemsBefore = myWorkItems.size();

        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(processDescription, null);

        try
        {
            Assert.assertNotNull(launchableWfItem);

            OwPropertyCollection properties = new OwStandardPropertyCollection();

            OwProperty propGroupAssignee = launchableWfItem.getProperty("bpm:groupAssignee");
            propGroupAssignee.setValue("GROUP_ALFRESCO_ADMINISTRATORS");
            properties.put(propGroupAssignee.getPropertyClass(), propGroupAssignee);

            launchableWfItem.setProperties(properties);

            launchableWfItem.dispatch();

            inboxQueue = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
            myWorkItems = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

            int workitemsAfter = myWorkItems.size();

            Assert.assertEquals(workitemsBefore + 1, workitemsAfter);
        }
        finally
        {
            String wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchableWfItem).getWorkflowInstanceId();
            deleteWorkflowInstance(wfId);
        }
    }

    @SuppressWarnings("unchecked")
    public void testStartWorkflowWithAssignees() throws Exception
    {
        OwWorkflowDescription processDescription = getParallelReviewAndApproveProcessDescription();
        OwUserInfo tester = this.getNetwork().getUserFromID("JUnitTester");
        OwUserInfo admin = this.getNetwork().getUserFromID("admin");

        OwWorkitemContainer inboxQueue = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwObjectCollection myWorkItems = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

        int workitemsBefore = myWorkItems.size();

        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(processDescription, null);

        try
        {
            Assert.assertNotNull(launchableWfItem);

            OwPropertyCollection properties = new OwStandardPropertyCollection();

            OwProperty propAssignees = launchableWfItem.getProperty("bpm:assignees");
            propAssignees.setValue(new OwUserInfo[] { tester, admin });
            properties.put(propAssignees.getPropertyClass(), propAssignees);

            launchableWfItem.setProperties(properties);

            launchableWfItem.dispatch();

            inboxQueue = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
            myWorkItems = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

            int workitemsAfter = myWorkItems.size();

            Assert.assertEquals(workitemsBefore + 1, workitemsAfter);
        }
        finally
        {
            String wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchableWfItem).getWorkflowInstanceId();
            deleteWorkflowInstance(wfId);
        }
    }

    public void testLaunchableWorkItemProperties() throws Exception
    {
        OwWorkflowDescription adhockWorkflowDescription = getAdhockProcessDescritpion();

        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(adhockWorkflowDescription, null);

        OwObjectClass objectClass = launchableWfItem.getObjectClass();
        System.err.println("launchableWfItem class: " + objectClass.getClassName());

        System.err.println("Properties :");
        OwPropertyCollection allProperties = launchableWfItem.getProperties(null);
        for (Object element : allProperties.values())
        {
            OwProperty property = (OwProperty) element;
            System.err.println(property.getPropertyClass().getClassName());
        }

        //P:cm:ownable.cm:owner
        OwProperty cmOwner = launchableWfItem.getProperty("P:cm:ownable.cm:owner");
        Assert.assertNotNull(cmOwner);

        //bpm:assignee
        OwProperty bpmAssignee = launchableWfItem.getProperty("bpm:assignee");
        Assert.assertNotNull(bpmAssignee);
    }

    public void testForwardWorkItem() throws Exception
    {
        String wfId = null;
        try
        {
            OwWorkflowDescription adhockWorkflowDescription = getAdhockProcessDescritpion();
            OwAlfrescoBPMLaunchableWorkItem launchableWfItem = (OwAlfrescoBPMLaunchableWorkItem) getBPMRepository().createLaunchableItem(adhockWorkflowDescription, null);
            launchableWfItem.dispatch();
            wfId = launchableWfItem.getWorkflowInstanceId();

            OwAlfrescoBPMWorkItem myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX);
            Assert.assertNotNull(myWorkItem);

            //OwProperty cmOwner = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_CM_OWNER);
            //OwProperty assignee = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_BPM_ASSIGNEE);

            myWorkItem.reassignToUserContainer("admin", false);

            myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX);
            Assert.assertNull(myWorkItem);

            //TODO check that the Admin has it in her QUEUE
            //            this.getNetwork().logout();
            //
            //            login("JUnitTester", "junit");
            //            myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMWorkitemContainer.ID_QUEUE_INBOX);
            //            Assert.assertNotNull(myWorkItem);
        }
        finally
        {
            if (null != wfId)
            {
                //                this.getNetwork().logout();
                //                loginAdmin();
                deleteWorkflowInstance(wfId);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testCompleteWorkItem() throws Exception
    {
        OwWorkflowDescription adhockWorkflowDescription = getAdhockProcessDescritpion();

        OwWorkitemContainer inboxQueue = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwObjectCollection myWorkItemsBefore = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(adhockWorkflowDescription, null);

        try
        {
            launchableWfItem.dispatch();

            OwObjectCollection myWorkItemsAfter = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

            Map<String, OwAlfrescoBPMWorkItem> diff = new HashMap<String, OwAlfrescoBPMWorkItem>();
            for (Object workItem : myWorkItemsAfter)
            {
                diff.put(((OwAlfrescoBPMWorkItem) workItem).getID(), (OwAlfrescoBPMWorkItem) workItem);
            }
            for (Object workItem : myWorkItemsBefore)
            {
                diff.remove(((OwAlfrescoBPMWorkItem) workItem).getID());
            }

            Assert.assertEquals(1, diff.size());

            myWorkItemsBefore = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);
            OwAlfrescoBPMWorkItem firstWorkItem = (OwAlfrescoBPMWorkItem) myWorkItemsBefore.iterator().next();

            firstWorkItem.dispatch();
            myWorkItemsAfter = inboxQueue.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);

            OwAlfrescoBPMWorkItem secondWorkItem = (OwAlfrescoBPMWorkItem) myWorkItemsAfter.iterator().next();

            Assert.assertFalse(firstWorkItem.getID() == secondWorkItem.getID());
            Assert.assertEquals(firstWorkItem.getWorkflowInstanceId(), secondWorkItem.getWorkflowInstanceId());
        }
        finally
        {
            String wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchableWfItem).getWorkflowInstanceId();
            deleteWorkflowInstance(wfId);
        }
    }

    @SuppressWarnings({ "unchecked" })
    public void testStartWorkflowWithNullDueDate() throws Exception
    {
        // See OWD-5293
        OwWorkflowDescription processDescription = getAdhockProcessDescritpion();
        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(processDescription, null);

        try
        {
            Assert.assertNotNull(launchableWfItem);

            OwPropertyCollection properties = new OwStandardPropertyCollection();

            OwProperty propWorkflowDescription = launchableWfItem.getProperty("bpm:workflowDescription");
            propWorkflowDescription.setValue("Foo baz - " + System.currentTimeMillis());
            properties.put("bpm:workflowDescription", propWorkflowDescription);

            OwProperty propBpmDueDate = launchableWfItem.getProperty("bpm:dueDate");
            propBpmDueDate.setValue(null);
            properties.put("bpm:dueDate", propBpmDueDate);

            OwProperty propBpmWorkflowDueDate = launchableWfItem.getProperty("bpm:workflowDueDate");
            propBpmWorkflowDueDate.setValue(null);
            properties.put("bpm:workflowDueDate", propBpmWorkflowDueDate);

            launchableWfItem.setProperties(properties);
            launchableWfItem.dispatch();

            String wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchableWfItem).getWorkflowInstanceId();
            OwAlfrescoBPMWorkItem myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX);

            OwProperty dueDate = myWorkItem.getProperty("bpm:dueDate");
            assertNotNull(dueDate);
            assertNull(dueDate.getValue());
        }
        finally
        {
            String wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchableWfItem).getWorkflowInstanceId();
            deleteWorkflowInstance(wfId);
        }
    }
}
