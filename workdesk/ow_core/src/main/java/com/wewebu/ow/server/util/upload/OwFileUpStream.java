package com.wewebu.ow.server.util.upload;

import java.io.FilterInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;

/**
 *<p>
 * <code>PartInputStream</code> filters a <code>ServletInputStream</code>, providing access
 * to a single MIME part contained with in which ends with the boundary specified.
 * It uses buffering to provide performance.
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
public class OwFileUpStream extends FilterInputStream
{

    // boundary which "ends" the stream
    private String m_boundary;

    // buffer, 64k
    private byte[] m_buf = new byte[64 * 1024];

    // number of bytes read into the buffer
    private int m_count;

    // current position in the buffer
    private int m_pos;

    // flag that indicates that boundary is encountered
    private boolean m_eof;

    /**
    *  Creates a <code>PartInputStream</code> which stops at the specified
    *  boundary from a <code>ServletInputStream</code>.
     * 
     * @param in_p
     * @param boundary_p
     * @throws IOException
     */
    public OwFileUpStream(ServletInputStream in_p, String boundary_p) throws IOException
    {
        super(in_p);
        this.m_boundary = boundary_p;
    }

    /*
       Fill up buffer from the underlying input stream, and check for the boundary
       that signifies end-of-file.
     */
    private void fill() throws IOException
    {
        if (m_eof)
        {
            return;
        }
        if (m_count > 0)
        {
            // if the caller left the requisite amount spare in the buffer
            if (m_count - m_pos == 2)
            {
                // copy it back to the start of the buffer
                System.arraycopy(m_buf, m_pos, m_buf, 0, m_count - m_pos);
                m_count -= m_pos;
                m_pos = 0;
            }
            else
            {
                // should never happen, but just in case
                throw new IllegalStateException("fill() detected illegal buffer state");
            }
        }

        /* try and fill the entire buffer, starting at count, line by line
           but never read so close to the end that we might split a boundary */
        int iread = 0;
        int imaxRead = m_buf.length - m_boundary.length();
        while (m_count < imaxRead)
        {

            // read a line
            iread = ((ServletInputStream) in).readLine(m_buf, m_count, m_buf.length - m_count);

            // check for eof and boundary
            if (iread == -1)
            {
                throw new IOException("unexpected end of part");
            }
            else
            {
                if (iread >= m_boundary.length())
                {
                    m_eof = true;
                    for (int i = 0; i < m_boundary.length(); i++)
                    {
                        if (m_boundary.charAt(i) != m_buf[m_count + i])
                        {
                            // Not the boundary!
                            m_eof = false;
                            break;
                        }
                    }
                    if (m_eof)
                    {
                        break;
                    }
                }
            }
            m_count += iread; //success
        }
    }

    /**
     *  Reads the next byte of data from the InputStream.
     *  @return int                 the next byte of data, or <code>-1</code> if the end of the stream is reached.
     *  @throws  IOException	if an input or output exception has occurred.
     */
    public int read() throws IOException
    {
        if (m_count - m_pos <= 2)
        {
            fill();
            if (m_count - m_pos <= 2)
            {
                return -1;
            }
        }
        return m_buf[m_pos++] & 0xff;
    }

    /** 
    *  Reads some number of bytes from the input stream and stores them into the
    *  buffer array. The number of bytes actually read is returned as integer.
    *  This method blocks untill input data is available, end of file is detected,
    *  or exception is thrown
    * @param b_p the byte array buffer into which the data is read.
     * 
     */
    public int read(byte b_p[]) throws IOException
    {
        return read(b_p, 0, b_p.length);
    }

    /**
     * Read upto len bytes of data from inputstream into an array of bytes.
     * The number of bytes actually read is returned as integer.
     * This method blocks untill input data is available, end of file is detected,
     * or an exception is thrown.
     * @param b_p the byte array buffer into which the data is read.
     * @param off_p int the start offset of the data.
     * @param len_p int the total number of bytes read into the buffer, or
     *                            <code>-1</code> if there is no more data because the
     *                            end of the stream has been reached
     * @return int
     * @exception  IOException  if an I/O error occurs.
     */
    public int read(byte b_p[], int off_p, int len_p) throws IOException
    {
        int itotal = 0;
        if (len_p == 0)
        {
            return 0;
        }

        int iavail = m_count - m_pos - 2;
        if (iavail <= 0)
        {
            fill();
            iavail = m_count - m_pos - 2;
            if (iavail <= 0)
            {
                return -1;
            }
        }
        int icopy = Math.min(len_p, iavail);
        System.arraycopy(m_buf, m_pos, b_p, off_p, icopy);
        m_pos += icopy;
        itotal += icopy;

        while (itotal < len_p)
        {
            fill();
            iavail = m_count - m_pos - 2;
            if (iavail <= 0)
            {
                return itotal;
            }
            icopy = Math.min(len_p - itotal, iavail);
            System.arraycopy(m_buf, m_pos, b_p, off_p + itotal, icopy);
            m_pos += icopy;
            itotal += icopy;
        }
        return itotal;
    }

    /**
     * Returns the number of bytes that can be read from this input stream without
     * blocking.
     * @return     the number of bytes that can be read from the input stream
     *             without blocking.
     * @exception  IOException  if an I/O error occurs.
     */
    public int available() throws IOException
    {
        int iavail = (m_count - m_pos - 2) + in.available();
        return (iavail < 0 ? 0 : iavail);
    }

    /**
     * Closes this input stream and releases any system resources associated with the stream.
     * <p>
     * @exception  IOException  if an I/O error occurs.
     */
    public void close() throws IOException
    {
        if (!m_eof)
        {
            while (read(m_buf, 0, m_buf.length) != -1)
            {
                ; /* do nothing */
            }
        }
    }
}