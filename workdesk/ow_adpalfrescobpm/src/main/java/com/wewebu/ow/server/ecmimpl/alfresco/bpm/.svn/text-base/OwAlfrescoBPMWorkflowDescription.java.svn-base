package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.ProcessDefinition;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Alfresco BPM based implementation of {@link OwWorkflowDescription}.
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
 *@since 4.0.0.0
 */
public class OwAlfrescoBPMWorkflowDescription implements OwWorkflowDescription
{
    private static final Logger LOG = OwLog.getLogger(OwAlfrescoBPMWorkflowDescription.class);

    private ProcessDefinition wfDefinition;
    private OwAlfrescoBPMRepository bpmRepository;

    public OwAlfrescoBPMWorkflowDescription(ProcessDefinition wfDefinition, OwAlfrescoBPMRepository bpmRepository)
    {
        this.wfDefinition = wfDefinition;
        this.bpmRepository = bpmRepository;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription#getStepProcessorInfo()
     */
    public OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription#getId()
     */
    public String getId()
    {
        return this.wfDefinition.getId();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription#getName()
     */
    public String getName()
    {
        return this.wfDefinition.getName();
    }

    public Integer getVersion()
    {
        return this.wfDefinition.getVersion();
    }

    /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString()
    {
        return this.wfDefinition.toString();
    }

    public String getStartFormResourceKey()
    {
        return this.wfDefinition.getStartFormResourceKey();
    }

    /**
     * A unique string that is shared by all versions of this Workflow Description (underlying process definition).
     * @return a String representing the key associated with this Workflow Description.
     */
    public String getKey()
    {
        return this.wfDefinition.getKey();
    }
}
