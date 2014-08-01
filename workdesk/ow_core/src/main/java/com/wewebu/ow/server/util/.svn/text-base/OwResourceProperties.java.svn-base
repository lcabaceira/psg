package com.wewebu.ow.server.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Class which upgrades the <code>java.util.Properties</code> with several methods. 
 * Also needed to read .properties files with different encoding, if there are saved in
 * using a unicode encoding.
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
 *@since 2.2.0.0
 */
public class OwResourceProperties extends Properties
{
    /** package logger or class logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwResourceProperties.class);

    /**	 */
    private static final long serialVersionUID = 1L;

    /**Bytes to be read for analyze UNICODE (UTF-8, UTF-16BE, UTF-16LE,
     * UTF-32BE, UTF-32LE) information [<b>B</b>yte <b>O</b>rder <b>M</b>ark].
     * <p>For more Information read: http://unicode.org/faq/utf_bom.html#BOM</p>*/
    public static final int BOM_SIZE = 4;

    /**Default encoding of Properties files.
     * @see Properties#load(InputStream)*/
    private static final String DEFAULT_ENC = "ISO-8859-1";

    /**
     * Contains the encoding, which is was used
     * to parse the properties file.
     * If it is <b>null</b> then as default the ISO-8859-1 is used for encoding.
     */
    private String encoding;

    /**
     * Upgrade of standard java.util.Properties, so we can analyze the encoding of the file. 
     * If <b> resource_p </b> is a unicode file, we use the {@link #load(Reader)} method
     * and a reader with the specified character encoding to read and convert 
     * the contained strings.
     * 
     * @param resource_p URL to the specific resource
     * @throws IOException throws if as example the resource was not found.
     */
    public void load(URL resource_p) throws IOException
    {
        InputStream is = null;
        PushbackInputStream inPush = null;
        try
        {
            is = resource_p.openStream();

            if (is == null)
            {
                throw new NullPointerException(resource_p.toString());
            }
            inPush = new PushbackInputStream(is, BOM_SIZE);
            encoding = getResourceEncoding(inPush);

            if (encoding != null)
            {
                load(new InputStreamReader(inPush, encoding));
            }
            else
            {
                encoding = DEFAULT_ENC;
                load(new InputStreamReader(inPush));
            }
        }
        finally
        {
            if (inPush != null)
            {
                inPush.close();
            }
        }

    }

    /**
     * Since the Alfresco Workdesk is supporting also the JRE 1.4.2, the java.util.Properties 
     * are also upgraded with a <code>load(Reader)</code> method - like in java 6. 
     * This method reads a properties file, using the specified character
     * encoding of the Reader.
     * 
     * @param reader_p Any Reader extending the java.io.Reader class. 
     * @throws IOException
     */
    public void load(Reader reader_p) throws IOException
    {
        int iChar = 0;
        char c;
        boolean comment = false;
        while ((iChar = reader_p.read()) > 0)
        {
            if (iChar == '#')
            {
                comment = true;
            }
            if (iChar == '\n' || iChar == '\r' || comment)
            {
                if (iChar == '\n' || iChar == '\r')
                {
                    comment = false;
                }
                continue;//get next character because this is an empty line
            }

            StringBuilder bufKey = new StringBuilder();
            StringBuilder bufVal = new StringBuilder();
            boolean escaped = false;
            //read key string
            while (iChar > 0)
            {
                c = (char) iChar;
                //don't read further, because key ends here with separator
                if (!escaped)
                {
                    if (c == '=' || c == ' ' || c == ':')
                    {
                        break;
                    }
                }

                //don't read further, because key definition ends here
                if (c == '\f' || c == '\t' || c == '\n' || c == '\r')
                {
                    if (!escaped)
                    {
                        break;
                    }
                }

                if (c == '\\')
                {
                    escaped = true;
                }
                else
                {
                    escaped = false;
                }
                //add the current character to the key-string
                if (!escaped)
                {
                    bufKey.append(c);
                }
                iChar = reader_p.read();
            }

            escaped = false;
            //read value string
            iChar = reader_p.read();
            while (iChar > 0)
            {
                c = (char) iChar;
                if (c == '\r' || c == '\n')
                {
                    if (!escaped)
                    {
                        break;// finish reading this line
                    }
                }
                if (c == '\\')
                {
                    escaped = escaped ? false : true;
                }
                else
                {
                    escaped = escaped ? c == '\r' || c == '\n' : false;
                }
                if (!escaped)
                {
                    bufVal.append(c);
                }
                iChar = reader_p.read();
            }

            // put the read strings into the HashTable, don't forget cut
            // leading and trailing white spaces

            put(bufKey.toString().trim(), bufVal.toString());
        }
    }

