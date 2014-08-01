package com.wewebu.ow.server.ui;

import java.util.Locale;

import com.wewebu.ow.server.ui.ua.OwOSFamily;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * A keyboard key description registry. 
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
public class OwKeyboardDescription
{

    private static OwKeyboardDescription instance = null;

    public synchronized static OwKeyboardDescription getInstance()
    {
        if (instance == null)
        {
            instance = new OwKeyboardDescription();
        }

        return instance;
    }

    private OwKeyboardDescription()
    {
        //void
    }

    public String getDescription(int keyCode, int ctrlCode, Locale locale, OwOSFamily family)
    {
        StringBuilder strDescription = new StringBuilder();

        if (ctrlCode != 0)
        {
            strDescription.append(getCtrlDescription(locale, ctrlCode));
            strDescription.append(" + ");
        }

        switch (keyCode)
        {
            case OwAppContext.KEYBOARD_KEY_INS:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_INS", "INS"));
                break;
            case OwAppContext.KEYBOARD_KEY_DEL:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_DEL", "DEL"));
                break;
            case OwAppContext.KEYBOARD_KEY_POS1:
            {
                if (OwOSFamily.OS_X == family)
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_FN_LEFT", "FN+LEFT"));
                }
                else
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_POS1", "POS1"));
                }
            }
                break;
            case OwAppContext.KEYBOARD_KEY_END:
            {
                if (OwOSFamily.OS_X == family)
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_FN_RIGHT", "FN+RIGHT"));
                }
                else
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_END", "END"));
                }
            }
                break;
            case OwAppContext.KEYBOARD_KEY_ESC:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_ESC", "ESC"));
                break;
            case OwAppContext.KEYBOARD_KEY_RETURN:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_RETURN", "RETURN"));
                break;
            case OwAppContext.KEYBOARD_KEY_UP:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_UP", "UP"));
                break;
            case OwAppContext.KEYBOARD_KEY_DN:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_DN", "DOWN"));
                break;
            case OwAppContext.KEYBOARD_KEY_LEFT:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_LEFT", "LEFT"));
                break;
            case OwAppContext.KEYBOARD_KEY_RIGHT:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_RIGHT", "RIGHT"));
                break;
            case OwAppContext.KEYBOARD_KEY_PAGE_UP:
            {
                if (OwOSFamily.OS_X == family)
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_FN_UP", "FN+UP"));
                }
                else
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_PAGE_UP", "PAGE-UP"));
                }
            }
                break;
            case OwAppContext.KEYBOARD_KEY_PAGE_DN:
            {
                if (OwOSFamily.OS_X == family)
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_FN_DN", "FN-DN"));
                }
                else
                {
                    strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_PAGE_DN", "PAGE-DOWN"));
                }
            }
                break;
            case OwAppContext.KEYBOARD_KEY_SPACE:
                strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_KEY_SPACE", "' '"));
                break;
            default:
                if ((keyCode >= OwAppContext.KEYBOARD_KEY_F1) && (keyCode <= OwAppContext.KEYBOARD_KEY_F12))
                {
                    strDescription.append("F");
                    strDescription.append((char) (keyCode - OwAppContext.KEYBOARD_KEY_F1 + '1'));
                }
                else
                {
                    if (keyCode >= 32 && keyCode != 127 && keyCode != 255)//don't use function ASCII values
                    {
                        strDescription.append((char) keyCode);
                    }
                }
                break;
        }

        return strDescription.toString();
    }

    /**
     * 
     * @param locale
     * @param iCtrlCode
     * @return localized string description for control keys
     */
    public String getCtrlDescription(Locale locale, int iCtrlCode)
    {
        StringBuilder strDescription = new StringBuilder();

        if ((iCtrlCode & OwAppContext.KEYBOARD_CTRLKEY_SHIFT) != 0)
        {
            strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_CTRLKEY_SHIFT", "SHIFT"));
        }
        if ((iCtrlCode & OwAppContext.KEYBOARD_CTRLKEY_CTRL) != 0)
        {
            strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_CTRLKEY_CTRL", "CTRL"));
        }
        if ((iCtrlCode & OwAppContext.KEYBOARD_CTRLKEY_ALT) != 0)
        {
            strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_CTRLKEY_ALT", "ALT"));
        }
        if ((iCtrlCode & OwAppContext.KEYBOARD_CTRLKEY_META) != 0)
        {
            strDescription.append(OwString.localize(locale, "app.OwAppContext.KEYBOARD_CTRLKEY_META", "CMD"));
        }

        return strDescription.toString();
    }
}
