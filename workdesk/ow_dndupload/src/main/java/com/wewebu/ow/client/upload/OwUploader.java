package com.wewebu.ow.client.upload;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wewebu.ow.client.upload.js.OwJSUploader;
import com.wewebu.ow.client.upload.swing.OwUploaderSwing;

/**
 *<p>
 * Abstraction for the uploader.
 * See {@link OwJSUploader} and {@link OwUploaderSwing}.
 *</p>
 *<p>
 * When we switch to XmlHTTPRequest 2 we should replace 
 * this with a pure Ajax based implementation.
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
public abstract class OwUploader
{
    private static final Logger LOGGER = Logger.getLogger(OwUploader.class.getName());
    protected OwUploadProcess uploadProcess;
    protected OwMessages messages;
    protected OwUploadCfg uploadCfg;

    public OwUploader(OwUploadCfg uploadCfg_p, OwMessages messages_p)
    {
        this.uploadCfg = uploadCfg_p;
        this.messages = messages_p;
    }

    /**
     * Is there any active upload running?
     * @return true if an upload is running.
     */
    public synchronized boolean isUploading()
    {
        LOGGER.log(Level.FINE, ("uploader: " + this.uploadProcess));
        return (null != this.uploadProcess && this.uploadProcess.isUploading());
    }

    /**
     * Upload the files to the server.
     * @param filesList_p a list of files to be uploaded. All elements must be files and not folders.
     * @param monitor_p a monitor to report progress to.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void uploadFiles(final List<OwTransferFile> filesList_p, OwProgressMonitor monitor_p)
    {
        // Start uploading
        synchronized (this)
        {
            this.uploadProcess = new OwUploadProcess(filesList_p, this.uploadCfg, monitor_p, this.messages);
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run()
                {
                    OwUploader.this.uploadProcess.uploadFiles();
                    return null;
                }
            });
        }
    }

    /**
     * @return the uploadCfg
     */
    public OwUploadCfg getUploadCfg()
    {
        return uploadCfg;
    }

    /**
     * @return the messages
     */
    public OwMessages getMessages()
    {
        return messages;
    }
}
