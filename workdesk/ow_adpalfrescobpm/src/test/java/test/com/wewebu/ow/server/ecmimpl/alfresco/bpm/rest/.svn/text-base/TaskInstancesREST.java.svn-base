package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstanceResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstanceVariablesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstancesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessEntry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.StartProcessBody;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Status;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstances;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Variable;

public class TaskInstancesREST extends AlfrescoBPMNativeFixture
{
    private static final int WORKFLOW_INSTANCES_COUNT = 110;

    @Ignore
    @Test
    public void testGetAdminTaskInstances() throws Exception
    {
        TaskInstancesResource resource = restFactory.taskInstancesResource("admin", null, null, Status.ACTIVE, null, new OperationContext());
        TaskInstances result = null;
        try
        {
            result = resource.list();
        }
        finally
        {
            Representation responseEntity = resource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        Assert.assertFalse(0 == result.list.getEntries().length);

        for (Entry<TaskInstance> instance : result.list.getEntries())
        {
            System.err.println(instance.getEntry().getProcessDefinitionId());
            System.err.println(instance.getEntry().getName());
            System.err.println(instance.getEntry().getDueAt());
        }
    }

    @Ignore
    @Test
    public void testListTaskInstancesWithPaging() throws Exception
    {

        String processDefinitionId = "activitiAdhoc:1:4";
        for (int i = 0; i < WORKFLOW_INSTANCES_COUNT; i++)
        {
            ProcessesResource processesResource = restFactory.processesResource();
            try
            {
                StartProcessBody body = new StartProcessBody();

                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("bpm_assignee", "admin");

                body.setProcessDefinitionId(processDefinitionId);
                body.setVariables(variables);
                processesResource.start(body);
            }
            finally
            {
                Representation responseEntity = processesResource.getClientResource().getResponseEntity();
                responseEntity.exhaust();
                responseEntity.release();
            }
            if (0 == (i % 10))
            {
                System.err.println("Started " + i + " instances of Adhoc Workflow.");
            }
        }

        //        TaskInstancesResource resource = restFactory.taskInstancesResource("admin", null, null, Status.ACTIVE, null, 10);
        //        TaskInstances result = null;
        //        try
        //        {
        //            result = resource.list();
        //        }
        //        finally
        //        {
        //            Representation responseEntity = resource.getClientResource().getResponseEntity();
        //            responseEntity.exhaust();
        //            responseEntity.release();
        //        }
        //
        //        Assert.assertFalse(0 == result.list.getEntries().length);
        //
        //        for (Entry<TaskInstance> instance : result.list.getEntries())
        //        {
        //            System.err.println(instance.getEntry().getProcessDefinitionId());
        //            System.err.println(instance.getEntry().getName());
        //            System.err.println(instance.getEntry().getDueAt());
        //        }
    }

    @Ignore
    @Test
    public void testGetMjacksonPooledTaskInstances() throws Exception
    {
        String url = "http://10.249.240.39:8080/alfresco/api/-default-/public/workflow/versions/1/tasks?maxItems=100&where=%28candidateUser%20%3D%20%27mjackson%27%20AND%20status%20%3D%20%27active%27%29";

        ClientResource cr = new ClientResource(url);
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "mjackson", "mjackson");
        cr.get().write(System.out);
    }

