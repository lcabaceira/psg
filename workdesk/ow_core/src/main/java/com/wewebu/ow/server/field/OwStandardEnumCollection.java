package com.wewebu.ow.server.field;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *<p>
 * Standard implementation of OwEnumCollection of OwEnum elements.
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
public class OwStandardEnumCollection extends java.util.ArrayList implements OwEnumCollection
{
    private static final long serialVersionUID = 1L;

    /** create a backup map for the items */
    private Map m_backupMap = new HashMap();

    /** overridden from Collection to adjust the backup map
     */
    public boolean add(Object o_p)
    {
        m_backupMap.put(((OwEnum) o_p).getValue(), o_p);
        return super.add(o_p);
    }

    /** retrieve a display name for the given object
     */
    public String getDisplayName(java.util.Locale local_p, Object object_p)
    {
        OwEnum enu = (OwEnum) m_backupMap.get(object_p);
        try
        {
            return enu.getDisplayName(local_p);
        }
        catch (NullPointerException e)
        {
            return object_p.toString();
        }
    }

    /** remove an object from the collection
     */
    public boolean remove(Object o_p)
    {
        m_backupMap.remove(o_p);
        return super.remove(o_p);
    }

    /** clear the collection
     */
    public void clear()
    {
        m_backupMap.clear();
        super.clear();
    }

    /** remove objects from the collection
     */
    public boolean removeAll(Collection c_p)
    {
        Iterator it = c_p.iterator();
        while (it.hasNext())
        {
            Object obj = it.next();
            m_backupMap.remove(obj);
        }
        return super.removeAll(c_p);
    }
}