package com.wewebu.ow.client.upload;

import java.applet.Applet;

/**
 *<p>
 * A list with all supported applet parameters.
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
 *@since 3.2.0.0
 */
public class OwAppletParams extends OwAbstractCfg
{
    public static final String PARAM_UPLOAD_URL = "upload_url";
    public static final String PARAM_COOKIES_DATA = "cookie_data";
    public static final String PARAM_MESSAGE = "message";
    public static final String PARAM_MULTIFILE = "multifile";

    public static final String PARAM_PROPS_FILE = "props_file";
    public static final String PROP_USE_EXTJS = "use_extjs";

    private Applet applet;

    public OwAppletParams(Applet applet_p)
    {
        this.applet = applet_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwAbstractCfg#getString(java.lang.String)
     */
    @Override
    public String getString(String paramName_p)
    {
        return this.applet.getParameter(paramName_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwAbstractCfg#has(java.lang.String)
     */
    @Override
    public boolean has(String paramName_p)
    {
        String value = this.applet.getParameter(paramName_p);
        return null != value && 0 != value.trim().length();
    }
}
