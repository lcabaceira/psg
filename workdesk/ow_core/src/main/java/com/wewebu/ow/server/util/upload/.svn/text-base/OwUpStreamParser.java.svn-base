package com.wewebu.ow.server.util.upload;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 *<p>
 * A utility class to handle <code>multipart/form-data</code> requests, the kind
 * of requests that support file uploads.  This class uses a "pull" model where
 * the reading of incoming files and parameters is controlled by the client code,
 * which allows incoming files to be stored into any <code>OutputStream</code>.
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
public class OwUpStreamParser
{

    // input stream to read parts from
    private ServletInputStream m_in;

    // MIME boundary that delimits parts
    private String m_boundary;

    // reference to the last file part we returned
    private OwMultiPart m_lastFilePart;

    // buffer for readLine method
    private byte[] m_buf = new byte[8 * 1024];

    /**
     * Creates a <code>MultipartParser</code> from the specified request buffers for performance
     * @param req_p HttpServletRequest the servlet request.
     * @throws  IOException	if an input or output exception has occurred.
     */
    public OwUpStreamParser(HttpServletRequest req_p) throws IOException
    {

        // Check that content type is "multipart/form-data" or not
        String type = null;
        String type1 = req_p.getHeader("Content-Type");
        String type2 = req_p.getContentType();

        if (type1 == null && type2 != null)
        {
            type = type2;
        }
        else if (type2 == null && type1 != null)
        {
            type = type1;
        }
        else if (type1 != null && type2 != null)
        {
            type = (type1.length() > type2.length() ? type1 : type2);
        }

        if (type == null || !type.toLowerCase().startsWith("multipart/form-data"))
        {
            throw new IOException("Posted content type is not multipart/form-data");
        }

        // check for boundary
        String boundary = extractBoundary(type);
        if (boundary == null)
        {
            throw new IOException("Separation boundary was not specified");
        }

        ServletInputStream in = req_p.getInputStream();
        in = new OwBufferedServletInputStream(in);

        this.m_in = in;
        this.m_boundary = boundary;

        // Read the first line, should be the first boundary
        String line = readLine();
        if (line == null)
        {
            throw new IOException("Corrupt form data: premature ending");
        }

        // Verify that the line is the boundary
        if (!line.startsWith(boundary))
        {
            throw new IOException("Corrupt form data: no leading boundary: " + line + " != " + boundary);
        }

    }

    /**
     * Read the next part arriving in the stream. Will be either a FilePart or to
     * indicate there are no more parts to be read.
     * @return FilePart           - either a <code>FilePart</code>, or
     *                          <code>null</code> if there are no more parts to read.
     * @throws  IOException	if an input or output exception has occurred.
     */
    public OwMultiPart readNextPart() throws IOException
    {

        // Make sure the last file was entirely read from the input
        if (m_lastFilePart != null)
        {
            m_lastFilePart.getInputStream().close();
            m_lastFilePart = null;
        }

        /* Read the headers; they look like this (not all may be present):
           Content-Disposition: form-data; name="fileName"; filename="file1.txt"
           Content-Type: type/subtype
           Content-Transfer-Encoding: binary */
        Vector headers = new Vector();
        String line = readLine();
        if (line == null)
        {
            return null;
        }
        else if (line.length() == 0)
        {
            return null;
        }

        headers.addElement(line);

        // Read the following header lines we hit an empty line
        while ((line = readLine()) != null && (line.length() > 0))
        {
            headers.addElement(line);
        }

        // If we got a null above, it's the end
        if (line == null)
        {
            return null;
        }

        String name = null;
        String filename = null;
        String origname = null;
        String contentType = "text/plain";

        Enumeration enu = headers.elements();
        while (enu.hasMoreElements())
        {
            String headerline = (String) enu.nextElement();
            if (headerline.toLowerCase().startsWith("content-disposition:"))
            {

                // Parse the content-disposition line
                String[] dispInfo = extractDispositionInfo(headerline);
                name = dispInfo[1];
                if (dispInfo[2] != null)
                {
                    filename = dispInfo[2];
                }
                else
                {
                    filename = null;
                }

                origname = dispInfo[3];
            }
            else if (headerline.toLowerCase().startsWith("content-type:"))
            {
                // Get the content type, or null if none specified
                String type = extractContentType(headerline);
                if (type != null)
                {
                    contentType = type;
                }
            }
        }
        // Now read the content (end after reading the boundary)
        if (filename != null && filename.equals(""))
        {
            // empty filename, probably an "empty" file param
            filename = null;
        }
        m_lastFilePart = new OwMultiPart(name, m_in, m_boundary, contentType, filename, origname);
        return m_lastFilePart;
    }

