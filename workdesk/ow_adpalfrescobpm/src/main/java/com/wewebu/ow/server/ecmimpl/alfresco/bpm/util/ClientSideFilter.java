package com.wewebu.ow.server.ecmimpl.alfresco.bpm.util;

import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Simple filter interface, based on OwObject. 
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
public interface ClientSideFilter
{
    boolean match(OwObject obj) throws Exception;
}
