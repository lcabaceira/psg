package com.wewebu.service.rendition;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * 
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
public interface OwRenditionServiceProvider
{
    /**
     * Get a rendition service for provided object.
     * @param obj OwObject to get rendition service for.
     * @return OwRenditionService
     * @throws OwException
     */
    OwRenditionService getRendtionService(OwObject obj) throws OwException;

    /**
     * @return the fall back rendition service.
     */
    OwRenditionService getFallbackRendtionService();
}
