package com.wewebu.ow.server.ui;

import java.util.Set;

import com.wewebu.ow.server.exceptions.OwConfigurationException;

/**
 *<p>
 *Centralized configuration abstraction : a collection of settings. 
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
public interface OwEventSettingsConfiguration
{
    Set<String> getActionIds();

    OwEventSetting getSetting(String actionId) throws OwConfigurationException;
}
