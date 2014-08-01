package com.wewebu.ow.server.util.upload;

import java.io.IOException;

import javax.servlet.ServletInputStream;

/**
 *<p>
 * A <code>BufferedServletInputStream</code> wraps a
 * <code>ServletInputStream</code> in order to provide input buffering and to
 * avoid calling the the <code>readLine</code> method of the wrapped
 * <code>ServletInputStream</code>.
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
public class OwBufferedServletInputStream extends ServletInputStream
{

    // input stream for filtering
    private ServletInputStream in;

    // buffer , 64k
    private byte[] buf = new byte[64 * 1024];

    // number of bytes read into the buffer
    private int count;

    // current position in the buffer
    private int pos;

    /**
     * Creates a <code>BufferedServletInputStream</code> that wraps
     * <code>ServletInputStream</code>.
     *
     * @param in_p  a servlet input stream.
     */
    public OwBufferedServletInputStream(ServletInputStream in_p)
    {
        this.in = in_p;
    }

    /*
      Fill up buffer from the underlying input stream.
      @ return void
      @exception  IOException  if an I/O error occurs.
     */
    private void fill() throws IOException
    {
        int i = in.read(buf, 0, buf.length);
        if (i > 0)
        {
            pos = 0;
            count = i;
        }
    }

    /**
     * Implement buffering <code>readLine</code> method of
     * the wrapped <code>ServletInputStream</code>.
     *
     * @param b_p    an array of bytes into which data is read.
     * @param off_p  an integer specifying the character at which
     *        this method begins reading.
     * @param len_p  an integer specifying the maximum number of
     *        bytes to read.
     * @return     an integer specifying the actual number of bytes
     *        read, or -1 if the end of the stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int readLine(byte b_p[], int off_p, int len_p) throws IOException
    {
        int total = 0;
        if (len_p == 0)
        {
            return 0;
        }

        int avail = count - pos;
        if (avail <= 0)
        {
            fill();
            avail = count - pos;
            if (avail <= 0)
            {
                return -1;
            }
        }
        int copy = Math.min(len_p, avail);
        int eol = findeol(buf, pos, copy);
        if (eol != -1)
        {
            copy = eol;
        }
        System.arraycopy(buf, pos, b_p, off_p, copy);
        pos += copy;
        total += copy;

        while (total < len_p && eol == -1)
        {
            fill();
            avail = count - pos;
            if (avail <= 0)
            {
                return total;
            }
            copy = Math.min(len_p - total, avail);
            eol = findeol(buf, pos, copy);
            if (eol != -1)
            {
                copy = eol;
            }
            System.arraycopy(buf, pos, b_p, off_p + total, copy);
            pos += copy;
            total += copy;
        }
        return total;
    }

    /*
      Attempt to find the '\n' end of line marker as defined in the comment of
      the <code>readLine</code> method of <code>ServletInputStream</code>.
      @param b byte array to search.
      @param pos position in byte array to search from.
      @param len maximum number of bytes to search.
      @return the number of bytes including the \n, or -1 if none found.
     */
    private static int findeol(byte b_p[], int pos_p, int len_p)
    {
        int end = pos_p + len_p;
        int i = pos_p;
        while (i < end)
        {
            if (b_p[i++] == '\n')
            {
                return i - pos_p;
            }
        }
        return -1;
    }

    /**
     * Implement buffering <code>read</code> method of
     * the wrapped <code>ServletInputStream</code>.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read() throws IOException
    {
        if (count <= pos)
        {
            fill();
            if (count <= pos)
            {
                return -1;
            }
        }
        return buf[pos++] & 0xff;
    }

    /**
     * Implement buffering <code>read</code> method of
     * the wrapped <code>ServletInputStream</code>.
     *
     * @param      b_p     the buffer into which the data is read.
     * @param      off_p   the start offset of the data.
     * @param      len_p   the maximum number of bytes read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end
     *             of the stream has been reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read(byte b_p[], int off_p, int len_p) throws IOException
    {
        int total = 0;
        while (total < len_p)
        {
            int avail = count - pos;
            if (avail <= 0)
            {
                fill();
                avail = count - pos;
                if (avail <= 0)
                {
                    if (total > 0)
                    {
                        return total;
                    }
                    else
                    {
                        return -1;
                    }
                }
            }
            int copy = Math.min(len_p - total, avail);
            System.arraycopy(buf, pos, b_p, off_p + total, copy);
            pos += copy;
            total += copy;
        }
        return total;
    }

    /**
     * close the wrapped stream
     * @see java.io.InputStream#close()
     */
    public void close() throws IOException
    {
        in.close();
    }
}