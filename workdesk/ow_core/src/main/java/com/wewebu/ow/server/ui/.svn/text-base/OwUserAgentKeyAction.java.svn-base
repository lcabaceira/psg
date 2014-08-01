package com.wewebu.ow.server.ui;

import javax.servlet.http.HttpServletRequest;

/**
 *<p>
 *HTTP user agent based key action.
 *Registers key events based on the information decoded by subclasses from 
 *the user agent string found in the a given {@link OwAppContext}.    
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
 *@since 4.1.1.0
 */
public abstract class OwUserAgentKeyAction implements OwKeyAction
{

    @Override
    public final void register(OwAppContext owAppContext)
    {
        HttpServletRequest request = owAppContext.getHttpRequest();
        String userAgentHeader = request.getHeader("user-agent");
        registerKeyEvent(userAgentHeader, owAppContext);
        //TODO: agent paresers
        //      http://user-agent-string.info/download/UASparser-for-JAVA
        //      http://uadetector.sourceforge.net/modules/uadetector-resources/index.html
    }

    protected abstract void registerKeyEvent(String userAgentHeader, OwAppContext owAppContext);

}
