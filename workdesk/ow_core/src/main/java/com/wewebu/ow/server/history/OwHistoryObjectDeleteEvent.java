package com.wewebu.ow.server.history;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.event.OwEvent;

/**
 *<p>
 * Interface for history events used with the history manager to add 
 * new events to the database.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwHistoryObjectDeleteEvent extends OwEvent
{
    /** return the parent or root object, or null if no parent / root exists */
    public abstract OwObjectReference getParent() throws Exception;

    /** return a list of object names that where deleted */
    public abstract java.util.Collection getAffectedObjectNames() throws Exception;

}