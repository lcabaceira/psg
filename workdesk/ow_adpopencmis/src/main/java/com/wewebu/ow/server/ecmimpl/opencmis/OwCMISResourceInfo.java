package com.wewebu.ow.server.ecmimpl.opencmis;

/**
 *<p>
 * OwCMISResourceInfo Interface.
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
public interface OwCMISResourceInfo
{
    OwCMISCapabilities getCapabilities();

    String getId();

    String getDisplayName();
}
