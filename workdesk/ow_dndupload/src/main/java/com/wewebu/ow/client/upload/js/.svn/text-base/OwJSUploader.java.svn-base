package com.wewebu.ow.client.upload.js;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import netscape.javascript.JSObject;

import com.wewebu.ow.client.upload.OwMessages;
import com.wewebu.ow.client.upload.OwTransferFile;
import com.wewebu.ow.client.upload.OwUploadCfg;
import com.wewebu.ow.client.upload.OwUploadProcess;
import com.wewebu.ow.client.upload.OwUploader;

/**
 *<p>
 * JS based implementation.
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
public class OwJSUploader extends OwUploader
{
    private static final Logger LOGGER = Logger.getLogger(OwJSUploader.class.getName());

    /**
     * @param uploadCfg_p
     * @param messages_p
     */
    public OwJSUploader(OwUploadCfg uploadCfg_p, OwMessages messages_p)
    {
        super(uploadCfg_p, messages_p);
    }

    /**
     * Entry point for the upload functionality.
     * This method is called from JS cod. 
     * It is just an adaptation for the superclass {@link #uploadFiles(List, com.wewebu.ow.client.upload.OwProgressMonitor)}.
     * @param files_p the JavaScript array with the files to be uploaded.
     * 
     * @see OwJSDataTransfer
     * @see OwJSTransferFile
     * @see OwUploadProcess
     */
    public void uploadFiles(final OwJSTransferFile[] files_p, JSObject jsMonitorObject_p)
    {
        LOGGER.log(Level.FINER, "In applet.uploadFiles --");
        final List<OwTransferFile> filesList = new LinkedList<OwTransferFile>();
        filesList.addAll(Arrays.asList(files_p));

        OwJSprogressMonitor monitor = new OwJSprogressMonitor(jsMonitorObject_p);

        this.uploadFiles(filesList, monitor);
    }
}
