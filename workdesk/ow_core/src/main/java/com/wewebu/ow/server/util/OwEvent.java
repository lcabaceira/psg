package com.wewebu.ow.server.util;

import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 *<p>
 * Utility class to manage a collection of listeners through one single instance.
 * The instance will automatically dispatch event to the subscribed listeners.<br/><br/>
 * <ul>
 *  <li>Derive a subclass which also implements the anticipated EventListner.</li>
 *  <li>Implement all EventListener Methods</li>
 *  <li>Get a iterator through getIterator, cast the EventListner interface and call the EventListner method.</li> 
 * </ul>
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
public abstract class OwEvent
{
    /**
     *<p>
     * Simple empty iterator.
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
    public static class EmptyIterator implements Iterator
    {
        public boolean hasNext()
        {
            return false;
        }

        public Object next()
        {
            throw new NoSuchElementException();
        }

        public void remove()
        {
        }
    }

    /** empty iterator */
    private static EmptyIterator m_emptyiterator = new EmptyIterator();

    /** collection of OwEventListener to be notified upon config changes */
    private Collection m_EventListeners;

    /** add a event listener to be notified
     * 
     * @param listener_p
     */
    public void addEventListener(EventListener listener_p)
    {
        if (m_EventListeners == null)
        {
            m_EventListeners = new Vector();
        }

        m_EventListeners.add(listener_p);
    }

    /** add a config change event listener to be notified about config changes
     * 
     * @param listener_p
     */
    public void removeEventListener(EventListener listener_p)
    {
        if (m_EventListeners != null)
        {
            m_EventListeners.remove(listener_p);
        }
    }

    /** get a iterator that iterates the subscribed listeners
     * 
     * @return Iterator
     */
    protected Iterator getIterator()
    {
        if (m_EventListeners != null)
        {
            return m_EventListeners.iterator();
        }
        else
        {
            return m_emptyiterator;
        }
    }
}
