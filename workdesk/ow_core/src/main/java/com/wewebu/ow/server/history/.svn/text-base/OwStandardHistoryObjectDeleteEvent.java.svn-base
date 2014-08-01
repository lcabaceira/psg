package com.wewebu.ow.server.history;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.ecm.OwObjectReference;

/**
 *<p>
 * OwStandardHistoryObjectDeleteEvent.
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
public class OwStandardHistoryObjectDeleteEvent implements OwHistoryObjectDeleteEvent
{
    /** single object */
    private String m_name;

    /** object collection */
    private java.util.Collection m_objectnames;

    /** the parent / root */
    private OwObjectReference m_parent;

    /** construct a history object delete event with one object and parent / root
    *
    * @param name_p String name of deleted object
    * @param parent_p parent or root object of the given object
    */
    public OwStandardHistoryObjectDeleteEvent(String name_p, OwObjectReference parent_p) throws Exception
    {
        if (name_p == null)
        {
            throw new NullPointerException("objectnames_p may not be null");
        }
        // just save the objects, construction of events must be fast
        m_name = name_p;
        m_parent = parent_p;
    }

    /** construct a history object change event with a list of objects and parent / root
     *
     * @param objectnames_p collection of object names of objects that where deleted. only {@link String} values are permitted.
     * @param parent_p parent or root object of the given object
     */
    public OwStandardHistoryObjectDeleteEvent(java.util.Collection objectnames_p, OwObjectReference parent_p) throws Exception
    {
        if (objectnames_p == null)
        {
            throw new NullPointerException("objectnames_p may not be null");
        }
        // just save the objects, construction of events must be fast, check if objectnames_p only contains String values
        m_objectnames = new ArrayList(objectnames_p.size());
        for (Iterator iterator = objectnames_p.iterator(); iterator.hasNext();)
        {
            //if element isn't type of string an class cast exception would be thrown
            String name = (String) iterator.next();
            m_objectnames.add(name);
        }
        m_parent = parent_p;
    }

    public Collection getAffectedObjectNames() throws Exception
    {
        if (null != m_objectnames)
        {
            return m_objectnames;
        }
        else if (m_name != null)
        {
            ArrayList ret = new ArrayList();

            ret.add(m_name);

            return ret;
        }

        return null;

    }

    public OwObjectReference getParent() throws Exception
    {
        return m_parent;
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

        if (null != m_name)
        {
            // === single object
            retString.append(m_name);
        }
        else if (null != m_objectnames)
        {
            // === object collection
            java.util.Iterator it = m_objectnames.iterator();
            while (it.hasNext())
            {
                String name = (String) it.next();
                retString.append(name);

                if (it.hasNext())
                {
                    retString.append(", ");
                }
            }
        }

        return retString.toString();
    }
}