    /*
      Extracts and returns the boundary token from a line.
    */
    private String extractBoundary(String line_p)
    {

        int index = line_p.lastIndexOf("boundary=");
        if (index == -1)
        {
            return null;
        }
        String boundary = line_p.substring(index + 9); // 9 for "boundary="
        if (boundary.charAt(0) == '"')
        {

            // The boundary is enclosed in quotes, strip them
            index = boundary.lastIndexOf('"');
            boundary = boundary.substring(1, index);
        }

        // The real boundary is always preceeded by an extra "--"
        boundary = "--" + boundary;

        return boundary;
    }

    /*
      Extracts and returns disposition info from a line, as a <code>String<code>
      array with elements: disposition, name, filename.
     */
    private String[] extractDispositionInfo(String line_p) throws IOException
    {
        // Return the line's data as an array: disposition, name, filename
        String[] retval = new String[4];

        /* Convert the line to a lowercase string without the ending \r\n
          Keep the original line for error messages and for variable names. */
        String origline = line_p;
        line_p = origline.toLowerCase();

        // Get the content disposition, should be "form-data"
        int start = line_p.indexOf("content-disposition: ");
        int end = line_p.indexOf(";");
        if (start == -1 || end == -1)
        {
            throw new IOException("Content disposition corrupt: " + origline);
        }
        String disposition = line_p.substring(start + 21, end);
        if (!disposition.equals("form-data"))
        {
            throw new IOException("Invalid content disposition: " + disposition);
        }

        // Get the field name
        start = line_p.indexOf("name=\"", end); // start at last semicolon
        end = line_p.indexOf("\"", start + 7); // skip name=\"
        if (start == -1 || end == -1)
        {
            throw new IOException("Content disposition corrupt: " + origline);
        }

        String name = origline.substring(start + 6, end);

        // Get the filename
        String filename = null;
        String origname = null;
        start = line_p.indexOf("filename=\"", end + 2); // start after name
        end = line_p.indexOf("\"", start + 10); // skip filename=\"

        if (start != -1 && end != -1)
        { // note the !=
            filename = origline.substring(start + 10, end);
            origname = filename;

            // The filename contains full path.Extract the filename
            int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
            if (slash > -1)
            {
                filename = filename.substring(slash + 1); // past last slash
            }
        }

        // Return a String array: disposition, name, filename, empty filename denotes no file posted
        retval[0] = disposition;
        retval[1] = name;
        retval[2] = filename;
        retval[3] = origname;
        return retval;
    }

    /*
      Extracts and returns the content type from a line, or null if the line was empty.
     */
    private String extractContentType(String line_p) throws IOException
    {
        String contentType = null;

        // Convert the line to a lowercase string
        String origline = line_p;
        line_p = origline.toLowerCase();

        // Get the content type
        if (line_p.startsWith("content-type"))
        {
            int start = line_p.indexOf(" ");
            if (start == -1)
            {
                throw new IOException("Content type corrupt: " + origline);
            }
            contentType = line_p.substring(start + 1);
        }
        else if (line_p.length() != 0)
        { // no content type, so should be empty
            throw new IOException("Malformed line after disposition: " + origline);
        }

        return contentType;
    }

    /* Read the next line of input. */

    private String readLine() throws IOException
    {
        StringBuffer sbuf = new StringBuffer();
        int result;
        do
        {
            result = m_in.readLine(m_buf, 0, m_buf.length); // does +=
            if (result != -1)
            {
                sbuf.append(new String(m_buf, 0, result, "ISO-8859-1"));
            }
        } while (result == m_buf.length); // loop only if the buffer was filled

        if (sbuf.length() == 0)
        {
            return null; // nothing read, must be at the end of stream
        }

        /* Cut off the trailing \n or \r\n */
        int len = sbuf.length();
        if (sbuf.charAt(len - 2) == '\r')
        {
            sbuf.setLength(len - 2); // cut \r\n
        }
        else
        {
            sbuf.setLength(len - 1); // cut \n
        }
        return sbuf.toString();
    }

    /**
     * tidy up all resources, especially close open streams
     * @throws IOException
     */
    public void tidyUp() throws IOException
    {
        m_in.close();
    }
}