package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import com.wewebu.ow.server.ecm.OwObjectReference;

import filenet.vw.api.VWQueue;

/**
 *<p>
 * FileNet BPM Repository. OwObject implementation for user queues.<br/>
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
public class OwFNBPM5UserQueueContainer extends OwFNBPM5QueueContainer
{
    public OwFNBPM5UserQueueContainer(OwFNBPM5Repository repository_p, VWQueue queue_p) throws Exception
    {
        super(repository_p, queue_p);
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/user";
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER;
    }
}