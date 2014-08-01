package com.wewebu.ow.client.upload;

import java.awt.datatransfer.Transferable;

/**
 *<p>
 * This is the abstraction of the drop target. 
 * Listeners for drop events should be added to one of the concrete implementations of this interface.
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
public interface OwDropTarget
{
    public static final String EVT_TRANSFER = "transfer";

    /**
     * Notifies interested listeners of a drop event.
     * @param eventType_p
     * @param transferable_p
     * @throws OwDNDException
     */
    public void fire(String eventType_p, Transferable transferable_p) throws OwDNDException;

    /**
     * Do we accept more drop events?
     * @return true if we are enabled and can accept drop events.
     */
    public boolean isEnabled();
}
