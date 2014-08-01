package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientProxy;
import org.restlet.resource.ResourceException;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Template to use when calling rest resources.
 * It makes sure to read the whole response and release the response entity. 
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
 * @param <T> The type of the REST resource that is used with this template.
 * @param <R> The type of the result returned by a call to this template.
 * @since 4.2.0.0
 */
public abstract class RestCallTemplate<T extends ClientProxy, R>
{
    private static final Logger LOG = OwLog.getLogger(RestCallTemplate.class);

    protected abstract R execWith(T resource) throws OwException;

    /**
     * Execute the template on the given resource. The result of the call to the resource is not usable after this method has finished.
     * @param resource
     * @return A result of type R
     * @throws OwRestException
     */
    public R doCall(T resource) throws OwException
    {
        try
        {
            return execWith(resource);
        }
        catch (ResourceException resEx)
        {
            LOG.error(resource.getClientResource().getResponse().getEntityAsText());
            onResourceException(resEx, resource);
            return null;
        }
        finally
        {
            Representation responseEntity = resource.getClientResource().getResponseEntity();
            if (null != responseEntity)
            {
                try
                {
                    responseEntity.exhaust();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                responseEntity.release();
            }
        }
    }

    protected void onResourceException(ResourceException resEx, T resource) throws OwException
    {
        throw new OwRestException("Could not load workflow descriptions.", resEx);
    }
}
