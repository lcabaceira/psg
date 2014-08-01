package com.wewebu.ow.client.upload.swing;

import java.awt.datatransfer.Transferable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wewebu.ow.client.upload.OwDNDException;
import com.wewebu.ow.client.upload.OwDropTarget;

/**
 *<p>
 * Swing based drop target.
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
 *@since 3.2.0.0
 */
public class OwDropTargetSwing implements OwDropTarget
{
    private static final Logger LOGGER = Logger.getLogger(OwDropTargetSwing.class.getName());
    private Map<String, Set<OwDropTargetListenerSwing>> eventMap = new HashMap<String, Set<OwDropTargetListenerSwing>>();

    /**
     * This Swing implementation simply calls all the {@link OwDropTargetListenerSwing} registered for this type of event. 
     */
    public void fire(String eventType_p, Transferable transferable_p) throws OwDNDException
    {
        Set<OwDropTargetListenerSwing> eventList = this.eventMap.get(eventType_p);
        if (eventList != null)
        {
            for (OwDropTargetListenerSwing listener : eventList)
            {
                listener.dropEvent(new OwDataTransferSwing(transferable_p));
            }
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDropTarget#isEnabled()
     */
    public boolean isEnabled()
    {
        return true;
    }

    public void addEventListener(String eventType_p, OwDropTargetListenerSwing listener_p)
    {
        Set<OwDropTargetListenerSwing> eventList = this.eventMap.get(eventType_p);
        if (eventList == null)
        {
            eventList = new HashSet<OwDropTargetListenerSwing>();
            this.eventMap.put(eventType_p, eventList);
        }

        eventList.add(listener_p);
        LOGGER.log(Level.INFO, "Added listener : " + listener_p + " for event : " + eventType_p);
    }
}
