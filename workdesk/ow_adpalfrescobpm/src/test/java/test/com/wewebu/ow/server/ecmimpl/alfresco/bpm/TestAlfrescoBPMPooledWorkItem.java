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

public class TestAlfrescoBPMPooledWorkItem extends AlfrescoBPMRepositoryFixture
{
    String wfId = null;

    public TestAlfrescoBPMPooledWorkItem(String name) throws Exception
    {
        super(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        OwWorkflowDescription processDescription = getPooledReviewProcessDescription();
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
        OwAlfrescoBPMWorkItem myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED);
        assertNotNull(myWorkItem);

        Collection propertyNames = myWorkItem.getObjectClass().getPropertyClassNames();
        for (Object propName : propertyNames)
        {
            System.err.println(propName);
        }

        OwProperty state = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_STATE);
        assertNotNull(state);
        assertEquals("unclaimed", state.getValue());

        OwProperty isClaimable = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE);
        assertNotNull(isClaimable);
        assertTrue((Boolean) isClaimable.getValue());

        OwProperty isReleasable = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE);
        assertNotNull(isReleasable);
        assertFalse((Boolean) isReleasable.getValue());

        OwProperty isPooled = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_POOLED);
        assertNotNull(isPooled);
        assertTrue((Boolean) isPooled.getValue());

        //        OwAlfrescoBPMWorkItem alfWorkItem = (OwAlfrescoBPMWorkItem) myWorkItem;
    }

    public void testClaimRelease() throws Exception
    {
        OwAlfrescoBPMWorkItem myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED);
        assertNotNull(myWorkItem);
        String myUserId = getNetwork().getCredentials().getUserInfo().getUserID();

        myWorkItem.reassignToUserContainer(myUserId, false);
        myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED);
        assertNull("The work item should not be in the unassigned queue anymore.", myWorkItem);

        myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX);
        assertNotNull(myWorkItem);

        OwProperty state = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_STATE);
        assertNotNull(state);
        assertEquals("claimed", state.getValue());

        OwProperty isClaimable = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE);
        assertNotNull(isClaimable);
        assertFalse((Boolean) isClaimable.getValue());

        OwProperty isReleasable = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE);
        assertNotNull(isReleasable);
        assertTrue((Boolean) isReleasable.getValue());

        myWorkItem.returnToSource();
        myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX);
        assertNull("After releasing, the workitem should be moved out from INBOX.", myWorkItem);

        myWorkItem = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED);
        assertNotNull(myWorkItem);

        state = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_STATE);
        assertNotNull(state);
        assertEquals("unclaimed", state.getValue());

        isClaimable = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE);
        assertNotNull(isClaimable);
        assertTrue((Boolean) isClaimable.getValue());

        isReleasable = myWorkItem.getProperty(OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE);
        assertNotNull(isReleasable);
        assertFalse((Boolean) isReleasable.getValue());

    }
}
