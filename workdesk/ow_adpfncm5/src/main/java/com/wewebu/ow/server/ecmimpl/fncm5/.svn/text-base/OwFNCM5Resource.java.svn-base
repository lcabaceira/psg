package com.wewebu.ow.server.ecmimpl.fncm5;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSIDProvider;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * A P8 5.0 engine resource implementation.
 * It defines an object model and keeps track of its source network.  
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
public abstract class OwFNCM5Resource implements OwResource, OwFNCM5DMSIDProvider
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5Resource.class);

    private OwFNCM5Network network;

    public OwFNCM5Resource(OwFNCM5Network network_p)
    {
        super();
        this.network = network_p;
    }

    public abstract OwFNCM5ContentObjectModel getObjectModel();

    public OwFNCM5Network getNetwork()
    {
        return this.network;
    }

    public abstract String getID() throws OwException;

    @Override
    public String toString()
    {
        try
        {
            return getID();
        }
        catch (OwException e)
        {
            LOG.error("Could not retrieve resource ID.", e);
            return "N/A";
        }
    }
}
