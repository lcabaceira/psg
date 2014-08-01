package com.wewebu.ow.server.history;

import com.wewebu.ow.server.event.OwEvent;

/**
 *<p>
 * Interface for history events used with the history manager to add
 * new events to the database.
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
public interface OwHistoryPropertyChangeEvent extends OwEvent
{
    /** return a the affected OwObjectReference, or null if no object was affected */
    public abstract com.wewebu.ow.server.ecm.OwObjectReference getAffectedObject() throws Exception;

    /** return a list of the affected Previous properties (OwField), or null if no Properties where affected */
    public abstract com.wewebu.ow.server.ecm.OwPropertyCollection getAffectedOldProperties() throws Exception;

    /** return a list of the affected New properties (OwField), or null if no Properties where affected */
    public abstract com.wewebu.ow.server.ecm.OwPropertyCollection getAffectedNewProperties() throws Exception;
}