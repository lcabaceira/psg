/**
 * 
 */
package com.wewebu.ow.server.servlets;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.wewebu.ow.server.ui.OwUsageCounter;

/**
 *<p>
 * Usage listener that listens to session timeouts and updates the license counts.
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
public class OwUsageSessionListener implements HttpSessionListener
{

    public void sessionCreated(HttpSessionEvent arg0_p)
    {
        OwUsageCounter.onSessionCreated(arg0_p.getSession().getId());
    }

    public void sessionDestroyed(HttpSessionEvent arg0_p)
    {
        OwUsageCounter.onSessionDestroyed(arg0_p.getSession().getId());
    }

}