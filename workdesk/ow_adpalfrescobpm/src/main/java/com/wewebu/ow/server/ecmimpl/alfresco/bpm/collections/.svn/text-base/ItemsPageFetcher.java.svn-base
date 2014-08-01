package com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwPage;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMRepository;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessInstanceItemsResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Item;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Items;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Pagination;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Fetches one page worth of {@link Item}s.
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
public class ItemsPageFetcher extends OwAbstractPageFetcher<Item>
{
    private static final Logger LOG = OwLog.getLogger(ItemsPageFetcher.class);
    private OwAlfrescoBPMRepository repository;
    private TaskInstance taskInstance;

    public ItemsPageFetcher(OwAlfrescoBPMRepository repository, TaskInstance taskInstance)
    {
        this(repository, taskInstance, OperationContext.DEF_MAX_ITEMS);
    }

    private ItemsPageFetcher(OwAlfrescoBPMRepository repository, TaskInstance taskInstance, long maxNumItems)
    {
        super(maxNumItems);
        this.repository = repository;
        this.taskInstance = taskInstance;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher#fetchPage(long)
     */
    @Override
    protected OwPage<Item> fetchPage(long skipCount)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Fetching a new page of variables for task  %s, from index %d to %d.", this.taskInstance.getId(), skipCount, skipCount + this.maxNumItems));
        }

        AlfrescoRESTFulFactory restfulFactory = this.repository.getRestFulFactory();
        ProcessInstanceItemsResource resource = restfulFactory.processInstanceItems(this.taskInstance.getProcessId());

        try
        {
            return new RestCallTemplate<ProcessInstanceItemsResource, OwPage<Item>>() {

                @Override
                protected OwPage<Item> execWith(ProcessInstanceItemsResource resource) throws OwException
                {
                    try
                    {
                        Items response = resource.list();
                        List<Item> items = new ArrayList<Item>();
                        for (Entry<Item> element : response.list.getEntries())
                        {
                            items.add(element.getEntry());
                        }
                        Pagination pagination = response.list.getPagination();
                        return new OwPage<Item>(items, pagination.getTotalItems(), pagination.isHasMoreItems());
                    }
                    catch (Exception ex)
                    {
                        throw new OwServerException("Could not load task items.", ex);
                    }
                }
            }.doCall(resource);
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
    public OwAbstractPageFetcher<Item> newCopy(int maxNumItems)
    {
        return new ItemsPageFetcher(repository, this.taskInstance, maxNumItems);
    }
}