package com.wewebu.ow.server.ecm;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 *Standard property comparator implementation.
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
 *@since 4.0.0.0 
 */
public final class OwPropertyComparator implements Comparator<OwProperty>
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwPropertyComparator.class);

    private static OwPropertyComparator instance = null;

    public static int legacyCompare(Object oProperty1_p, Object oProperty2_p)
    {
        if (oProperty2_p == null)
        {
            return oProperty1_p == null ? 0 : -1;
        }

        if (oProperty1_p == null)
        {
            return 1;
        }

        if (oProperty1_p instanceof OwProperty && oProperty2_p instanceof OwProperty)
        {
            return OwPropertyComparator.getInstance().compare((OwProperty) oProperty1_p, (OwProperty) oProperty2_p);
        }
        else
        {
            LOG.debug("OwCMISProperty.compareTo(): non property comparison of " + oProperty1_p.getClass() + " to " + oProperty2_p.getClass());
            return 1;
        }
    }

    public synchronized static OwPropertyComparator getInstance()
    {
        if (instance == null)
        {
            instance = new OwPropertyComparator();
        }

        return instance;
    }

    private OwPropertyComparator()
    {

    }

    public int compare(OwProperty property1_p, OwProperty property2_p)
    {
        if (property2_p == null)
        {
            return property1_p == null ? 0 : -1;
        }

        if (property1_p == null)
        {
            return 1;
        }

        Object value2 = null;
        Object value1 = null;
        try
        {

            value2 = property2_p.getValue();
            value1 = property1_p.getValue();

            if (value2 == null)
            {
                return value1 == null ? 0 : -1;
            }
            else if (value1 == null)
            {
                return 1;
            }
            else
            {

                if (value1 instanceof Comparable)
                {
                    return ((Comparable) value1).compareTo(value2);
                }
                else if (value1 instanceof String && value2 instanceof String)
                {
                    return ((String) value1).compareToIgnoreCase((String) value2);
                }
                else
                {
                    return value1.toString().compareToIgnoreCase(value2.toString());
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("OwPropertyComparator.compareTo(): error comparing properties ", e);
            if (value1 == null)
            {
                return value2 == null ? 0 : 1;
            }
            else
            {
                return value2 == null ? -1 : 1;
            }
        }

    }

}
