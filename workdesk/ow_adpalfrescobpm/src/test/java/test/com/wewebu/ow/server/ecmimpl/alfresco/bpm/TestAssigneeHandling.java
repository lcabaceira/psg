package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMLaunchableWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem;
import com.wewebu.ow.unittest.util.OwTestUserInfo;

@Ignore
public class TestAssigneeHandling extends AlfrescoBPMRepositoryFixture
{
    public static final String WF = "InvoiceVerification:1:7804";

    private String wfId;

    public TestAssigneeHandling(String name) throws Exception
    {
        super(name);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        setUpAdminUser("admin");
        setUpAdminPassword("admin");

        OwWorkflowDescription desc = getProcessDefinition(WF);
        Assert.assertNotNull(desc);
        OwWorkitem launchItem = getBPMRepository().createLaunchableItem(desc, null);

        OwPropertyCollection properties = new OwStandardPropertyCollection();

        OwProperty prop = launchItem.getProperty("bpm:groupAssignee");
        prop.setValue("GROUP_ALFRESCO_ADMINISTRATORS");
        properties.put(prop.getPropertyClass().getClassName(), prop);

        prop = launchItem.getProperty("bpm:workflowDescription");
        prop.setValue("JUnit_test" + System.currentTimeMillis());
        properties.put(prop.getPropertyClass().getClassName(), prop);

        launchItem.setProperties(properties);

        launchItem.dispatch();
        wfId = ((OwAlfrescoBPMLaunchableWorkItem) launchItem).getWorkflowInstanceId();
    }

    @Test
    @Ignore
    public void testInvoiceVerificationDelegate() throws Exception
    {
        getNetwork().logout();
        loginAdmin();
        OwAlfrescoBPMWorkItem item = getWFItemForWorkflowInstance(wfId, OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED);
        OwProperty prop = item.getProperty("bpm:assignee");
        OwTestUserInfo info = new OwTestUserInfo("admin");
        prop.setValue(info);

        OwPropertyCollection props = new OwStandardPropertyCollection();
        props.put(prop.getPropertyClass().getClassName(), prop);

        item.setProperties(props);
        item.dispatch();
    }
}
