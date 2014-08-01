package com.wewebu.ow.server.util;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Tests the OwSecretEncryption utility class.
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
public class OwSecretEncryptionTest extends TestCase
{
    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    private static final String m_testtext = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ÄÖÜ,.-;:_#<>+'*~^!\" $%&/()=?`[]@\\";

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testByteToStringEncoding() throws OwInvalidOperationException
    {
        byte[] test = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        String enc = OwSecretEncryption.bytesToString(test);
        byte[] dec = OwSecretEncryption.stringToBytes(enc);

        assertEquals(test.length, dec.length);
        for (int i = 0; i < test.length; i++)
        {
            assertEquals(test[i], dec[i]);
        }
    }

    public void testStringEncryption() throws OwInvalidOperationException, UnsupportedEncodingException
    {
        byte[] enc = OwSecretEncryption.encrypt(m_testtext.getBytes("UTF-8"));

        String text2 = OwSecretEncryption.decryptToString(enc, "UTF-8");

        assertEquals(m_testtext, text2);
    }

    public void testStringToTextEncryption() throws OwInvalidOperationException, UnsupportedEncodingException
    {
        byte[] enc = OwSecretEncryption.encrypt(m_testtext.getBytes("UTF-8"));

        // convert to string
        String bytes = OwSecretEncryption.bytesToString(enc);

        // convert back to byte
        byte[] enc2 = OwSecretEncryption.stringToBytes(bytes);

        String text2 = OwSecretEncryption.decryptToString(enc2, "UTF-8");

        assertEquals(m_testtext, text2);
    }

}
