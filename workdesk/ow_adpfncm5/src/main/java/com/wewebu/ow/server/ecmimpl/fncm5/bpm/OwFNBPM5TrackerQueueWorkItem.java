package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import filenet.vw.api.VWQueueElement;

/**
 *<p>
 * FileNet BPM Repository.<br/>
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
public class OwFNBPM5TrackerQueueWorkItem extends OwFNBPM5QueueWorkItem
{
    public OwFNBPM5TrackerQueueWorkItem(OwFNBPM5BaseContainer queue_p, VWQueueElement item_p) throws Exception
    {
        super(queue_p, item_p);

    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return OBJECT_TYPE_WORKITEM_TRACKER;
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitem/tracker";
    }
}
