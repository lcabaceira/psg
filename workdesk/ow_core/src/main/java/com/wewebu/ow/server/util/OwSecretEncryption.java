package com.wewebu.ow.server.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Utility class to encrypt / decrypt strings.<br/><br/>
 * You can use this class for simple encryption of strings.<br/>
 * <i>WARNING:</i><br/>
 * This class offers only weak encryption. Everybody with access to this class file
 * is able to read the encrypted string! The key is stored inside OwSecretKey.class
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
public class OwSecretEncryption
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwSecretEncryption.class);

    /** key singleton */
    private static volatile OwSecretKey m_key = new OwSecretKey();

    /** get a string representation of the given byte array
     * 
     * @param bytes_p
     * @return a string representation of the given byte array

     * @see #stringToBytes(String)
     */
    public static String bytesToString(byte[] bytes_p)
    {
        char[] hexdigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes_p.length; i++)
        {
            byte lownibble = (byte) (bytes_p[i] & 0x0000000F);
            byte highnibble = (byte) ((bytes_p[i] & 0x000000F0) >> 4);
            result.append(hexdigits[highnibble]);
            result.append(hexdigits[lownibble]);
        }
        return result.toString();
    }

    /** get a byte array from the given string, that was created with bytesToString
     * 
     * @param text_p
     * @return a byte array
     * @throws OwInvalidOperationException 
     * @throws OwInvalidOperationException 
     * @see #bytesToString(byte[])
     */
    public static byte[] stringToBytes(String text_p) throws OwInvalidOperationException
    {
        // sanity check
        if ((text_p.length() % 2) != 0)
        {
            return new byte[0];
        }
        // convert
        byte[] result = new byte[text_p.length() / 2];
        for (int i = 0; i < text_p.length(); i += 2)
        {
            try
            {
                int irep = Integer.parseInt(text_p.substring(i, i + 2), 16);
                if (irep > 127)
                {
                    irep -= 256;
                }
                result[i / 2] = Integer.valueOf(irep).byteValue();
            }
            catch (Exception e)
            {
                String msg = "Encrypt failed, cannot convert the string to byte[], maybe contains invalid characters...";
                LOG.debug(msg, e);
                throw new OwInvalidOperationException(msg, e);
            }
        }
        return result;
    }

    /** encrypt the given string
     * 
     * @param text_p
     * @return a byte array 
     * @throws OwInvalidOperationException 
     */
    public static byte[] encrypt(String text_p) throws OwInvalidOperationException
    {
        // encrypt
        return encrypt(text_p.getBytes());
    }

    /** encrypt the given byte array
     * 
     * @param bytes_p
     * @return the encrypted byte array
     * @throws OwInvalidOperationException 
     */
    public static byte[] encrypt(byte[] bytes_p) throws OwInvalidOperationException
    {
        // encrypt
        Cipher c;
        try
        {
            c = m_key.getCipher();
            c.init(Cipher.ENCRYPT_MODE, m_key.getKey());

            byte[] enc = c.doFinal(bytes_p);
            return enc;
        }
        catch (NoSuchAlgorithmException e)
        {
            LOG.error("Encrypt failed", e);
            throw new OwInvalidOperationException("Encrypt failed", e);
        }
        catch (NoSuchPaddingException e)
        {
            LOG.error("Encrypt failed", e);
            throw new OwInvalidOperationException("Encrypt failed", e);
        }
        catch (InvalidKeyException e)
        {
            LOG.error("Encrypt failed", e);
            throw new OwInvalidOperationException("Encrypt failed", e);
        }
        catch (IllegalStateException e)
        {
            LOG.error("Encrypt failed", e);
            throw new OwInvalidOperationException("Encrypt failed", e);
        }
        catch (IllegalBlockSizeException e)
        {
            LOG.error("Encrypt failed", e);
            throw new OwInvalidOperationException("Encrypt failed", e);
        }
        catch (BadPaddingException e)
        {
            LOG.error("Encrypt failed", e);
            throw new OwInvalidOperationException("Encrypt failed", e);
        }
    }

    /** decryt the given byte array
     * 
     * @param encrypted_p
     * @param format_p
     * @return a {@link String}
     * @throws UnsupportedEncodingException 
     * @throws OwInvalidOperationException
     */
    public static String decryptToString(byte[] encrypted_p, String format_p) throws OwInvalidOperationException, UnsupportedEncodingException
    {
        byte[] dec = decrypt(encrypted_p);
        return new String(dec, format_p);
    }

    /** decryt the given byte array to string using "ISO-8859-1" format
     * 
     * @param encrypted_p
     * @return a {@link String}
     * @throws OwInvalidOperationException
     */
    public static String decryptToString(byte[] encrypted_p) throws OwInvalidOperationException
    {
        try
        {
            return decryptToString(encrypted_p, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            String msg = "Decrypt failed";
            LOG.error(msg, e);
            throw new OwInvalidOperationException(msg, e);
        }
    }

    /** decrypt the given byte array
     * 
     * @param encrypted_p
     * @return the decrypted byte array
     * @throws OwInvalidOperationException 
     */
    public static byte[] decrypt(byte[] encrypted_p) throws OwInvalidOperationException
    {
        // decrypt
        Cipher c;
        try
        {
            c = m_key.getCipher();
            c.init(Cipher.DECRYPT_MODE, m_key.getKey());

            byte[] dec = c.doFinal(encrypted_p);
            return dec;
        }
        catch (NoSuchAlgorithmException e)
        {
            LOG.error("Decrypt failed", e);
            throw new OwInvalidOperationException("Decrypt failed", e);
        }
        catch (NoSuchPaddingException e)
        {
            LOG.error("Decrypt failed", e);
            throw new OwInvalidOperationException("Decrypt failed", e);
        }
        catch (InvalidKeyException e)
        {
            LOG.error("Decrypt failed", e);
            throw new OwInvalidOperationException("Decrypt failed", e);
        }
        catch (IllegalStateException e)
        {
            LOG.error("Decrypt failed", e);
            throw new OwInvalidOperationException("Decrypt failed", e);
        }
        catch (IllegalBlockSizeException e)
        {
            LOG.error("Decrypt failed", e);
            throw new OwInvalidOperationException("Decrypt failed", e);
        }
        catch (BadPaddingException e)
        {
            LOG.error("Decrypt failed", e);
            throw new OwInvalidOperationException("Decrypt failed", e);
        }
    }
}