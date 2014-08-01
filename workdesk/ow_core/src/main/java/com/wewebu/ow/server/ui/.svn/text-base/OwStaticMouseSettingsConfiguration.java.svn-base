package com.wewebu.ow.server.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.ui.OwOSFamilyMouseSetting.OwOSFamilyMouseActionCode;
import com.wewebu.ow.server.ui.ua.OwOSFamily;

/**
 *<p>
 *A hard-coded mouse settings configuration . 
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
public class OwStaticMouseSettingsConfiguration implements OwMouseSettingsConfiguration
{
    private static OwStaticMouseSettingsConfiguration instance = null;

    private Map<String, OwMouseSetting> mouseSettings = new HashMap<String, OwMouseSetting>();

    public synchronized static OwStaticMouseSettingsConfiguration getInstance()
    {
        if (instance == null)
        {
            instance = new OwStaticMouseSettingsConfiguration();
        }

        return instance;
    }

    private OwStaticMouseSettingsConfiguration()
    {
        {
            OwOSFamilyMouseActionCode osXCMDClick = new OwOSFamilyMouseActionCode(OwOSFamily.OS_X, OwAppContext.KEYBOARD_CTRLKEY_NONE, OwAppContext.KEYBOARD_CTRLKEY_META, OwMouseEventDescription.MOUSE_CLICK);
            OwOSFamilyMouseActionCode winCtrlClick = new OwOSFamilyMouseActionCode(OwOSFamily.WINDOWS, OwAppContext.KEYBOARD_CTRLKEY_NONE, OwAppContext.KEYBOARD_CTRLKEY_CTRL, OwMouseEventDescription.MOUSE_CLICK);
            OwOSFamilyMouseActionCode unknownCtrlClick = new OwOSFamilyMouseActionCode(OwOSFamily.UNKNOWN, OwAppContext.KEYBOARD_CTRLKEY_NONE, OwAppContext.KEYBOARD_CTRLKEY_CTRL, OwMouseEventDescription.MOUSE_CLICK);

            add(new OwOSFamilyMouseSetting(OwObjectListView.SELECT_DESELECT_NONCONSECUTIVE_OBJECTS_ACTION_ID, osXCMDClick, winCtrlClick, unknownCtrlClick));
        }

    }

    private void add(OwMouseSetting setting)
    {
        mouseSettings.put(setting.getId(), setting);
    }

    @Override
    public Set<String> getActionIds()
    {
        return mouseSettings.keySet();
    }

    @Override
    public OwMouseSetting getSetting(String actionId) throws OwConfigurationException
    {
        OwMouseSetting setting = mouseSettings.get(actionId);
        if (setting == null)
        {
            throw new OwConfigurationException("No mouse action setting " + actionId + " found.");
        }
        return setting;
    }

}
