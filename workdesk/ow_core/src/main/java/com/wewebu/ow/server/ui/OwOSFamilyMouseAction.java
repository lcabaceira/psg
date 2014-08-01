package com.wewebu.ow.server.ui;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.ui.ua.OwOSFamily;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Centralized key-interaction handling entity that can be 
 * registered as an {@link OwMouseEventDescription} within a given {@link OwAppContext}.   
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
public class OwOSFamilyMouseAction implements OwMouseAction
{
    private OwOSFamilyMouseSetting setting;
    private OwString description;

    public OwOSFamilyMouseAction(OwOSFamilyMouseSetting setting, OwString description)
    {
        super();
        this.setting = setting;
        this.description = description;
    }

    @Override
    public void register(OwAppContext owAppContext)
    {
        HttpServletRequest request = owAppContext.getHttpRequest();
        String userAgentHeader = request.getHeader("user-agent");

        OwOSFamily osFamily = OwOSFamily.from(userAgentHeader);

        OwMouseEventDescription mouseEvent = setting.createEvent(osFamily, description);

        if (mouseEvent != null)
        {
            owAppContext.registerMouseEventDescription(mouseEvent);
        }

    }

}
