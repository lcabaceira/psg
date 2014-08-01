package com.wewebu.ow.server.ecmimpl.owdummy;

import com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Workflow description implementation for the dummy BPM Repository.
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
 */
public class OwDummyWorkflowDescription implements OwWorkflowDescription
{
    private String m_name;

    /**
     * Constructor
     * @param name_p the name of the workflow (will be used as ID too)
     */
    public OwDummyWorkflowDescription(String name_p)
    {
        super();
        this.m_name = name_p;
    }

    public String getId()
    {
        return m_name;
    }

    public String getName()
    {
        return m_name;
    }

    public OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

}