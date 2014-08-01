package com.wewebu.ow.server.field;

/**
 *<p>
 * Standard implementation of the OwFieldColumnInfo Interface.
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
public class OwStandardFieldColumnInfo implements OwFieldColumnInfo
{

    /** field info for the column */
    protected OwFieldDefinition m_fielddefinition;

    /** the alignment to use for the column, as defined with OwFieldColumnInfo.ALIGNMENT_... */
    protected int m_iAlignment;

    /** column width in % or 0 if undefined */
    protected int m_iWidth = 0;

    /** construct and initialize Column info tuple 
    *
    * @param field_p OwFieldDefinition
    * @param iAlignment_p int alignment to use for the column, as defined with OwFieldColumnInfo.ALIGNMENT_...
    * @param iWidth_p int width [%]
    *
    */
    public OwStandardFieldColumnInfo(OwFieldDefinition field_p, int iAlignment_p, int iWidth_p)
    {
        m_fielddefinition = field_p;
        m_iAlignment = iAlignment_p;
        m_iWidth = iWidth_p;
    }

    /** construct and initialize Column info tuple 
     *
    * @param field_p OwFieldDefinition
    * @param iAlignment_p int alignment to use for the column, as defined with OwFieldColumnInfo.ALIGNMENT_...
     */
    public OwStandardFieldColumnInfo(OwFieldDefinition field_p, int iAlignment_p)
    {
        m_fielddefinition = field_p;
        m_iAlignment = iAlignment_p;
    }

    /** construct and initialize Column info tuple 
     * 
     * @param field_p OwFieldDefinition
     */
    public OwStandardFieldColumnInfo(OwFieldDefinition field_p)
    {
        m_fielddefinition = field_p;
        m_iAlignment = OwFieldColumnInfo.ALIGNMENT_DEFAULT;
    }

    /** get the string to display in the header
     *
     * @param locale_p Locale to use
     * @return String display name
     */
    public String getDisplayName(java.util.Locale locale_p)
    {
        return m_fielddefinition.getDisplayName(locale_p);
    }

    /** get the object property name this column is referring to
     *
     * @return String property name
     */
    public String getPropertyName()
    {
        return m_fielddefinition.getClassName();
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

    public String toString()
    {
        return getPropertyName();
    }

    /**
     * @see com.wewebu.ow.server.field.OwFieldColumnInfo#isSortable()
     */
    public boolean isSortable()
    {
        boolean isSortable = true;
        if (m_fielddefinition != null)
        {
            try
            {
                isSortable = !m_fielddefinition.isArray();
            }
            catch (Exception e)
            {
                isSortable = false;
            }
        }
        return isSortable;
    }

}