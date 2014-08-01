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
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskInstanceVariablesResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Pagination;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.TaskInstance;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Variable;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Variables;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Fetches one page worth of {@link Variable}s.
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
public class VariablesPageFetcher extends OwAbstractPageFetcher<Variable>
{
    private static final Logger LOG = OwLog.getLogger(VariablesPageFetcher.class);
    private OwAlfrescoBPMRepository repository;
    private TaskInstance taskInstance;

    public VariablesPageFetcher(OwAlfrescoBPMRepository repository, TaskInstance taskInstance)
    {
        this(repository, taskInstance, OperationContext.DEF_MAX_ITEMS);
    }

    private VariablesPageFetcher(OwAlfrescoBPMRepository repository, TaskInstance taskInstance, long maxNumItems)
    {
        super(maxNumItems);
        this.repository = repository;
        this.taskInstance = taskInstance;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher#fetchPage(long)
     */
    @Override
    protected OwPage<Variable> fetchPage(long skipCount)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Fetching a new page of variables for task  %s, from index %d to %d.", this.taskInstance.getId(), skipCount, skipCount + this.maxNumItems));
        }

        AlfrescoRESTFulFactory restFulFactory = this.repository.getRestFulFactory();
        TaskInstanceVariablesResource resource = restFulFactory.taskInstanceVariablesResource(this.taskInstance.getId());

        try
        {
            return new RestCallTemplate<TaskInstanceVariablesResource, OwPage<Variable>>() {

                @Override
                protected OwPage<Variable> execWith(TaskInstanceVariablesResource resource) throws OwException
                {
                    Variables result = resource.list();

                    List<Variable> taskVariables = new ArrayList<Variable>();
                    for (Entry<Variable> element : result.list.getEntries())
                    {
                        taskVariables.add(element.getEntry());
                    }

                    Pagination pagination = result.list.getPagination();
                    return new OwPage<Variable>(taskVariables, pagination.getTotalItems(), pagination.isHasMoreItems());
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
    public OwAbstractPageFetcher<Variable> newCopy(int maxNumItems)
    {
        return new VariablesPageFetcher(repository, this.taskInstance, maxNumItems);
    }
}