package com.wewebu.ow.server.role;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.util.OwAttributeBag;

/**
 *<p>
 * Since the role manager is only available after login and we need the option ID bag before, we use this delegate.
 * Maps option node names to IDs e.g.: "viewerservlet" - "viewerservlet.myid"
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
public class OwRoleOptionAttributeBag implements OwAttributeBag
{
    private Map m_optionIDMap = new HashMap();

    /** set the IDs after login */
    public void setIDs(Collection ids_p)
    {
        if (ids_p == null)
        {
            return;
        }
        Iterator it = ids_p.iterator();

        while (it.hasNext())
        {
            String id = (String) it.next();

            int i = id.lastIndexOf('.');
            String key = id.substring(0, i);

            m_optionIDMap.put(key, id);
        }
    }

    public int attributecount()
    {
        return m_optionIDMap.size();
    }

    public Object getAttribute(int index_p) throws Exception
    {
        throw new OwNotSupportedException("OwRoleOptionAttributeBag.getAttribute: Not implemented.");
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        return m_optionIDMap.get(strName_p);
    }

    public Collection getAttributeNames()
    {
        return m_optionIDMap.keySet();
    }

    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        Object ret = m_optionIDMap.get(strName_p);

        if (ret == null)
        {
            return default_p;
        }
        else
        {
            return ret;
        }
    }

    public boolean hasAttribute(String strName_p)
    {
        return m_optionIDMap.containsKey(strName_p);
    }

}