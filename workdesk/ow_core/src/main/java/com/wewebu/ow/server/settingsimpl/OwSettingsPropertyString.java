package com.wewebu.ow.server.settingsimpl;

/**
 *<p>
 * A single settings String property.
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
public class OwSettingsPropertyString extends OwSettingsPropertyBaseImpl
{
    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return oSingleValue_p;
    }

    /** overridable to create a default value for list properties
     *
     * @return Object with default value for a new list item
     */
    protected Object getDefaultListItemValue()
    {
        // default returns empty string
        return "";
    }
}