    @Ignore
    @Test
    public void testGetTaskForWhichAdminIsCandidate() throws Exception
    {
        TaskInstancesResource resource = restFactory.taskInstancesResource(null, "admin", null, Status.ACTIVE, null, new OperationContext());
        TaskInstances myPooledTasks = null;
        try
        {
            myPooledTasks = resource.list();
        }
        finally
        {
            Representation responseEntity = resource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        Assert.assertEquals(1, myPooledTasks.list.getEntries().length);

        for (Entry<TaskInstance> instance : myPooledTasks.list.getEntries())
        {
            System.err.println(instance.getEntry().getId());
            System.err.println(instance.getEntry().getProcessDefinitionId());
            System.err.println(instance.getEntry().getName());
            System.err.println(instance.getEntry().getDueAt());
        }
    }

    @Ignore
    @Test
    public void testGetTaskForWhichEmailContributorsIsCandidateGroup() throws Exception
    {
        TaskInstancesResource resource = restFactory.taskInstancesResource(null, null, "GROUP_EMAIL_CONTRIBUTORS", Status.ACTIVE, null, new OperationContext());
        TaskInstances myPooledTasks = null;
        try
        {
            myPooledTasks = resource.list();
        }
        finally
        {
            Representation responseEntity = resource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        Assert.assertEquals(1, myPooledTasks.list.getEntries().length);

        for (Entry<TaskInstance> instance : myPooledTasks.list.getEntries())
        {
            System.err.println(instance.getEntry().getId());
            System.err.println(instance.getEntry().getProcessDefinitionId());
            System.err.println(instance.getEntry().getName());
            System.err.println(instance.getEntry().getDescription());
            System.err.println(instance.getEntry().getDueAt());
        }
    }

    @Ignore
    @Test
    public void testEndTask() throws Exception
    {
        String taskId = URLEncoder.encode("activiti$870", "UTF-8");

        String resourceURI = BASE_URI + String.format("/service/api/workflow/task/end/%s", taskId);
        System.err.println(resourceURI);

        ClientResource cr = new ClientResource(resourceURI);
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");

        try
        {
            Representation result = cr.post(null);
            result.write(System.err);
        }
        catch (ResourceException e)
        {
            cr.getResponseEntity().write(System.err);
            throw e;
        }
        finally
        {
            cr.release();
        }
    }

    @Ignore
    @Test
    public void testReassignTask() throws Exception
    {
        TaskInstanceResource resource = restFactory.taskInstanceResource("36794", "assignee");
        JSONObject entity = new JSONObject();
        entity.put("assignee", "admin");

        Representation result = resource.getClientResource().put(entity);
        System.err.println(result);
    }

    @Ignore
    @Test
    public void testStartPooledReviewAndApproveActivitiProcess() throws Exception
    {
        String processDefinitionId = "activitiReviewPooled:1:12";
        ProcessesResource resource = restFactory.processesResource();

        StartProcessBody body = new StartProcessBody();
        body.setProcessDefinitionId(processDefinitionId);

        //See http://abs-alfone.alfresco.com:8080/alfresco/service/api/groups/ALFRESCO_ADMINISTRATORS
        Map<String, Object> parameters = new HashMap<String, Object>(0);
        //        parameters.put("bpm_groupAssignee", "ALFRESCO_ADMINISTRATORS");
        parameters.put("bpm_groupAssignee", "GROUP_ALFRESCO_ADMINISTRATORS");

        body.setVariables(parameters);

        try
        {
            ProcessEntry result = resource.start(body);
            System.err.println(result);
        }
        finally
        {
            System.err.println(resource.getClientResource().getResponseEntity().getText());
        }
    }

    @Ignore
    @Test
    public void startWithoutDueDate() throws Exception
    {
        String processDefinitionId = "activitiAdhoc:1:4";
        ProcessesResource resource = restFactory.processesResource();

        StartProcessBody body = new StartProcessBody();
        body.setProcessDefinitionId(processDefinitionId);

        //See http://abs-alfone.alfresco.com:8080/alfresco/service/api/groups/ALFRESCO_ADMINISTRATORS
        Map<String, Object> parameters = new HashMap<String, Object>(0);

        parameters.put("bpm_assignee", "admin");
        parameters.put("bpm_dueDate", null);
        parameters.put("bpm_workflowDueDate", null);

        body.setVariables(parameters);

        try
        {
            ProcessEntry result = resource.start(body);
            String processId = result.getEntry().getId();

            TaskInstancesResource resourceTasksInstances = restFactory.taskInstancesForProcessResource(processId);
            TaskInstances instances = resourceTasksInstances.list();
            for (Entry<TaskInstance> instance : instances.list.getEntries())
            {
                String instanceId = instance.getEntry().getId();
                TaskInstanceVariablesResource resourceVar = restFactory.taskInstanceVariablesResource(instanceId);
                Entry<Variable>[] variables = resourceVar.list().list.getEntries();

                Map<String, Variable> variablesMap = new HashMap<String, Variable>();
                for (Entry<Variable> entry : variables)
                {
                    Variable var = entry.getEntry();
                    variablesMap.put(var.getName(), var);
                }

                Variable bpmDueDate = variablesMap.get("bpm_dueDate");
                Variable bpmWorkflowDueDate = variablesMap.get("bpm_workflowDueDate");

                System.err.println(bpmDueDate);
                System.err.println(bpmWorkflowDueDate);

                Assert.assertNull(bpmDueDate.getValue());
                Assert.assertNull(bpmWorkflowDueDate.getValue());
            }

        }
        finally
        {
            System.err.println(resource.getClientResource().getResponseEntity().getText());
        }
    }

    @Ignore
    @Test
    public void testSetDateValue() throws Exception
    {
        String taskId = URLEncoder.encode("152239", "UTF-8");

        String resourceURI = BASE_URI + String.format("/api/-default-/public/workflow/versions/1/tasks/%s/variables/%s", taskId, "bpm_dueDate");
        System.err.println(resourceURI);

        ClientResource cr = new ClientResource(resourceURI);
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");

        String newDate = "2013-11-07T23:15:25.000+0000";
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "bpm_dueDate");
            jsonObject.put("value", newDate);
            jsonObject.put("scope", "local");

            Representation result = cr.put(jsonObject);
            JSONObject json = new JSONObject(result.getText());
            Object value = json.getJSONObject("entry").get("value");
            Assert.assertEquals(newDate, value);
        }
        catch (ResourceException e)
        {
            cr.getResponseEntity().write(System.err);
            throw e;
        }
        finally
        {
            cr.release();
        }

        resourceURI = BASE_URI + String.format("/api/-default-/public/workflow/versions/1/tasks/%s/variables", taskId);
        System.err.println(resourceURI);

        cr = new ClientResource(resourceURI);
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");

        try
        {
            Representation result = cr.get();
            result = cr.get();
            JSONObject json = new JSONObject(result.getText());
            JSONArray value = json.getJSONObject("list").getJSONArray("entries");
            String retrievedVariableValue = null;
            for (int i = 0; i < value.length(); i++)
            {
                JSONObject entry = value.getJSONObject(i).getJSONObject("entry");
                String name = entry.getString("name");
                if ("bpm_dueDate".equals(name))
                {
                    retrievedVariableValue = entry.getString("value");
                    break;
                }
            }
            Assert.assertEquals(newDate, retrievedVariableValue);
        }
        catch (ResourceException e)
        {
            cr.getResponseEntity().write(System.err);
            throw e;
        }
        finally
        {
            cr.release();
        }

    }

