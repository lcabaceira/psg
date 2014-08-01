package com.wewebu.ow.server.servlets;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ui.OwWebApplication;

/**
 *<p>
 * Logout listener that ends the session on time out.
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

public class OwLogoutSessionListener implements HttpSessionListener
{

    public void sessionCreated(HttpSessionEvent se_p)
    {
        // ignore
    }

    public void sessionDestroyed(HttpSessionEvent se_p)
    {
        HttpSession session = se_p.getSession();

        // logout upon timeout 
        OwMainAppContext context = (OwMainAppContext) session.getAttribute(OwWebApplication.CONTEXT_KEY);
        try
        {
            if (null != context)
            {
                context.onSessionTimeOut(se_p.getSession());
            }
        }
        finally
        {
            session.removeAttribute(OwWebApplication.CONTEXT_KEY);
        }
    }

}