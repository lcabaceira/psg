package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;

/**
 *<p>
 * Resource accessor that allows the changing of the pointed resource. 
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
public class OwFNCM5MutableAccessor<R extends OwFNCM5Resource> implements OwFNCM5ResourceAccessor<R>
{

    private R resource;

    public OwFNCM5MutableAccessor()
    {
        this(null);
    }

    public OwFNCM5MutableAccessor(R resource)
    {
        super();
        this.resource = resource;
    }

    public void set(R resource_p)
    {
        this.resource = resource_p;
    }

    public R get()
    {
        return this.resource;
    }

}
