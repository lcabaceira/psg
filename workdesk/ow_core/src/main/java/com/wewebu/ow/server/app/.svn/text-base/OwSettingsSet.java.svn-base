package com.wewebu.ow.server.app;

import java.util.Iterator;
import java.util.Map;

/**
 *<p>
 * Settings Set, containing the settings properties for a plugin. Each plugin may have a settings set.
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
public class OwSettingsSet
{
    /** constructs a settings info
     * @param strName_p String Name of the set i.e. ID of the plugin
     * @param properties_p map of OwSettingsProperty with the current values
     * @param strDisplayName_p String the display name of the plugin
     */
    public OwSettingsSet(String strName_p, Map properties_p, String strDisplayName_p)
    {
        m_Properties = properties_p;
        m_strDisplayName = strDisplayName_p;
        m_strName = strName_p;

        // iterate over the properties to set the editable flags
        Iterator it = m_Properties.values().iterator();
        while (it.hasNext())
        {
            OwSettingsProperty prop = (OwSettingsProperty) it.next();

            if (prop.isEditable())
            {
                // === found a editable property
                if (prop.isUser())
                {
                    m_fUserEditableProperties = true;
                }
                else
                {
                    m_fAppEditableProperties = true;
                }
            }
        }
    }

    /** Name of the set i.e. ID of the plugin */
    protected String m_strName;
    /** map of properties with current values */
    protected Map m_Properties;
    /** the display name of the set */
    protected String m_strDisplayName;
    /** flag indicating if set contains at least one property that is editable by the user */
    protected boolean m_fUserEditableProperties;
    /** flag indicating if set contains at least one property that is editable by the application */
    protected boolean m_fAppEditableProperties;

    /** check if set contains at least one property that is editable by the user
     * @return true if at least one property is editable by the user
     */
    public boolean hasUserEditableProperties()
    {
        return m_fUserEditableProperties;
    }

    /** check if set contains at least one property that is editable by the user
     * @return true if at least one property is editable by the user
     */
    public boolean hasAppEditableProperties()
    {
        return m_fAppEditableProperties;
    }

    /** get the name of the settingsset, i.e. the ID of the corresponding plugin
     * @return Name of the set i.e. ID of the plugin
     */
    public String getName()
    {
        return m_strName;
    }

    /** get the map of properties with current values
    * @return map of properties with current values
    */
    public Map getProperties()
    {
        return m_Properties;
    }

    /** get the display name of the set
    * @return display name of the set
    */
    public String getDisplayName()
    {
        return m_strDisplayName;
    }

    public String toString()
    {
        StringBuffer ret = new StringBuffer();

        ret.append("OwSettingsSet: ");
        ret.append(", getDisplayName() = " + getDisplayName());
        ret.append(", getName() = " + getName());
        ret.append(", hasAppEditableProperties() = " + hasAppEditableProperties());
        ret.append(", hasUserEditableProperties() = " + hasUserEditableProperties());
        ret.append("\r\n");
        ret.append(", getProperties()");
        ret.append("\r\n");
        ret.append(getProperties());

        return ret.toString();
    }
}