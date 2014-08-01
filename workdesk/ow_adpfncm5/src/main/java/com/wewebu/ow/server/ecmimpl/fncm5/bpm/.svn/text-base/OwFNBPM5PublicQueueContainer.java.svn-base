package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import com.wewebu.ow.server.ecm.OwObjectReference;

import filenet.vw.api.VWQueue;

/**
 *<p>
 * FileNet BPM Repository. OwObject implementation for public queues.<br/>
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
public class OwFNBPM5PublicQueueContainer extends OwFNBPM5QueueContainer
{
    public OwFNBPM5PublicQueueContainer(OwFNBPM5Repository repository_p, VWQueue queue_p) throws Exception
    {
        super(repository_p, queue_p);
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitemcontainer/public";
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_PUBLIC_QUEUE_FOLDER;
    }

    /** get a optional icon to be displayed with the container (gives more detail than the mimeicon)
     * @return String icon path relative to /<design dir>/, or null if no icon is defined
     */
    public String getIcon()
    {
        return "/micon/bpm/publicqueue.png";
    }
}