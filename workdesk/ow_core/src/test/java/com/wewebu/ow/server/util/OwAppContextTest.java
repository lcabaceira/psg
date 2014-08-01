package com.wewebu.ow.server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import junit.framework.TestCase;

import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Tests the OwMimeTypes utility class.
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
public class OwAppContextTest extends TestCase
{
    public static final String SPECIALCHARS = "! \" § $ % & / ( ) = ? \\ ´ ` + * ~ # ' @ ä ö ü Ä Ö Ü - _";
    public static final String ENC_SPECIALCHARS = "%21%20%22%20%C2%A7%20%24%20%25%20%26%20%2F%20%28%20%29%20%3D" + "%20%3F%20%5C%20%C2%B4%20%60%20%2B%20*%20%7E%20%23%20%27%20%40%20%E2%82%AC%20%C3%A4%20%C3%B6%20%C3%"
            + "BC%20%C3%84%20%C3%96%20%C3%9C%20-%20_";

    public void testEncodeURL() throws Exception
    {
        String enc = OwAppContext.encodeURL(SPECIALCHARS);

        String self = URLEncoder.encode(SPECIALCHARS, "UTF-8").replaceAll("[:+:]", "%20");

        assertTrue(enc.indexOf('+') < 0);
        assertTrue(self.indexOf('+') < 0);
        assertEquals(self, enc);

    }

    /**
     * Test decoding with an encoded 
     * @throws UnsupportedEncodingException
     */
    public void testDecodeURL() throws UnsupportedEncodingException
    {
        String dec = OwAppContext.decodeURL(ENC_SPECIALCHARS);
        String self = URLDecoder.decode(ENC_SPECIALCHARS, "UTF-8").replaceAll("%20", "+");

        assertTrue(dec.length() == self.length());
        assertEquals(self, dec);
    }

    /**
     * Test complete cycle of enc- and decoding with a given URL as String.
     * And also testing the de- and encoding of a encoded String.
     * @throws UnsupportedEncodingException
     */
    public void testCompleteCoding() throws UnsupportedEncodingException
    {
        String enc = OwAppContext.encodeURL(SPECIALCHARS);

        String dec = OwAppContext.decodeURL(enc);

        assertEquals(SPECIALCHARS, dec);

        dec = OwAppContext.decodeURL(ENC_SPECIALCHARS);

        enc = OwAppContext.encodeURL(dec);

        assertEquals(ENC_SPECIALCHARS, enc);
    }

}
