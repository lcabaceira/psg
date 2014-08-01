package com.wewebu.ow.clientservices.utils;

/**
 *<p>
 * Utility class for the Workdesk client services applet.
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
public interface OwProgressMonitorListener
{
    void progressChanged(int current_p);

    void targetChanged(int newTarget_p);
}