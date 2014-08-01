package com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwPage;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMRepository;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkflowDescription;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessDefinitionsResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Pagination;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessDefinition;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessDefinitions;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Fetches one page worth of process definitions.
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
public class ProcessDefinitionsPageFetcher extends OwAbstractPageFetcher<OwAlfrescoBPMWorkflowDescription>
{
    private static final Logger LOG = OwLog.getLogger(ProcessDefinitionsPageFetcher.class);
    private OwAlfrescoBPMRepository repository;

    public ProcessDefinitionsPageFetcher(OwAlfrescoBPMRepository repository)
    {
        this(repository, OperationContext.DEF_MAX_ITEMS);
    }

    private ProcessDefinitionsPageFetcher(OwAlfrescoBPMRepository repository, long maxNumItems)
    {
        super(maxNumItems);
        this.repository = repository;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher#fetchPage(long)
     */
    @Override
    protected OwPage<OwAlfrescoBPMWorkflowDescription> fetchPage(long skipCount)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Fetching a new page for process definitions from %d to %d.", skipCount, skipCount + this.maxNumItems));
        }

        AlfrescoRESTFulFactory restFulFactory = this.repository.getRestFulFactory();
        ProcessDefinitionsResource res = restFulFactory.processDefinitionsResource(new OperationContext(skipCount, this.maxNumItems));

        try
        {
            return new ProcessDefinitionsRestCall(this.repository).doCall(res);
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
    public OwAbstractPageFetcher<OwAlfrescoBPMWorkflowDescription> newCopy(int maxNumItems)
    {
        return new ProcessDefinitionsPageFetcher(repository, maxNumItems);
    }

    /**
     *<p>
     * Fetcher for ProcessDefinition sand wrapping into OwAlfrescoBPMWorkflowDescription,
     * providing paging functionality.
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
    protected static class ProcessDefinitionsRestCall extends RestCallTemplate<ProcessDefinitionsResource, OwPage<OwAlfrescoBPMWorkflowDescription>>
    {
        private OwAlfrescoBPMRepository repository;

        public ProcessDefinitionsRestCall(OwAlfrescoBPMRepository repository)
        {
            this.repository = repository;
        }

        @Override
        protected OwPage<OwAlfrescoBPMWorkflowDescription> execWith(ProcessDefinitionsResource resource)
        {
            ProcessDefinitions processDefinitions = resource.list();
            LinkedList<OwAlfrescoBPMWorkflowDescription> items = new LinkedList<OwAlfrescoBPMWorkflowDescription>();

            for (Entry<ProcessDefinition> definition : processDefinitions.list.getEntries())
            {
                items.add(new OwAlfrescoBPMWorkflowDescription(definition.getEntry(), getRepository()));
            }

            Pagination pagination = processDefinitions.list.getPagination();
            return new OwPage<OwAlfrescoBPMWorkflowDescription>(items, pagination.getTotalItems(), pagination.isHasMoreItems());
        }

        public OwAlfrescoBPMRepository getRepository()
        {
            return repository;
        }
    }
}