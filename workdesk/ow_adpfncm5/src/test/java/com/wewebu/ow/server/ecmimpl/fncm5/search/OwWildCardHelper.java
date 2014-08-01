package com.wewebu.ow.server.ecmimpl.fncm5.search;

import java.util.Locale;

import com.wewebu.ow.server.field.OwWildCardDefinition;

/**
 *<p>
 * Helper Class to create simple wildcard definitions.
 * Simply create a Instance with given values, and no
 * checks are done.
 * This class was created because OwStandardWildCardDefinition 
 * is using OwString for localization.
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
public class OwWildCardHelper implements OwWildCardDefinition
{
    String wildCard;
    String nativeWildCard;
    int type;

    public OwWildCardHelper(String wildCard_p, String nativeWildCard_p, int wildCardType_p)
    {
        this.wildCard = wildCard_p;
        this.nativeWildCard = nativeWildCard_p;
        this.type = wildCardType_p;
    }

    public String getDescription(Locale locale_p)
    {
        return "";
    }

    public String getNativeWildCard()
    {
        return this.nativeWildCard;
    }

    public int getType()
    {
        return this.type;
    }

    public String getWildCard()
    {
        return this.wildCard;
    }

}