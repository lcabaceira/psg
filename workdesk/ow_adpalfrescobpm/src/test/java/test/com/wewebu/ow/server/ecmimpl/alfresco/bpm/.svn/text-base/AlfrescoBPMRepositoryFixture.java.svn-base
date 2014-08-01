package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import test.com.wewebu.ow.server.ecm.OwIntegrationTest;
import test.com.wewebu.ow.server.ecm.OwIntegrationTestSetup;

import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.OwCredentialsConstants;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMBaseContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMRepository;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OwRestException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessInstanceResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstancesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessEntry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.StartProcessBody;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstances;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwException;

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
public abstract class AlfrescoBPMRepositoryFixture extends OwIntegrationTest<OwNetwork>
{
    public static final String PROP_OPENCMIS_BINDING = "openCmisBinding"; // ws or atom
    private static final String OPENCMIS_BINDING_ATOM = "atom";
    private static final String OPENCMIS_BINDING_WS = "ws";

    private static final String ALFRESCOBPM_CMIS_RESOURCES_FOLDER = "alfrescobpm-cmis";

    private static final String OPENCMIS_ATOM_OWBOOTSTRAP_DEF_XML = "alfrescobpm_open_cmis_atom_owbootstrap_def.xml";
    private static final String OPENCMIS_WS_OWBOOTSTRAP_DEF_XML = "alfrescobpm_open_cmis_ws_owbootstrap_def.xml";
    private static final String OPENCMIS_INTEGRATION_TEST_SETUP_CLASS = "com.wewebu.ow.server.ecmimpl.opencmis.integration.OwCMISBasicIntegrationTestSetup";

    private static final String ADHOC_PROCESS_DEF_ID = "owdAdhoc:1:404";
    private static final String POOLED_REVIEW_PROCESS_DEF_ID = "activitiReviewPooled:1:12";
    private static final String PARALLEL_GROUP_REVIEW_PROCESS_DEF_ID = "activitiParallelGroupReview:1:20";
    private static final String PARALLEL_REVIEW_AND_APPROVE_PROCESS_DEF_ID = "activitiParallelReview:1:16";

    protected static final String ADHOC_TASK_DESCRIPTION = "a task for testing";

    protected String adHocWorkflowInstanceId;
    protected String pooledWorkflowInstanceId;
    protected String adHocTaskId;
    protected String pooledTaskId;

    public AlfrescoBPMRepositoryFixture(String name) throws Exception
    {
        super(name);
    }

