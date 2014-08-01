package com.wewebu.ow.server.plug.owshortcut;

import java.io.Writer;

import com.wewebu.ow.server.ecm.OwRepository;
import com.wewebu.ow.server.ecm.OwStandardUnresolvedReference;

/**
 *<p>
 * Implementation of a shortcut item for object references that
 * can not be resolved anymore (e.g. deleted objects).
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
 *@since 3.0.0.0
 */
public class OwShortcutItemUnresolved extends OwShortCutItem
{
    private String m_name;

    private OwStandardUnresolvedReference m_unresolvedReference;

    private String m_id;

    /**
     * Constructor 
     * @param id_p shortcut ID 
     * @param name_p shortcut name
     * @param unresolvedReference_p the unresolved reference this link points to 
     */
    public OwShortcutItemUnresolved(String id_p, String name_p, OwStandardUnresolvedReference unresolvedReference_p)
    {
        super();
        this.m_name = name_p;
        this.m_unresolvedReference = unresolvedReference_p;
        this.m_id = id_p;
    }

    public String getId() throws Exception
    {
        return m_id;
    }

    public String getName()
    {
        return m_name;
    }

    protected String getPersistString() throws Exception
    {
        return null;
    }

    public int getType()
    {
        return m_unresolvedReference.getType();
    }

    public void insertIcon(OwShortCutItemContext context_p, Writer w_p) throws Exception
    {
        context_p.getMimeManager().insertIconLink(w_p, m_unresolvedReference);
    }

    public void insertLabel(OwShortCutItemContext context_p, Writer w_p) throws Exception
    {
        w_p.write(m_name);
    }

    @Override
    public void refresh(OwRepository repository_p)
    {
        //nothing to do here
    }

}