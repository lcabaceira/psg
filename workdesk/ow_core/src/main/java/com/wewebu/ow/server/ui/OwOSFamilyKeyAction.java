package com.wewebu.ow.server.ui;

import com.wewebu.ow.server.ui.ua.OwOSFamily;

/**
 *<p>
 *Key action that can distinguish between user agent described operating system families.    
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
public class OwOSFamilyKeyAction extends OwUserAgentKeyAction
{
    private String strEventURL;
    private String description;
    private String strFormName;
    private OwOSFamilyKeyCodeSetting setting;

    public OwOSFamilyKeyAction(OwOSFamilyKeyCodeSetting setting, String strEventURL, String description)
    {
        this(setting, strEventURL, null, description);
    }

    public OwOSFamilyKeyAction(OwOSFamilyKeyCodeSetting setting, String strEventURL, String strFormName, String description)
    {
        super();
        this.setting = setting;
        this.strEventURL = strEventURL;
        this.strFormName = strFormName;
        this.description = description;
    }

    @Override
    protected void registerKeyEvent(String userAgentHeader, OwAppContext owAppContext)
    {

        OwOSFamily osFamily = OwOSFamily.from(userAgentHeader);

        OwKeyEvent keyEvent = setting.createEvent(osFamily, strEventURL, strFormName, description);

        if (keyEvent != null)
        {
            owAppContext.registerKeyEvent(keyEvent);
        }
    }

}
