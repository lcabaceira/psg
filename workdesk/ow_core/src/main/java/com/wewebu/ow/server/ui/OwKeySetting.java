package com.wewebu.ow.server.ui;

/**
 *<p>
 *Identifiable key-action settings information holder.
 *Can produce {@link OwKeyAction}s for given event URLs , form names and descriptions that 
 *can be registered within an {@link OwAppContext}.  
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
public interface OwKeySetting extends OwEventSetting
{
    OwKeyAction createAction(String strEventURL, String strFormName, String description);
}
