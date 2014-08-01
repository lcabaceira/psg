package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import org.restlet.resource.Get;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.BasicListResponse;

/**
 *<p>
 * A resource that supports the list() operation.
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
 * @param <L> The type of the list produce by the list() operation.
 * @since 4.2.0.0
 */
public interface Listable<L extends BasicListResponse<?>>
{
    @Get
    L list();
}
