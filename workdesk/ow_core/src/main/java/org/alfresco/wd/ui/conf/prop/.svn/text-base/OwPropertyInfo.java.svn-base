package org.alfresco.wd.ui.conf.prop;

/**
 * 
 *<p>
 * Helper tuple definition of UI property Id and state.
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
 * @since 4.2.0.0
 */
public class OwPropertyInfo
{
    private String propertyName;
    private boolean readOnly;

    public OwPropertyInfo(String propertyName_p, boolean readOnly_p)
    {
        propertyName = propertyName_p;
        readOnly = readOnly_p;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }

    @Override
    public int hashCode()
    {
        return this.propertyName.hashCode();
    }

    @Override
    public String toString()
    {
        StringBuilder retStr = new StringBuilder(getClass().getSimpleName());
        retStr.append("[");
        retStr.append(getPropertyName());
        retStr.append(", ");
        retStr.append(isReadOnly());
        retStr.append("]");

        return retStr.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (super.equals(obj))
        {
            return true;
        }
        if (obj instanceof OwPropertyInfo)
        {
            return getPropertyName().equals(((OwPropertyInfo) obj).getPropertyName());
        }
        return false;
    }
}