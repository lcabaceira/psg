package com.wewebu.ow.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 *<p>
 * Class OwUnicodeInputStream.<br/>
 * The class recognize unicode BOM marks and will skip bytes:
 *<pre>
 *    <LI>00 00 FE FF    = UTF-32, big-endian</LI>
 *    <LI>FF FE 00 00    = UTF-32, little-endian</LI>
 *    <LI>EF BB BF       = UTF-8</LI>
 *    <LI>FE FF          = UTF-16, big-endian</LI>
 *    <LI>FF FE          = UTF-16, little-endian</LI>
 *</pre> 
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
public class OwUnicodeInputStream extends InputStream
{
    PushbackInputStream internalIn;
    boolean isInited = false;
    String defaultEnc;
    String encoding;

    private static final int BOM_SIZE = 4;

    /**
     * Creates OwUnicodeInputStream will recognize unicode BOM marks
     * and will skip bytes if getEncoding() method is called
     * before any of the read(...) methods.
     * @param in_p InputStream
     * @param defaultEnc_p defaultEncoding
     */
    OwUnicodeInputStream(InputStream in_p, String defaultEnc_p)
    {
        internalIn = new PushbackInputStream(in_p, BOM_SIZE);
        this.defaultEnc = defaultEnc_p;
    }

    /**
     * 
     * @return String defaultEncodinf
     */
    public String getDefaultEncoding()
    {
        return defaultEnc;
    }

    /**
     * 
     * @return String encoding
     */
    public String getEncoding()
    {
        if (!isInited)
        {
            try
            {
                init();
            }
            catch (IOException ex)
            {
                IllegalStateException ise = new IllegalStateException("Init method failed.", ex);
                ise.initCause(ise);
                throw ise;
            }
        }
        return encoding;
    }

    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are
     * unread back to the stream, only BOM bytes are skipped.
     */
    protected void init() throws IOException
    {
        if (isInited)
        {
            return;
        }

        byte bom[] = new byte[BOM_SIZE];
        int n, unread;
        n = internalIn.read(bom, 0, bom.length);

        if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF))
        {
            encoding = "UTF-32BE";
            unread = n - 4;
        }
        else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00))
        {
            encoding = "UTF-32LE";
            unread = n - 4;
        }
        else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF))
        {
            encoding = "UTF-8";
            unread = n - 3;
        }
        else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF))
        {
            encoding = "UTF-16BE";
            unread = n - 2;
        }
        else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE))
        {
            encoding = "UTF-16LE";
            unread = n - 2;
        }
        else
        {
            // Unicode BOM mark not found, unread all bytes
            encoding = defaultEnc;
            unread = n;
        }

        if (unread > 0)
        {
            internalIn.unread(bom, (n - unread), unread);
        }

        isInited = true;
    }

    public void close() throws IOException
    {
        isInited = true;
        internalIn.close();
    }

    public int read() throws IOException
    {
        isInited = true;
        return internalIn.read();
    }
}
