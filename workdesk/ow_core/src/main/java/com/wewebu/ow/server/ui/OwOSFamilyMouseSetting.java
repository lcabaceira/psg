package com.wewebu.ow.server.ui;

import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.ui.OwOSFamilyKeyCodeSetting.OwOSFamilyKeyCode;
import com.wewebu.ow.server.ui.ua.OwOSFamily;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 *Operating system based mouse setting. 
 *Holds key and control-codes codes that define one action for 
 *various operating system families.    
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
public class OwOSFamilyMouseSetting implements OwMouseSetting
{

    public static class OwOSFamilyMouseActionCode
    {
        public final OwOSFamilyKeyCode keyCode;
        public final OwOSFamily osFamily;
        public final int clickActionCode;

        public OwOSFamilyMouseActionCode(OwOSFamily osFamily, int keyCode, int ctrlKey, int clickActionCode)
        {
            super();
            this.osFamily = osFamily;
            this.keyCode = new OwOSFamilyKeyCode(osFamily, keyCode, ctrlKey);
            this.clickActionCode = clickActionCode;
        }

    }

    private String id;
    private Map<OwOSFamily, OwOSFamilyMouseActionCode> osFamilyKeyCodes = new HashMap<OwOSFamily, OwOSFamilyMouseActionCode>();

    public OwOSFamilyMouseSetting(String id, OwOSFamilyMouseActionCode... mouseCodes)
    {
        this.id = id;

        for (int i = 0; i < mouseCodes.length; i++)
        {
            osFamilyKeyCodes.put(mouseCodes[i].osFamily, mouseCodes[i]);
        }
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public OwMouseAction createAction(OwString description)
    {
        return new OwOSFamilyMouseAction(this, description);
    }

    public OwMouseEventDescription createEvent(OwOSFamily osFamily, OwString description)
    {
        OwOSFamilyMouseActionCode mouseActionCode = osFamilyKeyCodes.get(osFamily);

        if (mouseActionCode == null)
        {
            mouseActionCode = osFamilyKeyCodes.get(OwOSFamily.UNKNOWN);
        }

        if (mouseActionCode != null)
        {
            return new OwMouseEventDescription(mouseActionCode.keyCode.iKeyCode, mouseActionCode.keyCode.iCtrlKey, mouseActionCode.clickActionCode, description);
        }
        else
        {
            return null;
        }

    }

}
