package com.wewebu.ow.server.util;

import java.util.Locale;

/**
 *<p>
 * Utility class OwString2. Extends OwString with additional place holder attributes.
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
public class OwString2 extends OwString
{
    private String m_strParam1;
    private String m_strParam2;

    private String m_fullDisplayName = null;

    /** construct a string that can be localized later using getString(Locale locale_p)
    *
    * @param strKeyyName_p String unique localize key for display name
    * @param strDefaultDisplayName_p String default display name
     * @param strAttribute1_p String that replaces %1 tokens
     * @param strAttribute2_p String that replaces %2 tokens
    */
    public OwString2(String strKeyyName_p, String strDefaultDisplayName_p, String strAttribute1_p, String strAttribute2_p)
    {
        super(strKeyyName_p, strDefaultDisplayName_p);

        m_strParam1 = strAttribute1_p;
        m_strParam2 = strAttribute2_p;
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
                m_fullDisplayName = replaceAll(m_fullDisplayName, "%2", m_strParam2);
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
        return localize2(locale_p, m_strKeyName, m_strDefaultDisplayName, m_strParam1, m_strParam2);
    }
}