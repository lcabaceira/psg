package com.wewebu.ow.server.history;

import com.wewebu.ow.server.event.OwEvent;

/**
 *<p>
 * Special interface for session history events.<br/>
 * The event holds the DMSID of the deleted object. <br/>
 * <b>Important:</b> the contained DMSID belongs to a deleted object, do not try to obtain the object from the DMSID provided by this event.<br/><br/>
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
 *@since 3.1.0.0
 */
public interface OwSessionHistoryDeleteEvent extends OwEvent
{
    /**
     * Return the DMSID of the deleted object.<br/>
     * <b>Important:</b> the contained DMSID belongs to a deleted object, do not try to obtain the object from the DMSID provided by this event.
     * @return -  the DMSID of the deleted object.
     */
    public String getAffectedDmsId();
}
