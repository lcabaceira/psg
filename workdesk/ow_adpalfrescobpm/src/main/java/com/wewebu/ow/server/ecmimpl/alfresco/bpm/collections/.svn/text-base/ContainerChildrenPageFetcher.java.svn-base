package com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.restlet.resource.ClientProxy;

import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwPage;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItem;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstancesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Pagination;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstances;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Knows how to retrieve a page worth of "children" from a {@link OwAlfrescoBPMWorkitemContainer} container, based on a {@link TaskInstancesResource}.
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
public class ContainerChildrenPageFetcher extends OwAbstractPageFetcher<OwObject>
{
    private static final Logger LOG = OwLog.getLogger(ContainerChildrenPageFetcher.class);
    private ResourceProvider<TaskInstancesResource> resourceProvider;
    private OwAlfrescoBPMWorkitemContainer container;

    /**
     * @param maxNumItems
     */
    public ContainerChildrenPageFetcher(OwAlfrescoBPMWorkitemContainer container, long maxNumItems, ResourceProvider<TaskInstancesResource> resourceProvider)
    {
        super(maxNumItems);
        this.container = container;
        this.resourceProvider = resourceProvider;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher#fetchPage(long)
     */
    @Override
    protected OwPage<OwObject> fetchPage(long skipCount)
    {
        TaskInstancesResource res = this.resourceProvider.createResource(this.maxNumItems, skipCount);
        try
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Fetching a new page for %s from %d to %d.", this.container.getName(), skipCount, skipCount + this.maxNumItems));
            }
            return new RestCallTemplate<TaskInstancesResource, OwPage<OwObject>>() {

                @Override
                protected OwPage<OwObject> execWith(TaskInstancesResource resource) throws OwException
                {
                    TaskInstances tasks = resource.list();
                    List<OwObject> result = new ArrayList<OwObject>();
                    for (Entry<TaskInstance> instance : tasks.list.getEntries())
                    {
                        result.add(new OwAlfrescoBPMWorkItem(instance.getEntry(), ContainerChildrenPageFetcher.this.container, ContainerChildrenPageFetcher.this.container.getBpmRepository()));
                    }

                    Pagination pagination = tasks.list.getPagination();
                    return new OwPage<OwObject>(result, pagination.getTotalItems(), pagination.isHasMoreItems());
                }
            }.doCall(res);
        }
        catch (OwException e)
        {
            throw new OwAlfrescoBPMException("Could not fetch page.", e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher#newCopy(int)
     */
    @Override
    public OwAbstractPageFetcher<OwObject> newCopy(int maxNumItems)
    {
        OwAbstractPageFetcher<OwObject> newInstance = new ContainerChildrenPageFetcher(this.container, maxNumItems, this.resourceProvider);
        return newInstance;
    }

    /**
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
    public static interface ResourceProvider<R extends ClientProxy>
    {
        R createResource(long maxNumItems, long skipCount);
    }
}
