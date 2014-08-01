package com.wewebu.ow.server.plug.owshortcut;

import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Interface for rendering ShortCutItems.
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
public interface OwShortCutItemContext
{
    /** get the application context */
    public abstract OwAppContext getContext();

    /** get a MIME manager */
    public abstract OwMimeManager getMimeManager();
}