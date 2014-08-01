package com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwPage;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMRepository;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkflowDescription;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.OperationContext;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.ProcessDefinitionStartFormModelResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Pagination;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariableDefinition;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariablesDefinitions;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Fetches one page worth of {@link VariableDefinition}s.
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
public class VariablesDefinitionsPageFetcher extends OwAbstractPageFetcher<VariableDefinition>
{
    private static final Logger LOG = OwLog.getLogger(VariablesDefinitionsPageFetcher.class);
    private OwAlfrescoBPMRepository repository;
    private OwAlfrescoBPMWorkflowDescription workflowDescription;

    public VariablesDefinitionsPageFetcher(OwAlfrescoBPMRepository repository, OwAlfrescoBPMWorkflowDescription workflowDescription)
    {
        this(repository, workflowDescription, OperationContext.DEF_MAX_ITEMS);
    }

    private VariablesDefinitionsPageFetcher(OwAlfrescoBPMRepository repository, OwAlfrescoBPMWorkflowDescription workflowDescription, long maxNumItems)
    {
        super(maxNumItems);
        this.repository = repository;
        this.workflowDescription = workflowDescription;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher#fetchPage(long)
     */
    @Override
    protected OwPage<VariableDefinition> fetchPage(long skipCount)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Fetching a new page of variables definitions for process  %s, from index %d to %d.", this.workflowDescription.getId(), skipCount, skipCount + this.maxNumItems));
        }

        AlfrescoRESTFulFactory restFulFactory = this.repository.getRestFulFactory();
        ProcessDefinitionStartFormModelResource resource = restFulFactory.processDefinitionStartFormModelResource(workflowDescription.getId());

        try
        {
            return new RestCallTemplate<ProcessDefinitionStartFormModelResource, OwPage<VariableDefinition>>() {

                @Override
                protected OwPage<VariableDefinition> execWith(ProcessDefinitionStartFormModelResource resource) throws OwException
                {
                    VariablesDefinitions result = resource.list();
                    Entry<VariableDefinition>[] entries = result.list.getEntries();
                    List<VariableDefinition> list = new ArrayList<VariableDefinition>();
                    for (Entry<VariableDefinition> entry : entries)
                    {
                        VariableDefinition variableDef = entry.getEntry();
                        list.add(variableDef);
                    }

                    Pagination pagination = result.list.getPagination();
                    return new OwPage<VariableDefinition>(list, pagination.getTotalItems(), pagination.isHasMoreItems());
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
    public OwAbstractPageFetcher<VariableDefinition> newCopy(int maxNumItems)
    {
        return new VariablesDefinitionsPageFetcher(repository, this.workflowDescription, maxNumItems);
    }
}