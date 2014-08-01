package com.wewebu.ow.client.upload.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wewebu.ow.client.upload.OwDNDException;
import com.wewebu.ow.client.upload.OwMessages;
import com.wewebu.ow.client.upload.OwTransferFile;

/**
 *<p>
 * Handles a Drop event. See {@link OwDataTransferSwing} and the DND.handleTransfer JS function.
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
public class OwDropTargetListenerImplSwing implements OwDropTargetListenerSwing
{
    private static final Logger LOGGER = Logger.getLogger(OwDropTargetListenerImplSwing.class.getName());
    private OwDNDPlatformSwing dndPlatform;

    /**
     * @param dndPlatform_p
     */
    public OwDropTargetListenerImplSwing(OwDNDPlatformSwing dndPlatform_p)
    {
        this.dndPlatform = dndPlatform_p;
    }

    public void dropEvent(OwDataTransferSwing transferSwing_p) throws OwDNDException
    {
        LOGGER.log(Level.INFO, "Processing a drop.");
        List<OwTransferFile> files = transferSwing_p.getFiles();

        if (null == files || files.isEmpty())
        {
            LOGGER.info("No files selected for upload.");
            return;
        }

        // Validation checks
        int nbFiles = files.size();
        if (!dndPlatform.getUploadCfg().isAcceptMultifile() && nbFiles > 1)
        {
            String errMessage = this.dndPlatform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_ERRMULTIFILENOTSUPPORTED, "Multifile not supported");
            OwErrorMessageBoxSwing.showError(errMessage, this.dndPlatform);
            return;
        }

        if (dndPlatform.getUploadCfg().isAcceptMultifile() && dndPlatform.getUploadCfg().getMaxUploadCount() > 0 && dndPlatform.getUploadCfg().getMaxUploadCount() < nbFiles)
        {
            String errMessage = this.dndPlatform.getMessages().localize(OwMessages.UPLOA_MESSAGES_ERRTOOMANYFILES, "Too many files.", "" + nbFiles, "" + dndPlatform.getUploadCfg().getMaxUploadCount());
            OwErrorMessageBoxSwing.showError(errMessage, this.dndPlatform);
            return;
        }

        List<File> invalidFilesSize = new ArrayList<File>();
        List<File> invalidFilesExtension = new ArrayList<File>();
        final List<OwTransferFile> filesList = new ArrayList<OwTransferFile>();

        long batchSize = 0;
        int i = 0;
        for (i = 0; i < nbFiles; i++)
        {
            boolean valid = true;
            OwTransferFile owTransferFile = files.get(i);
            File aFile = owTransferFile.getFile();
            batchSize += aFile.length();

            if (!dndPlatform.getUploadCfg().isSizeAccepted(aFile.length()))
            {
                invalidFilesSize.add(aFile);
                valid = false;
            }

            if (!dndPlatform.getUploadCfg().isExtensionAccepted(aFile.getName()))
            {
                invalidFilesExtension.add(aFile);
                valid = false;
            }

            if (valid)
            {
                filesList.add(owTransferFile);
            }
        }

        if (0 <= dndPlatform.getUploadCfg().getMaxBatchSize() && batchSize > dndPlatform.getUploadCfg().getMaxBatchSize())
        {
            float maxBatchSizeMB = Math.round(100f * dndPlatform.getUploadCfg().getMaxBatchSize() / (1024f * 1024f)) / 100f;
            String errMsg = this.dndPlatform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_ERRINVALIDBATCHSIZE, "Invalid batch size.", "" + maxBatchSizeMB);
            OwErrorMessageBoxSwing.showError(errMsg, this.dndPlatform);
            return;
        }

        String invalidFilesSizeStr = "";
        if (0 != invalidFilesSize.size())
        {
            // Keep 2 decimals
            float maxSizeMB = Math.round(100f * dndPlatform.getUploadCfg().getMaxUploadSize() / (1024f * 1024f)) / 100f;
            invalidFilesSizeStr = "\n" + this.dndPlatform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_ERRINVALIDFILESSIZE, "Invalid file size.", "" + maxSizeMB);

            for (i = 0; i < invalidFilesSize.size(); i++)
            {
                File anInvalidFile = invalidFilesSize.get(i);
                invalidFilesSizeStr += "\n- " + anInvalidFile.getName();
            }
        }

        String invalidFilesExtensiontStr = "";
        if (0 != invalidFilesExtension.size())
        {
            invalidFilesExtensiontStr = "\n===========================\n" + this.dndPlatform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_ERRINVALIDFILESEXTENSION, "Wrong extension");

            for (i = 0; i < invalidFilesExtension.size(); i++)
            {
                File anInvalidFile = invalidFilesExtension.get(i);
                invalidFilesExtensiontStr += "\n- " + anInvalidFile.getName();
            }
        }

        if (0 == filesList.size())
        {
            String errMessage = this.dndPlatform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_ERR_NO_FILES, "No files to upload.");
            errMessage += invalidFilesSizeStr + invalidFilesExtensiontStr;
            OwErrorMessageBoxSwing.showError(errMessage, this.dndPlatform);
            return;
        }

        if (0 != invalidFilesSizeStr.length() || 0 != invalidFilesExtensiontStr.length())
        {
            String errMessage = "\n" + this.dndPlatform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_ERRINVALIDFILES, "Invalid files.") + "\n"
                    + this.dndPlatform.getMessages().localize(OwMessages.UPLOAD_MESSAGES_QUESTIONSKIPCONTINUE, "Skip/Continue") + "\n" + invalidFilesSizeStr + invalidFilesExtensiontStr;
            OwSkipCancelMessageBoxSwing.show(errMessage, new Runnable() {

                public void run()
                {
                    OwDropTargetListenerImplSwing.this.dndPlatform.getUploader().uploadFiles(filesList, new OwProgressMonitorSwing(new Runnable() {

                        public void run()
                        {
                            OwDropTargetListenerImplSwing.this.dndPlatform.uploadCompleted();
                        }
                    }, OwDropTargetListenerImplSwing.this.dndPlatform));
                }
            }, this.dndPlatform);
            return;
        }

        this.dndPlatform.getUploader().uploadFiles(files, new OwProgressMonitorSwing(new Runnable() {

            public void run()
            {
                OwDropTargetListenerImplSwing.this.dndPlatform.uploadCompleted();
            }
        }, this.dndPlatform));
    }
}