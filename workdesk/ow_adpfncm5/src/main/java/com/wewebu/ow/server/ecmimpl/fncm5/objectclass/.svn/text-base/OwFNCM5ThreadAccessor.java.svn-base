package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Special accessor for singleton class definition caching. 
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
public class OwFNCM5ThreadAccessor<R extends OwFNCM5Resource> implements OwFNCM5ResourceAccessor<R>
{
    private String resourceId;

    public R get() throws OwException
    {
        OwFNCM5Network localNetwork = OwFNCM5Network.localNetwork();
        return (R) localNetwork.getResource(getResourceId());
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

    public String getResourceId()
    {
        return this.resourceId;
    }
}
