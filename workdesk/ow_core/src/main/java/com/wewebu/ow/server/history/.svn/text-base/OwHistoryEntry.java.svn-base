package com.wewebu.ow.server.history;

import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Interface for history entries used with the history manager to display the history events.<br/><br/>
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
public interface OwHistoryEntry extends OwObject
{
    /** checks if the underlying event can be undone */
    public abstract boolean canUndo() throws Exception;

    /** rewinds the the underlying event to the state before it occurred */
    public abstract void undo() throws Exception;
}