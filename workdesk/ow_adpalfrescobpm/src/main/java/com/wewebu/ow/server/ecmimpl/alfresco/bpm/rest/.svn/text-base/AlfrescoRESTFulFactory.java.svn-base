package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.Form;
import org.restlet.data.Language;
import org.restlet.data.Parameter;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.WhereClause.Criterion;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Status;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverterFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old.AlfrescoRESTFulFactoryOld;
import com.wewebu.ow.server.field.OwSearchNode;

/**
 *<p>
 * Factory to create RESTful resources for the new <b>Alfresco Workflow Public REST API</b>.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since  4.2.0.0
 */
public class AlfrescoRESTFulFactory
{
    private static final Logger LOG = OwLog.getLogger(AlfrescoRESTFulFactory.class);
    protected static final String WORKFLOW_PUBLIC_REST_API_PATH = "/api/-default-/public/workflow/versions/1";

    private String baseURL;
    private NativeValueConverterFactory valueConverterFactory;
    private ArrayList<Preference<Language>> acceptedLanguages;
    private Client clientConnector;
    private Map<String, String> callUrls;
    private OwRestletAuthenticationHandler authHandler;

    @Deprecated
    private AlfrescoRESTFulFactoryOld oldAPIFactory;

    /**
     * Constructor for REST-Connection handler
     * @param baseURL String base part of URL
     * @param authHandler OwRestletAuthenticationHandler
     * @param currentLocale Locale
     * @param converterFactory NativeValueConverterFactory
     */
    public AlfrescoRESTFulFactory(String baseURL, OwRestletAuthenticationHandler authHandler, Locale currentLocale, NativeValueConverterFactory converterFactory)
    {
        this.baseURL = baseURL;
        this.authHandler = authHandler;
        this.callUrls = new HashMap<String, String>();
        this.valueConverterFactory = converterFactory;

        Language language = Language.valueOf(currentLocale.getLanguage());

        Preference<Language> currentLanguage = new Preference<Language>(language);
        this.acceptedLanguages = new ArrayList<Preference<Language>>();
        acceptedLanguages.add(currentLanguage);

        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(Protocol.HTTPS);
        protocols.add(Protocol.HTTP);
        this.clientConnector = new Client(protocols) {
            /* (non-Javadoc)
             * @see org.restlet.Client#handle(org.restlet.Request, org.restlet.Response)
             */
            @Override
            public void handle(Request request, Response response)
            {
                LOG.debug("REST : Calling resource " + request);
                super.handle(request, response);
            }
        };

        oldAPIFactory = new AlfrescoRESTFulFactoryOld(baseURL, authHandler, currentLocale, converterFactory);
    }

    /**
     * @return a REST resource for accessing the process-definitions service.
     * @throws OwRestException thrown if something goes wrong with the creation of the REST resource.
     */
    public ProcessDefinitionsResource processDefinitionsResource(OperationContext context) throws OwRestException
    {
        Form form = new Form();
        addPaginationParameters(form, context);

        String resourceURI;
        try
        {
            resourceURI = String.format(getCallUrl("/process-definitions?%s"), form.encode());
        }
        catch (IOException e)
        {
            throw new OwRestException("Could not encode query parameters.", e);
        }

        return createResourceFor(resourceURI, ProcessDefinitionsResource.class);
    }

    public ProcessDefinitionStartFormModelResource processDefinitionStartFormModelResource(String processDefinitionId) throws OwRestException
    {
        return createResourceFor(String.format(getCallUrl("/process-definitions/%s/start-form-model"), processDefinitionId), ProcessDefinitionStartFormModelResource.class);
    }

    public TaskFormModelResource taskFormModelResource(String taskId) throws OwRestException
    {
        return createResourceFor(String.format(getCallUrl("/tasks/%s/task-form-model"), taskId), TaskFormModelResource.class);
    }

    public ProcessesResource processesResource() throws OwRestException
    {
        return createResourceFor(getCallUrl("/processes"), ProcessesResource.class);
    }

