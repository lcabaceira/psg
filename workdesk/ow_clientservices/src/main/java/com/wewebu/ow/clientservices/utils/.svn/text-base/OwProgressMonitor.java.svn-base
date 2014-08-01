package com.wewebu.ow.clientservices.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * Utility class for the Workdesk client services applet.
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
public class OwProgressMonitor
{
    private int current = 0;
    private int target = 0;
    private List listeners = new LinkedList();

    public OwProgressMonitor()
    {
        current = 0;
    }

    public void addListener(OwProgressMonitorListener listener_p)
    {
        this.listeners.add(listener_p);
    }

    public boolean removeListener(OwProgressMonitorListener listener_p)
    {
        return this.listeners.remove(listener_p);
    }

    public int getTarget()
    {
        return target;
    }

    public int getCurrent()
    {
        return current;
    }

    public void setCurrent(int current_p)
    {
        if (current_p <= target)
        {
            this.current = current_p;
            Iterator iterator = listeners.iterator();
            while (iterator.hasNext())
            {
                OwProgressMonitorListener listener = (OwProgressMonitorListener) iterator.next();
                listener.progressChanged(current_p);
            }
        }
    }

    public void end()
    {
        this.setCurrent(getTarget());
    }

    public void start(int contentLength_p)
    {
        this.target = contentLength_p;
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext())
        {
            OwProgressMonitorListener listener = (OwProgressMonitorListener) iterator.next();
            listener.targetChanged(getTarget());
        }
        this.setCurrent(0);
    }

    public void increment(int i_p)
    {
        setCurrent(current + i_p);
    }
}