package com.wewebu.ow.server.util;

import java.util.Locale;

/**
 *<p>
 * Utility class OwString1. Extends OwString with additional placeholder attributes.
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
public class OwString1 extends OwString
{
    private String m_strParam1;

    private String m_fullDisplayName = null;

    /** construct a string that can be localized later using getString(Locale locale_p)
    *
    * @param strKeyyName_p String unique localize key for display name
    * @param strDefaultDisplayName_p String default display name
     * @param strAttribute1_p String that replaces %1 tokens
    */
    public OwString1(String strKeyyName_p, String strDefaultDisplayName_p, String strAttribute1_p)
    {
        super(strKeyyName_p, strDefaultDisplayName_p);

        m_strParam1 = strAttribute1_p;
    }

    /** construct a string that can be localized later using getString1(Locale locale_p,String strAttribute1_p)
    *
    * @param strKeyyName_p String unique localize key for display name
    * @param strDefaultDisplayName_p String default display name
    */
    public OwString1(String strKeyyName_p, String strDefaultDisplayName_p)
    {
        super(strKeyyName_p, strDefaultDisplayName_p);
    }

    /** get the default displayname */
    public String getDefaultDisplayName()
    {
        if (m_fullDisplayName == null)
        {
            Locale locale = new Locale("en");
            m_fullDisplayName = getString(locale);
            if (m_fullDisplayName == null || m_fullDisplayName.equals(""))
            {
                locale = new Locale("de");
                m_fullDisplayName = getString(locale);
            }
            if (m_fullDisplayName == null || m_fullDisplayName.equals(""))
            {
                //m_fullDisplayName = m_strDefaultDisplayName;
                m_fullDisplayName = replaceAll(m_strDefaultDisplayName, "%1", m_strParam1);
            }
        }
        return m_fullDisplayName;
    }

    /** localize this String
    *
    * @param locale_p Locale to use
    *
    * @return localized String
    */
    public String getString(Locale locale_p)
    {
        return localize1(locale_p, m_strKeyName, m_strDefaultDisplayName, m_strParam1);
    }

    /** localize this String
    *
    * @param locale_p Locale to use
    *
    * @return localized String
    */
    public String getString1(Locale locale_p, String strAttribute1_p)
    {
        return localize1(locale_p, m_strKeyName, m_strDefaultDisplayName, strAttribute1_p);
    }
}