package com.wewebu.ow.server.ecm.bpm;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Base interface for workflow descriptions.<br/><br/>
 * To be implemented with the specific BPM system.
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
public interface OwWorkflowDescription
{
    /** get a step processor info class for the work item
     * @return OwWorkitemProcessorInfo
     */
    public abstract OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException;

    /** get an ID for the workflow
     * 
     * @return String
     */
    public abstract String getId();

    /** get a display name for the workflow
     * 
     * @return String
     */
    public abstract String getName();
}