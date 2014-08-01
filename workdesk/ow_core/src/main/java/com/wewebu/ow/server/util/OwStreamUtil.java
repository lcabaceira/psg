package com.wewebu.ow.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.perf4j.LoggingStopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Utility class for Streams.
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
 */
public class OwStreamUtil
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStreamUtil.class);

    /** buffer for read and write */
    public static final int BUFFER_SIZE = 1024 * 64;

    /** min size if read is blocked */
    public static final int MIN_READ = (BUFFER_SIZE / 10);

    /** upload the given OutputStream to the given InputStream
     * The InputStream (in_p) and OutputStream (out_p) are closed 
     * @param in_p InputStream
     * @param out_p OutputStream
     * @param fClose_p Close the stream after finishing
     * @throws Exception 
     */
    public static void upload(InputStream in_p, OutputStream out_p, boolean fClose_p) throws Exception
    {
        LoggingStopWatch stopWatchUpload = new Log4JStopWatch("OwStreamUtil.upload");

        try
        {
            byte[] bBuffer = new byte[BUFFER_SIZE];

            int iRead = 0;
            do
            {
                // === compute the amount of bytes that should be requested by the next read operation
                // check availability without blocking other threads
                int iNextRead = in_p.available();

                // must not be more than buffer size
                if (iNextRead > BUFFER_SIZE)
                {
                    iNextRead = BUFFER_SIZE;
                }

                // if blocked, read at least some bytes
                if (iNextRead == 0)
                {
                    iNextRead = MIN_READ;
                }

                // === perform read
                iRead = in_p.read(bBuffer, 0, iNextRead);
                if (-1 != iRead)
                {
                    // write to output stream
                    out_p.write(bBuffer, 0, iRead);
                }
            } while (-1 != iRead);

            // flush output
            out_p.flush();
        }
        catch (Exception e)
        {
            String msg = "OwStreamUtil.upload: Error uploading the InputStream to the OutputStream.";
            LOG.error(msg, e);
            throw e;
        }
        finally
        {
            stopWatchUpload.stop();

            // === close streams
            if (fClose_p)
            {
                closeStreams(in_p, out_p);
            }
        }
    }

    /**
     * Helper method which redirect the input stream to the output stream,
     * using a buffer with given size.
     * 
     * @param in_p InputStream to redirect
     * @param out_p OutputStream for redirection
     * @param fClose_p flag for closing streams at the end
     * @param bufSize_p int buffer size to be used for redirection
     * @throws IOException if any problems during redirection
     * @since 3.1.0.0
     */
    public static void upload(InputStream in_p, OutputStream out_p, boolean fClose_p, int bufSize_p) throws IOException
    {
        LoggingStopWatch stopWatchUpload = new Log4JStopWatch("OwStreamUtil.upload");

        try
        {
            byte[] bBuffer = new byte[bufSize_p];

            int iRead = 0;
            while ((iRead = in_p.read(bBuffer, 0, bufSize_p)) != -1)
            {
                // write to output stream
                out_p.write(bBuffer, 0, iRead);
            }

            // flush output
            out_p.flush();
        }
        catch (IOException e)
        {
            String msg = "OwStreamUtil.upload: Error uploading the InputStream to the OutputStream.";
            LOG.error(msg, e);
            throw e;
        }
        finally
        {
            stopWatchUpload.stop();

            // === close streams
            if (fClose_p)
            {
                closeStreams(in_p, out_p);
            }
        }

    }

    /**
     * Helper method to close streams.
     * @param in_p Input stream to close
     * @param out_p Output stream to close
     * @throws IOException
     */
    private static void closeStreams(InputStream in_p, OutputStream out_p) throws IOException
    {
        if (in_p != null) // Inputstream
        {
            try
            {
                in_p.close();
                in_p = null;
            }
            catch (IOException e)
            {
                String msg = "OwStreamUtil.finally: Error closing the input stream.";
                LOG.warn(msg, e);
                throw e;
            }
        }
        if (out_p != null) // Outputstream
        {
            try
            {
                out_p.close();
                out_p = null;
            }
            catch (IOException e)
            {
                String msg = "OwStreamUtil.finally: Error closing the output stream.";
                LOG.warn(msg, e);
                throw e;
            }
        }
    }
}