    /**
     * Static function for analyzing resource defined by the URL.
     * This method try to open a stream using the given URL, and 
     * read the first 4 bytes of the resource. 
     * @see #getResourceEncoding(byte[])
     * 
     * @param url_p URL to the specific resource
     * @return String representing the unicode character set, or null if no BOM defined.
     * @throws IOException
     */
    public static String getResourceEncoding(URL url_p) throws IOException
    {
        String retEnc = null;
        BufferedInputStream buffIn = null;
        try
        {
            buffIn = new BufferedInputStream(url_p.openStream());
            byte[] bom = new byte[BOM_SIZE];
            int readedBom = buffIn.read(bom);
            retEnc = getResourceEncoding(bom);
            if (LOG.isDebugEnabled() && retEnc == null)
            {
                LOG.debug("OwResourceProperties.getResourceEncoding(URL=" + url_p + "): Unkown encoding, (Readed BOM=" + readedBom + ")...");
            }
        }
        finally
        {
            if (buffIn != null)
            {
                buffIn.close();
                buffIn = null;
            }
        }
        return retEnc;
    }

    /**
     * Static function for analyzing the BOM of a file, and returning an
     * encoding or null if no valid BOM is defined.
     * 
     * @param analyse_p byte[4] BOM (= <b>B</b>yte <b>O</b>rder <b>M</b>ark) - the first 4 bytes of a file or resource.
     * @return String representing the encoding of the unicode file, or <b>null</b> if BOM is not valid.
     */
    public static String getResourceEncoding(byte[] analyse_p)
    {
        String fileEncoding = null;
        if ((analyse_p[0] == (byte) 0x00) && (analyse_p[1] == (byte) 0x00) && (analyse_p[2] == (byte) 0xFE) && (analyse_p[3] == (byte) 0xFF))
        {
            fileEncoding = "UTF-32BE";
        }
        else if ((analyse_p[0] == (byte) 0xFF) && (analyse_p[1] == (byte) 0xFE) && (analyse_p[2] == (byte) 0x00) && (analyse_p[3] == (byte) 0x00))
        {
            fileEncoding = "UTF-32LE";
        }
        else if ((analyse_p[0] == (byte) 0xEF) && (analyse_p[1] == (byte) 0xBB) && (analyse_p[2] == (byte) 0xBF))
        {
            fileEncoding = "UTF-8";
        }
        else if ((analyse_p[0] == (byte) 0xFE) && (analyse_p[1] == (byte) 0xFF))
        {
            fileEncoding = "UTF-16BE";
        }
        else if ((analyse_p[0] == (byte) 0xFF) && (analyse_p[1] == (byte) 0xFE))
        {
            fileEncoding = "UTF-16LE";
        }
        else
        {
            // Unicode BOM mark not found, unread all bytes
            fileEncoding = null;
        }
        return fileEncoding;
    }

    /**
     * Reads encoding type and skips BOM bits
     * @param pushStream PushbackInputStream 
     * @return Encoding String 
     * @throws IOException
     * @since 3.2.0.2
     */
    public static String getResourceEncoding(PushbackInputStream pushStream) throws IOException
    {
        String encoding;
        byte[] bom = new byte[BOM_SIZE];
        int n, unread;

        n = pushStream.read(bom, 0, bom.length);
        encoding = getResourceEncoding(bom);

        if ("UTF-32BE".equals(encoding))
        {
            unread = n - 4;
        }
        else if ("UTF-32LE".equals(encoding))
        {
            unread = n - 4;
        }
        else if ("UTF-8".equals(encoding))
        {
            unread = n - 3;
        }
        else if ("UTF-16BE".equals(encoding))
        {
            unread = n - 2;
        }
        else if ("UTF-16LE".equals(encoding))
        {
            unread = n - 2;
        }
        else
        {
            // Unicode BOM mark not found, unread all bytes
            unread = n;
        }

        if (unread > 0)
        {
            pushStream.unread(bom, (n - unread), unread);
        }

        return encoding;
    }

}