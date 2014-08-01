package com.wewebu.ow.server.event;

/**
 *<p>
 * Interface for history events used with the event manager to add new events to the database.<br/>
 * The implementation of the event manager can decide how detailed the information should be written to the database.<br/><br/>
 * Example:
 * <ul>
 * <li>Simple event Managers will only write the getSummary() String</li>
 * <li>More detailed event Managers will write the OwHistoryEventObjectDescription descriptions to database</li>
 * <li>For absolute detail cast the OwHistoryEvent to the appropriate subclasses and retrieve detail information</li>
 * </ul>
 * <br/><br/>
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
public interface OwEvent
{
    /** get a summary of the event for systems that do only want to write a single string to the history database */
    public abstract String getSummary() throws Exception;
}