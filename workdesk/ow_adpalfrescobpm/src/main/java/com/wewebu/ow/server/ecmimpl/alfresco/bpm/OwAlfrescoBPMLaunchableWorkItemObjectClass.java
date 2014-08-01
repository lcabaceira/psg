package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.collections.OwCollectionIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.collections.VariablesDefinitionsPageFetcher;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariableDefinition;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Class to be used with {@link OwAlfrescoBPMLaunchableWorkItem}s only.
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
public class OwAlfrescoBPMLaunchableWorkItemObjectClass extends OwAlfrescoBPMWorkItemObjectClass
{

    /**
     * @param wrappedObjectClass
     */
    private OwAlfrescoBPMLaunchableWorkItemObjectClass(OwObjectClass wrappedObjectClass)
    {
        super(wrappedObjectClass);
    }

    @SuppressWarnings("rawtypes")
    public static OwAlfrescoBPMWorkItemObjectClass forWorkflowDescription(OwAlfrescoBPMLaunchableWorkitemContainer container, OwAlfrescoBPMWorkflowDescription workflowDescription) throws OwServerException
    {
        try
        {
            OwNetwork network = container.getNetwork();
            OwObjectClass nativeClass = network.getObjectClass("D:" + workflowDescription.getStartFormResourceKey(), null);

            final OwAlfrescoBPMWorkItemObjectClass objectClass = new OwAlfrescoBPMLaunchableWorkItemObjectClass(nativeClass);

            OwAlfrescoBPMRepository repository = container.getBpmRepository();
            OwIterable<VariableDefinition> varDefinitions = new OwCollectionIterable<VariableDefinition>(new VariablesDefinitionsPageFetcher(repository, workflowDescription));

            for (VariableDefinition variableDef : varDefinitions)
            {
                String name = variableDef.getName();
                /*
                 * + <aspect name="bpm:assignee">
                 * + <aspect name="bpm:assignees">
                 * + <aspect name="bpm:groupAssignee">
                 * + <aspect name="bpm:groupAssignees">
                 */
                if ("bpm_assignee".equals(name))
                {
                    objectClass.addBpmAssigneeVirtualProperty();
                    break;
                }

                if ("bpm_assignees".equals(name))
                {
                    objectClass.addBpmAssigneesVirtualProperty();
                    break;
                }

                if ("bpm_groupAssignee".equals(name))
                {
                    objectClass.addBpmGroupAssigneeVirtualProperty();
                    break;
                }

                if ("bpm_groupAssignees".equals(name))
                {
                    objectClass.addBpmGroupAssigneesVirtualProperty();
                    break;
                }
            }

            return objectClass;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not get class for task instance!", e);
        }
    }
}
