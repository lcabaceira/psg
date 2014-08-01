package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMContentCollection;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMContentElement;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMDMSID;
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
public class TestAlfrescoBPMWorkitem extends AlfrescoBPMRepositoryFixture
{
    //    private static final String BPM_PERCENT_COMPLETE = "bpm:percentComplete";

    public TestAlfrescoBPMWorkitem(String name) throws Exception
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        this.startAdHoc(null);
    }

    public void tearDown() throws Exception
    {
        this.deleteWorkflowInstance(this.adHocWorkflowInstanceId);
        super.tearDown();
    }

    //    @SuppressWarnings({ "rawtypes", "unchecked" })
    //    public void testCmisCreationDate() throws Exception
    //    {
    //        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMWorkitemContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
    //        OwObjectCollection workItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
    //        Assert.assertFalse(workItems.isEmpty());
    //
    //        OwWorkitem aWorkItem = (OwWorkitem) workItems.get(0);
    //        OwObjectClass objectClass = aWorkItem.getObjectClass();
    //        Collection propertyClasses = objectClass.getPropertyClassNames();
    //
    //        Collection<String> names = aWorkItem.getObjectClass().getPropertyClassNames();
    //        for (String propName : names)
    //        {
    //            System.err.println(propName);
    //        }
    //
    //        Assert.assertTrue(propertyClasses.contains("cmis:document.cmis:creationDate"));
    //        Assert.assertTrue(propertyClasses.contains("cm:created"));
    //
    //        OwPropertyClass cmCreatedPropertyClass = objectClass.getPropertyClass("cm:created");
    //        Assert.assertNotNull(cmCreatedPropertyClass);
    //
    //        OwProperty cmCreatedProperty = aWorkItem.getProperty("cm:created");
    //        Assert.assertNotNull(cmCreatedProperty);
    //        Assert.assertNotNull(cmCreatedProperty.getValue());
    //
    //        OwPropertyClass creationDatePropertyClass = objectClass.getPropertyClass("cmis:creationDate");
    //        Assert.assertNotNull(creationDatePropertyClass);
    //
    //        OwProperty creationDateProperty = aWorkItem.getProperty("cmis:creationDate");
    //        Assert.assertNotNull(creationDateProperty);
    //        Assert.assertNull(creationDateProperty.getValue());
    //    }

    @SuppressWarnings("rawtypes")
    public void testLoadProperties() throws Exception
    {
        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwObjectCollection workItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        Assert.assertFalse(workItems.isEmpty());

        OwWorkitem aWorkItem = (OwWorkitem) workItems.get(0);
        OwObjectClass objectClass = aWorkItem.getObjectClass();
        Collection propertyClasses = objectClass.getPropertyClassNames();

        Assert.assertTrue(propertyClasses.contains("D:bpm:task.bpm:description"));
        Assert.assertTrue(propertyClasses.contains("D:bpm:task.bpm:status"));

        OwProperty bpmDescription = aWorkItem.getProperty("bpm:description");
        Assert.assertNotNull(bpmDescription);
        Assert.assertEquals(ADHOC_TASK_DESCRIPTION, bpmDescription.getValue());

        OwProperty bpmStatus = aWorkItem.getProperty("bpm:status");
        Assert.assertNotNull(bpmStatus);
        Assert.assertEquals("Not Yet Started", bpmStatus.getValue());
    }

    @SuppressWarnings("rawtypes")
    public void testGetPropertiesLongShortNames() throws Exception
    {
        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwObjectCollection workItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        Assert.assertFalse(workItems.isEmpty());

        OwWorkitem aWorkItem = (OwWorkitem) workItems.get(0);
        OwObjectClass objectClass = aWorkItem.getObjectClass();

        OwPropertyClass lPropStatus = objectClass.getPropertyClass("D:bpm:task.bpm:status");
        OwPropertyClass sPropStatus = objectClass.getPropertyClass("bpm:status");

        Assert.assertNotNull(lPropStatus);
        Assert.assertNotNull(sPropStatus);

        Assert.assertEquals("D:bpm:task.bpm:status", lPropStatus.getClassName());
        Assert.assertEquals("bpm:status", sPropStatus.getClassName());

        OwProperty sBpmStatus = aWorkItem.getProperty("bpm:status");
        Assert.assertNotNull(sBpmStatus);
        Assert.assertEquals("bpm:status", sBpmStatus.getPropertyClass().getClassName());
        Assert.assertEquals("bpm:status", sBpmStatus.getFieldDefinition().getClassName());
        Assert.assertEquals("Not Yet Started", sBpmStatus.getValue());

        OwProperty lBpmStatus = aWorkItem.getProperty("D:bpm:task.bpm:status");
        Assert.assertNotNull(sBpmStatus);
        Assert.assertEquals("D:bpm:task.bpm:status", lBpmStatus.getPropertyClass().getClassName());
        Assert.assertEquals("D:bpm:task.bpm:status", lBpmStatus.getFieldDefinition().getClassName());
        Assert.assertEquals("Not Yet Started", lBpmStatus.getValue());
    }

    @SuppressWarnings("unchecked")
    public void testSaveProperties() throws Exception
    {
        OwWorkflowDescription adhockWorkflowDescription = getAdhockProcessDescritpion();
        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        inboxQueu.setFilterType(OwWorkitemContainer.FILTER_TYPE_NONE);
        OwObjectCollection myWorkItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        List<String> myWorkItemIds = new ArrayList<String>();
        for (Object object : myWorkItems)
        {
            OwAlfrescoBPMWorkItem item = (OwAlfrescoBPMWorkItem) object;
            myWorkItemIds.add(item.getID());
        }

        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(adhockWorkflowDescription, null);
        Assert.assertNotNull(launchableWfItem);

        OwPropertyCollection properties = new OwStandardPropertyCollection();
        OwProperty propWorkflowDescription = launchableWfItem.getProperty("bpm:workflowDescription");
        propWorkflowDescription.setValue("Foo baz ");
        properties.put("bpm:workflowDescription", propWorkflowDescription);

        launchableWfItem.setProperties(properties);

        launchableWfItem.dispatch();

        inboxQueu = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        inboxQueu.setFilterType(OwWorkitemContainer.FILTER_TYPE_NONE);
        OwObjectCollection myNewWorkItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);

        List<String> myNewWorkItemIds = new ArrayList<String>();
        for (Object object : myNewWorkItems)
        {
            OwAlfrescoBPMWorkItem item = (OwAlfrescoBPMWorkItem) object;
            myNewWorkItemIds.add(item.getID());
        }

        myNewWorkItemIds.removeAll(myWorkItemIds);
        Assert.assertEquals(1, myNewWorkItemIds.size());

        String newWorkItemId = myNewWorkItemIds.get(0);
        OwAlfrescoBPMWorkItem newWorkItem = null;
        for (Object object : myNewWorkItems)
        {
            OwAlfrescoBPMWorkItem item = (OwAlfrescoBPMWorkItem) object;
            if (newWorkItemId.equals(item.getID()))
            {
                newWorkItem = item;
                break;
            }
        }

        Assert.assertNotNull(newWorkItem);

        String timeStamp = "T_" + System.currentTimeMillis();
        String cmisName = "cmis:name_" + timeStamp;
        String bpmDescription = "bpm:description_" + timeStamp;
        int bpmPercentComplete = 10;
        Date bpmDueDate = new Date();

        String[] propertyNamesArray = new String[] { "cmis:name", "bpm:description", "bpm:percentComplete", "bpm:dueDate" };
        List<String> propertyNames = Arrays.asList(propertyNamesArray);
        properties = newWorkItem.getProperties(propertyNames);

        OwProperty cmisNameProp = (OwProperty) properties.get("cmis:name");
        cmisNameProp.setValue(cmisName);

        OwProperty bpmDescriptionProp = (OwProperty) properties.get("bpm:description");
        bpmDescriptionProp.setValue(bpmDescription);

        OwProperty bpmPercentCompleteProperty = (OwProperty) properties.get("bpm:percentComplete");
        bpmPercentCompleteProperty.setValue(bpmPercentComplete);

        OwProperty bpmDueDateProperty = (OwProperty) properties.get("bpm:dueDate");
        bpmDueDateProperty.setValue(bpmDueDate);

        newWorkItem.setProperties(properties);

        myNewWorkItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        newWorkItem = null;
        for (Object object : myNewWorkItems)
        {
            OwAlfrescoBPMWorkItem item = (OwAlfrescoBPMWorkItem) object;
            if (newWorkItemId.equals(item.getID()))
            {
                newWorkItem = item;
                break;
            }
        }

        Assert.assertNotNull(newWorkItem);

        properties = newWorkItem.getProperties(propertyNames);

        cmisNameProp = (OwProperty) properties.get("cmis:name");
        Assert.assertEquals(cmisName, cmisNameProp.getValue());

        bpmDescriptionProp = (OwProperty) properties.get("bpm:description");
        Assert.assertEquals(bpmDescription, bpmDescriptionProp.getValue());

        bpmPercentCompleteProperty = (OwProperty) properties.get("bpm:percentComplete");
        Assert.assertEquals(bpmPercentComplete, bpmPercentCompleteProperty.getValue());

        bpmDueDateProperty = (OwProperty) properties.get("bpm:dueDate");
        Assert.assertEquals(bpmDueDate, bpmDueDateProperty.getValue());
    }

    public void testCmTitle() throws Exception
    {
        OwWorkitem workItem = getWorkItemById(getInboxItems(), this.adHocTaskId);
        OwPropertyClass cmTitleProp = workItem.getObjectClass().getPropertyClass(OwAlfrescoBPMObjectClass.PROP_OW_TASK_TITLE);

        Assert.assertNotNull(cmTitleProp);
    }

    //    @SuppressWarnings("unchecked")
    //    public void testResetPropertyValue() throws Exception
    //    {
    //        OwWorkitem workItem = getWorkItemById(getInboxItems(), this.adHockTaskId);
    //        OwProperty jspStepProcessorProp = workItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_STEPPROCESSOR_JSP_PAGE);
    //        OwProperty bpmPercentCompleteProp = workItem.getProperty(BPM_PERCENT_COMPLETE);
    //        Assert.assertNull(jspStepProcessorProp.getValue());
    //
    //        OwPropertyCollection properties = new OwStandardPropertyCollection();
    //        jspStepProcessorProp.setValue("foo/bar.jsp");
    //        properties.put(jspStepProcessorProp.getPropertyClass(), jspStepProcessorProp);
    //        bpmPercentCompleteProp.setValue(10);
    //
    //        workItem.setProperties(properties);
    //
    //        workItem = getWorkItemById(getInboxItems(), this.adHockTaskId);
    //        jspStepProcessorProp = workItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_STEPPROCESSOR_JSP_PAGE);
    //        bpmPercentCompleteProp = workItem.getProperty(BPM_PERCENT_COMPLETE);
    //        Assert.assertEquals("foo/bar.jsp", jspStepProcessorProp.getValue());
    //        Assert.assertEquals(10, bpmPercentCompleteProp.getValue());
    //
    //        properties = new OwStandardPropertyCollection();
    //        jspStepProcessorProp.setValue(null);
    //        bpmPercentCompleteProp.setValue(null);
    //        properties.put(jspStepProcessorProp.getPropertyClass(), jspStepProcessorProp);
    //
    //        workItem.setProperties(properties);
    //
    //        workItem = getWorkItemById(getInboxItems(), this.adHockTaskId);
    //        jspStepProcessorProp = workItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_STEPPROCESSOR_JSP_PAGE);
    //        bpmPercentCompleteProp = workItem.getProperty(BPM_PERCENT_COMPLETE);
    //        Assert.assertNull(jspStepProcessorProp.getValue());
    //        Assert.assertNull(bpmPercentCompleteProp.getValue());
    //    }

    public void testDelete() throws Exception
    {
        OwObjectCollection workItems = getInboxItems();
        Assert.assertFalse(workItems.isEmpty());

        OwWorkitem myAdHocWorkItem = getWorkItemById(workItems, this.adHocTaskId);
        Assert.assertNotNull(myAdHocWorkItem);

        myAdHocWorkItem.delete();
        workItems = getInboxItems();

        myAdHocWorkItem = getWorkItemById(workItems, this.adHocTaskId);
        Assert.assertNull(myAdHocWorkItem);
    }

    public void testWorkItemImage() throws Exception
    {
        //There is one Adhoc workflow started for me in the setup() method.
        OwObjectCollection workItems = getInboxItems();
        OwWorkitem adhocWorkItem = getWorkItemById(workItems, this.adHocTaskId);
        Assert.assertNotNull(adhocWorkItem);

        Assert.assertTrue(adhocWorkItem.hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS));
        OwContentCollection contentCollection = adhocWorkItem.getContentCollection();
        Assert.assertNotNull(contentCollection);
        Assert.assertEquals(1, contentCollection.getContentTypes().size());
        Assert.assertTrue(contentCollection.getContentTypes().contains(OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM));
        Assert.assertTrue(OwAlfrescoBPMContentCollection.class.isAssignableFrom(contentCollection.getClass()));

        OwContentElement wfImageElement = null;

        wfImageElement = contentCollection.getContentElement(OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM, 0);
        Assert.assertTrue(OwAlfrescoBPMContentElement.class.isAssignableFrom(wfImageElement.getClass()));

        String filePath = wfImageElement.getContentFilePath();
        Assert.assertNotNull(filePath);
        Assert.assertTrue(new File(filePath).exists());
        Assert.assertTrue(new File(filePath).isFile());

        Assert.assertEquals("image/png", wfImageElement.getMIMEType());

        //        final File tempFile = new File("c:\\test.png");
        //
        //        InputStream is = wfImageElement.getContentStream(null);
        //        tempFile.deleteOnExit();
        //        FileOutputStream out = new FileOutputStream(tempFile);
        //        IOUtils.copy(is, out);

        wfImageElement.releaseResources();
        Assert.assertFalse("Resources should be properly disposed off when releasing the content element.", new File(wfImageElement.getContentFilePath()).exists());

    }

    public void testDMSID() throws Exception
    {
        OwObjectCollection workItems = getInboxItems();
        OwWorkitem adhocTask = this.getWorkItemById(workItems, this.adHocTaskId);
        Assert.assertNotNull(adhocTask);

        String dmsidStr = adhocTask.getDMSID();
        OwAlfrescoBPMDMSID dmsid = new OwAlfrescoBPMDMSID(dmsidStr);

        Assert.assertEquals(OwAlfrescoBPMDMSID.DMSID_PREFIX, dmsid.getPrefix());
        Assert.assertEquals(adHocTaskId, dmsid.getId());
    }

    public void testgetObjectFromDMSID() throws Exception
    {
        OwObjectCollection workItems = getInboxItems();
        OwWorkitem adhocTask = this.getWorkItemById(workItems, this.adHocTaskId);
        Assert.assertNotNull(adhocTask);

        OwObject adhocTaskFromNetwork = this.getNetwork().getObjectFromDMSID(adhocTask.getDMSID(), true);
        Assert.assertNotNull(adhocTaskFromNetwork);
        Assert.assertEquals(adhocTask.getDMSID(), adhocTaskFromNetwork.getDMSID());
        Assert.assertTrue(OwAlfrescoBPMWorkItem.class.isAssignableFrom(adhocTaskFromNetwork.getClass()));
    }
}