package com.wewebu.ow.client.zidilauncher;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *<p>
 * Encoder class to deliver filenames to MS Windows via WebDAV and decode them in reverse.<br/>
 * 
 * MS Windows rules for a valid filename or directory name.<br/>
 * Part 1 - illegal characters:<br/>
 * - &lt; (less than)<br/>
 * - &gt; (greater than)<br/>
 * - : (colon)<br/>
 * - &quot; (double quote)<br/>
 * - / (forward slash)<br/>
 * - \ (backslash)<br/>
 * - | (vertical bar or pipe)<br/>
 * - ? (question mark)<br/>
 * - * (asterisk)<br/>
 * - ASCII values 0 up to 31<br/>
 * <br/>
 * \ / : * ? " < > | # { } % ~ &<br/>
 * <br/>
 * Part 2 - naming conventions:<br/>
 * - A directory or filename must not end with a space or period<br/>
 * <br/>
 * Part 3 - illegal names at all:<br/>
 * <br/>
 * The following names are reserved in MS Windows:<br/>
 * - CON<br/>
 * - PRN<br/>
 * - AUX<br/>
 * - NUL<br/>
 * - NULL<br/>
 * - COM1<br/>
 * - COM2<br/>
 * - COM3<br/>
 * - COM4<br/>
 * - COM5<br/>
 * - COM6<br/>
 * - COM7<br/>
 * - COM8<br/>
 * - COM9<br/>
 * - LPT1<br/>
 * - LPT2<br/>
 * - LPT3<br/>
 * - LPT4<br/>
 * - LPT5<br/>
 * - LPT6<br/>
 * - LPT7<br/>
 * - LPT8<br/>
 * - LPT9<br/>
 * These names are forbidden names for directory and filenames. For filenames also if followed by any extension!
 * e.g. "NULL.txt" is a forbidden filename in MS Windows.
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
public class ZioiDafFileNameEncoder
{

    protected static LinkedHashMap<String, String> m_hashMapEncodings;
    protected static LinkedHashMap<String, String> m_hashMapDecodings;
    protected static String m_EscapeString;
    protected static List<String> m_IllegalFileNames;

    /**
     * Static constructor: creates the hash maps with the strings to encode / decode.
     */
    static
    {
        m_EscapeString = "$$";

        // always create the list with illegal filenames first!
        createIllegalList();

        // now create the encoding & decoding map
        createEncodingMap();
        createDecodingMap();
    }

    /**
     * Returns an escaped path with no more illegal characters and substrings.
     * 
     * @param sPath_p
     *            Path to encode / escape
     * 
     * @param pathDelimeter_p
     *            The delimiter character - either '/' or '\\'
     * 
     * @return an escaped path with no more illegal characters and substrings.
     */
    public static String encodePath(String sPath_p, char pathDelimeter_p)
    {
        if (null == sPath_p)
        {
            return null;
        }

        if (sPath_p.length() < 1)
        {
            return "";
        }

        // escape each path part
        StringBuffer tmpStrBuffer = new StringBuffer();
        String[] arrPath = sPath_p.split("" + pathDelimeter_p);
        int count = arrPath.length - 1;
        for (int i = 0; i <= count; i++)
        {
            String sPart = encode(arrPath[i]);
            tmpStrBuffer.append(sPart);
            if (i > 0 && i < count)
            {
                tmpStrBuffer.append(pathDelimeter_p);
            }
        }
        if (sPath_p.endsWith("" + pathDelimeter_p))
        {
            tmpStrBuffer.append(pathDelimeter_p);
        }

        // special case strings starts with \\ or //
        if (sPath_p.startsWith("" + pathDelimeter_p + pathDelimeter_p))
        {
            return pathDelimeter_p + pathDelimeter_p + tmpStrBuffer.toString();
        }
        // special case strings starts with \ or /
        if (sPath_p.startsWith("" + pathDelimeter_p))
        {
            return pathDelimeter_p + tmpStrBuffer.toString();
        }

        // return escaped string
        return tmpStrBuffer.toString();
    }

