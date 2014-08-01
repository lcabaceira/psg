package com.wewebu.ow.server.plug.owsettings;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;

/**
 *<p>
 * Settings Document.
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
public class OwSettingsDocument extends OwMasterDocument
{
    /**
     * The configuration element for dynamic split flag
     * @since 3.1.0.0
     */
    public static final String CONFIG_NODE_USE_DYNAMIC_SPLIT = "UseDynamicSplit";

    /** safe the user settings to a persistent store
     *
     */
    public void safeUserSettings() throws Exception
    {
        ((OwMainAppContext) getContext()).getSettings().saveUserPrefs();
    }

    /** safe the user settings to a persistent store
     *
     */
    public void safeSiteSettings() throws Exception
    {
        ((OwMainAppContext) getContext()).getSettings().saveSitePrefs();
    }
}