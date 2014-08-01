package com.wewebu.ow.server.history;

import com.wewebu.ow.server.ecm.OwObjectReference;

/**
 *<p>
 * Standard implementation of the OwHistoryObjectChangeEvent interface.
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
public class OwStandardHistoryObjectChangeEvent implements OwHistoryObjectChangeEvent
{
    /** single object */
    private OwObjectReference m_object;
    /** object collection */
    private java.util.Collection m_objects;

    /** the parent / root */
    private OwObjectReference m_parent;

    /** construct a history object change event with one object and parent / root
     *
     * @param object_p object that was involved / changed in the event
     * @param parent_p parent or root object of the given object
     */
    public OwStandardHistoryObjectChangeEvent(OwObjectReference object_p, OwObjectReference parent_p)
    {
        // just save the objects, construction of events must be fast
        m_object = object_p;
        m_parent = parent_p;
    }

    /** construct a history object change event with a list of objects and parent / root
     *
     * @param objects_p collection of OwObjectReference objects that where involved / changed in the event
     * @param parent_p parent or root object of the given object
     */
    public OwStandardHistoryObjectChangeEvent(java.util.Collection objects_p, OwObjectReference parent_p)
    {
        // just save the objects, construction of events must be fast
        m_objects = objects_p;
        m_parent = parent_p;
    }

    /** get a summary of the event for systems that do only want to write a single string to the history database */
    public String getSummary() throws Exception
    {
        StringBuffer retString = new StringBuffer();
        if (null != m_parent)
        {
            retString.append(m_parent.getName());
            retString.append(": ");
        }

        if (null != m_object)
        {
            // === single object
            retString = retString.append(m_object.getName());
        }
        else if (null != m_objects)
        {
            // === object collection
            java.util.Iterator it = m_objects.iterator();
            while (it.hasNext())
            {
                OwObjectReference ref = (OwObjectReference) it.next();
                retString.append(ref.getName());

                if (it.hasNext())
                {
                    retString.append(", ");
                }
            }
        }

        return retString.toString();
    }

    /** return the parent or root object, or null if no parent / root exists */
    public OwObjectReference getParent() throws Exception
    {
        return m_parent;
    }

    /** return a list of the affected OwObjectReference, or null if no objects where affected */
    public java.util.Collection getAffectedObjects() throws Exception
    {
        if (m_object != null)
        {
            java.util.List retList = new java.util.ArrayList();
            retList.add(m_object);

            return retList;
        }
        else
        {
            return m_objects;
        }
    }
}