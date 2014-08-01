package com.wewebu.ow.server.ui;

import java.util.Locale;

import com.wewebu.ow.server.ui.ua.OwOSFamily;

/**
 *<p>
 * OwKeyEvent.
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
public class OwKeyEvent implements OwEventDescription
{
    private int m_iKeyCode;
    private int m_iCtrlKey;
    String m_strEventURL;
    String m_strFormName;
    private String m_strDescription;

    /** create key event
     *
     * @param iKeyCode_p int code of the keyboard key as returned by the JavaScript method event.keyCode
     * @param iCtrlKey_p int any combination of the KEYBOARD_CTRLKEY_... definitions, can be 0
     * @param strEventURL_p String URL to be requested upon key press
     * @param strFormName_p String form name
     * @param description_p String description of the event
     *
     */
    public OwKeyEvent(int iKeyCode_p, int iCtrlKey_p, String strEventURL_p, String strFormName_p, String description_p)
    {
        m_iKeyCode = iKeyCode_p;
        m_iCtrlKey = iCtrlKey_p;
        m_strEventURL = strEventURL_p;
        m_strFormName = strFormName_p;
        m_strDescription = description_p;
    }

    public int getKeyCode()
    {
        return m_iKeyCode;
    }

    public int getCtrlKeyCode()
    {
        return m_iCtrlKey;
    }

    public String getEventURL()
    {
        return m_strEventURL;
    }

    public String getFormName()
    {
        return m_strFormName;
    }

    public String getDescription()
    {
        return m_strDescription;
    }

    /**
     * 
     * @param locale_p
     * @return a ctrl-key description based on the given locale and unknown user agent
     * @deprecated since 4.1.1.0 use {@link OwKeyboardDescription}
     */
    public static String getCtrlDescription(Locale locale_p, int iCtrlCode_p)
    {
        OwKeyboardDescription keyboardDescription = OwKeyboardDescription.getInstance();
        return keyboardDescription.getCtrlDescription(locale_p, iCtrlCode_p);
    }

    /**
     * 
     * @param locale
     * @param userAgentFamily
     * @return key event triggering key combination description based on the given locale and user agent OS family
     * @since 4.1.1.0
     */
    public String getKeyDescription(Locale locale, OwOSFamily userAgentFamily)
    {
        OwKeyboardDescription keyboardDescription = OwKeyboardDescription.getInstance();
        return keyboardDescription.getDescription(m_iKeyCode, m_iCtrlKey, locale, userAgentFamily);
    }

    @Override
    public String getEventString(Locale locale, OwOSFamily userAgentFamily)
    {
        return getKeyDescription(locale, userAgentFamily);
    }

    /**
     * 
     * @param locale_p
     * @return key description based on the given locale and unknown user agent
     * @deprecated since 4.1.1.0 use {@link #getKeyDescription(Locale, OwOSFamily)}
     */
    public String getKeyDescription(Locale locale_p)
    {
        return getKeyDescription(locale_p, OwOSFamily.UNKNOWN);
    }

    /** compute a code from keycode and CTRL code
     *
     * @return int
     */
    public int getMaskedCode()
    {
        return m_iKeyCode + m_iCtrlKey;
    }

    @Override
    public boolean equals(Object obj_p)
    {
        boolean result = false;
        if (obj_p != null && obj_p instanceof OwKeyEvent)
        {
            OwKeyEvent converted = (OwKeyEvent) obj_p;
            if (m_iKeyCode == converted.m_iKeyCode && m_iCtrlKey == converted.m_iCtrlKey)
            {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode()
    {
        return ("" + m_iKeyCode + "_" + m_iCtrlKey).hashCode();
    }

    @Override
    public String getDescription(Locale locale, OwOSFamily userAgentFamily)
    {
        return getDescription();
    }

}