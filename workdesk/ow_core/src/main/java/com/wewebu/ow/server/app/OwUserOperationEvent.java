package com.wewebu.ow.server.app;

import java.util.Date;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * The user operation event class. 
 * At the moment only LOGIN and LOGOUT operations are defined. 
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
 * @since 3.1.0.3
 */
public class OwUserOperationEvent
{
    /**the Alfresco Workdesk application id*/
    public static String OWD_APPLICATION = "OWD";

    /** Operation Types */
    public enum OwUserOperationType
    {
        LOGIN, LOGOUT, START, STOP;

        private OwUserOperationEvent newEvent(OwBaseUserInfo userInfo_p, String eventSource_p)
        {
            OwUserOperationEvent event = new OwUserOperationEvent(userInfo_p, this, eventSource_p);
            return event;
        }

        public void fire(OwUserOperationDispatch executor_p, OwBaseUserInfo userInfo_p, String eventSource_p) throws OwException
        {
            executor_p.fire(newEvent(userInfo_p, eventSource_p));
        }

        public void fireAndForget(OwUserOperationDispatch executor_p, OwBaseUserInfo userInfo_p, String eventSource_p)
        {
            executor_p.fireAndForget(newEvent(userInfo_p, eventSource_p));
        }

    }

    /**user info*/
    private OwBaseUserInfo userInfo;
    /**creation date of the event*/
    private Date creationTime;
    /**operation type that cause this event*/
    private OwUserOperationType type;
    /** the source of event: Alfresco Workdesk or another application*/
    private String eventSource;

    /**
     * Constructor
     * @param userInfo_p - user info
     * @param type_p - operation type
     * @param eventSource_p - the event source (OWD or other application) - can be <code>null</code>
     */
    public OwUserOperationEvent(OwBaseUserInfo userInfo_p, OwUserOperationType type_p, String eventSource_p)
    {
        this.userInfo = userInfo_p;
        this.creationTime = new Date();
        this.type = type_p;
        this.eventSource = eventSource_p;
    }

    /**
     * Getter for {@link OwBaseUserInfo} object
     * @return the {@link OwBaseUserInfo} object
     */
    public OwBaseUserInfo getUserInfo()
    {
        return userInfo;
    }

    /**
     * Getter for creation time.
     * @return the {@link Date} of event creation.
     */
    public Date getCreationTime()
    {
        return creationTime;
    }

    /**
     * Getter for event type.
     * @return the event type.
     */
    public OwUserOperationType getType()
    {
        return type;
    }

    /**
     * Get the event source.
     * @return - the event application source (OWD or other) can be <code>null</code>
     */
    public String getEventSource()
    {
        return eventSource;
    }
}
