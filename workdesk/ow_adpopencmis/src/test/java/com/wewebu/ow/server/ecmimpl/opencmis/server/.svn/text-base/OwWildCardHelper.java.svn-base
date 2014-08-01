package com.wewebu.ow.server.ecmimpl.opencmis.server;

import java.util.Locale;

import com.wewebu.ow.server.field.OwWildCardDefinition;

/**
 * Helper Class to create simple wildcard definitions.
 * Simply create a Instance with given values, and no
 * checks are done.
 * This class was created because OwStandardWildCardDefinition 
 * is using OwString for localization.
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