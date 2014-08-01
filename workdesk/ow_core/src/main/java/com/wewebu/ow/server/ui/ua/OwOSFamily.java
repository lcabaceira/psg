package com.wewebu.ow.server.ui.ua;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *<p>
 * HTTP user agent pattern based operating system family model.
 * Enumeration elements are capable of decoding themselves from user agent strings, operating system 
 * user agent  tokens or enumeratrion elemenrt string name.
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
 *@since 4.1.1.0
 */
public enum OwOSFamily
{
    WINDOWS("Windows", Pattern.compile("Windows")), OS_X("OS X", Pattern.compile("(Mac OS X|OS X)")), UNKNOWN("UNKNOWN", Pattern.compile("^$"));

    private static Map<String, OwOSFamily> familyByName = null;

    private final String familyName;
    private final Pattern pattern;

    /**
     * 
     * @param familyName
     * @return the operation system family enumeration element that has the given name 
     *         or UNKNOWN if there is no family defined with the give name  
     */
    public static OwOSFamily fromFamilyName(String familyName)
    {
        synchronized (OwOSFamily.class)
        {
            if (familyByName == null)
            {
                familyByName = Collections.synchronizedMap(new HashMap<String, OwOSFamily>());

                OwOSFamily[] allFamilies = values();
                for (int i = 0; i < allFamilies.length; i++)
                {
                    familyByName.put(allFamilies[i].getFamilyName(), allFamilies[i]);
                }
            }

        }

        OwOSFamily family = familyByName.get(familyName);
        if (family == null)
        {
            family = UNKNOWN;
        }

        return family;
    }

    /**
     * User agent string OS family parse-search 
     * 
     * @param userAgent
     * @return the operating system family of the first operating system token  found in the given user agent string 
     *         or UNKNOWN if no known operating system token is found
     *                               
     */
    public static OwOSFamily findFirst(String userAgent)
    {
        if (userAgent == null)
        {
            return UNKNOWN;
        }

        OwOSFamily family = UNKNOWN;
        int start = Integer.MAX_VALUE;
        for (OwOSFamily familyValue : values())
        {
            if (familyValue != UNKNOWN)
            {
                Matcher m = familyValue.getPattern().matcher(userAgent);
                if (m.find())
                {
                    int matchStart = m.start();
                    if (matchStart < start)
                    {
                        family = familyValue;
                        start = matchStart;
                    }
                }
            }
        }

        return family;
    }

    /**
     * 
     * @param family
     * @return the first enumeration element that has a matching pattern for the given family string
     *         or UNKNOWN if no element has a matching pattern
     */
    public static OwOSFamily matchPattern(String family)
    {
        if (family == null)
        {
            return UNKNOWN;
        }

        for (OwOSFamily familyValue : values())
        {
            if (familyValue != UNKNOWN)
            {
                Matcher m = familyValue.getPattern().matcher(family);
                if (m.matches())
                {
                    return familyValue;
                }
            }
        }

        return UNKNOWN;
    }

    /**
     * 
     * @param family
     * @return the operating system family that matches the given string by name , pattern 
     *         ,by user agent parse-search or by enumeration element definition name 
     *         in this exact order 
     */
    public static OwOSFamily from(String family)
    {
        if (family == null)
        {
            return UNKNOWN;
        }

        OwOSFamily osFamily = UNKNOWN;

        osFamily = fromFamilyName(family);

        if (osFamily == UNKNOWN)
        {
            osFamily = matchPattern(family);
        }

        if (osFamily == UNKNOWN)
        {
            osFamily = findFirst(family);
        }

        if (osFamily == UNKNOWN)
        {
            OwOSFamily[] allFamilies = values();
            for (int i = 0; i < allFamilies.length; i++)
            {
                if (allFamilies[i].name().equals(family))
                {
                    osFamily = allFamilies[i];
                    break;
                }
            }
        }

        return osFamily;
    }

    private OwOSFamily(String familyName, Pattern pattern)
    {
        this.familyName = familyName;
        this.pattern = pattern;
    }

    public String getFamilyName()
    {
        return familyName;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

}
