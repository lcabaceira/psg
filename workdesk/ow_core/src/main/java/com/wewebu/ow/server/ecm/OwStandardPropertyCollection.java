package com.wewebu.ow.server.ecm;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 *<p>
 * Standard Implementation for the OwPropertyCollection Map Class for property maps from an object. 
 * Keeps a map of OwProperty Objects with predictable order.<br/><br/>
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
public class OwStandardPropertyCollection extends LinkedHashMap implements OwPropertyCollection
{

    private static final long serialVersionUID = -5801272986506849971L;

    public String toString()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("OwStandardPropertyCollection [");
        if (this.values() != null)
        {
            Iterator it = this.values().iterator();
            if (it != null)
            {
                while (it.hasNext())
                {
                    OwProperty owProperty = (OwProperty) it.next();
                    stringBuffer.append("\n>> ").append(owProperty.toString());
                }
            }
        }
        stringBuffer.append("\n]");
        return stringBuffer.toString();
    }
}