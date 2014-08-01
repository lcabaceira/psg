package com.wewebu.ow.server.util.paramcodec;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Encodes a long value in a numeric base given by the length of given alphabet.
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
 *@since 3.0.0.0
 */
public class OwAlphabetCoder
{
    /**Default alphabet contains the following characters :  a-z, A-Z */
    private static final char[] DEFAULT_ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    private char[] m_alphabet = DEFAULT_ALPHABET;

    /**
     * Constructor
     * The {@link #DEFAULT_ALPHABET} will be used
     */
    public OwAlphabetCoder()
    {
        this(DEFAULT_ALPHABET);
    }

    /**
     * Constructor
     * @param alphabet_p char array alphabet 
     */
    public OwAlphabetCoder(char[] alphabet_p)
    {
        super();
        this.m_alphabet = alphabet_p;
    }

    /**
     * Encodes the given long in a numeric base given by the 
     * length of the alphabet.
     * 
     * @param aLong_p a positive long value
     * @return the encoded String value
     * @throws OwInvalidOperationException if the given long value is negative 
     */
    public String encode(long aLong_p) throws OwInvalidOperationException
    {
        if (aLong_p < 0)
        {
            throw new OwInvalidOperationException("Negative values can not be encoded : " + aLong_p);
        }
        long nameIndex = Math.abs(aLong_p);
        StringBuffer newNameBuffer = new StringBuffer();
        while (nameIndex >= m_alphabet.length)
        {
            int alphaMod = (int) (nameIndex % m_alphabet.length);
            newNameBuffer.append(m_alphabet[alphaMod]);
            nameIndex = nameIndex / m_alphabet.length;
        }
        newNameBuffer.append(m_alphabet[(int) nameIndex]);

        return newNameBuffer.toString();
    }

}