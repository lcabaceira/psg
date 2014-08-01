package com.wewebu.ow.server.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Implements a simple writable attribute bag that writes to internal memory.
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
public class OwSimpleAttributeBagWriteable implements OwAttributeBagWriteable
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwSimpleAttributeBagWriteable.class);

    /** the internal map */
    protected Map m_attributes = new HashMap();

    public void setAttribute(String strName_p, Object value_p) throws Exception
    {
        m_attributes.put(strName_p, value_p);
    }

    /** get the internal map */
    public Map getAttributeMap()
    {
        return m_attributes;
    }

    /** may be overridden to persist state */
    public void save() throws Exception
    {
        // ignore
    }

    public Object getAttribute(int iIndex_p) throws Exception
    {
        throw new OwInvalidOperationException("OwSimpleAttributeBagWriteable.getAttribute(int iIndex_p): Not implemented.");
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        Object o = m_attributes.get(strName_p);

        if (null == o)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwSimpleAttributeBagWriteable.getAttribute(String strName_p): Object not found Exception, strName_p = " + strName_p);
            }
            throw new OwObjectNotFoundException("OwSimpleAttributeBagWriteable.getAttribute(String strName_p): strName_p = " + strName_p);
        }

        return o;
    }

    /** get the attribute with the given name, returns default if not found. */
    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        Object o = m_attributes.get(strName_p);
        if (o == null)
        {
            return default_p;
        }
        else
        {
            return o;
        }
    }

    public boolean hasAttribute(String strName_p)
    {
        return m_attributes.containsKey(strName_p);
    }

    public int attributecount()
    {
        return m_attributes.size();
    }

    public Collection getAttributeNames()
    {
        return m_attributes.keySet();
    }

    /** clears the contents of the attribute bag */
    public void clear() throws Exception
    {
        m_attributes.clear();
    }

    /** remove the given attribute 
     * 
     * @param strName_p String name of attribute
     */
    public void remove(String strName_p)
    {
        m_attributes.remove(strName_p);
    }
}