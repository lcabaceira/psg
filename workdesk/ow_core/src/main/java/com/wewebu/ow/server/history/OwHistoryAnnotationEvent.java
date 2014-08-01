package com.wewebu.ow.server.history;

import com.wewebu.ow.server.event.OwEvent;

/**
 *<p>
 * Interface for history events used with the history manager to add new events to the database.<br/>
 * Created for annotation change events.<br/><br/>
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
public interface OwHistoryAnnotationEvent extends OwEvent
{
    /** change type see getChangeType() */
    public static final int CHANGE_TYPE_UNKNOWN = 0;
    /** change type see getChangeType() */
    public static final int CHANGE_TYPE_ADD = 1;
    /** change type see getChangeType() */
    public static final int CHANGE_TYPE_DELETE = 2;
    /** change type see getChangeType() */
    public static final int CHANGE_TYPE_MODIFY = 3;

    /** return the affected OwObjectReference */
    public abstract com.wewebu.ow.server.ecm.OwObjectReference getAffectedObject() throws Exception;

    /** return the change type of the annotation as defined with CHANGE_TYPE_... */
    public abstract int getChangeType();

    /** get the text of the annotation, can be null */
    public abstract String getText();

    /** get the annotation type that was modified, can be null */
    public abstract String getAnnotationType();

    /** get a identifying ID of the annotation in question, can be null if not defined */
    public abstract String getAnnotationID();
}
