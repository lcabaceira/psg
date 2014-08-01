package com.wewebu.ow.server.historyimpl.dbhistory;

/**
 *<p>
 * Implementors of this interface are meant to filter the events 
 * which should be tracked by the history manager.
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
public interface OwEventFilter
{

    /**
     * filter the events which should be tracked by the history manager
     * @param eventType_p
     * @param strEventID_p
     * @param status_p
     * @return true if event should be filtered, i.e. is not getting tracked.
     *
     */
    boolean match(int eventType_p, String strEventID_p, int status_p);
}