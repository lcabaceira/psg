package com.wewebu.ow.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 *<p>
 * In memory storage of attribute bags.<br/>
 * Should only be used for testing as it stores all attribute bags in memory.
 *</p>
 *@since 3.1.0.0
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public class OwTransientBagRepository
{
    private static OwTransientBagRepository instance;

    /**
     * 
     * @return singleton instance of this attribute bags repository
     */
    public synchronized static OwTransientBagRepository getInstance()
    {
        if (instance == null)
        {
            instance = new OwTransientBagRepository();
        }

        return instance;
    }

    private Map bagsMap = new HashMap();

    private OwTransientBagRepository()
    {
        //void
    }

    /**
     * 
     * @param userID_p
     * @param bagName_p
     * @return an {@link OwSimpleAttributeBagWriteable} for the given name and user ID
     */
    public synchronized OwSimpleAttributeBagWriteable getBag(String userID_p, String bagName_p)
    {
        Map usersBags = (Map) this.bagsMap.get(userID_p);
        if (usersBags == null)
        {
            usersBags = new HashMap();
            this.bagsMap.put(userID_p, usersBags);
        }
        OwSimpleAttributeBagWriteable bag = (OwSimpleAttributeBagWriteable) usersBags.get(bagName_p);
        if (bag == null)
        {
            bag = new OwSimpleAttributeBagWriteable();
            usersBags.put(bagName_p, bag);
        }
        return bag;
    }
}
