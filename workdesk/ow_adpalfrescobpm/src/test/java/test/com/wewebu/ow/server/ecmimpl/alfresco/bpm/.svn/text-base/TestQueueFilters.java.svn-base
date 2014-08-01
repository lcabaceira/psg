package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
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
public class TestQueueFilters extends AlfrescoBPMRepositoryFixture
{
    public TestQueueFilters(String name) throws Exception
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        startAdHoc(null);
        startAdHoc(null);
    }

    public void tearDown() throws Exception
    {
        purgeQueue(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    public void testFilterByResubmitDate() throws Exception
    {
        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        inboxQueu.setFilterType(OwWorkitemContainer.FILTER_TYPE_NONE);
        OwObjectCollection workItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        Assert.assertEquals(2, workItems.size());

        OwAlfrescoBPMWorkItem firstWorkItem = (OwAlfrescoBPMWorkItem) workItems.get(0);
        OwAlfrescoBPMWorkItem secondWorkItem = (OwAlfrescoBPMWorkItem) workItems.get(1);

        Calendar now = Calendar.getInstance();
        Calendar after = ((Calendar) now.clone());
        after.add(Calendar.DAY_OF_MONTH, 2);
        Calendar before = ((Calendar) now.clone());
        before.add(Calendar.DAY_OF_MONTH, -2);

        OwPropertyCollection properties = new OwStandardPropertyCollection();
        OwProperty resubmitProp = firstWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_RESUBMIT_DATE);
        resubmitProp.setValue(after.getTime());
        properties.put(resubmitProp.getPropertyClass(), resubmitProp);
        firstWorkItem.setProperties(properties);

        properties = new OwStandardPropertyCollection();
        resubmitProp = secondWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_OW_RESUBMIT_DATE);
        resubmitProp.setValue(before.getTime());
        properties.put(resubmitProp.getPropertyClass(), resubmitProp);
        secondWorkItem.setProperties(properties);

        inboxQueu.setFilterType(OwWorkitemContainer.FILTER_TYPE_NORMAL);
        OwObjectCollection workItemsNoResubmit = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        Assert.assertEquals(1, workItemsNoResubmit.size());

        Collection<String> workItemsNoResubmitIds = new HashSet<String>();
        for (Object object : workItemsNoResubmit)
        {
            OwAlfrescoBPMWorkItem workItem = (OwAlfrescoBPMWorkItem) object;
            workItemsNoResubmitIds.add(workItem.getID());
        }
        Assert.assertFalse(workItemsNoResubmitIds.contains(firstWorkItem.getID()));
        Assert.assertTrue(workItemsNoResubmitIds.contains(secondWorkItem.getID()));

        inboxQueu.setFilterType(OwWorkitemContainer.FILTER_TYPE_RESUBMISSION);
        OwObjectCollection workItemsResubmit = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        Assert.assertEquals(1, workItemsResubmit.size());

        Collection<String> workItemsResubmitIds = new HashSet<String>();
        for (Object object : workItemsResubmit)
        {
            OwAlfrescoBPMWorkItem workItem = (OwAlfrescoBPMWorkItem) object;
            workItemsResubmitIds.add(workItem.getID());
        }
        Assert.assertTrue(workItemsResubmitIds.contains(firstWorkItem.getID()));
        Assert.assertFalse(workItemsResubmitIds.contains(secondWorkItem.getID()));
    }
}
