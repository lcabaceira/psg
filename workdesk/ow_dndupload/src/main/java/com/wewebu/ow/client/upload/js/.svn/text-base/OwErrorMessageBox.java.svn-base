package com.wewebu.ow.client.upload.js;

import netscape.javascript.JSObject;

/**
 *<p>
 * Wrapper for a ExtJS MessageDialog.
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
public class OwErrorMessageBox
{
    private JSObject window;
    private String messageg;

    public OwErrorMessageBox(JSObject window_p, String message_p)
    {
        this.window = window_p;
        this.messageg = message_p;
    }

    public void show()
    {
        String js = "upload.ErrMessageBox.showError('" + this.messageg + "');";
        window.eval(js);
    }
}
