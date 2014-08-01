package com.wewebu.ow.client.upload.js;

import netscape.javascript.JSObject;

import com.wewebu.ow.client.upload.OwDNDApplet;
import com.wewebu.ow.client.upload.OwDNDPlatform;
import com.wewebu.ow.client.upload.OwDropTarget;
import com.wewebu.ow.client.upload.OwUploader;

/**
 *<p>
 * Produces ExtJs based implementations.
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
public class OwJSDNDPlatform extends OwDNDPlatform
{

    private OwDNDApplet applet;
    private OwJSDropTarget dropTarget;
    private OwUploader uploader;

    /**
     * @param owDNDApplet_p
     */
    public OwJSDNDPlatform(OwDNDApplet owDNDApplet_p)
    {
        this.applet = owDNDApplet_p;
        this.dropTarget = new OwJSDropTarget(owDNDApplet_p);
        this.uploader = new OwJSUploader(this.applet.getUploadCfg(), this.applet.getMessages());
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDNDPlatform#getDropTarget()
     */
    @Override
    public OwDropTarget getDropTarget()
    {
        return this.dropTarget;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDNDPlatform#setup()
     */
    @Override
    public void setup()
    {
        JSObject jsWindow = JSObject.getWindow(this.applet);
        JSObject dnd = (JSObject) jsWindow.getMember("DND");
        dnd.call("setup", new Object[] { this.getDropTarget(), this.uploader });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDNDPlatform#getUploader()
     */
    @Override
    public OwUploader getUploader()
    {
        return this.uploader;
    }
}
