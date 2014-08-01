package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.ContainerChildrenPageFetcher.ResourceProvider;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstancesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Status;

/**
 *<p>
 * Container to list unassigned tasks for which this group is a candidate.
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
final class OwAlfrescoBPMGroupQueueContainer extends OwAlfrescoBPMUserQueueContainer
{
    private static final Logger LOG = OwLog.getLogger(OwAlfrescoBPMGroupQueueContainer.class);
    private OwUserInfo groupInfo;

    /**
     * @param network
     * @param bpmRepository
     * @param groupInfo
     * @throws Exception 
     */
    @SuppressWarnings("rawtypes")
    OwAlfrescoBPMGroupQueueContainer(OwNetwork network, OwAlfrescoBPMRepository bpmRepository, OwUserInfo groupInfo) throws Exception
    {
        super(network, bpmRepository, groupInfo.getUserName());
        this.groupInfo = groupInfo;
    }

    @Override
    protected ResourceProvider getTasksInstancesResourceProvider(final OwLoadContext loadContext, final String user)
    {
        final AlfrescoRESTFulFactory restFulFactory = this.bpmRepository.getRestFulFactory();
        final String groupName;
        try
        {
            groupName = this.groupInfo.getUserName();
        }
        catch (Exception e)
        {
            throw new OwAlfrescoBPMException("could not get group name!", e);
        }
        ResourceProvider<TaskInstancesResource> resourceProvider = new ResourceProvider<TaskInstancesResource>() {

            @Override
            public TaskInstancesResource createResource(long maxNumItems, long skipCount)
            {
                TaskInstancesResource res = restFulFactory.taskInstancesResource(null, null, groupName, Status.ACTIVE, loadContext.getFilter(), new OperationContext(skipCount, maxNumItems));
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
        return OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectReference#getMIMEType()
     */
    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/public";
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkitemContainer#getName()
     */
    @Override
    public String getName()
    {
        String id = this.getID();
        try
        {
            String displayName = this.groupInfo.getUserDisplayName();
            if (null != displayName)
            {
                return displayName;
            }
            else
            {
                LOG.error("It seems that my group's display name was not fetched from the backend.");
                return id;
            }
        }
        catch (Exception e)
        {
            LOG.error("Could not get my group's display name.", e);
            return id;
        }
    }
}