package com.wewebu.ow.server.ecmimpl.fncm5.aspects;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.wewebu.ow.server.history.OwHistoryManager;

/**
 *<p>
 * History event trigger annotation.
 * Accesses to annotated methods are recorded through history-management events.
 * 
 * @see OwHistoryManager#addEvent(int, String, int)
 * @see OwHistoryManager#addEvent(int, String, com.wewebu.ow.server.event.OwEvent, int)
 * @see OwFNCM5HistoryManagement 
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
@Retention(RetentionPolicy.RUNTIME)
public @interface Historized
{
    int type();

    String id();
}
