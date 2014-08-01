package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import com.wewebu.ow.server.ecm.OwObjectReference;

import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueElement;

/**
 *<p>
 * FileNet BPM Plugin. OwObject implementation for queues.<br/>
 * A single workitem.
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
public class OwFNBPM5TrackerQueueContainer extends OwFNBPM5QueueContainer
{

    public OwFNBPM5TrackerQueueContainer(OwFNBPM5Repository repository_p, VWQueue queue_p) throws Exception
    {
        super(repository_p, queue_p);
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/tracker";
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_TRACKER_QUEUE_FOLDER;
    }

    /** overridable to create a new work item
     * @param workitem_p VWQueueElement
     * */
    protected OwFNBPM5WorkItem createWorkItem(VWQueueElement workitem_p) throws Exception
    {
        return new OwFNBPM5TrackerQueueWorkItem(this, workitem_p);
    }
}