    /**
     * Returns a path with no more escaped parts.
     * 
     * @param sPath_p
     *            Path to decode / remove escaped parts
     * 
     * @param pathDelimeter_p
     *            The delimiter character - either '/' or '\\'
     * 
     * @return a path with no more escaped parts.
     */
    public static String decodePath(String sPath_p, char pathDelimeter_p)
    {
        if (null == sPath_p)
        {
            return null;
        }

        if (sPath_p.length() < 1)
        {
            return "";
        }

        // escape each path part
        StringBuffer tmpStrBuffer = new StringBuffer();
        String[] arrPath = sPath_p.split("" + pathDelimeter_p);
        int count = arrPath.length - 1;
        for (int i = 0; i <= count; i++)
        {
            String sPart = decode(arrPath[i]);
            tmpStrBuffer.append(sPart);
            if (i > 0 && i < count && sPart.length() > 0)
            {
                tmpStrBuffer.append(pathDelimeter_p);
            }
        }
        if (sPath_p.endsWith("" + pathDelimeter_p))
        {
            tmpStrBuffer.append(pathDelimeter_p);
        }

        // special case strings starts with \\ or //
        if (sPath_p.startsWith("" + pathDelimeter_p + pathDelimeter_p))
        {
            return pathDelimeter_p + pathDelimeter_p + tmpStrBuffer.toString();
        }
        // special case strings starts with \ or /
        if (sPath_p.startsWith("" + pathDelimeter_p))
        {
            return pathDelimeter_p + tmpStrBuffer.toString();
        }

        // return escaped string
        return tmpStrBuffer.toString();
    }

    /**
     * Returns a filename not containing illegal characters for MS Windows.
     * 
     * @param fileName_p
     *            Filename (or directory name) to encode, may NOT contain a path
     * 
     * @return a filename not containing illegal characters for MS Windows.
     */
    public static String encode(String fileName_p)
    {
        if (null == fileName_p || fileName_p.length() < 1)
        {
            return "";
        }

        // long start_renderRows = System.currentTimeMillis();

        // create return value
        String returnFileName = fileName_p;

        // avoid double encodings!
        if (containsEscapings(fileName_p))
        {
            returnFileName = decode(fileName_p);
        }

        // replace each substring to encode
        Iterator<Entry<String, String>> itEncode = m_hashMapEncodings.entrySet().iterator();
        while (itEncode.hasNext())
        {
            Map.Entry<String, String> sKey = itEncode.next();

            returnFileName = replace(returnFileName, sKey.getKey(), sKey.getValue());
        }

        // check illegal filenames - return "$$" and appended original filename
        Iterator<String> itIllegal = m_IllegalFileNames.iterator();
        while (itIllegal.hasNext())
        {
            String sIllegal = itIllegal.next();
            String sIllegalDot = sIllegal + ".";
            if ((returnFileName.startsWith(sIllegal) && returnFileName.length() == sIllegal.length()) || returnFileName.startsWith(sIllegalDot))
            {
                returnFileName = m_EscapeString + returnFileName;
            }
        }

        // cut ending dot
        if (returnFileName.endsWith("."))
        {
            returnFileName = returnFileName.substring(0, returnFileName.length()) + m_EscapeString + "2E";
        }

        // cut ending space
        if (returnFileName.endsWith(" "))
        {
            returnFileName = returnFileName.substring(0, returnFileName.length()) + m_EscapeString + "20";
        }

        // cut first space
        if (returnFileName.startsWith(" "))
        {
            returnFileName = m_EscapeString + "20" + returnFileName.substring(0, returnFileName.length());
        }

        // Debugging code to find out how long this took
        // if (m_Logger.isDebugEnabled())
        // {
        // long end_renderRows = System.currentTimeMillis();
        // long duration_renderRows = end_renderRows - start_renderRows;
        // m_Logger.debug("ZioiDafFileNameEncoder.encode() took " + duration_renderRows + " ms.");
        // }
        return returnFileName;
    }

