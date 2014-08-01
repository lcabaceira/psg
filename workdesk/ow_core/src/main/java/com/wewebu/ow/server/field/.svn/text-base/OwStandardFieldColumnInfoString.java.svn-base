package com.wewebu.ow.server.field;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Implements the OwFieldColumnInfo interface using. As opposed to OwStandardFieldColumnInfo, 
 * OwStandardFieldColumnInfoString may be instantiated providing a String type property name parameter.
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
public class OwStandardFieldColumnInfoString implements OwFieldColumnInfo
{

    /** the alignment to use for the column, as defined with OwFieldColumnInfo.ALIGNMENT_... */
    protected int m_iAlignment;

    /** the string to display in the header */
    protected OwString m_displayname;

    /** the object property name this column is referring to */
    protected String m_propertyname;

    /** column width in % or 0 if undefined */
    protected int m_iWidth = 0;

    /** construct and initialize Column info tuple 
     *
     * @param strPropertyName_p String property this column is referring to
     * @param iAlignment_p int alignment to use for the column, as defined with OwFieldColumnInfo.ALIGNMENT_...
     *
     */
    public OwStandardFieldColumnInfoString(String strPropertyName_p, int iAlignment_p)
    {
        m_propertyname = strPropertyName_p;
        m_iAlignment = iAlignment_p;
    }

    /** construct and initialize Column info tuple 
     *
     * @param displayname_p OwString to display in the header
     * @param strPropertyName_p String property this column is referring to
     * @param iAlignment_p int alignment to use for the column, as defined with OwFieldColumnInfo.ALIGNMENT_...
     *
     */
    public OwStandardFieldColumnInfoString(OwString displayname_p, String strPropertyName_p, int iAlignment_p)
    {
        m_displayname = displayname_p;
        m_propertyname = strPropertyName_p;
        m_iAlignment = iAlignment_p;
    }

    /** construct and initialize Column info tuple 
    *
    * @param displayname_p OwString to display in the header
    * @param strPropertyName_p String property this column is referring to
    * @param iAlignment_p int alignment to use for the column, as defined with OwFieldColumnInfo.ALIGNMENT_...
    * @param iWidth_p int width [%]
    *
    */
    public OwStandardFieldColumnInfoString(OwString displayname_p, String strPropertyName_p, int iAlignment_p, int iWidth_p)
    {
        m_displayname = displayname_p;
        m_propertyname = strPropertyName_p;
        m_iAlignment = iAlignment_p;
        m_iWidth = iWidth_p;
    }

    /** get the string to display in the header
     *
     * @param locale_p Locale to use
     * @return String display name
     */
    public String getDisplayName(java.util.Locale locale_p)
    {
        if (m_displayname != null)
        {
            return m_displayname.getString(locale_p);
        }
        else
        {
            return OwString.localizeLabel(locale_p, m_propertyname);
        }
    }

    /** get the object property name this column is referring to
     *
     * @return String property name
     */
    public String getPropertyName()
    {
        return m_propertyname;
    }

    /** get the object property name this column is referring to
     *
     * @return int alignment as defined with OwFieldColumnInfo.ALIGNMENT_...
     */
    public int getAlignment()
    {
        return m_iAlignment;
    }

    /** get column width in % or 0 if undefined
     * @return int width [%]
     * */
    public int getWidth()
    {
        return m_iWidth;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getPropertyName();
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldColumnInfo#isSortable()
     */
    public boolean isSortable()
    {
        return true;
    }
}