    @Ignore
    @Test
    public void testSearchTasksDate() throws Exception
    {
        //d:dateTime after/before
        //String whereClause = "dueAt='2013-09-17T11:52:46.865+0000'"; // same value as bpm_workflowDueDate not bpm_dueDate
        String whereClause = "variables/local/owdbpm_resubmissionDate >= 'd:date 2013-11-19T22:00:00.000+0000'"; // same value as bpm_workflowDueDate not bpm_dueDate
        //String whereClause = "dueAt <= '2013-09-2T00:00:00.000+0300'";
        //String whereClause = "assignee = 'admin' AND priority = '2'";

        Form form = new Form();
        form.add("where", "(" + whereClause + ")");

        String resourceURI = BASE_URI + String.format("/api/-default-/public/workflow/versions/1/tasks?%s", form.encode());
        System.err.println(resourceURI);
        ClientResource cr = new ClientResource(resourceURI);
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");

        try
        {
            Representation result = cr.get();
            JSONObject jsonObject = new JSONObject(result.getText());
            JSONArray list = jsonObject.getJSONObject("list").getJSONArray("entries");

            Assert.assertEquals(1, list.length());
        }
        catch (ResourceException e)
        {
            cr.getResponseEntity().write(System.err);
            throw e;
        }
        finally
        {
            cr.release();
        }
    }

    @Test
    @Ignore
    public void testSearchTasksText() throws Exception
    {
        //variables of type d:text (isLike, equals etc.)
        String whereClause = "variables/bpm_comment MATCHES('A % comment%')"; //MATCHES('bla%')

        String resourceURI = BASE_URI + String.format("/api/-default-/public/workflow/versions/1/tasks?where=(%s)", URLEncoder.encode(whereClause, "UTF-8"));
        System.err.println(resourceURI);
        ClientResource cr = new ClientResource(resourceURI);
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");

        try
        {
            Representation result = cr.get();
            JSONObject jsonObject = new JSONObject(result.getText());
            JSONArray list = jsonObject.getJSONObject("list").getJSONArray("entries");

            Assert.assertEquals(2, list.length());
        }
        catch (ResourceException e)
        {
            cr.getResponseEntity().write(System.err);
            throw e;
        }
        finally
        {
            cr.release();
        }
    }

    @Ignore
    @Test
    public void testDate() throws Exception
    {
        System.err.println((Calendar.getInstance().getTimeZone()));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = format.parse("2013-11-07T23:15:25.000+0400");
        System.err.println(date);
        System.err.println(format.format(date));
    }
}