package com.wewebu.ow.csqlc.ast;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * External information template class used in transporting external search information along the 
 * search parse tree.
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
 *@since 3.1.0.0
 */
public class OwExternal<I>
{
    private I i;

    private List<String[]> externalObjectStores = null;

    public OwExternal()
    {
        this(null, null);
    }

    public OwExternal(I i_p)
    {
        this(i_p, null);
    }

    public OwExternal(OwExternal<?> external_p)
    {
        this(null, external_p);
    }

    public OwExternal(I i_p, OwExternal<?> external_p)
    {
        super();
        this.i = i_p;

        this.externalObjectStores = new LinkedList<String[]>();

        if (external_p != null && external_p.externalObjectStores != null)
        {
            this.externalObjectStores.addAll(external_p.externalObjectStores);
        }
    }

    public I getInternal()
    {
        return i;
    }

    public void setInternal(I i_p)
    {
        this.i = i_p;
    }

    public void addExternalObjectStore(String id_p, String name_p)
    {
        this.externalObjectStores.add(new String[] { id_p, name_p });
    }

    public List<String[]> getExternalObjectStores()
    {
        return this.externalObjectStores;
    }

    public boolean hasExternalObjectStores()
    {
        return !this.externalObjectStores.isEmpty();
    }
}
