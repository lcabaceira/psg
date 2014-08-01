package com.wewebu.service.rendition.impl;

import java.io.IOException;
import java.io.InputStream;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.service.rendition.OwRenditionService;

/**
 *<p>
 * Abstract implementation of OwRendtionService, with default implementations throwing exception.
 * Derived classes can override only required methods, and avoid the thrown exceptions.
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
 *@since 4.2.0.0
 */
public abstract class OwAbstractRenditionService implements OwRenditionService
{
    @Override
    public InputStream getRenditionStream(OwObject obj, String type) throws IOException, OwException
    {
        throw new OwInvalidOperationException("No Renditions available");
    }

    @Override
    public boolean hasRendition(OwObject obj, String type)
    {
        return false;
    }

    @Override
    public void createRendition(OwObject obj, String type) throws OwException
    {
        throw new OwInvalidOperationException("Renditions not available");
    }

    @Override
    public boolean canCreateRendition(OwObject obj, String type) throws OwException
    {
        return false;
    }

}
