package com.wewebu.ow.server.ecmimpl;

import com.wewebu.ow.server.ecm.OwLocation;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Simple implementation of the OwLocation interface.
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
 *@since 4.1.1.0
 */
public class OwSimpleLocation implements OwLocation
{
    private OwResource resource;
    private OwObject parent;

    public OwSimpleLocation()
    {
    }

    public OwSimpleLocation(OwResource res)
    {
        this();
        this.resource = res;
    }

    /**
     * Create a Location from parent, which will implicitly set
     * the resource from provided parent object.
     * @param parent
     * @throws OwException
     */
    public OwSimpleLocation(OwObject parent) throws OwException
    {
        this.parent = parent;
        try
        {
            this.resource = parent.getResource();
        }
        catch (Exception ex)
        {
            throw new OwServerException("Cannot retrieve resource from provided Parent", ex);
        }
    }

    @Override
    public OwResource getResource()
    {
        return resource;
    }

    public void setResource(OwResource res)
    {
        this.resource = res;
    }

    @Override
    public OwObject getParent()
    {
        return parent;
    }

    public void setParent(OwObject parent)
    {
        this.parent = parent;
    }

}
