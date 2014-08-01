package com.wewebu.ow.server.ui;

import com.wewebu.ow.server.exceptions.OwConfigurationException;

/**
 *<p>
 *Centralized key settings configuration : a collection of key settings. 
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
public interface OwKeySettingsConfiguration extends OwEventSettingsConfiguration
{
    OwKeySetting getSetting(String actionId) throws OwConfigurationException;
}
