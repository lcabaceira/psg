package com.wewebu.ow.server.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwSimpleAttributeBagWriteable;

/**
 *<p>
 * Document Base Class. Documents can share several Views.
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
public class OwDocument extends OwEventTarget implements OwAttributeBagWriteable
{
    /** list of attached views if any  */
    protected ArrayList m_ViewList = new ArrayList();

    /** map for OwAttributeBagWriteable */
    private Map m_userattributes;

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
    }

    /** attach a view to the document, which will receive onUpdate events when OwDocument.Update is called
     * @param view_p OwView to receive onUpdate events
     */
    public void attachView(OwUpdateTarget view_p)
    {
        m_ViewList.add(view_p);
    }

    /** detach a view to the document, which will receive onUpdate events when OwDocument.Update is called
     * 
     * @param view_p OwView to detach
     */
    public void detachView(OwUpdateTarget view_p)
    {
        m_ViewList.remove(view_p);
    }

    /** causes all attached views to receive an onUpdate event
     *
     *  @param caller_p OwEventTarget target that called update
     *  @param iCode_p int optional reason code
     *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
     */
    public void update(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        for (int i = 0; i < m_ViewList.size(); i++)
        {
            ((OwUpdateTarget) m_ViewList.get(i)).onUpdate(caller_p, iCode_p, param_p);
        }
    }

    /** the plugin scalar settings specific for the current user */
    private OwAttributeBagWriteable m_persistentattributebagwritable;

    /** (overridable) get a persistent OwAttributeBagWriteable (application scope)
     *  NOTE: use the OwAttributeBagWriteable implementation of this class to access non persistent (session scope) scalars
     *  
     * @return OwAttributeBagWriteable to read and write persistent scalars
     * 
     * @throws Exception 
     */
    public OwAttributeBagWriteable getPersistentAttributeBagWriteable() throws Exception
    {
        if (null == m_persistentattributebagwritable)
        {
            m_persistentattributebagwritable = new OwSimpleAttributeBagWriteable();
        }

        return m_persistentattributebagwritable;
    }

    /** (implements OwAttributeBagWriteable) get the attributes for the non persistent OwAttributeBagWriteable
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     * 
     * @return Map
     */
    private Map getUserAttributes()
    {
        if (null == m_userattributes)
        {
            m_userattributes = new HashMap();
        }

        return m_userattributes;
    }

    /** (implements OwAttributeBagWriteable) clear non persistent attributes 
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public void clear() throws Exception
    {
        getUserAttributes().clear();
    }

    /** (implements OwAttributeBagWriteable) remove non persistent attribute 
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public void remove(String strName_p)
    {
        getUserAttributes().remove(strName_p);
    }

    /** (implements OwAttributeBagWriteable) save non persistent attributes, does nothing
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public void save() throws Exception
    {
        // ignore
    }

    /** (implements OwAttributeBagWriteable) set non persistent attributes 
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public void setAttribute(String strName_p, Object value_p) throws Exception
    {
        getUserAttributes().put(strName_p, value_p);
    }

    /** (implements OwAttributeBagWriteable) get non persistent attributes count 
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public int attributecount()
    {
        return getUserAttributes().size();
    }

    /** (implements OwAttributeBagWriteable) get non persistent attribute 
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public Object getAttribute(int iIndex_p) throws Exception
    {
        return getUserAttributes().values().toArray()[iIndex_p];
    }

    /** (implements OwAttributeBagWriteable) get non persistent attribute 
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public Object getAttribute(String strName_p) throws Exception
    {
        return getUserAttributes().get(strName_p);
    }

    /** (implements OwAttributeBagWriteable) get non persistent attribute names
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public Collection getAttributeNames()
    {
        return getUserAttributes().keySet();
    }

    /** (implements OwAttributeBagWriteable) get non persistent attribute 
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        Object ret = getUserAttributes().get(strName_p);

        if (null == ret)
        {
            return default_p;
        }
        else
        {
            return ret;
        }
    }

    /** (implements OwAttributeBagWriteable) check for non persistent attribute
     * NOTE: use getPersistentAttributeBagWriteable() to obtain a persistent OwAttributeBagWriteable
     */
    public boolean hasAttribute(String strName_p)
    {
        return getUserAttributes().containsKey(strName_p);
    }

}