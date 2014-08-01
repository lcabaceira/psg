package com.wewebu.ow.server.app;

/**
 *<p>
 * Default implementation for combo box item.
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
 *@since 3.0.0.0
 */
public class OwDefaultComboItem implements OwComboItem
{
    /** the value */
    private String m_value;
    /** the display name */
    private String m_displayName;

    /**
     * Constructor
     * @param value_p - the value
     * @param displayName_p - the display name
     */
    public OwDefaultComboItem(String value_p, String displayName_p)
    {
        this.m_value = value_p;
        this.m_displayName = displayName_p;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboItem#getDisplayName()
     */
    public String getDisplayName()
    {
        return m_displayName;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwComboItem#getValue()
     */
    public String getValue()
    {
        return m_value;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return m_value.hashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object_p)
    {
        boolean result = false;
        if (object_p instanceof OwComboItem)
        {
            OwDefaultComboItem converted = (OwDefaultComboItem) object_p;
            if (converted.getValue() != null)
            {
                result = converted.getValue().equals(m_value);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "" + m_value + "|" + m_displayName;
    }
}