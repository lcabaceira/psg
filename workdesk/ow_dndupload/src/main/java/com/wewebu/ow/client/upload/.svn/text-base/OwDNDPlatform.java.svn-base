package com.wewebu.ow.client.upload;

import com.wewebu.ow.client.upload.js.OwJSDNDPlatform;
import com.wewebu.ow.client.upload.swing.OwDNDPlatformSwing;

/**
 *<p>
 * This is an abstract factory for all platform dependent features.
 * We intend to support an ExtJs and a pure Swing implementations.
 * See #5393
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
public abstract class OwDNDPlatform
{

    /**
     * @param owDNDApplet_p 
     * @return a new instance bound to the given applet.
     */
    public static OwDNDPlatform newInstance(OwDNDApplet owDNDApplet_p)
    {
        OwUploadCfg uploadCfg = owDNDApplet_p.getUploadCfg();
        OwMessages messages = owDNDApplet_p.getMessages();

        if (uploadCfg.isUseExtJs())
        {
            return new OwJSDNDPlatform(owDNDApplet_p);
        }
        else
        {
            return new OwDNDPlatformSwing(uploadCfg, messages, owDNDApplet_p);
        }
    }

    public abstract OwDropTarget getDropTarget();

    public abstract OwUploader getUploader();

    public abstract void setup();

    public boolean isAcceptingUploads()
    {
        return !this.getUploader().isUploading() && this.getDropTarget().isEnabled();
    }
}
