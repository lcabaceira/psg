package com.wewebu.ow.server.ui;

import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.ui.ua.OwOSFamily;

/**
 *<p>
 *Operating system based key setting. 
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
 *@since 4.1.1.0
 */
public class OwOSFamilyKeyCodeSetting extends OwAbstractKeySetting
{
    /**
     *<p>
     *  OwOSFamilyKeyCode. 
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
    public static class OwOSFamilyKeyCode
    {
        public final OwOSFamily osFamily;
        public final int iKeyCode;
        public final int iCtrlKey;

        public OwOSFamilyKeyCode(OwOSFamily osFamily, int iKeyCode, int iCtrlKey)
        {
            super();
            this.osFamily = osFamily;
            this.iKeyCode = iKeyCode;
            this.iCtrlKey = iCtrlKey;
        }

    }

    private Map<OwOSFamily, OwOSFamilyKeyCode> osFamilyKeyCodes = new HashMap<OwOSFamily, OwOSFamilyKeyCodeSetting.OwOSFamilyKeyCode>();

    public OwOSFamilyKeyCodeSetting(String id, OwOSFamilyKeyCode... keyCodes)
    {
        super(id);
        for (int i = 0; i < keyCodes.length; i++)
        {
            osFamilyKeyCodes.put(keyCodes[i].osFamily, keyCodes[i]);
        }
    }

    public OwKeyEvent createEvent(OwOSFamily osFamily, String strEventURL, String strFormName, String description)
    {
        OwOSFamilyKeyCode keyCode = osFamilyKeyCodes.get(osFamily);

        if (keyCode == null)
        {
            keyCode = osFamilyKeyCodes.get(OwOSFamily.UNKNOWN);
        }

        if (keyCode != null)
        {
            return new OwKeyEvent(keyCode.iKeyCode, keyCode.iCtrlKey, strEventURL, strFormName, description);
        }
        else
        {
            return null;
        }
    }

    @Override
    public OwOSFamilyKeyAction createAction(String strEventURL, String strFormName, String description)
    {
        return new OwOSFamilyKeyAction(this, strEventURL, strFormName, description);
    }

}
