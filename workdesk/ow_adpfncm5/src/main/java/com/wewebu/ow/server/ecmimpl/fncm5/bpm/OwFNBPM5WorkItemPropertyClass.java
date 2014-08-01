package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

/**
 *<p>
 * FileNet BPM Repository. Base interface for work item properties.
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
public interface OwFNBPM5WorkItemPropertyClass
{
    /** indicates if field can be filtered */
    public abstract boolean isFilterField();

    /** indicates weather the field is a pure DateField based in a queue definition = true,
     or a parameter which is only defined in the workflow object = false 
     */
    public abstract boolean isQueueDefinitionField();

}
