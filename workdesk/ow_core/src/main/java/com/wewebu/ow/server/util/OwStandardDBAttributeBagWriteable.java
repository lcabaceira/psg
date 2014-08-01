package com.wewebu.ow.server.util;

import java.util.Collection;

/**
 *<p>
 * Implements a writable attribute bag for DBs.<br/>
 * Uses the following attribute scheme to access the database e.g.:<br/><br/>
 * | (Number) | UserName (String) | <b>BagName</b> (String) | <b>AttributeName</b> (String) | <b>AttributeValue</b> (String) | <br/>
 * | 1 | OW_SITE_USER | ViewerSize | X | 100  | <br/>
 * | 2 | OW_SITE_USER | ViewerSize | Y | 100 | <br/>
 * | 3 | UserName | SelectedClass | Konto | 1 | <br/>
 * | 4 | UserName | SelectedClass | Kredit | 1 | <br/>
 * | 5 | UserName | SelectedClass | Depot | 1 | <br/>
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
public class OwStandardDBAttributeBagWriteable implements OwAttributeBagWriteable
{
    private String m_sBagName;
    private OwStandardDBAttributeBagWriteableFactory m_factory;

    public OwStandardDBAttributeBagWriteable(OwStandardDBAttributeBagWriteableFactory factory_p, String sBagName_p)
    {
        m_sBagName = sBagName_p;
        m_factory = factory_p;
    }

    public void setAttribute(String strName_p, Object value_p) throws Exception
    {
        m_factory.setAttribute(strName_p, value_p, m_sBagName);
    }

    public void save() throws Exception
    {
        m_factory.save(m_sBagName);
    }

    public Object getAttribute(int iIndex_p) throws Exception
    {
        return m_factory.getAttribute(iIndex_p, m_sBagName);
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        return m_factory.getAttribute(strName_p, m_sBagName);
    }

    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        return m_factory.getSafeAttribute(strName_p, default_p, m_sBagName);
    }

    public boolean hasAttribute(String strName_p)
    {
        return m_factory.hasAttribute(strName_p, m_sBagName);
    }

    public int attributecount()
    {
        return m_factory.attributecount(m_sBagName);
    }

    public Collection getAttributeNames()
    {
        return m_factory.getAttributeNames(m_sBagName);
    }

    /** clears the contents of the attribute bag */
    public void clear() throws Exception
    {
        m_factory.clear(m_sBagName);
    }

    /** remove the given attribute 
     * 
     * @param strName_p String name of attribute
     */
    public void remove(String strName_p)
    {
        m_factory.remove(strName_p, m_sBagName);
    }

}