package com.wewebu.ow.server.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *<p>
 * Utility class to get a tokenized string representation, using specific escape characters.
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
public class OwEscapedStringTokenizer
{
    /** the standard delimiter character to be used */
    public static final char STANDARD_DELIMITER = ';';
    /** the standard escape character to be used */
    public static final char STANDARD_ESCAPE = '\\';

    private char m_escape;
    private String m_text;
    private char m_delimiter;
    private int m_pos = 0;

    /**
     * 
     * @param text_p
     * @param delimiter_p
     * @param escapechar_p
     */
    public OwEscapedStringTokenizer(String text_p, char delimiter_p, char escapechar_p)
    {
        m_escape = escapechar_p;
        m_text = text_p;
        m_delimiter = delimiter_p;
    }

    /**
     * 
     * @param text_p
     */
    public OwEscapedStringTokenizer(String text_p)
    {
        m_escape = STANDARD_ESCAPE;
        m_text = text_p;

        if (m_text == null)
        {
            m_text = "";
        }

        m_delimiter = STANDARD_DELIMITER;
    }

    public boolean hasNext()
    {
        return (m_pos < m_text.length());
    }

    private String m_token;

    /** get next token
     * 
     * @return String
     */
    public String next()
    {
        boolean fEscape = false;

        int ipos = m_pos;

        while (ipos < m_text.length())
        {
            char c = m_text.charAt(ipos);
            ipos++;

            if (fEscape)
            {
                fEscape = false;
                continue;
            }

            if (c == m_escape)
            {
                fEscape = true;
                continue;
            }

            if (c == m_delimiter)
            {
                // found token
                m_token = m_text.substring(m_pos, ipos - 1);
                m_token = removeEscape(m_token);

                m_pos = ipos;
                return m_token;
            }
        }

        // last token
        m_token = m_text.substring(m_pos, ipos);
        m_token = removeEscape(m_token);

        m_pos = ipos;

        return m_token;
    }

    /** convert the tokenizer into a collection
     * 
     * @return Collection of tokens
     */
    public Collection<String> toCollection()
    {
        LinkedList<String> ret = new LinkedList<String>();

        while (hasNext())
        {
            ret.add(next());
        }

        return ret;
    }

    private String removeEscape(String text_p)
    {
        StringBuilder result = new StringBuilder();

        boolean fEscape = false;
        for (int i = 0; i < text_p.length(); i++)
        {
            char c = text_p.charAt(i);

            if (fEscape)
            {
                fEscape = false;

                result.append(c);

                continue;
            }

            if (c == m_escape)
            {
                fEscape = true;
                continue;
            }

            result.append(c);
        }

        return result.toString();
    }

    /** create a delimited string out of the given string collection that can later be tokenized
     * 
     * @param tokens_p
     */
    public static String createDelimitedString(Collection<String> tokens_p)
    {
        return createDelimitedString(tokens_p, STANDARD_DELIMITER, STANDARD_ESCAPE);
    }

    /** create a delimited string out of the given string collection that can later be tokenized
     * 
     * @param tokens_p
     * @param delimiter_p
     * @param escape_p
     */
    public static String createDelimitedString(Collection<String> tokens_p, char delimiter_p, char escape_p)
    {
        StringBuilder result = new StringBuilder();

        Iterator<String> it = tokens_p.iterator();
        while (it.hasNext())
        {
            String token = it.next();

            // append escaped token
            appendEscapedToken(result, token, delimiter_p, escape_p);

            // append delimiter between tokens
            if (it.hasNext())
            {
                result.append(delimiter_p);
            }
        }

        return result.toString();
    }

    /** append the given token converting it with given escapes
     * 
     * @param buf_p
     * @param token_p
     * @param delimiter_p
     * @param escape_p
     */
    protected static void appendEscapedToken(Appendable buf_p, String token_p, char delimiter_p, char escape_p)
    {
        if (null == token_p)
        {
            return;
        }

        // append text
        for (int i = 0; i < token_p.length(); i++)
        {
            char c = token_p.charAt(i);
            try
            {
                if ((c == delimiter_p) || (c == escape_p))
                {
                    buf_p.append(escape_p);
                    buf_p.append(c);
                }
                else
                {
                    buf_p.append(c);
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException("Failed to handle appendEscapedToken", e);
            }

        }
    }
}