package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkitemContainerInterface;

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
public class TestQueueGetChildren extends AlfrescoBPMRepositoryFixture
{
    private static final int TASKS_COUNT = 25;
    private static final int PAGE_SIZE = 10;

    private Collection<String> processesIds = new ArrayList<String>();

    public TestQueueGetChildren(String name) throws Exception
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        for (int i = 0; i < TASKS_COUNT; i++)
        {
            String processId = startAdHoc(null);
            this.processesIds.add(processId);
        }
    }

    public void tearDown() throws Exception
    {
        for (String processId : this.processesIds)
        {
            deleteWorkflowInstance(processId);

        }
        super.tearDown();
    }

    //    @SuppressWarnings("unchecked")
    //    public void testInboxQueue() throws Exception
    //    {
    //        ArrayList<String> userQueuesIDs = new ArrayList<String>(getBPMRepository().getWorkitemContainerIDs(true, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER));
    //        Assert.assertEquals(2, userQueuesIDs.size());
    //        String inboxQueueID = userQueuesIDs.get(0);
    //        Assert.assertEquals(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, inboxQueueID);
    //
    //        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(inboxQueueID, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
    //        int inboxCount = inboxQueu.getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
    //        Assert.assertFalse(0 == inboxCount);
    //
    //        OwObjectCollection inboxTasks = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
    //        Assert.assertFalse(inboxTasks.isEmpty());
    //    }
    //
    //    @SuppressWarnings("unchecked")
    //    public void testUnassignedQueue() throws Exception
    //    {
    //        startPooledReviewTask(null);
    //
    //        ArrayList<String> userQueuesIDs = new ArrayList<String>(getBPMRepository().getWorkitemContainerIDs(true, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER));
    //        Assert.assertEquals(2, userQueuesIDs.size());
    //        String unassignedQueueID = userQueuesIDs.get(1);
    //        Assert.assertEquals(OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED, unassignedQueueID);
    //
    //        OwWorkitemContainer unassignedQueu = this.getBPMRepository().getWorkitemContainer(unassignedQueueID, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
    //        int inboxCount = unassignedQueu.getChildCount(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
    //        Assert.assertEquals(1, inboxCount);
    //
    //        OwObjectCollection inboxTasks = unassignedQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
    //        Assert.assertFalse(inboxTasks.isEmpty());
    //        OwAlfrescoBPMWorkItem unassignedTask = (OwAlfrescoBPMWorkItem) inboxTasks.get(0);
    //
    //        OwPropertyCollection allProps = unassignedTask.getProperties(null);
    //        for (Object objProp : allProps.values())
    //        {
    //            OwProperty prop = (OwProperty) objProp;
    //            System.err.println(prop.getPropertyClass().getClassName() + ":" + prop.getValue());
    //        }
    //
    //        OwProperty bpmAssignee = unassignedTask.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ASSIGNEE);
    //        Assert.assertNotNull(bpmAssignee);
    //        Assert.assertNull(bpmAssignee.getValue());
    //
    //        OwProperty bpmDescription = unassignedTask.getProperty("bpm:description");
    //        Assert.assertNotNull(bpmDescription);
    //        Assert.assertEquals("This is a pooled review task. Review group  ALFRESCO_ADMINISTRATORS", bpmDescription.getValue());
    //
    //        OwProperty isClaimable = unassignedTask.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE);
    //        Assert.assertNotNull(isClaimable);
    //        Assert.assertNotNull(isClaimable.getValue());
    //        Assert.assertTrue((Boolean) isClaimable.getValue());
    //
    //        OwProperty isReleasable = unassignedTask.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE);
    //        Assert.assertNotNull(isReleasable);
    //        Assert.assertNotNull(isReleasable.getValue());
    //        Assert.assertFalse((Boolean) isReleasable.getValue());
    //    }

    @SuppressWarnings({ "rawtypes", "unused" })
    public void testInboxWorkItems() throws Exception
    {
        OwAlfrescoBPMWorkitemContainerInterface inboxQueu = (OwAlfrescoBPMWorkitemContainerInterface) this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        //        OwObjectCollection workItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);

        OwLoadContext loadContext = new OwLoadContext();
        loadContext.setObjectTypes(OwObjectReference.OBJECT_TYPE_WORKITEM);
        loadContext.setMaxSize(100);
        loadContext.setPageSize(PAGE_SIZE);
        loadContext.setVersionSelection(0);

        OwIterable<OwObject> workItems = inboxQueu.getChildren(loadContext);
        Assert.assertFalse(0 == workItems.getTotalNumItems());

        int totalItemCount = 0;
        int pageCount = 0;
        OwIterable<OwObject> page;

        do
        {
            page = workItems.skipTo(totalItemCount).getPage();
            for (OwObject owObject : page)
            {
                totalItemCount++;
            }
            pageCount++;
        } while (page.getHasMoreItems());

        Assert.assertEquals(3, pageCount);
        Assert.assertEquals(TASKS_COUNT, totalItemCount);
    }
}
