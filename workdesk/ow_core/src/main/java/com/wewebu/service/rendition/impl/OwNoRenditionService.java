package com.wewebu.service.rendition.impl;

import java.util.Collections;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.service.rendition.OwRenditionService;

/**
 *<p>
 * Service which will be returned in case no Rendition Service are available.
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
public class OwNoRenditionService extends OwAbstractRenditionService implements OwRenditionService
{
    public static final OwNoRenditionService INSTANCE = new OwNoRenditionService();

    @Override
    public List<String> getRenditionMimeType(OwObject obj, String type) throws OwException
    {
        return Collections.emptyList();
    }
}