package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMLaunchableWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMObjectClass;

public class TestAlfrescoBPMMultiInstanceWorkItem extends AlfrescoBPMRepositoryFixture
{
    String wfId = null;

    public TestAlfrescoBPMMultiInstanceWorkItem(String name) throws Exception
    {
        super(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        OwWorkflowDescription processDescription = getParallelGroupReviewProcessDescription();
        OwWorkitem launchableWfItem = getBPMRepository().createLaunchableItem(processDescription, null);

        OwPropertyCollection properties = new OwStandardPropertyCollection();

        OwProperty propGroupAssignee = launchableWfItem.getProperty("bpm:groupAssignee");
        propGroupAssignee.setValue("GROUP_ALFRESCO_ADMINISTRATORS");
        properties.put(propGroupAssignee.getPropertyClass(), propGroupAssignee);

        launchableWfItem.setProperties(properties);

        launchableWfItem.dispatch();
        wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchableWfItem).getWorkflowInstanceId();
    }

    @Override
    protected void tearDown() throws Exception
    {
        deleteWorkflowInstance(wfId);
        super.tearDown();
    }

    @SuppressWarnings({ "rawtypes" })
    public void testProperties() throws Exception
    {
        OwAlfrescoBPMWorkItem myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX);
        assertNotNull(myWorkItem);

        Collection propertyNames = myWorkItem.getObjectClass().getPropertyClassNames();
        for (Object propName : propertyNames)
        {
            System.err.println(propName);
        }

        OwProperty state = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_STATE);
        assertNotNull(state);
        assertEquals("claimed", state.getValue());

        OwAlfrescoBPMWorkItem alfWorkItem = myWorkItem;
        //        alfWorkItem.isMultiInstance();
    }

}
