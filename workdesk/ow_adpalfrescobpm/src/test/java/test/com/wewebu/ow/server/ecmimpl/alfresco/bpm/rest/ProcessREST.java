package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Status;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessDefinitionStartFormModelResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessDefinitionsResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessImageResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessInstanceResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskFormModelResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.BasicList;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Process;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessDefinition;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessDefinitions;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessEntry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.StartProcessBody;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariableDefinition;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariablesDefinitions;

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
public class ProcessREST extends AlfrescoBPMNativeFixture
{
    @Ignore
    @Test
    public void testGetProcessDefinitions() throws Exception
    {
        ProcessDefinitionsResource resource = restFactory.processDefinitionsResource(new OperationContext());
        ProcessDefinitions definitions = null;
        try
        {
            definitions = resource.list();
        }
        finally
        {
            Representation responseEntity = resource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        Assert.assertNotNull(definitions);
        Assert.assertNotNull(definitions.list);
        Assert.assertFalse(0 == definitions.list.getEntries().length);

        for (Entry<ProcessDefinition> element : definitions.list.getEntries())
        {
            System.err.println(element.getEntry().getName());
        }
    }

    @Ignore
    @Test
    public void testStartFormModel() throws Exception
    {
        ProcessDefinitionStartFormModelResource resource = restFactory.processDefinitionStartFormModelResource("activitiAdhoc:1:4");
        VariablesDefinitions result = resource.list();
        BasicList<VariableDefinition> list = result.list;

        VariableDefinition bpm_assignee = null;
        for (Entry<VariableDefinition> entry : list.getEntries())
        {
            VariableDefinition variableDef = entry.getEntry();
            if ("bpm_assignee".equals(variableDef.getName()))
            {
                bpm_assignee = variableDef;
                break;
            }
        }

        Assert.assertNotNull(bpm_assignee);
        Assert.assertTrue(bpm_assignee.isRequired());
        Assert.assertEquals("{http://www.alfresco.org/model/bpm/1.0}assignee", bpm_assignee.getQualifiedName());
        Assert.assertEquals("bpm_assignee", bpm_assignee.getName());
    }

    @Ignore
    @Test
    public void testTaskFormModel() throws Exception
    {
        TaskFormModelResource resource = restFactory.taskFormModelResource("41955");
        VariablesDefinitions result = resource.list();
        BasicList<VariableDefinition> list = result.list;

        VariableDefinition owdbpm_jspStepProcessor = null;
        for (Entry<VariableDefinition> entry : list.getEntries())
        {
            VariableDefinition variableDef = entry.getEntry();
            if ("owdbpm_jspStepProcessor".equals(variableDef.getName()))
            {
                owdbpm_jspStepProcessor = variableDef;
                break;
            }
        }

        Assert.assertNotNull(owdbpm_jspStepProcessor);
        Assert.assertFalse(owdbpm_jspStepProcessor.isRequired());
        Assert.assertEquals("{http://www.wewebu.org/model/content/bpm/1.0}jspStepProcessor", owdbpm_jspStepProcessor.getQualifiedName());
        Assert.assertEquals("owdbpm_jspStepProcessor", owdbpm_jspStepProcessor.getName());
    }

    @Ignore
    @Test
    public void testGetWFDefinitionByID() throws Exception
    {
        ClientResource cr = new ClientResource(BASE_URI + "/service/api/workflow-definitions/activiti$activitiAdhoc:1:4");
        try
        {
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "admin");
            Representation response = cr.get();
            response.write(System.err);
        }
        finally
        {
            cr.release();
        }
    }

    @Ignore
    @Test
    public void testStartProcess() throws Exception
    {
        String processDefinitionId = "activitiAdhoc:1:4";

        StartProcessBody body = new StartProcessBody();
        body.setProcessDefinitionId(processDefinitionId);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("bpm_assignee", "admin");
        variables.put("bpm_dueDate", "2013-05-03T11:51:43.640+02:00");
        variables.put("bpm_percentComplete", 10);
        variables.put("bpm_priority", 3);
        variables.put("bpm_workflowPriority", 3);

        body.setVariables(variables);

        ProcessesResource resource = restFactory.processesResource();
        Entry<Process> result = null;
        try
        {
            result = resource.start(body);
        }
        finally
        {
            Representation responseEntity = resource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getEntry());
        Assert.assertEquals(processDefinitionId, result.getEntry().getProcessDefinitionId());
    }

    @Ignore
    @Test
    public void testDeleteProcess() throws Exception
    {
        String processDefinitionId = "activitiAdhoc:1:4";

        StartProcessBody body = new StartProcessBody();
        body.setProcessDefinitionId(processDefinitionId);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("bpm_assignee", "admin");

        body.setVariables(variables);

        ProcessesResource startProcessResource = restFactory.processesResource();
        ProcessEntry newProcess = null;
        try
        {
            newProcess = startProcessResource.start(body);
        }
        finally
        {
            Representation responseEntity = startProcessResource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        ProcessInstanceResource processResource = restFactory.processInstance(newProcess.getEntry().getId());
        try
        {
            processResource.delete();
        }
        finally
        {
            Representation responseEntity = processResource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        try
        {
            processResource.get();
            Assert.fail("The process should have been deleted by now.");
        }
        catch (ResourceException e)
        {
            Assert.assertEquals(Status.CLIENT_ERROR_NOT_FOUND, e.getStatus());
        }
    }

    @Ignore
    @Test
    public void testDisplayGraphicalDiagram() throws Exception

    {
        //abs-alfone.alfresco.com:8080/alfresco/api/-default-/public/alfresco/versions/1/processes/55232
        //abs-alfone.alfresco.com:8080/alfresco/api/-default-/public/workflow/versions/1/processes/55232
        String processId = "55232";
        ProcessImageResource resource = restFactory.processImageResource(processId);
        InputRepresentation obj = resource.getImage();
        System.err.println(obj.getClass());

        File file = File.createTempFile("foo", ".png");

        file.deleteOnExit();

        OutputStream outputStream = new FileOutputStream(file);
        obj.write(outputStream);
        outputStream.close();
        System.err.println(file.getAbsolutePath());
        System.err.println(file.getAbsolutePath());

    }

}
