package com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes;

import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.collections.OwCollectionIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMRepository;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMWorkItemObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.ResourceVariablesDefinitionsPageFetcher;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.TaskFormModelResource;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariableDefinition;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Extended OwAlfrescoBPMWorkItemObjectClass, which uses also the task-form-model service
 * to get additional information about contained Variable definitions. 
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
public class OwAlfrescoBPMWorkItemTaskFormObjectClass extends OwAlfrescoBPMWorkItemObjectClass
{
    private OwAlfrescoBPMRepository repo;
    private String taskId;

    public OwAlfrescoBPMWorkItemTaskFormObjectClass(OwObjectClass wrappedObjectClass, OwAlfrescoBPMRepository repo, String taskId)
    {
        super(wrappedObjectClass);
        this.repo = repo;
        this.taskId = taskId;
    }

    protected OwAlfrescoBPMWorkItemTaskFormObjectClass(OwObjectClass wrappedObjectClass)
    {
        super(wrappedObjectClass);
    }

    public OwPropertyClass getPropertyClass(String strClassName_p) throws Exception
    {
        try
        {
            return super.getPropertyClass(strClassName_p);
        }
        catch (OwObjectNotFoundException ex)
        {
            fetchTaskModelInfomration();
            return super.getPropertyClass(strClassName_p);
        }
    }

    @Override
    public Collection getPropertyClassNames() throws Exception
    {
        fetchTaskModelInfomration();
        return super.getPropertyClassNames();
    }

    protected void fetchTaskModelInfomration() throws OwException
    {
        TaskFormModelResource resource = repo.getRestFulFactory().taskFormModelResource(taskId);
        OwIterable<VariableDefinition> itDefs = new OwCollectionIterable<VariableDefinition>(new ResourceVariablesDefinitionsPageFetcher<TaskFormModelResource>(resource));
        processDefinitions(itDefs);
    }

    /**
     * Process variable definitions and create corresponding OwPropertyClass(es) 
     * @param definitions OwIterable of VariableDefinition
     */
    protected void processDefinitions(OwIterable<VariableDefinition> definitions)
    {
        if (definitions != null)
        {
            Iterator<VariableDefinition> it = definitions.iterator();
            while (it.hasNext())
            {
                VariableDefinition def = it.next();
                if ("cm:person".equals(def.getDataType()))
                {
                    int idx = def.getName().indexOf('_');
                    if (idx > 0)
                    {
                        addUserInfoVirtualPropertyClass(def.getName().replaceFirst("_", ":"));
                    }
                }
            }
        }
    }
}
