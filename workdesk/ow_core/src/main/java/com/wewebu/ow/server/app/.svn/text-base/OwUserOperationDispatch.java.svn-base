package com.wewebu.ow.server.app;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Operation executor utility.
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
 * @since 3.2.0.0
 */
public class OwUserOperationDispatch implements OwUserOperationExecutor
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwMainAppContext.class);

    private List<OwUserOperationListener> listeners = new LinkedList<OwUserOperationListener>();

    public synchronized void fireAndForget(OwUserOperationEvent event_p)
    {
        try
        {
            fire(event_p);
        }
        catch (OwException e)
        {
            LOG.error(e);
        }
    }

    public synchronized void fire(OwUserOperationEvent event_p) throws OwException
    {
        for (OwUserOperationListener listener : listeners)
        {
            listener.operationPerformed(event_p);
        }
    }

    public synchronized void addUserOperationListener(OwUserOperationListener listener_p)
    {
        if (listener_p != null)
        {
            this.listeners.add(listener_p);
        }
    }

    public synchronized void removeUserOperationListener(OwUserOperationListener listener_p)
    {
        if (listener_p != null)
        {
            this.listeners.remove(listener_p);
        }
    }

}
