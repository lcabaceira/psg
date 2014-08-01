package com.wewebu.ow.client.upload.js;

import netscape.javascript.JSObject;

import com.wewebu.ow.client.upload.OwProgressMonitor;

/**
 *<p>
 * Java wrapper over the ProgressDialog JavaScript object.
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
public class OwJSprogressMonitor implements OwProgressMonitor
{
    private JSObject dialog;

    public OwJSprogressMonitor(JSObject dialog_p)
    {
        this.dialog = dialog_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwProgressMonitor#reset(int)
     */
    public void startingUpload(long fileSizes_p[])
    {
        String jsMethodName = "startingUpload";
        dialog.call(jsMethodName, new Object[] { fileSizes_p });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwProgressMonitor#startingNextUpload(java.lang.String)
     */
    public void startingNextFileUpload(String fileName_p)
    {
        String jsMethodName = "startingNextFileUpload";
        dialog.call(jsMethodName, new Object[] { fileName_p });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwProgressMonitor#fileUploaded()
     */
    public void fileUploaded()
    {
        String jsMethodName = "fileUploaded";
        dialog.call(jsMethodName, new Object[] {});
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwProgressMonitor#finish()
     */
    public void finish()
    {
        String jsMethodName = "finish";
        dialog.call(jsMethodName, new Object[] {});
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwProgressMonitor#fileUploadedProgress(double)
     */
    public void fileUploadedProgress(double progress_p)
    {
        String jsMethodName = "fileUploadedProgress";
        dialog.call(jsMethodName, new Object[] { progress_p });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwProgressMonitor#error()
     */
    public void error(String message_p)
    {
        String jsMethodName = "error";
        dialog.call(jsMethodName, new Object[] { message_p });
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.dnd.OwProgressMonitor#isCanceled()
     */
    public boolean isCanceled()
    {
        String jsMethodName = "isCanceled";
        return (Boolean) dialog.call(jsMethodName, new Object[] {});
    }
}