    /**
     * Creates a resource for working with a task instance.
     * 
     * @param taskId the ID of the task that will be updated.
     * @param selectStatement comma separated list of the names of the fields that should be updated (this is needed for a partial update).
     * @return a resource that you can use to update a task.
     * @throws OwRestException
     */
    public TaskInstanceResource taskInstanceResource(String taskId, String selectStatement) throws OwRestException
    {
        String taskInstanceIdEncoded = null;
        try
        {
            taskInstanceIdEncoded = URLEncoder.encode(taskId, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the workflow definition ID!", e);
        }
        String resourceUri = String.format(getCallUrl("/tasks/%s"), taskInstanceIdEncoded);
        if (null != selectStatement)
        {
            resourceUri += "?select=" + selectStatement;
        }
        return createResourceFor(resourceUri, TaskInstanceResource.class);
    }

    /**
     * Builds a restlet for the /tasks resource.
     * 
     * @param assignee - return only tasks for which the assignee is this user.
     * @param candidateUser - return only tasks for which this user is a candidate.
     * @param candidateGroup - return only tasks for which this group is a candidate.
     * @param status - only return tasks with a given status.
     * @param filterCriteria_p - additional filtering based on tasks' variables should be expressed as a {@link OwSearchNode}. 
     * @return a resource which you can call to get a list of tasks matching the specified criteria.
     * @throws OwRestException
     */
    public TaskInstancesResource taskInstancesResource(String assignee, String candidateUser, String candidateGroup, Status status, OwSearchNode filterCriteria_p, OperationContext context) throws OwRestException
    {
        Form form = new Form();
        addPaginationParameters(form, context);

        WhereClause whereClauseObj = WhereClause.emptyClause();
        if (null != filterCriteria_p)
        {
            whereClauseObj = WhereClause.fromOwSearchNode(filterCriteria_p, this.valueConverterFactory);
        }

        //String whereClause = String.format("assignee='%s' AND status='%s'", assignee, status.toString());
        if (null != assignee)
        {
            whereClauseObj.addCriterion(new Criterion("assignee", null, "=", assignee));
        }
        if (null != candidateUser)
        {
            whereClauseObj.addCriterion(new Criterion("candidateUser", null, "=", candidateUser));
        }
        else if (null != candidateGroup)
        {
            whereClauseObj.addCriterion(new Criterion("candidateGroup", null, "=", candidateGroup));
        }
        whereClauseObj.addCriterion(new Criterion("status", null, "=", status.toString()));

        whereClauseObj.addCriterion(new Criterion("includeTaskVariables", null, "=", "true"));
        whereClauseObj.addCriterion(new Criterion("includeProcessVariables", null, "=", "true"));

        form.add(new Parameter("where", String.format("(%s)", whereClauseObj.toString())));

        String resourceURI;
        try
        {
            resourceURI = String.format(getCallUrl("/tasks?%s"), form.encode());
        }
        catch (IOException e)
        {
            throw new OwRestException("Could not encode query parameters.", e);
        }

        TaskInstancesResource result = createResourceFor(resourceURI, TaskInstancesResource.class);
        return result;
    }

    private void addPaginationParameters(Form form, OperationContext context)
    {
        long skipCount = context.getSkipCount();
        long maxItems = context.getMaxItems();

        if (skipCount >= 0)
        {
            form.add(new Parameter("skipCount", Long.toString(skipCount)));
        }

        if (maxItems >= 0)
        {
            form.add(new Parameter("maxItems", Long.toString(maxItems)));
        }
        else
        {
            form.add(new Parameter("maxItems", Long.toString(Long.MAX_VALUE)));
        }
    }

    public TaskInstancesResource taskInstancesForProcessResource(String processId) throws OwRestException
    {
        String processIdEncoded = null;
        try
        {
            processIdEncoded = URLEncoder.encode(processId, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the workflow definition ID!", e);
        }
        String resourceURI = getCallUrl("/processes/%s/tasks");
        return createResourceFor(String.format(resourceURI, processIdEncoded), TaskInstancesResource.class);
    }

    public ProcessInstanceItemsResource processInstanceItems(String processId) throws OwRestException
    {
        String processIdEncoded = null;
        try
        {
            processIdEncoded = URLEncoder.encode(processId, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the process ID!", e);
        }
        String resourceURI = String.format(getCallUrl("/processes/%s/items"), processIdEncoded);
        return createResourceFor(resourceURI, ProcessInstanceItemsResource.class);
    }

    public ProcessInstanceItemResource processInstanceItem(String processId, String itemGuidId) throws OwRestException
    {
        String processIdEncoded = null;
        try
        {
            processIdEncoded = URLEncoder.encode(processId, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the item ID!", e);
        }
        String resourceURI = String.format(getCallUrl("/processes/%s/items/%s"), processIdEncoded, itemGuidId);
        return createResourceFor(resourceURI, ProcessInstanceItemResource.class);
    }

    public ProcessInstanceResource processInstance(String processId) throws OwRestException
    {
        String processInstanceIdEncoded = null;
        if (processId != null)
        {
            try
            {
                processInstanceIdEncoded = URLEncoder.encode(processId, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new OwRestException("Could not encode the workflow definition ID!", e);
            }
        }
        String resourceURI = String.format(getCallUrl("/processes/%s"), processInstanceIdEncoded);
        return createResourceFor(resourceURI, ProcessInstanceResource.class);
    }

    public ProcessImageResource processImageResource(String processId) throws OwRestException
    {
        String resourceURI = String.format(getCallUrl("/processes/%s/image"), processId);
        return createResourceFor(resourceURI, ProcessImageResource.class);
    }

    public TaskInstanceVariableResource taskInstanceVariableResource(String taskId, String variableName) throws OwRestException
    {
        String taskInstanceIdEncoded = null;
        String variableNameEncoded = null;
        try
        {
            taskInstanceIdEncoded = URLEncoder.encode(taskId, "UTF-8");
            variableNameEncoded = URLEncoder.encode(variableName, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the workflow definition ID!", e);
        }
        String resourceUri = String.format(getCallUrl("/tasks/%s/variables/%s"), taskInstanceIdEncoded, variableNameEncoded);
        return createResourceFor(resourceUri, TaskInstanceVariableResource.class);
    }

    public TaskInstanceVariablesResource taskInstanceVariablesResource(String taskId) throws OwRestException
    {
        String taskInstanceIdEncoded = null;
        try
        {
            taskInstanceIdEncoded = URLEncoder.encode(taskId, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the workflow definition ID!", e);
        }
        String resourceUri = String.format(getCallUrl("/tasks/%s/variables"), taskInstanceIdEncoded);
        return createResourceFor(resourceUri, TaskInstanceVariablesResource.class);
    }

    public TaskInstanceStateResource taskInstanceStateResource(String taskInstanceId) throws OwRestException
    {
        String taskInstanceIdEncoded = null;
        try
        {
            taskInstanceIdEncoded = URLEncoder.encode(taskInstanceId, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new OwRestException("Could not encode the workflow definition ID!", e);
        }
        String resourceUri = String.format(getCallUrl("/tasks/%s?select=state"), taskInstanceIdEncoded);
        return createResourceFor(resourceUri, TaskInstanceStateResource.class);
    }

    /**
     * Returns a full URL based on provided action URI,
     * will cache the full URL and return it again if the action URI is requested again. 
     * @param actionUri String URI of the action 
     * @return String full URL for requested action URI
     */
    protected String getCallUrl(String actionUri)
    {
        String callUrl = callUrls.get(actionUri);
        if (callUrl == null)
        {
            callUrl = this.baseURL + WORKFLOW_PUBLIC_REST_API_PATH + actionUri;
            callUrls.put(actionUri, callUrl);
        }
        return callUrl;
    }

    private <T> T createResourceFor(String resourceURI, Class<T> wrappedClass) throws OwRestException
    {
        if (null == this.clientConnector)
        {
            throw new OwRestException("RESTFull Factory was already released.");
        }
        ClientResource cr = new ClientResource(resourceURI);
        this.authHandler.prepareCall(cr);
        cr.getConverterService().setEnabled(true);
        cr.setNext(this.clientConnector);

        T resource = cr.wrap(wrappedClass);

        final Uniform originalNext = cr.getNext();
        cr.setNext(new Uniform() {

            public void handle(Request request, Response response)
            {
                request.getClientInfo().setAcceptedLanguages(acceptedLanguages);
                if (null != originalNext)
                {
                    originalNext.handle(request, response);
                }
            }
        });

        cr.getClientInfo().setAcceptedLanguages(acceptedLanguages);
        return resource;
    }

    /**
     * Call this when you have finished working with this factory.
     * It will release all Restlet client connectors.
     */
    public void release()
    {
        try
        {
            this.clientConnector.stop();
            this.clientConnector = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.oldAPIFactory.release();
    }

    /**
     * @return the oldAPIFactory
     * @deprecated Will be removed in the future. Only temporary into service until the new API exposes enough information about the tasks.
     */
    public AlfrescoRESTFulFactoryOld getOldAPIFactory()
    {
        return oldAPIFactory;
    }
}