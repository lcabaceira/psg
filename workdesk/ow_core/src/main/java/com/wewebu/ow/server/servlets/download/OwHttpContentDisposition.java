/**
 * 
 */
package com.wewebu.ow.server.servlets.download;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

/**
 *<p>
 * A simple class to generate the HTTP attachment element to be used with Content-Disposition header.
 * This class tries to handle UTF-8 file names in the best possible way for all user agents.
 * Everything is supposed to be UTF-8 encoded.
 *</p>
 *<p>
 *For a discussion of the specs see 
 *<ul>
 *<li>
 *<a href="http://tools.ietf.org/html/rfc2047">MIME (Multipurpose Internet Mail Extensions) Part Three:
 *             Message Header Extensions for Non-ASCII Text</a>
 *</li>
 *<li>
 *<a href="http://www.ietf.org/rfc/rfc1806.txt">Communicating Presentation Information in
 *                           Internet Messages:
 *                     The Content-Disposition Header
 *</a>
 *</li>
 *</ul>
 *</p>
 *<p>
 *For a discussion on how different user agents handle non ASCI characters in HTML headers see 
 *<a href="http://blogs.warwick.ac.uk/kieranshaw/entry/utf-8_internationalisation_with">http://blogs.warwick.ac.uk/kieranshaw/entry/utf-8_internationalisation_with</a>.
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
public class OwHttpContentDisposition
{
    private static final String UTF_8 = "UTF-8";
    private static final String HTTP_HEADER_USER_AGENT = "User-Agent";
    private String encodedFileName;
    private OwElementType elementType;

    /**
     *<p>
     * OwElementType.
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
    public enum OwElementType
    {
        ATTACHMENT("attachment"), INLINE("inline");
        private String elName;

        private OwElementType(String elName_p)
        {
            this.elName = elName_p;
        }

        @Override
        public String toString()
        {
            return this.elName;
        }
    }

    private OwHttpContentDisposition(String encodedFileName_p, OwElementType elementType_p)
    {
        this.encodedFileName = encodedFileName_p;
        this.elementType = elementType_p;
    }

    /**
     * Tries to build the right attachment element for the requesting agent. 
     * @param request_p The initial request from the client. We need it in order to detect the user agent.
     * @param fileName_p The name of the file to be used in the attachment element.
     * @param elementType_p inline or attachment
     * @return an instance of {@link OwHttpContentDisposition}
     * @throws UnsupportedEncodingException 
     */
    public static OwHttpContentDisposition forRequest(HttpServletRequest request_p, String fileName_p, OwElementType elementType_p) throws UnsupportedEncodingException
    {
        String userAgent = request_p.getHeader(HTTP_HEADER_USER_AGENT);

        //It seems that most user-agents support ISO-8859-1, although the specification requires only US-ASCII support.
        CharsetEncoder encoder = Charset.forName("ISO-8859-1").newEncoder();
        boolean is_ISO_8859_1 = encoder.canEncode(fileName_p);
        if (!is_ISO_8859_1)
        {
            String encodedFileName;
            if (userAgent.contains("MSIE") || userAgent.contains("Opera"))
            {
                encodedFileName = URLEncoder.encode(fileName_p, UTF_8);
            }
            else
            {
                // see http://tools.ietf.org/html/rfc2047
                encodedFileName = "=?UTF-8?B?" + new String(Base64.encodeBase64(fileName_p.getBytes(UTF_8)), UTF_8) + "?=";
            }

            return new OwHttpContentDisposition(encodedFileName, elementType_p);
        }
        else
        {
            return new OwHttpContentDisposition(fileName_p, elementType_p);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return new StringBuilder(this.elementType.toString() + ";filename=\"").append(encodedFileName).append("\"").toString();
    }
}
