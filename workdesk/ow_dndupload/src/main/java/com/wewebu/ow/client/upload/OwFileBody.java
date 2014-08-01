package com.wewebu.ow.client.upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.content.FileBody;

/**
 *<p>
 * Custom implementation of {@link FileBody} to be able to report the upload progress.
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
public class OwFileBody extends FileBody
{
    private static final int BUFFER_SIZE = 1024 * 4; // 4 KB bytes
    private static final int UPDATE_CHUNK_TRESHOLD = 1024 * 1024; // 1 MB

    private OwProgressMonitor progressMonitor;

    /**
     * Used only for reporting progress.
     */
    private String displayName;

    public OwFileBody(File file_p, String fileName_p, String displayName_p)
    {
        this(file_p, fileName_p, displayName_p, new NullProgressMonitor());
    }

    public OwFileBody(File file_p, String fileName_p, String displayName_p, OwProgressMonitor progressMonitor_p)
    {
        super(file_p, fileName_p, "application/octet-stream", null);
        this.progressMonitor = progressMonitor_p;
        this.displayName = displayName_p;
    }

    /* (non-Javadoc)
     * @see org.apache.http.entity.mime.content.FileBody#writeTo(java.io.OutputStream)
     */
    @Override
    public void writeTo(final OutputStream out_p) throws IOException
    {
        checkCanceled();

        if (out_p == null)
        {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        this.progressMonitor.startingNextFileUpload(getDisplayName());
        long size = getFile().length();
        int bytesWrote = 0;

        InputStream in = null;
        try
        {
            in = new BufferedInputStream(new FileInputStream(getFile()));
        }
        catch (FileNotFoundException fe_p)
        {
            throw new OwFileNotFoundException(getFile());
        }
        try
        {
            byte[] tmpBuffer = new byte[BUFFER_SIZE];

            int l;
            int chunckSent = 0;
            while ((l = in.read(tmpBuffer, 0, BUFFER_SIZE)) != -1)
            {
                out_p.write(tmpBuffer, 0, l);
                bytesWrote += l;

                chunckSent += l;
                if (chunckSent > UPDATE_CHUNK_TRESHOLD)
                {
                    chunckSent = 0;
                    this.progressMonitor.fileUploadedProgress((double) bytesWrote / size);
                    checkCanceled();
                }

            }
            out_p.flush();
            this.progressMonitor.fileUploadedProgress(1);
            this.progressMonitor.fileUploaded();

            checkCanceled();
        }
        finally
        {
            in.close();
        }
    }

    /**
     * 
     */
    private void checkCanceled()
    {
        if (this.progressMonitor.isCanceled())
        {
            throw new OwCanceledException("Canceled");
        }
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
