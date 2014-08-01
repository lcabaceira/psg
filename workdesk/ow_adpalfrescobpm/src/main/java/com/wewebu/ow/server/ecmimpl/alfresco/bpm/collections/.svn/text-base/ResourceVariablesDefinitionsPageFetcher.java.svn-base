package com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.ClientProxy;

import com.wewebu.ow.server.collections.impl.OwAbstractPageFetcher;
import com.wewebu.ow.server.collections.impl.OwPage;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMException;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.Listable;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.RestCallTemplate;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Entry;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.Pagination;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariableDefinition;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariablesDefinitions;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * General Variable Definition fetcher, which allows to process the VariableDefinitions 
 * from all {@link Listable} interfaces  
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
public class ResourceVariablesDefinitionsPageFetcher<T extends Listable<VariablesDefinitions> & ClientProxy> extends OwAbstractPageFetcher<VariableDefinition>
{
    private T resource;

    public ResourceVariablesDefinitionsPageFetcher(T resource)
    {
        this(100l, resource);
    }

    public ResourceVariablesDefinitionsPageFetcher(long maxNumItems, T resource)
    {
        super(maxNumItems);
        this.resource = resource;
    }

    @Override
    protected OwPage<VariableDefinition> fetchPage(long skipCount)
    {
        try
        {
            //TODO: implement skip count
            return new RestVariableDefinitionFetcher<T>().doCall(resource);
        }
        catch (OwException e)
        {
            throw new OwAlfrescoBPMException("Could not fetch page.", e);
        }
    }

    @Override
    public OwAbstractPageFetcher<VariableDefinition> newCopy(int maxNumItems)
    {
        return new ResourceVariablesDefinitionsPageFetcher<T>(maxNumItems, this.resource);
    }

    /**
     *<p>
     * RestVariableDefinitionFetcher.
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
    protected static class RestVariableDefinitionFetcher<T extends Listable<VariablesDefinitions> & ClientProxy> extends RestCallTemplate<T, OwPage<VariableDefinition>>
    {

        @Override
        protected OwPage<VariableDefinition> execWith(T resource) throws OwException
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
    }
}
