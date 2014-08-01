package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMLaunchableWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstancesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstances;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;

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
public class TestAttachments extends AlfrescoBPMRepositoryFixture
{
    private List<OwObject> attachments;
    private OwObject junitTestFolder;

    public TestAttachments(String name) throws Exception
    {
        super(name);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OwResource defaultResource = getNetwork().getResource(null);

        OwObject obj1 = getNetwork().getObjectFromPath(defaultResource.getID() + "/JUnitTest/TestDocument1", true);
        assertNotNull(obj1);

        OwObject obj2 = getNetwork().getObjectFromPath(defaultResource.getID() + "/JUnitTest/TestDocument2", true);
        assertNotNull(obj2);

        this.attachments = new ArrayList<OwObject>();
        attachments.add(obj1);
        attachments.add(obj2);

        OwObject folder = getNetwork().getObjectFromPath(defaultResource.getID() + "/JUnitTest", true);
        assertNotNull(folder);
        this.junitTestFolder = folder;
    }

    @Override
    protected void tearDown() throws Exception
    {
        this.attachments.clear();
        this.attachments = null;

        super.tearDown();
    }

    public void testPropertyAttachmentsLaunchItem() throws Exception
    {

        OwWorkflowDescription workflowDescription = getAdhockProcessDescritpion();
        OwWorkitem launchItem = getBPMRepository().createLaunchableItem(workflowDescription, null);

        OwPropertyClass bpmPackagePropClass = launchItem.getObjectClass().getPropertyClass(OwAlfrescoBPMObjectClass.PROP_BPM_PACKAGE);
        Assert.assertNotNull(bpmPackagePropClass);
        Assert.assertEquals(String.class.getName(), bpmPackagePropClass.getJavaClassName());

        OwProperty bpmPackageProp = launchItem.getProperty(OwAlfrescoBPMObjectClass.PROP_BPM_PACKAGE);
        Assert.assertNotNull(bpmPackageProp);
        Assert.assertNull(bpmPackageProp.getValue());

        OwPropertyClass owAttachmentsPropClass = launchItem.getObjectClass().getPropertyClass(OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS);
        Assert.assertNotNull(owAttachmentsPropClass);
        Assert.assertEquals(OwObject.class.getName(), owAttachmentsPropClass.getJavaClassName());

        OwProperty owAttachmentsProp = launchItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS);
        Assert.assertNotNull(owAttachmentsProp);
        Assert.assertEquals(0, ((Object[]) owAttachmentsProp.getValue()).length);
    }

    public void testLaunchWithAttachments() throws Exception
    {
        OwWorkflowDescription workflowDescription = getAdhockProcessDescritpion();
        OwAlfrescoBPMLaunchableWorkItem launchItem = (OwAlfrescoBPMLaunchableWorkItem) getBPMRepository().createLaunchableItem(workflowDescription, attachments);

        OwProperty owAttachmentsProp = launchItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS);
        Assert.assertNotNull(owAttachmentsProp);
        Assert.assertNotNull(owAttachmentsProp.getValue());

        String wfId = null;
        try
        {
            launchItem.dispatch();
            wfId = launchItem.getWorkflowInstanceId();

            TaskInstancesResource tasksResource = getBPMRepository().getRestFulFactory().taskInstancesForProcessResource(wfId);
            TaskInstances tasks = tasksResource.list();
            Assert.assertFalse(0 == tasks.list.getEntries().length);

            String myNewTaskId = tasks.list.getEntries()[0].getEntry().getId();
            OwWorkitem myNewTask = null;

            OwObjectCollection inboxTasks = getInboxItems();
            for (Object object : inboxTasks)
            {
                OwWorkitem task = (OwWorkitem) object;
                if (myNewTaskId.equals(task.getID()))
                {
                    myNewTask = task;
                    break;
                }
            }

            Assert.assertNotNull(myNewTask);

            OwProperty attachmentsProp = myNewTask.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS);
            Object[] attashmentsArray = (Object[]) attachmentsProp.getValue();

            List<Object> taskAttachments = Arrays.asList(attashmentsArray);
            Assert.assertEquals(attachments.size(), taskAttachments.size());
            //FIXME Sure that containsAll can be used? By default Object.equals method will compare references which are different
            //Assert.assertTrue("Missing attachments in Workflow", taskAttachments.containsAll(attachments));
        }
        finally
        {
            if (null != wfId)
            {
                deleteWorkflowInstance(wfId);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testAddAttachment() throws Exception
    {
        OwObject expectedAttachment = this.attachments.get(1);
        startAdHoc(new OwCMISObject[] { (OwCMISObject) expectedAttachment });

        try
        {
            OwWorkitem workItem = getWorkItemById(getInboxItems(), this.adHocTaskId);
            OwProperty attachmentsProp = workItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS);
            Object[] attachmentsArray = (Object[]) attachmentsProp.getValue();

            Assert.assertEquals(1, attachmentsArray.length);
            OwObject actualAttachment = (OwObject) attachmentsArray[0];
            Assert.assertEquals(expectedAttachment.getDMSID(), actualAttachment.getDMSID());

            OwObject newAttachemnt = this.attachments.get(0);

            attachmentsArray = new Object[] { newAttachemnt };
            attachmentsProp.setValue(attachmentsArray);

            OwPropertyCollection properties = new OwStandardPropertyCollection();
            properties.put(attachmentsProp.getPropertyClass(), attachmentsProp);

            workItem.setProperties(properties);
            try
            {
                Thread.sleep(3500l);
            }
            catch (InterruptedException ex)
            {
                //we need to wait, otherwise the WorkItem/Task is not available
            }
            workItem = getWorkItemById(getInboxItems(), this.adHocTaskId);
            Assert.assertNotNull("unable to retrieve workItem by Id = " + this.adHocTaskId, workItem);
            attachmentsProp = workItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS);
            attachmentsArray = (Object[]) attachmentsProp.getValue();

            actualAttachment = (OwObject) attachmentsArray[0];
            Assert.assertEquals(1, attachmentsArray.length);
            Assert.assertEquals(newAttachemnt, actualAttachment);
        }
        finally
        {
            this.deleteWorkflowInstance(this.adHocWorkflowInstanceId);
        }
    }

    public void testAddFolderAsAttachment() throws Exception
    {
        startAdHoc(new OwCMISObject[] { (OwCMISObject) this.junitTestFolder });

        try
        {
            OwWorkitem workItem = getWorkItemById(getInboxItems(), this.adHocTaskId);
            OwProperty attachmentsProp = workItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_ATTACHMENTS);
            Object[] attachmentsArray = (Object[]) attachmentsProp.getValue();

            Assert.assertEquals(1, attachmentsArray.length);
            OwObject actualAttachment = (OwObject) attachmentsArray[0];
            Assert.assertEquals(this.junitTestFolder.getDMSID(), actualAttachment.getDMSID());
        }
        finally
        {
            this.deleteWorkflowInstance(this.adHocWorkflowInstanceId);
        }
    }
}
