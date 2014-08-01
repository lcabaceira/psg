package com.wewebu.ow.server.util.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletInputStream;

/**
 *<p>
 * A FilePart is an upload part, which represents an input type = "file" form parameter.
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
public class OwMultiPart
{

    // name of the parameter
    private String m_name;

    // name of the file
    private String m_fileName;

    // path of the file as sent in the request
    private String m_filePath;

    // content type of the file
    private String m_contentType;

    // input stream containing file data
    private OwFileUpStream m_partInput;

    /**
     * Construct a file part
     * @param name_p String  - the name of the parameter.
     * @param in_p ServletInputStream  - the servlet input stream to read the file from.
     * @param boundary_p String         - the MIME boundary that delimits the end of file.
     * @param contentType_p String      - the content type of the file provided in the MIME header.
     * @param fileName_p String         - the file system name of the file provided in the MIME header.
     * @param filePath_p String         - the file system path of the file provided in the MIME header
     *                                  (as specified in disposition info).
     * @exception IOException	if an input or output exception has occurred.
     */
    public OwMultiPart(String name_p, ServletInputStream in_p, String boundary_p, String contentType_p, String fileName_p, String filePath_p) throws IOException
    {
        this.m_name = name_p;
        this.m_fileName = fileName_p;
        this.m_filePath = filePath_p;
        this.m_contentType = contentType_p;
        m_partInput = new OwFileUpStream(in_p, boundary_p);
    }

    /**
     *  Returns the name of the form element that this Part corresponds to.
     *  @return java.lang.String          Name of the form element that this Part corresponds to.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Returns the name that the file was stored with on the remote system, or
     * <code>null</code> if the user didn't enter a file to be uploaded.
     * @return String m_filename    Name of the file uploaded or <code>null</code>.
     */
    public String getFileName()
    {
        return m_fileName;
    }

    /**
     * Returns the full path and name of the file on the remote system, or
     * <code>null</code> if the user didn't enter a file to be uploaded.
     * @return String m_filePath    Path of the file uploaded or <code>null</code>.
     */
    public String getFilePath()
    {
        return m_filePath;
    }

    /**
     * Returns the content type of the file data contained within.
     * @return String m_contentType  content type of the file data contained within.
     */
    public String getContentType()
    {
        return m_contentType;
    }

    /**
     * Returns an input stream which contains the contents of the file supplied.
     * If the user didn't enter a file to upload there will be <code>0</code> bytes
     * in the input stream.
     * @return String m_partInput   input stream containing contents of file.
     */
    public InputStream getInputStream()
    {
        return m_partInput;
    }

    /**
     * Write this file part to a file or directory. If the user supplied a file,
     * we write it to that file, and if they supplied a directory, we write it to
     * that directory with the filename that accompanied it. If this part doesn't
     * contain a file this method does nothing.
     * @param fileName_p name of the uploaded file
     *  @param fileOrDirectory_p File/Dir to which data will be written
     *  @return long number of bytes written.
     *  @throws IOException if an input or output exception is occurred.
     */
    public long writeTo(File fileOrDirectory_p, String fileName_p) throws IOException
    {
        long lwritten = 0;
        OutputStream fileOut = null;
        FileOutputStream fileOutputStream = null;
        try
        {
            if (m_fileName != null)
            {
                File file;
                if (fileOrDirectory_p.isDirectory())
                {
                    // creating a temporary file
                    file = new File(fileOrDirectory_p, fileName_p);
                }
                else
                {
                    file = fileOrDirectory_p;
                }
                fileOutputStream = new FileOutputStream(file);
                fileOut = new BufferedOutputStream(fileOutputStream);
                lwritten = write(fileOut);
                fileOut.flush();
                fileOutputStream.flush();
            }
        }
        finally
        {
            if (fileOutputStream != null)
            {
                fileOutputStream.close();
            }
            if (fileOut != null)
            {
                fileOut.close();
            }
        }
        return lwritten;
    }

    /**
     *  Write this file part to the given output stream. If this part doesn't contain
     *  a file this method does nothing.
     *  @param out_p OutputStream to which data will be written
     *  @return long                        number of bytes written.
     *  @throws IOException if an input or output exception is occurred.
     */
    public long writeTo(OutputStream out_p) throws IOException
    {
        long lsize = 0;
        // if (m_fileName != null) {
        lsize = write(out_p);
        // }
        return lsize;
    }

    /**
     *  Internal method to write this file part; doesn't check to see if it has contents first.
     *  a file this method does nothing.
     *  @param out_p OutputStream to which data will be written
     *  @return long                        number of bytes written.
     *  @throws IOException if an input or output exception is occurred.
     */
    private long write(OutputStream out_p) throws IOException
    {
        long lsize = 0;
        int iread;
        byte[] bybuf = new byte[8 * 1024];
        while ((iread = m_partInput.read(bybuf)) != -1)
        {
            out_p.write(bybuf, 0, iread);
            lsize += iread;
        }
        return lsize;
    }

    /**
     *  Returns true if this Part is a FilePart.
     *  @return boolean                   true if this is a FilePart.
     */
    public boolean isFile()
    {
        return (m_fileName != null);
    }
}