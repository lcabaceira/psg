package com.wewebu.ow.client.upload;

/**
 *<p>
 * The java definition of the JavaScript <b>ProgressMonitor</b> interface 
 * (implemented in JavaScript by the ProgressDialog class).
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
public interface OwProgressMonitor
{
    /**
     * Call this before starting a new upload session.
     * @param fileSizes_p The relative sizes of files that will be uploaded. 
     * Make sure the order of elements in the array is the same as the order of files being uploaded.
     */
    void startingUpload(long fileSizes_p[]);

    /**
     * Call this when a new file is being uploaded.
     */
    void startingNextFileUpload(String fileName_p);

    /**
     * Call this when a file upload has been completed.
     */
    void fileUploaded();

    /**
     * Call this method when the upload has finished either with an error or with success.
     */
    void finish();

    /**
     * Call this to inform the progress monitor of the percent of the upload that was completed for the current file.
     * @param progress_p Upload progress between 0 and 1.
     */
    void fileUploadedProgress(double progress_p);

    /**
     * Call this to signal an upload error. The error messages will just be stacked.<br/> 
     * Clients still need to call {@link #finish()} to inform that the upload action was completed.
     */
    void error(String message_p);

    /**
     * Call this method to check if the user has requested cancellation of the upload process.
     * @return true if the user has canceled the upload.
     */
    boolean isCanceled();
}
