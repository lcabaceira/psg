package com.wewebu.ow.client.upload.swing;

import com.wewebu.ow.client.upload.OwMessages;
import com.wewebu.ow.client.upload.OwUploadCfg;
import com.wewebu.ow.client.upload.OwUploader;

/**
 *<p>
 * Swing based implementation of an uploader.
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
public class OwUploaderSwing extends OwUploader
{

    /**
     * @param uploadCfg_p
     * @param messages_p
     */
    public OwUploaderSwing(OwUploadCfg uploadCfg_p, OwMessages messages_p)
    {
        super(uploadCfg_p, messages_p);
    }
}
