package com.wewebu.ow.server.field;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Standard implementation of the OwEnum interface.
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
public class OwStandardLocalizeableEnum implements OwEnum
{
    /** construct a single enum value, used in getEnums
     *  Use this constructor if the display name should be retrieve from resource bundle with the given key
     *
     * @param value_p Object enum value
     * @param displayName_p OwString localizeable display name
     */
    public OwStandardLocalizeableEnum(Object value_p, OwString displayName_p)
    {
        m_oEnumValue = value_p;
        m_DisplaName = displayName_p;
    }

    /** value of the enum */
    private Object m_oEnumValue;

    /** localizeable displayname */
    private OwString m_DisplaName;

    public Object getValue()
    {
        return m_oEnumValue;
    }

    public String getDisplayName(java.util.Locale local_p)
    {
        return m_DisplaName.getString(local_p);
    }

    public boolean hasChildEnums()
    {

        return false;
    }

    public OwEnumCollection getChilds()
    {

        return null;
    }
}