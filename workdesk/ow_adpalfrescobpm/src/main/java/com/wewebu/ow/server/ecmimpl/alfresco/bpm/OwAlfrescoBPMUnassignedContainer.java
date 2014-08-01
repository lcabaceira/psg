package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.ContainerChildrenPageFetcher.ResourceProvider;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstancesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Status;

/**
 *<p>
 * Container to list unassigned tasks for which this user is a candidate.
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
 *@since 4.2.0.0
 */
@SuppressWarnings("rawtypes")
public class OwAlfrescoBPMUnassignedContainer extends OwAlfrescoBPMUserQueueContainer
{
    /**
     * @param network
     * @param bpmRepository
     */
    public OwAlfrescoBPMUnassignedContainer(OwNetwork network, OwAlfrescoBPMRepository bpmRepository)
    {
        super(network, bpmRepository, OwAlfrescoBPMBaseContainer.ID_QUEUE_UNASSIGNED);
    }

    @Override
    protected ResourceProvider getTasksInstancesResourceProvider(final OwLoadContext loadContext, final String user)
    {
        final AlfrescoRESTFulFactory restFulFactory = this.bpmRepository.getRestFulFactory();
        ResourceProvider<TaskInstancesResource> resourceProvider = new ResourceProvider<TaskInstancesResource>() {

            @Override
            public TaskInstancesResource createResource(long maxNumItems, long skipCount)
            {
                TaskInstancesResource res = restFulFactory.taskInstancesResource(null, user, null, Status.ACTIVE, loadContext.getFilter(), new OperationContext(skipCount, maxNumItems));
                return res;
            }
        };
        return resourceProvider;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getType()
     */
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getMIMEType()
     */
    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/pooleduser";
    }

    @Override
    public String createContaineeMIMEType(OwAlfrescoBPMWorkItem item)
    {
        return "ow_workitem/poolitem";
    }
}
