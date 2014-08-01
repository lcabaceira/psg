package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice;

import java.util.concurrent.ConcurrentHashMap;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.field.OwEnumCollection;

/**
 *<p>
 * Simple implementation of the OwFNCM5EnumCacheHandler. 
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
public class OwFNCM5SimpleEnumCacheHandler implements OwFNCM5EnumCacheHandler
{
    private ConcurrentHashMap<String, OwEnumCollection> cache;

    public OwFNCM5SimpleEnumCacheHandler()
    {
        cache = new ConcurrentHashMap<String, OwEnumCollection>();
    }

    public synchronized OwEnumCollection getEnumCollection(ChoiceList lst)
    {
        OwEnumCollection col = getEnumCollection(lst.get_Id());
        if (col == null)
        {
            col = new OwFNCM5EnumCollection(lst);
            cache.put(lst.get_Id().toString(), col);
        }
        return col;
    }

    public void clearCache()
    {
        cache.clear();
    }

    public OwEnumCollection getEnumCollection(Id id)
    {
        return cache.get(id.toString());
    }
}
