package com.wewebu.ow.clientservices.utils;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;

import com.wewebu.ow.clientservices.exception.OwDecodeException;

/**
 *<p>
 * This class knows how to decode a rfc2047 encoded string.<br/>
 * See <a href="http://tools.ietf.org/html/rfc2047">rfc2047</a>.
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
public class OwRfc2047Decoder
{
    private static final String SEQ_START = "=?";
    private static final String SEQ_END = "?=";
    private static final String ENCODING_B = "B";

    /**
     * "=?UTF-8?B?<Base64 encoded>?="
     * @param encodedMsg_p
     * @return The decoded string.
     * @throws OwDecodeException 
     */
    public static String decode(String encodedMsg_p) throws OwDecodeException
    {
        if (!encodedMsg_p.startsWith(SEQ_START) || !encodedMsg_p.endsWith(SEQ_END))
        {
            throw new OwDecodeException("String '" + encodedMsg_p + "' seems not to be a rfc2047 encoded one.");
        }
        String decodedMsg = encodedMsg_p;
        StringTokenizer tokenizer = new StringTokenizer(decodedMsg, "?");
        // skip =?
        tokenizer.nextToken();

        String charSet = tokenizer.nextToken();
        String encoding = tokenizer.nextToken();

        if (!encoding.equals(ENCODING_B))
        {
            throw new OwDecodeException("Trying to decode '" + encodedMsg_p + "'. Only the B encoding is currently supported.");
        }

        String encodedMsg = tokenizer.nextToken();
        if (encoding.equals(ENCODING_B))
        {
            //Base64
            try
            {
                decodedMsg = new String(Base64.decodeBase64(encodedMsg.getBytes(charSet)), charSet);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new OwDecodeException(e);
            }
        }

        return decodedMsg;
    }
}
