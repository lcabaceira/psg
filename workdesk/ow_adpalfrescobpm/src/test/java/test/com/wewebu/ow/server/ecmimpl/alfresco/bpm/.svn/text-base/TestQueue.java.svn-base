package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;

import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMObjectClass;

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
public class TestQueue extends AlfrescoBPMRepositoryFixture
{
    public TestQueue(String name) throws Exception
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        startAdHoc(null);
    }

    public void tearDown() throws Exception
    {
        deleteWorkflowInstance(this.adHocWorkflowInstanceId);
        if (null != this.pooledWorkflowInstanceId)
        {
            deleteWorkflowInstance(this.pooledWorkflowInstanceId);
        }
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    public void testInboxQueue() throws Exception
    {
        ArrayList<String> userQueuesIDs = new ArrayList<String>(getBPMRepository().getWorkitemContainerIDs(true, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER));
        Assert.assertEquals(2, userQueuesIDs.size());
        String inboxQueueID = userQueuesIDs.get(0);
        Assert.assertEquals(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, inboxQueueID);

        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(inboxQueueID, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        int inboxCount = inboxQueu.getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
        Assert.assertFalse(0 == inboxCount);

        OwObjectCollection inboxTasks = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        Assert.assertFalse(inboxTasks.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testUnassignedQueue() throws Exception
    {
        startPooledReviewTask(null);

        ArrayList<String> userQueuesIDs = new ArrayList<String>(getBPMRepository().getWorkitemContainerIDs(true, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER));
        Assert.assertEquals(2, userQueuesIDs.size());
        String unassignedQueueID = userQueuesIDs.get(1);
        Assert.assertEquals(OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED, unassignedQueueID);

        OwWorkitemContainer unassignedQueu = this.getBPMRepository().getWorkitemContainer(unassignedQueueID, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        int inboxCount = unassignedQueu.getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
        Assert.assertEquals(1, inboxCount);

        OwObjectCollection inboxTasks = unassignedQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        Assert.assertFalse(inboxTasks.isEmpty());
        OwAlfrescoBPMWorkItem unassignedTask = (OwAlfrescoBPMWorkItem) inboxTasks.get(0);

        OwPropertyCollection allProps = unassignedTask.getProperties(null);
        for (Object objProp : allProps.values())
        {
            OwProperty prop = (OwProperty) objProp;
            System.err.println(prop.getPropertyClass().getClassName() + ":" + prop.getValue());
        }

        OwProperty bpmAssignee = unassignedTask.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ASSIGNEE);
        Assert.assertNotNull(bpmAssignee);
        Assert.assertNull(bpmAssignee.getValue());

        OwProperty bpmDescription = unassignedTask.getProperty("bpm:description");
        Assert.assertNotNull(bpmDescription);
        Assert.assertEquals("This is a pooled review task. Review group  ALFRESCO_ADMINISTRATORS", bpmDescription.getValue());

        OwProperty isClaimable = unassignedTask.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE);
        Assert.assertNotNull(isClaimable);
        Assert.assertNotNull(isClaimable.getValue());
        Assert.assertTrue((Boolean) isClaimable.getValue());

        OwProperty isReleasable = unassignedTask.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE);
        Assert.assertNotNull(isReleasable);
        Assert.assertNotNull(isReleasable.getValue());
        Assert.assertFalse((Boolean) isReleasable.getValue());

        OwProperty isPooled = unassignedTask.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_POOLED);
        Assert.assertNotNull(isPooled);
        Assert.assertNotNull(isPooled.getValue());
        Assert.assertTrue((Boolean) isPooled.getValue());
    }

    @SuppressWarnings("rawtypes")
    public void testInboxWorkItem() throws Exception
    {
        OwObjectCollection workItems = getInboxItems();
        Assert.assertFalse(workItems.isEmpty());

        OwWorkitem myAdHokWorkItem = getWorkItemById(workItems, this.adHocTaskId);
        Assert.assertNotNull(myAdHokWorkItem);

        String className = myAdHokWorkItem.getClassName();
        OwObjectClass workItemClass = this.getNetwork().getObjectClass(className, null);
        Assert.assertNotNull(workItemClass);

        Collection names = workItemClass.getPropertyClassNames();
        Assert.assertFalse(names.isEmpty());
    }
}
