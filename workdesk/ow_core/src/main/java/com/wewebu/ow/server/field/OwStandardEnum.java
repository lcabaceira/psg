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
public class OwStandardEnum implements OwEnum
{
    /** value of the enum */
    private Object m_oEnumValue;
    /** display name which should be displayed for this enum value */
    private String m_strEnumDisplayName;
    /** key to be used for localization, without the owlabel. prefix
     * @since 2.5.3.1*/
    private String localizeKey;

    /** construct a single enum value, used in getEnums
     * Use this constructor if you got already a localized displayname.
     * However, getDisplayName locale must match the given locale here.
     * @param value_p Object enum value
     * @param strSymbolName_p String symbol name to be translated
     *
     */
    public OwStandardEnum(Object value_p, String strSymbolName_p)
    {
        this(value_p, strSymbolName_p, null);
    }

    /**
     * Constructor to create an enumeration entry with a predefined localization key.
     * @param value_p Object the native value
     * @param label_p String representing the default label
     * @param localizeKey_p String localization key without owlable. prefix
     * @since 2.5.3.1
     */
    public OwStandardEnum(Object value_p, String label_p, String localizeKey_p)
    {
        m_oEnumValue = value_p;
        m_strEnumDisplayName = label_p;
        localizeKey = localizeKey_p;
    }

    public Object getValue()
    {
        return m_oEnumValue;
    }

    public String getDisplayName(java.util.Locale local_p)
    {
        if (localizeKey == null)
        {
            return OwString.localizeLabel(local_p, m_strEnumDisplayName);
        }
        else
        {
            return OwString.localizeLabel(local_p, localizeKey, m_strEnumDisplayName);
        }
    }

    /** check if enum has a contained child collection */
    public boolean hasChildEnums()
    {
        return false;
    }

    /** get the contained child collection
     * 
     * @return OwEnumCollection or null if item has no children
     */
    public OwEnumCollection getChilds()
    {
        return null;
    }

    /**
     * Returns the localize key of this enumeration entry.
     * Remember it will be without the &quot;owlabel.&quot; prefix.
     * @return String the localize key
     * @since 3.0.0.1
     */
    public String getLocalizeKey()
    {
        return localizeKey;
    }
}