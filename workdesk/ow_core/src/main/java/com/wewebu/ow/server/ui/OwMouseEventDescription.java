package com.wewebu.ow.server.ui;

import java.util.Locale;

import com.wewebu.ow.server.ui.ua.OwOSFamily;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * A mouse event description : necessary key bindings and text description.
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
public class OwMouseEventDescription implements OwEventDescription
{
    public static final int MOUSE_NO_CLICK = 0;
    public static final int MOUSE_LEFT_CLICK = 1;
    public static final int MOUSE_RIGHT_CLICK = 2;
    public static final int MOUSE_CLICK = 3;

    private static final OwString[] CLICK_LABELS = new OwString[] { new OwString("OwMouseEventDescription.no.click", "NO CLICK"), new OwString("OwMouseEventDescription.left.click", "LEFT-CLICK"),
            new OwString("OwMouseEventDescription.right.click", "RIGHT-CLICK"), new OwString("OwMouseEventDescription.click", "CLICK") };

    private int modKeyCode;
    private int modCtrlKey;
    private int clickActionCode;

    private OwString description;

    public OwMouseEventDescription(int modKeyCode, int modCtrlKey, int clickActionCode, OwString description)
    {
        super();
        this.modKeyCode = modKeyCode;
        this.modCtrlKey = modCtrlKey;
        this.clickActionCode = (clickActionCode >= 0 && clickActionCode <= 3) ? clickActionCode : MOUSE_NO_CLICK;
        this.description = description;
    }

    @Override
    public String getEventString(Locale locale, OwOSFamily userAgentFamily)
    {
        OwKeyboardDescription keyboardDescription = OwKeyboardDescription.getInstance();
        return keyboardDescription.getDescription(modKeyCode, modCtrlKey, locale, userAgentFamily) + CLICK_LABELS[clickActionCode].getString(locale);
    }

    @Override
    public String getDescription(Locale locale, OwOSFamily userAgentFamily)
    {
        return description.getString(locale);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof OwMouseEventDescription)
        {
            OwMouseEventDescription descriptionObj = (OwMouseEventDescription) obj;

            return modCtrlKey == descriptionObj.modCtrlKey && modKeyCode == descriptionObj.modKeyCode;
        }
        return super.equals(obj);
    }

    public int hashCode()
    {
        return modCtrlKey;
    }
}
