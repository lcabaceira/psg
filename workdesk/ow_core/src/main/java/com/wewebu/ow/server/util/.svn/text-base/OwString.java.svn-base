package com.wewebu.ow.server.util;

import java.util.Locale;
import java.util.Map;

/**
 *<p>
 * Utility class OwString used to localize strings.
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
public class OwString
{
    //    /** package logger for the class */
    //    private static final Logger LOG = OwLogCore.getLogger(OwString.class);

    /** singleton for the properties map */
    private static OwStringProperties m_properties = OwStringProperties.getInstance();

    /** key for localization */
    protected String m_strKeyName;
    /** default displayname if key was not found*/
    protected String m_strDefaultDisplayName;

    /** get the key
     */
    public String getKey()
    {
        return m_strKeyName;
    }

    /** get the default displayname
     */
    public String getDefaultDisplayName()
    {
        return m_strDefaultDisplayName;
    }

    /** construct a string that can be localized later using getString(Locale locale_p)
     *
     * @param strKeyyName_p String unique localize key for display name
     * @param strDefaultDisplayName_p String default display name
     */
    public OwString(String strKeyyName_p, String strDefaultDisplayName_p)
    {
        m_strKeyName = strKeyyName_p;
        m_strDefaultDisplayName = strDefaultDisplayName_p;
    }

    /** construct a string that can be localized later using getString(Locale locale_p)
     *
     * @param strLabelOrKey_p String (like a symbolic name) or a unique key
     * @param strDefaultDisplayName_p String default display name
     * @param fTreatAsLabel_p boolean true = strLabelOrKey_p is a label, false = strLabelOrKey_p is a unique key
     */
    public OwString(String strLabelOrKey_p, String strDefaultDisplayName_p, boolean fTreatAsLabel_p)
    {
        if (fTreatAsLabel_p)
        {
            m_strKeyName = LABEL_PREFIX + strLabelOrKey_p;
        }
        else
        {
            m_strKeyName = strLabelOrKey_p;
        }

        m_strDefaultDisplayName = strDefaultDisplayName_p;
    }

    public String toString()
    {
        return m_strKeyName;
    }

    /** construct a label (symbolic name or metadata) that can be localized later using getString(Locale locale_p)
     *
     * @param strLabel_p String label (like a symbolic name)
     */
    public OwString(String strLabel_p)
    {
        m_strKeyName = LABEL_PREFIX + strLabel_p;
        m_strDefaultDisplayName = strLabel_p;
    }

    /** localize this String
     *
     * @param locale_p Locale to use
     *
     * @return localized String
     */
    public String getString(Locale locale_p)
    {
        return localize(locale_p, m_strKeyName, m_strDefaultDisplayName);
    }

    // === static methods    
    /** replaces all occurrences of strPattern_p with strReplacement_p
     * @param strIn_p StringBuffer to replace 
     * @param strPattern_p String to look for
     * @param strReplacement_p replacement string
     */
    public static void replaceAll(StringBuffer strIn_p, String strPattern_p, String strReplacement_p)
    {
        int iIndex = 0;
        while (-1 != (iIndex = strIn_p.indexOf(strPattern_p, iIndex)))
        {
            strIn_p.replace(iIndex, iIndex + strPattern_p.length(), strReplacement_p);

            iIndex += strReplacement_p.length();
        }
    }

    /**
     * Replace all occurrences of the provided pattern with the replacement String.
     * @param strIn_p StringBuilder
     * @param pattern_p String pattern to replace
     * @param replacement_p String replacement for pattern
     * @since 3.2.0.0
     */
    public static void replaceAll(StringBuilder strIn_p, String pattern_p, String replacement_p)
    {
        int idx = 0;
        while ((idx = strIn_p.indexOf(pattern_p, idx)) != -1)
        {
            strIn_p.replace(idx, idx + pattern_p.length(), replacement_p);

            idx += replacement_p.length();
        }
    }

    /** replaces all occurrences of strPattern_p with strReplaceMent_p
     * 
     *  NOTE:   This function might be replaced by String.replaceAll(...) function,
     *          which is provided in JDK 1.4.
     *          For JDK 1.3 compliance we need to write our own replace function
     *
     * @param strIn_p String to replace 
     * @param strPattern_p String to look for
     * @param strReplacement_p replacement string
     * @return the new replaced string
     */
    public static String replaceAll(String strIn_p, String strPattern_p, String strReplacement_p)
    {
        int iIndex = strIn_p.indexOf(strPattern_p);
        if (iIndex == -1)
        {
            return strIn_p;
        }

        StringBuffer strRet = new StringBuffer();

        int iOldIndex = 0;
        while (-1 != iIndex)
        {
            strRet.append(strIn_p.substring(iOldIndex, iIndex));
            strRet.append(strReplacement_p);

            iIndex += strPattern_p.length();

            iOldIndex = iIndex;
            iIndex = strIn_p.indexOf(strPattern_p, iIndex);
        }

        if (iOldIndex <= strIn_p.length())
        {
            strRet.append(strIn_p.substring(iOldIndex, strIn_p.length()));
        }

        return strRet.toString();
    }

    /** prefix for labels */
    public static final String LABEL_PREFIX = "owlabel.";

    /** localizes a label (symbolic name or metadata)
     *
     * @param local_p Locale to use
     * @param strLabel_p label that should be localized
     *
     * @return String localized String
     */
    public static String localizeLabel(Locale local_p, String strLabel_p)
    {
        return localize(local_p, LABEL_PREFIX + strLabel_p, strLabel_p);
    }

    /** check if a label (symbolic name or metadata) is defined
     *
     * @param local_p Locale to use
     * @param strLabel_p label that should be localized
     *
     * @return String localized String
     */
    public static boolean hasLabel(Locale local_p, String strLabel_p)
    {
        try
        {
            return m_properties.getProperties(local_p).containsKey(LABEL_PREFIX + strLabel_p);
        }
        catch (OwDebugModeException edebug)
        {
            return false;
        }
    }

    /** localizes a string
     *
     * @param local_p Locale to use
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     *
     * @return String localized strText_p
     */
    public static String localize(Locale local_p, String strKey_p, String strText_p)
    {
        try
        {
            return m_properties.getProperties(local_p).get(strKey_p).toString();
        }
        catch (NullPointerException e)
        {
            // not found
            return strText_p;
        }
        catch (OwDebugModeException edebug)
        {
            return "[" + strKey_p + "]";
        }
    }

    /** localizes a label (symbolic name or metadata)
    *
    * @param local_p Locale to use
    * @param strLabel_p label that should be localized
    * @param strDefault_p default if label does not exist
    *
    * @return String localized String
    */
    public static String localizeLabel(Locale local_p, String strLabel_p, String strDefault_p)
    {
        try
        {
            return m_properties.getProperties(local_p).get(LABEL_PREFIX + strLabel_p).toString();
        }
        catch (NullPointerException e)
        {
            // not found
            return strDefault_p;
        }
        catch (OwDebugModeException edebug)
        {
            return "[" + LABEL_PREFIX + strLabel_p + "]";
        }
    }

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param local_p Locale to use
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     *
     * @return String localized strText_p
     */
    public static String localize1(Locale local_p, String strKey_p, String strText_p, String strAttribute1_p)
    {
        StringBuilder text = new StringBuilder(localize(local_p, strKey_p, strText_p));
        //        String strRet = replaceAll(localize(local_p, strKey_p, strText_p), "%1", strAttribute1_p);
        replace(text, strAttribute1_p);
        return text.toString();
    }

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param local_p Locale to use
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     * @param strAttribute2_p String that replaces %2 tokens
     *
     * @return String localized strText_p
     */
    public static String localize2(Locale local_p, String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p)
    {
        StringBuilder text = new StringBuilder(localize(local_p, strKey_p, strText_p));
        replace(text, strAttribute1_p, strAttribute2_p);
        //        replaceAll(ret, "%1", strAttribute1_p);
        //        replaceAll(ret, "%2", strAttribute2_p);

        return text.toString();
    }

    /** localizes a string with additional parameter that is replaced with %1
     *
     * @param local_p Locale to use
     * @param strKey_p Key value used to retrieve localized string from resource
     * @param strText_p current language Text
     * @param strAttribute1_p String that replaces %1 tokens
     * @param strAttribute2_p String that replaces %2 tokens
     * @param strAttribute3_p String that replaces %3 tokens
     *
     * @return String localized strText_p
     */
    public static String localize3(Locale local_p, String strKey_p, String strText_p, String strAttribute1_p, String strAttribute2_p, String strAttribute3_p)
    {
        StringBuilder text = new StringBuilder(localize(local_p, strKey_p, strText_p));
        replace(text, strAttribute1_p, strAttribute2_p, strAttribute3_p);
        //        String strRet = replaceAll(localize(local_p, strKey_p, strText_p), "%1", strAttribute1_p);
        //        strRet = replaceAll(strRet, "%2", strAttribute2_p);
        //        strRet = replaceAll(strRet, "%3", strAttribute3_p);

        return text.toString();
    }

    private static void replace(StringBuilder text, String... replacements)
    {
        for (int i = 0; i < replacements.length; i++)
        {
            replaceAll(text, "%" + (1 + i), replacements[i]);
        }
    }

    /**
     * Adds the given key mapped localization properties to the current text mappings. 
     * 
     * @param locale_p
     * @param properties_p
     * @throws Exception
     * @since 3.1.0.5
     */
    public static synchronized void putAll(Locale locale_p, Map properties_p) throws Exception
    {
        m_properties.putAll(locale_p, properties_p);
    }

    /**
     * Adds the given key mapped label to the current text mappings.
     * The {@link OwString#LABEL_PREFIX} is added to the given key.
     * 
     * @param locale_p
     * @param key_p
     * @param label_p
     * @throws Exception
     * @since 3.1.0.5
     */
    public static synchronized void putLabel(Locale locale_p, String key_p, String label_p) throws Exception
    {
        m_properties.putLabel(locale_p, key_p, label_p);
    }
}