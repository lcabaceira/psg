package com.wewebu.ow.server.history;

import java.util.Iterator;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;

/**
 *<p>
 * Standard implementation of the OwHistoryPropertyChangeEvent interface.
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
public class OwStandardHistoryPropertyChangeEvent implements OwHistoryPropertyChangeEvent
{
    /** the OwObject that was modified */
    private OwObject m_object;

    /** OwPropertyCollection of the new properties */
    private OwPropertyCollection m_newProperties;

    /** OwPropertyCollection of the new properties */
    private OwPropertyCollection m_oldProperties;

    /** constructs a property change event class for historization
     * if object_p was already modified, use second constructor
     *
     * @param object_p the OwObject before it was modified
     * @param newProperties_p OwPropertyCollection of the new properties
     */
    public OwStandardHistoryPropertyChangeEvent(OwObject object_p, OwPropertyCollection newProperties_p)
    {
        m_object = object_p;
        m_newProperties = newProperties_p;
    }

    /** constructs a property change event class for historization
    *
    * @param object_p the OwObject that is modified
    * @param oldProperties_p OwPropertyCollection of the old properties
    * @param newProperties_p OwPropertyCollection of the new properties
    */
    public OwStandardHistoryPropertyChangeEvent(OwObject object_p, OwPropertyCollection oldProperties_p, OwPropertyCollection newProperties_p)
    {
        m_object = object_p;
        m_newProperties = newProperties_p;
        m_oldProperties = oldProperties_p;
    }

    /** return a the affected OwObjectReference, or null if no object was affected */
    public com.wewebu.ow.server.ecm.OwObjectReference getAffectedObject() throws Exception
    {
        return m_object;
    }

    /** return a list of the affected Previous properties (OwField), or null if no Properties where affected */
    public OwPropertyCollection getAffectedOldProperties() throws Exception
    {
        if (null == m_oldProperties)
        {
            return m_object.getProperties(m_newProperties.keySet());
        }
        else
        {
            return m_oldProperties;
        }
    }

    /** return a list of the affected New properties (OwField), or null if no Properties where affected */
    public OwPropertyCollection getAffectedNewProperties() throws Exception
    {
        return m_newProperties;
    }

    /** get a summary of the event for systems that do only want to write a single string to the history database */
    public String getSummary() throws Exception
    {
        StringBuffer summary = new StringBuffer();
        Iterator it = m_newProperties.values().iterator();

        while (it.hasNext())
        {
            OwProperty prop = (OwProperty) it.next();
            summary.append(prop.getPropertyClass().getClassName());

            if (it.hasNext())
            {
                summary.append(", ");
            }
        }

        return summary.toString();
    }

}