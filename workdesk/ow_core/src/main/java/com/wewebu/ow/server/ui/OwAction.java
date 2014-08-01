package com.wewebu.ow.server.ui;

/**
 *<p>
 *Describes a user interaction that can be registered within an {@link OwAppContext}.  
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
public interface OwAction
{
    void register(OwAppContext owAppContext);
}
