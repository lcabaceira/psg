package com.wewebu.ow.client.upload.js;

import java.awt.datatransfer.Transferable;
import java.io.File;

import com.wewebu.ow.client.upload.OwDNDException;
import com.wewebu.ow.client.upload.OwDataTransfer;

/**
 *<p>
 * Java implementation of the DataTransfer JavaScript object.
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
public class OwJSDataTransfer extends OwDataTransfer<OwJSTransferFile> implements OwJSObject
{
    public OwJSDataTransfer(Transferable transferable_p) throws OwDNDException
    {
        super(transferable_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwJSObject#asJavaScript()
     */
    public String asJavaScript()
    {
        throw new RuntimeException("Do not call this method ...");
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDataTransfer#fromFile(java.io.File)
     */
    @Override
    protected OwJSTransferFile fromFile(File file_p, File rootPath_p)
    {
        return new OwJSTransferFile(file_p, rootPath_p);
    }
}
