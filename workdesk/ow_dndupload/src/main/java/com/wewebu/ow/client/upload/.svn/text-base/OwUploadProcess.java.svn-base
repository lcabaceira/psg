package com.wewebu.ow.client.upload;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 *<p>
 * Java based upload process implementation.
 *</p>
 *<p>
 * Knows how to transfer some files to the server.
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
public class OwUploadProcess implements Runnable
{

    private static final Logger LOGGER = Logger.getLogger(OwUploadProcess.class.getName());

    OwProgressMonitor monitor;
    private boolean uploading = false;
    private OwMessages messages;
    private List<OwTransferFile> filesList;

    private OwUploadCfg cfg;

    public OwUploadProcess(List<OwTransferFile> filesList_p, OwUploadCfg cfg_p, OwProgressMonitor monitor_p, OwMessages messages_p)
    {
        this.filesList = filesList_p;
        this.cfg = cfg_p;
        this.monitor = monitor_p;
        this.messages = messages_p;
    }

    /**
     * Does the actual job.
     */
    public void uploadFiles()
    {
        LOGGER.log(Level.INFO, "Uploading files from files [" + this.filesList.size() + "]");
        this.uploading = true;
        new Thread(this).start();
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        long fileSizes[] = new long[this.filesList.size()];
        int i = 0;
        for (OwTransferFile aFile : filesList)
        {
            fileSizes[i] = aFile.getFile().length();
            i++;
        }
        this.monitor.startingUpload(fileSizes);

        HttpPost post = new HttpPost(this.cfg.getUploadURL());

        DefaultHttpClient client = null;
        try
        {
            client = createHttpClient();
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for (OwTransferFile aFileTransfer : this.filesList)
            {
                String displayName = aFileTransfer.getFile().getName();
                String fileName = makeName(aFileTransfer);

                if (this.cfg.isEncodePath())
                {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                }

                LOGGER.log(Level.INFO, "File : " + aFileTransfer.getFile().getAbsolutePath());

                FileBody fileBody = new OwFileBody(aFileTransfer.getFile(), fileName, displayName, monitor);
                FormBodyPart formBodyPart = new FormBodyPart(fileName, fileBody);
                entity.addPart(formBodyPart);
            }

            post.setEntity(entity);

            if (this.cfg.getCookiesData() != null && 0 != this.cfg.getCookiesData().trim().length())
            {
                Header extraHeader = new BasicHeader("Cookie", this.cfg.getCookiesData());
                post.addHeader(extraHeader);
            }

            // See: http://tools.ietf.org/html/rfc2388
            // See: http://tools.ietf.org/html/rfc1867
            HttpResponse response = client.execute(post);

            HttpEntity respEntity = response.getEntity();
            StatusLine statusLine = response.getStatusLine();
            EntityUtils.consume(respEntity);
            int statusCode = statusLine.getStatusCode();
            if (statusCode < 200 || statusCode >= 300)
            {
                String statusMessage = statusLine.getStatusCode() + " " + statusLine.getReasonPhrase();
                monitor.error(this.messages.localize(OwMessages.UPLOAD_MESSAGES_ERRUPLOAD, "General upload error.") + " " + statusMessage);
                LOGGER.log(Level.INFO, "HTTP error status " + statusMessage);
                return;
            }
            else
            {
                if (HttpStatus.SC_OK != statusCode)
                {
                    LOGGER.log(Level.INFO, "HTTP success status " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                }
            }
        }
        catch (OwFileNotFoundException fe_p)
        {
            post.abort();
            LOGGER.log(Level.SEVERE, "File not found.", fe_p);
            monitor.error(this.messages.localize(OwMessages.UPLOAD_MESSAGES_ERRFILENOTFOUND, "File not found.", "\n" + fe_p.getFile().getAbsolutePath()));
        }
        catch (OwCanceledException canceledException_p)
        {
            post.abort();
            LOGGER.log(Level.SEVERE, "Canceleld.", canceledException_p);
        }
        catch (IOException e_p)
        {
            post.abort();
            LOGGER.log(Level.SEVERE, "General upload error.", e_p);
            monitor.error(this.messages.localize(OwMessages.UPLOAD_MESSAGES_ERRUPLOAD, "General upload error."));
        }
        catch (OwDNDException e_p)
        {
            post.abort();
            LOGGER.log(Level.SEVERE, "General upload error.", e_p);
            monitor.error(this.messages.localize(OwMessages.UPLOAD_MESSAGES_ERRUPLOAD, "General upload error."));
        }
        finally
        {
            if (null != client)
            {
                client.getConnectionManager().shutdown();
            }
            this.uploading = false;
            monitor.finish();
        }
    }

    /**
     * Create a HTTP client instance.
     * @return DefaultHttpClient
     * @throws OwDNDException 
     */
    private DefaultHttpClient createHttpClient() throws OwDNDException
    {
        LOGGER.log(Level.INFO, "Creating a new HTTP Client");
        DefaultHttpClient client = new DefaultHttpClient(this.cfg.getClientConnectionManager());
        client.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {

            public boolean retryRequest(IOException exception_p, int executionCount_p, HttpContext context_p)
            {
                // no retries, fail fast
                return false;
            }
        });

        return client;
    }

    /**
     * Prepares the names for the multipart sections of the POST.
     * @param aTransferFile_p The file for which to prepare a section name.
     * @return String
     */
    private String makeName(OwTransferFile aTransferFile_p)
    {
        String result = "";
        if (this.cfg.isSendFullPath())
        {
            result = aTransferFile_p.getFile().getAbsolutePath();
            if (this.cfg.isStripRootPath())
            {
                String rootPathStr = aTransferFile_p.getRootPath().getAbsolutePath();
                if (result.startsWith(rootPathStr))
                {
                    result = result.substring(rootPathStr.length());
                }
            }

            if (this.cfg.isTranslatePath())
            {
                result = result.replace('\\', '/');
            }
        }
        else
        {
            result = aTransferFile_p.getFile().getName();
        }
        return result;
    }

    /**
     * Is this uploader still busy?
     * @return true if an upload is running.
     */
    public boolean isUploading()
    {
        return uploading;
    }
}
