package com.wewebu.ow.server.field;

import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Standard implementation of Interface for Wild character definitions used in searches.<br/><br/>
 * To be implemented with the specific ECM system.
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
public class OwStandardWildCardDefinition implements OwWildCardDefinition
{
    private String m_sWildcard;
    private int m_iType;
    private OwString1 m_sDescription;
    private String m_sNativeWildcard;

    /** create a wildcard definition
     * 
     * @param sNativeWildcard_p the native wildcard character
     * @param wildcard_p  the wildcard character that is used by the client and needs to be replaced by the native wildcard
     * @param type_p
     * @param description_p
     */
    public OwStandardWildCardDefinition(String sNativeWildcard_p, String wildcard_p, int type_p, OwString1 description_p)
    {
        m_sNativeWildcard = sNativeWildcard_p;

        if (null == wildcard_p)
        {
            // === client didn't define a wild card so use the native one
            m_sWildcard = sNativeWildcard_p;
        }
        else
        {
            // === client defined a wild card for replacement
            m_sWildcard = wildcard_p;
        }

        m_iType = type_p;
        m_sDescription = description_p;
    }

    /** get the wildcard character that is used by the client and will be replaced by the native wildcard
     * 
     * @return String the wildcard character placeholder i.g. "*"
     */
    public String getWildCard()
    {
        return m_sWildcard;
    }

    /** get the native wildcard character that is used internally by the system
     * 
     * @return String the wildcard character placeholder i.g. "*"
     */
    public String getNativeWildCard()
    {
        return m_sNativeWildcard;
    }

    /** get the type of wildcard defined by this character
     * 
     * @return int as defined with WILD_CARD_TYPE_...
     */
    public int getType()
    {
        return m_iType;
    }

    /** get a description of this wild character to be displayed in the client */
    public String getDescription(java.util.Locale locale_p)
    {
        return m_sDescription.getString1(locale_p, getWildCard());
    }

}