package com.wewebu.ow.server.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *<p>
 * Utility class for HTML creation.
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
public class OwHTMLHelper
{

    private static final Map<Character, String> JS_UNICODE_ESCAPES = new HashMap<Character, String>();

    private static final Map<Character, String> JS_BS_ESCAPES = new HashMap<Character, String>();

    static
    {
        JS_BS_ESCAPES.put(Character.valueOf('\\'), "\\\\");
        JS_BS_ESCAPES.put(Character.valueOf('\b'), "\\b");
        JS_BS_ESCAPES.put(Character.valueOf('\f'), "\\f");
        JS_BS_ESCAPES.put(Character.valueOf('\n'), "\\n");
        JS_BS_ESCAPES.put(Character.valueOf('\r'), "\\r");
        JS_BS_ESCAPES.put(Character.valueOf('\r'), "\\r");
        JS_BS_ESCAPES.put(Character.valueOf('\t'), "\\t");
        JS_BS_ESCAPES.put(Character.valueOf('"'), "\\\"");
        JS_BS_ESCAPES.put(Character.valueOf('\''), "\\'");
        JS_BS_ESCAPES.put(Character.valueOf('%'), "\\u" + toUnicode('%'));

        Set<Character> characters = JS_BS_ESCAPES.keySet();
        for (Iterator<Character> i = characters.iterator(); i.hasNext();)
        {
            Character c = i.next();
            JS_UNICODE_ESCAPES.put(c, "\\u" + toUnicode(c.charValue()));
        }

    }

    private static String toUnicode(char c_p)
    {
        char[] hexCode = new char[4];
        Arrays.fill(hexCode, '0');
        String hexValue = Integer.toHexString(c_p);
        int hexValueLen = hexValue.length();
        hexValue.getChars(0, hexValueLen, hexCode, 4 - hexValueLen);

        return new String(hexCode);
    }

    /** 
     * convert the given text to a string with HTML tags and special chars for HTML display
     * @param sText_p the text that have to be converted
     * 
     * @return String encoded string
     * 
     * @throws IOException
     */
    public static String encodeToSecureHTML(String sText_p) throws IOException
    {
        StringWriter stringWriter = new StringWriter();
        writeSecureHTML(stringWriter, sText_p, false);
        String secureHTML = stringWriter.toString();
        stringWriter.flush();
        stringWriter.close();
        return secureHTML;
    }

    /** 
     * write the given text and convert HTML tags and special chars
     * @param w_p writer
     * @param sText_p text to be converted
     * 
     * @throws IOException 
     */
    public static void writeSecureHTML(Writer w_p, String sText_p) throws IOException
    {
        writeSecureHTML(w_p, sText_p, false);
    }

    /** 
     * write the given text and convert HTML tags and special chars
     * @param w_p writer
     * @param sText_p text to be converted
     * @param encodeEnter_p flag for also encoding the ENTER char to <BR>
     * 
     * @throws IOException 
     */
    public static void writeSecureHTML(Writer w_p, String sText_p, boolean encodeEnter_p) throws IOException
    {
        if (sText_p != null && w_p != null)
        {
            for (int i = 0; i < sText_p.length(); i++)
            {
                char c = sText_p.charAt(i);
                if (c == '\n' && encodeEnter_p)
                {
                    w_p.write("<br>");
                }
                else
                {
                    switch (c)
                    {
                        case '&':
                            if (i + 1 < sText_p.length() && sText_p.charAt(i + 1) == '#')
                            {//look ahead, it could be an escaped Unicode character
                                w_p.write(c);
                            }
                            else
                            {
                                w_p.write("&amp;");
                            }
                            break;

                        case '"':
                            w_p.write("&quot;");
                            break;

                        case '\'':
                            w_p.write("&#39;");
                            break;

                        case '<':
                            w_p.write("&#60;");
                            break;

                        case '>':
                            w_p.write("&#62;");
                            break;

                        default:
                            w_p.write(c);
                            break;
                    }
                }
            }
        }
    }

    /**
    * Replaces all JavaScript Special characters with their backslash based escape sequence.
    * <br>
    * Some HTML JavaScripting related character sequences are also escaped to avoid 
    * security issues : 
    *  
    * 
    * 1. Any occurrence of the &lt;/script&gt; in the given string will be replaced 
    * by &lt;/script\u003e to avoid HTML parse errors.<br>
    * 
    * 2. Any occurrence of the <b>%</b> character is escaped using its unicode escape 
    * sequence to avoid HTML anchor-href escape interpretation script issues  
    * 
    * @param in_p String input to be encoded
    * 
    * @return String encoded string
    * */
    public static String encodeJavascriptString(String in_p)
    {
        return encodeJavascriptString(in_p, false);
    }

    /**
     * Replace illegal '-' char with '_' to obtain a legal JavaScript variable name.
     * @param input_p - the input string
     * @return - the modified string.
     * @since 3.1.0.0
     */
    public static String encodeJavascriptVariableName(String input_p)
    {
        String partialResult = encodeJavascriptString(input_p);
        return partialResult.replace('-', '_');
    }

    /** 
     * Replaces all JavaScript Special characters with their escape sequence.<br>
     * Depending on the value of the useUnicodeEscapes_p parameter unicode escape 
     * sequences or backslash escape sequences can be used.  
     * 
     * <br>
     * Some HTML JavaScripting related character sequences are also escaped to avoid 
     * security issues : 
     *  
     * 
     * 1. Any occurrence of the &lt;/script&gt; in the given string will be replaced 
     * by &lt;/script\u003e to avoid HTML parse errors.<br>
     * 
     * 2. Any occurrence of the <b>%</b> character is escaped using its unicode escape 
     * sequence to avoid HTML anchor-href escape interpretation script issues  
     * 
     * @param in_p String input to be encoded
     * @param useUnicodeEscapes_p if <code>true</code> special characters are using only 
     *        unicode escape sequences
     *         
     * @return String encoded string
     * @since 3.1.0.0
     * */
    public static String encodeJavascriptString(String in_p, boolean useUnicodeEscapes_p)
    {
        // null remains null
        if (null == in_p)
        {
            return (null);
        }
        Map<Character, String> escapes = useUnicodeEscapes_p ? JS_UNICODE_ESCAPES : JS_BS_ESCAPES;
        final String scriptEndTag = "</script>";
        StringBuffer sb = new StringBuffer();
        int scriptEndTagMatch = 0;
        for (int i = 0; i < in_p.length(); i++)
        {
            char c = in_p.charAt(i);
            char lowerC = Character.toLowerCase(c);
            if (lowerC == scriptEndTag.charAt(scriptEndTagMatch))
            {
                scriptEndTagMatch++;
                if (scriptEndTagMatch == scriptEndTag.length())
                {
                    sb.append("\\u" + toUnicode(c));
                    scriptEndTagMatch = 0;
                }
                else
                {
                    sb.append(c);
                }
            }
            else
            {
                scriptEndTagMatch = 0;

                String escape = escapes.get(Character.valueOf(c));

                if (escape != null)
                {
                    sb.append(escape);
                }
                else
                {
                    sb.append(c);
                }
            }

        }
        return (sb.toString());
    }
}