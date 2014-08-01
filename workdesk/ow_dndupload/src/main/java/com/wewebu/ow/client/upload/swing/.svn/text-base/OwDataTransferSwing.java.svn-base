package com.wewebu.ow.client.upload.swing;

import java.awt.datatransfer.Transferable;
import java.io.File;

import com.wewebu.ow.client.upload.OwDNDException;
import com.wewebu.ow.client.upload.OwDataTransfer;
import com.wewebu.ow.client.upload.OwTransferFile;

/**
 *<p>
 * A data transfer object for a list of files.
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
public class OwDataTransferSwing extends OwDataTransfer<OwTransferFile>
{

    public OwDataTransferSwing(Transferable transferable_p) throws OwDNDException
    {
        super(transferable_p);
    }

    protected OwTransferFile fromFile(File file_p, File rootPath_p)
    {
        return new OwTransferFile(file_p, rootPath_p);
    }
}