    /**
     * Returns the filename as it was in the ECM system before escaping illegal characters.
     * 
     * @param fileName_p
     *            Filename to decode.
     * 
     * @return the filename as it was in the ECM system before escaping illegal characters
     */
    public static String decode(String fileName_p)
    {
        if (null == fileName_p || fileName_p.length() < 1)
        {
            return "";
        }

        // check if the string is encoded at all! (Note: this will also create the maps if the aren't initialized)
        if (!containsEscapings(fileName_p))
        {
            return fileName_p;
        }

        // Create return value
        String returnFileName = fileName_p;

        // replace each substring to encode
        Iterator<Entry<String, String>> it = m_hashMapDecodings.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, String> sKey = it.next();
            returnFileName = replace(returnFileName, sKey.getKey(), sKey.getValue());
        }

        return returnFileName;
    }

    /**
     * Returns a list with filenames not containing illegal characters for MS Windows.
     * 
     * @param fileNames_p
     *            Filenames (or directory names) to encode, may NOT contain a path
     * 
     * @return a list with filenames not containing illegal characters for MS Windows
     */
    public static List<String> encode(List<String> fileNames_p)
    {
        if (null == fileNames_p)
        {
            return null;
        }

        List<String> retList = new LinkedList<String>();
        Iterator<String> it = fileNames_p.iterator();
        while (it.hasNext())
        {
            retList.add(encode(it.next()));
        }

        return retList;
    }

    /**
     * Returns a list with filenames as they were in the ECM system before escaping illegal characters.
     * 
     * @param fileNames_p
     *            Filenames to decode, each filename may also contain a path
     * 
     * @return a list with filenames as they were in the ECM system before escaping illegal characters
     */
    public static List<String> decode(List<String> fileNames_p)
    {
        if (null == fileNames_p)
        {
            return null;
        }

        List<String> retList = new LinkedList<String>();
        Iterator<String> it = fileNames_p.iterator();
        while (it.hasNext())
        {
            retList.add(decode(it.next()));
        }

        return retList;
    }

    /**
     * Returns if the current name is escaped not to contain any Windows illegal characters.
     * 
     * @param filename_p
     *            Filename to check.
     * 
     * @return if the current name is escaped not to contain any Windows illegal characters
     */
    public static boolean containsEscapings(String filename_p)
    {
        if (filename_p.indexOf(m_EscapeString) > 0)
        {
            return true;
        }

        return false;
    }

    /**
     * Creates the list with the illegal filenames
     */
    protected static void createIllegalList()
    {
        m_IllegalFileNames = new LinkedList<String>();
        m_IllegalFileNames.add("CON");
        m_IllegalFileNames.add("PRN");
        m_IllegalFileNames.add("AUX");
        m_IllegalFileNames.add("NUL");
        m_IllegalFileNames.add("COM1");
        m_IllegalFileNames.add("COM2");
        m_IllegalFileNames.add("COM3");
        m_IllegalFileNames.add("COM4");
        m_IllegalFileNames.add("COM5");
        m_IllegalFileNames.add("COM6");
        m_IllegalFileNames.add("COM7");
        m_IllegalFileNames.add("COM8");
        m_IllegalFileNames.add("COM9");
        m_IllegalFileNames.add("LPT1");
        m_IllegalFileNames.add("LPT2");
        m_IllegalFileNames.add("LPT3");
        m_IllegalFileNames.add("LPT4");
        m_IllegalFileNames.add("LPT5");
        m_IllegalFileNames.add("LPT6");
        m_IllegalFileNames.add("LPT7");
        m_IllegalFileNames.add("LPT8");
        m_IllegalFileNames.add("LPT9");
    }

    /**
     * Create the map with the names to decode
     */
    protected static void createDecodingMap()
    {
        // create map
        m_hashMapDecodings = new LinkedHashMap<String, String>();

        // add illegal filenames
        Iterator<String> itIllegal = m_IllegalFileNames.iterator();
        while (itIllegal.hasNext())
        {
            String illegalName = itIllegal.next();
            m_hashMapDecodings.put("$$" + illegalName, illegalName);
        }

        // add replacement strings
        m_hashMapDecodings.put("$$00", "\u0000");
        m_hashMapDecodings.put("$$01", "\u0001");
        m_hashMapDecodings.put("$$02", "\u0002");
        m_hashMapDecodings.put("$$03", "\u0003");
        m_hashMapDecodings.put("$$04", "\u0004");
        m_hashMapDecodings.put("$$05", "\u0005");
        m_hashMapDecodings.put("$$06", "\u0006");
        m_hashMapDecodings.put("$$07", "\u0007");
        m_hashMapDecodings.put("$$08", "\u0008");
        m_hashMapDecodings.put("$$09", "\t");
        m_hashMapDecodings.put("$$0A", "\n");
        m_hashMapDecodings.put("$$0B", "\u000b");
        m_hashMapDecodings.put("$$0C", "\u000c");
        m_hashMapDecodings.put("$$0D", "\r");
        m_hashMapDecodings.put("$$0E", "\u000e");
        m_hashMapDecodings.put("$$0F", "\u000f");
        m_hashMapDecodings.put("$$10", "\u0010");
        m_hashMapDecodings.put("$$11", "\u0011");
        m_hashMapDecodings.put("$$12", "\u0012");
        m_hashMapDecodings.put("$$13", "\u0013");
        m_hashMapDecodings.put("$$14", "\u0014");
        m_hashMapDecodings.put("$$15", "\u0015");
        m_hashMapDecodings.put("$$16", "\u0016");
        m_hashMapDecodings.put("$$17", "\u0017");
        m_hashMapDecodings.put("$$18", "\u0018");
        m_hashMapDecodings.put("$$19", "\u0019");
        m_hashMapDecodings.put("$$1A", "\u001a");
        m_hashMapDecodings.put("$$1B", "\u001b");
        m_hashMapDecodings.put("$$1C", "\u001c");
        m_hashMapDecodings.put("$$1D", "\u001d");
        m_hashMapDecodings.put("$$1E", "\u001e");
        m_hashMapDecodings.put("$$1F", "\u001f");
        m_hashMapDecodings.put("$$20", " ");
        m_hashMapDecodings.put("$$22", "\"");
        m_hashMapDecodings.put("$$26", "&");
        m_hashMapDecodings.put("$$27", "\'");
        m_hashMapDecodings.put("$$2A", "*");
        m_hashMapDecodings.put("$$2E", ".");
        m_hashMapDecodings.put("$$2F", "/");
        m_hashMapDecodings.put("$$3A", ":");
        m_hashMapDecodings.put("$$3B", ";");
        m_hashMapDecodings.put("$$3C", "<");
        m_hashMapDecodings.put("$$3E", ">");
        m_hashMapDecodings.put("$$3F", "?");
        m_hashMapDecodings.put("$$5C", "\\");
        m_hashMapDecodings.put("$$7C", "|");
        m_hashMapDecodings.put("$$23", "#");
        m_hashMapDecodings.put("$$7B", "{");
        m_hashMapDecodings.put("$$7D", "}");
        m_hashMapDecodings.put("$$25", "%");
        m_hashMapDecodings.put("$$7E", "~");

        // this MUST always be the last value!!
        m_hashMapDecodings.put("$$", "$$FF");
    }

    /**
     * Creates the encoding map which characters or substrings to replace
     */
    protected static void createEncodingMap()
    {
        // create map
        m_hashMapEncodings = new LinkedHashMap<String, String>();

        // add replacement strings
        m_hashMapEncodings.put("$$", "$$FF");
        m_hashMapEncodings.put("\u0000", "$$00");
        m_hashMapEncodings.put("\u0001", "$$01");
        m_hashMapEncodings.put("\u0002", "$$02");
        m_hashMapEncodings.put("\u0003", "$$03");
        m_hashMapEncodings.put("\u0004", "$$04");
        m_hashMapEncodings.put("\u0005", "$$05");
        m_hashMapEncodings.put("\u0006", "$$06");
        m_hashMapEncodings.put("\u0007", "$$07");
        m_hashMapEncodings.put("\u0008", "$$08");
        m_hashMapEncodings.put("\t", "$$09");
        m_hashMapEncodings.put("\n", "$$0A");
        m_hashMapEncodings.put("\u000b", "$$0B");
        m_hashMapEncodings.put("\u000c", "$$0C");
        m_hashMapEncodings.put("\r", "$$0D");
        m_hashMapEncodings.put("\u000e", "$$0E");
        m_hashMapEncodings.put("\u000f", "$$0F");
        m_hashMapEncodings.put("\u0010", "$$10");
        m_hashMapEncodings.put("\u0011", "$$11");
        m_hashMapEncodings.put("\u0012", "$$12");
        m_hashMapEncodings.put("\u0013", "$$13");
        m_hashMapEncodings.put("\u0014", "$$14");
        m_hashMapEncodings.put("\u0015", "$$15");
        m_hashMapEncodings.put("\u0016", "$$16");
        m_hashMapEncodings.put("\u0017", "$$17");
        m_hashMapEncodings.put("\u0018", "$$18");
        m_hashMapEncodings.put("\u0019", "$$19");
        m_hashMapEncodings.put("\u001a", "$$1A");
        m_hashMapEncodings.put("\u001b", "$$1B");
        m_hashMapEncodings.put("\u001c", "$$1C");
        m_hashMapEncodings.put("\u001d", "$$1D");
        m_hashMapEncodings.put("\u001e", "$$1E");
        m_hashMapEncodings.put("\u001f", "$$1F");
        // m_hashMapEncodings.put(" ", "$$20"); don't escape, but undo escaping to reconstruct leading or ending spaces!
        m_hashMapEncodings.put("\"", "$$22");
        m_hashMapEncodings.put("&", "$$26");
        //m_hashMapEncodings.put("\'", "$$27");
        m_hashMapEncodings.put("*", "$$2A");
        // m_hashMapEncodings.put(".", "$$2E"); don't escape, but undo escaping to reconstruct ending periods!
        m_hashMapEncodings.put("/", "$$2F");
        m_hashMapEncodings.put(":", "$$3A");
        m_hashMapEncodings.put(";", "$$3B");
        m_hashMapEncodings.put("<", "$$3C");
        m_hashMapEncodings.put(">", "$$3E");
        m_hashMapEncodings.put("?", "$$3F");
        m_hashMapEncodings.put("\\", "$$5C");
        m_hashMapEncodings.put("|", "$$7C");
        m_hashMapEncodings.put("#", "$$23");
        m_hashMapEncodings.put("{", "$$7B");
        m_hashMapEncodings.put("}", "$$7D");
        m_hashMapEncodings.put("%", "$$25");
        m_hashMapEncodings.put("~", "$$7E");
    }

    /**
     * Returns the text where each oldStr_p was replaced by newStr_p.
     * 
     * @param text_p
     *            Text which may contain oldStr_p to replace by newStr_p
     * 
     * @param oldStr_p
     *            Substring to find and replace in text_p
     * 
     * @param newStr_p
     *            Substring to replace oldStr_p with
     * 
     * @return the text where each oldStr_p was replaced by newStr_p.
     */
    public static String replace(String text_p, String oldStr_p, String newStr_p)
    {
        // spare time?
        if (null == text_p || null == oldStr_p)
        {
            return text_p;
        }

        // make sure the new strings doesn't contain null
        if (null == newStr_p)
        {
            newStr_p = "";
        }

        int oldLen = oldStr_p.length();
        int start = 0;
        int end = text_p.indexOf(oldStr_p);
        StringBuffer tmpStrBuffer = new StringBuffer();

        // replace until nothing else found
        while (end >= 0)
        {
            tmpStrBuffer.append(text_p.substring(start, end));
            tmpStrBuffer.append(newStr_p);
            start = end + oldLen;
            end = text_p.indexOf(oldStr_p, end + oldLen);
        }

        // append the rest of the old string
        tmpStrBuffer.append(text_p.substring(start));
        return tmpStrBuffer.toString();
    }
}
