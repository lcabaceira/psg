package com.wewebu.ow.server.ecm;

/**
 *<p>
 * OwPath value for search templates. Used to specify a resource path to search in.
 * @see OwSearchPathField
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
public class OwPath
{
    public OwPath(String strPath_p)
    {
        m_strPath = strPath_p;
        m_fEnabled = true;
        m_strDisplayName = strPath_p;
    }

    public OwPath(String strPath_p, boolean fEnabled_p)
    {
        m_strPath = strPath_p;
        m_fEnabled = fEnabled_p;
        m_strDisplayName = strPath_p;
    }

    public OwPath(String strPath_p, String strDisplayName_p, boolean fEnabled_p)
    {
        m_strPath = strPath_p;
        m_fEnabled = fEnabled_p;
        m_strDisplayName = strDisplayName_p;
    }

    private String m_strPath;
    private String m_strDisplayName;
    private boolean m_fEnabled;

    public String getPath()
    {
        return m_strPath;
    }

    public String getDisplayName(java.util.Locale locale_p)
    {
        return m_strDisplayName;
    }

    public boolean getEnabled()
    {
        return m_fEnabled;
    }

    public String toString()
    {
        return m_strPath;
    }

}