    /* (non-Javadoc)
     * @see test.com.wewebu.ow.server.ecm.OwIntegrationTest#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        login("JUnitTester", "junit");
        getNetwork().getRoleManager().loginInit();
        purgeQueue(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        purgeQueue(OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
    }

    protected void purgeQueue(String queueId, int queueType) throws Exception
    {
        int numTries = 2;
        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(queueId, queueType);
        inboxQueu.setFilterType(OwWorkitemContainer.FILTER_TYPE_NONE);
        OwObjectCollection myWorkItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);
        do
        {
            for (Object el : myWorkItems)
            {
                OwAlfrescoBPMWorkItem workItem = (OwAlfrescoBPMWorkItem) el;
                try
                {
                    String wfId = workItem.getWorkflowInstanceId();
                    deleteWorkflowInstance(wfId);
                }
                catch (Exception e)
                {
                    //we did our best
                    e.printStackTrace();
                }
            }
            myWorkItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 200, 0, null);
        } while (!myWorkItems.isEmpty() && 0 < --numTries);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends OwIntegrationTestSetup> getSetupClass() throws ClassNotFoundException
    {
        Class<? extends OwIntegrationTestSetup> setupClass;
        setupClass = (Class<? extends OwIntegrationTestSetup>) this.getClass().getClassLoader().loadClass(OPENCMIS_INTEGRATION_TEST_SETUP_CLASS);
        System.err.println("Using setup class: " + setupClass.getName());
        return setupClass;
    }

    @Override
    protected String getConfigBootstrapName()
    {
        String resultCfg;
        String binding = System.getProperty(PROP_OPENCMIS_BINDING);
        if (OPENCMIS_BINDING_ATOM.equalsIgnoreCase(binding))
        {
            resultCfg = OPENCMIS_ATOM_OWBOOTSTRAP_DEF_XML;
        }
        else if (OPENCMIS_BINDING_WS.equalsIgnoreCase(binding))
        {
            resultCfg = OPENCMIS_WS_OWBOOTSTRAP_DEF_XML;
        }
        else
        {
            resultCfg = OPENCMIS_ATOM_OWBOOTSTRAP_DEF_XML;
        }

        System.err.println("Using configuration: " + resultCfg);
        return resultCfg;
    }

    @Override
    protected String getResoucesFolderPath()
    {
        return ALFRESCOBPM_CMIS_RESOURCES_FOLDER;
    }

    protected OwAlfrescoBPMRepository getBPMRepository() throws Exception
    {
        return (OwAlfrescoBPMRepository) this.getNetwork().getInterface(OwWorkitemRepository.class.getName(), null);
    }

    protected void tearDown() throws Exception
    {
        this.getNetwork().logout();
        super.tearDown();
    }

    protected OwWorkflowDescription getParallelGroupReviewProcessDescription() throws Exception
    {
        return getProcessDefinition(PARALLEL_GROUP_REVIEW_PROCESS_DEF_ID);
    }

    protected OwWorkflowDescription getPooledReviewProcessDescription() throws Exception
    {
        return getProcessDefinition(POOLED_REVIEW_PROCESS_DEF_ID);
    }

    protected OwWorkflowDescription getParallelReviewAndApproveProcessDescription() throws Exception
    {
        return getProcessDefinition(PARALLEL_REVIEW_AND_APPROVE_PROCESS_DEF_ID);
    }

    protected OwWorkflowDescription getAdhockProcessDescritpion() throws Exception
    {
        return getProcessDefinition(ADHOC_PROCESS_DEF_ID);
    }

    @SuppressWarnings("unchecked")
    protected OwWorkflowDescription getProcessDefinition(String processDefId) throws Exception
    {
        Collection<OwWorkflowDescription> wfDescriptions = getBPMRepository().getLaunchableWorkflowDescriptions(null);
        Assert.assertFalse(wfDescriptions.isEmpty());

        Map<String, OwWorkflowDescription> wfDescriptionsMap = new HashMap<String, OwWorkflowDescription>();
        for (OwWorkflowDescription owWorkflowDescription : wfDescriptions)
        {
            wfDescriptionsMap.put(owWorkflowDescription.getId(), owWorkflowDescription);
        }
        Assert.assertTrue("Process definition " + processDefId + " was not found.", wfDescriptionsMap.keySet().contains(processDefId));
        OwWorkflowDescription adhockWorkflowDescription = wfDescriptionsMap.get(processDefId);
        Assert.assertNotNull(adhockWorkflowDescription);
        return adhockWorkflowDescription;
    }

    protected OwObjectCollection getInboxItems() throws Exception, OwException
    {
        OwWorkitemContainer inboxQueu = this.getBPMRepository().getWorkitemContainer(OwAlfrescoBPMBaseContainer.ID_QUEUE_INBOX, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        OwObjectCollection workItems = inboxQueu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        return workItems;
    }

    protected String startAdHoc(OwCMISObject[] attachmentobjects) throws Exception
    {
        AlfrescoRESTFulFactory factory = getBPMRepository().getRestFulFactory();
        ProcessesResource resource = factory.processesResource();

        try
        {
            StartProcessBody body = new StartProcessBody();
            body.setProcessDefinitionId(AlfrescoBPMRepositoryFixture.ADHOC_PROCESS_DEF_ID);

            Map<String, Object> variables = new HashMap<String, Object>();
            OwCredentials credentials = this.getNetwork().getCredentials();
            String user = credentials.getAuthInfo(OwCredentialsConstants.LOGIN_USR);
            variables.put("bpm_assignee", user);
            variables.put("bpm_description", ADHOC_TASK_DESCRIPTION);

            body.setVariables(variables);

            if (null != attachmentobjects)
            {
                for (OwCMISObject item : attachmentobjects)
                {
                    body.addItem(item.getNativeID());
                }
            }

            ProcessEntry processEntry = resource.start(body);
            adHocWorkflowInstanceId = processEntry.getEntry().getId();
            Assert.assertNotNull(adHocWorkflowInstanceId);

            this.adHocTaskId = null;

            TaskInstancesResource taskInstancesResource = factory.taskInstancesResource(user, null, null, com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Status.ACTIVE, null, new OperationContext(0, 1000));
            TaskInstances tasks = taskInstancesResource.list();

            Assert.assertNotNull(tasks);
            for (Entry<TaskInstance> element : tasks.list.getEntries())
            {
                if (adHocWorkflowInstanceId.equals(element.getEntry().getProcessId()))
                {
                    adHocTaskId = element.getEntry().getId();
                    break;
                }
            }

            Assert.assertNotNull(adHocTaskId);
            return adHocWorkflowInstanceId;
        }
        finally
        {
            resource.getClientResource().release();
        }
    }

    protected void startPooledReviewTask(OwCMISObject[] attachmentobjects) throws Exception
    {
        AlfrescoRESTFulFactory factory = getBPMRepository().getRestFulFactory();
        ProcessesResource resource = factory.processesResource();

        try
        {
            StartProcessBody body = new StartProcessBody();
            body.setProcessDefinitionId(AlfrescoBPMRepositoryFixture.POOLED_REVIEW_PROCESS_DEF_ID);

            Map<String, Object> variables = new HashMap<String, Object>();
            OwCredentials credentials = this.getNetwork().getCredentials();
            String user = credentials.getAuthInfo(OwCredentialsConstants.LOGIN_USR);
            variables.put("bpm_groupAssignee", "GROUP_ALFRESCO_ADMINISTRATORS");
            variables.put("bpm_description", "This is a pooled review task. Review group  ALFRESCO_ADMINISTRATORS");

            body.setVariables(variables);

            if (null != attachmentobjects)
            {
                for (OwCMISObject item : attachmentobjects)
                {
                    body.addItem(item.getNativeID());
                }
            }

            ProcessEntry processEntry = resource.start(body);
            pooledWorkflowInstanceId = processEntry.getEntry().getId();
            Assert.assertNotNull(pooledWorkflowInstanceId);

            this.pooledTaskId = null;

            TaskInstancesResource taskInstancesResource = factory.taskInstancesResource(null, user, null, com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Status.ACTIVE, null, new OperationContext(0, 1000));
            TaskInstances tasks = taskInstancesResource.list();

            Assert.assertNotNull(tasks);
            for (Entry<TaskInstance> element : tasks.list.getEntries())
            {
                if (pooledWorkflowInstanceId.equals(element.getEntry().getProcessId()))
                {
                    pooledTaskId = element.getEntry().getId();
                    break;
                }
            }

            Assert.assertNotNull(pooledTaskId);
        }
        finally
        {
            resource.getClientResource().release();
        }
    }

    protected void deleteWorkflowInstance(String workflowInstanceId) throws Exception
    {
        AlfrescoRESTFulFactory restFulFactory = getBPMRepository().getRestFulFactory();
        ProcessInstanceResource resource = restFulFactory.processInstance(workflowInstanceId);
        new RestCallTemplate<ProcessInstanceResource, Void>() {

            @Override
            protected Void execWith(ProcessInstanceResource resource) throws OwRestException
            {
                // check if it exists
                resource.delete();
                return null;
            }

            protected void onResourceException(ResourceException resEx, ProcessInstanceResource resource) throws OwException
            {
                if (Status.CLIENT_ERROR_NOT_FOUND.equals(resEx.getStatus()))
                {
                    // WF Instance not found, move on
                    return;
                }
                else
                {
                    throw new OwRestException("Could not delete workflow instance.", resEx);
                }
            };
        }.doCall(resource);
    }

    protected OwWorkitem getWorkItemById(OwObjectCollection workItems, String taskId)
    {
        OwWorkitem myAdHokWorkItem = null;
        for (Object workitemObj : workItems)
        {
            OwWorkitem workitem = (OwWorkitem) workitemObj;
            if (taskId.equals(workitem.getID()))
            {
                myAdHokWorkItem = workitem;
                break;
            }
        }
        return myAdHokWorkItem;
    }

    protected OwAlfrescoBPMWorkItem getWFItemForWorkflowInstance(String wfId, String queueId) throws Exception, OwException
    {
        OwWorkitemContainer queu = this.getBPMRepository().getWorkitemContainer(queueId, OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER);
        queu.setFilterType(OwWorkitemContainer.FILTER_TYPE_NONE);
        OwObjectCollection myWorkItems = queu.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_WORKITEM }, null, null, 100, 0, null);
        for (Object element : myWorkItems)
        {
            OwAlfrescoBPMWorkItem workitem = (OwAlfrescoBPMWorkItem) element;
            if (wfId.equals(workitem.getWorkflowInstanceId()))
            {
                return workitem;
            }
        }

        return null;
    }
}
