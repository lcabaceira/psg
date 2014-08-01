package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecmimpl.OwCredentialsConstants;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes.OwAlfrescoBPMObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.CMISPropertyAdapter;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OwRestException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessEntry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.StartProcessBody;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Fake {@link OwWorkitem} used to start a new ProcessREST.
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
 *@since 4.0.0.0
 */
@SuppressWarnings("rawtypes")
public class OwAlfrescoBPMLaunchableWorkItem extends OwAlfrescoBPMWorkItem
{
    private static final Logger LOG = OwLog.getLogger(OwAlfrescoBPMLaunchableWorkItem.class);
    private OwWorkflowDescription workflowDescription;

    @SuppressWarnings("unchecked")
    protected OwAlfrescoBPMLaunchableWorkItem(OwAlfrescoBPMLaunchableWorkitemContainer container, Collection attachmentobjects, OwAlfrescoBPMWorkflowDescription workflowDescription, OwAlfrescoBPMRepository bpmRepository) throws OwException
    {
        super();

        this.taskInstance = new TaskInstance();
        this.taskInstance.setProcessDefinitionId(workflowDescription.getId());

        this.container = container;
        this.bpmRepository = bpmRepository;
        this.properties = new OwAlfrescoBPMProperties();
        this.workflowDescription = workflowDescription;

        this.attachmentobjects = new LinkedList();
        if (null != attachmentobjects)
        {
            this.attachmentobjects.addAll(attachmentobjects);
        }
        this.taskInstanceVariables = Collections.emptyMap();
        this.objectClass = OwAlfrescoBPMLaunchableWorkItemObjectClass.forWorkflowDescription(container, workflowDescription);
    }

    @Override
    public void dispatch() throws Exception
    {
        String wfId = this.workflowDescription.getId();
        Map<String, Object> parameters = prepareParameters();

        //TODO add a default for bpm:groupAssignee too
        try
        {
            this.getProperty(OwAlfrescoBPMObjectClass.PROP_BPM_ASSIGNEE);
            String shrinkedBpmAssignee = CMISPropertyAdapter.variableNameForProperty(OwAlfrescoBPMObjectClass.PROP_BPM_ASSIGNEE);
            if (!parameters.containsKey(shrinkedBpmAssignee))
            {
                //Default is to assign to the current user
                String currentUserName = this.bpmRepository.getNetwork().getCredentials().getAuthInfo(OwCredentialsConstants.LOGIN_USR);
                parameters.put(shrinkedBpmAssignee, currentUserName);
            }
        }
        catch (OwObjectNotFoundException e)
        {
            //Go on
        }

        // filter out null values see OWD-5293
        Map<String, Object> filteredParameters = new HashMap<String, Object>();
        for (Map.Entry<String, Object> varEntry : parameters.entrySet())
        {
            Object val = varEntry.getValue();
            if (null != val)
            {
                filteredParameters.put(varEntry.getKey(), val);
            }
        }

        final StartProcessBody body = new StartProcessBody();
        body.setProcessDefinitionId(wfId);
        body.setVariables(filteredParameters);

        //Prepare attachment items
        if (null != this.attachmentobjects)
        {
            for (Object element : this.attachmentobjects)
            {
                OwCMISObject item = (OwCMISObject) element;
                body.addItem(item.getNativeID());
            }
        }

        AlfrescoRESTFulFactory restfulFactory = this.bpmRepository.getRestFulFactory();
        ProcessesResource resource = restfulFactory.processesResource();
        new RestCallTemplate<ProcessesResource, Void>() {

            @Override
            protected Void execWith(ProcessesResource resource) throws OwRestException
            {
                ProcessEntry result = resource.start(body);
                OwAlfrescoBPMLaunchableWorkItem.this.taskInstance.setProcessId(result.getEntry().getId());
                return null;
            }
        }.doCall(resource);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#canDispatch(int)
     */
    @Override
    public boolean canDispatch(int iContext_p) throws Exception
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#getLock(int)
     */
    @Override
    public boolean getLock(int iContext_p) throws Exception
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#save()
     */
    @Override
    protected void save()
    {
        //DO nothing
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection)
     */
    @Override
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        // TODO Auto-generated method stub
        super.setProperties(properties_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#loadAttachments()
     */
    @Override
    protected void loadAttachments()
    {
        //Do nothing, we already have them cached
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#canReassignToUserContainer(int)
     */
    @Override
    public boolean canReassignToUserContainer(int iContext_p) throws Exception
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#loadPropertyValue(java.lang.String, com.wewebu.ow.server.ecm.OwPropertyClass)
     */
    @Override
    protected Object loadPropertyValue(OwPropertyClass propertyClass) throws Exception
    {
        // When starting a workflow, unspecified properties have to have the default value set for them
        Object nativeValue = super.loadPropertyValue(propertyClass);
        if (null == nativeValue)
        {
            nativeValue = propertyClass.getDefaultValue();
        }
        return nativeValue;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem#getProperty(java.lang.String)
     */
    @Override
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        //TODO remove this crap and make sure that the class for a LaunchableWorkItem does not contain these properties 
        OwPropertyClass propertyClass = this.getObjectClass().getPropertyClass(strPropertyName_p);
        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_IS_CLAIMABLE))
        {
            return new OwStandardProperty(false, propertyClass);
        }

        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_IS_RELEASABLE))
        {
            return new OwStandardProperty(false, propertyClass);
        }

        if (strPropertyName_p.endsWith(OwAlfrescoBPMObjectClass.PROP_IS_POOLED))
        {
            return new OwStandardProperty(false, propertyClass);
        }

        return super.getProperty(strPropertyName_p);
    }
}