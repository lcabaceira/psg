package com.wewebu.ow.server.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.app.OwMainNavigationView;
import com.wewebu.ow.server.app.OwSubNavigationView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewEXTJSGrid;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.ui.OwOSFamilyKeyCodeSetting.OwOSFamilyKeyCode;
import com.wewebu.ow.server.ui.ua.OwOSFamily;

/**
 *<p>
 *A hard-coded key settings configuration . 
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
public class OwStaticKeySettingsConfiguration implements OwKeySettingsConfiguration
{
    private static OwStaticKeySettingsConfiguration instance = null;

    private Map<String, OwKeySetting> keySettings = new HashMap<String, OwKeySetting>();

    public synchronized static OwStaticKeySettingsConfiguration getInstance()
    {
        if (instance == null)
        {
            instance = new OwStaticKeySettingsConfiguration();
        }

        return instance;
    }

    private OwStaticKeySettingsConfiguration()
    {
        {
            OwOSFamilyKeyCode osXAltRight = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_RIGHT, OwAppContext.KEYBOARD_CTRLKEY_ALT);
            OwOSFamilyKeyCode winCtrlRight = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_RIGHT, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
            OwOSFamilyKeyCode unknownCtrlRight = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_RIGHT, OwAppContext.KEYBOARD_CTRLKEY_CTRL);

            add(new OwOSFamilyKeyCodeSetting(OwMainNavigationView.NEXT_PLUGIN_ACTION_ID, osXAltRight, winCtrlRight, unknownCtrlRight));
        }

        {
            OwOSFamilyKeyCode osXAltLeft = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_LEFT, OwAppContext.KEYBOARD_CTRLKEY_ALT);
            OwOSFamilyKeyCode winCtrlLeft = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_LEFT, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
            OwOSFamilyKeyCode unknownCtrlLeft = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_LEFT, OwAppContext.KEYBOARD_CTRLKEY_CTRL);

            add(new OwOSFamilyKeyCodeSetting(OwMainNavigationView.PREVIOUS_PLUGIN_ACTION_ID, osXAltLeft, winCtrlLeft, unknownCtrlLeft));
        }

        {
            OwOSFamilyKeyCode osXAltLeft = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_PAGE_UP, OwAppContext.KEYBOARD_CTRLKEY_ALT);
            OwOSFamilyKeyCode winCtrlLeft = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_PAGE_UP, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
            OwOSFamilyKeyCode unknownCtrlLeft = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_PAGE_UP, OwAppContext.KEYBOARD_CTRLKEY_CTRL);

            add(new OwOSFamilyKeyCodeSetting(OwObjectListViewEXTJSGrid.FIRST_PAGE_ACTION_ID, osXAltLeft, winCtrlLeft, unknownCtrlLeft));
        }

        {
            OwOSFamilyKeyCode osXAltLeft = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_PAGE_DN, OwAppContext.KEYBOARD_CTRLKEY_ALT);
            OwOSFamilyKeyCode winCtrlLeft = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_PAGE_DN, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
            OwOSFamilyKeyCode unknownCtrlLeft = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_PAGE_DN, OwAppContext.KEYBOARD_CTRLKEY_CTRL);

            add(new OwOSFamilyKeyCodeSetting(OwObjectListViewEXTJSGrid.LAST_PAGE_ACTION_ID, osXAltLeft, winCtrlLeft, unknownCtrlLeft));
        }

        {
            OwOSFamilyKeyCode osXAltDown = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_DN, OwAppContext.KEYBOARD_CTRLKEY_ALT);
            OwOSFamilyKeyCode winCtrlDown = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_DN, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
            OwOSFamilyKeyCode unknownCtrlDown = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_DN, OwAppContext.KEYBOARD_CTRLKEY_CTRL);

            add(new OwOSFamilyKeyCodeSetting(OwSubNavigationView.KEYBOARD_DOWN, osXAltDown, winCtrlDown, unknownCtrlDown));
        }

        {
            OwOSFamilyKeyCode osXAltUp = new OwOSFamilyKeyCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_KEY_UP, OwAppContext.KEYBOARD_CTRLKEY_ALT);
            OwOSFamilyKeyCode winCtrlUp = new OwOSFamilyKeyCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_KEY_UP, OwAppContext.KEYBOARD_CTRLKEY_CTRL);
            OwOSFamilyKeyCode unknownCtrlUp = new OwOSFamilyKeyCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_KEY_UP, OwAppContext.KEYBOARD_CTRLKEY_CTRL);

            add(new OwOSFamilyKeyCodeSetting(OwSubNavigationView.KEYBOARD_UP, osXAltUp, winCtrlUp, unknownCtrlUp));
        }

    }

    private void add(OwKeySetting setting)
    {
        keySettings.put(setting.getId(), setting);
    }

    @Override
    public Set<String> getActionIds()
    {
        return keySettings.keySet();
    }

    @Override
    public OwKeySetting getSetting(String actionId) throws OwConfigurationException
    {
        OwKeySetting setting = keySettings.get(actionId);
        if (setting == null)
        {
            throw new OwConfigurationException("No action key setting " + actionId + " found.");
        }
        return setting;
    }

}
